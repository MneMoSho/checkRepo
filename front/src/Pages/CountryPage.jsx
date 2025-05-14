import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import Header from '../Components/Header';
import FlightsDisplay from '../Components/FlightsDisplay';
import FlightService from '../ServicesAPI/FlightServiceAPI';
import Footer from '../Components/Footer';

const CountryPage = () => {
    const location = useLocation();
    const { selectedCountry, user } = location.state || {}; // Extract user from location state
    const [flights, setFlights] = useState([]);

    useEffect(() => {
        if (user) {
            // console.log(`User Info: Username - ${user.username}, Password - ${user.password}`);
        }
    }, [user]);

    useEffect(() => {
        if (selectedCountry) {
            FlightService.setFlightsByCountry(selectedCountry).then(response => {
                setFlights(response);
            });
        }
    }, [selectedCountry]);

    return (
        <div className="countryPage">
            <Header />
            <h1 className="titleText">Welcome to {selectedCountry}</h1>
            <div className="mostPopularCountries">
                {flights.length > 0 ? (
                    <FlightsDisplay flights={flights} user={user} /> // Pass user to FlightsDisplay
                ) : (<p>Loading flights...</p>)}
            </div>
        </div>
    );
};

export default CountryPage;