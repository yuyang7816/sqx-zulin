<template>
	<view>
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<view class="container">
			<view class="" v-for="(item,index) in evaluateList" :key="index">
				<view class="container_" style="margin-bottom: 30rpx;">
					<image :src="item.userHeader" mode="" class="imagepl">
					</image>
					<view class="">
						<view class="" style="color: #666666;font-size: 28rpx;">{{item.userName}}</view>
						<view class="label">{{item.content}}</view>
					</view>
					<view class="" style="margin-left: auto;color: #666666;font-size: 28rpx;">{{item.createTime.substring(0, 16)}}
					</view>
				</view>
				<view class="container_" style="margin-left: 90rpx;" v-if="item.reply">
					<image src="/static/logo.png" mode="" class="imagepl">
					</image>
					<view class="">
						<view class="" style="color: #666666;font-size: 28rpx;">商家回复</view>
						<view class="label">{{item.reply}}</view>
					</view>
				</view>
			</view>
		</view>
		<view style="height: 130rpx;"></view>
		<empty v-if="evaluateList.length == 0"></empty>
		<u-loadmore v-if="evaluateList.length>0" :status="status" :load-text="loadText" />
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				backText: '全部评论',
				backTextStyle: {
					color: '#333333',
				},
				evaluateList: [], //评论
				page: 1,
				limit: 10,
				goodId: '',
				status: 'loadmore',
				loadText: {
					loadmore: '轻轻上拉',
					loading: '努力加载中',
					nomore: '实在没有了'
				},
				commitotal: '',
				countPage: ''
			}
		},

		onLoad(option) {
			this.goodId = option.goodId
			this.getEvaluateList()
		},
		onPullDownRefresh() {
			this.page = 1
			this.getMyOrderList()
		},
		onReachBottom() {
			if (this.page < this.countPage) {
				this.page += 1
				this.status = 'loading'
				this.getMyOrderList()
			} else {
				this.status = 'nomore'
			}
		},
		onShow() {},
		methods: {
			// 获取商品评价
			getEvaluateList() {
				let data = {
					page: 1,
					limit: 999,
					goodId: this.goodId
				}
				this.$Request.getT('/app/orders/getEvaluateList', data).then(res => {
					if (res.status == 0) {
						this.countPage = res.data.pages
						if (this.page == this.countPage) {
							this.status = 'nomore'
						} else {
							this.status = 'loadmore'
						}
						console.log(this.status, "this.status")
						if (this.page == 1) {
							this.evaluateList = res.data.records
						} else {
							this.evaluateList = [...this.evaluateList, ...res.data.records]
						}
						this.commitotal = res.data.total
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #FFFFFF;
	}

	.more {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-right: 30rpx;
	}

	.imagepl {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		margin-right: 20rpx;
	}

	.label {
		font-size: 26rpx;
		color: #000;
		padding-top: 13rpx;
	}

	.container_ {
		width: calc(100% - 60rpx);
		margin: auto;
		display: flex;
		margin-bottom: 50rpx;
	}

	.pingj {
		font-size: 28rpx;
		font-weight: 700;
		padding-bottom: 28rpx;
		padding: 30rpx 0 30rpx 30rpx;
	}

	.qdbtn {
		width: 90%;
		background-color: #27CDCB;
		color: #FFFFFF;
		height: 88rpx;
		line-height: 88rpx;
		border-radius: 44rpx;
		text-align: center;
		margin-top: 30rpx;
	}

	.text_hid {
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		text-overflow: ellipsis;
		white-space: normal;
		-webkit-line-clamp: 2;
	}

	::v-deep .u-indicator-item-round-active {
		background-color: #15CAD2;
	}

	.container {
		height: auto;
		width: 100%;
		margin: auto;
		margin-top: 30rpx;
	}

	.wrap {
		margin-top: 16rpx;
	}

	.goodsCs {
		width: calc(100% - 60rpx);
		margin: auto;
		border-radius: 24rpx;
		margin-top: 22rpx;
		background-color: #FFFFFF;
		padding: 40rpx 30rpx 0rpx 30rpx;
	}

	.zhfa {
		font-size: 24rpx;
		color: #666666;
		display: flex;
		padding: 16rpx 0rpx 28rpx 0rpx;
		justify-content: space-between;
		align-items: center;
		flex-wrap: wrap;
		line-height: 40rpx;
	}
</style>