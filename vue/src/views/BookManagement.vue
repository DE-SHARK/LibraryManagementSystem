<template>
  <div class="book-management">
    <h2>图书管理</h2>
    
    <!-- 搜索和添加区域 -->
    <div class="action-bar">
      <div class="search-box">
        <input 
          v-model="searchQuery" 
          placeholder="输入书名、作者或ISBN" 
          @keyup.enter="searchBooks"
        />
        <button @click="searchBooks">搜索</button>
      </div>
      <button class="add-btn" @click="showAddDialog = true">添加图书</button>
    </div>

    <!-- 图书列表 -->
    <div class="book-list">
      <table>
        <thead>
          <tr>
            <th>ISBN</th>
            <th>书名</th>
            <th>作者</th>
            <th>分类号</th>
            <th>馆藏地</th>
            <th>总数量</th>
            <th>可借数量</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="book in books" :key="book.isbn">
            <td>{{ book.isbn }}</td>
            <td>{{ book.title }}</td>
            <td>{{ book.author }}</td>
            <td>{{ book.clcNumber }}</td>
            <td>{{ book.location }}</td>
            <td>{{ book.totalQuantity }}</td>
            <td>{{ book.availableQuantity }}</td>
            <td>
              <button @click="editBook(book)">编辑</button>
              <button @click="deleteBook(book.isbn)" class="delete-btn">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑图书对话框 -->
    <div v-if="showAddDialog" class="dialog-overlay">
      <div class="dialog">
        <h3>{{ isEditing ? '编辑图书' : '添加图书' }}</h3>
        <form @submit.prevent="submitBook">
          <div class="form-group">
            <label>ISBN</label>
            <input v-model="bookForm.isbn" :disabled="isEditing" required>
          </div>
          <div class="form-group">
            <label>书名</label>
            <input v-model="bookForm.title" required>
          </div>
          <div class="form-group">
            <label>作者</label>
            <input v-model="bookForm.author" required>
          </div>
          <div class="form-group">
            <label>分类号</label>
            <input v-model="bookForm.clcNumber" required>
          </div>
          <div class="form-group">
            <label>馆藏地</label>
            <input v-model="bookForm.location" required>
          </div>
          <div class="form-group">
            <label>数量</label>
            <input type="number" v-model="bookForm.totalQuantity" required>
          </div>
          <div class="dialog-buttons">
            <button type="submit">确定</button>
            <button type="button" @click="showAddDialog = false">取消</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'

const books = ref([])
const searchQuery = ref('')
const showAddDialog = ref(false)
const isEditing = ref(false)
const bookForm = ref({
  isbn: '',
  title: '',
  author: '',
  clcNumber: '',
  location: '',
  totalQuantity: 1,
  availableQuantity: 1
})

// 获取所有图书
const fetchBooks = async () => {
  try {
    const response = await axios.get('/admin/book/list')
    books.value = response.data.data
  } catch (error) {
    console.error('获取图书列表失败:', error)
  }
}

// 搜索图书
const searchBooks = async () => {
  if (!searchQuery.value) {
    await fetchBooks()
    return
  }
  try {
    const response = await axios.get(`/book/searchByTitle?title=${searchQuery.value}`)
    books.value = response.data.data
  } catch (error) {
    console.error('��索图书失败:', error)
  }
}

// 编辑图书
const editBook = (book) => {
  isEditing.value = true
  bookForm.value = { ...book }
  showAddDialog.value = true
}

// 删除图书
const deleteBook = async (isbn) => {
  if (!confirm('确定要删除这本图书吗？')) return
  
  try {
    await axios.delete(`/admin/book/delete/${isbn}`)
    await fetchBooks()
  } catch (error) {
    alert(error.response?.data?.message || '删除图书失败')
  }
}

// 提交图书表单
const submitBook = async () => {
  try {
    if (isEditing.value) {
      await axios.put('/admin/book/update', bookForm.value)
    } else {
      await axios.post('/admin/book/add', bookForm.value)
    }
    showAddDialog.value = false
    await fetchBooks()
  } catch (error) {
    alert(error.response?.data?.message || '操作失败')
  }
}

onMounted(fetchBooks)
</script>

<style scoped>
.book-management {
  padding: 20px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.search-box {
  display: flex;
  gap: 10px;
}

.search-box input {
  width: 300px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  background-color: #42b883;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #3aa876;
}

.delete-btn {
  background-color: #ff4444;
  margin-left: 8px;
}

.delete-btn:hover {
  background-color: #cc0000;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f5f5f5;
}

.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.dialog {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 500px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.dialog-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}
</style>
