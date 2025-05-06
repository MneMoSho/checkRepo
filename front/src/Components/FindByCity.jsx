import React, { useEffect, useContext, useState } from "react";
import { useLocation } from 'react-router-dom';
import FlightService from '../ServicesAPI/FlightServiceAPI';
import Header from '../Components/Header';
import FlightListBody from "../Components/FlightListBody";
import FlightsList from '../Components/FlightsList';
import { AuthContext } from '../Context';

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
                console.log("Flights fetched:", response);
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
                <div className="citiesContainer">
                    <FlightsList flights={flights} />
                </div>
            </div>
        </div>
    );
};

export default FindByCity;