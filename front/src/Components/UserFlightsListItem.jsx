import React from 'react';
import '../styles/FlightPage.css';

const UserFlightsListItem = ({ flight, user, onDelete }) => {
  return (
    <div className="userFlightCard">
      <div className="userFlightDetails">
        <p className="countries">
          Flight from {flight.startDestination} to {flight.endDestination} by <span className="company">{flight.flightCompany}</span>
        </p>
        <p className="flightBar"></p>
      </div>
      <div className="userFlightPrice">Price: ${flight.price}</div>
      <button className="userFlightDeleteButton" onClick={() => onDelete(flight.id)}>Delete</button>
    </div>
  );
};

export default UserFlightsListItem;