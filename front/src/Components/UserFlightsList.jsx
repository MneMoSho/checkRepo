import React, { useState, useEffect } from 'react';
import FlightService from '../ServicesAPI/FlightServiceAPI'; // Make sure the path is correct
import UserFlightsListItem from './UserFlightsListItem';

const UserFlightsList = ({ user }) => {
  const [userFlights, setUserFlights] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (user && user.username && user.password) {
      getFlights();
    } else {
      setError('User information is incomplete.');
    }
  }, [user]);

  const getFlights = async () => {
    try {
      const flights = await FlightService.findUserFlights(user);
      setUserFlights(flights || []);
      setError(null);
    } catch (error) {
      console.error('Error fetching user flights:', error);
      setError('Failed to load flights. Please try again later.');
      setUserFlights([]);
    }
  };

  const handleDeleteFlight = async (flightId) => {
    try {
      console.log(flightId);
      if (user) {
        const updatedUser = await FlightService.detachFlightFromUser(flightId, user);
        if (updatedUser && updatedUser.flights) {
          setUserFlights(updatedUser.flights);
        } else {
          setUserFlights(userFlights.filter(flight => flight.id !== flightId));
        }
      } else {
        setError('User information is missing.');
      }
    } catch (error) {
      console.error('Error detaching flight:', error);
    }
  };

  return (
    <div className="citiesListBig">
      {error ? (
        <p className="error">{error}</p>
      ) : userFlights.length > 0 ? (
        userFlights.map((flight, index) => (
          <UserFlightsListItem
            key={flight.id || index}
            flight={flight}
            user={user}
            onDelete={handleDeleteFlight}
          />
        ))
      ) : (
        <p>No flights found.</p>
      )}
    </div>
  );
};

export default UserFlightsList;