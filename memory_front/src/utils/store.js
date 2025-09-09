function saveLoginStatus(user,authToken) {
  localStorage.setItem('isLogin', 'true'); 
  localStorage.setItem('currentUser', JSON.stringify(user)); 
  localStorage.setItem('authToken', authToken); 
}

// 场景2：页面加载时读取登录状态
function checkLogin() {
  const isLogin = localStorage.getItem('isLogin') === 'true';
  if (isLogin) return true; 
  return false;
}

// 场景3：退出登录时清除状态
function logout() {
  localStorage.removeItem('isLogin');
  localStorage.removeItem('currentUser');
  localStorage.removeItem('authToken');
}

// 导出所有函数
export {
  saveLoginStatus,
  checkLogin,
  logout
};