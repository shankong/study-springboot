<script setup>
  import { ref } from 'vue'
  import bgImg from '@/assets/imges/login-bg.png'
  import { User, Lock } from '@element-plus/icons-vue'

  const username = ref('')
  const password = ref('')
  const checked1 = ref(false)
</script>

<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1">
      <el-image style="width: 100%; height: 100%" fit="cover"
          :src="bgImg" />
    </div>
    <div style="width: 500px; background-color: white">
      <div style="margin: 0 40px; text-align: center">
        <div style="margin-top: 150px">
          <div style="font-size: 25px; font-weight: bold">登陆</div>
          <div style="font-size: 14px; color: gray">进入系统之前请先输入用户名和密码登录</div>
        </div>
        <div style="margin-top: 50px">
          <el-input type="text" placeholder="用户名/邮箱" v-model="username" >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </div>
        <div style="margin-top: 10px">
          <el-input type="password" placeholder="密码" v-model="password" >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </div>
        <el-row>
          <el-col :span="12" style="text-align: left">
            <el-checkbox v-model="checked1" label="记住我" size="large" />
          </el-col>
          <el-col :span="12" style="text-align: right">
            <el-link>忘记密码?</el-link>
          </el-col>
        </el-row>
        <div style="margin-top: 100px">
          <el-button type="success" style="width: 300px" plain>立即登录</el-button>
        </div>
        <el-divider style="color: gray">没有账号</el-divider>
        <div style="margin-top: 30px">
          <el-button type="warning" style="width: 300px" plain>注册账号</el-button>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped></style>
