<template>
  <div class="app-container">
    <Header v-if="showHeader" />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import Header from './components/Header.vue'

const route = useRoute()
const showHeader = computed(() => !['/login', '/register'].includes(route.path))
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; }
.app-container { min-height: 100vh; background-color: #f5f7fa; }
.main-content { padding: 20px; min-height: calc(100vh - 60px); }
</style>