package org.example.restaurantservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvisDTO {
    private Long id;
    private String commentaire;
    private Integer note;
    private String auteurNom;
    private Long restaurantId;
    private String restaurantNom;
    private LocalDateTime createdAt;
}