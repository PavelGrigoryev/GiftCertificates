package ru.clevertec.ecl.giftcertificates.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.clevertec.ecl.giftcertificates.controller.openapi.OrderOpenApi;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController implements OrderOpenApi {

    private final OrderService orderService;

    @Override
    @PostMapping
    public ResponseEntity<OrderResponse> makeAnOrder(@RequestBody @Valid MakeAnOrderRequest request) {
        ResponseEntity<OrderResponse> response = ResponseEntity.status(HttpStatus.CREATED).body(orderService.makeAnOrder(request));
        log.info("makeAnOrder: request={}, \nresponse={}", request, response);
        return response;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<List<OrderResponse>> findAllUserOrders(@PathVariable Long id, @Valid OrderPageRequest request) {
        ResponseEntity<List<OrderResponse>> response = ResponseEntity.ok(orderService.findAllUserOrders(id, request));
        log.info("findAllUserOrders: request={}, \nresponse={}", request, response);
        return response;
    }

    @Override
    @PutMapping
    public ResponseEntity<OrderResponse> updateUserOrder(@RequestBody @Valid UpdateYourOrderRequest request) {
        ResponseEntity<OrderResponse> response = ResponseEntity.ok(orderService.updateUserOrder(request));
        log.info("updateUserOrder: request={}, \nresponse={}", request, response);
        return response;
    }

    @Override
    @DeleteMapping
    public ResponseEntity<DeleteResponse> deleteUserOrder(@RequestParam Long userId, Long orderId) {
        orderService.deleteUserOrder(userId, orderId);
        ResponseEntity<DeleteResponse> response =
                ResponseEntity.ok(new DeleteResponse("Order with ID " + orderId
                                                     + " for User with ID " + userId + " was successfully deleted"));
        log.info("deleteUserOrder: userId={}, orderId={}, \nresponse={}", userId, orderId, response);
        return response;
    }

}
