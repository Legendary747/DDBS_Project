<template>
  <div class="home">
    <el-button type="warning" @click="goToHome">Home</el-button>
  </div>
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

    <!-- Video Player -->
    <div ref="videoContainer" class="video-container"></div>
<!--    <div v-if="article.video" class="article-video">-->
<!--      <video ref="videoElement" controls style="width: 100%; max-height: 500px;"></video>-->
<!--      <button v-if="!isPlaying" @click="playVideo">Play Video</button>-->
<!--    </div>-->

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
import flvjs from 'flv.js';

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
      defaultImageUrl: defaultArticleImage,
      flvPlayer: null,
      isPlaying: false,
      videoElement: null,
    };
  },
  mounted() {
    this.videoElement = document.createElement('video');
    this.videoElement.controls = true;
    this.videoElement.style.width = '100%';
    this.videoElement.style.maxHeight = '500px';

    // Append it to the DOM or manipulate as needed
    // For example, if you have a container for this video:
    const videoContainer = this.$refs.videoContainer;
    if (videoContainer) {
      videoContainer.appendChild(this.videoElement);
    }
  },
  created() {
    this.fetchArticleDetails();
    this.fetchBeReadData();
  },
  beforeUnmount() {
    if (this.flvPlayer) {
      this.flvPlayer.destroy();
    }
  },
  methods: {
    goToHome() {
      this.$router.push('/');
    },
    fetchArticleDetails() {
      axios.get(`http://localhost:8080/article/${this.articleId}`)
          .then(response => {
            this.article = response.data;
            const numberOfImages = response.data.image.split(',').filter(Boolean).length;
            this.articleImages = new Array(numberOfImages).fill(this.defaultImageUrl); // Fill with default images
            this.fetchArticleImage(this.articleId);
            // Initialize the video player after fetching article details
            if (this.article.video && flvjs.isSupported()) {
              console.log("Here!!!!")
              this.initVideoPlayer();
            }
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
    },
    // New method for initializing the video player
    initVideoPlayer() {
      this.flvPlayer = flvjs.createPlayer({
        type: 'flv',
        url: `http://localhost:8080/article-videos/${this.articleId}`,
        isLive: false,
        enableStashBuffer: true,
        stashInitialSize: 256, // You can adjust this value
      });

      this.flvPlayer.attachMediaElement(this.videoElement);
      this.flvPlayer.load();
    },
    playVideo() {
      if (this.flvPlayer) {
        this.flvPlayer.play();
        this.isPlaying = true; // Update the flag when the video starts playing
      }
    },
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
.article-video {
  margin-top: 20px;
  text-align: center;
}

video {
  max-width: 100%;
  height: auto;
}

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
