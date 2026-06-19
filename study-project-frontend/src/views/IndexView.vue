<script setup lang="ts">
import { ref, shallowRef, onMounted } from 'vue'
import { get } from '@/net/index.js'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { List, SetUp, DataBoard, Fold, Expand } from '@element-plus/icons-vue'
import type { Account } from '@/types/account'

import DashboardPanel   from '@/components/index/DashboardPanel.vue'
import CatalogPanel    from '@/components/index/CatalogPanel.vue'
import PlaceholderPanel from '@/components/index/PlaceholderPanel.vue'
import AccountDropdown  from '@/components/index/AccountDropdown.vue'

// ---- 布局状态 ----
const isCollapse = ref(false)

// ---- 数据 ----
const accountList = ref<Account[]>([])
const loading     = ref(false)

// ---- 菜单 / 动态组件 ----
const activeMenu = ref('dashboard')
const panels: Record<string, any> = {
  dashboard: DashboardPanel,
  books:     CatalogPanel,
  borrow:    PlaceholderPanel,
}
const currentPanel = shallowRef(DashboardPanel)

const handleMenuSelect = (index: string) => {
  activeMenu.value = index
  currentPanel.value = panels[index] ?? PlaceholderPanel
}

// ---- 加载用户列表 ----
const loadAccounts = () => {
  loading.value = true
  get('/api/auth/showAccount', (data: any) => {
    accountList.value = (data as Account[]) || []
    loading.value = false
  })
}

// ---- 退出登录 ----
const logout = () => {
  get('/api/auth/logout', (_message: string) => {
    ElMessage.success('退出登录成功')
    router.push('/')
  })
}

onMounted(loadAccounts)
</script>

<template>
  <el-container class="layout-container">

    <!-- ===== 侧边栏 ===== -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside-menu">
      <div class="logo-box">
        <div class="logo-icon">📚</div>
        <span v-show="!isCollapse" class="logo-text">文章管理系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        @select="handleMenuSelect"
      >
        <el-menu-item index="dashboard"><el-icon><DataBoard /></el-icon><template #title>首页概览</template></el-menu-item>
        <el-menu-item index="books"><el-icon><List /></el-icon><template #title>栏目管理</template></el-menu-item>
        <el-menu-item index="borrow"><el-icon><SetUp /></el-icon><template #title>文章管理</template></el-menu-item>
      </el-menu>
    </el-aside>

    <!-- ===== 右侧主体 ===== -->
    <el-container>
      <!-- 顶部导航 -->
      <el-header class="top-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse" :size="22">
            <Fold v-if="!isCollapse" /><Expand v-else />
          </el-icon>
          <span class="header-title">文章管理系统 · 控制台</span>
        </div>
        <div class="header-right">
          <AccountDropdown @logout="logout" />
        </div>
      </el-header>

      <!-- 主内容：动态切换面板 -->
      <el-main class="main-content">
        <component :is="currentPanel" :account-list="accountList" :loading="loading" />
      </el-main>
    </el-container>

  </el-container>
</template>

<style scoped>
/* ===== 整体 ===== */
.layout-container { width: 100vw; height: 100vh; overflow: hidden; }

/* ===== 侧边栏 ===== */
.aside-menu { background-color: #304156; transition: width .3s; overflow-x: hidden; }
.logo-box {
  height: 64px; display: flex; align-items: center; justify-content: center; gap: 10px;
  border-bottom: 1px solid rgba(255,255,255,.1);
}
.logo-icon { font-size: 26px; flex-shrink: 0; }
.logo-text { color: #fff; font-size: 15px; font-weight: 600; letter-spacing: 2px; white-space: nowrap; }
.aside-menu :deep(.el-menu) { border-right: none; }

/* ===== 顶部导航 ===== */
.top-header {
  background: #fff; display: flex; align-items: center; justify-content: space-between;
  padding: 0 24px; box-shadow: 0 1px 4px rgba(0,0,0,.08); height: 64px; z-index: 10;
}
.header-left  { display: flex; align-items: center; gap: 14px; }
.header-right { display: flex; align-items: center; }
.collapse-btn  { cursor: pointer; color: #666; transition: color .2s; }
.collapse-btn:hover { color: #409EFF; }
.header-title  { font-size: 16px; font-weight: 500; color: #444; letter-spacing: 2px; }

/* ===== 主内容 ===== */
.main-content { background: #f0f2f5; padding: 24px; overflow-y: auto; }
</style>
