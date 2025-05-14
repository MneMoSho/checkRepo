import React, { useState, useEffect } from 'react'
import '@fontsource/just-me-again-down-here';
import '@fontsource/inter';
import '../styles/App.css'
import China from '../images/China.jpg'
import Russia from '../images/Russia.jpg'
import Japan from '../images/Japan.jpg'
import Georgia from '../images/Georgia.jpeg'
import FlightService from '../ServicesAPI/FlightServiceAPI';
import DataInput from '../Components/DataInput'
import CountryCard from '../Components/CountryCard'
import FlightsList from '../Components/FlightsList'
import { useNavigate } from 'react-router-dom';
import RegisterUserIcon from '../Components/RegisterUserIcon';
import RegisterUserWindow from '../modalWindows/RegisterUserWindow'
import UserMenu from '../modalWindows/UserMenu';
import Footer from '../Components/Footer';
import DatePickerComponent from '../Components/DatePickerComponent';

function MainPage() {
  const [destination, setDestination] = useState({
    timeLeaving: "",
    timeArriving: "",
    startDestination: "",
    endDestination: ""
  });

  const [flights, seFlights] = useState([]);
  const [noFlightsFound, setNoFlightsFound] = useState(false);

  const [modal, setModal] = useState(false);
  const [userMenuVisible, setUserMenuVisible] = useState(false);
  const [validateInputs, setValidateInputs] = useState(false);
  const navigate = useNavigate()

  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      setCurrentUser(JSON.parse(savedUser));
    }
  }, []);

  const getFlightFromAPI = async () => {
    setValidateInputs(true);
    console.log(destination.timeLeaving);
    if (
      !destination.timeLeaving ||
      !destination.startDestination ||
      !destination.endDestination ||
      !destination.timeArriving
    ) {
      return;
    }

    const leavingDate = new Date(destination.timeLeaving);
    const arrivingDate = new Date(destination.timeArriving);

    if (leavingDate < arrivingDate) {
      alert("The leaving time cannot be earlier than the arriving time.");
      return;
    }

    const response = await FlightService.findFlight(destination);
    if (response.length === 0) {
      alert("No flights found for the selected criteria.");
      setNoFlightsFound(true);
    } else {
      setNoFlightsFound(false);
      seFlights(response);
      navigate('/flights', { state: { flights: response } });
    }
  };

  const handleProfileClick = () => {
    if (currentUser) {
      setUserMenuVisible(true);
    } else {
      setModal(true);
    }
  };

  const handleLogin = (user) => {
    setCurrentUser(user);
    localStorage.setItem('currentUser', JSON.stringify(user)); 
  };

  const handleLogout = () => {
    setCurrentUser(null);
    localStorage.removeItem('currentUser');
  };

  const handleCountryClick = (countryName) => {
    navigate('/countryFlights', { state: { selectedCountry: countryName, user: currentUser } });
  };

  const handleQuestionMarkClick = () => {
    // Placeholder for future functionality
  };

  return (
    <div className="App">
      <div className="header">

        <div onClick={() => handleProfileClick(true)}>
        <RegisterUserIcon username={currentUser?.username} />
        </div>
        <div className="questionMark">
          <h1>?</h1>
          <div className="smallCylinder"></div>
        </div>
        <div className="titleName">
          <div className="SiteName">Fly me</div>
          <div className="SiteNameLow">to the moon</div>
        </div>
      </div>

      <div className="choosingDir">

        <div className="Squares">
          <div className='Squares top-right'></div>
          <div className='Squares bottom-left'></div>
        </div>

        <div className="bestTickets"><span id="line1">Best</span> <span id="line2">tickets</span></div>
        <div className="forBestPrice">For the best price</div>
        <div className="dataInput">
          <div className="whenTo">
            <DatePickerComponent
              placeholder="I'm arriving at"
              value={destination.timeArriving}
              onChange={(event) => setDestination({ ...destination, timeArriving: event.target.value })}
              showError={validateInputs && !destination.timeArriving}
            />
          </div>

          <div className="where">
            <DataInput
              fillName="going from"
              value={destination.startDestination}
              onChange={(event) => setDestination({ ...destination, startDestination: event.target.value })}
              showError={validateInputs && !destination.startDestination}
            />
          </div>

          <div className="where">
            <DataInput
              fillName="going to"
              value={destination.endDestination}
              onChange={(event) => setDestination({ ...destination, endDestination: event.target.value })}
              showError={validateInputs && !destination.endDestination}
            />
          </div>

          <div className="whenFrom">
            <DatePickerComponent
              placeholder="I'm leaving at"
              value={destination.timeLeaving}
              onChange={(event) => setDestination({ ...destination, timeLeaving: event.target.value })}
              showError={validateInputs && !destination.timeLeaving}
            />
          </div>
        </div>

        <div className="findButton" onClick={getFlightFromAPI}>
          <div className="find">
            search
          </div>
        </div>
      </div>

      <div className="mostPopularDir">

        <div className="mostPopularContainer">
          <CountryCard countryName="Japan" image={Japan} onClick={() => handleCountryClick('Japan')} />
          <CountryCard countryName="Russia" image={Russia} onClick={() => handleCountryClick('Russia')} />
          <CountryCard countryName="China" image={China} onClick={() => handleCountryClick('China')} />
          <CountryCard countryName="Georgia" image={Georgia} onClick={() => handleCountryClick('Georgia')} />
        </div>

      </div>

      <div className="mostPopularCitites">
       <div className="titleCities">The most popular routes</div> 
        <div className="citiesContainer">
          <FlightsList />
        </div>
      </div>

      <Footer />
      <RegisterUserWindow visible={modal} setVisible={setModal} onRegisterSuccess={handleLogin}></RegisterUserWindow>
      {currentUser && (
        <UserMenu 
          visible={userMenuVisible} 
          setVisible={setUserMenuVisible} 
          user={currentUser}
          onLogout={handleLogout}
        />
      )}
    </div>

  );
}

export default MainPage;