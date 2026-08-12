<template>
  <div class="dashboard">
    <!-- 顶部统计卡片 -->
    <el-row :gutter="20" class="dashboard-cards">
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">图书总数</div>
              <div class="card-value">{{ stats.bookCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">借阅中</div>
              <div class="card-value">{{ stats.borrowCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon><Edit /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">今日借阅</div>
              <div class="card-value">{{ stats.todayBorrow || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="card-content">
            <div class="card-icon">
              <el-icon><Check /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-title">今日归还</div>
              <div class="card-value">{{ stats.todayReturn || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="dashboard-charts">
      <!-- 借阅趋势 -->
      <el-col :span="14">
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">借阅趋势</div>
          </template>
          <div ref="trendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 热门图书排行榜 -->
      <el-col :span="10">
        <el-card class="chart-card">
          <template #header>
            <div class="chart-header">热门图书排行榜</div>
          </template>

          <el-table
            :data="topBooks"
            size="small"
            stripe
            style="width: 100%"
          >
            <el-table-column
              label="排名"
              type="index"
              width="60"
            />
            <el-table-column
              prop="bookName"
              label="图书名称"
              show-overflow-tooltip
            />
            <el-table-column
              prop="borrowCount"
              label="借阅次数"
              width="100"
              align="center"
            />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { Document, User, Edit, Check } from '@element-plus/icons-vue'
import {
  getDashboardStats,
  getBorrowTrend,
  getTopBooks
} from '@/api/stats'

// =====================
// 数据
// =====================
const stats = ref({})
const topBooks = ref([])

const trendChartRef = ref(null)
const trendChart = ref(null)

// =====================
// 加载统计数据
// =====================
const loadStats = async () => {
  const { data } = await getDashboardStats()
  stats.value = data || {}
}

// =====================
// 加载借阅趋势
// =====================
const loadBorrowTrend = async () => {
  const { data } = await getBorrowTrend()
  renderTrendChart(data.trend || [])
}

// =====================
// 加载热门图书
// =====================
const loadTopBooks = async () => {
  const { data } = await getTopBooks()
  topBooks.value = data.books || []
}

// =====================
// 渲染柱状图
// =====================
const renderTrendChart = (list) => {
  if (!trendChartRef.value) return

  nextTick(() => {
    if (!trendChart.value) {
      trendChart.value = echarts.init(trendChartRef.value)
    }

    trendChart.value.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: list.map(item => item.date),
        axisTick: { alignWithLabel: true }
      },
      yAxis: {
        type: 'value',
        name: '借阅次数'
      },
      series: [
        {
          name: '借阅次数',
          type: 'bar',       // 👈 核心：柱状图
          barWidth: '60%',
          data: list.map(item => item.count),
          itemStyle: {
            color: '#409eff'
          }
        }
      ]
    })
  })
}

// =====================
// 生命周期
// =====================
onMounted(async () => {
  await loadStats()
  await loadBorrowTrend()
  await loadTopBooks()

  window.addEventListener('resize', () => {
    trendChart.value && trendChart.value.resize()
  })
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.dashboard-cards {
  margin-bottom: 20px;
}

.dashboard-card {
  height: 100px;
}

.card-content {
  display: flex;
  align-items: center;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.card-icon .el-icon {
  font-size: 24px;
  color: #409eff;
}

.card-title {
  font-size: 14px;
  color: #909399;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.chart-card {
  height: 400px;
}

.chart-header {
  font-weight: bold;
}

.chart-container {
  width: 100%;
  height: 340px;
}
</style>
