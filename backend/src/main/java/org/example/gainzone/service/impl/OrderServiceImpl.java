package org.example.gainzone.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.gainzone.dto.request.OrderRequest;
import org.example.gainzone.dto.response.OrderResponse;
import org.example.gainzone.entity.Order;
import org.example.gainzone.entity.OrderItem;
import org.example.gainzone.entity.Product;
import org.example.gainzone.entity.User;
import org.example.gainzone.repository.OrderRepository;
import org.example.gainzone.repository.ProductRepository;
import org.example.gainzone.repository.UserRepository;
import org.example.gainzone.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

        private final OrderRepository orderRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;

        @Override
        @Transactional
        public OrderResponse placeOrder(OrderRequest request) {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

                Order order = Order.builder()
                                .user(user)
                                .orderDate(LocalDateTime.now())
                                .orderItems(new ArrayList<>())
                                .totalPrice(BigDecimal.ZERO)
                                .build();

                BigDecimal total = BigDecimal.ZERO;

                for (OrderRequest.OrderItemRequest itemRequest : request.items()) {
                        Product product = productRepository.findById(itemRequest.productId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Produit non trouvé : " + itemRequest.productId()));

                        if (product.getStockQuantity() < itemRequest.quantity()) {
                                throw new RuntimeException("Stock insuffisant pour le produit : " + product.getName());
                        }

                        // Mise à jour du stock
                        product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
                        productRepository.save(product);

                        OrderItem orderItem = OrderItem.builder()
                                        .order(order)
                                        .product(product)
                                        .quantity(itemRequest.quantity())
                                        .unitPrice(product.getPrice())
                                        .build();

                        order.getOrderItems().add(orderItem);

                        BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
                        total = total.add(itemTotal);
                }

                order.setTotalPrice(total);
                Order savedOrder = orderRepository.save(order);

                return new OrderResponse(
                                savedOrder.getId(),
                                savedOrder.getTotalPrice(),
                                savedOrder.getOrderDate());
        }
}
