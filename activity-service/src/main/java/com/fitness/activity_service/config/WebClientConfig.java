package com.fitness.activity_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced
    /* create bean of web-client*/
   public WebClient.Builder webClient(){
       return WebClient.builder();
   }

    @Bean
    public WebClient userServiceWebClient(WebClient.Builder webClientBuilder){
       return webClientBuilder
               .baseUrl("http://USER-SERVICE")
               .build();
    }
}
