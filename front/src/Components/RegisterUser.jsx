import React from "react";

const RegisterUser = (props) => {
    return (
        <div>
            <div className="topInfo">{props.setInfo}</div>
            <input className="registerUserInput" value={props.value} onChange={props.onChange}></input>
        </div>
    )
}

export default RegisterUser