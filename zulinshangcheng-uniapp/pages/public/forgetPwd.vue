<template>
	<view class="auth-page">
		<view class="bg-layer">
			<view class="bg-gradient"></view>
			<view class="bg-blob bg-blob-1"></view>
			<view class="bg-blob bg-blob-2"></view>
			<view class="bg-blob bg-blob-3"></view>
			<view class="bg-fade"></view>
		</view>

		<view class="nav-back" @click="navBack">
			<u-icon name="arrow-left" size="32" color="#ffffff"></u-icon>
		</view>

		<view class="page-shell">
			<view class="hero-section">
				<view class="hero-showcase">
					<view class="hero-copy">
						<view class="brand-zone">
							<view class="logo-wrap">
								<image class="logo-img" src="/static/logo.png" mode="aspectFit"></image>
							</view>
							<view class="brand-copy">
								<text class="brand-name">码兄租赁</text>
								<text class="brand-slogan">找回密码 继续使用租赁账号</text>
							</view>
						</view>
						<text class="hero-title">重置密码，继续下单</text>
						<text class="hero-desc">完成手机号验证后即可设置新密码，继续查看订单和租后服务。</text>
						<view class="hero-tags">
							<text class="hero-tag">短信验证</text>
							<text class="hero-tag">快速找回</text>
							<text class="hero-tag">账号安全</text>
						</view>
					</view>
					<view class="hero-device-cluster">
						<view class="device-float device-phone">
							<view class="phone-notch"></view>
							<view class="phone-screen">
								<view class="screen-glow"></view>
								<view class="screen-content">
									<view class="screen-chip"></view>
									<view class="screen-chip screen-chip--soft"></view>
								</view>
								<view class="screen-card"></view>
							</view>
						</view>
						<view class="device-float device-pad">
							<view class="pad-screen"></view>
						</view>
						<view class="device-float device-buds">
							<view class="bud"></view>
							<view class="bud bud-right"></view>
						</view>
					</view>
				</view>
			</view>

			<view class="auth-card">
				<!-- <view class="section-head">
					<view class="section-accent"></view>
					<text class="section-title">找回密码</text>
					<text class="section-desc">验证手机号并设置新密码</text>
				</view> -->

				<view class="form-zone">
					<view class="field-block">
						<text class="field-label">手机号</text>
						<view class="field-row" :class="{'field-focus': phoneFocus}">
							<view class="field-left">
								<u-icon name="phone" size="28" :color="phoneFocus ? '#15CAD2' : '#CBD5E1'"></u-icon>
							</view>
							<input class="field-input" type="number" :value="phone" maxlength="11" data-key="phone"
								placeholder="请输入手机号" placeholder-class="field-placeholder" @input="inputChange"
								@focus="phoneFocus = true" @blur="phoneFocus = false" />
						</view>
					</view>

					<view class="field-block">
						<view class="field-label-row">
							<text class="field-label">验证码</text>
							<text class="field-tip">短信验证</text>
						</view>
						<view class="field-row" :class="{'field-focus': codeFocus}">
							<view class="field-left">
								<u-icon name="lock" size="28" :color="codeFocus ? '#15CAD2' : '#CBD5E1'"></u-icon>
							</view>
							<input class="field-input" type="number" :value="code" maxlength="6" data-key="code"
								placeholder="请输入验证码" placeholder-class="field-placeholder" @input="inputChange"
								@confirm="toLogin" @focus="codeFocus = true" @blur="codeFocus = false" />
							<view class="code-btn" :class="{'code-disabled': sending}" @click="sendMsg">
								<text>{{ sendTime }}</text>
							</view>
						</view>
					</view>

					<view class="field-block">
						<text class="field-label">设置密码</text>
						<view class="field-row" :class="{'field-focus': passwordFocus}">
							<view class="field-left">
								<u-icon name="lock" size="28" :color="passwordFocus ? '#15CAD2' : '#CBD5E1'"></u-icon>
							</view>
							<input class="field-input" type="password" :value="password" maxlength="20" minlength="6"
								data-key="password" placeholder="请设置新密码" placeholder-class="field-placeholder"
								@input="inputChange" @confirm="toLogin" @focus="passwordFocus = true"
								@blur="passwordFocus = false" />
						</view>
					</view>
				</view>

				<view class="helper-bar">
					<text class="helper-link" @click="navTo('/pages/public/loginphone')">返回密码登录</text>
					<text class="helper-link" @click="navTo('/pages/public/login')">验证码登录</text>
				</view>

				<view class="btn-wrap" @click="toLogin">
					<view class="submit-btn" :class="{'btn-loading': logining}">
						<text v-if="!logining">立即找回</text>
						<u-icon v-else name="reload" size="32" color="#ffffff" class="btn-spin"></u-icon>
					</view>
				</view>

				<view class="protocol-bar">
					<view class="checkbox" :class="{'checked': showAgree}" @tap="isShowAgree">
						<u-icon v-if="showAgree" name="checkbox-mark" size="14" color="#fff"></u-icon>
					</view>
					<view class="protocol-text-wrap">
						<text class="protocol-text">已阅读并同意</text>
						<navigator url="/my/setting/xieyi?type=176" open-type="navigate">
							<text class="protocol-link">《隐私政策》</text>
						</navigator>
						<text class="protocol-text">和</text>
						<navigator url="/my/setting/xieyi?type=516" open-type="navigate">
							<text class="protocol-link">《用户服务协议》</text>
						</navigator>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				code: '',
				phone: '',
				password: '',
				sending: false,
				sendTime: '获取验证码',
				count: 60,
				logining: false,
				showAgree: false,
				phoneFocus: false,
				codeFocus: false,
				passwordFocus: false,
			}
		},
		methods: {
			isShowAgree() {
				this.showAgree = !this.showAgree
			},
			sendMsg() {
				const { phone } = this;
				if (!phone) {
					this.$queue.showToast("请输入手机号");
				} else if (phone.length !== 11) {
					this.$queue.showToast("请输入正确的手机号");
				} else {
					this.$queue.showLoading("正在发送验证码...");
					this.$Request.getT("/app/Login/sendMsg/" + phone + "/forget").then(res => {
						if (res.code === 0) {
							this.sending = true;
							this.$queue.showToast('验证码发送成功请注意查收');
							this.countDown();
							uni.hideLoading();
						} else {
							uni.hideLoading();
							uni.showModal({
								showCancel: false,
								title: '短信发送失败',
								content: res.msg ? res.msg : '请一分钟后再获取验证码'
							});
						}
					});
				}
			},
			countDown() {
				const { count } = this;
				if (count === 1) {
					this.count = 60;
					this.sending = false;
					this.sendTime = '获取验证码'
				} else {
					this.count = count - 1;
					this.sending = true;
					this.sendTime = count - 1 + '秒后重新获取';
					setTimeout(this.countDown.bind(this), 1000);
				}
			},
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			navBack() {
				uni.navigateBack();
			},
			navTo(url) {
				uni.navigateTo({ url })
			},
			toLogin() {
				const { phone, password, code } = this;
				if (!phone) {
					this.$queue.showToast("请输入手机号");
				} else if (!password) {
					this.$queue.showToast("请设置密码");
				} else if (password.length < 6) {
					this.$queue.showToast("密码位数必须大于六位");
				} else {
					this.logining = true;
					this.$queue.showLoading("正在修改密码中...");
					this.$Request.post("/app/Login/forgetPwd", {
						pwd: password,
						phone: phone,
						msg: code
					}).then(res => {
						uni.hideLoading();
						if (res.code === 0) {
							uni.navigateTo({ url: '/pages/public/loginphone' });
						} else {
							this.logining = false;
							uni.showModal({
								showCancel: false,
								title: '密码找回失败',
								content: res.msg,
							});
						}
					});
				}
			},
		},
	}
</script>

<style lang="scss" scoped>
	page {
		background: #F0F4F8;
		font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
		-webkit-font-smoothing: antialiased;
	}

	.auth-page {
		min-height: 100vh;
		position: relative;
		overflow: hidden;
	}

	.bg-layer {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 920rpx;
		z-index: 0;
		overflow: hidden;

		.bg-gradient {
			position: absolute;
			inset: 0;
			background: linear-gradient(165deg, #3AEBF2 0%, #1DD9E0 20%, #15CAD2 50%, #0DA8B0 80%, #089199 100%);
			border-radius: 0 0 120rpx 120rpx;
		}

		.bg-blob {
			position: absolute;
			border-radius: 50%;
			filter: blur(80rpx);
			pointer-events: none;
			animation: blobFloat 10s ease-in-out infinite;
		}

		.bg-blob-1 {
			width: 380rpx;
			height: 380rpx;
			background: rgba(255, 255, 255, 0.28);
			top: 8%;
			left: -8%;
			animation-delay: 0s;
		}

		.bg-blob-2 {
			width: 320rpx;
			height: 320rpx;
			background: rgba(255, 255, 255, 0.22);
			top: 2%;
			right: -10%;
			animation-delay: -3s;
		}

		.bg-blob-3 {
			width: 260rpx;
			height: 260rpx;
			background: rgba(255, 255, 255, 0.16);
			bottom: 15%;
			left: 35%;
			animation-delay: -6s;
		}

		.bg-fade {
			position: absolute;
			left: 0;
			right: 0;
			bottom: -2rpx;
			height: 200rpx;
			background: linear-gradient(180deg, rgba(240, 244, 248, 0) 0%, #F0F4F8 100%);
		}
	}

	@keyframes blobFloat {
		0%, 100% { transform: translateY(0) scale(1); }
		50% { transform: translateY(-40rpx) scale(1.08); }
	}

	.nav-back {
		position: absolute;
		top: calc(var(--status-bar-height) + 24rpx);
		left: 32rpx;
		width: 72rpx;
		height: 72rpx;
		background: rgba(255, 255, 255, 0.18);
		backdrop-filter: blur(20rpx);
		-webkit-backdrop-filter: blur(20rpx);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		border: 1rpx solid rgba(255, 255, 255, 0.25);
		z-index: 10;
		box-shadow: 0 12rpx 32rpx rgba(8, 100, 105, 0.12);
		transition: all 0.25s ease;

		&:active {
			transform: scale(0.9);
			background: rgba(255, 255, 255, 0.3);
		}
	}

	.page-shell {
		position: relative;
		z-index: 2;
		padding: calc(var(--status-bar-height) + 110rpx) 32rpx 60rpx;
	}

	.hero-section {
		padding: 20rpx 10rpx 40rpx;
		color: #FFFFFF;
		position: relative;
	}

	.hero-showcase {
		position: relative;
		padding: 40rpx 32rpx 36rpx;
		border-radius: 44rpx;
		background: linear-gradient(135deg, rgba(255, 255, 255, 0.22) 0%, rgba(255, 255, 255, 0.08) 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.2);
		backdrop-filter: blur(24rpx);
		-webkit-backdrop-filter: blur(24rpx);
		box-shadow: 0 32rpx 70rpx rgba(6, 100, 105, 0.18), inset 0 1rpx 0 rgba(255, 255, 255, 0.3);
		overflow: hidden;
	}

	.hero-copy {
		position: relative;
		z-index: 2;
		padding-right: 200rpx;
	}

	.brand-zone {
		display: flex;
		align-items: center;
		position: relative;
		z-index: 1;
	}

	.logo-wrap {
		width: 120rpx;
		height: 120rpx;
		border-radius: 36rpx;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0.1) 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.25);
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		box-shadow: 0 20rpx 50rpx rgba(6, 90, 95, 0.2);
		backdrop-filter: blur(16rpx);
		position: relative;

		&::after {
			content: '';
			position: absolute;
			inset: 6rpx;
			border-radius: 30rpx;
			border: 1rpx solid rgba(255, 255, 255, 0.15);
		}
	}

	.logo-img {
		width: 80rpx;
		height: 80rpx;
		border-radius: 24rpx;
	}

	.brand-copy {
		margin-left: 24rpx;
		min-width: 0;
	}

	.brand-name {
		display: block;
		font-size: 48rpx;
		font-weight: 800;
		line-height: 1.1;
		letter-spacing: 1rpx;
		text-shadow: 0 8rpx 20rpx rgba(6, 85, 90, 0.25);
	}

	.brand-slogan {
		display: block;
		margin-top: 10rpx;
		font-size: 24rpx;
		line-height: 1.5;
		color: rgba(255, 255, 255, 0.95);
		font-weight: 500;
		text-shadow: 0 4rpx 12rpx rgba(6, 85, 90, 0.15);
	}

	.hero-title {
		display: block;
		margin-top: 28rpx;
		font-size: 30rpx;
		font-weight: 700;
		line-height: 1.4;
		text-shadow: 0 4rpx 10rpx rgba(6, 85, 90, 0.15);
	}

	.hero-desc {
		display: block;
		margin-top: 10rpx;
		font-size: 22rpx;
		line-height: 1.5;
		color: rgba(255, 255, 255, 0.88);
		max-width: 340rpx;
	}

	.hero-tags {
		display: flex;
		gap: 14rpx;
		margin-top: 24rpx;
		flex-wrap: wrap;
		position: relative;
		z-index: 1;
	}

	.hero-tag {
		height: 50rpx;
		line-height: 50rpx;
		padding: 0 20rpx;
		font-size: 20rpx;
		color: #FFFFFF;
		background: rgba(8, 130, 136, 0.35);
		border-radius: 25rpx;
		border: 1rpx solid rgba(255, 255, 255, 0.2);
		backdrop-filter: blur(10rpx);
		font-weight: 600;
		box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.15);
	}

	.hero-device-cluster {
		position: absolute;
		top: 50%;
		right: 20rpx;
		transform: translateY(-50%);
		width: 200rpx;
		height: 260rpx;
		z-index: 1;
	}

	.device-float {
		position: absolute;
		animation: deviceBob 4s ease-in-out infinite;
	}

	.device-phone {
		width: 100rpx;
		height: 160rpx;
		background: linear-gradient(180deg, rgba(255,255,255,0.95) 0%, rgba(240,252,253,0.9) 100%);
		border-radius: 18rpx;
		padding: 8rpx;
		box-shadow: 0 20rpx 40rpx rgba(8, 110, 116, 0.2), inset 0 0 0 1rpx rgba(255,255,255,0.6);
		top: 20rpx;
		right: 10rpx;
		animation-delay: 0s;
		z-index: 3;

		.phone-notch {
			position: absolute;
			top: 8rpx;
			left: 50%;
			transform: translateX(-50%);
			width: 30rpx;
			height: 6rpx;
			border-radius: 0 0 6rpx 6rpx;
			background: rgba(49, 73, 86, 0.2);
			z-index: 2;
		}

		.phone-screen {
			width: 100%;
			height: 100%;
			border-radius: 14rpx;
			background: linear-gradient(180deg, #1FE0E5 0%, #15CAD2 60%, #0DA8B0 100%);
			overflow: hidden;
			position: relative;
		}

		.screen-glow {
			position: absolute;
			top: -20rpx;
			right: -20rpx;
			width: 60rpx;
			height: 60rpx;
			border-radius: 50%;
			background: rgba(255,255,255,0.25);
			filter: blur(8rpx);
		}

		.screen-content {
			position: absolute;
			top: 40rpx;
			left: 10rpx;
			display: flex;
			gap: 6rpx;
		}

		.screen-chip {
			width: 18rpx;
			height: 18rpx;
			border-radius: 8rpx;
			background: rgba(255,255,255,0.85);

			&.screen-chip--soft {
				background: rgba(255,255,255,0.45);
			}
		}

		.screen-card {
			position: absolute;
			bottom: 12rpx;
			left: 10rpx;
			right: 10rpx;
			height: 28rpx;
			border-radius: 8rpx;
			background: rgba(255,255,255,0.9);
			box-shadow: 0 4rpx 8rpx rgba(0,0,0,0.05);
		}
	}

	.device-pad {
		width: 80rpx;
		height: 110rpx;
		background: linear-gradient(180deg, rgba(255,255,255,0.85) 0%, rgba(230,245,247,0.8) 100%);
		border-radius: 14rpx;
		padding: 6rpx;
		box-shadow: 0 16rpx 32rpx rgba(8, 110, 116, 0.15);
		top: 10rpx;
		right: 90rpx;
		animation-delay: -1.5s;
		z-index: 2;
		transform: rotate(-12deg);

		.pad-screen {
			width: 100%;
			height: 100%;
			border-radius: 10rpx;
			background: linear-gradient(180deg, #E0F7FA 0%, #B2EBF2 100%);
		}
	}

	.device-buds {
		width: 56rpx;
		height: 50rpx;
		bottom: 20rpx;
		right: 70rpx;
		animation-delay: -3s;
		z-index: 4;

		.bud {
			position: absolute;
			width: 22rpx;
			height: 38rpx;
			border-radius: 12rpx 12rpx 16rpx 16rpx;
			background: linear-gradient(180deg, #FFFFFF 0%, #E2ECF2 100%);
			box-shadow: 0 6rpx 12rpx rgba(90, 111, 126, 0.15);
			left: 0;
			transform: rotate(-10deg);

			&::after {
				content: '';
				position: absolute;
				left: 6rpx;
				bottom: -6rpx;
				width: 10rpx;
				height: 10rpx;
				border-radius: 0 0 6rpx 6rpx;
				background: inherit;
			}

			&.bud-right {
				left: auto;
				right: 0;
				transform: rotate(10deg);
			}
		}
	}

	@keyframes deviceBob {
		0%, 100% { transform: translateY(0); }
		50% { transform: translateY(-12rpx); }
	}

	.auth-card {
		background: #ffffff;
		border-radius: 40rpx;
		padding: 42rpx 32rpx 36rpx;
		box-shadow: 0 30rpx 70rpx rgba(14, 52, 74, 0.12);
		position: relative;
	}

	.section-head {
		display: flex;
		flex-direction: column;
		padding-bottom: 28rpx;
	}

	.section-accent {
		width: 84rpx;
		height: 8rpx;
		border-radius: 999rpx;
		background: linear-gradient(90deg, #15cad2 0%, rgba(21, 202, 210, 0.18) 100%);
		margin-bottom: 20rpx;
	}

	.section-title {
		font-size: 38rpx;
		font-weight: 700;
		color: #182338;
		line-height: 1.2;
	}

	.section-desc {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: #627083;
	}

	.form-zone {
		position: relative;
	}

	.field-block {
		margin-bottom: 26rpx;
	}

	.field-label-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.field-label {
		display: block;
		margin-bottom: 14rpx;
		font-size: 24rpx;
		font-weight: 600;
		color: #182338;
	}

	.field-tip {
		font-size: 22rpx;
		color: #8d9bad;
	}

	.field-row {
		display: flex;
		align-items: center;
		height: 104rpx;
		background: linear-gradient(180deg, #ffffff 0%, #f8fbfd 100%);
		border-radius: 26rpx;
		padding: 0 22rpx;
		border: 2rpx solid #eaf0f5;
		transition: all 0.2s ease;

		&.field-focus {
			border-color: #15cad2;
			box-shadow: 0 0 0 8rpx rgba(21, 202, 210, 0.06);
		}
	}

	.field-left {
		width: 52rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.field-input {
		flex: 1;
		height: 100%;
		margin-left: 10rpx;
		font-size: 28rpx;
		color: #182338;
		caret-color: #15cad2;
	}

	.field-placeholder {
		font-size: 28rpx;
		color: #a8b4c3;
	}

	.code-btn {
		min-width: 152rpx;
		height: 64rpx;
		line-height: 64rpx;
		padding: 0 18rpx;
		border-radius: 22rpx;
		text-align: center;
		font-size: 22rpx;
		font-weight: 600;
		color: #15cad2;
		background: rgba(21, 202, 210, 0.12);
		border: 1rpx solid rgba(21, 202, 210, 0.18);
		flex-shrink: 0;
	}

	.code-disabled {
		color: #a8b4c3;
		background: #f1f5f9;
		border-color: #e2e8f0;
	}

	.helper-bar {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 6rpx;
	}

	.helper-link {
		font-size: 24rpx;
		line-height: 1.6;
		color: #5f7087;
	}

	.protocol-bar {
		display: flex;
		align-items: flex-start;
		margin-top: 28rpx;
	}

	.checkbox {
		width: 32rpx;
		height: 32rpx;
		border-radius: 10rpx;
		border: 2rpx solid #d7e0e8;
		background: #ffffff;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 12rpx;
		margin-top: 2rpx;
		flex-shrink: 0;
	}

	.checkbox.checked {
		background: #15cad2;
		border-color: #15cad2;
	}

	.protocol-text-wrap {
		display: flex;
		flex-wrap: wrap;
		align-items: center;
		gap: 4rpx;
	}

	.protocol-text {
		font-size: 22rpx;
		line-height: 1.6;
		color: #8b97a7;
	}

	.protocol-link {
		font-size: 22rpx;
		line-height: 1.6;
		color: #15cad2;
		font-weight: 600;
	}

	.btn-wrap {
		margin-top: 34rpx;
	}

	.submit-btn {
		height: 100rpx;
		line-height: 100rpx;
		border-radius: 999rpx;
		text-align: center;
		font-size: 32rpx;
		font-weight: 700;
		color: #ffffff;
		background: linear-gradient(135deg, #2fe0e5 0%, #15cad2 46%, #0ba6af 100%);
		box-shadow: 0 22rpx 40rpx rgba(21, 202, 210, 0.24);

		&.btn-loading {
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}

	.btn-spin {
		animation: spin 1s linear infinite;
	}

	@keyframes spin {
		from {
			transform: rotate(0deg);
		}

		to {
			transform: rotate(360deg);
		}
	}
</style>
