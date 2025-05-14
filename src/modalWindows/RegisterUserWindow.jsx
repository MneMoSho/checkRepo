import React, { useState } from 'react'
import RegisterUser from '../Components/RegisterUser'
import cl from './ModalWindowStyles.module.css'
import NewUser from './NewUser';
import FlightService from '../ServicesAPI/FlightServiceAPI';

const RegisterUserWindow = ({ visible, setVisible, onRegisterSuccess }) => {
    const rootClasses = [cl.modalPage]
    const [modalNew, setModalNew] = useState(false);
    const [error, setError] = useState(null);
    const [user, setUser] = useState({
        password: "",
        username: "",
    });

    const checkUserFromApi = async () => {
        try {
            const response = await FlightService.findUser(user);
            if (response.error) {
                throw new Error(response.error);
            }
            setUser(user);
            setError(null); 
            setVisible(false);
            onRegisterSuccess(user);
        } catch (error) {
            console.error("Authorization error:", error);
            setError("Incorrect name or password");
        }
    }

    const handleInputChange = (field) => (event) => {
        setUser({ ...user, [field]: event.target.value });
        setError(null);
    }

    if (visible) {
        rootClasses.push(cl.active)
    }

    return (
        <>
            <div className={rootClasses.join(' ')}>
                <div className={`${cl.myModalContent} ${error ? cl.errorBorder : ''}`}>
                    <div className="commonPhrase">Hello, Welcome!</div>
                    
                    {error && <div className={cl.errorText}>{error}</div>}
                    
                    <RegisterUser 
                        setInfo={"enterName"}
                        value={user.username}
                        onChange={handleInputChange('username')}
                        hasError={!!error}
                    />

                    <RegisterUser 
                        setInfo={"enter password"}
                        value={user.password}
                        onChange={handleInputChange('password')}
                        hasError={!!error}
                    />
                    
                    <div className="buttonContainer">
                        <div className="Button" onClick={checkUserFromApi}>
                            enter
                        </div>
                        <div className="Button" onClick={() => { setVisible(false); setModalNew(true) }}>
                            register now
                        </div>
                        <div className="Button" onClick={() => {setVisible(false); setError(null)}}>
                            close
                        </div>
                    </div>
                </div>
            </div>
            <NewUser visible={modalNew} setVisible={setModalNew} />
        </>
    );
};
export default RegisterUserWindow