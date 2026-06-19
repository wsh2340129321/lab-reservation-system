<template>
  <header class="header">
    <div class="header-left">
      <router-link :to="isAdmin ? '/admin/laboratories' : '/'" class="logo">
        <el-icon class="logo-icon" size="24"><OfficeBuilding /></el-icon>
        <span>实验室预约系统</span>
      </router-link>
    </div>
    <nav class="header-nav">
      <template v-if="!isAdmin">
        <router-link to="/laboratories" class="nav-item">实验室查询</router-link>
        <router-link to="/reservations" class="nav-item">我的预约</router-link>
        <router-link to="/profile" class="nav-item">个人中心</router-link>
        <router-link to="/notifications" class="nav-item notification-item">
          <el-icon><Bell /></el-icon>
          消息提醒
          <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
        </router-link>
      </template>
      <template v-else>
        <router-link to="/admin/laboratories" class="nav-item">实验室管理</router-link>
        <router-link to="/admin/reservations" class="nav-item">预约管理</router-link>
        <router-link to="/admin/users" class="nav-item">用户管理</router-link>
      </template>
    </nav>
    <div class="header-right" v-if="isLoggedIn">
      <span class="user-info">{{ user?.username }} ({{ user?.studentId }})</span>
      <button @click="handleLogout" class="logout-btn">
        <el-icon><UserFilled /></el-icon>
        退出登录
      </button>
    </div>
    <div class="header-right" v-else>
      <router-link to="/login" class="login-btn">登录</router-link>
      <router-link to="/register" class="register-btn">注册</router-link>
    </div>
  </header>
</template>

<script setup>
import { computed, onMounted, onUnmounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { OfficeBuilding, Bell, UserFilled } from '@element-plus/icons-vue'

const store = useStore()
const router = useRouter()

let notificationPollingTimer = null

const user = computed(() => store.state.user)
const isAdmin = computed(() => store.getters.isAdmin)
const isLoggedIn = computed(() => store.getters.isLoggedIn)
const unreadCount = computed(() => store.state.unreadCount)

const handleLogout = () => {
  clearInterval(notificationPollingTimer)
  store.dispatch('logout')
  router.push('/login')
}

onMounted(() => {
  if (store.getters.isLoggedIn) {
    store.dispatch('fetchNotifications')
    notificationPollingTimer = setInterval(() => {
      store.dispatch('fetchNotifications')
    }, 30000)
  }
})

onUnmounted(() => {
  clearInterval(notificationPollingTimer)
})
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}
.header-left .logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  color: #1f2937;
  font-weight: 600;
  font-size: 18px;
}
.logo-icon { color: #409eff; }
.header-nav { display: flex; align-items: center; gap: 20px; }
.nav-item {
  text-decoration: none;
  color: #6b7280;
  padding: 8px 16px;
  border-radius: 6px;
  transition: all 0.2s;
}
.nav-item:hover { background-color: #f3f4f6; color: #1f2937; }
.nav-item.router-link-active,
.nav-item.router-link-exact-active {
  background-color: #409eff;
  color: white;
}
.notification-item { position: relative; }
.badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 16px;
  height: 16px;
  background-color: #ef4444;
  color: white;
  font-size: 10px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
}
.header-right { display: flex; align-items: center; gap: 16px; }
.user-info { color: #6b7280; font-size: 14px; }
.logout-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 12px;
  background-color: #f3f4f6;
  border: none;
  border-radius: 6px;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s;
}
.logout-btn:hover { background-color: #e5e7eb; color: #1f2937; }
.login-btn, .register-btn {
  text-decoration: none;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.2s;
}
.login-btn {
  color: #6b7280;
}
.login-btn:hover { color: #1f2937; }
.register-btn {
  background-color: #409eff;
  color: white;
}
.register-btn:hover { background-color: #3b82f6; }
</style>