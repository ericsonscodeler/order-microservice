package com.ericson.order_microservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ericson.order_microservice.controller.dto.OrderResponseDTO;
import com.ericson.order_microservice.entity.OrderEntity;
import com.ericson.order_microservice.entity.OrderItem;
import com.ericson.order_microservice.listener.dto.OrderCreatedEventDTO;
import com.ericson.order_microservice.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(OrderCreatedEventDTO event) {

        var entity = new OrderEntity();

        entity.setOrderId(event.codigoPedido());
        entity.setCustomerId(event.codigoCliente());
        entity.setItems(getOrderItems(event));
        entity.setTotal(getTotal(event));

        orderRepository.save(entity);
    }

    public Page<OrderResponseDTO> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);

        return orders.map(OrderResponseDTO::fromEntity);
    }

    private static BigDecimal getTotal(OrderCreatedEventDTO event) {
        return event.itens()
                .stream()
                .map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEventDTO event) {
        return event.itens()
                .stream()
                .map(i -> new OrderItem(
                        i.produto(),
                        i.quantidade(),
                        i.preco()))
                .toList();
    }
}
