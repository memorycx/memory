import request from '../utils/request'

export function getArticleList() {
  return request({
    url: '/api/article/getArticleList',
    method: 'GET',
  }).catch(error => {
    throw error;
  })
}

export function queryWord(word) {
  return request({
    url: '/api/word/queryWord',
    method: 'GET',
    params: {
      word: word
    }
  }).catch(error => {
    throw error;
  })
}

export function addUnknownWord(wordId,textId) {
  return request({
    url: '/api/word/addUnknownWord',
    method: 'GET',
    params:{
      wordId: wordId,
      textId: textId
    }
  }).catch(error => {
    throw error;
  })
}

export function deleteUnknownWord(wordId,textId) {
  return request({
    url: '/api/word/deleteUnknownWord',
    method: 'GET',
    params:{
      wordId: wordId,
      textId: textId
    }
  }).catch(error => {
    throw error;
  })
}

export function getUnknownWords(textId) {
  return request({
    url: '/api/word/getUnknownWords',
    method: 'GET',
    params:{
      textId: textId
    }
  }).catch(error => {
    throw error;
  })
}


export function finishReading(id) {
  return request({
    url: '/api/article/finishReading',
    method: 'GET',
    params: {
      id: id
    }
  }).catch(error => {
    throw error;
  })
}



