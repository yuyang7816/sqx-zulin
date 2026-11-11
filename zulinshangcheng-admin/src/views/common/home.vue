<template>
	<div class="home-page">
		<!-- hero + 筛选白卡 -->
		<div class="home-page__top">
			<div class="nf-page-hero home-page__hero">
				<span class="nf-page-hero__badge">数据中心</span>
				<h2 class="nf-page-hero__title">运营数据概览</h2>
				<p class="nf-page-hero__desc">实时汇总用户、订单与收入核心指标，支持按天/月/年维度筛选</p>
			</div>
			<div class="nf-filter-card home-page__filter">
				<div class="nf-filter-card__head">
					<i class="el-icon-search"></i>
					<span>筛选条件</span>
				</div>
				<div class="home-page__filter-body">
					<el-select v-model="flag" style="width:100%;" @change="orderfenxi">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
					<el-date-picker style="width:100%;" v-model="info.stockDate" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间" @change="animeOrder">
					</el-date-picker>
				</div>
			</div>
		</div>

		<!-- KPI 四列 · 用户指标 -->
		<div class="home-page__kpi-row">
			<div class="home-kpi-card">
				<div class="home-kpi-card__icon"><i class="el-icon-user"></i></div>
				<div class="home-kpi-card__label">总用户数</div>
				<div class="home-kpi-card__value"><span>{{tableData.totalUsers ? tableData.totalUsers : 0}}</span><span class="home-kpi-card__unit">人</span></div>
			</div>
			<div class="home-kpi-card">
				<div class="home-kpi-card__icon"><i class="el-icon-plus"></i></div>
				<div class="home-kpi-card__label">今日新增</div>
				<div class="home-kpi-card__value"><span>{{tableData.newToday ? tableData.newToday : 0}}</span><span class="home-kpi-card__unit">人</span></div>
			</div>
			<div class="home-kpi-card">
				<div class="home-kpi-card__icon"><i class="el-icon-date"></i></div>
				<div class="home-kpi-card__label">本月新增</div>
				<div class="home-kpi-card__value"><span>{{tableData.newMonth ? tableData.newMonth : 0}}</span><span class="home-kpi-card__unit">人</span></div>
			</div>
			<div class="home-kpi-card">
				<div class="home-kpi-card__icon"><i class="el-icon-tickets"></i></div>
				<div class="home-kpi-card__label">本年新增</div>
				<div class="home-kpi-card__value"><span>{{tableData.newYear ? tableData.newYear : 0}}</span><span class="home-kpi-card__unit">人</span></div>
			</div>
		</div>

		<!-- KPI 四列 · 收入指标 -->
		<div class="home-page__kpi-row">
			<div class="home-kpi-card">
				<div class="home-kpi-card__icon"><i class="el-icon-money"></i></div>
				<div class="home-kpi-card__label">今日收入</div>
				<div class="home-kpi-card__value"><span>{{tableData.todayRevenue ? tableData.todayRevenue : 0}}</span><span class="home-kpi-card__unit">元</span></div>
			</div>
			<div class="home-kpi-card">
				<div class="home-kpi-card__icon"><i class="el-icon-s-order"></i></div>
				<div class="home-kpi-card__label">本月收入</div>
				<div class="home-kpi-card__value"><span>{{tableData.monthRevenue ? tableData.monthRevenue : 0}}</span><span class="home-kpi-card__unit">元</span></div>
			</div>
			<div class="home-kpi-card">
				<div class="home-kpi-card__icon"><i class="el-icon-s-data"></i></div>
				<div class="home-kpi-card__label">今年收入</div>
				<div class="home-kpi-card__value"><span>{{tableData.yearRevenue ? tableData.yearRevenue : 0}}</span><span class="home-kpi-card__unit">元</span></div>
			</div>
			<div class="home-kpi-card">
				<div class="home-kpi-card__icon"><i class="el-icon-s-finance"></i></div>
				<div class="home-kpi-card__label">总收入</div>
				<div class="home-kpi-card__value"><span>{{tableData.totalRevenue ? tableData.totalRevenue : 0}}</span><span class="home-kpi-card__unit">元</span></div>
			</div>
		</div>

		<!-- 明细 tabs · stat-tile 网格 -->
		<div class="home-page__detail-card">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="home-page__tabs">

				<el-tab-pane label="用户分析" name="third">
					<div class="home-stat-grid">
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">总用户统计</div>
							<div class="home-stat-tile__value"><span>{{userStat.totalNumber ? userStat.totalNumber : 0}}</span><span class="home-stat-tile__unit">人</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">公众号用户</div>
							<div class="home-stat-tile__value"><span>{{userStat.publicUserNumber ? userStat.publicUserNumber : 0}}</span><span class="home-stat-tile__unit">人</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">小程序用户</div>
							<div class="home-stat-tile__value"><span>{{userStat.wxUserNumber ? userStat.wxUserNumber : 0}}</span><span class="home-stat-tile__unit">人</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">APP用户</div>
							<div class="home-stat-tile__value"><span>{{userStat.appUserNumber ? userStat.appUserNumber : 0}}</span><span class="home-stat-tile__unit">人</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">H5用户</div>
							<div class="home-stat-tile__value"><span>{{userStat.h5UserNumber ? userStat.h5UserNumber : 0}}</span><span class="home-stat-tile__unit">人</span></div>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="支付统计" name="sixth">
					<div class="home-stat-grid">
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">支付总金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.allMoney ? chongzhiData.allMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">app微信支付金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.appMoney ? chongzhiData.appMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">微信公众号支付金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.wxRwmMoney ? chongzhiData.wxRwmMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">微信小程序支付金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.wxAppMoney ? chongzhiData.wxAppMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">app支付宝支付金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.zfbMoney ? chongzhiData.zfbMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">支付宝H5支付金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.zfbH5Money ? chongzhiData.zfbH5Money : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">零钱支付金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.lqMoney ? chongzhiData.lqMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">系统支付金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.sysMoney ? chongzhiData.sysMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">已退款金额</div>
							<div class="home-stat-tile__value"><span>{{chongzhiData.refundMoney ? chongzhiData.refundMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="待审核统计" name="seven">
					<div class="home-stat-grid">
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">认证待审核</div>
							<div class="home-stat-tile__value home-stat-tile__value--alert">
								<span class="home-stat-tile__link" @click="btnDsh(1)">{{chatNumRz ? chatNumRz : 0}}</span><span class="home-stat-tile__unit">个</span>
							</div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">商品待审核</div>
							<div class="home-stat-tile__value home-stat-tile__value--alert">
								<span class="home-stat-tile__link" @click="btnDsh(2)">{{chatNumFw ? chatNumFw : 0}}</span><span class="home-stat-tile__unit">个</span>
							</div>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="订单统计" name="first">
					<div class="home-stat-grid">
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">普通订单总收益</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.totalRevenue ? rechgeData.totalRevenue : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">普通订单总数量</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.ordinaryCount ? rechgeData.ordinaryCount : 0}}</span><span class="home-stat-tile__unit">单</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">回收订单总收益</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.retrieveRevenue ? rechgeData.retrieveRevenue : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">回收订单数量</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.retrieveCount ? rechgeData.retrieveCount : 0}}</span><span class="home-stat-tile__unit">单</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">普通订单待支付单数</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.daifukuanCount ? rechgeData.daifukuanCount : 0}}</span><span class="home-stat-tile__unit">单</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">普通订单已支付单数</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.yizhifuCount ? rechgeData.yizhifuCount : 0}}</span><span class="home-stat-tile__unit">单</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">普通订单已完成单数</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.yiwanchengCount ? rechgeData.yiwanchengCount : 0}}</span><span class="home-stat-tile__unit">单</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">待回收订单总数</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.retrieveDaihuishou ? rechgeData.retrieveDaihuishou : 0}}</span><span class="home-stat-tile__unit">单</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">已回收订单总数</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.retrieveYiwancheng ? rechgeData.retrieveYiwancheng : 0}}</span><span class="home-stat-tile__unit">单</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">待收货回收订单总数</div>
							<div class="home-stat-tile__value"><span>{{rechgeData.retrieveDaishouhuo ? rechgeData.retrieveDaishouhuo : 0}}</span><span class="home-stat-tile__unit">单</span></div>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="收入分析" name="fourth">
					<div class="home-stat-grid">
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">平台收益</div>
							<div class="home-stat-tile__value"><span>{{tableData2.platformMoney ? tableData2.platformMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">一级佣金收益</div>
							<div class="home-stat-tile__value"><span>{{tableData2.firstMoney ? tableData2.firstMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
						<div class="home-stat-tile">
							<div class="home-stat-tile__label">二级佣金收益</div>
							<div class="home-stat-tile__value"><span>{{tableData2.secondMoney ? tableData2.secondMoney : 0}}</span><span class="home-stat-tile__unit">元</span></div>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 双列明细 · 图表 -->
		<div class="home-page__charts">
			<div class="home-chart-card">
				<div class="home-chart-card__header">
					<span>订单折线图</span>
					<el-date-picker v-model="selectYear" :picker-options="pickerOptions" type="daterange"
						range-separator="至" start-placeholder="开始日期" @change="selectYears"
						end-placeholder="结束日期" size="small">
					</el-date-picker>
				</div>
				<div class="home-chart-card__body">
					<div id="Chart1" class="Chart1" v-loading="tableDataLoading1"></div>
				</div>
			</div>
			<div class="home-chart-card">
				<div class="home-chart-card__header">
					<span>用户增长折线图</span>
					<el-date-picker v-model="selectDayDate" :picker-options="pickerOptions" type="daterange"
						range-separator="至" start-placeholder="开始日期" @change="selectTime"
						end-placeholder="结束日期" size="small">
					</el-date-picker>
				</div>
				<div class="home-chart-card__body">
					<div id="Chart2" class="Chart1" v-loading="tableDataLoading2"></div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	//按需加载echarts
	import * as echarts from 'echarts/core';
	import {
		ToolboxComponent,
		TooltipComponent,
		GridComponent,
		LegendComponent
	} from 'echarts/components';
	import {
		LineChart
	} from 'echarts/charts';
	import {
		UniversalTransition
	} from 'echarts/features';
	import {
		CanvasRenderer
	} from 'echarts/renderers';
	export default {
		data() {
			return {
				limit: 5,
				page: 1,
				tableData: {},
				tableData2: {
					list: [],
					totalCount: 0
				},
				colonelData: [],
				commodityData: [],
				userjfData: [],
				incomeData: [],
				activeName: 'third',
				tableDataLoading: true,
				flag: 1,
				usersumData: {},
				taskStat: {},
				data: '',
				time: '',
				platform: '',
				type: '',
				balance: -1,
				flag: 1,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				rechgeData: {},
				chongzhiData: {},
				userStat: {},

				chatNumRz: 0,
				chatNumDp: 0,
				chatNumFw: 0,

				tableDataLoading1: true,
				tableDataLoading2: true,
				orderEcharts: {}, //订单折线图
				increaseEcharts: {}, //用户增长折线图
				selectDayDate: '',
				pickerOptions: {
					onPick: ({
						maxDate,
						minDate
					}) => {
						// 把选择的第一个日期赋值给一个变量。
						this.choiceDate = minDate.getTime()
						// 如何你选择了两个日期了，就把那个变量置空
						if (maxDate) this.choiceDate = ''
					},
					disabledDate: time => {
						// 如何选择了一个日期
						if (this.choiceDate) {
							// 7天的时间戳
							const one = 6 * 24 * 3600 * 1000
							// 当前日期 - one = 7天之前
							const minTime = this.choiceDate - one
							// 当前日期 + one = 7天之后
							const maxTime = this.choiceDate + one
							return (
								time.getTime() < minTime ||
								time.getTime() > maxTime
							)
						} else {
							return false
						}
					}
				},
				bodyStyle: {
					padding: '0'
				},
				selectYear: '',
				qiehuan_type: 1,
				lirunStat: {},
			}
		},
		mounted() {
			this._getData() //用户统计
			this.userData()
			setInterval(() => {
				if (this.$cookie.get('numberFw')) {
					this.chatNumFw = this.$cookie.get('numberFw')
				}
				if (this.$cookie.get('numberDp')) {
					this.chatNumDp = this.$cookie.get('numberDp')
				}
				if (this.$cookie.get('numberRz')) {
					this.chatNumRz = this.$cookie.get('numberRz')
				}
			}, 1000)
			this.getUserOrderEcharts();
			this.getUserIncreaseEcharts()
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			updates1(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId1
					}
				})
			},
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId2
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val;
				this.colonel();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.colonel();
			},
			// 用户分析选择日期
			animeDat() {
				this._getData()
			},
			// 订单分析选择日期
			animeOrder() {
				this.taskData()
				this.colonel()
				this.userData()
				this.rechSelect()
			},
			// 订单分析年月日
			orderfenxi(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.taskData()
					this.colonel()
					this.userData()
					this.rechSelect()
				}
			},
			// Tabs点击事件
			handleClick(tab, event) {
				if (tab._props.label == '订单统计') {
					this.taskData()
				}
				if (tab._props.label == '收入分析') {
					this.colonel()
				}
				if (tab._props.label == '用户分析') {
					this.info.stockDate = this.getNowTime()
					this.userData()
				}
				if (tab._props.label == '支付统计') {
					this.info.stockDate = this.getNowTime()
					this.rechSelect()
				}
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 用户统计
			_getData() {
				this.$http({
					url: this.$http.adornUrl2('/user/homeMessage'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			//订单统计
			taskData() {
				this.$http({
					url: this.$http.adornUrl2('/user/orderMessageCount'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.rechgeData = returnData.data
				})
			},
			// 购买商品分析
			colonel() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/user/incomeStatistics'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						'date': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						let returnData = data.data.data
						this.tableData2 = returnData
					}
				})
			},
			// 用户分析
			userData(platform, type) {
				this.$http({
					url: this.$http.adornUrl2('/user/userMessage'),
					method: 'get',
					params: this.$http.adornParams({
						'data': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.userStat = returnData
				})
			},
			// 充值统计
			rechSelect() {
				this.$http({
					url: this.$http.adornUrl2('/user/payMessage'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info.stockDate,
						'type': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.chongzhiData = returnData
					}
				})
			},
			// 待审核跳转
			btnDsh(index) {
				if (index == 1) {
					this.$router.replace({
						name: 'authentication',
						query: {
							status: 0
						}
					})
				}
				if (index == 2) {
					this.$router.replace({
						name: 'shopManage',
						query: {
							status: 0
						}
					})
				}
				if (index == 3) {
					this.$router.replace({
						name: 'shopManage',
						query: {
							status: 1
						}
					})
				}
			},
			//订单折线图
			getUserOrderEcharts(startTime, endTime) {
				this.tableDataLoading1 = true
				var times = this.info.stockDate.slice(0, 10)
				if (startTime) {
					var startTime = startTime
					var endTime = endTime
				} else {
					// var now = Date.parse(new Date())
					// var oldD = new Date(now)
					// // var year = oldD.getFullYear(); //得到年份
					// var year = this.info2.stockDate
					// // this.selectYear = year
					var now = Date.parse(new Date())
					// var choiceDate = (new Date(now)).getTime() / 1000;
					var choiceDate = Date.parse(new Date())
					var one = 6 * 24 * 3600 * 1000
					// 当前日期 - one = 7天之前
					var minTime = choiceDate - one
					var oldD = new Date(minTime)
					var year = oldD.getFullYear(); //得到年份
					var month = oldD.getMonth(); //得到月份
					var date = oldD.getDate(); //得到日期
					var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
					var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
					var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					var defaultDate = `${year}-${month}-${date}`;

					var startTime = defaultDate
					var endTime = times
					this.selectYear = [startTime, endTime]
				}
				this.$http({
					url: this.$http.adornUrl2('/admin/orders/selectOrdersCountStatisticsByYear'),
					method: 'get',
					params: this.$http.adornParams({
						// 'time': year ? year : '',
						'startTime': startTime ? startTime : '',
						'endTime': endTime ? endTime : ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.orderEcharts = data.data
						//使用数据初始化图表
						this.setEcharts()
						this.tableDataLoading1 = false
					}
					// this.dataInfo = data.data
				})
			},
			//用户增长折线图
			getUserIncreaseEcharts(startTime, endTime) {
				this.tableDataLoading2 = true
				var times = this.info.stockDate.slice(0, 10)
				console.log('times', times)
				if (startTime) {
					var startTime = startTime
					var endTime = endTime
				} else {
					var now = Date.parse(new Date())
					// var choiceDate = (new Date(now)).getTime() / 1000;
					var choiceDate = Date.parse(new Date())
					var one = 6 * 24 * 3600 * 1000
					// 当前日期 - one = 7天之前
					var minTime = choiceDate - one
					var oldD = new Date(minTime)
					var year = oldD.getFullYear(); //得到年份
					var month = oldD.getMonth(); //得到月份
					var date = oldD.getDate(); //得到日期
					var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
					var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
					var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					var defaultDate = `${year}-${month}-${date}`;

					var startTime = defaultDate
					var endTime = times
					this.selectDayDate = [startTime, endTime]
				}
				this.$http({
					url: this.$http.adornUrl2('/user/selectUserCountStatisticsByTime'),
					method: 'get',
					params: this.$http.adornParams({
						'startTime': startTime ? startTime : '',
						'endTime': endTime ? endTime : ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.increaseEcharts = data.data
						//使用数据初始化图表
						this.setEcharts2()
						this.tableDataLoading2 = false
					}
					// this.dataInfo = data.data
				})
			},
			//使用数据初始化图表
			setEcharts() {
				echarts.use([
					ToolboxComponent,
					TooltipComponent,
					GridComponent,
					LegendComponent,
					LineChart,
					CanvasRenderer,
					UniversalTransition
				]);

				var chartDom = document.getElementById('Chart1');
				var myChart = echarts.init(chartDom);
				var option;

				option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['订单总量', '普通商品单量', '回收商品单量'],
						top: '5%'
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					toolbox: {
						top: '3%',
						feature: {
							saveAsImage: {},

						}
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.orderEcharts.year
					},
					yAxis: {
						type: 'value'
					},
					series: [{
							name: '订单总量',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.allOrdersCounts
						},
						{
							name: '普通商品单量',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.ordersCountList
						},
						{
							name: '回收商品单量',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.retrieveCountList
						},
					]
				};

				option && myChart.setOption(option);
			},
			//使用数据初始化图表
			setEcharts2() {
				echarts.use([
					ToolboxComponent,
					TooltipComponent,
					GridComponent,
					LegendComponent,
					LineChart,
					CanvasRenderer,
					UniversalTransition
				]);
				var chartDom = document.getElementById('Chart2');
				var myChart = echarts.init(chartDom);
				var option;

				option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['用户增长量'],
						top: '5%'
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					toolbox: {
						top: '3%',
						feature: {
							saveAsImage: {},
						}
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.increaseEcharts.year
					},
					yAxis: {
						type: 'value'
					},
					series: [{
						name: '用户增长量',
						type: 'line',
						stack: 'Total',
						data: this.increaseEcharts.userCountList
					}, ]
				};
				option && myChart.setOption(option);
			},
			//根据年份筛选
			selectYears(e) {
				console.log(e, '`````````', this.info.stockDate)
				if (e == null) {
					this.getUserOrderEcharts()
				} else {
					// var now = new Date();
					// // var year = now.getFullYear(); //得到年份
					// var year = this.info2.stockDate
					// let startTime = (new Date(e)).getTime() / 1000;
					// this.getUserOrderEcharts(year)
					var now = new Date(e[0]);
					var year = now.getFullYear(); //得到年份
					var month = now.getMonth(); //得到月份
					var date = now.getDate(); //得到日期
					var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
					var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
					var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
					var defaultDate = `${year}-${month}-${date}`;

					var now1 = new Date(e[1]);
					var year1 = now1.getFullYear(); //得到年份
					var month1 = now1.getMonth(); //得到月份
					var date1 = now1.getDate(); //得到日期
					var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
					var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
					var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
					month1 = month + 1;
					month1 = month.toString().padStart(2, "0");
					date1 = date1.toString().padStart(2, "0");
					var defaultDate1 = `${year1}-${month1}-${date1}`;

					let startTime = defaultDate
					let endTime = defaultDate1
					console.log(startTime, '`````````', endTime)
					this.getUserOrderEcharts(startTime, endTime)
				}
			},
			//根据日期区间筛选
			selectTime(e) {
				console.log(e)
				if (e == null) {
					this.getUserIncreaseEcharts()
				} else {
					var now = new Date(e[0]);
					var year = now.getFullYear(); //得到年份
					var month = now.getMonth(); //得到月份
					var date = now.getDate(); //得到日期
					var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
					var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
					var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
					var defaultDate = `${year}-${month}-${date}`;

					var now1 = new Date(e[1]);
					var year1 = now1.getFullYear(); //得到年份
					var month1 = now1.getMonth(); //得到月份
					var date1 = now1.getDate(); //得到日期
					var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
					var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
					var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
					month1 = month + 1;
					month1 = month.toString().padStart(2, "0");
					date1 = date1.toString().padStart(2, "0");
					var defaultDate1 = `${year1}-${month1}-${date1}`;

					let startTime = defaultDate
					let endTime = defaultDate1

					// let startTime = (new Date(e[0])).getTime() / 1000;
					// let endTime = (new Date(e[1])).getTime() / 1000;
					this.getUserIncreaseEcharts(startTime, endTime)
				}
			},
		}
	}
</script>

<style scoped>
	.home-page {
		padding: 0;
		overflow-x: hidden;
	}

	.home-page__top {
		display: flex;
		gap: 12px;
		margin-bottom: 12px;
		align-items: stretch;
	}

	.home-page__hero {
		flex: 1;
		margin-bottom: 0;
	}

	.home-page__filter {
		flex: 0 0 280px;
		margin-bottom: 0;
	}

	.home-page__filter-body {
		display: flex;
		flex-direction: column;
		gap: 12px;
	}

	.home-page__kpi-row {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 12px;
		margin-bottom: 12px;
	}

	.home-kpi-card {
		background: #fff;
		border: 1px solid #E8EAED;
		border-radius: 8px;
		padding: 16px;
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
	}

	.home-kpi-card__icon {
		width: 40px;
		height: 40px;
		border-radius: 8px;
		background: #e3f9fa;
		display: flex;
		align-items: center;
		justify-content: center;
		color: #15cad2;
		margin-bottom: 12px;
		font-size: 20px;
	}

	.home-kpi-card__label {
		font-size: 13px;
		color: #606266;
		margin-bottom: 8px;
	}

	.home-kpi-card__value {
		font-size: 24px;
		font-weight: 600;
		color: #15cad2;
		line-height: 1.2;
	}

	.home-kpi-card__unit {
		font-size: 13px;
		font-weight: 400;
		color: #909399;
		margin-left: 4px;
	}

	.home-page__detail-card {
		background: #fff;
		border: 1px solid #E8EAED;
		border-radius: 8px;
		padding: 0 16px 16px;
		margin-bottom: 12px;
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
	}

	.home-page__tabs {
		margin-bottom: 0;
	}

	.home-page__tabs >>> .el-tabs__header {
		margin-bottom: 0;
	}

	.home-stat-grid {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 12px;
		padding-top: 12px;
	}

	.home-stat-tile {
		background: #F7F8FA;
		border: 1px solid #E8EAED;
		border-radius: 8px;
		padding: 16px;
	}

	.home-stat-tile__label {
		font-size: 13px;
		color: #606266;
		margin-bottom: 8px;
	}

	.home-stat-tile__value {
		font-size: 20px;
		font-weight: 600;
		color: #15cad2;
		line-height: 1.2;
	}

	.home-stat-tile__value--alert .home-stat-tile__link {
		color: #f56c6c;
		cursor: pointer;
	}

	.home-stat-tile__value--alert .home-stat-tile__link:hover {
		color: #f78989;
	}

	.home-stat-tile__unit {
		font-size: 13px;
		font-weight: 400;
		color: #909399;
		margin-left: 4px;
	}

	.home-page__charts {
		display: grid;
		grid-template-columns: 1fr 1fr;
		gap: 12px;
	}

	.home-chart-card {
		background: #fff;
		border: 1px solid #E8EAED;
		border-radius: 8px;
		box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
		overflow: hidden;
	}

	.home-chart-card__header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 12px 16px;
		border-bottom: 1px solid #E8EAED;
		font-size: 14px;
		font-weight: 600;
		color: #303133;
		gap: 12px;
		flex-wrap: wrap;
	}

	.home-chart-card__body {
		padding: 0;
	}

	.Chart1 {
		width: 100%;
		height: 300px;
	}

	@media (max-width: 1200px) {
		.home-page__kpi-row,
		.home-stat-grid {
			grid-template-columns: repeat(2, 1fr);
		}

		.home-page__charts {
			grid-template-columns: 1fr;
		}
	}

	@media (max-width: 900px) {
		.home-page__top {
			flex-direction: column;
		}

		.home-page__filter {
			flex: 1 1 auto;
		}
	}
</style>