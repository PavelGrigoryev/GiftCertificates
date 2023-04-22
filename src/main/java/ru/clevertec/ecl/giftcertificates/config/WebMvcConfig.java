package ru.clevertec.ecl.giftcertificates.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * This class serves as the configuration for the Spring Web MVC framework. It implements the {@link WebMvcConfigurer}
 * interface and overrides the {@link #configureMessageConverters(List)} method to add a custom message converter that
 * uses the Jackson library to convert Java objects to JSON format.
 * <p>
 * The {@link ComponentScan} annotation is used to specify the base package for component scanning, which is where
 * Spring will look for annotated components such as controllers, services, and repositories.
 * The {@link EnableWebMvc} annotation enables Spring's Web MVC framework and provides default configuration
 * for handling requests and responses.
 */
@Configuration
@ComponentScan("ru.clevertec.ecl.giftcertificates")
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Method creates an instance of the {@link ObjectMapper} class from the
     * Jackson library and registers a {@link JavaTimeModule} to handle Java 8 date/time classes. Then it creates a new
     * instance of the {@link MappingJackson2HttpMessageConverter} class and sets the ObjectMapper instance as its object
     * mapper. Finally, it adds the message converter to the list of converters.
     *
     * @param converters List of {@link HttpMessageConverter}
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        converters.add(converter);
    }

}
