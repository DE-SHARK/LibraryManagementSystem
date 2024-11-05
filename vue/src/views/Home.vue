<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userRole = ref('')
const searchKeyword = ref('')
const searchType = ref('title') // 搜索类型:title/author/clcNumber
const searchResults = ref([])
const loading = ref(false)

// 检查是否已登录
onMounted(() => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  if (!token || !role) {
    router.push('/login')
  } else {
    userRole.value = role
  }
})

// 退出登录
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('role')
  userRole.value = ''
  router.push('/login')
}

// 跳转个人中心
const goToUserCenter = () => {
  router.push('/user-center')
}

// 搜索图书
const searchBooks = async () => {
  if (!searchKeyword.value.trim()) {
    alert('请输入搜索关键词')
    return
  }

  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const response = await fetch(`http://localhost:8080/book/searchBy${searchType.value.charAt(0).toUpperCase() + searchType.value.slice(1)}?${searchType.value}=${searchKeyword.value}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    const data = await response.json()
    if (data.code === 200) {
      searchResults.value = data.data
    } else {
      alert(data.message)
    }
  } catch (error) {
    console.error('搜索失败:', error)
    alert('搜索失败,请重试')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="home-layout">
    <!-- 头部 -->
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <h1>广东药科大学图书馆</h1>
        </div>
        <div class="header-right">
          <span class="welcome">欢迎您，{{ userRole }}</span>
          <a @click="goToUserCenter">个人中心</a>
          <span class="divider">|</span>
          <a @click="logout">退出登录</a>
        </div>
      </div>
    </header>

    <!-- 主要内容区 -->
    <main class="main-content">
      <!-- 搜索区域 -->
      <div class="search-section">
        <div class="search-type">
          <select v-model="searchType">
            <option value="title">书名</option>
            <option value="author">作者</option>
            <option value="clcNumber">分类号</option>
          </select>
        </div>
        <div class="search-box">
          <input 
            type="text" 
            v-model="searchKeyword"
            :placeholder="searchType === 'title' ? '搜索书名...' : searchType === 'author' ? '搜索作者...' : '搜索分类号...'"
            @keyup.enter="searchBooks"
          />
          <button class="search-btn" @click="searchBooks" :disabled="loading">
            {{ loading ? '搜索中...' : '搜索' }}
          </button>
        </div>
      </div>

      <!-- 搜索结果区域 -->
      <div v-if="searchResults.length > 0" class="search-results">
        <div v-for="book in searchResults" :key="book.isbn" class="book-card">
          <h3>{{ book.title }}</h3>
          <p>作者: {{ book.author }}</p>
          <p>ISBN: {{ book.isbn }}</p>
          <p>位置: {{ book.location }}</p>
          <p>可借数量: {{ book.availableQuantity }}/{{ book.totalQuantity }}</p>
        </div>
      </div>
      <div v-else-if="searchKeyword && !loading" class="no-results">
        未找到相关图书
      </div>

      <!-- 功能区域 -->
      <div class="features-section">
        <div class="feature-card">
          <h3>图书借阅</h3>
          <p>浏览和借阅图书馆藏书</p>
        </div>
        <div class="feature-card">
          <h3>借阅历史</h3>
          <p>查看个人借阅记录</p>
        </div>
        <div class="feature-card">
          <h3>预约服务</h3>
          <p>预约借阅或阅览室</p>
        </div>
        <div class="feature-card">
          <h3>个人信息</h3>
          <p>管理个人账户信息</p>
        </div>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-section">
          <h4>联系我们</h4>
          <p>地址：XX市XX区XX街XX号</p>
          <p>电话：XXX-XXXXXXXX</p>
          <p>邮箱：library@example.com</p>
        </div>
        <div class="footer-section">
          <h4>开放时间</h4>
          <p>周一至周五：9:00-21:00</p>
          <p>周六至周日：9:00-17:00</p>
        </div>
        <div class="footer-section">
          <h4>快速链接</h4>
          <p><a href="#">关于我们</a></p>
          <p><a href="#">借阅规则</a></p>
          <p><a href="#">新书推荐</a></p>
        </div>
      </div>
      <div class="copyright">
        © 2024 图书管理系统 版权所有
      </div>
    </footer>
  </div>
</template>

<style scoped>
.home-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: #1a1a1a;
  color: white;
  padding: 1rem 0;
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1280px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.header-right a {
  color: white;
  cursor: pointer;
}

.header-right a:hover {
  color: #646cff;
}

.divider {
  color: #666;
}

.main-content {
  flex: 1;
  margin-top: 80px;
  padding: 2rem;
}

.search-section {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  text-align: center;
}

.search-box {
  display: flex;
  gap: 1rem;
  max-width: 600px;
  margin: 0 auto;
}

.search-box input {
  flex: 1;
  padding: 0.8rem;
  border: 2px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.search-btn {
  padding: 0.8rem 2rem;
  background-color: #646cff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.features-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  max-width: 1280px;
  margin: 3rem auto;
  padding: 0 2rem;
}

.feature-card {
  background-color: #2c2c2c;
  padding: 2rem;
  border-radius: 8px;
  text-align: center;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-card h3 {
  color: #646cff;
  margin-bottom: 1rem;
}

.footer {
  background-color: #1a1a1a;
  color: #fff;
  padding: 3rem 0 1rem;
  margin-top: auto;
}

.footer-content {
  max-width: 1280px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  padding: 0 2rem;
}

.footer-section h4 {
  color: #646cff;
  margin-bottom: 1rem;
}

.footer-section a {
  color: #fff;
  text-decoration: none;
}

.footer-section a:hover {
  color: #646cff;
}

.copyright {
  text-align: center;
  padding-top: 2rem;
  margin-top: 2rem;
  border-top: 1px solid #333;
}

.search-type {
  margin-bottom: 1rem;
}

.search-type select {
  padding: 0.8rem;
  border: 2px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  width: 200px;
}

.search-results {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
  padding: 2rem;
  max-width: 1280px;
  margin: 0 auto;
}

.book-card {
  background-color: #2c2c2c;
  padding: 1.5rem;
  border-radius: 8px;
  transition: transform 0.3s;
}

.book-card:hover {
  transform: translateY(-5px);
}

.book-card h3 {
  color: #646cff;
  margin-bottom: 1rem;
}

.book-card p {
  margin: 0.5rem 0;
  color: #ddd;
}

.no-results {
  text-align: center;
  padding: 2rem;
  color: #666;
}

button:disabled {
  background-color: #999;
  cursor: not-allowed;
}
</style>