<template>
  <div class="vocabulary-container">
    <el-card class="vocabulary-card">
      <template #header>
        <div class="card-header">
          <el-button @click= "() => this.dialogVisible = true"> {{ currentBook }} 自定义词汇本</el-button>

          <el-dropdown placement="bottom-end">
            <el-button> {{ selectedBook }} <i class="el-icon-arrow-down"></i></el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-for="item in bookList" :key="item.id" :label="item.bookName" :value="item.bookName" @click="changeBookID(item.id,item.bookName)">{{ item.bookName }}</el-dropdown-item>      
              </el-dropdown-menu>
            </template>
          </el-dropdown>


        </div>
      </template>
      
      <!-- 学习完成界面 -->
      <div v-if="isLearningCompleted" class="completion-container">
        <div class="completion-stats">
          <div class="completion-stat-item">
            <div class="completion-stat-value">{{ learnedToday }}</div>
            <div class="completion-stat-name">学习单词数</div>
          </div>
          <div class="completion-stat-item">
            <div class="completion-stat-value">{{ reviewToday }}</div>
            <div class="completion-stat-name">复习单词数</div>
          </div>
          <div class="completion-stat-item">
            <div class="completion-stat-value">{{ learnedToday + reviewToday }}</div>
            <div class="completion-stat-name">总计单词数</div>
          </div>
        </div>
        <div class="completion-message">
          恭喜你完成了今天的学习目标！
          <br>继续保持，你会变得越来越棒！
        </div>
        <el-button class="btn completion-btn" @click="restartLearning">继续学习</el-button>
      </div>
      
      <!-- 正常学习界面 -->
      <div v-else>
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
                @keyup.enter="checkAnswer(currentWord.state || 0,currentWord.wordId)"
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
          </div>
        </div>
        
        <div class="action-buttons">
          <el-button class="btn" @click="previousWord" :disabled="currentIndex === 0">上一个</el-button>
          <el-button class="btn" type="primary" @click="nextStep" v-if="!userInputMode">默写</el-button>
          <el-button class="btn" type="primary" @click="checkAnswer(currentWord.state || 0,currentWord.wordId)" v-else>提交答案</el-button>
          <el-button class="btn" @click="pass(currentWord.wordId,currentWord.state || 0)" >已会</el-button>
        </div>
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
            <i class="fas fa-bell"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ reviewToday }}</div>
            <div class="stat-name">今日复习数</div>
          </div>
        </div>
        <div class="stat-box">
          <div class="stat-icon">
            <i class="fas fa-book"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ learnedToday }}</div>
            <div class="stat-name">今日背诵数</div>
          </div>
        </div>

      </div>
      </el-card>
      <el-dialog
        v-model="dialogVisible"
        class="custom-transition-dialog"
        :title="添加词汇"
        width="30%"
        :transition="transitionConfig"
        align-center = true
      >
        
        <el-form :model="form" label-width="auto" style="max-width: 600px">
          <el-form-item label="新建词汇本">
            <el-switch v-model="form.newBook" />
          </el-form-item>
          <el-form-item v-if="form.newBook" label="词汇本名称">
            <el-input v-model="form.bookName" placeholder="请输入词汇本名称" />
          </el-form-item>
          <el-form-item v-else label="词汇本">
            <el-select v-model="form.bookName" placeholder="请选择词汇本">
              <el-option label="Zone one" value="shanghai" />
              <el-option label="Zone two" value="beijing" />
            </el-select>
          </el-form-item>
          <el-form-item prop="desc" 
          label="添加词汇"
          >
          <el-input 
              placeholder="规则说明：文本支持markdown中的列表无序列表格式，并且中英文之间要有空格分隔。"
              v-model="form.content" 
              type="textarea" 
              :autosize="{ minRows: 8}"/>

          </el-form-item>
        </el-form>
        <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitWord">
          提交
        </el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script>
import {addword,setKnow ,getContinueLearn,getNumTodayAlready, changeBook , getWordList ,getBookList ,getCurrentBook ,updateWordStatus} from '../api/word'
export default {
  name: 'VocabularyView',
  data() {
    return {
      selectedBook: 'CET4',
      bookList: [
        { id : 1, bookName: 'CET4', vocabularyNum : 4000 },
        { id : 2, bookName: 'CET6', vocabularyNum : 6000 },
        { id : 3, bookName: 'GRE', vocabularyNum : 4400 },
        { id : 4, bookName: 'TOEFL', vocabularyNum : 5600 }],
      vocabularyList: [
        { wordId : 0,state: 1, word: 'abandon', meaning: 'v. 放弃；抛弃，遗弃' },
        { wordId : 1,state: 1, word: 'ability', meaning: 'n. 能力，才能' },
        { wordId : 2,state: 1, word: 'abroad', meaning: 'adv. 在国外，到国外' },
        { wordId : 3,state: 1, word: 'absolute', meaning: 'adj. 绝对的，完全的' },
        { wordId : 4,state: 1, word: 'absorb', meaning: 'v. 吸收；吸引…的注意' },
        { wordId : 5,state: 1, word: 'abstract', meaning: 'adj. 抽象的 n. 摘要' },
        { wordId : 6,state: 1, word: 'abundant', meaning: 'adj. 丰富的，充裕的' },
        { wordId : 7,state: 1, word: 'abuse', meaning: 'v. 滥用；虐待 n. 滥用' },
        { wordId : 8,state: 1, word: 'academic', meaning: 'adj. 学术的；教学的' },
        { wordId : 9,state: 1, word: 'academy', meaning: 'n. 学院，研究院' },
        { wordId : 10,state: 1, word: 'accelerate', meaning: 'v. 加速，促进' },
        { wordId : 11,state: 1, word: 'accent', meaning: 'n. 口音，腔调；重音' },
        { wordId : 12,state: 1, word: 'accept', meaning: 'v. 接受；同意' },
        { wordId : 13,state: 1, word: 'access', meaning: 'n. 通道，入口；接近' },
        { wordId : 14,state: 1, word: 'accident', meaning: 'n. 事故，意外事件' },
        { wordId : 15,state: 1, word: 'accompany', meaning: 'v. 陪伴，陪同；伴随' },
        { wordId : 16,state: 1, word: 'accomplish', meaning: 'v. 完成，实现' },
        { wordId : 17,state: 1, word: 'according', meaning: 'prep. 根据，按照' },
        { wordId : 18,state: 1, word: 'account', meaning: 'n. 账户；描述；解释' },
        { wordId : 19,state: 1, word: 'accurate', meaning: 'adj. 准确的，精确的' }
      ],
      currentIndex: 0,
      userInputMode: false,
      userInput: '',
      showResult: false,
      isCorrect: false,
      learnedToday: 0, //今日已学习单词数
      reviewToday:0, //今日复习单词数
      isLearningCompleted: false, // 学习是否完成的状态标志
      totalMax: 0, // 最大下标

      dialogVisible: false, 
      form:{
        content:'',
        bookName:'',
        newBook :false,
      },

      createNewBook:false,
    }
  },
  computed: {
    currentWord() {
      return this.vocabularyList[this.currentIndex]
    },
    progress() {
      return ((this.currentIndex ) / this.vocabularyList.length) * 100
    },
  },
  methods: {
    async submitWord(){
      this.dialogVisible = false;
      if(this.form.content.trim() === '' || this.form.bookName.trim() === '') return;

      addword(this.form).then(async ()=> {
        const book = await getBookList();
        if(book != null) this.bookList = book;
      })

      this.form.content = ''
      this.form.bookName = ''
      this.form.newBook = false
    },
    async pass(id,state) {
      if(this.totalMax == this.currentIndex){
        await setKnow({wordId:id,state:state});
        const learned = await getNumTodayAlready();
        if(learned != null) {
          this.learnedToday = learned.learnNum;
          this.reviewToday = learned.reviewNum;
        }
        this.totalMax++;
      }
      this.currentIndex++;
    },
    async restartLearning() {
      this.vocabularyList = await getContinueLearn()
      this.isLearningCompleted = false
      this.currentIndex = 0
      this.userInputMode = false
      this.showResult = false
    },
    async init(){
      try{
        const bookName = await getCurrentBook();
        if(bookName != null) this.selectedBook = bookName;
        const book = await getBookList();
        if(book != null) this.bookList = book;
        const word = await getWordList();
        if(word != null) this.vocabularyList = word;
        if(word.length === 0)  this.isLearningCompleted = true;
        const learned = await getNumTodayAlready();
        if(learned != null) {
          this.learnedToday = learned.learnNum;
          this.reviewToday = learned.reviewNum;
        }
        
      }catch(error){
        console.log(error)
      }
    },
    async changeBookID(id,bookName) {
      try{
        this.selectedBook = bookName;
        await changeBook(id);
        const word = await getWordList();
        if(word != null) this.vocabularyList = word;
      }catch(error){
        console.log(error)
      }

    },

    progressFormat(percentage) {
      return `${percentage.toFixed(0)}%`
    },
    playPronunciation() {
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
    async checkAnswer(state,id) {
      this.showResult = true
      
      if (this.userInput.toLowerCase().trim() === this.currentWord.word.toLowerCase()) {
        this.isCorrect = true
        if(this.totalMax == this.currentIndex){
          await updateWordStatus(state,id,this.currentWord.word);
          this.totalMax++;
        }
        const learned = await getNumTodayAlready();
        if(learned != null) {
          this.learnedToday = learned.learnNum;
          this.reviewToday = learned.reviewNum;
        }
        setTimeout(() => {
          this.nextWord()
        }, 800)
      } else {
        this.isCorrect = false
        setTimeout(() => {
          this.userInputMode = false
          this.showResult = false
        }, 800)
      }
    },
    nextWord() {
      if (this.currentIndex < this.vocabularyList.length - 1) {
        this.currentIndex++
        this.userInputMode = false
        this.showResult = false
      } else {
        this.isLearningCompleted = true
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
  created(){
    this.init()
  },

}
</script>

<style scoped>
.btn{
  background-color: #6A8FBF;
  border-color: #E8F0F7;
  color:#f5f7fa
}

/* 学习完成界面样式 */
.completion-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  min-height: 400px;
  text-align: center;
}

.completion-icon {
  font-size: 80px;
  color: #67c23a;
  margin-bottom: 30px;
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-30px); }
  60% { transform: translateY(-15px); }
}


.completion-stats {
  display: flex;
  justify-content: space-around;
  width: 100%;
  max-width: 500px;
  margin-bottom: 30px;
}

.completion-stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.completion-stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #4A6FA5;
  margin-bottom: 10px;
}

.completion-stat-name {
  font-size: 16px;
  color: #666;
}

.completion-message {
  font-size: 18px;
  color: #666;
  margin-bottom: 40px;
  line-height: 1.6;
}

.completion-btn {
  font-size: 18px;
  padding: 12px 36px;
  background-color: #4A6FA5;
  border-color: #4A6FA5;
}

.completion-btn:hover {
  background-color: #6A8FBF;
  border-color: #6A8FBF;
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