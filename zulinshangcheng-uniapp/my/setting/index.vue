<template>
	<view class="setting-page">
		<view class="setting-shell">
			<view class="setting-hero">
				<view class="hero-main">
					<view>
						<view class="hero-title">设置中心</view>
						<view class="hero-subtitle">管理帮助信息、协议条款与账号安全相关设置</view>
					</view>
					<view class="hero-badge">
						<u-icon name="setting" size="34" color="#0EAFB6"></u-icon>
					</view>
				</view>
				<view class="hero-note">
					<view class="hero-note-dot"></view>
					<text>涉及账号相关操作前，请确认当前登录状态与绑定信息</text>
				</view>
			</view>

			<view class="setting-card">
				<view class="card-title">基础服务</view>
				<!-- <view class="setting-row" @click="goNav('/my/feedback/index')" v-if="XCXIsSelect!='否'">
					<view class="row-main">
						<view class="row-icon feedback-icon">
							<u-icon name="edit-pen" size="26" color="#15CAD2"></u-icon>
						</view>
						<view class="row-content">
							<view class="row-title">意见反馈</view>
							<view class="row-desc">提交问题与建议</view>
						</view>
					</view>
					<u-icon name="arrow-right" color="#A0AAB8" size="24"></u-icon>
				</view> -->
				<view class="setting-row" @click="goNav('/my/setting/help')">
					<view class="row-main">
						<view class="row-icon help-icon">
							<u-icon name="server-man" size="26" color="#15CAD2"></u-icon>
						</view>
						<view class="row-content">
							<view class="row-title">帮助中心</view>
							<view class="row-desc">查看常见问题与使用说明</view>
						</view>
					</view>
					<u-icon name="arrow-right" color="#A0AAB8" size="24"></u-icon>
				</view>
				<view class="setting-row" @click="goNav('/my/setting/xieyi?type=516')">
					<view class="row-main">
						<view class="row-icon agreement-icon">
							<u-icon name="file-text" size="26" color="#4F7CFF"></u-icon>
						</view>
						<view class="row-content">
							<view class="row-title">用户协议</view>
							<view class="row-desc">查看平台服务条款与使用规范</view>
						</view>
					</view>
					<u-icon name="arrow-right" color="#A0AAB8" size="24"></u-icon>
				</view>
				<view class="setting-row is-last" @click="goNav('/my/setting/xieyi?type=176')">
					<view class="row-main">
						<view class="row-icon privacy-icon">
							<u-icon name="info-circle" size="26" color="#7A63FF"></u-icon>
						</view>
						<view class="row-content">
							<view class="row-title">隐私政策</view>
							<view class="row-desc">了解个人信息收集与使用规则</view>
						</view>
					</view>
					<u-icon name="arrow-right" color="#A0AAB8" size="24"></u-icon>
				</view>
			</view>

			<view class="setting-card danger-card">
				<view class="card-title">账号安全</view>
				<view class="setting-row is-last" @click="goNav('/my/setting/zhuxiao')">
					<view class="row-main">
						<view class="row-icon danger-icon">
							<u-icon name="close-circle" size="26" color="#FF6B6B"></u-icon>
						</view>
						<view class="row-content">
							<view class="row-title">注销账号</view>
							<view class="row-desc">申请注销并清除账号关联信息</view>
						</view>
					</view>
					<u-icon name="arrow-right" color="#A0AAB8" size="24"></u-icon>
				</view>
			</view>

			<view class="logout-btn" @click="goOut">退出登录</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				XCXIsSelect: '否',
				userId: "",
				iosShangjia: '否',
				checked: true,
				checkedgx: true
			}
		},
		onLoad() {
			// #ifdef APP-PLUS
			// console.log(this.$queue.getData('gexing'))
			let gexing = this.$queue.getData('gexing');
			if (gexing == 1) {
				this.checkedgx = true;
			} else if (gexing == 2) {
				this.checkedgx = false;
			} else {
				this.checkedgx = true;
			}
			// #endif

			this.getUserInfo()
			this.XCXIsSelect = this.$queue.getData("XCXIsSelect");
		},
		onShow() {
			this.iosShangjia = uni.getStorageSync('iosShangjia')
			this.userId = uni.getStorageSync('userId')

		},
		methods: {
			getUserInfo() {
				this.$Request.getT("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						console.log(res.data.isSendMsg)
						if (res.data.isSendMsg) {
							this.checked = res.data.isSendMsg == 1 || res.data.isSendMsg == null ? true : false
						}

					}
				});
			},
			changegx(status) {
				console.log(status)
				if (status) {
					this.$queue.setData('gexing', 1);
				} else {
					this.$queue.setData('gexing', 2);
				}
			},
			change(status) {
				console.log(status);
				this.$Request.post('/app/user/updateSendMsg', {
					isSendMsg: status ? 1 : 2
				}).then(res => {
					if (res.code == 0) {

					}
				});
			},
			goNav(e) {
				if (e == "/my/detail/goreport" || e == "/my/setting/zhuxiao") {
					if (!uni.getStorageSync('userId')) {
						uni.navigateTo({
							url: '/pages/public/login'
						})
					} else {
						uni.navigateTo({
							url: e
						})
					}


				} else {
					uni.navigateTo({
						url: e
					})
				}

			},
			goOut() {
				uni.showModal({
					title: '提示',
					content: '确定退出登录吗？',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.removeStorageSync('userName')
							uni.removeStorageSync('avatar')
							uni.removeStorageSync('userId')
							uni.removeStorageSync('token')
							uni.removeStorageSync('phone')
							uni.removeStorageSync('zhiFuBaoName')
							uni.removeStorageSync('zhiFuBao')
							uni.removeStorageSync('invitationCode')
							uni.removeStorageSync('unionId')
							uni.removeStorageSync('openId')
							uni.removeStorageSync('isVIP')
							// uni.clearStorageSync()
							uni.showToast({
								title: '退出成功！',
								icon: 'none'
							})
							setTimeout(function() {
								uni.navigateBack()
							}, 1000)
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			}
		}

	}
</script>

<style lang="scss">
	page {
		background: #F3F6F8;
	}

	$primary: #15CAD2;
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;

	.setting-page {
		min-height: 100vh;
	}

	.setting-shell {
		padding: 24rpx 24rpx 120rpx;
	}

	.setting-hero {
		background: #FFFFFF;
		border-radius: 28rpx;
		padding: 28rpx 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.hero-main {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
	}

	.hero-title {
		font-size: 34rpx;
		font-weight: 700;
		color: $text-main;
	}

	.hero-subtitle {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.hero-badge {
		width: 84rpx;
		height: 84rpx;
		border-radius: 24rpx;
		background: #ECF9FA;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.hero-note {
		margin-top: 24rpx;
		padding: 20rpx 22rpx;
		border-radius: 20rpx;
		background: #F7FAFB;
		border: 1rpx solid #EDF3F5;
		display: flex;
		align-items: flex-start;
		gap: 12rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.hero-note-dot {
		width: 10rpx;
		height: 10rpx;
		border-radius: 50%;
		background: $primary;
		margin-top: 12rpx;
		flex-shrink: 0;
	}

	.setting-card {
		margin-top: 18rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.card-title {
		font-size: 28rpx;
		font-weight: 700;
		color: $text-main;
	}

	.setting-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 18rpx;
		padding: 24rpx 0;
		border-bottom: 1rpx solid #EEF2F5;
	}

	.setting-row.is-last {
		padding-bottom: 4rpx;
		border-bottom: 0;
	}

	.row-main {
		flex: 1;
		display: flex;
		align-items: center;
		gap: 18rpx;
		min-width: 0;
	}

	.row-icon {
		width: 68rpx;
		height: 68rpx;
		border-radius: 20rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.help-icon {
		background: #ECF9FA;
	}

	.agreement-icon {
		background: #EDF3FF;
	}

	.privacy-icon {
		background: #F1EEFF;
	}

	.danger-icon {
		background: #FFF1F1;
	}

	.row-content {
		flex: 1;
		min-width: 0;
	}

	.row-title {
		font-size: 28rpx;
		font-weight: 600;
		color: $text-main;
	}

	.row-desc {
		margin-top: 8rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: $text-soft;
	}

	.danger-card .card-title,
	.danger-card .row-title {
		color: #A94848;
	}

	.logout-btn {
		height: 88rpx;
		line-height: 88rpx;
		text-align: center;
		border-radius: 44rpx;
		margin-top: 28rpx;
		background: linear-gradient(135deg, #21C9D1 0%, #0EAFB6 100%);
		box-shadow: 0 12rpx 24rpx rgba(16, 137, 145, 0.16);
		color: #FFFFFF;
		font-size: 28rpx;
		font-weight: 700;
	}
</style>
