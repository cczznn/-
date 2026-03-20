import request from './request'

export function chatAi(question) {
  return request({
    url: '/api/ai/chat',
    method: 'post',
    data: { question }
  })
}

