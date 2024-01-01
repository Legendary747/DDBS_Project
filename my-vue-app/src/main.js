import { createApp } from 'vue';
import App from './App.vue'; // Import the main App component
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import router from './router'; // Make sure to import the router

const app = createApp(App); // Use the main App component

app.use(ElementPlus);
app.use(router); // Tell Vue to use the vue-router plugin

app.mount('#app');
