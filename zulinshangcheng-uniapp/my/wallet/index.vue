<template>
	<view class="wallet-page">
		<!-- 余额支付 -->
		<!-- <view v-if="title_color==1" class="padding">
			<view class=" radius bg">
				<view class=" u-flex u-p-l-30 u-p-t-30 u-p-b-30">
					<view class="u-m-r-20">
						<u-avatar :src="avatar" size="80"></u-avatar>
					</view>
					<view class="u-flex-1 ">
						<view class="u-font-16 text-white text-bold">{{money}}元</view>
						<view class="u-font-12 u-m-t-10" style="color: #999999;">用于平台支付</view>
					</view>
				</view>
			</view>
			<view class="margin-top radius bg flex justify-between flex-wrap padding-lr padding-bottom">
				<view v-for="(item,index) in wallet" :key='index' class="flex justify-between padding-sm radius margin-top"
					style="color: #1E1F31;background-color: #F7F7F7; width: 46%;" @click="active(item)"
					:class="{active:item.isSelect}">
					<view>
						¥{{item.price}}
					</view>
				</view>
			</view>

			<view class="bg margin-top padding-lr radius">
				<view>
					<view style="display: flex;height: 100upx;align-items: center;padding: 20upx 0;"
						v-for="(item,index) in openLists" :key='index'>
						<image :src="item.image" style="width: 55upx;height: 55upx;border-radius: 50upx;"></image>
						<view style="font-size: 30upx;margin-left: 20upx;width: 70%;">{{item.text}}
						</view>
						<radio-group name="openWay" style="margin-left: 20upx;" @tap='selectWay(item)'>
							<label class="tui-radio">
								<radio color="#1789FD" :checked="openWay === item.id ? true : false" />
							</label>
						</radio-group>
					</view>
				</view>
			</view>
			<view class="btn" @click="pay">确认支付</view>
		</view> -->
		<view class="wallet-shell">
			<view class="wallet-hero">
				<view>
					<view class="wallet-eyebrow">钱包账户</view>
					<view class="wallet-balance-label">可用余额</view>
					<view class="wallet-balance">{{money}}<text>元</text></view>
					<view class="wallet-hint">提现申请会根据当前账户信息进行审核与处理</view>
				</view>
				<view class="wallet-badge">
					<u-icon name="coupon" size="40" color="#FFFFFF"></u-icon>
				</view>
			</view>

			<view class="wallet-card wallet-form-card">
				<view class="section-title">提现金额</view>
				<view class="amount-input-wrap">
					<text class="amount-prefix">¥</text>
					<input class="amount-input" type="text" placeholder="请输入提现金额" v-model="moneyNum">
				</view>
				<view class="wallet-desc">{{placeholder}}</view>
			</view>

			<view class="wallet-card method-card">
				<view class="section-title">提现方式</view>
				<view class="method-list">
					<view class="method-item" :class="{'method-item--active': opentype === item.id}" v-for="(item,index) in openLists"
						:key='index' @click="selectWay1(item)">
						<view class="method-main">
							<image :src="item.image" class="method-icon"></image>
							<view class="method-name">{{item.text}}</view>
						</view>
						<view class="method-radio">
							<view class="method-radio-dot" v-if="opentype === item.id"></view>
						</view>
					</view>
				</view>
			</view>

			<view class="wallet-submit" @click="cashMoney">立即提现</view>

			<view class="wallet-card quick-card">
				<view class="section-title">钱包服务</view>
				<view class="quick-grid">
					<view class="quick-item" @click="goNav('/my/wallet/zhifubao')">
						<view class="quick-icon quick-icon--wallet"><u-icon name="rmb-circle" size="38" color="#D98934"></u-icon></view>
						<text>提现账号</text>
					</view>
					<view class="quick-item" @click="isShow">
						<view class="quick-icon quick-icon--code"><u-icon name="grid" size="36" color="#3A9B63"></u-icon></view>
						<text>微信收款码</text>
					</view>
					<view class="quick-item" @click="goNav('/my/wallet/cashList')">
						<view class="quick-icon quick-icon--record"><u-icon name="clock" size="36" color="#4B7FBE"></u-icon></view>
						<text>提现记录</text>
					</view>
					<view class="quick-item" @click="goNav('/my/wallet/qblist')">
						<view class="quick-icon quick-icon--detail"><u-icon name="file-text" size="36" color="#0EAFB6"></u-icon></view>
						<text>钱包明细</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 微信收款码弹框 -->
		<u-popup v-model="show" mode="center">
			<view class="wallet-popup">
				<view class="popup-head">
					<view></view>
					<view class="popup-title">添加微信收款码</view>
					<view class="popup-close" @click="show=false">×</view>
				</view>
				<!-- <view class="text-center padding-top-sm padding-bottom-lg" style="color: #999999;">请提交微信号和微信二维码
				</view> -->
				<view class="popup-body">
					<view class="qr-upload" @click.stop="weixin">
						<!-- <image src="../../static/images/my/erweima.png" v-if="!wximg"></image> -->
						<image :src="wxImg?wxImg:erweima" mode="widthFix"></image>
					</view>
					<view class="popup-tip">上传清晰的微信收款码，方便提现审核使用</view>
				</view>
				<!-- <view class="text-center margin-top-sm " @click="submit"
					style="border-radius: 10rpx;background-color: #7E59FF;color: #fff;height: 80rpx;line-height: 80rpx;">保存</view> -->
			</view>
		</u-popup>
	</view>
</template>

<script>
	import configdata from '@/common/config.js';
	export default {
		data() {
			return {
				show: false,
				title_color: 1,
				money: 0,
				avatar: '',
				Profit: 0,
				erweima: 'https://xianyu.xianmaxiong.com/file/uploadPath/2024/11/14/f537b096d4357ccb68437ee8b8b56cae.png',
				openLists: [{
					image: '/static/zlImage/image/zhifubao.png',
					text: '支付宝',
					id: 1
				}, {
					image: '/static/zlImage/image/icon_weixin.png',
					text: '微信',
					id: 2
				}],
				checkZFB: '否',
				openWay: 1,
				wallet: [],
				moneyNum: null,
				thisSelect: {},
				charge: 0, //提现手续费
				maxMoney: 0, //最高提现额度
				minMoney: 0, //最低提现额度
				ratio: '', //金元比例
				placeholder: '',
				sp: 0,
				opentype: 1,
				wxImg: '',
				WxTXFangShi: 1
			}
		},
		onLoad() {
			this.avatar = uni.getStorageSync('avatar')
			this.getCharge()
			this.getMinMoney()
			this.getMaxMoney()

			// #ifdef APP-PLUS
			this.openLists = [{
				image: '/static/zlImage/image/zhifubao.png',
				text: '支付宝',
				id: 1
			}, {
				image: '/static/zlImage/image/icon_weixin.png',
				text: '微信',
				id: 2
			}];
			this.openWay = 1;
			// #endif

			// #ifdef MP-WEIXIN
			this.openLists = [{
				image: '/static/zlImage/image/icon_weixin.png',
				text: '微信',
				id: 2
			}];
			this.openWay = 2;
			// #endif

			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				//公众号是否自动登录  237
				this.$Request.get('/app/common/type/237').then(res => {
					if (res.data && res.data.value && res.data.value == '是') {
						this.openLists = [{
							image: '../../static/images/my/zhifubao.png',
							text: '支付宝',
							id: 1
						}, {
							image: '../../static/images/my/icon_weixin.png',
							text: '微信',
							id: 2
						}];
						this.openWay = 2;
					} else {
						this.openLists = [{
							image: '../../static/images/my/zhifubao.png',
							text: '支付宝',
							id: 1
						}];
						this.openWay = 1;
					}
				})
			}
			// #endif

			// this.$Request.getT('/app/common/type/244').then(res => {
			// 	if (res.code == 0) {
			// 		if (res.data && res.data.value) {
			// 			this.WxTXFangShi = res.data.value;
			// 		}
			// 	}
			// });

			// this.$Request.getT('/app/common/type/245').then(res => {
			// 	if (res.code == 0) {
			// 		if (res.data && res.data.value) {
			// 			this.checkZFB = res.data.value;
			// 			if (res.data.value === '否') {
			// 				this.openList = [{
			// 					image: '../static/icon_weixin.png',
			// 					text: '微信',
			// 					id: 2
			// 				}];
			// 				this.opentype = 2;
			// 			}
			// 		}
			// 	}
			// });

		},
		onShow() {
			// this.getMoneyType()
			// this.getMoney()
			// this.getCharge()
			// this.getMinMoney()
			// this.getMaxMoney()
			// this.getRatio()
			this.getUserInfo()
		},
		methods: {
			active(e) {
				this.wallet.forEach(res => {
					if (res.id == e.id) {
						res.isSelect = true
						this.thisSelect = e
					} else {
						res.isSelect = false
					}
				})
			},
			// getMoneyType() {
			// 	this.$Request.getT('/app/common/type/619').then(res => {
			// 		if (res.code == 0 && res.data && res.data.value) {
			// 			let money = res.data.value.split(',')
			// 			let arr = []
			// 			money.forEach((item, index) => {
			// 				let data = {}
			// 				data.id = index
			// 				data.price = item,
			// 					data.isSelect = false
			// 				arr.push(data)
			// 			})
			// 			this.wallet = arr
			// 			this.thisSelect = arr[0]
			// 			this.thisSelect.isSelect = true
			// 		}
			// 	});
			// },
			//用户收款码弹框
			isShow() {
				// this.getUserInfo()
				this.show = true
				console.log('111111111', this.show)
			},
			getUserInfo() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$Request.getT('/app/user/selectUserByUserId', data).then(res => {
					if (res.code == 0) {
						this.avatar = res.data.avatar ? res.data.avatar : '../../static/logo.png'
						uni.setStorageSync('avatar', res.data.avatar)
						this.wxImg = res.data.wxImg
						this.money = res.data.balance ? res.data.balance : '0'
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			// 提现手续费
			getCharge() {
				this.$Request.get("/app/common/type/152").then(res => {
					if (res.code == 0) {
						this.charge = res.data.value
						this.placeholder = '手续费:' + this.charge * 100 + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			// 最低提现额度
			getMinMoney() {
				this.$Request.get("/app/common/type/112").then(res => {
					if (res.code == 0) {
						this.minMoney = res.data.value
						this.placeholder = '服务费:' + this.charge * 100 + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			// 最高提现额度
			getMaxMoney() {
				this.$Request.get("/app/common/type/153").then(res => {
					if (res.code == 0) {
						this.maxMoney = res.data.value
						this.placeholder = '服务费:' + this.charge * 100 + '%' + ',最低提现:' +
							this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
					}
				});
			},
			// 金元比例
			// getRatio() {
			// 	this.$Request.get("/app/common/type/154").then(res => {
			// 		if (res.code == 0) {
			// 			this.ratio = res.data.value
			// 			this.placeholder = '服务费:' + this.charge * 100 + '%' + ',最低提现:' +
			// 				this.minMoney * 1 + ',最高提现:' + this.maxMoney * 1
			// 			this.wallet.forEach(re => {
			// 				re.num = re.price * res.data.value
			// 			})
			// 		}
			// 	});
			// },
			cut(e) {
				this.title_color = e
			},
			goNav(url) {
				uni.navigateTo({
					url
				})
			},

			// 我的余额
			// getMoney() {
			// 	let data = {
			// 		userId: uni.getStorageSync('userId')
			// 	}
			// 	this.$Request.get("/app/user/selectUserByUserId", data).then(res => {
			// 		if (res.code == 0 && res.data) {
			// 			console.log(res.data.money)
			// 			this.money = res.data.money
			// 		}
			// 	});
			// },
			selectWay: function(item) {
				this.openWay = item.id;
			},
			// pay() {
			// 	let data = {
			// 		money: this.thisSelect.price
			// 	}
			// 	console.log(data)
			// 	// 获取订单编号
			// 	this.$Request.post("/app/appOrder/insertOrder", data).then(res => {
			// 		console.log(res, '********')
			// 		uni.showLoading({
			// 			title: '支付中...'
			// 		});
			// 		if (res.code == 0) {
			// 			// this.Profit = res.data
			// 			if (this.openWay == 2) {
			// 				// #ifdef APP-PLUS
			// 				// 微信APP支付 根据订单id获取支付信息
			// 				this.$Request.post("/app/wxPay/payAppOrder", {
			// 					id: res.data.id
			// 				}).then(ret => {
			// 					this.isCheckPay(ret.code, 'wxpay', JSON.stringify(ret.data));
			// 				});
			// 				// #endif

			// 				// #ifdef MP-WEIXIN
			// 				// 微信小程序支付
			// 				this.$Request.post('/app/wxPay/wxPayJsApiOrder', {
			// 					orderId: res.data.id
			// 				}).then(ret => {
			// 					uni.hideLoading()
			// 					uni.requestPayment({
			// 						provider: 'wxpay',
			// 						timeStamp: ret.data.timestamp,
			// 						nonceStr: ret.data.noncestr,
			// 						package: ret.data.package,
			// 						signType: ret.data.signType,
			// 						paySign: ret.data.sign,
			// 						success: function(suc) {
			// 							console.log('success:' + JSON.stringify(suc));
			// 							uni.showToast({
			// 								title: '支付成功',
			// 								icon: 'success'
			// 							})
			// 							setTimeout(d => {
			// 								uni.navigateBack(1)
			// 							}, 1000);
			// 						},
			// 						fail: function(err) {
			// 							console.log('fail:' + JSON.stringify(err));
			// 							uni.showToast({
			// 								title: '支付失败',
			// 								icon: 'none'
			// 							})
			// 						}
			// 					});
			// 				});
			// 				// #endif

			// 				// #ifdef H5
			// 				let ua = navigator.userAgent.toLowerCase();
			// 				if (ua.indexOf('micromessenger') !== -1) {
			// 					this.$Request.post('/app/wxPay/wxPayMpOrder?orderId=' + res.data.id).then(res => {
			// 						if (res.code == 0) {
			// 							this.callPay(res.data);
			// 						} else {
			// 							uni.showToast({
			// 								icon: 'none',
			// 								title: '支付失败!'
			// 							});
			// 						}
			// 					});
			// 				}
			// 				// #endif

			// 			} else {
			// 				// #ifdef H5
			// 				this.$Request.post('//app/aliPay/payMoneyOrder?orderId=' + res.data.id + '&classify=2')
			// 					.then(
			// 						res => {
			// 							if (res.code === 0) {
			// 								const div = document.createElement('div')
			// 								div.innerHTML = res.data //此处form就是后台返回接收到的数据
			// 								document.body.appendChild(div)
			// 								document.forms[0].submit()
			// 							} else {
			// 								uni.showToast({
			// 									icon: 'none',
			// 									title: '支付失败!'
			// 								});
			// 							}
			// 						});
			// 				// #endif
			// 				// #ifdef APP
			// 				// APP支付宝支付
			// 				this.$Request.post("/app/aliPay/payMoneyOrder", {
			// 					orderId: res.data.id,
			// 					classify: 1
			// 				}).then(ret => {
			// 					console.log(ret)
			// 					this.isCheckPay(ret.code, 'alipay', ret.data);
			// 				});
			// 				// #endif

			// 			}
			// 		}
			// 	});
			// },
			// callPay: function(response) {
			// 	if (typeof WeixinJSBridge === "undefined") {
			// 		if (document.addEventListener) {
			// 			document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
			// 		} else if (document.attachEvent) {
			// 			document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
			// 			document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
			// 		}
			// 	} else {
			// 		this.onBridgeReady(response);
			// 	}
			// },
			// onBridgeReady: function(response) {
			// 	let that = this;
			// 	if (!response.package) {
			// 		return;
			// 	}
			// 	console.log(response, '++++++++')
			// 	WeixinJSBridge.invoke(
			// 		'getBrandWCPayRequest', {
			// 			"appId": response.appid, //公众号名称，由商户传入
			// 			"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
			// 			"nonceStr": response.noncestr, //随机串
			// 			"package": response.package,
			// 			"signType": response.signType, //微信签名方式：
			// 			"paySign": response.sign //微信签名
			// 		},
			// 		function(res) {
			// 			console.log(res, '/*-/*-/*-')
			// 			if (res.err_msg === "get_brand_wcpay_request:ok") {
			// 				// 使用以上方式判断前端返回,微信团队郑重提示：
			// 				//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
			// 				uni.showLoading({
			// 					title: '支付成功'
			// 				});
			// 				setTimeout(function() {
			// 					uni.hideLoading();
			// 				}, 1000);
			// 			} else {
			// 				uni.hideLoading();
			// 			}
			// 			WeixinJSBridge.log(response.err_msg);
			// 		}
			// 	);
			// },
			isCheckPay(status, name, order) {
				if (status == 0) {
					this.setPayment(name, order);
				} else {
					uni.hideLoading();
					uni.showToast({
						title: '支付信息有误',
						icon: 'none'
					});
				}
			},
			setPayment(name, order) {
				console.log('*-*-*')
				uni.requestPayment({
					provider: name,
					orderInfo: order, //微信、支付宝订单数据
					success: function(res) {
						console.log(res)
						uni.hideLoading();
						uni.showLoading({
							title: '支付成功'
						});
					},
					fail: function(err) {
						console.log(err)
						uni.hideLoading();
					},
					complete() {
						uni.hideLoading();
					}
				});
			},
			selectWay1: function(item) {
				this.opentype = item.id;
			},
			// 提现
			cashMoney() {
				let that = this
				if (!that.moneyNum) {
					uni.showToast({
						icon: 'none',
						title: '请输入提现金额'
					})
					return
				}
				// console.log(that.moneyNum,Number(that.moneyNum)>= Number(that.minMoney))
				if (Number(that.moneyNum) < Number(that.minMoney)) {
					uni.showToast({
						icon: 'none',
						title: '提现金额不能少于' + that.minMoney + '元'
					})
					return
				}

				if (that.moneyNum > that.money * 1) {
					uni.showToast({
						icon: 'none',
						title: '您的余额不足'
					})
					return
				}
				// if (that.moneyNum*1+that.charge*that.moneyNum > that.money*1) {
				// 	uni.showToast({
				// 		icon: 'none',
				// 		title: '您的手续费不足'
				// 	})
				// 	return
				// }
				if (that.opentype == 1) { //支付宝提现
					let zhiFuBao = uni.getStorageSync('zhiFuBao')
					let zhiFuBaoName = uni.getStorageSync('zhiFuBaoName')

					if (!zhiFuBao && !zhiFuBaoName) {
						uni.navigateTo({
							url: '/my/wallet/zhifubao'
						})
						return
					}
					that.sp = (that.moneyNum * that.charge).toFixed(2)
					uni.showModal({
						title: '提示',
						content: '本次提现' + that.moneyNum + '元，服务费' + that.sp + '元，是否确认提现？',
						success: function(res) {
							if (res.confirm) {
								that.$Request.get("/app/cash/cashMoney", {
									money: that.moneyNum,
									classify: '1',
									userType: 1
								}).then(res => {
									if (res.code == 0) {
										uni.showToast({
											icon: 'none',
											title: res.msg
										})
										that.moneyNum = null
									} else {
										uni.showToast({
											icon: 'none',
											title: res.msg
										})
									}
									that.getUserInfo()
								});
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					});
				} else if (that.opentype == 2) {
					if (that.WxTXFangShi == 2) {
						if (that.wxImg) {
							that.sp = (that.moneyNum * that.charge).toFixed(2)
							uni.showModal({
								title: "提现申请提示",
								content: '本次提现' + that.moneyNum + '元，服务费' + that.sp + '元，是否确认提现？',
								success: (e) => {
									if (e.confirm) {
										that.$queue.showLoading("提现中...");
										that.$Request.get("/app/cash/cashMoney", {
											money: that.moneyNum,
											classify: '2',
											userType: 1
										}).then(res => {
											uni.hideLoading();
											if (res.code == 0) {
												uni.showToast({
													icon: 'none',
													title: res.msg
												})
												that.moneyNum = null
											} else {
												uni.showToast({
													icon: 'none',
													title: res.msg
												})
											}
											that.getUserInfo()
										});
									}
								}
							});
						} else {
							that.show = true;
						}
					} else {
						that.$queue.showLoading("提现中...");
						that.$Request.get("/app/cash/cashMoney", {
							money: that.moneyNum,
							classify: '2',
							userType: 1
						}).then(res => {
							uni.hideLoading();
							if (res.code == 0) {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
								that.moneyNum = null
							} else {
								uni.showToast({
									icon: 'none',
									title: res.msg
								})
							}
							that.getUserInfo()
						});
					}
				}
			},
			weixin() {
				let that = this
				uni.chooseImage({
					count: 1, //默认9
					sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
					sourceType: ['album', 'camera'], //从相册选择
					success: (res) => {
						that.$queue.showLoading("上传中...");
						for (let i = 0; i < 1; i++) {
							uni.uploadFile({ // 上传接口
								url: "https://xianyu.xianmaxiong.com/sqx_fast/alioss/upload", //真实的接口地址
								// url: 'https://tcwm.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									// let wxImg = JSON.parse(uploadFileRes.data).data
									// let userId = that.$queue.getData('userId');
									let data = {
										wxImg: JSON.parse(uploadFileRes.data).data
									}
									that.$Request.postJson('/app/user/updateUser',
										data).then(
										res => {
											if (res.code == 0) {
												that.getUserInfo()
												setTimeout(function() {
													that.$queue.showToast('上传成功')
												}, 1000)
												that.getUserInfo()
												that.show = false
											}
										});
									uni.hideLoading();

								}
							});
						}

					}
				});
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			}
		}
	}
</script>

<style lang="scss">
	$primary: #15CAD2;
	$primary-dark: #0C98A1;
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;

	page {
		background: #F3F6F8;
	}

	.wallet-page {
		min-height: 100vh;
	}

	.wallet-shell {
		padding: 24rpx;
	}

	.wallet-hero {
		padding: 32rpx 30rpx;
		border-radius: 28rpx;
		background: linear-gradient(135deg, #21C9D1 0%, #0EAFB6 100%);
		box-shadow: 0 16rpx 36rpx rgba(16, 137, 145, 0.16);
		display: flex;
		justify-content: space-between;
		gap: 20rpx;
		color: #FFFFFF;
	}

	.wallet-eyebrow {
		font-size: 22rpx;
		opacity: 0.82;
	}

	.wallet-balance-label {
		font-size: 28rpx;
		margin-top: 14rpx;
	}

	.wallet-balance {
		font-size: 62rpx;
		font-weight: 700;
		line-height: 1.1;
		margin-top: 12rpx;

		text {
			font-size: 28rpx;
			font-weight: 500;
			margin-left: 6rpx;
		}
	}

	.wallet-hint {
		margin-top: 18rpx;
		font-size: 22rpx;
		line-height: 1.6;
		opacity: 0.88;
	}

	.wallet-badge {
		width: 92rpx;
		height: 92rpx;
		border-radius: 28rpx;
		background: rgba(255, 255, 255, 0.16);
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.wallet-badge-icon {
		width: 46rpx;
		height: 46rpx;
	}

	.wallet-card {
		margin-top: 18rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 26rpx 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.section-title {
		font-size: 28rpx;
		font-weight: 700;
		color: $text-main;
	}

	.amount-input-wrap {
		margin-top: 18rpx;
		height: 92rpx;
		border-radius: 20rpx;
		background: #F7FAFB;
		border: 1rpx solid #E6EDF1;
		display: flex;
		align-items: center;
		padding: 0 24rpx;
	}

	.amount-prefix {
		font-size: 36rpx;
		font-weight: 700;
		color: $text-main;
		margin-right: 16rpx;
	}

	.amount-input {
		flex: 1;
		height: 92rpx;
		line-height: 92rpx;
		font-size: 30rpx;
		color: $text-main;
	}

	.wallet-desc {
		margin-top: 14rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: $text-soft;
	}

	.method-list {
		margin-top: 12rpx;
		display: flex;
		flex-direction: column;
	}

	.method-item {
		height: 92rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		border-bottom: 1rpx solid #EEF3F5;
		transition: all 0.2s ease;

		&:last-child {
			border-bottom: none;
		}
	}

	.method-item--active {
		.method-name {
			color: $primary-dark;
			font-weight: 600;
		}
	}

	.method-main {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.method-icon {
		width: 44rpx;
		height: 44rpx;
		border-radius: 50%;
	}

	.method-name {
		font-size: 28rpx;
		color: $text-main;
	}

	.method-radio {
		width: 36rpx;
		height: 36rpx;
		border-radius: 50%;
		border: 2rpx solid #C8D5DB;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.method-radio-dot {
		width: 18rpx;
		height: 18rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #21C9D1 0%, #0EAFB6 100%);
	}

	.wallet-submit {
		height: 88rpx;
		line-height: 88rpx;
		border-radius: 44rpx;
		background: linear-gradient(135deg, #21C9D1 0%, #0EAFB6 100%);
		color: #FFFFFF;
		font-size: 28rpx;
		font-weight: 700;
		text-align: center;
		margin-top: 26rpx;
		box-shadow: 0 12rpx 24rpx rgba(16, 137, 145, 0.16);
	}

	.quick-grid {
		margin-top: 20rpx;
		display: grid;
		grid-template-columns: repeat(4, minmax(0, 1fr));
		gap: 18rpx 8rpx;
	}

	.quick-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 12rpx;
		text-align: center;

		text {
			font-size: 22rpx;
			line-height: 1.4;
			color: $text-sub;
		}
	}

	.quick-icon {
		width: 80rpx;
		height: 80rpx;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			width: 42rpx;
			height: 42rpx;
		}
	}

	.quick-icon--wallet { background: #FDF1E8; }
	.quick-icon--code { background: #EAF7EE; }
	.quick-icon--record { background: #EEF5FD; }
	.quick-icon--detail { background: #ECF9FA; }

	.wallet-popup {
		width: 620rpx;
		background: #FFFFFF;
		border-radius: 28rpx;
		padding: 28rpx 28rpx 32rpx;
	}

	.popup-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.popup-title {
		font-size: 30rpx;
		font-weight: 700;
		color: $text-main;
	}

	.popup-close {
		width: 48rpx;
		height: 48rpx;
		border-radius: 50%;
		background: #F4F7F8;
		color: $text-soft;
		font-size: 34rpx;
		line-height: 48rpx;
		text-align: center;
	}

	.popup-body {
		margin-top: 24rpx;
	}

	.qr-upload {
		width: 100%;
		min-height: 360rpx;
		border-radius: 22rpx;
		background: #F7FAFB;
		border: 1rpx dashed #D7E3E8;
		overflow: hidden;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			width: 100%;
		}
	}

	.popup-tip {
		margin-top: 16rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: $text-soft;
	}
</style>
