<template>
	<view class="order-page">
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<!-- 顶部品牌渐变横幅 -->
		<view class="page-hero">
			<view class="hero-glow hero-glow--a"></view>
			<view class="hero-glow hero-glow--b"></view>
		</view>
		<view class="ambient ambient--a"></view>
		<view class="ambient ambient--b"></view>
		<view class="page-body">
			<view class="orderCont address-card" v-if="goodData.thIndex == 1||goodData.thIndex==2">
				<view class="address-header" @click="navigate('/my/address/index?choose=choose')">
					<view class="address-icon">
						<u-icon name="map-fill" color="#FFFFFF" size="32"></u-icon>
					</view>
					<view class="address-label">
						<text>收货地址</text>
					</view>
					<view class="address-arrow">
						<u-icon name="arrow-right" color="#9AA6A6" size="24"></u-icon>
					</view>
				</view>
				<view class="address-body" @click="navigate('/my/address/index?choose=choose')">
					<view class="address-empty" v-if="Object.keys(currentAddress).length == 0">
						<text class="address-empty-plus">+</text>请选择收货地址
					</view>
					<view v-else>
						<view class="address-user">
							{{currentAddress.userName}}<text>{{currentAddress.userPhone}}</text>
						</view>
						<view class="address-detail">
							{{currentAddress.province}}{{currentAddress.city}}{{currentAddress.district}}{{currentAddress.addressDetail}}
						</view>
					</view>
				</view>
				<view class="address-line">
					<image src="/static/images/line.png"></image>
				</view>
			</view>
			<view class="orderCont shop-address-card" v-if="goodData.thIndex == 3">
				<view class="shop-address-header">
					<view class="shop-address-title">
						<view class="shop-address-icon">
							<u-icon name="map-fill" color="#FFFFFF" size="28"></u-icon>
						</view>
						<text>商家地址</text>
					</view>
					<view class="copy-btn" hover-class="mini-hover" @click="copy">复制</view>
				</view>
				<view class="shop-address-body">
					<view class="shop-address-text">{{shopAddress}}
					</view>
				</view>
			</view>
			<view class="orderCont auth-tip" v-if="userInfo.isAuth == 0" @click="navigate('/my/realName')">
				<view class="auth-icon">
					<image src="/static/zlImage/sh.png" mode=""></image>
				</view>
				<view class="auth-text">
					<view class="auth-tip-title">完善您的身份信息</view>
					<view class="auth-tip-sub">填写资料，加速审核</view>
				</view>
				<view class="auth-arrow">
					<u-icon name="arrow-right" color="#B97900" size="24"></u-icon>
				</view>
			</view>
			<!-- 商品信息 -->
			<view class="orderCont goods-card">
				<view class="card-label">租赁商品</view>
				<view class="goods-row">
					<view class="goods-image">
						<image :src="goodInfo.coverImg" mode="aspectFit">
						</image>
					</view>
					<view class="goods-info">
						<view class="text_hid goods-title">
							{{goodInfo.title || ''}}
						</view>
						<template v-if="goodData.thIndex">
							<view class="goods-sku">
								<text class="sku-label">已选规格</text>
								<text class="sku-value">{{goodData.thIndex == 1 ?'包邮':(goodData.thIndex == 2)?'不包邮':'自取'}} · {{goodData.ggChoose}}</text>
							</view>
						</template>
						<view class="goods-count">数量 ×{{goodData.goodsNum || ''}}</view>
					</view>
				</view>
				<view class="orderDate" v-if="zqTime != '买断'">
					<view class="date-box">
						<view class="date-label">
							<u-icon name="calendar" color="#0A9A9A" size="24"></u-icon>
							<text>预计起租日</text>
						</view>
						<view class="date-value">{{currentDate || ''}}</view>
					</view>
					<view class="date-arrow">
						<image src="../../static/zlImage/jt.png" mode=""></image>
						<text class="date-days">{{zqTime}}天</text>
					</view>
					<view class="date-box">
						<view class="date-label">
							<u-icon name="calendar-fill" color="#0A9A9A" size="24"></u-icon>
							<text>预计归还日</text>
						</view>
						<view class="date-value">{{futureDate || ''}}</view>
					</view>
				</view>
			</view>
			<!-- 费用明细 -->
			<view class="orderCont fee-card">
				<view class="section-title">费用明细</view>
				<view class="fee-item">
					<view class="fee-label">
						<view class="fee-label-icon fee-label-icon--coupon">
							<u-icon name="coupon" color="#FFFFFF" size="24"></u-icon>
						</view>
						优惠券
					</view>
					<view class="fee-value"
						@click="navigate(`/my/ticket/myList`,{price:totalPrice,couponId:discountId})">
						<text :class="ticketList&&ticketList.length>0&&!discountAmount?'coupon-link':(discountAmount > 0 ?'coupon-active':'')">{{ticketList&&ticketList.length>0&&!discountAmount?'立即使用':(discountAmount > 0 ?'-'+discountAmount :'暂无优惠券')}}</text>
						<image class="fee-arrow" src="../../static/images/my/right.png" mode=""
							v-if="ticketList&&ticketList.length>0">
						</image>
					</view>
				</view>
				<view class="fee-item" v-if="zqTime != '买断'">
					<view class="fee-label">日租金</view>
					<view class="fee-price"><text>￥</text>{{dayPirce || '0'}}
					</view>
				</view>
				<view class="fee-item">
					<view class="fee-label">租期</view>
					<view class="fee-value">{{zqTime || '0'}}<text v-if="zqTime != '买断'">天</text>
					</view>
				</view>
				<view class="fee-item">
					<view class="fee-label">提货方式</view>
					<view class="fee-value" v-if="goodData.thIndex == 1">包邮</view>
					<view class="fee-price" v-if="goodData.thIndex == 2">
						<text>￥</text>{{goodInfo.postagePrice}}
					</view>
					<view class="fee-value" v-if="goodData.thIndex == 3">自提</view>
				</view>
				<view class="fee-item" v-if="goodInfo.isExpress == 2&&goodInfo.postagePrice">
					<view class="fee-label">运费</view>
					<view class="fee-price"><text>￥</text>{{goodInfo.postagePrice}}</view>
				</view>
				<view class="fee-item">
					<view class="fee-label">押金</view>
					<view class="fee-price"><text>￥</text>{{Number(goodInfo.depositMoney?goodInfo.depositMoney:0) * Number(goodData.goodsNum?goodData.goodsNum:0) || '0'}}
					</view>
				</view>
				<view class="fee-item">
					<view class="fee-label">总租金</view>
					<view class="fee-price"><text>￥</text>{{goodPrice|| '0'}}
					</view>
				</view>
				<view class="fee-divider"></view>
				<view class="fee-item total-row">
					<view class="fee-label fee-label--total">合计</view>
					<view class="fee-total"><text>￥</text>{{totalPrice?totalPrice:'0'}}
					</view>
				</view>
			</view>
			<!-- 备注 -->
			<view class="orderCont remark-card">
				<view class="section-title">备注</view>
				<u-input v-model="remakText" type="textarea" :border="border" :height="height" :auto-height="autoHeight"
					placeholder="建议留言前先与商家沟通确认" style="padding-left: 30rpx;" />
			</view>
			<view class="bottom-space"></view>
		</view>
		<!-- 底部按钮 -->
		<view class="footerBtn">
			<view class="footer">
				<image v-if="showAgree" @tap="isShowAgree"
					src="https://api.shengqianxiong.com.cn/img/20201112/669aa8946cfb4ebdb459d57193c0ebca.png"></image>
				<image v-else @tap="isShowAgree"
					src="https://api.shengqianxiong.com.cn/img/20201112/1e9102fc891f4d86a13c7b2ba6921cba.png"></image>
				<text class="agreement-text">我已阅读并同意</text>
				<!-- 协议地址 -->
				<navigator url="/my/setting/xieyi?type=520" open-type="navigate" class="agreement-link">《租赁及服务相关协议》
				</navigator>
				<text>与</text>
				<navigator url="/my/setting/xieyi?type=516" open-type="navigate" class="agreement-link">《用户协议》
				</navigator>
			</view>
			<view class="pay-bar">
				<view class="pay-total">
					<text>合计</text>
					<view class="pay-price">
						￥<text>{{totalPrice || '0'}}</text>
					</view>
				</view>
				<view class="btn_zs" hover-class="cta-hover" @click="goPay">去支付</view>
			</view>
		</view>
		<!-- 支付弹框 -->
		<u-popup v-model="showpay" mode="bottom" border-radius="24" :closeable="true" @close="close">
			<view class="pay-popup-title">选择支付方式</view>
			<view class="pay-popup-body">
				<view class="pay-item" :class="{'pay-item--active': payWay === item.id}"
					v-for="(item,index) in payList" :key='index'>
					<view class="pay-item-left">
						<image :src="item.image"></image>
						<view class="pay-item-name">{{item.name}}
						</view>
					</view>
					<view class="pay-item-right">
						<radio-group name="payWay" @tap='selectWay(item.id)'>
							<label class="tui-radio">
								<radio color="#15CAD2" :checked="payWay === item.id ? true : false" />
							</label>
						</radio-group>
					</view>
				</view>
				<view class="pay-item balance-row" :class="{'pay-item--active': payWay === 6}" v-if="userInfo.balance>0">
					<view class="pay-item-left">
						<image src="/static/images/lingqian.png">
						</image>
						<view class="pay-item-name">
							余额
						</view>
					</view>
					<view class="pay-item-right">
						<radio-group name="openWay" @tap='selectWay(6)'>
							<label class="tui-radio">
								<radio color="#15CAD2" :checked="payWay === 6 ? true : false" />
							</label>
						</radio-group>
					</view>
				</view>
			</view>
			<view class="pay-confirm" hover-class="cta-hover" @tap="addOrder()">确认支付</view>
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
				backText: '确认订单',
				backTextStyle: {
					color: '#333333',
					// fontweighth:'bold',
				},
				remakText: '', //备注
				type: 'textarea',
				border: false,
				height: 50,
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
				shopAddress: ''
			}
		},
		computed: {
			goodPrice() {
				let price = 0; //商品价格
				let vipPrice = 0;
				let finalPrice = 0; //最终取两位小数价格
				if (this.goodInfo && this.goodInfo.sku && this.goodDays.length > 0) { //没有选中买断--按照天数计算价格
					if (this.goodData.zqIndex != this.goodDays.length + 1) {
						this.goodInfo.sku.map(item => { //判断所选规格--获取对应价格
							if (item.detailJson == this.goodData.ggChoose) {
								this.dayPirce = this.isVip == 1 ? item.memberRent : item.dailyRent
								price = this.zqTime * (this.dayPirce * this.goodData.goodsNum) //计算租期天数总金额
								return
							}
						})
					} else if (this.goodInfo.supportBuyout == 1 && this.goodData.zqIndex == this.goodDays.length +
						1) { //选中买断
						this.goodInfo.sku.map(item => {
							if (item.detailJson == this.goodData.ggChoose) {
								price = (this.isVip == 1 ? item.memberRent : item.skuOriginalPrice) * this.goodData
									.goodsNum
							}
						})
					}
					if (this.discountAmount) {
						price = price - this.discountAmount
					}
				}
				if (Number(price) > 0) {
					finalPrice = price.toFixed(2)
				}
				return finalPrice
			},
			totalPrice() {
				let totalprice = 0;
				let finalPrice = 0; //最终取两位小数价格
				if (this.goodInfo && this.goodInfo.isExpress == 2) { //判断是否包邮--不包邮加邮费
					totalprice = Number(this.goodPrice) + Number(this.goodInfo.depositMoney * this.goodData.goodsNum) +
						this
						.goodInfo.postagePrice
				} else {
					totalprice = Number(this.goodPrice) + Number(this.goodInfo.depositMoney * this.goodData.goodsNum)
				}
				if (Number(totalprice) > 0) {
					finalPrice = totalprice.toFixed(2)
				}
				console.log(totalprice, 'totalprice')
				return finalPrice
			}
		},
		onLoad(option) {
			this.shopAddress = uni.getStorageSync('shopAddressZt')
			if (option) {
				this.goodData = JSON.parse(decodeURIComponent(option.data));
				console.log(this.goodData)
			}
			// if (this.goodData.zqIndex) {
			// 	this.zqDays = this.goodData.zqIndex
			// }
			// console.log(this.zqDays, "zqDayszqDays")
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
			this.getUserInfo()
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
			})
		},
		methods: {
			getDiscount(discountId) {

			},
			copy() {
				let that = this;
				uni.setClipboardData({
					data: that.shopAddress,
					success: r => {
						this.$queue.showToast('复制成功');
					}
				});
			},
			close() {
				this.showpay = false
			},
			goPay() {
				let isExpress = this.goodInfo.isExpress.split(",");
				console.log(isExpress, 'isExpress')
				this.getUserInfo()
				if (this.goodData.thIndex != 3 && Object.keys(this.currentAddress).length == 0) {
					uni.showToast({
						title: '请添加收货地址',
						icon: 'none'
					})
					return
				}
				// isExpress.map(item => {
				// 	if (item != 3 && Object.keys(this.currentAddress).length == 0) {
				// 		uni.showToast({
				// 			title: '请添加收货地址',
				// 			icon: 'none'
				// 		})
				// 		return
				// 	}
				// })
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
				if (!this.goodData.thIndex) {
					uni.showToast({
						title: '请选择收货方式',
						icon: 'none'
					})
					return
				}
				if (this.userInfo.isAuth == 0) {
					uni.showToast({
						title: '您还未实名认证，请点击上方完善您的身份信息再提交订单',
						icon: 'none',
						duration: 3000
					})
					return
				} else if (this.userInfo.isAuth == 1) {
					if (this.userInfo.status1 == 0) {
						uni.showToast({
							title: '实名认证审核中,通过后再来提交订单',
							icon: 'none',
							duration: 3000
						})
					} else if (this.userInfo.status1 == 1) {
						this.showpay = true
					}
				} else if (this.userInfo.isAuth == 2) {
					// status1  0--待审核  1--已通过  2--未通过
					if (this.userInfo.status1 == 2) {
						uni.showToast({
							title: '您的实名认证被拒绝,请重新提交',
							icon: 'none'
						})
					}
				}
			},
			// 生成订单
			addOrder() {
				let orderType = 5;
				if (this.zqDays != this.goodDays.length + 1) {
					orderType = 5
				} else {
					orderType = 6
				}
				let data = {
					descrition: this.remakText, //备注
					number: this.goodData.goodsNum, //数量
					payWay: this.payWay,
					consignee: this.currentAddress.userName,
					mobile: this.currentAddress.userPhone,
					provinces: this.currentAddress.province + this.currentAddress.city + this.currentAddress.district,
					detail: this.currentAddress.addressDetail,
					addressId: this.currentAddress.addressId,
					postagePrice: this.goodInfo.postagePrice, //邮费
					goodsId: this.goodInfo.id,
					detailJson: this.goodData.ggChoose, //所选规格
					orderType: orderType, // 5租赁 6买断
					couponUserId: this.discountId,
					dayId: this.dayId, //租赁天数
					deposit: this.goodInfo.depositMoney,
					leaseStartDate: this.currentDate, //起租日期
					returnDate: this.futureDate, //归还日期
					dailyRent: this.dayPirce, //日租金
					isExpress: this.goodData.thIndex, //1包邮 2邮寄 3自取
					skuId: this.goodData.skuId,
					couponMoney: this.discountAmount, //优惠券金额
					leaseTerm: this.goodData.leaseTerm,
					img: this.goodInfo.coverImg,
					buyoutPrice: this.isVip == 1 ? this.goodInfo.memberPrice : this.goodInfo.price
				}
				uni.showLoading({
					title: '请稍后...'
				})
				this.$Request.postJson('/app/orders/save', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						let payData = {
							orderId: res.data,
							classify: this.payWay,
						}
						// 1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱 7积分
						if (this.payWay == 3) { //微信
							payData.orderType = orderType // 5租赁 6买断
							payData.money = this.totalPrice
							this.wxPay(payData)
						} else if (this.payWay == 1) {
							payData.orderType = orderType // 5租赁 6买断
							payData.money = this.totalPrice
							this.wxPaApp(payData)
						} else if (this.payWay == 2) {
							payData.orderType = orderType // 5租赁 6买断
							payData.money = this.totalPrice
							this.wxPayH5(payData)
						} else if (this.payWay == 4) { //zfb
							payData.orderType = orderType // 5租赁 6买断
							payData.money = this.totalPrice
							this.zhfPayApp(payData)
						} else if (this.payWay == 5) {
							payData.orderType = orderType // 5租赁 6买断
							payData.money = this.totalPrice
							this.zhfPay(payData)
						} else if (this.payWay == 6) {
							this.lqPay(payData)
						}
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
						console.log(res, "resresres微信app支付成功")
						that.showpay = false
						uni.showToast({
							title: '支付成功',
							icon: 'none'
						});
						setTimeout(() => {
							uni.switchTab({
								url: '/pages/order/index'
							})
						}, 50)
					},
					fail: function(err) {
						console.log(err, "errerrerr微信app支付失败")
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
							// 发起支付
							uni.requestPayment({
								appId: paymentParams.appid,
								timeStamp: paymentParams.timestamp.toString(),
								nonceStr: paymentParams.noncestr,
								package: paymentParams.package,
								signType: paymentParams.signType,
								paySign: paymentParams.sign,
								success: function(res) {
									that.showpay = false
									uni.showToast({
										title: '支付成功',
										icon: 'none'
									});
									setTimeout(() => {
										uni.switchTab({
											url: '/pages/order/index'
										})
									}, 50)
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
						}, 50)
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
						let zqDays = this.goodData.zqIndex
						if (that.zqDays == res.data.records.length + 1) {
							that.zqTime = '买断'
						} else {
							that.zqTime = that.goodDays[zqDays].dayName
							this.dayId = that.goodDays[zqDays].id
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
			getAddlist(page) {
				this.$Request.getT('/app/address/getAddressList', {
					page: 1,
					limit: 10
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
		background: linear-gradient(180deg, #D8F0EF 0%, #E4F4F2 28%, #EDF7F4 55%, #F3F9F7 100%);
	}

	.order-page {
		padding-bottom: 260rpx;
		position: relative;
		min-height: 100vh;
	}

	// 顶部品牌渐变横幅
	.page-hero {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 430rpx;
		background: linear-gradient(180deg, #16CAD1 0%, #10B0B3 38%, #46C7C4 66%, #9FE2DE 85%, #D8F0EF 100%);
		overflow: hidden;
	}

	.hero-glow {
		position: absolute;
		border-radius: 50%;
	}

	.hero-glow--a {
		width: 360rpx;
		height: 360rpx;
		right: -120rpx;
		top: -160rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0) 68%);
	}

	.hero-glow--b {
		width: 260rpx;
		height: 260rpx;
		left: -110rpx;
		bottom: -120rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.18) 0%, rgba(255, 255, 255, 0) 70%);
	}

	// 环境光晕（柔化页面中下段）
	.ambient {
		position: absolute;
		border-radius: 50%;
		z-index: 0;
	}

	.ambient--a {
		width: 420rpx;
		height: 420rpx;
		right: -160rpx;
		top: 900rpx;
		background: radial-gradient(circle, rgba(23, 205, 212, 0.1) 0%, rgba(23, 205, 212, 0) 70%);
	}

	.ambient--b {
		width: 460rpx;
		height: 460rpx;
		left: -180rpx;
		top: 1900rpx;
		background: radial-gradient(circle, rgba(14, 168, 168, 0.08) 0%, rgba(14, 168, 168, 0) 70%);
	}

	.page-body {
		position: relative;
		z-index: 1;
	}

	// 通用卡片
	.orderCont {
		border-radius: 32rpx;
		width: calc(100% - 60rpx);
		margin: auto;
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		overflow: hidden;
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		box-shadow: 0 16rpx 44rpx rgba(11, 148, 155, 0.08);
		margin-top: 28rpx;
		padding: 34rpx;
	}

	.text_hid {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
	}

	// 地址卡片
	.address-card {
		padding: 34rpx 34rpx 0;
		margin-top: 24rpx;
	}

	.address-header {
		display: flex;
		align-items: center;
	}

	.address-icon {
		width: 64rpx;
		height: 64rpx;
		border-radius: 50%;
		background: linear-gradient(145deg, #19CFD6 0%, #0CA5A9 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.35);
		flex-shrink: 0;
		margin-right: 20rpx;
	}

	.address-label {
		flex: 1;
		font-size: 30rpx;
		font-weight: 800;
		color: #1A2323;
	}

	.address-arrow {
		width: 44rpx;
		height: 44rpx;
		border-radius: 50%;
		background: #E9F2EF;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.address-body {
		margin-top: 26rpx;
	}

	.address-empty {
		font-weight: 600;
		font-size: 28rpx;
		color: #0A9A9A;
		padding: 36rpx 0;
		display: flex;
		align-items: center;
		justify-content: center;
		border: 2rpx dashed rgba(21, 202, 210, 0.4);
		border-radius: 20rpx;
		background: rgba(21, 202, 210, 0.04);
	}

	.address-empty-plus {
		font-size: 34rpx;
		font-weight: 700;
		margin-right: 10rpx;
	}

	.address-user {
		color: #1A2323;
		font-size: 34rpx;
		font-weight: 800;

		text {
			font-size: 26rpx;
			color: #7A8A8A;
			font-weight: 500;
			margin-left: 22rpx;
		}
	}

	.address-detail {
		color: #4A5858;
		font-size: 26rpx;
		margin-top: 16rpx;
		line-height: 1.6;
	}

	.address-line {
		position: relative;
		width: calc(100% + 68rpx);
		height: 8rpx;
		margin: 30rpx -34rpx 0;

		image {
			width: 100%;
			height: 8rpx;
		}
	}

	// 商家地址（深色渐变卡）
	.shop-address-card {
		padding: 34rpx;
		background: linear-gradient(140deg, #0FB9BF 0%, #0B949B 100%);
		box-shadow: 0 16rpx 40rpx rgba(11, 148, 155, 0.3);
	}

	.shop-address-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 22rpx;
	}

	.shop-address-title {
		font-weight: 800;
		font-size: 30rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
	}

	.shop-address-icon {
		width: 52rpx;
		height: 52rpx;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.2);
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 16rpx;
	}

	.copy-btn {
		background: rgba(255, 255, 255, 0.22);
		border: 1rpx solid rgba(255, 255, 255, 0.45);
		color: #FFFFFF;
		font-size: 22rpx;
		padding: 10rpx 28rpx;
		text-align: center;
		border-radius: 999rpx;
		font-weight: 600;
	}

	.mini-hover {
		opacity: 0.8;
	}

	.shop-address-body {
		background: rgba(255, 255, 255, 0.14);
		border-radius: 20rpx;
		padding: 24rpx 26rpx;
	}

	.shop-address-text {
		color: #FFFFFF;
		font-size: 28rpx;
		line-height: 1.6;
		font-weight: 500;
	}

	// 实名认证提示
	.auth-tip {
		padding: 28rpx 34rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		background: linear-gradient(120deg, #FFF6E9 0%, #FFEDD6 100%);
		border: 1rpx solid rgba(240, 115, 0, 0.14);
	}

	.auth-icon {
		width: 72rpx;
		height: 72rpx;
		border-radius: 22rpx;
		background: linear-gradient(145deg, #FFB74D 0%, #F07300 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 22rpx;
		box-shadow: 0 8rpx 20rpx rgba(240, 115, 0, 0.28);
		flex-shrink: 0;

		image {
			width: 40rpx;
			height: 40rpx;
		}
	}

	.auth-text {
		flex: 1;
		min-width: 0;
	}

	.auth-tip-title {
		font-size: 30rpx;
		font-weight: 800;
		color: #8A4B00;
	}

	.auth-tip-sub {
		font-size: 24rpx;
		color: #B97900;
		margin-top: 8rpx;
	}

	.auth-arrow {
		width: 44rpx;
		height: 44rpx;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.7);
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	// 商品信息
	.goods-card {
		padding: 34rpx;
	}

	.card-label {
		font-size: 30rpx;
		font-weight: 800;
		color: #1A2323;
		margin-bottom: 28rpx;
		position: relative;
		padding-left: 24rpx;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 10rpx;
			height: 30rpx;
			border-radius: 6rpx;
			background: linear-gradient(180deg, #15CAD2 0%, #0EA8A8 100%);
			box-shadow: 0 4rpx 10rpx rgba(21, 202, 210, 0.4);
		}
	}

	.goods-row {
		display: flex;
		align-items: flex-start;
	}

	.goods-image {
		width: 180rpx;
		height: 180rpx;
		border-radius: 24rpx;
		overflow: hidden;
		background: #EDF5F2;
		flex-shrink: 0;
		margin-right: 26rpx;
		box-shadow: 0 10rpx 26rpx rgba(16, 60, 62, 0.1);

		image {
			width: 180rpx;
			height: 180rpx;
		}
	}

	.goods-info {
		flex: 1;
		min-width: 0;
	}

	.goods-title {
		width: 100%;
		font-size: 30rpx;
		font-weight: 700;
		color: #1A2323;
		line-height: 1.4;
		white-space: normal;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
	}

	.goods-sku {
		margin-top: 16rpx;
		background: linear-gradient(120deg, #F0FAFA 0%, #E9F6F4 100%);
		border: 1rpx solid rgba(21, 202, 210, 0.12);
		border-radius: 14rpx;
		padding: 12rpx 18rpx;
		font-size: 22rpx;
		line-height: 1.5;
		display: flex;

		.sku-label {
			color: #0A9A9A;
			font-weight: 700;
			flex-shrink: 0;
			margin-right: 12rpx;
		}

		.sku-value {
			color: #4A5858;
			font-weight: 500;
		}
	}

	.goods-count {
		color: #9AA6A6;
		font-size: 24rpx;
		padding: 14rpx 0 0;
		font-weight: 500;
	}

	// 租期日期带
	.orderDate {
		background: linear-gradient(135deg, #E6F8F8 0%, #D9F1EF 100%);
		border: 1rpx solid rgba(21, 202, 210, 0.14);
		display: flex;
		border-radius: 24rpx;
		width: calc(100% - 32rpx);
		margin: auto;
		padding: 24rpx 16rpx;
		justify-content: space-between;
		align-items: center;
		margin-top: 30rpx;
	}

	.date-box {
		flex: 1;
		background: #FFFFFF;
		border-radius: 18rpx;
		padding: 22rpx 10rpx;
		text-align: center;
		box-shadow: 0 8rpx 20rpx rgba(11, 148, 155, 0.1);
	}

	.date-label {
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 22rpx;
		color: #7A8A8A;
		padding-bottom: 10rpx;
		font-weight: 500;

		text {
			margin-left: 6rpx;
		}
	}

	.date-value {
		font-size: 28rpx;
		color: #1A2323;
		font-weight: 800;
	}

	.date-arrow {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		margin: 0 18rpx;
		flex-shrink: 0;

		image {
			width: 50rpx;
			height: 29rpx;
			margin-bottom: 10rpx;
		}
	}

	.date-days {
		font-size: 20rpx;
		color: #FFFFFF;
		font-weight: 700;
		background: linear-gradient(135deg, #15CAD2 0%, #0CA5A9 100%);
		border-radius: 999rpx;
		padding: 6rpx 18rpx;
		box-shadow: 0 6rpx 16rpx rgba(21, 202, 210, 0.35);
	}

	// 费用明细
	.fee-card {
		padding: 34rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 800;
		color: #1A2323;
		position: relative;
		padding-left: 24rpx;
		margin-bottom: 32rpx;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 10rpx;
			height: 30rpx;
			border-radius: 6rpx;
			background: linear-gradient(180deg, #15CAD2 0%, #0EA8A8 100%);
			box-shadow: 0 4rpx 10rpx rgba(21, 202, 210, 0.4);
		}
	}

	.fee-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-bottom: 30rpx;

		// &:last-child {
		// 	padding-bottom: 0;
		// }
	}

	.fee-label {
		font-size: 26rpx;
		color: #4A5858;
		display: flex;
		align-items: center;
		font-weight: 500;
	}

	.fee-label--total {
		font-size: 28rpx;
		font-weight: 800;
		color: #1A2323;
	}

	.fee-label-icon {
		width: 40rpx;
		height: 40rpx;
		border-radius: 12rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 12rpx;
		flex-shrink: 0;
	}

	.fee-label-icon--coupon {
		background: linear-gradient(145deg, #FF8A5C 0%, #F1302E 100%);
		box-shadow: 0 6rpx 14rpx rgba(241, 48, 46, 0.28);
	}

	.fee-value {
		color: #4A5858;
		font-size: 28rpx;
		display: flex;
		align-items: center;
	}

	.coupon-link {
		color: #FFFFFF;
		font-weight: 600;
		font-size: 22rpx;
		background: linear-gradient(135deg, #FF8A5C 0%, #F1302E 100%);
		border-radius: 999rpx;
		padding: 8rpx 22rpx;
		box-shadow: 0 6rpx 16rpx rgba(241, 48, 46, 0.25);
	}

	.coupon-active {
		color: #F1302E;
		font-weight: 700;
	}

	.fee-price {
		color: #2E3A3A;
		font-size: 30rpx;
		font-weight: 600;

		text {
			font-size: 22rpx;
			margin-right: 2rpx;
		}
	}

	.fee-total {
		color: #F1302E;
		font-size: 46rpx;
		font-weight: 800;

		text {
			font-size: 26rpx;
			margin-right: 4rpx;
		}
	}

	.fee-divider {
		border-top: 2rpx dashed #E3EBEB;
		margin: 26rpx 0;
	}

	.total-row {
		padding-bottom: 0;
		background: linear-gradient(120deg, #FFF3F2 0%, #FFE9E8 100%);
		border-radius: 18rpx;
		padding: 24rpx 26rpx;
		margin-top: 4rpx;
	}

	.fee-arrow {
		width: 12rpx;
		height: 22rpx;
		margin-left: 12rpx;
		opacity: 0.5;
	}

	// 备注
	.remark-card {
		padding: 34rpx;
	}

	::v-deep .u-input {
		background: #EDF5F3;
		border-radius: 20rpx;
		padding: 24rpx;
		font-size: 26rpx;
	}

	.bottom-space {
		height: 40rpx;
	}

	// 底部
	.footerBtn {
		width: calc(100% - 60rpx);
		left: 30rpx;
		background-color: #fff;
		position: fixed;
		bottom: 24rpx;
		z-index: 66;
		padding: 24rpx 30rpx 28rpx;
		border-radius: 36rpx;
		box-shadow: 0 16rpx 44rpx rgba(16, 60, 62, 0.16);
	}

	.footer {
		font-size: 22rpx;
		color: #7A8A8A;
		display: flex;
		align-items: center;
		flex-wrap: wrap;

		image {
			width: 32rpx;
			height: 32rpx;
			flex-shrink: 0;
		}
	}

	.agreement-text {
		margin-left: 10rpx;
	}

	.agreement-link {
		color: #0A9A9A;
		font-weight: 600;
	}

	.pay-bar {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 22rpx;
		padding-top: 24rpx;
		border-top: 1rpx solid #F2F5F5;
	}

	.pay-total {
		display: flex;
		align-items: baseline;

		text {
			color: #7A8A8A;
			font-size: 26rpx;
			font-weight: 500;
		}
	}

	.pay-price {
		font-weight: 800;
		color: #F1302E;
		font-size: 28rpx;
		margin-left: 12rpx;

		text {
			font-size: 52rpx;
			color: #F1302E;
			font-weight: 800;
			line-height: 1;
		}
	}

	.btn_zs {
		border: none;
		padding: 26rpx 80rpx;
		font-size: 30rpx;
		color: #ffffff;
		background: linear-gradient(135deg, #15CAD2 0%, #0CA5A9 100%);
		border-radius: 999rpx;
		font-weight: 700;
		box-shadow: 0 12rpx 28rpx rgba(21, 202, 210, 0.4);
		letter-spacing: 4rpx;
	}

	.cta-hover {
		opacity: 0.88;
		transform: scale(0.97);
	}

	// 支付弹框
	.pay-popup-title {
		width: 100%;
		text-align: center;
		font-size: 34rpx;
		font-weight: 800;
		padding-top: 44rpx;
		margin-bottom: 36rpx;
		color: #1A2323;
		position: relative;

		&::after {
			content: '';
			position: absolute;
			left: 50%;
			bottom: -14rpx;
			transform: translateX(-50%);
			width: 72rpx;
			height: 6rpx;
			border-radius: 6rpx;
			background: linear-gradient(90deg, #15CAD2, #0EA8A8);
		}
	}

	.pay-popup-body {
		padding: 10rpx 30rpx 0;
	}

	.pay-item {
		display: flex;
		height: 112rpx;
		align-items: center;
		padding: 0 26rpx;
		background: #F0F7F5;
		border: 2rpx solid transparent;
		border-radius: 22rpx;
		margin-bottom: 18rpx;
		transition: all 0.2s;
	}

	.pay-item--active {
		background: #E8F6F3;
		border: 2rpx solid rgba(21, 202, 210, 0.55);
		box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.14);
	}

	.pay-item-left {
		display: flex;
		flex: 1;
		align-items: center;

		image {
			width: 56rpx;
			height: 56rpx;
			border-radius: 50%;
		}
	}

	.pay-item-name {
		font-size: 30rpx;
		margin-left: 20rpx;
		color: #1A2323;
		font-weight: 600;
	}

	.pay-item-right {
		display: flex;
		justify-content: flex-end;
		flex-shrink: 0;
	}

	.balance-row {
		margin-top: 0;
	}

	.pay-confirm {
		width: calc(100% - 60rpx);
		height: 92rpx;
		background: linear-gradient(135deg, #15CAD2 0%, #0CA5A9 100%);
		text-align: center;
		line-height: 92rpx;
		border-radius: 999rpx;
		margin: 30rpx auto 40rpx;
		color: #ffffff;
		font-size: 30rpx;
		font-weight: 700;
		box-shadow: 0 12rpx 28rpx rgba(21, 202, 210, 0.35);
		letter-spacing: 4rpx;
	}

	::v-deep .u-radio__icon-wrap {
		border-color: #15CAD2 !important;
	}
</style>
