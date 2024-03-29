package com.eden.orderservice.controller;

import com.eden.orderservice.dto.OrderDto;
import com.eden.orderservice.jpa.OrderEntity;
import com.eden.orderservice.messagequeue.KafkaProducer;
import com.eden.orderservice.messagequeue.OrderProducer;
import com.eden.orderservice.service.OrderService;
import com.eden.orderservice.vo.RequsetOrder;
import com.eden.orderservice.vo.ResponseOrder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/order-service")
public class OrderController {
  Environment env;
  OrderService orderService;

  KafkaProducer kafkaProducer;

  OrderProducer orderProducer;

  public OrderController(Environment env, OrderService orderService, KafkaProducer kafkaProducer,
                         OrderProducer orderProducer) {
    this.env = env;
    this.orderService = orderService;
    this.kafkaProducer = kafkaProducer;
    this.orderProducer = orderProducer;
  }

  @GetMapping("/health_check")
  public String status(HttpServletRequest request) {return String.format("It's Working in Order Service on Port %s", request.getServerPort());}

  @PostMapping("/{userId}/orders")
  public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId
          , @RequestBody RequsetOrder orderDetails) {
    log.info("Before add orders data");
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
    orderDto.setUserId(userId);
    /* JPA */
    OrderDto createdOrder = orderService.createOrder(orderDto);
    ResponseOrder responseOrder = mapper.map(createdOrder, ResponseOrder.class);

    /* Kafka */
//    orderDto.setOrderId(UUID.randomUUID().toString());
//    orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());

    /* send this order to the kafka */
//    kafkaProducer.send("example-catalog-topic", orderDto);
//    orderProducer.send("orders", orderDto);

//    ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

    log.info("After added orders data");
    return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
  }

  @GetMapping("/{userId}/orders")
  public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId){
    log.info("Before retrieve orders data");
    Iterable<OrderEntity> orderList = orderService.getOrdersByUserId(userId);

    List<ResponseOrder> result = new ArrayList<>();
    orderList.forEach(v -> {
      result.add(new ModelMapper().map(v, ResponseOrder.class));
    });
    log.info("After retrieved orders data");

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
