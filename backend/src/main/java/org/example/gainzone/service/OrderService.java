package org.example.gainzone.service;

import org.example.gainzone.dto.request.OrderRequest;
import org.example.gainzone.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(OrderRequest request);
}
