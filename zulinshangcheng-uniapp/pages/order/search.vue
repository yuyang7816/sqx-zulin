<template>
	<view>
		<!-- #ifndef H5 -->
		<view class="" style="height: 100rpx;"></view>
		<!-- #endif -->
		<view v-if="selectOption.list.length > 0" class="container" style="padding-bottom: 20rpx;">
			<view class="" style="border-radius: 24rpx;overflow: hidden;">
				<view style="background-color: #FFFFFF;display: flex;padding: 20upx;padding-bottom: 0;">
					<!-- <view style="display: flex;width: 80%;align-items: center;margin-left: 20upx;">
						<view style="font-size: 30upx;color: #333333;font-weight: bold;">手机号</view>
						<view style="margin-left: 10upx;font-size: 30upx;color: #333333;font-weight: bold;">
							{{selectOption.courierPhone}}
						</view>
					</view>
					<view style="color: #999999;font-size: 24upx;margin-left: 10upx;font-weight: bold;"
						v-if="selectOption.courierPhone" @tap="copyOrder(selectOption.courierPhone)">
						复制</view> -->
					<view class="" style="font-size: 30upx;color: #333333;font-weight: bold;padding-left: 15rpx;">
						{{deliverystatus(selectOption.deliverystatus)}}
					</view>
				</view>
				<view style="background-color: #FFFFFF;display: flex;padding: 20upx;align-items: center;">
					<view style="display: flex;width: 80%;align-items: center;margin-left: 10upx;">
						<image :src="selectOption.logo" mode="" style="width: 50rpx;height: 50rpx;border-radius: 50%;"></image>
						<view style="margin-left: 10upx;font-size: 30upx;color: #333333;font-weight: bold;">
							{{selectOption.expName}} {{selectOption.number}}
						</view>
					</view>
					<view style="color: #999999;font-size: 24upx;margin-left: 20upx;font-weight: bold;" v-if="selectOption.number"
						@tap="copyOrder(selectOption.number)">
						复制</view>
				</view>
			</view>
			<view
				style="padding: 20upx;align-items: center;margin-top: 20upx;background-color: #FFFFFF;border-radius: 24rpx;overflow: hidden;">
				<view v-for="(item,index) in selectOption.list">
					<view style="display: flex;">
						<view style="margin-top: 5upx;">
							<view style="background: #DFDFDF;width: 30upx;height: 30upx;border-radius: 100upx;"></view>
							<!-- <image src="/static/logo.png" mode="" style="width: 30upx;height: 30upx;border-radius: 50%;"></image> -->
							<view style="height: 180upx;background: #DFDFDF;width: 1upx;margin-left: 15upx;"
								v-if="index != selectOption.list.length - 1"></view>
						</view>
						<view style="margin-left: 20upx;">
							<!-- <view style="color: #303030;font-size: 30upx;font-weight: bold;">{{item.operator}}</view> -->
							<view style="color: #303030;font-size: 24upx;margin-top: 10upx;">{{item.status}}</view>
							<view style="color: #999999;font-size: 24upx;margin-top: 10upx;">{{item.time}}</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- <empty v-if="selectOption.list.length == 0" des="暂无物流信息" show="true"></empty> -->
		<empty v-if="selectOption.list.length == 0" :content="emptyText" :height="emptyH" style="width: 100%;"></empty>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				expName: '',
				selectOption: {
					expName: ''
				},
				searchValue: '',
				page: 1,
				limit: 10,
				campus: '',
				emptyText: '暂无物流信息',
				emptyH: '100vh',
			};
		},
		onLoad() {
			let selectOption = this.$queue.getData('selectOption');
			if (selectOption) {
				this.selectOption = selectOption;
			}
			console.log(this.selectOption,"this.selectOption")
		},
		methods: {
			deliverystatus(value) {
				if (value == 0) {
					return '快递正在揽件中...'
				} else if (value == 1) {
					return '快递正在运输中...'
				} else if (value == 2) {
					return '快递正在派件中...'
				} else if (value == 3) {
					return '快递已签收'
				} else if (value == 4) {
					return '派送失败'
				} else if (value == 5) {
					return '疑难件'
				} else if (value == 4) {
					return '退件签收'
				}
			},
			copyOrder(msg) {
				let that = this;
				uni.setClipboardData({
					data: msg,
					success: r => {
						that.$queue.showToast('复制成功！')
					}
				});
			}
		}
	};
</script>

<style lang="scss">
	page {
		background: #F5F7FF;
	}

	.container {
		padding: 0 20upx;
		margin-top: 30rpx;

		.header {
			margin: 30upx 16upx 20upx;
			border: 1upx solid #45AEFF;
			width: 686upx;
			height: 90upx;
			background: #FFFFFF;
			border-radius: 8upx;

			.tui-search {
				display: flex;
				align-items: center;
				padding-left: 20upx;

				// flex: 1;
				// height: 60upx;
				.search-icon {
					width: 35upx;
					height: 34upx;
				}

				.text-search {
					line-height: 80upx;
					padding: 0 20upx;
					// background: #F2F2F2;
					height: 80upx;
					border-radius: 32upx;
					display: flex;
					color: #333;
					font-size: 28upx;
				}
			}

		}

		.btn {
			color: #FFFFFF;
			background-color: #45AEFF;
			font-size: 30upx;
			margin: 30upx 16upx 20upx;
			width: 686upx;
			height: 90upx;
			line-height: 90upx;
			text-align: center;
			border-radius: 8rpx;
		}

		.main {
			padding-top: 88upx;

			.title {
				padding: 20upx;
			}

			.item-text-title {
				color: #000000;
				font-size: 15px;
				font-weight: bold;
				margin-left: 20upx;
				margin: 10upx;
				color: #333;
			}

			.item {
				display: flex;
				flex-direction: row;
				flex-wrap: wrap;
			}

			.item-text {
				margin-left: 20upx;
				border-radius: 50upx;
				margin: 10upx;
				border: 1px solid #dee0de;
				padding: 5upx;
				padding-left: 20upx;
				padding-right: 20upx;
				font-size: 13px;
				color: #555555;
			}
		}
	}

	.empty-content {
		padding-top: 440rpx !important;
	}
</style>