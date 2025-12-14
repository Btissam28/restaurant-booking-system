import { useState, useEffect } from 'react';
import { Search } from 'lucide-react';
import { restaurantService } from '../services/restaurantService';
import RestaurantCard from '../components/RestaurantCard';

const Home = () => {
    const [restaurants, setRestaurants] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [cuisines, setCuisines] = useState([]);
    const [selectedCuisine, setSelectedCuisine] = useState('');

    useEffect(() => {
        fetchInitialData();
    }, []);

    const fetchInitialData = async () => {
        try {
            const data = await restaurantService.getPopular(8);
            const cuisinesData = await restaurantService.getCuisines();
            setRestaurants(data);
            setCuisines(cuisinesData);
        } catch (error) {
            console.error('Error fetching home data:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (!searchTerm && !selectedCuisine) {
                // Reset to popular if fields are empty
                const data = await restaurantService.getPopular(8);
                setRestaurants(data);
            } else {
                // Create the search criteria object expected by backend
                const searchCriteria = {};
                if (searchTerm) searchCriteria.nom = searchTerm;
                if (selectedCuisine) searchCriteria.typeCuisine = selectedCuisine;

                // Use the search endpoint
                const data = await restaurantService.search(searchCriteria);
                setRestaurants(data);
            }
        } catch (error) {
            console.error('Search failed:', error);
            // Fallback: filter locally
            const allRestaurants = await restaurantService.getAll();
            const filtered = allRestaurants.filter(restaurant => {
                let matches = true;
                if (searchTerm) {
                    matches = matches && restaurant.nom.toLowerCase().includes(searchTerm.toLowerCase());
                }
                if (selectedCuisine) {
                    matches = matches && restaurant.typeCuisine.includes(selectedCuisine);
                }
                return matches;
            });
            setRestaurants(filtered);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            {/* Hero Section */}
            <section style={{
                backgroundColor: 'var(--secondary)',
                color: 'white',
                padding: '5rem 0',
                marginBottom: '3rem'
            }}>
                <div className="container text-center">
                    <h1 style={{ fontSize: '3rem', marginBottom: '1.5rem', fontWeight: 800 }}>
                        Découvrez & Réservez <br /> <span style={{ color: 'var(--primary)' }}>Les Meilleurs Restaurants</span>
                    </h1>
                    <p style={{ fontSize: '1.25rem', color: 'var(--text-light)', marginBottom: '3rem', maxWidth: '600px', margin: '0 auto 3rem' }}>
                        Trouvez la table parfaite pour toutes les occasions parmi notre sélection de restaurants top-notés.
                    </p>

                    <form onSubmit={handleSearch} style={{
                        background: 'white',
                        padding: '0.5rem',
                        borderRadius: 'var(--radius-full)',
                        maxWidth: '700px',
                        margin: '0 auto',
                        display: 'flex',
                        gap: '0.5rem',
                        boxShadow: 'var(--shadow-lg)'
                    }}>
                        <div style={{ flex: 1, position: 'relative', display: 'flex', alignItems: 'center', paddingLeft: '1.5rem' }}>
                            <Search size={20} color="var(--text-light)" />
                            <input
                                type="text"
                                placeholder="Nom, Adresse..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                style={{
                                    border: 'none',
                                    outline: 'none',
                                    width: '100%',
                                    padding: '1rem',
                                    fontSize: '1rem',
                                    color: 'var(--text-main)'
                                }}
                            />
                        </div>

                        <div style={{ borderLeft: '1px solid var(--surface-alt)', padding: '0 1rem', display: 'flex', alignItems: 'center' }}>
                            <select
                                value={selectedCuisine}
                                onChange={(e) => setSelectedCuisine(e.target.value)}
                                style={{
                                    border: 'none',
                                    outline: 'none',
                                    padding: '1rem',
                                    fontSize: '1rem',
                                    color: 'var(--text-main)',
                                    backgroundColor: 'transparent',
                                    cursor: 'pointer'
                                }}
                            >
                                <option value="">Toutes cuisines</option>
                                {cuisines.map(c => (
                                    <option key={c} value={c}>{c}</option>
                                ))}
                            </select>
                        </div>

                        <button type="submit" className="btn btn-primary" style={{ borderRadius: 'var(--radius-full)', padding: '0 2rem' }}>
                            Rechercher
                        </button>
                    </form>
                </div>
            </section>

            {/* Restaurants List */}
            <div className="container">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
                    <h2 style={{ fontSize: '1.75rem', fontWeight: 700 }}>
                        {searchTerm || selectedCuisine ? 'Résultats de recherche' : 'Restaurants Populaires'}
                    </h2>
                </div>

                {loading ? (
                    <div className="text-center" style={{ padding: '4rem' }}>Chargement...</div>
                ) : (
                    <>
                        {restaurants.length > 0 ? (
                            <div className="grid grid-3">
                                {restaurants.map(restaurant => (
                                    <RestaurantCard key={restaurant.id} restaurant={restaurant} />
                                ))}
                            </div>
                        ) : (
                            <div className="text-center" style={{ padding: '4rem', color: 'var(--text-secondary)' }}>
                                Aucun restaurant trouvé.
                            </div>
                        )}
                    </>
                )}
            </div>
        </div>
    );
};

export default Home;