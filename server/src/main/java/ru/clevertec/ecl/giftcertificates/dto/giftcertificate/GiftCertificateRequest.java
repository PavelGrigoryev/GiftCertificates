package ru.clevertec.ecl.giftcertificates.dto.giftcertificate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;

import java.math.BigDecimal;
import java.util.List;

public record GiftCertificateRequest(@NotBlank(message = "Name cannot be blank")
                                     String name,

                                     @NotBlank(message = "Description cannot be blank")
                                     String description,

                                     @NotNull(message = "Price cannot be null")
                                     @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
                                     BigDecimal price,

                                     @NotNull(message = "Duration cannot be null")
                                     @Positive(message = "Duration must be greater than 0")
                                     Integer duration,

                                     @Valid
                                     List<TagDto> tags) {

}
