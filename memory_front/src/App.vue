<script setup>
import { ref } from 'vue';
import WordLearning from './components/WordLearning.vue';
import DataAnalysis from './components/DataAnalysis.vue';
import ArticleReading from './components/ArticleReading.vue';
import SpeakingPractice from './components/SpeakingPractice.vue';

// 默认显示数据分析（个人中心）
const activeTab = ref('data-analysis');

const handleTabChange = (tab) => {
  activeTab.value = tab;
};
</script>

<template>
  <div class="app-container">
    <!-- 导航栏 -->
    <header class="nav-header">
      <div class="nav-container">
        <h1 class="app-title">学习助手</h1>
        <nav class="nav-tabs">
          <button 
            class="nav-tab" 
            :class="{ active: activeTab === 'data-analysis' }"
            @click="handleTabChange('data-analysis')"
          >
            个人中心
          </button>
          <button 
            class="nav-tab" 
            :class="{ active: activeTab === 'word-learning' }"
            @click="handleTabChange('word-learning')"
          >
            背单词
          </button>
          <button 
            class="nav-tab" 
            :class="{ active: activeTab === 'article-reading' }"
            @click="handleTabChange('article-reading')"
          >
            文章阅读
          </button>
          <button 
            class="nav-tab" 
            :class="{ active: activeTab === 'speaking-practice' }"
            @click="handleTabChange('speaking-practice')"
          >
            口语训练
          </button>
        </nav>
      </div>
    </header>

    <!-- 主内容区域 -->
    <main class="main-content">
      <div class="content-container">
        <DataAnalysis v-if="activeTab === 'data-analysis'" />
        <WordLearning v-else-if="activeTab === 'word-learning'" />
        <ArticleReading v-else-if="activeTab === 'article-reading'" />
        <SpeakingPractice v-else-if="activeTab === 'speaking-practice'" />
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="app-footer">
      <p>© 2023 学习助手 - 简洁高效的学习工具</p>
    </footer>
  </div>
</template>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.nav-header {
  background-color: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-container {
  /* 导航栏最大宽度控制 - 与主内容区域保持一致 */
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.app-title {
  font-size: 1.5em;
  margin: 0;
  padding: 16px 0;
}

.nav-tabs {
  display: flex;
  gap: 4px;
}

.nav-tab {
  padding: 16px 24px;
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  color: #666;
  position: relative;
  transition: all 0.2s ease;
}

.nav-tab:hover {
  color: #4a6fa5;
  background-color: #f8f9fa;
}

.nav-tab.active {
  color: #4a6fa5;
  font-weight: 600;
}

.nav-tab.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 3px;
  background-color: #4a6fa5;
}

.main-content {
  flex: 1;
  padding: 24px 0;
}

.content-container {
  /* 主内容区域最大宽度控制 - 整个应用的主要宽度限制 */
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.app-footer {
  background-color: #f8f9fa;
  padding: 20px 0;
  text-align: center;
  border-top: 1px solid #e9ecef;
  margin-top: auto;
}

.app-footer p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    align-items: stretch;
    padding: 0 10px;
  }

  .app-title {
    text-align: center;
    padding: 12px 0;
  }

  .nav-tabs {
    justify-content: center;
    flex-wrap: wrap;
  }

  .nav-tab {
    padding: 12px 16px;
    font-size: 14px;
  }

  .content-container {
    padding: 0 10px;
  }
}
</style>
