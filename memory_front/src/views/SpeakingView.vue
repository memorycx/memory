<template>
  <div class="speaking-container">
    <el-card class="speaking-card">
      <template #header>
        <div class="card-header">
          <span>口语训练</span>
        </div>
      </template>
      
      <div class="speaking-content">
        <div class="instruction-section">
          <h2>欢迎进行口语训练</h2>
          <p>点击下方麦克风图标开始录音</p>
          <p>系统将分析您的发音并提供反馈</p>
        </div>
        
        <div class="microphone-section">
          <div 
            class="microphone-button"
            :class="{ 'recording': isRecording }"
            @click="toggleRecording"
          >
            <i class="fas fa-microphone"></i>
            <div v-if="isRecording" class="recording-indicator">
              <div class="pulse-ring"></div>
            </div>
          </div>
          <div class="recording-text" v-if="isRecording">正在录音...</div>
          <div class="recording-time" v-if="isRecording">{{ recordingTime }}</div>
        </div>
        
        <div v-if="feedbackMessage" class="feedback-section">
          <el-card :class="feedbackType === 'success' ? 'success-feedback' : 'error-feedback'">
            <template #header>
              <div class="feedback-header">
                <span>{{ feedbackType === 'success' ? '发音很棒！' : '继续努力！' }}</span>
              </div>
            </template>
            <div class="feedback-content">
              <p>{{ feedbackMessage }}</p>
              <div v-if="feedbackType === 'error'" class="suggestions">
                <h4>改进建议：</h4>
                <ul>
                  <li v-for="(suggestion, index) in suggestions" :key="index">{{ suggestion }}</li>
                </ul>
              </div>
            </div>
          </el-card>
        </div>
        
        <div class="practice-words-section">
          <h3>练习单词</h3>
          <div class="word-list">
            <div 
              v-for="word in practiceWords" 
              :key="word.id"
              class="practice-word"
              @click="practiceWord(word.text)"
            >
              {{ word.text }}
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref, computed, onUnmounted } from 'vue'

export default {
  name: 'SpeakingView',
  setup() {
    const isRecording = ref(false)
    const recordingStartTime = ref(null)
    const recordingInterval = ref(null)
    const recordingSeconds = ref(0)
    const feedbackMessage = ref('')
    const feedbackType = ref('success')
    const suggestions = ref([])
    const practiceWords = ref([
      { id: 1, text: 'pronunciation' },
      { id: 2, text: 'conversation' },
      { id: 3, text: 'communication' },
      { id: 4, text: 'vocabulary' },
      { id: 5, text: 'grammar' },
      { id: 6, text: 'fluency' },
      { id: 7, text: 'accent' },
      { id: 8, text: 'expression' }
    ])
    
    const recordingTime = computed(() => {
      const minutes = Math.floor(recordingSeconds.value / 60)
      const seconds = recordingSeconds.value % 60
      return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
    })
    
    const startRecording = () => {
      isRecording.value = true
      recordingStartTime.value = new Date()
      recordingSeconds.value = 0
      
      // 开始计时
      recordingInterval.value = setInterval(() => {
        recordingSeconds.value++
      }, 1000)
      
      // 在实际项目中，这里会接入录音API
      console.log('开始录音')
      
      // 模拟录音功能，实际项目中需要使用MediaRecorder API
      try {
        // 这里只是模拟，实际项目中需要浏览器的录音权限
      } catch (error) {
        console.error('录音失败:', error)
        stopRecording()
        alert('无法开始录音，请检查您的浏览器权限')
      }
    }
    
    const stopRecording = () => {
      isRecording.value = false
      
      // 清除计时器
      if (recordingInterval.value) {
        clearInterval(recordingInterval.value)
        recordingInterval.value = null
      }
      
      // 在实际项目中，这里会停止录音并处理录音文件
      console.log('停止录音，录音时长:', recordingTime.value)
      
      // 模拟生成反馈
      generateFeedback()
    }
    
    const toggleRecording = () => {
      if (isRecording.value) {
        stopRecording()
      } else {
        startRecording()
      }
    }
    
    const generateFeedback = () => {
      // 模拟生成随机反馈
      const isSuccess = Math.random() > 0.3
      
      if (isSuccess) {
        feedbackType.value = 'success'
        const successMessages = [
          '您的发音非常清晰，继续保持！',
          '语调把握得很好，听起来很自然！',
          '语速适中，表达流畅，很棒！',
          '单词发音准确，语调优美！'
        ]
        feedbackMessage.value = successMessages[Math.floor(Math.random() * successMessages.length)]
        suggestions.value = []
      } else {
        feedbackType.value = 'error'
        const errorMessages = [
          '有几个单词的发音需要改进',
          '语调可以更加自然一些',
          '语速稍微快了一点，建议适当放慢',
          '部分音节的重音位置需要注意'
        ]
        feedbackMessage.value = errorMessages[Math.floor(Math.random() * errorMessages.length)]
        
        // 生成改进建议
        const allSuggestions = [
          '练习时可以对着镜子观察口型',
          '尝试放慢语速，确保每个音节都发清楚',
          '多听标准发音并模仿',
          '注意单词的重音位置',
          '练习时录音并对比标准发音',
          '可以尝试使用绕口令练习舌头灵活性'
        ]
        
        // 随机选择2-3个建议
        const suggestionCount = Math.floor(Math.random() * 2) + 2
        suggestions.value = []
        const usedIndices = new Set()
        
        while (suggestions.value.length < suggestionCount) {
          const randomIndex = Math.floor(Math.random() * allSuggestions.length)
          if (!usedIndices.has(randomIndex)) {
            usedIndices.add(randomIndex)
            suggestions.value.push(allSuggestions[randomIndex])
          }
        }
      }
      
      // 5秒后自动播放反馈
      setTimeout(() => {
        playFeedback()
      }, 1000)
    }
    
    const playFeedback = () => {
      // 实际项目中可以接入语音合成API播放反馈
      console.log('播放反馈:', feedbackMessage.value)
    }
    
    const practiceWord = (word) => {
      // 练习特定单词
      console.log('练习单词:', word)
      alert(`请练习单词: ${word}`)
    }
    
    onUnmounted(() => {
      // 组件卸载时清理计时器
      if (recordingInterval.value) {
        clearInterval(recordingInterval.value)
      }
    })
    
    return {
      isRecording,
      recordingTime,
      feedbackMessage,
      feedbackType,
      suggestions,
      practiceWords,
      toggleRecording,
      practiceWord
    }
  }
}
</script>

<style scoped>
.speaking-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  height: 100vh;
  background-color: #f5f7fa;
}

.speaking-card {
  width: 100%;
  max-width: 800px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.speaking-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px;
}

.instruction-section {
  text-align: center;
  margin-bottom: 40px;
}

.instruction-section h2 {
  margin: 0 0 15px 0;
  color: var(--primary-color);
  font-size: 28px;
}

.instruction-section p {
  margin: 10px 0;
  color: #666;
  font-size: 16px;
}

.microphone-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40px;
}

.microphone-button {
  position: relative;
  width: 150px;
  height: 150px;
  background-color: var(--primary-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(74, 111, 165, 0.4);
}

.microphone-button:hover {
  background-color: var(--primary-hover);
  transform: scale(1.05);
  box-shadow: 0 6px 16px rgba(74, 111, 165, 0.5);
}

.microphone-button.recording {
  background-color: #f56c6c;
  box-shadow: 0 4px 12px rgba(245, 108, 108, 0.4);
}

.microphone-button.recording:hover {
  background-color: #f78989;
  box-shadow: 0 6px 16px rgba(245, 108, 108, 0.5);
}

.microphone-button i {
  font-size: 60px;
  color: white;
}

.recording-indicator {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.pulse-ring {
  position: absolute;
  top: -10px;
  left: -10px;
  width: 170px;
  height: 170px;
  border-radius: 50%;
  background-color: rgba(245, 108, 108, 0.3);
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  100% {
    transform: scale(1.5);
    opacity: 0;
  }
}

.recording-text {
  margin-top: 20px;
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
}

.recording-time {
  margin-top: 10px;
  font-size: 24px;
  color: #666;
  font-family: monospace;
}

.feedback-section {
  width: 100%;
  margin-bottom: 40px;
}

.success-feedback {
  border-color: #67c23a;
}

.error-feedback {
  border-color: #f56c6c;
}

.feedback-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.success-feedback .feedback-header span {
  color: #67c23a;
  font-weight: bold;
}

.error-feedback .feedback-header span {
  color: #f56c6c;
  font-weight: bold;
}

.feedback-content p {
  margin: 0;
  line-height: 1.6;
  color: #666;
}

.suggestions {
  margin-top: 15px;
}

.suggestions h4 {
  margin: 0 0 10px 0;
  color: var(--primary-color);
}

.suggestions ul {
  margin: 0;
  padding-left: 20px;
}

.suggestions li {
  margin-bottom: 5px;
  color: #666;
  line-height: 1.4;
}

.practice-words-section {
  width: 100%;
}

.practice-words-section h3 {
  margin: 0 0 20px 0;
  text-align: center;
  color: var(--primary-color);
}

.word-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.practice-word {
  padding: 10px 20px;
  background-color: var(--primary-bg);
  border: 1px solid var(--primary-color);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s;
  color: var(--primary-color);
  font-size: 14px;
}

.practice-word:hover {
  background-color: var(--primary-color);
  color: white;
  transform: scale(1.05);
}
</style>