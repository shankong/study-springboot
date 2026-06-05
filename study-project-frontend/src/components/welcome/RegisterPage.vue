<script setup lang="ts">
import { ref, reactive } from 'vue'
import { Lock, User, Message, Reading, ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { postJson } from '@/net/index.js'
import router from '@/router'

// 步骤：1=填写信息，2=输入验证码完成注册
const step = ref(1)

// 第一步的表单数据
const form = reactive({
  email: '',
  username: '',
  password: ''
})

// 验证码
const code = ref('')

// 第一步：发送验证码
const sendCode = () => {
  if (!form.email) {
    ElMessage.warning('请输入邮箱地址')
    return
  }
  if (!form.username) {
    ElMessage.warning('请输入用户名')
    return
  }
  if (form.username.length < 3) {
    ElMessage.warning('用户名长度不能少于3位')
    return
  }
  if (!form.password) {
    ElMessage.warning('请输入密码')
    return
  }
  if (form.password.length < 6) {
    ElMessage.warning('密码长度不能少于6位')
    return
  }
  postJson('/api/auth/user-register', {
    email: form.email,
    username: form.username,
    password: form.password
  }, (message) => {
    ElMessage.success(message || '验证码已发送，请查收邮件')
    step.value = 2
  })
}

// 第二步：提交注册
const doRegister = () => {
  if (!code.value) {
    ElMessage.warning('请输入验证码')
    return
  }
  postJson('/api/auth/commit-register', {
    email: form.email,
    username: form.username,
    password: form.password,
    emailCode: code.value
  }, (message) => {
    ElMessage.success(message || '注册成功')
    router.push('/')
  })
}

// 返回上一步
const goBack = () => {
  step.value = 1
  code.value = ''
}
</script>

<template>
  <div class="register-card">
    <!-- 图标 -->
    <div class="avatar-box">
      <div class="avatar-icon">
        <el-icon :size="36"><Reading /></el-icon>
      </div>
    </div>

    <h2 class="title">注册账号</h2>
    <p class="subtitle">图书管理系统</p>

    <!-- ============ 第一步：填写信息 ============ -->
    <div v-if="step === 1" class="form-box">
      <div class="input-group">
        <el-input
          v-model="form.email"
          type="email"
          placeholder="请输入邮箱地址"
          size="large"
        >
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>
      </div>

      <div class="input-group">
        <el-input
          v-model="form.username"
          type="text"
          placeholder="请输入用户名"
          size="large"
        >
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </div>

      <div class="input-group">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="请输入密码"
          size="large"
          show-password
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </div>

      <el-button
        class="submit-btn"
        type="primary"
        size="large"
        round
        @click="sendCode"
      >
        发送验证码
      </el-button>

      <div class="back-row">
        <el-link :underline="false" @click="router.push('/')">
          <el-icon><ArrowLeft /></el-icon>
          返回登录
        </el-link>
      </div>
    </div>

    <!-- ============ 第二步：输入验证码 ============ -->
    <div v-else class="form-box">
      <p class="step-desc">验证码已发送至 <strong>{{ form.email }}</strong></p>

      <div class="input-group">
        <el-input
          v-model="code"
          type="text"
          placeholder="请输入6位验证码"
          size="large"
          maxlength="6"
        >
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>
      </div>

      <el-button
        class="submit-btn"
        type="primary"
        size="large"
        round
        @click="doRegister"
      >
        注 册
      </el-button>

      <div class="back-row">
        <el-link :underline="false" @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          更换邮箱
        </el-link>
      </div>
    </div>

    <p class="footer-text">Library Management System v1.0</p>
  </div>
</template>

<style scoped>
.register-card {
  width: 380px;
  padding: 0 20px;
  text-align: center;
}

.avatar-box {
  margin-bottom: 16px;
}

.avatar-icon {
  width: 72px;
  height: 72px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  color: #fff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.35);
}

.title {
  font-size: 26px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 6px;
  letter-spacing: 4px;
}

.subtitle {
  font-size: 13px;
  color: #999;
  margin: 0 0 24px;
  letter-spacing: 2px;
}

.step-desc {
  font-size: 13px;
  color: #888;
  margin: 0 0 24px;
  line-height: 1.8;
}

.step-desc strong {
  color: #667eea;
}

.form-box {
  text-align: left;
}

.input-group {
  margin-bottom: 18px;
}

.input-group :deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #e8e8e8 inset;
  border-radius: 8px;
  padding: 2px 12px;
  transition: box-shadow 0.3s;
}

.input-group :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #667eea inset;
}

.input-group :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.25) inset;
}

.submit-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
}

.submit-btn:hover {
  opacity: 0.9;
}

.back-row {
  text-align: center;
  margin-top: 22px;
}

.back-row .el-link {
  font-size: 13px;
  color: #999;
}

.back-row .el-link:hover {
  color: #667eea;
}

.footer-text {
  font-size: 12px;
  color: #ccc;
  margin-top: 60px;
  letter-spacing: 2px;
}
</style>
