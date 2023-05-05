package ru.clevertec.ecl.giftcertificates.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.giftcertificates.dto.OrderDto;
import ru.clevertec.ecl.giftcertificates.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<OrderDto> makeAnOrder(@RequestParam Long userId, Long giftId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.makeAnOrder(userId, giftId));
    }

}
