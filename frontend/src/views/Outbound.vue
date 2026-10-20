<template>
  <div>
    <h2>出库管理</h2>
    <el-table :data="list" border style="width: 100%; margin-top: 15px">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="outboundNo" label="出库单号"></el-table-column>
      <el-table-column prop="productName" label="商品名称"></el-table-column>
      <el-table-column prop="quantity" label="数量"></el-table-column>
      <el-table-column prop="warehouseLocation" label="库位"></el-table-column>
      <el-table-column prop="outboundTime" label="出库时间"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'Outbound',
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
      this.$axios.get('/outbound/list').then(res => {
        this.list = res.data.data
      })
    }
  }
}
</script>
