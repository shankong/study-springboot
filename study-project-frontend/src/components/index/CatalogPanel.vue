<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { get, post, postJson } from '@/net/index.js'
import { ElMessage, ElMessageBox } from 'element-plus'

interface Catalog {
  catalogId: string
  catalogName: string
  catalogNumber: string
  catalogState: number
}

const list = ref<Catalog[]>([])
const loading = ref(false)

const loadList = () => {
  loading.value = true
  get('/api/catalog/list', (data: any) => {
    list.value = (data as Catalog[]) || []
    loading.value = false
    clearSelection()
  })
}

// ==================== 多选 ====================
const selectedIds = ref<string[]>([])

const handleSelectChange = (rows: Catalog[]) => {
  selectedIds.value = rows.map(r => r.catalogId)
}

const clearSelection = () => {
  selectedIds.value = []
}

// ==================== 批量删除 ====================
const batchDelete = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请先选择要删除的栏目')
    return
  }
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedIds.value.length} 个栏目吗？`,
    '批量删除',
    { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
  ).then(() => {
    postJson('/api/catalog/batch-deactivate', selectedIds.value, () => {
      ElMessage.success('批量删除成功')
      loadList()
    })
  }).catch(() => {})
}

// ==================== 上下移动 ====================
const move = (catalogId: string, direction: string) => {
  post('/api/catalog/move', { catalogId, direction }, () => {
    loadList()
  })
}

// ==================== 新增 / 编辑弹窗 ====================
const dialogVisible = ref(false)
const isEdit = ref(false)
const editingId = ref('')

const form = reactive({
  catalogName: '',
  catalogNumber: '',
  catalogState: 1
})

const submitting = ref(false)

const openAdd = () => {
  isEdit.value = false
  editingId.value = ''
  form.catalogName = ''
  form.catalogNumber = ''
  form.catalogState = 1
  dialogVisible.value = true
}

const openEdit = (row: Catalog) => {
  isEdit.value = true
  editingId.value = row.catalogId
  form.catalogName = row.catalogName
  form.catalogNumber = row.catalogNumber
  form.catalogState = row.catalogState
  dialogVisible.value = true
}

const submit = () => {
  if (!form.catalogName) {
    ElMessage.warning('请输入栏目名称')
    return
  }
  if (!form.catalogNumber) {
    ElMessage.warning('请输入栏目编号')
    return
  }

  submitting.value = true
  const url = isEdit.value ? '/api/catalog/update' : '/api/catalog/insert'
  const body = isEdit.value
    ? { catalogId: editingId.value, ...form }
    : { ...form }

  postJson(url, body, () => {
    submitting.value = false
    ElMessage.success(isEdit.value ? '更改成功' : '添加成功')
    dialogVisible.value = false
    loadList()
  }, (msg: any) => {
    submitting.value = false
    return ElMessage.error(msg)
  })
}

// ==================== 单条删除 ====================
const doDelete = (row: Catalog) => {
  ElMessageBox.confirm(
    `确定要删除栏目「${row.catalogName}」吗？`,
    '删除确认',
    { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
  ).then(() => {
    get(`/api/catalog/deactivate?catalogId=${row.catalogId}`, () => {
      ElMessage.success('删除成功')
      loadList()
    })
  }).catch(() => {})
}

// ==================== 状态标签 ====================
const stateTag = (state: number) => {
  return state === 1
    ? { text: '启用', type: 'success' as const }
    : { text: '禁用', type: 'danger' as const }
}

const isFirst = (index: number) => index === 0
const isLast  = (index: number) => index === list.value.length - 1

onMounted(loadList)
</script>

<template>
  <div class="table-card">
    <div class="table-header">
      <h3 class="table-title">栏目管理</h3>
      <div class="header-actions">
        <el-button
          v-if="selectedIds.length > 0"
          type="danger"
          size="small"
          plain
          @click="batchDelete"
        >
          批量删除（{{ selectedIds.length }}）
        </el-button>
        <el-button type="primary" size="small" @click="openAdd">新增栏目</el-button>
      </div>
    </div>

    <!-- 表格 -->
    <el-table
      ref="tableRef"
      :data="list"
      v-loading="loading"
      stripe border
      style="width: 100%"
      empty-text="暂无栏目数据"
      @selection-change="handleSelectChange"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="序号" width="80" align="center">
        <template #default="{ $index }">{{ $index + 1 }}</template>
      </el-table-column>
      <el-table-column prop="catalogName"   label="栏目名称"  min-width="160" align="center" />
      <el-table-column prop="catalogNumber" label="栏目编号"  min-width="140" align="center" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="stateTag(row.catalogState).type" size="small" effect="dark">
            {{ stateTag(row.catalogState).text }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" align="center" fixed="right">
        <template #default="{ row, $index }">
          <el-button :disabled="isFirst($index)" size="small" plain @click="move(row.catalogId, 'up')">上移</el-button>
          <el-button :disabled="isLast($index)"  size="small" plain @click="move(row.catalogId, 'down')">下移</el-button>
          <el-divider direction="vertical" />
          <el-button type="primary" size="small" link @click="openEdit(row)">编辑</el-button>
          <el-divider direction="vertical" />
          <el-button type="danger" size="small" link @click="doDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑栏目' : '新增栏目'"
      width="440px"
      :close-on-click-modal="false"
      center
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="栏目名称">
          <el-input v-model="form.catalogName" placeholder="请输入栏目名称" />
        </el-form-item>
        <el-form-item label="栏目编号">
          <el-input v-model="form.catalogNumber" placeholder="请输入栏目编号" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.catalogState">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit" :loading="submitting">
          {{ isEdit ? '保存' : '添加' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.table-card {
  background: #fff; border-radius: 10px; padding: 20px 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,.06);
}
.table-header {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 20px;
}
.header-actions {
  display: flex; align-items: center; gap: 10px;
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
</style>
