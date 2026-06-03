import { createStore } from 'vuex'
import axios from '../axios'

export default createStore({
  state: {
    user: null,
    token: localStorage.getItem('token') || null,
    notifications: [],
    unreadCount: 0
  },
  mutations: {
    SET_USER(state, user) { state.user = user },
    SET_TOKEN(state, token) { state.token = token; localStorage.setItem('token', token) },
    CLEAR_AUTH(state) { state.user = null; state.token = null; localStorage.removeItem('token') },
    SET_NOTIFICATIONS(state, notifications) { state.notifications = notifications; state.unreadCount = notifications.filter(n => !n.isRead).length },
    MARK_NOTIFICATION_READ(state, id) { const n = state.notifications.find(x => x.id === id); if (n) { n.isRead = true; state.unreadCount-- } },
    MARK_ALL_NOTIFICATIONS_READ(state) { state.notifications.forEach(n => n.isRead = true); state.unreadCount = 0 }
  },
  actions: {
    async login({ commit }, credentials) {
      const res = await axios.post('/auth/login', credentials)
      commit('SET_TOKEN', res.data.token)
      commit('SET_USER', res.data.user)
      return res.data
    },
    async register({ commit }, user) {
      const res = await axios.post('/auth/register', user)
      commit('SET_TOKEN', res.data.token)
      commit('SET_USER', res.data.user)
      return res.data
    },
    logout({ commit }) { commit('CLEAR_AUTH') },
    async fetchNotifications({ commit, state }) {
      if (!state.user || !state.user.id) return
      const res = await axios.get(`/notifications/user/${state.user.id}`)
      commit('SET_NOTIFICATIONS', res.data)
    },
    async markNotificationRead({ commit }, id) {
      await axios.post(`/notifications/${id}/read`)
      commit('MARK_NOTIFICATION_READ', id)
    },
    async markAllNotificationsRead({ commit, state }) {
      await axios.post(`/notifications/user/${state.user.id}/read-all`)
      commit('MARK_ALL_NOTIFICATIONS_READ')
    }
  },
  getters: {
    isLoggedIn: state => !!state.token && !!state.user,
    isAdmin: state => state.user && state.user.role === 'ADMIN',
    userId: state => state.user?.id,
    studentId: state => state.user?.studentId
  }
})