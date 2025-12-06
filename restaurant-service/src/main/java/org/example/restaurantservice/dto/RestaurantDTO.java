package org.example.restaurantservice.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDTO {
    private Long id;
    private String nom;
    private String adresse;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String typeCuisine;
    private String description;
    private String heureOuverture;
    private String heureFermeture;
    private BigDecimal prixMoyen;
    private Double noteMoyenne;
    private Integer nombreAvis;
    private Integer capaciteTotale;
    private Double distance;
}
