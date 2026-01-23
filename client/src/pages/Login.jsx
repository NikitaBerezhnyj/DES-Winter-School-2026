import { useState } from "react";
import ThemeToggle from "../components/ThemeToggle";
import "../styles/login.css";

export default function Login({ onLogin }) {
  const [loading, setLoading] = useState(false);

  const handleLogin = () => {
    if (loading) return;
    setLoading(true);

    setTimeout(() => {
      onLogin({
        email: "winterschool55@gmail.com",
        name: "Winter School User"
      });
    }, 800);
  };

  return (
    <div className="login-page">
      <div className="login-theme">
        <ThemeToggle />
      </div>

      <div className="login-card">
        <h1>UASmartAnalytics</h1>
        <p>Увійдіть, щоб переглянути аналітику вашого сайту</p>

        <button onClick={handleLogin} disabled={loading}>
          {loading ? "Завантаження..." : "Увійти через Google"}
        </button>
      </div>
    </div>
  );
}
