package ru.clevertec.ecl.giftcertificates.util.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.giftcertificates.dto.pagination.TagPageRequest;
import ru.clevertec.ecl.giftcertificates.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aTagPageRequest")
@With
public class TagPageRequestTestBuilder implements TestBuilder<TagPageRequest> {

    private Integer page = 0;

    private Integer size = 10;

    private String sortBy = "id";

    @Override
    public TagPageRequest build() {
        TagPageRequest request = new TagPageRequest(sortBy);
        request.setPage(page);
        request.setSize(size);
        return request;
    }

}
