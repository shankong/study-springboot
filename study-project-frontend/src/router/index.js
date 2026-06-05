import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      {
          path: '/',
          name: 'welcome',
          component: () => import('../views/WelcomeView.vue'),
          children: [
              {
                  path: '',
                  name: 'welcome-login',
                  component: () => import('@/components/welcome/LoginPage.vue'),
              },
              {
                  path: 'forgot-password',
                  name: 'welcome-forgot-password',
                  component: () => import('@/components/welcome/ForgotPasswordPage.vue'),
              },
              {
                  path: 'register',
                  name: 'welcome-register',
                  component: () => import('@/components/welcome/RegisterPage.vue'),
              },
          ]
      },
      {
          path: '/index',
          name: 'index',
          component: () => import('../views/IndexView.vue'),
      },

  ],
})

// 路由守卫：如果用户已有"记住我"登录状态，直接进入首页，跳过登录页
router.beforeEach((to, from, next) => {
    // 只拦截根路径（登录页），忘记密码页面不管
    if (to.path === '/') {
        axios.get('/api/auth/status', { withCredentials: true })
            .then(() => {
                // 已登录（包括"记住我"自动登录）→ 直接跳首页
                next('/index')
            })
            .catch(() => {
                // 未登录 → 正常显示登录页
                next()
            })
    } else {
        next()
    }
})

export default router
