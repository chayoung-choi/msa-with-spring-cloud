package com.eden.orderservice.service;

import com.eden.orderservice.dto.OrderDto;
import com.eden.orderservice.jpa.OrderEntity;

public interface OrderService {
  OrderDto createOrder(OrderDto orderDetails);
  OrderDto getOrderByOrderId(String orderId);
  Iterable<OrderEntity> getOrdersByUserId(String userId);
}
