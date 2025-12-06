package org.example.reservationservice.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.reservationservice.enums.ReservationStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private Long id;
    private Long restaurantId;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDateTime reservationDateTime;
    private Integer numberOfGuests;
    private ReservationStatus status;
    private String specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
