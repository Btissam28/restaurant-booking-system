package org.example.reservationservice.config;

import lombok.RequiredArgsConstructor;
import org.example.reservationservice.entity.Reservation;
import org.example.reservationservice.entity.User;
import org.example.reservationservice.enums.ReservationStatus;
import org.example.reservationservice.repository.ReservationRepository;
import org.example.reservationservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialisation des utilisateurs
        if (userRepository.count() == 0) {
            List<User> users = Arrays.asList(
                    User.builder()
                            .userId("client001")
                            .firstName("Mehdi")
                            .lastName("Alaoui")
                            .email("mehdi.alaoui@email.com")
                            .phone("+212612345678")
                            .build(),

                    User.builder()
                            .userId("client002")
                            .firstName("Fatima")
                            .lastName("Zahra")
                            .email("fatima.zahra@email.com")
                            .phone("+212698765432")
                            .build(),

                    User.builder()
                            .userId("client003")
                            .firstName("Karim")
                            .lastName("Benjelloun")
                            .email("karim.ben@email.com")
                            .phone("+212600112233")
                            .build(),

                    User.builder()
                            .userId("client004")
                            .firstName("Amina")
                            .lastName("El Fassi")
                            .email("amina.elfassi@email.com")
                            .phone("+212655443322")
                            .build(),

                    User.builder()
                            .userId("client005")
                            .firstName("Youssef")
                            .lastName("Mansouri")
                            .email("youssef.mansouri@email.com")
                            .phone("+212677889900")
                            .build(),

                    User.builder()
                            .userId("client006")
                            .firstName("Leila")
                            .lastName("Chraibi")
                            .email("leila.chraibi@email.com")
                            .phone("+212644556677")
                            .build(),

                    User.builder()
                            .userId("client007")
                            .firstName("Hassan")
                            .lastName("Bennani")
                            .email("hassan.bennani@email.com")
                            .phone("+212633445566")
                            .build(),

                    User.builder()
                            .userId("client008")
                            .firstName("Nadia")
                            .lastName("Rhouzi")
                            .email("nadia.rhouzi@email.com")
                            .phone("+212688997766")
                            .build(),

                    User.builder()
                            .userId("client009")
                            .firstName("Omar")
                            .lastName("Khattabi")
                            .email("omar.khattabi@email.com")
                            .phone("+212622334455")
                            .build(),

                    User.builder()
                            .userId("client010")
                            .firstName("Samira")
                            .lastName("Belkhadir")
                            .email("samira.belkhadir@email.com")
                            .phone("+212699887766")
                            .build()
            );

            List<User> savedUsers = userRepository.saveAll(users);
            System.out.println("✅ " + savedUsers.size() + " utilisateurs chargés en base de données");

            // Initialisation des réservations
            if (reservationRepository.count() == 0) {
                LocalDateTime now = LocalDateTime.now();

                List<Reservation> reservations = Arrays.asList(
                        // Réservations à venir (futures)
                        Reservation.builder()
                                .restaurantId(1L) // Rick's Café
                                .user(savedUsers.get(0)) // client001
                                .customerName("Mehdi Alaoui")
                                .customerEmail("mehdi.alaoui@email.com")
                                .customerPhone("+212612345678")
                                .reservationDateTime(now.plusDays(2).withHour(19).withMinute(0))
                                .numberOfGuests(4)
                                .status(ReservationStatus.CONFIRMED)
                                .specialRequests("Table près de la fenêtre, anniversaire")
                                .build(),

                        Reservation.builder()
                                .restaurantId(2L) // Al Mounia
                                .user(savedUsers.get(1)) // client002
                                .customerName("Fatima Zahra")
                                .customerEmail("fatima.zahra@email.com")
                                .customerPhone("+212698765432")
                                .reservationDateTime(now.plusDays(3).withHour(20).withMinute(30))
                                .numberOfGuests(2)
                                .status(ReservationStatus.CONFIRMED)
                                .specialRequests("Régime végétarien")
                                .build(),

                        Reservation.builder()
                                .restaurantId(3L) // La Sqala
                                .user(savedUsers.get(2)) // client003
                                .customerName("Karim Benjelloun")
                                .customerEmail("karim.ben@email.com")
                                .customerPhone("+212600112233")
                                .reservationDateTime(now.plusDays(1).withHour(21).withMinute(0))
                                .numberOfGuests(6)
                                .status(ReservationStatus.CONFIRMED)
                                .specialRequests("Réunion d'affaires")
                                .build(),

                        Reservation.builder()
                                .restaurantId(4L) // Le Cabestan
                                .user(savedUsers.get(3)) // client004
                                .customerName("Amina El Fassi")
                                .customerEmail("amina.elfassi@email.com")
                                .customerPhone("+212655443322")
                                .reservationDateTime(now.plusDays(4).withHour(20).withMinute(0))
                                .numberOfGuests(3)
                                .status(ReservationStatus.CONFIRMED)
                                .specialRequests("Vue sur la mer")
                                .build(),

                        Reservation.builder()
                                .restaurantId(5L) // Le Port de Pêche
                                .user(savedUsers.get(4)) // client005
                                .customerName("Youssef Mansouri")
                                .customerEmail("youssef.mansouri@email.com")
                                .customerPhone("+212677889900")
                                .reservationDateTime(now.plusDays(5).withHour(19).withMinute(30))
                                .numberOfGuests(8)
                                .status(ReservationStatus.CONFIRMED)
                                .specialRequests("Grande table pour famille")
                                .build(),

                        // Réservations en attente (futures)
                        Reservation.builder()
                                .restaurantId(6L) // Taverne du Dauphin
                                .user(savedUsers.get(5)) // client006
                                .customerName("Leila Chraibi")
                                .customerEmail("leila.chraibi@email.com")
                                .customerPhone("+212644556677")
                                .reservationDateTime(now.plusDays(6).withHour(20).withMinute(0))
                                .numberOfGuests(2)
                                .status(ReservationStatus.PENDING)
                                .specialRequests("Allergie aux fruits de mer")
                                .build(),

                        Reservation.builder()
                                .restaurantId(7L) // Dar Beida
                                .user(savedUsers.get(6)) // client007
                                .customerName("Hassan Bennani")
                                .customerEmail("hassan.bennani@email.com")
                                .customerPhone("+212633445566")
                                .reservationDateTime(now.plusDays(7).withHour(21).withMinute(0))
                                .numberOfGuests(5)
                                .status(ReservationStatus.PENDING)
                                .specialRequests("Célébration anniversaire")
                                .build(),

                        // Réservations passées (pour tests historiques) - TOUTES DANS LE FUTUR maintenant
                        Reservation.builder()
                                .restaurantId(8L) // Le Riad
                                .user(savedUsers.get(7)) // client008
                                .customerName("Nadia Rhouzi")
                                .customerEmail("nadia.rhouzi@email.com")
                                .customerPhone("+212688997766")
                                .reservationDateTime(now.plusDays(8).withHour(20).withMinute(0))
                                .numberOfGuests(4)
                                .status(ReservationStatus.CONFIRMED) // CHANGÉ de CANCELLED à CONFIRMED
                                .specialRequests("Vue sur le patio")
                                .build(),

                        Reservation.builder()
                                .restaurantId(9L) // La Maison du Tajine
                                .user(savedUsers.get(8)) // client009
                                .customerName("Omar Khattabi")
                                .customerEmail("omar.khattabi@email.com")
                                .customerPhone("+212622334455")
                                .reservationDateTime(now.plusDays(9).withHour(19).withMinute(0))
                                .numberOfGuests(3)
                                .status(ReservationStatus.CONFIRMED) // CHANGÉ de CANCELLED à CONFIRMED
                                .specialRequests("Tajine d'agneau")
                                .build(),

                        // Réservations passées complétées
                        Reservation.builder()
                                .restaurantId(10L) // NKOA
                                .user(savedUsers.get(9)) // client010
                                .customerName("Samira Belkhadir")
                                .customerEmail("samira.belkhadir@email.com")
                                .customerPhone("+212699887766")
                                .reservationDateTime(now.minusDays(3).withHour(20).withMinute(30)) // PASSÉE
                                .numberOfGuests(2)
                                .status(ReservationStatus.COMPLETED)
                                .specialRequests("Dîner romantique")
                                .build(),

                        // Autres réservations
                        Reservation.builder()
                                .restaurantId(11L) // L'Annexe
                                .user(savedUsers.get(0)) // client001
                                .customerName("Rachid El Amrani")
                                .customerEmail("rachid.elamrani@email.com")
                                .customerPhone("+212611223344")
                                .reservationDateTime(now.minusDays(5).withHour(21).withMinute(0)) // PASSÉE
                                .numberOfGuests(10)
                                .status(ReservationStatus.COMPLETED)
                                .specialRequests("Célébration de mariage")
                                .build(),

                        Reservation.builder()
                                .restaurantId(12L) // Le Studio
                                .user(savedUsers.get(1)) // client002
                                .customerName("Sofia Bouzid")
                                .customerEmail("sofia.bouzid@email.com")
                                .customerPhone("+212655667788")
                                .reservationDateTime(now.minusDays(1).withHour(20).withMinute(0)) // PASSÉE (hier)
                                .numberOfGuests(3)
                                .status(ReservationStatus.NO_SHOW)
                                .specialRequests("Table au calme")
                                .build(),

                        Reservation.builder()
                                .restaurantId(13L) // La Bodega
                                .user(savedUsers.get(2)) // client003
                                .customerName("Khalid Mouline")
                                .customerEmail("khalid.mouline@email.com")
                                .customerPhone("+212677889911")
                                .reservationDateTime(now.plusDays(10).withHour(20).withMinute(0))
                                .numberOfGuests(6)
                                .status(ReservationStatus.CONFIRMED)
                                .specialRequests("Tapas variées")
                                .build(),

                        Reservation.builder()
                                .restaurantId(14L) // Iloli
                                .user(savedUsers.get(3)) // client004
                                .customerName("Myriam Tazi")
                                .customerEmail("myriam.tazi@email.com")
                                .customerPhone("+212633445577")
                                .reservationDateTime(now.plusDays(11).withHour(19).withMinute(0))
                                .numberOfGuests(2)
                                .status(ReservationStatus.CONFIRMED)
                                .specialRequests("Sushi et sashimi")
                                .build(),

                        Reservation.builder()
                                .restaurantId(15L) // Le Bavarois
                                .user(savedUsers.get(4)) // client005
                                .customerName("Anas Berrada")
                                .customerEmail("anas.berrada@email.com")
                                .customerPhone("+212688990011")
                                .reservationDateTime(now.plusDays(12).withHour(20).withMinute(30))
                                .numberOfGuests(4)
                                .status(ReservationStatus.PENDING)
                                .specialRequests("Steak bien cuit")
                                .build()
                );

                // Sauvegarder les réservations
                try {
                    reservationRepository.saveAll(reservations);
                    System.out.println("✅ " + reservations.size() + " réservations chargées en base de données");
                } catch (Exception e) {
                    System.err.println("❌ Erreur lors de l'insertion des réservations: " + e.getMessage());
                    // Insérer une par une pour identifier l'erreur
                    int successCount = 0;
                    for (Reservation reservation : reservations) {
                        try {
                            reservationRepository.save(reservation);
                            successCount++;
                        } catch (Exception ex) {
                            System.err.println("Échec pour la réservation: " + reservation.getCustomerName() +
                                    " - Date: " + reservation.getReservationDateTime() +
                                    " - Erreur: " + ex.getMessage());
                        }
                    }
                    System.out.println("✅ " + successCount + " réservations insérées avec succès");
                }
            }
        } else {
            System.out.println("ℹ️  La base contient déjà des utilisateurs");
        }
    }
}