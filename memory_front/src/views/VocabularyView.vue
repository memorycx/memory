<template>
  <div class="vocabulary-container">
    <el-card class="vocabulary-card">
      <template #header>
        <div class="card-header">
          <span>背单词</span>


        <el-dropdown placement="bottom-end">
          <el-button> bottomEnd </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>The Action 1st</el-dropdown-item>
              <el-dropdown-item>The Action 2nd</el-dropdown-item>
              <el-dropdown-item>The Action 3rd</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>


        </div>
      </template>
      
      <div class="word-display">
        <div v-if="!userInputMode">
          <div class="word-section">
            <h1 class="word">{{ currentWord.word }}</h1>
            <div class="word-meaning">{{ currentWord.meaning }}</div>
            <button class="pronunciation-btn" @click="playPronunciation">
              <i class="fas fa-volume-up"></i>
            </button>
          </div>
        </div>
        <div v-else>
          <div class="user-input-section">
            <h3>请输入英文单词：</h3>
            <div class="meaning-hint">{{ currentWord.meaning }}</div>
            <el-input
              v-model="userInput"
              placeholder="请输入英文单词"
              class="word-input"
              @keyup.enter="checkAnswer"
            ></el-input>
            <div v-if="showResult" class="result-feedback">
              <div v-if="isCorrect" class="correct-answer">
                <i class="fas fa-check-circle"></i> 正确！
              </div>
              <div v-else class="wrong-answer">
                <i class="fas fa-times-circle"></i> 错误！正确答案是：{{ currentWord.word }}
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="progress-section">
        <el-progress :percentage="progress" :format="progressFormat" stroke="#6A8FBF"/>
        <div class="progress-info">
          <span>{{ currentIndex + 1 }} / {{ vocabularyList.length }}</span>
          <span>正确率: {{ accuracy }}%</span>
        </div>
      </div>
      
      <div class="action-buttons">
        <el-button class="btn" @click="previousWord" :disabled="currentIndex === 0">上一个</el-button>
        <el-button class="btn" type="primary" @click="nextStep" v-if="!userInputMode">默写</el-button>
        <el-button class="btn" type="primary" @click="checkAnswer" v-else>提交答案</el-button>
      </div>
    </el-card>
    
    <el-card class="learning-stats">
      <template #header>
        <div class="card-header">
          <span>学习统计</span>
        </div>
      </template>
      <div class="stats-grid">
        <div class="stat-box">
          <div class="stat-icon">
            <i class="fas fa-book"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ learnedToday }}</div>
            <div class="stat-name">今日学习</div>
          </div>
        </div>
        <div class="stat-box">
          <div class="stat-icon">
            <i class="fas fa-check"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ correctCount }}</div>
            <div class="stat-name">正确单词</div>
          </div>
        </div>
        <div class="stat-box">
          <div class="stat-icon">
            <i class="fas fa-clock"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ learningTime }}分钟</div>
            <div class="stat-name">学习时长</div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'VocabularyView',
  data() {
    return {
      selectedBook: 'cet4',
      vocabularyList: [
        { word: 'abandon', meaning: 'v. 放弃；抛弃，遗弃' },
        { word: 'ability', meaning: 'n. 能力，才能' },
        { word: 'abroad', meaning: 'adv. 在国外，到国外' },
        { word: 'absolute', meaning: 'adj. 绝对的，完全的' },
        { word: 'absorb', meaning: 'v. 吸收；吸引…的注意' },
        { word: 'abstract', meaning: 'adj. 抽象的 n. 摘要' },
        { word: 'abundant', meaning: 'adj. 丰富的，充裕的' },
        { word: 'abuse', meaning: 'v. 滥用；虐待 n. 滥用' },
        { word: 'academic', meaning: 'adj. 学术的；教学的' },
        { word: 'academy', meaning: 'n. 学院，研究院' },
        { word: 'accelerate', meaning: 'v. 加速，促进' },
        { word: 'accent', meaning: 'n. 口音，腔调；重音' },
        { word: 'accept', meaning: 'v. 接受；同意' },
        { word: 'access', meaning: 'n. 通道，入口；接近' },
        { word: 'accident', meaning: 'n. 事故，意外事件' },
        { word: 'accompany', meaning: 'v. 陪伴，陪同；伴随' },
        { word: 'accomplish', meaning: 'v. 完成，实现' },
        { word: 'according', meaning: 'prep. 根据，按照' },
        { word: 'account', meaning: 'n. 账户；描述；解释' },
        { word: 'accurate', meaning: 'adj. 准确的，精确的' }
      ],
      currentIndex: 0,
      userInputMode: false,
      userInput: '',
      showResult: false,
      isCorrect: false,
      correctCount: 0,
      totalAttempts: 0,
      learnedToday: 0,
      startTime: new Date()
    }
  },
  computed: {
    currentWord() {
      return this.vocabularyList[this.currentIndex]
    },
    progress() {
      return ((this.currentIndex + 1) / this.vocabularyList.length) * 100
    },
    accuracy() {
      if (this.totalAttempts === 0) return 0
      return Math.round((this.correctCount / this.totalAttempts) * 100)
    },
    learningTime() {
      const now = new Date()
      const diffMinutes = Math.floor((now - this.startTime) / 60000)
      return Math.max(0, diffMinutes)
    }
  },
  methods: {
    progressFormat(percentage) {
      return `${percentage.toFixed(0)}%`
    },
    playPronunciation() {
      // 播放单词发音
      console.log('播放单词发音:', this.currentWord.word)
      // 模拟语音播放效果
      const audio = new Audio()
      audio.src = `https://api.dictionaryapi.dev/media/pronunciations/en/${this.currentWord.word}-us.mp3`
      audio.play().catch(error => {
        console.log('无法播放发音:', error)
      })
    },
    nextStep() {
      this.userInputMode = true
      this.userInput = ''
      this.showResult = false
    },
    checkAnswer() {
      this.totalAttempts++
      this.showResult = true
      
      if (this.userInput.toLowerCase().trim() === this.currentWord.word.toLowerCase()) {
        this.isCorrect = true
        this.correctCount++
        this.learnedToday++
        
        // 延迟进入下一个单词
        setTimeout(() => {
          this.nextWord()
        }, 1500)
      } else {
        this.isCorrect = false
        // 延迟重置输入模式
        setTimeout(() => {
          this.userInputMode = false
          this.showResult = false
        }, 2000)
      }
    },
    nextWord() {
      if (this.currentIndex < this.vocabularyList.length - 1) {
        this.currentIndex++
        this.userInputMode = false
        this.showResult = false
      } else {
        // 单词学习完成
        alert('恭喜你完成了今天的单词学习！')
      }
    },
    previousWord() {
      if (this.currentIndex > 0) {
        this.currentIndex--
        this.userInputMode = false
        this.showResult = false
      }
    }
  },
  mounted() {
    // 初始化学习数据
    this.startTime = new Date()
  }
}
</script>

<style scoped>
.btn{
  background-color: #6A8FBF;
  border-color: #E8F0F7;
  color:#f5f7fa
}
/* 修改按钮 hover 状态颜色 */
.el-dropdown .el-button:hover  {
  background-color: #6A8FBF;
  border-color: #6A8FBF;
  color:#f5f7fa
}

.vocabulary-container {
  display: flex;
  gap: 20px;
  padding: 20px;
  height: 100%;
  padding-top: 10%;
  background-color: #f5f7fa;
}

.vocabulary-card {
  flex: 1;
  background-color: white;
  border-radius: 10px;
  
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.book-selector {
  display: flex;
  align-items: center;
}

.word-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  padding: 20px;
  text-align: center;
}

.word-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.word {
  font-size: 48px;
  font-weight: bold;
  color: var(--primary-color);
  margin-bottom: 20px;
}

.word-meaning {
  font-size: 20px;
  color: #666;
  margin-bottom: 20px;
  max-width: 600px;
  line-height: 1.5;
}

.pronunciation-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: var(--primary-color);
  cursor: pointer;
  padding: 10px;
  transition: color 0.3s;
}

.pronunciation-btn:hover {
  color: var(--primary-hover);
}

.user-input-section {
  width: 100%;
  max-width: 600px;
}

.meaning-hint {
  font-size: 20px;
  color: #666;
  margin-bottom: 20px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.word-input {
  margin-bottom: 20px;
}

.result-feedback {
  font-size: 18px;
  padding: 10px;
  border-radius: 8px;
}

.correct-answer {
  color: #67c23a;
  background-color: #f0f9eb;
}

.wrong-answer {
  color: #f56c6c;
  background-color: #fef0f0;
}

.progress-section {
  margin: 20px 0;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

.learning-stats {
  width: 300px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stats-grid {
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 20px;
}

.stat-box {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background-color: var(--primary-color);
  color: white;
  border-radius: 50%;
  margin-right: 15px;
  font-size: 18px;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-name {
  font-size: 14px;
  color: #666;
}

@media (max-width: 1000px) {
  .learning-stats{
    display: none;
  }
}
</style>