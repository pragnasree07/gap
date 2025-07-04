package com.example.create.repository;

import com.example.create.model.ServiceStatusDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceStatusDetailRepository extends JpaRepository<ServiceStatusDetail, Long> {
}