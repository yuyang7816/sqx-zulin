<template>
	<view class="wallet-list-page">
		<view class="wallet-list-shell">
			<view v-for="(item, index) in list" :key="index" class="record-card">
				<view class="record-head">
					<view class="record-type">{{ item.payWay==0 ? '零钱补缴' : item.payWay==1 ? '微信补缴' : '支付宝补缴' }}</view>
					<text class="record-amount">-{{item.payMoney}}元</text>
				</view>
				<view class="record-copy" @tap="copyOrderNumber(item.orderNo)">订单编号：{{item.orderNo}} <u-icon name="zhuanfa" size="22"></u-icon></view>
				<view class="record-time">创建时间：{{item.createTime}}</view>
			</view>
			<empty v-if="list.length == 0" content="暂无明细"></empty>
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
				tabIndex: 1,
				checkReZhiShu: '否',
				checkReTuanZhang: '否',
				checkReFeiZhiShu: '否',
				scrollTop: false,
				contentText: {
					contentdown: '上拉显示更多',
					contentrefresh: '正在加载...',
					contentnomore: '没有更多数据了'
				}
			}
		},
		onLoad() {
			this.$queue.showLoading("加载中...");
			this.getList();
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
			copyOrderNumber(orderNum) {
				uni.setClipboardData({
					data: orderNum,
					success: r => {
						this.$queue.showToast('复制成功')
					}
				});
			},
			getList() {
				let userId = this.$queue.getData('userId');
				let data = {
					page: this.page,
					limit: this.limit,
				}
				this.$Request.getT('/app/repairRecord/getRecordList',data).then(
					res => {
						if (res.code === 0) {
							if (this.page === 1) {
								this.list = res.data.records;
							} else {
								this.list = [...this.list, ...res.data.records];
							}
						}
						uni.stopPullDownRefresh();
						uni.hideLoading();
					});
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getList();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getList();
		}
	}
</script>

<style lang="less">
	@textMain: #1A2233;
	@textSub: #627083;
	@textSoft: #8E9BAB;
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
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.record-type {
		font-size: 28rpx;
		font-weight: 700;
		color: @textMain;
	}

	.record-amount {
		font-size: 32rpx;
		font-weight: 700;
		color: #D54848;
	}

	.record-copy {
		margin-top: 14rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: @textSub;
	}

	.record-time {
		margin-top: 10rpx;
		font-size: 22rpx;
		color: @textSoft;
	}
</style>
