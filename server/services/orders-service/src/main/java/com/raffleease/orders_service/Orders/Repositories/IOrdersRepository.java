package com.raffleease.orders_service.Orders.Repositories;

import com.raffleease.orders_service.Orders.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrdersRepository extends JpaRepository<Order, Long> {
}