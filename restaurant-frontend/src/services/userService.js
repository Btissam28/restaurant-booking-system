import { api } from './api';

export const userService = {
    getByEmail: (email) => api.get(`/users/email/${email}`),
    create: (userData) => api.post('/users', userData),
};
