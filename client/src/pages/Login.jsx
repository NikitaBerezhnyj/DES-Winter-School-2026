import { useState } from "react";
import ThemeToggle from "../components/ThemeToggle";
import "../styles/login.css";
// import { loginWithGoogle } from "../api/auth";

export default function Login({ onLogin }) {
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {
    setLoading(true);

    // ⏳ MOCK — заміниться на OAuth
    setTimeout(() => {
      onLogin({ email: "user@metricmind.app" });
    }, 1000);

    /*
    const res = await loginWithGoogle(token);
    onLogin(res.user);
    */
  };

  return (
    <div className="login-page">
      <ThemeToggle />

      <div className="login-card">
        <h1>MetricMind</h1>
        <p>Увійдіть, щоб переглянути аналітику</p>

        <button onClick={handleLogin} disabled={loading}>
          {loading ? "Завантаження..." : "Увійти через Google"}
        </button>
      </div>
    </div>
  );
}
