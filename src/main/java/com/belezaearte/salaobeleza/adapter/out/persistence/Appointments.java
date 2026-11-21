package com.belezaearte.salaobeleza.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

// O agendamento é a VISITA do cliente ao salão (tabela appointments).
// Os serviços da visita, com profissional, horário e preço, ficam em appointment_items.
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

    // Quem agendou (FK -> clients.id).
    @Column(name = "client_id", nullable = false)
    private Integer idClient;

    // Preenchidos só quando o agendamento é cancelado.
    @Column(name = "cancelled_by")
    private String cancelledBy;

    @CreationTimestamp
    @Column(name = "cancelled_at")
    private OffsetDateTime cancelled;

    // Preenchido automaticamente com a data/hora em que o agendamento foi feito.
    // updatable = false: um update nunca muda essa data.
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime created;

    // Preenchido automaticamente sempre que o agendamento é alterado.
    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updated;

}
