import React, { useEffect, useContext, useState } from "react";
import { useLocation } from 'react-router-dom';
import FlightService from '../ServicesAPI/FlightServiceAPI';
import Header from '../Components/Header';
import FlightListBody from "../Components/FlightListBody";
import FlightsList from '../Components/FlightsList';
import { AuthContext } from '../Context';
import Footer from '../Components/Footer';

const FindByCity = () => {
    const location = useLocation();
    const { startDestination, endDestination, country } = location.state || {};
    const { currentUser } = useContext(AuthContext);
    const [flights, setFlights] = useState([]);

    useEffect(() => {
        if (startDestination && endDestination && country && currentUser) {
            const fetchFlights = async () => {
                const response = await FlightService.findFlightByCity({ 
                    startDestination, 
                    endDestination, 
                    country, 
                    user: currentUser 
                });
                setFlights(response);
            };
            fetchFlights();
        }
    }, [startDestination, endDestination, country, currentUser]);

    return (
        <div>
            <Header />
            <h1>Flights from {startDestination} to {endDestination}</h1>
            <FlightListBody flights={flights} user={currentUser} />
            <div className="mostPopularCitites">
                       <div className="titleCities">The most popular routes</div> 
                <div className="citiesContainer">
                    <FlightsList flights={flights} />
                </div>
            </div>
            <Footer />
        </div>
    );
};

export default FindByCity;