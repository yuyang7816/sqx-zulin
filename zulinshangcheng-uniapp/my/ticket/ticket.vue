<template>
	<view class="">
		<!-- <u-navbar back-text="优惠券"></u-navbar> -->
		<view style="padding-bottom: 100rpx;">
			<view>
				<view class="wdq_view">
					<view class="wdq_toptext">优惠劵商城，您的省钱小帮手</view>
				</view>

				<view class="" style="margin: 0 30rpx;">
					<view class="wdq_my">
						<view class="flex justify-between align-center">
							<view class="wdq_mytext">我的优惠券</view>
							<view class="flex align-center">
								<view class="wdq_mychakan" @tap="goMyQuan">查看更多</view>
								<image src="/static/images/my/right.png"></image>
							</view>
						</view>
					</view>
					<view class="" style="margin-top: 50rpx;" v-if="yhqList && yhqList.length > 0">
						<scroll-view class="scroll-view_H" scroll-x="true" scroll-left="0"
							:style="{width:yhqList.length > 1?'91%':'100%',margin:yhqList.length > 1?'auto':''}">
							<view class="flex">
								<view class="wdq_itemview" v-for="(item, index) in yhqList"
									:style="{marginRight:yhqList.length > 1?'20rpx':''}">
									<image src="../static/yhqbackgroud.png" class="wdq_itemimage"></image>
									<view class="itemview_item">
										<view class="item_moneyview">￥<text>{{ item.lessMoney }}</text> </view>
										<view class="item_rightview">
											<view>
												<view class="item_coupName">{{ item.couponName }}</view>
												<view class="item_coupEndTime" v-if="item.failureTime">
													将于{{ item.failureTime }}到期</view>
												<view class="item_coupEndTime" v-else> 永久有效</view>
											</view>
											<view class="item_coupBtn" @click="shiyong()">去使用</view>
										</view>
									</view>
								</view>
							</view>
						</scroll-view>
					</view>
					<view style="width: 100%;display: flex;justify-content: center;" v-else>
						<image src="../static/noyhq.png" style="width: 251rpx;height: 178rpx;"></image>
					</view>
				</view>
			</view>

			<view class="hd_yhqview">
				<view class="hd_topview">活动优惠券</view>

				<view class="margin-top" v-if="hdyhqList && hdyhqList.length > 0">
					<scroll-view class="hdscroll-view_H" scroll-x="true" scroll-left="0">
						<view class="flex">
							<view class="hdwdq_itemview" v-for="(item, index) in hdyhqList">
								<image src="../static/hdyhq1.png" class="hdwdq_itemimage"></image>
								<view class="hditemview_item">
									<view class="hd_itemname">{{ item.couponName }}</view>
									<view class="hditem_moneyview">￥<text>{{ item.lessMoney }}</text> </view>
									<view class="hd_itemtext" v-if="item.minMoney">满{{ item.minMoney }}元使用</view>
									<view class="hd_itemtext" v-else>无门槛使用</view>
									<view class="hd_itembtn" @click="lingqu(item)">去领取</view>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
				<view style="width: 100%;display: flex;justify-content: center;margin-top: 40rpx;" v-else>
					<image src="../static/noyhq1.png" style="width: 251rpx;height: 178rpx;"></image>
				</view>
			</view>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				couponId: 0,
				showpays: false,
				closeable: true,
				openLists: [],
				openWay: 2,
				yhqList: [],
				hdyhqList: [],
				page: 1,
				size: 10
			}
		},
		onLoad() {
			// 两个接口一样但活动的优惠券可以领取
			this.getMyList();
			this.getHuoDong()
		},
		methods: {
			lingqu(e) {
				this.$Request.postJson('/app/selfCouponUser/save', {
					couponIssueId: e.couponIssueId,
					couponId: e.couponId
				}).then(res => {
					if (res.code == 0 || res.status == 0) {
						uni.showToast({
							title: '领取成功',
							icon: 'none'
						})
						setTimeout(() => {
							this.getMyList()
						}, 50)
					} else {
						this.$queue.showToast(res.msg);
					}
				});
			},
			shiyong() {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			// 活动优惠券
			getHuoDong() {
				this.$Request.getT('/app/selfCouponIssue/useList').then(res => {
					if (res.code == 0 || res.status == 0) {
						this.hdyhqList = res.data;
					}
				});
			},
			getMyList() {
				let data = {
					page: this.page,
					size: this.size,
					userId: uni.getStorageSync('userId'),
					// type: 1
				}
				this.$Request.getT('/app/selfCouponUser/userList', data).then(res => {
					if (res.status == 0) {
						let data = res.data.records
						for (var i = 0; i < data.length; i++) {
							if (data[i].failureTime) {
								data[i].failureTime = data[i].failureTime.substring(0, 10)
							}
						}
						this.yhqList = res.data.records;
					}
				})
			},
			goMyQuan() {
				uni.navigateTo({
					url: '/my/ticket/myList'
				});
			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #F2F2F2;
	}

	.popu_view {
		width: 690rpx;
		height: 420rpx;
		padding: 40rpx 30rpx;

		.popu_viewtitle {
			width: 100%;
			text-align: center;
			font-size: 34rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #333333;
		}

		.kami_btn {
			width: 640rpx;
			height: 88rpx;
			background: #268CFF;
			border-radius: 44rpx;
			text-align: center;
			line-height: 88rpx;
			font-size: 28rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #FFFFFF;
			margin-top: 40rpx;
		}

		.popu_kamiview {
			display: flex;
			align-items: center;
			width: 630rpx;
			height: 100rpx;
			background: #F7F5F5;
			border-radius: 10rpx;
			margin-top: 60rpx;
			padding: 0rpx 20rpx;

			.kamiview_lefttext {
				font-size: 32rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #999999;
			}

			.kamiview_righttext {
				width: 480rpx;
				height: 100rpx;
				font-size: 30rpx;
				color: #333333;
			}

		}

		.placeholderclass {
			font-size: 30rpx;
			color: #333333;
		}
	}


	.qb_view {
		width: 686rpx;
		height: 800rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.qbscroll-view_H {
			width: 100%;
			height: 700rpx;
			padding: 30rpx 0rpx;
		}

		.qb_itemview {
			width: 626rpx;
			height: 160rpx;
			background: #FFFFFF;
			border: 1rpx solid #FEE3C0;
			border-radius: 24rpx;
			padding: 20rpx;
			margin-top: 20rpx;
			display: flex;
			align-items: center;

			.qb_btn {
				width: 140rpx;
				height: 60rpx;
				background: linear-gradient(90deg, #3FD3D1 0%, #3FD3D1 100%);
				border-radius: 30rpx;
				text-align: center;
				line-height: 60rpx;
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #FFFFFF;
				margin-left: 20rpx;
			}

			.qb_itemname {
				width: 200rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				font-size: 28rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #1A1A1A;
			}

			.qb_money {
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #F01B1F;
			}

			.qb_oldmoney {
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: 500;
				// text-decoration: line-through;
				color: #999999;
				margin-left: 10rpx;
			}

			image {
				width: 195rpx;
				height: 96rpx;
			}
		}
	}

	.hd_topview {
		font-size: 32rpx;
		font-family: PingFang SC;
		font-weight: 800;
		color: #050505;

		text {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #999999;
			margin-left: 20rpx;
		}
	}

	.wdq_view {
		width: calc(100% - 64rpx);
		margin: auto;
		height: 130rpx;
		// background: linear-gradient(90deg, #31A1FE 0%, #2589FF 100%);
		background: linear-gradient(90deg, #3FD3D1 0%, #40d9d7 100%);
		border-radius: 24rpx;
		margin-top: 30rpx;
	}

	.wdq_itemview {
		width: 560rpx;
		height: 140rpx;
		margin: 15rpx auto;

		.wdq_itemimage {
			width: 560rpx;
			height: 130rpx;
		}

		.itemview_item {
			display: flex;
			position: absolute;
			top: 10rpx;
			width: 560rpx;
			height: 140rpx;
			align-items: center;
			z-index: 99;

			.item_moneyview {
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #84592B;
				width: 140rpx;
				text-align: center;

				text {
					font-size: 48rpx;
				}
			}

			.item_rightview {
				margin-left: 40rpx;
				display: flex;
				justify-content: space-between;
				width: 360rpx;
				height: 140rpx;
				align-items: center;

				.item_coupName {
					font-size: 28rpx;
					font-family: PingFang SC;
					font-weight: 800;
					color: #1A1A1A;
					width: 200rpx;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
				}

				.item_coupEndTime {
					font-size: 24rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #999999;
					margin-top: 16rpx;
				}

				.item_coupBtn {
					width: 130rpx;
					height: 50rpx;
					background: linear-gradient(90deg, #3FD3D1 0%, #3FD3D1 100%);
					border-radius: 25rpx;
					text-align: center;
					line-height: 50rpx;
					font-size: 24rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #FFFFFF;
				}
			}
		}
	}

	.scroll-view_H {
		white-space: nowrap;
		width: 100%;
		height: 150rpx;
		display: flex;
	}

	.wdq_my {
		width: calc(100% - 60rpx);
		margin: auto;
		height: 240rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		position: absolute;
		/* #ifdef H5 */
		top: 0rpx;
		margin-top: 110rpx;
		/* #endif */
		/* #ifndef H5 */
		top: 130rpx;
		/* #endif */
		// z-index: 99;
		padding: 30rpx;

		.wdq_mytext {
			font-size: 32rpx;
			font-family: PingFang SC;
			font-weight: 800;
			color: #050505;
		}

		.wdq_mychakan {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #999999;
			margin-right: 10rpx;
		}

		image {
			width: 11rpx;
			height: 20rpx;
		}
	}

	.popup_pay {
		width: 100%;
		position: relative;
		padding-bottom: 45rpx;
	}

	.pay_btn {
		width: 90%;
		margin: 0 auto;
		text-align: center;
		background: #0278FE;
		height: 80rpx;
		border-radius: 16rpx;
		color: #ffffff;
		line-height: 80rpx;

	}

	.wdq_toptext {
		font-size: 30rpx;
		font-family: PingFang SC;
		font-weight: bold;
		color: #FFFFFF;
		padding: 30rpx;
	}

	.imag_dw {
		position: absolute;
		top: 18upx;
		left: 15upx;
		z-index: 99;
		color: #ED724B;
		font-size: 32upx;
		font-weight: bold;
		letter-spacing: 1rpx;
	}

	.imag_dw text {
		font-size: 24upx;
	}

	.hd_yhqview {
		width: calc(100% - 60rpx);
		// margin: auto;
		height: 380rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 60rpx 30rpx;
		padding: 30rpx;

		.hdscroll-view_H {
			white-space: nowrap;
			width: 100%;
			height: 280rpx;
			display: flex;
		}

		.hdwdq_itemview {
			margin-right: 20rpx;
			width: 220rpx;
			height: 260rpx;

			.hdwdq_itemimage {
				width: 220rpx;
				height: 260rpx;
			}

			.hditemview_item {
				position: absolute;
				top: 0rpx;
				width: 220rpx;
				height: 260rpx;
				z-index: 99;
				text-align: center;
				padding: 20rpx 0rpx;
				justify-content: center;

				.hd_itemname {
					font-size: 28rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #1A1A1A;
					width: calc(100% - 40rpx);
					margin: auto;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
				}

				.hd_itemtext {
					font-size: 24rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #999999;
				}

				.hd_itembtn {
					width: 130rpx;
					height: 50rpx;
					background: #FEFAEF;
					border-radius: 25rpx;
					margin-top: 40rpx;
					font-size: 24rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #F90C14;
					text-align: center;
					line-height: 50rpx;
					margin-left: 44rpx;
				}

				.hditem_moneyview {
					font-size: 24rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #F01B1F;
					width: 220rpx;
					text-align: center;
					margin: 10rpx 0rpx;

					text {
						font-size: 40rpx;
					}
				}
			}
		}
	}
</style>