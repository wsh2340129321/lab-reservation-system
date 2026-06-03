<template>
  <div class="admin-page">
    <div class="content-header">
      <h2>预约管理</h2>
      <el-button type="primary" @click="exportReservations">导出预约记录</el-button>
    </div>
    <el-table :data="reservations" border>
      <el-table-column prop="id" label="预约ID" />
      <el-table-column prop="laboratoryName" label="实验室名称" />
      <el-table-column prop="userName" label="用户姓名" />
      <el-table-column prop="studentId" label="学号" />
      <el-table-column prop="reservationDate" label="预约日期" />
      <el-table-column prop="startTime" label="开始时间" />
      <el-table-column prop="endTime" label="结束时间" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <span :class="getStatusClass(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="rejectReason" label="驳回理由" show-overflow-tooltip />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button v-if="scope.row.status === 'PENDING' || scope.row.status === 'CONFIRMED'" size="small" type="warning" @click="rejectReservation(scope.row)">驳回</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../axios'
import { ElMessageBox } from 'element-plus'

const reservations = ref([])

const fetchReservations = async () => {
  try {
    const response = await axios.get('/reservations')
    reservations.value = response.data
  } catch (error) {
    console.error('Failed to fetch reservations:', error)
  }
}

const rejectReservation = async (reservation) => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入驳回理由', '驳回预约', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入驳回理由（选填）'
    })
    await axios.post(`/reservations/${reservation.id}/reject`, null, {
      params: { 
        reason: reason || ''
      }
    })
    fetchReservations()
    alert('驳回成功')
  } catch (error) {
    if (error !== 'cancel') {
      const message = error.response?.data || '驳回失败'
      alert(message)
    }
  }
}

const exportReservations = async () => {
  try {
    const response = await axios.get('/reservations/export', { responseType: 'blob' })
    const blob = new Blob([response.data], { type: 'text/csv;charset=UTF-8' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url; a.download = 'reservations.csv'; a.click(); URL.revokeObjectURL(url)
  } catch (error) {
    console.error('Failed to export reservations:', error)
  }
}

const getStatusText = (status) => {
  const map = { PENDING: '待使用', CONFIRMED: '已确认', COMPLETED: '已使用', CANCELLED: '已取消', REJECTED: '已驳回' }
  return map[status] || status
}

const getStatusClass = (status) => {
  const map = { 
    PENDING: 'status-pending', 
    CONFIRMED: 'status-confirmed',
    COMPLETED: 'status-completed', 
    CANCELLED: 'status-cancelled', 
    REJECTED: 'status-rejected' 
  }
  return map[status] || ''
}

onMounted(() => {
  fetchReservations()
})
</script>

<style scoped>
.admin-page { max-width: 1200px; margin: 0 auto; }
.content-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.content-header h2 { font-size: 20px; }
.status-pending { color: #e65100; font-weight: 500; }
.status-confirmed { color: #1565c0; font-weight: 500; }
.status-completed { color: #2e7d32; font-weight: 500; }
.status-cancelled { color: #757575; font-weight: 500; }
.status-rejected { color: #c62828; font-weight: 500; }
</style>
