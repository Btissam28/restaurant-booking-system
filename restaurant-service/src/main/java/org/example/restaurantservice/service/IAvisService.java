package org.example.restaurantservice.service;
import org.example.restaurantservice.dto.AvisDTO;

import java.util.List;

public interface IAvisService {
    AvisDTO ajouterAvis(AvisDTO avisDTO);
    List<AvisDTO> getAvisByRestaurant(Long restaurantId);
    List<AvisDTO> getTop5AvisByRestaurant(Long restaurantId);
}