<script setup lang="ts">
import { ref } from 'vue'
import { post } from '@/net/index.js'
import { ElMessage } from 'element-plus'
import router from '@/router'

const props = defineProps<{
  modelValue: boolean
  email: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
}>()

const step = ref(1)
const code = ref('')
const password = ref('')
const rePassword = ref('')
const sending = ref(false)
const submitting = ref(false)

const close = () => {
  step.value = 1
  code.value = ''
  password.value = ''
  rePassword.value = ''
  emit('update:modelValue', false)
}

// 第一步→第二步：发送验证码
const sendCode = () => {
  sending.value = true
  post('/api/auth/change-password/send-code', {}, () => {
    ElMessage.success('验证码已发送，请查收邮件')
    step.value = 2
    sending.value = false
  }, (msg: any) => {
    sending.value = false
    return ElMessage.error(msg)
  })
}

// 第二步：提交修改密码
const submitChange = () => {
  if (!code.value) {
    ElMessage.warning('请输入验证码')
    return
  }
  if (!password.value) {
    ElMessage.warning('请输入新密码')
    return
  }
  if (password.value.length < 6) {
    ElMessage.warning('密码长度不能少于6位')
    return
  }
  if (password.value !== rePassword.value) {
    ElMessage.warning('两次输入的密码不一致')
    return
  }
  submitting.value = true
  post('/api/auth/change-password', {
    code: code.value,
    password: password.value
  }, () => {
    ElMessage.success('密码修改成功，请重新登录')
    close()
    router.push('/')
  }, (msg: any) => {
    submitting.value = false
    return ElMessage.error(msg)
  })
}
</script>

<template>
  <el-dialog
    :model-value="modelValue"
    @update:model-value="close"
    title="修改密码"
    width="420px"
    :close-on-click-modal="false"
    center
  >
    <!-- 第一步：说明 -->
    <template v-if="step === 1">
      <div style="text-align: center; padding: 10px 0">
        <p style="font-size: 14px; color: #555; margin-bottom: 16px">
          为了您的账户安全，需验证您的身份
        </p>
        <p style="font-size: 13px; color: #999">
          验证码将发送至 <strong style="color: #667eea">{{ email }}</strong>
        </p>
      </div>
    </template>

    <!-- 第二步：输验证码 + 新密码 -->
    <template v-else>
      <p style="text-align: center; font-size: 13px; color: #888; margin-bottom: 20px">
        验证码已发送至 <strong style="color: #667eea">{{ email }}</strong>
      </p>

      <el-input
        v-model="code"
        placeholder="请输入6位验证码"
        maxlength="6"
        size="large"
        style="margin-bottom: 16px"
      />

      <el-input
        v-model="password"
        type="password"
        placeholder="请输入新密码"
        size="large"
        show-password
        style="margin-bottom: 16px"
      />

      <el-input
        v-model="rePassword"
        type="password"
        placeholder="请再次输入新密码"
        size="large"
        show-password
      />
    </template>

    <template #footer>
      <el-button @click="close" :disabled="submitting">取消</el-button>
      <el-button
        v-if="step === 1"
        type="primary"
        @click="sendCode"
        :loading="sending"
      >
        发送验证码
      </el-button>
      <el-button
        v-else
        type="primary"
        @click="submitChange"
        :loading="submitting"
      >
        确认修改
      </el-button>
    </template>
  </el-dialog>
</template>
