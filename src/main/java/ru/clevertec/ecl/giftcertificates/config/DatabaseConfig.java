package ru.clevertec.ecl.giftcertificates.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * This class serves as the configuration for database and Hibernate.
 * <p>
 * The {@link PropertySource} annotation is used to indicate the resource locations of the properties files to be loaded.
 * The {@link ComponentScan} annotation is used to specify the base package for component scanning, which is where
 * Spring will look for annotated components such as controllers, services, and repositories.
 * The {@link EnableTransactionManagement} annotation enables Spring's annotation-driven transaction management capability.
 */
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan("ru.clevertec.ecl.giftcertificates")
@RequiredArgsConstructor
@EnableTransactionManagement
public class DatabaseConfig {

    private final Environment env;

    /**
     * Creates a new instance of the {@link HikariDataSource} class, which provides a connection pool for the database.
     * The method retrieves the database connection properties from the application properties file using the
     * {@link Environment}. Then creates a new {@link HikariConfig} class and sets these properties
     * on it before passing it to the constructor of a new {@link HikariDataSource} object.
     *
     * @return the new {@link HikariDataSource} object is returned to be used as a data source for the Hibernate ORM.
     */
    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getRequiredProperty("spring.datasource.driver"));
        hikariConfig.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        hikariConfig.setUsername(env.getRequiredProperty("spring.datasource.username"));
        hikariConfig.setPassword(env.getRequiredProperty("spring.datasource.password"));
        return new HikariDataSource(hikariConfig);
    }

    /**
     * Creates a new instance of the {@link LocalSessionFactoryBean} class. The method sets the data source to be
     * used by the SessionFactory object by calling the {@link HikariDataSource} bean.
     * It also specifies the package where the Hibernate entities are located using the setPackagesToScan() method.
     * Then it sets the Hibernate properties using the {@link #hibernateProperties()} method.
     *
     * @return a LocalSessionFactoryBean object configured with the data source and Hibernate properties.
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory(HikariDataSource hikariDataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(hikariDataSource);
        sessionFactory.setPackagesToScan("ru.clevertec.ecl.giftcertificates");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    /**
     * Creates a new instance of the {@link HibernateTransactionManager} class. The method sets
     * the {@link org.hibernate.SessionFactory} object to be used by the transaction manager by calling the getObject()
     * method on the {@link LocalSessionFactoryBean} returned by the LocalSessionFactoryBean.
     *
     * @return a {@link PlatformTransactionManager} object configured with the SessionFactory object.
     */
    @Bean
    public PlatformTransactionManager hibernateTransactionManager(LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

    /**
     * Create a new instance of the {@link Properties} class. The method sets necessary properties for Hibernate.
     *
     * @return a {@link Properties} object containing the Hibernate properties.
     */
    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        hibernateProperties.setProperty("hibernate.format_sql", "true");
        return hibernateProperties;
    }

}
