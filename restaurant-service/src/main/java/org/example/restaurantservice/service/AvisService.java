package org.example.restaurantservice.service;
import lombok.RequiredArgsConstructor;
import org.example.restaurantservice.dto.AvisDTO;
import org.example.restaurantservice.entity.Avis;
import org.example.restaurantservice.entity.Restaurant;
import org.example.restaurantservice.repository.AvisRepository;
import org.example.restaurantservice.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvisService implements IAvisService {

    private final AvisRepository avisRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public AvisDTO ajouterAvis(AvisDTO avisDTO) {
        if (avisDTO.getNote() < 1 || avisDTO.getNote() > 5) {
            throw new RuntimeException("La note doit être entre 1 et 5");
        }

        Restaurant restaurant = restaurantRepository.findById(avisDTO.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant non trouvé"));

        Avis avis = Avis.builder()
                .commentaire(avisDTO.getCommentaire())
                .note(avisDTO.getNote())
                .auteurNom(avisDTO.getAuteurNom())
                .restaurant(restaurant)
                .build();

        Avis savedAvis = avisRepository.save(avis);
        mettreAJourNoteMoyenne(restaurant.getId());

        return convertToDTO(savedAvis);
    }

    @Override
    public List<AvisDTO> getAvisByRestaurant(Long restaurantId) {
        return avisRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AvisDTO> getTop5AvisByRestaurant(Long restaurantId) {
        return avisRepository.findTop5ByRestaurantIdOrderByCreatedAtDesc(restaurantId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void mettreAJourNoteMoyenne(Long restaurantId) {
        Double moyenneNote = avisRepository.findAverageNoteByRestaurantId(restaurantId);
        Long nombreAvis = avisRepository.countByRestaurantId(restaurantId);

        if (moyenneNote != null) {
            restaurantRepository.findById(restaurantId).ifPresent(restaurant -> {
                restaurant.setNoteMoyenne(moyenneNote);
                restaurant.setNombreAvis(nombreAvis.intValue());
                restaurantRepository.save(restaurant);
            });
        }
    }

    private AvisDTO convertToDTO(Avis avis) {
        return AvisDTO.builder()
                .id(avis.getId())
                .commentaire(avis.getCommentaire())
                .note(avis.getNote())
                .auteurNom(avis.getAuteurNom())
                .restaurantId(avis.getRestaurant().getId())
                .restaurantNom(avis.getRestaurant().getNom())
                .createdAt(avis.getCreatedAt())
                .build();
    }
}