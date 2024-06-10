import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import Home from './pages/Home';

const App = () => {
    return (
        <Router>
            <div>
                <Header />
                <main>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        {/* Добавьте другие маршруты, если есть */}
                    </Routes>
                </main>

            </div>
        </Router>
    );
};

export default App;