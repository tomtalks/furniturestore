package com.sopra.opel.furniturestore.controller;

import com.sopra.opel.furniturestore.services.WoodShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class NeedWoodController {
  String storeName = UUID.randomUUID()
          .toString()
          .substring(0, 12);


  @Autowired
  WoodShopService wss ;

  @GetMapping("/api/need")
  @Scheduled(initialDelay = 2,fixedDelay = 20,timeUnit = TimeUnit.SECONDS)
  public void need() {
    wss.askFor(storeName);
  }



}
