package com.belezaearte.salaobeleza.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "services")
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column (name = "name", nullable = true)
    private String name;

    @Column (name = "description", nullable = true)
    private String description;

    @Column (name = "price", nullable = true)
    private BigDecimal price;

    @Column (name = "duration_minutes", nullable = true)
    private Integer duration_minutes;

    @Column (name = "created_at", nullable = true)
    private OffsetDateTime created;

    @Column (name = "category", nullable = true)
    private String category;
}
