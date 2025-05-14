import React, { useState, useEffect } from 'react';
import cl from './ModalWindowStyles.module.css';
import FlightService from '../ServicesAPI/FlightServiceAPI';

const FlightInputModal = ({ visible, setVisible, onSubmit }) => {
    const rootClasses = [cl.modalPage];
    const [flightData, setFlightData] = useState({
        startDestination: "",
        endDestination: "",
        timeLeaving: "",
        timeArriving: "",
        price: "",
        company: ""
    });

    useEffect(() => {
        if (visible) {
            document.body.style.overflow = 'hidden';
        } else {
            document.body.style.overflow = '';
        }

        return () => {
            document.body.style.overflow = '';
        };
    }, [visible]);

    const handleInputChange = (field) => (event) => {
        setFlightData({ ...flightData, [field]: event.target.value });
    };

    const handleSubmit = () => {
        FlightService.createNewFlight(flightData)
            .then(response => {
                console.log("Flight added successfully:", response);
            })
            .catch(error => {
                console.error("Error adding flight:", error);
            });
        setVisible(false);
    };

    const handleClose = () => {
        setFlightData({
            startDestination: "",
            endDestination: "",
            timeLeaving: "",
            timeArriving: "",
            price: "",
            company: "",
            length: "",
        });
        setVisible(false);
    };

    if (visible) {
        rootClasses.push(cl.active);
    }

    return (
        <div className={rootClasses.join(' ')}>
            <div className={cl.myModalContent}>
                <div className="commonPhrase">Enter Flight Information</div>
                <div>
                    <input
                        className="registerUserInput"
                        placeholder="Start Destination"
                        value={flightData.startDestination}
                        onChange={handleInputChange('startDestination')}
                    />
                </div>
                <div>
                    <input
                        className="registerUserInput"
                        placeholder="End Destination"
                        value={flightData.endDestination}
                        onChange={handleInputChange('endDestination')}
                    />
                </div>
                <div>
                    <input
                        className="registerUserInput"
                        placeholder="Time Leaving"
                        value={flightData.timeLeaving}
                        onChange={handleInputChange('timeLeaving')}
                    />
                </div>
                <div>
                    <input
                        className="registerUserInput"
                        placeholder="Time Arriving"
                        value={flightData.timeArriving}
                        onChange={handleInputChange('timeArriving')}
                    />
                </div>
                <div>
                    <input
                        className="registerUserInput"
                        placeholder="Price"
                        value={flightData.price}
                        onChange={handleInputChange('price')}
                    />
                </div>

                <div>
                    <input
                        className="registerUserInput"
                        placeholder="Company"
                        value={flightData.company}
                        onChange={handleInputChange('company')}
                    />
                </div>

                <div>
                    <input
                        className="registerUserInput"
                        placeholder="Country"
                        value={flightData.country}
                        onChange={handleInputChange('country')}
                    />
                </div>

                <div>
                    <input
                        className="registerUserInput"
                        placeholder="length"
                        value={flightData.length}
                        onChange={handleInputChange('length')}
                    />
                </div>

                <div className="buttonContainer">
                    <div className="Button" onClick={handleSubmit}>
                        Submit
                    </div>
                    <div className="Button" onClick={handleClose}>
                        Close
                    </div>
                </div>
            </div>
        </div>
    );
};

export default FlightInputModal;