import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  // withCredentials: true,             // if you use cookies
  headers: {
    "Content-Type": "application/json",
  },
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor
api.interceptors.response.use(
  (res) => res,
  (error) => {
    const status = error.response?.status;

    // Backend validation errors
    if (status === 400 && error.response.data) {
      console.warn("Validation error:", error.response.data);
    }

    // Unauthorized / JWT expired
    if (status === 401) {
      alert(`Error: ${error.response.data.message}`);
      localStorage.removeItem("token");
      window.location.href = "/"; // Redirect to login
    }

    // Forbidden (insufficient role)
    if (status === 403) {
      alert("You do not have permission.");
    }

    // Forbidden (insufficient role)
    if (status === 404) {
      alert("404 Error.");
    }

    // Generic error
    if (status >= 500) {
      alert("Server error. Try again later.");
    }

    return Promise.reject(error);
  }
);

export default api;
