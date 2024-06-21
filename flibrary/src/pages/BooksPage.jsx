import React, { useState, useEffect } from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import queryString from 'query-string';
import BookCard from '../components/BookCard';
import Genres from '../components/Genres';
import config from '../config';
import Header from "../components/Header.jsx";
import '../styles/BooksPage.css'
import Footer from "../components/Footer.jsx";

const BooksPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = queryString.parse(location.search);
    const [books, setBooks] = useState([]);
    const [totalElements, setTotalElements] = useState([]);
    const [pageTitle, setPageTitle] = useState('');
    const [sortOption, setSortOption] = useState(queryParams.sort); // Хранение выбранного варианта сортировки
    const [page, setPage] = useState(0); // Хранение текущей страницы

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                let url = `${config.API_URL}/books?`;

                // Проверяем наличие и значения параметров запроса
                if (queryParams.genreId) {
                    const genreId = queryParams.genreId;
                    const genreResponse = await fetch(`${config.API_URL}/genres/${genreId}`);
                    if (genreResponse.ok) {
                        const genreData = await genreResponse.json();
                        setPageTitle(`Все книги в жанре "${genreData.name}"`);
                    } else {
                        console.error('Ошибка загрузки жанра');
                    }
                    url += `genreId=${genreId}`;
                } else if (queryParams.authorId) {
                    const authorId = queryParams.authorId;
                    const authorResponse = await fetch(`${config.API_URL}/authors/${authorId}`);
                    if (authorResponse.ok) {
                        const authorData = await authorResponse.json();
                        setPageTitle('Все книги автора ' + `"${authorData.firstName} ${authorData.lastName}"`.toUpperCase());
                    } else {
                        console.error('Ошибка загрузки автора');
                    }
                    url += `authorId=${authorId}`;
                } else if (queryParams.username) {
                    setPageTitle(`Все книги пользователя "${queryParams.username}"`);
                    url += `username=${queryParams.username}`;
                } else if (queryParams.searchTerms) {
                    setPageTitle(`Поиск по "${queryParams.searchTerms}"`);
                    url += `searchTerms=${queryParams.searchTerms}`;
                } else {
                    setPageTitle('Все книги');
                }

                // Добавляем параметр страницы
                if (url.at(url.length - 1) === '?') {
                    url += `page=${page}&size=10`;
                } else {
                    url += `&page=${page}&size=10`;
                }
                if (queryParams.sort) {
                    url += `&sort=${queryParams.sort}`;
                }

                console.log("URL: " + url);

                const response = await fetch(url);
                if (response.ok) {
                    const data = await response.json();
                    setTotalElements(data.totalElements);
                    setBooks(data.content);
                } else {
                    console.error('Ошибка загрузки списка книг');
                }
            } catch (error) {
                console.error('Ошибка загрузки списка книг', error);
            }
        };

        fetchBooks();
    }, [location.search, queryParams.genreId, queryParams.authorId, queryParams.username, queryParams.searchTerms, sortOption, page]);

    const handleSortChange = (event) => {
        const value = event.target.value;
        delete queryParams.sort;
        const updatedParams = {...queryParams, sort: value};
        const searchString = queryString.stringify(updatedParams, { arrayFormat: 'comma' });
        navigate(`${location.pathname}?${searchString}`);
        setSortOption(value);
    };

    return (
        <div>
            <Header />
            <main>
                <h2 className="book-page_header">{pageTitle}</h2>
                <p className="count-books">Всего книг: {totalElements}</p>
                <div className="sort-selection">
                    <label htmlFor="sort" className="sort-selection_label">Сортировать </label>
                    <select id="sort" className="sort-selection_select" value={sortOption} onChange={handleSortChange}>
                        <option value="downloads,desc">По популярности</option>
                        <option value="publishDate,desc">По новизне</option>
                        <option value="rating,desc">По рейтингу</option>
                    </select>
                </div>
                <div className="book-page_container">
                    <div className="books">
                        {books.map(book => (
                            <BookCard key={book.id} book={book}/>
                        ))}
                    </div>
                    <div className="genres">
                        <Genres />
                    </div>
                </div>
            </main>
            <Footer />
        </div>
    );
};

export default BooksPage;