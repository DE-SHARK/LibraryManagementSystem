<template>
  <div class="home-container">
    <div class="user-menu">
      <div class="user-icon" @click="toggleDropdown">
        <i class="fas fa-user"></i>
      </div>
      <div class="dropdown-menu" v-if="showDropdown">
        <div class="user-info">
          <p>用户名：{{ username }}</p>
        </div>
        <div class="divider"></div>
        <div class="menu-item" @click="handleLogout">
          <i class="fas fa-sign-out-alt"></i>
          退出登录
        </div>
      </div>
    </div>
    
    <div class="nav-menu" v-if="userRole">
      <router-link 
        v-if="['ADMIN', 'LIBRARIAN'].includes(userRole.toUpperCase())" 
        to="/book-management" 
        class="nav-item"
      >
        <i class="fas fa-book"></i>
        图书管理
      </router-link>
      
      <router-link 
        v-if="userRole.toUpperCase() === 'ADMIN'" 
        to="/librarian-management" 
        class="nav-item"
      >
        <i class="fas fa-users-cog"></i>
        管理员管理
      </router-link>
      
      <router-link 
        v-if="['ADMIN', 'LIBRARIAN'].includes(userRole.toUpperCase())" 
        to="/borrower-management" 
        class="nav-item"
      >
        <i class="fas fa-users"></i>
        借阅者管理
      </router-link>
      
      <router-link 
        v-if="userRole.toUpperCase() === 'BORROWER'" 
        to="/my-borrows" 
        class="nav-item"
      >
        <i class="fas fa-book-reader"></i>
        我的借阅
      </router-link>
    </div>
    
    <h1>欢迎使用图书管理系统</h1>
    <div class="search-section">
      <div class="search-box">
        <input 
          v-model="searchQuery" 
          placeholder="输入书名、作者或CLC号" 
          @keyup.enter="searchBooks"
        />
        <button @click="searchBooks" class="search-btn">查询图书</button>
      </div>
    </div>

    <div class="books-container" v-if="books.length > 0">
      <div class="book-card" v-for="book in books" :key="book.isbn">
        <div class="book-info">
          <h3>{{ book.title }}</h3>
          <p><span>作者：</span>{{ book.author }}</p>
          <p><span>ISBN：</span>{{ book.isbn }}</p>
          <p><span>分类号：</span>{{ book.clcNumber }}</p>
          <p><span>馆藏地：</span>{{ book.location }}</p>
          <p><span>馆藏数量：</span>{{ book.totalQuantity }}</p>
          <p><span>可借数量：</span>{{ book.availableQuantity }}</p>
        </div>
        <div class="book-actions">
          <button 
            @click="reserveBook(book.isbn)"
            :disabled="book.availableQuantity === 0"
          >预借</button>
          <button 
            @click="borrowBook(book.isbn)"
            :disabled="book.availableQuantity === 0"
          >借阅</button>
          <button @click="renewBook(book.isbn)">续借</button>
          <button @click="returnBook(book.isbn)">归还</button>
        </div>
      </div>
    </div>
    
    <div class="no-results" v-else-if="hasSearched">
      未找到相关图书
    </div>
  </div>
</template>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.search-section {
  margin: 20px 0;
}

.search-box {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-bottom: 20px;
}

.search-box input {
  width: 300px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.search-btn {
  padding: 8px 20px;
  background-color: #42b883;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-btn:hover {
  background-color: #3aa876;
}

.books-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 20px 0;
}

.book-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.book-info h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.book-info p {
  margin: 5px 0;
  font-size: 14px;
}

.book-info span {
  color: #666;
  font-weight: bold;
}

.book-actions {
  display: flex;
  gap: 8px;
  margin-top: 15px;
  flex-wrap: wrap;
}

.book-actions button {
  flex: 1;
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  min-width: 60px;
}

.book-actions button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.no-results {
  text-align: center;
  padding: 40px;
  color: #666;
  font-size: 16px;
}

.user-menu {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 100;
}

.user-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #42b883;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s;
}

.user-icon:hover {
  background-color: #3aa876;
}

.dropdown-menu {
  position: absolute;
  top: 50px;
  right: 0;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  min-width: 200px;
  padding: 10px 0;
}

.user-info {
  padding: 10px 15px;
}

.divider {
  height: 1px;
  background-color: #eee;
  margin: 5px 0;
}

.menu-item {
  padding: 10px 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

.menu-item:hover {
  background-color: #f5f5f5;
}

.nav-menu {
  margin: 20px 0;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background-color: #42b883;
  color: white;
  text-decoration: none;
  border-radius: 4px;
}

.nav-item:hover {
  background-color: #3aa876;
}
</style>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import axios from '../utils/axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const showDropdown = ref(false)
const username = ref(localStorage.getItem('username'))
const userRole = ref(localStorage.getItem('role'))

// 添加调试信息
console.log('Current user role:', userRole.value)

// 切换下拉菜单显示状态
const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value
}

// 处理退出登录
const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}

// 点击其他区域关闭下拉菜单
const closeDropdown = (e) => {
  if (!e.target.closest('.user-menu')) {
    showDropdown.value = false
  }
}

// 添加全局点击事件监听
onMounted(() => {
  document.addEventListener('click', closeDropdown)
})

onUnmounted(() => {
  document.removeEventListener('click', closeDropdown)
})

const searchQuery = ref('')
const books = ref([])
const hasSearched = ref(false)

const searchBooks = async () => {
  if (!searchQuery.value.trim()) return
  
  try {
    const response = await axios.get(`/api/book/searchByTitle?title=${searchQuery.value}`)
    console.log(response.data.data)
    books.value = response.data.data
    hasSearched.value = true
  } catch (error) {
    console.error('查询图书失败', error)
    books.value = []
  }
}

const reserveBook = async (isbn) => {
  try {
    await axios.post('/api/reservation/reserve', { 
      isbn, 
      expectedBorrowDate: new Date().toISOString().split('T')[0] 
    })
    alert('预借成功')
    searchBooks() // 刷新图书列表
  } catch (error) {
    alert(error.response?.data?.message || '预借失败')
  }
}

const borrowBook = async (isbn) => {
  try {
    await axios.post('/api/reservation/borrow', { isbn })
    alert('借阅成功')
    searchBooks() // 刷新图书列表
  } catch (error) {
    alert(error.response?.data?.message || '借阅失败')
  }
}

const renewBook = async (isbn) => {
  try {
    await axios.post('/api/reservation/renew', { isbn })
    alert('续借成功')
    searchBooks() // 刷新图书列表
  } catch (error) {
    alert(error.response?.data?.message || '续借失败')
  }
}

const returnBook = async (isbn) => {
  try {
    await axios.post('/api/reservation/return', { isbn })
    alert('归还成功')
    searchBooks() // 刷新图书列表
  } catch (error) {
    alert(error.response?.data?.message || '归还失败')
  }
}
</script>