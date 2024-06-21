import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import config from '../config';
import '../styles/AddBookPage.css';
import Footer from "../components/Footer.jsx";
import Header from "../components/Header.jsx";

const AddBookPage = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [pages, setPages] = useState('');
    const [publishDate, setPublishDate] = useState('');
    const [genres, setGenres] = useState('');
    const [authors, setAuthors] = useState('');
    const [coverFile, setCoverFile] = useState(null);
    const [bookFile, setBookFile] = useState(null);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            // Создание книги
            const bookResponse = await fetch(`${config.API_URL}/books`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
                },
                body: JSON.stringify({
                    title,
                    description,
                    pages: parseInt(pages, 10),
                    publishDate,
                    genres: genres.split(',').map((genre) => genre.trim()),
                    authors: authors.split(',').map((author) => {
                        const [firstName, lastName] = author.trim().split(' ');
                        return { firstName, lastName };
                    }),
                }),
            });

            if (!bookResponse.ok) {
                throw new Error('Ошибка при создании книги');
            }

            const bookData = await bookResponse.json();
            const bookId = bookData.id;

            // Загрузка обложки книги
            if (coverFile) {
                const coverFormData = new FormData();
                coverFormData.append('file', coverFile);

                const coverResponse = await fetch(`${config.API_URL}/cover-files/books/${bookId}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
                    },
                    body: coverFormData,
                });

                if (!coverResponse.ok) {
                    throw new Error('Ошибка при загрузке обложки книги');
                }
            }

            // Загрузка файла книги
            if (bookFile) {
                const bookFormData = new FormData();
                bookFormData.append('file', bookFile);

                const bookFileResponse = await fetch(`${config.API_URL}/book-files/books/${bookId}`, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
                    },
                    body: bookFormData,
                });

                if (!bookFileResponse.ok) {
                    throw new Error('Ошибка при загрузке файла книги');
                }
            }

            navigate(`/books/${bookId}`);
        } catch (error) {
            console.error('Ошибка при добавлении книги:', error);
        }
    };

    return (
        <div>
            <Header />
            <div className="add-book-wrapper">
                <h2>Добавить книгу</h2>
                <form onSubmit={handleSubmit} className="add-book-form">
                    <label>
                        Название:
                        <input type="text" value={title} onChange={(e) => setTitle(e.target.value)} required />
                    </label>
                    <label>
                        Описание:
                        <textarea value={description} onChange={(e) => setDescription(e.target.value)} />
                    </label>
                    <label>
                        Страниц:
                        <input type="number" value={pages} onChange={(e) => setPages(e.target.value)} required />
                    </label>
                    <label>
                        Дата публикации:
                        <input type="date" value={publishDate} onChange={(e) => setPublishDate(e.target.value)} />
                    </label>
                    <label>
                        Жанры (через запятую):
                        <input type="text" value={genres} onChange={(e) => setGenres(e.target.value)} required />
                    </label>
                    <label>
                        Авторы (имя и фамилия через запятую):
                        <input type="text" value={authors} onChange={(e) => setAuthors(e.target.value)} required />
                    </label>
                    <label>
                        Обложка книги:
                        <input type="file" onChange={(e) => setCoverFile(e.target.files[0])} />
                    </label>
                    <label>
                        Файл книги:
                        <input type="file" onChange={(e) => setBookFile(e.target.files[0])} />
                    </label>
                    <button type="submit">Загрузить</button>
                </form>
            </div>
            <Footer />
        </div>
    );
};

export default AddBookPage;