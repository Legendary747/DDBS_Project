<template>
  <div v-if="showPopup" class="popup-container">
    <el-select class='drop-down' v-model="searchType">
      <el-option value="Article">Article</el-option>
      <el-option value="User">User</el-option>
    </el-select>

    <el-input style="width: 200px" v-model="searchQuery" type="text" placeholder="Enter Id" />
    <el-button @click="performSearch">Search</el-button>
    <el-button @click="closePopup">Close</el-button>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  data() {
    return {
      showPopup: false,
      searchType: 'Article',
      searchQuery: '',
    };
  },
  methods: {
    togglePopup() {
      this.showPopup = !this.showPopup;
    },
    closePopup() {
      this.showPopup = false;
      this.$emit('close');
    },
    performSearch() {
      // Check if the search query is all numbers
      if (!/^\d+$/.test(this.searchQuery)) {
        ElMessage.error('Please enter a valid Id');
        return;
      }

      // If search type is 'Article', navigate to Article page
      if (this.searchType === 'Article') {
        this.$router.push(`/article/${this.searchQuery}`);
      }
      // If search type is 'User', navigate to User page
      else if (this.searchType === 'User') {
        this.$router.push(`/user/${this.searchQuery}`);
      }
    }
  }
};
</script>

<style scoped>
.drop-down {
  width: 100px;
}
.popup-container {
  display: flex;        /* Enable flex container */
  justify-content: flex-end; /* Align children to the right */
  width: 800px;
}
</style>
