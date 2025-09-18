import { en } from 'element-plus/es/locales.mjs';
import request from '../utils/request'

export function getArticleList() {
  return request({
    url: '/api/article/getArticleList',
    method: 'GET',
  }).catch(error => {
    throw error;
  })
}

export async function queryWord(word) {
  const targetUrl = `https://api.dictionaryapi.dev/api/v2/entries/en/${encodeURIComponent(word)}`;
  try{
    const res = await fetch(targetUrl);
    const data = await res.json();
    return data;
  }catch(error){
    throw error;
  }
}

export function addUnknownWord(word,textId) {
  return request({
    url: '/api/word/addUnknownWord',
    method: 'GET',
    params:{
      word: word,
      textId: textId
    }
  }).catch(error => {
    throw error;
  })
}

export function deleteUnknownWord(word,textId) {
  return request({
    url: '/api/word/deleteUnknownWord',
    method: 'GET',
    params:{
      word: word,
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

export function getFinishReading() {
  return request({
    url: '/api/article/getFinishReading',
    method: 'GET',
  }).catch(error => {
    throw error;
  })
}


