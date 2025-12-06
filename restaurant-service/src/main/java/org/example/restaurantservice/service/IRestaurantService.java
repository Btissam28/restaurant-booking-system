package org.example.restaurantservice.service;
import org.example.restaurantservice.dto.RechercheRestaurantRequest;
import org.example.restaurantservice.dto.RestaurantDTO;
import org.example.restaurantservice.entity.Restaurant;

import java.math.BigDecimal;
import java.util.List;

public interface IRestaurantService {
    Restaurant findById(Long id);
    List<Restaurant> findAll();
    List<Restaurant> searchByNom(String nom);
    List<Restaurant> findByTypeCuisine(String typeCuisine);
    List<Restaurant> findByNoteMin(Double noteMin);
    List<Restaurant> findByPrixMax(BigDecimal prixMax);
    List<Restaurant> findNearbyRestaurants(BigDecimal latitude, BigDecimal longitude, Double rayon);
    List<String> getAllTypeCuisine();
    List<Restaurant> getTopRatedRestaurants();
    List<RestaurantDTO> rechercherRestaurants(RechercheRestaurantRequest request);
    boolean estOuvertMaintenant(Restaurant restaurant);
}
