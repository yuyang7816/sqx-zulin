<template>
	<view class="content">
		<view class="view1" style="border-radius: 20upx;margin-bottom: 20upx;">
			<view style="display: flex;">
				<view style="margin: 70upx 0 0 20upx;height: 100upx; width: 300upx; text-align: center;">
					<view style="font-size: 40upx;">{{ countChildren }}</view>
					<view style="font-size: 28upx;margin-left: 20upx;">邀请好友（人）</view>
				</view>
				<view style="margin: 70upx 0 0 75upx;height: 100upx; width: 300upx;text-align: center;" @click="goDet">
					<view style="font-size: 40upx;">{{ teamMoney?teamMoney:0 }}</view>
					<view style="font-size: 28upx;">我的收益</view>
				</view>
			</view>
			<view style="margin: 20rpx 30rpx;font-size: 28rpx;color: #ffc234;">
				<!-- <view>*用户可得邀请好友开通会员奖励</view> -->
				<view style="margin: 20rpx 0rpx;font-size: 28rpx;color:#ffc234;" v-if="yijiyongjin">
					*邀请一级好友可得{{yijiyongjin*100}}%的消费奖励</view>
				<view style="margin: 20rpx 0rpx;font-size: 28rpx;color:#ffc234;" v-if="erjiyongjin">
					*邀请二级好友可得{{erjiyongjin*100}}%的消费奖励</view>
			</view>

			<button class="yaoqing_btn" @click="goYao">邀请好友</button>
		</view>
		<view class="navbar">
			<view class="nav-item" :class="{current: type == 1}" @click="changeList(1)">
				一级( {{countChildren}} )
			</view>
			<view class="nav-item" :class="{current: type == 2}" @click="changeList(2)">
				二级( {{countChildrenChildren}} )
			</view>
		</view>
		<view class="view2" style="border-radius: 20upx;margin-bottom: 50upx;padding-bottom: 20rpx;">
			<view style="display: flex;flex-direction: row;padding: 20upx;">
				<view style="width: 15%;">编号</view>
				<view style="width: 20%;">头像</view>
				<view style="width: 40%;">昵称</view>
				<view style="width: 35%;text-align: center;">时间</view>
			</view>
			<view class="flex justify-between align-center padding" v-if="list.length > 0" v-for="(item, index) in list"
				:key="index" style="margin-bottom: 16upx;">
				<view style="width: 15%;">
					<view style="font-size: 28upx;margin-left: 15upx;margin-top: 6upx;">{{ index + 1 }}
					</view>
				</view>
				<view style="width: 20%;">
					<image :src="item.avatar?item.avatar:'/static/logo.png'" class="round" style="width: 50rpx;height: 50rpx;">
					</image>
				</view>
				<view style="width: 40%;display: flex;flex-direction: row;align-items: center;">
					<view style="font-size: 28upx;width: 90%;overflow: hidden;">{{ item.userName?item.userName:'' }}
					</view>
				</view>
				<view style="width: 35%;text-align: center;">
					<view style="font-size: 28upx;">
						{{ item.createTime.substr(0,10) }}
					</view>
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
				countChildren: 0,
				countChildrenChildren: 0,
				YaoqingShangJin: 0,
				setting: 1,
				list: [],
				page: 1,
				limit: 10,
				type: 1,
				status: 0,
				teamCount: 0,
				teamMoney: 0,
				isFenxiao: '否',
				oneTeamCount: 0,
				twoTeamCount: 0,
				yijiyongjin: '',
				erjiyongjin: '',
				currentTab: 0,
				tabsnva: [{
					loadingType: ''
				}],
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
				}],
				count: 0,

			};
		},
		onLoad() {
			let YaoqingShangJin = this.$queue.getData('YaoqingShangJin');
			if (YaoqingShangJin && YaoqingShangJin != 0) {
				this.YaoqingShangJin = YaoqingShangJin;
			}
			this.userId = uni.getStorageSync('userId')
			//用户信息
			this.getUserInfo();
			//邀请的用户
			this.getMoney();
			//用户推广信息
			this.getTeamMoney()
		},
		methods: {
			//获取用户信息
			getUserInfo() {
				let data = {
					userId: this.userId
				}
				this.$Request.getT('/app/user/selectUserByUserId', data).then(res => {
					if (res.code == 0) {
						this.yijiyongjin = res.data.divideScale ? res.data.divideScale : 0;
						this.erjiyongjin = res.data.divideTwoScale ? res.data.divideTwoScale : 0;
						uni.setStorageSync('avatar', res.data.avatar)
						uni.setStorageSync('invitationCode', res.data.invitationCode)
					}
				})
			},
			//用户推广信息
			getTeamMoney() {
				this.$Request.getT('/app/invite/getExtensionData').then(res => {
					if (res.code == 0) {
						this.teamMoney = res.data.extensionProfit
						this.countChildren = res.data.countChildren
						this.countChildrenChildren = res.data.countChildrenChildren
					}
				})

			},
			goYao() {
				uni.navigateTo({
					url: '/pages/my/invitationUser'
				});
			},
			changeList(zhishu) {
				this.page = 1
				this.type = zhishu

				this.getMoney()
			},
			//切换一级二级
			changeTab(e) {
				console.log('eeeeeeeee', e)
				this.currentTab = e.target.current;
				if (this.currentTab == 0) {
					this.type = 1
					this.getMoney()
				} else if (this.currentTab == 1) {
					this.type = 2
					this.getMoney()
				}
			},
			//邀请的用户
			getMoney() {
				this.loadingType = 1;
				uni.showLoading({
					title: '加载中...'
				});
				let data = {
					page: this.page,
					limit: this.limit,
					type: this.type,
				}
				this.$Request.getT('/app/invite/getExtensionList', data).then(res => {
					if (res.code == 0) {
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
			goDet() {
				uni.navigateTo({
					url: './earnings'
				})
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getMoney();
		},
		onReachBottom() {
			if (this.list.length < this.count) {
				this.page = this.page + 1;
				this.getMoney()
			} else {
				uni.showToast({
					title: '没有更多了',
					icon: 'none'
				})
			}
		},
	};
</script>

<style lang="scss">
	@import '../../static/css/index.css';

	page {
		background: #F5F7FF;
	}

	.view1 {
		background-color: #FFFFFF;
		width: 93%;
		// height: 300upx;
		margin-left: 26upx;
		border-radius: 20upx;
		margin-top: 20upx;
		padding-bottom: 32upx;
	}

	.view2 {
		background-color: #FFFFFF;
		width: 93%;
		// height: 100%;
		border-top-left-radius: 20upx;
		border-top-right-radius: 20upx;

		margin-left: 26upx;
		margin-top: 20upx;

	}

	.navbar {
		display: flex;
		height: 40px;
		border-radius: 20upx;
		position: relative;
		z-index: 10;
		margin: 0 24rpx;
		border-radius: 20rpx;
		overflow: hidden;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;

			position: relative;

			&.current {

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

	.yaoqing_btn {
		// width: 80%;

		line-height: 80upx;
		margin-top: 30upx;
		height: 85upx;
		color: #333333;
		background: #FFE60F;
		margin-left: 32upx;
		margin-right: 32upx;

		background-size: 100%;
	}

	::v-deep uni-button:after {
		border: none;
	}
</style>