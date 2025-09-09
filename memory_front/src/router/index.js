import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import VocabularyView from '../views/VocabularyView.vue'
import ReadingView from '../views/ReadingView.vue'
import SpeakingView from '../views/SpeakingView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'

export default createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/vocabulary',
      name: 'vocabulary',
      component: VocabularyView
    },
    {
      path: '/reading',
      name: 'reading',
      component: ReadingView
    },
    {
      path: '/speaking',
      name: 'speaking',
      component: SpeakingView
    }
  ]
})