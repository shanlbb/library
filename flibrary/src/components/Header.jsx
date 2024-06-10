import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; // Добавьте этот импорт
import '../styles/Header.css';

const Header = () => {
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearch = (e) => {
        e.preventDefault();
        navigate(`/search?query=${encodeURIComponent(searchQuery)}`);
    };

    return (
        <header>
            <div className="header-all">
                <div className="header-top">
                    <div className="logo">
                        <a href="/" className="logo_link">
                            <img src="/images/logo.png" alt="logo" className="logo_img"/>
                        </a>
                    </div>
                    <form className="search-form" onSubmit={handleSearch}>
                        <input type="text" placeholder="Книга или автор" className="search-form_input"
                               value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)}/>
                        <button type="submit" className="search_form_button">Поиск</button>
                    </form>
                    <nav className="header-nav">
                        <a href="/" className="header-link">Библиотека</a>
                        <a href="/" className="header-link">Войти</a>
                    </nav>
                </div>

                <nav className="header-bottom">
                    <a href="/" className="header-button">📖 Книги</a>
                    <a href="/" className="header-button">🔥 Новинки</a>
                    <a href="/" className="header-button">❤️ Топ книг</a>
                </nav>
            </div>
        </header>
    );
};

export default Header;