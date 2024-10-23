package com.raffleease.payments_service.Kafka.Brokers.Producers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SuccessProducer {
    /*
    private final KafkaTemplate<String, PSuccessMessage> successTemplate;

    private static final Logger logger = LoggerFactory.getLogger(WebHookService.class);
    public void notifySuccess(PSuccessMessage request) {
        Message<PSuccessMessage> message = MessageBuilder
                .withPayload(request)
                .setHeader(KafkaHeaders.TOPIC, "p-success-topic")
                .build();

        try {
            logger.info("ENVIANDO MENSAJE");
            successTemplate.send(message).get();
            logger.info("MENSAJE ENVIADO");

            SendResult<String, PSuccessMessage> result = successTemplate.send(message).get();
            logger.info("RESULTADO: {}", result);
            long offset = result.getRecordMetadata().offset();
            logger.info("Mensaje enviado con éxito al offset: {}", offset);
        } catch (KafkaException exp) {
            throw new CustomKafkaException("Error sending order success notification: " + exp.getMessage());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

     */
}
