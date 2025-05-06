import axios from "axios"

export default class FlightService {

    static async findFlight(destination) {
        console.log(destination);
        const response = await axios.post('http://localhost:8080/api/flights/FindFromFront', destination);
        return response.data;
    }

    static async findMinPrice() {
        const response = await axios.get('http://localhost:8080/api/flights/findMinPrice');
        return response.data;
    }

    static async findAllUniqueCities() {
        const response = await axios.get('http://localhost:8080/api/flights/findByCountry');
        console.log(response.data);
        return response.data;
    }

    static async findUser(currentUser) {
        const response = await axios.post('http://localhost:8080/api/users/fromExisting', currentUser);
        console.log(response.data);
        return response;
    }

    static async newUser(newUser) {
        const response = await axios.post('http://localhost:8080/api/users', newUser);
        console.log(response.data);
        return response;
    }

    static async findUserFlights(currentUser) {
        console.log(currentUser.username)
        console.log(currentUser.password)
        const response = await axios.post('http://localhost:8080/api/users/getAllUserFlights', currentUser);
        console.log(response.data);
        return response.data;
    }

    static async detachFlightFromUser(flightId, user) {
        const response = await axios.post(
            `http://localhost:8080/api/users/removeUser?flightId=${flightId}`, user);
        return response.data;
    }

    static async setFlightsByCountry(destination) {
        console.log(destination);
        const response = await axios.get('http://localhost:8080/api/flights/findAllToursByCountry', {
            params: { country: destination }
        });
        return response.data;
    }

    static async bookFlight({ user, flightId }) {
        console.log(flightId);
       const response = await axios.post(
           `http://localhost:8080/api/users/saveFlightToUser?flightId=${flightId}`, user);
       return response.data;
    }

    static async findFlightByCity(flight) {
        console.log(flight);
       const response = await axios.post(
           'http://localhost:8080/api/flights/findOnlyByCities', flight);
       return response.data;
    }

    static async createNewFlight(flight) {
        console.log(flight);
       const response = await axios.post(
           'http://localhost:8080/api/flights/createNewFlight', flight);
       return response.data;
    }  
}
