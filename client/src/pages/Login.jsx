import { useState } from "react";
import { GoogleLogin } from "@react-oauth/google";
import ThemeToggle from "../components/ThemeToggle";
import { jwtDecode } from "jwt-decode";
import "../styles/login.css";

export default function Login({ onLogin }) {
  const [loading, setLoading] = useState(false);

  const handleSuccess = credentialResponse => {
    setLoading(true);
    try {
      const userData = jwtDecode(credentialResponse.credential);
      console.log("Google User Data:", userData);

      onLogin({
        email: userData.email,
        name: userData.name,
        picture: userData.picture
      });
    } catch (err) {
      console.error("Failed to decode token:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleError = () => {
    console.error("Login Failed");
    setLoading(false);
  };

  return (
    <div className="login-page">
      <div className="login-theme">
        <ThemeToggle />
      </div>

      <div className="login-card">
        <h1>UASmartAnalytics</h1>
        <p>Увійдіть, щоб переглянути аналітику вашого сайту</p>

        <GoogleLogin onSuccess={handleSuccess} onError={handleError} useOneTap />

        {loading && <p>Завантаження...</p>}
      </div>
    </div>
  );
}
