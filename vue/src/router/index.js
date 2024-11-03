import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import BookManagement from '../views/BookManagement.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/book-management',
    name: 'BookManagement',
    component: BookManagement,
    meta: { 
      requiresAuth: true,
      roles: ['ADMIN', 'LIBRARIAN']
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const role = localStorage.getItem('role')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.meta.roles && !to.meta.roles.includes(role)) {
    // 如果页面需要特定角色权限但用户不具备
    next('/')  // 重定向到首页
  } else {
    next()
  }
})

export default router 