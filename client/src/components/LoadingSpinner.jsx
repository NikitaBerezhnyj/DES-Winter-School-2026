import React from "react";
import "../styles/components/LoadingSpinner.css";

const LoadingSpinner = () => {
  return (
    <div className="loading-overlay">
      <div className="loading-spinner" />
    </div>
  );
};

export default LoadingSpinner;
