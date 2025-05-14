import axios from "axios";

export default class FlightService {

    static API_BASE_URL = "https://checkrepo-urlj.onrender.com"; // Убрал лишний слеш в конце

    static async findFlight(destination) {
        const response = await axios.post(`${FlightService.API_BASE_URL}/api/flights/FindFromFront`, destination);
        return response.data;
    }

    static async findMinPrice() {
        const response = await axios.get(`${FlightService.API_BASE_URL}/api/flights/findMinPrice`);
        return response.data;
    }

    static async findAllUniqueCities() {
        const response = await axios.get(`${FlightService.API_BASE_URL}/api/flights/findByCountry`);
        return response.data;
    }

    static async findUser(currentUser) {
        const response = await axios.post(`${FlightService.API_BASE_URL}/api/users/fromExisting`, currentUser);
        return response;
    }

    static async newUser(newUser) {
        const response = await axios.post(`${FlightService.API_BASE_URL}/api/users`, newUser);
        return response;
    }

    static async findUserFlights(currentUser) {
        const response = await axios.post(`${FlightService.API_BASE_URL}/api/users/getAllUserFlights`, currentUser);
        return response.data;
    }

    static async detachFlightFromUser(flightId, user) {
        const response = await axios.post(
            `${FlightService.API_BASE_URL}/api/users/removeUser?flightId=${flightId}`, user);
        return response.data;
    }

    static async setFlightsByCountry(destination) {
        const response = await axios.get(`${FlightService.API_BASE_URL}/api/flights/findAllToursByCountry`, {
            params: { country: destination }
        });
        return response.data;
    }

    static async bookFlight({ user, flightId }) {
        const response = await axios.post(
            `${FlightService.API_BASE_URL}/api/users/saveFlightToUser?flightId=${flightId}`, user);
        return response.data;
    }

    static async findFlightByCity(flight) {
        const response = await axios.post(
            `${FlightService.API_BASE_URL}/api/flights/findOnlyByCities`, flight);
        return response.data;
    }

    static async createNewFlight(flight) {
        const response = await axios.post(
            `${FlightService.API_BASE_URL}/api/flights/createNewFlight`, flight);
        return response.data;
    }
}