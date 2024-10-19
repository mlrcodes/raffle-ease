package com.raffleease.notifications_service.Email;

import com.raffleease.notifications_service.Kafka.Messages.Success.CustomerDTO;
import com.raffleease.notifications_service.Kafka.Messages.Success.OrderData;
import com.raffleease.notifications_service.Kafka.Messages.Success.PaymentData;
import com.raffleease.notifications_service.Kafka.Messages.Success.SuccessNotification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static com.raffleease.notifications_service.Email.EmailTemplates.ORDER_SUCCESS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendOrderSuccessNotification(SuccessNotification notificationRequest) throws MessagingException {
        Map<String, Object> variables = createOrderSuccessNotificationVariables(notificationRequest);
        sendEmail(notificationRequest.customerData().email(),
                ORDER_SUCCESS.getSubject(),
                ORDER_SUCCESS.getTemplate(),
                variables
        );
    }

    private Map<String, Object> createOrderSuccessNotificationVariables(SuccessNotification paymentConfirmation) {
        Map<String, Object> variables = new HashMap<>();

        PaymentData paymentData = paymentConfirmation.paymentData();
        CustomerDTO customer = paymentConfirmation.customerData();
        OrderData orderData = paymentConfirmation.orderData();

        variables.put("customer", customer);
        variables.put("paymentData", paymentData);
        variables.put("orderData", orderData);

        variables.put("customerName", customer.name());
        variables.put("customerEmail", customer.email());
        variables.put("customerPhoneNumber", customer.phoneNumber());

        variables.put("paymentMethod", paymentData.paymentMethod());
        variables.put("paymentTotal", paymentData.total());

        variables.put("orderReference", orderData.orderReference());
        variables.put("orderDate", orderData.orderDate());
        variables.put("ticketList", orderData.tickets());

        variables.put("ticketCount", orderData.tickets().size());

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