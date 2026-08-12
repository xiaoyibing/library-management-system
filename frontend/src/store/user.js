import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))
  const role = ref(localStorage.getItem('role') || '')

  // 设置登录信息
  const setLoginInfo = (data) => {
    token.value = data.token
    userInfo.value = data.user
    role.value = data.user.role
    
    localStorage.setItem('token', data.token)
    localStorage.setItem('userInfo', JSON.stringify(data.user))
    localStorage.setItem('role', data.user.role)
  }

  // 退出登录
  const logout = () => {
    token.value = ''
    userInfo.value = {}
    role.value = ''
    
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('role')
  }

  // 更新用户信息
  const updateUserInfo = (data) => {
    userInfo.value = { ...userInfo.value, ...data }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  return {
    token,
    userInfo,
    role,
    setLoginInfo,
    logout,
    updateUserInfo
  }
})
