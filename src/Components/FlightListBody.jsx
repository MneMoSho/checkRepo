import FlightPage from '../styles/FlightPage.css'
import React, { useState, useEffect } from "react";
import { useLocation } from 'react-router-dom';
import FlightListComponent from '../Components/FlightListComponent'

const FlightListBody = ({ flights, user }) => {

  const location = useLocation();
  const { flights: locationFlights } = location.state || {};

  console.log(locationFlights || flights);

  return (
    <div className="mainBackground"> 
      <div className="mainBody">
        {(locationFlights || flights).map(flight => (
          <FlightListComponent flight={flight} user={user} />
        ))}
      </div>
    </div>
  );
};

export default FlightListBody