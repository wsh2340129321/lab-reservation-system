<template>
  <div class="laboratory-list-page">
    <div class="search-bar">
      <el-input v-model="searchKeyword" placeholder="搜索实验室名称、位置或类型" prefix-icon="Search" class="search-input" />
      <el-select v-model="filterType" placeholder="实验室类型" class="filter-select">
        <el-option label="全部" value="" />
        <el-option label="计算机" value="计算机" />
        <el-option label="化学" value="化学" />
        <el-option label="物理" value="物理" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>
    
    <div class="laboratory-list">
      <div class="available-section">
        <h2 class="section-title">可预约实验室</h2>
        <div v-for="lab in availableLaboratories" :key="lab.id" class="laboratory-card" @click="goToDetail(lab.id)">
          <div class="card-header">
            <h3>{{ lab.name }}</h3>
            <span class="status-tag available">可预约</span>
          </div>
          <div class="card-info">
            <p><el-icon size="16" color="#409eff"><MapLocation /></el-icon> {{ lab.location }}</p>
            <p><el-icon size="16" color="#67c23a"><CollectionTag /></el-icon> {{ lab.type }}</p>
            <p><el-icon size="16" color="#f56c6c"><UserFilled /></el-icon> 容量: {{ lab.capacity }}人</p>
          </div>
          <div class="card-footer">
            <span class="view-detail">查看详情 →</span>
          </div>
        </div>
        <div v-if="availableLaboratories.length === 0" class="empty-tip">暂无可预约的实验室</div>
      </div>

      <div class="unavailable-section">
        <h2 class="section-title">不可预约实验室</h2>
        <div v-for="lab in unavailableLaboratories" :key="lab.id" class="laboratory-card unavailable" @click="handleUnavailableClick(lab)">
          <div class="card-header">
            <h3>{{ lab.name }}</h3>
            <span class="status-tag unavailable">不可预约</span>
          </div>
          <div class="card-info">
            <p><el-icon size="16" color="#409eff"><MapLocation /></el-icon> {{ lab.location }}</p>
            <p><el-icon size="16" color="#67c23a"><CollectionTag /></el-icon> {{ lab.type }}</p>
            <p><el-icon size="16" color="#f56c6c"><UserFilled /></el-icon> 容量: {{ lab.capacity }}人</p>
          </div>
          <div class="card-footer">
            <span class="view-detail">查看详情 →</span>
          </div>
        </div>
        <div v-if="unavailableLaboratories.length === 0" class="empty-tip">暂无不可预约的实验室</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { MapLocation, CollectionTag, UserFilled } from '@element-plus/icons-vue'
import axios from '../axios'

const router = useRouter()
const laboratories = ref([])
const searchKeyword = ref('')
const filterType = ref('')

const availableLaboratories = computed(() => {
  return laboratories.value.filter(lab => lab.status === 'AVAILABLE')
})

const unavailableLaboratories = computed(() => {
  return laboratories.value.filter(lab => lab.status !== 'AVAILABLE')
})

const fetchLaboratories = async () => {
  try {
    const response = await axios.get('/laboratories/search', {
      params: { keyword: searchKeyword.value, type: filterType.value }
    })
    laboratories.value = response.data
  } catch (error) {
    console.error('Failed to fetch laboratories:', error)
  }
}

const handleSearch = () => {
  fetchLaboratories()
}

const goToDetail = (id) => {
  router.push(`/laboratory/${id}`)
}

const handleUnavailableClick = (lab) => {
  alert(`"${lab.name}" 当前状态为不可预约，无法进行预约操作。\n\n您可以查看实验室详情，但无法预约该实验室。`)
  router.push(`/laboratory/${lab.id}`)
}

onMounted(() => {
  fetchLaboratories()
})
</script>

<style scoped>
.laboratory-list-page { max-width: 1200px; margin: 0 auto; }
.search-bar {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  padding: 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.search-input { flex: 1; max-width: 400px; }
.filter-select { width: 150px; }
.laboratory-list {
  display: flex;
  flex-direction: column;
  gap: 32px;
}
.section-title {
  font-size: 18px;
  margin-bottom: 16px;
  padding-left: 12px;
  border-left: 4px solid #409eff;
}
.available-section, .unavailable-section {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.available-section > div:not(.empty-tip), .unavailable-section > div:not(.empty-tip) {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 16px;
}
.laboratory-card {
  background-color: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.2s;
}
.laboratory-card:not(.unavailable):hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.12); }
.laboratory-card.unavailable {
  opacity: 0.8;
}
.laboratory-card.unavailable:hover {
  opacity: 1;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.card-header h3 { font-size: 20px; margin: 0; }
.status-tag {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}
.status-tag.available { background-color: #e8f5e9; color: #2e7d32; }
.status-tag.unavailable { background-color: #ffebee; color: #c62828; }
.card-info p { margin: 8px 0; display: flex; align-items: center; gap: 8px; color: #6b7280; }
.card-footer { margin-top: 16px; padding-top: 16px; border-top: 1px solid #f3f4f6; }
.view-detail { color: #409eff; font-weight: 500; }
.empty-tip {
  text-align: center;
  padding: 32px;
  background-color: #f9fafb;
  border-radius: 12px;
  color: #9ca3af;
}
</style>