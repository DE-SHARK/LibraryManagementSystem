<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const categories = ref([
  { name: '文学艺术', icon: '📚', count: 156 },
  { name: '自然科学', icon: '🔬', count: 234 },
  { name: '社会科学', icon: '🌍', count: 189 },
  { name: '医药卫生', icon: '💊', count: 467 },
  { name: '工程技术', icon: '⚙️', count: 245 },
  { name: '计算机科学', icon: '💻', count: 312 }
])

const recommendedBooks = ref([
  { title: '中医基础理论', author: '张三', category: '医药卫生', cover: '/book-covers/1.jpg', status: '可借阅' },
  { title: '药物化学', author: '李四', category: '医药卫生', cover: '/book-covers/2.jpg', status: '可借阅' },
  { title: '临床医学概论', author: '王五', category: '医药卫生', cover: '/book-covers/3.jpg', status: '已借出' },
  { title: '生物化学', author: '赵六', category: '自然科学', cover: '/book-covers/4.jpg', status: '可借阅' }
])

const goToUserCenter = () => {
  router.push('/user-center')
}
</script>

<template>
  <div class="home-layout">
    <header class="main-header">
      <div class="header-content">
        <h1>广东药科大学线上图书馆</h1>
        <div class="header-right">
          <div class="search-box">
            <input type="text" placeholder="搜索图书...">
            <button class="search-btn">搜索</button>
          </div>
          <button class="user-center-btn" @click="goToUserCenter">个人中心</button>
        </div>
      </div>
    </header>

    <main class="main-content">
      <div class="banner">
        <h2>欢迎使用广东药科大学线上图书馆</h2>
        <p>在这里，你可以浏览和借阅丰富的图书资源</p>
      </div>

      <section class="categories-section">
        <h2>图书分类</h2>
        <div class="category-grid">
          <div v-for="category in categories" 
               :key="category.name" 
               class="category-card">
            <div class="category-icon">{{ category.icon }}</div>
            <h3>{{ category.name }}</h3>
            <p class="category-count">{{ category.count }} 本馆藏</p>
          </div>
        </div>
      </section>

      <section class="recommended-section">
        <h2>馆藏推荐</h2>
        <div class="book-grid">
          <div v-for="book in recommendedBooks" 
               :key="book.title" 
               class="book-card">
            <div class="book-cover">
              <div class="book-icon">📖</div>
            </div>
            <div class="book-info">
              <h3>{{ book.title }}</h3>
              <p class="book-author">作者：{{ book.author }}</p>
              <p class="book-category">分类：{{ book.category }}</p>
              <p class="book-status" :class="{ 'available': book.status === '可借阅' }">
                {{ book.status }}
              </p>
            </div>
          </div>
        </div>
      </section>
    </main>

    <footer class="main-footer">
      <div class="footer-content">
        <p>© 2024 广东药科大学图书馆. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.home-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.main-header {
  background-color: #1a1a1a;
  color: white;
  padding: 1rem 0;
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-right {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-box {
  display: flex;
  gap: 0.5rem;
}

.search-box input {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  border: none;
  width: 300px;
}

.search-btn {
  background-color: #4a5568;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.user-center-btn {
  background-color: #646cff;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  border: none;
  cursor: pointer;
}

.main-content {
  margin-top: 80px;
  flex: 1;
  padding: 2rem;
  max-width: 1400px;
  margin-left: auto;
  margin-right: auto;
}

.banner {
  text-align: center;
  padding: 3rem 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px;
  margin-bottom: 2rem;
}

.banner h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.categories-section, .recommended-section {
  background-color: white;
  border-radius: 8px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 1.5rem;
  margin-top: 1rem;
}

.category-card {
  background-color: #f8f9fa;
  padding: 1.5rem;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.3s;
}

.category-card:hover {
  transform: translateY(-5px);
}

.category-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.category-count {
  color: #666;
  margin-top: 0.5rem;
}

.book-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
  margin-top: 1rem;
}

.book-card {
  background-color: #f8f9fa;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s;
}

.book-card:hover {
  transform: translateY(-5px);
}

.book-cover {
  height: 200px;
  background-color: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
}

.book-info {
  padding: 1.5rem;
}

.book-author, .book-category {
  color: #666;
  margin: 0.5rem 0;
}

.book-status {
  display: inline-block;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  background-color: #dc3545;
  color: white;
  margin-top: 0.5rem;
}

.book-status.available {
  background-color: #28a745;
}

.main-footer {
  background-color: #1a1a1a;
  color: white;
  padding: 1rem 0;
  margin-top: auto;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
  text-align: center;
}
</style> 