<template>
  <el-container>
    <el-aside width="200px">
      <SideMenu></SideMenu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>OSSP一站式服务管理平台</strong>
        <div class="header-avatar">
          <el-avatar size="medium"
                     :src="userInfo.avatar"></el-avatar>
          <el-dropdown>
						<span class="el-dropdown-link">
						{{ userInfo.username }}<i class="el-icon-arrow-down el-icon--right"></i>
						</span>
            <el-dropdown-menu slot="dropdown">

              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item>开放平台</el-dropdown-item>
              <el-dropdown-item>权限申请</el-dropdown-item>
              <el-dropdown-item @click.native="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link href="http://csdn.net/ruozhuliufeng" target="_blank">CSDN</el-link>
          <el-link href="http://gitee.com/ruozhuliufeng" target="_blank">Gitee</el-link>
        </div>
      </el-header>
      <el-main>
        <Tabs></Tabs>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Tabs from "@/views/include/Tabs";
import SideMenu from "@/views/include/SideMenu";

export default {
  name: "Home",
  // 声明组件
  components: {
    SideMenu,
    Tabs,
  },
  data() {
    return {
      userInfo: {
        id: "",
        username: "",
        avatar: ""
      }
    }
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then(res => {
        this.userInfo = res.data.data
      })
    },
    logout() {
      this.$axios.post("/logout").then(res => {
        // 清除登录信息
        localStorage.clear();
        sessionStorage.clear();
        this.$store.commit("resetState");
        this.$router.push("/login")
      })
    }
  }
}
</script>

<style>

.el-container {
  padding: 0;
  margin: 0;
  height: 100%;
}

.header-avatar {
  float: right;
  width: 210px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
}

.el-header {
  background-color: #17B3A3;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  line-height: 200px;
}

.el-main {
  color: #333;
  text-align: center;
  padding: 0;
}

a {
  text-decoration: none;
}


</style>
