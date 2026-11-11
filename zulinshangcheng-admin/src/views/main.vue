<template>
  <div
    class="site-wrapper nf-shell-root"
    :class="{ 'site-sidebar--fold': sidebarFold }"
    v-loading.fullscreen.lock="loading"
    element-loading-text="正在加载管理后台…"
    element-loading-background="rgba(244, 246, 250, 0.92)"
    element-loading-custom-class="nf-shell-loading">
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div class="nf-shell-root__content site-content__wrapper" :style="{ 'min-height': documentClientHeight + 'px' }">
        <main-content v-if="!$store.state.common.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
  import MainNavbar from './main-navbar'
  import MainSidebar from './main-sidebar'
  import MainContent from './main-content'
  export default {
    provide () {
      return {
        // 刷新
        refresh () {
          this.$store.commit('common/updateContentIsNeedRefresh', true)
          this.$nextTick(() => {
            this.$store.commit('common/updateContentIsNeedRefresh', false)
          })
        }
      }
    },
    data () {
      return {
        loading: true
      }
    },
    components: {
      MainNavbar,
      MainSidebar,
      MainContent
    },
    computed: {
      documentClientHeight: {
        get () { return this.$store.state.common.documentClientHeight },
        set (val) { this.$store.commit('common/updateDocumentClientHeight', val) }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      userId: {
        get () { return this.$store.state.user.id },
        set (val) { this.$store.commit('user/updateId', val) }
      },
      userName: {
        get () { return this.$store.state.user.name },
        set (val) { this.$store.commit('user/updateName', val) }
      }
    },
    created () {
      this.getUserInfo()
    },
    mounted () {
      this.resetDocumentClientHeight()
    },
    methods: {
      // 重置窗口可视高度
      resetDocumentClientHeight () {
        this.documentClientHeight = document.documentElement['clientHeight']
        window.onresize = () => {
          this.documentClientHeight = document.documentElement['clientHeight']
        }
      },
      // 获取当前管理员信息
      getUserInfo () {
		  let userId = this.$cookie.get('userId')
        this.$http({
          url: this.$http.adornUrl2(`/admin/${userId}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.status === 0) {
            this.loading = false
						let returnData = data.data;
            this.userId = returnData.id
            this.userName = returnData.userName
          }else{
			this.$router.replace({ name: 'login' })  
		  }
		    
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
.nf-shell-root.site-wrapper {
  position: relative;
  min-height: 100vh;
  overflow-x: hidden;
  background: var(--nf-page-bg, #f4f6fa);
}

.nf-shell-root__content.site-content__wrapper {
  overflow-x: hidden;
  transition: margin-left 0.3s;
}

/* 全屏加载 · Brand 色 spinner 与文案 */
::v-deep .nf-shell-loading {
  .el-loading-spinner {
    margin-top: -28px;
  }
  .circular {
    width: 42px;
    height: 42px;
  }
  .path {
    stroke: var(--nf-brand, #15cad2);
    stroke-width: 3;
  }
  .el-loading-text {
    margin-top: 12px;
    font-size: 14px;
    color: var(--nf-brand-deep, #109da4);
    letter-spacing: 0.5px;
  }
}
</style>
