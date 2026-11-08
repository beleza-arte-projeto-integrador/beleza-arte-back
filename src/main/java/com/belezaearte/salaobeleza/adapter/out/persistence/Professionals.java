package com.belezaearte.salaobeleza.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "professionals")
public class Professionals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    // Só os 11 números, sem pontos e traço (o service tira a máscara).
    @Column(name = "cpf")
    private String cpf;

    // Anos de experiência.
    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String password;

    // true = ativo, false = inativo.
    @Column(name = "active")
    private Boolean active;

    // Preenchido automaticamente com a data/hora do cadastro.
    // updatable = false: um update nunca muda a data de cadastro.
    @CreationTimestamp
    @Column (name= "created_at", updatable = false)
    private LocalDateTime created;

    // Preenchido automaticamente sempre que o cadastro é alterado (controle/auditoria).
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updated;
}
