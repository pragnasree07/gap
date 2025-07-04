package com.example.create.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "srv_stat_dtl")
public class ServiceStatusDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long srvStatKey;

    private Long entyPrxyKey;
    private Long entyId;
    private String srvName;

    @Enumerated(EnumType.STRING)
    private ServiceStatus srvStat;

    private LocalDateTime crtDttm;
    private LocalDateTime lstUpdtDttm;
}