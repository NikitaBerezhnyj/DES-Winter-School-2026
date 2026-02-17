import { useEffect, useState } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Error from "./pages/Error";
import LoadingSpinner from "./components/LoadingSpinner";
import { AuthContext } from "./context/AuthContext";
import { logout, getCurrentUser } from "./api/auth";
import { THEME_STANDARD_VALUE, THEME_STORAGE_KEY } from "./constants/constants";

export default function App() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [serverError, setServerError] = useState(false);

  useEffect(() => {
    const init = async () => {
      const theme = localStorage.getItem(THEME_STORAGE_KEY) || THEME_STANDARD_VALUE;
      document.documentElement.setAttribute("data-theme", theme);

      try {
        const userData = await getCurrentUser();
        setUser(userData);
      } catch (err) {
        if (err.status === 401) {
          setUser(null);
        } else {
          setServerError(true);
          console.error("Auth check failed:", err);
        }
      } finally {
        setLoading(false);
      }
    };
    init();
  }, []);

  const handleLogout = async () => {
    await logout();
    setUser(null);
  };

  const authValue = {
    user,
    setUser,
    logout: handleLogout
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (serverError) {
    return <Error code={500} />;
  }

  return (
    <AuthContext.Provider value={authValue}>
      <Routes>
        <Route path="/" element={<Landing />} />

        <Route
          path="/login"
          element={user ? <Navigate to="/dashboard" replace /> : <Login onLogin={setUser} />}
        />

        <Route
          path="/dashboard"
          element={
            user ? (
              <Dashboard user={user} onLogout={handleLogout} />
            ) : (
              <Navigate to="/login" replace />
            )
          }
        />

        <Route path="*" element={<Error code={404} />} />
      </Routes>
    </AuthContext.Provider>
  );
}
