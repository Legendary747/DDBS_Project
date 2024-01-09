<template>
  <div class="top-articles-container">
    <div class="home-and-search">
      <el-button type="warning" @click="goToHome">Home</el-button>
      <el-button type="success" @click="goToDashboard">Dashboard</el-button>
      <el-button type="success" v-if="showSearchButton" @click="togglePopup">Search</el-button>
      <PopUp ref="popupComponent" @close="handlePopupClose" />
    </div>
  </div>
  <div class="top-articles-container">
    <el-row type="flex" justify="center" align="middle" class="header-row">
      <span class="header-title">{{ capitalizedMode }} Top Articles</span>
    </el-row>

    <el-row type="flex" justify="center" align="middle" class="navigation-row">
      <el-button type="primary" @click="switchMode('previous')"><el-icon><ArrowLeft /></el-icon></el-button>

      <div class="articles-slider">
        <el-row :gutter="20" class="articles-row">
          <el-col v-for="(article, index) in currentArticles" :key="index" span="4" class="article-col">
            <el-card @click="goToArticle(article.aid)" class="article-card">
              <div class="article-content">
                <!-- Use ref and set a dynamic ID based on the article ID -->
                <img :ref="'img-' + article.aid" :src="defaultImageUrl" class="article-image" />
                <div class="article-name">{{ article.title }}</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <el-button type="primary" @click="switchMode('next')"><el-icon><ArrowRight /></el-icon></el-button>
    </el-row>
  </div>
  <div class="user-form-container">
    <el-row style="padding-bottom: 5px"><strong>Create User</strong></el-row>
    <el-form :model="userForm" label-width="120px">
      <el-form-item label="Name">
        <el-input v-model="userForm.name" />
      </el-form-item>
      <el-form-item label="Gender">
        <el-input v-model="userForm.gender" />
      </el-form-item>
      <el-form-item label="Email">
        <el-input v-model="userForm.email" />
      </el-form-item>
      <el-form-item label="Phone">
        <el-input v-model="userForm.phone" />
      </el-form-item>
      <el-form-item label="Department">
        <el-input v-model="userForm.dept" />
      </el-form-item>
      <el-form-item label="Grade">
        <el-input v-model="userForm.grade" />
      </el-form-item>
      <el-form-item label="Language">
        <el-input v-model="userForm.language" />
      </el-form-item>
      <el-form-item label="Region">
        <el-select v-model="userForm.region" placeholder="Please select your region">
          <el-option label="Beijing" value="Beijing" />
          <el-option label="Hong Kong" value="HongKong" />
        </el-select>
      </el-form-item>
      <el-form-item label="Role">
        <el-input v-model="userForm.role" />
      </el-form-item>
      <el-form-item label="Prefer Tags">
        <el-input v-model="userForm.preferTags" />
      </el-form-item>
      <el-form-item label="Obtained Credits">
        <el-input v-model="userForm.obtainedCredits" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitUserForm">Create User</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios';
import { ElButton, ElCard, ElCol, ElRow, ElIcon } from 'element-plus';
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue';
import defaultArticleImage from '@/assets/default-article-image.jpeg';
import PopUp from '@/components/PopUp.vue';

export default {
  name: 'TopArticles',
  components: {
    PopUp,
    ElButton,
    ElCard,
    ElCol,
    ElRow,
    ElIcon,
    ArrowLeft,
    ArrowRight,
  },
  data() {
    return {
      mode: 'daily', // 'daily', 'weekly', 'monthly'
      articles: {
        daily: [],
        weekly: [],
        monthly: []
      }, // This will hold articles for each mode
      defaultImageUrl: defaultArticleImage,
      showSearchButton: true,
      userForm: {
        name: '',
        gender: '',
        email: '',
        phone: '',
        dept: '',
        grade: '',
        language: '',
        region: '',
        role: '',
        preferTags: '',
        obtainedCredits: ''
      }
    };
  },
  computed: {
    currentArticles() {
      return this.articles[this.mode];
    },
    capitalizedMode() {
      return this.mode.charAt(0).toUpperCase() + this.mode.slice(1);
    }
  },
  methods: {
    goToHome() {
      this.$router.push('/');
    },
    togglePopup() {
      this.showSearchButton = false;
      this.$refs.popupComponent.togglePopup();
    },
    handlePopupClose() {
      this.showSearchButton = true;
    },
    switchMode(direction) {
      if (direction === 'next') {
        this.mode = this.mode === 'daily' ? 'weekly' : this.mode === 'weekly' ? 'monthly' : 'daily';
      } else {
        this.mode = this.mode === 'daily' ? 'monthly' : this.mode === 'monthly' ? 'weekly' : 'daily';
      }
      this.fetchArticles();
    },
    goToArticle(articleId) {
      this.$router.push(`/article/${articleId}`);
    },
    fetchArticles() {
      axios.get('http://localhost:8080/top_articles')
          .then(response => {
            this.articles.daily = response.data.daily;
            this.articles.weekly = response.data.weekly;
            this.articles.monthly = response.data.monthly;
            this.currentArticles.forEach(article => {
              this.fetchArticleImage(article.aid);
            });
          })
          .catch(error => {
            console.error('There was an error fetching the articles:', error);
          });
    },
    fetchArticleImage(articleId) {
      axios.get(`http://localhost:8080/article-images/${articleId}`)
          .then(response => {
            const base64Image = response.data[0];
            if (base64Image) {
              this.$nextTick(() => {
                const imgRef = this.$refs['img-' + articleId];
                if (imgRef && imgRef.length) {
                  imgRef[0].src = `data:image/jpeg;base64,${base64Image}`;
                }
              });
            }
          })
          .catch(error => {
            console.error('Error fetching article image:', error);
          });
    },
    submitUserForm() {
      axios.post('http://localhost:8080/users', this.userForm)
          .then(response => {
            // Handle successful response
            console.log('User created:', response.data);
          })
          .catch(error => {
            // Handle error
            console.error('Error creating user:', error);
          });
    },
    goToDashboard() {
      window.location.href = 'http://localhost:3000/d/c97d5ae1-f55a-4e39-ba51-6ab451e6fa25/project-dash?orgId=1';
    }
  },
  mounted() {
    this.fetchArticles();
  }
}
</script>

<style scoped>
.top-articles-container {
  width: 100%;
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.navigation-row {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  margin-top: 1rem;
}

.articles-slider {
  display: flex;
  justify-content: center;
  flex-grow: 1; /* Allows the slider to grow as needed */
}

.user-form-container {
  width: 80%;
  margin: 20px auto;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.article-col {
  flex: 0 0 18%; /* Reduce the size of each article */
  max-width: 200px; /* Or set a maximum width */
}

.article-card {
  border: 1px solid transparent;
  transition: border-color 0.3s;
  cursor: pointer;
  height: 100%;
}

.article-card:hover {
  border-color: blue;
}

.article-image {
  max-width: 100%;
  height: auto; /* Adjust the height as needed */
  object-fit: cover; /* Adjust the object fit as needed */
  margin-bottom: 8px;
}

.article-name {
  font-size: 1rem;
  text-align: center;
}

.header-row {
  margin-bottom: 1rem;
}

.header-title {
  font-size: 1.5rem;
  font-weight: bold;
}
.home-and-search {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>
