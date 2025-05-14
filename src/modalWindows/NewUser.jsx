import React, { useState } from 'react'
import RegisterUser from '../Components/RegisterUser'
import cl from './ModalWindowStyles.module.css'
import FlightService from '../ServicesAPI/FlightServiceAPI';

const NewUser = ({ visible, setVisible }) => {

    const rootClasses = [cl.modalPage]
        const [error, setError] = useState(null);
        const [newUser, setNewUser] = useState({
            password: "",
            username: "",
            email: ""
        });

        const handleClose = () => {
            setVisible(false);
            setError(null);
            setNewUser({
                password: "",
                username: "",
                email: ""
            });
        }

    if (visible) {
        rootClasses.push(cl.active)
    }

    const checkUserFromApi = async () => {
            try {
                const response = await FlightService.newUser(newUser);
                if (response.error) {
                    throw new Error(response.error);
                }
                handleClose();
            } catch (error) {
                console.error("Authorization error:", error);
                setError("Incorrect name or password");
            }
        }

    return (
        <div className={rootClasses.join(' ')}>
            <div className={cl.myModalContent}>
                <div className="commonPhrase">Hello, Welcome!</div>
                <RegisterUser setInfo={"enter Name"} value={newUser.username} onChange={event => setNewUser({ ...newUser, username: event.target.value })} />
                <RegisterUser setInfo={"enter Email"} value={newUser.email}  onChange={event => setNewUser({ ...newUser, email: event.target.value })} />
                <RegisterUser setInfo={"enter password"} value={newUser.password} onChange={event => setNewUser({ ...newUser, password: event.target.value })} />
                <div className="buttonContainer">
                    <div className="Button" onClick={()=>{checkUserFromApi(newUser); handleClose()}}>
                        register
                    </div>
                    <div className="Button" onClick={() =>{handleClose()}}>
                            close
                        </div>
                </div>
            </div>
        </div>
    );
};

export default NewUser