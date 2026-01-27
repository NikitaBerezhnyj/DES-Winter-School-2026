import { useEffect, useState } from "react";
import Landing from "./pages/Landing";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";

export default function App() {
  const [page, setPage] = useState("landing");
  const [user, setUser] = useState(null);

  useEffect(() => {
    const theme = localStorage.getItem("theme") || "light";
    document.documentElement.setAttribute("data-theme", theme);
  }, []);

  if (!user) {
    if (page === "login") {
      return <Login onLogin={setUser} />;
    }

    return <Landing onStart={() => setPage("login")} />;
  }

  return <Dashboard user={user} />;
}
