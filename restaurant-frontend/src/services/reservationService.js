import { api } from './api';

export const reservationService = {
    create: (data) => api.post('/reservations', data),
    getUserReservations: (userId) => api.get(`/reservations/user/${userId}`),
    getUpcoming: (userId) => api.get(`/reservations/user/${userId}/upcoming`),
    cancel: (id) => api.post(`/reservations/${id}/cancel`),
};
