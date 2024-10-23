package com.raffleease.orders_service.Kafka.Configs.ConsumersConfigs;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TestConsumerConfig {

    @Value("${kafka_server}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, MessageDTO> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "orders-group");
        configProps.put(JsonDeserializer.TYPE_MAPPINGS, "test-topic:com.raffleease.orders_service.Kafka.Messages.MessageDTO");
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new JsonDeserializer<>());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
