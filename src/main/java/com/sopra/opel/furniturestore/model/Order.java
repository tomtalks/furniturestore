package com.sopra.opel.furniturestore.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

  private String customer;

  private String type;

  private Integer quantity;

  private Integer honored;
}
