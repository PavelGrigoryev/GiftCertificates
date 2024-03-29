package ru.clevertec.ecl.giftcertificates.dto.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractPageRequest {

    @NotNull(message = "Page cannot be null")
    @PositiveOrZero(message = "Page must be greater than or equal to 0")
    private Integer page;

    @NotNull(message = "Size cannot be null")
    @Min(value = 1, message = "Size must be greater than or equal 1")
    @Max(value = 20, message = "Size must be less than or equal 20")
    private Integer size;

}
