import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { Star, MapPin, Clock, Info } from 'lucide-react';
import { restaurantService } from '../services/restaurantService';
import { avisService } from '../services/avisService';
import BookingForm from '../components/BookingForm';
import ReviewList from '../components/ReviewList';
import ReviewForm from '../components/ReviewForm';

const Restaurant = () => {
    const { id } = useParams();
    const [restaurant, setRestaurant] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [activeTab, setActiveTab] = useState('overview');

    const fetchReviews = useCallback(async () => {
        try {
            const reviewsData = await avisService.getByRestaurant(id);
            setReviews(reviewsData || []);
        } catch (e) {
            console.warn('Could not fetch reviews', e);
            setReviews([]);
        }
    }, [id]);

    useEffect(() => {
        const fetchRestaurantData = async () => {
            try {
                const data = await restaurantService.getById(id);
                setRestaurant(data);
                await fetchReviews();
            } catch (error) {
                console.error('Failed to load restaurant', error);
            } finally {
                setLoading(false);
            }
        };

        fetchRestaurantData();
    }, [id, fetchReviews]);

    if (loading) return <div className="container mt-4 text-center">Chargement...</div>;
    if (!restaurant) return <div className="container mt-4 text-center">Restaurant introuvable.</div>;

    return (
        <div>
            {/* Header Image */}
            <div style={{ height: '300px', backgroundColor: '#e9ecef', position: 'relative', overflow: 'hidden' }}>
                <img
                    src={`https://source.unsplash.com/1600x900/?restaurant,${restaurant.typeCuisine}`}
                    alt={restaurant.nom}
                    style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                    onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = 'https://via.placeholder.com/1600x900?text=Restaurant+Image';
                    }}
                />
                <div style={{
                    position: 'absolute',
                    bottom: 0,
                    left: 0,
                    right: 0,
                    background: 'linear-gradient(to top, rgba(0,0,0,0.8), transparent)',
                    padding: '2rem 0',
                    color: 'white'
                }}>
                    <div className="container">
                        <h1 style={{ fontSize: '2.5rem', marginBottom: '0.5rem', fontWeight: 700 }}>{restaurant.nom}</h1>
                        <div style={{ display: 'flex', gap: '1.5rem', alignItems: 'center', fontSize: '1rem' }}>
                            <span style={{ backgroundColor: 'var(--primary)', padding: '0.25rem 0.75rem', borderRadius: 'var(--radius-full)' }}>
                                {restaurant.typeCuisine}
                            </span>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                                <Star size={18} fill="var(--warning)" color="var(--warning)" />
                                <span style={{ fontWeight: 600 }}>{restaurant.noteMoyenne}</span>
                                <span style={{ opacity: 0.8 }}>({restaurant.nombreAvis} avis)</span>
                            </div>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.25rem' }}>
                                <MapPin size={18} />
                                <span>{restaurant.adresse}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="container mt-4">
                <div className="grid" style={{ gridTemplateColumns: '2fr 1fr', alignItems: 'start' }}>

                    {/* Main Content */}
                    <div>
                        {/* Tabs */}
                        <div style={{ display: 'flex', borderBottom: '1px solid var(--surface-alt)', marginBottom: '2rem' }}>
                            <button
                                className={`btn ${activeTab === 'overview' ? 'btn-primary' : ''}`}
                                style={{
                                    borderRadius: '0',
                                    borderBottom: activeTab === 'overview' ? '2px solid var(--primary)' : 'none',
                                    backgroundColor: 'transparent',
                                    color: activeTab === 'overview' ? 'var(--primary)' : 'var(--text-secondary)'
                                }}
                                onClick={() => setActiveTab('overview')}
                            >
                                Vue d'ensemble
                            </button>
                            <button
                                className={`btn ${activeTab === 'reviews' ? 'btn-primary' : ''}`}
                                style={{
                                    borderRadius: '0',
                                    borderBottom: activeTab === 'reviews' ? '2px solid var(--primary)' : 'none',
                                    backgroundColor: 'transparent',
                                    color: activeTab === 'reviews' ? 'var(--primary)' : 'var(--text-secondary)'
                                }}
                                onClick={() => setActiveTab('reviews')}
                            >
                                Avis ({reviews.length})
                            </button>
                        </div>

                        {activeTab === 'overview' && (
                            <div className="card" style={{ padding: '2rem' }}>
                                <h3 style={{ fontSize: '1.5rem', marginBottom: '1rem' }}>À propos</h3>
                                <p style={{ lineHeight: 1.8, color: 'var(--text-secondary)', marginBottom: '2rem' }}>
                                    {restaurant.description || "Aucune description disponible pour ce restaurant."}
                                </p>

                                <h4 style={{ fontSize: '1.1rem', marginBottom: '1rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                    <Info size={20} /> Informations Pratiques
                                </h4>
                                <div className="grid grid-2">
                                    <div>
                                        <span style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>Horaires</span>
                                        <p style={{ fontWeight: 500 }}>{restaurant.heureOuverture} - {restaurant.heureFermeture}</p>
                                    </div>
                                    <div>
                                        <span style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>Capacité</span>
                                        <p style={{ fontWeight: 500 }}>{restaurant.capaciteTotale} places</p>
                                    </div>
                                    <div>
                                        <span style={{ color: 'var(--text-secondary)', fontSize: '0.9rem' }}>Prix Moyen</span>
                                        <p style={{ fontWeight: 500 }}>{restaurant.prixMoyen}€</p>
                                    </div>
                                </div>
                            </div>
                        )}

                        {activeTab === 'reviews' && (
                            <div>
                                <ReviewForm restaurantId={id} onReviewAdded={fetchReviews} />
                                <ReviewList reviews={reviews} />
                            </div>
                        )}
                    </div>

                    {/* Sidebar - Booking */}
                    <div style={{ position: 'sticky', top: '5rem' }}>
                        <BookingForm restaurantId={id} />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Restaurant;