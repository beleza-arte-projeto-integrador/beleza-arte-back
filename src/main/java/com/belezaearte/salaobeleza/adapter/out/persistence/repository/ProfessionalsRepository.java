package com.belezaearte.salaobeleza.adapter.out.persistence.repository;

import com.belezaearte.salaobeleza.adapter.out.persistence.Professionals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessionalsRepository  extends JpaRepository<Professionals, Integer> {

    // CPF, e-mail e usuário são únicos no banco.
    // Usados no cadastro (existsBy...) e na edição (existsBy...AndIdNot: ignora o próprio profissional).
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    boolean existsByCpfAndIdNot(String cpf, Integer id);
    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsByUsernameAndIdNot(String username, Integer id);
}
