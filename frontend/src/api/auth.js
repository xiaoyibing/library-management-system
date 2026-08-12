import request from './request'

// 登录
export const login = (data) => {
  return request.post('/auth/login', data)
}

// 获取当前用户信息
export const getUserInfo = () => {
  return request.get('/auth/info')
}

// 修改密码
export const changePassword = (data) => {
  return request.put('/auth/password', data)
}
