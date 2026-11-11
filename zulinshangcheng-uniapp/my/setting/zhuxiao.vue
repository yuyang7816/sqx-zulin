<template>
	<view class="cancel-page">
		<view class="cancel-shell">
			<view class="cancel-hero">
				<view class="hero-main">
					<view>
						<view class="hero-title">账号注销</view>
						<view class="hero-subtitle">注销申请提交后，将进入不可逆处理流程，请谨慎确认</view>
					</view>
					<view class="hero-badge">
						<u-icon name="close-circle" size="34" color="#FF6B6B"></u-icon>
					</view>
				</view>
				<view class="hero-note">
					<view class="hero-note-dot"></view>
					<text>如果是误操作，建议立即返回并保留当前账号</text>
				</view>
			</view>

			<view class="cancel-card">
				<view class="card-title">注销提示</view>
				<view class="card-desc">若你是二次申请注销，则注销申请会直接通过</view>
				<view class="warn-title">一旦账号注销成功</view>
				<view class="warn-list">
					<view class="warn-item">
						<view class="warn-dot"></view>
						<view class="warn-text">您的账号将无法登陆与使用</view>
					</view>
					<view class="warn-item">
						<view class="warn-dot"></view>
						<view class="warn-text">您的账号信息将会永久清除且无法恢复</view>
					</view>
					<view class="warn-item">
						<view class="warn-dot"></view>
						<view class="warn-text">您的收益将被永久清空且无法恢复</view>
					</view>
					<view class="warn-item">
						<view class="warn-dot"></view>
						<view class="warn-text">您的账号所关联的订单将无法查询与找回</view>
					</view>
					<view class="warn-item">
						<view class="warn-dot"></view>
						<view class="warn-text">您的租赁商城将无法进行下去</view>
					</view>
					<view class="warn-item">
						<view class="warn-dot"></view>
						<view class="warn-text">您在租赁商城的账号信息将会永久清除且无法恢复</view>
					</view>
					<view class="warn-item warn-item--danger">
						<view class="warn-dot warn-dot--danger"></view>
						<view class="warn-text warn-text--danger">您所绑定的手机号码3个月内将无法再次注册租赁商城</view>
					</view>
				</view>
			</view>

			<view class="action-row">
				<view class="action-btn action-btn--ghost" @tap="send">继续注销</view>
				<view class="action-btn action-btn--primary" @tap="back">暂不注销</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {}
		},
		methods: {
			back() {
				uni.navigateBack();
			},
			send() {
				let that = this;
				let userId = this.$queue.getData('userId');
				let phone = this.$queue.getData('phone') ? this.$queue.getData('phone') : '';
				uni.showModal({
					title: '温馨提示',
					content: '注销后您的账号信息将会永久清除且无法恢复，继续注销将在三日内清除完毕，确认继续注销吗？',
					showCancel: true,
					cancelText: '暂不注销',
					confirmText: '继续注销',
					success: res => {
						if (res.confirm) {
							that.$queue.showLoading('注销中...');
							that.$Request
								.postJson('/app/message/insertMessage', {
									state: 50,
									title: phone ? phone : '注销账号',
									content: '我要注销账号！',
									phone: phone,
									userId: userId
								})
								.then(res => {
									if (res.code === 0) {
										uni.showToast({
											title: '申请成功！'
										});
										that.$queue.logout();
										setTimeout(function() {
											uni.switchTab({
												url: '/pages/index/index'
											})
										}, 1000);
									} else {
										uni.hideLoading();
										uni.showModal({
											showCancel: false,
											title: '注销失败',
											content: res.msg
										});
									}
								});
						}
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F3F6F8;
	}

	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;

	.cancel-page {
		min-height: 100vh;
	}

	.cancel-shell {
		padding: 24rpx;
	}

	.cancel-hero,
	.cancel-card {
		background: #FFFFFF;
		border-radius: 28rpx;
		padding: 28rpx 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.cancel-card {
		margin-top: 18rpx;
	}

	.hero-main {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
	}

	.hero-title,
	.card-title,
	.warn-title {
		color: $text-main;
		font-weight: 700;
	}

	.hero-title {
		font-size: 34rpx;
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
		background: #FFF1F1;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.hero-note {
		margin-top: 24rpx;
		padding: 20rpx 22rpx;
		border-radius: 20rpx;
		background: #FFF8F8;
		border: 1rpx solid #F7DFDF;
		display: flex;
		align-items: flex-start;
		gap: 12rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: #A05C5C;
	}

	.hero-note-dot {
		width: 10rpx;
		height: 10rpx;
		border-radius: 50%;
		background: #FF6B6B;
		margin-top: 12rpx;
		flex-shrink: 0;
	}

	.card-title {
		font-size: 28rpx;
	}

	.card-desc {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.warn-title {
		margin-top: 24rpx;
		font-size: 26rpx;
	}

	.warn-list {
		margin-top: 18rpx;
		padding: 6rpx 0;
	}

	.warn-item {
		display: flex;
		align-items: flex-start;
		gap: 12rpx;
	}

	.warn-item + .warn-item {
		margin-top: 18rpx;
	}

	.warn-dot {
		width: 10rpx;
		height: 10rpx;
		border-radius: 50%;
		background: #AEB8C4;
		margin-top: 12rpx;
		flex-shrink: 0;
	}

	.warn-text {
		font-size: 24rpx;
		line-height: 1.7;
		color: $text-soft;
	}

	.warn-item--danger .warn-text,
	.warn-text--danger {
		color: #D65F5F;
	}

	.warn-dot--danger {
		background: #FF6B6B;
	}

	.action-row {
		display: flex;
		gap: 16rpx;
		margin-top: 28rpx;
	}

	.action-btn {
		flex: 1;
		height: 88rpx;
		line-height: 88rpx;
		text-align: center;
		border-radius: 44rpx;
		font-size: 28rpx;
		font-weight: 700;
	}

	.action-btn--ghost {
		background: #E9EDF1;
		color: #5D6A7B;
	}

	.action-btn--primary {
		background: linear-gradient(135deg, #21C9D1 0%, #0EAFB6 100%);
		box-shadow: 0 12rpx 24rpx rgba(16, 137, 145, 0.16);
		color: #FFFFFF;
	}
</style>
