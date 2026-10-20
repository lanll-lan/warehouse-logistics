<template>
  <div>
    <h2>订单管理</h2>
    <el-table :data="list" border style="width: 100%; margin-top: 15px">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="orderNo" label="订单号"></el-table-column>
      <el-table-column prop="customerName" label="客户名称"></el-table-column>
      <el-table-column prop="productName" label="商品名称"></el-table-column>
      <el-table-column prop="quantity" label="数量"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'Order',
  data() {
    return {
      list: []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.$axios.get('/orders/list').then(res => {
        this.list = res.data.data
      })
    }
  }
}
</script>
