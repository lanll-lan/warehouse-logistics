<template>
  <el-container style="height: 100vh">
    <el-aside width="210px" style="background: #545c64">
      <div class="logo">仓储物流管理系统</div>
      <el-menu
        :default-active="$route.path"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        router
      >
        <el-menu-item index="/inventory">
          <i class="el-icon-box"></i>
          <span>库存管理</span>
        </el-menu-item>
        <el-menu-item index="/order">
          <i class="el-icon-document"></i>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/outbound">
          <i class="el-icon-sell"></i>
          <span>出库管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="page-title">{{ $route.meta.title || '首页' }}</span>
        <div class="user-area">
          <span v-if="user" class="user-name">
            <i class="el-icon-user-solid"></i>
            {{ user.username }}
            <el-tag size="mini" type="info">{{ roleLabel }}</el-tag>
          </span>
          <el-button type="text" @click="logout">退出登录</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { clearAuth } from '../utils/auth'

export default {
  name: 'Layout',
  computed: {
    user() {
      return this.$store.state.user
    },
    roleLabel() {
      const role = this.user && this.user.role
      if (role === 'ADMIN') return '管理员'
      if (role === 'OPERATOR') return '操作员'
      return role || '-'
    }
  },
  methods: {
    logout() {
      clearAuth()
      this.$store.commit('SET_USER', null)
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #ffd04b;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #434a50;
}
.header {
  background: #fff;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.page-title {
  font-size: 16px;
  font-weight: 500;
}
.user-area {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-name {
  font-size: 14px;
  color: #606266;
}
</style>
