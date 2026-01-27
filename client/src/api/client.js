const API_BASE = import.meta.env.VITE_API_URL || "http://localhost:8080";

export async function apiRequest(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...options.headers
    },
    credentials: "include",
    ...options
  });

  if (!res.ok) {
    const error = await res.text();
    throw new Error(error || "API error");
  }

  return res.json();
}
