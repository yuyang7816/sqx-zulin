<template>
	<view>
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<view class="flex align-center justify-between  bg padding tab">
			<view v-for="(item, index) in list" :key="index" :class="tabIndex == index ? 'active' : ''"
				@click="change(index)">
				{{ item.name }}
			</view>
		</view>
		<!-- 订单列表 -->
		<view class="orderList">
			<view class="orderListItem" @click="navigate('/subpackage/order/orderDet')">
				<view class="" style="display: flex;padding:30rpx 30rpx;justify-content: space-between;">
					<view class="">
						<view class="text_hid" style="font-size: 30rpx;font-weight: 700;">新人首单惊喜优惠券</view>
						<view class="" style="color: #999999;font-size: 24rpx;padding-top: 10rpx;">有效期至2024-03-18</view>
					</view>
					<view class="" style="color: #E72B2F;font-size: 30rpx;font-weight: 600;">
						￥<text style="font-size: 48rpx;">4.00</text>
					</view>
				</view>
				<view class="line"></view>
				<view class=""
					style="display: flex;padding: 30rpx 0; justify-content: space-between;margin:0 30rpx;align-items: baseline;">
					<view class="" style="color: #999999;font-size: 24rpx;">满10元可使用</view>
					<view class="btnBorder">立即使用</view>
				</view>
			</view>
			<view class="orderListItem" @click="navigate('/subpackage/order/orderDet')">
				<view class="" style="display: flex;padding:30rpx 30rpx;justify-content: space-between;">
					<view class="">
						<view class="text_hid" style="font-size: 30rpx;font-weight: 700;">新人首单惊喜优惠券</view>
						<view class="" style="color: #999999;font-size: 24rpx;padding-top: 10rpx;">有效期至2024-03-18</view>
					</view>
					<view class="" style="color: #E72B2F;font-size: 30rpx;font-weight: 600;">
						￥<text style="font-size: 48rpx;">4.00</text>
					</view>
				</view>
				<view class="line"></view>
				<view class=""
					style="display: flex;padding: 30rpx 0; justify-content: space-between;margin:0 30rpx;align-items: baseline;">
					<view class="" style="color: #999999;font-size: 24rpx;">满10元可使用</view>
					<view class="btnBorder">立即使用</view>
				</view>
			</view>
			<view class="orderListItem" @click="navigate('/subpackage/order/orderDet')">
				<view class="" style="display: flex;padding:30rpx 30rpx;justify-content: space-between;">
					<view class="">
						<view class="text_hid" style="font-size: 30rpx;font-weight: 700;">新人首单惊喜优惠券</view>
						<view class="" style="color: #999999;font-size: 24rpx;padding-top: 10rpx;">有效期至2024-03-18</view>
					</view>
					<view class="" style="color: #E72B2F;font-size: 30rpx;font-weight: 600;">
						￥<text style="font-size: 48rpx;">4.00</text>
					</view>
				</view>
				<view class="line"></view>
				<view class=""
					style="display: flex;padding: 30rpx 0; justify-content: space-between;margin:0 30rpx;align-items: baseline;">
					<view class="" style="color: #999999;font-size: 24rpx;">满10元可使用</view>
					<view class="btnBorder">立即使用</view>
				</view>
			</view>
		</view>
		<empty v-if="yhqList.length == 0"></empty>
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
				backText: '我的优惠券',
				backTextStyle: {
					color: '#333333',
					// fontweighth:'bold',
				},
				list: [{
					id: 1,
					name: '可使用'
				}, {
					id: 2,
					name: '已使用'
				}, {
					id: 3,
					name: '已失效'
				}],
				tabIndex: 0,
				page: 0,
				size: 10,
				yhqList: []
			}
		},
		onLoad() {
			this.getUserCoupon()
		},
		onShow() {},
		methods: {
			change(index) {
				this.tabIndex = index
				this.page = 1;
			},
			getUserCoupon() {
				let data = {
					page: this.page,
					size: this.size,
					userId: uni.getStorageSync('userId'),
					type: 1
				}
				this.$Request.getT('/app/selfCouponUser/userList', data).then(res => {
					if (res.code == 0) {

					}
				})
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getUserCoupon()
		},
		onReachBottom() {
			if (this.page < this.commitotal) {
				this.page += 1
				this.getUserCoupon()
			}
		},
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F5F5;
	}

	.bg {
		background-color: #FFFFFF;
	}

	.active {
		color: #15CAD2;
		font-size: 32upx;
		font-weight: bold;
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}

	.slot-wrap {
		display: flex;
		padding-left: 30rpx;
	}

	.tabItem {
		font-size: 26rpx;
		color: #999999;
	}

	.tabAic {
		font-weight: 700;
		font-size: 32rpx;
		color: #000000;
		background: linear-gradient(to top, hsla(183, 100%, 91%, 1) 0%, #FFFFFF 100%) no-repeat bottom / 100% 10px, white;
	}

	.text_hid {
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		text-overflow: ellipsis;
		white-space: normal;
		-webkit-line-clamp: 2;
	}

	.orderList {
		padding: 22rpx 30rpx;

		.orderListItem {
			border-radius: 24rpx;
			background-color: #FFFFFF;
			margin-bottom: 20rpx;
		}
	}

	.line {
		height: 1rpx;
		width: 90%;
		border: 1rpx dashed #eeeeee;
		margin: auto;
		// background-color: #eeeeee;
		// margin: 30rpx auto 10rpx auto;
	}

	.btnBorder {
		border: solid 1rpx #15CAD2;
		height: 48rpx;
		line-height: 48rpx;
		text-align: center;
		font-size: 26rpx;
		color: #15CAD2;
		border-radius: 35rpx;
		width: 180rpx;
	}

	.btnBack {
		height: 70rpx;
		line-height: 70rpx;
		text-align: center;
		width: 180rpx;
		font-size: 26rpx;
		color: #fff;
		background-color: #15CAD2;
		border-radius: 35rpx;
	}

	.btnBackBorder {
		height: 70rpx;
		line-height: 70rpx;
		text-align: center;
		width: 180rpx;
		font-size: 26rpx;
		color: #15CAD2;
		border: solid 1rpx #15CAD2;
		border-radius: 35rpx;
	}

	.sjaddress {
		border-radius: 24rpx;
		background-color: #FFFFFF;
		width: calc(100% - 60rpx);
		margin: auto;
		padding: 25rpx;
		display: flex;
		align-items: center;
	}

	.orderdh {
		border-radius: 24rpx;
		background-color: #FFFFFF;
		width: calc(100% - 60rpx);
		margin: auto;
		padding: 25rpx;
		margin-top: 21rpx;
	}

	.addressTag {
		color: #FFFFFF;
		background-color: #15CAD2;
		border-radius: 6rpx;
		font-size: 24rpx;
		padding: 10rpx 0;
		width: 120rpx;
		text-align: center;
	}

	.orderNum {
		font-size: 24rpx;
		color: #999999;
		background-color: #F5F5F5;
		margin-top: 17rpx;
		padding: 22rpx 0 22rpx 16rpx;
		border-radius: 10rpx;
		width: 95%;
	}

	.btnQrgh {
		background-color: #15CAD2;
		color: #FFFFFF;
		width: calc(100% - 90rpx);
		margin: 30rpx auto;
		font-size: 26rpx;
		height: 88rpx;
		border-radius: 44rpx;
		text-align: center;
		line-height: 88rpx;
	}

	.maxItem {
		border-radius: 38rpx;
		background-color: #F6F6F6;
		border: solid 1rpx #F6F6F6;
		color: #333333;
		height: 76rpx;
		line-height: 76rpx;
		width: 186rpx;
		text-align: center;
		white-space: nowrap;
		margin-bottom: 30rpx;
	}

	.maxItem_ait {
		border-radius: 38rpx;
		background-color: #E6F9F9;
		border: solid 1rpx #15CAD2;
		color: #15CAD2;
		height: 76rpx;
		line-height: 76rpx;
		width: 186rpx;
		text-align: center;
		padding: 0 20rpx;
		white-space: nowrap;
		margin-bottom: 30rpx;
	}
</style>