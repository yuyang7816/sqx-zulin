<template>
	<view class="list-page">
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<!-- 顶部搜索和分类 -->
		<view class="backAsearch" :style="{'top':statusBarHeight+'px'}">
			<view class="hero-glow"></view>
			<view class="search-row" v-if="pageType == 0">
				<view class="searchBox">
					<view class="search_input">
						<u-icon name="search" class="search_icon" size="34" color="#8A9A9A"></u-icon>
						<input type="text" placeholder="请输入商品名称" class="input" v-model="keyword" @input="changeInput" />
					</view>
					<view class="search_btn" hover-class="btn-hover" @click="getGoodsList">搜索</view>
				</view>
			</view>
			<view class="searchBox searchBox--usearch" v-if="pageType == 1">
				<u-search placeholder="请输入商品名称" v-model="keyword" :animation="true" bg-color="" @search="categoryGoodsList"
					@clear="categoryGoodsList"></u-search>
			</view>
			<!-- 筛选 -->
			<view class="filter-card">
				<view class="filter-wrap">
					<view class="filter-row">
						<view class="filter-item" :class="current==0?'select':''" @click="filterClick(0)">全部</view>
						<view class="filter-item" @click="cpflCkick">
							<view class="filter-item-inner">
								<view :class="isShow?'select':''">{{goodsSort}}</view>
								<u-icon name="arrow-up-fill" size="15" style="margin-left: 6rpx;" v-if="isShow"></u-icon>
								<u-icon name="arrow-down-fill" size="15" style="margin-left: 6rpx;" v-if="!isShow"></u-icon>
							</view>
						</view>
						<view class="filter-item filter-item--sort" :class="current==2?'select':''"
							@click="priceSortBtn()">价格排序
							<view class="sort-arrows">
								<u-icon name="arrow-up-fill" size="12" :color="priceSort !== '1'?'#333333':'#15CAD2'"></u-icon>
								<u-icon name="arrow-down-fill" size="12" :color="priceSort !== '2'?'#333333':'#15CAD2'"></u-icon>
							</view>
						</view>
					</view>
					<view class="brand-overlay" v-if="isShow" @click="isShow =false">
						<view class="brand-panel">
							<scroll-view :scroll-y="true" style="width: 100%;height: 100%;">
								<view class="brand-item" v-for="item in brandList" @click.stop="getSelect(item)">
									<view class="brand-label" :class="item.select?'select':''">{{item.label}}</view>
									<u-icon name="checkbox-mark" color="#15CAD2" size="28" v-if="item.select"></u-icon>
								</view>
							</scroll-view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 商品 -->
		<view class="goodsCont" v-if="goodsList.length > 0">
			<view class="goodsCont_list">
				<view class="goodsCont_item" v-for="(item,index) in goodsList" :key="index" @click="routerGo(item)">
					<image :src="item.coverImg" mode="aspectFill" class="goods-cover"></image>
					<view class="goods-info">
						<view class="text_hid goods-title">{{item.title}}</view>
						<view class="goods-tags">
							<view class="tag" v-if="item.oldDegree">{{item.oldDegree}}</view>
						</view>
						<view class="goods-bottom">
							<view class="goods-price"
								v-if="item.sku.length > 0&&item.sku[0].memberRent&&item.sku[0].dailyRent">
								￥<text
									class="goods-price-num">{{isVip == 1?item.sku[0].memberRent:item.sku[0].dailyRent}}</text>/天
							</view>
							<view class="goods-lease" v-if="item.leaseStart">
								{{item.leaseStart}}天起租
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-else :top="emptyT"></empty>
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
				emptyT: '250px',
				backText: '',
				cityName: '选择城市',
				backTextStyle: {
					color: '#333333',
					// fontweighth:'bold',
				},
				page: 1,
				limit: 10,
				isShow: false, //产品分类弹框
				current: 0, //筛选
				goodsSort: '品牌',
				priceSort: '',
				typeId: '', //品牌选择id
				brandList: [], //品牌列表
				goodsList: [], //商品列表
				isVip: uni.getStorageSync('isVip'), // isVip 1是会员 2不是
				commitotal: '',
				keyword: '', //品牌跟商品名称搜索
				pageType: '', //0--首页金刚区进入   1--分类品牌进入
				threeTypeId: '', //分类进入品牌id
				latitude: '',
				longitude: '',
				status: 'loadmore',
				loadText: {
					loadmore: '轻轻上拉',
					loading: '努力加载中',
					nomore: '实在没有了'
				},
				countPage: '',
				brandId: '',
				statusBarHeight: ''
			}
		},
		onLoad(option) {
			let that = this;
			if (option.typeName) {
				this.backText = option.typeName
			}
			if (option.pageType) {
				this.pageType = option.pageType
			}
			if (option.brandId) {
				this.threeTypeId = option.brandId
			}
			if (option.typeId) {
				this.typeId = option.typeId
			}
			uni.getSystemInfo({
				success: function(res) {
					that.statusBarHeight = res.statusBarHeight + 44
					console.log(res.statusBarHeight, "statusBarHeightstatusBarHeight")
				}
			});
			uni.getLocation({
				type: 'gcj02', //wgs84  gcj02
				success: function(res) {
					console.log(res, '获取地址成功')
					that.latitude = res.latitude
					that.longitude = res.longitude
					that.shengcheng(res.longitude, res.latitude)
				},
				fail: function(ret) {
					console.log('获取地址失败', ret)
				},
				complete: function(ree) {
					console.log('获取地址失败==', ree)
				}
			})
			if (this.pageType == 0) {
				this.getGoodsList()
			} else {
				this.categoryGoodsList()
			}
			this.goodsBrand()
		},
		onShow() {},
		onPageScroll(e) {
			if (e.scrollTop <= 44) {
				this.opacity = e.scrollTop / 44 * 1
			} else if (e.scrollTop > 44) {
				this.opacity = 1
			}
		},
		methods: {
			routerGo(item) {
				if (uni.getStorageSync('token')) {
					uni.navigateTo({
						url: '/subpackage/goods/goodsDet?goodId=' + item.id
					})
				} else {
					this.noLogin()
				}
			},
			noLogin() {
				let that = this;
				uni.showModal({
					title: '提示',
					content: '您还未登录,请先登录！',
					showCancel: false, // 是否显示取消按钮
					success: function(res) {
						if (res.confirm) {
							uni.navigateTo({
								url: '/pages/public/login'
							})
						}
					}
				});
			},
			shengcheng(longitude, latitude) {
				this.$Request.getT('/app/Login/selectCity', {
					lat: latitude,
					lng: longitude
				}).then(res => {
					if (res.code == 0) {
						this.cityName = res.data.data.city
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						});
					}
				})
			},
			// 选择地址
			chooseAddress() {
				uni.chooseLocation({
					success: function(res) {
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
						// this.cityName = res.name
					}
				});
			},
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
				if (this.pageType == 0) {
					this.getGoodsList()
				} else {
					this.categoryGoodsList()
				}
			},
			filterClick(index) {
				this.current = index
				this.priceSort = ''
				this.brandId = ''
				if (this.pageType == 0) {
					this.getGoodsList()
				} else {
					this.categoryGoodsList()
				}
			},
			// 获取品牌列表
			goodsBrand() {
				this.$Request.getT('/app/selfGoodsBrand/result').then(res => {
					if (res.status == 0) {
						this.brandList = res.data
					}
				})
			},
			// 品牌选择
			getSelect(e) {
				console.log(e, "2222222222")
				this.brandList.forEach(res => {
					if (res.value == e.value) {
						res.select = true
						this.goodsSort = e.label
						this.isShow = false
						this.brandId = e.value
						if (this.pageType == 0) {
							this.getGoodsList()
						} else {
							this.categoryGoodsList()
						}
					} else {
						res.select = false
					}
				})
			},
			changeInput(e) {
				if (e.detail.value == '') {
					console.log(e, "hshshshshsh")
					this.page = 1
					this.limit = 10
					this.priceSort = ''
					this.keyword = ''
					this.getGoodsList()
				}
			},
			// 首页进入--商品列表
			getGoodsList() {
				this.$Request.getT('/app/goods/goodsInfoByBrand', {
					page: this.page,
					size: this.limit,
					typeId: this.typeId, //产品类型id
					type: this.priceSort, //1:价格正序 2:价格倒序
					title: this.keyword,
					brandId: this.brandId
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
			},
			// 分类进入--商品列表
			categoryGoodsList() {
				this.$Request.getT('/app/goods/goodsListByBrandId', {
					page: this.page,
					size: this.limit,
					typeId: this.typeId, //产品类型id
					type: this.priceSort, //1:价格正序 2:价格倒序
					title: this.keyword,
					threeTypeId: this.threeTypeId,
					brandId: this.brandId
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
			if (this.pageType == 0) {
				this.getGoodsList()
			} else {
				this.categoryGoodsList()
			}
		},
		onReachBottom() {
			if (this.page < this.countPage) {
				this.page += 1
				this.status = 'loading'
				if (this.pageType == 0) {
					this.getGoodsList()
				} else {
					this.categoryGoodsList()
				}
			} else {
				this.status = 'nomore'
			}
		},
	}
</script>

<style lang="scss" scoped>
	page {
		background: linear-gradient(180deg, #D8F0EF 0%, #E4F4F2 28%, #EDF7F4 55%, #F3F9F7 100%);
	}

	.list-page {
		position: relative;
		min-height: 100vh;
	}

	.select {
		color: #0FA8AD;
		font-weight: 700;
	}

	.btn-hover {
		opacity: 0.88;
	}

	// 顶部品牌渐变区
	.backAsearch {
		position: fixed;
		left: 0;
		right: 0;
		z-index: 10;
		background: linear-gradient(180deg, #16CAD1 0%, #10B0B3 40%, #46C7C4 68%, #9FE2DE 88%, rgba(216, 240, 239, 0) 100%);
		padding: 12rpx 0 40rpx;
		overflow: visible;
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

	.search-row {
		display: flex;
		align-items: center;
		padding-top: 6rpx;
	}

	// 搜索框
	.searchBox {
		position: relative;
		width: calc(100% - 60rpx);
		margin: auto;
		background: #FFFFFF;
		border-radius: 999rpx;
		box-shadow: 0 10rpx 26rpx rgba(11, 148, 155, 0.18);

		.search_input {
			width: 100%;
			border-radius: 999rpx;
			margin: auto;

			.search_icon {
				position: absolute;
				top: 50%;
				transform: translateY(-50%);
				left: 34rpx;
			}

			.input {
				background: transparent;
				border-radius: 999rpx;
				color: #1A2323;
				font-size: 26rpx;
				height: 76rpx;
				line-height: 76rpx;
				padding-left: 84rpx;
				padding-right: 160rpx;
				box-sizing: border-box;
				width: 100%;
			}
		}

		.search_btn {
			position: absolute;
			background: linear-gradient(135deg, #1FD8DE 0%, #0EB5BC 100%);
			box-shadow: 0 8rpx 18rpx rgba(14, 181, 188, 0.32);
			font-size: 26rpx;
			font-weight: 600;
			color: #FFFFFF;
			width: 128rpx;
			height: 58rpx;
			line-height: 58rpx;
			border-radius: 999rpx;
			text-align: center;
			top: 50%;
			transform: translateY(-50%);
			right: 10rpx;
			z-index: 55;
		}
	}

	.searchBox--usearch {
		overflow: hidden;
		margin-top: 8rpx;
	}

	// 筛选卡
	.filter-card {
		width: calc(100% - 60rpx);
		margin: 22rpx auto 0;
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		border-radius: 24rpx;
		box-shadow: 0 12rpx 32rpx rgba(11, 148, 155, 0.1);
		padding: 24rpx 30rpx;
		box-sizing: border-box;
		position: relative;
		z-index: 5;
	}

	.filter-wrap {
		position: relative;
	}

	.filter-row {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.filter-item {
		font-size: 28rpx;
		color: #4A5858;
		font-weight: 600;
		display: flex;
		align-items: center;
	}

	.filter-item-inner {
		display: flex;
		align-items: center;
		margin: 0 auto;
	}

	.filter-item--sort {
		display: flex;
		align-items: center;
	}

	.sort-arrows {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-left: 6rpx;
	}

	// 品牌下拉
	.brand-overlay {
		position: absolute;
		top: calc(100% + 34rpx);
		left: -30rpx;
		right: -30rpx;
		z-index: 20;
		background: rgba(0, 0, 0, 0.45);
		height: 100vh;
	}

	.brand-panel {
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		height: 490rpx;
		overflow: hidden;
		border-radius: 0 0 32rpx 32rpx;
	}

	.brand-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 15rpx 36rpx;
		min-height: 84rpx;
		box-sizing: border-box;
		border-bottom: 1rpx solid #E2F0EC;
	}

	.brand-label {
		font-size: 28rpx;
		color: #4A5858;
	}

	// 商品列表
	.goodsCont_list {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		width: calc(100% - 40rpx);
		margin: auto;
		margin-top: 300rpx;
		padding-bottom: 30rpx;
	}

	.goodsCont_item {
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		width: calc((100%/2) - 10rpx);
		margin-bottom: 20rpx;
		border-radius: 28rpx;
		overflow: hidden;
		box-shadow: 0 14rpx 40rpx rgba(11, 148, 155, 0.08);
	}

	.goods-cover {
		width: 100%;
		height: 320rpx;
		display: block;
	}

	.goods-info {
		padding: 20rpx 24rpx 26rpx;
	}

	.goods-title {
		font-size: 26rpx;
		color: #1A2323;
		font-weight: 600;
		line-height: 1.4;
		min-height: 72rpx;
	}

	.goods-tags {
		display: flex;
		margin-top: 14rpx;
		min-height: 36rpx;
	}

	.tag {
		color: #0E8A8F;
		background: linear-gradient(120deg, rgba(21, 202, 210, 0.14) 0%, rgba(21, 202, 210, 0.06) 100%);
		border: 1rpx solid rgba(21, 202, 210, 0.16);
		border-radius: 999rpx;
		height: 36rpx;
		line-height: 36rpx;
		font-size: 20rpx;
		text-align: center;
		padding: 0 18rpx;
		margin-right: 14rpx;
	}

	.tag:last-child {
		margin-right: 0 !important;
	}

	.goods-bottom {
		display: flex;
		justify-content: space-between;
		align-items: baseline;
		margin-top: 18rpx;
	}

	.goods-price {
		font-size: 22rpx;
		color: #FD3C44;
		font-weight: 600;
	}

	.goods-price-num {
		font-size: 34rpx;
		font-weight: 800;
	}

	.goods-lease {
		color: #7A8A8A;
		font-size: 20rpx;
		background: #EDF5F2;
		border-radius: 999rpx;
		padding: 6rpx 16rpx;
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
</style>
