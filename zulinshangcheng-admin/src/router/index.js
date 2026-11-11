/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import http from '@/utils/httpRequest'
import { isURL } from '@/utils/validate'
import { clearLoginInfo } from '@/utils'

Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
  { path: '/404', component: _import('common/404'), name: '404', meta: { title: '404未找到' } },
  { path: '/login', component: _import('common/login'), name: 'login', meta: { title: '登录' } },
  { path: '/merchant', component: _import('common/merchant'), name: 'merchant', meta: { title: '商家入驻' } },
  { path: '/merchantClone', component: _import('common/merchantClone'), name: 'merchantClone', meta: { title: '商家入驻' } }
]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
  path: '/',
  component: _import('main'),
  name: 'main',
  redirect: { name: 'home' },
  meta: { title: '主入口整体布局' },
  children: [
    // 通过meta对象设置路由展示方式
    // 1. isTab: 是否通过tab展示内容, true: 是, false: 否
    // 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
    // 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
    { path: '/home', component: _import('common/home'), name: 'home', meta: { title: '首页' } },
	{ path: '/userList', component: _import('user/userList'), name: 'userList', meta: { title: '用户列表',isTab: true} },
	{ path: '/allocationList', component: _import('allocation/allocationList'), name: 'allocationList', meta: { title: '配置列表',isTab: true} },
	{ path: '/order', component: _import('order/order'), name: 'order', meta: { title: '订单中心',isTab: true} },
	{ path: '/financeList', component: _import('finance/financeList'), name: 'financeList', meta: { title: '财务中心',isTab: true} },
	{ path: '/message', component: _import('message/message'), name: 'message', meta: { title: '消息中心',isTab: true} },
	{ path: '/userDetail', component: _import('user/userDetail'), name: 'userDetail', meta: { title: '用户详情',isTab: true} },
	{ path: '/userDetail1', component: _import('user/userDetail1'), name: 'userDetail1', meta: { title: '用户详情',isTab: false} },
	{ path: '/wechatList', component: _import('wechat/wechatList'), name: 'wechatList', meta: { title: '微信配置',isTab: true} },
	{ path: '/storeList', component: _import('store/storeList'), name: 'storeList', meta: { title: '商城配置',isTab: true} },
	{path: '/materialLink', component: _import('store/materialLink'), name: 'materialLink', meta: {title: '素材链接', isTab: true}},
	{ path: '/missionAdd', component: _import('mission/missionAdd'), name: 'missionAdd', meta: { title: '添加任务',isTab: false} },
	{ path: '/missionRedact', component: _import('mission/missionRedact'), name: 'missionRedact', meta: { title: '修改任务',isTab: false} },
	{ path: '/taskList', component: _import('task/taskList'), name: 'taskList', meta: { title: '任务中心',isTab: true} },
	{ path: '/mission', component: _import('mission/mission'), name: 'mission', meta: { title: '任务管理',isTab: true} },
	{ path: '/materialsList', component: _import('materials/materialsList'), name: 'materialsList', meta: { title: '文章管理',isTab: true} },
	{ path: '/classifyAdmin', component: _import('selfShop/classifyAdmin'), name: 'classifyAdmin', meta: { title: '商品分类',isTab: true} },
	{ path: '/brandShop', component: _import('selfShop/brandShop'), name: 'brandShop', meta: { title: '商品品牌',isTab: true} },
	{ path: '/shopAdmin', component: _import('selfShop/shopAdmin'), name: 'shopAdmin', meta: { title: '商品管理',isTab: true} },
	{ path: '/shopAdmin1', component: _import('selfShop/shopAdmin1'), name: 'shopAdmin1', meta: { title: '商品管理',isTab: false} },
	{ path: '/specification', component: _import('selfShop/specification'), name: 'specification', meta: { title: '商品规格',isTab: true} },
	{ path: '/shopPublish', component: _import('selfShop/shopPublish'), name: 'shopPublish', meta: { title: '发布商品',isTab: false} },
	{ path: '/shopAmend', component: _import('selfShop/shopAmend'), name: 'shopAmend', meta: { title: '修改商品',isTab: false} },
	{ path: '/discuss', component: _import('selfShop/discuss'), name: 'discuss', meta: { title: '商品评论',isTab: true} },
  { path: '/marketing', component: _import('marketing/marketing'), name: 'marketing', meta: { title: '拼团商品',isTab: true} },
	{ path: '/marketingAdd', component: _import('marketing/marketingAdd'), name: 'marketingAdd', meta: { title: '拼团商品添加',isTab: false} },
	{ path: '/marketingAmend', component: _import('marketing/marketingAmend'), name: 'marketingAmend', meta: { title: '拼团商品修改',isTab: false} },
	{ path: '/marketList', component: _import('marketing/marketList'), name: 'marketList', meta: { title: '拼团列表',isTab: true} },
  { path: '/seckill', component: _import('seckill/seckill'), name: 'seckill', meta: { title: '秒杀商品',isTab: true} },
  { path: '/seckillAdd', component: _import('seckill/seckillAdd'), name: 'seckillAdd', meta: { title: '秒杀商品添加',isTab: false} },
  { path: '/seckillAmend', component: _import('seckill/seckillAmend'), name: 'seckillAmend', meta: { title: '秒杀商品修改',isTab: false} },
  { path: '/seckillConfig', component: _import('seckill/seckillConfig'), name: 'seckillConfig', meta: { title: '秒杀配置',isTab: true} },
  { path: '/orderDetails', component: _import('selfShop/orderDetails'), name: 'orderDetails', meta: { title: '订单详情',isTab: true} },
  { path: '/virtual', component: _import('virtual/virtual'), name: 'virtual', meta: { title: '虚拟商品',isTab: true} },
  { path: '/virtualAmend', component: _import('virtual/virtualAmend'), name: 'virtualAmend', meta: { title: '兑换码列表',isTab: false} },
  { path: '/coupon', component: _import('coupon/coupon'), name: 'coupon', meta: { title: '优惠券制作',isTab: true} },
  { path: '/couponissue', component: _import('coupon/couponissue'), name: 'couponissue', meta: { title: '已发布优惠券',isTab: true} },
  { path: '/couponuser', component: _import('coupon/couponuser'), name: 'couponuser', meta: { title: '优惠券领取记录',isTab: true} },
  { path: '/lottery', component: _import('lottery/lottery'), name: 'lottery', meta: { title: '抽奖规则',isTab: true} },
  { path: '/lotteryPrize', component: _import('lottery/lotteryPrize'), name: 'lotteryPrize', meta: { title: '抽奖奖品',isTab: false} },
  { path: '/lotteryRecord', component: _import('lottery/lotteryRecord'), name: 'lotteryRecord', meta: { title: '抽奖记录',isTab: true} },
  { path: '/vueMchat', component: _import('vueMchat/vueMchat'), name: 'vueMchat', meta: { title: '聊天室',isTab: true} },
  { path: '/integralist', component: _import('integral/integralist'), name: 'integralist', meta: { title: '积分商品',isTab: true} },
  { path: '/intepublich', component: _import('integral/intepublich'), name: 'intepublich', meta: { title: '添加积分商品',isTab: false} },
  { path: '/inteAmend', component: _import('integral/inteAmend'), name: 'inteAmend', meta: { title: '修改积分商品',isTab: false} },
  
  { path: '/comment', component: _import('comment/comment'), name: 'comment', meta: { title: '评价管理',isTab: true} },
	//新增
	{ path: '/aggregate', component: _import('aggregate/aggregate'), name: 'aggregate', meta: { title: '集合商品',isTab: false} },
	{ path: '/aggregateType', component: _import('aggregate/aggregateType'), name: 'aggregateType', meta: { title: '集合分类',isTab: true} },
	// 2024.12.10 租赁 新增
	{ path: '/rentalNotice', component: _import('lease/rentalNotice'), name: 'rentalNotice', meta: { title: '租赁须知',isTab: true} },
	{ path: '/commonProblem', component: _import('lease/commonProblem'), name: 'commonProblem', meta: { title: '租赁天数',isTab: true} },
	{ path: '/accessory', component: _import('lease/accessory'), name: 'accessory', meta: { title: '商品配件',isTab: true} },
	{ path: '/productGifts', component: _import('lease/productGifts'), name: 'productGifts', meta: { title: '赠品配件',isTab: true} },
	{path: '/autonym', component: _import('autonym/autonym'), name: 'autonym', meta: {title: '实名认证', isTab: true}},
	{ path: '/labelShop', component: _import('selfShop/labelShop'), name: 'labelShop', meta: { title: '标签管理',isTab: true} },
	{path: '/memberDetails',component: _import('members/memberDetails'),name: 'memberDetails',meta: {title: '会员配置',isTab: true}},
	
	 
  ],
  beforeEnter (to, from, next) {
    let token = Vue.cookie.get('token')
    if (!token || !/\S/.test(token)) {
      clearLoginInfo()
      next({ name: 'login' })
    }
    next()
  }
}

const router = new Router({
  mode: 'history', //  hash
  scrollBehavior: () => ({ y: 0 }),
  isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
  routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
	let userId = Vue.cookie.get('userId')
  // 添加动态(菜单)路由
  // 1. 已经添加 or 全局路由, 直接访问
  // 2. 获取菜单列表, 添加并保存本地存储

  if (userId == '' || router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
    next()
  } else {

    http({
      url: http.adornUrl2(`/sys/menu/nav?userId=${userId}`),
      method: 'get',
      params: http.adornParams()
    }).then(({data}) => {
      if (data && data.status === 0) {
        fnAddDynamicMenuRoutes(data.data.menuList)
        router.options.isAddDynamicMenuRoutes = true
        sessionStorage.setItem('menuList', JSON.stringify(data.data.menuList || '[]'))
        sessionStorage.setItem('permissions', JSON.stringify(data.data.permissions || '[]'))
        next({ ...to, replace: true })
      } else {
        sessionStorage.setItem('menuList', '[]')
        sessionStorage.setItem('permissions', '[]')
        next()
      }
    }).catch((e) => {
      console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
      router.push({ name: 'login' })
    })
  }
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType (route, globalRoutes = []) {
  var temp = []
  for (var i = 0; i < globalRoutes.length; i++) {
    if (route.path === globalRoutes[i].path) {
      return 'global'
    } else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
      temp = temp.concat(globalRoutes[i].children)
    }
  }
  return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
  var temp = []
  for (var i = 0; i < menuList.length; i++) {
    if (menuList[i].list && menuList[i].list.length >= 1) {
      temp = temp.concat(menuList[i].list)
    } else if (menuList[i].url && /\S/.test(menuList[i].url)) {
      menuList[i].url = menuList[i].url.replace(/^\//, '')
      var route = {
        path: menuList[i].url.replace('/', '-'),
        component: null,
        name: menuList[i].url.replace('/', '-'),
        meta: {
          menuId: menuList[i].menuId,
          title: menuList[i].name,
          isDynamic: true,
          isTab: true,
          iframeUrl: ''
        }
      }
      // url以http[s]://开头, 通过iframe展示
      if (isURL(menuList[i].url)) {
        route['path'] = `i-${menuList[i].menuId}`
        route['name'] = `i-${menuList[i].menuId}`
        route['meta']['iframeUrl'] = menuList[i].url
      } else {
        try {
          route['component'] = _import(`modules/${menuList[i].url}`) || null
        } catch (e) {}
      }
      routes.push(route)
    }
  }
  if (temp.length >= 1) {
    fnAddDynamicMenuRoutes(temp, routes)
  } else {
    mainRoutes.name = 'main-dynamic'
    mainRoutes.children = routes
    router.addRoutes([
      mainRoutes,
      { path: '*', redirect: { name: '404' } }
    ])
    sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
    console.log('\n')
    console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
    console.log(mainRoutes.children)
    console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
  }
}

export { fnAddDynamicMenuRoutes }
export default router
