package org.example.restaurantservice.controller;
import lombok.RequiredArgsConstructor;
import org.example.restaurantservice.dto.AvisDTO;
import org.example.restaurantservice.service.AvisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avis")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AvisController {

    private final AvisService avisService;

    @PostMapping
    public ResponseEntity<AvisDTO> ajouterAvis(@RequestBody AvisDTO avisDTO) {
        return ResponseEntity.ok(avisService.ajouterAvis(avisDTO));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<AvisDTO>> getAvisByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(avisService.getAvisByRestaurant(restaurantId));
    }

    @GetMapping("/restaurant/{restaurantId}/top5")
    public ResponseEntity<List<AvisDTO>> getTop5AvisByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(avisService.getTop5AvisByRestaurant(restaurantId));
    }
}