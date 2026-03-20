import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/dashboard'
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('../views/Register.vue')
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('../views/Layout.vue'),
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('../views/Home.vue')
        },
        {
          path: 'posts',
          name: 'posts',
          component: () => import('../views/PostList.vue')
        },
        {
          path: 'posts/:id',
          name: 'post-detail',
          component: () => import('../views/PostDetail.vue')
        },
        {
          path: 'resources',
          name: 'resources',
          component: () => import('../views/ResourceList.vue')
        },
        {
          path: 'clock',
          name: 'clock',
          component: () => import('../views/ClockList.vue')
        },
        {
          path: 'assistant',
          name: 'assistant',
          component: () => import('../views/AiAssistant.vue')
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/register']
  const authRequired = !publicPages.includes(to.path)
  const userStore = useUserStore()

  if (authRequired && !userStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
