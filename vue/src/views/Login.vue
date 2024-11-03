<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from '../utils/axios'

const router = useRouter()
const username = ref('')
const password = ref('')
const errorMessage = ref('')

const handleLogin = async () => {
  try {
    const response = await axios.post('/user/login', {
      username: username.value,
      password: password.value
    })
    
    if (response.data.code === 200) {
      localStorage.setItem('token', response.data.data)
      localStorage.setItem('username', username.value)
      router.push('/')
    } else {
      errorMessage.value = response.data.message || '登录失败'
    }
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '登录失败，请稍后重试'
  }
}
</script>

<template>
  <div class="login-container">
    <h2>登录</h2>
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <input 
          type="text" 
          v-model="username" 
          placeholder="用户名"
          required
        >
      </div>
      <div class="form-group">
        <input 
          type="password" 
          v-model="password" 
          placeholder="密码"
          required
        >
      </div>
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      <button type="submit">登录</button>
    </form>
    <div class="register-link">
      还没有账号？<router-link to="/register">立即注册</router-link>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 40px auto;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 15px;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #3aa876;
}

.error-message {
  color: red;
  margin-bottom: 10px;
}

.register-link {
  margin-top: 15px;
  text-align: center;
}

.register-link a {
  color: #42b883;
  text-decoration: none;
}
</style> 