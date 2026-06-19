<template>
  <div class="reservation-list-page">
    <div class="filter-tabs">
      <button v-for="tab in tabs" :key="tab.value" class="tab-btn" :class="{ active: activeTab === tab.value }" @click="activeTab = tab.value">
        {{ tab.label }}
      </button>
    </div>
    
    <div class="reservation-list">
      <div v-for="reservation in filteredReservations" :key="reservation.id" class="reservation-card">
        <div class="card-header">
          <h3>{{ getLaboratoryName(reservation.laboratoryId) }}</h3>
          <span class="status-tag" :class="getStatusClass(reservation.status)">
            {{ getStatusText(reservation.status) }}
          </span>
        </div>
        <div class="card-info">
          <p><el-icon size="16" color="#409eff"><Calendar /></el-icon> {{ reservation.reservationDate }}</p>
          <p><el-icon size="16" color="#67c23a"><Clock /></el-icon> {{ getTimeText(reservation.reservationTimeId) }}</p>
        </div>
        <div class="card-footer">
          <button v-if="reservation.status === 'PENDING'" class="cancel-btn" @click="handleCancel(reservation.id)">
            <el-icon><CircleClose /></el-icon>
            取消预约
          </button>
          <span v-else class="action-text">已{{ reservation.status === 'COMPLETED' ? '完成' : '取消' }}</span>
        </div>
      </div>
    </div>
    
    <div v-if="filteredReservations.length === 0" class="empty-state">
      <el-icon size="64" color="#909399"><Folder /></el-icon>
      <p>暂无预约记录</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { Calendar, Clock, CircleClose, Folder } from '@element-plus/icons-vue'
import axios from '../axios'

const store = useStore()
const reservations = ref([])
const laboratories = ref({})
const reservationTimes = ref({})
const activeTab = ref('all')

const tabs = [
  { label: '全部', value: 'all' },
  { label: '待使用', value: 'PENDING' },
  { label: '已使用', value: 'COMPLETED' },
  { label: '已取消', value: 'CANCELLED' }
]

const filteredReservations = computed(() => {
  if (activeTab.value === 'all') return reservations.value
  return reservations.value.filter(r => r.status === activeTab.value)
})

const getLaboratoryName = (id) => laboratories.value[id] || '未知实验室'
const getTimeText = (id) => {
  const time = reservationTimes.value[id]
  return time ? `${time.startTime} - ${time.endTime}` : '未知时段'
}

const getStatusText = (status) => {
  const map = { PENDING: '待使用', COMPLETED: '已使用', CANCELLED: '已取消', REJECTED: '已驳回' }
  return map[status] || status
}

const getStatusClass = (status) => {
  const map = { PENDING: 'pending', COMPLETED: 'completed', CANCELLED: 'cancelled', REJECTED: 'rejected' }
  return map[status] || ''
}

const fetchReservations = async () => {
  try {
    const response = await axios.get(`/reservations/user/${store.getters.userId}`)
    reservations.value = response.data
    await fetchLaboratories()
    await fetchReservationTimes()
  } catch (error) {
    console.error('Failed to fetch reservations:', error)
  }
}

const fetchLaboratories = async () => {
  try {
    const response = await axios.get('/laboratories')
    response.data.forEach(lab => { laboratories.value[lab.id] = lab.name })
  } catch (error) {
    console.error('Failed to fetch laboratories:', error)
  }
}

const fetchReservationTimes = async () => {
  try {
    const response = await axios.get('/reservation-times')
    response.data.forEach(time => { reservationTimes.value[time.id] = time })
  } catch (error) {
    console.error('Failed to fetch reservation times:', error)
  }
}

const handleCancel = async (id) => {
  if (!confirm('确定要取消该预约吗？')) return
  try {
    await axios.post(`/reservations/${id}/cancel`, null, { params: { userId: store.getters.userId } })
    alert('取消成功')
    fetchReservations()
  } catch (error) {
    const message = error.response?.data || '取消失败'
    alert(message)
  }
}

onMounted(() => {
  fetchReservations()
})
</script>

<style scoped>
.reservation-list-page { max-width: 800px; margin: 0 auto; }
.filter-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 20px;
  background-color: white;
  padding: 8px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.tab-btn {
  flex: 1;
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  background-color: transparent;
  cursor: pointer;
  transition: all 0.2s;
}
.tab-btn:hover { background-color: #f3f4f6; }
.tab-btn.active { background-color: #409eff; color: white; }
.reservation-list { display: flex; flex-direction: column; gap: 16px; }
.reservation-card {
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.card-header h3 { font-size: 18px; margin: 0; }
.status-tag { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.status-tag.pending { background-color: #fff3e0; color: #e65100; }
.status-tag.completed { background-color: #e8f5e9; color: #2e7d32; }
.status-tag.cancelled { background-color: #f5f5f5; color: #757575; }
.status-tag.rejected { background-color: #ffebee; color: #c62828; }
.card-info p { margin: 8px 0; display: flex; align-items: center; gap: 8px; color: #6b7280; }
.card-footer { margin-top: 12px; padding-top: 12px; border-top: 1px solid #f3f4f6; display: flex; justify-content: flex-end; }
.cancel-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  background-color: #ffebee;
  color: #c62828;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
}
.cancel-btn:hover { background-color: #ffcdd2; }
.action-text { color: #909399; font-size: 14px; }
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.empty-state p { margin-top: 16px; color: #909399; }
</style>