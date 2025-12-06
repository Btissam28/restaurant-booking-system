package org.example.reservationservice.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.reservationservice.dto.ReservationRequestDTO;
import org.example.reservationservice.dto.ReservationResponseDTO;
import org.example.reservationservice.entity.Reservation;
import org.example.reservationservice.entity.User;
import org.example.reservationservice.enums.ReservationStatus;
import org.example.reservationservice.repository.ReservationRepository;
import org.example.reservationservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RestaurantClient restaurantClient;

    @Transactional
    public ReservationResponseDTO createReservation(ReservationRequestDTO requestDTO) {
        log.info("Création d'une réservation pour le restaurant {}", requestDTO.getRestaurantId());

        // Validation
        validateReservationRequest(requestDTO);

        // Vérifier l'utilisateur
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // Vérifier le restaurant
        if (!restaurantExists(requestDTO.getRestaurantId())) {
            throw new IllegalArgumentException("Restaurant non trouvé");
        }

        // Vérifier la disponibilité
        if (!checkAvailability(requestDTO)) {
            throw new IllegalStateException("Restaurant non disponible");
        }

        // Créer la réservation
        Reservation reservation = Reservation.builder()
                .restaurantId(requestDTO.getRestaurantId())
                .user(user)
                .customerName(requestDTO.getCustomerName())
                .customerEmail(requestDTO.getCustomerEmail())
                .customerPhone(requestDTO.getCustomerPhone())
                .reservationDateTime(requestDTO.getReservationDateTime())
                .numberOfGuests(requestDTO.getNumberOfGuests())
                .status(ReservationStatus.CONFIRMED)
                .specialRequests(requestDTO.getSpecialRequests())
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);
        return convertToDTO(savedReservation);
    }

    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDTO> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDTO> getUpcomingUserReservations(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return reservationRepository.findUpcomingByUserId(userId, now).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReservationResponseDTO> getReservationById(Long id) {
        return reservationRepository.findById(id).map(this::convertToDTO);
    }

    public List<ReservationResponseDTO> getRestaurantReservations(Long restaurantId) {
        return reservationRepository.findByRestaurantId(restaurantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponseDTO updateReservation(Long id, ReservationRequestDTO requestDTO) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        validateReservationRequest(requestDTO);

        reservation.setCustomerName(requestDTO.getCustomerName());
        reservation.setCustomerEmail(requestDTO.getCustomerEmail());
        reservation.setCustomerPhone(requestDTO.getCustomerPhone());
        reservation.setReservationDateTime(requestDTO.getReservationDateTime());
        reservation.setNumberOfGuests(requestDTO.getNumberOfGuests());
        reservation.setSpecialRequests(requestDTO.getSpecialRequests());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return convertToDTO(updatedReservation);
    }

    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        if (!reservation.canBeCancelled()) {
            throw new IllegalStateException("Impossible d'annuler cette réservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Réservation non trouvée");
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    public ReservationResponseDTO updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation non trouvée"));

        reservation.setStatus(status);
        Reservation updatedReservation = reservationRepository.save(reservation);
        return convertToDTO(updatedReservation);
    }

    private void validateReservationRequest(ReservationRequestDTO requestDTO) {
        if (requestDTO.getReservationDateTime().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new IllegalArgumentException("La réservation doit être au moins 1 heure à l'avance");
        }

        if (requestDTO.getNumberOfGuests() < 1 || requestDTO.getNumberOfGuests() > 20) {
            throw new IllegalArgumentException("Nombre d'invités invalide (1-20)");
        }
    }

    private boolean restaurantExists(Long restaurantId) {
        try {
            Boolean exists = restaurantClient.restaurantExists(restaurantId);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du restaurant: {}", e.getMessage());
            return false;
        }
    }

    private boolean checkAvailability(ReservationRequestDTO requestDTO) {
        try {
            Map<String, Object> response = restaurantClient.checkAvailability(
                    requestDTO.getRestaurantId(),
                    requestDTO.getReservationDateTime().toString(),
                    requestDTO.getNumberOfGuests()
            );

            Object available = response.get("available");
            if (available instanceof Boolean) {
                return (Boolean) available;
            } else if (available instanceof String) {
                return Boolean.parseBoolean((String) available);
            }
            return false;
        } catch (Exception e) {
            log.error("Erreur de disponibilité: {}", e.getMessage());
            throw new IllegalStateException("Erreur lors de la vérification de disponibilité");
        }
    }

    private ReservationResponseDTO convertToDTO(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getId(),
                reservation.getRestaurantId(),
                reservation.getUser().getId(),
                reservation.getCustomerName(),
                reservation.getCustomerEmail(),
                reservation.getCustomerPhone(),
                reservation.getReservationDateTime(),
                reservation.getNumberOfGuests(),
                reservation.getStatus(),
                reservation.getSpecialRequests(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}
