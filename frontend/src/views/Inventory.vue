<template>
  <div>
    <h2 class="page-header">库存管理</h2>

    <el-form :inline="true" :model="query" class="query-form">
      <el-form-item label="商品">
        <el-select
          v-model="query.productId"
          clearable
          placeholder="全部商品"
          style="width: 190px"
        >
          <el-option
            v-for="p in productOptions"
            :key="p.value"
            :label="p.label"
            :value="p.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="库位">
        <el-select
          v-model="query.locationId"
          clearable
          placeholder="全部库位"
          style="width: 190px"
        >
          <el-option
            v-for="l in locationOptions"
            :key="l.value"
            :label="l.label"
            :value="l.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" border v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column label="商品" min-width="180">
        <template slot-scope="scope">{{ productLabel(scope.row.productId) }}</template>
      </el-table-column>
      <el-table-column label="库位" min-width="170">
        <template slot-scope="scope">{{ locationLabel(scope.row.locationId) }}</template>
      </el-table-column>
      <el-table-column prop="quantity" label="账面数量" width="120"></el-table-column>
      <el-table-column prop="version" label="版本号" width="100"></el-table-column>
      <el-table-column prop="updatedAt" label="更新时间" min-width="190"></el-table-column>
    </el-table>

    <el-alert
      class="tip"
      title="说明：库存查询已对接后端；库存调整（入库/盘点）接口后端尚未开放，界面暂不提供操作入口。"
      type="info"
      :closable="false"
      show-icon
    ></el-alert>
  </div>
</template>

<script>
import { inventoryApi } from '../api'
import {
  PRODUCT_OPTIONS,
  LOCATION_OPTIONS,
  productLabel,
  locationLabel
} from '../data/dictionary'

export default {
  name: 'Inventory',
  data() {
    return {
      loading: false,
      list: [],
      query: { productId: null, locationId: null },
      productOptions: PRODUCT_OPTIONS,
      locationOptions: LOCATION_OPTIONS
    }
  },
  methods: {
    productLabel,
    locationLabel,
    loadData() {
      this.loading = true
      const params = {}
      if (this.query.productId != null) params.productId = this.query.productId
      if (this.query.locationId != null) params.locationId = this.query.locationId
      inventoryApi
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
      this.query = { productId: null, locationId: null }
      this.loadData()
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
}
.tip {
  margin-top: 15px;
}
</style>
