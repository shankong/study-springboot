<script setup lang="ts">
  import {reactive} from 'vue'
  import {Lock, User} from "@element-plus/icons-vue";
  import {ElMessage} from "element-plus";
  import {post} from "@/net/index.js";
import router from '@/router';

  const form = reactive({
    username: '',
    password: '',
    remember: false
  })
  const login = () => {
    if(!form.username || !form.password){
      ElMessage.warning('请输入用户名和密码')
    }else {
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
  <div style="margin: 0 40px; text-align: center">
    <div style="margin-top: 150px">
      <div style="font-size: 25px; font-weight: bold">登陆</div>
      <div style="font-size: 14px; color: gray">进入系统之前请先输入用户名和密码登录</div>
    </div>
    <div style="margin-top: 50px">
      <el-input v-model="form.username" type="text" placeholder="用户名/邮箱" >
        <template #prefix>
          <el-icon><User /></el-icon>
        </template>
      </el-input>
    </div>
    <div style="margin-top: 10px">
      <el-input v-model="form.password" type="password" placeholder="密码">
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
    </div>
    <el-row>
      <el-col :span="12" style="text-align: left">
        <el-checkbox v-model="form.remember" label="记住我" size="large" />
      </el-col>
      <el-col :span="12" style="text-align: right">
        <el-link>忘记密码?</el-link>
      </el-col>
    </el-row>
    <div style="margin-top: 100px">
      <el-button @click="login()" type="success" style="width: 300px" plain>立即登录</el-button>
    </div>
    <el-divider style="color: gray">没有账号</el-divider>
    <div style="margin-top: 30px">
      <el-button type="warning" style="width: 300px" plain>注册账号</el-button>
    </div>
  </div>
</template>

<style scoped>

</style>