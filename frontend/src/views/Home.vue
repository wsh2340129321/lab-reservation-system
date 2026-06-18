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
    
    <div class="main-content">
      <div class="left-features">
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
          <el-icon size="48" color="#e6a23c"><UserFilled /></el-icon>
          <h3>用户管理</h3>
          <p>管理用户账号，权限设置</p>
        </div>
      </div>
      
      <div class="laboratory-preview" v-if="laboratories.length > 0">
        <div class="carousel-container" @mouseenter="stopAutoPlay" @mouseleave="startAutoPlay">
          <button class="carousel-prev" @click.stop="prevSlide">
            <el-icon size="28" color="white"><ArrowLeft /></el-icon>
          </button>
          <div class="carousel-wrapper">
            <div 
              class="carousel-track" 
              :class="{ 'no-transition': !enableTransition }"
              :style="{ transform: `translateX(-${currentSlide * slideWidth}px)` }"
            >
              <div 
                v-for="lab in laboratories" 
                :key="lab.id" 
                class="carousel-slide" 
                @click="goToDetail(lab.id)"
              >
                <div class="card-image" v-if="lab.imageUrl">
                  <img :src="lab.imageUrl" :alt="lab.name" />
                </div>
                <div class="card-image placeholder" v-else>
                  <el-icon size="64" color="#9ca3af"><Picture /></el-icon>
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
          <button class="carousel-next" @click.stop="nextSlide">
            <el-icon size="28" color="white"><ArrowRight /></el-icon>
          </button>
          <div class="carousel-indicators">
            <span 
              v-for="(_, index) in laboratories" 
              :key="index" 
              class="indicator"
              :class="{ active: currentSlide === index }"
              @click.stop="goToSlide(index)"
            ></span>
          </div>
        </div>
      </div>
      
      <div class="right-features">
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
        <div class="feature-card">
          <el-icon size="48" color="#f4a460"><OfficeBuilding /></el-icon>
          <h3>实验室管理</h3>
          <p>管理实验室资源，维护信息</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { Search, Calendar, Bell, User, Picture, ArrowLeft, ArrowRight, UserFilled, OfficeBuilding } from '@element-plus/icons-vue'
import axios from '../axios'

const router = useRouter()
const store = useStore()
const laboratories = ref([])
const isLoggedIn = computed(() => store.getters.isLoggedIn)
const currentSlide = ref(0)
const slideWidth = ref(580)
const enableTransition = ref(true)
let carouselTimer = null
let isAutoPlay = false

const goToDetail = (id) => {
  if (isLoggedIn.value) {
    router.push(`/laboratory/${id}`)
  } else {
    router.push('/login')
  }
}

const nextSlide = () => {
  if (laboratories.value.length === 0) return
  if (isAutoPlay) {
    currentSlide.value = (currentSlide.value + 1) % laboratories.value.length
  } else {
    if (currentSlide.value === laboratories.value.length - 1) {
      enableTransition.value = false
      currentSlide.value = 0
      void document.querySelector('.carousel-track')?.offsetWidth
      requestAnimationFrame(() => {
        enableTransition.value = true
      })
    } else {
      currentSlide.value++
    }
  }
}

const prevSlide = () => {
  if (laboratories.value.length === 0) return
  if (isAutoPlay) {
    currentSlide.value = (currentSlide.value - 1 + laboratories.value.length) % laboratories.value.length
  } else {
    if (currentSlide.value === 0) {
      enableTransition.value = false
      currentSlide.value = laboratories.value.length - 1
      void document.querySelector('.carousel-track')?.offsetWidth
      requestAnimationFrame(() => {
        enableTransition.value = true
      })
    } else {
      currentSlide.value--
    }
  }
}

const goToSlide = (index) => {
  currentSlide.value = index
}

const startAutoPlay = () => {
  carouselTimer = setInterval(() => {
    isAutoPlay = true
    nextSlide()
    isAutoPlay = false
  }, 2000)
}

const stopAutoPlay = () => {
  if (carouselTimer) {
    clearInterval(carouselTimer)
    carouselTimer = null
  }
}

onMounted(async () => {
  try {
    const response = await axios.get('/laboratories')
    laboratories.value = response.data.slice(0, 4)
    if (laboratories.value.length > 0) {
      startAutoPlay()
    }
  } catch (error) {
    console.error('Failed to fetch laboratories:', error)
  }
})

onUnmounted(() => {
  stopAutoPlay()
})
</script>

<style scoped>
.home-page { min-height: 100vh; }
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 60px 20px;
  text-align: center;
  position: relative;
  overflow: hidden;
}
.hero-section::before {
  content: '';
  position: absolute;
  top: -100px;
  right: -100px;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  border-radius: 50%;
}
.hero-section::after {
  content: '';
  position: absolute;
  bottom: -80px;
  left: -80px;
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  border-radius: 50%;
}
.hero-content {
  position: relative;
  z-index: 1;
}
.hero-content h1 { font-size: 40px; margin-bottom: 12px; }
.hero-content p { font-size: 18px; opacity: 0.9; margin-bottom: 24px; }
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
.main-content {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  gap: 30px;
  padding: 40px 20px;
  background: linear-gradient(180deg, #ffffff 0%, #f9fafb 100%);
  max-width: 1400px;
  margin: 0 auto;
}
.left-features, .right-features {
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 250px;
}
.feature-card {
  background-color: white;
  padding: 28px 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  text-align: center;
  transition: all 0.3s;
}
.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.12);
}
.feature-card h3 { margin: 14px 0 6px; font-size: 18px; }
.feature-card p { color: #6b7280; font-size: 14px; }
.laboratory-preview { 
  padding: 0; 
}
.laboratory-preview h2 { 
  font-size: 28px; 
  text-align: center; 
  margin-bottom: 28px; 
  color: #1f2937;
}
.carousel-container {
  position: relative;
  max-width: 650px;
  margin: 0 auto;
  padding: 0 35px;
}
.carousel-wrapper {
  overflow: hidden;
  position: relative;
  width: 580px;
  margin: 0 auto;
}
.carousel-track {
  display: flex;
  transition: transform 0.5s ease-in-out;
}
.carousel-track.no-transition {
  transition: none;
}
.carousel-slide {
  flex: 0 0 580px;
  background-color: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0,0,0,0.12);
  cursor: pointer;
  transition: all 0.3s;
}
.carousel-slide:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}
.carousel-prev, .carousel-next {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}
.carousel-prev:hover, .carousel-next:hover {
  transform: translateY(-50%) scale(1.05);
}
.carousel-prev {
  left: 0;
}
.carousel-next {
  right: 0;
}
.carousel-indicators {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}
.indicator {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: #d1d5db;
  cursor: pointer;
  transition: all 0.2s;
}
.indicator:hover {
  background-color: #9ca3af;
}
.indicator.active {
  background-color: #667eea;
  width: 28px;
  border-radius: 6px;
}
.card-image {
  width: 100%;
  height: 350px;
  overflow: hidden;
  background-color: #f3f4f6;
}
.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}
.carousel-slide:hover .card-image img {
  transform: scale(1.03);
}
.card-image.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
}
.card-content {
  padding: 24px;
}
.carousel-slide h3 { 
  margin-bottom: 12px; 
  font-size: 24px; 
  font-weight: 600; 
  color: #1f2937;
}
.carousel-slide .location { 
  color: #409eff; 
  font-size: 16px; 
  margin-bottom: 6px;
}
.carousel-slide .type { 
  color: #67c23a; 
  font-size: 16px; 
  margin-bottom: 6px;
}
.carousel-slide .capacity { 
  color: #6b7280; 
  font-size: 16px; 
}
</style>