package com.belezaearte.salaobeleza.adapter.out.persistence.repository;

import com.belezaearte.salaobeleza.adapter.out.persistence.Clients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<Clients, Integer> {

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, Integer id);
}
