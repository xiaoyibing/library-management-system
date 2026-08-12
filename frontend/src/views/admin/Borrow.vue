<template>
  <div class="borrow-page">
    <!-- 快捷操作 -->
    <el-row :gutter="20" class="action-row">
      <el-col :span="12">
        <el-card shadow="hover" class="action-card">
          <template #header>
            <span>办理借书</span>
          </template>
          <el-form :inline="true" :model="borrowForm">
            <el-form-item label="读者卡号">
              <el-input v-model="borrowForm.cardNo" placeholder="请输入读者卡号" />
            </el-form-item>
            <el-form-item label="图书ISBN">
              <el-input v-model="borrowForm.isbn" placeholder="请输入图书ISBN" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleBorrow">办理借书</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="action-card">
          <template #header>
            <span>办理还书</span>
          </template>
          <el-form :inline="true" :model="returnForm">
            <el-form-item label="借阅编号">
              <el-input v-model="returnForm.borrowId" placeholder="请输入借阅编号" />
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="handleReturn">办理还书</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 借阅记录 -->
    <el-card shadow="never" class="table-card">
      <template #header>
        <div class="card-header">
          <span>借阅记录</span>
          <el-radio-group v-model="statusFilter" @change="loadData">
            <el-radio-button value="">全部</el-radio-button>
            <el-radio-button value="BORROWED">借阅中</el-radio-button>
            <el-radio-button value="RETURNED">已归还</el-radio-button>
            <el-radio-button value="OVERDUE">已逾期</el-radio-button>
          </el-radio-group>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="借阅编号" width="100" />
        <el-table-column prop="readerName" label="读者姓名" width="100" />
        <el-table-column prop="bookName" label="图书名称" min-width="150" />
        <el-table-column prop="borrowTime" label="借阅时间" width="160" />
        <el-table-column prop="dueTime" label="应还时间" width="160" />
        <el-table-column prop="returnTime" label="归还时间" width="160" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 'BORROWED'" 
              type="success" 
              link 
              @click="doReturn(row)"
            >还书</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getBorrowRecords, borrowBook, returnBook } from '../../api/borrow'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const statusFilter = ref('')

const borrowForm = reactive({
  cardNo: '',
  isbn: ''
})

const returnForm = reactive({
  borrowId: ''
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
    const res = await getBorrowRecords({
      status: statusFilter.value,
      page: pagination.page,
      size: pagination.size
    })
    tableData.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    tableData.value = [
      { id: 1, readerName: '张三', bookName: 'Java编程思想', borrowTime: '2024-01-10 10:00:00', dueTime: '2024-02-10 10:00:00', returnTime: null, status: 'BORROWED' },
      { id: 2, readerName: '李四', bookName: 'JavaScript高级程序设计', borrowTime: '2024-01-05 14:30:00', dueTime: '2024-02-05 14:30:00', returnTime: '2024-01-25 09:00:00', status: 'RETURNED' }
    ]
    pagination.total = 2
  } finally {
    loading.value = false
  }
}

const handleBorrow = async () => {
  if (!borrowForm.cardNo || !borrowForm.isbn) {
    ElMessage.warning('请输入读者卡号和图书ISBN')
    return
  }
  try {
    await borrowBook(borrowForm)
    ElMessage.success('借书成功')
    borrowForm.cardNo = ''
    borrowForm.isbn = ''
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleReturn = async () => {
  if (!returnForm.borrowId) {
    ElMessage.warning('请输入借阅编号')
    return
  }
  try {
    await returnBook(returnForm.borrowId)
    ElMessage.success('还书成功')
    returnForm.borrowId = ''
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const doReturn = async (row) => {
  try {
    await returnBook(row.id)
    ElMessage.success('还书成功')
    loadData()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.borrow-page {
  padding: 10px;
}

.action-row {
  margin-bottom: 15px;
}

.action-card {
  height: 150px;
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
