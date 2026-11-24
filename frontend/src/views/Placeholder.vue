<template>
  <div class="placeholder">
    <i class="el-icon-setting"></i>
    <h2>{{ title }}</h2>
    <p>该功能开发中，由对应任务负责人实现。</p>
  </div>
</template>

<script>
export default {
  name: 'Placeholder',
  computed: {
    title() {
      return this.$route.meta.title || '开发中'
    }
  }
}
</script>

<style scoped>
.placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 60vh;
  color: #909399;
}
.placeholder i {
  font-size: 60px;
  margin-bottom: 20px;
}
.placeholder h2 {
  margin-bottom: 10px;
}
</style>
