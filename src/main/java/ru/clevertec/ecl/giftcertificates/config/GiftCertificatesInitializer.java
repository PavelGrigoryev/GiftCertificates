package ru.clevertec.ecl.giftcertificates.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * This class serves as the initializer for the web application. It implements the {@link WebApplicationInitializer}
 * interface and overrides the {@link #onStartup(ServletContext)} method to initialize the web application by
 * registering the {@link WebMvcConfig} configuration and creating a {@link DispatcherServlet} instance to handle
 * incoming requests.
 */
public class GiftCertificatesInitializer implements WebApplicationInitializer {

    /**
     * Initializes the web application by creating an {@link AnnotationConfigWebApplicationContext} and registering
     * the {@link WebMvcConfig} configuration. Then creates a {@link DispatcherServlet} instance and registers it with
     * the ServletContext to handle incoming requests.The servlet is named "dispatcher" and is mapped to the root
     * URL ("/").
     *
     * @param servletContext the ServletContext of the web application.
     */
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebMvcConfig.class);

        ServletRegistration.Dynamic dispatcher
                = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

}
