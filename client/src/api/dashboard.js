import { apiRequest } from "./client";

export function fetchDashboard() {
  return apiRequest("/dashboard");
}

export function fetchAiReport() {
  return apiRequest("/dashboard/ai-report");
}
