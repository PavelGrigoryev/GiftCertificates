package ru.clevertec.ecl.giftcertificates.util.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
import ru.clevertec.ecl.giftcertificates.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrderPageRequest")
@With
public class OrderPageRequestTestBuilder implements TestBuilder<OrderPageRequest> {

    private Integer page = 0;

    private Integer size = 10;

    private String sortBy = "id";

    @Override
    public OrderPageRequest build() {
        OrderPageRequest request = new OrderPageRequest(sortBy);
        request.setPage(page);
        request.setSize(size);
        return request;
    }

}
