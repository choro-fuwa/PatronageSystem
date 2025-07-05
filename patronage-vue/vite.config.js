import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  optimizeDeps: {
    include: ['vue', 'vue-router', 'element-plus', 'axios', '@vue/shared']
  },
  server: {
    port: 5173,
    host: true,
    open: true
  },
  build: {
    rollupOptions: {
      external: ['@vue/shared']
    }
  },
  assetsInclude: ['**/*.xlsx']
})
