<script setup>
import { ref } from 'vue'
import axios from 'axios'

const username = ref('')
const password = ref('')
const message = ref('')

const loginUser = async () => {
  try {
    const response = await axios.post('/api/login', {
      username: username.value,
      password: password.value
    })
    message.value = response.data.message
  } catch (error) {
    console.error('Error logging in:', error)
    message.value = '登录失败'
  }
}

const registerUser = async () => {
  try {
    const response = await axios.post('/api/register', {
      username: username.value,
      password: password.value
    })
    message.value = response.data.message
  } catch (error) {
    console.error('Error registering:', error)
    message.value = '注册失败'
  }
}
</script>

<template>
  <h1>用户登录/注册</h1>
  <input v-model="username" placeholder="用户名" />
  <input v-model="password" type="password" placeholder="密码" />
  <button @click="loginUser">登录</button>
  <button @click="registerUser">注册</button>
  <p>{{ message }}</p>
</template>