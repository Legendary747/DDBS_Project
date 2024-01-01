import { createRouter, createWebHistory } from 'vue-router';
import TopArticles from './components/TopArticles.vue';
import ArticleDetails from './components/ArticleDetails.vue';
import UserPage from './components/UserPage.vue'; // Import the UserPage component

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
        props: true // Pass the :id as a prop
    },
    {
        path: '/user/:id',
        name: 'UserPage',
        component: UserPage,
        props: true // Pass the :id as a prop
    },
    // Add other routes as needed
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
});

export default router;
