package org.example.restaurantservice.repository;
import org.example.restaurantservice.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>,
        JpaSpecificationExecutor<Restaurant> {

    List<Restaurant> findByNomContainingIgnoreCase(String nom);
    List<Restaurant> findByTypeCuisineContainingIgnoreCase(String typeCuisine);
    List<Restaurant> findByNoteMoyenneGreaterThanEqual(Double noteMin);
    List<Restaurant> findByPrixMoyenLessThanEqual(BigDecimal prixMax);

    @Query(value = "SELECT r.*, " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(r.latitude)) * " +
            "cos(radians(r.longitude) - radians(:lng)) + sin(radians(:lat)) * " +
            "sin(radians(r.latitude)))) AS distance " +
            "FROM restaurants r " +
            "HAVING distance < :rayon " +
            "ORDER BY distance",
            nativeQuery = true)
    List<Restaurant> findNearbyRestaurants(@Param("lat") BigDecimal latitude,
                                           @Param("lng") BigDecimal longitude,
                                           @Param("rayon") Double rayon);

    @Query("SELECT DISTINCT r.typeCuisine FROM Restaurant r ORDER BY r.typeCuisine")
    List<String> findDistinctTypeCuisine();

    @Query("SELECT r FROM Restaurant r WHERE r.noteMoyenne IS NOT NULL ORDER BY r.noteMoyenne DESC")
    List<Restaurant> findTopRatedRestaurants();
}