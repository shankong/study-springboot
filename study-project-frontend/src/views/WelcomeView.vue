<script setup lang="ts">

  import bgImg from '@/assets/imges/login-bg.png'

</script>

<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1">
      <el-image style="width: 100%; height: 100%" fit="cover"
                :src="bgImg" />
    </div>
    <div style="width: 500px; background-color: white">
      <router-view/>
    </div>
  </div>
</template>

<style scoped>

</style>