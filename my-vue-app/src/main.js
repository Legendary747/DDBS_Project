import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css'; // Make sure to import the CSS
import TopArticles from './components/TopArticles.vue';

const app = createApp(TopArticles);

app.use(ElementPlus);

app.mount('#app');
