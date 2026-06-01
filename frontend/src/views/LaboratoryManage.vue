<template>
  <div class="admin-page">
    <div class="content-header">
      <h2>实验室管理</h2>
      <el-button type="primary" @click="addLab">添加实验室</el-button>
    </div>
    <el-table :data="laboratories" border>
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="capacity" label="容量" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <span :class="scope.row.status.toLowerCase() === 'available' ? 'status-available' : 'status-unavailable'">
            {{ scope.row.status === 'AVAILABLE' ? '可预约' : '不可预约' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="small" @click="editLab(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteLab(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="showLabModal" title="实验室信息" width="500px">
      <el-form :model="labForm" label-width="100px">
        <el-form-item label="名称"><el-input v-model="labForm.name" /></el-form-item>
        <el-form-item label="位置"><el-input v-model="labForm.location" /></el-form-item>
        <el-form-item label="类型"><el-input v-model="labForm.type" /></el-form-item>
        <el-form-item label="容量"><el-input v-model="labForm.capacity" type="number" /></el-form-item>
        <el-form-item label="状态">
          <el-select v-model="labForm.status">
            <el-option label="可预约" value="AVAILABLE" />
            <el-option label="不可预约" value="UNAVAILABLE" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备配置">
          <textarea v-model="labForm.equipment" rows="3" class="lab-textarea" />
        </el-form-item>
        <el-form-item label="开放规则">
          <textarea v-model="labForm.rules" rows="3" class="lab-textarea" />
        </el-form-item>
        <el-form-item label="使用须知">
          <textarea v-model="labForm.notes" rows="3" class="lab-textarea" />
        </el-form-item>
        <el-form-item label="实验室图片">
          <div class="image-upload">
            <div v-if="labForm.imageUrl" class="preview-image">
              <img :src="labForm.imageUrl" alt="预览" />
              <span class="delete-icon" @click="deleteImage">
                <el-icon size="20" color="white"><Close /></el-icon>
              </span>
            </div>
            <div v-else class="upload-placeholder">
              <el-icon size="48" color="#d1d5db"><Upload /></el-icon>
              <p>点击或拖拽上传图片</p>
              <input type="file" ref="fileInput" accept="image/*" @change="handleImageUpload" style="display: none" />
              <el-button type="primary" @click="triggerFileUpload">选择图片</el-button>
            </div>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showLabModal = false">取消</el-button>
        <el-button type="primary" @click="saveLab">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Upload, Close } from '@element-plus/icons-vue'
import axios from '../axios'

const showLabModal = ref(false)
const laboratories = ref([])
const fileInput = ref(null)

const labForm = reactive({
  id: null, name: '', location: '', type: '', capacity: '',
  equipment: '', rules: '', notes: '', status: 'AVAILABLE', imageUrl: ''
})

const fetchLaboratories = async () => {
  try {
    const response = await axios.get('/laboratories')
    laboratories.value = response.data
  } catch (error) {
    console.error('Failed to fetch laboratories:', error)
  }
}

const addLab = () => {
  resetLabForm()
  showLabModal.value = true
}

const editLab = (lab) => {
  Object.assign(labForm, lab)
  showLabModal.value = true
}

const saveLab = async () => {
  try {
    if (labForm.id) {
      await axios.put(`/laboratories/${labForm.id}`, labForm)
    } else {
      await axios.post('/laboratories', labForm)
    }
    showLabModal.value = false
    fetchLaboratories()
    resetLabForm()
    alert('保存成功')
  } catch (error) {
    const message = error.response?.data || '保存失败'
    alert(message)
  }
}

const resetLabForm = () => {
  labForm.id = null; labForm.name = ''; labForm.location = ''; labForm.type = '';
  labForm.capacity = ''; labForm.equipment = ''; labForm.rules = ''; labForm.notes = '';
  labForm.status = 'AVAILABLE'; labForm.imageUrl = '';
}

const triggerFileUpload = () => {
  fileInput.value?.click()
}

const handleImageUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  const formData = new FormData()
  formData.append('file', file)

  try {
    let uploadUrl = '/upload/image'
    if (labForm.id) {
      uploadUrl = `/upload/laboratory/${labForm.id}/image`
    }

    const response = await axios.post(uploadUrl, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    if (response.data.success) {
      labForm.imageUrl = response.data.data
    } else {
      alert('上传失败：' + response.data.message)
    }
  } catch (error) {
    console.error('Upload failed:', error)
    alert('上传失败')
  }
}

const deleteImage = () => {
  labForm.imageUrl = ''
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

const deleteLab = async (id) => {
  if (!confirm('确定要删除该实验室吗？')) return
  try {
    await axios.delete(`/laboratories/${id}`)
    fetchLaboratories()
    alert('删除成功')
  } catch (error) {
    const message = error.response?.data || '删除失败'
    alert(message)
  }
}

onMounted(() => {
  fetchLaboratories()
})
</script>

<style scoped>
.admin-page { max-width: 1200px; margin: 0 auto; }
.content-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.content-header h2 { font-size: 20px; }
.status-available { color: #2e7d32; font-weight: 500; }
.status-unavailable { color: #c62828; font-weight: 500; }
.lab-textarea {
  width: 100%;
  min-height: 80px;
  padding: 8px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  resize: vertical;
}
.image-upload {
  width: 100%;
}
.preview-image {
  position: relative;
  display: inline-block;
}
.preview-image img {
  max-width: 200px;
  max-height: 200px;
  border-radius: 8px;
  object-fit: cover;
}
.delete-icon {
  position: absolute;
  top: -12px;
  right: -12px;
  width: 28px;
  height: 28px;
  background-color: #ef5350;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
  transition: all 0.2s;
}
.delete-icon:hover {
  background-color: #c62828;
  transform: scale(1.1);
}
.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
  border: 2px dashed #d1d5db;
  border-radius: 8px;
  background-color: #f9fafb;
  cursor: pointer;
  transition: all 0.2s;
}
.upload-placeholder:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}
.upload-placeholder p {
  margin: 12px 0;
  color: #6b7280;
}
</style>
