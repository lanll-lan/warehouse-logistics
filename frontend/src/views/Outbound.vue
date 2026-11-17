<template>
  <div>
    <h2 class="page-header">出库管理</h2>

    <el-form :inline="true" :model="query" class="query-form">
      <el-form-item label="出库单号">
        <el-input
          v-model="query.outboundNo"
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
        <el-button type="success" icon="el-icon-plus" @click="openCreate">新建出库单</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="list" border v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="70"></el-table-column>
      <el-table-column prop="outboundNo" label="出库单号" min-width="160"></el-table-column>
      <el-table-column label="关联订单" min-width="150">
        <template slot-scope="scope">订单 #{{ scope.row.orderId }}</template>
      </el-table-column>
      <el-table-column label="关联库存" min-width="120">
        <template slot-scope="scope">库存 #{{ scope.row.inventoryId }}</template>
      </el-table-column>
      <el-table-column label="实拣量" width="95">
        <template slot-scope="scope">
          {{ scope.row.pickedQty == null ? '-' : scope.row.pickedQty }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="105">
        <template slot-scope="scope">
          <el-tag :type="outboundStatus(scope.row.status).type" size="small">
            {{ outboundStatus(scope.row.status).label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="运单号" min-width="150">
        <template slot-scope="scope">{{ scope.row.trackingNo || '-' }}</template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="175"></el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === 'DRAFT'"
            type="primary"
            size="mini"
            @click="openPick(scope.row)"
          >拣选</el-button>
          <el-button
            v-if="scope.row.status === 'PICKED'"
            type="success"
            size="mini"
            @click="openConfirm(scope.row)"
          >确认出库</el-button>
          <span v-if="scope.row.status === 'CONFIRMED'" class="done-text">已完成</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新建出库单（草稿） -->
    <el-dialog
      title="新建出库单（草稿）"
      :visible.sync="createVisible"
      width="620px"
      @closed="resetCreateForm"
    >
      <el-form
        :model="createForm"
        :rules="createRules"
        ref="createForm"
        label-width="100px"
        v-loading="optionsLoading"
      >
        <el-form-item label="出库单号" prop="outboundNo">
          <el-input v-model="createForm.outboundNo" placeholder="如 OUT20261117002"></el-input>
        </el-form-item>
        <el-form-item label="关联订单" prop="orderId">
          <el-select
            v-model="createForm.orderId"
            placeholder="请选择待发货订单"
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="o in orderOptions"
              :key="o.id"
              :label="orderOptionLabel(o)"
              :value="o.id"
            ></el-option>
          </el-select>
          <div v-if="!optionsLoading && orderOptions.length === 0" class="form-tip">
            暂无可出库订单（仅"待发货且未关联出库单"的订单可选）
          </div>
        </el-form-item>
        <el-form-item label="出库库存" prop="inventoryId">
          <el-select
            v-model="createForm.inventoryId"
            placeholder="请选择库存记录"
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="inv in inventoryOptions"
              :key="inv.id"
              :label="inventoryOptionLabel(inv)"
              :value="inv.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="createVisible = false">取消</el-button>
        <el-button type="primary" :loading="creating" @click="submitCreate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 拣选 -->
    <el-dialog
      title="拣选"
      :visible.sync="pickVisible"
      width="460px"
      @closed="resetPickForm"
    >
      <el-form :model="pickForm" :rules="pickRules" ref="pickForm" label-width="100px">
        <el-form-item label="出库单号">
          <span>{{ pickForm.outboundNo }}</span>
        </el-form-item>
        <el-form-item label="实拣数量" prop="pickedQty">
          <el-input-number
            v-model="pickForm.pickedQty"
            :min="1"
            :step="1"
            style="width: 100%"
          ></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="pickVisible = false">取消</el-button>
        <el-button type="primary" :loading="picking" @click="submitPick">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 确认出库 -->
    <el-dialog
      title="确认出库"
      :visible.sync="confirmVisible"
      width="460px"
      @closed="resetConfirmForm"
    >
      <el-form
        :model="confirmForm"
        :rules="confirmRules"
        ref="confirmForm"
        label-width="100px"
      >
        <el-form-item label="出库单号">
          <span>{{ confirmForm.outboundNo }}</span>
        </el-form-item>
        <el-form-item label="实拣数量">
          <span>{{ confirmForm.pickedQty }}</span>
        </el-form-item>
        <el-form-item label="运单号" prop="trackingNo">
          <el-input v-model="confirmForm.trackingNo" placeholder="请输入模拟运单号"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="confirmVisible = false">取消</el-button>
        <el-button type="primary" :loading="confirming" @click="submitConfirm">确认出库</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { outboundApi, orderApi, inventoryApi } from '../api'
import { currentUserId } from '../utils/auth'
import {
  OUTBOUND_STATUS_OPTIONS,
  outboundStatus,
  productLabel,
  locationLabel
} from '../data/dictionary'

export default {
  name: 'Outbound',
  data() {
    return {
      loading: false,
      list: [],
      query: { outboundNo: '', status: '' },
      statusOptions: OUTBOUND_STATUS_OPTIONS,

      // 新建草稿
      createVisible: false,
      optionsLoading: false,
      orderOptions: [],
      inventoryOptions: [],
      createForm: this.emptyCreateForm(),
      createRules: {
        outboundNo: [{ required: true, message: '请输入出库单号', trigger: 'blur' }],
        orderId: [{ required: true, message: '请选择关联订单', trigger: 'change' }],
        inventoryId: [{ required: true, message: '请选择出库库存', trigger: 'change' }]
      },
      creating: false,

      // 拣选
      pickVisible: false,
      pickForm: { id: null, outboundNo: '', pickedQty: 1 },
      pickRules: {
        pickedQty: [{ required: true, message: '请输入实拣数量', trigger: 'change' }]
      },
      picking: false,

      // 确认出库
      confirmVisible: false,
      confirmForm: { id: null, outboundNo: '', pickedQty: null, trackingNo: '' },
      confirmRules: {
        trackingNo: [{ required: true, message: '请输入运单号', trigger: 'blur' }]
      },
      confirming: false
    }
  },
  methods: {
    outboundStatus,
    productLabel,
    locationLabel,
    emptyCreateForm() {
      return { outboundNo: '', orderId: null, inventoryId: null }
    },
    orderOptionLabel(o) {
      return (
        o.orderNo +
        ' | ' + o.customerName +
        ' | ' + productLabel(o.productId) +
        ' | 需求' + o.quantity
      )
    },
    inventoryOptionLabel(inv) {
      return (
        '库存#' + inv.id +
        ' | ' + productLabel(inv.productId) +
        ' | ' + locationLabel(inv.locationId) +
        ' | 可用' + inv.quantity
      )
    },
    loadData() {
      this.loading = true
      const params = {}
      if (this.query.outboundNo) params.outboundNo = this.query.outboundNo
      if (this.query.status) params.status = this.query.status
      outboundApi
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
      this.query = { outboundNo: '', status: '' }
      this.loadData()
    },

    // ---------- 新建草稿 ----------
    openCreate() {
      this.createVisible = true
      this.optionsLoading = true
      // 后端无 product/location/订单下拉接口，这里临时取列表并在前端组装选项；
      // 过滤掉已有出库单的订单（数据库 order_id 有唯一约束）。
      Promise.all([orderApi.list(), inventoryApi.list(), outboundApi.list()])
        .then(([orders, inventories, outbounds]) => {
          const usedOrderIds = new Set((outbounds || []).map(o => o.orderId))
          this.orderOptions = (orders || []).filter(
            o => o.status === 'PENDING' && !usedOrderIds.has(o.id)
          )
          this.inventoryOptions = inventories || []
        })
        .catch(() => {})
        .finally(() => {
          this.optionsLoading = false
        })
    },
    resetCreateForm() {
      this.createForm = this.emptyCreateForm()
      if (this.$refs.createForm) this.$refs.createForm.resetFields()
    },
    submitCreate() {
      this.$refs.createForm.validate(valid => {
        if (!valid) return
        const createdBy = currentUserId()
        if (!createdBy) {
          this.$message.error('登录态异常，请重新登录')
          return
        }
        this.creating = true
        outboundApi
          .draft({
            outboundNo: this.createForm.outboundNo,
            orderId: String(this.createForm.orderId),
            inventoryId: String(this.createForm.inventoryId),
            createdBy: String(createdBy)
          })
          .then(() => {
            this.$message.success('出库草稿创建成功')
            this.createVisible = false
            this.loadData()
          })
          .catch(() => {})
          .finally(() => {
            this.creating = false
          })
      })
    },

    // ---------- 拣选 ----------
    openPick(row) {
      this.pickForm = {
        id: row.id,
        outboundNo: row.outboundNo,
        pickedQty: row.pickedQty || 1
      }
      this.pickVisible = true
    },
    resetPickForm() {
      this.pickForm = { id: null, outboundNo: '', pickedQty: 1 }
      if (this.$refs.pickForm) this.$refs.pickForm.resetFields()
    },
    submitPick() {
      this.$refs.pickForm.validate(valid => {
        if (!valid) return
        this.picking = true
        outboundApi
          .pick(this.pickForm.id, this.pickForm.pickedQty)
          .then(() => {
            this.$message.success('拣选完成，单据状态已更新为「已拣选」')
            this.pickVisible = false
            this.loadData()
          })
          .catch(() => {})
          .finally(() => {
            this.picking = false
          })
      })
    },

    // ---------- 确认出库 ----------
    openConfirm(row) {
      this.confirmForm = {
        id: row.id,
        outboundNo: row.outboundNo,
        pickedQty: row.pickedQty,
        trackingNo: ''
      }
      this.confirmVisible = true
    },
    resetConfirmForm() {
      this.confirmForm = { id: null, outboundNo: '', pickedQty: null, trackingNo: '' }
      if (this.$refs.confirmForm) this.$refs.confirmForm.resetFields()
    },
    submitConfirm() {
      this.$refs.confirmForm.validate(valid => {
        if (!valid) return
        const confirmedBy = currentUserId()
        if (!confirmedBy) {
          this.$message.error('登录态异常，请重新登录')
          return
        }
        this.confirming = true
        outboundApi
          .confirm(this.confirmForm.id, this.confirmForm.trackingNo, confirmedBy)
          .then(() => {
            this.$message.success('确认出库成功，库存已扣减')
            this.confirmVisible = false
            this.loadData()
          })
          .catch(() => {})
          .finally(() => {
            this.confirming = false
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
.form-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.5;
  padding-top: 4px;
}
.done-text {
  color: #909399;
  font-size: 13px;
}
</style>
