<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-form">
        <div class="form-header">
          <el-icon size="48" color="#409eff"><CirclePlus /></el-icon>
          <h2>实验室预约系统</h2>
          <p>注册账号</p>
        </div>
        <el-form :model="form" ref="formRef" label-width="80px" autocomplete="off">
          <el-form-item label="学号" prop="studentId">
            <el-input v-model="form.studentId" placeholder="请输入学号" prefix-icon="User" autocomplete="new-username" />
          </el-form-item>
          <el-form-item label="姓名" prop="username">
            <el-input v-model="form.username" placeholder="请输入姓名" prefix-icon="User" autocomplete="name" />
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" prefix-icon="Mail" autocomplete="new-email" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" autocomplete="new-password" />
          </el-form-item>
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码" prefix-icon="Lock" autocomplete="new-password" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleRegister" class="register-btn" :loading="loading">注册</el-button>
          </el-form-item>
        </el-form>
        <p class="login-link">已有账号？<router-link to="/login">立即登录</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { CirclePlus } from '@element-plus/icons-vue'

const router = useRouter()
const store = useStore()
const form = reactive({ studentId: '', username: '', email: '', password: '', confirmPassword: '' })
const formRef = ref(null)
const loading = ref(false)

const handleRegister = async () => {
  if (form.password !== form.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }
  loading.value = true
  try {
    await store.dispatch('register', {
      studentId: form.studentId,
      username: form.username,
      email: form.email,
      password: form.password
    })
    router.push('/laboratories')
  } catch (error) {
    const message = error.response?.data || '注册失败'
    alert(message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.register-container { width: 100%; max-width: 420px; padding: 20px; }
.register-form {
  background-color: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.15);
}
.form-header { text-align: center; margin-bottom: 32px; }
.form-header h2 { margin: 16px 0 8px; font-size: 24px; }
.form-header p { color: #6b7280; }
.register-btn { width: 100%; padding: 12px; font-size: 16px; }
.login-link { text-align: center; margin-top: 20px; color: #6b7280; }
.login-link a { color: #409eff; text-decoration: none; }
.login-link a:hover { text-decoration: underline; }
</style>