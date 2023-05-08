package ru.clevertec.ecl.giftcertificates.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.giftcertificates.dto.DeleteResponse;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderResponse;
import ru.clevertec.ecl.giftcertificates.dto.order.UpdateYourOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
import ru.clevertec.ecl.giftcertificates.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> makeAnOrder(@RequestBody @Valid MakeAnOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.makeAnOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderResponse>> findAllYourOrders(@PathVariable Long id, @Valid OrderPageRequest request) {
        return ResponseEntity.ok(orderService.findAllByUserId(id, request));
    }

    @PutMapping
    public ResponseEntity<OrderResponse> addToYourOrder(@RequestBody @Valid UpdateYourOrderRequest request) {
        return ResponseEntity.ok(orderService.addToYourOrder(request));
    }

    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteYourOrder(@RequestParam Long userId, Long orderId) {
        orderService.delete(userId, orderId);
        return ResponseEntity.ok(new DeleteResponse("Order with ID " + orderId
                                                    + " for User with ID " + userId + " was successfully deleted"));
    }

}
