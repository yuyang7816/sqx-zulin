<template>
	<view>
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<!-- 订单列表 -->
		<view class="orderList">
			<view class="" style="border-bottom: solid 1rpx #E6E6E6;padding-bottom: 10rpx;padding-top: 40rpx;">
				<view class="" style="font-size: 32rpx;font-weight: 700;">法人姓名</view>
				<input type="text" placeholder="请输入法人姓名" class="input" />
			</view>
			<view class="" style="border-bottom: solid 1rpx #E6E6E6;padding-bottom: 10rpx;padding-top: 40rpx;">
				<view class="" style="font-size: 32rpx;font-weight: 700;">法人身份证</view>
				<input type="text" placeholder="请输入身份证号码" class="input" />
			</view>
			<view class="" style="border-bottom: solid 1rpx #E6E6E6;padding-bottom: 10rpx;padding-top: 40rpx;">
				<view class="" style="font-size: 32rpx;font-weight: 700;">联系电话</view>
				<input type="text" placeholder="请输入电话号码" class="input" />
			</view>
			<view class="" style="border-bottom: solid 1rpx #E6E6E6;padding-bottom: 10rpx;padding-top: 40rpx;">
				<view class="" style="font-size: 32rpx;font-weight: 700;">企业名称</view>
				<input type="text" placeholder="请输入企业名称" class="input" />
			</view>
			<view class="" style="border-bottom: solid 1rpx #E6E6E6;padding-bottom: 10rpx;padding-top: 40rpx;">
				<view class="" style="font-size: 32rpx;font-weight: 700;">营业执照注册号</view>
				<input type="text" placeholder="请输入营业执照注册号" class="input" />
			</view>
			<view class="" style="border-bottom: solid 1rpx #E6E6E6;padding-bottom: 20rpx;padding-top: 40rpx;">
				<view class="" style="font-size: 32rpx;font-weight: 700;">详细地址</view>
				<input type="text" placeholder="请输入详细地址" class="input" />
			</view>
		</view>
		<view class="orderList">
			<view class="" style="font-weight: 700;font-size: 32rpx;padding: 30rpx 0;">上传营业执照</view>
			<view class=""
				style="width: 530rpx;height: 300rpx;border-radius: 20rpx;border: 2rpx #15CAD2 dashed;margin: auto;text-align: center;">
				<image src="/static/zlImage/smxj.png" mode="" style="width: 122rpx;height: 122rpx;margin-top: 65rpx;"></image>
				<view class="">上传营业执照证件</view>
			</view>
		</view>
		<view class="footerBtn">
			提交信息</view>
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
				backText: '商家入驻',
				backTextStyle: {
					color: '#333333',
					// fontweighth:'bold',
				},
				keyword: '',
				tabListIndex: 0,
				list: [{
					name: '全部'
				}, {
					name: '待付款'
				}, {
					name: '待发货',
				}, {
					name: '待收货'
				}, {
					name: '待归还'
				}],
				tabCurrent: 0,
				activeItemStyle: {
					fontSize: '32rpx'
				},
				ghPopup: false, //归还
				jxPopup: false, //继续租赁
				maxItemList: [{
						name: '7天'
					},
					{
						name: '30天'
					},
					{
						name: '60天'
					},
					{
						name: '90天'
					},
					{
						name: '180天'
					},
					{
						name: '365天'
					}
				],
				maxIndex: 0,
				mdPopup: false
			}
		},
		onLoad() {
			this.iStatusBarHeight = uni.getSystemInfoSync().statusBarHeight + 155;
			console.log(this.iStatusBarHeight, "iStatusBarHeight")
		},
		onShow() {},
		methods: {
			// 顶部点击事件
			changeTabIndex(index) {
				this.tabListIndex = index
			},
			change(index) {
				this.tabCurrent = index;
			},
			// 归还
			backGood() {
				this.ghPopup = true
			},
			// 继续租赁
			goonGood() {
				this.jxPopup = true
			},
			//继续租赁天数选择
			maxItem(index) {
				this.maxIndex = index
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F5F5;
	}

	.footerBtn {
		background-color: #15CAD2;
		border-radius: 12rpx;
		height: 88rpx;
		line-height: 88rpx;
		width: calc(100% - 60rpx);
		margin: auto;
		color: #ffffff;
		text-align: center;
		font-weight: bold;
		margin-top: 20rpx;
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}

	.input {
		font-size: 24rpx;
		color: #999999;
		margin-top: 30rpx;
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
		background-color: #FFFFFF;
		width: calc(100% - 60rpx);
		margin: auto;
		border-radius: 24rpx;
		margin-top: 20rpx;
	}

	.line {
		height: 1rpx;
		width: 90%;
		margin: auto;
		background-color: #eeeeee;
		margin: 30rpx auto 10rpx auto;
	}

	.btnBorder {
		border: solid 1rpx #15CAD2;
		height: 70rpx;
		line-height: 70rpx;
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