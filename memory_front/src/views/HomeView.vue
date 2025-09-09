<template>
  <div class="home-view">
    <div class="home-container">
      <el-card class="personal-card">
        <div class="personal-info">
          <div class="avatar-section">
            <h2>{{ userData.username }}</h2>
            <span>{{ userData.email }}</span>
          </div>
          <hr>
          <br>
          <div class="info-section">
            <div class="info-item">
              <span>性别: {{ userData.gender === 1? '男' : '女' }}</span>
            </div>
            <div class="info-item">
              <span>地址: {{ userData.address }}</span>
            </div>
            <div class="info-item">
              <span>学校: {{ userData.school }}</span>
            </div>
            <div class="info-item">

              <span>每日新学: {{ userData.newLearnPlane }}</span>
            </div>
            <div class="info-item">
              <span>每日复习: {{ userData.reviewPlane }}</span>
            </div>
            <div class="info-item">
              <span>背诵书籍: {{ userData.currentBookId }}</span>
              <el-button type="primary" class="edit-button">编辑个人资料</el-button>
            </div>
          </div>
        </div>
      </el-card>
      

      <div class="data-analysis">
        <div class="stat-item card">
          <img src="../assets/sigma.png" alt="" class="ico">
          <div class="card-content">
            <div class="stat-label">已掌握单词</div>
            <div class="stat-number">{{ userData.wordNums }}</div>
            <div class="stat-label">总单词数 {{ allWordNums }}</div>
          </div>
        </div>
        <div class="stat-item card">
          <img src="../assets/books-stack-of-three.png" alt="" class="ico">
          <div class="card-content">
            <div class="stat-label">阅读文章</div>
            <div class="stat-number">{{  userData.textNums }}</div>
            <div class="stat-label">累计阅读量</div>
          </div>
        </div>

      </div>

      <div class="data-analysis">
        <div class="stat-item card">
          <img src="../assets/voice-search.png" alt="" class="ico">
          <div class="card-content">
            <div class="stat-label">口语练习</div>
            <div class="stat-number">{{ userData.speekNums }}</div>
            <div class="stat-label">练习次数</div>
          </div>

        </div>
        <div class="stat-item card">
          <img src="../assets/analytics.png" alt="" class="ico">
          <div class="card-content">
            <div class="stat-label">学习天数</div>
            <div class="stat-number">{{ day }}</div>
            <div class="stat-label">连续学习天数</div>
          </div>
        </div>
      </div>

      
    </div>
  </div>
  <div class="learning-record">
    <h1>学习记录</h1>
    <el-table :data="tableData" style="width: 100%">
      <el-table-column prop="date" label="Date" width="180" />
      <el-table-column prop="name" label="Name" width="180" />
      <el-table-column prop="content" label="Content" />
    </el-table>

  </div>  

</template>

<script>
import 'cal-heatmap/cal-heatmap.css'
import { info , wordNumber ,tableData ,day} from '../api/user'
export default {
  name: 'HomeView',
  data() {
    return {
      userData:{
        username: 'Qiushui',
        email: 'example@example.com',
        gender: 1,
        address: '嘉兴',
        school: '嘉兴南湖学院',
        newLearnPlane: 25,
        reviewPlane: 30,
        currentBookId: 1,
        textNums: 20,
        wordNums: 1500,
        speekNums: 21,
      },
      day : 20,
      allWordNums : 0,
      tableData: [
        { date: '2021-10-10', name: 'QiuShui', content: '背20个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '复习12个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读1篇文章' },
        { date: '2021-10-10', name: 'QiuShui', content: '背20个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '复习12个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读1篇文章' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读2篇文章' },
        { date: '2021-10-10', name: 'QiuShui', content: '背20个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '复习12个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读1篇文章' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读2篇文章' },
        { date: '2021-10-10', name: 'QiuShui', content: '背20个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '复习12个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读1篇文章' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读2篇文章' },
        { date: '2021-10-10', name: 'QiuShui', content: '背20个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '复习12个单词' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读1篇文章' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读2篇文章' },
        { date: '2021-10-11', name: 'QiuShui', content: '阅读2篇文章' },]
    }
  },
  methods: {
    async fetchData() {
      try{
         const data = await info()
         if(data != null) this.userData = data
      }
      catch(error){
        console.log(error)
      }
    },
    async fetchWordNumber(){
      try{
        const data = await wordNumber()
        if(data != null) this.allWordNums = data
      }
      catch(error){
        console.log(error)
      }
    },
    async fetchTableData(){
      try{
        const data = await tableData()
        if(data != null) this.tableData = data
      }
      catch(error){
        console.log(error)
      }
    },
    async fetchDay(){
      try{
        const data = await day()
        if(data != null) this.day = data
      }
      catch(error){
        console.log(error)
      }
    }
  },
  mounted() {
    this.fetchWordNumber()
    this.fetchData()
    this.fetchTableData()
    this.fetchDay()
  }
}
</script>

<style scoped>
.learning-record{
  padding: 20px;
}
.ico{
  width: 40px;
  height: 40px;
  margin-right: 16px;
}
.home-view{
  display: flex;
  height: 100%;
}
.home-container {
  display: flex;
  gap: 20px;
  padding: 20px;
  width: 100%;
  background-color: #f5f7fa;
}

.personal-card {
  width: 500px;
  height: 400px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.personal-info {
  padding: 20px;
}

.avatar-section {
  text-align: center;
  margin-bottom: 20px;
}



.info-section {
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  color: #666;
}

.info-item i {
  width: 20px;
  margin-right: 10px;
  color: var(--primary-color);
}

.edit-button {
  width: 30%;
  margin-left: auto;
  background-color: var(--primary-color);
  border-color: var(--primary-color);
}

.data-analysis {
  flex: 2;
  display: flex;
  flex-direction: column;
  /* justify-content:end; */
  gap: 20px;
}

.data-card {
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.heatmap-container {
  height: 180px;
  padding: 20px 0;
  overflow-x: auto;
  overflow-y: hidden;
}

/* 响应式调整 */
@media (max-width: 1250px) {
  .edit-button{
      width: 30%;
      min-width: 140px;
  }

  .home-container {
    flex-direction: column;
  }
  
  .personal-card {
    width: 100%;
  }
  
  .heatmap-container {
    overflow-x: auto;
    -webkit-overflow-scrolling: touch;
  }
  
  /* 隐藏滚动条但保留功能 */
  .heatmap-container::-webkit-scrollbar {
    height: 4px;
  }
  
  .heatmap-container::-webkit-scrollbar-track {
    background: #f1f1f1;
  }
  
  .heatmap-container::-webkit-scrollbar-thumb {
    background: #c0c4cc;
    border-radius: 2px;
  }
  
  .heatmap-container::-webkit-scrollbar-thumb:hover {
    background: #909399;
  }
}

.stat-item {
  display: flex;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  height: 50%;
  justify-content: center;
  align-items: center;
}

.stat-number {
  font-size: 28px;
  font-weight: bold;
  color: var(--primary-color);
  margin-bottom: 5px;
}

.stat-label {
  color: #666;
  font-size: 14px;
}
</style>