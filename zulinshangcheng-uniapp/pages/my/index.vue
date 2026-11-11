<template>
	<view class="my-page">
		<!-- 顶部背景 -->
		<view class="header-bg">
			<view class="header-grid"></view>
			<view class="header-highlight"></view>
			<view class="header-shadow"></view>
		</view>

		<!-- 用户信息卡片 -->
		<view class="user-card">
			<view class="user-main">
				<view class="avatar-wrap" @click="routeGo('/pages/my/userinfo')">
					<image class="avatar" :src="userInfo.avatar ? userInfo.avatar : '/static/logo.png'" mode="aspectFill"></image>
					<view class="vip-corner" v-if="userInfo.isVip == 1"><text>V</text></view>
				</view>

				<view class="user-info" v-if="Object.keys(userInfo).length > 0">
					<view class="name-line">
						<text class="username">{{userInfo.userName}}</text>
						<view class="credit-tag">
							<text class="dot"></text>
							<text>信誉{{userInfo.reputationStatus}}</text>
						</view>
					</view>
					<view class="auth-line">
						<text class="auth-tag" :class="{'auth-pass': userInfo.status1 == 1}" @click.stop="routeGo('/subpackage/authentication/authentication')">
							{{ userInfo.isAuth == 0 ? '未实名认证' : userInfo.status1 == 2 ? '认证未通过' : userInfo.status1 == 0 ? '审核中' : '已实名认证' }}
						</text>
					</view>
				</view>

				<view class="user-info" v-else @click="navigate('/pages/public/login')">
					<text class="username">点击登录</text>
					<text class="auth-tag">登录后享受更多服务</text>
				</view>

				<view class="msg-btn" @click="routeGo('/pages/msg/index')">
					<view class="msg-btn-icon">
						<u-icon name="email-fill" size="34" color="#FFFFFF"></u-icon>
					</view>
					<u-badge v-if="AllchatCount > 0" type="error" :count="AllchatCount" absolute :offset="[-4, -4]"></u-badge>
				</view>
			</view>

			<view class="stats-bar">
				<view class="stat-item" @click="routeGo('/my/myCollection')">
					<text class="stat-num">{{userData.collectCount || '0'}}</text>
					<text class="stat-label">收藏</text>
				</view>
				<view class="stat-item" @click="routeGo('/my/liulanGoods')">
					<text class="stat-num">{{userData.browseCount || '0'}}</text>
					<text class="stat-label">浏览记录</text>
				</view>
				<view class="stat-item" @click="routeGo('/my/ticket/ticket')">
					<text class="stat-num">{{userData.couponCount || '0'}}</text>
					<text class="stat-label">优惠券</text>
				</view>
			</view>
		</view>

		<!-- VIP 卡片 -->
		<view class="vip-card" @click="routeGo('/my/vip/index')">
			<view class="vip-card__shine"></view>
			<view class="vip-card__mesh"></view>
			<view class="vip-inner">
				<view class="vip-top">
					<view class="vip-left">
						<view class="vip-badge">
							<image class="vip-badge-icon" src="/static/zlImage/my/vip.png" mode="aspectFit"></image>
						</view>
						<view class="vip-meta">
							<text class="vip-eyebrow">会员专属</text>
							<text class="vip-title">VIP 尊享卡</text>
							<text class="vip-desc">折扣租赁、优先服务、专属权益</text>
						</view>
					</view>

					<view class="vip-action" :class="{'vip-active': userInfo.isVip == 1}">
						<text>{{userInfo.isVip == 1 ? '会员中' : '立即开通'}}</text>
						<u-icon v-if="userInfo.isVip != 1" name="arrow-right" size="18" color="#24170A"></u-icon>
					</view>
				</view>

				

				<view class="vip-footer">
					<text class="vip-note">{{userInfo.isVip == 1 ? '当前已开通会员权益，可继续享受专属优惠' : '开通后可享专属折扣价与更快服务响应'}}</text>
					
				</view>
			</view>
		</view>

		<!-- 我的订单 -->
		<view class="order-card">
			<view class="card-header" @click="routeGo('/pages/order/index')">
				<view class="header-left">
					<view class="header-bar"></view>
					<text class="header-title">我的订单</text>
				</view>
				<view class="header-right">
					<text>全部订单</text>
					<u-icon name="arrow-right" size="20" color="#bbb"></u-icon>
				</view>
			</view>
			<view class="order-grid">
				<view class="order-item" @click="routeGo('/pages/order/index?status=0')">
					<view class="order-icon"><u-icon name="order" size="40" color="#15CAD2"></u-icon></view>
					<text class="order-label">待付款</text>
				</view>
				<view class="order-item" @click="routeGo('/pages/order/index?status=1')">
					<view class="order-icon"><u-icon name="bag" size="40" color="#15CAD2"></u-icon></view>
					<text class="order-label">待发货</text>
				</view>
				<view class="order-item" @click="routeGo('/pages/order/index?status=2')">
					<view class="order-icon"><u-icon name="car" size="40" color="#15CAD2"></u-icon></view>
					<text class="order-label">待收货</text>
				</view>
				<view class="order-item" @click="routeGo('/pages/order/index?status=10')">
					<view class="order-icon"><u-icon name="chat" size="40" color="#15CAD2"></u-icon></view>
					<text class="order-label">待评价</text>
				</view>
			</view>
		</view>

		<!-- 常用服务 -->
		<view class="service-card">
			<view class="card-header no-border">
				<view class="header-left">
					<view class="header-bar"></view>
					<text class="header-title">常用服务</text>
				</view>
			</view>
			<view class="service-grid">
				<view class="service-item" @click="routeGo('/my/realName')">
					<view class="service-icon" style="background: #E6F8F9;">
						<u-icon name="man-add" size="40" color="#15CAD2"></u-icon>
					</view>
					<text class="service-label">实名认证</text>
				</view>
				<view class="service-item" @click="routeGo('/my/wallet/index')">
					<view class="service-icon" style="background: #FFF5E6;">
						<u-icon name="coupon" size="40" color="#FF9500"></u-icon>
					</view>
					<text class="service-label">我的钱包</text>
				</view>
				<view class="service-item" @click="routeGo('/my/address/index')">
					<view class="service-icon" style="background: #E8F5E9;">
						<u-icon name="map" size="40" color="#52C41A"></u-icon>
					</view>
					<text class="service-label">地址管理</text>
				</view>
				<view class="service-item" @click="routeGo('/my/kefu/kefu')">
					<view class="service-icon" style="background: #E3F2FD;">
						<u-icon name="server-man" size="40" color="#2196F3"></u-icon>
					</view>
					<text class="service-label">客服中心</text>
				</view>
				<view class="service-item" @click="routeGo('/my/feedback')">
					<view class="service-icon" style="background: #F3E5F5;">
						<u-icon name="edit-pen" size="40" color="#9C27B0"></u-icon>
					</view>
					<text class="service-label">意见反馈</text>
				</view>
				<view class="service-item" @click="routeGo('/my/setting/about')">
					<view class="service-icon" style="background: #E0F2F1;">
						<u-icon name="info-circle" size="40" color="#009688"></u-icon>
					</view>
					<text class="service-label">关于我们</text>
				</view>
				<view class="service-item" @click="routeGo('/my/setting/index')">
					<view class="service-icon" style="background: #ECEFF1;">
						<u-icon name="setting" size="40" color="#607D8B"></u-icon>
					</view>
					<text class="service-label">设置中心</text>
				</view>
			</view>
		</view>

		<view class="safe-bottom"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				isLogin: true,
				isVip: false,
				userInfo: {},
				userData: {},
				messageCount: 2,
				AllchatCount: 0,
				time: null,
				arr: [],
				showModal: true,
			}
		},
		onLoad() {
			let that = this
			this.$Request.getT('/app/common/type/236').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.arr.push(res.data.value)
					}
				}
			})
			this.$Request.getT('/app/common/type/522').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.arr.push(res.data.value)
					}
				}
			})
		},
		onShow() {
			let that = this
			if (uni.getStorageSync('token')) {
				this.AllchatCount = uni.getStorageSync('AllchatCount')
				this.getUserInfo()
				this.getData()
				that.time = setInterval(function() {
					that.$nextTick(function() {
						that.AllchatCount = uni.getStorageSync('AllchatCount')
					})
				}, 3000)
			} else {
				this.userInfo = {}
				this.userData = {}
				this.AllchatCount = 0
			}
			let userId = uni.getStorageSync('userId');
			// #ifdef MP-WEIXIN
			if (userId) {
				if (!uni.getStorageSync('sendindexMsg')) {
					that.openMsg()
				}
			}
			// #endif
		},
		onHide() {
			if (this.time) clearInterval(this.time)
		},
		onUnload() {
			if (this.time) clearInterval(this.time)
		},
		methods: {
			pageGoon(url) {
				let token = uni.getStorageSync('token')
				if (token) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendindexMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) {
								var datas = JSON.stringify(re);
								if (datas.indexOf("accept") != -1) {
									console.log(re)
								}
							},
							fail: (res) => {
								console.log(res)
							}
						})
					}
					// #endif
				}
				uni.navigateTo({
					url: url
				})
			},
			openMsg() {
				var that = this
				wx.getSetting({
					withSubscriptions: true,
					success(ret) {
						if (ret.subscriptionsSetting.itemSettings) {
							uni.setStorageSync('sendindexMsg', true)
							uni.openSetting({
								success(rea) {
									console.log(rea.authSetting)
								}
							});
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
													console.log(re)
													uni.setStorageSync('sendindexMsg', true)
												}
											},
											fail: (res) => {
												console.log(res)
											}
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
			routeGo(url) {
				if (uni.getStorageSync('token')) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendindexMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) {
								var datas = JSON.stringify(re);
								if (datas.indexOf("accept") != -1) {
									console.log(re)
								}
							},
							fail: (res) => {
								console.log(res)
							}
						})
					}
					// #endif
					if (url == '/my/kefu/kefu') {
						this.Changekefu()
					} else if (url.indexOf('/pages/order/index') === 0) {
						const statusMatch = url.match(/[?&]status=([^&]+)/)
						uni.setStorageSync('orderStatus', statusMatch ? decodeURIComponent(statusMatch[1]) : '')
						uni.switchTab({
							url: '/pages/order/index'
						})
					} else {
						uni.navigateTo({
							url: url
						})
					}
				} else {
					this.noLogin()
				}
			},
			noLogin() {
				let that = this;
				uni.showModal({
					title: '提示',
					content: '您还未登录,请先登录！',
					showCancel: false,
					success: function(res) {
						if (res.confirm) {
							uni.navigateTo({
								url: '/pages/public/login'
							})
						}
					}
				});
			},
			Changekefu() {
				if (uni.getStorageSync('kefuType') == 1) {
					uni.makePhoneCall({
						phoneNumber: uni.getStorageSync('kefuPhone')
					});
				} else if (uni.getStorageSync('kefuType') == 3) {
					uni.navigateTo({
						url: '/mine/kefu/kefu'
					})
				} else {
					// #ifdef MP-WEIXIN
					let that = this
					try {
						wx.openCustomerServiceChat({
							extInfo: {
								url: that.$queue.getData('kefuUrl')
							},
							corpId: that.$queue.getData('kefuAppId'),
							success(res) {},
							fail(res) {
								console.error(res)
							}
						})
					} catch (error) {
						console.error("catchcatch" + error)
						uni.showToast({
							title: '请更新至微信最新版本'
						});
					}
					// #endif
					// #ifndef MP-WEIXIN
					let url = this.$queue.getData('kefuUrl');
					if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
						uni.navigateTo({
							url
						});
					} else {
						//#ifndef H5
						uni.navigateTo({
							url: '/pages/index/webView?url=' + url
						});
						//#endif
						//#ifdef H5
						window.location.href = url;
						//#endif
					}
					// #endif
				}
			},
			getUserInfo() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$Request.getT('/app/user/selectUserByUserId', data).then(res => {
					if (res.code == 0) {
						this.userInfo = res.data
						if (this.userInfo.reputationStatus == 1) {
							this.userInfo.reputationStatus = '差'
						} else if (this.userInfo.reputationStatus == 2) {
							this.userInfo.reputationStatus = '良好'
						} else if (this.userInfo.reputationStatus == 3) {
							this.userInfo.reputationStatus = '优'
						}
						uni.setStorageSync('isVip', res.data.isVip)
					} else {
						this.userInfo = {}
					}
				})
			},
			getData() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$Request.getT('/app/Login/getDataCenter', data).then(res => {
					if (res.code == 0) {
						this.userData = res.data.data
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	/* ========== 设计令牌 ========== */
	$primary: #15CAD2;
	$primary-dark: #0C98A1;
	$primary-deep: #0A7F86;
	$primary-light: #E7F8FA;
	$panel-border: rgba(255, 255, 255, 0.7);
	$text-main: #1A2233;
	$text-sub: #666666;
	$text-hint: #999999;
	$vip-gold: #D4AF37;
	$vip-gold-light: #F0E6C8;

	page {
		background:
			linear-gradient(180deg, #73E3E8 0%, #9DECEF 18%, #C6F6F7 42%, #E3FBFC 68%, #F2FEFE 100%),
			repeating-linear-gradient(135deg, rgba(255, 255, 255, 0.14) 0, rgba(255, 255, 255, 0.14) 16rpx, rgba(255, 255, 255, 0) 16rpx, rgba(255, 255, 255, 0) 44rpx);
		font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
		-webkit-font-smoothing: antialiased;
		position: relative;
	}

	/* 页面环境层 */
	.my-page::before {
		content: '';
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		height: 520rpx;
		background: linear-gradient(180deg, rgba(12, 152, 161, 0.22) 0%, rgba(12, 152, 161, 0.1) 42%, rgba(12, 152, 161, 0) 100%);
		pointer-events: none;
		z-index: 0;
	}

	.my-page::after {
		content: '';
		position: fixed;
		inset: 0;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.06) 0%, rgba(157, 236, 239, 0.02) 30%, rgba(255, 255, 255, 0.12) 100%);
		pointer-events: none;
		z-index: 0;
	}

	/* ========== 顶部背景 ========== */
	.header-bg {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 228rpx;
		background: linear-gradient(135deg, #25D1D8 0%, $primary 42%, $primary-dark 100%);
		border-radius: 0 0 40rpx 40rpx;
		overflow: hidden;
		z-index: 0;
		box-shadow: inset 0 -24rpx 40rpx rgba(6, 82, 87, 0.14);
	}

	.header-grid {
		position: absolute;
		inset: 0;
		background:
			linear-gradient(90deg, rgba(255, 255, 255, 0.1) 1rpx, transparent 1rpx),
			linear-gradient(180deg, rgba(255, 255, 255, 0.08) 1rpx, transparent 1rpx);
		background-size: 44rpx 44rpx;
		opacity: 0.22;
	}

	.header-highlight {
		position: absolute;
		top: 24rpx;
		left: 28rpx;
		width: 320rpx;
		height: 88rpx;
		background: linear-gradient(90deg, rgba(255, 255, 255, 0.34), rgba(255, 255, 255, 0));
		transform: skewX(-26deg);
	}

	.header-shadow {
		position: absolute;
		left: 0;
		right: 0;
		bottom: 0;
		height: 72rpx;
		background: linear-gradient(180deg, rgba(10, 127, 134, 0) 0%, rgba(10, 127, 134, 0.26) 100%);
	}

	/* ========== 页面根 ========== */
	.my-page {
		position: relative;
		padding: 0 30rpx;
		box-sizing: border-box;
	}

	/* ========== 用户卡片 ========== */
	.user-card {
		position: relative;
		z-index: 2;
		margin-top: 14rpx;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.97) 0%, rgba(255, 255, 255, 0.92) 100%);
		border-radius: 32rpx;
		padding: 36rpx 32rpx 32rpx;
		box-shadow: 0 18rpx 44rpx rgba(32, 72, 92, 0.08);
		border: 1rpx solid $panel-border;
	}

	.user-main {
		display: flex;
		align-items: center;
	}

	/* 头像 */
	.avatar-wrap {
		position: relative;
		flex-shrink: 0;
		width: 120rpx;
		height: 120rpx;
		padding: 4rpx;
		background: linear-gradient(135deg, $primary, $primary-light);
		border-radius: 50%;
		box-sizing: border-box;
		box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.2);

		.avatar {
			width: 100%;
			height: 100%;
			border-radius: 50%;
			border: 4rpx solid #fff;
			box-sizing: border-box;
			display: block;
		}

		.vip-corner {
			position: absolute;
			bottom: 0;
			right: 0;
			width: 36rpx;
			height: 36rpx;
			background: linear-gradient(135deg, #D4AF37, #B8941F);
			border-radius: 50%;
			border: 2rpx solid #fff;
			display: flex;
			align-items: center;
			justify-content: center;

			text {
				font-size: 18rpx;
				font-weight: 800;
				color: #1A1A1A;
			}
		}
	}

	/* 用户信息 */
	.user-info {
		flex: 1;
		margin-left: 24rpx;
		min-width: 0;
		display: flex;
		flex-direction: column;
		justify-content: center;
		gap: 14rpx;
	}

	.name-line {
		display: flex;
		align-items: center;
		gap: 12rpx;
		flex-wrap: wrap;

		.username {
			font-size: 38rpx;
			font-weight: 700;
			color: $text-main;
			line-height: 1.2;
		}

		.credit-tag {
			display: inline-flex;
			align-items: center;
			height: 36rpx;
			padding: 0 14rpx;
			background: $primary-light;
			border-radius: 18rpx;
			font-size: 22rpx;
			color: $primary;
			font-weight: 500;

			.dot {
				width: 10rpx;
				height: 10rpx;
				background: #52C41A;
				border-radius: 50%;
				margin-right: 8rpx;
			}
		}
	}

	.auth-line {
		.auth-tag {
			display: inline-block;
			height: 40rpx;
			line-height: 40rpx;
			padding: 0 16rpx;
			background: #F5F5F5;
			border-radius: 999rpx;
			font-size: 22rpx;
			color: $text-hint;
			transition: all 0.2s;

			&.auth-pass {
				background: $primary-light;
				color: $primary;
			}

			&:active {
				opacity: 0.7;
			}
		}
	}

	/* 消息按钮 */
	.msg-btn {
		position: relative;
		width: 84rpx;
		height: 84rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.96) 0%, rgba(235, 250, 251, 0.98) 100%);
		border-radius: 28rpx;
		margin-left: 16rpx;
		flex-shrink: 0;
		transition: all 0.2s;
		box-shadow: 0 10rpx 24rpx rgba(21, 202, 210, 0.12), inset 0 1rpx 0 rgba(255, 255, 255, 0.92);
		border: 1rpx solid rgba(21, 202, 210, 0.12);

		&:active {
			transform: scale(0.92);
		}
	}

	.msg-btn-icon {
		width: 56rpx;
		height: 56rpx;
		border-radius: 20rpx;
		background: linear-gradient(135deg, #2AD5DB 0%, $primary-dark 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 18rpx rgba(21, 202, 210, 0.24);
	}

	/* ========== 数据统计 ========== */
	.stats-bar {
		display: flex;
		align-items: center;
		margin-top: 36rpx;
		padding-top: 28rpx;
		border-top: 1rpx solid rgba(16, 86, 92, 0.08);
	}

	.stat-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
		position: relative;

		&:active {
			opacity: 0.6;
		}

		&:not(:last-child)::after {
			content: '';
			position: absolute;
			right: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 1rpx;
			height: 44rpx;
			background: #EEEEEE;
		}

		.stat-num {
			font-size: 40rpx;
			font-weight: 700;
			color: $text-main;
			line-height: 1.2;
		}

		.stat-label {
			font-size: 24rpx;
			color: $text-sub;
		}
	}

	/* ========== VIP 卡片 ========== */
	.vip-card {
		position: relative;
		z-index: 1;
		margin-top: 24rpx;
		border-radius: 32rpx;
		padding: 0;
		overflow: hidden;
		background: linear-gradient(135deg, #3A2A15 0%, #22170D 44%, #110C08 100%);
		box-shadow: 0 20rpx 60rpx rgba(32, 18, 8, 0.24);
		border: 1rpx solid rgba(255, 232, 190, 0.12);

		&:active {
			transform: translateY(2rpx);
		}
	}

	.vip-card__shine {
		position: absolute;
		top: 0;
		left: -24%;
		width: 48%;
		height: 100%;
		background: linear-gradient(90deg, rgba(255, 255, 255, 0), rgba(255, 240, 212, 0.16), rgba(255, 255, 255, 0));
		transform: skewX(-24deg);
	}

	.vip-card__mesh {
		position: absolute;
		inset: 0;
		background-image: repeating-linear-gradient(
			-36deg,
			transparent,
			transparent 18rpx,
			rgba(255, 232, 190, 0.05) 18rpx,
			rgba(255, 232, 190, 0.05) 20rpx
		);
		pointer-events: none;
	}

	.vip-inner {
		position: relative;
		z-index: 1;
		padding: 32rpx;
	}

	.vip-top {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		flex-wrap: wrap;
		gap: 20rpx;
		position: relative;
	}

	.vip-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
		min-width: 0;
		flex: 1;
	}

	.vip-badge {
		position: relative;
		width: 96rpx;
		height: 96rpx;
		background: linear-gradient(135deg, rgba(255, 223, 162, 0.24), rgba(255, 223, 162, 0.1));
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.2);
		flex-shrink: 0;
		border: 1rpx solid rgba(255, 232, 190, 0.14);

		&::before {
			content: '';
			position: absolute;
			inset: 8rpx;
			border: 1rpx solid rgba(255, 232, 190, 0.18);
			border-radius: 18rpx;
		}
	}

	.vip-badge-icon {
		width: 54rpx;
		height: 54rpx;
		position: relative;
		z-index: 1;
	}

	.vip-meta {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
		min-width: 0;

		.vip-eyebrow {
			font-size: 20rpx;
			color: rgba(245, 213, 141, 0.78);
			letter-spacing: 0;
		}

		.vip-title {
			font-size: 34rpx;
			font-weight: 700;
			color: $vip-gold-light;
			letter-spacing: 0;
		}

		.vip-desc {
			font-size: 22rpx;
			color: rgba(255, 245, 227, 0.6);
			line-height: 1.5;
		}
	}

	.vip-action {
		min-width: 148rpx;
		height: 64rpx;
		padding: 0 24rpx;
		background: linear-gradient(135deg, #F0CB79, #D4AF37);
		border-radius: 999rpx;
		font-size: 24rpx;
		font-weight: 600;
		color: #24170A;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 6rpx;
		transition: all 0.2s;
		flex-shrink: 0;
		box-shadow: 0 8rpx 18rpx rgba(212, 175, 55, 0.22);

		&:active {
			transform: scale(0.95);
			box-shadow: 0 2rpx 8rpx rgba(212, 175, 55, 0.15);
		}

		&.vip-active {
			background: rgba(255, 223, 162, 0.12);
			color: #F5D58D;
			border: 1rpx solid rgba(245, 213, 141, 0.24);
			box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.08);
		}
	}

	.vip-benefits {
		display: flex;
		flex-wrap: wrap;
		gap: 16rpx;
		margin-top: 28rpx;
	}

	.benefit-item {
		display: inline-flex;
		align-items: center;
		gap: 10rpx;
		height: 52rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: rgba(255, 255, 255, 0.06);
		border: 1rpx solid rgba(255, 232, 190, 0.12);
		color: rgba(255, 245, 227, 0.84);
		font-size: 22rpx;
	}

	.benefit-icon {
		width: 26rpx;
		height: 26rpx;
		flex-shrink: 0;
	}

	.vip-footer {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
		margin-top: 28rpx;
		padding-top: 24rpx;
		border-top: 1rpx solid rgba(255, 232, 190, 0.12);
	}

	.vip-note {
		flex: 1;
		font-size: 22rpx;
		line-height: 1.6;
		color: rgba(255, 245, 227, 0.56);
	}

	.vip-footer-icon {
		width: 88rpx;
		height: 48rpx;
		flex-shrink: 0;
		opacity: 0.88;
	}

	/* ========== 通用卡片头部 ========== */
	.card-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 8rpx;
		padding-bottom: 20rpx;
		border-bottom: 1rpx solid #F5F5F5;

		&.no-border {
			border-bottom: none;
			padding-bottom: 0;
		}

		.header-left {
			display: flex;
			align-items: center;
			gap: 12rpx;

			.header-bar {
				width: 6rpx;
				height: 28rpx;
				background: linear-gradient(180deg, $primary, $primary-dark);
				border-radius: 4rpx;
			}

			.header-title {
				font-size: 32rpx;
				font-weight: 700;
				color: $text-main;
			}
		}

		.header-right {
			display: flex;
			align-items: center;
			gap: 4rpx;
			font-size: 24rpx;
			color: $text-hint;
			transition: all 0.2s;

			&:active {
				opacity: 0.6;
			}
		}
	}

	/* ========== 我的订单 ========== */
	.order-card {
		position: relative;
		z-index: 1;
		margin-top: 24rpx;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.96) 0%, rgba(255, 255, 255, 0.9) 100%);
		border-radius: 28rpx;
		padding: 28rpx 24rpx 20rpx;
		box-shadow: 0 14rpx 32rpx rgba(32, 72, 92, 0.06);
		border: 1rpx solid $panel-border;
	}

	.order-grid {
		display: flex;
		align-items: center;
		padding-top: 8rpx;
	}

	.order-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 12rpx;
		padding: 16rpx 0;
		transition: all 0.15s;

		&:active {
			transform: scale(0.92);
			opacity: 0.8;
		}

		.order-icon {
			width: 84rpx;
			height: 84rpx;
			background: linear-gradient(180deg, #F0FBFC 0%, #E2F7F9 100%);
			border-radius: 26rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.9);
		}

		.order-label {
			font-size: 24rpx;
			color: $text-sub;
		}
	}

	/* ========== 常用服务 ========== */
	.service-card {
		position: relative;
		z-index: 1;
		margin-top: 24rpx;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.96) 0%, rgba(255, 255, 255, 0.9) 100%);
		border-radius: 28rpx;
		padding: 28rpx 24rpx 16rpx;
		box-shadow: 0 14rpx 32rpx rgba(32, 72, 92, 0.06);
		border: 1rpx solid $panel-border;
	}

	.service-grid {
		display: flex;
		flex-wrap: wrap;
	}

	.service-item {
		width: 25%;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 24rpx 0 16rpx;
		transition: all 0.15s;

		&:active {
			transform: scale(0.92);
			opacity: 0.8;
		}

		.service-icon {
			width: 88rpx;
			height: 88rpx;
			border-radius: 24rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 14rpx;
			box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.85);
		}

		.service-label {
			font-size: 24rpx;
			color: $text-sub;
			line-height: 1.4;
		}
	}

	/* ========== 安全区 ========== */
	.safe-bottom {
		height: env(safe-area-inset-bottom);
	}
</style>
