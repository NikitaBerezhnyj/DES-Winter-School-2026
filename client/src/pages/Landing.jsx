import ThemeToggle from "../components/ThemeToggle";
import { BarChart3, Brain, Zap, Database, Sparkles, ShieldCheck } from "lucide-react";
import "../styles/landing.css";

export default function Landing({ onStart }) {
  return (
    <div className="landing-page">
      {/* HEADER */}
      <header className="landing-header">
        <div className="header-inner">
          <div className="header-logo">MetricMind</div>

          <div className="header-actions">
            <ThemeToggle />
            <button className="landing-cta" onClick={onStart}>
              Увійти
            </button>
          </div>
        </div>
      </header>

      {/* HERO */}
      <section className="landing-hero">
        <h1 className="landing-title">Аналітика нового покоління</h1>
        <p className="landing-subtitle">MetricMind перетворює складні цифри в зрозумілі інсайти</p>

        <button className="landing-cta" onClick={onStart}>
          Почати роботу
        </button>
      </section>

      {/* FEATURES */}
      <section className="landing-features">
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
      </section>

      {/* ROADMAP */}
      <section className="roadmap">
        <h2 className="roadmap-title">Roadmap</h2>

        <div className="roadmap-grid">
          <div className="hover-card roadmap-item">
            <Database className="feature-icon" />
            <span>Q1</span>
            <h3>Інтеграція GA4</h3>
            <p>Автоматичне отримання даних</p>
          </div>

          <div className="hover-card roadmap-item">
            <Sparkles className="feature-icon" />
            <span>Q2</span>
            <h3>AI-звіти</h3>
            <p>Генерація інсайтів та рекомендацій</p>
          </div>

          <div className="hover-card roadmap-item">
            <ShieldCheck className="feature-icon" />
            <span>Q3</span>
            <h3>Система доступу</h3>
            <p>Контроль можливостей (без командного доступу)</p>
          </div>
        </div>
      </section>

      {/* FOOTER */}
      <footer className="landing-footer">
        <div>
          <strong>Про проєкт</strong>
          <p>MetricMind — AI-аналітика для бізнесу</p>
        </div>

        <div>
          <strong>Контакти</strong>
          <p>support@metricmind.app</p>
        </div>
      </footer>
    </div>
  );
}
