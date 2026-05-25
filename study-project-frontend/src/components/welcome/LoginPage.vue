<script setup lang="ts">
  import { reactive } from 'vue'
  import { Lock, User, Reading } from '@element-plus/icons-vue'
  import { ElMessage } from 'element-plus'
  import { post } from '@/net/index.js'
  import router from '@/router'

  const form = reactive({
    username: '',
    password: '',
    remember: false
  })

  const login = () => {
    if (!form.username || !form.password) {
      ElMessage.warning('请输入用户名和密码')
    } else {
      post('/api/auth/login', {
        username: form.username,
        password: form.password,
        remember: form.remember,
      }, (message) => {
        ElMessage.success(message)
        router.push('/index')
      })
    }
  }
</script>

<template>
  <div class="login-card">
    <!-- 图书图标 -->
    <div class="avatar-box">
      <div class="avatar-icon">
        <el-icon :size="36"><Reading /></el-icon>
      </div>
    </div>

    <h2 class="title">欢迎登录</h2>
    <p class="subtitle">图书管理系统</p>

    <!-- 表单 -->
    <div class="form-box">
      <div class="input-group">
        <el-input
          v-model="form.username"
          type="text"
          placeholder="用户名 / 邮箱"
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
          placeholder="密码"
          size="large"
          show-password
        >
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </div>

      <div class="extra-row">
        <el-checkbox v-model="form.remember" label="记住我" />
        <el-link :underline="false">忘记密码？</el-link>
      </div>

      <el-button
        class="login-btn"
        type="primary"
        size="large"
        round
        @click="login()"
      >
        登 录
      </el-button>

      <div class="divider-row">
        <span class="divider-text">还没有账号</span>
      </div>

      <el-button
        class="register-btn"
        size="large"
        round
        plain
      >
        注册账号
      </el-button>
    </div>

    <p class="footer-text">Library Management System v1.0</p>
  </div>
</template>

<style scoped>
.login-card {
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
  margin: 0 0 40px;
  letter-spacing: 2px;
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

.extra-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.login-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  letter-spacing: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
}

.login-btn:hover {
  opacity: 0.9;
}

.divider-row {
  text-align: center;
  margin: 28px 0 20px;
  position: relative;
}

.divider-row::before,
.divider-row::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 80px;
  height: 1px;
  background-color: #e8e8e8;
}

.divider-row::before {
  left: 0;
}

.divider-row::after {
  right: 0;
}

.divider-text {
  font-size: 13px;
  color: #bbb;
}

.register-btn {
  width: 100%;
  height: 46px;
  font-size: 15px;
  letter-spacing: 4px;
  border-radius: 8px;
  border-color: #d0d0d0;
  color: #666;
}

.register-btn:hover {
  border-color: #667eea;
  color: #667eea;
}

.footer-text {
  font-size: 12px;
  color: #ccc;
  margin-top: 40px;
  letter-spacing: 2px;
}
</style>
