<script setup lang="ts">
import { ref } from 'vue'
import { postJson, post } from '@/net/index.js'
import { ElMessage } from 'element-plus'
import router from '@/router'

const props = defineProps<{
  modelValue: boolean
  email: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

// 步骤：1=警告确认, 2=输入验证码
const step = ref(1)
const code = ref('')
const sending = ref(false)
const confirming = ref(false)

// 关闭弹窗，重置状态
const close = () => {
  step.value = 1
  code.value = ''
  emit('update:modelValue', false)
}

// 第一步→第二步：发送验证码
const sendCode = () => {
  sending.value = true
  post('/api/auth/deactivate/send-code', {}, () => {
    ElMessage.success('验证码已发送，请查收邮件')
    step.value = 2
    sending.value = false
  }, (msg: any) => {
    sending.value = false
    return ElMessage.error(msg)
  })
}

// 第二步：确认注销
const confirmDeactivate = () => {
  if (!code.value) {
    ElMessage.warning('请输入验证码')
    return
  }
  confirming.value = true
  post('/api/auth/deactivate', { code: code.value }, () => {
    ElMessage.success('账号已注销')
    close()
    // 退出登录，跳回登录页
    router.push('/')
  }, (msg: any) => {
    confirming.value = false
    return ElMessage.error(msg)
  })
}
</script>

<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="close"
    title="注销账号"
    width="420px"
    :close-on-click-modal="false"
    center
  >
    <!-- 第一步：警告 -->
    <template v-if="step === 1">
      <div style="text-align: center; padding: 10px 0">
        <p style="font-size: 15px; color: #333; margin-bottom: 16px">
          注销后您的账号将被<strong style="color: #F56C6C">永久禁用</strong>，无法登录。
        </p>
        <p style="font-size: 13px; color: #999">
          我们将发送验证码到 <strong>{{ email }}</strong>
        </p>
      </div>
    </template>

    <!-- 第二步：输入验证码 -->
    <template v-else>
      <p style="text-align: center; font-size: 13px; color: #888; margin-bottom: 20px">
        验证码已发送至 <strong style="color: #667eea">{{ email }}</strong>
      </p>
      <el-input
        v-model="code"
        placeholder="请输入6位验证码"
        maxlength="6"
        size="large"
        style="margin-bottom: 4px"
      />
    </template>

    <template #footer>
      <el-button @click="close" :disabled="confirming">取消</el-button>
      <el-button
        v-if="step === 1"
        type="danger"
        @click="sendCode"
        :loading="sending"
      >
        发送验证码
      </el-button>
      <el-button
        v-else
        type="danger"
        @click="confirmDeactivate"
        :loading="confirming"
      >
        确认注销
      </el-button>
    </template>
  </el-dialog>
</template>
