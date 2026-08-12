import request from './request'

// 获取读者列表（分页）
export const getReaders = (params) => {
  return request.get('/readers', { params })
}

// 获取读者详情
export const getReaderById = (id) => {
  return request.get(`/readers/${id}`)
}

// 新增读者
export const addReader = (data) => {
  return request.post('/readers', data)
}

// 修改读者
export const updateReader = (id, data) => {
  return request.put(`/readers/${id}`, data)
}

// 删除读者
export const deleteReader = (id) => {
  return request.delete(`/readers/${id}`)
}
