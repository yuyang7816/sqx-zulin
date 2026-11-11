<template>
  <aside class="site-sidebar site-sidebar--nf" :class="'site-sidebar--' + sidebarLayoutSkin">
    <div class="site-sidebar__inner">
      <el-menu
        ref="sidebarMenu"
        :default-active="menuActiveName"
        :collapse="sidebarFold"
        :collapseTransition="false"
        class="site-sidebar__menu">
       <!-- <el-menu-item index="home" @click="$router.push({ name: 'home' })" v-if="state !== '4'">
          <icon-svg name="shuju" class="site-sidebar__menu-icon"></icon-svg>
          <span slot="title">数据中心</span>
        </el-menu-item>
		<el-menu-item  index="order" @click="$router.push({ name: 'order' })" v-if="state !== '4'">
			<icon-svg name="ordercenter" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">订单中心</span>
		</el-menu-item>
		<el-menu-item index="userList" @click="$router.push({ name: 'userList' })" v-if="state !== '4'">
			<icon-svg name="yonghul" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">用户中心</span>
		</el-menu-item>
		<el-menu-item index="financeList" @click="$router.push({ name: 'financeList' })">
			<icon-svg name="caiwu" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">财务中心</span>
		</el-menu-item>
		<el-menu-item index="message" @click="$router.push({ name: 'message' })" v-if="state !== '4'">
			<icon-svg name="xiaoxi" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">消息中心</span>
		</el-menu-item>
		<el-menu-item index="wechatList" @click="$router.push({ name: 'wechatList' })" v-if="state !== '4'">
			<icon-svg name="winxin" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">微信配置</span>
		</el-menu-item>
		<el-menu-item index="storeList" @click="$router.push({ name: 'storeList' })" v-if="state == '1'|| state == '2'">
			<icon-svg name="shangpin" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">商城配置</span>
		</el-menu-item> -->
		<!-- <el-submenu index="materials"  v-if="state !== '4'">
		  <template slot="title">
		    <icon-svg name="zonghe" class="site-sidebar__menu-icon"></icon-svg>
		    <span>营销中心</span>
		  </template>
		  <el-menu-item index="materialsList" @click="$router.push({ name: 'materialsList' })">
		    <icon-svg name="xinxi" class="site-sidebar__menu-icon"></icon-svg>
		    <span slot="title">好物圈</span>
		  </el-menu-item>
			<el-menu-item index="taskList" @click="$router.push({ name: 'taskList' })">
				<icon-svg name="renwu" class="site-sidebar__menu-icon"></icon-svg>
				<span slot="title">任务中心</span>
			</el-menu-item>
		  <el-menu-item index="mission" @click="$router.push({ name: 'mission' })">
		  	<icon-svg name="pingtai" class="site-sidebar__menu-icon"></icon-svg>
		  	<span slot="title">系统任务</span>
		  </el-menu-item>
		</el-submenu> -->
		<!-- <el-submenu index="selfShop"  v-if="state !== '4'">
		  <template slot="title">
		    <icon-svg name="shangcheng" class="site-sidebar__menu-icon"></icon-svg>
		    <span>自营商城</span>
		  </template>
		  <el-menu-item index="classifyAdmin" @click="$router.push({ name: 'classifyAdmin' })">
		  	<icon-svg name="zyfl" class="site-sidebar__menu-icon"></icon-svg>
		  	<span slot="title">自营分类</span>
		  </el-menu-item>
		<el-menu-item index="orderAdmin" @click="$router.push({ name: 'orderAdmin' })">
			<icon-svg name="ordercenter" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">订单管理</span>
		</el-menu-item>
		  <el-menu-item index="addressAamin" @click="$router.push({ name: 'addressAamin' })">
		  	<icon-svg name="dizhi" class="site-sidebar__menu-icon"></icon-svg>
		  	<span slot="title">地址管理</span>
		  </el-menu-item>
		</el-submenu> -->
		<!-- <el-menu-item index="authorityList" @click="$router.push({ name: 'authorityList' })"  v-if="state == '1'|| state == '2'">
			<icon-svg name="quanxian" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">权限中心</span>
		</el-menu-item> -->
		<!-- <el-menu-item index="allocationList" @click="$router.push({ name: 'allocationList' })"  v-if="state == '1'|| state == '2'">
			<icon-svg name="system" class="site-sidebar__menu-icon"></icon-svg>
			<span slot="title">系统配置</span>
		</el-menu-item> -->
       <sub-menu
          v-for="menu in menuList"
          :key="menu.menuId"
          :menu="menu"
          :dynamicMenuRoutes="dynamicMenuRoutes">
        </sub-menu>
      </el-menu>
    </div>
  </aside>
</template>

<script>
  import SubMenu from './main-sidebar-sub-menu'
  import { isURL } from '@/utils/validate'
  export default {
    data () {
      return {
        dynamicMenuRoutes: [],
		state:''
      }
    },
    components: {
      SubMenu
    },
    computed: {
      sidebarLayoutSkin: {
        get () { return this.$store.state.common.sidebarLayoutSkin }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      menuList: {
        get () { return this.$store.state.common.menuList },
        set (val) { this.$store.commit('common/updateMenuList', val) }
      },
      menuActiveName: {
        get () { return this.$store.state.common.menuActiveName },
        set (val) { this.$store.commit('common/updateMenuActiveName', val) }
      },
      mainTabs: {
        get () { return this.$store.state.common.mainTabs },
        set (val) { this.$store.commit('common/updateMainTabs', val) }
      },
      mainTabsActiveName: {
        get () { return this.$store.state.common.mainTabsActiveName },
        set (val) { this.$store.commit('common/updateMainTabsActiveName', val) }
      }
    },
    watch: {
      menuActiveName (val) {
        this.syncMenuActive(val)
      },
      $route: 'routeHandle'
    },
    created () {
      this.menuList = JSON.parse(sessionStorage.getItem('menuList') || '[]')
      this.dynamicMenuRoutes = JSON.parse(sessionStorage.getItem('dynamicMenuRoutes') || '[]')
      this.routeHandle(this.$route)
    },
    methods: {
		// 同步菜单选中态（default-active 在 Element UI 中仅初始化生效，需手动设置 activeIndex）
		syncMenuActive (activeName) {
		  if (!activeName) return
		  this.$nextTick(() => {
		    const menu = this.$refs.sidebarMenu
		    if (!menu) return
		    const applyActive = () => {
		      if (menu.items[activeName]) {
		        menu.activeIndex = activeName
		        menu.initOpenedMenu()
		        return true
		      }
		      return false
		    }
		    if (!applyActive()) {
		      this.$nextTick(applyActive)
		    }
		  })
		},
		// 获取用户权限
		userSelect () {
		  let userId = this.$cookie.get('userId')
		 this.$http({
		 	url: this.$http.adornUrl2(`/admin/${userId}`),
		 	method: 'get',
		 	params: this.$http.adornParams({
		 	})
		 }).then(({data}) => {
		 	let returnData = data.data;
		 	this.state = returnData.state
			if(this.state == '4'){
				this.$router.replace({ name: 'financeList' })
			}
		 })
		},
      // 路由操作
      routeHandle (route) {
        if (route.meta.isTab) {
          // tab选中, 不存在先添加
          var tab = this.mainTabs.filter(item => item.name === route.name)[0]
          if (!tab) {
            if (route.meta.isDynamic) {
              if (!this.dynamicMenuRoutes.length) {
                this.dynamicMenuRoutes = JSON.parse(sessionStorage.getItem('dynamicMenuRoutes') || '[]')
              }
              route = this.dynamicMenuRoutes.filter(item => item.name === route.name)[0]
              if (!route) {
                return console.error('未能找到可用标签页!')
              }
            }
            tab = {
              menuId: route.meta.menuId || route.name,
              name: route.name,
              title: route.meta.title,
              type: isURL(route.meta.iframeUrl) ? 'iframe' : 'module',
              iframeUrl: route.meta.iframeUrl || '',
              params: route.params,
              query: route.query
            }
            this.mainTabs = this.mainTabs.concat(tab)
          }
          this.menuActiveName = tab.menuId + ''
          this.mainTabsActiveName = tab.name
        }
      }
    },
	mounted() {
		this.userSelect()
		this.syncMenuActive(this.menuActiveName)
	}
  }
</script>
