<script setup lang="ts">
import { computed } from 'vue'
import { UserFilled, List } from '@element-plus/icons-vue'
import { getStateTag } from '@/types/account'
import type { Account } from '@/types/account'

const props = defineProps<{
  accountList: Account[]
  loading: boolean
}>()

const totalUsers   = computed(() => props.accountList.length)
const activeUsers  = computed(() => props.accountList.filter(a => a.accountState === 1).length)
const disabledUsers = computed(() => props.accountList.filter(a => a.accountState === 0).length)
</script>

<template>
  <!-- 统计卡片 -->
  <div class="stats-grid">
    <div class="stat-card blue">
      <div class="stat-left">
        <div class="stat-number">{{ totalUsers }}</div>
        <div class="stat-label">用户总数</div>
      </div>
      <div class="stat-icon-box blue"><el-icon :size="36"><UserFilled /></el-icon></div>
    </div>
    <div class="stat-card green">
      <div class="stat-left">
        <div class="stat-number">{{ activeUsers }}</div>
        <div class="stat-label">启用用户</div>
      </div>
      <div class="stat-icon-box green"><el-icon :size="36"><UserFilled /></el-icon></div>
    </div>
    <div class="stat-card red">
      <div class="stat-left">
        <div class="stat-number">{{ disabledUsers }}</div>
        <div class="stat-label">禁用用户</div>
      </div>
      <div class="stat-icon-box red"><el-icon :size="36"><UserFilled /></el-icon></div>
    </div>
    <div class="stat-card purple">
      <div class="stat-left">
        <div class="stat-number">0</div>
        <div class="stat-label">图书总数</div>
      </div>
      <div class="stat-icon-box purple"><el-icon :size="36"><List /></el-icon></div>
    </div>
  </div>

  <!-- 只读用户表 -->
  <div class="table-card">
    <h3 class="table-title">用户列表</h3>
    <el-table
      :data="accountList"
      v-loading="loading"
      stripe border
      style="width: 100%; margin-top: 16px"
      empty-text="暂无用户数据"
    >
      <el-table-column label="序号" width="80" align="center">
        <template #default="{ $index }">{{ $index + 1 }}</template>
      </el-table-column>
      <el-table-column prop="accountUsername" label="用户名" min-width="160" align="center" />
      <el-table-column label="密码" min-width="200" align="center">
        <template #default="{ row }">
          <span class="password-masked">{{ row.accountPassword }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="getStateTag(row.accountState).type" size="small" effect="dark">
            {{ getStateTag(row.accountState).text }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}
.stat-card {
  background: #fff; border-radius: 10px; padding: 24px 20px;
  display: flex; align-items: center; justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0,0,0,.06);
  transition: transform .2s, box-shadow .2s;
}
.stat-card:hover { transform: translateY(-3px); box-shadow: 0 4px 16px rgba(0,0,0,.12); }
.stat-number { font-size: 30px; font-weight: 700; color: #2c3e50; margin-bottom: 4px; }
.stat-label  { font-size: 13px; color: #999; }
.stat-icon-box {
  width: 64px; height: 64px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center; color: #fff;
}
.stat-icon-box.blue   { background: linear-gradient(135deg, #409EFF, #337ecc); }
.stat-icon-box.green  { background: linear-gradient(135deg, #67C23A, #529b2e); }
.stat-icon-box.red    { background: linear-gradient(135deg, #F56C6C, #c45656); }
.stat-icon-box.purple { background: linear-gradient(135deg, #667eea, #764ba2); }

.table-card {
  background: #fff; border-radius: 10px; padding: 20px 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,.06);
}
.table-title {
  font-size: 17px; font-weight: 600; color: #2c3e50; margin: 0;
  position: relative; padding-left: 14px;
}
.table-title::before {
  content: ''; position: absolute; left: 0; top: 2px;
  height: 18px; width: 4px; border-radius: 2px;
  background: linear-gradient(135deg, #667eea, #764ba2);
}
.password-masked { font-family: 'Courier New', monospace; font-size: 13px; color: #999; }

@media (max-width: 1200px) { .stats-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 768px)  { .stats-grid { grid-template-columns: 1fr; } }
</style>
