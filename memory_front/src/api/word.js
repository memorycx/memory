import request from '../utils/request'


export function getCurrentBook() {
  return request({
    url: '/api/word/getCurrentBook',
    method: 'GET',
  }).catch(error => {
    throw error;
  })
}


export function getBookList() {
  return request({
    url: '/api/word/getBookList',
    method: 'GET',
  }).catch(error => {
    throw error;
  })
}


export function changeBook(data) {
  return request({
    url: '/api/word/changeBook',
    method: 'POST',
    data
  }).catch(error => {
    throw error;
  })
}

export function getWordList() {
  return request({
    url: '/api/word/getWordList',
    method: 'GET',
  }).catch(error => {
    throw error;
  })
}

export function updateWordStatus(state,wordId) {
  console.log(state,wordId)
  return request({
    url: '/api/user/updateWordStatus',
    method: 'POST',
    data: {
      state,
      wordId
    }

  }).catch(error => {
    throw error;
  })
}


