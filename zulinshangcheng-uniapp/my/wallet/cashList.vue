<template>
	<view class="wallet-list-page">
		<view class="wallet-list-shell">
			<view v-for="(item, index) in list" :key="index" class="record-card">
				<view class="record-head">
					<view class="record-status" :class="{'is-fail': item.state==-1}">{{ item.state==1 ? '提现成功' : item.state==0 ? '提现中' : '提现失败' }}</view>
					<text class="record-amount">￥{{item.money}}</text>
				</view>
				<view class="record-body" v-if="item.classify==1">
					<view class="record-line">支付宝账号：{{item.zhifubao}}</view>
					<view class="record-line">支付宝姓名：{{item.zhifubaoName}}</view>
					<view class="record-line">提现时间：{{item.createAt}}</view>
					<view class="record-line" v-if="item.state==1">成功时间：{{item.outAt}}</view>
					<view class="record-line record-line--fail" v-if="item.state==-1">{{item.refund}}</view>
				</view>
				<view class="record-body" v-if="item.classify==2">
					<view class="record-line">提现时间：{{item.createAt}}</view>
					<view class="record-line" v-if="item.state==1">成功时间：{{item.outAt}}</view>
					<view class="record-line record-line--fail" v-if="item.state==-1">{{item.refund}}</view>
				</view>
			</view>
			<empty v-if="list.length === 0" content="暂无提现记录" show="false" :height="emptyH"></empty>
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
				emptyH:'94.5vh'
			}
		},
		onLoad: function(e) {
			this.$queue.showLoading("加载中...");
			this.getMoney();
		},
		methods: {
			getMoney() {
				let that = this;
				let token = that.$queue.getData("token");
				let userId = that.$queue.getData("userId");
				if (token) {
					//可以提现金额查询预估收入查询
					let data = {
						page: that.page,
						limit: that.limit
					}
					that.$Request.getT("/app/cash/selectPayDetails", data).then(res => {
						if (res.code === 0 && res.data) {
							if (this.page == 1) {
								that.list = res.data.list;
							} else {
								that.list = [...that.list, ...res.data.list]
							}
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();
					});
				}

			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getMoney();
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
		padding: 26rpx 24rpx;
		margin-bottom: 16rpx;
		border-radius: 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.record-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.record-status {
		font-size: 28rpx;
		font-weight: 700;
		color: #0EAFB6;

		&.is-fail {
			color: #D54848;
		}
	}

	.record-amount {
		font-size: 34rpx;
		font-weight: 700;
		color: #D54848;
	}

	.record-body {
		margin-top: 18rpx;
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}

	.record-line {
		font-size: 24rpx;
		line-height: 1.6;
		color: $text-sub;

		&.record-line--fail {
			color: #D54848;
		}
	}
</style>
