<template>
	<view>
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<!-- <view class="orderCont" style="margin-top: 22rpx;">
			<view style="padding: 10rpx 25rpx 0rpx 25rpx;">
				<view class="u-flex" style="justify-content: space-between;"
					@click="navigate('/my/address/index?choose=choose')">
					<view class="u-flex-1 text-white margin-left-xs">
						<view class="padding-tb" style="font-weight: 600;font-size: 30rpx;"
							v-if="Object.keys(currentAddress).length == 0">请选择地址</view>
						<view class="margin-top-sm" v-else>
							<view style="color: #333333;font-size: 32rpx;font-weight: bold;">{{currentAddress.userName}}<text
									class="margin-left-sm">{{currentAddress.userPhone}}</text>
							</view>
							<view style="color: #999999;font-size: 28rpx;margin-top: 20rpx;">
								{{currentAddress.province}}{{currentAddress.city}}{{currentAddress.district}}{{currentAddress.addressDetail}}
							</view>
						</view>
					</view>
					<u-icon name="arrow-right" color="#999999"></u-icon>
				</view>
			</view>
			<view style="position: relative;top: 5upx;">
				<image src="/static/images/line.png" style="width: 100%;height: 8rpx;"></image>
			</view>
		</view> -->
		<view class="orderCont" style="margin-top: 22rpx;">
			<view class="" style="padding: 30rpx 25rpx 0rpx 25rpx;">
				<view class="" style="display: flex;justify-content: space-between;">
					<view class="" style="font-weight: bold;font-size: 32rpx;">商家地址：</view>
					<view @click="copy"
						style="background-color:#F5F5F5 ;color: #333333;font-size: 22rpx;width: 70rpx;height: 36rpx;text-align: center;margin-left: 31rpx;">
						复制</view>
				</view>
				<view class="u-flex" style="justify-content: space-between;">
					<view class="u-flex-1 text-white ">
						<view class="margin-top-sm flex justify-between">
							<view style="color: #999999;font-size: 28rpx;">{{shopAddress}}
							</view>
						</view>
					</view>
					<!-- <u-icon name="arrow-right" color="#999999"></u-icon> -->
				</view>
			</view>
			<view style="position: relative;top: 5upx;">
				<image src="/static/images/line.png" style="width: 100%;height: 8rpx;"></image>
			</view>
		</view>
		<!-- 商品信息 -->
		<view class="orderCont" style="font-size: 26rpx;color: #333333;padding: 12rpx;margin-top: 22rpx;">
			<view style="display: flex;">
				<view v-if="goodInfo.coverImg">
					<image :src="goodInfo.coverImg" mode="aspectFit" style="width: 150rpx;height: 150rpx;margin-left: 30rpx;">
					</image>
				</view>
				<view style="padding-left: 22rpx;">
					<view class="text_hid" style="width: 80%;font-size: 30rpx;font-weight: 700;"> {{goodInfo.title || ''}}
					</view>
					<view style="color: #999999;font-size: 24rpx;padding-top: 10rpx;">已选规格：<text
							v-if="orderInfo.isExpress">{{orderInfo.isExpress == 1 ?'包邮':(orderInfo.isExpress == 2)?'不包邮':'自取'}},</text>{{orderInfo.detailJson || ''}}
					</view>
					<view style="color: #999999;font-size: 24rpx;padding: 7rpx 0;">数量：{{orderInfo.number || ''}}</view>
				</view>
			</view>
			<view class="orderDate">
				<view>
					<view style="font-size: 30rpx;color: #333333;padding-bottom: 15rpx;font-weight: bold;">预计起租日</view>
					<view style="font-size: 26rpx;color: #999999;">{{orderInfo.leaseStartDate || ''}}</view>
				</view>
				<image src="../../static/zlImage/jt.png" mode="" style="width: 50rpx;height: 29rpx;"></image>
				<view>
					<view style="font-size: 30rpx;color: #333333;padding-bottom: 15rpx;font-weight: bold;">预计归还日</view>
					<view style="font-size: 26rpx;color: #999999;">{{orderInfo.returnDate || ''}}</view>
				</view>
			</view>
		</view>
		<!-- 费用明细 -->
		<view class="orderCont" style="margin-top: 22rpx;padding: 30rpx;">
			<view style="font-size: 32rpx;font-weight: bold;color: #333333;padding-bottom: 30rpx;">费用明细</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">优惠券</view>
				<view style="color: #666666;font-size: 28rpx;display: flex;align-items: center;"
					@click="navigate(`/my/ticket/myList`,{price:totalPrice,couponId:discountId})">
					{{ticketList&&ticketList.length>0&&!discountAmount?'立即使用':(discountAmount > 0 ?'-'+discountAmount :'暂无优惠券')}}
					<image src="../../static/images/my/right.png" mode="" style="width: 16rpx;height: 24rpx;margin-left: 10rpx;"
						v-if="ticketList&&ticketList.length>0">
					</image>
				</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">日租金</view>
				<view style="color: #666666;font-size: 32rpx;"><text
						style="font-size: 24rpx;">￥</text>{{orderInfo.dailyRent || '0'}}
				</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">租期</view>
				<view style="color: #666666;font-size: 28rpx;">{{zqTime || '0'}}<text>天</text></view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">提货方式</view>
				<view style="color: #666666;font-size: 28rpx;">
					{{(orderInfo.isExpress == 1 ?'包邮':(orderInfo.isExpress == 2)?'不包邮':'自取') || ''}}
				</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;"
				v-if="orderInfo.isExpress == 2&&orderInfo.postagePrice">
				<view style="font-size: 26rpx;color: #666666;">运费</view>
				<view style="color: #666666;font-size: 28rpx;"><text
						style="font-size: 24rpx;">￥</text>{{orderInfo.postagePrice || '0'}}</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">押金</view>
				<view style="color: #666666;font-size: 32rpx;"><text
						style="font-size: 24rpx;">￥</text>{{orderInfo.deposit || '0'}}
				</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">总租金</view>
				<view style="color: #666666;font-size: 32rpx;"><text style="font-size: 24rpx;">￥</text>{{ dayPirce|| '0'}}
				</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;"
				v-if="orderInfo.descrition">
				<view style="font-size: 26rpx;color: #666666;">备注</view>
				<view style="color: #666666;font-size: 30rpx;">{{orderInfo.descrition || ''}}
				</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 0rpx;">
				<view style="font-size: 26rpx;color: #666666;">合计</view>
				<view style="color: #333333;font-size: 38rpx;"><text style="font-size: 24rpx;">￥</text>{{totalPrice || '0'}}
				</view>
			</view>

		</view>
		<!-- 备注 -->
		<!-- <view class="orderCont" style="margin-top: 22rpx;padding: 30rpx;font-size: 24rpx;color: #666666;">
			<view style="font-size: 32rpx;font-weight: bold;color: #333333;padding-bottom: 30rpx;">备注</view>
			<view style="display: flex;justify-content: space-between;align-items: center;">
				<u-input v-model="remakText" :type="type" :border="border" :height="height" :auto-height="autoHeight"
					placeholder="建议留言前先与商家沟通确认" />
			</view>
		</view> -->
		<view style="height: 225rpx;"></view>
		<!-- 底部按钮 -->
		<view class="footerBtn">
			<view class="footer">
				<image v-if="showAgree" @tap="isShowAgree"
					src="https://api.shengqianxiong.com.cn/img/20201112/669aa8946cfb4ebdb459d57193c0ebca.png"
					style="width: 34upx;height: 34upx;"></image>
				<image v-else @tap="isShowAgree"
					src="https://api.shengqianxiong.com.cn/img/20201112/1e9102fc891f4d86a13c7b2ba6921cba.png"
					style="width: 34upx;height: 34upx;"></image>
				<text class="margin-left-xs">我已阅读并同意</text>
				<!-- 协议地址 -->
				<navigator url="/my/setting/xieyi?type=520" open-type="navigate" style="color: #15CAD2;">《租赁及服务相关协议》</navigator>
				与
				<navigator url="/my/setting/xieyi?type=516" open-type="navigate" style="color: #15CAD2;">《用户协议》</navigator>
			</view>
			<view style="display: flex;align-items: center;justify-content: space-between;margin-top: 30rpx;">
				<view style="display: flex;align-items: center;">
					<text style="color: #999999;font-size: 32rpx;">合计：</text>
					<view style="font-weight: bold;color: #E72D30;">
						￥<text style="font-size: 48rpx;">{{totalPrice || '0'}}</text>
					</view>
				</view>
				<view class="btn_zs" @click="goPay">去支付</view>
			</view>
		</view>
		<!-- 支付弹框 -->
		<u-popup v-model="showpay" mode="bottom" border-radius="14" :closeable="true" @close="close">
			<view
				style="width: 100%;text-align: center;font-size:38rpx;font-weight: bold;margin-top:15rpx;margin-bottom: 80rpx;">
				选择支付方式
			</view>
			<view style="display: flex;height: 100upx;align-items: center;padding: 20upx 30rpx;"
				v-for="(item,index) in payList" :key='index'>
				<view style="display: flex;width:80%;align-items: center;">
					<image :src="item.image" style="width: 55upx;height: 55upx;border-radius: 50upx;"></image>
					<view style="font-size: 30upx;margin-left: 20upx;width: 70%;">{{item.name}}
					</view>
				</view>
				<view style="width: 20%;display: flex;justify-content: flex-end;">
					<radio-group name="payWay" style="margin-left: 20upx;" @tap='selectWay(item.id)'>
						<label class="tui-radio">
							<radio color="#15CAD2" :checked="payWay === item.id ? true : false" />
						</label>
					</radio-group>
				</view>
			</view>
			<view style="display: flex;height: 100upx;align-items: center;padding: 20upx 0;justify-content: center;"
				v-if="userInfo.balance>0">
				<image src="/static/images/lingqian.png" style="width: 55upx;height: 55upx;border-radius: 50upx;">
				</image>
				<view style="font-size: 30upx;margin-left: 20upx;width: 70%;">
					余额
				</view>
				<radio-group name="openWay" style="margin-left: 45upx;" @tap='selectWay(6)'>
					<label class="tui-radio">
						<radio color="#15CAD2" :checked="payWay === 6 ? true : false" />
					</label>
				</radio-group>
			</view>
			<view
				style="width: 690rpx;height: 80rpx;background:#15CAD2;text-align: center;line-height: 80rpx;border-radius: 50rpx;margin: 30rpx;color: #ffffff;"
				@tap="addOrder()">确认支付</view>
		</u-popup>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				orderId: '',
				backText: '确认订单',
				backTextStyle: {
					color: '#333333',
					// fontweighth:'bold',
				},
				remakText: '', //备注
				type: 'textarea',
				border: false,
				height: 100,
				autoHeight: true,
				showAgree: false, //协议是否选择
				showpay: false, //支付弹框
				payList: [{
					image: '/static/zlImage/image/icon_weixin.png',
					name: '微信',
					id: 2
				}, {
					image: '/static/zlImage/image/zhifubao.png',
					name: '支付宝',
					id: 3
				}],
				payWay: 2,
				goodInfo: {},
				goodDays: [],
				isVip: uni.getStorageSync('isVip'), // isVip 1是会员 2不是
				ticketList: [],
				userInfo: {},
				discountId: '',
				discountAmount: '',
				dayPirce: '', //日租金
				zqTime: '', //租期
				currentAddress: {}, //地址
				currentDate: '', //当前日期
				futureDate: '', //归还日期
				zqDays: '', //租期天数
				dayId: '', //租赁天数Id
				goodData: {}, //上个页面参数
				orderInfo: {}, //订单列表参数
				exceedPrice: '', //超出租期补费金额
				shopAddress: ''
			}
		},
		computed: {
			totalPrice() {
				let totalprice = 0;
				let finalPrice = 0; //最终取的价格
				totalprice = this.orderInfo.payMoney
				if (this.discountAmount) { //优惠券
					totalprice = totalprice - Number(this.discountAmount)
				}
				if (this.orderInfo.postagePrice) {
					this.dayPirce = Number(this.orderInfo.grossRent) - Number(this.orderInfo.postagePrice)
				} else {
					this.dayPirce = Number(this.orderInfo.grossRent)
				}
				if (Number(totalprice) > 0) {
					finalPrice = totalprice.toFixed(2)
				}
				return finalPrice
			}
		},
		onLoad(option) {
			if (option) {
				this.orderId = option.orderId;
			}
			if (option.exceedPrice) {
				this.exceedPrice = option.exceedPrice
			}
			this.shopAddress = uni.getStorageSync('shopAddressZt')
			this.getUserInfo()
			this.getOrderInfo()
			this.getMyList()
			this.getAddlist()
			// 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分
			// #ifdef MP-WEIXIN
			this.payList = [{
				image: '/static/zlImage/image/icon_weixin.png',
				name: '微信',
				id: 3
			}]
			this.payWay = 3;
			// #endif
			// #ifdef APP
			this.payList = [{
				image: '/static/zlImage/image/icon_weixin.png',
				name: '微信',
				id: 1
			}, {
				image: '/static/zlImage/image/zhifubao.png',
				name: '支付宝',
				id: 4
			}]
			this.payWay = 1;
			// #endif
			// #ifdef H5
			//公众号是否自动登录
			this.$Request.get('/app/common/type/108').then(res => {
				if (res.data && res.data.value && res.data.value == '是') {
					let ua = navigator.userAgent.toLowerCase();
					console.log(ua.indexOf('micromessenger'), "ua.indexOf('micromessenger')")
					if (ua.indexOf('micromessenger') !== -1) {
						this.payList = [{
							image: '/static/zlImage/image/icon_weixin.png',
							name: '微信',
							id: 2
						}];
						this.payWay = 2;
					} else {
						this.payList = [{
							image: '/static/zlImage/image/zhifubao.png',
							name: '支付宝',
							id: 5
						}];
						this.payWay = 5;
					}
				} else {
					this.payList = [{
						image: '/static/zlImage/image/zhifubao.png',
						name: '支付宝',
						id: 5
					}];
					this.payWay = 5;
				}
			})
			// #endif
		},
		onShow() {
			uni.$once('getUserAddress', data => {
				this.currentAddress = data
			})
			uni.$once('getDiscountId', data => {
				let datas = {
					couponUserId: data.discountId,
					id: this.orderInfo.goodsId
				}
				if (data.discountId) {
					this.$Request.getT('/app/orders/checkCoupon', datas).then(res => {
						if (res.code == 0 || res.status == 0) {
							this.discountId = data.discountId
							this.discountAmount = data.price
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
						uni.hideLoading()
					})
				} else {
					this.discountId = ''
					this.discountAmount = ''
				}
				console.log(data, "data")
			})
		},
		methods: {
			copy() {
				let that = this;
				uni.setClipboardData({
					data: that.shopAddress,
					success: r => {
						this.$queue.showToast('复制成功');
					}
				});
			},
			getOrderInfo() {
				let data = {
					id: this.orderId
				}
				this.$Request.getT('/app/orders/find', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						this.orderInfo = res.data
						// this.getGoodsDays()
						// 根据起租日期和结束日期计算天数
						// this.allDate = this.daysBetweenDates(this.orderInfo.leaseStartDate, this.orderInfo.returnDate);
						// 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分
						if (this.orderInfo.payWay == 1 || this.orderInfo.payWay == 2 || this.orderInfo.payWay == 3) {
							this.orderPayWay = '微信支付'
						} else if (this.orderInfo.payWay == 4 || this.orderInfo.payWay == 5) {
							this.orderPayWay = '支付宝支付'
						} else if (this.orderInfo.payWay == 6) {
							this.orderPayWay = '余额支付'
						}
						this.findGoods()
					} else {
						uni.showToast({
							title: res.msg
						})
					}
				})
			},
			close() {
				this.showpay = false
			},
			goPay() {
				console.log(Object.keys(this.currentAddress).length, "this.currentAddress")
				if (Object.keys(this.currentAddress).length == 0 && this.orderInfo.isExpress != 3) {
					uni.showToast({
						title: '请添加收货地址',
						icon: 'none'
					})
					return
				}
				if (!this.showAgree) {
					uni.showToast({
						title: '请阅读并同意相关协议',
						icon: 'none'
					})
					return
				}
				this.showpay = true
			},
			// 支付订单
			addOrder() {
				let payData = {
					orderId: this.orderInfo.id,
					classify: this.payWay,
				}
				console.log(payData, "payData")
				// 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分
				if (this.payWay == 3) { //微信
					payData.money = this.totalPrice
					payData.orderType = this.orderInfo.orderType // 5租赁 6买断
					this.wxPay(payData)
				} else if (this.payWay == 1) {
					payData.money = this.totalPrice
					payData.orderType = this.orderInfo.orderType // 5租赁 6买断
					this.wxPaApp(payData)
				} else if (this.payWay == 2) {
					payData.money = this.totalPrice
					payData.orderType = this.orderInfo.orderType // 5租赁 6买断
					this.wxPayH5(payData)
				} else if (this.payWay == 4) { //zfb
					payData.money = this.totalPrice
					payData.orderType = this.orderInfo.orderType // 5租赁 6买断
					this.zhfPayApp(payData)
				} else if (this.payWay == 5) {
					payData.money = this.totalPrice
					payData.orderType = this.orderInfo.orderType // 5租赁 6买断
					this.zhfPay(payData)
				} else if (this.payWay == 6) {
					this.lqPay(payData)
				}
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
						that.showpay = false
						setTimeout(function() {
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
				let that = this
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
									that.showpay = false
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
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
			},
			// 零钱支付
			lqPay(payData) {
				uni.showLoading({
					title: '支付中...'
				})
				this.$Request.getT('/app/orders/changePay', payData).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.showToast({
							title: '支付成功',
							icon: 'none'
						})
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/order/index'
							})
						}, 500)
						this.showpay = false
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
					uni.hideLoading()
				})
			},
			isShowAgree() {
				//是否选择协议
				this.showAgree = !this.showAgree;
			},
			selectWay: function(id) {
				this.payWay = id;
			},
			// 获取用户信息
			getUserInfo() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$Request.getT('/app/user/selectUserByUserId', data).then(res => {
					if (res.code == 0) {
						this.userInfo = res.data
					} else {
						this.userInfo = {}
					}
				})
			},
			// 获取商品详情
			findGoods() {
				let data = {
					id: this.orderInfo.goodsId,
					userId: uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
				}
				this.$Request.getT('/app/goods/findById', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						this.goodInfo = res.data
						this.getGoodsDays()
					}
				})
			},
			// 获取商品租赁天数
			getGoodsDays() {
				let that = this;
				let data = {
					id: this.orderInfo.goodsId,
					page: 1,
					limit: 999
				}
				this.$Request.getT('/app/leaseDays/getLeaseDaysList', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						that.goodDays = res.data.records
						let zqDays = that.zqDays
						res.data.records.map(item => {
							if (this.orderInfo.dayId == item.id) {
								that.zqTime = item.dayName
							}
						})
					}
				})
			},
			// 获取个人优惠券
			getMyList() {
				let data = {
					page: 1,
					size: 999,
					userId: uni.getStorageSync('userId'),
					type: 1,
					status: 1
				}
				this.$Request.getT('/app/selfCouponUser/userList', data).then(res => {
					if (res.status == 0) {
						this.ticketList = res.data.records;
					}
				})
			},
			// 获取地址
			getAddlist(page) {
				this.$Request.getT('/app/address/getAddressList', {
					page: 1,
					limit: 999
				}).then(res => {
					if (res.data.data.records.length > 0) {
						res.data.data.records.map(item => {
							if (item.addressDefault == 1) {
								this.currentAddress = item
							}
						})
					}
				})
			},
			// 获取租赁日期区间
			getCurrentAndFutureDates(zqTime) {
				const today = new Date();
				const sevenDaysLater = new Date(today);
				if (this.zqTime != '买断') {
					sevenDaysLater.setDate(sevenDaysLater.getDate() + Number(zqTime));
					this.currentDate = this.formatDate(today);
					this.futureDate = this.formatDate(sevenDaysLater);
				}
				console.log(this.currentDate, "currentDatecurrentDate")
				console.log(this.futureDate, "futureDatefutureDatefutureDate")
			},
			formatDate(date) {
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0');
				const day = String(date.getDate()).padStart(2, '0');
				return `${year}-${month}-${day}`;
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #F8F5F8;
	}

	.footer {
		font-size: 24upx;
		color: #666666;
		display: flex;
		align-items: center;
	}

	.text_hid {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
	}

	.orderCont {
		border-radius: 24rpx;
		width: calc(100% - 40rpx);
		margin: auto;
		background-color: #fff;
		overflow: hidden;
	}

	.orderStatus {
		background-color: #15CAD2;
		border-radius: 24rpx;
		width: calc(100% - 40rpx);
		margin: auto;
		color: #fff;
		padding-bottom: 31rpx;
		margin-top: 30rpx;
	}

	.status_title {
		display: flex;
		align-items: center;
		padding: 28rpx 0 21rpx 38rpx;
		font-size: 32rpx;
		font-weight: bold;
	}

	.orderDate {
		background-color: #F7F8FA;
		display: flex;
		border-radius: 24rpx;
		width: calc(100% - 40rpx);
		margin: auto;
		padding: 22rpx 40rpx;
		justify-content: space-between;
		align-items: center;
		text-align: center;
		margin-top: 25rpx;
		margin-bottom: 15rpx;
	}

	.footerBtn {
		width: 100%;
		background-color: #fff;
		height: 200rpx;
		position: fixed;
		bottom: 0;
		padding: 42rpx 30rpx 20rpx 30rpx;
	}

	.btn_gray {
		border: solid 1rpx #CCCCCC;
		padding: 20rpx 38rpx;
		font-size: 26rpx;
		color: #999999;
		border-radius: 35rpx;
	}

	.btn_zs {
		border: solid 1rpx #15CAD2;
		padding: 20rpx 80rpx;
		font-size: 26rpx;
		color: #ffffff;
		background-color: #15CAD2;
		border-radius: 48rpx;
		margin-left: 17rpx;
	}

	.btn_zs_border {
		border: solid 1rpx #15CAD2;
		padding: 20rpx 38rpx;
		font-size: 26rpx;
		color: #15CAD2;
		border-radius: 35rpx;
		margin-left: 17rpx;
	}
</style>