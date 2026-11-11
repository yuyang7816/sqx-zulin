<template>
	<view>
		<view class="searchBox">
			<view class="search_input">
				<u-search placeholder="请输入商品名称" v-model="keyword" :animation="true" bg-color="" @search="getGoodsList"
					@clear="clearGoodsList"></u-search>
			</view>
			<view class="goodsCont_sx" style="position: relative;">
				<view class="" style="display: flex;justify-content: space-between; width: calc(100% - 60rpx);margin: auto;">
					<view class="" :class="current==0?'select':''" @click="filterClick(0)">精选推荐</view>
					<view class="flex" @click="cpflCkick">
						<view class="flex align-center" style="margin: 0 auto;">
							<view :class="isShow?'select':''">{{goodsSort}}</view>
							<u-icon name="arrow-up-fill" size="15" style="margin-left: 6rpx;" v-if="isShow"></u-icon>
							<u-icon name="arrow-down-fill" size="15" style="margin-left: 6rpx;" v-if="!isShow"></u-icon>
						</view>
					</view>
					<view class="" style="display: flex;align-items: center;" :class="current==2?'select':''"
						@click="priceSortBtn()">价格排序
						<view class=""
							style="display: flex;flex-direction: column;align-items: center;padding-left: 4rpx;padding-top: 6rpx;">
							<u-icon name="arrow-up-fill" size="12" :color="priceSort !== '1'?'#333333':'#15CAD2'"></u-icon>
							<u-icon name="arrow-down-fill" size="12" :color="priceSort !== '2'?'#333333':'#15CAD2'"></u-icon>
						</view>
					</view>
				</view>
				<view v-if="isShow"
					style="position: absolute;top: 50rpx;width: 100%;z-index: 10;background: rgba(0,0,0,.5);height: 100vh;"
					@click="isShow =false">
					<view class="bg-white" style="height: 490rpx;overflow: hidden;">
						<scroll-view :scroll-y="true" style="width: 100%;height: 100%;">
							<view class="flex justify-between align-center padding-tb-sm u-border-bottom"
								style="padding:15rpx 30rpx;min-height: 80rpx;border-bottom: solid 1rpx #f3f3f3;"
								v-for="item in goodsTypeList" @click.stop="getSelect(item)">
								<view class="text-df" :class="item.select?'select':''">{{item.label}}</view>
								<u-icon name="checkbox-mark" color="#15CAD2" size="28" v-if="item.select"></u-icon>
							</view>
						</scroll-view>
					</view>
				</view>
			</view>
		</view>
		<!-- 筛选和商品 -->
		<view class="goodsCont">
			<view class="goodsCont_list">
				<view class="goodsCont_item" v-for="(item,index) in goodsList" :key="index"
					@click="navigate('/subpackage/goods/goodsDet?goodId='+item.id)">
					<image :src="item.coverImg" mode="aspectFit" style="width: 100%;height: 300rpx;margin: auto;">
					</image>
					<view class="">
						<view class="text_hid" style="font-size: 26rpx;color: #333333;width: 85%;margin: 0 24rpx;">{{item.title}}
						</view>
						<view class="" style="margin:15rpx 20rpx;display: flex;">
							<view class="tag" v-if="item.oldDegree">{{item.oldDegree}}</view>
							<!-- <view class="tag">顺丰包邮</view> -->
						</view>
						<view class="" style="padding:0 0 23rpx 24rpx;display: flex;justify-content: space-between;"
							v-if="item.sku.length>0&&item.sku[0].dailyRent&&item.sku[0].memberRent">
							<view class="" style="font-size: 22rpx;color: #E72C2F;">
								￥<text
									style="font-size: 30rpx;font-weight: 700;">{{isVip == 1?item.sku[0].memberRent:item.sku[0].dailyRent}}</text>/天
							</view>
							<view class="" style="color: #999999;font-size: 24rpx;padding-right: 24rpx;" v-if="item.leaseStart">
								{{item.leaseStart}}天起租
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-if="goodsList.length == 0"></empty>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty,
		},
		data() {
			return {
				background: {
					backgroundColor: 'rgba(63, 211, 209, 1)'
				},
				cityName: '', //顶部城市
				searchWord: '', //搜索框
				maxIndex: 0, //热租爆品index
				opacity: 0,
				isShow: false, //产品分类弹框
				current: 0, //筛选
				goodsSort: '产品分类',
				priceSort: '',
				page: 1,
				limit: 10,
				categoryList: [], //金刚区
				goodsList: [], //商品列表
				selfBanner: [], //轮播图
				rzGoodsList: [], //热租爆品商品
				rzCategory: [], //热租爆品分类
				labelId: '', //热租爆品分类id
				isVip: uni.getStorageSync('isVip'), // isVip 1是会员 2不是
				goodsTypeList: [], //产品分类
				typeId: '', //产品分类选择id
				commitotal: '',
				keyword: '', //品牌跟商品名称搜索
				status: 'loadmore',
				loadText: {
					loadmore: '轻轻上拉',
					loading: '努力加载中',
					nomore: '实在没有了'
				},
				countPage: ''
			}
		},
		onLoad() {
			let that = this;
			uni.getLocation({
				type: 'wgs84', //wgs84  gcj02
				success: function(res) {
					console.log(res, '获取地址成功')
					this.cityName = res.name
				},
				fail: function(ret) {
					console.log('获取地址失败', ret)
				},
				complete: function(ree) {
					console.log('获取地址失败==', ree)
				}
			})
			this.goodsType()
			this.getGoodsList()
		},
		onShow() {

		},
		methods: {
			// 产品分类点击
			cpflCkick() {
				this.isShow = !this.isShow
				this.priceSort = ''
				this.current = 1
			},
			// 价格升序降序
			priceSortBtn(type) {
				// 1:价格正序 2:价格倒序
				this.priceSort = this.priceSort === '' ? '1' : this.priceSort === '1' ? '2' : '1';
				console.log(`现在是`, this.priceSort);
				this.current = 2
				this.isShow = false
				this.getGoodsList()
			},
			filterClick(index) {
				this.current = index
				this.priceSort = ''
				this.typeId = ''
				this.getGoodsList()
			},
			// 产品分类选择
			getSelect(e) {
				console.log(e, "2222222222")
				this.goodsTypeList.forEach(res => {
					if (res.value == e.value) {
						res.select = true
						this.goodsSort = e.label
						this.isShow = false
						this.typeId = e.value
						this.getGoodsList()
					} else {
						res.select = false
					}
				})
			},
			// 获取产品分类
			goodsType() {
				this.$Request.getT('/app/goodsType/result1').then(res => {
					if (res.status == 0) {
						res.data.map(item => {
							item.select = false
						})
						this.goodsTypeList = res.data
					}
				})
			},
			clearGoodsList() {
				this.page = 1
				this.limit = 10
				this.typeId = ''
				this.priceSort = ''
				this.keyword = ''
				this.goodsList = []
			},
			// 商品列表
			getGoodsList() {
				this.$Request.getT('/app/goods/selectedRecommendations', {
					page: this.page,
					size: this.limit,
					typeId: this.typeId, //产品类型id
					type: this.priceSort, //1:价格正序 2:价格倒序
					title: this.keyword
				}).then(res => {
					if (res.status == 0) {
						this.countPage = res.data.pages
						if (this.page == this.countPage) {
							this.status = 'nomore'
						} else {
							this.status = 'loadmore'
						}
						if (this.page == 1) {
							this.goodsList = res.data.records
						} else {
							this.goodsList = [...this.goodsList, ...res.data.records]
						}
						this.commitotal = res.data.total
					}
				})
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getGoodsList()
		},
		onReachBottom() {
			if (this.page < this.countPage) {
				this.page += 1
				this.status = 'loading'
				this.getGoodsList()
			} else {
				this.status = 'nomore'
			}
		},
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #F7F7F7;
	}

	.goodsCont {
		margin-top: 24%;
	}

	.select {
		color: #15CAD2;
	}

	::v-deep .u-indicator-item-round-active {
		background-color: #15CAD2;
	}

	::v-deep.u-dropdown__menu {
		height: auto !important;
	}

	::v-deep.u-dropdown__content {
		// width: 700px !important;
	}

	.text_hid {
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		text-overflow: ellipsis;
		white-space: normal;
		-webkit-line-clamp: 2;
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}

	.searchBox {
		padding-top: 4rpx;
		position: fixed;
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		/* #ifdef H5 */
		top: 84rpx;
		/* #endif */
		z-index: 9999;
		width: 100%;
		background-color: #FFFFFF;

		.search_input {
			width: calc(100% - 60rpx);
			background-color: #F7F7F7;
			border-radius: 38rpx;
			margin: auto;

			.search_icon {
				position: absolute;
				top: 15.5%;
				left: 6.5%;
			}

			.input {
				background-color: #F7F7F7;
				border-radius: 38rpx;
				color: #999999;
				font-size: 26rpx;
				height: 76rpx;
				line-height: 76rpx;
				padding-left: 66rpx;
			}
		}

		.search_btn {
			position: absolute;
			background-color: #3FD3D1;
			font-size: 28rpx;
			color: #333333;
			width: 129rpx;
			height: 66rpx;
			line-height: 66rpx;
			border-radius: 33rpx;
			text-align: center;
			top: 9.5%;
			right: 4.5%;
		}
	}

	.goodsCont_sx {
		font-size: 28rpx;
		color: #333333;
		font-weight: bold;
		display: flex;
		// width: calc(100% - 60rpx);
		margin: 24rpx auto;
		justify-content: space-between;
		align-items: center;
	}

	.goodsCont_list {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		width: calc(100% - 40rpx);
		margin: auto;
	}

	.goodsCont_item {
		background-color: #fff;
		width: calc((100%/2) - 10rpx);
		margin-bottom: 21rpx;
		border-radius: 24rpx;
	}

	.tag {
		color: #999999;
		background-color: #F5F5F5;
		border-radius: 32rpx;
		height: 40rpx;
		line-height: 40rpx;
		font-size: 22rpx;
		text-align: center;
		padding: 0 20rpx;
		margin-right: 20rpx;
	}

	.tag:last-child {
		margin-right: 0 !important;
	}
</style>