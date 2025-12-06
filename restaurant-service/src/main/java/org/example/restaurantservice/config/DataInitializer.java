package org.example.restaurantservice.config;
import lombok.RequiredArgsConstructor;
import org.example.restaurantservice.entity.Restaurant;
import org.example.restaurantservice.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;

    @Override
    public void run(String... args) throws Exception {
        if (restaurantRepository.count() == 0) {
            List<Restaurant> restaurants = Arrays.asList(
                    // 🎬 RESTAURANTS EMBLÉMATIQUES
                    Restaurant.builder()
                            .nom("Rick's Café")
                            .adresse("248 Boulevard Sour Jdid, Casablanca")
                            .latitude(new BigDecimal("33.6055"))
                            .longitude(new BigDecimal("-7.6165"))
                            .typeCuisine("International/Marocain")
                            .description("Célèbre café inspiré du film Casablanca, ambiance années 40 avec piano-bar et cuisine fusion maroco-internationale")
                            .heureOuverture("12:00")
                            .heureFermeture("23:30")
                            .prixMoyen(new BigDecimal("350"))
                            .noteMoyenne(4.5)
                            .nombreAvis(120)
                            .capaciteTotale(80)
                            .build(),

                    Restaurant.builder()
                            .nom("Al Mounia")
                            .adresse("95 Rue du Prince Moulay Abdallah, Casablanca")
                            .latitude(new BigDecimal("33.5890"))
                            .longitude(new BigDecimal("-7.6112"))
                            .typeCuisine("Marocaine Traditionnelle")
                            .description("Restaurant historique avec magnifique patio andalou, spécialités marocaines authentiques")
                            .heureOuverture("12:30")
                            .heureFermeture("23:00")
                            .prixMoyen(new BigDecimal("280"))
                            .noteMoyenne(4.6)
                            .nombreAvis(95)
                            .capaciteTotale(60)
                            .build(),

                    Restaurant.builder()
                            .nom("La Sqala")
                            .adresse("Boulevard des Almohades, Casablanca")
                            .latitude(new BigDecimal("33.6095"))
                            .longitude(new BigDecimal("-7.6231"))
                            .typeCuisine("Marocaine")
                            .description("Restaurant installé dans une forteresse du 18ème siècle avec beau jardin et cuisine traditionnelle")
                            .heureOuverture("11:00")
                            .heureFermeture("22:30")
                            .prixMoyen(new BigDecimal("200"))
                            .noteMoyenne(4.4)
                            .nombreAvis(200)
                            .capaciteTotale(120)
                            .build(),

                    // 🌊 RESTAURANTS AVEC VUE SUR LA MER
                    Restaurant.builder()
                            .nom("Le Cabestan")
                            .adresse("90 Phare d'El Hank, Casablanca")
                            .latitude(new BigDecimal("33.6078"))
                            .longitude(new BigDecimal("-7.6478"))
                            .typeCuisine("Méditerranéenne/Française")
                            .description("Vue spectaculaire sur l'océan Atlantique, cuisine raffinée et ambiance élégante")
                            .heureOuverture("12:00")
                            .heureFermeture("00:00")
                            .prixMoyen(new BigDecimal("400"))
                            .noteMoyenne(4.7)
                            .nombreAvis(150)
                            .capaciteTotale(90)
                            .build(),

                    Restaurant.builder()
                            .nom("Le Port de Pêche")
                            .adresse("Port de Pêche, Casablanca")
                            .latitude(new BigDecimal("33.6092"))
                            .longitude(new BigDecimal("-7.6325"))
                            .typeCuisine("Poissons/Fruits de Mer")
                            .description("Poissons et fruits de mer frais dans le port de pêche, ambiance authentique")
                            .heureOuverture("12:00")
                            .heureFermeture("23:00")
                            .prixMoyen(new BigDecimal("250"))
                            .noteMoyenne(4.3)
                            .nombreAvis(80)
                            .capaciteTotale(70)
                            .build(),

                    Restaurant.builder()
                            .nom("Taverne du Dauphin")
                            .adresse("115 Boulevard de la Corniche, Ain Diab")
                            .latitude(new BigDecimal("33.5923"))
                            .longitude(new BigDecimal("-7.6876"))
                            .typeCuisine("Poissons/Fruits de Mer")
                            .description("Institution casablancaise spécialisée dans les fruits de mer depuis 1958")
                            .heureOuverture("12:00")
                            .heureFermeture("23:00")
                            .prixMoyen(new BigDecimal("280"))
                            .noteMoyenne(4.4)
                            .nombreAvis(110)
                            .capaciteTotale(85)
                            .build(),

                    // 🍖 CUISINE MAROCAINE TRADITIONNELLE
                    Restaurant.builder()
                            .nom("Dar Beida")
                            .adresse("12 Rue Ahmed Cherkaoui, Casablanca")
                            .latitude(new BigDecimal("33.5732"))
                            .longitude(new BigDecimal("-7.6321"))
                            .typeCuisine("Marocaine")
                            .description("Cuisine marocaine familiale dans un cadre chaleureux, spécialités de tajines et couscous")
                            .heureOuverture("12:00")
                            .heureFermeture("22:30")
                            .prixMoyen(new BigDecimal("180"))
                            .noteMoyenne(4.2)
                            .nombreAvis(75)
                            .capaciteTotale(40)
                            .build(),

                    Restaurant.builder()
                            .nom("Le Riad")
                            .adresse("13 Rue de Yougoslavie, Casablanca")
                            .latitude(new BigDecimal("33.5915"))
                            .longitude(new BigDecimal("-7.6189"))
                            .typeCuisine("Marocaine")
                            .description("Riad traditionnel avec patio, musique live et cuisine marocaine raffinée")
                            .heureOuverture("12:00")
                            .heureFermeture("23:00")
                            .prixMoyen(new BigDecimal("220"))
                            .noteMoyenne(4.5)
                            .nombreAvis(90)
                            .capaciteTotale(50)
                            .build(),

                    Restaurant.builder()
                            .nom("La Maison du Tajine")
                            .adresse("45 Rue Allal Ben Ahmed, Casablanca")
                            .latitude(new BigDecimal("33.5789"))
                            .longitude(new BigDecimal("-7.6123"))
                            .typeCuisine("Marocaine")
                            .description("Spécialiste du tajine sous toutes ses formes, recettes traditionnelles")
                            .heureOuverture("11:30")
                            .heureFermeture("22:00")
                            .prixMoyen(new BigDecimal("160"))
                            .noteMoyenne(4.1)
                            .nombreAvis(65)
                            .capaciteTotale(35)
                            .build(),

                    // 🥘 CUISINE FUSION ET MODERNE
                    Restaurant.builder()
                            .nom("NKOA")
                            .adresse("123 Boulevard d'Anfa, Casablanca")
                            .latitude(new BigDecimal("33.5876"))
                            .longitude(new BigDecimal("-7.6321"))
                            .typeCuisine("Marocaine Moderne")
                            .description("Cuisine marocaine réinventée avec des touches contemporaines")
                            .heureOuverture("12:30")
                            .heureFermeture("23:30")
                            .prixMoyen(new BigDecimal("320"))
                            .noteMoyenne(4.6)
                            .nombreAvis(85)
                            .capaciteTotale(45)
                            .build(),

                    Restaurant.builder()
                            .nom("L'Annexe")
                            .adresse("Rue Chaouia, Casablanca")
                            .latitude(new BigDecimal("33.5712"))
                            .longitude(new BigDecimal("-7.6678"))
                            .typeCuisine("Fusion")
                            .description("Cuisine créative fusion maroco-européenne dans un cadre design")
                            .heureOuverture("12:00")
                            .heureFermeture("23:00")
                            .prixMoyen(new BigDecimal("290"))
                            .noteMoyenne(4.4)
                            .nombreAvis(70)
                            .capaciteTotale(40)
                            .build(),

                    Restaurant.builder()
                            .nom("Le Studio")
                            .adresse("Avenue Hassan II, Casablanca")
                            .latitude(new BigDecimal("33.5943"))
                            .longitude(new BigDecimal("-7.6098"))
                            .typeCuisine("Marocaine Moderne")
                            .description("Cuisine marocaine revisitée avec des produits locaux de qualité")
                            .heureOuverture("12:00")
                            .heureFermeture("22:30")
                            .prixMoyen(new BigDecimal("270"))
                            .noteMoyenne(4.3)
                            .nombreAvis(60)
                            .capaciteTotale(30)
                            .build(),

                    // 🍝 RESTAURANTS INTERNATIONAUX
                    Restaurant.builder()
                            .nom("La Bodega")
                            .adresse("Rue Allal Ben Ahmed, Casablanca")
                            .latitude(new BigDecimal("33.5791"))
                            .longitude(new BigDecimal("-7.6134"))
                            .typeCuisine("Espagnole")
                            .description("Tapas et spécialités espagnoles dans une ambiance chaleureuse")
                            .heureOuverture("12:00")
                            .heureFermeture("23:00")
                            .prixMoyen(new BigDecimal("230"))
                            .noteMoyenne(4.3)
                            .nombreAvis(95)
                            .capaciteTotale(55)
                            .build(),

                    Restaurant.builder()
                            .nom("Iloli")
                            .adresse("107 Rue Allal Ben Ahmed, Casablanca")
                            .latitude(new BigDecimal("33.5785"))
                            .longitude(new BigDecimal("-7.6142"))
                            .typeCuisine("Japonaise")
                            .description("Cuisine japonaise fusion et cocktails créatifs")
                            .heureOuverture("12:30")
                            .heureFermeture("23:30")
                            .prixMoyen(new BigDecimal("310"))
                            .noteMoyenne(4.5)
                            .nombreAvis(105)
                            .capaciteTotale(40)
                            .build(),

                    Restaurant.builder()
                            .nom("Le Bavarois")
                            .adresse("Avenue des FAR, Casablanca")
                            .latitude(new BigDecimal("33.5689"))
                            .longitude(new BigDecimal("-7.6576"))
                            .typeCuisine("Française")
                            .description("Brasserie française classique, steaks et plats traditionnels")
                            .heureOuverture("12:00")
                            .heureFermeture("22:30")
                            .prixMoyen(new BigDecimal("260"))
                            .noteMoyenne(4.2)
                            .nombreAvis(80)
                            .capaciteTotale(60)
                            .build(),

                    // 🍢 RESTAURANTS POPULAIRES ET AUTHENTIQUES
                    Restaurant.builder()
                            .nom("Boulevard des Saveurs")
                            .adresse("Boulevard Mohamed V, Casablanca")
                            .latitude(new BigDecimal("33.5934"))
                            .longitude(new BigDecimal("-7.6156"))
                            .typeCuisine("Street Food Marocaine")
                            .description("Stand célèbre pour les brochettes, merguez et sandwiches marocains")
                            .heureOuverture("11:00")
                            .heureFermeture("22:00")
                            .prixMoyen(new BigDecimal("50"))
                            .noteMoyenne(4.0)
                            .nombreAvis(150)
                            .capaciteTotale(20)
                            .build(),

                    Restaurant.builder()
                            .nom("Café Clock")
                            .adresse("Derb Chtouka, Casablanca")
                            .latitude(new BigDecimal("33.5901"))
                            .longitude(new BigDecimal("-7.6123"))
                            .typeCuisine("Marocaine/Café Culture")
                            .description("Café culturel avec cuisine marocaine, ateliers et musique live")
                            .heureOuverture("10:00")
                            .heureFermeture("22:00")
                            .prixMoyen(new BigDecimal("120"))
                            .noteMoyenne(4.4)
                            .nombreAvis(110)
                            .capaciteTotale(50)
                            .build(),

                    Restaurant.builder()
                            .nom("Le Petit Rocher")
                            .adresse("Corniche Ain Diab, Casablanca")
                            .latitude(new BigDecimal("33.5912"))
                            .longitude(new BigDecimal("-7.6823"))
                            .typeCuisine("Marocaine/Internationale")
                            .description("Vue sur la mer, ambiance décontractée et cuisine variée")
                            .heureOuverture("11:30")
                            .heureFermeture("23:00")
                            .prixMoyen(new BigDecimal("190"))
                            .noteMoyenne(4.1)
                            .nombreAvis(85)
                            .capaciteTotale(65)
                            .build(),

                    // 🍛 RESTAURANTS GASTRONOMIQUES
                    Restaurant.builder()
                            .nom("Gusto")
                            .adresse("Angle Rue Ibnou Batouta & Rue Cadi Bnou Al Arabi")
                            .latitude(new BigDecimal("33.5612"))
                            .longitude(new BigDecimal("-7.6678"))
                            .typeCuisine("Italienne")
                            .description("Cuisine italienne raffinée, pâtes fraîches et pizza au feu de bois")
                            .heureOuverture("12:30")
                            .heureFermeture("23:30")
                            .prixMoyen(new BigDecimal("340"))
                            .noteMoyenne(4.7)
                            .nombreAvis(95)
                            .capaciteTotale(40)
                            .build(),

                    Restaurant.builder()
                            .nom("La Table du Marché")
                            .adresse("Marina de Casablanca")
                            .latitude(new BigDecimal("33.6012"))
                            .longitude(new BigDecimal("-7.6345"))
                            .typeCuisine("Française")
                            .description("Cuisine française market, produits frais du marché")
                            .heureOuverture("12:00")
                            .heureFermeture("23:00")
                            .prixMoyen(new BigDecimal("380"))
                            .noteMoyenne(4.6)
                            .nombreAvis(75)
                            .capaciteTotale(35)
                            .build(),

                    Restaurant.builder()
                            .nom("Le Gatsby")
                            .adresse("Avenue Mers Sultan, Casablanca")
                            .latitude(new BigDecimal("33.5834"))
                            .longitude(new BigDecimal("-7.6098"))
                            .typeCuisine("Internationale")
                            .description("Ambiance années 20, cuisine internationale créative")
                            .heureOuverture("19:00")
                            .heureFermeture("01:00")
                            .prixMoyen(new BigDecimal("420"))
                            .noteMoyenne(4.8)
                            .nombreAvis(65)
                            .capaciteTotale(30)
                            .build()
            );

            restaurantRepository.saveAll(restaurants);
            System.out.println("✅ " + restaurants.size() + " restaurants casablancais chargés en base de données");
        } else {
            System.out.println("ℹ️  La base contient déjà " + restaurantRepository.count() + " restaurants");
        }
    }
}