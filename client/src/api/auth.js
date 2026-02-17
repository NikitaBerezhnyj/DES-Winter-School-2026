import { apiRequest } from "../utils/ApiClient";

export function login(idToken) {
  return apiRequest("/api/auth/login", {
    method: "POST",
    body: JSON.stringify({ idToken })
  });
}

export function getCurrentUser() {
  return apiRequest("/api/auth/me");
}

export async function logout() {
  return await apiRequest("/api/auth/logout", { method: "POST" });
}
