<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userRole = ref('')
const activeMenu = ref('home')

onMounted(() => {
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

const switchMenu = (menu) => {
  activeMenu.value = menu
}
</script>

<template>
  <div class="layout">
    <header class="header">
      <div class="header-left">
        <h1>个人中心</h1>
      </div>
      <div class="header-right">
        <span class="user-role">角色：{{ userRole }}</span>
        <button @click="logout" class="logout-btn">退出登录</button>
      </div>
    </header>

    <div class="main-container">
      <nav class="sidebar">
        <div class="menu-item" 
             :class="{ active: activeMenu === 'home' }"
             @click="switchMenu('home')">
          首页
        </div>
        <div class="menu-item" 
             :class="{ active: activeMenu === 'borrowing' }"
             @click="switchMenu('borrowing')">
          我的借阅
        </div>
        <div class="menu-item" 
             :class="{ active: activeMenu === 'history' }"
             @click="switchMenu('history')">
          借阅历史
        </div>
        <div class="menu-item" 
             :class="{ active: activeMenu === 'profile' }"
             @click="switchMenu('profile')">
          个人信息
        </div>
      </nav>

      <main class="content">
        <div v-if="activeMenu === 'home'" class="welcome-section">
          <h2>个人中心</h2>
          <p>在这里，您可以：</p>
          <ul>
            <li>查看当前借阅</li>
            <li>查看借阅历史</li>
            <li>管理个人信息</li>
          </ul>
        </div>
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
  height: 60px;
  background-color: #1a1a1a;
  color: white;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-role {
  color: #e6e6e6;
}

.logout-btn {
  background-color: #dc3545;
  color: white;
  padding: 0.5rem 1rem;
}

.logout-btn:hover {
  background-color: #bb2d3b;
}

.main-container {
  display: flex;
  margin-top: 60px;
  flex: 1;
}

.sidebar {
  width: 200px;
  background-color: #2c2c2c;
  padding: 1rem 0;
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
}

.menu-item {
  padding: 1rem 1.5rem;
  color: #e6e6e6;
  cursor: pointer;
  transition: background-color 0.3s;
}

.menu-item:hover {
  background-color: #3c3c3c;
}

.menu-item.active {
  background-color: #4c4c4c;
  border-left: 4px solid #646cff;
}

.content {
  flex: 1;
  padding: 2rem;
  margin-left: 200px;
  background-color: #1c1c1c;
  min-height: calc(100vh - 60px);
}

.welcome-section {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #2c2c2c;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}
</style> 