package com.ericson.order_microservice.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String ODER_CREATED_QUEUE = "product-order-created";
}
