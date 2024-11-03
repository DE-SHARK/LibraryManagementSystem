<template>
  <div class="borrower-management">
    <h2>借阅者管理</h2>
    
    <!-- 添加借阅者按钮 -->
    <div class="action-bar">
      <button class="add-btn" @click="showAddDialog = true">添加借阅者</button>
    </div>

    <!-- 借阅者列表 -->
    <div class="borrower-list">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="borrower in borrowers" :key="borrower.id">
            <td>{{ borrower.id }}</td>
            <td>{{ borrower.username }}</td>
            <td>
              <button @click="editBorrower(borrower)">编辑</button>
              <button @click="viewHistory(borrower.id)">借阅历史</button>
              <button @click="deleteBorrower(borrower.id)" class="delete-btn">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 添加/编辑对话框 -->
    <div v-if="showAddDialog" class="dialog-overlay">
      <div class="dialog">
        <h3>{{ isEditing ? '编辑借阅者' : '添加借阅者' }}</h3>
        <form @submit.prevent="submitBorrower">
          <div class="form-group">
            <label>用户名</label>
            <input v-model="borrowerForm.username" required>
          </div>
          <div class="form-group">
            <label>密码</label>
            <input type="password" v-model="borrowerForm.password" :required="!isEditing">
            <small v-if="isEditing">留空表示不修改密码</small>
          </div>
          <div class="dialog-buttons">
            <button type="submit">确定</button>
            <button type="button" @click="showAddDialog = false">取消</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 借阅历史对话框 -->
    <div v-if="showHistoryDialog" class="dialog-overlay">
      <div class="dialog history-dialog">
        <h3>借阅历史</h3>
        <div class="history-list">
          <table>
            <thead>
              <tr>
                <th>书名</th>
                <th>借阅时间</th>
                <th>归还时间</th>
                <th>状态</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(record, index) in borrowHistory" :key="index">
                <td>{{ record.bookTitle }}</td>
                <td>{{ formatDate(record.borrowDate) }}</td>
                <td>{{ record.returnDate ? formatDate(record.returnDate) : '-' }}</td>
                <td>{{ record.status }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="dialog-buttons">
          <button @click="showHistoryDialog = false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'

const borrowers = ref([])
const showAddDialog = ref(false)
const showHistoryDialog = ref(false)
const isEditing = ref(false)
const borrowHistory = ref([])
const borrowerForm = ref({
  id: null,
  username: '',
  password: ''
})

// 获取所有借阅者
const fetchBorrowers = async () => {
  try {
    const response = await axios.get('/admin/user/borrowers')
    borrowers.value = response.data.data
  } catch (error) {
    console.error('获取借阅者列表失败:', error)
  }
}

// 编辑借阅者
const editBorrower = (borrower) => {
  isEditing.value = true
  borrowerForm.value = {
    id: borrower.id,
    username: borrower.username,
    password: ''
  }
  showAddDialog.value = true
}

// 查看借阅历史
const viewHistory = async (id) => {
  try {
    const response = await axios.get(`/admin/user/borrower/${id}/history`)
    borrowHistory.value = response.data.data
    showHistoryDialog.value = true
  } catch (error) {
    alert(error.response?.data?.message || '获取借阅历史失败')
  }
}

// 删除借阅者
const deleteBorrower = async (id) => {
  if (!confirm('确定要删除这个借阅者吗？')) return
  
  try {
    await axios.delete(`/admin/user/borrower/${id}`)
    await fetchBorrowers()
  } catch (error) {
    alert(error.response?.data?.message || '删除失败')
  }
}

// 提交表单
const submitBorrower = async () => {
  try {
    if (isEditing.value) {
      await axios.put(`/admin/user/borrower/${borrowerForm.value.id}`, borrowerForm.value)
    } else {
      await axios.post('/admin/user/borrower', borrowerForm.value)
    }
    showAddDialog.value = false
    await fetchBorrowers()
    // 重置表单
    borrowerForm.value = { id: null, username: '', password: '' }
    isEditing.value = false
  } catch (error) {
    alert(error.response?.data?.message || '操作失败')
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(fetchBorrowers)
</script>

<style scoped>
.borrower-management {
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

.history-dialog {
  width: 800px;
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
