<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loginForm = ref({
  username: '',
  password: ''
})

const login = async () => {
  try {
    const response = await fetch('http://localhost:8080/user/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(loginForm.value)
    })
    const data = await response.json()
    if (data.code === 200) {
      localStorage.setItem('token', data.data.token)
      localStorage.setItem('role', data.data.role)
      router.push('/')
    } else {
      alert(data.message)
    }
  } catch (error) {
    console.error('登录失败:', error)
    alert('登录失败，请重试')
  }
}
</script>

<template>
  <div class="login-container">
    <h2>登录</h2>
    <form @submit.prevent="login" class="login-form">
      <div class="form-item">
        <label>用户名:</label>
        <input type="text" v-model="loginForm.username" required>
      </div>
      <div class="form-item">
        <label>密码:</label>
        <input type="password" v-model="loginForm.password" required>
      </div>
      <button type="submit">登录</button>
      <div class="register-link">
        <router-link to="/register">还没有账号？立即注册</router-link>
      </div>
    </form>
  </div>
</template>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  padding: 10px;
  background-color: #646cff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #535bf2;
}

.register-link {
  text-align: center;
}
</style> 