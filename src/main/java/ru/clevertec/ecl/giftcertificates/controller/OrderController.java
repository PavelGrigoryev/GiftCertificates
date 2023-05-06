package ru.clevertec.ecl.giftcertificates.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.giftcertificates.dto.PaginationRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderDto;
import ru.clevertec.ecl.giftcertificates.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> makeAnOrder(@RequestBody MakeAnOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.makeAnOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDto>> findAllByUserId(@PathVariable Long id,
                                                          PaginationRequest request) {
        return ResponseEntity.ok(orderService.findAllByUserId(id, request));
    }

}
