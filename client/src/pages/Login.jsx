import { useState } from "react";
import ThemeToggle from "../components/ThemeToggle";
import "../styles/login.css";

export default function Login({ onLogin }) {
  const [loading, setLoading] = useState(false);

  const handleLogin = () => {
    setLoading(true);

    // 🔧 Тимчасовий mock-login (до backend OAuth)
    setTimeout(() => {
      onLogin({ email: "user@metricmind.app" });
      setLoading(false);
    }, 1000);
  };

  return (
    <div className="login-page">
      <ThemeToggle />

      <div className="login-card">
        <h1>MetricMind</h1>
        <p>Увійдіть, щоб переглянути аналітику</p>

        <button className="login-button" onClick={handleLogin} disabled={loading}>
          {loading ? "Завантаження..." : "Увійти через Google"}
        </button>
      </div>
    </div>
  );
}
