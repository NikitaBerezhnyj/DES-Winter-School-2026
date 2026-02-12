import { useState } from "react";
import { GoogleLogin } from "@react-oauth/google";
import ThemeToggle from "../components/ThemeToggle";
import { jwtDecode } from "jwt-decode";
import "../styles/login.css";

export default function Login({ onLogin }) {
  const [loading, setLoading] = useState(false);
  const [googleError, setGoogleError] = useState(false);

  const handleGoogleSuccess = credentialResponse => {
    if (!credentialResponse?.credential) {
      setGoogleError(true);
      return;
    }

    setLoading(true);

    try {
      console.log("Google Credential Response:", credentialResponse);
      const userData = jwtDecode(credentialResponse.credential);

      onLogin({
        email: userData.email,
        name: userData.name,
        picture: userData.picture
      });
    } catch (err) {
      console.error("Failed to decode Google token:", err);
      setGoogleError(true);
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleError = () => {
    console.error("Google Login Failed");
    setGoogleError(true);
  };

  const handleMockLogin = () => {
    setLoading(true);

    setTimeout(() => {
      onLogin({
        email: "user@metricmind.app",
        name: "Test User",
        picture: null
      });

      setLoading(false);
    }, 1000);
  };

  return (
    <div className="login-page">
      <div className="login-theme">
        <ThemeToggle />
      </div>

      <div className="login-card">
        <h1>MetricMind</h1>
        <p>Увійдіть, щоб переглянути аналітику вашого сайту</p>

        <GoogleLogin onSuccess={handleGoogleSuccess} onError={handleGoogleError} useOneTap />

        {loading && <p className="login-loading">Завантаження...</p>}

        {googleError && (
          <button className="login-button" onClick={handleMockLogin} disabled={loading}>
            {loading ? "Завантаження..." : "Увійти (тимчасовий режим)"}
          </button>
        )}
      </div>
    </div>
  );
}
