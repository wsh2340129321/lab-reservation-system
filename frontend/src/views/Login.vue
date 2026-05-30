<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-form">
        <div class="form-header">
          <el-icon size="48" color="#409eff"><OfficeBuilding /></el-icon>
          <h2>实验室预约系统</h2>
          <p>欢迎登录</p>
        </div>
        <el-form :model="form" ref="formRef" label-width="80px">
          <el-form-item label="学号" prop="studentId">
            <el-input v-model="form.studentId" placeholder="请输入学号" prefix-icon="User" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleLogin" class="login-btn" :loading="loading">登录</el-button>
          </el-form-item>
        </el-form>
        <p class="register-link">还没有账号？<router-link to="/register">立即注册</router-link></p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { OfficeBuilding } from '@element-plus/icons-vue'

const router = useRouter()
const store = useStore()
const form = reactive({ studentId: '', password: '' })
const formRef = ref(null)
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    await store.dispatch('login', form)
    const isAdmin = store.getters.isAdmin
    if (isAdmin) {
      router.push('/admin/laboratories')
    } else {
      router.push('/laboratories')
    }
  } catch (error) {
    const message = error.response?.data || '登录失败'
    alert(message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-container { width: 100%; max-width: 420px; padding: 20px; }
.login-form {
  background-color: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0,0,0,0.15);
}
.form-header { text-align: center; margin-bottom: 32px; }
.form-header h2 { margin: 16px 0 8px; font-size: 24px; }
.form-header p { color: #6b7280; }
.login-btn { width: 100%; padding: 12px; font-size: 16px; }
.register-link { text-align: center; margin-top: 20px; color: #6b7280; }
.register-link a { color: #409eff; text-decoration: none; }
.register-link a:hover { text-decoration: underline; }
</style>