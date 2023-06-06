package com.example.debate.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configurable
public class JpaConfig {
    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> Optional.ofNullable(1L);
    }
}
