<template>
  <div class="home-page">
    <div class="hero-section">
      <div class="hero-content">
        <h1>实验室预约系统</h1>
        <p>便捷预约，高效管理</p>
        <div class="hero-buttons">
          <router-link v-if="!isLoggedIn" to="/login" class="btn-primary">登录系统</router-link>
          <router-link v-if="!isLoggedIn" to="/register" class="btn-secondary">注册账号</router-link>
          <router-link v-else to="/laboratories" class="btn-primary">开始预约</router-link>
        </div>
      </div>
    </div>
    
    <div class="features-section">
      <h2>功能特点</h2>
      <div class="features-grid">
        <div class="feature-card">
          <el-icon size="48" color="#409eff"><Search /></el-icon>
          <h3>智能查询</h3>
          <p>支持多条件筛选和关键词搜索</p>
        </div>
        <div class="feature-card">
          <el-icon size="48" color="#67c23a"><Calendar /></el-icon>
          <h3>时段预约</h3>
          <p>实时查看空闲时段，一键预约</p>
        </div>
        <div class="feature-card">
          <el-icon size="48" color="#f56c6c"><Bell /></el-icon>
          <h3>消息提醒</h3>
          <p>预约状态变化实时通知</p>
        </div>
        <div class="feature-card">
          <el-icon size="48" color="#909399"><User /></el-icon>
          <h3>个人管理</h3>
          <p>管理预约记录和个人信息</p>
        </div>
      </div>
    </div>

    <div class="laboratory-preview" v-if="laboratories.length > 0">
      <h2>热门实验室</h2>
      <div class="laboratory-grid">
        <div v-for="lab in laboratories" :key="lab.id" class="laboratory-card" @click="goToDetail(lab.id)">
          <div class="card-image" v-if="lab.imageUrl">
            <img :src="lab.imageUrl" :alt="lab.name" />
          </div>
          <div class="card-image placeholder" v-else>
            <el-icon size="48" color="#d1d5db"><Picture /></el-icon>
          </div>
          <div class="card-content">
            <h3>{{ lab.name }}</h3>
            <p class="location">{{ lab.location }}</p>
            <p class="type">{{ lab.type }}</p>
            <p class="capacity">容量: {{ lab.capacity }}人</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { Search, Calendar, Bell, User, Picture } from '@element-plus/icons-vue'
import axios from '../axios'

const router = useRouter()
const store = useStore()
const laboratories = ref([])
const isLoggedIn = computed(() => store.getters.isLoggedIn)

const goToDetail = (id) => {
  if (isLoggedIn.value) {
    router.push(`/laboratory/${id}`)
  } else {
    router.push('/login')
  }
}

onMounted(async () => {
  try {
    const response = await axios.get('/laboratories')
    laboratories.value = response.data.slice(0, 4)
  } catch (error) {
    console.error('Failed to fetch laboratories:', error)
  }
})
</script>

<style scoped>
.home-page { min-height: 100vh; }
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 80px 20px;
  text-align: center;
}
.hero-content h1 { font-size: 48px; margin-bottom: 16px; }
.hero-content p { font-size: 20px; opacity: 0.9; margin-bottom: 32px; }
.hero-buttons { display: flex; gap: 16px; justify-content: center; }
.btn-primary, .btn-secondary {
  padding: 12px 24px;
  border-radius: 8px;
  text-decoration: none;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.2s;
}
.btn-primary { background-color: white; color: #667eea; }
.btn-primary:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.2); }
.btn-secondary {
  background-color: rgba(255,255,255,0.2);
  color: white;
  border: 1px solid rgba(255,255,255,0.3);
}
.btn-secondary:hover { background-color: rgba(255,255,255,0.3); }
.features-section { padding: 60px 20px; text-align: center; }
.features-section h2 { font-size: 32px; margin-bottom: 40px; }
.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
}
.feature-card {
  background-color: white;
  padding: 32px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  text-align: center;
}
.feature-card h3 { margin: 16px 0 8px; font-size: 20px; }
.feature-card p { color: #6b7280; }
.laboratory-preview { padding: 60px 20px; background-color: #f9fafb; }
.laboratory-preview h2 { font-size: 32px; text-align: center; margin-bottom: 40px; }
.laboratory-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
}
.laboratory-card {
  background-color: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.2s;
}
.laboratory-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.12); }
.card-image {
  width: 100%;
  height: 160px;
  overflow: hidden;
  background-color: #f3f4f6;
}
.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.card-image.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
}
.card-content {
  padding: 16px;
}
.laboratory-card h3 { margin-bottom: 8px; font-size: 18px; }
.laboratory-card .location { color: #409eff; font-size: 14px; margin-bottom: 4px; }
.laboratory-card .type { color: #67c23a; font-size: 14px; margin-bottom: 4px; }
.laboratory-card .capacity { color: #909399; font-size: 14px; }
</style>