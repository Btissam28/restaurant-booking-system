import { Link } from 'react-router-dom';
import { UtensilsCrossed, CalendarDays, Search } from 'lucide-react';
import '../styles/global.css'; // Just to ensure vars are available

const Navbar = () => {
    return (
        <nav style={{
            backgroundColor: 'var(--surface)',
            boxShadow: 'var(--shadow-sm)',
            position: 'sticky',
            top: 0,
            zIndex: 100
        }}>
            <div className="container" style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                height: '4rem'
            }}>
                {/* Logo */}
                <Link to="/" style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '0.5rem',
                    fontWeight: 'bold',
                    fontSize: '1.25rem',
                    color: 'var(--primary)'
                }}>
                    <UtensilsCrossed size={24} />
                    <span>RestoBook</span>
                </Link>

                {/* Links */}
                <div style={{ display: 'flex', gap: '1.5rem', alignItems: 'center' }}>
                    <Link to="/" style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '0.25rem',
                        color: 'var(--text-secondary)',
                        fontWeight: 500,
                        transition: 'color var(--transition-fast)'
                    }}
                        onMouseEnter={(e) => e.target.style.color = 'var(--primary)'}
                        onMouseLeave={(e) => e.target.style.color = 'var(--text-secondary)'}
                    >
                        <Search size={18} />
                        <span>Explorer</span>
                    </Link>

                    <Link to="/bookings" className="btn btn-primary" style={{ padding: '0.5rem 1rem', fontSize: '0.9rem' }}>
                        <CalendarDays size={18} />
                        <span>Mes Réservations</span>
                    </Link>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
