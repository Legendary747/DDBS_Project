<template>
  <div class="top-articles-container">
    <el-row type="flex" justify="center" align="middle" class="header-row">
      <span class="header-title">{{ capitalizedMode }} Top Articles</span>
    </el-row>

    <el-row type="flex" justify="center" align="middle" class="navigation-row">
      <el-button type="primary" @click="switchMode('previous')"><el-icon><ArrowLeft /></el-icon></el-button>

      <div class="articles-slider">
        <el-row :gutter="20" class="articles-row">
          <el-col v-for="(article, index) in currentArticles" :key="index" span="4" class="article-col">
            <el-card @click="goToArticle(article.id)" class="article-card">
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
</template>

<script>
import axios from 'axios';
import { ElButton, ElCard, ElCol, ElRow, ElIcon } from 'element-plus';
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue';
import defaultArticleImage from '@/assets/default-article-image.jpeg';

export default {
  name: 'TopArticles',
  components: {
    ElButton,
    ElCard,
    ElCol,
    ElRow,
    ElIcon,
    ArrowLeft,
    ArrowRight
  },
  data() {
    return {
      mode: 'daily', // 'daily', 'weekly', 'monthly'
      articles: {
        daily: [],
        weekly: [],
        monthly: []
      }, // This will hold articles for each mode
      defaultImageUrl: defaultArticleImage
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

.el-row.articles-row {
  flex-wrap: nowrap;
  justify-content: center; /* Center the articles in the row */
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

.el-button {
  flex: none;
}
</style>
