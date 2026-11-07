package com.belezaearte.salaobeleza.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Clients {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    // Único: é o que identifica o cliente quando ele volta a agendar.
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    // Preferências e observações (ex: "alergia a amônia").
    @Column(name = "preferences")
    private String preferences;

    // Preenchido automaticamente com a data/hora do cadastro.
    // updatable = false: um update nunca muda a data de cadastro.
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created;

    // Preenchido automaticamente sempre que o cadastro é alterado.
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updated;
}
