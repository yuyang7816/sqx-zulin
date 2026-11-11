<template>
	<view>
		<view class="navbar">
			<view v-for="(item, index) in tabList" :key="index" class="nav-item"
				:class="{ current: tabCurrentIndex === item.state }" @click="tabClicks(item.state)">
				{{ item.text }}
			</view>
		</view>

		<view v-for="(item,index) in list" v-if="tabCurrentIndex == 0" @click="goAdd(1,0,item.id)">
			<view
				style="margin-top: 20upx;color: #557EFD;display: flex;align-items: center;background: #FFFFFF;padding: 0rpx 20rpx;">
				<view style="height: 80upx;line-height: 80upx;width: 80%;">
					{{item.content}}
				</view>
				<view style="width: 20%;display: flex;justify-content: flex-end;">
					<image src="../../static/images/my/right_icon.png" style="width: 15rpx;height: 30rpx;"></image>
				</view>
			</view>
		</view>

		<view v-if="tabCurrentIndex == 1 && contentMsg" @click="goAdd(1,1)"
			style="margin-top: 20upx;color: #557EFD;display: flex;align-items: center;background: #FFFFFF;padding: 0rpx 20rpx;">
			<view style="height: 80upx;line-height: 80upx;width: 80%;">
				{{contentMsg}}
			</view>
			<view style="width: 20%;display: flex;justify-content: flex-end;">
				<image src="../../static/images/my/right_icon.png" style="width: 15rpx;height: 30rpx;"></image>
			</view>
		</view>

		<view class="padding-lr-sm" style="position: fixed;bottom: 40upx;width: 100%;z-index: 999;"
			v-if="tabCurrentIndex == 0">
			<u-button class="" :custom-style="customStyle1" :hair-line="false" @click="goAdd(0,0)">
				<u-icon name="plus" color="#fff" size="30" style="margin-right: 10upx;"></u-icon>
				添加
			</u-button>
		</view>
		<view class="padding-lr-sm" style="position: fixed;bottom: 40upx;width: 100%;z-index: 99999;"
			v-if="tabCurrentIndex == 1 && contentMsg == ''">
			<u-button class="" :custom-style="customStyle1" :hair-line="false" @click="goAdd(0,1)">
				<u-icon name="plus" color="#fff" size="30" style="margin-right: 10upx;"></u-icon>
				添加
			</u-button>
		</view>
		<empty v-if="tabCurrentIndex == 0 && list.length == 0" content='暂无常用语信息'></empty>
		<empty v-if="tabCurrentIndex == 1 && contentMsg == ''" content='暂无自动回复信息'></empty>
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
				customStyle1: {
					backgroundColor: "#557EFD",
					border: 0,
					color: "#FFF",
					height: '98rpx'
				},
				contentMsg: '',
				tabCurrentIndex: 0,
				fromInfo: 5,
				limit: 20,
				list: [],
				page: 1,
				scrollTop: false,
				tabList: [{
						state: 0,
						text: '常用词语',
						totalElements: 0
					},
					{
						state: 1,
						text: '自动回复',
						totalElements: 0
					}
				]
			}
		},
		onShow() {
			this.page = 1;
			this.loadData();
		},
		methods: {
			onPageScroll: function(e) {
				this.scrollTop = e.scrollTop > 200;
			},
			onReachBottom: function() {
				this.page = this.page + 1;
				this.loadData();
			},
			onPullDownRefresh: function() {
				this.page = 1;
				this.loadData();
			},
			goAdd(index, type, id) {
				uni.navigateTo({
					url: '/pages/msg/add?type=' + index + '&type1=' + type + '&id=' + id
				});
			},
			//顶部渠道点击
			tabClicks(index) {
				this.list = [];
				this.page = 1;
				this.tabCurrentIndex = index;
				// this.$queue.showLoading("加载中...")
				this.loadData();
			},
			//获取消息列表
			loadData() {
				let that = this;
				let number = 10;
				let token = this.$queue.getData('token');
				if (token) {
					this.$queue.showLoading("加载中...")
					if (this.tabCurrentIndex == 0) {
						this.$Request.getT('/app/message/selectMessageByUserId?page=' + this.page + '&limit=' + this
								.limit +
								'&state=20')
							.then(res => {
								if (res.code == 0) {
									if (this.page == 1) {
										this.list = [];
									}
									res.data.list.forEach(d => {
										this.list.push(d);
									});
								}
								uni.hideLoading();
							});
					} else {
						this.$Request.get("/app/user/selectUserById").then(res => {
							if (res.code == 0) {
								this.contentMsg = res.data.msg ? res.data.msg : '';
							}
							uni.hideLoading();
						});
					}
				}
			}
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.loadData();
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F5F5F5;
	}

	.bg {
		background-color: #FFFFFF;
	}

	.navbar {
		display: flex;
		height: 40px;
		padding: 0 5px;
		background: #FFFFFF;
		box-shadow: 0 1px 5px rgba(0, 0, 0, 0.06);
		position: relative;
		z-index: 10;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;
			color: #000000;
			position: relative;

			&.current {
				color: #557EFD;

				&:after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 44px;
					height: 0;
					border-bottom: 2px solid #557EFD;
				}
			}
		}
	}
</style>
