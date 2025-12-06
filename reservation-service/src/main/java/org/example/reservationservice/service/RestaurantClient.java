package org.example.reservationservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "restaurant-service", url = "${restaurant.service.url}")
public interface RestaurantClient {

    @GetMapping("/api/restaurants/{id}/availability")
    Map<String, Object> checkAvailability(
            @PathVariable("id") Long restaurantId,
            @RequestParam("dateTime") String dateTime,
            @RequestParam("guests") Integer guests);

    @GetMapping("/api/restaurants/{id}/exists")
    Boolean restaurantExists(@PathVariable("id") Long restaurantId);
}