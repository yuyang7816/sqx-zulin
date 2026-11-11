<template>
	<view class="addr-page">
		<!-- 顶部品牌渐变背景 -->
		<view class="page-hero">
			<view class="hero-glow hero-glow--a"></view>
			<view class="hero-glow hero-glow--b"></view>
		</view>

		<!-- 氛围光斑 -->
		<view class="ambient ambient--a"></view>
		<view class="ambient ambient--b"></view>

		<view class="page-body">
			<view class="address_list" v-if="list.length">
				<view class="address_item" v-for="(item, index) in list" :key="index">
					<view class="addr-main" @click="choose && chooseThis(item)">
						<view class="addr-head">
							<view class="addr-name">{{ item.userName }}</view>
							<view class="addr-phone">{{ item.userPhone }}</view>
							<view class="addr-default-tag" v-if="item.addressDefault">默认</view>
						</view>
						<view class="addr-detail">
							<view class="addr-text">{{ item.province }}{{ item.city }}{{ item.district }}{{ item.addressDetail }}</view>
							<view class="addr-edit" @click.stop="navigate('/my/address/add?addressId='+item.addressId)">
								<u-icon name="edit-pen" size="28" color="#0E8A8F"></u-icon>
							</view>
						</view>
					</view>
					<view class="operate_bar">
						<u-radio-group :value="item.addressDefault ? `${index}` : ''" @change="changeDefault($event,item)">
							<u-radio label-size="26rpx" active-color="#15CAD2" :name="`${index}`">当前默认地址</u-radio>
						</u-radio-group>
						<view class="addr-del" @click="showDialog(item.addressId)">
							<u-icon name="trash" size="26" color="#D66B6B"></u-icon>
							<text>删除</text>
						</view>
					</view>
				</view>
			</view>
			<empty v-if="list.length == 0"></empty>
		</view>

		<!-- 底部固定操作栏 -->
		<view class="footer_bar">
			<view class="add_btn" hover-class="btn-hover" @click="navigate('/my/address/add')">
				<u-icon name="plus" size="30" color="#FFFFFF"></u-icon>
				<text>添加地址</text>
			</view>
		</view>
	</view>
</template>


<script>
	import empty from "@/components/empty.vue"
	export default {
		components: {
			empty
		},
		data() {
			return {
				choose: false,
				value: '',
				page: 1,
				limit: 10,
				list: [],
				show: false,
				addressId: null,
				status: 'loadmore',
				loadText: {
					loadmore: '轻轻上拉',
					loading: '努力加载中',
					nomore: '实在没有了'
				},
				countPage: ''
			}
		},
		onLoad(option) {
			if (option.index) {
				this.index = option.index
			}
			if (option.choose === 'choose') {
				this.choose = true
			}
		},
		onShow() {
			this.page = 1
			this.getAddlist()
		},
		methods: {
			// 修改默认地址
			changeDefault(e, item) {
				console.log(e, '11111111111')
				console.log(item, '22222222222')
				let userId = this.$queue.getData('userId');
				let data = {
					userName: item.userName,
					userPhone: item.userPhone,
					addressDetail: item.addressDetail,
					lat: item.lat,
					lng: item.lng,
					province: item.province,
					city: item.city,
					district: item.district,
					userId: userId,
					addressDefault: item.addressDefault == 0 ? 1 : 0,
					addressId: item.addressId,
				}
				this.$Request.postT('/app/address/saveAddress', data).then(res => {
					if (res.code == 0) {
						uni.hideLoading()
						this.$queue.showToast("修改成功!");
						if (data.isDefault) {
							uni.setStorageSync('address', data)
						}
						setTimeout(d => {
							uni.navigateBack();
						}, 1000)
					} else {
						uni.hideLoading()
						this.$queue.showToast(res.msg);
					}
				});
			},
			bindhelp(item) {
				let userId = this.$queue.getData('userId');
				let data = {
					userName: this.name,
					userPhone: this.mobile,
					addressDetail: this.detailaddress,
					lat: this.latitude,
					lng: this.longitude,
					province: this.province,
					city: this.city,
					district: this.district,
					userId: userId,
					addressId: this.id,
					addressDefault: this.isDefault,
				}
				// console.log( data,' this.latitude')
				// return
				this.$Request.postT('/app/address/saveAddress', data).then(res => {
					if (res.code == 0) {
						uni.hideLoading()
						this.$queue.showToast("添加成功!");
						if (data.isDefault) {
							uni.setStorageSync('address', data)
						}
						setTimeout(d => {
							uni.navigateBack();
						}, 1000)
					} else {
						uni.hideLoading()
						this.$queue.showToast(res.msg);
					}
				});
			},
			cancelBtn() {
				this.show = false
			},
			showDialog(addressId) {
				this.addressId = addressId
				let that = this;
				uni.showModal({
					title: '',
					content: '确定要删除该地址吗？',
					success: function(res) {
						if (res.confirm) {
							that.delAddress()
							console.log('用户点击确定');
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
				console.log(this.show, 'this.show')
			},
			// 删除
			delAddress() {
				this.$Request.postT('/app/address/deleteAddress', {
					addressId: this.addressId
				}).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '删除成功',
							icon: 'success'
						})
						this.page = 1
						this.show = false
						setTimeout(() => {
							this.getAddlist()
						}, 1500)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			radioChange(index, e) {
				return
			},
			chooseThis(address) {
				uni.$emit('getUserAddress', address)
				uni.navigateBack()
			},
			openMap() { //打开地图选择位置
				let that = this
				uni.chooseLocation({
					success: function(res) {
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
					}
				});
			},
			getAddlist(page) {
				this.$Request.getT('/app/address/getAddressList', {
					page: this.page,
					limit: this.limit
				}).then(res => {
					this.countPage = res.data.pages
					if (this.page == this.countPage) {
						this.status = 'nomore'
					} else {
						this.status = 'loadmore'
					}
					if (this.page == 1) {
						this.list = res.data.data.records
					} else {
						this.list = [...this.list, ...res.data.data.records]
					}
					this.page++
				})
			}
		},
		watch: {
			show(newv) {
				if (!newv) {
					this.addressId = null
				}
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getAddlist()
		},
		onReachBottom() {
			if (this.page < this.countPage) {
				this.page += 1
				this.status = 'loading'
				this.getAddlist()
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

	.addr-page {
		position: relative;
		min-height: 100vh;
		padding-bottom: 220rpx;
	}

	// 顶部品牌渐变横幅
	.page-hero {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 420rpx;
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

	// 氛围光斑
	.ambient {
		position: absolute;
		border-radius: 50%;
		z-index: 0;
	}

	.ambient--a {
		width: 420rpx;
		height: 420rpx;
		right: -160rpx;
		top: 860rpx;
		background: radial-gradient(circle, rgba(23, 205, 212, 0.1) 0%, rgba(23, 205, 212, 0) 70%);
	}

	.ambient--b {
		width: 460rpx;
		height: 460rpx;
		left: -180rpx;
		top: 1800rpx;
		background: radial-gradient(circle, rgba(14, 168, 168, 0.08) 0%, rgba(14, 168, 168, 0) 70%);
	}

	.page-body {
		position: relative;
		z-index: 1;
	}

	.address_list {
		padding-top: 30rpx;
	}

	// 地址卡片
	.address_item {
		margin: 0 30rpx 26rpx;
		border-radius: 32rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		box-shadow: 0 16rpx 44rpx rgba(11, 148, 155, 0.07);
		padding: 32rpx 30rpx 24rpx;
		overflow: hidden;
	}

	.addr-head {
		display: flex;
		align-items: center;
	}

	.addr-name {
		font-size: 32rpx;
		font-weight: 700;
		color: #1A2323;
	}

	.addr-phone {
		font-size: 26rpx;
		color: #8A9A9A;
		margin-left: 22rpx;
	}

	.addr-default-tag {
		margin-left: auto;
		font-size: 20rpx;
		color: #FFFFFF;
		background: linear-gradient(135deg, #1FD8DE 0%, #0EB5BC 100%);
		border-radius: 999rpx;
		padding: 5rpx 20rpx;
		font-weight: 600;
		letter-spacing: 1rpx;
		flex-shrink: 0;
	}

	.addr-detail {
		display: flex;
		align-items: center;
		margin-top: 18rpx;
	}

	.addr-text {
		flex: 1;
		font-size: 27rpx;
		color: #4A5858;
		line-height: 1.6;
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
	}

	.addr-edit {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background: #E9F2EF;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-left: 22rpx;
		flex-shrink: 0;
	}

	// 底部操作条
	.operate_bar {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 24rpx;
		padding-top: 22rpx;
		border-top: 2rpx dashed rgba(21, 202, 210, 0.18);
	}

	.addr-del {
		display: flex;
		align-items: center;
		gap: 8rpx;
		font-size: 24rpx;
		color: #D66B6B;
		background: #FBEFEE;
		border-radius: 999rpx;
		padding: 8rpx 24rpx;
		flex-shrink: 0;
	}

	::v-deep .u-radio__label {
		color: #4A5858;
	}

	::v-deep .u-radio__icon-wrap--disabled {
		background: transparent !important;
	}

	// 底部固定操作栏
	.footer_bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: #FFFFFF;
		padding: 20rpx 30rpx calc(20rpx + constant(safe-area-inset-bottom));
		padding: 20rpx 30rpx calc(20rpx + env(safe-area-inset-bottom));
		box-shadow: 0 -12rpx 40rpx rgba(11, 148, 155, 0.08);
		z-index: 99;
	}

	.add_btn {
		height: 92rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #1FD8DE 0%, #0EB5BC 100%);
		box-shadow: 0 14rpx 30rpx rgba(14, 181, 188, 0.35);
		color: #FFFFFF;
		font-size: 30rpx;
		font-weight: 700;
		letter-spacing: 2rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 12rpx;
	}

	.btn-hover {
		opacity: 0.88;
	}
</style>
