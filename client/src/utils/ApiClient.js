const API_BASE = import.meta.env.VITE_API_URL || "http://localhost:8080";

export async function apiRequest(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers,
    credentials: "include"
  });

  if (!response.ok) {
    const errorText = await response.text();
    const error = new Error(errorText || "API error");
    error.status = response.status;
    throw error;
  }

  const contentType = response.headers.get("Content-Type") || "";
  if (response.status === 204 || !contentType.includes("application/json")) {
    return null;
  }

  return response.json();
}
