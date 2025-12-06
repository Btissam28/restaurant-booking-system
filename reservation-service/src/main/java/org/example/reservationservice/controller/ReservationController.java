package org.example.reservationservice.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.reservationservice.dto.ReservationRequestDTO;
import org.example.reservationservice.dto.ReservationResponseDTO;
import org.example.reservationservice.enums.ReservationStatus;
import org.example.reservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequestDTO requestDTO) {
        try {
            ReservationResponseDTO reservation = reservationService.createReservation(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("VALIDATION_ERROR", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("BUSINESS_ERROR", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getUserReservations(@PathVariable Long userId) {
        List<ReservationResponseDTO> reservations = reservationService.getUserReservations(userId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<List<ReservationResponseDTO>> getUpcomingUserReservations(@PathVariable Long userId) {
        List<ReservationResponseDTO> reservations = reservationService.getUpcomingUserReservations(userId);
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequestDTO requestDTO) {
        try {
            ReservationResponseDTO updatedReservation = reservationService.updateReservation(id, requestDTO);
            return ResponseEntity.ok(updatedReservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("VALIDATION_ERROR", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("BUSINESS_ERROR", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status) {
        try {
            ReservationResponseDTO updatedReservation = reservationService.updateReservationStatus(id, status);
            return ResponseEntity.ok(updatedReservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("BUSINESS_ERROR", e.getMessage()));
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(createErrorResponse("BUSINESS_ERROR", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteReservation(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReservationResponseDTO>> getRestaurantReservations(
            @PathVariable Long restaurantId) {
        List<ReservationResponseDTO> reservations = reservationService.getRestaurantReservations(restaurantId);
        return ResponseEntity.ok(reservations);
    }

    private Map<String, String> createErrorResponse(String code, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", code);
        error.put("message", message);
        return error;
    }
}
