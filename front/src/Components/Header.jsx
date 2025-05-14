import React from "react";
import { useNavigate } from 'react-router-dom';
import FlightPage from '../styles/FlightPage.css'
import test from '../images/test.png'

const Header = () => {
    const navigate = useNavigate();

    const handleTitleClick = () => {
        navigate('/');
    };

    return (
        <div className="HeaderForOtherPages">
            <img src={test} alt="Флаг страны" className="flyMeIcon" onClick={handleTitleClick} style={{ cursor: 'pointer' }} />
            <div className="titleNameForPages" onClick={handleTitleClick} style={{ cursor: 'pointer' }}>
                <div className="SiteName">Fly me</div>
                <div className="SiteNameLow">to the moon</div>
            </div>
        </div>
    )
}

export default Header