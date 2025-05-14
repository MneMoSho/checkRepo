import React from "react";
import '../styles/App.css';

const DataInput = (props) => {
    return (
      <input
        className={`inputTime ${props.showError ? "empty" : ""}`}
        placeholder={props.fillName}
        value={props.value}
        onChange={props.onChange}
      />
    );
  };
  
  export default DataInput;