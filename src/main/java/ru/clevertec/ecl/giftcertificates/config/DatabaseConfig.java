package ru.clevertec.ecl.giftcertificates.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("ru.clevertec.ecl.giftcertificates")
@RequiredArgsConstructor
public class DatabaseConfig {

    private final Environment env;

    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getRequiredProperty("spring.datasource.driver"));
        hikariConfig.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        hikariConfig.setUsername(env.getRequiredProperty("spring.datasource.username"));
        hikariConfig.setPassword(env.getRequiredProperty("spring.datasource.password"));
        return new HikariDataSource(hikariConfig);
    }
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }

}
