package ru.clevertec.ecl.giftcertificates.util.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aTag")
@With
public class TagTestBuilder implements TestBuilder<Tag> {

    private Long id = 1L;
    private String name = "Bambucha";

    @Override
    public Tag build() {
        return Tag.builder()
                .id(id)
                .name(name)
                .build();
    }

}
