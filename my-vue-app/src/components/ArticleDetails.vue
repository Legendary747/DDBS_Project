<template>
  <div class="article-page">
    <h1>{{ article.title }}</h1>

    <div class="article-images">
      <img v-for="(image, index) in articleImages" :key="index" :ref="'img-' + index" :src="defaultImageUrl" class="article-image" />
    </div>

    <ul class="article-details">
      <li v-for="(value, key) in articleAttributes" :key="key">
        <strong>{{ key }}:</strong> {{ value }}
      </li>
    </ul>

    <div class="article-interactions">
      <div class="interaction-item">
        <el-icon><Reading /></el-icon>
        <span>{{ beRead.readNum }} Reads</span>
      </div>
      <div class="interaction-item">
        <el-icon><Comment /></el-icon>
        <span>{{ beRead.commentNum }} Comments</span>
      </div>
      <div class="interaction-item">
        <el-icon><Select /></el-icon>
        <span>{{ beRead.agreeNum }} Likes</span>
      </div>
      <div class="interaction-item">
        <el-icon><Share /></el-icon>
        <span>{{ beRead.shareNum }} Shares</span>
      </div>
    </div>

    <div class="read-by-users">
      <h2>Read by:</h2>
      <button v-for="userId in readUsersList" :key="userId" @click="goToUserPage(userId)">
        <el-icon><User /></el-icon> {{ userId }}
      </button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { ElIcon } from 'element-plus';
import { Reading, Comment, Select, Share, User } from '@element-plus/icons-vue';
import defaultArticleImage from '@/assets/default-article-image.jpeg';

export default {
  name: 'ArticleDetails',
  components: {
    ElIcon,
    Reading,
    Comment,
    Select,
    Share,
    User
  },
  data() {
    return {
      articleId: this.$route.params.id,
      article: {},
      articleImages: new Array(3).fill(defaultArticleImage), // Initialize with default images
      beRead: {},
      readUsersList: [],
      defaultImageUrl: defaultArticleImage
    };
  },
  created() {
    this.fetchArticleDetails();
    this.fetchBeReadData();
  },
  methods: {
    fetchArticleDetails() {
      axios.get(`http://localhost:8080/article/${this.articleId}`)
          .then(response => {
            this.article = response.data;
            const numberOfImages = response.data.image.split(',').filter(Boolean).length;
            this.articleImages = new Array(numberOfImages).fill(this.defaultImageUrl); // Fill with default images
            this.fetchArticleImage(this.articleId);
          })
          .catch(error => {
            console.error('Error fetching article details:', error);
          });
    },
    fetchArticleImage(articleId) {
      axios.get(`http://localhost:8080/article-images/${articleId}`)
          .then(response => {
            response.data.forEach((base64Image, index) => {
              if (base64Image) {
                this.$nextTick(() => {
                  const imgRef = this.$refs['img-' + index];
                  if (imgRef && imgRef.length) {
                    imgRef[0].src = `data:image/jpeg;base64,${base64Image}`;
                  }
                });
              }
            });
          })
          .catch(error => {
            console.error('Error fetching article image:', error);
          });
    },
    fetchBeReadData() {
      axios.get(`http://localhost:8080/be-read/${this.articleId}`)
          .then(response => {
            this.beRead = response.data;
            this.readUsersList = response.data.readUidList.split(',');
          })
          .catch(error => {
            console.error('Error fetching be-read data:', error);
          });
    },
    goToUserPage(userId) {
      this.$router.push(`/user/${userId}`);
    }
  }
};
</script>


<style scoped>

.interaction-item {
  display: flex;
  align-items: center;
}

.article-page {
  max-width: 800px;
  margin: auto;
}

.article-images {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.article-image {
  max-width: calc(100% / 3); /* Display up to 3 images in a row */
  height: auto;
  margin-right: 5px;
}

.article-details {
  list-style-type: none;
  padding: 0;
}

.read-by-users {
  margin-top: 20px;
}

.read-by-users button {
  margin-right: 10px;
  margin: 5px;
  cursor: pointer;
}
</style>



<style scoped>

.interaction-item {
  display: flex;
  align-items: center;
}

.article-page {
  max-width: 800px;
  margin: auto;
}

.article-images {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.article-image {
  max-width: calc(100% / 3); /* Display up to 3 images in a row */
  height: auto;
  margin-right: 5px;
}

.article-details {
  list-style-type: none;
  padding: 0;
}

.read-by-users {
  margin-top: 20px;
}

.read-by-users button {
  margin-right: 10px;
  margin: 5px;
  cursor: pointer;
}
</style>
