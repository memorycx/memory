<template>
  <div class="login-container">
    <div class="login-form-wrapper">
      <div class="login-header">
        <div class="logo">
          <i class="fas fa-language"></i>
          <span>英语学习助手</span>
        </div>
        <p>欢迎回来，请登录您的账号</p>
      </div>
      
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名或邮箱"
            prefix-icon="User"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.pwd"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
          ></el-input>
        </el-form-item>
        
        <div class="login-options">
          <el-checkbox v-model="rememberMe">记住我</el-checkbox>
          <router-link to="#" class="forgot-password">忘记密码？</router-link>
        </div>
        
        <el-form-item>
          <el-button type="primary" class="login-button" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
      
      <div class="register-link">
        <span>还没有账号？</span>
        <router-link to="/register" class="link">立即注册</router-link>
      </div>
      
      <!-- <div class="divider">
        <span>其他登录方式</span>
      </div>
      
      <div class="social-login">
        <el-button class="social-btn" icon="Weibo"></el-button>
        <el-button class="social-btn" icon="Wechat"></el-button>
        <el-button class="social-btn" icon="Github"></el-button>
      </div> -->
    </div>
  </div>
</template>

<script>
import { login } from '../api/user'
import { saveLoginStatus } from '../utils/store'
import { ElMessage } from 'element-plus'; // 导入消息提示组件
export default {
  name: 'LoginView',
  data() {
    return {
      loginForm: {
        username: '',
        pwd: ''
      },
      rememberMe: false,
      loginRules: {
        username: [
          { required: true, message: '请输入用户名或邮箱', trigger: 'blur' }
        ],
        pwd: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
     handleLogin() {

      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          try{
            const data = await login(this.loginForm);
            saveLoginStatus(this.loginForm.username, data);
            ElMessage.success('登录成功！');
            this.$router.push('/');
          }catch(err){
            ElMessage.error('用户名或密码错误！');
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.login-form-wrapper {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  color: var(--primary-color, #409eff);
  margin-bottom: 15px;
}

.logo i {
  margin-right: 10px;
  font-size: 28px;
}

.login-header h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 8px;
}

.login-header p {
  color: #909399;
  font-size: 14px;
}

.login-form {
  margin-bottom: 20px;
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.forgot-password {
  color: var(--primary-color, #409eff);
  font-size: 14px;
  text-decoration: none;
}

.login-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.register-link {
  text-align: center;
  margin-bottom: 20px;
  font-size: 14px;
  color: #606266;
}

.register-link .link {
  color: var(--primary-color, #409eff);
  text-decoration: none;
  margin-left: 5px;
}

.divider {
  text-align: center;
  position: relative;
  margin: 20px 0;
}

.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: calc(50% - 60px);
  height: 1px;
  background-color: #e4e7ed;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.divider span {
  display: inline-block;
  padding: 0 10px;
  background-color: white;
  color: #909399;
  font-size: 14px;
}

.social-login {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

.social-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #dcdfe6;
  color: #606266;
  transition: all 0.3s;
}

.social-btn:hover {
  color: var(--primary-color, #409eff);
  border-color: var(--primary-color, #409eff);
}
</style>