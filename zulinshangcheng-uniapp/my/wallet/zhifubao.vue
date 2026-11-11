<template>
	<view class="wallet-form-page">
		<view class="wallet-form-shell">
			<view class="form-hero">
				<view>
					<view class="form-title">提现账号</view>
					<view class="form-subtitle">绑定支付宝账号后，可用于钱包提现与收款审核</view>
				</view>
				<view class="form-badge">
					<u-icon name="account" size="36" color="#EAFBFC"></u-icon>
				</view>
			</view>

			<view class="form-card">
				<view class="field-block field-block--split">
					<view class="field-title">收款人姓名</view>
					<view class="field-desc">请填写支付宝实名认证姓名</view>
					<input class="field-input" type="text" placeholder="请输入支付宝收款人姓名" v-model="zhiFuBaoName" />
				</view>
				<view class="field-block">
					<view class="field-title">支付宝账号</view>
					<view class="field-desc">请输入要绑定的支付宝手机号</view>
					<input class="field-input" type="number" placeholder="请输入要绑定的支付宝手机号" v-model="zhiFuBao" />
				</view>
			</view>

			<view class="action-card">
				<view class="action-head">
					<view class="action-title">确认提交</view>
					<view class="action-subtitle">提交前请再次核对账户信息，避免影响提现处理</view>
				</view>
				<wButton text="绑定账号" :rotate="logining" @click.native="toLogin()"></wButton>
				<view class="form-tip">提示：请正确填写收款人的支付宝账号和真实姓名，否则将无法正常收款</view>
			</view>
		</view>
	</view>
</template>

<script>
	import wButton from '@/components/watch-login/watch-button.vue'; //button
	export default {
		components: {
			wButton
		},
		data() {
			return {
				zhiFuBao: '',
				zhiFuBaoName: '',
				logining: false
			}
		},
		onLoad() {
			let userId = this.$queue.getData("userId");
			if (userId) {
				let data = {
					userId: userId
				}
				this.$Request.getT("/app/user/selectUserByUserId", data).then(res => {
					if (res.code == 0) {
						if (res.data.zhiFuBao) {
							this.zhiFuBao = res.data.zhiFuBao;
						}
						if (res.data.zhiFuBaoName) {
							this.zhiFuBaoName = res.data.zhiFuBaoName;
						}
					}
				});
			}

		},
		methods: {
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			navBack() {
				uni.navigateBack();
			},

			toLogin() {
				let userId = this.$queue.getData("userId");
				let token = uni.getStorageSync("token");
				const {
					zhiFuBao,
					zhiFuBaoName
				} = this;
				if (!zhiFuBao) {
					this.$queue.showToast("请设置收款人支付宝账号");
				} else if (!zhiFuBaoName) {
					this.$queue.showToast("请设置收款人姓名");
				} else {
					this.$queue.showLoading("请稍后...");
					let data = {
						zhiFuBao: this.zhiFuBao,
						zhiFuBaoName: this.zhiFuBaoName
					}
					this.$Request.postJson('/app/user/updateUser', data).then(res => {
						if (res.code === 0) {
							uni.setStorageSync('zhiFuBao', zhiFuBao)
							uni.setStorageSync('zhiFuBaoName', zhiFuBaoName)
							uni.showToast({
								title: '添加成功',
								icon: 'none',
								complete() {
									setTimeout(function() {
										uni.navigateBack();
									}, 1000)
								}
							})
						} else {
							this.$queue.showToast(res.msg)
						}
						uni.hideLoading();
					});
				}
			},
		},

	}
</script>

<style lang='scss'>
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;

	page {
		background: linear-gradient(180deg, #E7F4F5 0%, #F3F6F8 26%, #F5F7F9 100%);
	}

	::v-deep.dlbutton {
		border-radius: 50rpx !important;
		background: linear-gradient(135deg, #21C9D1 0%, #0EAFB6 100%) !important;
		box-shadow: 0 12rpx 24rpx rgba(16, 137, 145, 0.16) !important;
	}

	.wallet-form-page {
		min-height: 100vh;

		&::before {
			content: '';
			position: fixed;
			left: 0;
			right: 0;
			top: 0;
			height: 300rpx;
			background: linear-gradient(180deg, rgba(33, 201, 209, 0.16) 0%, rgba(33, 201, 209, 0.05) 46%, rgba(33, 201, 209, 0) 100%);
			pointer-events: none;
		}
	}

	.wallet-form-shell {
		padding: 24rpx;
	}

	.form-hero {
		padding: 24rpx 24rpx;
		border-radius: 28rpx;
		background: linear-gradient(135deg, #1FC7CF 0%, #11AEB5 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.18);
		box-shadow: 0 16rpx 30rpx rgba(17, 145, 152, 0.16);
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
		position: relative;
		overflow: hidden;

		&::after {
			content: '';
			position: absolute;
			right: -40rpx;
			top: -40rpx;
			width: 180rpx;
			height: 180rpx;
			border-radius: 50%;
			background: radial-gradient(circle, rgba(255, 255, 255, 0.22) 0%, rgba(255, 255, 255, 0) 72%);
			pointer-events: none;
		}
	}

	.form-title {
		font-size: 32rpx;
		font-weight: 700;
		color: #FFFFFF;
	}

	.form-subtitle {
		margin-top: 10rpx;
		font-size: 23rpx;
		line-height: 1.6;
		color: rgba(255, 255, 255, 0.86);
		max-width: 460rpx;
	}

	.form-badge {
		width: 76rpx;
		height: 76rpx;
		border-radius: 24rpx;
		background: rgba(255, 255, 255, 0.14);
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.22);
	}

	.form-card {
		margin-top: 16rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #FCFDFE 100%);
		border-radius: 24rpx;
		padding: 4rpx 24rpx;
		border: 1rpx solid #E3ECEF;
		box-shadow: 0 10rpx 24rpx rgba(31, 44, 61, 0.05);
		position: relative;
		overflow: hidden;

		&::before {
			content: '';
			position: absolute;
			right: -24rpx;
			top: -20rpx;
			width: 180rpx;
			height: 180rpx;
			border-radius: 50%;
			background: radial-gradient(circle, rgba(33, 201, 209, 0.12) 0%, rgba(33, 201, 209, 0.05) 34%, rgba(33, 201, 209, 0) 72%);
			pointer-events: none;
		}

		&::after {
			content: '';
			position: absolute;
			left: -36rpx;
			bottom: -42rpx;
			width: 150rpx;
			height: 150rpx;
			border-radius: 50%;
			background: radial-gradient(circle, rgba(33, 201, 209, 0.08) 0%, rgba(33, 201, 209, 0.03) 34%, rgba(33, 201, 209, 0) 74%);
			pointer-events: none;
		}
	}

	.action-card {
		margin-top: 20rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 24rpx 24rpx 22rpx;
		border: 1rpx solid #E3ECEF;
		box-shadow: 0 10rpx 24rpx rgba(31, 44, 61, 0.05);
	}

	.action-head {
		margin-bottom: 8rpx;
	}

	.action-title {
		font-size: 28rpx;
		font-weight: 700;
		color: $text-main;
	}

	.action-subtitle {
		margin-top: 10rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: #7F909D;
	}

	.field-block {
		padding: 26rpx 0 24rpx;
		position: relative;
		z-index: 1;

		&.field-block--split {
			border-bottom: 1rpx solid #EEF3F5;
		}
	}

	.field-title {
		font-size: 28rpx;
		font-weight: 700;
		color: $text-main;
		letter-spacing: 0;
	}

	.field-desc {
		margin-top: 10rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: #7F909D;
	}

	.field-input {
		height: 72rpx;
		line-height: 72rpx;
		background: linear-gradient(180deg, #F7FAFB 0%, #F3F8FA 100%);
		border: 1rpx solid #DDE8EC;
		border-radius: 16rpx;
		padding: 0 18rpx;
		font-size: 28rpx;
		color: $text-main;
		margin-top: 16rpx;
	}

	.form-tip {
		padding: 18rpx 16rpx 0;
		font-size: 22rpx;
		line-height: 1.7;
		color: #6D7E8B;
		position: relative;

		&::before {
			content: '';
			display: block;
			width: 56rpx;
			height: 4rpx;
			border-radius: 999rpx;
			background: rgba(21, 202, 210, 0.24);
			margin-bottom: 14rpx;
		}
	}

	::v-deep .dlbutton {
		margin-top: 24rpx !important;
		letter-spacing: 0 !important;
	}
</style>
