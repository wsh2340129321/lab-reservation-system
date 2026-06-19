import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue') },
  { path: '/laboratories', name: 'LaboratoryList', component: () => import('../views/LaboratoryList.vue'), meta: { requiresAuth: true } },
  { path: '/laboratory/:id', name: 'LaboratoryDetail', component: () => import('../views/LaboratoryDetail.vue'), meta: { requiresAuth: true } },
  { path: '/reservations', name: 'ReservationList', component: () => import('../views/ReservationList.vue'), meta: { requiresAuth: true } },
  { path: '/profile', name: 'Profile', component: () => import('../views/Profile.vue'), meta: { requiresAuth: true } },
  { path: '/notifications', name: 'Notifications', component: () => import('../views/Notifications.vue'), meta: { requiresAuth: true } },
  { path: '/admin/laboratories', name: 'LaboratoryManage', component: () => import('../views/LaboratoryManage.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/reservations', name: 'ReservationManage', component: () => import('../views/ReservationManage.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/users', name: 'UserManage', component: () => import('../views/UserManage.vue'), meta: { requiresAuth: true, requiresAdmin: true } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isLoggedIn = store.getters.isLoggedIn
  const isAdmin = store.getters.isAdmin
  if (to.meta.requiresAuth && !isLoggedIn) {
    next('/login')
  } else if (to.meta.requiresAdmin && !isAdmin) {
    next('/')
  } else if (to.path === '/' && isLoggedIn && isAdmin) {
    next('/admin/laboratories')
  } else {
    next()
  }
})

export default router