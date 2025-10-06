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

export function updateWordStatus(state,wordId,word) {
  return request({
    url: '/api/user/updateWordStatus',
    method: 'POST',
    data: {
      state,
      wordId,
      word
    }
  }).catch(error => {
    throw error;
  })
}


export function getNumTodayAlready() {
  return request({
    url: 'api/user/getToday',
    method: 'GET',
  }).catch(error => {
    throw error;
  })
}


export function getContinueLearn() {
  return request({
    url: '/api/word/continueLearn',
    method: 'GET',
  }).catch(error => {
    throw error;
  })
}

export function setKnow(params) {
  return request({
    url: '/api/word/setKnow',
    method: 'GET',
    params: params 
  }).catch(error => {
    throw error;
  })
}


// -------------
// 该接口将数据发给后端，由后端来解析，并且保存
export function addword(data) {
  return request({
    url: '/api/word/addWord',
    method: 'POST',
    data
  }).catch(error => {
    throw error;
  })
}



