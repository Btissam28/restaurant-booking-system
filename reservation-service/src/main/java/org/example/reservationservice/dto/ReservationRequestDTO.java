package org.example.reservationservice.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

    @NotNull(message = "L'identifiant du restaurant est obligatoire")
    private Long restaurantId;

    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long userId;

    @NotBlank(message = "Le nom du client est obligatoire")
    private String customerName;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String customerEmail;

    private String customerPhone;

    @NotNull(message = "La date et heure de réservation sont obligatoires")
    @Future(message = "La réservation doit être dans le futur") // Keep here for API validation
    private LocalDateTime reservationDateTime;

    @Min(value = 1, message = "Le nombre d'invités doit être d'au moins 1")
    @Max(value = 20, message = "Le nombre d'invités ne peut pas dépasser 20")
    private Integer numberOfGuests;

    private String specialRequests;
}