<template>
  <div class="admin-page">
    <div class="content-header">
      <h2>用户管理</h2>
    </div>
    <el-table :data="users" border>
      <el-table-column prop="studentId" label="学号" />
      <el-table-column prop="username" label="姓名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <span :class="scope.row.status.toLowerCase() === 'active' ? 'status-active' : 'status-blocked'">
            {{ scope.row.status === 'ACTIVE' ? '正常' : '封禁' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="viewUserReservations(scope.row)">查看预约</el-button>
          <el-button size="small" :type="scope.row.status === 'ACTIVE' ? 'danger' : 'success'" @click="toggleUserStatus(scope.row)">
            {{ scope.row.status === 'ACTIVE' ? '封禁' : '解封' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showUserReservations" :title="`用户 ${currentUser.username} 的预约记录`" width="90%">
      <el-table :data="userReservations" border>
        <el-table-column prop="id" label="预约ID" />
        <el-table-column prop="laboratoryId" label="实验室ID" />
        <el-table-column prop="reservationDate" label="预约日期" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <span :class="getStatusClass(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button v-if="scope.row.status === 'PENDING'" size="small" type="warning" @click="rejectReservation(scope.row.id)">驳回</el-button>
            <el-button v-if="scope.row.status === 'PENDING' || scope.row.status === 'CONFIRMED'" size="small" type="danger" @click="cancelReservation(scope.row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../axios'

const users = ref([])
const showUserReservations = ref(false)
const userReservations = ref([])
const currentUser = ref({ id: null, username: '' })

const fetchUsers = async () => {
  try {
    const response = await axios.get('/users')
    users.value = response.data
  } catch (error) {
    console.error('Failed to fetch users:', error)
  }
}

const viewUserReservations = async (user) => {
  currentUser.value = user
  showUserReservations.value = true
  try {
    const response = await axios.get(`/reservations/user/${user.id}`)
    userReservations.value = response.data
  } catch (error) {
    console.error('Failed to fetch user reservations:', error)
  }
}

const toggleUserStatus = async (user) => {
  const newStatus = user.status === 'ACTIVE' ? 'BLOCKED' : 'ACTIVE'
  try {
    await axios.put(`/users/${user.id}/status`, null, { params: { status: newStatus } })
    user.status = newStatus
    alert(`用户已${newStatus === 'ACTIVE' ? '解封' : '封禁'}`)
  } catch (error) {
    const message = error.response?.data || '操作失败'
    alert(message)
  }
}

const rejectReservation = async (id) => {
  if (!confirm('确定要驳回该预约吗？')) return
  try {
    await axios.post(`/reservations/${id}/reject`)
    await viewUserReservations(currentUser.value)
    alert('驳回成功')
  } catch (error) {
    const message = error.response?.data || '驳回失败'
    alert(message)
  }
}

const cancelReservation = async (reservation) => {
  if (!confirm('确定要取消该预约吗？')) return
  try {
    await axios.post(`/reservations/${reservation.id}/cancel-by-admin`, null, {
      params: { userId: reservation.userId }
    })
    await viewUserReservations(currentUser.value)
    alert('取消成功')
  } catch (error) {
    const message = error.response?.data || '取消失败'
    alert(message)
  }
}

const getStatusText = (status) => {
  const map = { PENDING: '待使用', COMPLETED: '已使用', CANCELLED: '已取消', REJECTED: '已驳回', CONFIRMED: '已确认' }
  return map[status] || status
}

const getStatusClass = (status) => {
  const map = { 
    PENDING: 'status-pending', 
    COMPLETED: 'status-completed', 
    CANCELLED: 'status-cancelled', 
    REJECTED: 'status-rejected',
    CONFIRMED: 'status-confirmed'
  }
  return map[status] || ''
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.admin-page { max-width: 1200px; margin: 0 auto; }
.content-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.content-header h2 { font-size: 20px; }
.status-active { color: #2e7d32; font-weight: 500; }
.status-blocked { color: #c62828; font-weight: 500; }
.status-pending { color: #e65100; font-weight: 500; }
.status-completed { color: #2e7d32; font-weight: 500; }
.status-cancelled { color: #757575; font-weight: 500; }
.status-rejected { color: #c62828; font-weight: 500; }
.status-confirmed { color: #1565c0; font-weight: 500; }
</style>
