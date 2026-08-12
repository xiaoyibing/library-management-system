import request from './request'

// 获取用户列表（分页）
export const getUsers = (params) => {
  return request.get('/users', { params })
}

// 新增用户
export const addUser = (data) => {
  return request.post('/users', data)
}

// 修改用户
export const updateUser = (id, data) => {
  return request.put(`/users/${id}`, data)
}

// 删除用户
export const deleteUser = (id) => {
  return request.delete(`/users/${id}`)
}

// 重置密码
export const resetPassword = (id) => {
  return request.put(`/users/${id}/reset-password`)
}
