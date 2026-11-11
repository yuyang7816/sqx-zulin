<template>
	<view>
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<!-- <view class="orderCont" style="margin-top: 22rpx;">
			<view style="padding: 10rpx 25rpx 0rpx 25rpx;">
				<view class="u-flex" style="justify-content: space-between;">
					<view class="u-flex-1 text-white margin-left-xs">
						<view class="margin-top-sm">
							<view style="color: #333333;font-size: 32rpx;font-weight: bold;">{{currentAddress.userName}}<text
									class="margin-left-sm">{{currentAddress.userPhone}}</text>
							</view>
							<view style="color: #999999;font-size: 28rpx;margin-top: 20rpx;">
								{{currentAddress.province}}{{currentAddress.city}}{{currentAddress.district}}{{currentAddress.addressDetail}}
							</view>
						</view>
					</view>
				</view>
			</view>
			<view style="position: relative;top: 5upx;">
				<image src="/static/images/line.png" style="width: 100%;height: 8rpx;"></image>
			</view>
		</view> -->
		<view class="orderCont" style="font-size: 26rpx;color: #333333;padding: 30rpx;margin-top: 22rpx;"
			v-if="userInfo.isAuth == 0" @click="navigate('/my/realName')">
			<view style="display: flex;align-items: center;justify-content: space-between;">
				<view>
					<view style="font-size: 32rpx;font-weight: 700;">完善您的身份信息</view>
					<view style="font-size: 28rpx;color: #999999;margin-top: 14rpx;">填写资料，加速审核</view>
				</view>
				<u-icon name="arrow-right" color="#999999"></u-icon>
			</view>
		</view>
		<!-- 商品信息 -->
		<view class="orderCont" style="font-size: 26rpx;color: #333333;padding: 12rpx;margin-top: 22rpx;">
			<view style="display: flex;">
				<view>
					<image :src="goodInfo.coverImg" mode="aspectFit" style="width: 150rpx;height: 150rpx;margin-left: 30rpx;">
					</image>
				</view>
				<view style="padding-left: 22rpx;">
					<view class="text_hid" style="width: 80%;font-size: 30rpx;font-weight: 700;"> {{goodInfo.title || ''}}
					</view>
					<view style="color: #999999;font-size: 24rpx;padding-top: 10rpx;">已选规格：<text
							v-if="goodInfo.isExpress">{{goodInfo.isExpress == 1 ?'包邮':(goodInfo.isExpress == 2)?'不包邮':'自取'}},</text>{{goodData.detailJson || ''}}
					</view>
					<view style="color: #999999;font-size: 24rpx;padding: 7rpx 0;">数量：{{goodData.number || ''}}</view>
				</view>
			</view>
			<view class="orderDate" v-if="zqTime != '买断'">
				<view>
					<view style="font-size: 30rpx;color: #333333;padding-bottom: 15rpx;font-weight: bold;">预计起租日</view>
					<view style="font-size: 26rpx;color: #999999;">{{goodData.returnDate || ''}}</view>
				</view>
				<image src="../../static/zlImage/jt.png" mode="" style="width: 50rpx;height: 29rpx;"></image>
				<view>
					<view style="font-size: 30rpx;color: #333333;padding-bottom: 15rpx;font-weight: bold;">预计归还日</view>
					<view style="font-size: 26rpx;color: #999999;">{{futureDate || ''}}</view>
				</view>
			</view>
			<view style="font-size: 24rpx;color: #999999;padding: 10rpx 30rpx;" v-if="goOnText">
				提示:若上次订单超出归还日期尚未归还，则当前选择归还日期需叠加超出天数。</view>
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
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;"
				v-if="zqTime != '买断'">
				<view style="font-size: 26rpx;color: #666666;">日租金</view>
				<view style="color: #666666;font-size: 32rpx;"><text style="font-size: 24rpx;">￥</text>{{dayPirce || '0'}}
				</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">租期</view>
				<view style="color: #666666;font-size: 28rpx;">{{zqTime || '0'}}<text v-if="zqTime != '买断'">天</text></view>
			</view>
			<!-- <view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">提货方式</view>
				<view style="color: #666666;font-size: 28rpx;" v-if="goodData.thIndex == 1">包邮</view>
				<view style="color: #666666;font-size: 28rpx;" v-if="goodData.thIndex == 2">
					<text style="font-size: 24rpx;">￥</text>{{goodInfo.postagePrice}}
				</view>
				<view style="color: #666666;font-size: 28rpx;" v-if="goodData.thIndex == 3">自提</view>
			</view> -->
			<!-- <view class="" style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;"
				v-if="goodInfo.isExpress == 2&&goodInfo.postagePrice">
				<view class="" style="font-size: 26rpx;color: #666666;">运费</view>
				<view class="" style="color: #666666;font-size: 28rpx;"><text
						style="font-size: 24rpx;">￥</text>{{goodInfo.postagePrice}}</view>
			</view> -->
			<!-- <view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">押金</view>
				<view style="color: #666666;font-size: 32rpx;"><text
						style="font-size: 24rpx;">￥</text>{{Number(goodInfo.depositMoney?goodInfo.depositMoney:0) * Number(goodData.goodsNum?goodData.goodsNum:0) || '0'}}
				</view>
			</view> -->
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 50rpx;">
				<view style="font-size: 26rpx;color: #666666;">总租金</view>
				<view style="color: #666666;font-size: 32rpx;"><text style="font-size: 24rpx;">￥</text>{{goodPrice|| '0'}}
				</view>
			</view>
			<view style="display: flex;justify-content: space-between;align-items: center;padding-bottom: 0rpx;">
				<view style="font-size: 26rpx;color: #666666;">合计</view>
				<view style="color: #333333;font-size: 38rpx;"><text style="font-size: 24rpx;">￥</text>{{goodPrice || '0'}}
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
						￥<text style="font-size: 48rpx;">{{goodPrice || '0'}}</text>
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
				backText: '继续租赁',
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
				isExceedDays: '', //是否超出租赁天数
				exceedDays: '', //当前日期超出结束日期的天数
				goOnText: '',
				daysBetween: '' //上次归还日期与当前日期间隔多少天
			}
		},
		computed: {
			goodPrice() {
				let price = 0; //商品价格
				let vipPrice = 0;
				let finalPrice = 0; //最终取的价格
				if (this.goodInfo && this.goodInfo.sku && this.goodDays.length > 0) { //没有选中买断--按照天数计算价格
					if (this.goodData.maxIndex != this.goodDays.length + 1) {
						this.goodInfo.sku.map(item => { //判断所选规格--获取对应价格
							if (item.detailJson == this.goodData.detailJson) {
								this.dayPirce = this.isVip == 1 ? item.memberRent : item.dailyRent
								price = this.zqTime * (this.dayPirce * this.goodData.number) //计算租期天数总金额
							}
						})
					}
					console.log(this.dayPirce, "this.dayPirce")
					if (this.discountAmount) {
						price = price - this.discountAmount
					}
				}
				if (Number(price) > 0) {
					finalPrice = price.toFixed(2)
				}
				return finalPrice
			},
			// totalPrice() {
			// 	let totalprice = 0;
			// 	totalprice = Number(this.goodPrice) + Number(this.goodInfo.depositMoney * this.goodData.goodsNum)
			// 	return totalprice
			// }
		},
		onLoad(option) {
			if (option) {
				this.goodData = JSON.parse(decodeURIComponent(option.data));
			}
			if (this.goodData.maxIndex) {
				this.zqDays = this.goodData.maxIndex
			}
			console.log(this.goodData, "this.goodDatagoodData")
			this.getUserInfo()
			this.findGoods()
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
			// 继续租赁超出未归还提示
			this.$Request.get('/app/common/type/528').then(res => {
				if (res.code == 0) {
					this.goOnText = res.data.value
				}
			});
		},
		onShow() {
			uni.$once('getUserAddress', data => {
				this.currentAddress = data
			})
			uni.$once('getDiscountId', data => {
				let datas = {
					couponUserId: data.discountId,
					id: this.goodData.goodsId
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
			close() {
				this.showpay = false
			},
			goPay() {
				// console.log(Object.keys(this.currentAddress).length, "this.currentAddress")''
				if (!this.currentAddress && this.goodData.isExpress != 3) {
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
				if (!this.zqTime) {
					uni.showToast({
						title: '请选择租期',
						icon: 'none'
					})
					return
				}
				// if (!this.goodData.thIndex) {
				// 	uni.showToast({
				// 		title: '请选择收货方式',
				// 		icon: 'none'
				// 	})
				// 	return
				// }
				this.showpay = true
			},
			// 生成订单
			addOrder() {
				let payData = {
					orderId: this.goodData.id,
					classify: this.payWay,
				}
				// 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分
				if (this.payWay == 3) { //微信
					payData.orderType = this.goodData.orderType // 5租赁 6买断
					payData.money = this.goodPrice
					this.wxPay(payData)
				} else if (this.payWay == 1) {
					payData.orderType = this.goodData.orderType // 5租赁 6买断
					payData.money = this.goodPrice
					this.wxPaApp(payData)
				} else if (this.payWay == 2) {
					payData.orderType = this.goodData.orderType // 5租赁 6买断
					payData.money = this.goodPrice
					this.wxPayH5(payData)
				} else if (this.payWay == 4) { //zfb
					payData.orderType = this.goodData.orderType // 5租赁 6买断
					payData.money = this.goodPrice
					this.zhfPayApp(payData)
				} else if (this.payWay == 5) {
					payData.orderType = this.goodData.orderType // 5租赁 6买断
					payData.money = this.goodPrice
					this.zhfPay(payData)
				} else if (this.payWay == 6) {
					this.lqPay(payData)
				}
			},
			afterOrder() {
				let that = this;
				let orderType = 5;
				if (this.zqDays != this.goodDays.length + 1) {
					orderType = 5
				} else {
					orderType = 6
				}
				let data = {
					descrition: this.remakText, //备注
					number: this.goodData.number, //数量
					payWay: this.payWay,
					consignee: this.currentAddress.userName,
					mobile: this.currentAddress.userPhone,
					provinces: this.currentAddress.province + this.currentAddress.city + this.currentAddress.district,
					detail: this.currentAddress.addressDetail,
					addressId: this.currentAddress.addressId,
					postagePrice: this.goodData.postagePrice, //邮费
					goodsId: this.goodData.goodsId,
					detailJson: this.goodData.detailJson, //所选规格
					orderType: this.goodData.orderType, // 5租赁 6买断
					couponUserId: this.discountId,
					dayId: this.goodData.maxIndex, //租赁天数
					deposit: this.goodData.deposit,
					leaseStartDate: this.currentDate, //起租日期
					returnDate: this.futureDate, //归还日期
					dailyRent: this.dayPirce, //日租金
					isExpress: this.goodData.isExpress, //1包邮 2邮寄 3自取
					skuId: this.goodData.skuId,
					couponMoney: this.discountAmount, //优惠券金额
					leaseTerm: this.goodData.leaseTerm,
					id: this.goodData.id,
					status: 7,
					continuingLeaseMoney: this.goodPrice
				}
				uni.showLoading({
					title: '请稍后...'
				})
				this.$Request.postJson('/app/orders/continuingLease', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.hideLoading()
						console.log(res, "resresresresres")
						that.showpay = false
						setTimeout(function() {
							uni.navigateBack()
						}, 50)
					} else {
						uni.hideLoading()
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
						that.afterOrder()
						that.showpay = false
						setTimeout(function() {
							uni.switchTab({
								url: '/pages/oder/index'
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
				uni.showLoading({
					title: '支付中...'
				})
				let that = this;
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
									that.afterOrder()
									that.showpay = false
									setTimeout(function() {
										uni.switchTab({
											url: '/pages/oder/index'
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
						this.afterOrder()
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
					id: this.goodData.goodsId,
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
					id: this.goodData.goodsId,
					page: 1,
					limit: 999
				}
				this.$Request.getT('/app/leaseDays/getLeaseDaysList', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						that.goodDays = res.data.records
						// let zqDays = 0;
						res.data.records.map(item => {
							if (that.zqDays == item.id) {
								// zqDays = item.dayName
								that.zqTime = item.dayName
								this.dayId = item.id
							}
						})
						console.log(that.zqDays, "that.zqDays")
						if (that.zqDays == res.data.records.length + 1) {
							that.zqTime = '买断'
						} else {
							console.log(that.zqTime, "that.zqTime")
							this.getCurrentAndFutureDates(that.zqTime);
						}
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
			getAddlist() {
				this.$Request.getT('/app/address/getAddressInfo', {
					addressId: this.goodData.addressId
				}).then(res => {
					if (res.code == 0) {
						this.currentAddress = res.data
					}
				})
			},
			// 获取租赁日期区间
			getCurrentAndFutureDates(zqTime) {
				const today = new Date();
				// const returnDate = new Date(this.goodData.returnDate);
				const sevenDaysLater = new Date(this.goodData.returnDate);
				if (this.zqTime != '买断') {
					this.currentDate = this.formatDate(today);
					this.calculateDaysAndCheckExceed(this.currentDate, this.goodData.returnDate)
					if (this.daysBetween > 0) {
						//租赁天数 + 剩余天数
						console.log(this.daysBetween, "this.daysBetween")
						let daysNum = Number(zqTime) + this.daysBetween
						sevenDaysLater.setDate(sevenDaysLater.getDate() + Number(zqTime));
						this.futureDate = this.formatDate(sevenDaysLater);
						console.log(daysNum, "daysNumdaysNum")
					} else {
						if (this.exceedDays > 0) { //上次归还日期已超出当前日期
							//租赁天数 - 超出天数
							sevenDaysLater.setDate(sevenDaysLater.getDate() + Number(zqTime - this.exceedDays));
							this.futureDate = this.formatDate(sevenDaysLater);
						} else { //上次归还日期未超出
							//上次归还日期 + 租赁天数
							sevenDaysLater.setDate(sevenDaysLater.getDate() + Number(zqTime));
							this.futureDate = this.formatDate(sevenDaysLater);
						}
					}
				}
				console.log(this.currentDate, "currentDatecurrentDate")
				console.log(this.futureDate, "futureDatefutureDatefutureDate")
			},
			formatDate(date) {
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0');
				const day = String(date.getDate()).padStart(2, '0');
				return `${year}-${month}-${day}`;
			},
			// 判断结束日期是否超过当前日期
			calculateDaysAndCheckExceed(startDateStr, endDateStr) {
				var startDate = new Date(startDateStr);
				var endDate = new Date(endDateStr);
				var currentDate = new Date();
				// 计算两个日期之间的天数间隔
				this.daysBetween = Math.ceil((endDate - startDate) / (1000 * 60 * 60 * 24));
				console.log("两个日期之间相隔 " + this.daysBetween + " 天");
				// 判断当前日期是否超出结束日期
				if (currentDate > endDate) {
					// 如果当前日期超出结束日期，计算超出的时间
					var exceedMilliseconds = currentDate - endDate;
					var exceedHours = Math.floor(exceedMilliseconds / (1000 * 60 * 60));
					this.exceedDays = Math.floor(exceedHours / 24);
					if (this.exceedDays > 0) {
						console.log("当前日期超出结束日期 " + this.exceedDays + " 天");
					} else {
						console.log("当前日期超出结束日期 " + exceedHours + " 小时");
					}
					this.isExceedDays = 0
				} else if (currentDate < endDateStr) {
					console.log("当前日期还未达到开始日期");
					this.isExceedDays = false
				} else {
					console.log("当前日期在指定的日期范围内");
					this.isExceedDays = false
				}
			},
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