package ru.clevertec.ecl.giftcertificates.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "name"})
public record TagDto(Long id, String name) {
}
