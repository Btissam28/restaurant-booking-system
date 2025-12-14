import { Star, User } from 'lucide-react';

const ReviewList = ({ reviews }) => {
    if (!reviews || reviews.length === 0) {
        return (
            <div style={{ padding: '2rem', textAlign: 'center', color: 'var(--text-secondary)', fontStyle: 'italic' }}>
                Aucun avis pour le moment. Soyez le premier à donner votre avis !
            </div>
        );
    }

    return (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {reviews.map((review) => (
                <div key={review.id} className="card" style={{ padding: '1.5rem', boxShadow: 'none', border: '1px solid var(--surface-alt)' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.75rem' }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                            <div style={{
                                width: '40px',
                                height: '40px',
                                borderRadius: '50%',
                                backgroundColor: 'var(--surface-alt)',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                color: 'var(--text-secondary)'
                            }}>
                                <User size={20} />
                            </div>
                            <div>
                                <div style={{ fontWeight: 600 }}>{review.auteurNom}</div>
                                <div style={{ fontSize: '0.85rem', color: 'var(--text-light)' }}>
                                    {new Date(review.createdAt).toLocaleDateString()}
                                </div>
                            </div>
                        </div>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '2px' }}>
                            {[...Array(5)].map((_, i) => (
                                <Star
                                    key={i}
                                    size={16}
                                    fill={i < review.note ? "var(--warning)" : "none"}
                                    color={i < review.note ? "var(--warning)" : "var(--text-light)"}
                                />
                            ))}
                        </div>
                    </div>
                    <p style={{ color: 'var(--text-secondary)', lineHeight: 1.6 }}>
                        {review.commentaire}
                    </p>
                </div>
            ))}
        </div>
    );
};

export default ReviewList;
