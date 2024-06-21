import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import BooksPage from "./pages/BooksPage.jsx";
import BookPage from "./pages/BookPage.jsx";
import AddBookPage from "./pages/AddBookPage.jsx";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/books" element={<BooksPage />}/>
                <Route path="/books/:id" element={<BookPage />} />
                <Route path="/add" element={<AddBookPage />} />
            </Routes>
        </Router>
    );
};

export default App;