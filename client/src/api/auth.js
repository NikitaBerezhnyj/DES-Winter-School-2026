import { apiRequest } from "./client";

export function loginWithGoogle(token) {
  return apiRequest("/auth/google", {
    method: "POST",
    body: JSON.stringify({ token })
  });
}

export function getMe() {
  return apiRequest("/auth/me");
}

export function logout() {
  return apiRequest("/auth/logout", {
    method: "POST"
  });
}
