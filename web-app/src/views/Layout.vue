<script setup>
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="logo">学伴领航</div>
      <el-menu
        :default-active="$route.path"
        router
        class="el-menu-vertical"
      >
        <el-menu-item index="/dashboard">
          <el-icon><component is="House" /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/dashboard/posts">
          <el-icon><component is="ChatLineRound" /></el-icon>
          <span>社区交流</span>
        </el-menu-item>
        <el-menu-item index="/dashboard/resources">
          <el-icon><component is="Folder" /></el-icon>
          <span>资源中心</span>
        </el-menu-item>
        <el-menu-item index="/dashboard/clock">
          <el-icon><component is="Timer" /></el-icon>
          <span>每日打卡</span>
        </el-menu-item>
        <el-menu-item index="/dashboard/assistant">
          <el-icon><component is="ChatDotRound" /></el-icon>
          <span>学习问答</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-content">
          <div class="breadcrumb">Web 端管理控制台</div>
          <div class="user-info">
            <span class="welcome">欢迎, {{ userStore.userInfo.nickname || userStore.userInfo.username }}</span>
            <el-button type="danger" size="small" circle @click="handleLogout">
              <el-icon><component is="SwitchButton" /></el-icon>
            </el-button>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
}

.el-aside {
  background-color: #304156;
  color: #fff;
  
  .logo {
    height: 60px;
    line-height: 60px;
    text-align: center;
    font-size: 20px;
    font-weight: bold;
    border-bottom: 1px solid #1f2d3d;
    color: #fff;
  }
  
  .el-menu {
    border-right: none;
    background-color: transparent;
    
    :deep(.el-menu-item) {
      color: #bfcbd9;
      
      &:hover {
        background-color: #263445;
      }
      
      &.is-active {
        color: #409eff;
        background-color: #1f2d3d;
      }
    }
  }
}

.el-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  padding: 0 20px;
  
  .header-content {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .welcome {
      font-size: 14px;
      color: #606266;
    }
  }
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
