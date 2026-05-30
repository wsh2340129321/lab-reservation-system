import axios from 'axios'
import store from './store'
import router from './router'

const instance = axios.create({
  baseURL: '/api',
  timeout: 10000
})

instance.interceptors.request.use(
  config => {
    if (store.state.token) {
      config.headers.Authorization = `Bearer ${store.state.token}`
    }
    return config
  },
  error => Promise.reject(error)
)

instance.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      store.dispatch('logout')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default instance