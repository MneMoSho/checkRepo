import React from "react";
import { useNavigate } from 'react-router-dom';
import '../styles/App.css';

const Listitem = (props) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate('/flightsByCities', { state: { 
            startDestination: props.flight.startDestination, 
            endDestination: props.flight.endDestination, 
            country: props.flight.country 
        }});
    };

    return (
        <div className="countryRouteCard" onClick={handleClick}>
            <div className="countryCardTitle">{props.flight.country}</div>
            <div className="countryCardInfo">{props.flight.startDestination} - {props.flight.endDestination}</div>
        </div>
    );
};

export default Listitem;