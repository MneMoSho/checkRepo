import React from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { format, isValid, parse } from "date-fns";

const DatePickerComponent = ({ value, onChange, placeholder, showError }) => {
  const handleDateChange = (date) => {
    if (date && isValid(date)) {
      onChange({ target: { value: format(date, "dd-MM-yyyy") } });
    } else {
      onChange({ target: { value: "" } });
    }
  };

  const parsedDate = value
    ? parse(value, "dd-MM-yyyy", new Date())
    : null;

  return (
    <DatePicker
      className={`inputTime ${showError ? "empty" : ""}`}
      placeholderText={placeholder}
      selected={parsedDate && isValid(parsedDate) ? parsedDate : null}
      onChange={handleDateChange}
      dateFormat="dd-MM-yyyy"
      showPopperArrow={false}
      preventOpenOnFocus
      onFocus={(e) => e.target.blur()}
      customInput={<input readOnly />}
    />
  );
};

export default DatePickerComponent;