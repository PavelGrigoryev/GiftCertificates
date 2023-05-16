package ru.clevertec.ecl.giftcertificates.util.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.giftcertificates.dto.pagination.UserPageRequest;
import ru.clevertec.ecl.giftcertificates.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserPageRequest")
@With
public class UserPageRequestTestBuilder implements TestBuilder<UserPageRequest> {

    private Integer page = 0;

    private Integer size = 10;

    private String sortBy = "id";

    @Override
    public UserPageRequest build() {
        UserPageRequest request = new UserPageRequest(sortBy);
        request.setPage(page);
        request.setSize(size);
        return request;
    }

}
