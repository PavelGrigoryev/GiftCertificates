package ru.clevertec.ecl.giftcertificates.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapperImpl;
import ru.clevertec.ecl.giftcertificates.mapper.OrderMapperImpl;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapperImpl;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapperImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {UserMapperImpl.class, OrderMapperImpl.class, GiftCertificateMapperImpl.class, TagMapperImpl.class})
@ExtendWith(MockitoExtension.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public @interface ServiceTest {
}
