import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import config from '../config.js';
import '../styles/Auth.css';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`${config.API_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password })
            });

            if (response.ok) {
                const data = await response.json();
                // Сохраните токены в localStorage или context
                localStorage.setItem('accessToken', data.accessToken);
                localStorage.setItem('refreshToken', data.refreshToken);
                navigate('/'); // Перенаправление на домашнюю страницу после успешного входа
            } else {
                setError('Неверные учетные данные');
            }
        } catch (error) {
            setError('Ошибка при входе в систему');
        }
    };

    return (
        <div className="auth_container">
            <h2 className="auth_header">ВХОД</h2>
            {error && <p className="error">{error}</p>}
            <form onSubmit={handleLogin} className="auth_form">
                <div className="auth_field">
                    <label className="auth_label">Email:</label>
                    <input className="auth_input" type="email" value={email} onChange={(e) => setEmail(e.target.value)}
                           required/>
                </div>
                <div className="auth_field">
                    <label className="auth_label">Пароль:</label>
                    <input className="auth_input" type="password" value={password}
                           onChange={(e) => setPassword(e.target.value)} required/>
                </div>
                <div className="auth_field">
                    <button type="submit" className="auth_button">Войти</button>
                </div>
            </form>
            <p className="auth_link-label">Нет аккаунта? <a className="auth_link" href="/register">Зарегистрироваться</a></p>
        </div>
    );
};

export default Login;