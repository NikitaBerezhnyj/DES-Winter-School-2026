import { apiRequest } from "../utils/ApiClient";

export const getDashboardReport = async period => {
  return apiRequest(`/api/dashboard?period=${period}`);
};
