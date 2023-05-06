package ru.clevertec.ecl.giftcertificates.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationRequest {

    private Integer page;
    private Integer size;
    private String sortBy;

}
