import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import ThemeToggle from "./ThemeToggle";
import "../styles/components/Header.css";

export default function Header() {
  const navigate = useNavigate();

  const { user, logout } = useAuth();

  const handleClick = () => {
    if (user) {
      logout();
      navigate("/");
    } else {
      navigate("/login");
    }
  };

  return (
    <header className="header">
      <div className="header-inner container">
        <div className="header-logo">MetricMind</div>
        <div className="header-actions">
          <ThemeToggle />
          <button onClick={handleClick} className="auth-btn">
            {user ? "Вийти" : "Увійти"}
          </button>
        </div>
      </div>
    </header>
  );
}
