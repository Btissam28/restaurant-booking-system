import { Link } from 'react-router-dom';
import { Star, MapPin, Clock } from 'lucide-react';

const RestaurantCard = ({ restaurant }) => {
    return (
        <Link to={`/restaurant/${restaurant.id}`} className="card" style={{ display: 'flex', flexDirection: 'column', height: '100%', textDecoration: 'none', color: 'inherit' }}>
            <div style={{ height: '200px', backgroundColor: '#e9ecef', position: 'relative' }}>
                {/* Placeholder for image - using gradient or service if available */}
                <img
                    src={`https://source.unsplash.com/800x600/?restaurant,${restaurant.typeCuisine}`}
                    alt={restaurant.nom}
                    style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                    onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = 'https://via.placeholder.com/800x600?text=Restaurant';
                    }}
                />
                <div style={{
                    position: 'absolute',
                    top: '1rem',
                    right: '1rem',
                    backgroundColor: 'white',
                    padding: '0.25rem 0.5rem',
                    borderRadius: 'var(--radius-md)',
                    fontWeight: 'bold',
                    display: 'flex',
                    alignItems: 'center',
                    gap: '0.25rem',
                    boxShadow: 'var(--shadow-sm)'
                }}>
                    <Star size={16} fill="var(--warning)" color="var(--warning)" />
                    <span>{restaurant.noteMoyenne}</span>
                </div>
            </div>

            <div style={{ padding: '1.5rem', flex: 1, display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'start' }}>
                    <h3 style={{ fontSize: '1.25rem', fontWeight: 600 }}>{restaurant.nom}</h3>
                    <span style={{
                        fontSize: '0.875rem',
                        padding: '0.25rem 0.75rem',
                        backgroundColor: 'var(--surface-alt)',
                        borderRadius: 'var(--radius-full)',
                        color: 'var(--text-secondary)'
                    }}>
                        {restaurant.typeCuisine}
                    </span>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
                    <MapPin size={16} />
                    <span style={{ whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{restaurant.adresse}</span>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
                    <Clock size={16} />
                    <span>{restaurant.heureOuverture} - {restaurant.heureFermeture}</span>
                </div>

                <div style={{ marginTop: 'auto', paddingTop: '1rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <span style={{ fontWeight: 600, color: 'var(--text-main)' }}>
                        ~{restaurant.prixMoyen}€ <span style={{ fontWeight: 400, color: 'var(--text-light)' }}>/ pers</span>
                    </span>
                    <span style={{ color: 'var(--primary)', fontWeight: 500 }}>Voir détails →</span>
                </div>
            </div>
        </Link>
    );
};

export default RestaurantCard;
