<template>
	<view class="footprint-page">
		<u-navbar :back-text="backText" back-icon-color="#1A2233" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>

		<view class="footprint-shell">
			

			<view v-if="goodsList.length" class="footprint-group-list">
				<view class="footprint-group" v-for="(detail,index) in goodsList" :key="index">
					<view class="group-header">
						<view class="group-dot"></view>
						<view class="group-date">{{detail.date}}</view>
					</view>
					<view class="goods-grid">
						<view class="goods-card" v-for="(item,indexs) in detail.footprints" :key="indexs">
							<template v-if="item.goods&&item.goods.coverImg">
								<image class="goods-cover" :src="item.goods.coverImg" mode="aspectFill"></image>
							</template>
							<view class="goods-body">
								<template v-if="item.goods&&item.goods.title">
									<view class="text_hid goods-title">{{item.goods.title || ''}}</view>
								</template>
								<view class="goods-tags" v-if="item.goods&&item.goods.oldDegree">
									<view class="tag">{{item.goods.oldDegree}}</view>
								</view>
								<view class="goods-foot">
									<view class="goods-price" v-if="item.goods&&item.goods.memberRent&&item.goods.dailyRent">
										<text class="price-unit">￥</text>
										<text class="price-value">{{isVip == 1?item.goods.memberRent:item.goods.dailyRent}}</text>
										<text class="price-suffix">/天</text>
									</view>
									<view class="goods-lease" v-if="item.goods&&item.goods.leaseStart">
										{{item.goods.leaseStart}}天起租
									</view>
								</view>
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
				backTextStyle: {
					color: '#333333',
				},
				backText: '浏览记录',
				page: 1,
				limit: 10,
				goodsList: [],
				isVip: uni.getStorageSync('isVip'), // isVip 1是会员 2不是
				commitotal: '',
				shareInfo: {
					title: '',
					path: '',
					imageUrl: ''
				},
				status: 'loadmore',
				loadText: {
					loadmore: '轻轻上拉',
					loading: '努力加载中',
					nomore: '实在没有了'
				},
				countPage: ''
			}
		},
		onLoad(option) {
			this.onShareAppMessage = this.setShareAppMessage(this.shareInfo);
			this.footprintList()
		},
		onShow() {},
		methods: {
			setShareAppMessage(shareInfo) {
				return function() {
					return {
						title: shareInfo.title,
						path: shareInfo.path,
						imageUrl: shareInfo.imageUrl
					};
				};
				console.log('999999999')
			},
			// 获取收藏列表
			footprintList() {
				this.$Request.getT('/app/scFollow/selectMyFootprintList', {
					pageNum: this.page,
					pageSize: this.limit,
				}).then(res => {
					let that = this
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
			this.footprintList()
		},
		onReachBottom() {
			if (this.page < this.countPage) {
				this.page += 1
				this.status = 'loading'
				this.footprintList()
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
		background: linear-gradient(180deg, #D8F1F3 0%, #EBF7F8 26%, #F5FAFB 100%);
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}

	.footprint-page {
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

	.footprint-shell {
		position: relative;
		z-index: 1;
		padding: 18rpx 24rpx calc(32rpx + env(safe-area-inset-bottom));
	}

	.footprint-hero {
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

	.footprint-group-list {
		margin-top: 24rpx;
		display: flex;
		flex-direction: column;
		gap: 18rpx;
	}

	.footprint-group {
		padding: 0 2rpx;
	}

	.group-header {
		display: flex;
		align-items: center;
		gap: 12rpx;
		padding: 6rpx 8rpx 18rpx;
	}

	.group-dot {
		width: 14rpx;
		height: 14rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #2AD5DB 0%, #0C98A1 100%);
		box-shadow: 0 0 0 8rpx rgba(21, 202, 210, 0.08);
	}

	.group-date {
		font-size: 30rpx;
		font-weight: 700;
		color: $text-main;
	}

	.goods-grid {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		gap: 16rpx 0;
	}

	.goods-card {
		background: #FFFFFF;
		width: calc((100% - 16rpx) / 2);
		border-radius: 26rpx;
		overflow: hidden;
		border: 1rpx solid rgba(230, 243, 244, 0.95);
		box-shadow: 0 12rpx 26rpx rgba(33, 84, 90, 0.06);
	}

	.goods-cover {
		width: 100%;
		height: 300rpx;
		display: block;
		background: #F3F7F8;
	}

	.goods-body {
		padding: 20rpx 22rpx 22rpx;
	}

	.tag {
		color: $primary-dark;
		background-color: rgba(21, 202, 210, 0.1);
		border-radius: 32rpx;
		height: 40rpx;
		line-height: 40rpx;
		font-size: 22rpx;
		text-align: center;
		padding: 0 20rpx;
		margin-right: 20rpx;
	}

	.goods-title {
		font-size: 26rpx;
		color: $text-main;
		line-height: 1.5;
		min-height: 78rpx;
	}

	.goods-tags {
		margin-top: 16rpx;
		display: flex;
	}

	.goods-foot {
		margin-top: 18rpx;
		display: flex;
		justify-content: space-between;
		align-items: flex-end;
		gap: 10rpx;
	}

	.goods-price {
		font-size: 22rpx;
		color: #E65054;
		display: flex;
		align-items: flex-end;
		flex-wrap: wrap;
	}

	.price-unit {
		font-size: 22rpx;
		line-height: 1.2;
	}

	.price-value {
		font-size: 30rpx;
		font-weight: 700;
		line-height: 1;
		padding: 0 4rpx;
	}

	.price-suffix {
		font-size: 22rpx;
		line-height: 1.4;
	}

	.goods-lease {
		color: $text-soft;
		font-size: 24rpx;
		flex-shrink: 0;
	}

	.text_hid {
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		text-overflow: ellipsis;
		white-space: normal;
		-webkit-line-clamp: 2;
	}
</style>
