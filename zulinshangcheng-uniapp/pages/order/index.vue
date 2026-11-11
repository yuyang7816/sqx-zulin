<template>
	<view class="page-container">
		<!-- 顶部装饰背景 -->
		<view class="header-bg"></view>
		
		<!-- 顶部导航 -->
		<u-navbar :is-back="false" :border-bottom="false" :background="{ background: 'transparent' }">
			<view class="slot-wrap">
				<view class="tab-switch">
					<view 
						class="tab-switch-item" 
						:class="tabListIndex == 1 ? 'tab-switch-active' : ''" 
						@click="changeTabIndex(1)"
					>
						快递订单
					</view>
					<view 
						class="tab-switch-item" 
						:class="tabListIndex == 3 ? 'tab-switch-active' : ''" 
						@click="changeTabIndex(3)"
					>
						自提订单
					</view>
				</view>
			</view>
		</u-navbar>

		<!-- 状态筛选 -->
		<view class="status-tabs-wrap">
			<u-tabs 
				:list="list" 
				:is-scroll="true" 
				:current="tabCurrent" 
				@change="change" 
				active-color="#15CAD2"
				font-size="26" 
				:active-item-style="activeItemStyle" 
				inactive-color="#999999"
				:bar-style="{ background: 'linear-gradient(90deg, #15CAD2, #0EA8B0)', height: '4rpx', borderRadius: '2rpx', width: '40rpx', margin: '0 auto' }"
			></u-tabs>
		</view>

		<!-- 订单列表 -->
		<view class="order-list" v-if="orderList && orderList.length > 0">
			<view class="order-card" v-for="(item, index) in orderList" :key="index">
				<!-- 卡片头部 -->
				<view class="card-header">
					<view class="order-status-badge" :class="'badge-' + item.status">
						<view class="status-dot"></view>
						<text>{{ orderTextStatus(item.status) }}</text>
					</view>
					<view class="order-no" v-if="item.orderNum">NO.{{ item.orderNum }}</view>
				</view>

				<!-- 商品信息 -->
				<view class="goods-wrap" @click="pageGoon('/subpackage/order/orderDet?orderId=' + item.id)">
					<view class="goods-img-box">
						<image :src="item.goods.coverImg" mode="aspectFill" class="goods-img"></image>
					</view>
					<view class="goods-info">
						<view class="goods-title text-ellipsis-2">{{ item.goods.title }}</view>
						<view class="goods-spec">
							<text class="spec-tag" v-if="item.isExpress">
								{{ item.isExpress == 1 ? '包邮' : (item.isExpress == 2) ? '不包邮' : '自取' }}
							</text>
							<text v-if="item.detailJson">{{ item.detailJson }}</text>
							<text v-if="item.leaseTerm"> · {{ item.leaseTerm }}天</text>
						</view>
						<view class="goods-meta">
							<text class="meta-item" v-if="item.number">数量 x{{ item.number }}</text>
							<text class="meta-item" v-if="item.postagePrice && item.isExpress == 2 && item.status != 7">
								邮费 ¥{{ item.postagePrice }}
							</text>
						</view>
						<view class="goods-price-row">
							<view class="price-total" v-if="item.estimateMoney && item.status != 8">
								<text class="price-label">应付</text>
								<text class="price-symbol">¥</text>
								<text class="price-num">{{ item.payMoney }}</text>
							</view>
							<view class="price-total" v-else>
								<text class="price-label">应付</text>
								<text class="price-symbol">¥</text>
								<text class="price-num">{{ item.buyoutPrice }}</text>
							</view>
							<view class="price-deposit" v-if="item.deposit && item.status != 8">
								含押金¥{{ item.deposit }} · 归还退回
							</view>
						</view>
					</view>
				</view>

				<!-- 操作按钮区：三级区分 -->
				<view class="action-bar">
					<!-- 弱操作：纯文字 -->
					<view class="action-btn btn-text" @click="cancelOrder(item)"
						v-if="item.status == 0 || item.status == 1 || (item.status == 2 && item.isExpress == 3)">
						取消订单
					</view>
					
					<!-- 次要操作：浅色底 + 主题字 -->
					<view class="action-btn btn-secondary" @click="callShop"
						v-if="item.status == 1 || item.status == 2 || item.status == 8 || item.status == 7">
						联系客服
					</view>
					<view class="action-btn btn-secondary" @click="findExpress(item)"
						v-if="(item.status == 2 || item.status == 5) && (item.expressNumber || item.sellerNumber)">
						查看物流
					</view>
					<view class="action-btn btn-secondary" v-if="item.status == 6 || item.status == 8 || item.status == 9"
						@click="pageGoon('/subpackage/goods/goodsDet?goodId=' + item.goodsId)">
						重新下单
					</view>
					<view class="action-btn btn-secondary" @click="backGoodXG(item)" v-if="item.status == 5">
						修改物流
					</view>
					<view class="action-btn btn-secondary" @click="mdGoodbtn(item)"
						v-if="(item.status == 3 || item.status == 4) && item.overdueStatus == 2">
						我要买断
					</view>
					<view class="action-btn btn-secondary" @click="goonGood(item)"
						v-if="(item.status == 3 || item.status == 4) && item.overdueStatus == 2">
						继续租赁
					</view>
					<view class="action-btn btn-secondary"
						@click="pageGoon('/pages/order/feedback?orderId=' + item.id)"
						v-if="item.status == 6 && item.isEvaluate == 0">
						评价
					</view>
					
					<!-- 主要操作：主题渐变实心 -->
					<view class="action-btn btn-primary" v-if="item.status == 0" @click="gopay(item, item.id)">
						去支付
					</view>
					<view class="action-btn btn-primary" @click="remindOrder(item.id)" v-if="item.status == 1">
						催发货
					</view>
					<view class="action-btn btn-primary" @click="confirmOrder(item.id)" v-if="item.status == 2">
						确认收货
					</view>
					<view class="action-btn btn-primary" @click="backGood(item)"
						v-if="item.status == 4 || item.status == 7 || item.status == 3">
						确认归还
					</view>
				</view>
			</view>
		</view>

		<u-loadmore v-if="orderList.length > 0" :status="status" :load-text="loadText" margin-top="40" margin-bottom="40" />

		<!-- 归还弹框 -->
		<u-popup v-model="ghPopup" mode="bottom" border-radius="32" :closeable="true" @close="closeghPopup">
			<view class="popup-deco"></view>
			<view class="popup-body">
				<view class="popup-title">确认归还</view>
				<view class="popup-card">
					<view class="popup-card-main">
						<view class="tag-primary">商家地址</view>
						<view class="popup-text text-ellipsis" style="width: 85%; padding-top: 16rpx;">
							{{ shopAddress }}
						</view>
					</view>
					<view class="popup-copy-btn" @click="copyAddress">复制</view>
				</view>
				<view class="popup-card" style="margin-top: 24rpx;">
					<view class="popup-input-row">
						<view class="tag-primary" style="width: 160rpx;">物流单号</view>
						<input 
							type="text" 
							placeholder="如果您已寄出，请填写物流单号" 
							v-model="expressNumber"
							class="popup-input" 
							placeholder-style="color: #C0C4CC; font-size: 28rpx;" 
						/>
					</view>
				</view>
				<view class="popup-tips" v-html="ghTsText"></view>
				<view class="popup-confirm-btn" @click="returnGoods">确认归还</view>
			</view>
		</u-popup>

		<!-- 继续租赁 -->
		<u-popup v-model="jxPopup" mode="bottom" border-radius="32" :closeable="true">
			<view class="popup-deco"></view>
			<view class="popup-body">
				<view class="popup-title">继续租赁</view>
				<view class="popup-goods" v-if="orderInfo.goods">
					<image :src="orderInfo.goods.coverImg" mode="aspectFill" class="popup-goods-img"></image>
					<view class="popup-goods-info">
						<view class="popup-goods-title text-ellipsis">{{ orderInfo.goods.title }}</view>
						<view class="popup-goods-spec">
							<text v-if="orderInfo.goods.isExpress">
								{{ orderInfo.goods.isExpress == 1 ? '包邮' : (orderInfo.goods.isExpress == 2) ? '不包邮' : '自取' }}
							</text>
							<text v-if="orderInfo.detailJson"> · {{ orderInfo.detailJson }}</text>
						</view>
						<view class="popup-goods-meta">数量：1</view>
						<view class="popup-goods-price">
							<text class="rent-label">租金 ¥{{ orderInfo.dailyRent }}/天</text>
							<view class="rent-total">
								<text class="rent-total-label">合计</text>
								<text class="rent-symbol">¥</text>
								<text class="rent-num">{{ goonPrice }}</text>
							</view>
						</view>
					</view>
				</view>
				<view class="section-title">续租日期</view>
				<view class="day-select-wrap">
					<view 
						v-for="(item, index) in goodDays" 
						:key="index" 
						@click="maxItem(item, index)"
						:class="index == maxIndex ? 'day-tag day-tag-active' : 'day-tag'"
					>
						{{ item.dayName }}天
					</view>
				</view>
				<view class="popup-error" v-if="exceedDays > dayName && showCtext == true">
					您的订单已超出归还日期 {{ exceedDays }} 天，请重新选择日期！
				</view>
				<view class="popup-confirm-btn btn-disabled" v-if="showCtext == true">确认续租</view>
				<view class="popup-confirm-btn" @click="reletOrder" v-else>确认续租</view>
			</view>
		</u-popup>

		<!-- 我要买断 -->
		<u-popup v-model="mdPopup" mode="bottom" border-radius="32" :closeable="true">
			<view class="popup-deco"></view>
			<view class="popup-body">
				<view class="popup-title">我要买断</view>
				<view class="popup-goods" v-if="Object.keys(orderInfo).length > 0 && orderInfo.goods">
					<image :src="orderInfo.goods.coverImg" mode="aspectFill" class="popup-goods-img"></image>
					<view class="popup-goods-info">
						<view class="popup-goods-title text-ellipsis">{{ orderInfo.goods.title }}</view>
						<view class="popup-goods-spec">
							<text v-if="orderInfo.isExpress">
								{{ orderInfo.isExpress == 1 ? '包邮' : (orderInfo.isExpress == 2) ? '不包邮' : '自取' }}
							</text>
							<text v-if="orderInfo.detailJson"> · {{ orderInfo.detailJson }}</text>
						</view>
						<view class="popup-goods-meta">数量：{{ orderInfo.number }}</view>
						<view class="popup-goods-price">
							<text class="rent-label">
								买断价 ¥{{ userInfo.isVip == 1 ? orderInfo.goods.memberPrice : orderInfo.goods.price }}
							</text>
							<view class="rent-total">
								<text class="rent-total-label">补差</text>
								<text class="rent-symbol">¥</text>
								<text class="rent-num">{{ buyUpPrice || '0' }}</text>
							</view>
						</view>
					</view>
				</view>
				<view class="popup-confirm-btn" @click="confirmMd">确认买断</view>
			</view>
		</u-popup>

		<!-- 支付弹框 -->
		<u-popup v-model="showpay" mode="bottom" border-radius="32" :closeable="true" @close="close">
			<view class="popup-deco"></view>
			<view class="popup-body" style="background: #ffffff;">
				<view class="popup-title" style="text-align: center;">补缴租金</view>
				<view class="popup-info-row">
					<view class="info-block">
						<text class="info-label">订单号</text>
						<text class="info-value">{{ orderInfo.orderNum }}</text>
					</view>
					<view class="popup-copy-btn-sm" @click="copy">复制</view>
				</view>
				<view class="popup-info-row" style="justify-content: flex-start; gap: 60rpx;">
					<view class="info-block">
						<text class="info-label">应还日期</text>
						<text class="info-value">{{ orderInfo.returnDate }}</text>
					</view>
					<view class="info-block">
						<text class="info-label">超出天数</text>
						<text class="info-value" style="color: #E72D30; font-weight: 700;">{{ exceedDays }} 天</text>
					</view>
				</view>
				<view class="pay-method-list">
					<view class="pay-method-item" v-for="(item, index) in payList" :key="index" @tap="selectWay(item.id)">
						<view class="pay-method-left">
							<view class="pay-icon-wrap">
								<image :src="item.image" class="pay-icon"></image>
							</view>
							<text class="pay-name">{{ item.name }}</text>
						</view>
						<view class="pay-check" :class="payWay === item.id ? 'pay-check-active' : ''">
							<view v-if="payWay === item.id" class="pay-check-inner"></view>
						</view>
					</view>
					<view class="pay-method-item" v-if="userInfo.balance > 0" @tap="selectWay(6)">
						<view class="pay-method-left">
							<view class="pay-icon-wrap" style="background: #FFF3E0;">
								<image src="/static/images/lingqian.png" class="pay-icon"></image>
							</view>
							<text class="pay-name">余额</text>
						</view>
						<view class="pay-check" :class="payWay === 6 ? 'pay-check-active' : ''">
							<view v-if="payWay === 6" class="pay-check-inner"></view>
						</view>
					</view>
				</view>
				<view class="popup-confirm-btn" @tap="addOrder()">确认支付</view>
			</view>
		</u-popup>

		<!-- 修改物流 -->
		<u-popup v-model="xgghPopup" mode="bottom" border-radius="32" :closeable="true" @close="closexgPopup">
			<view class="popup-deco"></view>
			<view class="popup-body">
				<view class="popup-title">修改物流信息</view>
				<view class="popup-card">
					<view class="popup-card-main">
						<view class="tag-primary">商家地址</view>
						<view class="popup-text text-ellipsis" style="width: 85%; padding-top: 16rpx;">
							{{ shopAddress }}
						</view>
					</view>
					<view class="popup-copy-btn" @click="copy(shopAddress)">复制</view>
				</view>
				<view class="popup-card" style="margin-top: 24rpx;">
					<view class="popup-input-row">
						<view class="tag-primary" style="width: 180rpx;">物流订单编号</view>
						<input 
							type="text" 
							placeholder="如果您已寄出，请填写物流订单号" 
							v-model="wuliuNumber"
							class="popup-input" 
							placeholder-style="color: #C0C4CC; font-size: 28rpx;" 
						/>
					</view>
				</view>
				<view class="popup-tips" v-html="ghTsText"></view>
				<view class="popup-confirm-btn" @click="goodXGwl">确认修改</view>
			</view>
		</u-popup>

		<empty v-if="orderList.length == 0"></empty>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components: { empty },
		data() {
			return {
				keyword: '',
				tabListIndex: 1,
				list: [
					{ name: '全部', id: '' },
					{ name: '待付款', id: '0' },
					{ name: '待发货', id: '1' },
					{ name: '待收货', id: '2' },
					{ name: '待归还', id: '4' },
					{ name: '归还中', id: '5' },
					{ name: '已完成', id: '6' },
					{ name: '已买断', id: '8' },
					{ name: '已取消', id: '9' },
					{ name: '待评价', id: '10' },
				],
				tabCurrent: 0,
				activeItemStyle: { fontSize: '30rpx', fontWeight: 'bold' },
				ghPopup: false,
				jxPopup: false,
				maxItemList: [],
				maxIndex: 0,
				mdPopup: false,
				page: 1,
				status: 'loadmore',
				loadText: { loadmore: '轻轻上拉', loading: '努力加载中', nomore: '实在没有了' },
				countPage: '',
				orderList: [],
				commitotal: '',
				isVip: uni.getStorageSync('isVip'),
				goodInfo: {},
				orderInfo: {},
				exceedPrice: '',
				goodDays: [],
				shopAddress: '',
				expressNumber: '',
				ghTsText: '',
				dayName: 0,
				exceedDays: 0,
				showpay: false,
				payList: [
					{ image: '/static/zlImage/image/icon_weixin.png', name: '微信', id: 2 },
					{ image: '/static/zlImage/image/zhifubao.png', name: '支付宝', id: 3 }
				],
				payWay: 2,
				userInfo: {},
				bjPrice: '',
				xgghPopup: false,
				wuliuNumber: '',
				showCtext: false,
				statusIndex: '',
				arr: [],
				showModal: true,
			}
		},
		computed: {
			buyUpPrice() {
				let price = 0;
				let vipPrice = 0;
				if (Object.keys(this.orderInfo).length > 0) {
					vipPrice = this.userInfo.isVip == 1 ? this.orderInfo.goods.memberPrice : this.orderInfo.goods.price
					price = (vipPrice * this.orderInfo.number) - this.orderInfo.payMoney - this.exceedPrice
				}
				return price
			},
			goonPrice() {
				let price = 0;
				if (Object.keys(this.orderInfo).length > 0) {
					price = this.orderInfo.dailyRent * this.dayName
				}
				return price
			}
		},
		onLoad(options) {
			let that = this;
			this.applyStatusFromOptions(options)
			this.iStatusBarHeight = uni.getSystemInfoSync().statusBarHeight + 155;
			this.shopAddress = uni.getStorageSync('shopAddress')
			this.$Request.getT('/app/common/type/524').then(res => {
				if (res.code == 0 && res.data && res.data.value) this.ghTsText = res.data.value
			});
			this.$Request.getT('/app/common/type/236').then(res => {
				if (res.code == 0 && res.data && res.data.value) that.arr.push(res.data.value)
			})
			this.$Request.getT('/app/common/type/522').then(res => {
				if (res.code == 0 && res.data && res.data.value) that.arr.push(res.data.value)
			})
			// #ifdef MP-WEIXIN
			this.payList = [{ image: '/static/zlImage/image/icon_weixin.png', name: '微信', id: 3 }]
			this.payWay = 3;
			// #endif
			// #ifdef APP
			this.payList = [
				{ image: '/static/zlImage/image/icon_weixin.png', name: '微信', id: 1 },
				{ image: '/static/zlImage/image/zhifubao.png', name: '支付宝', id: 4 }
			]
			this.payWay = 1;
			// #endif
			// #ifdef H5
			this.$Request.get('/app/common/type/108').then(res => {
				if (res.data && res.data.value && res.data.value == '是') {
					let ua = navigator.userAgent.toLowerCase();
					if (ua.indexOf('micromessenger') !== -1) {
						this.payList = [{ image: '/static/zlImage/image/icon_weixin.png', name: '微信', id: 2 }];
						this.payWay = 2;
					} else {
						this.payList = [{ image: '/static/zlImage/image/zhifubao.png', name: '支付宝', id: 5 }];
						this.payWay = 5;
					}
				} else {
					this.payList = [{ image: '/static/zlImage/image/zhifubao.png', name: '支付宝', id: 5 }];
					this.payWay = 5;
				}
			})
			// #endif
		},
		onShow() {
			let that = this;
			let token = uni.getStorageSync('token')
			if (token) {
				this.applyStatusFromOptions({
					status: uni.getStorageSync('orderStatus')
				})
				uni.removeStorageSync('orderStatus')
				this.getMyOrderList()
				this.getUserInfo()
			}
			let userId = uni.getStorageSync('userId');
			// #ifdef MP-WEIXIN
			if (userId && !uni.getStorageSync('sendindexMsg')) that.openMsg()
			// #endif
		},
		methods: {
			applyStatusFromOptions(options = {}) {
				const targetStatus = options.status === undefined ? '' : String(options.status)
				const targetIndex = this.list.findIndex(item => item.id === targetStatus)
				if (targetIndex !== -1) {
					this.tabCurrent = targetIndex
					this.statusIndex = this.list[targetIndex].id
					return
				}
				this.tabCurrent = 0
				this.statusIndex = ''
			},
			pageGoon(url) {
				let token = uni.getStorageSync('token')
				if (token) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendindexMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) { if (datas.indexOf("accept") != -1) console.log(re) },
							fail: (res) => { console.log(res) }
						})
					}
					// #endif
				}
				uni.navigateTo({ url: url })
			},
			openMsg() {
				var that = this
				wx.getSetting({
					withSubscriptions: true,
					success(ret) {
						if (ret.subscriptionsSetting.itemSettings) {
							uni.setStorageSync('sendindexMsg', true)
							uni.openSetting({ success(rea) { console.log(rea.authSetting) } });
						} else {
							uni.setStorageSync('sendindexMsg', false)
							uni.showModal({
								title: '提示',
								content: '为了更好的体验,请绑定消息推送',
								confirmText: '确定',
								cancelText: '取消',
								success: function(res) {
									if (res.confirm) {
										uni.requestSubscribeMessage({
											tmplIds: that.arr,
											success(re) {
												var datas = JSON.stringify(re);
												if (datas.indexOf("accept") != -1) {
													uni.setStorageSync('sendindexMsg', true)
												}
											},
											fail: (res) => { console.log(res) }
										})
										that.showModal = false
									} else if (res.cancel) {
										that.showModal = true
									}
								}
							})
						}
					}
				})
			},
			closexgPopup() {
				this.xgghPopup = false
				this.wuliuNumber = ''
			},
			closeghPopup() {
				this.ghPopup = false
				this.expressNumber = ''
			},
			goodXGwl() {
				if (!this.wuliuNumber) {
					uni.showToast({ title: '请填写物流订单编号', icon: 'none', duration: 2000 })
					return
				}
				let data = { expressNumber: this.wuliuNumber, id: this.orderInfo.id }
				uni.showLoading({ title: '修改中......' })
				this.$Request.postT('/app/orders/updateOrder', data).then(res => {
					uni.hideLoading()
					if (res.status == 0) {
						uni.showToast({ title: '修改成功', icon: 'none' })
						setTimeout(() => { this.getMyOrderList() }, 500)
						this.wuliuNumber = ''
						this.xgghPopup = false
					} else {
						this.wuliuNumber = ''
						this.xgghPopup = false
						uni.showToast({ title: res.msg, icon: 'none' })
					}
				})
			},
			addOrder() {
				uni.showLoading({ title: '请稍后...' })
				let payData = { orderId: this.orderInfo.id, classify: this.payWay, supplementMoney: this.bjPrice }
				if (this.payWay == 3) this.wxPay(payData)
				else if (this.payWay == 1) this.wxPaApp(payData)
				else if (this.payWay == 2) this.wxPayH5(payData)
				else if (this.payWay == 4) this.zhfPayApp(payData)
				else if (this.payWay == 5) this.zhfPay(payData)
				else if (this.payWay == 6) this.lqPay()
			},
			zhfPayApp(payData) {
				let that = this;
				uni.showLoading({ title: '支付中...' })
				this.$Request.postT('/app/aliPay/payRent', payData).then(res => {
					uni.hideLoading()
					if (res.code == 0) that.setPayment('alipay', res.data);
					else uni.showToast({ title: res.msg, icon: 'none' })
				})
			},
			zhfPay(payData) {
				uni.showLoading({ title: '支付中...' })
				this.$Request.postT('/app/aliPay/payRent', payData).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						const div = document.createElement('div')
						div.innerHTML = res.data
						document.body.appendChild(div)
						document.forms[0].submit()
					} else uni.showToast({ title: res.msg, icon: 'none' })
				})
			},
			wxPayH5(payData) {
				let that = this;
				uni.showLoading({ title: '支付中...' })
				this.$Request.postT('/app/wxPay/paySupplement', payData).then(res => {
					uni.hideLoading()
					if (res.code == 0) that.callPay(res.data);
					else uni.showToast({ title: res.msg, icon: 'none' })
				})
			},
			callPay: function(response) {
				if (typeof WeixinJSBridge === "undefined") {
					if (document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
					} else if (document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
						document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
					}
				} else this.onBridgeReady(response);
			},
			wxPaApp(payData) {
				let that = this;
				uni.showLoading({ title: '支付中...' })
				this.$Request.postT('/app/wxPay/paySupplement', payData).then(res => {
					uni.hideLoading()
					if (res.code == 0) that.setPayment('wxpay', JSON.stringify(res.data));
					else uni.showToast({ title: res.msg, icon: 'none' })
				})
			},
			setPayment(name, order) {
				let that = this;
				uni.requestPayment({
					provider: name,
					orderInfo: order,
					success: function(res) {
						uni.showLoading({ title: '支付成功', icon: 'none' });
						uni.hideLoading();
						setTimeout(function() { this.getMyOrderList() }, 500)
					},
					fail: function(err) { uni.hideLoading(); }
				});
			},
			wxPay(payData) {
				uni.showLoading({ title: '支付中...' })
				this.$Request.postT('/app/wxPay/paySupplement', payData).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						let paymentParams = res.data
						uni.requestPayment({
							appId: paymentParams.appid,
							timeStamp: paymentParams.timestamp.toString(),
							nonceStr: paymentParams.noncestr,
							package: paymentParams.package,
							signType: paymentParams.signType,
							paySign: paymentParams.sign,
							success: function(res) {
								setTimeout(function() {
									this.ghPopup = true
									this.getMyOrderList()
								}, 500)
							},
							fail: function(err) { console.log('支付失败', err); }
						});
					} else uni.showToast({ title: res.msg, icon: 'none' })
				})
			},
			lqPay(payData) {
				uni.showLoading({ title: '支付中...' })
				let data = { orderId: this.orderInfo.id, supplementMoney: this.bjPrice }
				this.$Request.postT('/app/orders/changePaySupplementMoney', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.showToast({ title: '支付成功', icon: 'none' })
						setTimeout(() => {
							this.ghPopup = true
							this.getMyOrderList()
						}, 500)
						this.showpay = false
					} else uni.showToast({ title: res.msg, icon: 'none' })
					uni.hideLoading()
				})
			},
			selectWay: function(id) { this.payWay = id; },
			close() { this.showpay = false; },
			copy() {
				let that = this;
				uni.setClipboardData({
					data: that.orderInfo.orderNum,
					success: r => { this.$queue.showToast('复制成功'); }
				});
			},
			getUserInfo() {
				let data = { userId: uni.getStorageSync('userId') }
				this.$Request.getT('/app/user/selectUserByUserId', data).then(res => {
					if (res.code == 0) this.userInfo = res.data
					else this.userInfo = {}
				})
			},
			copyAddress() {
				let that = this;
				uni.setClipboardData({
					data: that.shopAddress,
					success: function() { uni.showToast({ title: '复制成功', icon: 'none' }) }
				});
			},
			returnGoods() {
				let that = this;
				if (!this.expressNumber) {
					uni.showToast({ title: '请填写物流订单编号', icon: 'none', duration: 2000 })
					return
				}
				let data = { expressNumber: this.expressNumber, id: this.orderInfo.id }
				that.$Request.postJson('/app/orders/returnGoods', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.showToast({ title: '商品归还信息提交成功,等待商家收货', icon: 'none', duration: 2000 })
						this.ghPopup = false
						setTimeout(() => {
							this.page = 1
							this.orderList = []
							this.getMyOrderList()
						}, 50)
						this.expressNumber = ''
					} else {
						this.ghPopup = false
						this.expressNumber = ''
						uni.showToast({ title: res.msg, icon: 'none' })
					}
				})
			},
			reletOrder() {
				if (!this.orderInfo.maxIndex) {
					uni.showToast({ title: '请选择续租日期', icon: 'none', duration: 2000 })
					return
				}
				this.jxPopup = false
				uni.navigateTo({
					url: '/pages/order/orderDet_payGono?data=' + encodeURIComponent(JSON.stringify(this.orderInfo))
				})
				this.showCtext = false
				this.exceedDays = ''
				this.dayName = this.goodDays[0].dayName
			},
			remindOrder(id) {
				let that = this;
				let data = { orderId: id }
				that.$Request.getT('/app/orders/remind', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.showToast({ title: '已帮您提醒商家尽快发货,请耐心等待', icon: 'none', duration: 2000 })
					} else uni.showToast({ title: res.msg, icon: 'none' })
				})
			},
			gopay(item, orderId) { uni.navigateTo({ url: '/pages/order/orderDet_paynow?orderId=' + orderId }) },
			calculateDaysAndCheckExceed(startDateStr, endDateStr) {
				var startDate = new Date(startDateStr);
				var endDate = new Date(endDateStr);
				var currentDate = new Date();
				if (currentDate > endDate) {
					var exceedMilliseconds = currentDate - endDate;
					var exceedHours = Math.floor(exceedMilliseconds / (1000 * 60 * 60));
					this.exceedDays = Math.floor(exceedHours / 24);
				}
			},
			confirmMd() {
				uni.navigateTo({
					url: '/pages/order/orderDet?orderInfo=' + encodeURIComponent(JSON.stringify(this.orderInfo)) +
						'&orderType=' + this.orderInfo.orderType + '&exceedPrice=' + this.exceedPrice
				})
				this.mdPopup = false
			},
			mdGoodbtn(item) {
				let that = this;
				uni.showModal({
					title: '提示',
					content: '是否确认买断该商品，确认买断押金则抵扣对应的补差金额;如已超出租赁日期，则需补费超出金额',
					success: function(res) {
						if (res.confirm) {
							that.mdPopup = true
							that.orderInfo = item
							that.orderInfo.orderType = 6
							let data = { id: item.id }
							that.$Request.getT('/app/orders/calculateTheNumberDay', data).then(res => {
								if (res.status == 0 || res.code == 0) this.exceedPrice = res.data
							})
							that.calculateDaysAndCheckExceed(item.leaseStartDate, item.returnDate)
						}
					}
				})
			},
			orderTextStatus(val) {
				if (val == 0) return '待付款'
				else if (val == 1) return '待发货'
				else if (val == 2) return '待收货'
				else if (val == 4) return '待归还'
				else if (val == 5) return '归还中'
				else if (val == 6) return '已完成'
				else if (val == 8) return '已买断'
				else if (val == 9) return '已取消'
				else if (val == 10) return '待评价'
			},
			changeTabIndex(index) {
				this.tabListIndex = index
				this.page = 1
				this.orderList = []
				this.tabCurrent = 0
				this.statusIndex = ''
				this.getMyOrderList()
			},
			change(index) {
				this.tabCurrent = index
				this.statusIndex = this.list[index].id;
				this.page = 1
				this.orderList = []
				this.getMyOrderList()
			},
			backGoodXG(item) {
				this.orderInfo = item
				this.xgghPopup = true
			},
			backGood(item) {
				let that = this;
				this.orderInfo = item
				let data = { id: item.id }
				that.$Request.getT('/app/orders/calculateTheNumberDay', data).then(res => {
					if (res.status == 0 || res.code == 0) {
						that.calculateDaysAndCheckExceed(item.leaseStartDate, item.returnDate)
						if (this.orderInfo.supplementStatus == 2 && Number(res.data) > 0) {
							uni.showModal({
								title: '提示',
								content: '您的订单已超出归还日期' + that.exceedDays + '天，需补缴金额' + res.data + '元！',
								success: function(red) {
									if (red.confirm) {
										that.showpay = true
										that.bjPrice = res.data
									}
								}
							})
						} else {
							uni.showModal({
								title: '提示',
								content: '请确认归还日期，若提前归还不予退还剩余租金！',
								success: function(res) {
									if (res.confirm) that.ghPopup = true
								}
							})
						}
					}
				})
			},
			goonGood(item) {
				let that = this;
				this.orderInfo = item;
				this.jxPopup = true
				this.getGoodsDays()
				that.calculateDaysAndCheckExceed(this.orderInfo.leaseStartDate, this.orderInfo.returnDate)
				if (this.exceedDays > this.dayName) this.showCtext = true
				else this.showCtext = false
			},
			maxItem(item, index) {
				this.maxIndex = index
				this.orderInfo.maxIndex = item.id
				this.dayName = item.dayName
				if (this.exceedDays > this.dayName) this.showCtext = true
				else this.showCtext = false
			},
			getGoodsDays() {
				let data = { id: this.orderInfo.goodsId, page: 1, limit: 999 }
				this.$Request.getT('/app/leaseDays/getLeaseDaysList', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						this.goodDays = res.data.records
						this.dayName = this.goodDays[this.maxIndex].dayName
						this.orderInfo.maxIndex = this.goodDays[this.maxIndex].id
					}
				})
			},
			callShop() { uni.navigateTo({ url: '/my/setting/chat' }) },
			getMyOrderList() {
				let status = this.tabCurrent == 0 ? '' : this.tabCurrent
				let data = {
					page: this.page,
					size: 10,
					userId: uni.getStorageSync('userId'),
					isExpress: this.tabListIndex,
					status: this.statusIndex
				}
				this.$Request.getT('/app/orders/findMyList', data).then(res => {
					this.countPage = res.data.pages
					this.status = this.page == this.countPage ? 'nomore' : 'loadmore'
					if (this.page == 1) this.orderList = res.data.records
					else this.orderList = [...this.orderList, ...res.data.records]
					this.commitotal = res.data.total
				})
			},
			cancelOrder(item) {
				let data = { id: item.id }
				this.$Request.getT('/app/orders/cancel', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.showToast({ title: '取消成功', icon: 'none' })
						this.page = 1
						this.orderList = []
						setTimeout(() => { this.getMyOrderList() }, 50)
					} else uni.showToast({ title: res.msg, icon: 'none' })
				})
			},
			confirmOrder(id) {
				uni.showLoading({ title: '请稍后......' })
				let data = { id: id }
				this.$Request.getT('/app/orders/confirm', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.hideLoading();
						uni.showToast({ title: '确认收货成功', icon: 'none' })
						this.page = 1
						this.orderList = []
						setTimeout(() => { this.getMyOrderList() }, 50)
					} else {
						uni.showToast({ title: res.msg, icon: 'none' })
						uni.hideLoading();
					}
				})
			},
			findExpress(item) {
				this.$queue.showLoading('查询中...');
				var expressNumber = '';
				if (item.status == 2) expressNumber = item.sellerNumber
				else if (item.status == 5) expressNumber = item.expressNumber
				if (expressNumber) {
					var exNum = expressNumber.slice(0, 2)
					var phNum = item.mobile.slice(7, 11)
					if (exNum == 'SF') expressNumber = expressNumber + ':' + phNum
					else expressNumber = expressNumber + ':4013'
				}
				this.$Request.getT('/app/orders/findExpress', { expressNumber: expressNumber }).then(res => {
					if (res.code == 0 || res.status == 0) {
						this.$queue.setData('selectOption', res.data.result);
						uni.navigateTo({ url: './search' });
					} else uni.showToast({ title: res.msg, icon: "none" })
					uni.hideLoading();
				})
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getMyOrderList()
		},
		onReachBottom() {
			if (this.page < this.countPage) {
				this.page += 1
				this.status = 'loading'
				this.getMyOrderList()
			} else this.status = 'nomore'
		},
	}
</script>

<style lang="scss" scoped>
	$primary: #15CAD2;
	$primary-light: #E3FAFB;
	$primary-soft: #B3F0F3;
	$primary-dark: #0EA8B0;
	$primary-deep: #088F96;
	$bg: #F0F9FA;
	$card-bg: #FFFFFF;
	$text-main: #1A1A2E;
	$text-sub: #4A5568;
	$text-tip: #A0AEC0;
	$border: #EDF2F7;
	$danger: #E72D30;
	$shadow: rgba(21, 202, 210, 0.08);

	page { background: $bg !important; }

	.page-container {
		min-height: 100vh;
		background: $bg;
		position: relative;
	}

	/* 顶部渐变装饰 */
	.header-bg {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 320rpx;
		background: linear-gradient(180deg, #E3FAFB 0%, #F0F9FA 70%, transparent 100%);
		pointer-events: none;
		z-index: 0;
	}

	/* 顶部导航 */
	.slot-wrap {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 100%;
	}

	.tab-switch {
		display: flex;
		align-items: center;
		background: rgba(255, 255, 255, 0.85);
		backdrop-filter: blur(10px);
		border-radius: 40rpx;
		padding: 4rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.06);
		border: 1rpx solid rgba(255, 255, 255, 0.6);
	}

	.tab-switch-item {
		font-size: 28rpx;
		color: $text-sub;
		padding: 14rpx 40rpx;
		border-radius: 36rpx;
		transition: all 0.3s ease;
		font-weight: 500;
	}

	.tab-switch-active {
		background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
		color: #FFFFFF;
		font-weight: 600;
		box-shadow: 0 4rpx 16rpx rgba(21, 202, 210, 0.35);
	}

	/* 状态标签栏 */
	.status-tabs-wrap {
		background: transparent;
		position: sticky;
		top: 0;
		z-index: 10;
		padding: 0 20rpx;
	}

	/* 订单列表 */
	.order-list {
		padding: 16rpx 24rpx 40rpx;
		position: relative;
		z-index: 1;
	}

	.order-card {
		background: $card-bg;
		border-radius: 24rpx;
		margin-bottom: 28rpx;
		overflow: hidden;
		box-shadow: 0 8rpx 32rpx $shadow;
		position: relative;
		transition: transform 0.2s ease;
	}

	.order-card:active { transform: scale(0.995); }

	/* 卡片头部 */
	.card-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 28rpx 28rpx 20rpx;
	}

	.order-status-badge {
		display: inline-flex;
		align-items: center;
		gap: 10rpx;
		font-size: 24rpx;
		font-weight: 600;
		padding: 8rpx 18rpx;
		border-radius: 12rpx;
	}

	.status-dot {
		width: 14rpx;
		height: 14rpx;
		border-radius: 50%;
		background: currentColor;
		position: relative;
	}

	.status-dot::after {
		content: '';
		position: absolute;
		top: -4rpx;
		left: -4rpx;
		right: -4rpx;
		bottom: -4rpx;
		border-radius: 50%;
		background: currentColor;
		opacity: 0.25;
		animation: pulse 2s infinite;
	}

	@keyframes pulse {
		0% { transform: scale(1); opacity: 0.25; }
		50% { transform: scale(1.3); opacity: 0; }
		100% { transform: scale(1); opacity: 0.25; }
	}

	.badge-0 { color: #FF9500; background: #FFF8E1; }
	.badge-1 { color: $primary-deep; background: $primary-light; }
	.badge-2 { color: #2979FF; background: #E3F2FD; }
	.badge-4 { color: #9C27B0; background: #F3E5F5; }
	.badge-5 { color: #795548; background: #EFEBE9; }
	.badge-6 { color: #4CAF50; background: #E8F5E9; }
	.badge-8 { color: $danger; background: #FFEBEE; }
	.badge-9 { color: $text-tip; background: #F5F5F5; }
	.badge-10 { color: #FF5722; background: #FBE9E7; }

	.order-no {
		font-size: 22rpx;
		color: $text-tip;
		font-family: monospace;
		letter-spacing: 1rpx;
	}

	/* 商品区域 */
	.goods-wrap {
		display: flex;
		padding: 0 28rpx 28rpx;
	}

	.goods-img-box {
		width: 168rpx;
		height: 168rpx;
		border-radius: 20rpx;
		overflow: hidden;
		flex-shrink: 0;
		position: relative;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.08);
	}

	.goods-img {
		width: 100%;
		height: 100%;
	}

	.goods-info {
		flex: 1;
		margin-left: 28rpx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		min-width: 0;
	}

	.goods-title {
		font-size: 30rpx;
		font-weight: 700;
		color: $text-main;
		line-height: 1.4;
	}

	.text-ellipsis-2 {
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		overflow: hidden;
		text-overflow: ellipsis;
	}

	.goods-spec {
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		gap: 8rpx;
		margin-top: 10rpx;
		font-size: 24rpx;
		color: $text-tip;
	}

	.spec-tag {
		background: $primary-light;
		color: $primary-deep;
		font-size: 22rpx;
		padding: 4rpx 12rpx;
		border-radius: 8rpx;
		font-weight: 500;
	}

	.goods-meta {
		display: flex;
		gap: 24rpx;
		margin-top: 10rpx;
	}

	.meta-item {
		font-size: 24rpx;
		color: $text-tip;
	}

	.goods-price-row {
		margin-top: 14rpx;
		display: flex;
		align-items: baseline;
		flex-wrap: wrap;
		gap: 12rpx;
	}

	.price-total {
		display: flex;
		align-items: baseline;
	}

	.price-label {
		font-size: 24rpx;
		color: $text-sub;
		margin-right: 8rpx;
		font-weight: 500;
	}

	.price-symbol {
		font-size: 24rpx;
		color: $text-main;
		font-weight: 700;
	}

	.price-num {
		font-size: 36rpx;
		color: $text-main;
		font-weight: 800;
		letter-spacing: -1rpx;
	}

	.price-deposit {
		font-size: 22rpx;
		color: $text-tip;
		background: #F7FAFC;
		padding: 4rpx 12rpx;
		border-radius: 8rpx;
	}

	/* ===== 操作按钮：三级区分 ===== */
	.action-bar {
		display: flex;
		flex-wrap: wrap;
		justify-content: flex-end;
		gap: 16rpx;
		padding: 24rpx 28rpx 28rpx;
		border-top: 1rpx solid $border;
		position: relative;
	}

	/* 一级：主要操作（实心渐变） */
	.btn-primary {
		min-width: 148rpx;
		height: 64rpx;
		line-height: 64rpx;
		text-align: center;
		font-size: 26rpx;
		border-radius: 32rpx;
		padding: 0 28rpx;
		color: #FFFFFF;
		background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
		box-shadow: 0 6rpx 20rpx rgba(21, 202, 210, 0.3);
		font-weight: 600;
		transition: all 0.2s ease;
	}

	.btn-primary:active {
		transform: scale(0.96);
		box-shadow: 0 3rpx 10rpx rgba(21, 202, 210, 0.2);
	}

	/* 二级：次要操作（浅底+主题字+细边框） */
	.btn-secondary {
		min-width: 132rpx;
		height: 60rpx;
		line-height: 58rpx;
		text-align: center;
		font-size: 26rpx;
		border-radius: 30rpx;
		padding: 0 22rpx;
		color: $primary-deep;
		background: linear-gradient(135deg, #FFFFFF 0%, $primary-light 100%);
		border: 2rpx solid rgba(21, 202, 210, 0.25);
		font-weight: 500;
		transition: all 0.2s ease;
	}

	.btn-secondary:active {
		transform: scale(0.96);
		background: $primary-light;
	}

	/* 三级：弱操作（纯文字） */
	.btn-text {
		min-width: 100rpx;
		height: 60rpx;
		line-height: 60rpx;
		text-align: center;
		font-size: 26rpx;
		border-radius: 30rpx;
		padding: 0 16rpx;
		color: $text-tip;
		background: transparent;
		font-weight: 400;
		transition: all 0.2s ease;
	}

	.btn-text:active {
		color: $text-sub;
		background: #F7FAFC;
	}

	/* ========== 弹窗通用 ========== */
	.popup-deco {
		width: 60rpx;
		height: 6rpx;
		background: #E2E8F0;
		border-radius: 3rpx;
		margin: 16rpx auto 8rpx;
	}

	.popup-body {
		background: #FFFFFF;
		padding-bottom: constant(safe-area-inset-bottom);
		padding-bottom: env(safe-area-inset-bottom);
	}

	.popup-title {
		font-size: 34rpx;
		font-weight: 800;
		color: $text-main;
		padding: 28rpx 32rpx 24rpx;
		letter-spacing: 1rpx;
	}

	.popup-card {
		background: linear-gradient(135deg, #F8FAFC 0%, #F1F5F9 100%);
		border-radius: 20rpx;
		margin: 0 32rpx;
		padding: 28rpx;
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		border: 1rpx solid $border;
	}

	.popup-card-main {
		flex: 1;
		min-width: 0;
	}

	.popup-text {
		font-size: 26rpx;
		color: $text-main;
		line-height: 1.6;
	}

	.popup-copy-btn {
		background: linear-gradient(135deg, $primary-light 0%, #FFFFFF 100%);
		color: $primary-deep;
		font-size: 24rpx;
		padding: 10rpx 22rpx;
		border-radius: 12rpx;
		margin-left: 20rpx;
		flex-shrink: 0;
		font-weight: 600;
		border: 1rpx solid rgba(21, 202, 210, 0.2);
		box-shadow: 0 2rpx 8rpx rgba(21, 202, 210, 0.1);
	}

	.popup-input-row {
		display: flex;
		align-items: center;
		width: 100%;
	}

	.popup-input {
		flex: 1;
		margin-left: 20rpx;
		font-size: 28rpx;
		color: $text-main;
		font-weight: 500;
	}

	.popup-tips {
		color: $text-sub;
		font-size: 26rpx;
		margin: 32rpx;
		line-height: 1.7;
		padding: 20rpx;
		background: #FFFBEB;
		border-radius: 12rpx;
		border-left: 4rpx solid #F59E0B;
	}

	.popup-confirm-btn {
		background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
		color: #FFFFFF;
		margin: 20rpx 32rpx 40rpx;
		font-size: 30rpx;
		height: 90rpx;
		border-radius: 45rpx;
		text-align: center;
		line-height: 90rpx;
		font-weight: 700;
		box-shadow: 0 12rpx 32rpx rgba(21, 202, 210, 0.25);
		letter-spacing: 2rpx;
	}

	.btn-disabled {
		background: #E2E8F0 !important;
		box-shadow: none;
		color: #A0AEC0;
	}

	.popup-error {
		color: $danger;
		font-size: 26rpx;
		text-align: center;
		margin: 0 32rpx 20rpx;
		font-weight: 500;
	}

	.tag-primary {
		color: #FFFFFF;
		background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
		border-radius: 8rpx;
		font-size: 22rpx;
		padding: 8rpx 16rpx;
		display: inline-block;
		font-weight: 600;
		box-shadow: 0 4rpx 12rpx rgba(21, 202, 210, 0.2);
	}

	/* 续租/买断商品 */
	.popup-goods {
		display: flex;
		padding: 0 32rpx 28rpx;
	}

	.popup-goods-img {
		width: 148rpx;
		height: 148rpx;
		border-radius: 16rpx;
		flex-shrink: 0;
		background: $bg;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.06);
	}

	.popup-goods-info {
		flex: 1;
		margin-left: 24rpx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		min-width: 0;
	}

	.popup-goods-title {
		font-size: 28rpx;
		font-weight: 700;
		color: $text-main;
	}

	.popup-goods-spec {
		font-size: 24rpx;
		color: $text-tip;
		margin-top: 6rpx;
	}

	.popup-goods-meta {
		font-size: 24rpx;
		color: $text-tip;
		margin-top: 6rpx;
	}

	.popup-goods-price {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 10rpx;
	}

	.rent-label {
		font-size: 24rpx;
		color: $text-sub;
		font-weight: 500;
	}

	.rent-total {
		display: flex;
		align-items: baseline;
	}

	.rent-total-label {
		font-size: 24rpx;
		color: $text-sub;
		margin-right: 8rpx;
		font-weight: 500;
	}

	.rent-symbol {
		font-size: 22rpx;
		color: $danger;
		font-weight: 700;
	}

	.rent-num {
		font-size: 34rpx;
		color: $danger;
		font-weight: 800;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 700;
		color: $text-main;
		padding: 24rpx 32rpx 20rpx;
		position: relative;
		display: inline-block;
	}

	.section-title::after {
		content: '';
		position: absolute;
		bottom: 16rpx;
		left: 32rpx;
		width: 40rpx;
		height: 6rpx;
		background: linear-gradient(90deg, $primary, $primary-soft);
		border-radius: 3rpx;
	}

	.day-select-wrap {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
		padding: 0 32rpx;
	}

	.day-tag {
		min-width: 148rpx;
		height: 76rpx;
		line-height: 76rpx;
		text-align: center;
		background: #F1F5F9;
		border-radius: 16rpx;
		font-size: 26rpx;
		color: $text-sub;
		padding: 0 28rpx;
		border: 2rpx solid transparent;
		transition: all 0.25s ease;
		font-weight: 500;
	}

	.day-tag-active {
		background: linear-gradient(135deg, $primary-light 0%, #FFFFFF 100%);
		color: $primary-deep;
		border-color: $primary;
		font-weight: 700;
		box-shadow: 0 6rpx 20rpx rgba(21, 202, 210, 0.15);
		transform: translateY(-2rpx);
	}

	/* 支付弹窗 */
	.popup-info-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin: 0 32rpx 20rpx;
		padding: 24rpx 28rpx;
		background: linear-gradient(135deg, #F8FAFC 0%, #F1F5F9 100%);
		border-radius: 16rpx;
		border: 1rpx solid $border;
	}

	.info-block {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}

	.info-label {
		font-size: 22rpx;
		color: $text-tip;
		font-weight: 500;
	}

	.info-value {
		font-size: 28rpx;
		color: $text-main;
		font-weight: 600;
	}

	.popup-copy-btn-sm {
		background: linear-gradient(135deg, $primary-light 0%, #FFFFFF 100%);
		color: $primary-deep;
		font-size: 22rpx;
		padding: 8rpx 18rpx;
		border-radius: 10rpx;
		font-weight: 600;
		border: 1rpx solid rgba(21, 202, 210, 0.2);
	}

	.pay-method-list {
		padding: 8rpx 32rpx 0;
	}

	.pay-method-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 28rpx 0;
		border-bottom: 1rpx solid $border;
	}

	.pay-method-left {
		display: flex;
		align-items: center;
	}

	.pay-icon-wrap {
		width: 64rpx;
		height: 64rpx;
		border-radius: 50%;
		background: #E8F5E9;
		display: flex;
		align-items: center;
		justify-content: center;
		overflow: hidden;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.pay-icon {
		width: 40rpx;
		height: 40rpx;
	}

	.pay-name {
		font-size: 30rpx;
		color: $text-main;
		margin-left: 20rpx;
		font-weight: 600;
	}

	.pay-check {
		width: 44rpx;
		height: 44rpx;
		border-radius: 50%;
		border: 3rpx solid #E2E8F0;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: all 0.25s ease;
		background: #FFFFFF;
	}

	.pay-check-active {
		border-color: $primary;
		background: $primary;
		box-shadow: 0 4rpx 12rpx rgba(21, 202, 210, 0.25);
	}

	.pay-check-inner {
		width: 18rpx;
		height: 18rpx;
		background: #FFFFFF;
		border-radius: 50%;
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}
</style>
