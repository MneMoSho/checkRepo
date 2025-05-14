import React from "react";
import RegisterUser from '../Components/RegisterUser'
import ModalWindow from './ModalWindowStyles.css'

const RegisterWindow = (visible, setVisible) => {

    const rootClasses = [ModalWindow.modalPage]

    if(visible) {
        rootClasses.push(ModalWindow.active);
    }

    return(
        <div className={rootClasses.join(' ')}>
            <div className = {ModalWindow.myModalContent}>
            <RegisterUser setInfo = {"enterName"}/>
            </div>
        </div>
    )
}

export default RegisterWindow