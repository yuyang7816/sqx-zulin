<template>
	<view class="page-container">
	<!-- 顶部渐变背景层（常驻，进入页面即有主题色背景） -->
	<view class="header-bg">
		<view class="header-blob header-blob-1"></view>
		<view class="header-blob header-blob-2"></view>
		<view class="header-blob header-blob-3"></view>
	</view>
	
	<!-- 固定导航 -->
	<view class="nav-bar-fixed" :style="{'background-color': `rgba(21, 202, 210, ${opacity})`, 'box-shadow': opacity > 0.8 ? '0 4rpx 20rpx rgba(14, 168, 166, 0.25)' : 'none'}">
		<view class="nav-inner">
			<view class="nav-top-row">
				<view class="brand-lockup">
					<view class="brand-logo">
						<text class="brand-logo-text">租</text>
					</view>
					<view class="brand-texts">
						<text class="brand-name">租赁商城</text>
						<text class="brand-slogan">品质好物 · 随心所租</text>
					</view>
				</view>
				<view class="location-pill" @click="chooseAddress">
					<u-icon name="map-fill" size="24" color="#0EA8A6"></u-icon>
					<text class="location-text">{{cityName||'选择城市'}}</text>
					<u-icon name="arrow-down-fill" size="16" color="#15CAD2"></u-icon>
				</view>
			</view>
			<view class="search-box" @click="pageGoon('/pages/index/search')">
				<view class="search-icon-wrap">
					<u-icon name="search" size="28" color="#15CAD2"></u-icon>
				</view>
				<text class="search-placeholder">搜索你想要的租赁商品</text>
				<view class="search-btn">搜索</view>
			</view>
		</view>
	</view>

		<!-- 内容区 -->
		<view class="content-wrap">
			<!-- 金刚区 + Banner -->
			<view class="top-section">
				<view class="category-card">
					<view class="category-grid">
						<view class="category-item" v-for="(item,index) in categoryList" :key="index" @click="pageGoon(item.url)">
							<view class="category-icon-wrap">
								<image :src="item.image_url" mode="aspectFit" class="category-icon"></image>
							</view>
							<text class="category-title">{{item.title}}</text>
						</view>
					</view>
				</view>
				
				<view class="banner-wrap">
					<u-swiper :list="selfBanner" border-radius="20" img-mode="aspectFill" bg-color="transparent" height="180"></u-swiper>
				</view>
			</view>

			<!-- 租机流程 -->
			<view class="flow-section">
				<view class="section-header">
				<view class="section-title-wrap">
					<view class="section-title">租机流程</view>
					<text class="section-subtitle">四步轻松租</text>
				</view>
			</view>
				<view class="flow-track">
					<view class="flow-track-line"></view>
					<view class="flow-steps">
						<view class="flow-step" v-for="(step, idx) in flowSteps" :key="idx">
							<view class="step-circle">
								<text class="step-num">{{idx + 1}}</text>
							</view>
							<text class="step-text">{{step}}</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 热租爆品 -->
			<view class="hot-section">
				<view class="section-header section-header-hot">
					<view class="section-title-wrap">
						<view class="section-title">热租爆品</view>
						<text class="section-subtitle">人气精选</text>
					</view>
					<view class="title-line"></view>
				</view>
				<scroll-view class="hot-tabs" :scroll-x="true" show-scrollbar="false">
					<view class="hot-tabs-inner">
						<view class="hot-tab" :class="{'hot-tab-active': index == maxIndex}" v-for="(item,index) in rzCategory" :key="index" @click="clickItme(item,index)">
							{{item.label}}
						</view>
					</view>
				</scroll-view>
				<scroll-view class="hot-goods-scroll" :scroll-x="true" show-scrollbar="false" v-if="rzGoodsList.length > 0">
					<view class="hot-goods-list">
						<view class="hot-goods-item" v-for="(item,index) in rzGoodsList" :key="index" @click="pageGoon('/subpackage/goods/goodsDet?goodId='+item.id)">
							<view class="hot-img-wrap">
								<image :src="item.coverImg" mode="aspectFill" class="hot-img"></image>
								<view class="hot-badge">热租</view>
							</view>
							<view class="hot-info">
								<view class="hot-title text_hid">全新·{{item.title}}</view>
								<view class="hot-price" v-if="(item.sku[0].memberRent&&item.sku[0].memberRent > 0&&item.sku[0].memberRent!=null)&&(item.sku[0].dailyRent&&item.sku[0].dailyRent >0&&item.sku[0].dailyRent!=null)">
									<text class="price-symbol">¥</text>
									<text class="price-num">{{isVip == 1?item.sku[0].memberRent:item.sku[0].dailyRent}}</text>
									<text class="price-unit">/天</text>
								</view>
							</view>
						</view>
					</view>
				</scroll-view>
				<view class="hot-empty" v-else>
					<image src="/static/zlImage/image/nogoods.png" mode="aspectFit" class="empty-img"></image>
					<text class="empty-text">换个分类试试吧~</text>
				</view>
			</view>

			<!-- 筛选 + 商品瀑布流 -->
			<view class="goods-section">
				<!-- 吸顶筛选 -->
				<view class="filter-bar">
					<view class="filter-inner">
						<view class="filter-segment">
							<view class="filter-item" :class="{'filter-active': current==0}" @click="filterClick(0)">精选推荐</view>
							<view class="filter-item filter-dropdown" :class="{'filter-active': current==1 || isShow}" @click="cpflCkick">
								<text>{{goodsSort}}</text>
								<u-icon :name="isShow?'arrow-up-fill':'arrow-down-fill'" size="14" :color="(isShow || current==1)?'#15CAD2':'#999'" style="margin-left: 4rpx;"></u-icon>
							</view>
							<view class="filter-item" :class="{'filter-active': current==2}" @click="priceSortBtn()">
								<text>价格</text>
								<view class="sort-arrows">
									<u-icon name="arrow-up-fill" size="10" :color="priceSort === '1'?'#15CAD2':'#ccc'"></u-icon>
									<u-icon name="arrow-down-fill" size="10" :color="priceSort === '2'?'#15CAD2':'#ccc'" style="margin-top: 2rpx;"></u-icon>
								</view>
							</view>
						</view>
					</view>
					
				<!-- 分类下拉面板 -->
				<view v-if="isShow" class="dropdown-panel">
					<view class="dropdown-content">
						<scroll-view :scroll-y="true" class="dropdown-scroll">
							<view class="dropdown-item" :class="{'dropdown-active': item.select}" v-for="item in goodsTypeList" :key="item.value" @click="getSelect(item)">
								<text class="dropdown-label">{{item.label}}</text>
								<u-icon name="checkbox-mark" color="#15CAD2" size="28" v-if="item.select"></u-icon>
							</view>
						</scroll-view>
					</view>
				</view>
			</view>

			<!-- 瀑布流商品列表 -->
				<view class="waterfall-wrap">
					<view class="waterfall-column">
						<view class="goods-card" v-for="(item,index) in goodsList" :key="index" v-if="index % 2 === 0" @click="routeGO(item)">
							<view class="goods-img-wrap">
								<image :src="item.coverImg" mode="widthFix" class="goods-img"></image>
							</view>
							<view class="goods-body">
								<view class="goods-title text_hid">{{item.title}}</view>
								<view class="goods-tags" v-if="item.oldDegree">
									<text class="tag">{{item.oldDegree}}</text>
								</view>
								<view class="goods-footer">
									<view class="goods-price" v-if="item.sku.length>0&&(item.sku[0].memberRent != null&&item.sku[0].memberRent > 0)&&(item.sku[0].dailyRent&&item.sku[0].dailyRent >0)">
										<text class="price-symbol">¥</text>
										<text class="price-num">{{isVip == 1?item.sku[0].memberRent:item.sku[0].dailyRent}}</text>
										<text class="price-unit">/天</text>
									</view>
									<text class="goods-lease" v-if="item.leaseStart">{{item.leaseStart}}</text>
								</view>
							</view>
						</view>
					</view>
					<view class="waterfall-column">
						<view class="goods-card" v-for="(item,index) in goodsList" :key="index" v-if="index % 2 === 1" @click="routeGO(item)">
							<view class="goods-img-wrap">
								<image :src="item.coverImg" mode="widthFix" class="goods-img"></image>
							</view>
							<view class="goods-body">
								<view class="goods-title text_hid">{{item.title}}</view>
								<view class="goods-tags" v-if="item.oldDegree">
									<text class="tag">{{item.oldDegree}}</text>
								</view>
								<view class="goods-footer">
									<view class="goods-price" v-if="item.sku.length>0&&(item.sku[0].memberRent != null&&item.sku[0].memberRent > 0)&&(item.sku[0].dailyRent&&item.sku[0].dailyRent >0)">
										<text class="price-symbol">¥</text>
										<text class="price-num">{{isVip == 1?item.sku[0].memberRent:item.sku[0].dailyRent}}</text>
										<text class="price-unit">/天</text>
									</view>
									<text class="goods-lease" v-if="item.leaseStart">{{item.leaseStart}}</text>
								</view>
							</view>
						</view>
					</view>
				</view>
				
				<empty v-if="goodsList.length == 0" :height="emptyH" :top="emptyT"></empty>
				<u-loadmore v-if="goodsList.length>0" :status="status" :load-text="loadText" margin-top="30" margin-bottom="30"/>
			</view>
		</view>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: { empty },
		data() {
			return {
				background: { backgroundColor: 'rgba(63, 211, 209, 1)' },
				cityName: '',
				searchWord: '',
				list: [],
				maxIndex: 0,
				opacity: 0,
				opacityHeigth: 100,
				isShow: false,
				current: 0,
				goodsSort: '产品分类',
				priceSort: '',
				page: 1,
				limit: 10,
				categoryList: [],
				goodsList: [],
				selfBanner: [],
				rzGoodsList: [],
				rzCategory: [],
				labelId: '',
				isVip: uni.getStorageSync('isVip'),
				goodsTypeList: [],
				typeId: '',
				commitotal: '',
				emptyC: '换个分类试试吧~',
				emptyH: '25vh',
				emptyT: '10px',
				latitude: '',
				longitude: '',
				status: 'loadmore',
				loadText: { loadmore: '轻轻上拉', loading: '努力加载中', nomore: '实在没有了' },
				countPage: '',
				arr: [],
				showModal: true,
				flowSteps: ['选择商品', '身份认证', '缴保证金', '开始下单']
			}
		},
		onLoad() {
			let that = this;
			uni.getLocation({
				type: 'gcj02',
				success: function(res) {
					that.latitude = res.latitude
					that.longitude = res.longitude
					that.shengcheng(res.longitude, res.latitude)
				},
				fail: function(ret) { console.log('获取地址失败', ret) },
				complete: function(ree) { console.log('获取地址失败==', ree) }
			})
			this.$Request.getT('/app/common/type/236').then(res => {
				if (res.code == 0) { if (res.data && res.data.value) { that.arr.push(res.data.value) } }
			})
			this.$Request.getT('/app/common/type/522').then(res => {
				if (res.code == 0) { if (res.data && res.data.value) { that.arr.push(res.data.value) } }
			})
			this.selectBySectionId()
			this.getGoodsList()
			this.getBanner()
			this.rzCategoryList()
			this.goodsType()
		},
		onShow() {
			let userId = uni.getStorageSync('userId');
			let that = this;
			// #ifdef MP-WEIXIN
			if (userId) {
				if (!uni.getStorageSync('sendindexMsg')) { that.openMsg() }
			}
			// #endif
		},
		onPageScroll(e) {
			if (e.scrollTop <= 44) {
				this.opacity = e.scrollTop / 44 * 1
			} else if (e.scrollTop > 44) {
				this.opacity = 1
				// #ifdef APP
				this.opacityHeigth = 140
				// #endif
				// #ifdef MP-WEIXIN
				this.opacityHeigth = 160
				// #endif
			}
		},
		methods: {
			pageGoon(url) {
				let token = uni.getStorageSync('token')
				if (token) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendindexMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) { if (datas.indexOf("accept") != -1) { console.log(re) } },
							fail: (res) => { console.log(res) }
						})
					}
					// #endif
				}
				uni.navigateTo({ url: url })
			},
			openMsg() {
				var that = this
				wx.getSetting({
					withSubscriptions: true,
					success(ret) {
						if (ret.subscriptionsSetting.itemSettings) {
							uni.setStorageSync('sendindexMsg', true)
							uni.openSetting({
								success(rea) { console.log(rea.authSetting) }
							});
						} else {
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
												var datas = JSON.stringify(re);
												if (datas.indexOf("accept") != -1) {
													uni.setStorageSync('sendindexMsg', true)
												}
											},
											fail: (res) => { console.log(res) }
										})
										that.showModal = false
									} else if (res.cancel) { that.showModal = true }
								}
							})
						}
					}
				})
			},
			routeGO(item) {
				if (uni.getStorageSync('token')) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendindexMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) { if (datas.indexOf("accept") != -1) { console.log(re) } },
							fail: (res) => { console.log(res) }
						})
					}
					// #endif
					uni.navigateTo({ url: '/subpackage/goods/goodsDet?goodId=' + item.id })
				} else { this.noLogin() }
			},
			noLogin() {
				let that = this;
				uni.showModal({
					title: '提示',
					content: '您还未登录,请先登录！',
					showCancel: false,
					success: function(res) {
						if (res.confirm) { uni.navigateTo({ url: '/pages/public/login' }) }
					}
				});
			},
			shengcheng(longitude, latitude) {
				this.$Request.getT('/app/Login/selectCity', { lat: latitude, lng: longitude }).then(res => {
					if (res.code == 0) {
						if (res.data.data.city) { this.cityName = res.data.data.city }
						else { this.cityName = res.data.data.province + res.data.data.district }
					} else {
						uni.showToast({ icon: 'none', title: res.msg });
					}
				})
			},
			chooseAddress() {
				let that = this;
				uni.chooseLocation({
					success: function(res) {
						that.shengcheng(res.longitude, res.latitude)
					}
				});
			},
			cpflCkick() {
				this.isShow = !this.isShow
				this.priceSort = ''
				this.current = 1
			},
			priceSortBtn(type) {
				this.priceSort = this.priceSort === '' ? '1' : this.priceSort === '1' ? '2' : '1';
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
			getSelect(e) {
				this.goodsTypeList.forEach(res => {
					if (res.value == e.value) {
						res.select = true
						this.goodsSort = e.label
						this.isShow = false
						this.typeId = e.value
						this.getGoodsList()
					} else { res.select = false }
				})
			},
			change(e) { console.log('e:', e); },
			clickItme(item, index) {
				this.maxIndex = index
				this.labelId = item.labelId
				this.rzbpGoodsList()
			},
			closeDropdown() { this.$refs.uDropdown.close(); },
			selectBySectionId() {
				this.$Request.getT('/app/selfActivity/list').then(res => {
					if (res.status == 0) { this.categoryList = res.data }
				})
			},
			getBanner() {
				this.$Request.getT('/app/advert/list').then(res => {
					if (res.code == 0) {
						this.selfBanner = res.data
						this.selfBanner.map(item => { item.image = item.imgUrl })
					}
				})
			},
			rzCategoryList() {
				this.$Request.getT('/app/goods/selectByLabelType', { page: this.page, size: this.limit }).then(res => {
					if (res.status == 0) {
						this.rzCategory = res.data.records
						this.labelId = res.data.records[0].labelId
						this.rzbpGoodsList()
					}
				})
			},
			rzbpGoodsList() {
				this.$Request.getT('/app/goods/selectedGood', { page: this.page, size: this.limit, labelId: this.labelId }).then(res => {
					if (res.status == 0) { this.rzGoodsList = res.data.records }
				})
			},
			goodsType() {
				this.$Request.getT('/app/goodsType/result1').then(res => {
					if (res.status == 0) {
						res.data.map(item => { item.select = false })
						this.goodsTypeList = res.data
					}
				})
			},
			getGoodsList() {
				this.$Request.getT('/app/goods/selectedRecommendations', {
					page: this.page,
					size: this.limit,
					typeId: this.typeId,
					type: this.priceSort
				}).then(res => {
					if (res.status == 0) {
						this.countPage = res.data.pages
						if (this.page == this.countPage) { this.status = 'nomore' }
						else { this.status = 'loadmore' }
						if (this.page == 1) { this.goodsList = res.data.records }
						else { this.goodsList = [...this.goodsList, ...res.data.records] }
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
			} else { this.status = 'nomore' }
		},
	}
</script>

<style lang="scss" scoped>
	/* ========== 设计令牌（基于主题色 #15CAD2 提取） ========== */
	$primary: #15CAD2;
	$primary-dark: #0EA8A6;
	$primary-darker: #0B8A88;
	$primary-light: #E8F8F8;
	$primary-lighter: #F0FCFC;
	$primary-soft: rgba(21, 202, 210, 0.12);
	$primary-glow: rgba(21, 202, 210, 0.28);
	$primary-shadow: rgba(21, 202, 210, 0.18);
	$accent: #FF4757;
	$accent-soft: rgba(255, 71, 87, 0.08);
	$bg: #F2F6F8;
	$card-bg: #FFFFFF;
	$text-main: #1A1A2E;
	$text-sub: #5C6370;
	$text-tip: #A0A8B3;
	$border: #E8ECEF;
	$shadow-sm: 0 2rpx 12rpx rgba(26, 26, 46, 0.04);
	$shadow: 0 8rpx 32rpx rgba(26, 26, 46, 0.06);
	$shadow-lg: 0 16rpx 48rpx rgba(26, 26, 46, 0.08);
	$radius-sm: 12rpx;
	$radius-md: 20rpx;
	$radius-lg: 28rpx;

	/* ========== 全局 ========== */
	page { background-color: $bg; }
	
	.page-container {
		min-height: 100vh;
		background: $bg;
	}

	.text_hid {
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		text-overflow: ellipsis;
		-webkit-line-clamp: 2;
		word-break: break-all;
	}

	/* ========== 顶部背景装饰 ========== */
	.header-bg {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		height: 560rpx;
		background: linear-gradient(180deg, #11C4D2 0%, #2BD3D5 38%, #5EDFD9 62%, rgba(94, 223, 217, 0.35) 82%, rgba(242, 246, 248, 0) 100%);
		z-index: 1;
		pointer-events: none;
		overflow: hidden;
	}

	.header-blob {
		position: absolute;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.1);
		&-1 {
			width: 340rpx;
			height: 340rpx;
			top: -100rpx;
			right: -80rpx;
		}
		&-2 {
			width: 200rpx;
			height: 200rpx;
			top: 200rpx;
			left: -60rpx;
			background: rgba(255, 255, 255, 0.06);
		}
		&-3 {
			width: 120rpx;
			height: 120rpx;
			top: 60rpx;
			left: 45%;
			background: rgba(255, 255, 255, 0.08);
		}
	}

	/* ========== 固定导航 ========== */
	.nav-bar-fixed {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		z-index: 99;
		padding-top: var(--status-bar-height);
		/* #ifdef MP-WEIXIN */
		padding-top: calc(var(--status-bar-height) + 20rpx);
		/* #endif */
		transition: background-color 0.3s;
	}

	.nav-inner {
		padding: 12rpx 28rpx 24rpx;
	}

	.nav-top-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 20rpx;
		/* 为微信小程序右上角胶囊按钮预留空间 */
		/* #ifdef MP-WEIXIN */
		padding-right: 190rpx;
		/* #endif */
	}

	/* ========== 品牌区 ========== */
	.brand-lockup {
		display: flex;
		align-items: center;
	}

	.brand-logo {
		width: 64rpx;
		height: 64rpx;
		background: linear-gradient(145deg, #FFFFFF 0%, #E8FCFC 100%);
		border-radius: 18rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 6rpx 16rpx rgba(11, 138, 136, 0.25), inset 0 -2rpx 6rpx rgba(21, 202, 210, 0.15);
		margin-right: 16rpx;
		.brand-logo-text {
			font-size: 34rpx;
			font-weight: 900;
			color: $primary-dark;
			line-height: 1;
		}
	}

	.brand-texts {
		display: flex;
		flex-direction: column;
		.brand-name {
			font-size: 36rpx;
			font-weight: 800;
			color: #fff;
			letter-spacing: 3rpx;
			line-height: 1.25;
			text-shadow: 0 2rpx 8rpx rgba(11, 138, 136, 0.3);
		}
		.brand-slogan {
			font-size: 20rpx;
			color: rgba(255, 255, 255, 0.85);
			letter-spacing: 2rpx;
			margin-top: 4rpx;
		}
	}

	/* ========== 定位胶囊（白底芯片，避开右上角微信胶囊） ========== */
	.location-pill {
		display: flex;
		align-items: center;
		background: #fff;
		border-radius: 30rpx;
		padding: 10rpx 18rpx;
		box-shadow: 0 4rpx 14rpx rgba(11, 138, 136, 0.2);
		.u-icon:first-child {
			margin-right: 6rpx;
		}
		.u-icon:last-child {
			margin-left: 6rpx;
		}
		&:active { opacity: 0.85; }
	}

	.location-text {
		font-size: 24rpx;
		color: $primary-dark;
		font-weight: 600;
		max-width: 140rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.search-box {
		display: flex;
		align-items: center;
		height: 76rpx;
		background: #fff;
		border-radius: 38rpx;
		padding: 0 8rpx 0 24rpx;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
		.search-icon-wrap {
			display: flex;
			align-items: center;
			justify-content: center;
			margin-right: 12rpx;
		}
		.search-placeholder {
			flex: 1;
			font-size: 26rpx;
			color: $text-tip;
		}
		.search-btn {
			flex-shrink: 0;
			height: 60rpx;
			line-height: 60rpx;
			padding: 0 28rpx;
			background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
			color: #fff;
			font-size: 26rpx;
			font-weight: 600;
			border-radius: 30rpx;
			box-shadow: 0 4rpx 16rpx $primary-glow;
		}
	}

	/* ========== 内容区 ========== */
	.content-wrap {
		position: relative;
		z-index: 2;
		padding: 0 24rpx 40rpx;
		/* #ifdef MP-WEIXIN */
		padding-top: 252rpx;
		/* #endif */
		/* #ifdef APP */
		padding-top: 232rpx;
		/* #endif */
		/* #ifdef H5 */
		padding-top: 222rpx;
		/* #endif */
	}

	/* ========== 金刚区 + Banner ========== */
	.top-section {
		margin-bottom: 28rpx;
	}

	.category-card {
		background: $card-bg;
		border-radius: $radius-lg;
		padding: 32rpx 8rpx 16rpx;
		box-shadow: $shadow;
		margin-bottom: 24rpx;
		border: 1rpx solid rgba(255, 255, 255, 0.8);
	}

	.category-grid {
		display: flex;
		flex-wrap: wrap;
	}

	.category-item {
		width: 20%;
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 20rpx;
		&:active { transform: scale(0.92); transition: transform 0.15s; }
	}

	.category-icon-wrap {
		width: 96rpx;
		height: 96rpx;
		background: linear-gradient(145deg, $primary-lighter 0%, $primary-light 100%);
		border-radius: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 14rpx;
		border: 2rpx solid rgba(21, 202, 210, 0.15);
		box-shadow: 0 4rpx 16rpx $primary-shadow;
		overflow: hidden;
		.category-icon {
			width: 52rpx;
			height: 52rpx;
			border-radius: 16rpx;
		}
	}

	.category-title {
		font-size: 24rpx;
		color: $text-sub;
		font-weight: 500;
		line-height: 1.3;
		text-align: center;
	}

	.banner-wrap {
		border-radius: $radius-lg;
		overflow: hidden;
		box-shadow: $shadow-lg;
		border: 4rpx solid rgba(255, 255, 255, 0.6);
	}

	/* ========== 区块标题 ========== */
	.section-header {
		display: flex;
		align-items: center;
		margin-bottom: 24rpx;
	}

	.section-title-wrap {
		display: flex;
		align-items: baseline;
		.section-subtitle {
			margin-left: 12rpx;
		}
	}

	.section-title {
		font-size: 34rpx;
		font-weight: 800;
		color: $text-main;
		position: relative;
		padding-left: 22rpx;
		letter-spacing: 1rpx;
		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 8rpx;
			height: 32rpx;
			background: linear-gradient(180deg, $primary 0%, $primary-darker 100%);
			border-radius: 4rpx;
			box-shadow: 0 2rpx 8rpx $primary-glow;
		}
	}

	.section-subtitle {
		font-size: 22rpx;
		color: $primary-dark;
		background: $primary-soft;
		padding: 4rpx 14rpx;
		border-radius: 20rpx;
		font-weight: 500;
	}

	.title-line {
		flex: 1;
		height: 2rpx;
		background: linear-gradient(90deg, $border 0%, transparent 100%);
		margin-left: 20rpx;
	}

	/* ========== 租机流程 ========== */
	.flow-section {
		background: $card-bg;
		border-radius: $radius-lg;
		padding: 32rpx 28rpx;
		box-shadow: $shadow;
		margin-bottom: 28rpx;
		border: 1rpx solid $border;
	}

	.flow-track {
		position: relative;
		padding: 8rpx 0;
	}

	.flow-track-line {
		position: absolute;
		top: 38rpx;
		left: 12.5%;
		right: 12.5%;
		height: 4rpx;
		background: linear-gradient(90deg, $primary-light 0%, $primary 50%, $primary-light 100%);
		border-radius: 2rpx;
		z-index: 0;
	}

	.flow-steps {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		position: relative;
		z-index: 1;
	}

	.flow-step {
		display: flex;
		flex-direction: column;
		align-items: center;
		flex: 1;
		.step-circle {
			width: 64rpx;
			height: 64rpx;
			background: linear-gradient(145deg, $primary 0%, $primary-darker 100%);
			border-radius: 50%;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 16rpx;
			box-shadow: 0 6rpx 20rpx $primary-glow;
			border: 4rpx solid #fff;
			.step-num {
				font-size: 26rpx;
				color: #fff;
				font-weight: 700;
			}
		}
		.step-text {
			font-size: 24rpx;
			color: $text-sub;
			font-weight: 500;
			text-align: center;
			line-height: 1.3;
		}
	}

	/* ========== 热租爆品 ========== */
	.hot-section {
		background: linear-gradient(180deg, #fff 0%, $primary-lighter 100%);
		border-radius: $radius-lg;
		padding: 32rpx 0 32rpx 28rpx;
		box-shadow: $shadow;
		margin-bottom: 28rpx;
		border: 1rpx solid rgba(21, 202, 210, 0.12);
	}

	.section-header-hot {
		padding-right: 28rpx;
	}

	.hot-tabs {
		margin-bottom: 24rpx;
		.hot-tabs-inner {
			display: flex;
			padding-right: 28rpx;
		}
		.hot-tab {
			flex-shrink: 0;
			padding: 12rpx 28rpx;
			background: rgba(255, 255, 255, 0.8);
			color: $text-sub;
			font-size: 26rpx;
			border-radius: 32rpx;
			margin-right: 16rpx;
			font-weight: 500;
			border: 1rpx solid $border;
			transition: all 0.25s;
			&-active {
				background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
				color: #fff;
				border-color: transparent;
				box-shadow: 0 6rpx 20rpx $primary-glow;
				font-weight: 600;
			}
		}
	}

	.hot-goods-scroll {
		.hot-goods-list {
			display: flex;
			min-width: 100%;
			padding-right: 28rpx;
		}
		.hot-goods-item {
			flex: 1 0 228rpx;
			min-width: 228rpx;
			margin-right: 20rpx;
			background: #fff;
			border-radius: $radius-md;
			overflow: hidden;
			box-shadow: $shadow-sm;
			border: 1rpx solid $border;
			&:active { transform: scale(0.97); transition: transform 0.15s; }
			.hot-img-wrap {
				position: relative;
				width: 100%;
				height: 228rpx;
				background: $bg;
				.hot-img {
					width: 100%;
					height: 100%;
				}
				.hot-badge {
					position: absolute;
					top: 12rpx;
					left: 12rpx;
					background: linear-gradient(135deg, $accent 0%, #FF6B7A 100%);
					color: #fff;
					font-size: 20rpx;
					font-weight: 600;
					padding: 4rpx 14rpx;
					border-radius: 20rpx;
					box-shadow: 0 4rpx 12rpx rgba(255, 71, 87, 0.35);
				}
			}
			.hot-info {
				padding: 16rpx;
				.hot-title {
					font-size: 24rpx;
					color: $text-main;
					line-height: 1.45;
					margin-bottom: 10rpx;
					font-weight: 500;
				}
				.hot-price {
					display: inline-flex;
					align-items: baseline;
					background: $accent-soft;
					padding: 6rpx 12rpx;
					border-radius: 8rpx;
					.price-symbol {
						font-size: 22rpx;
						color: $accent;
						font-weight: 700;
					}
					.price-num {
						font-size: 34rpx;
						color: $accent;
						font-weight: 800;
						line-height: 1;
					}
					.price-unit {
						font-size: 22rpx;
						color: $text-tip;
						margin-left: 2rpx;
					}
				}
			}
		}
	}

	.hot-empty {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 48rpx 0;
		.empty-img {
			width: 200rpx;
			height: 140rpx;
			opacity: 0.5;
		}
		.empty-text {
			font-size: 26rpx;
			color: $text-tip;
			margin-top: 20rpx;
		}
	}

	/* ========== 筛选栏 ========== */
	.goods-section {
		position: relative;
	}

	.filter-bar {
		position: sticky;
		top: calc(var(--status-bar-height) + 200rpx);
		/* #ifdef MP-WEIXIN */
		top: calc(var(--status-bar-height) + 220rpx);
		/* #endif */
		z-index: 50;
		background: $bg;
		padding: 12rpx 0 20rpx;
		margin: 0 -24rpx;
		padding-left: 24rpx;
		padding-right: 24rpx;
	}

	.filter-inner {
		background: $card-bg;
		border-radius: $radius-md;
		padding: 8rpx;
		box-shadow: $shadow-sm;
		border: 1rpx solid $border;
	}

	.filter-segment {
		display: flex;
		align-items: center;
	}

	.filter-item {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		height: 64rpx;
		font-size: 26rpx;
		color: $text-sub;
		font-weight: 500;
		border-radius: $radius-sm;
		transition: all 0.2s;
		&.filter-active {
			color: $primary-dark;
			font-weight: 700;
			background: $primary-soft;
		}
	}

	.filter-dropdown {
		.u-icon {
			margin-left: 4rpx;
		}
	}

	.sort-arrows {
		display: flex;
		flex-direction: column;
		margin-left: 4rpx;
	}

	/* ========== 分类下拉面板 ========== */
	.dropdown-panel {
		position: absolute;
		top: 100%;
		left: 0;
		right: 0;
		z-index: 60;
		margin-top: 8rpx;
		.dropdown-content {
			background: $card-bg;
			border-radius: $radius-md;
			box-shadow: $shadow-lg;
			overflow: hidden;
			max-height: 520rpx;
			border: 1rpx solid $border;
		}
		.dropdown-scroll {
			max-height: 520rpx;
		}
		.dropdown-item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 28rpx 32rpx;
			border-bottom: 1rpx solid $primary-lighter;
			.dropdown-label {
				font-size: 28rpx;
				color: $text-sub;
			}
			&.dropdown-active {
				background: $primary-lighter;
				.dropdown-label {
					color: $primary-dark;
					font-weight: 700;
				}
			}
			&:active { background: $primary-light; }
		}
	}

	/* ========== 瀑布流商品 ========== */
	.waterfall-wrap {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
	}

	.waterfall-column {
		width: calc(50% - 10rpx);
	}

	.goods-card {
		background: $card-bg;
		border-radius: $radius-md;
		overflow: hidden;
		margin-bottom: 20rpx;
		box-shadow: $shadow-sm;
		border: 1rpx solid $border;
		transition: transform 0.2s, box-shadow 0.2s;
		&:active {
			transform: scale(0.98);
			box-shadow: $shadow;
		}
		.goods-img-wrap {
			width: 100%;
			background: linear-gradient(180deg, $primary-lighter 0%, $bg 100%);
			padding: 16rpx 16rpx 0;
			.goods-img {
				width: 100%;
				display: block;
				border-radius: $radius-sm $radius-sm 0 0;
			}
		}
		.goods-body {
			padding: 18rpx 16rpx 20rpx;
		}
		.goods-title {
			font-size: 26rpx;
			color: $text-main;
			line-height: 1.5;
			margin-bottom: 12rpx;
			font-weight: 600;
		}
		.goods-tags {
			margin-bottom: 14rpx;
			.tag {
				display: inline-block;
				font-size: 20rpx;
				color: $primary-darker;
				background: $primary-soft;
				padding: 6rpx 16rpx;
				border-radius: 6rpx;
				font-weight: 600;
				border: 1rpx solid rgba(21, 202, 210, 0.2);
			}
		}
		.goods-footer {
			display: flex;
			align-items: flex-end;
			justify-content: space-between;
			padding-top: 4rpx;
			border-top: 1rpx dashed $border;
			.goods-price {
				display: flex;
				align-items: baseline;
				padding-top: 12rpx;
				.price-symbol {
					font-size: 22rpx;
					color: $accent;
					font-weight: 700;
				}
				.price-num {
					font-size: 36rpx;
					color: $accent;
					font-weight: 800;
					line-height: 1;
				}
				.price-unit {
					font-size: 22rpx;
					color: $text-tip;
					margin-left: 2rpx;
				}
			}
			.goods-lease {
				font-size: 22rpx;
				color: $primary-dark;
				background: $primary-soft;
				padding: 4rpx 12rpx;
				border-radius: 6rpx;
				font-weight: 500;
			}
		}
	}

	::v-deep .u-indicator-item-round-active {
		background-color: $primary !important;
		width: 24rpx !important;
	}

	::v-deep .u-loadmore__content__text {
		color: $text-tip !important;
		font-size: 24rpx !important;
	}
</style>