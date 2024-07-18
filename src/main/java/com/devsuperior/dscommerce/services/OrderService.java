package com.devsuperior.dscommerce.services;

import com.devsuperior.dscommerce.dto.OrderDTO;
import com.devsuperior.dscommerce.dto.OrderItemDTO;
import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.entities.*;
import com.devsuperior.dscommerce.repositories.OrderItemRepository;
import com.devsuperior.dscommerce.repositories.OrderRepository;
import com.devsuperior.dscommerce.repositories.ProductRepository;
import com.devsuperior.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {

@Autowired
private OrderRepository repository;

@Autowired
private UserService service;

@Autowired
private ProductRepository productRepository;

@Autowired
private OrderItemRepository orderItemRepository;

@Autowired
private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado"));
        authService.validateSelfOrtAdmin(order.getClient().getId());

        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {

        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);
        User user = service.authenticated(); // get the authenticated user
        order.setClient(user);// user is the authenticated user

        for(OrderItemDTO itemDto : dto.getItems()) {
            Product product = productRepository.getReferenceById(itemDto.getProductId());// get the product from the database
            OrderItem item = new OrderItem(order, product,itemDto.getQuantity(), product.getPrice());// create an OrderItem object
            order.getItems().add(item);// add the OrderItem object to the Order object

        }
        repository.save(order);// save the Order object in the database
        orderItemRepository.saveAll(order.getItems());// save the OrderItem objects in the database
        return new OrderDTO(order);
    }
}
