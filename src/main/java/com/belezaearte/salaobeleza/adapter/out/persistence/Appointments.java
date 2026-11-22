package com.belezaearte.salaobeleza.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments")
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Clients idClients;

    @Column(name = "cancelled_by")
    private String cancelledBy;

    @CreationTimestamp
    @Column(name = "cancelled_at")
    private OffsetDateTime cancelled;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime created;

    @CreationTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updated;

}
