import React from "react";
import FlightService from '../ServicesAPI/FlightServiceAPI';

const Button = ({ user, flight }) => {
    const handleClick = async () => {
        try {
            if (user?.flights?.some(userFlight => userFlight.id === flight.id)) {
                console.log("Already booked");
                return;
            }
            const response = await FlightService.bookFlight({ user, flightId: flight.id });
            console.log("Booking successful:", response);
            alert("Booking successful!"); // Added alert for successful booking
        } catch (error) {
            console.error("Error booking flight:", error);
        }
    };

    return (
        <div className="buttonToSend" onClick={handleClick}>Book</div>
    );
};

export default Button;