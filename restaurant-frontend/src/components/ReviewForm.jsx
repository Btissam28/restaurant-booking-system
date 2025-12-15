import { useState } from 'react';
import { Star, Send } from 'lucide-react';
import { avisService } from '../services/avisService';

const ReviewForm = ({ restaurantId, onReviewAdded }) => {
    const [formData, setFormData] = useState({
        auteurNom: '',
        commentaire: '',
        note: 5
    });
    const [hoveredStar, setHoveredStar] = useState(null);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(false);

        if (!formData.auteurNom.trim() || !formData.commentaire.trim()) {
            setError('Veuillez remplir tous les champs');
            return;
        }

        setSubmitting(true);

        try {
            await avisService.ajouterAvis({
                restaurantId: parseInt(restaurantId),
                auteurNom: formData.auteurNom.trim(),
                commentaire: formData.commentaire.trim(),
                note: formData.note
            });

            setSuccess(true);
            setFormData({
                auteurNom: '',
                commentaire: '',
                note: 5
            });

            // Appeler le callback pour rafraîchir la liste des avis
            if (onReviewAdded) {
                onReviewAdded();
            }

            // Masquer le message de succès après 3 secondes
            setTimeout(() => setSuccess(false), 3000);
        } catch (err) {
            console.error('Erreur lors de l\'ajout de l\'avis:', err);
            setError(err.message || 'Une erreur est survenue lors de l\'ajout de votre avis');
        } finally {
            setSubmitting(false);
        }
    };

    const handleStarClick = (rating) => {
        setFormData({ ...formData, note: rating });
    };

    return (
        <div className="card" style={{ padding: '1.5rem', marginBottom: '2rem' }}>
            <h3 style={{ fontSize: '1.25rem', marginBottom: '1rem', fontWeight: 600 }}>
                Donner votre avis
            </h3>

            {error && (
                <div style={{
                    padding: '0.75rem',
                    backgroundColor: 'rgba(220, 53, 69, 0.1)',
                    border: '1px solid rgba(220, 53, 69, 0.3)',
                    borderRadius: 'var(--radius)',
                    color: '#dc3545',
                    marginBottom: '1rem',
                    fontSize: '0.9rem'
                }}>
                    {error}
                </div>
            )}

            {success && (
                <div style={{
                    padding: '0.75rem',
                    backgroundColor: 'rgba(40, 167, 69, 0.1)',
                    border: '1px solid rgba(40, 167, 69, 0.3)',
                    borderRadius: 'var(--radius)',
                    color: '#28a745',
                    marginBottom: '1rem',
                    fontSize: '0.9rem'
                }}>
                    Votre avis a été ajouté avec succès !
                </div>
            )}

            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '1rem' }}>
                    <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
                        Votre nom *
                    </label>
                    <input
                        type="text"
                        value={formData.auteurNom}
                        onChange={(e) => setFormData({ ...formData, auteurNom: e.target.value })}
                        placeholder="Entrez votre nom"
                        style={{
                            width: '100%',
                            padding: '0.75rem',
                            border: '1px solid var(--surface-alt)',
                            borderRadius: 'var(--radius)',
                            fontSize: '1rem',
                            backgroundColor: 'var(--surface)',
                            color: 'var(--text-primary)'
                        }}
                        required
                    />
                </div>

                <div style={{ marginBottom: '1rem' }}>
                    <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
                        Note *
                    </label>
                    <div style={{ display: 'flex', gap: '0.25rem', alignItems: 'center' }}>
                        {[1, 2, 3, 4, 5].map((rating) => (
                            <button
                                key={rating}
                                type="button"
                                onClick={() => handleStarClick(rating)}
                                onMouseEnter={() => setHoveredStar(rating)}
                                onMouseLeave={() => setHoveredStar(null)}
                                style={{
                                    background: 'none',
                                    border: 'none',
                                    cursor: 'pointer',
                                    padding: '0.25rem',
                                    display: 'flex',
                                    alignItems: 'center'
                                }}
                            >
                                <Star
                                    size={28}
                                    fill={
                                        (hoveredStar !== null && rating <= hoveredStar) ||
                                        (!hoveredStar && rating <= formData.note)
                                            ? "var(--warning)"
                                            : "none"
                                    }
                                    color={
                                        (hoveredStar !== null && rating <= hoveredStar) ||
                                        (!hoveredStar && rating <= formData.note)
                                            ? "var(--warning)"
                                            : "var(--text-light)"
                                    }
                                />
                            </button>
                        ))}
                        <span style={{ marginLeft: '0.5rem', color: 'var(--text-secondary)', fontSize: '0.9rem' }}>
                            {formData.note} / 5
                        </span>
                    </div>
                </div>

                <div style={{ marginBottom: '1.5rem' }}>
                    <label style={{ display: 'block', marginBottom: '0.5rem', fontWeight: 500 }}>
                        Votre commentaire *
                    </label>
                    <textarea
                        value={formData.commentaire}
                        onChange={(e) => setFormData({ ...formData, commentaire: e.target.value })}
                        placeholder="Partagez votre expérience..."
                        rows={4}
                        style={{
                            width: '100%',
                            padding: '0.75rem',
                            border: '1px solid var(--surface-alt)',
                            borderRadius: 'var(--radius)',
                            fontSize: '1rem',
                            fontFamily: 'inherit',
                            resize: 'vertical',
                            backgroundColor: 'var(--surface)',
                            color: 'var(--text-primary)'
                        }}
                        required
                    />
                </div>

                <button
                    type="submit"
                    disabled={submitting}
                    className="btn btn-primary"
                    style={{
                        width: '100%',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        gap: '0.5rem',
                        opacity: submitting ? 0.6 : 1,
                        cursor: submitting ? 'not-allowed' : 'pointer'
                    }}
                >
                    {submitting ? (
                        <>Envoi en cours...</>
                    ) : (
                        <>
                            <Send size={18} />
                            Publier mon avis
                        </>
                    )}
                </button>
            </form>
        </div>
    );
};

export default ReviewForm;


