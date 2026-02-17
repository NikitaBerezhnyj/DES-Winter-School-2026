import { apiRequest } from "../utils/ApiClient";

export async function getGa4AuthorizeUrl() {
  try {
    const data = await apiRequest("/api/analytics/oauth2/authorize-url");
    return data?.url;
  } catch (error) {
    if (error.message?.includes("401")) {
      throw new Error("Unauthorized: please login first");
    }
    throw new Error("Failed to get GA4 authorize URL");
  }
}

export async function getGa4Properties() {
  try {
    return await apiRequest("/api/analytics/properties");
  } catch (error) {
    throw new Error(error.message || "Failed to get GA4 properties");
  }
}

export async function selectGa4Property(propertyId) {
  return apiRequest(`/api/analytics/properties/${propertyId}/select`, {
    method: "POST"
  });
}
