<template>
  <el-row type="flex" class="row-bg" justify="center">
    <el-col :xl="6" :lg="7">
      <div class="grid-content bg-purple">
        <h2>欢迎使用OSSP一站式服务平台</h2>
        <el-image
            style="height: 180px;
            weight:180px"
            :src="imgUrl"></el-image>
        <p>公众号：若竹流风</p>
        <p>关注公众号，加入开放平台</p>
      </div>
    </el-col>
    <el-col :span="1">
      <el-divider direction="vertical">
      </el-divider>
    </el-col>
    <el-col :xl="6" :lg="7">
      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="用户名称" prop="username" style="width:380px">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="用户密码" prop="password" style="width:380px">
          <el-input v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code" style="width:380px">
          <el-input v-model="loginForm.code" style="width:172px;float: left"></el-input>
          <el-image src="" class="captchaImg"></el-image>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('loginForm')">立即创建</el-button>
          <el-button @click="resetForm('loginForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      imgUrl: 'http://img.aixuxi.cn/20211027225532.jpg',
      loginForm: {
        username: '',
        password: '',
        code: '',
        token: ''
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名称', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        code: [
          {required: true, message: '请输入验证码', trigger: 'blur'},
          {min: 5, max: 5, message: "长度为五个字符", trigger: 'blur'}
        ],
      },
      captchaImg: null
    }
  },
  methods:
      {
        created() {
          this.getCaptcha()
        },
        submitForm(formName) {
          this.$refs[formName].validate((valid) => {
            if (valid) {
              this.$axios.post("/login", this.loginForm).thren(res => {
                const jwt = res.headers['authorization']
                this.$store.commit('SET_TOKEN', jwt)
                this.$router.push("/index")
              })
            } else {
              return false;
            }
          });
        }
        ,
        resetForm(formName) {
          this.$refs[formName].resetFields();
        },
        getCaptcha() {
          console.log("/captcha")
          this.$axios.get("/captcha").then(res => {
            console.log(res)
            this.loginForm.token = res.data.data.token
            this.captchaImg = res.data.data.captchaImg
          })
        }
      }
}
</script>

<style scoped>
.el-row {
  background-color: #fafafa;
  height: 100%;
  display: flex;
  align-items: center;
  text-align: center;
  justify-content: center;
}

.el-divider {
  height: 200px
}

.captchaImg {
  float: left;
  margin-left: 8px;
  border-radius: 4px;
}
</style>
