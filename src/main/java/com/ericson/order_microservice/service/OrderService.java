package com.ericson.order_microservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.ericson.order_microservice.controller.dto.OrderResponseDTO;
import com.ericson.order_microservice.entity.OrderEntity;
import com.ericson.order_microservice.entity.OrderItem;
import com.ericson.order_microservice.listener.dto.OrderCreatedEventDTO;
import com.ericson.order_microservice.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
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

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        MatchOperation matchOperation = Aggregation.match(Criteria.where("customerId").is(customerId));
        GroupOperation groupOperation = Aggregation.group().sum("total").as("total");

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, groupOperation);
        AggregationResults<Document> response = mongoTemplate.aggregate(aggregation, "tb_orders", Document.class);

        Document result = response.getUniqueMappedResult();
        if (result != null && result.get("total") != null) {
            return new BigDecimal(result.get("total").toString());
        } else {
            return BigDecimal.ZERO;
        }
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
                .map(i -> new OrderItem(i.produto(), i.quantidade(), i.preco()))
                .toList();
    }
}
