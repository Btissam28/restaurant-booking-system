import { useState } from 'react';
import { Calendar, Clock, Users, User, Mail, Phone, CheckCircle, AlertCircle, Plus, Minus } from 'lucide-react';
import { restaurantService } from '../services/restaurantService';
import { reservationService } from '../services/reservationService';
import { userService } from '../services/userService';

const BookingForm = ({ restaurantId }) => {
    const [step, setStep] = useState(1);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const [debugLogs, setDebugLogs] = useState([]);

    const [bookingData, setBookingData] = useState({
        date: '',
        time: '',
        guests: 2,
        name: '',
        email: '',
        phone: '',
        requests: ''
    });

    const addDebugLog = (message) => {
        setDebugLogs(prev => [...prev, `${new Date().toLocaleTimeString()}: ${message}`]);
        console.log(`🔍 ${message}`);
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBookingData(prev => ({
            ...prev,
            [name]: name === 'guests' ? parseInt(value) || 1 : value
        }));
        setError(null);
    };

    const handleGuestsChange = (increment) => {
        setBookingData(prev => {
            const newGuests = prev.guests + increment;
            return {
                ...prev,
                guests: Math.max(1, Math.min(20, newGuests))
            };
        });
    };

    const checkAvailability = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setDebugLogs([]);

        try {
            addDebugLog('Début vérification disponibilité');

            if (!bookingData.date || !bookingData.time) {
                throw new Error("Veuillez sélectionner une date et une heure.");
            }

            const dateTime = `${bookingData.date}T${bookingData.time}:00`;
            addDebugLog(`Date/Heure: ${dateTime}, Invités: ${bookingData.guests}`);

            const response = await restaurantService.checkAvailability(restaurantId, dateTime, bookingData.guests);
            addDebugLog(`Réponse disponibilité: ${JSON.stringify(response)}`);

            if (response.available) {
                addDebugLog('Table disponible, passage à l\'étape 2');
                setStep(2);
            } else {
                setError(response.message || "Pas de disponibilité pour ce créneau.");
            }
        } catch (err) {
            addDebugLog(`Erreur vérification: ${err.message}`);
            setError(err.message || "Erreur lors de la vérification.");
        } finally {
            setLoading(false);
        }
    };

    const handleBooking = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        setDebugLogs([]);

        try {
            addDebugLog('=== DÉBUT PROCESSUS RÉSERVATION ===');
            addDebugLog(`Données: ${JSON.stringify(bookingData, null, 2)}`);

            // 1. Find or Create User
            let userId;
            try {
                addDebugLog(`Recherche utilisateur avec email: ${bookingData.email}`);
                const user = await userService.getByEmail(bookingData.email);
                addDebugLog(`Utilisateur trouvé: ${JSON.stringify(user)}`);
                userId = user.id;
            } catch (err) {
                addDebugLog(`Utilisateur non trouvé, création: ${err.message}`);

                const nameParts = bookingData.name.trim().split(' ');
                const firstName = nameParts[0] || 'Client';
                const lastName = nameParts.slice(1).join(' ') || 'Inconnu';
                const userUuid = `user_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;

                const userData = {
                    userId: userUuid,
                    firstName,
                    lastName,
                    email: bookingData.email,
                    phone: bookingData.phone || ''
                };

                addDebugLog(`Création utilisateur: ${JSON.stringify(userData)}`);
                const newUser = await userService.create(userData);
                addDebugLog(`Utilisateur créé: ${JSON.stringify(newUser)}`);
                userId = newUser.id;
            }

            addDebugLog(`ID Utilisateur: ${userId}`);
            addDebugLog(`Type ID Utilisateur: ${typeof userId}`);

            // 2. Create Reservation
            const reservationRequest = {
                restaurantId: parseInt(restaurantId),
                userId: parseInt(userId), // S'assurer que c'est un nombre
                customerName: bookingData.name,
                customerEmail: bookingData.email,
                customerPhone: bookingData.phone || '',
                reservationDateTime: `${bookingData.date}T${bookingData.time}:00`,
                numberOfGuests: parseInt(bookingData.guests),
                specialRequests: bookingData.requests || ''
            };

            addDebugLog(`Requête réservation: ${JSON.stringify(reservationRequest, null, 2)}`);

            const result = await reservationService.create(reservationRequest);
            addDebugLog(`Réservation créée: ${JSON.stringify(result)}`);

            setSuccess(true);
            setStep(3);
            addDebugLog('=== RÉSERVATION RÉUSSIE ===');

        } catch (err) {
            addDebugLog(`❌ ERREUR RÉSERVATION: ${err.message}`);
            addDebugLog(`Stack: ${err.stack}`);

            // Essayer d'obtenir plus de détails
            if (err.response) {
                addDebugLog(`Réponse erreur: ${JSON.stringify(err.response)}`);
            }

            setError(`Erreur lors de la réservation: ${err.message || 'Erreur inconnue'}`);
        } finally {
            setLoading(false);
        }
    };

    if (success) {
        return (
            <div className="card" style={{ padding: '2rem', textAlign: 'center' }}>
                <CheckCircle size={64} color="var(--success)" style={{ margin: '0 auto 1rem' }} />
                <h3 style={{ fontSize: '1.5rem', marginBottom: '1rem' }}>Réservation Confirmée !</h3>
                <p style={{ color: 'var(--text-secondary)', marginBottom: '1.5rem' }}>
                    Merci {bookingData.name}, votre table est réservée.<br />
                    Un email de confirmation a été envoyé à {bookingData.email}.
                </p>
                <button onClick={() => window.location.reload()} className="btn btn-outline">
                    Nouvelle réservation
                </button>
            </div>
        );
    }

    return (
        <div className="card" style={{ padding: '1.5rem' }}>
            <h3 style={{ fontSize: '1.25rem', fontWeight: 600, marginBottom: '1.5rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                <Calendar size={20} />
                Réserver une table
            </h3>

            {error && (
                <div style={{
                    backgroundColor: '#ffe3e6',
                    color: 'var(--danger)',
                    padding: '1rem',
                    borderRadius: 'var(--radius-md)',
                    marginBottom: '1rem'
                }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.5rem' }}>
                        <AlertCircle size={18} />
                        <strong>{error}</strong>
                    </div>

                    {debugLogs.length > 0 && (
                        <div style={{
                            fontSize: '0.8rem',
                            backgroundColor: 'rgba(0,0,0,0.05)',
                            padding: '0.5rem',
                            borderRadius: '4px',
                            maxHeight: '150px',
                            overflowY: 'auto',
                            fontFamily: 'monospace'
                        }}>
                            <strong>Logs de debug:</strong>
                            {debugLogs.map((log, index) => (
                                <div key={index} style={{ marginTop: '2px' }}>{log}</div>
                            ))}
                        </div>
                    )}
                </div>
            )}

            {step === 1 ? (
                <form onSubmit={checkAvailability} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                    <div>
                        <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '0.25rem', fontWeight: 500 }}>Date</label>
                        <div style={{ position: 'relative' }}>
                            <input
                                type="date"
                                name="date"
                                required
                                min={new Date().toISOString().split('T')[0]}
                                value={bookingData.date}
                                onChange={handleChange}
                                style={{ width: '100%', padding: '0.75rem', borderRadius: 'var(--radius-md)', border: '1px solid #ced4da' }}
                            />
                        </div>
                    </div>

                    <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                        <div>
                            <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '0.25rem', fontWeight: 500 }}>Heure</label>
                            <div style={{ position: 'relative' }}>
                                <Clock size={16} style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-light)' }} />
                                <input
                                    type="time"
                                    name="time"
                                    required
                                    value={bookingData.time}
                                    onChange={handleChange}
                                    style={{ width: '100%', padding: '0.75rem 0.75rem 0.75rem 2.25rem', borderRadius: 'var(--radius-md)', border: '1px solid #ced4da' }}
                                />
                            </div>
                        </div>
                        <div>
                            <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '0.25rem', fontWeight: 500 }}>Invités</label>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                <div style={{
                                    display: 'flex',
                                    alignItems: 'center',
                                    border: '1px solid #ced4da',
                                    borderRadius: 'var(--radius-md)',
                                    overflow: 'hidden',
                                    flex: 1
                                }}>
                                    <button
                                        type="button"
                                        onClick={() => handleGuestsChange(-1)}
                                        style={{
                                            padding: '0.75rem',
                                            background: 'var(--surface-alt)',
                                            border: 'none',
                                            cursor: 'pointer',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center'
                                        }}
                                    >
                                        <Minus size={16} />
                                    </button>

                                    <div style={{
                                        flex: 1,
                                        display: 'flex',
                                        alignItems: 'center',
                                        justifyContent: 'center',
                                        gap: '0.5rem',
                                        padding: '0.75rem',
                                        background: 'white'
                                    }}>
                                        <Users size={16} color="var(--text-light)" />
                                        <span style={{ fontWeight: 500 }}>{bookingData.guests}</span>
                                        <span style={{ color: 'var(--text-light)', fontSize: '0.9rem' }}>personne(s)</span>
                                    </div>

                                    <button
                                        type="button"
                                        onClick={() => handleGuestsChange(1)}
                                        style={{
                                            padding: '0.75rem',
                                            background: 'var(--surface-alt)',
                                            border: 'none',
                                            cursor: 'pointer',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center'
                                        }}
                                    >
                                        <Plus size={16} />
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <button type="submit" disabled={loading} className="btn btn-primary" style={{ marginTop: '0.5rem' }}>
                        {loading ? 'Vérification...' : 'Vérifier la disponibilité'}
                    </button>
                </form>
            ) : (
                <form onSubmit={handleBooking} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontSize: '0.9rem', color: 'var(--success)', padding: '0.5rem 0' }}>
                        <CheckCircle size={16} />
                        Table disponible pour {bookingData.guests} personnes le {bookingData.date} à {bookingData.time}
                    </div>

                    <div>
                        <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '0.25rem', fontWeight: 500 }}>Nom Complet</label>
                        <div style={{ position: 'relative' }}>
                            <User size={16} style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-light)' }} />
                            <input
                                type="text"
                                name="name"
                                required
                                placeholder="John Doe"
                                value={bookingData.name}
                                onChange={handleChange}
                                style={{ width: '100%', padding: '0.75rem 0.75rem 0.75rem 2.25rem', borderRadius: 'var(--radius-md)', border: '1px solid #ced4da' }}
                            />
                        </div>
                    </div>

                    <div>
                        <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '0.25rem', fontWeight: 500 }}>Email</label>
                        <div style={{ position: 'relative' }}>
                            <Mail size={16} style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-light)' }} />
                            <input
                                type="email"
                                name="email"
                                required
                                placeholder="john@example.com"
                                value={bookingData.email}
                                onChange={handleChange}
                                style={{ width: '100%', padding: '0.75rem 0.75rem 0.75rem 2.25rem', borderRadius: 'var(--radius-md)', border: '1px solid #ced4da' }}
                            />
                        </div>
                    </div>

                    <div>
                        <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '0.25rem', fontWeight: 500 }}>Téléphone</label>
                        <div style={{ position: 'relative' }}>
                            <Phone size={16} style={{ position: 'absolute', left: '0.75rem', top: '50%', transform: 'translateY(-50%)', color: 'var(--text-light)' }} />
                            <input
                                type="tel"
                                name="phone"
                                placeholder="06 12 34 56 78"
                                value={bookingData.phone}
                                onChange={handleChange}
                                style={{ width: '100%', padding: '0.75rem 0.75rem 0.75rem 2.25rem', borderRadius: 'var(--radius-md)', border: '1px solid #ced4da' }}
                            />
                        </div>
                    </div>

                    <div>
                        <label style={{ display: 'block', fontSize: '0.9rem', marginBottom: '0.25rem', fontWeight: 500 }}>Demandes spéciales</label>
                        <textarea
                            name="requests"
                            value={bookingData.requests}
                            onChange={handleChange}
                            rows="3"
                            style={{ width: '100%', padding: '0.75rem', borderRadius: 'var(--radius-md)', border: '1px solid #ced4da', fontFamily: 'inherit' }}
                        ></textarea>
                    </div>

                    <div style={{ display: 'flex', gap: '1rem', marginTop: '0.5rem' }}>
                        <button type="button" onClick={() => setStep(1)} className="btn btn-outline" style={{ flex: 1 }}>
                            Retour
                        </button>
                        <button type="submit" disabled={loading} className="btn btn-primary" style={{ flex: 2 }}>
                            {loading ? 'Traitement...' : 'Confirmer la réservation'}
                        </button>
                    </div>
                </form>
            )}
        </div>
    );
};

export default BookingForm;