package com.nba.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
public class RabbitMQConfig {
    public static final String QUEUE_PLAYER_STATS = "player.stats.queue";
    public static final String EXCHANGE_PLAYER_STATS = "player.stats.exchange";
    public static final String ROUTING_KEY_PLAYER_STATS = "player.stats";

    @Bean
    public Queue playerStatsQueue() {
        return QueueBuilder.durable(QUEUE_PLAYER_STATS)
                .withArgument("x-dead-letter-exchange", EXCHANGE_PLAYER_STATS + ".dlq")
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY_PLAYER_STATS + ".dlq")
                .build();
    }

    @Bean
    public DirectExchange playerStatsExchange() {
        return new DirectExchange(EXCHANGE_PLAYER_STATS);
    }

    @Bean
    public Binding playerStatsBinding(Queue playerStatsQueue, DirectExchange playerStatsExchange) {
        return BindingBuilder.bind(playerStatsQueue)
                .to(playerStatsExchange)
                .with(ROUTING_KEY_PLAYER_STATS);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, 
                                       MessageConverter jsonMessageConverter,
                                       @Nullable MeterRegistry meterRegistry) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        if (meterRegistry != null) {
            template.setObservationEnabled(true);
        }
        return template;
    }
} 