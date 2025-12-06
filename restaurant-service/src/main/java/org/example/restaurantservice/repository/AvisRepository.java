package org.example.restaurantservice.repository;
import org.example.restaurantservice.entity.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisRepository extends JpaRepository<Avis, Long> {

    List<Avis> findByRestaurantId(Long restaurantId);
    List<Avis> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);

    @Query("SELECT AVG(a.note) FROM Avis a WHERE a.restaurant.id = :restaurantId")
    Double findAverageNoteByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT COUNT(a) FROM Avis a WHERE a.restaurant.id = :restaurantId")
    Long countByRestaurantId(@Param("restaurantId") Long restaurantId);

    List<Avis> findTop5ByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
}