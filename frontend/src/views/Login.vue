<template>
  <div class="login-container">
    <el-form
      :model="form"
      :rules="rules"
      ref="form"
      class="login-form"
      @keyup.enter.native="onSubmit"
    >
      <h3>仓储物流管理系统</h3>
      <el-form-item prop="username">
        <el-input
          v-model="form.username"
          placeholder="用户名"
          prefix-icon="el-icon-user"
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="form.password"
          type="password"
          placeholder="密码"
          prefix-icon="el-icon-lock"
          show-password
          autocomplete="off"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          :loading="loading"
          @click="onSubmit"
          style="width: 100%"
        >登 录</el-button>
      </el-form-item>
      <div class="tip">默认账号：admin / admin123</div>
    </el-form>
  </div>
</template>

<script>
import { authApi } from '../api'
import { setToken, setUser } from '../utils/auth'

export default {
  name: 'Login',
  data() {
    return {
      loading: false,
      form: { username: '', password: '' },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    onSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.loading = true
        authApi
          .login({ username: this.form.username, password: this.form.password })
          .then(data => {
            setToken(data.token)
            setUser(data.user)
            this.$store.commit('SET_USER', data.user)
            this.$router.push('/')
          })
          .catch(() => {})
          .finally(() => {
            this.loading = false
          })
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f5f5f5;
}
.login-form {
  width: 350px;
  padding: 30px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
.login-form h3 {
  text-align: center;
  margin-bottom: 24px;
}
.tip {
  text-align: center;
  font-size: 12px;
  color: #909399;
}
</style>
