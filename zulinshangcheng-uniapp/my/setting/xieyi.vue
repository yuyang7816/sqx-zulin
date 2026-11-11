<template>
	<view class="article-page">
		<view class="article-shell">
			<view class="article-hero">
				<view class="hero-main">
					<view>
						<view class="hero-title">{{pageTitle}}</view>
						<view class="hero-subtitle">请仔细阅读相关条款与规则，了解平台服务边界及信息处理说明</view>
					</view>
					<view class="hero-badge">
						<u-icon :name="heroIcon" size="34" :color="heroColor"></u-icon>
					</view>
				</view>
			</view>

			<view class="article-card">
				<view class="card-title">正文内容</view>
				<view class="article-body" v-html="content"></view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				content: '',
				type: '',
				pageTitle: '协议说明'
			}
		},
		computed: {
			heroIcon() {
				return this.type === '176' ? 'info-circle' : 'file-text'
			},
			heroColor() {
				return this.type === '176' ? '#7A63FF' : '#4F7CFF'
			}
		},
		onLoad(option) {
			if (option.type) {
				this.type = option.type
			}
			switch (this.type) {
				case '520':
					this.pageTitle = '租赁服务协议'
					uni.setNavigationBarTitle({ title: '租赁服务协议' })
					break;
				case '516':
					this.pageTitle = '用户协议'
					uni.setNavigationBarTitle({ title: '用户协议' })
					break;
				case '176':
					this.pageTitle = '隐私政策'
					uni.setNavigationBarTitle({ title: '隐私政策' })
					break;
				case '177':
					this.pageTitle = '注册协议'
					uni.setNavigationBarTitle({ title: '注册协议' })
					break;
			}
			this.getGuize();
		},
		methods: {
			getGuize() {
				this.$Request.getT('/app/common/type/' + this.type).then(res => {
					if (res.code === 0) {
						this.content = res.data.value;
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

	$primary: #15CAD2;
	$text-main: #1A2233;
	$text-sub: #627083;

	.article-page {
		min-height: 100vh;
	}

	.article-shell {
		padding: 24rpx;
	}

	.article-hero,
	.article-card {
		background: #FFFFFF;
		border-radius: 28rpx;
		padding: 28rpx 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.article-card {
		margin-top: 18rpx;
	}

	.hero-main {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
	}

	.hero-title,
	.card-title {
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
		background: #F4F7FB;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.card-title {
		font-size: 28rpx;
	}

	.article-body {
		margin-top: 20rpx;
		font-size: 26rpx;
		line-height: 1.8;
		color: #445266;
		word-break: break-all;
	}

	.article-body img {
		max-width: 100%;
		height: auto;
	}

	.article-body table {
		width: 100% !important;
		border-collapse: collapse;
	}

	.article-body p,
	.article-body div,
	.article-body li {
		line-height: 1.8;
		color: #445266;
	}
</style>
