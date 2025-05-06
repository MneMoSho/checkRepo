import React from "react";
import Header from '../Components/Header';
import { useLocation, useNavigate } from 'react-router-dom';
import '../styles/App.css';
import UserFlightsList from "../Components/UserFlightsList";
import '../styles/FlightPage.css'; // Импортируем файл стилей для этой страницы

const Flights = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const user = location.state?.user;

  return (
    <div>
      <Header />
      <div className="userFlights">
        <div className="fieldForInput">
          <div className="newPhrase"> Your Flights</div>
          <UserFlightsList user={user} />
          <div onClick={() => navigate('/')} className="Button">Back to Main Page</div>
        </div>
      </div>
    </div>
  );
};
export default Flights;