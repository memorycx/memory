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
              <el-button type="primary" class="edit-button" @click="openEditDialog">编辑个人资料</el-button>
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
      <el-table-column prop="username" label="Name" width="180" />
      <el-table-column prop="content" label="Content" />
    </el-table>
  </div>
    
    <!-- 编辑个人资料弹窗 -->
    <el-dialog
      title="编辑个人资料"
      v-model="editDialogVisible"
      width="40%"
      :before-close="closeEditDialog"
      class="edit-profile-dialog"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        label-width="80px"
        class="edit-profile-form"
      >
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="editForm.gender">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="地址" prop="address">
          <el-input
            v-model="editForm.address"
            placeholder="请输入地址"
            maxlength="50"
            show-word-limit
          ></el-input>
        </el-form-item>
        
        <el-form-item label="学校" prop="school">
          <el-input
            v-model="editForm.school"
            placeholder="请输入学校"
            maxlength="50"
            show-word-limit
          ></el-input>
        </el-form-item>
        
        <el-form-item label="每日新学" prop="newLearnPlane">
          <el-input-number
            v-model="editForm.newLearnPlane"
            :min="1"
            :max="100"
            label="每日新学数量"
          ></el-input-number>
        </el-form-item>
        
        <el-form-item label="背诵书籍" prop="currentBookId">
          <el-select
            v-model="editForm.currentBookId"
            placeholder="请选择背诵书籍"
            style="width: 100%"
          >
            <el-option
              v-for="item in bookOptions"
              :key="item.id"
              :label="item.bookName"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeEditDialog">取消</el-button>
        <el-button type="primary" @click="saveProfile">确定</el-button>
      </div>
    </el-dialog>
  </template>

<script>
import 'cal-heatmap/cal-heatmap.css'
import { update,info , wordNumber ,tableData ,day} from '../api/user'
import { getBookList } from '../api/word'
export default {
  name: 'HomeView',
  data() {
    return {
      fieldsToSplit : ['new_learn', 'review', 'read', 'speek'],
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
        { date: '2021-10-10', username: 'QiuShui', content: '背20个单词' },
        { date: '2021-10-11', username: 'QiuShui', content: '复习12个单词' },
        { date: '2021-10-11', username: 'QiuShui', content: '阅读1篇文章' },
        { date: '2021-10-11', username: 'QiuShui', content: '阅读2篇文章' }
      ],
      // 编辑个人资料弹窗相关
      editDialogVisible: false,
      editForm: {
        gender: 1,
        address: '',
        school: '',
        newLearnPlane: 25,
        currentBookId: 1
      },
      // 背诵书籍选项
      bookOptions: [
        { bookName: 'CET4', id: 1 },
        { bookName: 'CET6', id: 2 },
        { bookName: 'GRE', id: 3 },
        { bookName: 'TOEFL', id: 4 }
      ]
    }
  },
  methods: {
    openEditDialog() {
      this.editForm = {
        gender: this.userData.gender,
        address: this.userData.address,
        school: this.userData.school,
        newLearnPlane: this.userData.newLearnPlane,
        currentBookId: this.userData.currentBookId
      }
      this.editDialogVisible = true
    },
    
    // 关闭编辑个人资料弹窗
    closeEditDialog() {
      this.editDialogVisible = false
    },
    

    async saveProfile() {
      // 更新用户数据
      this.userData.gender = this.editForm.gender
      this.userData.address = this.editForm.address
      this.userData.school = this.editForm.school
      this.userData.newLearnPlane = this.editForm.newLearnPlane
      this.userData.currentBookId = this.editForm.currentBookId
      
      
      await update(this.userData)
      
      // 显示成功提示
      this.$message({
        message: '个人资料更新成功',
        type: 'success'
      })
      
      // 关闭弹窗
      this.editDialogVisible = false
    },
    splitData(data) {
        this.fieldsToSplit.forEach(field => {
            if (data.hasOwnProperty(field)) {
                // 创建新对象，包含公共字段和当前字段
                const newObj = {
                    date: data.date,
                    username: data.username
                };
                if(field === "new_learn") newObj["content"] = "新学" + data[field] +"个单词";
                if(field === "review") newObj["content"] = "复习" + data[field] +"个单词";
                if(field === "read") newObj["content"] = "阅读" + data[field] + "篇文章";
                if(field === "speek") newObj["content"] = "口语" + data[field] + "次";
                this.tableData.push(newObj);
            }
        });
    },

    async init() {
      try{
        this.userData = await info()
        this.allWordNums = await wordNumber()
        this.day = await day()
        this.bookOptions = await getBookList()

        const data = await tableData()
        if(data != null){
          this.tableData = [];
          data.forEach(item => {
            this.splitData(item)
          })
        }

      }
      catch(error){
        console.log(error)
      }
    }
  },
  mounted() {
    this.init();
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
  
  /* 编辑个人资料弹窗样式 */
  .edit-profile-dialog {
    .el-dialog__header {
      background-color: #f5f7fa;
      border-bottom: 1px solid #e4e7ed;
    }
    .el-dialog__title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
    .edit-profile-form {
      margin-top: 20px;
      .el-form-item {
        margin-bottom: 20px;
      }
      .el-radio-group {
        display: flex;
        .el-radio {
          margin-right: 20px;
        }
      }
      .el-input, .el-select {
        width: 100%;
      }
    }
    .dialog-footer {
      text-align: right;
    }
  }
</style>