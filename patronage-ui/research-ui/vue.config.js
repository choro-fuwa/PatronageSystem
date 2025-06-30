module.exports = {
  devServer: {
    port: 8888,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 确保与后端地址一致
        changeOrigin: true,
        pathRewrite: {
          '^/api': '' // 移除/api前缀
        },
        // 添加安全选项
        secure: false,
        // 添加跨域支持
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Access-Control-Allow-Methods': 'GET,POST,PUT,DELETE,PATCH,OPTIONS'
        }
      }
    }
  }
}