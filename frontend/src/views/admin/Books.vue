<template>
  <div class="book-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>图书管理</span>
          <el-button type="primary" @click="handleAdd">添加图书</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="图书名称">
          <el-input v-model="searchForm.name" placeholder="请输入图书名称" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="searchForm.author" placeholder="请输入作者" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.categoryId" placeholder="请选择类型" clearable>
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
        </el-form-item>
      </el-form>
      
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="isbn" label="ISBN" width="150" />
        <el-table-column prop="name" label="书名" />
        <el-table-column prop="author" label="作者" />
        <el-table-column prop="categoryName" label="类型" />
        <el-table-column prop="publisher" label="出版社" />
        <el-table-column prop="price" label="价格" width="100" />
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="available" label="可借" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        style="margin-top: 20px; text-align: right"
      />
    </el-card>
    
    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="ISBN" prop="isbn">
              <el-input v-model="form.isbn" placeholder="请输入ISBN" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="书名" prop="name">
              <el-input v-model="form.name" placeholder="请输入书名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="作者" prop="author">
              <el-input v-model="form.author" placeholder="请输入作者" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择类型" style="width: 100%">
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出版社" prop="publisher">
              <el-input v-model="form.publisher" placeholder="请输入出版社" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出版日期" prop="publishDate">
              <el-date-picker
                v-model="form.publishDate"
                type="date"
                placeholder="选择日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input-number v-model="form.price" :precision="2" :step="0.01" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存" prop="stock">
              <el-input-number v-model="form.stock" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getBooks, addBook, updateBook, deleteBook, getCategories, exportBooks } from '@/api/book'
import { ElMessage, ElMessageBox } from 'element-plus'

// 表格数据
const tableData = ref([])
const loading = ref(false)
const searchForm = ref({
  name: '',
  author: '',
  categoryId: null
})

// 分页信息
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()
const form = ref({
  id: null,
  isbn: '',
  name: '',
  author: '',
  categoryId: null,
  publisher: '',
  publishDate: '',
  price: 0,
  stock: 0,
  description: ''
})

// 分类列表
const categories = ref([])

// 表单验证规则
const formRules = {
  isbn: [
    { required: true, message: '请输入ISBN', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入书名', trigger: 'blur' }
  ],
  author: [
    { required: true, message: '请输入作者', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择类型', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存', trigger: 'blur' }
  ]
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.value.current,
      size: pagination.value.size,
      name: searchForm.value.name,
      author: searchForm.value.author,
      categoryId: searchForm.value.categoryId
    }
    const { data } = await getBooks(params)
    // 添加分类名称到数据中
    tableData.value = data.records.map(book => ({
      ...book,
      categoryName: getCategoryName(book.categoryId)
    }))
    pagination.value.total = data.total
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载分类数据
const loadCategories = async () => {
  try {
    const { data } = await getCategories({ page: 1, size: 100 })
    categories.value = data.records
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

// 获取分类名称
const getCategoryName = (categoryId) => {
  const category = categories.value.find(cat => cat.id === categoryId)
  return category ? category.name : '未知分类'
}

// 搜索
const handleSearch = () => {
  pagination.value.current = 1
  loadData()
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.value.size = size
  loadData()
}

// 当前页改变
const handleCurrentChange = (current) => {
  pagination.value.current = current
  loadData()
}

// 添加
const handleAdd = () => {
  dialogTitle.value = '添加图书'
  form.value = {
    id: null,
    isbn: '',
    name: '',
    author: '',
    categoryId: null,
    publisher: '',
    publishDate: '',
    price: 0,
    stock: 0,
    description: ''
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑图书'
  form.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这本图书吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteBook(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 确认提交
const handleConfirm = async () => {
  try {
    await formRef.value.validate()
    
    if (form.value.id) {
      await updateBook(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await addBook(form.value)
      ElMessage.success('添加成功')
    }
    
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('操作失败:', error)
  }
}

onMounted(async () => {
  await loadCategories()
  loadData()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>