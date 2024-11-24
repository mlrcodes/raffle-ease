package com.raffleease.notifications_service.Email;

import com.raffleease.common_models.DTO.Customers.CustomerDTO;
import com.raffleease.common_models.DTO.Kafka.OrderSuccess;
import com.raffleease.common_models.DTO.Orders.OrderDTO;
import com.raffleease.common_models.DTO.Payment.PaymentDTO;
import com.raffleease.common_models.DTO.Tickets.TicketDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.raffleease.notifications_service.Email.EmailTemplates.ORDER_SUCCESS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendOrderSuccessNotification(OrderSuccess notificationRequest) throws MessagingException {
        Map<String, Object> variables = createOrderSuccessNotificationVariables(notificationRequest);
        sendEmail(notificationRequest.customer().email(),
                ORDER_SUCCESS.getSubject(),
                ORDER_SUCCESS.getTemplate(),
                variables
        );
    }

    private Map<String, Object> createOrderSuccessNotificationVariables(OrderSuccess orderSuccess) {
        Map<String, Object> variables = new HashMap<>();
        PaymentDTO paymentData = orderSuccess.payment();
        CustomerDTO customer = orderSuccess.customer();
        OrderDTO orderData = orderSuccess.order();

        LocalDateTime orderDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(orderData.orderDate()),
                ZoneId.systemDefault()
        );
        String formattedOrderDate = orderDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));

        List<String> ticketNumbers = orderData.tickets().stream()
                .map(TicketDTO::ticketNumber)
                .collect(Collectors.toList());

        variables.put("customer", customer);
        variables.put("paymentData", paymentData);
        variables.put("orderData", orderData);
        variables.put("customerName", customer.name());
        variables.put("customerEmail", customer.email());
        variables.put("customerPhoneNumber", customer.phoneNumber());
        variables.put("paymentMethod", paymentData.paymentMethod());
        variables.put("paymentTotal", paymentData.total());
        variables.put("orderReference", orderData.orderReference());
        variables.put("orderDate", formattedOrderDate);
        variables.put("ticketList", ticketNumbers);
        variables.put("ticketCount", ticketNumbers.size());

        return variables;
    }


    private void sendEmail(String to,
                           String subject,
                           String templateName,
                           Map<String, Object> variables
    ) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED_RELATED, UTF_8.name());

        Context context = new Context();
        context.setVariables(variables);
        String htmlTemplate = templateEngine.process(templateName, context);

        messageHelper.setSubject(subject);
        messageHelper.setText(htmlTemplate, true);
        messageHelper.setTo(to);
        messageHelper.setFrom("${spring.mail.username}");

        mailSender.send(mimeMessage);
    }
}