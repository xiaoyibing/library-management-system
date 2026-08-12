import request from './request'

// 图书相关API
export const getBooks = (params) => {
  return request({
    url: '/books',
    method: 'get',
    params
  })
}

export const getBookById = (id) => {
  return request({
    url: `/books/${id}`,
    method: 'get'
  })
}

export const addBook = (data) => {
  return request({
    url: '/books',
    method: 'post',
    data
  })
}

export const updateBook = (id, data) => {
  return request({
    url: `/books/${id}`,
    method: 'put',
    data
  })
}

export const deleteBook = (id) => {
  return request({
    url: `/books/${id}`,
    method: 'delete'
  })
}

export const exportBooks = () => {
  return request({
    url: '/books/export',
    method: 'get',
    responseType: 'blob'
  })
}

// 书籍类型相关API
export const getCategories = (params) => {
  return request({
    url: '/categories',
    method: 'get',
    params
  })
}

export const getCategoryById = (id) => {
  return request({
    url: `/categories/${id}`,
    method: 'get'
  })
}

export const addCategory = (data) => {
  return request({
    url: '/categories',
    method: 'post',
    data
  })
}

export const updateCategory = (id, data) => {
  return request({
    url: `/categories/${id}`,
    method: 'put',
    data
  })
}

export const deleteCategory = (id) => {
  return request({
    url: `/categories/${id}`,
    method: 'delete'
  })
}

// 书架相关API
export const getShelves = (params) => {
  return request({
    url: '/shelves',
    method: 'get',
    params
  })
}

export const getShelfById = (id) => {
  return request({
    url: `/shelves/${id}`,
    method: 'get'
  })
}

export const addShelf = (data) => {
  return request({
    url: '/shelves',
    method: 'post',
    data
  })
}

export const updateShelf = (id, data) => {
  return request({
    url: `/shelves/${id}`,
    method: 'put',
    data
  })
}

export const deleteShelf = (id) => {
  return request({
    url: `/shelves/${id}`,
    method: 'delete'
  })
}