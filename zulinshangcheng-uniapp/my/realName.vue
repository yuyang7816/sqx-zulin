<template>
	<view class="realname-page">
		<u-navbar :back-text="backText" back-icon-color="#1A2233" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>

		<view class="realname-shell">
			<view class="hero-card">
				<view class="hero-decor hero-decor-1"></view>
				<view class="hero-decor hero-decor-2"></view>
				<view class="hero-main">
					<view>
						<view class="hero-title">实名认证</view>
						<view class="hero-subtitle">提交真实身份信息后，可用于下单、收货与相关服务校验</view>
					</view>
					<view class="hero-mark">
						<view class="hero-badge">
							<view class="hero-badge-ring"></view>
							<view class="hero-badge-core">
								<view class="hero-check hero-check-a"></view>
								<view class="hero-check hero-check-b"></view>
							</view>
						</view>
					</view>
				</view>
				<view class="hero-tips">
					<view class="tip-item">
						<view class="tip-dot"></view>
						<text>姓名与证件信息需真实有效</text>
					</view>
					<view class="tip-item">
						<view class="tip-dot"></view>
						<text>收货人姓名需与实名认证姓名一致</text>
					</view>
				</view>
			</view>

			<view class="reject-card" v-if="userInfo.status == 2">
				<view class="reject-title">拒绝原因</view>
				<view class="reject-text">{{userInfo.remark}}</view>
			</view>

			<view class="form-card">
				<view class="field-block field-block--split">
					<view class="field-label">真实姓名</view>
					<input type="text" placeholder="请输入真实姓名" class="input" v-model="userInfo.realName" />
				</view>

				<view class="field-block">
					<view class="field-label">证件号码</view>
					<input type="text" placeholder="请输入证件号码" class="input" v-model="userInfo.idNumber" />
				</view>
			</view>

			<view class="form-card form-card--single">
				<view class="field-block">
					<view class="field-label-row">
						<view class="field-label">收货人姓名</view>
						<view class="field-desc">需与上方实名认证姓名一致</view>
					</view>
					<input type="text" placeholder="请输入姓名" class="input" v-model="userInfo.consignee" />
				</view>
			</view>

			<view class="status-card" v-if="userInfo.status == 1">
				<view class="status-icon">
					<view class="status-icon-core">
						<view class="status-check status-check-a"></view>
						<view class="status-check status-check-b"></view>
					</view>
				</view>
				<view class="status-title">实名认证已完成</view>
				<view class="status-desc">当前信息已通过校验，可直接用于下单与收货场景</view>
			</view>

			<view class="footerBtn" @click="saveUserMsg" v-if="userInfo.status != 1">{{btnStatus == 0?'提交认证':'修改信息'}}</view>
		</view>
	</view>
</template>

<script>
	import empty from '../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				backText: '实名认证',
				backTextStyle: {
					color: '#333333',
				},
				userInfo: {
					realName: '',
					idNumber: '',
					consignee: ''
				},
				btnStatus: 0
			}
		},
		onLoad() {
			this.iStatusBarHeight = uni.getSystemInfoSync().statusBarHeight + 155;
			console.log(this.iStatusBarHeight, "iStatusBarHeight")
			let userId = uni.getStorageSync('userId')
			if (userId) {
				this.getByUserId()
			}
		},
		onShow() {},
		methods: {
			// 提交/修改实名认证信息
			saveUserMsg() {
				console.log(this.userInfo.realName, "this.userInfo.realName")
				if (!this.userInfo.realName) {
					uni.showToast({
						title: '请输入真实姓名',
						icon: 'none'
					})
					return
				}
				if (!this.userInfo.idNumber) {
					uni.showToast({
						title: '请输入证件号码',
						icon: 'none'
					})
					return
				}
				let reg = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
				if (!reg.test(this.userInfo.idNumber)) {
					uni.showToast({
						title: '证件号码格式不正确，请重新输入',
						icon: 'none'
					})
					return
				}
				if (!this.userInfo.consignee) {
					uni.showToast({
						title: '请输入收货人姓名',
						icon: 'none'
					})
					return
				}
				if (this.userInfo.consignee != this.userInfo.realName) {
					uni.showToast({
						title: '收货人姓名需与上方实名认证姓名一致',
						icon: 'none'
					})
					return
				}
				let data = {
					realName: this.userInfo.realName, //真实姓名
					idNumber: this.userInfo.idNumber, //证件号码
					consignee: this.userInfo.consignee, //收货人姓名
				}
				if (this.userInfo.id) {
					data.id = this.userInfo.id
				}

				this.$Request.postT('/app/userCertification/saveUserCertification', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '提交信息',
							icon: 'none'
						})
						this.btnStatus = 1
						setTimeout(() => {
							uni.navigateBack()
						}, 500)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			getByUserId() {
				this.$Request.getT('/app/userCertification/getUserCertificationByUserId', {
					userId: uni.getStorageSync('userId')
				}).then(res => {
					if (res.code == 0) {
						if (res.data) {
							this.userInfo = res.data
							this.btnStatus = 1
						}
					}
				})
			}

		}
	}
</script>

<style lang="scss" scoped>
	$primary: #15CAD2;
	$primary-dark: #0C98A1;
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;
	$panel-border: rgba(255, 255, 255, 0.76);

	page {
		background: linear-gradient(180deg, #D8F1F3 0%, #EBF7F8 26%, #F5FAFB 100%);
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}

	.realname-page {
		position: relative;
		min-height: 100vh;

		&::before {
			content: '';
			position: fixed;
			left: 0;
			right: 0;
			top: 0;
			height: 480rpx;
			background:
				linear-gradient(180deg, rgba(21, 202, 210, 0.18) 0%, rgba(21, 202, 210, 0.06) 44%, rgba(21, 202, 210, 0) 100%),
				radial-gradient(circle at 88% 10%, rgba(255, 255, 255, 0.26) 0%, rgba(255, 255, 255, 0) 22%);
			pointer-events: none;
			z-index: 0;
		}

		&::after {
			content: '';
			position: fixed;
			inset: 0;
			background: linear-gradient(180deg, rgba(255, 255, 255, 0.14) 0%, rgba(255, 255, 255, 0) 22%, rgba(255, 255, 255, 0.2) 100%);
			pointer-events: none;
			z-index: 0;
		}
	}

	.realname-shell {
		position: relative;
		z-index: 1;
		padding: 18rpx 24rpx calc(40rpx + env(safe-area-inset-bottom));
	}

	.hero-card,
	.form-card,
	.reject-card {
		background: linear-gradient(180deg, #FFFFFF 0%, #F8FEFE 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.88);
		box-shadow: 0 16rpx 34rpx rgba(33, 84, 90, 0.08);
		border-radius: 32rpx;
	}

	.hero-card {
		position: relative;
		overflow: hidden;
		padding: 30rpx 28rpx;
	}

	.hero-decor {
		position: absolute;
		border-radius: 50%;
		pointer-events: none;

		&.hero-decor-1 {
			width: 220rpx;
			height: 220rpx;
			top: -100rpx;
			right: -60rpx;
			background: radial-gradient(circle, rgba(21, 202, 210, 0.18) 0%, rgba(21, 202, 210, 0) 72%);
		}

		&.hero-decor-2 {
			width: 160rpx;
			height: 160rpx;
			left: -50rpx;
			bottom: -70rpx;
			background: radial-gradient(circle, rgba(12, 152, 161, 0.14) 0%, rgba(12, 152, 161, 0) 74%);
		}
	}

	.hero-main {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
	}

	.hero-title {
		font-size: 40rpx;
		font-weight: 700;
		color: $text-main;
		line-height: 1.2;
	}

	.hero-subtitle {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.hero-mark {
		width: 84rpx;
		height: 84rpx;
		border-radius: 28rpx;
		background: linear-gradient(135deg, #E7FAFB 0%, #D9F6F7 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.9);
		flex-shrink: 0;
	}

	.hero-badge {
		position: relative;
		width: 44rpx;
		height: 44rpx;
	}

	.hero-badge-ring {
		position: absolute;
		inset: 0;
		border-radius: 50%;
		background: linear-gradient(135deg, rgba(42, 213, 219, 0.18) 0%, rgba(12, 152, 161, 0.08) 100%);
		border: 2rpx solid rgba(21, 202, 210, 0.18);
	}

	.hero-badge-core {
		position: absolute;
		left: 50%;
		top: 50%;
		transform: translate(-50%, -50%);
		width: 22rpx;
		height: 22rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #23D3DA 0%, #0C98A1 100%);
		box-shadow: 0 6rpx 12rpx rgba(21, 202, 210, 0.2);
	}

	.hero-check {
		position: absolute;
		background: #FFFFFF;
		border-radius: 999rpx;

		&.hero-check-a {
			width: 4rpx;
			height: 9rpx;
			left: 7rpx;
			top: 9rpx;
			transform: rotate(-45deg);
		}

		&.hero-check-b {
			width: 4rpx;
			height: 13rpx;
			left: 13rpx;
			top: 6rpx;
			transform: rotate(45deg);
		}
	}

	.hero-tips {
		margin-top: 26rpx;
		padding: 22rpx 24rpx;
		border-radius: 24rpx;
		background: #F5FCFC;
		border: 1rpx solid rgba(21, 202, 210, 0.08);
		display: flex;
		flex-direction: column;
		gap: 16rpx;
	}

	.tip-item {
		display: flex;
		align-items: flex-start;
		gap: 12rpx;
		font-size: 23rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.tip-dot {
		width: 12rpx;
		height: 12rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #2AD5DB 0%, #0C98A1 100%);
		margin-top: 11rpx;
		flex-shrink: 0;
	}

	.reject-card {
		margin-top: 20rpx;
		padding: 24rpx 28rpx;
		background: linear-gradient(180deg, #FFF8F8 0%, #FFF2F2 100%);
		border-color: rgba(255, 214, 214, 0.9);
		box-shadow: 0 12rpx 28rpx rgba(214, 107, 107, 0.08);
	}

	.reject-title {
		font-size: 28rpx;
		font-weight: 700;
		color: #D54848;
	}

	.reject-text {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.7;
		color: #A95F5F;
	}

	.form-card {
		margin-top: 20rpx;
		padding: 10rpx 28rpx;

		&.form-card--single {
			padding-top: 6rpx;
		}
	}

	.field-block {
		padding: 26rpx 0 24rpx;

		&.field-block--split {
			border-bottom: 1rpx solid #EDF4F5;
		}
	}

	.field-label-row {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}

	.field-label {
		font-size: 28rpx;
		font-weight: 700;
		color: $text-main;
	}

	.field-desc {
		font-size: 22rpx;
		color: $text-soft;
		line-height: 1.5;
	}

	.input {
		height: 78rpx;
		line-height: 78rpx;
		font-size: 26rpx;
		color: $text-main;
		margin-top: 18rpx;
		padding: 0 22rpx;
		background: #F6FBFC;
		border-radius: 22rpx;
		border: 1rpx solid #E5F2F4;
	}

	.status-card {
		margin-top: 20rpx;
		padding: 34rpx 30rpx;
		border-radius: 32rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #F8FEFE 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.88);
		box-shadow: 0 16rpx 34rpx rgba(33, 84, 90, 0.08);
		display: flex;
		flex-direction: column;
		align-items: center;
		text-align: center;
	}

	.status-icon {
		width: 88rpx;
		height: 88rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, rgba(42, 213, 219, 0.14) 0%, rgba(12, 152, 161, 0.06) 100%);
		border: 2rpx solid rgba(21, 202, 210, 0.14);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.status-icon-core {
		position: relative;
		width: 42rpx;
		height: 42rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #23D3DA 0%, #0C98A1 100%);
		box-shadow: 0 8rpx 16rpx rgba(21, 202, 210, 0.18);
	}

	.status-check {
		position: absolute;
		background: #FFFFFF;
		border-radius: 999rpx;

		&.status-check-a {
			width: 5rpx;
			height: 13rpx;
			left: 14rpx;
			top: 17rpx;
			transform: rotate(-45deg);
		}

		&.status-check-b {
			width: 5rpx;
			height: 18rpx;
			left: 23rpx;
			top: 11rpx;
			transform: rotate(45deg);
		}
	}

	.status-title {
		margin-top: 20rpx;
		font-size: 30rpx;
		font-weight: 700;
		color: $text-main;
	}

	.status-desc {
		margin-top: 12rpx;
		font-size: 23rpx;
		line-height: 1.7;
		color: $text-sub;
	}

	.footerBtn {
		background: linear-gradient(135deg, #1FD0D7 0%, $primary-dark 100%);
		border-radius: 44rpx;
		height: 88rpx;
		line-height: 88rpx;
		width: calc(100% - 40rpx);
		margin: 36rpx auto 0;
		color: #ffffff;
		text-align: center;
		font-weight: 700;
		font-size: 28rpx;
		box-shadow: 0 12rpx 24rpx rgba(21, 202, 210, 0.18);
	}
</style>
