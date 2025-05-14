import React, { useState, useEffect } from "react";
import Listitem from "./ListItem";
import FlightService from '../ServicesAPI/FlightServiceAPI';

const FlightsList = () => {

    const [flights, setFlights] = useState([]);

    useEffect(() => {
        getFlights();
    }, [])

    const getFlights = async () => {
        const flightsToSet = await FlightService.findAllUniqueCities();
        setFlights(flightsToSet.slice(0, 8)); // Limit to 8 elements
    }

    return (
        <div className="citiesList">
            {flights.map(flight => (
                <Listitem flight = {flight}/>
            ))}
        </div>
    );
};

export default FlightsList;