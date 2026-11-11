<template>
	<view class="cat-page">
		<!-- 顶部搜索 -->
		<view class="backAsearch">
			<view class="hero-glow"></view>
			<view class="searchBox">
				<u-search placeholder="请输入分类名称" v-model="keyword" :animation="true" @search="searchGoodsType"
					@clear="searchGoodsType"></u-search>
			</view>
		</view>
		<view class="cat-body">
			<scroll-view class="scroll_tab" :scroll-y="true" :style="[{ height: `${screenHeight - 60}px` }]">
				<view v-for="(item, index) in tabList" :key="index" :class="index == tabListIndex?'tabAic':'tabItem'"
					@click="handleClick(item,index)">{{ item.name }}
				</view>
			</scroll-view>
			<scroll-view class="scroll_tab_right" :scroll-y="true" :style="[{ height: `${screenHeight - 60}px` }]">
				<view class="" v-for="(item,index) in categoryList" :key="index">
					<template v-if="typeId == item.id">
						<view class="cat-banner" v-if="item.img">
							<image :src="item.img" mode="aspectFill"></image>
						</view>
						<view class="categoryBox" v-for="(category,indexcat) in item.children">
							<view class="cat-group-title">{{category.name}}</view>
							<view class="cat-grid" v-if="category.children.length > 0">
								<view class="cat-cell" v-for="(children, indexchi) in category.children" :key="indexchi"
									@click="pageGoon('/pages/index/categoryList?typeName='+category.name+'&pageType=1&brandId='+children.id)">
									<view class="cat-icon">
										<image :src="children.img" mode="aspectFill"></image>
									</view>
									<view class="cat-name">{{children.name}}</view>
								</view>
							</view>
							<view class="cat-empty" v-else>
								<image src="/static/zlImage/image/nogoods.png"></image>
								<view class="cat-empty-text">换个分类试试吧~</view>
							</view>
						</view>
						<view class="cat-empty cat-empty--page" v-if="item.children.length == 0">
							<image src="/static/zlImage/image/nogoods.png"></image>
							<view class="cat-empty-text">换个分类试试吧~</view>
						</view>
					</template>
				</view>
			</scroll-view>
		</view>
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
				keyword: '',
				tabListIndex: 0,
				tabList: [],
				categoryList: [], //左侧数据
				iStatusBarHeight: '',
				typeId: '', //左侧分类id
				categoryH: '10vh',
				categoryT: '10px',
				screenWidth: '', // 屏幕宽度，单位为px
				screenHeight: '',
				arr: [],
				showModal: true,
			}
		},
		onLoad() {
			let that = this;
			uni.getSystemInfo({
				success: (res) => {
					this.screenWidth = res.windowWidth; // 屏幕宽度，单位为px
					this.screenHeight = res.windowHeight; // 屏幕高度，单位为px
					console.log('屏幕宽度：', this.screenWidth);
					console.log('屏幕高度：', this.screenHeight);
				},
			});
			this.iStatusBarHeight = uni.getSystemInfoSync().statusBarHeight;
			this.$Request.getT('/app/common/type/236').then(res => { //退款微信通知
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.arr.push(res.data.value)
					}
				}
			})
			this.$Request.getT('/app/common/type/522').then(res => { //微信订单通知模板
				if (res.code == 0) {
					if (res.data && res.data.value) {
						that.arr.push(res.data.value)
					}
				}
			})
			console.log(this.iStatusBarHeight, "iStatusBarHeight")
			this.goodsType()
		},
		onShow() {
			let userId = uni.getStorageSync('userId');
			let that = this;
			// #ifdef MP-WEIXIN
			//订阅
			if (userId) {
				if (!uni.getStorageSync('sendindexMsg')) {
					that.openMsg()
				}
			}
			// #endif
		},
		methods: {
			pageGoon(url) {
				let token = uni.getStorageSync('token')
				if (token) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendindexMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) {
								if (datas.indexOf("accept") != -1) {
									console.log(re)
								}
							},
							fail: (res) => {
								console.log(res)
							}
						})
					}
					// #endif
				}
				uni.navigateTo({
					url: url
				})
			},
			openMsg() {
				var that = this
				console.log(that.arr, "that.arrthat.arr")
				wx.getSetting({
					withSubscriptions: true, //是否获取用户订阅消息的订阅状态，默认false不返回
					success(ret) {
						if (ret.subscriptionsSetting.itemSettings) {
							uni.setStorageSync('sendindexMsg', true)
							uni.openSetting({ // 打开设置页 
								success(rea) {
									console.log(rea.authSetting)
								}
							});
						} else { // 用户没有点击“总是保持以上，不再询问”则每次都会调起订阅消息
							uni.setStorageSync('sendindexMsg', false)
							uni.showModal({
								title: '提示',
								content: '为了更好的体验,请绑定消息推送',
								confirmText: '确定',
								cancelText: '取消',
								success: function(res) {
									if (res.confirm) {
										uni.requestSubscribeMessage({
											tmplIds: that.arr,
											success(re) {
												// console.log(re,'**********')
												var datas = JSON.stringify(re);
												if (datas.indexOf("accept") != -1) {
													console.log(re)
													uni.setStorageSync('sendindexMsg', true)
												}
											},
											fail: (res) => {
												console.log(res)
											}
										})
										that.showModal = false
									} else if (res.cancel) {
										that.showModal = true
									}
								}
							})
						}
					}
				})
			},
			// 左侧点击事件
			handleClick(item, index) {
				this.tabListIndex = index
				this.typeId = item.id
			},
			searchGoodsType() {
				this.$Request.getT('/app/goodsType/list', {
					name: this.keyword
				}).then(res => {
					if (res.status == 0) {
						this.typeId = res.data[0].id
						this.tabList.map((item, index) => {
							if (this.typeId == item.id) {
								this.tabListIndex = index
							}
						})
					}
				})
			},
			goodsType() {
				this.$Request.getT('/app/goodsType/list', {
					name: this.keyword
				}).then(res => {
					if (res.status == 0) {
						this.tabList = res.data
						this.typeId = res.data[0].id
						res.data.map(item => {
							this.categoryList.push({
								img: item.img,
								id: item.id,
								children: item.children
							})
						})
						console.log(this.categoryList, "this.categoryList")
					}
				})
			}

		}
	}
</script>

<style lang="scss" scoped>
	page {
		background: linear-gradient(180deg, #D8F0EF 0%, #E4F4F2 28%, #EDF7F4 55%, #F3F9F7 100%);
	}

	.cat-page {
		position: relative;
		min-height: 100vh;
	}

	// 顶部品牌渐变搜索区
	.backAsearch {
		position: fixed;
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		/* #ifdef H5 */
		top: 80rpx;
		/* #endif */
		left: 0;
		right: 0;
		z-index: 10;
		background: linear-gradient(180deg, #16CAD1 0%, #10B0B3 42%, #46C7C4 72%, #9FE2DE 100%);
		padding: 14rpx 0 26rpx;
		overflow: hidden;
	}

	.hero-glow {
		position: absolute;
		width: 300rpx;
		height: 300rpx;
		right: -80rpx;
		top: -160rpx;
		border-radius: 50%;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.26) 0%, rgba(255, 255, 255, 0) 70%);
	}

	.searchBox {
		position: relative;
		width: calc(100% - 60rpx);
		margin: auto;
		background: #FFFFFF;
		border-radius: 999rpx;
		overflow: hidden;
		box-shadow: 0 10rpx 26rpx rgba(11, 148, 155, 0.18);
	}

	.cat-body {
		display: flex;
		padding-top: 132rpx;
	}

	// 左侧分类栏
	.scroll_tab {
		width: 196rpx;
		flex-shrink: 0;

		.tabItem {
			height: 96rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 26rpx;
			color: #4A5858;
			margin: 8rpx 14rpx;
			border-radius: 20rpx;
			text-align: center;
			padding: 0 10rpx;
			box-sizing: border-box;
		}
	}

	.tabAic {
			height: 96rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 26rpx;
			margin: 8rpx 14rpx;
			border-radius: 20rpx;
			text-align: center;
			padding: 0 10rpx 0 24rpx;
			box-sizing: border-box;
			background: linear-gradient(180deg, #FFFFFF 0%, #F0F9F6 100%);
			color: #0E8A8F;
			font-weight: 700;
			box-shadow: 0 10rpx 24rpx rgba(11, 148, 155, 0.12);
			position: relative;

			&::before {
				content: '';
				position: absolute;
				left: 12rpx;
				top: 50%;
				transform: translateY(-50%);
				width: 8rpx;
				height: 40rpx;
				border-radius: 8rpx;
				background: linear-gradient(180deg, #1FD8DE 0%, #0EB5BC 100%);
			}
		}

	// 右侧内容区
	.scroll_tab_right {
		flex: 1;
		box-sizing: border-box;
		padding-right: 20rpx;
	}

	// 分类 banner
	.cat-banner {
		width: 100%;
		height: 160rpx;
		border-radius: 24rpx;
		overflow: hidden;
		margin-bottom: 20rpx;
		box-shadow: 0 12rpx 30rpx rgba(11, 148, 155, 0.12);

		image {
			width: 100%;
			height: 160rpx;
			display: block;
		}
	}

	// 分组卡片
	.categoryBox {
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		box-shadow: 0 14rpx 40rpx rgba(11, 148, 155, 0.07);
		border-radius: 28rpx;
		margin-bottom: 20rpx;
		padding: 28rpx 24rpx 8rpx;
	}

	.cat-group-title {
		font-size: 30rpx;
		font-weight: 700;
		color: #1A2323;
		padding-left: 22rpx;
		position: relative;
		margin-bottom: 28rpx;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 8rpx;
			height: 30rpx;
			border-radius: 8rpx;
			background: linear-gradient(180deg, #1FD8DE 0%, #0EB5BC 100%);
		}
	}

	.cat-grid {
		display: flex;
		flex-wrap: wrap;
	}

	.cat-cell {
		width: calc(100% / 3);
		text-align: center;
		margin-bottom: 32rpx;
	}

	.cat-icon {
		width: 96rpx;
		height: 96rpx;
		border-radius: 24rpx;
		overflow: hidden;
		background: #EDF5F2;
		margin: 0 auto;
		box-shadow: 0 8rpx 18rpx rgba(11, 148, 155, 0.1);

		image {
			width: 96rpx;
			height: 96rpx;
			display: block;
		}
	}

	.cat-name {
		font-size: 24rpx;
		color: #4A5858;
		margin-top: 14rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	// 空状态
	.cat-empty {
		padding: 50rpx 0 60rpx;

		image {
			width: 200rpx;
			height: 100rpx;
			display: block;
			margin: auto;
		}
	}

	.cat-empty--page {
		margin-top: 40%;
		padding: 0;

		image {
			width: 400rpx;
			height: 200rpx;
		}
	}

	.cat-empty-text {
		text-align: center;
		padding: 24rpx 0;
		font-size: 26rpx;
		font-weight: 600;
		color: #8A9A9A;
	}
</style>
