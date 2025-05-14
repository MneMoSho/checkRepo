import React, { useState } from 'react'
import '../styles/App.css'

const RegisterUserIcon = ({ username }) => {
    return (
            <div className="Person">
                <div className="sphere"></div>
                <div className="body"></div>
                <div className="username">{username}</div>
            </div>
    )
}

export default RegisterUserIcon