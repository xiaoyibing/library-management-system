<template>
  <div class="borrow-page">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ borrowStats.borrowing }}</div>
            <div class="stat-label">借阅中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ borrowStats.returned }}</div>
            <div class="stat-label">已归还</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card overdue">
          <div class="stat-content">
            <div class="stat-value">{{ borrowStats.overdue }}</div>
            <div class="stat-label">已逾期</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 借阅记录 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span>我的借阅记录</span>
          <el-radio-group v-model="statusFilter" @change="loadData">
            <el-radio-button value="">全部</el-radio-button>
            <el-radio-button value="BORROWED">借阅中</el-radio-button>
            <el-radio-button value="RETURNED">已归还</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="bookName" label="图书名称" min-width="180" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="borrowTime" label="借阅时间" width="160" />
        <el-table-column prop="dueTime" label="应还时间" width="160" />
        <el-table-column prop="returnTime" label="归还时间" width="160">
          <template #default="{ row }">
            {{ row.returnTime || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 'BORROWED'" 
              type="primary" 
              link 
              @click="handleReturn(row)"
            >还书</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMyBorrowRecords, returnBook } from '../../api/borrow'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const statusFilter = ref('')

const borrowStats = reactive({
  borrowing: 0,
  returned: 0,
  overdue: 0
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const tableData = ref([])

const getStatusType = (status) => {
  const map = { BORROWED: 'primary', RETURNED: 'success', OVERDUE: 'danger' }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = { BORROWED: '借阅中', RETURNED: '已归还', OVERDUE: '已逾期' }
  return map[status] || status
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getMyBorrowRecords({
      status: statusFilter.value,
      page: pagination.page,
      size: pagination.size
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
    borrowStats.borrowing = res.data.borrowing || 0
    borrowStats.returned = res.data.returned || 0
    borrowStats.overdue = res.data.overdue || 0
  } catch (error) {
    tableData.value = [
      { id: 1, bookName: 'Java编程思想', author: 'Bruce Eckel', borrowTime: '2024-01-10 10:00:00', dueTime: '2024-02-10 10:00:00', returnTime: null, status: 'BORROWED' },
      { id: 2, bookName: 'JavaScript高级程序设计', author: 'Nicholas C. Zakas', borrowTime: '2024-01-05 14:30:00', dueTime: '2024-02-05 14:30:00', returnTime: '2024-01-25 09:00:00', status: 'RETURNED' }
    ]
    pagination.total = 2
    borrowStats.borrowing = 1
    borrowStats.returned = 1
    borrowStats.overdue = 0
  } finally {
    loading.value = false
  }
}

const handleReturn = (row) => {
  ElMessageBox.confirm(`确定要归还《${row.bookName}》吗？`, '确认还书', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(async () => {
    try {
      await returnBook(row.id)
      ElMessage.success('还书成功！')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.borrow-page {
  padding: 10px;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px 0;
}

.stat-card.overdue .stat-value {
  color: #f56c6c;
}

.stat-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409eff;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.table-card {
  margin-bottom: 15px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}
</style>
