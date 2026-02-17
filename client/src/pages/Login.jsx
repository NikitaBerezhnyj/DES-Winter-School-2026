import { useState } from "react";
import { useRef, useEffect } from "react";
import { GoogleLogin } from "@react-oauth/google";
import { useNavigate } from "react-router-dom";
import ThemeToggle from "../components/ThemeToggle";
import LoadingSpinner from "../components/LoadingSpinner";
import { login } from "../api/auth";
import "../styles/pages/Login.css";
import { ArrowLeft } from "lucide-react";
import { getGa4AuthorizeUrl } from "../api/analytics";

export default function Login({ onLogin }) {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const cardRef = useRef(null);
  const isDragging = useRef(false);
  const offset = useRef({ x: 0, y: 0 });

  useEffect(() => {
    if (!cardRef.current) return;

    const card = cardRef.current;
    const rect = card.getBoundingClientRect();

    const left = (window.innerWidth - rect.width) / 2;
    const top = (window.innerHeight - rect.height) / 2;

    card.style.left = `${left}px`;
    card.style.top = `${top}px`;

    const handleMouseMove = e => {
      if (!isDragging.current || !cardRef.current) return;

      const newLeft = e.clientX - offset.current.x;
      const newTop = e.clientY - offset.current.y;

      const card = cardRef.current;
      const rect = card.getBoundingClientRect();

      const maxLeft = window.innerWidth - rect.width;
      const maxTop = window.innerHeight - rect.height;

      card.style.left = `${Math.max(0, Math.min(newLeft, maxLeft))}px`;
      card.style.top = `${Math.max(0, Math.min(newTop, maxTop))}px`;
      card.style.transform = "none";
    };

    const handleMouseUp = () => {
      isDragging.current = false;
      cardRef.current?.classList.remove("dragging");
    };

    window.addEventListener("mousemove", handleMouseMove);
    window.addEventListener("mouseup", handleMouseUp);

    return () => {
      window.removeEventListener("mousemove", handleMouseMove);
      window.removeEventListener("mouseup", handleMouseUp);
    };
  }, []);

  const handleOnBackClick = () => {
    navigate("/");
  };

  const handleGoogleSuccess = async credentialResponse => {
    if (!credentialResponse?.credential) {
      setError("Failed to get Google credential");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await login(credentialResponse.credential);
      console.log("Login successful:", response);

      onLogin(response.user);

      if (response.user.hasGa4Access) {
        navigate("/dashboard");
      } else {
        const authUrl = await getGa4AuthorizeUrl();

        window.location.href = authUrl;
      }
    } catch (err) {
      console.error("Login/GA4 auth failed:", err);
      setError(err.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleError = () => {
    console.error("Google Login Failed");
    setError("Google login failed. Please try again.");
  };

  return (
    <>
      <div className="login-page">
        <div className="login-theme">
          <ThemeToggle />
        </div>

        <div className="login-card hover-card" ref={cardRef}>
          <button onClick={handleOnBackClick} className="login-back">
            <ArrowLeft size={18} />
          </button>

          <div
            className="login-accent"
            onMouseDown={e => {
              if (!cardRef.current) return;

              isDragging.current = true;
              cardRef.current.classList.add("dragging");

              const rect = cardRef.current.getBoundingClientRect();
              offset.current = {
                x: e.clientX - rect.left,
                y: e.clientY - rect.top
              };
            }}
          />

          <h1 className="login-title">MetricMind</h1>
          <p className="login-description">
            Увійдіть через Google, щоб переглянути аналітику та AI-інсайти для вашого сайту.
          </p>

          <div className="login-provider">
            <GoogleLogin
              onSuccess={handleGoogleSuccess}
              onError={handleGoogleError}
              useOneTap
              disabled={loading}
            />
          </div>

          {error && <p className="login-error">{error}</p>}
        </div>
      </div>

      {loading && <LoadingSpinner />}
    </>
  );
}
