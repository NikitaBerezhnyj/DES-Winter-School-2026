import { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { BarChart3, Users, TrendingUp, Activity } from "lucide-react";
import Header from "../components/Header";
import { useAuth } from "../context/AuthContext";
import Error from "./Error";
import LoadingSpinner from "../components/LoadingSpinner";
import { PERIOD_STORAGE_KEY, PERIOD_STANDARD_VALUE } from "../constants/constants";
import { getDashboardReport } from "../api/dashboard";
import { getGa4Properties, selectGa4Property } from "../api/analytics";
import "../styles/pages/Dashboard.css";

const EMPTY_REPORT = {
  metrics: {
    sessions: 0,
    users: 0,
    conversions: 0,
    eventCount: 0,
    engagementTime: 0,
    trafficSource: {}
  },
  aiReport: {
    summary: "",
    explanation: "",
    recommendation: ""
  }
};

export default function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();

  const [period, setPeriod] = useState(PERIOD_STANDARD_VALUE);
  const [report, setReport] = useState(EMPTY_REPORT);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [serverError, setServerError] = useState(false);

  const [properties, setProperties] = useState([]);
  const [selectedPropertyId, setSelectedPropertyId] = useState(null);
  const [propertiesLoading, setPropertiesLoading] = useState(true);

  useEffect(() => {
    const storedPeriod = localStorage.getItem(PERIOD_STORAGE_KEY);
    if (storedPeriod) {
      setPeriod(Number(storedPeriod));
    } else {
      localStorage.setItem(PERIOD_STORAGE_KEY, PERIOD_STANDARD_VALUE);
    }

    const ga4Status = searchParams.get("ga4");
    if (ga4Status === "error") {
      const reason = searchParams.get("reason") || "unknown";
      setError(`Не вдалося підключити GA4: ${reason}`);
      setSearchParams({}, { replace: true });
    }
    if (ga4Status === "success") {
      setSearchParams({}, { replace: true });
    }
  }, []);

  useEffect(() => {
    const loadProperties = async () => {
      setPropertiesLoading(true);
      try {
        const props = await getGa4Properties();
        setProperties(props);

        if (props.length === 0) {
          setError("Не знайдено жодної GA4 property.");
          setPropertiesLoading(false);
          return;
        }

        const savedId = user?.selectedPropertyId;
        const initialId =
          savedId && props.find(p => p.propertyId === savedId) ? savedId : props[0].propertyId;

        setSelectedPropertyId(initialId);

        if (!savedId || savedId !== initialId) {
          await selectGa4Property(initialId);
        }
      } catch (err) {
        if (err.message?.includes("401") || err.message?.includes("GA4 account not connected")) {
          navigate("/login", { replace: true });
          return;
        }
        setError("Не вдалося завантажити список GA4 properties");
      } finally {
        setPropertiesLoading(false);
      }
    };

    loadProperties();
  }, []);

  useEffect(() => {
    if (!selectedPropertyId) return;
    localStorage.setItem(PERIOD_STORAGE_KEY, period);
    loadDashboard(period);
  }, [period, selectedPropertyId]);

  const handlePropertyChange = async newPropertyId => {
    if (newPropertyId === selectedPropertyId) return;
    try {
      await selectGa4Property(newPropertyId);
      setSelectedPropertyId(newPropertyId);
    } catch (err) {
      setError("Не вдалося вибрати property");
      console.log("Select property error: ", err);
    }
  };

  const loadDashboard = async selectedPeriod => {
    setLoading(true);
    setError(null);
    setServerError(false);

    try {
      const data = await getDashboardReport(selectedPeriod);
      console.log("Dashboard data:", data);
      setReport(data);
    } catch (err) {
      console.error("Failed to load dashboard:", err);

      let errMessage = err.message;
      let errCode = null;

      try {
        const parsed = JSON.parse(err.message);
        errMessage = parsed.message;
        errCode = parsed.code;
      } catch (err) {
        console.error("Failed to get error:", err);
      }

      if (errCode === "INTERNAL_SERVER_ERROR" || err?.status >= 500) {
        setServerError(true);
        return;
      }

      setError(errMessage || "Не вдалося завантажити звіт");
      setReport(EMPTY_REPORT);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = async () => {
    try {
      await logout();
    } catch (err) {
      console.error("Logout failed:", err);
    }
  };

  if (serverError) return <Error code={500} />;

  const metrics = report?.metrics;
  const ai = report?.aiReport;

  return (
    <>
      <div className="dashboard">
        <Header isLoggedIn={true} onAction={handleLogout} />
        <section className="dashboard-content">
          <div className="container dashboard-content-inner">
            <div className="welcome-user">
              {user?.pictureUrl && (
                <img src={user.pictureUrl} alt={user.name} className="user-avatar" />
              )}
              <div>
                <h2>Вітаю, {user?.name || "користувач"}!</h2>
                <p>{user?.email}</p>
              </div>
            </div>

            {error && <div className="dashboard-error">{error}</div>}

            <div className="dashboard-filters">
              <div className="dashboard-period">
                <label htmlFor="property" className="period-label">
                  Property:
                </label>
                <select
                  id="property"
                  value={selectedPropertyId ?? ""}
                  onChange={e => handlePropertyChange(e.target.value)}
                  className="period-select"
                  disabled={propertiesLoading || properties.length === 0}
                >
                  {propertiesLoading ? (
                    <option>Завантаження...</option>
                  ) : (
                    properties.map(prop => (
                      <option key={prop.propertyId} value={prop.propertyId}>
                        {prop.displayName} ({prop.propertyId})
                      </option>
                    ))
                  )}
                </select>
              </div>

              <div className="dashboard-period">
                <label htmlFor="period" className="period-label">
                  Період:
                </label>
                <select
                  id="period"
                  value={period}
                  onChange={e => {
                    const newPeriod = Number(e.target.value);
                    if (newPeriod !== period) setPeriod(newPeriod);
                  }}
                  className="period-select"
                >
                  <option value={7}>7 днів</option>
                  <option value={30}>30 днів</option>
                </select>
              </div>
            </div>

            <div className="stats-grid">
              <div className="hover-card stat-card">
                <BarChart3 className="stat-icon" />
                <div className="stat-info">
                  <span className="stat-label">Сеанси</span>
                  <strong className="stat-value">{metrics.sessions.toLocaleString()}</strong>
                </div>
              </div>

              <div className="hover-card stat-card">
                <Users className="stat-icon" />
                <div className="stat-info">
                  <span className="stat-label">Користувачі</span>
                  <strong className="stat-value">{metrics.users.toLocaleString()}</strong>
                </div>
              </div>

              <div className="hover-card stat-card">
                <TrendingUp className="stat-icon" />
                <div className="stat-info">
                  <span className="stat-label">Конверсія</span>
                  <strong className="stat-value">{metrics.conversions.toLocaleString()} %</strong>
                </div>
              </div>

              <div className="hover-card stat-card">
                <Activity className="stat-icon" />
                <div className="stat-info">
                  <span className="stat-label">Події</span>
                  <strong className="stat-value">{metrics.eventCount.toLocaleString()}</strong>
                </div>
              </div>

              <div className="hover-card stat-card">
                <Activity className="stat-icon" />
                <div className="stat-info">
                  <span className="stat-label">Час взаємодії</span>
                  <strong className="stat-value">
                    {Math.round(metrics.engagementTime / 60).toLocaleString()} хв
                  </strong>
                </div>
              </div>

              {Object.entries(metrics.trafficSource ?? {}).map(([channel, users]) => (
                <div key={channel} className="hover-card stat-card">
                  <BarChart3 className="stat-icon" />
                  <div className="stat-info">
                    <span className="stat-label">{channel}</span>
                    <strong className="stat-value">{(users ?? 0).toLocaleString()}</strong>
                  </div>
                </div>
              ))}
            </div>

            <div className="hover-card analytics-report">
              <h2>ШІ Підсумки</h2>
              <div className="analytics-content">
                <h3>Підсумки</h3>
                <p>{ai.summary || "—"}</p>
                <hr />
                <h3>Пояснення</h3>
                <p>{ai.explanation || "—"}</p>
                <hr />
                <h3>Рекомендації</h3>
                <p>{ai.recommendation || "—"}</p>
              </div>
            </div>
          </div>
        </section>
      </div>
      {loading && <LoadingSpinner />}
    </>
  );
}
