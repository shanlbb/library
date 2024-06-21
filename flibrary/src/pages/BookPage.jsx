import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import config from '../config';
import Header from '../components/Header';
import '../styles/BookPage.css';
import ReviewForm from "../components/ReviewForm.jsx";
import ReviewList from "../components/ReviewList.jsx";
import Footer from "../components/Footer.jsx";

const BookPage = () => {
    const { id } = useParams();
    const [book, setBook] = useState(null);
    const [coverUrl, setCoverUrl] = useState(null);
    const [loadingFile, setLoadingFile] = useState(false); // Для отслеживания состояния загрузки файла

    useEffect(() => {
        const fetchBookData = async () => {
            try {
                // Получение данных книги
                const bookResponse = await fetch(`${config.API_URL}/books/${id}`);
                if (bookResponse.ok) {
                    const bookData = await bookResponse.json();
                    setBook(bookData);
                } else {
                    console.error('Ошибка загрузки данных книги');
                }

                // Получение данных обложки
                const coverResponse = await fetch(`${config.API_URL}/cover-files/books/${id}`);
                if (coverResponse.ok) {
                    const coverData = await coverResponse.json();
                    setCoverUrl(coverData.fileUrl);
                } else {
                    console.error('Ошибка загрузки данных обложки');
                }
            } catch (error) {
                console.error('Ошибка загрузки данных', error);
            }
        };

        fetchBookData();
    }, [id]);

    const handleDownload = async () => {
        try {
            setLoadingFile(true); // Устанавливаем состояние загрузки файла в true
            const fileResponse = await fetch(`${config.API_URL}/book-files/books/${id}`);
            if (fileResponse.ok) {
                const fileData = await fileResponse.json();
                // Создаем временную ссылку для скачивания файла
                const link = document.createElement('a');
                link.href = fileData.fileUrl;
                link.download = fileData.originalName;
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            } else {
                console.error('Ошибка загрузки данных файла книги');
            }
        } catch (error) {
            console.error('Ошибка загрузки данных файла книги', error);
        } finally {
            setLoadingFile(false); // Устанавливаем состояние загрузки файла в false
        }
    };

    if (!book) {
        return <div>Загрузка...</div>;
    }

    const publishYear = new Date(book.publishDate).getFullYear();

    return (
        <div>
            <Header />
            <div className="book-page-wrapper">
                <div className="book-page-imgs-container">
                    <div className="book-page-background">
                        {coverUrl ? (
                            <img src={coverUrl} alt="Обложка книги" className="book-page-background_img"/>
                        ) : (
                            <div className="book-page-background-placeholder">Обложка недоступна</div>
                        )}
                    </div>
                    <div className="book-page-cover">
                        {coverUrl ? (
                            <img src={coverUrl} alt="Обложка книги" className="book-page-cover_img"/>
                        ) : (
                            <div className="book-page-cover-placeholder">Обложка недоступна</div>
                        )}
                    </div>
                </div>
                <div className="book-page-info">
                    <div className="book-page-rating-line">
                        <svg width="16" height="16" fill="#ffc72c">
                            <path fillRule="evenodd" clipRule="evenodd"
                                  d="M8.486 13.376a1 1 0 00-.973 0l-2.697 1.502a1 1 0 01-1.474-1.033l.543-3.35a1 1 0 00-.27-.855L1.277 7.225a1 1 0 01.566-1.684l3.136-.483a1 1 0 00.754-.566l1.361-2.921a1 1 0 011.813 0l1.362 2.921a1 1 0 00.754.566l3.136.483a1 1 0 01.566 1.684l-2.34 2.415a1 1 0 00-.269.856l.542 3.349a1 1 0 01-1.473 1.033l-2.698-1.502z"></path>
                        </svg>
                        <p className="book-page-rating">{book.rating}</p>
                    </div>
                    <h1 className="book-page-title">{book.title}</h1>
                        {book.authors.map(author => (
                        <Link className="book-page-author" to={`/books?authorId=${author.id}`} key={author.id}>
                            {author.firstName} {author.lastName}
                        </Link>
                    )).reduce((prev, curr) => [prev, ', ', curr])}
                    <div className="book-page-details">
                        <p className="book-page-pages">{book.pages} печатных страниц</p>
                        <p className="book-page-date">{publishYear} год</p>
                    </div>
                    <div className="book-page-downloads">
                        <p className="book-page-downloads-message">Скачиваний: {book.downloads}</p>
                        <button onClick={handleDownload} className="book-download-button" disabled={loadingFile}>
                            {loadingFile ? 'Загрузка...' : 'Скачать книгу'}
                        </button>
                    </div>
                    <h2>О книге</h2>
                    <p className="book-page-description">{book.description || 'Описание отсутствует'}</p>
                    <h2>Жанры</h2>
                    <div className="book-page-genres">
                        {book.genres.map(genre => (
                        <Link className="book-page-genre" to={`/books?genreId=${genre.id}&sort=downloads,desc`} key={genre.id}>
                            {genre.name}
                        </Link>
                    ))}
                    </div>
                </div>
                <div className="book-page-user">
                    <h2 className="book-page-user-h2">Пользователь</h2>
                    <Link className="book-page-username" to={`/books?username=${book.username}&sort=downloads,desc`}>
                        {book.username}
                    </Link>
                </div>
            </div>
            <div className="book-page-reviews">
                <ReviewForm bookId={id} />
                <ReviewList bookId={id} />
            </div>
            <Footer />
        </div>
    );
};

export default BookPage;