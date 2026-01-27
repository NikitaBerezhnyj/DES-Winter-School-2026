import { useEffect, useState } from "react";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import { getMe } from "./api/auth";

export default function App() {
  const [page, setPage] = useState("landing");
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getMe()
      .then(setUser)
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  if (loading) return null;

  if (!user) {
    if (page === "login") {
      return <Login onLogin={setUser} />;
    }

    return <Landing onStart={() => setPage("login")} />;
  }

  return <Dashboard user={user} />;
}
