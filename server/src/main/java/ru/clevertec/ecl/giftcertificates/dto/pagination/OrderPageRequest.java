package ru.clevertec.ecl.giftcertificates.dto.pagination;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderPageRequest extends AbstractPageRequest {

    @NotBlank(message = "SortBy cannot be null")
    @Pattern(regexp = "^(id|price|purchaseTime|lastAdditionTime)$",
            message = "Acceptable values are only: id, price, purchaseTime, lastAdditionTime")
    private String sortBy;

}
