package org.example.reservationservice.repository;
import org.example.reservationservice.entity.Reservation;
import org.example.reservationservice.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRestaurantId(Long restaurantId);
    List<Reservation> findByStatus(ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.reservationDateTime > :now ORDER BY r.reservationDateTime ASC")
    List<Reservation> findUpcomingByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("SELECT r FROM Reservation r WHERE r.restaurantId = :restaurantId AND r.reservationDateTime BETWEEN :start AND :end")
    List<Reservation> findReservationsByRestaurantAndDateTime(
            @Param("restaurantId") Long restaurantId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    Optional<Reservation> findByIdAndUserId(Long id, Long userId);
}