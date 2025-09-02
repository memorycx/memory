import axios from 'axios';

// 假设你的后端API地址是 http://localhost:3000
const API_URL = 'http://localhost:3000';

export const registerUser = (userData) => {
    return axios.post(`${API_URL}/register`, userData);
};