package ru.clevertec.ecl.giftcertificates;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Gift Certificates Application",
                contact = @Contact(
                        name = "Author: Grigoryev Pavel",
                        url = "https://pavelgrigoryev.github.io/GrigoryevPavel/"
                )
        ),
        servers = @Server(url = "http://localhost:8080")
)
@SpringBootApplication
public class GiftCertificatesApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiftCertificatesApplication.class, args);
    }

}
