package com.example.create.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "entity_proxity")
public class EntityProxity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entyPrxyKey;

    private String entyType;
    private Long entyId;

    @Enumerated(EnumType.STRING)
    private EntityStatus entyStat; // This field is used in findByEntyStat

    private LocalDateTime crtDttm;
    private LocalDateTime lstUpdtDttm;
}