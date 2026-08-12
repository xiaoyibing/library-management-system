import request from './request'

export function getDashboardStats() {
  return request({
    url: '/stats/dashboard',
    method: 'get'
  })
}

export function getBorrowTrend() {
  return request({
    url: '/stats/borrow-trend',
    method: 'get'
  })
}

export function getTopBooks() {
  return request({
    url: '/stats/top-books',
    method: 'get'
  })
}