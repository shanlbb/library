import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom'; // Импорт Link
import config from '../config.js';
import '../styles/Header.css';

const Header = () => {
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState('');
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        setIsAuthenticated(!!token); // Проверяем, есть ли токен
    }, []);

    const handleSearch = (e) => {
        e.preventDefault();
        navigate(`/books?searchTerms=${encodeURIComponent(searchQuery)}&sort=downloads,desc`);
    };

    const handleLogout = async () => {
        try {
            const response = await fetch(`${config.API_URL}/auth/logout`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`
                }
            });

            if (response.ok) {
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                setIsAuthenticated(false);
                navigate('/');
            } else {
                console.error('Logout failed');
            }
        } catch (error) {
            console.error('Error during logout:', error);
        }
    };

    return (
        <header>
            <div className="header-all">
                <div className="header-top">
                    <div className="logo">
                        <Link to="/" className="logo_link"> {/* Используем Link здесь */}
                            <img src="/images/logo.png" alt="logo" className="logo_img"/>
                        </Link>
                    </div>
                    <form className="search-form" onSubmit={handleSearch}>
                        <input type="text" placeholder="Книга или автор" className="search-form_input"
                               value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)}/>
                        <button type="submit" className="search_form_button">Поиск</button>
                    </form>
                    <nav className="header-nav">
                        <Link to="/books?sort=downloads,desc" className="header-link">Библиотека</Link> {/* Используем Link здесь */}
                        {isAuthenticated ? (
                            <>
                                <Link to="/add" className="header-link">Добавить</Link> {/* Используем Link здесь */}
                                <a onClick={handleLogout} className="header-link">Выйти</a>
                            </>
                        ) : (
                            <Link to="/login" className="header-link">Войти</Link>
                            )}
                    </nav>
                </div>

                <nav className="header-bottom">
                    <Link to="/books?sort=downloads,desc" className="header-button">📖 Книги</Link> {/* Используем Link здесь */}
                    <Link to="/books?sort=publishDate,desc" className="header-button">🔥 Новинки</Link> {/* Используем Link здесь */}
                    <Link to="/books?sort=rating,desc" className="header-button">❤️ Топ книг</Link> {/* Используем Link здесь */}
                </nav>
            </div>
        </header>
    );
};

export default Header;