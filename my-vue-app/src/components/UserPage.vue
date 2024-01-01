<template>
  <div class="home">
    <el-button type="warning" @click="goToHome">Home</el-button>
  </div>
  <div class="user-page">
    <img :src="defaultUserImage" class="user-image" alt="Default User" />

    <div class="user-details">
      <h2>{{ user.name }}</h2>
      <p><strong>Gender:</strong> {{ user.gender }}</p>
      <p><strong>Department:</strong> {{ user.dept }}</p>
      <p><strong>Grade:</strong> {{ user.grade }}</p>
      <p><strong>Language:</strong> {{ user.language }}</p>
      <p><strong>Role:</strong> {{ user.role }}</p>
      <p><strong>Prefer Tags:</strong> {{ user.preferTags }}</p>
      <p><strong>Obtained Credits:</strong> {{ user.obtainedCredits }}</p>
      <p><el-icon><FolderOpened /></el-icon> {{ user.email }}</p>
      <p><el-icon><Iphone /></el-icon> {{ user.phone }}</p>
      <p><el-icon><Location /></el-icon> {{ user.region }}</p>
    </div>

    <div class="user-read-articles">
      <h2>Articles Read:</h2>
      <div class="read-articles-wrapper">
        <button v-for="articleId in readArticleIds" :key="articleId" @click="goToArticle(articleId)" class="read-article-button">
          <el-icon><Document /></el-icon> {{ articleId }}
        </button>
      </div>
    </div>

  </div>
</template>

<script>
import axios from 'axios';
import { ElIcon } from 'element-plus';
import { FolderOpened, Iphone, Location, Document } from '@element-plus/icons-vue';

import defaultUserImage from '@/assets/default-article-image.jpeg';

export default {
  name: 'UserPage',
  components: {
    ElIcon,
    FolderOpened,
    Iphone,
    Location,
    Document,
  },
  data() {
    return {
      userId: this.$route.params.id,
      user: {},
      readArticleIds: [],
      defaultUserImage: defaultUserImage,
    };
  },
  created() {
    this.fetchUserData();
    this.fetchUserReadArticles();
  },
  methods: {
    goToHome() {
      this.$router.push('/');
    },
    fetchUserData() {
      axios.get(`http://localhost:8080/user/${this.userId}`)
          .then(response => {
            this.user = response.data;
          })
          .catch(error => console.error('Error fetching user data:', error));
    },
    fetchUserReadArticles() {
      axios.get(`http://localhost:8080/user-read/${this.userId}`)
          .then(response => {
            this.readArticleIds = response.data;
          })
          .catch(error => console.error('Error fetching user read articles:', error));
    },
    goToArticle(articleId) {
      this.$router.push(`/article/${articleId}`);
    },
  }
};
</script>

<style scoped>
.user-page {
  max-width: 800px;
  margin: auto;
  text-align: center;
}

.user-image {
  max-width: 150px;
  height: auto;
  margin: 20px auto;
}

.user-details {
  margin: 20px 0;
}

.read-articles button {
  margin: 5px;
  cursor: pointer;
}

.read-articles-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
  gap: 10px; /* Space between buttons */
}

.read-article-button {
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 5px 10px; /* Adjust padding as needed */
  border: 1px solid #ccc; /* Optional border for better visibility */
}

</style>
