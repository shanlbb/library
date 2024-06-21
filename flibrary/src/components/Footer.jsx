import React from 'react';
import '../styles/Footer.css'; // Добавьте стили для вашего футера

const Footer = () => {
    return (
        <footer className="footer">
            <div className="footer-gradient">

            </div>
            <div className="footer-content">
                <div className="footer-left">
                    <p className="footer-name">LIBRARY</p>
                    <p className="footer-year">©2024</p>
                </div>
                <div className="footer-center">
                    <a className="footer-link" href="https://github.com/shanlbb/library">
                        <img className="footer-img" src="../images/git.png"/>
                    </a>
                    <a className="footer-link" href="mailto:library.tsu@gmail.com">
                        <img className="footer-img" src="../images/mail.png"/>
                    </a>
                    <a className="footer-link" href="t.me/library_tsu">
                        <img className="footer-img" src="../images/tg.png"/>
                    </a>
                </div>
                <div className="footer-right">

                </div>
            </div>
        </footer>
    );
};

export default Footer;