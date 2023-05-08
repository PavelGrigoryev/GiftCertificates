package ru.clevertec.ecl.giftcertificates.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@JsonPropertyOrder({"id", "name"})
public record TagDto(

        @Positive(message = "Tag ID must be greater than 0")
        Long id,

        @NotBlank(message = "Name cannot be blank")
        String name) {
}
