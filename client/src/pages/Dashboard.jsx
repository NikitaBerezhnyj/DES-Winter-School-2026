import ThemeToggle from "../components/ThemeToggle";
import "../styles/dashboard.css";

export default function Dashboard({ user }) {
  return (
    <div className="dashboard-page">
      {/* HEADER */}
      <header className="dashboard-header">
        <div className="dashboard-header-inner">
          <strong>MetricMind</strong>
          <ThemeToggle />
        </div>
      </header>

      {/* CONTENT */}
      <main className="dashboard-content">
        <p className="welcome">
          Ласкаво просимо, <b>{user.email}</b>
        </p>

        <section className="metrics-grid">
          <Metric icon="👥" label="Користувачі" value="12 340" />
          <Metric icon="📈" label="Сеанси" value="28 912" />
          <Metric icon="💰" label="Дохід" value="$4 560" />
          <Metric icon="⚡" label="Конверсія" value="3.4%" />
        </section>

        <section className="ai-card">
          <h3>🤖 AI-аналітика</h3>
          <p>
            За останні 30 днів трафік зріс на <b>18%</b>. Найкраще працюють мобільні користувачі —
            рекомендуємо оптимізувати сторінки під них.
          </p>
        </section>
      </main>
    </div>
  );
}

function Metric({ icon, label, value }) {
  return (
    <div className="metric-card">
      <span className="metric-label">{label}</span>
      <strong className="metric-value">{value}</strong>
      <span className="metric-icon">{icon}</span>
    </div>
  );
}
