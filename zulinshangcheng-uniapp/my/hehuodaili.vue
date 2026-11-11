<template>
	<view>
		<view class="" style="width: 100%;height:100vh;position: relative;">
			<image src="/static/zlImage/daili.png" mode="" style="width: 100%;height: 100%;display: block;"></image>
		</view>
		<view class="msgBox">
			<view class="">
				<view class="" style="font-size: 28rpx;font-weight: bold;padding: 30rpx 0;">姓名</view>
				<view class="input">
					<input type="text" placeholder="请输入姓名" />
				</view>
			</view>
			<view class="">
				<view class="" style="font-size: 28rpx;font-weight: bold;padding: 30rpx 0;">手机号</view>
				<view class="input">
					<input type="text" placeholder="请输入手机号" />
				</view>
			</view>
			<view class="">
				<view class="" style="font-size: 28rpx;font-weight: bold;padding: 30rpx 0;">代理城市</view>
				<view class="input">
					<input type="text" placeholder="请输入" />
				</view>
			</view>
			<view class="footerBtn">提交申请</view>
		</view>
	</view>
</template>

<script>
	import empty from '../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {}
		},
		onLoad() {},
		onShow() {},
		methods: {}
	}
</script>

<style lang="scss" scoped>
	.msgBox {
		position: absolute;
		z-index: 2;
		top: 39%;
		left: 12%;
		width: calc(100% - 180rpx);
		margin: auto;
	}

	.input {
		background-color: #F7F7F7 !important;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 16rpx;
		overflow: hidden;

		input {
			font-size: 28rpx !important;
			color: #999999;
			line-height: 80rpx;
			height: 100%;
			padding-left: 20rpx;
		}
	}

	.footerBtn {
		background-color: #15CAD2;
		color: #ffffff;
		height: 96rpx;
		line-height: 96rpx;
		border-radius: 48rpx;
		text-align: center;
		margin: 95rpx auto;
	}
</style>