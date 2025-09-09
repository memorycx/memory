
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus'; // 假设使用 Element Plus 弹窗提示
import router from '../router/index'; // 引入路由实例，用于 Token 过期跳转登录页

// 1. 创建 Axios 实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL, 
  timeout: 5000, 
  headers: {
    'Content-Type': 'application/json;charset=utf-8' 
  }
});


// 2. 请求拦截器：发送请求前执行
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = token;
    }
    return config; 
  },
  (error) => {
    return Promise.reject(error);
  }
);


// 3. 响应拦截器：接收响应后执行
// 对后端验证做一层筛选
request.interceptors.response.use(
  (response) => {
    const res = response.data; 
    if (res.code !== 1) { 
      ElMessage.error({ message: res.msg || '操作失败', duration: 3000 });
      return Promise.reject(new Error(res.msg || 'Error')); // 转为 Promise 错误，让业务代码 catch 处理
    } else {
      return res.data; 
    }
  },
  (error) => {
    // 处理 4xx/5xx  HTTP 错误或网络错误
    let errorMsg = '请求失败，请稍后重试';
    
    // 1. 判断是否有响应（网络错误时无 response）
    if (error.response) {
      const status = error.response.status; // 获取 HTTP 状态码
      const resData = error.response.data; // 错误响应体
      
      // 根据状态码处理不同场景
      switch (status) {
        case 401: // Token 过期/未登录
          errorMsg = resData.msg || '登录已过期，请重新登录';
          // 清除过期 Token，避免死循环
          localStorage.removeItem('authToken');
          // 弹窗提示并跳转登录页（阻止重复跳转）
          ElMessageBox.confirm(
            errorMsg,
            '提示',
            { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }
          ).then(() => {
            router.push('/login').then(() => {
              window.location.reload(); // 刷新页面，清除旧状态
            });
          });
          break;
          
        case 403: // 无权限（如普通用户访问管理员接口）
          errorMsg = resData.msg || '您没有权限执行此操作';
          break;
          
        case 404: // 接口不存在
          errorMsg = '请求的接口不存在';
          break;
          
        case 500: // 服务器错误
          errorMsg = '服务器内部错误，请联系管理员';
          break;
          
        default: // 其他错误（如 400 参数错误）
          errorMsg = resData.msg || `请求错误（${status}）`;
      }
    } else if (error.request) {
      // 有请求但无响应（如网络断开、超时）
      errorMsg = '网络异常，请检查网络连接';
    }
    
    // 提示最终错误信息
    ElMessage.error({ message: errorMsg, duration: 3000 });
    // 返回错误，让业务代码可 catch 处理
    return Promise.reject(error);
  }
);

export default request;