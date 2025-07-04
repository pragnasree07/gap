package com.example.create.service;

import com.example.create.model.*;
import com.example.create.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Random;
@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityProxityRepository entityProxityRepository;

    @Autowired
    private ServiceStatusDetailRepository serviceStatusDetailRepository;
    private final Random random = new Random();
    private final WebClient webClient = WebClient.builder().baseUrl("http://localhost:8081").build(); // Validate App

    public Order processOrder(Order order) {
        log.debug("Processing Order ID: {}", order.getId());

        EntityProxity entityProxity = entityProxityRepository.findByEntyId(order.getId())
                .orElseGet(() -> {
                    log.debug("Creating new EntityProxity for Order ID: {}", order.getId());
                    return createEntityProxity(order);
                });

        log.debug("Current EntityProxity Status: {}", entityProxity.getEntyStat());

        switch (entityProxity.getEntyStat()) {
            case CREATED:
                log.debug("Order ID: {} is in CREATED state. Executing Task 1...", order.getId());
                if (task1(order)) {
                    log.debug("Task 1 succeeded for Order ID: {}", order.getId());
                    updateEntityProxity(entityProxity, EntityStatus.IN_PROGRESS);
                    updateServiceStatusDetail(order.getId(), "TASK_1", ServiceStatus.DONE);
                    if (task2(order)) {
                        log.debug("Task 2 succeeded for Order ID: {}", order.getId());
                        updateEntityProxity(entityProxity, EntityStatus.CONFIRMED);
                        updateServiceStatusDetail(order.getId(), "TASK_2", ServiceStatus.DONE);
                        sendToValidateApp(order);
                    } else {
                        log.error("Task 2 failed for Order ID: {}", order.getId());
                        updateEntityProxity(entityProxity, EntityStatus.FAILED);
                        updateServiceStatusDetail(order.getId(), "TASK_2", ServiceStatus.FAILED);
                    }
                } else {
                    log.error("Task 1 failed for Order ID: {}", order.getId());
                    updateEntityProxity(entityProxity, EntityStatus.FAILED);
                    updateServiceStatusDetail(order.getId(), "TASK_1", ServiceStatus.FAILED);
                }

                break;

//            case IN_PROGRESS:
//                log.debug("Order ID: {} is in IN_PROGRESS state. Executing Task 2...", order.getId());
//                if (task2(order)) {
//                    log.debug("Task 2 succeeded for Order ID: {}", order.getId());
//                    updateEntityProxity(entityProxity, EntityStatus.CONFIRMED);
//                    updateServiceStatusDetail(order.getId(), "TASK_2", ServiceStatus.DONE);
//                    sendToValidateApp(order);
//                } else {
//                    log.error("Task 2 failed for Order ID: {}", order.getId());
//                    updateEntityProxity(entityProxity, EntityStatus.FAILED);
//                    updateServiceStatusDetail(order.getId(), "TASK_2", ServiceStatus.FAILED);
//                }
//                break;
            case FAILED:
                log.debug("Order ID: {} is in FAILED state. Retrying from the beginning...", order.getId());
                // Reset the state to CREATED and retry Task 1
                updateEntityProxity(entityProxity, EntityStatus.CREATED);
                processOrder(order); // Recursively retry
                break;

            default:
                log.debug("Order ID: {} is in an unknown state: {}", order.getId(), entityProxity.getEntyStat());
                break;
        }

        return order;
    }

    private boolean task1(Order order) {
        log.debug("Executing Task 1: CHECKING PAYMENT AUTHORIZATION for Order ID: {}", order.getId());
        if (random.nextBoolean()) { // 50% chance of failure
            log.error("Task 1 Failed for Order ID: {}", order.getId());
            return false;
        }
        order.setOrderNumber(System.currentTimeMillis());
        log.info("Task 1 Succeeded for Order ID: {}", order.getId());
        return true;
    }

    private boolean task2(Order order) {
        log.debug("Executing Task 2: INVENTORY RESERVATION for Order ID: {}", order.getId());
        if (random.nextBoolean()) { // 50% chance of failure
            log.error("Task 2 Failed for Order ID: {}", order.getId());
            return false;
        }
        log.info("Task 2 Succeeded for Order ID: {}", order.getId());
        return true;
    }
        private EntityProxity createEntityProxity(Order order) {
        EntityProxity entityProxity = new EntityProxity();
        entityProxity.setEntyType("ORDER");
        entityProxity.setEntyId(order.getId());
        entityProxity.setEntyStat(EntityStatus.CREATED);
        entityProxity.setCrtDttm(LocalDateTime.now());
        entityProxity.setLstUpdtDttm(LocalDateTime.now());
        return entityProxityRepository.save(entityProxity);
    }

private void updateEntityProxity(EntityProxity entityProxity, EntityStatus status) {
    entityProxity.setEntyStat(status);
    entityProxity.setLstUpdtDttm(LocalDateTime.now());
    entityProxityRepository.save(entityProxity);
}

    private void updateServiceStatusDetail(Long entyId, String srvName, ServiceStatus status) {
        ServiceStatusDetail serviceStatusDetail = new ServiceStatusDetail();
        serviceStatusDetail.setEntyId(entyId);
        serviceStatusDetail.setSrvName(srvName);
        serviceStatusDetail.setSrvStat(status);
        serviceStatusDetail.setCrtDttm(LocalDateTime.now());
        serviceStatusDetail.setLstUpdtDttm(LocalDateTime.now());
        serviceStatusDetailRepository.save(serviceStatusDetail);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
        private void sendToValidateApp(Order order) {
        System.out.println("Sending order to Validate App: " + order);
        webClient.post()
                .uri("/orders/create") // Validate App API
                .bodyValue(order)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> {
                    System.out.println("Order " + order.getId() + " sent to Validate App.");
                    updateServiceStatusDetail(order.getId(), "VALIDATE_APP", ServiceStatus.DONE);
                }, error -> {
                    System.err.println("Failed to send order: " + error.getMessage());
                    updateServiceStatusDetail(order.getId(), "VALIDATE_APP", ServiceStatus.FAILED);
                });
    }
}
//{
//
//        "customerName": "Pragna",
//        "customerEmail": "pragnasreepallam@gmail.com",
//        "orderNumber": 1,
//        "price": 99.99,
//        "quantity": 2
//        }

