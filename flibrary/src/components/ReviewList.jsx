import React, { useState, useEffect } from 'react';
import config from '../config';
import '../styles/ReviewList.css';

const ReviewList = ({ bookId }) => {
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        const fetchReviews = async () => {
            try {
                const response = await fetch(`${config.API_URL}/reviews/books/${bookId}?page=0&size=20&sort=createdAt,desc`);
                if (response.ok) {
                    const data = await response.json();
                    setReviews(data.content);
                } else {
                    console.error('Ошибка загрузки отзывов');
                }
            } catch (error) {
                console.error('Ошибка загрузки отзывов', error);
            }
        };

        fetchReviews();
    }, [bookId]);

    return (
        <div className="reviews-list">
            <h2 className="review-list-h2">Отзывы</h2>
            {reviews.length > 0 ? (
                reviews.map((review, index) => (
                    <div key={index} className="review">
                        <div className="review-line">
                            <p className="review-author">{review.author}</p>
                            <p className="review-rating">Оценка: {review.rating}</p>
                        </div>
                        <div className="review-horizontal"></div>
                        <p className="review-review">{review.review}</p>
                        <div className="review-horizontal-2"></div>
                        <p className="review-date">{new Date(review.createdAt).toLocaleDateString()}</p>
                    </div>
                ))
            ) : (
                <p></p>
            )}
        </div>
    );
};

export default ReviewList;