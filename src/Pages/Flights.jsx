import React, { useContext } from "react";
import { useLocation } from 'react-router-dom';
import { useEffect, useState } from 'react';
import Header from '../Components/Header'
import FlightListBody from "../Components/FlightListBody";
import FlightsList from '../Components/FlightsList'
import FlightService from '../ServicesAPI/FlightServiceAPI';
import { AuthContext } from '../Context';
import Footer from '../Components/Footer';
import '../styles/App.css'

const Flights = () => {
    const location = useLocation();
    const [flights, setFlights] = useState([]);
    const selectedCountry = location.state?.selectedCountry;
    const { currentUser } = useContext(AuthContext);

    useEffect(() => {
        if (selectedCountry) {
            FlightService.findFlightsByCountry(selectedCountry).then(response => {
                setFlights(response);
            });
        }
    }, [selectedCountry]);

    return (
        <div>
            <Header />
            {selectedCountry && <h2>Flights to {selectedCountry}</h2>}
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

export default Flights;