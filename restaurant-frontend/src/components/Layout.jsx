import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';

const Layout = () => {
    return (
        <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column' }}>
            <Navbar />
            <main style={{ flex: 1, paddingBottom: '3rem' }}>
                <Outlet />
            </main>
            <footer style={{
                backgroundColor: 'var(--secondary)',
                color: 'white',
                padding: '2rem 0',
                textAlign: 'center'
            }}>
                <div className="container">
                    <p>© 2024 RestoBook. Tous droits réservés.</p>
                </div>
            </footer>
        </div>
    );
};

export default Layout;
