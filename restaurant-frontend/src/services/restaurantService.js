import { api } from './api';

export const restaurantService = {
    getAll: () => api.get('/restaurants'),
    getById: (id) => api.get(`/restaurants/${id}`),
    getTopRated: () => api.get('/restaurants/top-rated'),
    getPopular: (limit = 10) => api.get(`/restaurants/populaires?limit=${limit}`),
    getCuisines: () => api.get('/restaurants/cuisines'),
    search: (query) => api.post('/restaurants/recherche', query),
    checkAvailability: (id, dateTime, guests) =>
        api.get(`/restaurants/${id}/availability?dateTime=${dateTime}&guests=${guests}`),
};
