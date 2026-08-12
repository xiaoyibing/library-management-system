import request from './request'

// 获取借阅记录列表（分页）
export const getBorrowRecords = (params) => {
  return request.get('/borrow', { params })
}

// 获取我的借阅记录
export const getMyBorrowRecords = (params) => {
  return request.get('/borrow/my', { params })
}

// 借书
export const borrowBook = (data) => {
  return request.post('/borrow', data)
}

// 还书
export const returnBook = (id) => {
  return request.put(`/borrow/${id}/return`)
}

// 获取借阅详情
export const getBorrowDetail = (id) => {
  return request.get(`/borrow/${id}`)
}
