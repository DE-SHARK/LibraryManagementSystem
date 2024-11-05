<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userRole = ref('')

onMounted(() => {
  // 检查是否已登录
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  if (!token || !role) {
    router.push('/login')
  } else {
    userRole.value = role
  }
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  userRole.value = ''
  router.push('/login')
}
</script>

<template>
  <div class="home-container">
    <header class="header">
      <h1>图书管理系统</h1>
      <button @click="logout" class="logout-btn">退出登录</button>
    </header>
    <main class="main-content">
      <h2>欢迎使用图书管理系统</h2>
      <div class="role-info">
        当前角色：{{ userRole }}
      </div>
    </main>
  </div>
</template>

<style scoped>
.home-container {
  width: 100%;
  min-height: 100vh;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: #1a1a1a;
  color: white;
}

.logout-btn {
  background-color: #dc3545;
  color: white;
  padding: 0.5rem 1rem;
}

.logout-btn:hover {
  background-color: #bb2d3b;
}

.main-content {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.role-info {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 4px;
}
</style> 