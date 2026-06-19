<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { get } from '@/net/index.js'
import { ElMessage } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import type { Account } from '@/types/account'
import DeactivateDialog from './DeactivateDialog.vue'
import ChangePasswordDialog from './ChangePasswordDialog.vue'
import router from '@/router'

const account = ref<Account | null>(null)
const showDeactivateDialog = ref(false)
const showChangePasswordDialog = ref(false)

// 退出登录
const emit = defineEmits<{
  logout: []
}>()

// 加载当前用户信息
onMounted(() => {
  get('/api/auth/me', (data: any) => {
    account.value = data as Account
  })
})
</script>

<template>
  <div v-if="account" class="account-area">
    <el-dropdown trigger="click">
      <span class="account-trigger">
        <el-icon :size="18" style="margin-right: 6px"><UserFilled /></el-icon>
        <span class="username">{{ account.accountUsername }}</span>
        <el-icon :size="12" class="arrow"><slot name="arrow" /></el-icon>
      </span>

      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item disabled>
            <span class="dropdown-email">{{ account.accountEmail }}</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span @click="showChangePasswordDialog = true">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item divided>
            <span style="color: #F56C6C" @click="showDeactivateDialog = true">注销账号</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click="emit('logout')">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>

    <!-- 注销账号弹窗 -->
    <DeactivateDialog
      v-model="showDeactivateDialog"
      :email="account.accountEmail"
    />

    <!-- 修改密码弹窗 -->
    <ChangePasswordDialog
      v-model="showChangePasswordDialog"
      :email="account.accountEmail"
    />
  </div>
</template>

<style scoped>
.account-trigger {
  display: flex; align-items: center; cursor: pointer; color: #555;
  padding: 6px 12px; border-radius: 6px; transition: background .2s;
}
.account-trigger:hover { background: #f5f5f5; }
.username { font-size: 14px; max-width: 120px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.arrow { margin-left: 4px; color: #bbb; }
.dropdown-email { font-size: 12px; color: #999; }
</style>
