<template>
  <div class="app-container">
    <!-- 顶部导航栏 -->
    <header class="app-header">
      <div class="header-content">
        <div class="logo">
          <i class="fas fa-language"></i>
          <span>英语学习助手</span>
        </div>
        <nav class="main-nav">
          <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
            <i class="fas fa-chart-line"></i>
            <span>数据分析</span>
          </router-link>
          <router-link to="/vocabulary" class="nav-item" :class="{ active: $route.path === '/vocabulary' }">
            <i class="fas fa-book"></i>
            <span>背单词</span>
          </router-link>
          <router-link to="/reading" class="nav-item" :class="{ active: $route.path === '/reading' }">
            <i class="fas fa-newspaper"></i>
            <span>文章阅读</span>
          </router-link>
          <router-link to="/speaking" class="nav-item" :class="{ active: $route.path === '/speaking' }">
            <i class="fas fa-microphone"></i>
            <span>口语训练</span>
          </router-link>
        </nav>
        <div class="user-info">
          <el-dropdown>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>设置</el-dropdown-item>
                <el-dropdown-item divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>
    
    <!-- 主内容区域 -->
    <main class="app-main">
      <router-view />
    </main>
    
    <!-- 页脚 -->
    <footer class="app-footer">
      <div class="footer-content">
        <p>© 2024 英语学习助手 - 让学习更高效</p>
      </div>
    </footer>
  </div>
</template>

<script>

export default {
  name: 'App'
}
</script>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f5f7fa;
}

/* 应用容器 */
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 顶部导航栏 */
.app-header {
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  color: var(--primary-color);
}

.logo i {
  margin-right: 10px;
  font-size: 24px;
}

/* 主导航 */
.main-nav {
  display: flex;
  gap: 10px;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 10px 20px;
  color: #666;
  text-decoration: none;
  border-radius: 6px;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: var(--primary-bg);
  color: var(--primary-color);
}

.nav-item.active {
  background-color: var(--primary-light);
  color: var(--primary-color);
  font-weight: 500;
}

.nav-item i {
  margin-right: 8px;
}

/* 用户信息 */
.user-info {
  display: flex;
  align-items: center;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
}

/* 主内容区域 */
.app-main {
  flex: 1;
  padding: 0;
  width: 70%;
  margin: 0 auto;
}

/* 页脚 */
.app-footer {
  background-color: white;
  padding: 20px 0;
  text-align: center;
  color: #999;
  margin-top: auto;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .header-content {
    padding: 0 15px;
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-wrap: wrap;
    height: auto;
    padding: 10px;
  }
  
  .main-nav {
    width: 100%;
    justify-content: center;
    margin: 10px 0;
  }
  
  .nav-item {
    padding: 8px 16px;
  }
  .app-main{
    width: 100%;
  }
}
</style>
