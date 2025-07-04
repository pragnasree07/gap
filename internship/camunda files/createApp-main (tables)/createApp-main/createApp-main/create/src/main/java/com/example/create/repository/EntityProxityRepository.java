package com.example.create.repository;

import com.example.create.model.EntityProxity;
import com.example.create.model.EntityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EntityProxityRepository extends JpaRepository<EntityProxity, Long> {
    // Find all EntityProxity records by entyStat
    List<EntityProxity> findByEntyStat(EntityStatus entyStat);
    Optional<EntityProxity> findByEntyId(Long entyId);
}