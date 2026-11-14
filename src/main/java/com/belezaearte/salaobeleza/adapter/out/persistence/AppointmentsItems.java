package com.belezaearte.salaobeleza.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "appointment_items")
public class AppointmentsItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name= "appointment_id", nullable = false)
    private Integer appointment_id;

    @Column(name= "service_id", nullable = false)
    private Integer service_id;

    @Column(name= "professional_id", nullable = false)
    private Integer professional_id;

    @Column(name= "resource_id", nullable = false)
    private Integer resource_id;

    @Column(name= "start_at", nullable = false)
    private OffsetDateTime start_at;

    @Column(name= "end_at", nullable = false)
    private OffsetDateTime end_at;

    @Column(name= "price", nullable = false)
    private BigDecimal price;
}
