<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside
      :width="isCollapse ? '64px' : '200px'"
      class="aside"
    >
      <div class="logo">
        <span v-if="!isCollapse">图书管理系统</span>
      </div>

      <el-menu
        :default-active="$route.path"
        class="sidebar-menu"
        :router="true"
        :collapse="isCollapse"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据统计</span>
        </el-menu-item>
        <el-menu-item index="/admin/books">
          <el-icon><Document /></el-icon>
          <span>图书管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <el-icon><FolderOpened /></el-icon>
          <span>类型管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/shelves">
          <el-icon><Grid /></el-icon>
          <span>书架管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/borrow">
          <el-icon><Edit /></el-icon>
          <span>借阅管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/readers">
          <el-icon><User /></el-icon>
          <span>读者管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><UserFilled /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="header">
        <!-- 左侧 -->
        <div class="header-left">
          <el-icon class="collapse-btn" @click="toggleCollapse">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>
          <span class="page-title">{{ currentTitle }}</span>
        </div>

        <!-- 右侧 -->
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" icon="UserFilled" />
              <span class="username">{{ userInfo.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主体 -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  DataAnalysis,
  Document,
  Edit,
  User,
  UserFilled,
  FolderOpened,
  Grid,
  Expand,
  Fold,
  SwitchButton
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const userInfo = ref({})
const isCollapse = ref(false)

// 当前页面标题
const currentTitle = computed(() => {
  return route.meta.title || '数据统计'
})

// 折叠侧边栏
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    logout()
  }
}

// 退出登录
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  localStorage.removeItem('role')
  router.push('/login')
}

// 初始化用户信息
onMounted(() => {
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    userInfo.value = JSON.parse(storedUserInfo)
  }
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

/* 侧边栏 */
.aside {
  background-color: #545c64;
  transition: width 0.2s;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #444;
}

.sidebar-menu {
  border-right: none;
}

/* Header */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  margin-right: 15px;
}

.page-title {
  font-size: 18px;
  font-weight: bold;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
}

/* 主体 */
.main {
  background-color: #f5f7fa;
  padding: 20px;
}
</style>
