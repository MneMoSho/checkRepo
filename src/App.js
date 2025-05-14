import { BrowserRouter, Route, Routes } from "react-router-dom";
import React, { useState, useEffect } from "react";
import MainPage from "./Pages/MainPage";
import Flights from "./Pages/Flights";
import { AuthContext } from "./Context";
import UserFlights from "./Pages/UserFlights"
import CountryPage from "./Pages/CountryPage";
import FindByCity from "./Components/FindByCity";

function App() {
  const [isAuth, setIsAuth] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    // Load user data from localStorage on app load
    const savedUser = localStorage.getItem("currentUser");
    if (savedUser) {
      setCurrentUser(JSON.parse(savedUser));
      setIsAuth(true);
    }
  }, []);

  const handleLogin = (user) => {
    setCurrentUser(user);
    setIsAuth(true);
    localStorage.setItem("currentUser", JSON.stringify(user)); // Save user to localStorage
  };

  const handleLogout = () => {
    setCurrentUser(null);
    setIsAuth(false);
    localStorage.removeItem("currentUser"); // Remove user from localStorage
  };

  return (
    <AuthContext.Provider
      value={{
        isAuth,
        setIsAuth,
        currentUser,
        handleLogin,
        handleLogout,
      }}
    >
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/flights/" element={<Flights />} />
          <Route path="/userFlights" element={<UserFlights />} />
          <Route path="/countryFlights" element={<CountryPage />} />
          <Route path="/flightsByCities" element={<FindByCity />} />
        </Routes>
      </BrowserRouter>
    </AuthContext.Provider>
  );
}
export default App;