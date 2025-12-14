import { Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Home from './pages/Home';
import Restaurant from './pages/Restaurant';
import MyBookings from './pages/MyBookings';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Home />} />
        <Route path="restaurant/:id" element={<Restaurant />} />
        <Route path="bookings" element={<MyBookings />} />
      </Route>
    </Routes>
  );
}

export default App;
