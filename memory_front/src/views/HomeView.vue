<template>
  <div class="home-view">
    <div class="home-container">
      <el-card class="personal-card">
        <div class="personal-info">
          <div class="avatar-section">
            <h2>Qiushui</h2>
            <span>example@example.com</span>
          </div>
          <hr>
          <br>
          <div class="info-section">
            <div class="info-item">
              <span>性别: 男</span>
            </div>
            <div class="info-item">
              <span>地址: 嘉兴</span>
            </div>
            <div class="info-item">
              <span>学校: 嘉兴南湖学院</span>
            </div>
            <div class="info-item">

              <span>每日新学: 20</span>
            </div>
            <div class="info-item">
              <span>每日复习: 30</span>
            </div>
            <div class="info-item">
              <span>背诵书籍: 大学英语四级词汇</span>
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
            <div class="stat-number">2500</div>
            <div class="stat-label">总单词数4500</div>
          </div>
        </div>
        <div class="stat-item card">
          <img src="../assets/books-stack-of-three.png" alt="" class="ico">
          <div class="card-content">
            <div class="stat-label">阅读文章</div>
            <div class="stat-number">120</div>
            <div class="stat-label">累计阅读量</div>
          </div>
        </div>

      </div>

      <div class="data-analysis">
        <div class="stat-item card">
          <img src="../assets/voice-search.png" alt="" class="ico">
          <div class="card-content">
            <div class="stat-label">口语练习</div>
            <div class="stat-number">45</div>
            <div class="stat-label">练习次数</div>
          </div>

        </div>
        <div class="stat-item card">
          <img src="../assets/analytics.png" alt="" class="ico">
          <div class="card-content">
            <div class="stat-label">学习天数</div>
            <div class="stat-number">30</div>
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
      <el-table-column prop="address" label="Address" />
    </el-table>

  </div>  

</template>

<script>
import 'cal-heatmap/cal-heatmap.css'

export default {

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
@media (max-width: 768px) {
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