import { useState } from "react";
import { GoogleLogin } from "@react-oauth/google";
import ThemeToggle from "../components/ThemeToggle";
import { jwtDecode } from "jwt-decode";
import "../styles/login.css";
// import { loginWithGoogle } from "../api/auth";

export default function Login({ onLogin }) {
  const [loading, setLoading] = useState(false);

  const handleSuccess = async credentialResponse => {
    setLoading(true);

    try {
      const userData = jwtDecode(credentialResponse.credential);

      // ⬇️ Тимчасово: фронтенд-логін
      onLogin({
        email: userData.email,
        name: userData.name,
        picture: userData.picture
      });

      // ⬇️ Коли backend буде готовий
      /*
      const res = await loginWithGoogle(credentialResponse.credential);
      onLogin(res.user);
      */
    } catch (err) {
      console.error("Google login failed", err);
      setLoading(false);
    }
  };

  const handleError = () => {
    console.error("Google login error");
    setLoading(false);
  };

  return (
    <div className="login-page">
      <ThemeToggle />

      <div className="login-card">
        <h1 className="login-title">MetricMind</h1>
        <p className="login-subtitle">Увійдіть, щоб переглянути аналітику вашого сайту</p>

        <GoogleLogin onSuccess={handleSuccess} onError={handleError} useOneTap />

        {loading && <p className="login-loading">Завантаження...</p>}
      </div>
    </div>
  );
}
