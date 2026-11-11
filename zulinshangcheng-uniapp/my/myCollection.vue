<template>
	<view class="collection-page">
		<u-navbar :back-text="backText" back-icon-color="#1A2233" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>

		<view class="collection-shell">


			<view v-if="goodsList.length" class="collection-list">
				<view class="collection-card" v-for="(item,index) in goodsList" :key="index">
					<view class="collection-media">
						<image class="collection-cover" :src="item.coverImg" mode="aspectFill"></image>
						<view class="collection-badge">收藏</view>
					</view>
					<view class="collection-body">
						<view class="collection-title text_hid">{{item.title}}</view>
						<view class="collection-meta">
							<view class="collection-tag">
								<text>{{isVip == 1 ? '会员价' : '日租价'}}</text>
							</view>
						</view>
						<view class="collection-foot">
							<view class="collection-price">
								<text class="price-unit">¥</text>
								<text class="price-value">{{isVip == 1?(item.memberRent?item.memberRent:'0'):(item.dailyRent?item.dailyRent:'0')}}</text>
								<text class="price-suffix">/天</text>
							</view>
							<view class="btn-order" @click="navigate('/subpackage/goods/goodsDet?goodId='+item.id)">
								去下单
							</view>
						</view>
					</view>
				</view>
			</view>

			<empty v-if="goodsList.length == 0"></empty>
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
				backText: '我的收藏',
				backTextStyle: {
					color: '#333333',
				},
				page: 1,
				limit: 10,
				goodsList: [],
				isVip: uni.getStorageSync('isVip'), // isVip 1是会员 2不是
				commitotal: '',
				status: 'loadmore',
				loadText: {
					loadmore: '轻轻上拉',
					loading: '努力加载中',
					nomore: '实在没有了'
				},
				countPage: ''
			}
		},
		onLoad() {
			this.iStatusBarHeight = uni.getSystemInfoSync().statusBarHeight + 155;
			console.log(this.iStatusBarHeight, "iStatusBarHeight")
		},
		onShow() {
			this.getFollowList()
		},
		methods: {
			// 获取收藏列表
			getFollowList() {
				this.$Request.postT('/app/scFollow/getFollowList', {
					classify: "1",
					page: this.page,
					limit: this.limit
				}).then(res => {
					if (res.code == 0) {
						this.countPage = res.data.pages
						if (this.page == this.countPage) {
							this.status = 'nomore'
						} else {
							this.status = 'loadmore'
						}
						if (this.page == 1) {
							this.goodsList = res.data.records
						} else {
							this.goodsList = [...this.goodsList, ...res.data.records]
						}
						this.commitotal = res.data.total
					}
				})
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getFollowList()
		},
		onReachBottom() {
			if (this.page < this.countPage) {
				this.page += 1
				this.status = 'loading'
				this.getFollowList()
			} else {
				this.status = 'nomore'
			}
		},
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
		background:
			linear-gradient(180deg, #D8F1F3 0%, #EBF7F8 26%, #F5FAFB 100%);
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}

	.collection-page {
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
			background:
				linear-gradient(180deg, rgba(255, 255, 255, 0.14) 0%, rgba(255, 255, 255, 0) 22%, rgba(255, 255, 255, 0.2) 100%);
			pointer-events: none;
			z-index: 0;
		}
	}

	.collection-shell {
		position: relative;
		z-index: 1;
		padding: 18rpx 24rpx calc(32rpx + env(safe-area-inset-bottom));
	}

	.collection-hero {
		position: relative;
		overflow: hidden;
		padding: 30rpx 28rpx;
		border-radius: 32rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #F8FEFE 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.88);
		box-shadow: 0 16rpx 34rpx rgba(33, 84, 90, 0.08);
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

	.hero-top {
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

	.hero-icon {
		width: 42rpx;
		height: 42rpx;
	}

	.hero-stats {
		margin-top: 28rpx;
		padding: 22rpx 24rpx;
		border-radius: 24rpx;
		background: #F5FCFC;
		border: 1rpx solid rgba(21, 202, 210, 0.08);
		display: flex;
		align-items: center;
	}

	.hero-stat {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}

	.hero-stat-num {
		font-size: 36rpx;
		font-weight: 700;
		color: $text-main;
	}

	.hero-stat-label {
		font-size: 22rpx;
		color: $text-soft;
	}

	.hero-divider {
		width: 1rpx;
		height: 52rpx;
		background: rgba(12, 152, 161, 0.12);
		margin: 0 12rpx;
	}

	.text_hid {
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		text-overflow: ellipsis;
		white-space: normal;
		-webkit-line-clamp: 2;
	}

	.collection-list {
		margin-top: 24rpx;
		display: flex;
		flex-direction: column;
		gap: 16rpx;
	}

	.collection-card {
		display: flex;
		gap: 22rpx;
		padding: 24rpx;
		border-radius: 30rpx;
		background: #FFFFFF;
		border: 1rpx solid rgba(230, 243, 244, 0.95);
		box-shadow: 0 12rpx 26rpx rgba(33, 84, 90, 0.06);
	}

	.collection-media {
		position: relative;
		width: 172rpx;
		height: 172rpx;
		flex-shrink: 0;
	}

	.collection-cover {
		width: 100%;
		height: 100%;
		border-radius: 24rpx;
		display: block;
		background: #F3F7F8;
	}

	.collection-badge {
		position: absolute;
		left: 12rpx;
		top: 12rpx;
		height: 36rpx;
		padding: 0 14rpx;
		border-radius: 999rpx;
		background: rgba(26, 34, 51, 0.6);
		color: #FFFFFF;
		font-size: 20rpx;
		display: inline-flex;
		align-items: center;
		justify-content: center;
	}

	.collection-body {
		flex: 1;
		min-width: 0;
		display: flex;
		flex-direction: column;
	}

	.collection-title {
		font-size: 30rpx;
		font-weight: 700;
		line-height: 1.45;
		color: $text-main;
	}

	.collection-meta {
		margin-top: 14rpx;
	}

	.collection-tag {
		display: inline-flex;
		align-items: center;
		height: 40rpx;
		padding: 0 16rpx;
		border-radius: 999rpx;
		background: rgba(21, 202, 210, 0.1);
		color: $primary-dark;
		font-size: 22rpx;
		font-weight: 500;
	}

	.collection-foot {
		margin-top: auto;
		padding-top: 20rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.collection-price {
		color: #E65054;
		font-weight: 700;
		display: flex;
		align-items: flex-end;
		flex-wrap: wrap;
	}

	.price-unit {
		font-size: 24rpx;
		line-height: 1.2;
	}

	.price-value {
		font-size: 40rpx;
		line-height: 1;
		padding: 0 6rpx;
	}

	.price-suffix {
		font-size: 22rpx;
		line-height: 1.4;
		color: $text-sub;
	}

	.btn-order {
		height: 70rpx;
		line-height: 70rpx;
		text-align: center;
		width: 180rpx;
		font-size: 26rpx;
		font-weight: 600;
		color: #FFFFFF;
		background: linear-gradient(135deg, #1FD0D7 0%, $primary-dark 100%);
		border-radius: 35rpx;
		box-shadow: 0 8rpx 18rpx rgba(21, 202, 210, 0.16);
	}
</style>
