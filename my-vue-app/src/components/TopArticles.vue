<template>
  <div class="top-articles">
    <el-row type="flex" justify="space-between" align="middle">
      <el-button icon="el-icon-arrow-left" @click="switchMode('previous')"></el-button>

      <!-- ... -->

      <el-row :gutter="20">
        <el-col v-for="(article, index) in currentArticles" :key="index" span="4">
          <el-card @click="goToArticle(article.id)">
            <div style="text-align: center;">
              <!-- Assume the first image in the list is the one you want to display -->
              <img :src="getFirstImage(article.image)" class="article-image" />
              <div>{{ article.title }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- ... -->

    </el-row>
  </div>
</template>

<script>
import axios from 'axios'; // You should have axios installed or use your preferred way to make HTTP requests

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
      // Example logic to switch between 'daily', 'weekly', and 'monthly'
      if(direction === 'next') {
        if(this.mode === 'daily') this.mode = 'weekly';
        else if(this.mode === 'weekly') this.mode = 'monthly';
        else if(this.mode === 'monthly') this.mode = 'daily';
      } else if(direction === 'previous') {
        if(this.mode === 'daily') this.mode = 'monthly';
        else if(this.mode === 'monthly') this.mode = 'weekly';
        else if(this.mode === 'weekly') this.mode = 'daily';
      }
      this.fetchArticles();
    },
    goToArticle(articleId) {
      this.$router.push(`/article/${articleId}`);
    },
    fetchArticles() {
      axios.get('http://localhost:8080/top_articles') // Use your actual backend URL here
          .then(response => {
            // Assuming the response format is as shown in your example
            this.articles.daily = response.data.daily;
            this.articles.weekly = response.data.weekly;
            this.articles.monthly = response.data.monthly;
          })
          .catch(error => {
            console.error('There was an error fetching the articles:', error);
          });
    },
    getFirstImage(imageString) {
      // Assuming the images are comma-separated, get the first one
      const images = imageString.split(',');
      return images[0]; // Return the first image URL
    }
  },
  mounted() {
    this.fetchArticles();
  }
}
</script>

<style scoped>
.article-image {
  max-width: 100%;
  height: auto;
}
</style>
