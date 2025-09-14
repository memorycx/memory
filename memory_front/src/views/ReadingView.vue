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
            <div class="vocabulary-meaning">{{ note.meaning }}</div>
            <el-button type="text" @click="removeVocabularyNote(note.wordId)" size="small">
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

    <!-- 单词点击弹窗 -->
    <el-dialog
      title="单词释义"
      v-model="wordDialogVisible"
      width="30%"
      :before-close="handleDialogClose"
      align-center
    >
      <div class="word-dialog-content">
        <h3>{{ clickedWord }}</h3>
      </div>
      <div class="meaning-hint">
        {{ temMean }}
      </div>

      <div class="dia-button">
        <el-button type="primary" @click="addWord()">添加为陌生词汇</el-button>
      </div>  
    </el-dialog>
  </div>
</template>


<script>
import { ca } from 'element-plus/es/locales.mjs'
import { finishReading,getArticleList , queryWord,addUnknownWord ,getUnknownWords, deleteUnknownWord} from '../api/text'
export default {
  name: 'ReadingView',
  data() {
    return {
      readArticles: [1], // 存储已阅读文章的ID
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
      vocabularyNotes: [{wordId:-1,word: 'abandon', meaning: 'v. 放弃；抛弃，遗弃'}],
      // 新增的用于单词点击功能的属性
      wordDialogVisible: false,
      clickedWord: '',
      temMean:'v. 放弃；抛弃，遗弃',
      temId: -1,
      value: ''
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
        this.articles = articles.map(article => ({
          ...article,
          endContent: this.transformContent(article.endContent || [])
        }))
      } catch (error) {
        console.error('Failed to get article list:', error)
        // 保持使用本地数据
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

    splitTextIntoWords(text) {
      if (!text) return []
      // 这个正则表达式会将文本分割成单词、空格和标点符号
      const words = text.match(/\S+|\s/g) || []
      return words
    },
    
    async handleWordClick(word) {
      if (word && word.trim()) {
        this.clickedWord = word.trim()
        try{
          const result = await queryWord(word.trim())
          this.temMean = result.meaning || ''
          this.temId = result.wordId || -1
          this.wordDialogVisible = true
        }catch(error){
          console.error('Failed to query word:', error)
        }
      }
    },
    // 新增：处理弹窗关闭
    handleDialogClose() {
      this.wordDialogVisible = false
    },
    async addWord(){
      await addUnknownWord(this.temId,this.selectedArticleId);
      this.vocabularyNotes.push({wordId:this.temId,word: this.clickedWord, meaning: this.temMean});
      this.wordDialogVisible = false
    },

    async removeVocabularyNote(wordId) {
      this.vocabularyNotes = this.vocabularyNotes.filter(item => item.wordId !== wordId);
      await deleteUnknownWord(wordId,this.selectedArticleId)
    },
    clearVocabularyNotes() {
      if (this.vocabularyNotes.length > 0) {
        if (confirm('确定要清空所有陌生词汇记录吗？')) {
          this.vocabularyNotes.forEach(async (item) => {
             await deleteUnknownWord(item.wordId,this.selectedArticleId)
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

.word-dialog-content {
  text-align: center;
  padding: 20px 0;
}

.word-dialog-content h3 {
  font-size: 40px;
  color: var(--primary-color);
  margin: 0;
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