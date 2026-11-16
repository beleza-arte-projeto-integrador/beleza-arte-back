package com.belezaearte.salaobeleza.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// LGPD (art. 46): a senha é um dado que precisa de proteção.
// O BCrypt transforma a senha num hash que não dá para desfazer: no banco fica só o hash,
// nunca a senha original. No login, o sistema compara a senha digitada com o hash
// usando passwordEncoder.matches(senhaDigitada, hashDoBanco).
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
