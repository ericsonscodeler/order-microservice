package com.ericson.order_microservice.controller.dto;

import java.math.BigDecimal;

import com.ericson.order_microservice.entity.OrderEntity;

public record OrderResponseDTO(Long orderId, Long customerId, BigDecimal total) {

    public static OrderResponseDTO fromEntity(OrderEntity entity) {
        return new OrderResponseDTO(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
    }
}
