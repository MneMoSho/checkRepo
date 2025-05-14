import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import FlightService from '../ServicesAPI/FlightServiceAPI';

const CountryCard = ({ countryName, image, onClick }) => {
     const [minPrice, setMinPrice] = useState(0);
     const navigate = useNavigate();

     useEffect(() => {
        findMinPrice();
    }, [])

  const findMinPrice = async () => {
      const price = await FlightService.findMinPrice();
      setMinPrice(price);
  }

  const handleClick = () => {
   onClick()
  };

    return (
        <div className="cityCard" onClick={handleClick}>
            <div className="someText">{countryName}</div>
            <img src={image} alt="Флаг страны" className="countryFlag" />
            <div className="minPrice">from {minPrice} $</div>
        </div>
    )
}

export default CountryCard