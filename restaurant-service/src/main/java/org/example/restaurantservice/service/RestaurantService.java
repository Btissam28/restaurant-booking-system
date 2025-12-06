package org.example.restaurantservice.service;

import lombok.RequiredArgsConstructor;
import org.example.restaurantservice.dto.RechercheRestaurantRequest;
import org.example.restaurantservice.dto.RestaurantDTO;
import org.example.restaurantservice.entity.Restaurant;
import org.example.restaurantservice.repository.RestaurantRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService implements IRestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public Restaurant findById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé avec l'ID: " + id));
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchByNom(String nom) {
        return restaurantRepository.findByNomContainingIgnoreCase(nom);
    }

    @Override
    public List<Restaurant> findByTypeCuisine(String typeCuisine) {
        return restaurantRepository.findByTypeCuisineContainingIgnoreCase(typeCuisine);
    }

    @Override
    public List<Restaurant> findByNoteMin(Double noteMin) {
        return restaurantRepository.findByNoteMoyenneGreaterThanEqual(noteMin);
    }

    @Override
    public List<Restaurant> findByPrixMax(BigDecimal prixMax) {
        return restaurantRepository.findByPrixMoyenLessThanEqual(prixMax);
    }

    @Override
    public List<Restaurant> findNearbyRestaurants(BigDecimal latitude, BigDecimal longitude, Double rayon) {
        return restaurantRepository.findNearbyRestaurants(latitude, longitude, rayon);
    }

    @Override
    public List<String> getAllTypeCuisine() {
        return restaurantRepository.findDistinctTypeCuisine();
    }

    @Override
    public List<Restaurant> getTopRatedRestaurants() {
        return restaurantRepository.findTopRatedRestaurants();
    }

    @Override
    public List<RestaurantDTO> rechercherRestaurants(RechercheRestaurantRequest request) {
        Specification<Restaurant> spec = Specification.where(null);

        // Filtre par nom
        if (request.getNom() != null && !request.getNom().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("nom")), "%" + request.getNom().toLowerCase() + "%"));
        }

        // Filtre par type de cuisine
        if (request.getTypeCuisine() != null && !request.getTypeCuisine().isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("typeCuisine")),
                            "%" + request.getTypeCuisine().toLowerCase() + "%"));
        }

        // Filtre par note minimum
        if (request.getNoteMin() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("noteMoyenne"), request.getNoteMin()));
        }

        // Filtre par prix minimum
        if (request.getPrixMin() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("prixMoyen"), request.getPrixMin()));
        }

        // Filtre par prix maximum
        if (request.getPrixMax() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("prixMoyen"), request.getPrixMax()));
        }

        // Filtre par capacité minimum
        if (request.getCapaciteMin() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("capaciteTotale"), request.getCapaciteMin()));
        }

        // Filtre par capacité maximum
        if (request.getCapaciteMax() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("capaciteTotale"), request.getCapaciteMax()));
        }

        List<Restaurant> restaurants = restaurantRepository.findAll(spec);

        // Filtrage par distance si coordonnées fournies
        if (request.getLatitude() != null && request.getLongitude() != null) {
            restaurants = restaurants.stream()
                    .map(restaurant -> {
                        double distance = calculerDistance(
                                request.getLatitude().doubleValue(),
                                request.getLongitude().doubleValue(),
                                restaurant.getLatitude().doubleValue(),
                                restaurant.getLongitude().doubleValue()
                        );
                        return new RestaurantDistance(restaurant, distance);
                    })
                    .filter(rd -> request.getRayon() == null || rd.distance <= request.getRayon())
                    .sorted(getComparator(request))
                    .map(rd -> rd.restaurant)
                    .collect(Collectors.toList());
        } else {
            // Tri sans distance
            restaurants.sort(getComparatorSansDistance(request));
        }

        // Filtre par ouverture maintenant
        if (request.getOuvertMaintenant() != null && request.getOuvertMaintenant()) {
            restaurants = restaurants.stream()
                    .filter(this::estOuvertMaintenant)
                    .collect(Collectors.toList());
        }

        return restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private Comparator<RestaurantDistance> getComparator(RechercheRestaurantRequest request) {
        return (rd1, rd2) -> {
            if (request.getTri() == null) {
                return 0;
            }

            switch (request.getTri().toLowerCase()) {
                case "distance":
                    return Double.compare(rd1.distance, rd2.distance);

                case "note":
                    return Double.compare(rd2.restaurant.getNoteMoyenne(), rd1.restaurant.getNoteMoyenne());

                case "popularite":
                    return Integer.compare(rd2.restaurant.getNombreAvis(), rd1.restaurant.getNombreAvis());

                case "prix_asc":
                    return rd1.restaurant.getPrixMoyen().compareTo(rd2.restaurant.getPrixMoyen());

                case "prix_desc":
                    return rd2.restaurant.getPrixMoyen().compareTo(rd1.restaurant.getPrixMoyen());

                case "capacite":
                    return Integer.compare(rd2.restaurant.getCapaciteTotale(), rd1.restaurant.getCapaciteTotale());

                default:
                    return 0;
            }
        };
    }

    private Comparator<Restaurant> getComparatorSansDistance(RechercheRestaurantRequest request) {
        return (r1, r2) -> {
            if (request.getTri() == null) {
                return 0;
            }

            switch (request.getTri().toLowerCase()) {
                case "note":
                    return Double.compare(r2.getNoteMoyenne(), r1.getNoteMoyenne());

                case "popularite":
                    return Integer.compare(r2.getNombreAvis(), r1.getNombreAvis());

                case "prix_asc":
                    return r1.getPrixMoyen().compareTo(r2.getPrixMoyen());

                case "prix_desc":
                    return r2.getPrixMoyen().compareTo(r1.getPrixMoyen());

                case "capacite":
                    return Integer.compare(r2.getCapaciteTotale(), r1.getCapaciteTotale());

                default:
                    return 0;
            }
        };
    }

    public boolean estOuvertMaintenant(Restaurant restaurant) {
        try {
            LocalTime maintenant = LocalTime.now();

            if (restaurant.getHeureOuverture() == null || restaurant.getHeureFermeture() == null) {
                return false;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime heureOuverture = LocalTime.parse(restaurant.getHeureOuverture(), formatter);
            LocalTime heureFermeture = LocalTime.parse(restaurant.getHeureFermeture(), formatter);

            // Gérer les cas où le restaurant ferme après minuit
            if (heureFermeture.isBefore(heureOuverture)) {
                return !maintenant.isBefore(heureOuverture) || !maintenant.isAfter(heureFermeture);
            } else {
                return !maintenant.isBefore(heureOuverture) && !maintenant.isAfter(heureFermeture);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private double calculerDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Rayon de la Terre en km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

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

    // Nouvelle méthode pour les restaurants populaires
    public List<RestaurantDTO> getRestaurantsPopulaires(int limit) {
        return restaurantRepository.findAll().stream()
                .sorted((r1, r2) -> Integer.compare(r2.getNombreAvis(), r1.getNombreAvis()))
                .limit(limit)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Nouvelle méthode pour les restaurants à proximité
    public List<RestaurantDTO> getRestaurantsProches(BigDecimal latitude, BigDecimal longitude, Double rayon) {
        return restaurantRepository.findNearbyRestaurants(latitude, longitude, rayon).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Nouvelle méthode pour les recommandations personnalisées
    public List<RestaurantDTO> getRecommandations(String typeCuisinePref, Double noteMinPref) {
        Specification<Restaurant> spec = Specification.where(null);

        if (typeCuisinePref != null && !typeCuisinePref.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("typeCuisine")), "%" + typeCuisinePref.toLowerCase() + "%"));
        }

        if (noteMinPref != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("noteMoyenne"), noteMinPref));
        }

        return restaurantRepository.findAll(spec).stream()
                .sorted((r1, r2) -> Double.compare(r2.getNoteMoyenne(), r1.getNoteMoyenne()))
                .limit(10)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private static class RestaurantDistance {
        Restaurant restaurant;
        double distance;

        RestaurantDistance(Restaurant restaurant, double distance) {
            this.restaurant = restaurant;
            this.distance = distance;
        }
    }

}