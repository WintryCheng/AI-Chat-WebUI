<template>
  <div class="chat-container">
    <el-scrollbar style="height: 500px">
      <div class="chat-messages">
        <!-- 使用 v-for 循环渲染聊天记录 -->
        <div v-for="(message, index) in messages" :key="index" :class="{ user: message.isSelf }">
          <el-card shadow="hover">
            <div>{{ message.content }}</div>
          </el-card>
        </div>
      </div>
      <!-- 用户输入框 -->
      <el-input v-model="inputMessage" placeholder="输入消息" class="input-with-select"> </el-input>
      <!-- 发送按钮 -->
      <el-button @click="sendMessage" class="send-message-btn">发送</el-button>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 数据
const messages = ref([{ content: '你好，请问有什么可以帮助您的？' }])
const inputMessage = ref('') // 用户输入数据
const isFirstResponse = ref(true) // 是否为流式输出第一个字符

// 方法
const sendMessage = async () => {
  if (inputMessage.value.trim().length > 0) {
    messages.value.push({ content: inputMessage.value, isSelf: true })
  }
  // 创建 SSE 请求
  // const eventSource = new EventSource('http://localhost:8081/question/webflux' + `?userMessage=${inputMessage.value}`)
  // const eventSource = new EventSource('http://localhost:8081/question/microservice' + `?userMessage=${inputMessage.value}`)
  const eventSource = new EventSource('http://localhost:8081/question/microserviceWebflux' + `?userMessage=${inputMessage.value}`)

  // 连接成功
  eventSource.onopen = function (event) {
    console.log('连接成功: {}', event)
  }

  // 流式接收后端消息
  eventSource.onmessage = function (event) {
    if (isFirstResponse.value) {
      messages.value.push({ content: event.data })
      isFirstResponse.value = false
    } else {
      messages.value.slice(-1)[0].content += event.data
    }
  }

  // 报错或连接关闭时触发
  eventSource.onerror = function (event) {
    if (event.eventPhase === EventSource.CLOSED) {
      console.log('关闭连接')
      eventSource.close()
    } else {
      eventSource.close()
    }
  }

  // 重置数据
  inputMessage.value = ''
  isFirstResponse.value = true
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.chat-messages {
  flex-grow: 1;
  padding: 10px;
}

.user {
  margin-left: auto;
  text-align: right;
}

.send-message-btn {
  margin-top: 10px;
}
</style>
