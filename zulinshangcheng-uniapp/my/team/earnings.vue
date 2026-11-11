<template>
	<view>
		<!-- <view class="view1" style="border-radius: 20upx;" @click="goTixian">
			<view>
				<view style="font-size: 40rpx;color: #1789FD;">{{ teamMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">总收益</view>
			</view>
		</view> -->
		<view class="box">
			<view class="  " style="padding: 50upx 50upx;">
				<view class="flex align-center justify-between">
					<!-- @click="navigate('/mine/wallet/index')" 如果有提现的话跳转 -->
					<view class="text-center padding-left-sm" @click="navigate('/my/wallet/index')">
						<view style=" font-size: 40rpx;color:#FFE60F;">{{ teamMoney }}</view>
						<view style="font-size: 28rpx;margin-top: 10rpx;">总收益</view>
					</view>
					<view class="text-center">
						<view style="font-size: 40rpx;color:#FFE60F;">{{ oneTeamMoney }}</view>
						<view style="font-size: 28rpx;margin-top: 10rpx;">一级收益</view>
					</view>
					<view class="text-center">
						<view style="font-size: 40rpx;color:#FFE60F;">{{ twoTeamMoney }}</view>
						<view style="font-size: 28rpx;margin-top: 10rpx;">二级收益</view>
					</view>
				</view>

				<!-- <view class="flex align-center justify-between margin-top-xl">
					<view class="btn" @click="goTixian">提现</view>
					<view class="btn " >收益明细</view>
				</view> -->
			</view>
		</view>

		<!-- <view class="view1" style="border-radius: 20upx;display: flex;flex-direction: row;" @click="goTixian">
			<view style="margin: 40upx 0 0 60upx;height: 100upx; width: 150upx;text-align: center;">
				<view style="font-size: 40rpx;color: #82A9FE;">{{ teamMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">总收益</view>
			</view>
			<view style="margin: 40upx 0 0 60upx;height: 100upx; width: 150upx;text-align: center;">
				<view style="font-size: 40rpx;color: #82A9FE;">{{ oneTeamMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">一级收益</view>
			</view>
			<view style="margin: 40upx 0 0 60upx;height: 100upx; width: 150upx;text-align: center;">
				<view style="font-size: 40rpx;color: #82A9FE;">{{ twoTeamMoney }}</view>
				<view style="font-size: 28rpx;margin-top: 10rpx;">二级收益</view>
			</view>
		</view> -->
		<view class="navbar">
			<view v-for="(item, index) in tabList" :key="index" class="nav-item" :class="{current: type == item.type}"
				@click="changeList(item.type)">
				{{item.text}}
			</view>
		</view>
		<view class="view2" style="border-radius: 20upx;margin-bottom: 50upx;">
			<view style="display: flex;flex-direction: row;padding: 20upx;">
				<view style="width: 15%;">编号</view>
				<view style="width: 20%;">头像</view>
				<view style="width: 45%;">昵称</view>
				<view style="width: 30%;text-align: center;">奖励</view>
			</view>
			<view class="flex justify-between align-center padding" v-for="(item, index) in list" :key="index">
				<view style="width: 15%;">
					<view style="font-size: 28upx;margin-left: 15upx;margin-top: 6upx;">{{ index + 1 }}
					</view>
				</view>
				<view style="width: 20%;">
					<image :src="item.avatar?item.avatar:'../../static/logo.png'" class="round"
						style="width: 50rpx;height: 50rpx;">
					</image>
				</view>
				<view style="width: 45%;display: flex;flex-direction: row;align-items: center;">
					<view style="font-size: 28upx;width: 90%;overflow: hidden;">{{ item.userName }}</view>
				</view>
				<view style="width: 30%;text-align: center;display: flex;justify-content: center;align-items: center;">
					<view style="font-size: 32upx;color: #FF7F00;">￥{{ item.money ? item.money : 0 }}</view>
				</view>
			</view>
			<empty v-if="list.length==0"></empty>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				total: 0,
				first: 0,
				second: 0,
				list: [],
				page: 1,
				limit: 10,
				tabIndex: 1,
				checkReZhiShu: '否',
				checkReTuanZhang: '否',
				checkReFeiZhiShu: '否',
				scrollTop: false,
				tabList: [{
					state: 'zs',
					text: '一级',
					type: 1,
					number: 0
				}, {
					state: 'fzs',
					text: '二级',
					type: 2,
					number: 0
				}, ],
				type: 1,
				teamMoney: 0,
				oneTeamMoney: 0,
				twoTeamMoney: 0,
				count: 0,
			}
		},
		onLoad() {

			this.$queue.showLoading("加载中...");
			this.getTeamMoney()
			this.getList();
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
			// golist() {
			// 	uni.navigateTo({
			// 		url: '/my/wallet/shouyi'
			// 	})
			// },
			changeList(e) {
				this.page = 1
				this.type = e
				this.getList()
			},
			getTeamMoney() {
				this.$Request.getT('/app/invite/getExtensionData').then(res => {
					if (res.code == 0) {
						this.teamMoney = res.data.extensionProfit
						this.oneTeamMoney = res.data.oneMoney
						this.twoTeamMoney = res.data.twoMoney
					}
				})
			},
			//收益列表
			getList() {
				uni.showLoading({
					title: '加载中...'
				});
				let data = {
					page: this.page,
					limit: this.limit,
					type: this.type
				}
				this.$Request.getT('/app/invite/getProfitList', data).then(res => {
					if (res.code == 0 && res.data) {
						this.count = res.data.total
						if (this.page == 1) {
							this.list = res.data.records
						} else {
							this.list = [...this.list, ...res.data.records]
						}
					}
					uni.hideLoading();
					uni.stopPullDownRefresh();
				})
			},
			// goTixian() {
			// 	uni.navigateTo({
			// 		url: '/mine/wallet/index'
			// 	})
			// }
		},
		onReachBottom: function() {
			if (this.list.length < this.count) {
				this.page = this.page + 1;
				this.getList();
			} else {
				uni.showToast({
					title: '没有更多了',
					icon: 'none'
				})
			}

		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getList();
		}
	}
</script>

<style lang="less">
	page {
		background: #F5F7FF;
	}

	.box {
		margin: 30upx;
		// box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		background-color: #FFFFFF;
		// background: linear-gradient(90deg, #6C91FE 0%, #4965F9 100%);
		border-radius: 24upx;
		color: #333333;
	}

	.btn {
		width: 280rpx;
		text-align: center;
		// background: fd6f4b;
		background: linear-gradient(90deg, #FFE60F 0%, #FFE60F 100%);
		border-radius: 35upx;
		padding: 16upx 45upx;
		color: #FFFFFF;
	}

	.view1 {
		background-color: #FFFFFF;
		width: 93%;
		// height: 300upx;
		margin-left: 26upx;
		border-radius: 20upx;
		margin-top: 20upx;
		// box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);

		text-align: center;
		padding-bottom: 20rpx;
		// padding: 100upx 0;
	}

	.view2 {
		background-color: #FFFFFF;
		width: 93%;
		margin-left: 26upx;
		border-radius: 20upx;
		margin-top: 20upx;
		margin-bottom: 20upx;
	}

	.yaoqing_view {
		width: 95%;
		display: flex;
		position: fixed;
		bottom: 100rpx;
		justify-content: center;
	}

	.yaoqing_btn {
		width: 520rpx;
		height: 80rpx;
		line-height: 80rpx;
		text-align: center;
		background: #FFE60F;
		// color: #FFFFFF;
		border-radius: 50rpx;
	}

	.tui-tab-item-title {
		// color: #ffffff;
		font-size: 30rpx;
		height: 80rpx;
		line-height: 80rpx;
		flex-wrap: nowrap;
		white-space: nowrap;
	}

	.tui-tab-item-title-active {
		border-bottom: 1px solid#FFE60F;
		color: #FFE60F;
		font-size: 32upx;
		font-weight: bold;
		border-bottom-width: 6upx;
		text-align: center;
	}

	.item {
		background: #f5f5f5;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		// box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
	}

	.navbar {
		display: flex;
		height: 40px;
		border-radius: 20upx;
		// box-shadow: 0 1px 5px rgba(0, 0, 0, .06);
		background: #FFFFFF;
		position: relative;
		z-index: 10;
		margin: 20upx 24upx 0;
		border-radius: 16upx;
		overflow: hidden;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;
			// color: $font-color-dark;
			position: relative;

			&.current {
				// color: $base-color;
				background-color: #FFE60F;
				color: #333333;

				&:after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 44px;
					height: 0;
					// border-bottom: 2px solid $base-color;
				}
			}
		}
	}
</style>