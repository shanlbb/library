import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import {Link, useNavigate} from 'react-router-dom';
import '../styles/BookCard.css';
import config from '../config';

const BookCard = ({ book }) => {
    const [coverUrl, setCoverUrl] = useState(null);

    useEffect(() => {
        const fetchCover = async () => {
            try {
                const response = await fetch(`${config.API_URL}/cover-files/books/${book.id}`);
                if (response.ok) {
                    const fileData = await response.json();
                    setCoverUrl(fileData.fileUrl);
                } else {
                    console.error('Ошибка загрузки обложки книги');
                }
            } catch (error) {
                console.error('Ошибка загрузки обложки книги', error);
            }
        };

        fetchCover();
    }, [book.id]);

    const navigate = useNavigate();

    const handleCardClick = () => {
        navigate(`/books/${book.id}`);
    };


    return (
        <div className="book-card">
            <div onClick={handleCardClick} className="book-cover">
                {coverUrl ? (
                    <img src={coverUrl} alt="Обложка книги" className="book-cover-img" />
                ) : (
                    <div className="book-cover-placeholder">Обложка недоступна</div>
                )}
            </div>
            <div className="book-info">
                <p onClick={handleCardClick} className="book-title">{book.title}</p>
                <p className="book-authors">
                    {book.authors.map(author => (
                        <Link
                            className="book-author"
                            key={author.id}
                            to={`/books?authorId=${author.id}&sort=downloads,desc`}
                        >
                            {author.firstName} {author.lastName}
                        </Link>
                    )).reduce((prev, curr) => [prev, ', ', curr])}
                </p>
                <div className="book-info_line">
                    <svg width="16" height="16" fill="#ffc72c">
                        <path fillRule="evenodd" clipRule="evenodd"
                              d="M8.486 13.376a1 1 0 00-.973 0l-2.697 1.502a1 1 0 01-1.474-1.033l.543-3.35a1 1 0 00-.27-.855L1.277 7.225a1 1 0 01.566-1.684l3.136-.483a1 1 0 00.754-.566l1.361-2.921a1 1 0 011.813 0l1.362 2.921a1 1 0 00.754.566l3.136.483a1 1 0 01.566 1.684l-2.34 2.415a1 1 0 00-.269.856l.542 3.349a1 1 0 01-1.473 1.033l-2.698-1.502z"></path>
                    </svg>
                    <p className="book-rating">{book.rating}</p>
                    <Link
                        className="book-user"
                        to={`/books?username=${book.username}&sort=downloads,desc`}
                    >
                        {book.username}
                    </Link>
                </div>
                <p onClick={handleCardClick} className="book-description">{book.description ? `${book.description.slice(0, 300)}...` : 'Описание отсутствует'}</p>
            </div>
        </div>
    );
};

BookCard.propTypes = {
    book: PropTypes.shape({
        id: PropTypes.string.isRequired,
        title: PropTypes.string.isRequired,
        description: PropTypes.string,
        authors: PropTypes.arrayOf(PropTypes.shape({
            id: PropTypes.string.isRequired,
            firstName: PropTypes.string.isRequired,
            lastName: PropTypes.string.isRequired
        })).isRequired,
        username: PropTypes.string.isRequired,
        rating: PropTypes.number.isRequired
    }).isRequired
};

export default BookCard;