import React from "react";
import Button from "./button";
import FlightPage from '../styles/FlightPage.css';

const FlightListComponent = ({ flight, user }) => {
    const userHasFlight = user?.flights?.some(userFlight => userFlight.id === flight.id);

    return (
        <div className={`flightPassCard ${userHasFlight ? 'userHasFlight' : ''}`}>
            <div className="priceSect">
                <div className="price">{flight.price}$</div>
                <div className="bookButton">
                    <Button user={user} flight={flight} />
                </div>
            </div>

            <div className="flightInfo">
                <div className="company">{flight.company}</div>
                <div className="startAndEnd">
                    <div className="countries">{flight.startDestination}</div>
                    <div className="flightBar"></div>
                    <div className="countries">{flight.endDestination}</div>
                </div>
                <div className="timeLeaving">{flight.timeLeaving} - {flight.timeArriving}</div>
                <div className="countryFrom">{flight.country}</div>
            </div>
        </div>
    );
};

export default FlightListComponent;