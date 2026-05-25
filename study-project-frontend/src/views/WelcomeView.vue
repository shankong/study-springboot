<script setup lang="ts">
  import bgImg from '@/assets/imges/login-bg.png'
</script>

<template>
  <div class="welcome-container">
    <!-- 左侧：图书馆主题背景 -->
    <div class="left-panel">
      <el-image class="bg-image" fit="cover" :src="bgImg" />
      <div class="overlay">
        <div class="overlay-content">
          <h1 class="overlay-title">图书管理系统</h1>
          <p class="overlay-subtitle">Library Management System</p>
          <p class="overlay-desc">欢迎使用本系统，管理和借阅图书的智能平台</p>
        </div>
      </div>
    </div>
    <!-- 右侧：登录表单 -->
    <div class="right-panel">
      <router-view />
    </div>
  </div>
</template>

<style scoped>
.welcome-container {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  display: flex;
}

.left-panel {
  flex: 1;
  position: relative;
  overflow: hidden;
}

.bg-image {
  width: 100%;
  height: 100%;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(44, 62, 80, 0.75) 0%, rgba(52, 73, 94, 0.6) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlay-content {
  text-align: center;
  color: #fff;
  padding: 60px;
}

.overlay-title {
  font-size: 38px;
  font-weight: 700;
  letter-spacing: 8px;
  margin: 0 0 12px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.overlay-subtitle {
  font-size: 16px;
  font-weight: 300;
  letter-spacing: 4px;
  margin: 0 0 24px;
  opacity: 0.85;
  text-transform: uppercase;
}

.overlay-desc {
  font-size: 14px;
  opacity: 0.7;
  line-height: 1.8;
  margin: 0;
}

.right-panel {
  width: 480px;
  background-color: #fafafa;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
