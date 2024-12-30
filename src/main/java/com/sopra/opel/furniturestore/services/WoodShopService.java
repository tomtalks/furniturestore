package com.sopra.opel.furniturestore.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class WoodShopService {

  private final List<String> woodTypes = new ArrayList<>();
  @Value("${app.woodshop.url}")
  private String hostApiUrl;

  @PostConstruct
  private void initWoodTypes(){
    getWoodTypes();
  }

   private void getWoodTypes() {
     WebClient client = WebClient.builder()
             .baseUrl(hostApiUrl)
             .defaultCookie("cookieKey", "cookieValue")
             .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
             .defaultUriVariables(Collections.singletonMap("url", hostApiUrl))
             .build();
     client.get().uri("/api/stock/types").exchangeToMono(clientResponse -> clientResponse.bodyToMono(List.class).retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5)).jitter(0.75))).subscribe(this::addType);
   }

  public void addType(List<String> t){
    this.woodTypes.addAll(t);
  }

  @Retryable(retryFor = WebClientRequestException.class, maxAttempts = 20, backoff = @Backoff(delay = 10000))
  public void askFor(String storeName) {
    Random r = new Random();
    String woodType="oak";
    if(!woodTypes.isEmpty()) {
      woodType = woodTypes.get(r.nextInt(0,woodTypes.size()));
    }else{
      getWoodTypes();
    }

    log.info("Ask for {}", woodType);
    WebClient client = WebClient.builder()
            .baseUrl(hostApiUrl)
            .defaultCookie("cookieKey", "cookieValue")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultUriVariables(Collections.singletonMap("url", hostApiUrl))
            .build();
    client.post().uri("/order").bodyValue("{\"customer\":\""+storeName+"\",\"type\":\""+woodType+"\",\"quantity\":\""+r.nextInt(70)+"\"}").exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class)).subscribe(log::info);

  }


}
