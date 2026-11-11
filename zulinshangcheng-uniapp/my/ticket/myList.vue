<template>
	<view class="ticket-page">
		<!-- 顶部品牌渐变背景 -->
		<view class="page-hero">
			<view class="hero-glow hero-glow--a"></view>
			<view class="hero-glow hero-glow--b"></view>
		</view>

		<view class="page-body">
			<!-- 状态切换 -->
			<view class="tab-card">
				<view class="tab-track">
					<view v-for="(item, index) in list" :key="index"
						:class="tabIndex == index ? 'tab-item tab-item--active' : 'tab-item'" @click="change(index)">
						{{ item.name }}
					</view>
				</view>
			</view>

			<u-checkbox-group @change="checkboxGroupChange" v-if="tabIndex == 1000">
				<u-checkbox @change="checkboxChange" shape="circle" v-model="item.checkbox" v-for="(item, index) in ticketList"
					:key="index" :name="item.name" active-color="#FD3C44">
					<view class="flex align-start justify-between" style="width: 100%;">
						<view>
							<view style="color: #000000;font-size: 30upx;">{{ item.couponName }}</view>
							<view style="color: #999999;font-size: 24upx;margin-top: 10upx;" v-if="item.failureTime">
								有效期至{{ item.failureTime }}</view>
							<view style="color: #999999;font-size: 24upx;margin-top: 10upx;" v-else>永久有效</view>
						</view>
						<view style="color: #FD3C44;font-size: 30upx;">¥<text style="font-size: 48upx;font-weight: bold;">{{ item.lessMoney
						  }}</text></view>
					</view>
					<view style="width: 100%;border-top:1rpx dashed #E6E6E6;margin: 30upx 0upx;"></view>
					<view class="flex align-center justify-between">
						<view v-if="item.minMoney">满{{ item.minMoney }}元可用</view>
						<view v-else>无门槛使用</view>
						<!-- <view class="btn" @click="shiyong(item)" v-if="item.status == 0">立即使用</view> -->
					</view>
				</u-checkbox>
			</u-checkbox-group>

			<!-- 优惠券卡片 -->
			<view class="ticket-card" v-for="(item, index) in ticketList" :key="index">
				<view class="ticket-main">
					<view :class="tabIndex == 0 ? 'ticket-left' : 'ticket-left ticket-left--disabled'">
						<view class="ticket-amount">¥<text class="ticket-num">{{ item.lessMoney }}</text></view>
						<view class="ticket-condition" v-if="item.minMoney">满{{ item.minMoney }}元可用</view>
						<view class="ticket-condition" v-else>无门槛使用</view>
					</view>
					<view class="ticket-divider"></view>
					<view class="ticket-right">
						<view class="ticket-name">{{ item.couponName }}</view>
						<view class="ticket-date" v-if="item.failureTime">有效期至{{ item.failureTime }}</view>
						<view class="ticket-date" v-else>永久有效</view>
						<view class="ticket-actions">
							<view class="ticket-btn ticket-btn--plain" @click="noShiyong(item)"
								v-if="item.status == 1 && couponId == item.couponUserId">取消使用</view>
							<view class="ticket-btn" @click="shiyong(item)"
								v-if="item.status == 1 && couponId != item.couponUserId">立即使用</view>
						</view>
					</view>
				</view>
			</view>

			<empty v-if="ticketList&&ticketList.length == 0" height="420rpx" top="40rpx" content="暂无数据"></empty>
		</view>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	import {
		mapState
	} from 'vuex'
	export default {
		components: {
			empty,
		},
		data() {
			return {
				valueIndex: [],
				price: 0,
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
				page: 1,
				size: 10,
				ticketList: [],
				count: 0,
				price: '',
				couponId: '', //已选中的优惠券id
				status: 'loadmore',
				loadText: {
					loadmore: '轻轻上拉',
					loading: '努力加载中',
					nomore: '实在没有了'
				},
				countPage: ''
			}
		},
		onShow() {},
		onLoad(option) {
			this.price = option.price
			if (option.couponId) {
				this.couponId = option.couponId
			}
			console.log(this.couponId,"this.couponId")
			this.getMyList()
		},
		methods: {
			checkboxChange(n) {
				let index = n.name
				if (this.price < this.ticketList[index].minMoney) {
					uni.showToast({
						title: '未达到优惠券满减金额',
						icon: 'none'
					})
				} else if (this.price > 0) {
					uni.$emit('getDiscountId', {
						discountId: this.ticketList[index].couponUserId
					})
					uni.navigateBack()
					this.$emit('countMoney', this.ticketList[index].money)
					// this.$store.commit('goodModule/setDiscountAmount', this.ticketList[index].money)
				}
			},
			// 选中任一checkbox时，由checkbox-group触发
			checkboxGroupChange(e) {
				console.log(e, "xx");
			},
			noShiyong(item) {
				uni.$emit('getDiscountId', {
					discountId: '',
					price: ''
				})
				uni.navigateBack()
			},
			shiyong(item) {
				console.log(this.price,"this.price")
				console.log(item.minMoney,"item.minMoney")
				if (Number(this.price) < Number(item.minMoney)) {
					uni.showToast({
						title: '未达到优惠券满减金额',
						icon: 'none'
					})
				} else {
					uni.$emit('getDiscountId', {
						discountId: item.couponUserId,
						price: item.lessMoney
					})
					uni.navigateBack()
				}
			},
			change(index) {
				this.tabIndex = index
				this.page = 1;
				this.getMyList()
			},
			// 获取我的优惠券列表
			getMyList() {
				let data = {
					page: this.page,
					size: this.size,
					userId: uni.getStorageSync('userId'),
					// type: 1,
					status: this.tabIndex + 1
				}
				this.$Request.getT('/app/selfCouponUser/userList', data).then(res => {
					if (res.status == 0) {
						this.countPage = res.data.pages
						if (this.page == this.countPage) {
							this.status = 'nomore'
						} else {
							this.status = 'loadmore'
						}
						if (this.page == 1) {
							this.ticketList = res.data.records
						} else {
							this.ticketList = [...this.ticketList, ...res.data.records]
						}
						this.commitotal = res.data.total
						if (this.ticketList.length > 0) {
							let data = this.ticketList
							for (var i = 0; i < data.length; i++) {
								if (data[i].failureTime) {
									data[i].failureTime = data[i].failureTime.substring(0, 10)
								}
							}
						}
					}
				})
			},
		},
		onPullDownRefresh() {
			this.page = 1
			this.getMyList()
		},
		onReachBottom() {
			if (this.page < this.countPage) {
				this.page += 1
				this.status = 'loading'
				this.getMyList()
			} else {
				this.status = 'nomore'
			}
		},
		// watch: {
		// 	tabIndex: {
		// 		immediate: true,
		// 		handler(newv) {
		// 			this.getMyList()
		// 		}
		// 	}
		// }
	}
</script>

<style lang="scss" scoped>
	page {
		background: linear-gradient(180deg, #D8F0EF 0%, #E4F4F2 28%, #EDF7F4 55%, #F3F9F7 100%);
	}

	.ticket-page {
		position: relative;
		min-height: 100vh;
		padding-bottom: 60rpx;
	}

	// 顶部品牌渐变横幅
	.page-hero {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 150rpx;
		background: linear-gradient(180deg, #16CAD1 0%, #10B0B3 38%, #46C7C4 66%, #9FE2DE 85%, #D8F0EF 100%);
		overflow: hidden;
	}

	.hero-glow {
		position: absolute;
		border-radius: 50%;
	}

	.hero-glow--a {
		width: 340rpx;
		height: 340rpx;
		right: -90rpx;
		top: -130rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.26) 0%, rgba(255, 255, 255, 0) 70%);
	}

	.hero-glow--b {
		width: 240rpx;
		height: 240rpx;
		left: -70rpx;
		top: 110rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.16) 0%, rgba(255, 255, 255, 0) 70%);
	}

	.page-body {
		position: relative;
		z-index: 1;
		padding-top: 20rpx;
	}

	// 状态切换卡
	.tab-card {
		margin: 0 30rpx;
		border-radius: 999rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		box-shadow: 0 16rpx 44rpx rgba(11, 148, 155, 0.08);
		padding: 10rpx;
		position: relative;
		z-index: 2;
	}

	.tab-track {
		display: flex;
		background: #E7F1EE;
		border-radius: 999rpx;
		padding: 8rpx;
	}

	.tab-item {
		flex: 1;
		height: 68rpx;
		border-radius: 999rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 26rpx;
		color: #4A5858;
		font-weight: 500;
		transition: all 0.2s ease;
	}

	.tab-item--active {
		background: linear-gradient(135deg, #1FD8DE 0%, #0EB5BC 100%);
		color: #FFFFFF;
		font-weight: 700;
		box-shadow: 0 8rpx 18rpx rgba(14, 181, 188, 0.32);
	}

	// 优惠券卡片
	.ticket-card {
		margin: 22rpx 30rpx 0;
		border-radius: 32rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		box-shadow: 0 16rpx 44rpx rgba(11, 148, 155, 0.07);
		overflow: hidden;
	}

	.ticket-main {
		display: flex;
		align-items: stretch;
	}

	// 左侧金额区
	.ticket-left {
		width: 208rpx;
		background: linear-gradient(150deg, #1FD8DE 0%, #0EB5BC 100%);
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 38rpx 0;
		flex-shrink: 0;
	}

	.ticket-left--disabled {
		background: linear-gradient(150deg, #C3D2CF 0%, #9DB3AF 100%);
	}

	.ticket-amount {
		color: #FFFFFF;
		font-size: 28rpx;
		font-weight: 600;
		display: flex;
		align-items: baseline;
	}

	.ticket-num {
		font-size: 58rpx;
		font-weight: 800;
	}

	.ticket-condition {
		font-size: 20rpx;
		color: rgba(255, 255, 255, 0.88);
		margin-top: 10rpx;
	}

	// 打孔分割线
	.ticket-divider {
		width: 0;
		border-left: 2rpx dashed rgba(21, 202, 210, 0.3);
		margin: 28rpx 0;
		position: relative;

		&::before,
		&::after {
			content: '';
			position: absolute;
			left: -14rpx;
			width: 26rpx;
			height: 26rpx;
			border-radius: 50%;
			background: #D9EEEA;
		}

		&::before {
			top: -42rpx;
		}

		&::after {
			bottom: -42rpx;
		}
	}

	// 右侧信息区
	.ticket-right {
		flex: 1;
		padding: 32rpx 28rpx;
		display: flex;
		flex-direction: column;
	}

	.ticket-name {
		font-size: 30rpx;
		font-weight: 700;
		color: #1A2323;
		line-height: 1.4;
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 1;
	}

	.ticket-date {
		font-size: 22rpx;
		color: #9AAEAC;
		margin-top: 14rpx;
	}

	.ticket-actions {
		margin-top: auto;
		padding-top: 22rpx;
		display: flex;
		justify-content: flex-end;
	}

	.ticket-btn {
		font-size: 24rpx;
		font-weight: 600;
		color: #FFFFFF;
		background: linear-gradient(135deg, #1FD8DE 0%, #0EB5BC 100%);
		border-radius: 999rpx;
		padding: 10rpx 30rpx;
		box-shadow: 0 8rpx 18rpx rgba(14, 181, 188, 0.28);
	}

	.ticket-btn--plain {
		background: #FBEFEE;
		color: #D66B6B;
		box-shadow: none;
	}

	// 选择模式（兼容原 checkbox 结构）
	::v-deep .u-checkbox-group {
		width: 100%;
	}

	::v-deep .u-checkbox {
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		width: calc(100% - 60rpx) !important;
		margin: 24rpx auto;
		border-radius: 28rpx;
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		box-shadow: 0 16rpx 44rpx rgba(11, 148, 155, 0.07);
		padding: 26rpx;
		box-sizing: border-box;
	}

	::v-deep .u-checkbox__label {
		width: 100%;
		padding-bottom: 20rpx;
	}

	::v-deep .u-checkbox__icon-wrap {
		margin: 0 15rpx;
	}
</style>
