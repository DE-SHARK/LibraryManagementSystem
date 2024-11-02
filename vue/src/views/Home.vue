<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
  // 检查是否已登录，如果未登录则跳转到登录页
  const token = localStorage.getItem('token')
  if (!token) {
    router.push('/login')
  }
})
</script>

<template>
  <div class="home-container">
    <h1>欢迎使用图书管理系统</h1>
    <div class="content">
      <p>您已成功登录系统</p>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  padding: 20px;
  text-align: center;
}

.content {
  margin-top: 20px;
}
</style> 