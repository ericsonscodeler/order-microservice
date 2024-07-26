package com.ericson.order_microservice.listener.dto;

import java.math.BigDecimal;

public record OrderItemEventDTO(String produto,
        Integer quantidade,
        BigDecimal preco) {
}
