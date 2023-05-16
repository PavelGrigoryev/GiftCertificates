package ru.clevertec.ecl.giftcertificates.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditConfiguration {

    @Value("${user.name}")
    private String userName;

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of(userName);
    }

}
