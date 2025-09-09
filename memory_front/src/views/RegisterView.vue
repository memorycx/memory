<template>
  <div class="register-container">
    <div class="register-form-wrapper">
      <div class="register-header">
        <div class="logo">
          <i class="fas fa-language"></i>
          <span>英语学习助手</span>
        </div>
        <h2>账号注册</h2>
        <p>创建您的账号，开始英语学习之旅</p>
      </div>
      
      <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请设置用户名"
            prefix-icon="User"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            type="email"
            placeholder="请输入邮箱"
            prefix-icon="Message"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.pwd"
            type="password"
            placeholder="请设置密码（6-20位）"
            prefix-icon="Lock"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="agreement">
          <el-checkbox v-model="registerForm.agreement">
            我已阅读并同意 <a href="#" class="agreement-link">用户协议</a> 和 <a href="#" class="agreement-link">隐私政策</a>
          </el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" class="register-button" @click="handleRegister">立即注册</el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-link">
        <span>已有账号？</span>
        <router-link to="/login" class="link">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { register } from '../api/user'
import { ElMessage } from 'element-plus';
export default {
  name: 'RegisterView',
  data() {
    return {
      registerForm: {
        username: '',
        email: '',
        pwd: '',
        confirmPassword: '',
        agreement: false
      },
      registerRules: {
        username: [
          { required: true, message: '请设置用户名', trigger: 'blur' },
          { min: 2, max: 20, message: '用户名长度在2到20个字符之间', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        pwd: [
          { required: true, message: '请设置密码', trigger: 'blur' },
          { min: 6, max: 20, message: '密码长度在6到20个字符之间', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.registerForm.pwd) {
                callback(new Error('两次输入的密码不一致'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        agreement: [
          { required: true, validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error('请阅读并同意用户协议和隐私政策'))
              } else {
                callback()
              }
            },
            trigger: 'change'
          }
        ]
      }
    }
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          await register(this.registerForm).then(() => {
            ElMessage.success('注册成功，请登录')
            this.$router.push('/login')
          }).catch(err => {
            ElMessage.error(err.message)
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}

.register-form-wrapper {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

.register-header {
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

.register-header h2 {
  font-size: 24px;
  color: #303133;
  margin-bottom: 8px;
}

.register-header p {
  color: #909399;
  font-size: 14px;
}

.register-form {
  margin-bottom: 20px;
}

.register-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.login-link {
  text-align: center;
  font-size: 14px;
  color: #606266;
}

.login-link .link {
  color: var(--primary-color, #409eff);
  text-decoration: none;
  margin-left: 5px;
}

.agreement-link {
  color: var(--primary-color, #409eff);
  text-decoration: none;
}
</style>