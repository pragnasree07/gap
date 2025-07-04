package com.example.create.repository;

import com.example.create.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // This method is provided by JpaRepository
    Optional<Order> findById(Long id);
}