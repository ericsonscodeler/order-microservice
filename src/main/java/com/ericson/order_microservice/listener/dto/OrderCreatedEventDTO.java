package com.ericson.order_microservice.listener.dto;

import java.util.List;

public record OrderCreatedEventDTO(Long codigoPedido,
        Long codigoCliente,
        List<OrderItemEventDTO> itens) {
}
