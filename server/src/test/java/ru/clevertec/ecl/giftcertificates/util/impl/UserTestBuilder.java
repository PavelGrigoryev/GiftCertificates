package ru.clevertec.ecl.giftcertificates.util.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.giftcertificates.model.Order;
import ru.clevertec.ecl.giftcertificates.model.User;
import ru.clevertec.ecl.giftcertificates.util.TestBuilder;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUser")
@With
public class UserTestBuilder implements TestBuilder<User> {

    private Long id = 1L;
    private String username = "Hector";
    private List<Order> orders = new ArrayList<>(List.of(
            OrderTestBuilder.aOrder().build(),
            OrderTestBuilder.aOrder().withId(2L).build()
    ));

    @Override
    public User build() {
        return User.builder()
                .id(id)
                .username(username)
                .orders(orders)
                .build();
    }

}
