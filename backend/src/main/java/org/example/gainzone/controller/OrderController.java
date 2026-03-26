package org.example.gainzone.controller;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.OrderRequest;
import org.example.gainzone.dto.response.OrderResponse;
import org.example.gainzone.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.placeOrder(request));
    }
}
