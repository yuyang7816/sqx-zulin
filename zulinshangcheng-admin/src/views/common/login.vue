<template>
  <div class="site-wrapper site-page--login login-page">
    <div class="login-page__bg" aria-hidden="true">
      <span class="login-page__orb login-page__orb--1"></span>
      <span class="login-page__orb login-page__orb--2"></span>
      <span class="login-page__orb login-page__orb--3"></span>
    </div>
    <div class="login-layout">
      <div class="login-content">
      <div class="login-brand">
        <div class="login-brand__inner">
          <img class="login-brand__logo" src="~@/assets/img/avatar.png" alt="logo">
          <h1 class="login-brand__title">{{ entryName }}</h1>
          <p class="login-brand__desc">设备租赁管理后台 · 数据一目了然，运营更高效</p>
          <ul class="login-brand__features">
            <li><i class="el-icon-s-data"></i> 运营数据实时汇总</li>
            <li><i class="el-icon-s-check"></i> 设备租赁全流程管理</li>
            <li><i class="el-icon-s-cooperation"></i> 多角色权限协同</li>
          </ul>
        </div>
      </div>
      <div class="login-panel">
        <div class="login-main">
          <div class="login-main__accent" aria-hidden="true"></div>
          <h3 class="login-title">账号登录</h3>
          <p class="login-subtitle">请输入您的账号信息以继续</p>
          <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
            <el-form-item prop="userName">
              <el-input v-model="dataForm.userName" placeholder="帐号" prefix-icon="el-icon-user"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="dataForm.password" type="password" placeholder="密码" prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
            <el-form-item prop="captcha">
              <el-row :gutter="12">
                <el-col :span="14">
                  <el-input v-model="dataForm.captcha" placeholder="验证码" prefix-icon="el-icon-key"></el-input>
                </el-col>
                <el-col :span="10" class="login-captcha">
                  <img :src="captchaPath" @click="getCaptcha()" alt="验证码" title="点击刷新验证码">
                </el-col>
              </el-row>
            </el-form-item>
            <el-form-item>
              <el-button class="login-btn-submit nf-login-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      </div>
    </div>
    <el-dialog title="商家入驻" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 15px;">
        <span style="width: 200px;display: inline-block;text-align: right;">商户选择：</span>
        <el-radio-group v-model="isWindow" @change="merchClick">
            <el-radio :label="1">新商户入驻</el-radio>
            <el-radio :label="2">商户修改</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 15px;" v-if="isWindow == 2">
        <span style="width: 200px;display: inline-block;text-align: right;">商户编号：</span>
        <el-input style="width:50%;" type="text" v-model="id" placeholder="请输入商户编号"></el-input>
      </div>
      <div slot="footer" class="dialog-footer" v-if="isWindow == 1">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="merchoneTo()">确 定</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-if="isWindow == 2">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="merchtwoTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getUUID } from '@/utils'
  import router, { fnAddDynamicMenuRoutes } from '@/router'
  import { entryName } from '@/utils/httpRequest'
  export default {
    data () {
      return {
        dataForm: {
          userName: '',
          password: '',
          uuid: '',
          captcha: ''
        },
        id:'',
        isWindow:1,
        dialogFormVisible:false,
        dataRule: {
          userName: [
            { required: true, message: '帐号不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          captcha: [
            { required: true, message: '验证码不能为空', trigger: 'blur' }
          ]
        },
        captchaPath: '',
        entryName:'',
      }
    },
    created () {
      this.entryName = entryName
      this.getCaptcha()
    },
    methods: {
      merchadd(){
        this.dialogFormVisible = true
      },
      merchClick(val){
        console.log(val)
      },
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl2(`/admin/login/${this.dataForm.userName}/${this.dataForm.password}`),
              method: 'post',
              params: this.$http.adornParams({
                'uuid': this.dataForm.uuid,
                'captcha': this.dataForm.captcha,
              })
            }).then(({data}) => {
              if (data.status === 0) {
                this.$cookie.set('token', data.data.uuid)
                this.$cookie.set('userId', data.data.adminId)
                this.getRouter(data.data.adminId)

              } else {
                this.$message.error(data.msg)
                this.getCaptcha()
              }
            })
          }
        })
      },
      getRouter(userId) {
        this.$http({
          url: this.$http.adornUrl2(`/sys/menu/nav?userId=${userId}`),
          method: 'get',
          params: this.$http.adornData()
        }).then(({data}) => {
          if (data && data.status === 0) {
            fnAddDynamicMenuRoutes(data.data.menuList)
            router.options.isAddDynamicMenuRoutes = true
            sessionStorage.setItem('menuList', JSON.stringify(data.data.menuList || '[]'))
            sessionStorage.setItem('permissions', JSON.stringify(data.data.permissions || '[]'))
            this.$store.commit('common/updateMenuList', data.data.menuList || [])
            this.$store.commit('common/updateMenuActiveName', '')
            this.$store.commit('common/updateMainTabs', [])
            this.$store.commit('common/updateMainTabsActiveName', '')
            this.$router.replace({ name: 'home' })
          } else {
            sessionStorage.setItem('menuList', '[]')
            sessionStorage.setItem('permissions', '[]')
          }
        })
      },
      merchoneTo(){
        this.$router.push({path: '/merchant'})
      },
      merchtwoTo(){
         this.$router.push({path: '/merchantClone', query: {id: this.id}})
      },
      getCaptcha () {
        this.dataForm.uuid = getUUID()
        this.captchaPath = this.$http.adornUrl2(`/captcha/captcha.jpg?uuid=${this.dataForm.uuid}`)
      }
    }
  }
</script>

<style lang="scss">
  @import '@/assets/scss/_nf-scaffold-tokens.scss';

  .site-page--login.login-page {
    position: fixed;
    inset: 0;
    min-width: 960px;
    background: linear-gradient(145deg, $nf-brand-lite 0%, $nf-brand 52%, $nf-brand-deep 100%);
    overflow: hidden;
  }
  .login-page__bg {
    position: absolute;
    inset: 0;
    pointer-events: none;
    overflow: hidden;
  }
  .login-page__orb {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.08);
    &--1 {
      width: 420px;
      height: 420px;
      top: -120px;
      right: -80px;
    }
    &--2 {
      width: 280px;
      height: 280px;
      bottom: -60px;
      left: -80px;
      background: rgba(255, 255, 255, 0.06);
    }
    &--3 {
      width: 160px;
      height: 160px;
      bottom: 18%;
      right: 12%;
      background: rgba(255, 255, 255, 0.1);
    }
  }
  .login-layout {
    position: relative;
    z-index: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    padding: 40px 24px;
  }
  .login-content {
    display: flex;
    width: 100%;
    max-width: 980px;
    min-height: 520px;
    border-radius: 16px;
    background: linear-gradient(160deg, $nf-brand-lite 0%, #fff 48%, $nf-page-bg 100%);
    box-shadow: 0 20px 60px rgba($nf-brand-deep, 0.28), 0 0 0 1px rgba(255, 255, 255, 0.45);
    overflow: hidden;
  }
  .login-brand {
    position: relative;
    flex: 0 0 44%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 48px 40px;
    border-right: 1px solid rgba($nf-brand, 0.12);
  }
  .login-brand__inner {
    position: relative;
    z-index: 1;
    max-width: 380px;
    text-align: center;
  }
  .login-brand__logo {
    width: 76px;
    height: 76px;
    border-radius: 16px;
    object-fit: cover;
    margin-bottom: 20px;
    box-shadow: 0 8px 28px rgba(21, 202, 210, 0.22);
    border: 2px solid #fff;
  }
  .login-brand__title {
    margin: 0 0 10px;
    font-size: 26px;
    font-weight: 700;
    color: $nf-text-primary;
    line-height: 1.35;
    letter-spacing: 0.02em;
  }
  .login-brand__desc {
    margin: 0 0 28px;
    font-size: 14px;
    line-height: 1.7;
    color: $nf-text-secondary;
  }
  .login-brand__features {
    list-style: none;
    margin: 0;
    padding: 0;
    text-align: left;
    li {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px 16px;
      margin-bottom: 8px;
      font-size: 13px;
      color: $nf-text-primary;
      background: rgba(255, 255, 255, 0.72);
      border-radius: 8px;
      border: 1px solid rgba($nf-brand, 0.12);
      i {
        font-size: 16px;
        color: $nf-brand;
        flex-shrink: 0;
      }
    }
  }
  .login-panel {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 32px 24px;
  }
  .login-main {
    position: relative;
    width: 100%;
    max-width: 420px;
    padding: 36px 36px 40px;
    border-radius: 10px;
    background: #fff;
    box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06), 0 1px 4px rgba(0, 0, 0, 0.04);
    border: 1px solid $nf-border;
    overflow: hidden;
  }
  .login-main__accent {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: linear-gradient(90deg, $nf-brand 0%, $nf-brand-deep 100%);
  }
  .login-title {
    margin: 0 0 6px;
    font-size: 22px;
    font-weight: 600;
    color: $nf-text-primary;
    text-align: left;
  }
  .login-subtitle {
    margin: 0 0 24px;
    font-size: 13px;
    color: $nf-text-secondary;
    text-align: left;
  }
  .login-page {
    .el-form-item {
      margin-bottom: 20px;
    }
    .el-input__inner {
      height: 42px;
      line-height: 42px;
      border-radius: 6px;
      border-color: $nf-border;
      transition: border-color 0.2s, box-shadow 0.2s;
      &:focus {
        border-color: $nf-brand;
        box-shadow: 0 0 0 2px rgba($nf-brand, 0.15);
      }
    }
    .el-input__prefix {
      color: $nf-text-secondary;
    }
  }
  .login-captcha {
    overflow: hidden;
    border-radius: 6px;
    border: 1px solid $nf-border;
    height: 42px;
    transition: border-color 0.2s, box-shadow 0.2s;
    &:hover {
      border-color: $nf-brand;
      box-shadow: 0 0 0 2px rgba($nf-brand, 0.1);
    }
    > img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      cursor: pointer;
      display: block;
    }
  }
  .login-btn-submit {
    width: 100%;
    height: 44px;
    margin-top: 4px;
    font-size: 15px;
    font-weight: 600;
    letter-spacing: 0.06em;
    border-radius: 6px;
    background-color: $nf-brand !important;
    border-color: $nf-brand !important;
    color: #fff !important;
    transition: background-color 0.2s, border-color 0.2s, transform 0.15s;
    &:hover,
    &:focus {
      background-color: $nf-brand-deep !important;
      border-color: $nf-brand-deep !important;
      color: #fff !important;
    }
    &:active {
      transform: scale(0.99);
    }
  }
  @media (max-width: 900px) {
    .site-page--login.login-page {
      min-width: 0;
    }
    .login-layout {
      padding: 24px 16px;
    }
    .login-content {
      flex-direction: column;
      min-height: auto;
    }
    .login-brand {
      display: none;
    }
    .login-panel {
      flex: 1;
      padding: 24px 16px;
    }
    .login-main {
      max-width: 100%;
      padding: 32px 24px 36px;
    }
  }
</style>
