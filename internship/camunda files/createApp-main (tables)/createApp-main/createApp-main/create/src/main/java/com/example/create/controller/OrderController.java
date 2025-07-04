package com.example.create.controller;

import com.example.create.model.EntityProxity;
import com.example.create.model.EntityStatus;
import com.example.create.model.Order;
import com.example.create.repository.EntityProxityRepository;
import com.example.create.repository.OrderRepository;
import com.example.create.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityProxityRepository entityProxityRepository;

    @PostMapping("/create")
    public Order createOrder(@RequestBody Order order) {
        orderRepository.save(order);
        return orderService.processOrder(order);
    }

    @PostMapping("/process/{id}")
    public Order processOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        return orderService.processOrder(order);
    }
    @PostMapping("/retry-failed")
    public String retryFailedOrders() {
        List<EntityProxity> failedOrders = entityProxityRepository.findByEntyStat(EntityStatus.FAILED);

        for (EntityProxity entityProxity : failedOrders) {
            Order order = orderService.getOrderById(entityProxity.getEntyId());
            orderService.processOrder(order);
        }

        return "Retry triggered for failed orders.";
    }
    @GetMapping("/heartbeat")
    public ResponseEntity<String> heartbeat() {
        return ResponseEntity.ok("Application is running - " + LocalDateTime.now());
    }

    @GetMapping("/pending")
    public List<Order> getPendingOrders() {
        return orderRepository.findAll(); // Adjust this based on your new logic
    }
}
