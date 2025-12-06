package org.example.restaurantservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechercheRestaurantRequest {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Double rayon; // en kilomètres
    private String typeCuisine;
    private String nom;
    private Double noteMin;
    private BigDecimal prixMin;
    private BigDecimal prixMax;
    private String tri; // "distance", "note", "popularite", "prix_asc", "prix_desc"
    private Boolean ouvertMaintenant; // Filtre par horaire
    private Integer capaciteMin;
    private Integer capaciteMax;
}