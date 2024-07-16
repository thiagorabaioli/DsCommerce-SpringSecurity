package com.devsuperior.dscommerce.dto;

import com.devsuperior.dscommerce.entities.Order;
import com.devsuperior.dscommerce.entities.OrderItem;
import com.devsuperior.dscommerce.entities.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.security.core.parameters.P;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Instant moment;
    private OrderStatus status;
    private ClientDTO client;
    private PaymentDTO payment;


    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO(Order entity) {
        id = entity.getId();
        moment = entity.getMoment();
        status = entity.getStatus();
        client = new ClientDTO(entity.getClient()); // entity.getClient() returns a User object
        payment = (entity.getPayment() == null) ? null : new PaymentDTO(entity.getPayment()); // entity.getPayment() returns a Payment object
        //entity.getItems().forEach(item -> items.add(new OrderItemDTO(item)));
        for (OrderItem item : entity.getItems()) {
            items.add(new OrderItemDTO(item));
        }
    }

    public OrderDTO(Long id, Instant moment, OrderStatus status, ClientDTO client, PaymentDTO payment) {
        this.id = id;
        this.moment = moment;
        this.status = status;
        this.client = client;
        this.payment = payment;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public ClientDTO getClient() {
        return client;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getMoment() {
        return moment;
    }
    public Double getTotal() {
        double sum = 0.0;
        for (OrderItemDTO item : items) {
            sum += item.getSubTotal();
        }
        return sum;
    }
}
