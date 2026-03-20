<script setup>
import { ref, nextTick } from 'vue'
import { chatAi } from '../api/ai'
import { ElMessage } from 'element-plus'

const input = ref('')
const loading = ref(false)
const messages = ref([
  {
    role: 'assistant',
    content: '你好！我是学习问答助手，可以帮你讲解概念、规划学习路径、推荐练习方式。'
  }
])

const listRef = ref(null)

const scrollToBottom = async () => {
  await nextTick()
  if (listRef.value) {
    listRef.value.scrollTop = listRef.value.scrollHeight
  }
}

const send = async () => {
  const question = input.value.trim()
  if (!question || loading.value) return

  messages.value.push({ role: 'user', content: question })
  input.value = ''
  loading.value = true
  await scrollToBottom()

  try {
    const res = await chatAi(question)
    messages.value.push({ role: 'assistant', content: res.data })
  } catch (error) {
    ElMessage.error('AI 服务请求失败，请稍后再试')
  } finally {
    loading.value = false
    await scrollToBottom()
  }
}
</script>

<template>
  <div class="assistant-page">
    <el-card class="assistant-card">
      <template #header>
        <div class="card-header">
          <div>
            <h2>学习问答助手</h2>
            <p>输入你的学习问题，AI 会给出清晰的解释与学习建议。</p>
          </div>
          <el-tag type="success" effect="light">在线</el-tag>
        </div>
      </template>

      <div ref="listRef" class="message-list">
        <div
          v-for="(item, index) in messages"
          :key="index"
          :class="['message-item', item.role]"
        >
          <div class="avatar">
            <el-icon><component :is="item.role === 'assistant' ? 'ChatLineRound' : 'User'" /></el-icon>
          </div>
          <div class="bubble">
            <pre>{{ item.content }}</pre>
          </div>
        </div>
      </div>

      <div class="input-area">
        <el-input
          v-model="input"
          type="textarea"
          :rows="3"
          placeholder="例如：如何高效复习数据结构？"
          @keyup.enter.exact.prevent="send"
        />
        <div class="actions">
          <el-button type="primary" :loading="loading" @click="send">
            发送
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.assistant-page {
  padding: 10px;
}

.assistant-card {
  min-height: 70vh;
  display: flex;
  flex-direction: column;

  :deep(.el-card__body) {
    display: flex;
    flex-direction: column;
    height: calc(70vh - 60px);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  h2 {
    margin: 0 0 4px;
  }

  p {
    margin: 0;
    color: #909399;
    font-size: 13px;
  }
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 10px 4px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;

  .avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: #f0f2f5;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #409eff;
  }

  .bubble {
    max-width: 75%;
    padding: 12px 14px;
    border-radius: 12px;
    background: #f5f7fa;
    color: #303133;
    white-space: pre-wrap;
    line-height: 1.5;
  }

  &.user {
    flex-direction: row-reverse;

    .avatar {
      background: #409eff;
      color: #fff;
    }

    .bubble {
      background: #409eff;
      color: #fff;
    }
  }
}

.input-area {
  margin-top: 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.actions {
  display: flex;
  justify-content: flex-end;
}

pre {
  margin: 0;
  font-family: inherit;
}
</style>
