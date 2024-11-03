<template>
    <div class="librarian-management">
      <h2>图书管理员管理</h2>
      
      <!-- 添加图书管理员按钮 -->
      <div class="action-bar">
        <button class="add-btn" @click="showAddDialog = true">添加图书管理员</button>
      </div>
  
      <!-- 图书管理员列表 -->
      <div class="librarian-list">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>用户名</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="librarian in librarians" :key="librarian.id">
              <td>{{ librarian.id }}</td>
              <td>{{ librarian.username }}</td>
              <td>
                <button @click="editLibrarian(librarian)">编辑</button>
                <button @click="deleteLibrarian(librarian.id)" class="delete-btn">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
  
      <!-- 添加/编辑对话框 -->
      <div v-if="showAddDialog" class="dialog-overlay">
        <div class="dialog">
          <h3>{{ isEditing ? '编辑图书管理员' : '添加图书管理员' }}</h3>
          <form @submit.prevent="submitLibrarian">
            <div class="form-group">
              <label>用户名</label>
              <input v-model="librarianForm.username" required>
            </div>
            <div class="form-group">
              <label>密码</label>
              <input type="password" v-model="librarianForm.password" :required="!isEditing">
              <small v-if="isEditing">留空表示不修改密码</small>
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
  
  const librarians = ref([])
  const showAddDialog = ref(false)
  const isEditing = ref(false)
  const librarianForm = ref({
    id: null,
    username: '',
    password: ''
  })
  
  // 获取所有图书管理员
  const fetchLibrarians = async () => {
    try {
      const response = await axios.get('/admin/librarian/list')
      librarians.value = response.data.data
    } catch (error) {
      console.error('获取图书管理员列表失败:', error)
    }
  }
  
  // 编辑图书管理员
  const editLibrarian = (librarian) => {
    isEditing.value = true
    librarianForm.value = { 
      id: librarian.id,
      username: librarian.username,
      password: '' 
    }
    showAddDialog.value = true
  }
  
  // 删除图书管理员
  const deleteLibrarian = async (id) => {
    if (!confirm('确定要删除这个图书管理员吗？')) return
    
    try {
      await axios.delete(`/admin/librarian/delete/${id}`)
      await fetchLibrarians()
    } catch (error) {
      alert(error.response?.data?.message || '删除失败')
    }
  }
  
  // 提交表单
  const submitLibrarian = async () => {
    try {
      if (isEditing.value) {
        await axios.put(`/admin/librarian/update/${librarianForm.value.id}`, librarianForm.value)
      } else {
        await axios.post('/admin/librarian/add', librarianForm.value)
      }
      showAddDialog.value = false
      await fetchLibrarians()
      // 重置表单
      librarianForm.value = { id: null, username: '', password: '' }
      isEditing.value = false
    } catch (error) {
      alert(error.response?.data?.message || '操作失败')
    }
  }
  
  onMounted(fetchLibrarians)
  </script>
  
  <style scoped>
  .librarian-management {
    padding: 20px;
  }
  
  .action-bar {
    margin-bottom: 20px;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
  }
  
  th, td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  th {
    background-color: #f5f5f5;
  }
  
  .delete-btn {
    background-color: #ff4444;
    margin-left: 8px;
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
    width: 400px;
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
  
  small {
    color: #666;
    font-size: 12px;
  }
  </style>