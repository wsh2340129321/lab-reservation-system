<template>
  <div class="laboratory-detail-page">
    <div class="detail-header">
      <button @click="goBack" class="back-btn">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </button>
      <h1>{{ laboratory.name }}</h1>
    </div>
    
    <div class="detail-content" v-if="laboratory">
      <div class="info-section">
        <h2>基本信息</h2>
        <div class="info-grid">
          <div class="info-item">
            <span class="label">位置</span>
            <span class="value">{{ laboratory.location }}</span>
          </div>
          <div class="info-item">
            <span class="label">类型</span>
            <span class="value">{{ laboratory.type }}</span>
          </div>
          <div class="info-item">
            <span class="label">容量</span>
            <span class="value">{{ laboratory.capacity }}人</span>
          </div>
          <div class="info-item">
            <span class="label">状态</span>
            <span class="value" :class="laboratory?.status?.toLowerCase()">{{ laboratory?.status === 'AVAILABLE' ? '可预约' : '不可预约' }}</span>
          </div>
        </div>
      </div>
      
      <div class="equipment-section">
        <h2>设备配置</h2>
        <p>{{ laboratory.equipment }}</p>
      </div>
      
      <div class="rules-section">
        <h2>开放规则</h2>
        <p>{{ laboratory.rules }}</p>
      </div>
      
      <div class="notes-section">
        <h2>使用须知</h2>
        <p>{{ laboratory.notes }}</p>
      </div>
      
      <div class="booking-section" v-if="laboratory.status === 'AVAILABLE'">
        <h2>预约时段</h2>
        <el-date-picker 
          v-model="selectedDate" 
          type="date" 
          placeholder="选择日期" 
          :disabled-date="disabledDate"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="fetchAvailableTimes" />
        
        <div class="time-slots" v-if="availableTimes.length > 0">
          <div v-for="time in availableTimes" :key="time.id" class="time-slot" :class="{ available: time.isAvailable, booked: !time.isAvailable, selected: selectedTime && selectedTime.id === time.id }" @click="time.isAvailable && selectTime(time)">
            <span>{{ time.startTime }} - {{ time.endTime }}</span>
            <span class="slot-status">{{ time.isAvailable ? '可预约' : '已预约' }}</span>
          </div>
        </div>
        
        <el-button v-if="selectedTime" type="primary" @click="handleBooking" class="booking-btn">确认预约</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ArrowLeft } from '@element-plus/icons-vue'
import axios from '../axios'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const store = useStore()

const laboratory = ref({})
const availableTimes = ref([])
const selectedDate = ref(dayjs().format('YYYY-MM-DD'))
const selectedTime = ref(null)

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 24 * 60 * 60 * 1000
}

const goBack = () => {
  router.push('/laboratories')
}

const fetchLaboratory = async () => {
  try {
    const response = await axios.get(`/laboratories/${route.params.id}`)
    laboratory.value = response.data
  } catch (error) {
    console.error('Failed to fetch laboratory:', error)
  }
}

const fetchAvailableTimes = async () => {
  try {
    const response = await axios.get(`/laboratories/${route.params.id}/available-times`, {
      params: { date: selectedDate.value }
    })
    availableTimes.value = response.data
    selectedTime.value = null
  } catch (error) {
    console.error('Failed to fetch available times:', error)
  }
}

const selectTime = (time) => {
  selectedTime.value = time
}

const handleBooking = async () => {
  if (!selectedTime.value || !selectedDate.value) return
  
  try {
    await axios.post('/reservations', {
      userId: store.getters.userId,
      laboratoryId: route.params.id,
      reservationTimeId: selectedTime.value.id,
      reservationDate: selectedDate.value
    })
    alert('预约成功！')
    await store.dispatch('fetchNotifications')
    fetchAvailableTimes()
  } catch (error) {
    const message = error.response?.data || '预约失败'
    alert(message)
  }
}

onMounted(() => {
  fetchLaboratory()
  fetchAvailableTimes()
})
</script>

<style scoped>
.laboratory-detail-page { max-width: 800px; margin: 0 auto; }
.detail-header { display: flex; align-items: center; gap: 16px; margin-bottom: 24px; }
.back-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 16px;
  background-color: #f3f4f6;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
}
.back-btn:hover { background-color: #e5e7eb; }
.detail-header h1 { font-size: 28px; }
.detail-content {
  background-color: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.info-section, .equipment-section, .rules-section, .notes-section, .booking-section { margin-bottom: 24px; }
.info-section h2, .equipment-section h2, .rules-section h2, .notes-section h2, .booking-section h2 {
  font-size: 18px;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid #409eff;
}
.info-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }
.info-item { display: flex; flex-direction: column; }
.info-item .label { color: #6b7280; font-size: 14px; margin-bottom: 4px; }
.info-item .value { font-weight: 500; }
.info-item .value.available { color: #2e7d32; }
.info-item .value.unavailable { color: #c62828; }
.equipment-section p, .rules-section p, .notes-section p { color: #4b5563; line-height: 1.8; }
.time-slots { display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr)); gap: 12px; margin: 16px 0; }
.time-slot {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
}
.time-slot.available { background-color: #e8f5e9; border: 2px solid #4caf50; }
.time-slot.available:hover { background-color: #c8e6c9; }
.time-slot.available.selected { background-color: #4caf50; border: 2px solid #2e7d32; color: white; transform: scale(1.02); }
.time-slot.available.selected .slot-status { background-color: white; color: #4caf50; }
.time-slot.booked { background-color: #ffebee; border: 2px solid #ef5350; cursor: not-allowed; }
.slot-status { font-size: 12px; padding: 2px 8px; border-radius: 10px; }
.time-slot.available .slot-status { background-color: #4caf50; color: white; }
.time-slot.booked .slot-status { background-color: #ef5350; color: white; }
.booking-btn { width: 100%; padding: 12px; font-size: 16px; }
</style>