<template>
  <div>
    <h2>库存管理</h2>
    <el-table :data="list" border style="width: 100%; margin-top: 15px">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="productName" label="商品名称"></el-table-column>
      <el-table-column prop="quantity" label="数量"></el-table-column>
      <el-table-column prop="warehouseLocation" label="库位"></el-table-column>
      <el-table-column prop="updateTime" label="更新时间"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'Inventory',
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
      this.$axios.get('/inventory/list').then(res => {
        this.list = res.data.data
      })
    }
  }
}
</script>
