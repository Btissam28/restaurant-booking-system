const API_BASE_URL = '/api';

export const api = {
    request: async (endpoint, options = {}) => {
        const defaultHeaders = {
            'Content-Type': 'application/json',
        };

        const config = {
            ...options,
            headers: {
                ...defaultHeaders,
                ...options.headers,
            },
        };

        const url = `${API_BASE_URL}${endpoint}`;
        console.log(`🌐 API Call: ${config.method || 'GET'} ${url}`);
        if (config.body) {
            console.log('📦 Request body:', config.body);
        }

        try {
            const response = await fetch(url, config);

            console.log(`📥 Response status: ${response.status} ${response.statusText}`);

            // Essayer de lire le corps de la réponse pour toutes les réponses
            const responseText = await response.text();
            console.log('📄 Response body:', responseText);

            let data;
            try {
                data = responseText ? JSON.parse(responseText) : null;
            } catch (e) {
                console.log('Could not parse response as JSON');
                data = responseText;
            }

            if (!response.ok) {
                let errorMessage = `Error ${response.status}`;
                if (data && data.message) {
                    errorMessage = data.message;
                } else if (data && data.error) {
                    errorMessage = data.error;
                } else if (typeof data === 'string') {
                    errorMessage = data;
                }

                const error = new Error(errorMessage);
                error.response = response;
                error.data = data;
                error.status = response.status;
                throw error;
            }

            return data;

        } catch (error) {
            console.error('❌ API Request Failed:', {
                endpoint,
                error: error.message,
                status: error.status,
                data: error.data
            });
            throw error;
        }
    },

    get: (endpoint) => api.request(endpoint, { method: 'GET' }),

    post: (endpoint, data) => api.request(endpoint, {
        method: 'POST',
        body: JSON.stringify(data)
    }),

    put: (endpoint, data) => api.request(endpoint, {
        method: 'PUT',
        body: JSON.stringify(data)
    }),

    delete: (endpoint) => api.request(endpoint, { method: 'DELETE' }),
};