import React from "react";
import '../styles/Footer.css';
import test from '../images/image.png';

const Footer = () => {
    return (
        <div className="footer">
            <div className="left">
                <img src={test} alt="Test Icon" className="footerIcon" />
                Fly me
            </div>
            <div className="center">to the moon</div>
            <div className="right">and let me play among the stars</div>
        </div>
    );
};

export default Footer;