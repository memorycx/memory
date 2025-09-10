import request from '../utils/request'

export function login(data) {
  return request({
    url: '/api/login',
    method: 'POST',
    data
  }).catch(error => {
    throw error;
  })
}

export function register(data) {
  return request({
    url: '/api/register',
    method: 'POST',
    data
  }).catch(error => {
    throw error;
  })
}

export function info() {
  return request({
    url: 'api/user/info',
    method: 'GET',
  }).catch(error => {
    console.log(error)
  })
}

export function wordNumber() {
  return request({
    url: 'api/user/wordNumber',
    method: 'GET',
  }).catch(error => {
    console.log(error)
  })
}

export function tableData() {
  return request({
    url: 'api/user/tableData',
    method: 'GET',
  }).catch(error => {
    console.log(error)
  })
}

export function day() {
  return request({
    url: 'api/user/day',
    method: 'GET',
  }).catch(error => {
    console.log(error)
  })
}

export function update(data) {
  return request({
    url: 'api/user/update',
    method: 'POST',
    data
  }).catch(error => {
    console.log(error)
  })
}
