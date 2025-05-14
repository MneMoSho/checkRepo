import React, { useEffect } from 'react';
import FlightListComponent from './FlightListComponent';
import FlightPage from '../styles/FlightPage.css'

const FlightsDisplay = ({ flights, user }) => {
  useEffect(() => {
    if (user) {
      console.log(`User Info: Username - ${user.username}, Password - ${user.password}`);
    }
  }, [user]);

  return (
    <div className="mainBody">
      {flights && flights.length > 0 ? (
        flights.map((flight, index) => (
          <FlightListComponent key={index} flight={flight} user={user} />
        ))
      ) : (
        <p>No flights available.</p>
      )}
    </div>
  );
};

export default FlightsDisplay;