import { BarChart3, Users, TrendingUp, Activity } from "lucide-react";
import "../styles/dashboard.css";

export default function Dashboard({ user }) {
  return (
    <div className="dashboard-page">
      {/* HEADER */}
      <header className="dashboard-header">
        <h1 className="dashboard-title">Dashboard</h1>
        <span className="dashboard-user">{user?.email}</span>
      </header>

      {/* STATS */}
      <section className="dashboard-stats">
        <div className="hover-card stat-card">
          <BarChart3 className="stat-icon" />
          <div className="stat-content">
            <span className="stat-label">Сеанси</span>
            <strong className="stat-value">12 430</strong>
          </div>
        </div>

        <div className="hover-card stat-card">
          <Users className="stat-icon" />
          <div className="stat-content">
            <span className="stat-label">Користувачі</span>
            <strong className="stat-value">3 280</strong>
          </div>
        </div>

        <div className="hover-card stat-card">
          <TrendingUp className="stat-icon" />
          <div className="stat-content">
            <span className="stat-label">Конверсія</span>
            <strong className="stat-value">4.8%</strong>
          </div>
        </div>

        <div className="hover-card stat-card">
          <Activity className="stat-icon" />
          <div className="stat-content">
            <span className="stat-label">Активність</span>
            <strong className="stat-value">Висока</strong>
          </div>
        </div>
      </section>

      {/* PLACEHOLDER */}
      <section className="dashboard-placeholder hover-card">
        <h2>Аналітика скоро</h2>
        <p>Тут зʼявляться AI-звіти, графіки та рекомендації після інтеграції з backend</p>
      </section>
    </div>
  );
}
