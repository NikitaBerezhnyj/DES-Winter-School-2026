import { BarChart3, Brain, Zap, Database, Sparkles, ShieldCheck } from "lucide-react";
import { useNavigate } from "react-router-dom";
import "../styles/pages/Landing.css";
import Header from "../components/Header";

export default function Landing() {
  const navigate = useNavigate();

  const handleOnStartClick = () => {
    navigate("/dashboard");
  };

  return (
    <div className="landing">
      <Header />

      <section className="hero">
        <div className="container">
          <div className="hero-content">
            <div className="hero-badge">
              <Sparkles size={14} />
              <span>AI-powered Analytics</span>
            </div>

            <h1 className="title">
              Перетворюємо дані GA4
              <br />
              на бізнес-рішення
            </h1>

            <p className="subtitle">
              Автоматична аналітика веб-трафіку з AI-рекомендаціями.
              <br />
              Без складних налаштувань — просто підключіть і отримуйте інсайти.
            </p>

            <div className="hero-actions">
              <button onClick={handleOnStartClick}>Почати роботу</button>
            </div>
          </div>
        </div>
      </section>

      <section className="features">
        <div className="container">
          <h2 className="features-title">Features</h2>
          <div className="features-grid">
            <div className="hover-card feature">
              <BarChart3 className="feature-icon" />
              <h3>GA4 автоматизація</h3>
              <p>Жодних налаштувань — просто підключіть сайт</p>
            </div>
            <div className="hover-card feature">
              <Brain className="feature-icon" />
              <h3>AI-аналітика</h3>
              <p>Розумні пояснення та рекомендації</p>
            </div>
            <div className="hover-card feature">
              <Zap className="feature-icon" />
              <h3>Швидкі рішення</h3>
              <p>Менше цифр — більше користі</p>
            </div>
          </div>
        </div>
      </section>

      <section className="roadmap">
        <div className="container">
          <h2 className="roadmap-title">Roadmap</h2>
          <div className="roadmap-grid">
            <div className="hover-card roadmap-item">
              <Database className="feature-icon" />
              <span className="roadmap-quarter">Q1</span>
              <h3>Інтеграція GA4</h3>
              <p>Автоматичне отримання даних</p>
            </div>
            <div className="hover-card roadmap-item">
              <Sparkles className="feature-icon" />
              <span className="roadmap-quarter">Q2</span>
              <h3>AI-звіти</h3>
              <p>Генерація інсайтів та рекомендацій</p>
            </div>
            <div className="hover-card roadmap-item">
              <ShieldCheck className="feature-icon" />
              <span className="roadmap-quarter">Q3</span>
              <h3>Система доступу</h3>
              <p>Контроль можливостей (без командного доступу)</p>
            </div>
          </div>
        </div>
      </section>

      <footer className="footer">
        <div className="footer-content">
          <div>
            <strong>Про проєкт</strong>
            <p>MetricMind — AI-аналітика для бізнесу</p>
          </div>
          <div className="footer-contacts">
            <strong>Контакти</strong>
            <p>support@metricmind.app</p>
          </div>
        </div>
      </footer>
    </div>
  );
}
