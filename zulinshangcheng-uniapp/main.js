import Vue from 'vue'
import App from './App'

import HttpRequest from './common/httpRequest'
import HttpCache from './common/cache'
import queue from './common/queue'
// import store from './store/store.js'
import {
	navigateTo,
	redirectTo,
	switchTab,
	reLaunch
} from './utils/jump';
Vue.prototype.navigate = navigateTo;
Vue.prototype.redirectTo = redirectTo;
Vue.prototype.switchTab = switchTab;
Vue.prototype.reLaunch = reLaunch;
Vue.config.productionTip = false
Vue.prototype.$Request = HttpRequest;
Vue.prototype.$queue = queue;

Vue.prototype.$Sysconf = HttpRequest.config;
Vue.prototype.$SysCache = HttpCache;


App.mpType = 'app'

// 引入全局uView
import uView from "uview-ui";
Vue.use(uView);

const app = new Vue({
	// store,//引入vuex实例
	...App
})

app.$mount()