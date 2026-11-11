<template>
	<view>
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<view class="searchBox">
			<u-search placeholder="搜索联系人、聊天记录" v-model="keyword" :show-action="false"
				style="width: calc(100% - 60rpx);margin: auto;"></u-search>
		</view>
		<view class="margin-topW">
			<view class="flex padding-tb radius padding-lr-sm bg" @click="goMsg">
				<view>
					<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;" src="../../static/zlImage/magkf.png"></image>
				</view>
				<view class="flex-sub margin-left-sm">
					<view class="flex justify-between">
						<view class="text-white">在线客服</view>
						<view
							style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
							1
						</view>
					</view>
					<view>
						<view class="text-grey">您好，有什么需要反馈的吗？</view>
					</view>
				</view>
			</view>
			<view class="flex padding-tb radius padding-lr-sm bg" @click="goMsg">
				<view>
					<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;" src="../../static/zlImage/msgtz.png"></image>
				</view>
				<view class="flex-sub margin-left-sm">
					<view class="flex justify-between">
						<view class="text-white">通知消息</view>
						<view
							style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
							2
						</view>
					</view>
					<view>
						<view class="text-grey">超时未付款，系统关闭了订单</view>
					</view>
				</view>
			</view>
		</view>
		<!-- <view class="margin-top-sm" @tap="goChat">
			<view class="flex padding-tb radius padding-lr-sm bg">
				<view>
					<image style="width: 80rpx;height: 80rpx;border-radius: 80rpx;" src="../../static/logo.png"></image>
				</view>
				<view class="flex-sub margin-left-sm">
					<view class="flex justify-between">
						<view class="text-white">在线客服</view>
						<view v-if="adminmessageCount>0"
							style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
							{{adminmessageCount}}
						</view>
					</view>
					<view style="margin-top: 10rpx;">
						<view class="text-grey">有问题请联系在线客服~</view>
					</view>
				</view>
			</view>
		</view> -->
		<!-- <view v-if="chatList.length" class="margin-top-sm  content ">
			<view class="radius padding-lr-sm bg" style="margin-top: 4rpx;" @click="goIM(item)"
				v-for="(item,index) in chatList" :key='index'>
				<view class="flex padding-tb " v-if="userId == item.userId">
					<view>
						<u-image shape="circle" width='80rpx' height="80rpx" :src="item.byAvatar"></u-image>
					</view>
					<view class="flex-sub margin-left-sm">
						<view class="flex justify-between">
							<view class="text-white">{{item.byUserName}}</view>
							<view class="text-grey">{{item.messageTime}}</view>
						</view>
						<view class="flex justify-between">
							<view class="text-grey" v-if="item.messageType == 1">{{ item.content }}</view>
							<view class="text-grey" v-if="item.messageType == 2">[图片]</view>
							<view class="text-grey" v-if="item.messageType == 3">[语音]</view>
							<view class="text-grey" v-if="item.messageType == 4">[表情]</view>
							<view class="text-grey" v-if="item.messageType == 5">[地图]</view>
							<view class="text-grey" v-if="item.messageType == 6">[订单]</view>
							<view class="text-grey" v-if="item.messageType == 7">[服务]</view>
							<view v-if="item.contentCount"
								style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
								{{item.contentCount}}
							</view>
						</view>
					</view>
				</view>
				<view class="flex padding-tb" v-else>
					<view>
						<u-image shape="circle" width='80rpx' height="80rpx" :src="item.avatar"></u-image>
					</view>
					<view class="flex-sub margin-left-sm">
						<view class="flex justify-between">
							<view class="text-white">{{item.userName}}</view>
							<view class="text-grey">{{item.messageTime}}</view>
						</view>
						<view class="flex justify-between">
							<view class="text-grey">{{ item.content }}</view>
							<view v-if="item.contentCount"
								style="height: 32rpx;width: 32rpx;border-radius: 100rpx;background-color: red;color: #FFF;text-align: center;">
								{{item.contentCount}}
							</view>
						</view>
					</view>
				</view>
			</view>
		</view> -->

		<!-- <empty v-if="!chatList.length && !msgList.length" content='暂无消息'></empty>
		<u-tabbar :list="tabbarList" :mid-button="true" bg-color="#FFFFFF" active-color="#557EFD" inactive-color="#999999">
		</u-tabbar> -->
		<!-- 	<u-tabbar v-model="current" :list="list" :mid-button="true" mid-button-size="80" icon-size="41"
			active-color="#557EFD" inactive-color="#999999"></u-tabbar> -->
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
				backText: '消息',
				backTextStyle: {
					color: '#333333',
					// fontweighth:'bold',
				},
				keyword: '',
				page: 1,
				limit: 100,
				chatList: [],
				userId: '',
				msgList: {
					title: '',
					content: '',
				},
				time: '',
				arr: [],
				messageCount: 0,
				adminmessageCount: 0,
			}
		},
		onLoad() {},
		onShow() {},
		onHide() {},
		methods: {}
	}
</script>

<style>
	page {
		background-color: #F7F7F7;
	}

	.bg {
		background: #FFFFFF;
		width: calc(100% - 60rpx);
		margin: auto;
		border-radius: 18rpx;
		margin-top: 15rpx;
	}

	.searchBox {
		width: 100%;
		background-color: #FFFFFF;
		padding-bottom: 35rpx;
		padding-top: 20rpx;
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}
</style>