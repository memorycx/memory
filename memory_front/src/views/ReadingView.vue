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
          :class="{ 'active': selectedArticleId === article.id }"
          @click="selectArticle(article.id)"
        >
          <h3>{{ article.title }}</h3>
          <p class="article-summary">{{ article.summary }}</p>
          <div class="article-meta">
            <span>{{ article.source }}</span>
            <span>{{ article.wordCount }} 词</span>
          </div>
        </div>
      </div>
    </el-card>
    
    <div class="main-content">
      <el-card class="article-content-card">
        <template #header>
          <div class="card-header">
            <span>{{ selectedArticle?.title || '请选择一篇文章开始阅读' }}</span>
              <el-select class="select-text" v-model="value" placeholder="Select" style="width: 240px">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </div>
        </template>
        <div v-if="selectedArticle" class="article-content">
          <div class="article-header">
            <h2>{{ selectedArticle.title }}</h2>
            <div class="article-info">
              <span>{{ selectedArticle.source }}</span>
              <span>{{ new Date(selectedArticle.date).toLocaleDateString() }}</span>
              <span>{{ selectedArticle.wordCount }} 词</span>
            </div>
          </div>
          <div class="article-body">
            <p v-for="paragraph in selectedArticle.content" :key="paragraph.index">
              {{ paragraph.text }}
            </p>
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
          <div v-for="(note, index) in vocabularyNotes" :key="index" class="vocabulary-item">
            <div class="vocabulary-word">{{ note.word }}</div>
            <div class="vocabulary-meaning">{{ note.meaning }}</div>
            <el-button type="text" @click="removeVocabularyNote(index)" size="small">
              <i class="fas fa-times"></i>
            </el-button>
          </div>
        </div>
        <div v-else class="no-vocabulary">
          <p>暂无记录的陌生词汇</p>
          <p class="hint">提示：遇到陌生词汇时，请点击添加按钮记录</p>
        </div>
        
        <div class="add-vocabulary-section">
          <el-input
            v-model="newVocabulary.word"
            placeholder="输入陌生单词"
            class="vocabulary-input"
          ></el-input>
          <el-input
            v-model="newVocabulary.meaning"
            placeholder="输入单词释义"
            class="vocabulary-input"
          ></el-input>
          <el-button type="primary" @click="addVocabularyNote">添加词汇</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue'

export default {
  name: 'ReadingView',
  setup() {
    const articles = ref([
      {
        id: 1,
        title: 'How to Improve Your English Reading Skills',
        summary: 'Learn effective strategies to enhance your English reading comprehension and speed.',
        source: 'English Learning Magazine',
        date: '2024-08-15',
        wordCount: 850,
        content: [
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
        wordCount: 620,
        content: [
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
        wordCount: 780,
        content: [
          { index: 1, text: 'In the past, education was often viewed as a phase that ended when one entered the workforce. However, in today\'s fast-paced world, lifelong learning has become a necessity. Technologies evolve, industries transform, and new knowledge emerges at an unprecedented rate.' },
          { index: 2, text: 'Lifelong learning refers to the ongoing, voluntary pursuit of knowledge for personal or professional reasons. It can take many forms, from formal education to self-study, online courses, workshops, and more. The key is to maintain a curious mindset and a willingness to adapt.' },
          { index: 3, text: 'One of the main benefits of lifelong learning is professional development. In competitive job markets, individuals who continuously update their skills are more likely to secure promotions and stay relevant in their fields. Employers value employees who show initiative and a commitment to growth.' },
          { index: 4, text: 'Lifelong learning also has personal benefits. It keeps the mind active, enhances self-esteem, and provides a sense of accomplishment. It can open up new hobbies, interests, and social connections. Ultimately, it contributes to a more fulfilling and meaningful life.' }
        ]
      }
    ])
    
    const selectedArticleId = ref(null)
    const vocabularyNotes = ref([])
    const newVocabulary = ref({ word: '', meaning: '' })
    
    const selectedArticle = computed(() => {
      return articles.value.find(article => article.id === selectedArticleId.value)
    })
    
    const selectArticle = (id) => {
      selectedArticleId.value = id
    }
    
    const addVocabularyNote = () => {
      if (newVocabulary.value.word && newVocabulary.value.meaning) {
        vocabularyNotes.value.push({
          word: newVocabulary.value.word,
          meaning: newVocabulary.value.meaning
        })
        // 清空输入框
        newVocabulary.value.word = ''
        newVocabulary.value.meaning = ''
      } else {
        alert('请输入单词和释义')
      }
    }
    
    const removeVocabularyNote = (index) => {
      vocabularyNotes.value.splice(index, 1)
    }
    
    const clearVocabularyNotes = () => {
      if (vocabularyNotes.value.length > 0) {
        if (confirm('确定要清空所有陌生词汇记录吗？')) {
          vocabularyNotes.value = []
        }
      }
    }
    
    return {
      articles,
      selectedArticleId,
      selectedArticle,
      vocabularyNotes,
      newVocabulary,
      selectArticle,
      addVocabularyNote,
      removeVocabularyNote,
      clearVocabularyNotes
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
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-content-card {
  flex: 1;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow-y: auto;
}

.article-content {
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

.add-vocabulary-section {
  display: flex;
  gap: 10px;
  padding: 10px 0;
}

.vocabulary-input {
  flex: 1;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.select-text{
    display: none;
}

@media (max-width: 1000px) {
  .article-list-card{
    display: none;
  }
  .select-text{
     display: block;
  }
}
</style>