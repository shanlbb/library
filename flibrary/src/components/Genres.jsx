import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../styles/Genres.css';
import config from '../config';

const Genres = () => {
    const [genres, setGenres] = useState([]);

    useEffect(() => {
        const fetchGenres = async () => {
            try {
                const response = await fetch(`${config.API_URL}/genres/top?page=0&pageSize=20`);
                if (response.ok) {
                    const data = await response.json();
                    setGenres(data.content);
                } else {
                    console.error('Ошибка загрузки популярных жанров');
                }
            } catch (error) {
                console.error('Ошибка загрузки популярных жанров', error);
            }
        };

        fetchGenres();
    }, []);

    return (
        <div className="genres-container">
            <p className="genres_header">Жанры</p>
            {genres.map((genre) => (
                <Link
                    key={genre.id}
                    className="genre_link"
                    to={`/books?genreId=${genre.id}&sort=downloads,desc`}
                >
                    {genre.name}
                </Link>
            ))}
        </div>
    );
};

export default Genres;