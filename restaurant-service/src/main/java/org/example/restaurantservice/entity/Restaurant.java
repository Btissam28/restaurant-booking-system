package org.example.restaurantservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Column(nullable = false)
    private String adresse;

    @NotNull(message = "La latitude est obligatoire")
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @NotNull(message = "La longitude est obligatoire")
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @NotBlank(message = "Le type de cuisine est obligatoire")
    @Column(name = "type_cuisine", nullable = false)
    private String typeCuisine;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "heure_ouverture")
    private String heureOuverture;

    @Column(name = "heure_fermeture")
    private String heureFermeture;

    @Column(name = "prix_moyen", precision = 10, scale = 2)
    private BigDecimal prixMoyen;

    @Column(name = "note_moyenne")
    @Builder.Default
    private Double noteMoyenne = 0.0;

    @Column(name = "nombre_avis")
    @Builder.Default
    private Integer nombreAvis = 0;

    @Column(name = "capacite_totale")
    private Integer capaciteTotale;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<Avis> avis = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}