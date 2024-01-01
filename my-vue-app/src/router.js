import { createRouter, createWebHistory } from 'vue-router';
import TopArticles from './components/TopArticles.vue'; // Your top articles component
import ArticleDetails from './components/ArticleDetails.vue'; // Your article details component

const routes = [
    {
        path: '/',
        name: 'TopArticles',
        component: TopArticles
    },
    {
        path: '/article/:id',
        name: 'ArticleDetails',
        component: ArticleDetails,
        props: true // This allows us to pass the :id as a prop to the component
    },
    // Add other routes as needed
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL), // Use the environment base URL
    routes,
});

export default router;
