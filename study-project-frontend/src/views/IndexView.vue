<script setup lang="ts">
  import { get } from "@/net";
import router from "@/router";
  import {ElMessage} from "element-plus";

  const logout = () => {
    get('/api/auth/logout', (message) => {
        ElMessage.success(message)
        router.push('/')
    })
  }
</script>

<template>
  <div>欢迎</div>
  <div>
    <el-button @click="logout()" type="danger" plain>退出登录</el-button>
  </div>
</template>

<style scoped>

</style>