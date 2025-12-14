import { useState } from 'react';
import { Mail, Calendar, Clock, MapPin, Search } from 'lucide-react';
import { reservationService } from '../services/reservationService';
import { userService } from '../services/userService';
import { Link } from 'react-router-dom';

const MyBookings = () => {
    const [email, setEmail] = useState('');
    const [reservations, setReservations] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setReservations(null);

        try {
            // 1. Get User ID
            const user = await userService.getByEmail(email);
            if (!user) {
                throw new Error("Aucun utilisateur trouvé avec cet email.");
            }

            // 2. Get Reservations
            const data = await reservationService.getUserReservations(user.id);

            // Fetch restaurant details for each reservation if needed, 
            // but ReservationResponseDTO usually contains basics. 
            // Actually, looking at ReservationResponseDTO it has restaurantId but maybe not name?
            // Let's assume we might need to fetch names or the DTO has it. 
            // Checking DTO... it has `restaurantId` but NOT `restaurantName`.
            // We should fetch restaurant names or just show ID. 
            // To be nice, let's fetch restaurant details for each.

            const enricheddata = await Promise.all(data.map(async (res) => {
                try {
                    // We can't import restaurantService here easily due to circular deps if not careful? 
                    // No, we can.
                    const resData = await import('../services/restaurantService').then(m => m.restaurantService.getById(res.restaurantId));
                    return { ...res, restaurantName: resData.nom, restaurantCuisine: resData.typeCuisine };
                } catch (e) {
                    return { ...res, restaurantName: 'Restaurant Inconnu' };
                }
            }));

            setReservations(enricheddata);

        } catch (err) {
            console.error(err);
            setError(err.message || "Erreur lors de la récupération des réservations.");
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = async (id) => {
        if (!window.confirm("Voulez-vous vraiment annuler cette réservation ?")) return;

        try {
            await reservationService.cancel(id);
            // Refresh
            const updated = reservations.map(r => r.id === id ? { ...r, status: 'CANCELLED' } : r);
            setReservations(updated);
        } catch (e) {
            alert("Erreur lors de l'annulation");
        }
    };

    return (
        <div className="container mt-4">
            <div style={{ maxWidth: '800px', margin: '0 auto' }}>
                <h1 style={{ marginBottom: '2rem' }}>Mes Réservations</h1>

                <div className="card" style={{ padding: '2rem', marginBottom: '2rem' }}>
                    <form onSubmit={handleSearch} style={{ display: 'flex', gap: '1rem', alignItems: 'flex-end' }}>
                        <div style={{ flex: 1 }}>
                            <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '0.5rem', fontWeight: 500 }}>
                                Retrouver mes réservations par Email
                            </label>
                            <div style={{ position: 'relative' }}>
                                <Mail size={18} style={{ position: 'absolute', left: '1rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-light)' }} />
                                <input
                                    type="email"
                                    required
                                    placeholder="votre@email.com"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    style={{
                                        width: '100%',
                                        padding: '1rem 1rem 1rem 3rem',
                                        borderRadius: 'var(--radius-md)',
                                        border: '1px solid var(--surface-alt)',
                                        backgroundColor: 'var(--background)'
                                    }}
                                />
                            </div>
                        </div>
                        <button type="submit" disabled={loading} className="btn btn-primary" style={{ padding: '1rem 2rem' }}>
                            {loading ? 'Recherche...' : 'Rechercher'}
                        </button>
                    </form>

                    {error && (
                        <div style={{ marginTop: '1rem', color: 'var(--danger)' }}>
                            {error}
                        </div>
                    )}
                </div>

                {reservations && (
                    <div style={{ display: 'grid', gap: '1rem' }}>
                        {reservations.length === 0 ? (
                            <div className="text-center" style={{ color: 'var(--text-secondary)', padding: '2rem' }}>
                                Aucune réservation trouvée pour cet email.
                            </div>
                        ) : (
                            reservations.map(booking => (
                                <div key={booking.id} className="card" style={{ padding: '1.5rem', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                    <div>
                                        <h3 style={{ fontSize: '1.25rem', marginBottom: '0.5rem' }}>
                                            {booking.restaurantName}
                                            {booking.status === 'CANCELLED' && <span style={{ fontSize: '0.8rem', color: 'var(--danger)', marginLeft: '1rem', border: '1px solid var(--danger)', padding: '0.2rem 0.5rem', borderRadius: '4px' }}>ANNULÉE</span>}
                                            {booking.status === 'CONFIRMED' && <span style={{ fontSize: '0.8rem', color: 'var(--success)', marginLeft: '1rem', border: '1px solid var(--success)', padding: '0.2rem 0.5rem', borderRadius: '4px' }}>CONFIRMÉE</span>}
                                        </h3>
                                        <div style={{ display: 'flex', gap: '1.5rem', color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                                <Calendar size={16} />
                                                <span>{new Date(booking.reservationDateTime).toLocaleDateString()}</span>
                                            </div>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                                <Clock size={16} />
                                                <span>{new Date(booking.reservationDateTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                                            </div>
                                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                                <span>{booking.numberOfGuests} pers.</span>
                                            </div>
                                        </div>
                                    </div>

                                    {booking.status !== 'CANCELLED' && (
                                        <div style={{ display: 'flex', gap: '1rem' }}>
                                            <Link to={`/restaurant/${booking.restaurantId}`} className="btn btn-outline" style={{ padding: '0.5rem 1rem', fontSize: '0.9rem' }}>
                                                Voir Restaurant
                                            </Link>
                                            <button
                                                onClick={() => handleCancel(booking.id)}
                                                className="btn btn-outline"
                                                style={{ padding: '0.5rem 1rem', fontSize: '0.9rem', color: 'var(--danger)', borderColor: 'var(--danger)' }}
                                            >
                                                Annuler
                                            </button>
                                        </div>
                                    )}
                                </div>
                            ))
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default MyBookings;
