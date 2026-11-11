<template>
	<view class="message-page">
		<view class="message-shell">
			

			<view v-if="list.length" class="message-list">
				<view v-for="(item, index) in list" :key="index" class="message-card">
					<view class="message-card__line"></view>
					<view class="message-card__head">
						<view class="message-card__title-wrap">
							<view class="message-card__status" :class="{'is-unread': item.isSee == 0}"></view>
							<view class="message-card__title">{{ item.title }}</view>
						</view>
						<view class="message-card__badge" v-if="item.isSee == 0">未读</view>
					</view>
					<view class="message-card__content">{{ item.content }}</view>
					<view class="message-card__foot">
						<text class="message-card__time">{{ item.createAt }}</text>
					</view>
				</view>
			</view>

			<empty v-if="list.length === 0" content="暂无消息" show="false"></empty>
		</view>
	</view>
</template>

<script>
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	import empty from '@/components/empty';

	export default {
		components: {
			uniLoadMore,
			empty
		},
		computed: {
			unreadCount() {
				return this.list.filter(item => item.isSee == 0).length
			}
		},
		data() {
			return {
				tabFromIndex: 5,
				tabCurrentIndex: 0,
				fromInfo: 5,
				list: [],
				page: 1,
				limit: 10,
				totalPage: 1,
				scrollTop: false,
				tabList: [{
						state: 5,
						text: '用户消息',
						totalElements: 0
					},
					{
						state: 4,
						text: '订单消息',
						totalElements: 0
					}
				]
			};
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		onReachBottom: function() {
			if (this.totalPage > this.page) {
				this.page = this.page + 1;
				this.loadData();
			} else {
				uni.showToast({
					title: '已经到底了!',
					icon: 'none'
				})
			}

		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.loadData();
		},
		onLoad(options) {
			this.$queue.showLoading("加载中...")
			this.loadData();
		},
		methods: {
			goOrder(status = '') {
				uni.setStorageSync('orderStatus', String(status))
				uni.switchTab({
					url: '/pages/order/index'
				})
			},
			goDet(e, title) {
				// console.log(title,'任务通知')
				console.log(e.indexOf('下单'))
				if (e.indexOf('下单') != -1) {
					this.goOrder()
				} else if (title.indexOf('任务通知') != -1) {
					// console.log(title,'任务通知')
					uni.navigateTo({
						url: '/my/renwu/index'
					})
				} else if (title.indexOf('万能任务') != -1) {
					// console.log(title,'任务通知')
					uni.navigateTo({
						url: '/my/renwu/index'
					})
				} else if (e.indexOf('派发') != -1) {
					uni.navigateTo({
						url: '/my/renwu/index'
					})
				} else if (title.indexOf('订单通知') != -1) {
					this.goOrder()
				} else if (title.indexOf('提现成功通知') != -1) {
					uni.navigateTo({
						url: '/my/wallet/cashList'
					})
				}
			},
			//顶部渠道点击
			tabClicks(index) {
				this.list = [];
				this.page = 1;
				this.tabFromIndex = index;
				this.$queue.showLoading("加载中...")
				this.loadData();
			},
			//获取消息列表
			loadData() {
				let that = this;
				let number = 10;
				let token = this.$queue.getData('token');
				if (token) {
					let data = {
						page: this.page,
						limit: this.limit,
					}
					this.$Request.getT('/app/message/getUserMessageInfoList', data).then(res => {
						if (res.code === 0) {
							this.totalPage = res.data.totalPage
							if (this.page == 1) {
								this.list = res.data.records
							} else {
								res.data.list.forEach(d => {
									this.list.push(d);
								});
							}
						}
						uni.hideLoading();
						uni.stopPullDownRefresh();
					});
				}
			}
		}
	};
</script>

<style lang="scss">
	$primary: #15CAD2;
	$primary-dark: #0C98A1;
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;
	$panel-border: rgba(255, 255, 255, 0.76);

	page {
		background:
			linear-gradient(180deg, #7CE5EA 0%, #BFF3F5 22%, #E8FBFC 58%, #F4FEFE 100%),
			repeating-linear-gradient(135deg, rgba(255, 255, 255, 0.14) 0, rgba(255, 255, 255, 0.14) 16rpx, rgba(255, 255, 255, 0) 16rpx, rgba(255, 255, 255, 0) 44rpx);
	}

	.message-page {
		position: relative;
		min-height: 100vh;

		&::before {
			content: '';
			position: fixed;
			left: 0;
			right: 0;
			top: 0;
			height: 520rpx;
			background: linear-gradient(180deg, rgba(12, 152, 161, 0.2) 0%, rgba(12, 152, 161, 0.06) 48%, rgba(12, 152, 161, 0) 100%);
			pointer-events: none;
			z-index: 0;
		}
	}

	.message-shell {
		position: relative;
		z-index: 1;
		padding: 20rpx 24rpx calc(40rpx + env(safe-area-inset-bottom));
	}

	.message-hero {
		position: relative;
		overflow: hidden;
		padding: 30rpx 28rpx;
		border-radius: 32rpx;
		background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(240, 252, 252, 0.9) 100%);
		border: 1rpx solid $panel-border;
		box-shadow: 0 18rpx 40rpx rgba(33, 84, 90, 0.08);
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
		background: linear-gradient(135deg, rgba(21, 202, 210, 0.14) 0%, rgba(21, 202, 210, 0.04) 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.9);
		flex-shrink: 0;
	}

	.hero-summary {
		margin-top: 28rpx;
		padding: 22rpx 24rpx;
		border-radius: 24rpx;
		background: rgba(244, 254, 254, 0.72);
		border: 1rpx solid rgba(21, 202, 210, 0.08);
		display: flex;
		align-items: center;
	}

	.summary-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}

	.summary-num {
		font-size: 36rpx;
		font-weight: 700;
		color: $text-main;

		&.unread {
			color: $primary-dark;
		}
	}

	.summary-label {
		font-size: 22rpx;
		color: $text-soft;
	}

	.summary-divider {
		width: 1rpx;
		height: 52rpx;
		background: rgba(12, 152, 161, 0.12);
		margin: 0 12rpx;
	}

	.message-list {
		margin-top: 24rpx;
		display: flex;
		flex-direction: column;
		gap: 18rpx;
	}

	.message-card {
		position: relative;
		overflow: hidden;
		padding: 28rpx 28rpx 24rpx;
		border-radius: 30rpx;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.97) 0%, rgba(255, 255, 255, 0.92) 100%);
		border: 1rpx solid $panel-border;
		box-shadow: 0 16rpx 36rpx rgba(33, 84, 90, 0.07);
	}

	.message-card__line {
		position: absolute;
		left: 0;
		top: 0;
		bottom: 0;
		width: 8rpx;
		background: linear-gradient(180deg, #2AD5DB 0%, $primary-dark 100%);
	}

	.message-card__head {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 18rpx;
	}

	.message-card__title-wrap {
		flex: 1;
		min-width: 0;
		display: flex;
		align-items: center;
		gap: 12rpx;
	}

	.message-card__status {
		width: 14rpx;
		height: 14rpx;
		border-radius: 50%;
		background: rgba(12, 152, 161, 0.2);
		flex-shrink: 0;

		&.is-unread {
			background: #FF6B6B;
			box-shadow: 0 0 0 8rpx rgba(255, 107, 107, 0.12);
		}
	}

	.message-card__title {
		flex: 1;
		min-width: 0;
		font-size: 32rpx;
		font-weight: 700;
		line-height: 1.4;
		color: $text-main;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.message-card__badge {
		height: 40rpx;
		padding: 0 16rpx;
		border-radius: 999rpx;
		background: rgba(255, 107, 107, 0.12);
		color: #FF6B6B;
		font-size: 22rpx;
		font-weight: 600;
		display: inline-flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.message-card__content {
		margin-top: 18rpx;
		font-size: 26rpx;
		line-height: 1.7;
		color: $text-sub;
	}

	.message-card__foot {
		margin-top: 18rpx;
		display: flex;
		justify-content: flex-end;
	}

	.message-card__time {
		font-size: 22rpx;
		color: $text-soft;
	}
</style>
