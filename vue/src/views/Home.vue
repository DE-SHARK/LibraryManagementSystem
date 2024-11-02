<template>
  <div class="home-container">
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
</style>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

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