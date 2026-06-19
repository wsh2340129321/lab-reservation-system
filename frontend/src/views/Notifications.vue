<template>
  <div class="notifications-page">
    <div class="page-header">
      <h1>消息提醒</h1>
      <el-button @click="handleMarkAllRead" v-if="unreadCount > 0">
        <el-icon><CircleCheck /></el-icon>
        全部已读
      </el-button>
    </div>
    
    <div class="notification-list">
      <div v-for="notification in notifications" :key="notification.id" class="notification-item" :class="{ unread: !notification.isRead }" @click="handleMarkRead(notification.id)">
        <div class="notification-icon" :class="getTypeClass(notification.type)">
          <el-icon :size="24"><component :is="getTypeIcon(notification.type)" /></el-icon>
        </div>
        <div class="notification-content">
          <p class="content">{{ notification.content }}</p>
          <p class="time">{{ formatTime(notification.createdAt || notification.created_at) }}</p>
        </div>
        <div class="notification-status">
          <span v-if="!notification.isRead" class="unread-dot"></span>
        </div>
      </div>
    </div>
    
    <div v-if="notifications.length === 0" class="empty-state">
      <el-icon size="64" color="#909399"><Bell /></el-icon>
      <p>暂无消息提醒</p>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { CircleCheck, Bell, Check, Warning, CircleClose, Clock } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

const store = useStore()

const notifications = computed(() => store.state.notifications)
const unreadCount = computed(() => store.state.unreadCount)

const getTypeIcon = (type) => {
  const icons = {
    RESERVATION_SUCCESS: Check,
    RESERVATION_REMINDER: Clock,
    RESERVATION_CANCEL: CircleClose,
    RESERVATION_CANCELLED_BY_ADMIN: Warning,
    RESERVATION_REJECTED: Warning
  }
  return icons[type] || Bell
}

const getTypeClass = (type) => {
  const classes = {
    RESERVATION_SUCCESS: 'success',
    RESERVATION_REMINDER: 'warning',
    RESERVATION_CANCEL: 'info',
    RESERVATION_CANCELLED_BY_ADMIN: 'danger',
    RESERVATION_REJECTED: 'danger'
  }
  return classes[type] || 'info'
}

const formatTime = (time) => {
  if (!time) return ''
  const date = dayjs(time)
  if (date.isValid()) {
    return date.format('YYYY-MM-DD HH:mm')
  }
  return ''
}

const handleMarkRead = async (id) => {
  await store.dispatch('markNotificationRead', id)
}

const handleMarkAllRead = async () => {
  await store.dispatch('markAllNotificationsRead')
}

onMounted(() => {
  store.dispatch('fetchNotifications')
})
</script>

<style scoped>
.notifications-page { max-width: 600px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h1 { font-size: 24px; }
.notification-list { display: flex; flex-direction: column; gap: 12px; }
.notification-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
  transition: all 0.2s;
}
.notification-item:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.notification-item.unread {
  background-color: #f0f7ff;
  border-left: 3px solid #409eff;
}
.notification-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.notification-icon.success { background-color: #e8f5e9; color: #2e7d32; }
.notification-icon.warning { background-color: #fff3e0; color: #e65100; }
.notification-icon.info { background-color: #e3f2fd; color: #1976d2; }
.notification-icon.danger { background-color: #ffebee; color: #c62828; }
.notification-content { flex: 1; }
.notification-content .content { margin: 0 0 4px; color: #1f2937; }
.notification-content .time { margin: 0; font-size: 12px; color: #909399; }
.notification-status { flex-shrink: 0; }
.unread-dot {
  display: inline-block;
  width: 8px;
  height: 8px;
  background-color: #409eff;
  border-radius: 50%;
}
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.empty-state p { margin-top: 16px; color: #909399; }
</style>