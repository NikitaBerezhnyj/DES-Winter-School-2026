import { useNavigate } from "react-router-dom";
import { AlertTriangle } from "lucide-react";
import Header from "../components/Header";
import "../styles/pages/Error.css";

export default function Error({ code = 404, title, message, showHomeButton = true }) {
  const navigate = useNavigate();

  const defaultContent = {
    404: {
      title: "Сторінку не знайдено",
      message: "Можливо, ви перейшли за неправильним посиланням або сторінка була видалена."
    },
    403: {
      title: "Доступ заборонено",
      message: "У вас немає прав для перегляду цієї сторінки."
    },
    500: {
      title: "Помилка сервера",
      message: "Щось пішло не так. Спробуйте пізніше."
    }
  };

  const content = defaultContent[code] || {
    title: title || "Сталася помилка",
    message: message || "Невідома помилка."
  };

  return (
    <div className="error-page">
      <Header />

      <div className="error-content container">
        <div className="error-card">
          <AlertTriangle size={48} className="error-icon" />

          <span className="error-code">{code}</span>

          <h1>{content.title}</h1>

          <p>{content.message}</p>

          <div className="error-actions">
            {showHomeButton && <button onClick={() => navigate("/")}>На головну</button>}

            <button className="secondary-btn" onClick={() => navigate(-1)}>
              Назад
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
