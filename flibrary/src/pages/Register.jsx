import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import config from '../config.js';
import '../styles/Auth.css';

const Register = () => {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setError('Пароли не совпадают');
            return;
        }

        try {
            const response = await fetch(`${config.API_URL}/auth/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, username, password, confirmPassword })
            });

            if (response.ok) {
                const data = await response.json();
                // Сохраните токены в localStorage или context
                localStorage.setItem('accessToken', data.accessToken);
                localStorage.setItem('refreshToken', data.refreshToken);
                navigate('/'); // Перенаправление на домашнюю страницу после успешной регистрации
            } else {
                setError('Ошибка при регистрации');
            }
        } catch (error) {
            setError('Ошибка при регистрации');
        }
    };

    return (
        <div className="auth_container">
            <h2 className="auth_header">РЕГИСТРАЦИЯ</h2>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleRegister} className="auth_form">
                <div className="auth_field">
                    <label className="auth_label">Email:</label>
                    <input className="auth_input" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                </div>
                <div className="auth_field">
                    <label className="auth_label">Имя пользователя:</label>
                    <input className="auth_input" type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
                </div>
                <div className="auth_field">
                    <label className="auth_label">Пароль:</label>
                    <input className="auth_input" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </div>
                <div className="auth_field">
                    <label className="auth_label">Подтвердите пароль:</label>
                    <input className="auth_input" type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required />
                </div>
                <div className="auth_field">
                    <button type="submit" className="auth_button">Зарегистрироваться</button>
                </div>
            </form>
            <p className="auth_link-label">Уже есть аккаунт? <a className="auth_link" href="/login">Войти</a></p>
        </div>
    );
};

export default Register;