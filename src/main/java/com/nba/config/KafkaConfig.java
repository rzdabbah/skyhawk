package com.nba.config;

import com.nba.event.PlayerStatsEvent;
import com.nba.event.GameStatsEvent;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic playerStatsTopic() {
        return new NewTopic("nba.player-stats", 3, (short) 1);
    }

    @Bean
    public NewTopic gameStatsTopic() {
        return new NewTopic("nba.game-stats", 3, (short) 1);
    }

    @Bean
    public NewTopic gamesTopic() {
        return new NewTopic("nba.games", 3, (short) 1);
    }

    @Bean
    public NewTopic teamsTopic() {
        return new NewTopic("nba.teams", 3, (short) 1);
    }

    @Bean
    public ProducerFactory<String, PlayerStatsEvent> playerStatsProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ProducerFactory<String, GameStatsEvent> gameStatsProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, PlayerStatsEvent> playerStatsKafkaTemplate() {
        return new KafkaTemplate<>(playerStatsProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, GameStatsEvent> gameStatsKafkaTemplate() {
        return new KafkaTemplate<>(gameStatsProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, PlayerStatsEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "nba-stats-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.nba.event");
        return new DefaultKafkaConsumerFactory<>(props, 
                new StringDeserializer(), 
                new JsonDeserializer<>(PlayerStatsEvent.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PlayerStatsEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PlayerStatsEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
} 