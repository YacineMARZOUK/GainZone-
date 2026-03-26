package org.example.gainzone.repository;

import org.example.gainzone.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0.0) FROM Order o")
    double sumTotalPrice();
}
