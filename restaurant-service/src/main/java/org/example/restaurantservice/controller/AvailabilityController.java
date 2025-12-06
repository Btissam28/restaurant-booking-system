package org.example.restaurantservice.controller;
import lombok.RequiredArgsConstructor;
import org.example.restaurantservice.entity.Restaurant;
import org.example.restaurantservice.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AvailabilityController {

    private final RestaurantService restaurantService;

    @GetMapping("/{id}/availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @PathVariable Long id,
            @RequestParam String dateTime,
            @RequestParam Integer guests) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validation des paramètres
            if (guests == null || guests < 1) {
                response.put("available", false);
                response.put("message", "Nombre d'invités invalide");
                return ResponseEntity.ok(response);
            }

            // Parsing de la date
            LocalDateTime reservationTime;
            try {
                reservationTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                response.put("available", false);
                response.put("message", "Format de date invalide");
                return ResponseEntity.ok(response);
            }

            // Récupération du restaurant
            Restaurant restaurant = restaurantService.findById(id);

            // Vérification de la capacité
            if (restaurant.getCapaciteTotale() != null && guests > restaurant.getCapaciteTotale()) {
                response.put("available", false);
                response.put("message", "Capacité insuffisante");
                return ResponseEntity.ok(response);
            }

            // Vérification si c'est dans le futur
            if (reservationTime.isBefore(LocalDateTime.now())) {
                response.put("available", false);
                response.put("message", "La réservation doit être dans le futur");
                return ResponseEntity.ok(response);
            }

            // Simulation de disponibilité
            boolean isAvailable = simulateAvailability(restaurant, reservationTime, guests);

            if (isAvailable) {
                response.put("available", true);
                response.put("message", "Disponible");
                response.put("restaurantName", restaurant.getNom());
                response.put("capacity", restaurant.getCapaciteTotale());
                response.put("priceRange", restaurant.getPrixMoyen());
            } else {
                response.put("available", false);
                response.put("message", "Pas de disponibilité pour cette date");
            }

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            response.put("available", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("available", false);
            response.put("message", "Erreur interne");
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> restaurantExists(@PathVariable Long id) {
        try {
            restaurantService.findById(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    private boolean simulateAvailability(Restaurant restaurant, LocalDateTime dateTime, Integer guests) {
        int hour = dateTime.getHour();
        if (hour >= 22) {
            return false;
        }

        String day = dateTime.getDayOfWeek().toString();
        if (day.equals("SATURDAY") || day.equals("SUNDAY")) {
            return Math.random() > 0.3;
        }

        return true;
    }
}