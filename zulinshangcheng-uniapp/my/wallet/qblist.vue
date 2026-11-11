<template>
	<view class="wallet-list-page">
		<view class="wallet-list-shell">
			<view v-for="(item, index) in list" :key="index" class="record-card">
				<view class="record-head">
					<view>
						<view class="record-time">{{item.createTime}}</view>
						<view class="record-content">{{item.content}}</view>
					</view>
					<text class="record-amount">￥{{item.money}}</text>
				</view>
			</view>
			<empty v-if="list.length === 0" show="false" :height="emptyH"></empty>
		</view>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				list: [],
				page: 1,
				limit: 10,
				emptyH: '94.5vh',
				countPage: ''
			}
		},
		onLoad: function(e) {
			this.$queue.showLoading("加载中...");
			this.getMoney();
		},
		methods: {
			text(val) {
				if (val == 0) {
					return '充值余额'
				} else if (val == 1) {
					return '支付订单'
				} else if (val == 3) {
					return '邀请好友'
				} else if (val == 4) {
					return '退款'
				}
			},
			getMoney() {
				let that = this;
				let token = that.$queue.getData("token");
				let userId = that.$queue.getData("userId");
				if (token) {
					//可以提现金额查询预估收入查询
					let data = {
						page: that.page,
						limit: that.limit,
						userId: uni.getStorageSync('userId')
					}
					that.$Request.getT("/app/cash/queryUserMoneyDetails", data).then(res => {
						if (res.code === 0 && res.data) {
							if (this.page == 1) {
								that.list = res.data.records;
							} else {
								that.list = [...that.list, ...res.data.records]
							}
							this.countPage = res.data.pages
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();
					});
				}

			},
		},
		onReachBottom: function() {
			if (this.page < this.countPage) {
				this.page = this.page + 1;
				this.getMoney();
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getMoney();
		}

	}
</script>

<style lang='scss'>
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;

	page {
		background: #F3F6F8;
	}

	.wallet-list-shell {
		padding: 24rpx;
	}

	.record-card {
		background: #FFFFFF;
		padding: 24rpx;
		margin-bottom: 16rpx;
		border-radius: 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.record-head {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 16rpx;
	}

	.record-time {
		font-size: 22rpx;
		color: $text-soft;
	}

	.record-content {
		margin-top: 10rpx;
		font-size: 26rpx;
		line-height: 1.6;
		color: $text-main;
	}

	.record-amount {
		font-size: 32rpx;
		font-weight: 700;
		color: #0EAFB6;
		flex-shrink: 0;
	}
</style>
