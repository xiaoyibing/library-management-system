<template>
  <div class="books-page">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="书名">
          <el-input v-model="searchForm.name" placeholder="请输入书名" clearable />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="searchForm.author" placeholder="请输入作者" clearable />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图书列表 -->
    <el-row :gutter="20" class="book-list">
      <el-col :span="6" v-for="book in bookList" :key="book.id" class="book-col">
        <el-card shadow="hover" class="book-card">
          <div class="book-cover">
            <el-icon class="book-icon"><Reading /></el-icon>
          </div>
          <div class="book-info">
            <h4 class="book-name">{{ book.name }}</h4>
            <p class="book-author">{{ book.author }}</p>
            <p class="book-category">
              <el-tag size="small">{{ book.category }}</el-tag>
            </p>
            <p class="book-stock">
              可借: <span :class="book.available > 0 ? 'available' : 'unavailable'">{{ book.available }}</span> / {{ book.stock }}
            </p>
            <el-button 
              type="primary" 
              size="small" 
              :disabled="book.available <= 0"
              @click="handleBorrow(book)"
              style="width: 100%"
            >
              {{ book.available > 0 ? '借阅' : '暂无库存' }}
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :page-sizes="[8, 16, 24, 32]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next"
        @size-change="loadData"
        @current-change="loadData"
      />
    </div>

    <!-- 借阅确认对话框 -->
    <el-dialog v-model="borrowDialogVisible" title="确认借阅" width="400px">
      <div class="borrow-confirm">
        <p><strong>图书名称：</strong>{{ selectedBook?.name }}</p>
        <p><strong>作者：</strong>{{ selectedBook?.author }}</p>
        <p><strong>借阅期限：</strong>30天</p>
        <p><strong>应还日期：</strong>{{ dueDate }}</p>
      </div>
      <template #footer>
        <el-button @click="borrowDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBorrow" :loading="borrowLoading">确认借阅</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getBooks } from '../../api/book'
import { borrowBook } from '../../api/borrow'
import { ElMessage } from 'element-plus'

const borrowLoading = ref(false)
const borrowDialogVisible = ref(false)
const selectedBook = ref(null)

const searchForm = reactive({
  name: '',
  author: '',
  categoryId: ''
})

const pagination = reactive({
  page: 1,
  size: 8,
  total: 0
})

const bookList = ref([])
const categories = ref([])

const dueDate = computed(() => {
  const date = new Date()
  date.setDate(date.getDate() + 30)
  return date.toLocaleDateString('zh-CN')
})

const loadData = async () => {
  try {
    const params = {
      page: pagination.page,
      size: pagination.size,
      name: searchForm.name,
      author: searchForm.author,
      categoryId: searchForm.categoryId
    }
    const res = await getBooks(params)
    bookList.value = res.data.records
    pagination.total = res.data.total
  } catch (error) {
    console.error('加载图书数据失败:', error)
    // 模拟数据
    bookList.value = [
      { id: 1, name: 'Java编程思想', author: 'Bruce Eckel', category: '计算机', stock: 10, available: 8 },
      { id: 2, name: 'JavaScript高级程序设计', author: 'Nicholas C. Zakas', category: '计算机', stock: 15, available: 12 },
      { id: 3, name: '深入理解计算机系统', author: 'Randal E. Bryant', category: '计算机', stock: 8, available: 5 },
      { id: 4, name: 'Spring Boot实战', author: 'Craig Walls', category: '计算机', stock: 6, available: 0 },
      { id: 5, name: '红楼梦', author: '曹雪芹', category: '文学', stock: 20, available: 18 },
      { id: 6, name: '三国演义', author: '罗贯中', category: '文学', stock: 15, available: 10 },
      { id: 7, name: '经济学原理', author: '曼昆', category: '经济', stock: 12, available: 9 },
      { id: 8, name: '中国通史', author: '吕思勉', category: '历史', stock: 8, available: 6 }
    ]
    pagination.total = 8
  }
}

// 加载分类数据
const loadCategories = async () => {
  try {
    const res = await getCategories({ page: 1, size: 100 })
    categories.value = res.data.records
  } catch (error) {
    console.error('加载分类数据失败:', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.author = ''
  searchForm.category = ''
  handleSearch()
}

const handleBorrow = (book) => {
  selectedBook.value = book
  borrowDialogVisible.value = true
}

const confirmBorrow = async () => {
  borrowLoading.value = true
  try {
    await borrowBook({ bookId: selectedBook.value.id })
    ElMessage.success('借阅成功！')
    borrowDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  } finally {
    borrowLoading.value = false
  }
}

onMounted(() => {
  loadData()
  loadCategories()
})
</script>

<style scoped>
.books-page {
  padding: 10px;
}

.search-card {
  margin-bottom: 20px;
}

.book-list {
  min-height: 400px;
}

.book-col {
  margin-bottom: 20px;
}

.book-card {
  height: 320px;
}

.book-cover {
  height: 100px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  margin-bottom: 12px;
}

.book-icon {
  font-size: 48px;
  color: #fff;
}

.book-info {
  text-align: center;
}

.book-name {
  font-size: 14px;
  font-weight: bold;
  margin: 0 0 8px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.book-author {
  font-size: 12px;
  color: #909399;
  margin: 0 0 8px 0;
}

.book-category {
  margin: 0 0 8px 0;
}

.book-stock {
  font-size: 12px;
  margin: 0 0 12px 0;
}

.available {
  color: #67c23a;
  font-weight: bold;
}

.unavailable {
  color: #f56c6c;
  font-weight: bold;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.borrow-confirm p {
  margin: 10px 0;
}
</style>
