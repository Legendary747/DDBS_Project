<template>
  <div class="top-articles-container">
    <el-row type="flex" justify="center" align="middle">
      <el-button icon="el-icon-arrow-left" @click="switchMode('previous')"></el-button>

      <div class="articles-slider">
        <el-row :gutter="20">
          <el-col v-for="(article, index) in currentArticles" :key="index" span="4">
            <el-card @click="goToArticle(article.id)">
              <div style="text-align: center;">
                <img v-if="article.image" :src="article.image" class="article-image" />
                <div class="article-name">{{ article.title }}</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <el-button icon="el-icon-arrow-right" @click="switchMode('next')"></el-button>
    </el-row>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'TopArticles',
  data() {
    return {
      mode: 'daily', // 'daily', 'weekly', 'monthly'
      articles: {
        daily: [],
        weekly: [],
        monthly: []
      } // This will hold articles for each mode
    };
  },
  computed: {
    currentArticles() {
      // Return the articles for the current mode
      return this.articles[this.mode];
    }
  },
  methods: {
    switchMode(direction) {
      // Logic to switch between 'daily', 'weekly', and 'monthly'
      if (direction === 'next') {
        if (this.mode === 'daily') this.mode = 'weekly';
        else if (this.mode === 'weekly') this.mode = 'monthly';
        else if (this.mode === 'monthly') this.mode = 'daily';
      } else if (direction === 'previous') {
        if (this.mode === 'daily') this.mode = 'monthly';
        else if (this.mode === 'monthly') this.mode = 'weekly';
        else if (this.mode === 'weekly') this.mode = 'daily';
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
            const base64Image = response.data[0]; // Assuming the first image is the one we want
            console.log("Here!")
            if (base64Image) {
              const article = this.currentArticles.find(a => a.aid === articleId);
              console.log("Here!!")
              if (article) {
                article.image = `data:image/jpeg;base64,${base64Image}`;
                console.log("Here!!!")
              }
            }
          })
          .catch(error => {
            console.error('Error fetching article image:', error);
          });
    }
  },
  watch: {
    mode() {
      this.currentArticles.forEach(article => {
        this.fetchArticleImage(article.id);
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
  overflow: hidden; /* Prevents horizontal scroll */
}

.articles-slider {
  display: flex;
  overflow-x: auto; /* Allows for a horizontal scroll */
  scroll-behavior: smooth;
}

.article-image {
  max-width: 100%;
  height: auto; /* Adjust as necessary */
  margin-bottom: 8px; /* Space between image and article name */
}

.article-name {
  font-size: 1rem; /* Adjust as necessary */
  text-align: center;
}

.el-col {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.el-row {
  flex-wrap: nowrap; /* Prevents wrapping of child elements */
}

.el-button {
  flex: none; /* Prevents buttons from stretching */
}
</style>

