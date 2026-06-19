<template>
  <div class="profile-page">
    <div class="profile-container">
      <div class="profile-header">
        <div class="avatar">
          <el-icon size="64" color="#409eff"><User /></el-icon>
        </div>
        <div class="user-info">
          <h2>{{ user?.username }}</h2>
          <p>学号: {{ user?.studentId }}</p>
          <p>邮箱: {{ user?.email }}</p>
          <p>电话: {{ user?.phone || '未填写' }}</p>
        </div>
      </div>
      
      <div class="tabs">
        <button class="tab-btn" :class="{ active: activeTab === 'summary' }" @click="activeTab = 'summary'">预约汇总</button>
        <button class="tab-btn" :class="{ active: activeTab === 'profile' }" @click="activeTab = 'profile'">修改资料</button>
        <button class="tab-btn" :class="{ active: activeTab === 'password' }" @click="activeTab = 'password'">修改密码</button>
      </div>
      
      <div class="tab-content" v-if="activeTab === 'summary'">
        <h3>预约统计</h3>
        <div class="summary-grid">
          <div class="summary-card">
            <el-icon size="32" color="#e65100"><Clock /></el-icon>
            <div class="summary-info">
              <span class="count">{{ summary.pending }}</span>
              <span class="label">待使用</span>
            </div>
          </div>
          <div class="summary-card">
            <el-icon size="32" color="#2e7d32"><CircleCheck /></el-icon>
            <div class="summary-info">
              <span class="count">{{ summary.completed }}</span>
              <span class="label">已使用</span>
            </div>
          </div>
          <div class="summary-card">
            <el-icon size="32" color="#757575"><CircleClose /></el-icon>
            <div class="summary-info">
              <span class="count">{{ summary.cancelled }}</span>
              <span class="label">已取消</span>
            </div>
          </div>
        </div>
        <router-link to="/reservations" class="view-all-link">查看全部预约 →</router-link>
      </div>
      
      <div class="tab-content" v-if="activeTab === 'profile'">
        <h3>个人资料</h3>
        <el-form :model="profileForm" label-width="100px">
          <el-form-item label="姓名"><el-input v-model="profileForm.username" /></el-form-item>
          <el-form-item label="邮箱"><el-input v-model="profileForm.email" /></el-form-item>
          <el-form-item label="电话"><el-input v-model="profileForm.phone" /></el-form-item>
          <el-form-item><el-button type="primary" @click="handleUpdateProfile">保存修改</el-button></el-form-item>
        </el-form>
      </div>
      
      <div class="tab-content" v-if="activeTab === 'password'">
        <h3>修改密码</h3>
        <el-form :model="passwordForm" label-width="120px">
          <el-form-item label="原密码"><el-input v-model="passwordForm.oldPassword" type="password" /></el-form-item>
          <el-form-item label="新密码"><el-input v-model="passwordForm.newPassword" type="password" /></el-form-item>
          <el-form-item label="确认密码"><el-input v-model="passwordForm.confirmPassword" type="password" /></el-form-item>
          <el-form-item><el-button type="primary" @click="handleUpdatePassword">修改密码</el-button></el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { User, Clock, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import axios from '../axios'

const store = useStore()
const activeTab = ref('summary')

const user = computed(() => store.state.user)

const summary = ref({ pending: 0, completed: 0, cancelled: 0 })

const profileForm = reactive({ username: '', email: '', phone: '' })
const passwordForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

const fetchSummary = async () => {
  try {
    const response = await axios.get(`/reservations/user/${store.getters.userId}/summary`)
    summary.value = response.data
  } catch (error) {
    console.error('Failed to fetch summary:', error)
  }
}

const initProfileForm = () => {
  profileForm.username = user.value?.username || ''
  profileForm.email = user.value?.email || ''
  profileForm.phone = user.value?.phone || ''
}

const handleUpdateProfile = async () => {
  try {
    await axios.put(`/users/${store.getters.userId}/profile`, profileForm)
    alert('修改成功')
    initProfileForm()
  } catch (error) {
    const message = error.response?.data || '修改失败'
    alert(message)
  }
}

const handleUpdatePassword = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    alert('两次密码不一致')
    return
  }
  try {
    await axios.put(`/users/${store.getters.userId}/password`, null, {
      params: { oldPassword: passwordForm.oldPassword, newPassword: passwordForm.newPassword }
    })
    alert('密码修改成功')
    passwordForm.oldPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
  } catch (error) {
    const message = error.response?.data || '修改失败'
    alert(message)
  }
}

onMounted(() => {
  fetchSummary()
  initProfileForm()
})
</script>

<style scoped>
.profile-page { max-width: 600px; margin: 0 auto; }
.profile-container {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  overflow: hidden;
}
.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}
.avatar {
  width: 80px;
  height: 80px;
  background-color: rgba(255,255,255,0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.user-info h2 { margin: 0 0 8px; }
.user-info p { margin: 4px 0; opacity: 0.9; }
.tabs { display: flex; border-bottom: 1px solid #f3f4f6; }
.tab-btn {
  flex: 1;
  padding: 16px;
  border: none;
  background-color: transparent;
  cursor: pointer;
  font-size: 16px;
  transition: all 0.2s;
}
.tab-btn:hover { background-color: #f9fafb; }
.tab-btn.active { color: #409eff; border-bottom: 2px solid #409eff; }
.tab-content { padding: 24px; }
.tab-content h3 { margin-bottom: 20px; font-size: 18px; }
.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}
.summary-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background-color: #f9fafb;
  border-radius: 8px;
}
.summary-info { display: flex; flex-direction: column; }
.summary-info .count { font-size: 24px; font-weight: 600; color: #1f2937; }
.summary-info .label { font-size: 14px; color: #6b7280; }
.view-all-link {
  display: inline-block;
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}
.view-all-link:hover { text-decoration: underline; }
</style>