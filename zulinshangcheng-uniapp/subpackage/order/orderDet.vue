<template>
	<view class="page-container">
		<!-- 顶部装饰背景 -->
		<view class="header-bg"></view>

		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle" back-icon-size="29"
			:border-bottom="false" :background="{ background: 'transparent' }"></u-navbar>

		<!-- 状态区域 -->
		<view class="status-card">
			<view class="status-main">
				<view class="status-icon-wrap">
					<image src="/static/zlImage/image/guihuan.png" mode="aspectFit" class="status-icon"></image>
				</view>
				<view class="status-text">
					<view class="status-title">{{ orderTextStatus(orderInfo.status) }}</view>
					<view class="status-desc">{{ orderTextStatusWa(orderInfo.status) }}</view>
				</view>
			</view>
			<view class="status-deco">
				<view class="deco-circle"></view>
				<view class="deco-circle deco-circle-2"></view>
			</view>
		</view>

		<!-- 寄回/取件地址 -->
		<view class="info-card" v-if="orderInfo.status == 4 || orderInfo.status == 3">
			<view class="card-row between">
				<view class="row-title">寄回地址</view>
				<view class="copy-tag" @click="copy(shopAddress)">复制</view>
			</view>
			<view class="address-text">{{ shopAddress || '' }}</view>
		</view>

		<view class="info-card" v-if="(orderInfo.status == 2 || orderInfo.status == 1) && orderInfo.isExpress == 3">
			<view class="card-row between">
				<view class="row-title">取件地址</view>
				<view class="copy-tag" @click="copy(shopAddress)">复制</view>
			</view>
			<view class="address-text">{{ shopAddress || '' }}</view>
		</view>

		<!-- 物流单号 -->
		<view class="info-card"
			v-if="(orderInfo.status == 2 || orderInfo.status == 5) && (orderInfo.expressNumber || orderInfo.sellerNumber) && orderInfo.expressName">
			<view class="card-row between">
				<view class="express-info">
					<text class="express-label">物流单号</text>
					<text class="express-value">{{ orderInfo.expressName || '' }} {{ (orderInfo.status == 2 ? orderInfo.sellerNumber : orderInfo.expressNumber) || '' }}</text>
				</view>
				<view class="copy-tag" @click="copy(orderInfo.status == 2 ? orderInfo.sellerNumber : orderInfo.expressNumber)">复制</view>
			</view>
		</view>

		<!-- 商品信息 -->
		<view class="info-card goods-card">
			<view class="goods-row" @click="navigate('/subpackage/goods/goodsDet?goodId=' + orderInfo.goodsId)">
				<view class="goods-img-box">
					<image :src="goodInfo.coverImg" mode="aspectFill" class="goods-img"></image>
				</view>
				<view class="goods-info">
					<view class="goods-title text-ellipsis-2">{{ goodInfo.title || '' }}</view>
					<view class="goods-spec">
						<text class="spec-tag" v-if="orderInfo.isExpress">{{ (orderInfo.isExpress == 1 ? '包邮' : (orderInfo.isExpress == 2) ? '不包邮' : '自取') || '' }}</text>
						<text v-if="orderInfo.detailJson">{{ orderInfo.detailJson || '' }}</text>
					</view>
					<view class="goods-meta">数量：{{ orderInfo.number || '' }}</view>
				</view>
			</view>

			<!-- 起租/归还日期 -->
			<view class="date-bar" v-if="orderInfo.status == 0 || orderInfo.status == 1 || orderInfo.status == 2">
				<view class="date-item">
					<view class="date-label">预计起租日</view>
					<view class="date-value">{{ orderInfo.leaseStartDate || '' }}</view>
				</view>
				<view class="date-arrow">
					<image src="../../static/zlImage/jt.png" mode="aspectFit" style="width: 44rpx; height: 26rpx;"></image>
				</view>
				<view class="date-item">
					<view class="date-label">预计归还日</view>
					<view class="date-value danger">{{ orderInfo.returnDate || '' }}</view>
				</view>
			</view>

			<view class="date-bar"
				v-if="orderInfo.status == 3 || orderInfo.status == 4 || orderInfo.status == 5 || orderInfo.status == 6 || orderInfo.status == 9 || orderInfo.status == 10">
				<view class="date-item">
					<view class="date-label">起租日</view>
					<view class="date-value">{{ orderInfo.leaseStartDate || '' }}</view>
				</view>
				<view class="date-arrow">
					<image src="../../static/zlImage/jt.png" mode="aspectFit" style="width: 44rpx; height: 26rpx;"></image>
				</view>
				<view class="date-item">
					<view class="date-label">归还日</view>
					<view class="date-value danger">{{ orderInfo.returnDate || '' }}</view>
				</view>
			</view>
		</view>

		<!-- 费用明细 -->
		<view class="info-card">
			<view class="card-header-title">费用明细</view>
			<view class="fee-list">
				<view class="fee-item">
					<text class="fee-label">日租金</text>
					<text class="fee-value"><text class="fee-symbol">¥</text>{{ orderInfo.dailyRent || '' }}</text>
				</view>
				<view class="fee-item" v-if="allDate">
					<text class="fee-label">租期</text>
					<text class="fee-value">{{ orderInfo.leaseTerm || '' }}天</text>
				</view>
				<view class="fee-item" v-if="orderInfo.isExpress">
					<text class="fee-label">提货方式</text>
					<text class="fee-value">{{ (orderInfo.isExpress == 1 ? '包邮' : (orderInfo.isExpress == 2) ? '不包邮' : '自取') || '' }}</text>
				</view>
				<view class="fee-item" v-if="orderInfo.isExpress == 2 && orderInfo.postagePrice">
					<text class="fee-label">运费</text>
					<text class="fee-value"><text class="fee-symbol">¥</text>{{ orderInfo.postagePrice || '0' }}</text>
				</view>
				<view class="fee-item">
					<text class="fee-label">押金</text>
					<text class="fee-value"><text class="fee-symbol">¥</text>{{ orderInfo.deposit || '' }}</text>
				</view>
				<view class="fee-item" v-if="orderInfo.couponMoney">
					<text class="fee-label">优惠券</text>
					<text class="fee-value fee-discount"><text class="fee-symbol">-¥</text>{{ orderInfo.couponMoney || '0' }}</text>
				</view>
				<view class="fee-item" v-if="orderInfo.supplementStatus == 1">
					<text class="fee-label">补费金额</text>
					<text class="fee-value"><text class="fee-symbol">¥</text>{{ orderInfo.supplementMoney || '0' }}</text>
				</view>
				<view class="fee-item" v-if="orderInfo.grossRent">
					<text class="fee-label">总租金</text>
					<text class="fee-value"><text class="fee-symbol">¥</text>{{ orderInfo.grossRent || '0' }}</text>
				</view>
				<view class="fee-item" v-if="orderInfo.status == 8">
					<text class="fee-label">买断价格</text>
					<text class="fee-value fee-highlight"><text class="fee-symbol">¥</text>{{ orderInfo.buyoutPrice || '0' }}</text>
				</view>
				<view class="fee-item" v-if="orderInfo.status == 8">
					<text class="fee-label">补差额</text>
					<text class="fee-value fee-highlight"><text class="fee-symbol">¥</text>{{ orderInfo.price || '0' }}</text>
				</view>
				<view class="fee-item fee-total" v-else>
					<text class="fee-label">合计</text>
					<text class="fee-value fee-highlight"><text class="fee-symbol">¥</text>{{ orderInfo.payMoney || '0' }}</text>
				</view>
			</view>
		</view>

		<!-- 订单信息 -->
		<view class="info-card">
			<view class="card-header-title">订单信息</view>
			<view class="info-list">
				<view class="info-item between">
					<text class="info-label">订单号</text>
					<view class="info-right">
						<text class="info-value mono">{{ orderInfo.orderNum || '' }}</text>
						<view class="copy-tag-sm" @click="copy(orderInfo.orderNum)">复制</view>
					</view>
				</view>
				<view class="info-item between">
					<text class="info-label">下单时间</text>
					<text class="info-value">{{ orderInfo.createAt || '' }}</text>
				</view>
				<view class="info-item between" v-if="orderInfo.descrition">
					<text class="info-label">备注</text>
					<text class="info-value">{{ orderInfo.descrition || '' }}</text>
				</view>
				<view class="info-item between" v-if="orderPayWay">
					<text class="info-label">支付方式</text>
					<text class="info-value">{{ orderPayWay || '' }}</text>
				</view>
			</view>
		</view>

		<view style="height: 160rpx;"></view>

		<!-- 底部按钮 -->
		<view class="footer-bar">
			<view class="footer-inner">
				<view class="action-btn btn-text"
					@click="navigate('/subpackage/goods/goodsDet?goodId=' + orderInfo.goodsId)"
					v-if="orderInfo.status == 6 || orderInfo.status == 8 || orderInfo.status == 9">重新下单</view>
				<view class="action-btn btn-secondary"
					v-if="orderInfo.status == 1 || orderInfo.status == 2 || orderInfo.status == 8 || orderInfo.status == 7"
					@click="callKef">联系客服</view>
				<view class="action-btn btn-secondary" @click="backGoodXG()" v-if="orderInfo.status == 5">修改物流</view>
				<view class="action-btn btn-secondary"
					@click="navigate('/pages/order/feedback?orderId=' + orderInfo.id)"
					v-if="orderInfo.status == 6 && orderInfo.isEvaluate == 0">评价</view>
				<view class="action-btn btn-secondary" @click="findExpress()"
					v-if="(orderInfo.status == 2 || orderInfo.status == 5) && (orderInfo.expressNumber || orderInfo.sellerNumber)">查看物流</view>
				<view class="action-btn btn-primary" v-if="orderInfo.status == 4 || orderInfo.status == 7 || orderInfo.status == 3"
					@click="backGood()">确认归还</view>
				<view class="action-btn btn-primary" @click="remindOrder()" v-if="orderInfo.status == 1">催发货</view>
				<view class="action-btn btn-primary" @click="confirmOrder()" v-if="orderInfo.status == 2">确认收货</view>
			</view>
		</view>

		<!-- 修改物流弹窗 -->
		<u-popup v-model="xgghPopup" mode="bottom" border-radius="32" :closeable="true" @close="closexgPopup">
			<view class="popup-deco"></view>
			<view class="popup-body">
				<view class="popup-title">修改物流信息</view>
				<view class="popup-card">
					<view class="popup-card-main">
						<view class="tag-primary">商家地址</view>
						<view class="popup-text text-ellipsis" style="width: 85%; padding-top: 16rpx;">{{ shopAddress }}</view>
					</view>
					<view class="popup-copy-btn" @click="copy(shopAddress)">复制</view>
				</view>
				<view class="popup-card" style="margin-top: 24rpx;">
					<view class="popup-input-row">
						<view class="tag-primary" style="width: 180rpx;">物流订单编号</view>
						<input type="text" placeholder="如果您已寄出，请填写物流订单号" v-model="wuliuNumber" class="popup-input"
							placeholder-style="color: #C0C4CC; font-size: 28rpx;" />
					</view>
				</view>
				<view class="popup-tips" v-html="ghTsText"></view>
				<view class="popup-confirm-btn" @click="goodXGwl">确认修改</view>
			</view>
		</u-popup>

		<!-- 归还弹窗 -->
		<u-popup v-model="ghPopup" mode="bottom" border-radius="32" :closeable="true" @close="closeghPopup">
			<view class="popup-deco"></view>
			<view class="popup-body">
				<view class="popup-title">确认归还</view>
				<view class="popup-card">
					<view class="popup-card-main">
						<view class="tag-primary">商家地址</view>
						<view class="popup-text text-ellipsis" style="width: 85%; padding-top: 16rpx;">{{ shopAddress }}</view>
					</view>
					<view class="popup-copy-btn" @click="copy(shopAddress)">复制</view>
				</view>
				<view class="popup-card" style="margin-top: 24rpx;">
					<view class="popup-input-row">
						<view class="tag-primary" style="width: 160rpx;">物流单号</view>
						<input type="text" placeholder="如果您已寄出，请填写物流订单号" v-model="wuliuNumber" class="popup-input"
							placeholder-style="color: #C0C4CC; font-size: 28rpx;" />
					</view>
				</view>
				<view class="popup-tips" v-html="ghTsText"></view>
				<view class="popup-confirm-btn" @click="returnGoods">确认归还</view>
			</view>
		</u-popup>

		<!-- 支付弹窗 -->
		<u-popup v-model="showpay" mode="bottom" border-radius="32" :closeable="true" @close="close">
			<view class="popup-deco"></view>
			<view class="popup-body">
				<view class="popup-title" style="text-align: center;">补缴租金</view>
				<view class="popup-info-row">
					<view class="info-block">
						<text class="info-label">订单号</text>
						<text class="info-value">{{ orderInfo.orderNum }}</text>
					</view>
					<view class="popup-copy-btn-sm" @click="copy(orderInfo.orderNum)">复制</view>
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
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components: { empty },
		data() {
			return {
				backText: '订单详情',
				backTextStyle: { color: '#333333' },
				orderId: '',
				orderInfo: {},
				isVip: uni.getStorageSync('isVip'),
				allDate: '',
				orderPayWay: '',
				shopAddress: '',
				goodInfo: {},
				ghPopup: false,
				ghTsText: '',
				wuliuNumber: '',
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
			}
		},
		onLoad(option) {
			this.shopAddress = uni.getStorageSync('shopAddress')
			if (option.orderId) this.orderId = option.orderId
			this.getOrderInfo()
			this.getUserInfo()
			this.$Request.getT('/app/common/type/524').then(res => {
				if (res.code == 0 && res.data && res.data.value) this.ghTsText = res.data.value
			});
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
		onShow() {},
		methods: {
			closexgPopup() {
				this.xgghPopup = false
				this.wuliuNumber = ''
			},
			closeghPopup() {
				this.ghPopup = false
				this.wuliuNumber = ''
			},
			backGoodXG() {
				this.xgghPopup = true
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
						setTimeout(() => { this.getOrderInfo() }, 500)
						this.wuliuNumber = ''
						this.xgghPopup = false
					} else {
						this.wuliuNumber = ''
						this.xgghPopup = false
						uni.showToast({ title: res.msg, icon: 'none' })
					}
				})
			},
			getUserInfo() {
				let data = { userId: uni.getStorageSync('userId') }
				this.$Request.getT('/app/user/selectUserByUserId', data).then(res => {
					if (res.code == 0) this.userInfo = res.data
					else this.userInfo = {}
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
						setTimeout(function() { that.ghPopup = true }, 500)
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
			callKef() {
				uni.navigateTo({ url: '/my/setting/chat' });
			},
			copy(content) {
				let that = this;
				uni.setClipboardData({
					data: content,
					success: r => { this.$queue.showToast('复制成功'); }
				});
			},
			findExpress() {
				this.$queue.showLoading('查询中...');
				var expressnumber = '';
				if (this.orderInfo.status == 2) expressnumber = this.orderInfo.sellerNumber
				else if (this.orderInfo.status == 5) expressnumber = this.orderInfo.expressNumber
				if (expressnumber) {
					var exNum = expressnumber.slice(0, 2)
					var phNum = this.orderInfo.mobile.slice(7, 11)
					if (exNum == 'SF') expressnumber = expressnumber + ':' + phNum
					else expressnumber = expressnumber + ':' + phNum
				}
				this.$Request.getT('/app/orders/findExpress', { expressNumber: expressnumber }).then(res => {
					if (res.code == 0 || res.status == 0) {
						this.$queue.setData('selectOption', res.data.result);
						uni.navigateTo({ url: '/pages/order/search' });
					} else uni.showToast({ title: res.msg, icon: "none" })
					uni.hideLoading();
				})
			},
			confirmOrder(id) {
				uni.showLoading({ title: '请稍后......' })
				let data = { id: this.orderInfo.id }
				this.$Request.getT('/app/orders/confirm', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.hideLoading();
						uni.showToast({ title: '确认收货成功', icon: 'none' })
						setTimeout(() => { this.getOrderInfo() }, 50)
					} else {
						uni.showToast({ title: res.msg, icon: 'none' })
						uni.hideLoading();
					}
				})
			},
			remindOrder() {
				let that = this;
				let data = { orderId: this.orderInfo.id }
				that.$Request.getT('/app/orders/remind', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.showToast({ title: '已帮您提醒商家尽快发货,请耐心等待', icon: 'none', duration: 2000 })
					} else uni.showToast({ title: res.msg, icon: 'none' })
				})
			},
			backGood() {
				let that = this;
				let data = { id: this.orderInfo.id }
				that.$Request.getT('/app/orders/calculateTheNumberDay', data).then(res => {
					if (res.status == 0 || res.code == 0) {
						that.calculateDaysAndCheckExceed(this.orderInfo.leaseStartDate, this.orderInfo.returnDate)
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
			returnGoods() {
				let that = this;
				if (!this.wuliuNumber) {
					uni.showToast({ title: '请填写物流订单编号', icon: 'none', duration: 2000 })
					return
				}
				let data = { expressNumber: this.wuliuNumber, id: this.orderInfo.id }
				that.$Request.postJson('/app/orders/returnGoods', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.showToast({ title: '商品归还信息提交成功,等待商家收货', icon: 'none', duration: 2000 })
						this.ghPopup = false
						this.wuliuNumber = ''
						setTimeout(() => { this.getOrderInfo() }, 50)
					} else {
						this.wuliuNumber = ''
						this.ghPopup = false
						uni.showToast({ title: res.msg, icon: 'none' })
					}
				})
			},
			daysBetweenDates(date1, date2) {
				var startDate = new Date(date1);
				var endDate = new Date(date2);
				var differenceInMilliseconds = endDate - startDate;
				var differenceInDays = differenceInMilliseconds / (1000 * 60 * 60 * 24);
				return Math.abs(differenceInDays);
			},
			findGoods() {
				let data = {
					id: this.orderInfo.goodsId,
					userId: uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
				}
				this.$Request.getT('/app/goods/findById', data).then(res => {
					if (res.code == 0 || res.status == 0) this.goodInfo = res.data
				})
			},
			orderTextStatus(val) {
				if (val == 0) return '待支付'
				else if (val == 1) return '待发货'
				else if (val == 2) return '待收货'
				else if (val == 3) return '已收货'
				else if (val == 4) return '待归还'
				else if (val == 5) return '归还中'
				else if (val == 6) return '已完成'
				else if (val == 7) return '继续租赁'
				else if (val == 8) return '已买断'
				else if (val == 9) return '已取消'
				else if (val == 10) return '待评价'
			},
			orderTextStatusWa(val) {
				if (val == 0) return '您的订单还未进行支付，请及时处理'
				else if (val == 1) return '订单支付成功，等待商家发货'
				else if (val == 2) return '您的商品已寄出，请随时查看物流信息！'
				else if (val == 3) return '您的商品已收到货'
				else if (val == 4) return '商品使用中~'
				else if (val == 5) return '订单已到期,请尽快归还或者续租！'
				else if (val == 6) return '订单已完成'
				else if (val == 7) return '订单已即将到期，尽快归还或续租~'
				else if (val == 8) return '订单支付成功，等待商家确认'
				else if (val == 9) return '您的订单已取消，如有支付金额将原路退回'
				else if (val == 10) return '订单已完成，给个好评吧~'
			},
			getOrderInfo() {
				let data = { id: this.orderId }
				this.$Request.getT('/app/orders/find', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						this.orderInfo = res.data
						this.getGoodsDays()
						if (this.orderInfo.payWay == 1 || this.orderInfo.payWay == 2 || this.orderInfo.payWay == 3) {
							this.orderPayWay = '微信支付'
						} else if (this.orderInfo.payWay == 4 || this.orderInfo.payWay == 5) {
							this.orderPayWay = '支付宝支付'
						} else if (this.orderInfo.payWay == 6) {
							this.orderPayWay = '余额支付'
						}
						this.findGoods()
					} else uni.showToast({ title: res.msg })
				})
			},
			getGoodsDays() {
				let data = { id: this.goodId, page: 1, limit: 999 }
				this.$Request.getT('/app/leaseDays/getLeaseDaysList', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						if (this.orderInfo) {
							res.data.records.map(item => {
								if (this.orderInfo.dayId == item.id) this.allDate = item.dayName
							})
						}
					}
				})
			},
			navigate(url) { uni.navigateTo({ url: url }) }
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
		height: 300rpx;
		background: linear-gradient(180deg, #E3FAFB 0%, #F0F9FA 70%, transparent 100%);
		pointer-events: none;
		z-index: 0;
	}

	/* ========== 状态卡片 ========== */
	.status-card {
		position: relative;
		margin: 0 24rpx 24rpx;
		background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
		border-radius: 24rpx;
		padding: 40rpx 32rpx;
		color: #FFFFFF;
		overflow: hidden;
		box-shadow: 0 12rpx 40rpx rgba(21, 202, 210, 0.25);
		z-index: 1;
	}

	.status-main {
		display: flex;
		align-items: center;
		position: relative;
		z-index: 2;
	}

	.status-icon-wrap {
		width: 72rpx;
		height: 72rpx;
		background: rgba(255, 255, 255, 0.2);
		backdrop-filter: blur(4px);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 24rpx;
		flex-shrink: 0;
	}

	.status-icon {
		width: 40rpx;
		height: 40rpx;
	}

	.status-title {
		font-size: 36rpx;
		font-weight: 800;
		margin-bottom: 8rpx;
		letter-spacing: 1rpx;
	}

	.status-desc {
		font-size: 26rpx;
		opacity: 0.9;
		line-height: 1.5;
	}

	.status-deco {
		position: absolute;
		top: 0;
		right: 0;
		bottom: 0;
		width: 240rpx;
		pointer-events: none;
	}

	.deco-circle {
		position: absolute;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.08);
	}

	.deco-circle-1 {
		width: 200rpx;
		height: 200rpx;
		top: -60rpx;
		right: -40rpx;
	}

	.deco-circle-2 {
		width: 140rpx;
		height: 140rpx;
		bottom: -40rpx;
		right: 60rpx;
	}

	/* ========== 通用信息卡片 ========== */
	.info-card {
		background: $card-bg;
		border-radius: 24rpx;
		margin: 0 24rpx 24rpx;
		padding: 32rpx;
		box-shadow: 0 8rpx 32rpx $shadow;
		position: relative;
		z-index: 1;
	}

	.card-header-title {
		font-size: 32rpx;
		font-weight: 800;
		color: $text-main;
		margin-bottom: 8rpx;
		letter-spacing: 1rpx;
		position: relative;
		display: inline-block;
	}

	.card-header-title::after {
		content: '';
		position: absolute;
		bottom: -8rpx;
		left: 0;
		width: 40rpx;
		height: 6rpx;
		background: linear-gradient(90deg, $primary, $primary-soft);
		border-radius: 3rpx;
	}

	.card-row {
		display: flex;
		align-items: center;
	}

	.card-row.between {
		justify-content: space-between;
	}

	.row-title {
		font-size: 30rpx;
		font-weight: 700;
		color: $text-main;
	}

	.address-text {
		margin-top: 16rpx;
		font-size: 28rpx;
		color: $text-sub;
		line-height: 1.6;
	}

	.copy-tag {
		background: linear-gradient(135deg, $primary-light 0%, #FFFFFF 100%);
		color: $primary-deep;
		font-size: 22rpx;
		padding: 8rpx 20rpx;
		border-radius: 10rpx;
		font-weight: 600;
		border: 1rpx solid rgba(21, 202, 210, 0.2);
		box-shadow: 0 2rpx 8rpx rgba(21, 202, 210, 0.08);
	}

	.copy-tag-sm {
		background: #F1F5F9;
		color: $text-sub;
		font-size: 20rpx;
		padding: 6rpx 14rpx;
		border-radius: 6rpx;
		margin-left: 16rpx;
		font-weight: 500;
	}

	/* 物流 */
	.express-info {
		display: flex;
		flex-direction: column;
		gap: 6rpx;
	}

	.express-label {
		font-size: 22rpx;
		color: $text-tip;
		font-weight: 500;
	}

	.express-value {
		font-size: 28rpx;
		color: $text-main;
		font-weight: 600;
		font-family: monospace;
	}

	/* ========== 商品区域 ========== */
	.goods-card {
		padding-bottom: 24rpx;
	}

	.goods-row {
		display: flex;
		padding-bottom: 24rpx;
	}

	.goods-img-box {
		width: 160rpx;
		height: 160rpx;
		border-radius: 20rpx;
		overflow: hidden;
		flex-shrink: 0;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.06);
	}

	.goods-img {
		width: 100%;
		height: 100%;
	}

	.goods-info {
		flex: 1;
		margin-left: 24rpx;
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
		margin-top: 8rpx;
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
		font-size: 24rpx;
		color: $text-tip;
		margin-top: 8rpx;
	}

	/* 日期条 */
	.date-bar {
		display: flex;
		align-items: center;
		justify-content: space-between;
		background: linear-gradient(135deg, #F8FAFC 0%, #F1F5F9 100%);
		border-radius: 20rpx;
		padding: 24rpx 32rpx;
		margin-top: 4rpx;
	}

	.date-item {
		text-align: center;
		flex: 1;
	}

	.date-label {
		font-size: 24rpx;
		color: $text-tip;
		margin-bottom: 10rpx;
		font-weight: 500;
	}

	.date-value {
		font-size: 28rpx;
		color: $text-main;
		font-weight: 700;
	}

	.date-value.danger {
		color: $danger;
	}

	.date-arrow {
		flex-shrink: 0;
		padding: 0 16rpx;
		opacity: 0.6;
	}

	/* ========== 费用明细：去边框，用留白分隔 ========== */
	.fee-list {
		display: flex;
		flex-direction: column;
		margin-top: 24rpx;
	}

	.fee-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 18rpx 0;
	}

	.fee-label {
		font-size: 28rpx;
		color: $text-sub;
		font-weight: 500;
	}

	.fee-value {
		font-size: 28rpx;
		color: $text-main;
		font-weight: 600;
	}

	.fee-symbol {
		font-size: 22rpx;
		margin-right: 4rpx;
		font-weight: 700;
	}

	.fee-discount {
		color: #4CAF50;
	}

	.fee-highlight {
		font-size: 36rpx;
		font-weight: 800;
		color: $text-main;
		letter-spacing: -1rpx;
	}

	.fee-total {
		margin-top: 16rpx;
		padding-top: 20rpx;
		position: relative;
	}

	/* 合计上方一条极淡的渐变分隔线，替代实线边框 */
	.fee-total::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 2rpx;
		background: linear-gradient(90deg, transparent, $primary-soft, transparent);
	}

	.fee-total .fee-label {
		font-size: 30rpx;
		font-weight: 700;
		color: $text-main;
	}

	/* ========== 订单信息：去边框，用留白分隔 ========== */
	.info-list {
		display: flex;
		flex-direction: column;
		margin-top: 24rpx;
	}

	.info-item {
		display: flex;
		align-items: center;
		padding: 18rpx 0;
	}

	.info-item.between {
		justify-content: space-between;
	}

	.info-label {
		font-size: 28rpx;
		color: $text-tip;
		font-weight: 500;
		flex-shrink: 0;
	}

	.info-right {
		display: flex;
		align-items: center;
		min-width: 0;
	}

	.info-value {
		font-size: 28rpx;
		color: $text-main;
		font-weight: 500;
		text-align: right;
	}

	.info-value.mono {
		font-family: monospace;
		letter-spacing: 1rpx;
	}

	/* ========== 底部按钮栏 ========== */
	.footer-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: rgba(255, 255, 255, 0.95);
		backdrop-filter: blur(10px);
		border-top: 1rpx solid $border;
		padding-bottom: constant(safe-area-inset-bottom);
		padding-bottom: env(safe-area-inset-bottom);
		z-index: 100;
	}

	.footer-inner {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		gap: 16rpx;
		padding: 20rpx 28rpx;
	}

	.action-btn {
		transition: all 0.2s ease;
	}

	.action-btn:active {
		transform: scale(0.96);
		opacity: 0.85;
	}

	/* 一级：主要操作 */
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
	}

	/* 二级：次要操作 */
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
	}

	/* 三级：弱操作 */
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

	.pay-method-item:last-child {
		border-bottom: none;
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