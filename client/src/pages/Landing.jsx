import ThemeToggle from "../components/ThemeToggle";
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
        <div className="feature">
          <span className="feature-icon">📊</span>
          <h3>GA4 автоматизація</h3>
          <p>Жодних налаштувань — просто підключіть сайт</p>
        </div>

        <div className="feature">
          <span className="feature-icon">🤖</span>
          <h3>AI-аналітик</h3>
          <p>Розумні пояснення та рекомендації</p>
        </div>

        <div className="feature">
          <span className="feature-icon">⚡</span>
          <h3>Швидкі рішення</h3>
          <p>Менше цифр — більше користі</p>
        </div>
      </section>

      {/* ROADMAP */}
      <section className="roadmap">
        <h2 className="roadmap-title">Roadmap</h2>

        <div className="roadmap-grid">
          <div className="roadmap-item">
            <span>Q1</span>
            <h3>Інтеграція GA4</h3>
            <p>Автоматичне отримання даних</p>
          </div>

          <div className="roadmap-item">
            <span>Q2</span>
            <h3>AI-звіти</h3>
            <p>Генерація інсайтів та рекомендацій</p>
          </div>

          <div className="roadmap-item">
            <span>Q3</span>
            <h3>Командний доступ</h3>
            <p>Ролі, права та спільна робота</p>
          </div>
        </div>
      </section>

      {/* PRICING */}
      <section className="pricing">
        <h2 className="pricing-title">Тарифи</h2>

        <div className="pricing-grid">
          <div className="price-card">
            <h3>Free</h3>
            <div className="price">
              0$ <span>/ місяць</span>
            </div>
            <p>Базові метрики</p>
          </div>

          <div className="price-card popular">
            <h3>Pro</h3>
            <div className="price">
              19$ <span>/ місяць</span>
            </div>
            <p>AI-аналітика та рекомендації</p>
          </div>

          <div className="price-card">
            <h3>Business</h3>
            <div className="price">
              49$ <span>/ місяць</span>
            </div>
            <p>Командний доступ</p>
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
