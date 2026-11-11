<template>
	<view class="order_bar flex alic bf">
		<view class="order_image mr18">
			<view style="width:136rpx ;height: 136rpx;" v-if="order.coverImg&&order.coverImg!=null">
				<image :src="order.coverImg" mode="" style="border-radius: 12rpx;width: 100%;height: 100%;">
				</image>
			</view>
		</view>
		<view class="order_info flex-c" style="width: calc(100% - 150rpx);">
			<view class="order_price f32 bold"><text
					style="font-weight: 500;font-size: 28rpx;">￥</text>{{isVip == 1 ? order.memberRent : order.dailyRent}} <text
					style="font-weight: 500;font-size: 28rpx;"> /天</text></view>
			<view class="tag">到期买断价：￥{{isVip == 1 ? order.memberPrice : order.price}}</view>
			<view class="order_good_name f30 c333 bold ellipsis" style="width:100%;">{{order.title}}</view>
		</view>
	</view>
</template>


<script>
	export default {
		props: {
			order: {
				type: Object,
				required: true
			}
		},
		data() {
			return {
				isVip: uni.getStorageSync('isVip'), // isVip 1是会员 2不是
			}
		},
		mounted() {
			let that = this;
			console.log(that.order, "order")
		},
		methods: {
			childShowVideo(event, detail) {
				// 原生方法阻止事件冒泡
				event.stopPropagation();
				detail.videoShow = true
			},
			async getHeight() {
				const {
					height
				} = await getRect(this, '.order_bar')
				return height
			}
		}
	}
</script>


<style lang="scss" scoped>
	.ellipsis {
		text-overflow: ellipsis;
		white-space: nowrap;
		overflow: hidden;
		width: 100%;
	}

	.tag {
		background-color: #FDECF0;
		color: #E72C2F;
		width: 45%;
		text-align: center;
		font-size: 24rpx;
		border-radius: 4rpx;
		padding: 5rpx 0;
		margin: 10rpx 0;
	}

	.order_good_name {
		color: #333333;
		font-size: 30rpx;
		font-weight: bold;
	}

	.order {
		&_bar {
			width: 100%;
			box-sizing: border-box;
			// padding: 32rpx;
		}

		&_image {
			width: 136rpx;
			height: 136rpx;
			border-radius: 24rpx;
			overflow: hidden;
		}

		&_info {
			margin-left: 15rpx;
		}

		&_price {
			color: #FF1B1B;
			font-size: 38rpx;
			font-weight: bold;
		}
	}
</style>