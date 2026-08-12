import { createRouter, createWebHistory } from 'vue-router'
import AdminLayout from '@/layout/AdminLayout.vue'
import UserLayout from '@/layout/UserLayout.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'  // 添加根路径重定向到登录页
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/admin',
    component: AdminLayout,
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '数据统计' }
      },
      {
        path: 'books',
        name: 'AdminBooks',
        component: () => import('@/views/admin/Books.vue'),
        meta: { title: '图书管理' }
      },
      {
        path: 'categories',
        name: 'AdminCategories',
        component: () => import('@/views/admin/Categories.vue'),
        meta: { title: '类型管理' }
      },
      {
        path: 'shelves',
        name: 'AdminShelves',
        component: () => import('@/views/admin/Shelves.vue'),
        meta: { title: '书架管理' }
      },
      {
        path: 'borrow',
        name: 'AdminBorrow',
        component: () => import('@/views/admin/Borrow.vue'),
        meta: { title: '借阅管理' }
      },
      {
        path: 'readers',
        name: 'AdminReaders',
        component: () => import('@/views/admin/Readers.vue'),
        meta: { title: '读者管理' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('@/views/admin/Users.vue'),
        meta: { title: '用户管理' }
      }
    ]
  },
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/books',
    children: [
      {
        path: 'books',
        name: 'UserBooks',
        component: () => import('@/views/user/Books.vue'),
        meta: { title: '图书浏览' }
      },
      {
        path: 'borrow',
        name: 'UserBorrow',
        component: () => import('@/views/user/Borrow.vue'),
        meta: { title: '我的借阅' }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { title: '个人信息' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router