<template>
    <div class="my-borrows">
      <h2>我的借阅</h2>
      
      <!-- 借阅状态统计 -->
      <div class="borrow-stats">
        <div class="stat-card">
          <h4>当前借阅</h4>
          <p>{{ stats.currentBorrows || 0 }}</p>
        </div>
        <div class="stat-card">
          <h4>即将到期</h4>
          <p>{{ stats.dueBooks || 0 }}</p>
        </div>
        <div class="stat-card">
          <h4>已逾期</h4>
          <p>{{ stats.overdueBooks || 0 }}</p>
        </div>
      </div>
  
      <!-- 借阅列表 -->
      <div class="borrow-list">
        <h3>借阅记录</h3>
        <table>
          <thead>
            <tr>
              <th>书名</th>
              <th>借阅时间</th>
              <th>应还时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="record in borrowRecords" :key="record.id">
              <td>{{ record.bookTitle }}</td>
              <td>{{ formatDate(record.borrowDate) }}</td>
              <td>{{ formatDate(record.dueDate) }}</td>
              <td :class="getStatusClass(record.status)">{{ getStatusText(record.status) }}</td>
              <td>
                <button 
                  v-if="record.status === 'BORROWED'"
                  @click="handleReturn(record.isbn)"
                >归还</button>
                <button 
                  v-if="record.status === 'BORROWED' && !record.renewed"
                  @click="handleRenew(record.isbn)"
                >续借</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
  
      <!-- 预约列表 -->
      <div class="reservation-list">
        <h3>预约记录</h3>
        <table>
          <thead>
            <tr>
              <th>书名</th>
              <th>预约时间</th>
              <th>预计借阅时间</th>
              <th>状态</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="record in reservations" :key="record.id">
              <td>{{ record.bookTitle }}</td>
              <td>{{ formatDate(record.reservationDate) }}</td>
              <td>{{ formatDate(record.expectedBorrowDate) }}</td>
              <td>{{ record.status }}</td>
              <td>
                <button 
                  v-if="record.status === 'PENDING'"
                  @click="cancelReservation(record.id)"
                >取消预约</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import axios from '../utils/axios'
  
  const borrowRecords = ref([])
  const reservations = ref([])
  const stats = ref({})
  
  // 获取借阅记录
  const fetchBorrowRecords = async () => {
    try {
      const response = await axios.get('/api/borrow/my-records')
      borrowRecords.value = response.data.data
    } catch (error) {
      console.error('获取借阅记录失败:', error)
    }
  }
  
  // 获取预约记录
  const fetchReservations = async () => {
    try {
      const response = await axios.get('/api/reservation/my-records')
      reservations.value = response.data.data
    } catch (error) {
      console.error('获取预约记录失败:', error)
    }
  }
  
  // 获取借阅统计
  const fetchStats = async () => {
    try {
      const response = await axios.get('/api/borrow/my-stats')
      stats.value = response.data.data
    } catch (error) {
      console.error('获取借阅统计失败:', error)
    }
  }
  
  // 处理归还
  const handleReturn = async (isbn) => {
    try {
      await axios.post('/api/borrow/return', { isbn })
      await fetchBorrowRecords()
      await fetchStats()
    } catch (error) {
      alert(error.response?.data?.message || '归还失败')
    }
  }
  
  // 处理续借
  const handleRenew = async (isbn) => {
    try {
      await axios.post('/api/borrow/renew', { isbn })
      await fetchBorrowRecords()
      await fetchStats()
    } catch (error) {
      alert(error.response?.data?.message || '续借失败')
    }
  }
  
  // 取消预约
  const cancelReservation = async (id) => {
    try {
      await axios.delete(`/api/reservation/cancel/${id}`)
      await fetchReservations()
    } catch (error) {
      alert(error.response?.data?.message || '取消预约失败')
    }
  }
  
  // 格式化日期
  const formatDate = (dateStr) => {
    return new Date(dateStr).toLocaleDateString('zh-CN')
  }
  
  // 获取状态样式类
  const getStatusClass = (status) => {
    return {
      'status-borrowed': status === 'BORROWED',
      'status-returned': status === 'RETURNED',
      'status-overdue': status === 'OVERDUE'
    }
  }
  
  // 获取状态文本
  const getStatusText = (status) => {
    const statusMap = {
      'BORROWED': '借阅中',
      'RETURNED': '已归还',
      'OVERDUE': '已逾期'
    }
    return statusMap[status] || status
  }
  
  onMounted(() => {
    fetchBorrowRecords()
    fetchReservations()
    fetchStats()
  })
  </script>
  
  <style scoped>
  .my-borrows {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
  }
  
  .borrow-stats {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    margin-bottom: 30px;
  }
  
  .stat-card {
    background-color: #f5f5f5;
    padding: 20px;
    border-radius: 8px;
    text-align: center;
  }
  
  .stat-card h4 {
    margin: 0;
    color: #666;
  }
  
  .stat-card p {
    margin: 10px 0 0;
    font-size: 24px;
    color: #42b883;
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 30px;
  }
  
  th, td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  th {
    background-color: #f5f5f5;
  }
  
  .status-borrowed {
    color: #42b883;
  }
  
  .status-returned {
    color: #666;
  }
  
  .status-overdue {
    color: #ff4444;
  }
  
  button {
    padding: 6px 12px;
    border: none;
    border-radius: 4px;
    background-color: #42b883;
    color: white;
    cursor: pointer;
    margin-right: 8px;
  }
  
  button:hover {
    background-color: #3aa876;
  }
  
  h3 {
    margin: 30px 0 20px;
    color: #333;
  }
  </style>