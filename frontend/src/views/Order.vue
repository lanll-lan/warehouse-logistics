<template>
  <div>
    <h2 class="page-header">订单管理</h2>

    <el-form :inline="true" :model="query" class="query-form">
      <el-form-item label="订单号">
        <el-input
          v-model="query.orderNo"
          clearable
          placeholder="模糊查询"
          style="width: 180px"
          @keyup.enter.native="loadData"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select
          v-model="query.status"
          clearable
          placeholder="全部状态"
          style="width: 140px"
        >
          <el-option
            v-for="s in statusOptions"
            :key="s.value"
            :label="s.label"
            :value="s.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="openCreate">新增订单</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" border v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="70"></el-table-column>
      <el-table-column prop="orderNo" label="订单号" min-width="165"></el-table-column>
      <el-table-column prop="customerName" label="客户名称" min-width="120"></el-table-column>
      <el-table-column label="商品" min-width="160">
        <template slot-scope="scope">{{ productLabel(scope.row.productId) }}</template>
      </el-table-column>
      <el-table-column prop="quantity" label="需求数量" width="110"></el-table-column>
      <el-table-column label="状态" width="105">
        <template slot-scope="scope">
          <el-tag :type="orderStatus(scope.row.status).type" size="small">
            {{ orderStatus(scope.row.status).label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="180"></el-table-column>
    </el-table>

    <el-dialog
      title="新增订单"
      :visible.sync="createVisible"
      width="500px"
      @closed="resetForm"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="90px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="如 ORD20261117002"></el-input>
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="form.customerName" placeholder="请输入客户名称"></el-input>
        </el-form-item>
        <el-form-item label="商品" prop="productId">
          <el-select v-model="form.productId" placeholder="请选择商品" style="width: 100%">
            <el-option
              v-for="p in productOptions"
              :key="p.value"
              :label="p.label"
              :value="p.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number
            v-model="form.quantity"
            :min="1"
            :step="1"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitCreate">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { orderApi } from '../api'
import { currentUserId } from '../utils/auth'
import {
  PRODUCT_OPTIONS,
  ORDER_STATUS_OPTIONS,
  productLabel,
  orderStatus
} from '../data/dictionary'

export default {
  name: 'Order',
  data() {
    return {
      loading: false,
      submitting: false,
      list: [],
      query: { orderNo: '', status: '' },
      createVisible: false,
      productOptions: PRODUCT_OPTIONS,
      statusOptions: ORDER_STATUS_OPTIONS,
      form: this.emptyForm(),
      rules: {
        orderNo: [{ required: true, message: '请输入订单号', trigger: 'blur' }],
        customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
        productId: [{ required: true, message: '请选择商品', trigger: 'change' }],
        quantity: [{ required: true, message: '请输入数量', trigger: 'change' }]
      }
    }
  },
  methods: {
    productLabel,
    orderStatus,
    emptyForm() {
      return { orderNo: '', customerName: '', productId: null, quantity: 1 }
    },
    loadData() {
      this.loading = true
      const params = {}
      if (this.query.orderNo) params.orderNo = this.query.orderNo
      if (this.query.status) params.status = this.query.status
      orderApi
        .list(params)
        .then(data => {
          this.list = data || []
        })
        .catch(() => {})
        .finally(() => {
          this.loading = false
        })
    },
    resetQuery() {
      this.query = { orderNo: '', status: '' }
      this.loadData()
    },
    openCreate() {
      this.createVisible = true
    },
    resetForm() {
      this.form = this.emptyForm()
      if (this.$refs.form) this.$refs.form.resetFields()
    },
    submitCreate() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const createdBy = currentUserId()
        if (!createdBy) {
          this.$message.error('登录态异常，请重新登录')
          return
        }
        this.submitting = true
        orderApi
          .create({
            orderNo: this.form.orderNo,
            customerName: this.form.customerName,
            productId: String(this.form.productId),
            quantity: String(this.form.quantity),
            createdBy: String(createdBy)
          })
          .then(() => {
            this.$message.success('订单创建成功')
            this.createVisible = false
            this.loadData()
          })
          .catch(() => {})
          .finally(() => {
            this.submitting = false
          })
      })
    }
  },
  created() {
    this.loadData()
  }
}
</script>

<style scoped>
.page-header {
  margin-bottom: 15px;
  font-size: 20px;
}
.query-form {
  background: #fafafa;
  padding: 12px 12px 0;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 15px;
}
</style>
