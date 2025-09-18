<template>
  <div class="reading-container">
    <el-card class="article-list-card">
      <template #header>
        <div class="card-header">
          <span>文章列表</span>
        </div>
      </template>
      <div class="article-list">
        <div 
          v-for="article in articles" 
          :key="article.id"
          class="article-item" 
          :class="{ 
            'active': selectedArticleId === article.id, 
            'read': readArticles.includes(article.id) 
          }"
          @click="selectArticle(article.id)"
        >
          <h3>{{ article.title }}</h3>
          <p class="article-summary">{{ article.summary }}</p>
          <div class="article-meta">
            <span>{{ article.source }}</span>
            <span>{{ article.nums }} 词</span>
          </div>
        </div>
      </div>
    </el-card>
    
    <div class="main-endContent">
      <el-card class="article-endContent-card">
        <template #header>
          <div class="card-header">
            <span>{{ selectedArticle?.title || '请选择一篇文章开始阅读' }}</span>
              <el-select class="select-text" v-model="value" placeholder="Select" style="width: 240px">
              <el-option
                v-for="item in articles"
                :key="item.id"
                :label="item.title"
                :value="item.title"
              />
            </el-select>
          </div>
        </template>
        <div v-if="selectedArticle" class="article-endContent">
          <div class="article-header">
            <h2>{{ selectedArticle.title }}</h2>
            <div class="article-info">
              <span>{{ selectedArticle.source }}</span>
              <span>{{ new Date(selectedArticle.date).toLocaleDateString() }}</span>
              <span>{{ selectedArticle.nums }} 词</span>
            </div>
          </div>
          <div class="article-body">
            <p v-for="paragraph in selectedArticle.endContent" :key="paragraph.index">
              <template v-for="(word, index) in splitTextIntoWords(paragraph.text)">
                <span 
                  v-if="word.trim()"
                  :key="index"
                  @click="handleWordClick(word)"
                  class="clickable-word"
                >
                  {{ word }}
                </span>
                <template v-else>{{ "&hairsp;" }}</template>
              </template>
            </p>
            <el-button type="primary" @click="finishRead()">完成阅读</el-button>
          </div>
        </div>
        <div v-else class="no-article">
          <p>请从左侧列表中选择一篇文章开始阅读</p>
        </div>
      </el-card>
      
      <el-card class="vocabulary-notes-card">
        <template #header>
          <div class="card-header">
            <span>陌生词汇</span>
            <el-button type="text" @click="clearVocabularyNotes" size="small">
              <i class="fas fa-trash-alt"></i> 清空
            </el-button>
          </div>
        </template>
        <div v-if="vocabularyNotes.length > 0" class="vocabulary-list">
          <div v-for="note in vocabularyNotes" :key="note.wordId" class="vocabulary-item">
            <div class="vocabulary-word">{{ note.word }}</div>
            <!-- <div class="vocabulary-meaning">{{ note.meaning }}</div> -->
            <el-button type="text" @click="removeVocabularyNote(note.word)" size="small">
              <i class="fas fa-times"></i>
            </el-button>
          </div>
        </div>
        <div v-else class="no-vocabulary">
          <p>暂无记录的陌生词汇</p>
          <p class="hint">记录陌生词汇</p>
        </div>
      </el-card>
    </div>

  
  </div>
  
  <!-- 单词详情弹窗 -->
  <el-dialog
    v-model="wordDialogVisible"
    title="单词详情"
    width="40%"
    :before-close="handleDialogClose"
  >
    <div class="word-dialog-content">
      <h3>{{ clickedWord }}</h3>

      <!-- 单词详细信息 -->
      <div v-if="word_mean" class="word-details">
        <!-- 展示音标信息 -->
        <div class="word-phonetic">
          <span v-if="word_mean.phonetic">{{ word_mean.phonetic }}</span>
          <button class="pronunciation-btn" @click="playPronunciation">
            <i class="fas fa-volume-up"></i>
          </button>
        </div>

        
        <!-- 展示单词释义 -->
        <div class="word-meanings">
          <div v-for="(meaning, index) in word_mean.meanings" :key="index" class="meaning-item">
            <!-- 词性 -->
            <div class="meaning-type">
              {{ meaning.partOfSpeech }}
            </div>
            
            <!-- 释义内容 -->
            <div class="meaning-content">
              <div v-for="(definition, defIndex) in meaning.definitions" :key="defIndex" class="definition-item">
                <div class="definition-text">
                  {{ definition.definition }}
                </div>

                <!-- 例句 -->
                <div v-if="definition.example" class="example-item">
                  <span class="example-label">例句:</span>
                  <span class="example-text">{{ definition.example }}</span>
                </div>
                
                <!-- 同义词 -->
                <div v-if="definition.synonyms && definition.synonyms.length > 0" class="synonyms-item">
                  <span class="synonyms-label">同义词:</span>
                  <span class="synonyms-text">{{ definition.synonyms.join(', ') }}</span>
                </div>
                
                <!-- 反义词 -->
                <div v-if="definition.antonyms && definition.antonyms.length > 0" class="antonyms-item">
                  <span class="antonyms-label">反义词:</span>
                  <span class="antonyms-text">{{ definition.antonyms.join(', ') }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 加载中状态 -->
      <div v-else class="loading-state">
        <el-icon><Loading /></el-icon>
        <span>加载中...</span>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleDialogClose">关闭</el-button>
      <el-button type="primary" @click="addWord">添加到词汇本</el-button>
    </div>
  </el-dialog>
</template>


<script>
import { ca } from 'element-plus/es/locales.mjs'
import { Loading } from '@element-plus/icons-vue'
import { getFinishReading,finishReading,getArticleList , queryWord,addUnknownWord ,getUnknownWords, deleteUnknownWord} from '../api/text'
export default {
  name: 'ReadingView',
  data() {
    return {
      meanings: ca.messages, // 语言包
      articles: [
        {
          id: 1,
          title: 'How to Improve Your English Reading Skills',
          summary: 'Learn effective strategies to enhance your English reading comprehension and speed.',
          source: 'English Learning Magazine',
          date: '2024-08-15',
          nums: 850,
          endContent: [
            { index: 1, text: 'Improving your English reading skills is essential for academic and professional success. Whether you are a student preparing for exams or a professional looking to advance your career, strong reading skills can open up new opportunities.' },
            { index: 2, text: 'One effective strategy is to start with materials that match your current level. If you are a beginner, try children\'s books or simple news articles. As you progress, gradually move to more complex texts. This approach helps build confidence and vocabulary.' },
            { index: 3, text: 'Another important technique is active reading. Instead of passively scanning the text, engage with it by asking questions, making predictions, and summarizing key points. This not only improves comprehension but also retention of information.' },
            { index: 4, text: 'Vocabulary building is also crucial. Keep a notebook or use a digital tool to record new words you encounter. Review these words regularly to reinforce your memory. Over time, you will notice a significant improvement in your reading fluency.' },
            { index: 5, text: 'Finally, practice regularly. Set aside dedicated time each day for reading. Consistency is key to developing any skill, and reading is no exception. With patience and persistence, you will see remarkable progress in your English reading abilities.' }
          ]
        },
        {
          id: 2,
          title: 'The Benefits of Reading Daily',
          summary: 'Discover how daily reading can enhance your cognitive abilities, vocabulary, and overall knowledge.',
          source: 'Health & Wellness Journal',
          date: '2024-08-10',
          nums: 620,
          endContent: [
            { index: 1, text: 'In today\'s digital age, many people spend more time scrolling through social media than reading books. However, maintaining a daily reading habit offers numerous benefits that cannot be replicated by other activities.' },
            { index: 2, text: 'Research has shown that regular reading can improve cognitive function and memory. It stimulates the brain, keeping it active and engaged. This mental exercise can even help prevent age-related cognitive decline, such as Alzheimer\'s disease.' },
            { index: 3, text: 'Reading also expands your vocabulary and improves language skills. Exposure to new words in context helps with retention and application. This is particularly beneficial for language learners who want to achieve fluency.' },
            { index: 4, text: 'Moreover, reading can reduce stress and promote relaxation. Getting lost in a good book can transport you to different worlds, providing a much-needed escape from daily pressures. It can also improve sleep quality when done before bed, unlike screen time which can disrupt sleep patterns.' }
          ]
        },
        {
          id: 3,
          title: 'The Importance of Lifelong Learning',
          summary: 'Explore why continuous education is vital in today\'s rapidly changing world.',
          source: 'Education Today',
          date: '2024-08-05',
          nums: 780,
          endContent: [
            { index: 1, text: 'In the past, education was often viewed as a phase that ended when one entered the workforce. However, in today\'s fast-paced world, lifelong learning has become a necessity. Technologies evolve, industries transform, and new knowledge emerges at an unprecedented rate.' },
            { index: 2, text: 'Lifelong learning refers to the ongoing, voluntary pursuit of knowledge for personal or professional reasons. It can take many forms, from formal education to self-study, online courses, workshops, and more. The key is to maintain a curious mindset and a willingness to adapt.' },
            { index: 3, text: 'One of the main benefits of lifelong learning is professional development. In competitive job markets, individuals who continuously update their skills are more likely to secure promotions and stay relevant in their fields. Employers value employees who show initiative and a commitment to growth.' },
            { index: 4, text: 'Lifelong learning also has personal benefits. It keeps the mind active, enhances self-esteem, and provides a sense of accomplishment. It can open up new hobbies, interests, and social connections. Ultimately, it contributes to a more fulfilling and meaningful life.' }
          ]
        }
      ],
      selectedArticleId: null,
      vocabularyNotes: [], //这是给用户来展示该文章的陌生词汇
      wordDialogVisible: false,
      clickedWord: '',
      value: '',
      word_mean: null,
      readArticles: [], // 存储已阅读文章的ID
    }
  },
  created() {
    this.init()
  },
  computed: {
    selectedArticle() {
      return this.articles.find(article => article.id === this.selectedArticleId)
    }
  },
  methods: {
    playPronunciation() {
      const audio = new Audio()
      audio.src = `https://api.dictionaryapi.dev/media/pronunciations/en/${this.clickedWord}-us.mp3`
      audio.play().catch(error => {
        console.log('无法播放发音:', error)
      })
    },
    async finishRead() {
      try {
        await finishReading(this.selectedArticleId)
        // 将已阅读文章ID添加到数组中
        if (!this.readArticles.includes(this.selectedArticleId)) {
          this.readArticles.push(this.selectedArticleId)
        }
      } catch (error) { 
        console.error('Failed to finish reading:', error)
      }
    },
    async init(){
      try {
        const articles = await getArticleList()
        this.readArticles = await getFinishReading()
        this.articles = articles.map(article => ({
          ...article,
          endContent: this.transformContent(article.endContent || [])
        }))
      } catch (error) {
        console.error('Failed to get article list:', error)
      }
    },
    transformContent(contentObj) {
      // 将后端传来的数据转换成前端需要的格式
      if (!contentObj || typeof contentObj !== 'object') {
        return []
      }
      return Object.keys(contentObj).map((key, idx) => ({
        index: idx + 1,
        text: contentObj[key]
      }))
    },
    async selectArticle(id) {
      this.selectedArticleId = id;
      this.vocabularyNotes = await getUnknownWords(id)
    },


    // 处理文本，使得每个单词可以点击的函数
    splitTextIntoWords(text) {
      if (!text) return []
      // 这个正则表达式会将文本分割成单词、空格和标点符号
      const words = text.match(/\S+|\s/g) || []
      return words
    },
    
    // 处理单词点击的事件
    async handleWordClick(word) {
      if (word && word.trim()) {
        this.clickedWord = word.trim()
        const lastChar = this.clickedWord.slice(-1);
        // 如果最后一个字符是.或,则去除
        if (lastChar === '.' || lastChar === ',') {
          this.clickedWord = this.clickedWord.slice(0, -1);
        }
        try{
          this.word_mean = await queryWord(this.clickedWord.trim())
          this.word_mean = this.word_mean[0]
          this.wordDialogVisible = true
        }catch(error){
          console.error('Failed to query word:', error)
        }
      }
    },

    // 处理弹窗关闭
    handleDialogClose() {
      this.wordDialogVisible = false
    },
    async addWord(){
      try {
        // 确保我们有单词和释义数据
        if (this.clickedWord && this.word_mean) {
          
          // 添加到词汇本
          await addUnknownWord(this.clickedWord,this.selectedArticleId);
          
          // 更新本地词汇列表
          this.vocabularyNotes.push({word: this.clickedWord});
          
          // 显示成功提示
          this.$message.success('已添加到词汇本');
          
          // 关闭弹窗
          this.wordDialogVisible = false;
        }
      } catch (error) {
        console.error('添加单词失败:', error);
        this.$message.error('添加失败，请重试');
      }
    },
    

    async removeVocabularyNote(word) {
      this.vocabularyNotes = this.vocabularyNotes.filter(item => item.word !== word);
      await deleteUnknownWord(word,this.selectedArticleId)
    },
    clearVocabularyNotes() {
      if (this.vocabularyNotes.length > 0) {
        if (confirm('确定要清空所有陌生词汇记录吗？')) {
          this.vocabularyNotes.forEach(async (item) => {
             await deleteUnknownWord(item.word,this.selectedArticleId)
          });
        }
      }
      this.vocabularyNotes = []
    }
  }
}
</script>


<style scoped>
.reading-container {
  display: flex;
  gap: 20px;
  padding: 20px;
  height: 100vh;
  background-color: #f5f7fa;
}

.article-list-card {
  width: 350px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
}

.article-list {
  flex: 1;
  max-height: calc(100vh - 200px); /* 设置最大高度，减去其他元素的高度 */
  overflow-y: auto;
  padding-right: 10px;
}

.article-item {
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #e4e7ed;
}

.article-item:hover {
  background-color: #f5f7fa;
  border-color: var(--primary-color);
}

.article-item.active {
  background-color: var(--primary-light);
  border-color: var(--primary-color);
}

/* 已阅读文章样式弱化效果 */
.article-item.read {
  opacity: 0.7;
  border-color: #d9d9d9;
}

.article-item.read h3 {
  color: #999;
}

.article-item h3 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #333;
}

.article-summary {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.main-endContent {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-endContent-card {
  flex: 1;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
}

.article-endContent {
  padding: 20px;
}

.article-header {
  margin-bottom: 30px;
  text-align: center;
}

.article-header h2 {
  margin: 0 0 15px 0;
  font-size: 24px;
  color: #333;
}

.article-info {
  display: flex;
  justify-content: center;
  gap: 20px;
  font-size: 14px;
  color: #666;
}

.article-body {
  line-height: 1.8;
  font-size: 16px;
  color: #333;
}

.article-body p {
  margin-bottom: 20px;
  text-align: justify;
}

.article-body p span {
  cursor: pointer;
  transition: background-color 0.2s;
  padding: 2px 4px;
  border-radius: 3px;
}

.article-body p span:hover {
  background-color: #f0f0f0;
}

.clickable-word {
  cursor: pointer;
  transition: background-color 0.2s;
  border-radius: 3px;
}

.clickable-word:hover {
  background-color: #f0f0f0;
}

.dia-button{
   text-align: center;
   margin-bottom: 10px;
}
.meaning-hint {
  text-align: center;
  font-size: 15px;
  color: #666;
  margin-bottom: 10px;
  padding: 10px;
  /* background-color: #f8f9fa; */
  border-radius: 8px;
}

.no-article {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 300px;
  color: #999;
  font-size: 16px;
}

.vocabulary-notes-card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.vocabulary-list {
  max-height: 200px;
  overflow-y: auto;
  margin-bottom: 20px;
}

.vocabulary-item {
  display: flex;
  align-items: center;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.vocabulary-word {
  font-weight: bold;
  color: var(--primary-color);
  margin-right: 10px;
  min-width: 100px;
}

.vocabulary-meaning {
  flex: 1;
  color: #666;
}

.no-vocabulary {
  padding: 40px 20px;
  text-align: center;
  color: #999;
}

.no-vocabulary .hint {
  margin-top: 10px;
  font-size: 14px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.select-text{
    display: none;
}







/* 单词弹窗样式 */

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
.word-dialog-content {
  padding: 20px 0;
}

.word-dialog-content h3 {
  font-size: 28px;
  color: var(--primary-color);
  margin-bottom: 15px;
  text-align: center;
}

.word-phonetic {
  text-align: center;
  color: #666;
  font-style: italic;
  margin-bottom: 20px;
}

.word-details {
  text-align: left;
}

.word-meanings {
  margin-top: 20px;
}

.meaning-item {
  display: flex;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.meaning-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.meaning-type {
  font-weight: bold;
  color: #666;
  margin-right: 15px;
  min-width: 60px;
  flex-shrink: 0;
}

.meaning-content {
  flex: 1;
}

.definition-item {
  margin-bottom: 15px;
}

.definition-item:last-child {
  margin-bottom: 0;
}

.definition-text {
  margin-bottom: 8px;
  line-height: 1.6;
  color: #333;
}

.example-item,
.synonyms-item,
.antonyms-item {
  margin-left: 20px;
  margin-bottom: 8px;
  font-size: 14px;
  line-height: 1.6;
}

.example-label,
.synonyms-label,
.antonyms-label {
  color: #999;
  margin-right: 5px;
}

.example-text {
  color: #666;
  font-style: italic;
}

.synonyms-text,
.antonyms-text {
  color: #666;
}

.loading-state {
  text-align: center;
  padding: 40px 0;
  color: #999;
}

.loading-state .el-icon {
  display: block;
  margin: 0 auto 10px;
  font-size: 40px;
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 隐藏滚动条但保持滚动功能 */
/* WebKit浏览器(Chrome, Safari等) */
.article-list::-webkit-scrollbar,
.article-endContent-card::-webkit-scrollbar,
.vocabulary-list::-webkit-scrollbar {
  display: none;
  width: 0 !important;
  height: 0 !important;
}

/* Firefox浏览器 */
.article-list,
.article-endContent-card,
.vocabulary-list {
  scrollbar-width: none;
  scrollbar-color: transparent transparent;
  scroll-behavior: smooth;
  -ms-overflow-style: none;
}

/* 确保内容可以正常滚动 */
.article-list,
.article-endContent-card,
.vocabulary-list {
  overflow-y: auto;
}

@media (max-width: 1000px) {
  .article-list-card{
    display: none;
  }
  .select-text{
     display: block;
  }
  .article-list {
    max-height: calc(100vh - 150px);
  }
}
</style>