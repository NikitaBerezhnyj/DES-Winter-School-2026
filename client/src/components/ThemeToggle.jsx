import { useEffect, useState } from "react";
import { Sun, Moon } from "lucide-react";
import "../styles/components/ThemeToggle.css";

export default function ThemeToggle() {
  const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "light");

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
  }, [theme]);

  const toggleTheme = () => {
    const nextTheme = theme === "light" ? "dark" : "light";
    setTheme(nextTheme);
    localStorage.setItem("theme", nextTheme);
  };

  return (
    <button className="theme-toggle-button" onClick={toggleTheme}>
      <span className={`icon-wrapper ${theme}`}>{theme === "light" ? <Sun /> : <Moon />}</span>
    </button>
  );
}
