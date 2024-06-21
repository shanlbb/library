import React, { useState } from 'react';
import config from '../config';
import '../styles/ReviewForm.css';

const ReviewForm = ({ bookId }) => {
    const [author, setAuthor] = useState('');
    const [rating, setRating] = useState(1);
    const [review, setReview] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(null);

        const reviewData = {
            author,
            rating,
            review
        };

        try {
            const response = await fetch(`${config.API_URL}/reviews/books/${bookId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(reviewData)
            });

            if (response.ok) {
                // Перезагружаем страницу для обновления списка отзывов
                window.location.reload();
            } else {
                console.error('Ошибка при отправке отзыва');
                setError('Не удалось отправить отзыв. Попробуйте позже.');
            }
        } catch (error) {
            console.error('Ошибка при отправке отзыва', error);
            setError('Не удалось отправить отзыв. Попробуйте позже.');
        }
    };

    return (
        <div className="review-form-wrapper">
            <h2 className="review-form-h2">Оставить отзыв</h2>
            <form className="review-form" onSubmit={handleSubmit}>
                <div className="review-form-group">
                    <input
                        className="review-form-input"
                        placeholder="Имя"
                        type="text"
                        id="author"
                        value={author}
                        onChange={(e) => setAuthor(e.target.value)}
                        required
                    />
                    <select
                        className="review-form-select"
                        id="rating"
                        value={rating}
                        onChange={(e) => setRating(e.target.value)}
                        required
                    >
                        {[1, 2, 3, 4, 5].map(num => (
                            <option key={num} value={num}>{num}</option>
                        ))}
                    </select>
                </div>
                <div className="review-form-group">
                    <textarea
                        className="review-form-group-textarea"
                        id="review"
                        placeholder="Отзыв"
                        value={review}
                        onChange={(e) => setReview(e.target.value)}
                    />
                </div>
                {error && <p className="error">{error}</p>}
                <button className="review-form-button" type="submit">Отправить</button>
            </form>
        </div>
    );
};

export default ReviewForm;