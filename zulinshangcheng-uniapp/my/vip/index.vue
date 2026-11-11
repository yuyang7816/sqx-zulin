<template>
	<view>
		<view class="">
			<view class="margin-lr margin-top" style="position: relative;border-radius: 10upx;overflow: hidden;">
				<view class="" style="position: relative;">
					<image src="/static/zlImage/vips.png" style="width: 100%;height: 250rpx;"></image>
					<view class=""
						style="position: absolute;color:rgb(223, 197, 167);z-index: 2;top: 8%;left:12%;font-weight:bold;font-size: 34rpx;">
						超级会员
					</view>
				</view>
				<view class=" u-flex u-p-l-30 u-p-t-80 u-p-b-30" style="position: absolute;top: 30rpx;width: 100%;">
					<view class="u-m-r-20">
						<u-avatar :src="userInfo.avatar?userInfo.avatar:'/static/logo.png'" size="80"></u-avatar>
					</view>
					<view class="u-flex-1 ">
						<view class="u-font-16 text-white text-bold" style="color: #C59D7C;">
							{{userInfo.userName?userInfo.userName:'匿名'}}
						</view>
						<view class="u-font-10 text-bold" style="margin-top: 10rpx;color: #C59D7C;font-size: 28rpx;"
							v-if="userInfo.vipEndTime && userInfo.isVip">
							有效期：{{userInfo.vipEndTime}}</view>
						<view class="u-font-12 u-m-t-10 u-tips-color" style="color: #C59D7C;" v-if="!userInfo.isVip">您目前还未开通会员
						</view>
					</view>
				</view>
			</view>
			<view class="" style="width: calc(100% - 60rpx);margin: auto;">
				<scroll-view style="white-space: nowrap;flex-wrap: nowrap;" :scroll-x="true" :scroll-with-animation="true"
					:scroll-into-view="scrollIntoView">
					<view class="check_vip flex sb">
						<view class="item flex-c alic" :class="{ item_active: index === current }" v-for="(item, index) in vipList"
							:key="index" :id="`vip_${index}`" @click="chooseThis(index)">
							<view>{{ item.vipName }}</view>
							<v-price-format :custom-style="{ color: '#DFC5A7', fontSize: '38rpx' }">{{ item.money }}</v-price-format>
						</view>
					</view>
				</scroll-view>
			</view>

			<view class="padding-tb radius margin-top margin-lr" style="background-color: #343339;border-radius: 10upx;">
				<view class="text-center text-xl text-bold " style="color: #CAB49C;">会员特权</view>
				<view class="flex  flex-wrap">
					<view v-for="(item,index) in MemberList" :key="index"
						style="width: 33%;text-align: center;margin-top: 34upx;">
						<image :src="item.memberImg" mode="" style="margin: 0 auto;height: 45rpx;width: 45rpx;"></image>
						<view class="grid-text margin-top-sm" style="color: #DFC5A7;">{{item.memberName}}</view>
					</view>
				</view>
			</view>
		</view>
		<view style="height: 110rpx;"></view>
		<view class="flex justify-between cu-bar foot bg padding-lr" v-if="!isVip">
			<view style="color: #DFC5A7;">
				应付：<text style="font-size: 38upx;margin-top: 8upx;">{{payMoney}}</text>元
			</view>
			<view class="">
				<u-button :custom-style="customStyle" @click="showpay = true" shape="circle" :hair-line="false">立即开通
				</u-button>
			</view>
		</view>

		<!-- 支付方式 -->
		<u-popup v-model="showpay" mode="bottom" :closeable="closeable">
			<view class="popup_pay">
				<view style="background-color: #fff;">
					<view style="padding: 0 20upx;margin-top: 60rpx;margin-bottom: 20rpx;">
						<view style="display: flex;height: 100upx;align-items: center;padding: 20upx 0;justify-content: center;"
							v-for="(item,index) in openLists" :key='index'>
							<image :src="item.image" style="width: 55upx;height: 55upx;border-radius: 50upx;">
							</image>
							<view style="font-size: 30upx;margin-left: 20upx;width: 70%;">
								{{item.name}}
							</view>
							<radio-group name="openWay" style="margin-left: 45upx;" @tap='selectWay(item.id)'>
								<label class="tui-radio">
									<radio color="#15CAD2" :checked="openWay === item.id ? true : false" />
								</label>
							</radio-group>
						</view>
						<!-- <view style="display: flex;height: 100upx;align-items: center;padding: 20upx 0;justify-content: center;"
							v-if="userInfo.balance>0">
							<image src="/static/images/lingqian.png" style="width: 55upx;height: 55upx;border-radius: 50upx;">
							</image>
							<view style="font-size: 30upx;margin-left: 20upx;width: 70%;">
								余额
							</view>
							<radio-group name="openWay" style="margin-left: 45upx;" @tap='selectWay(0)'>
								<label class="tui-radio">
									<radio color="#15CAD2" :checked="openWay === 0 ? true : false" />
								</label>
							</radio-group>
						</view> -->
					</view>
				</view>
				<view class="pay_btn" @click="buyVip()">确认支付</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				vipExpirationTime: '',
				showpay: false,
				closeable: true,
				openLists: [],
				customStyle: {
					width: '250rpx',
					color: '#402321',
					background: "#DFC5A7",
					border: 0,
					fontWeight: '700'
				},
				avatar: '',
				userName: '匿名',
				vipList: [],
				selNum: 1,
				newPrice: 0,
				money: 0,
				price: 0,
				MemberList: [],
				isVip: false,
				openWay: 3,
				userInfo: {},
				scrollIntoView: '',
				current: 0,
				payMoney: '',
				vipList: [],
				vipDetailsId: ''
			}
		},
		onLoad() {
			// 0余额支付 1app  2 二维码支付  3小程序 公众号支付 4支付宝APP支付 5支付宝H5支付
			// #ifdef MP-WEIXIN
			this.openLists = [{
				image: '/static/zlImage/image/icon_weixin.png',
				name: '微信',
				id: 3
			}]
			this.openWay = 3;
			this.paymentType = 1
			// #endif
			// #ifdef APP
			this.openLists = [{
				image: '/static/zlImage/image/icon_weixin.png',
				name: '微信',
				id: 1
			}, {
				image: '/static/zlImage/image/zhifubao.png',
				name: '支付宝',
				id: 4
			}]
			this.openWay = 1;
			this.paymentType = 2
			// #endif
			// #ifdef H5
			this.paymentType = 1
			//公众号是否自动登录  237
			this.$Request.get('/app/common/type/108').then(res => {
				if (res.data && res.data.value && res.data.value == '是') {
					let ua = navigator.userAgent.toLowerCase();
					console.log(ua.indexOf('micromessenger'), "ua.indexOf('micromessenger')")
					if (ua.indexOf('micromessenger') !== -1) {
						this.openLists = [{
							image: '/static/zlImage/image/icon_weixin.png',
							name: '微信',
							id: 3
						}];
						this.openWay = 3;
					} else {
						this.openLists = [{
							image: '/static/zlImage/image/zhifubao.png',
							name: '支付宝',
							id: 5
						}];
						this.openWay = 5;
					}
				} else {
					this.openLists = [{
						image: '/static/zlImage/image/zhifubao.png',
						name: '支付宝',
						id: 5
					}];
					this.openWay = 5;
				}
			})
			// #endif
		},
		onShow() {
			this.getUserInfo()
			this.getVipList()
			this.selectVipList()
		},
		methods: {
			selectWay(id) {
				this.openWay = id
				console.log(this.openWay, "this.openWay")
			},
			chooseThis(index) {
				this.current = index
				this.$nextTick(() => {
					this.scrollIntoView = `vip_${index}`
				})
				this.vipDetailsId = this.vipList[this.current].id
				this.payMoney = this.vipList[this.current].money.toFixed(2) || '0.00'
			},
			// 获取用户信息
			getUserInfo() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$Request.getT('/app/user/selectUserByUserId', data).then(res => {
					if (res.code == 0) {
						this.userInfo = res.data
						this.userInfo.vipEndTime = this.userInfo.vipEndTime.slice(0, 10);
						uni.setStorageSync('isVip', res.data.isVip)
					} else {
						this.userInfo = {}
					}
				})
			},
			// 查询会员分类
			getVipList() {
				this.$Request.getT('/app/vipDetails/getVipDetailsList', {
					page: 1,
					limit: 999
				}).then(res => {
					if (res.code == 0) {
						this.vipList = res.data.records
						this.vipDetailsId = this.vipList[0].id
						this.payMoney = this.vipList[0].money.toFixed(2) || '0.00'
					}
				})
			},
			// 获取用户权益
			selectVipList() {
				this.$Request.getT('/app/member/selectMemberList').then(res => {
					if (res.code == 0) {
						this.MemberList = res.data
					}
				})
			},
			// 购买优惠券
			buyVip() {
				uni.showLoading({
					title: '支付中'
				})
				let data = {
					money: this.payMoney,
					paymentType: this.paymentType,
					vipDetailsId: this.vipDetailsId,
					businessType: 2,
					classify: this.openWay //支付方式 0余额支付 1app  2 二维码支付  3小程序 公众号支付 4支付宝APP支付 5支付宝H5支付
				}
				console.log(data, "购买会员")
				this.$Request.postJson('/app/vipDetails/userBuyVip', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						if (this.openWay == 0) { //零钱支付
							uni.showToast({
								title: '开通成功',
								icon: 'none'
							})
						} else if (this.openWay == 5) {
							this.zhfPay(res.data)
						} else if (this.openWay == 4) {
							this.setPayment('alipay', res.data);
						} else if (this.openWay == 1) {
							this.setPayment('wxpay', JSON.stringify(res.data));
						} else if (this.openWay == 3) {
							let paymentParams = res.data
							console.log(res.data, "2222")
							// 发起支付
							uni.requestPayment({
								appId: paymentParams.appid,
								timeStamp: paymentParams.timestamp.toString(),
								nonceStr: paymentParams.noncestr,
								package: paymentParams.package,
								signType: paymentParams.signType,
								paySign: paymentParams.sign,
								success: function(res) {
									console.log('支付成功');
									setTimeout(function() {
										uni.switchTab({
											url: '/pages/order/index'
										})
									}, 500)
								},
								fail: function(err) {
									console.log('支付失败', err);
								}
							});
						}
						this.showpay = false
						this.current = 0
						setTimeout(() => {
							this.getUserInfo()
						}, 50)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
					uni.hideLoading()
				})
			},
			// 支付宝APP支付
			zhfPayApp(payData) {
				let that = this;
				uni.showLoading({
					title: '支付中...'
				})
				this.$Request.postT('/app/aliPay/payOrder', payData).then(
					res => {
						uni.hideLoading()
						if (res.code == 0) {
							that.setPayment('alipay', res.data);
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
			},
			// 支付宝H5支付
			zhfPay(payData) {
				uni.showLoading({
					title: '支付中...'
				})
				this.$Request.postT('/app/aliPay/payOrder', payData).then(
					res => {
						uni.hideLoading()
						if (res.code == 0) {
							const div = document.createElement('div')
							div.innerHTML = res.data //此处form就是后台返回接收到的数据
							document.body.appendChild(div)
							document.forms[0].submit()
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
			},
			// 微信H5支付
			wxPayH5(payData) {
				let that = this;
				uni.showLoading({
					title: '支付中...'
				})
				this.$Request.postT('/app/wxPay/payOrder', payData).then(
					res => {
						uni.hideLoading()
						if (res.code == 0) {
							that.callPay(res.data);
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
			},
			// H5支付
			callPay: function(response) {
				if (typeof WeixinJSBridge === "undefined") {
					if (document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
					} else if (document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
						document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
					}
				} else {
					this.onBridgeReady(response);
				}
			},
			// 微信app支付
			wxPaApp(payData) {
				let that = this;
				uni.showLoading({
					title: '支付中...'
				})
				this.$Request.postT('/app/wxPay/payOrder', payData).then(
					res => {
						uni.hideLoading()
						if (res.code == 0) {
							console.log(JSON.stringify(res.data), "JSON.stringify(red.data)")
							that.setPayment('wxpay', JSON.stringify(res.data));
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
			},
			// 微信app支付
			setPayment(name, order) {
				let that = this;
				uni.requestPayment({
					provider: name,
					orderInfo: order, //微信、支付宝订单数据
					success: function(res) {
						// uni.removeStorageSync('EditAddress')
						uni.showLoading({
							title: '支付成功',
							icon: 'none'
						});
						uni.hideLoading();
						setTimeout(function() {
							that.current = 0
							uni.switchTab({
								url: '/pages/order/index'
							})
						}, 500)
						console.log("成功成功")
					},
					fail: function(err) {
						uni.hideLoading();
						console.log(err, "失败失败")
					}
				});
			},
			// 小程序微信支付
			wxPay(payData) {
				let that = this;
				uni.showLoading({
					title: '支付中...'
				})
				this.$Request.postT('/app/wxPay/payOrder', payData).then(
					res => {
						uni.hideLoading()
						if (res.code == 0) {
							let paymentParams = res.data
							console.log(res.data, "2222")
							// 发起支付
							uni.requestPayment({
								appId: paymentParams.appid,
								timeStamp: paymentParams.timestamp.toString(),
								nonceStr: paymentParams.noncestr,
								package: paymentParams.package,
								signType: paymentParams.signType,
								paySign: paymentParams.sign,
								success: function(res) {
									console.log('支付成功');
									setTimeout(function() {
										that.current = 0
										uni.switchTab({
											url: '/pages/order/index'
										})
									}, 500)
								},
								fail: function(err) {
									console.log('支付失败', err);
								}
							});
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
			},
			// 支付宝H5支付
			zhfPay(data) {
				const div = document.createElement('div')
				div.innerHTML = data //此处form就是后台返回接收到的数据
				document.body.appendChild(div)
				document.forms[0].submit()
			},
		},

		computed: {

		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: rgb(56, 55, 62);
	}

	.check_vip {
		text-align: center;
		margin-top: 20rpx;

		.item_active {
			background-color: #343339 !important;
			color: #DFC5A7;
		}

		.item {
			text-align: center;
			padding-top: 62rpx;
			padding-bottom: 80rpx;
			border-radius: 30rpx;
			width: 213rpx;
			background-color: #403f46;
			height: 230rpx;
			box-sizing: border-box;
			min-width: 213rpx;
			margin-right: 25rpx;
			color: #DFC5A7;

			view {
				&:first-child {
					margin-bottom: 26rpx;
					font-weight: 700;
				}
			}
		}

		.active_ {
			background-color: #EEF8FF;
			// border: 2rpx solid $theme-color;
		}

	}

	.bg {
		background-color: #1E1F31;
	}

	.btn {
		width: 100%;
		height: 88upx;
		background: linear-gradient(0deg, #af8262 0%, #cab49c 100%);
		border-radius: 44upx;
		text-align: center;
		line-height: 88upx;
		margin-top: 40upx;
		font-size: 34upx;
		font-weight: 600;
		color: #402321;
	}

	.active {
		border: 1px solid #cab49c !important;
	}

	.popup_pay {
		width: 100%;
		position: relative;
		padding-bottom: 45rpx;
	}

	.pay_btn {
		width: 90%;
		margin: 0 auto;
		text-align: center;
		background: #15CAD2;
		height: 80rpx;
		border-radius: 16rpx;
		color: #ffffff;
		line-height: 80rpx;

	}
</style>