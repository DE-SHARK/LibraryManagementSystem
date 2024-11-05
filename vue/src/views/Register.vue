<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const registerForm = ref({
  username: '',
  password: ''
})

const register = async () => {
  try {
    const response = await fetch('http://localhost:8080/user/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(registerForm.value)
    })
    const data = await response.json()
    if (data.code === 200) {
      alert('注册成功')
      router.push('/login')
    } else {
      alert(data.message)
    }
  } catch (error) {
    console.error('注册失败:', error)
    alert('注册失败，请重试')
  }
}
</script>

<template>
  <div class="register-container">
    <h2>注册</h2>
    <form @submit.prevent="register" class="register-form">
      <div class="form-item">
        <label>用户名:</label>
        <input type="text" v-model="registerForm.username" required>
      </div>
      <div class="form-item">
        <label>密码:</label>
        <input type="password" v-model="registerForm.password" required>
      </div>
      <button type="submit">注册</button>
      <div class="login-link">
        <router-link to="/login">已有账号？立即登录</router-link>
      </div>
    </form>
  </div>
</template>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.register-form {
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

.login-link {
  text-align: center;
}
</style> 