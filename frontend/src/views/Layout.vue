<template>
  <el-container style="height: 100vh">
    <el-aside width="200px" style="background: #545c64">
      <el-menu
        :default-active="$route.path"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        router
      >
        <el-menu-item index="/inventory">
          <i class="el-icon-box"></i>
          <span>库存</span>
        </el-menu-item>
        <el-menu-item index="/order">
          <i class="el-icon-document"></i>
          <span>订单</span>
        </el-menu-item>
        <el-menu-item index="/outbound">
          <i class="el-icon-sell"></i>
          <span>出库</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background: #fff; border-bottom: 1px solid #eee; display: flex; align-items: center; justify-content: space-between">
        <span>仓储物流管理系统</span>
        <el-button type="text" @click="logout">退出</el-button>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout',
  methods: {
    logout() {
      localStorage.removeItem('token')
      this.$router.push('/login')
    }
  }
}
</script>
