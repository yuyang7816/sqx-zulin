<template>
  <nav class="site-navbar site-navbar--nf" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand">
        <a class="site-navbar__brand-lg" href="javascript:;">
          <img src="~@/assets/img/avatar.png" class="site-navbar__logo" alt="logo">
          <span>{{ entryName }}</span>
        </a>
        <a class="site-navbar__brand-mini" href="javascript:;">
          <img src="~@/assets/img/avatar.png" class="site-navbar__logo" alt="logo">
        </a>
      </h1>
    </div>
    <div class="site-navbar__body clearfix">
      <el-menu class="site-navbar__menu" mode="horizontal">
        <el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
          <icon-svg name="zhedie"></icon-svg>
        </el-menu-item>
      </el-menu>
      <div class="site-navbar__context">
        <span class="nf-context-badge">{{ contextBadge }}</span>
        <span class="site-navbar__context-title">{{ pageTitle }}</span>
      </div>
      <el-menu class="site-navbar__menu site-navbar__menu--right" mode="horizontal">
        <el-menu-item index="1" style="position: relative;" @click="$router.push({ name: 'vueMchat' })">
          <template slot="title">
            <span class="left-number" v-if="chatNum">{{ chatNum }}</span>
            <icon-svg name="chat" class="el-icon-setting" style="font-size: 28px;"></icon-svg>
            <audio id="audio" controls preload hidden="hidden" autoplay="autoplay" v-if="chatNum>0">
              <source src="https://pw.xianmxkj.com/file/uploadPath/2022/01/19/0753211f78d718d44ee6372e33eae9ee.mp3"
                type="audio/mpeg">
            </audio>
            <audio id="audio1" preload hidden="hidden" autoplay="autoplay" v-if="chatNum2>0">
              <source src="https://pw.xianmxkj.com/file/uploadPath/2022/01/12/01beb0bc15549b9aa4d18027a7751bae.mp3"
                type="audio/mpeg">
            </audio>
          </template>
        </el-menu-item>
        <el-menu-item class="site-navbar__avatar" index="3">
          <el-dropdown :show-timeout="0" placement="bottom">
            <span class="user-pill el-dropdown-link">
              <img class="user-pill__avatar" src="~@/assets/img/avatar.png" :alt="userName">
              <span class="user-pill__info">
                <span class="user-pill__name">{{ username ? username : '管理员' }}</span>
                <span class="user-pill__hint" v-if="username">欢迎您登录</span>
              </span>
              <i class="el-icon-arrow-down user-pill__arrow"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-menu-item>
      </el-menu>
    </div>
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  </nav>
</template>

<script>
  import UpdatePassword from './main-navbar-update-password'
  import {
    clearLoginInfo
  } from '@/utils'
  import { entryName } from '@/utils/httpRequest'
  export default {
    data() {
      return {
        updatePassowrdVisible: false,
        username: '',
        intervalId: 0,
        storeId: 0,
        isshow: false,
        chatNum: 0,
        chatNum2: 0,
        errCount: 0,
        entryName:'',
      }
    },
    components: {
      UpdatePassword
    },
    computed: {
      navbarLayoutType: {
        get() {
          return this.$store.state.common.navbarLayoutType
        }
      },
      sidebarFold: {
        get() {
          return this.$store.state.common.sidebarFold
        },
        set(val) {
          this.$store.commit('common/updateSidebarFold', val)
        }
      },
      mainTabs: {
        get() {
          return this.$store.state.common.mainTabs
        },
        set(val) {
          this.$store.commit('common/updateMainTabs', val)
        }
      },
      userName: {
        get() {
          return this.$store.state.user.name
        }
      },
      pageTitle() {
        const route = this.$route
        if (route && route.meta && route.meta.title) {
          return route.meta.title
        }
        const tab = this.mainTabs.find(item => item.name === this.$store.state.common.mainTabsActiveName)
        return tab ? tab.title : '管理系统'
      },
      contextBadge() {
        const title = this.pageTitle
        if (!title || title === '首页') return '数据中心'
        if (title.indexOf('列表') >= 0 || title.indexOf('中心') >= 0) return '列表'
        if (title.indexOf('详情') >= 0 || title.indexOf('修改') >= 0 || title.indexOf('添加') >= 0) return '表单'
        return '页面'
      }
    },
    methods: {
      updatePasswordHandle() {
        this.updatePassowrdVisible = true
        this.$nextTick(() => {
          this.$refs.updatePassowrd.init()
        })
      },
      logoutHandle() {
        this.$confirm(`确定进行[退出]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$cookie.set('token', '')
          this.$cookie.set('userId', '')
          this.delAllCookie()
          clearInterval(this.chatNum2)
          clearInterval(this.chatNum)
          this.$router.push({
            name: 'login'
          })
        }).catch(() => {})
      },
      delAllCookie() {
        var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
        if (keys) {
          for (var i = keys.length; i--;) {
            document.cookie =
              keys[i] + "=0;path=/;expires=" + new Date(0).toUTCString();
            document.cookie =
              keys[i] +
              "=0;path=/;domain=" +
              document.domain +
              ";expires=" +
              new Date(0).toUTCString();
            document.cookie =
              keys[i] +
              "=0;path=/;expires=" +
              new Date(0).toUTCString();
          }
        }
      },
      userSelect() {
        let userId = this.$cookie.get('userId')
        this.$http({
          url: this.$http.adornUrl2(`/admin/${userId}`),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          let returnData = data.data;
          if (returnData == null) {
            this.$notify({
              title: '提示',
              duration: 1800,
              message: '用户信息失效，退出登录',
              type: 'warning'
            });
            this.$cookie.set('token', '')
            this.$cookie.set('userId', '')
            sessionStorage.clear()
            this.$router.push({
              path: '/login'
            });
          } else {
            this.username = returnData.phone
          }
        })
      },
      chatSelect() {
        this.$http({
          url: this.$http.adornUrl2('/chat/count'),
          method: 'get',
          params: this.$http.adornParams({
            'storeId': this.storeId
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.chatNum = returnData
          if (this.chatNum == 0) {
            this.isshow = false
          } else {
            this.isshow = true
          }
        })
      },
      chatSelect2() {
        this.$http({
          url: this.$http.adornUrl2('/chat/count'),
          method: 'get',
          params: this.$http.adornParams({
            'storeId': this.storeId
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          if (data.data > 0) {
            if(this.errCount!=data.data){
              this.$notify.info({
                title: '消息',
                dangerouslyUseHTMLString: true,
                message: '您有' + data.data + '条未读消息。',
                offset: 80,
                duration: 2500,
                type: "warning"
              });
              this.chatNum = data.data
              this.errCount = data.data
            }
          }else{
            this.chatNum = 0
            this.errCount = 0
          }
        })
      },
      newsSelect() {
        var _this = this
        _this.$http({
          url: _this.$http.adornUrl2(`/orders/remindOrder`),
          method: 'get',
          params: _this.$http.adornParams({})
        }).then(({
          data
        }) => {
          if (data.data > 0) {
            _this.$notify.info({
              title: '消息',
              dangerouslyUseHTMLString: true,
              message: '有' + data.data + '件商品提醒发货，请前往订单中心待发货订单页面处理。',
              offset: 80,
              duration: 0,
              type: "warning",
              onClose() {
                _this.defineCallBack();
              }
            });
            _this.chatNum2 = data.data
          }
          if (data.data == 0) {
            _this.chatNum2 = 0
          }

        })
      },
      defineCallBack() {
        this.$router.replace({
          name: 'order'
        })
      },
      aplayAudio() {
        document.getElementById('audio').play()
      },
      startPlay() {
        document.getElementById('audio1').play()
      },
    },
    mounted() {
      this.entryName = entryName
      this.userSelect()
      this.chatSelect()
      this.chatSelect2()
    },
    created() {
      this.newsSelect()
    },
    beforeDestroy() {
      clearInterval(this.intervalId)
      clearInterval(this.chatNum)
    },
    watch: {
      'chatNum': function() {
        if (this.chatNum > 0) {
          this.aplayAudio()
        }
      },
      'chatNum2': function() {
        if (this.chatNum2 > 0) {
          this.startPlay()
        }
      },
    }
  }
</script>
<style scoped="scoped">
  .left-number {
    position: absolute;
    top: 0;
    right: 20px;
    width: 18px;
    height: 18px;
    line-height: 18px;
    border-radius: 50%;
    background: #ff4949;
    color: #ffffff;
    text-align: center;
    font-size: 10px;
    font-family: PingFang SC, PingFang SC-Regular;
    font-weight: 400;
    margin-top: 5px;
  }
</style>
