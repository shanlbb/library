import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";

const Home = () => {
    const navigate = useNavigate();

    useEffect(() => {
        navigate('/books?sort=downloads,desc');
    }, [navigate]);

    return (
        <div>
            <Header />
            <main>
            </main>
            <Footer />
        </div>
    );
};

export default Home;