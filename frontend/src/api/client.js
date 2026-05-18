import axios from 'axios';
const api = axios.create({ baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8081' });
api.interceptors.request.use(cfg => {
  const t = localStorage.getItem('accessToken');
  if (t) cfg.headers.Authorization = `Bearer ${t}`;
  return cfg;
});
api.interceptors.response.use(r => r, async err => {
  const original = err.config;
  if (err.response?.status === 401 && !original._retry) {
    original._retry = true;
    const rt = localStorage.getItem('refreshToken');
    if (rt) {
      try {
        const { data } = await axios.post(`${api.defaults.baseURL}/api/auth/refresh`, { refreshToken: rt });
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        original.headers.Authorization = `Bearer ${data.accessToken}`;
        return api(original);
      } catch {}
    }
  }
  return Promise.reject(err);
});
export default api;
