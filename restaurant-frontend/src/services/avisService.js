import { api } from './api';

export const avisService = {
    // Récupérer tous les avis d'un restaurant
    getByRestaurant: (restaurantId) => api.get(`/avis/restaurant/${restaurantId}`),
    
    // Récupérer les 5 derniers avis d'un restaurant
    getTop5ByRestaurant: (restaurantId) => api.get(`/avis/restaurant/${restaurantId}/top5`),
    
    // Ajouter un nouvel avis
    ajouterAvis: (avisData) => api.post('/avis', avisData),
};

