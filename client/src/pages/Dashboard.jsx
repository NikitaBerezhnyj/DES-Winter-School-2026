import ThemeToggle from "../components/ThemeToggle";

import "../styles/layout.css";
import "../styles/dashboard.css";

export default function Dashboard({ user }) {
  if (!user) return null;

  return (
    <div className="app page">
      {/* HEADER */}
      <header className="app-header">
        <h1 className="logo">UASmartAnalytics</h1>
        <ThemeToggle />
      </header>

      {/* CONTENT */}
      <main className="app-content">
        <div className="container">
          <p className="welcome">
            Ласкаво просимо, <strong>{user.email}</strong>
          </p>

          {/* METRICS */}
          <section className="metrics">
            <Metric title="Користувачі" value="12 340" />
            <Metric title="Конверсія" value="3,4%" />
            <Metric title="Дохід" value="$4 560" />
          </section>

          {/* AI SECTION */}
          <section className="ai-section">
            <h3>Аналітика штучного інтелекту</h3>
            <p>
              Трафік зріс на 18% за останні 30 днів. Рекомендується зосередитись на мобільній
              оптимізації для підвищення конверсії.
            </p>
          </section>
        </div>
      </main>
    </div>
  );
}

/* ===== METRIC CARD ===== */

function Metric({ title, value }) {
  return (
    <div className="metric-card">
      <span>{title}</span>
      <strong>{value}</strong>
    </div>
  );
}
