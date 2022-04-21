package com.eden.orderservice.vo;

import lombok.Data;

@Data
public class RequsetOrder {
  private String productId;
  private Integer qty;
  private Integer unitPrice;
}
