package org.example.restaurantservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.restaurantservice.dto.RechercheRestaurantRequest;
import org.example.restaurantservice.dto.RestaurantDTO;
import org.example.restaurantservice.entity.Restaurant;
import org.example.restaurantservice.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // CORRECTION 1: Utiliser DTO au lieu d'entité
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.findAll();
        List<RestaurantDTO> restaurantDTOs = restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantDTOs);
    }

    // CORRECTION 2: Utiliser DTO au lieu d'entité
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.findById(id);
        return ResponseEntity.ok(convertToDTO(restaurant));
    }

    // CORRECTION 3: Utiliser DTO au lieu d'entité
    @GetMapping("/search/nom/{nom}")
    public ResponseEntity<List<RestaurantDTO>> searchByNom(@PathVariable String nom) {
        List<Restaurant> restaurants = restaurantService.searchByNom(nom);
        List<RestaurantDTO> restaurantDTOs = restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantDTOs);
    }

    // CORRECTION 4: Utiliser DTO au lieu d'entité
    @GetMapping("/cuisine/{typeCuisine}")
    public ResponseEntity<List<RestaurantDTO>> getByCuisine(@PathVariable String typeCuisine) {
        List<Restaurant> restaurants = restaurantService.findByTypeCuisine(typeCuisine);
        List<RestaurantDTO> restaurantDTOs = restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantDTOs);
    }

    @PostMapping("/recherche")
    public ResponseEntity<List<RestaurantDTO>> rechercherRestaurants(
            @RequestBody RechercheRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.rechercherRestaurants(request));
    }

    // CORRECTION 5: Utiliser DTO au lieu d'entité
    @GetMapping("/top-rated")
    public ResponseEntity<List<RestaurantDTO>> getTopRated() {
        List<Restaurant> restaurants = restaurantService.getTopRatedRestaurants();
        List<RestaurantDTO> restaurantDTOs = restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(restaurantDTOs);
    }

    @GetMapping("/cuisines")
    public ResponseEntity<List<String>> getAllCuisines() {
        return ResponseEntity.ok(restaurantService.getAllTypeCuisine());
    }

    @GetMapping("/populaires")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsPopulaires(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(restaurantService.getRestaurantsPopulaires(limit));
    }

    @GetMapping("/proches")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsProches(
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude,
            @RequestParam(defaultValue = "5.0") Double rayon) {
        return ResponseEntity.ok(restaurantService.getRestaurantsProches(latitude, longitude, rayon));
    }

    @GetMapping("/recommandations")
    public ResponseEntity<List<RestaurantDTO>> getRecommandations(
            @RequestParam(required = false) String typeCuisine,
            @RequestParam(required = false) Double noteMin) {
        return ResponseEntity.ok(restaurantService.getRecommandations(typeCuisine, noteMin));
    }

    @GetMapping("/filtres/avances")
    public ResponseEntity<Map<String, Object>> getFiltresAvances() {
        List<String> cuisines = restaurantService.getAllTypeCuisine();

        // Calculer les plages de prix
        List<Restaurant> restaurants = restaurantService.findAll();
        BigDecimal prixMin = restaurants.stream()
                .map(Restaurant::getPrixMoyen)
                .filter(p -> p != null)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        BigDecimal prixMax = restaurants.stream()
                .map(Restaurant::getPrixMoyen)
                .filter(p -> p != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.valueOf(1000));

        // Calculer les plages de capacité
        Integer capaciteMin = restaurants.stream()
                .map(Restaurant::getCapaciteTotale)
                .filter(c -> c != null)
                .min(Integer::compareTo)
                .orElse(0);
        Integer capaciteMax = restaurants.stream()
                .map(Restaurant::getCapaciteTotale)
                .filter(c -> c != null)
                .max(Integer::compareTo)
                .orElse(100);

        return ResponseEntity.ok(Map.of(
                "cuisines", cuisines,
                "prixMin", prixMin,
                "prixMax", prixMax,
                "capaciteMin", capaciteMin,
                "capaciteMax", capaciteMax,
                "noteMax", 5.0,
                "noteMin", 0.0
        ));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<Restaurant> restaurants = restaurantService.findAll();

        long totalRestaurants = restaurants.size();
        double noteMoyenneGenerale = restaurants.stream()
                .mapToDouble(Restaurant::getNoteMoyenne)
                .average()
                .orElse(0.0);

        // Compter par type de cuisine
        Map<String, Long> statsCuisines = restaurants.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Restaurant::getTypeCuisine,
                        java.util.stream.Collectors.counting()
                ));

        // Restaurants ouverts maintenant
        long ouvertsMaintenant = restaurants.stream()
                .filter(r -> {
                    try {
                        return restaurantService.estOuvertMaintenant(r);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();

        return ResponseEntity.ok(Map.of(
                "totalRestaurants", totalRestaurants,
                "noteMoyenneGenerale", Math.round(noteMoyenneGenerale * 100.0) / 100.0,
                "statsCuisines", statsCuisines,
                "ouvertsMaintenant", ouvertsMaintenant,
                "totalAvis", restaurants.stream()
                        .mapToInt(Restaurant::getNombreAvis)
                        .sum()
        ));
    }

    // Méthode de conversion d'entité vers DTO
    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .nom(restaurant.getNom())
                .adresse(restaurant.getAdresse())
                .latitude(restaurant.getLatitude())
                .longitude(restaurant.getLongitude())
                .typeCuisine(restaurant.getTypeCuisine())
                .description(restaurant.getDescription())
                .heureOuverture(restaurant.getHeureOuverture())
                .heureFermeture(restaurant.getHeureFermeture())
                .prixMoyen(restaurant.getPrixMoyen())
                .noteMoyenne(restaurant.getNoteMoyenne())
                .nombreAvis(restaurant.getNombreAvis())
                .capaciteTotale(restaurant.getCapaciteTotale())
                .build();
    }
}