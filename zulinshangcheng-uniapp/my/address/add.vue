<template>
	<view class="add-page">
		<view class="page-bg">
			<view class="page-glow page-glow--left"></view>
			<view class="page-glow page-glow--right"></view>
		</view>

		<view class="page-body">
			<view class="form-card">
				<view class="card-header">
					<view class="header-left">
						<view class="header-bar"></view>
						<text class="header-title">{{ id ? '编辑地址' : '新增地址' }}</text>
					</view>
					<text class="header-desc">填写后可直接下单使用</text>
				</view>

				<view class="field-row">
					<view class="field-main">
						<view class="field-label">姓名</view>
						<view class="field-tip">收货人</view>
					</view>
					<u-input placeholder="请填写姓名" :border="false" v-model="name" />
				</view>
				<view class="field-row">
					<view class="field-main">
						<view class="field-label">联系电话</view>
						<view class="field-tip">用于联系收货</view>
					</view>
					<u-input placeholder="请填写联系电话" :border="false" v-model="mobile" />
				</view>
				<view class="field-row field-row--picker" @click="bindmap">
					<view class="field-main">
						<view class="field-label">选择地址</view>
					</view>
					<view class="picker-value picker-placeholder" v-if="!cityaddress">请选择收货地址（省、市、区）</view>
					<view class="picker-value" v-else>{{cityaddress}}</view>
					<view class="picker-arrow">
						<u-icon name="arrow-right" size="24" color="#0FA8AD"></u-icon>
					</view>
				</view>
				<view class="field-block">
					<view class="field-block__head">
						<view class="field-label">详细地址</view>
						<view class="field-tip">街道、门牌号、小区楼栋</view>
					</view>
					<view class="textarea-wrap">
						<u-input v-model="detailaddress" type="textarea" placeholder="例如街道,门牌号,小区,楼栋号等"
							:border="false" height="100" :auto-height="true" />
					</view>
				</view>
			</view>

			<view class="switch-card">
				<view class="switch-info">
					<view class="switch-title">设为默认地址</view>
					<view class="switch-desc">下单时将优先使用该地址</view>
				</view>
				<u-switch v-model="value" active-color="#15CAD2"></u-switch>
			</view>
		</view>

		<view class="footer_bar">
			<view class="footer-main">
				<view class="submit-btn" hover-class="btn-hover" @click="bindhelp">确定</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				// provinces: '选择地址',
				// consignee: '',
				// mobile: '',
				// detail: '',
				// createAt: '',
				id: '',
				isDefault: 0,
				// id: 0,
				mobile: '',
				name: '',
				cityaddress: '',
				detailaddress: '',
				latitude: '',
				longitude: '',
				province: '',
				city: '',
				district: '',
				value: false
			}
		},
		onLoad(d) {
			let id = d.addressId;
			if (id) {
				this.id = d.addressId;
				uni.setNavigationBarTitle({
					title: '编辑地址'
				});
				this.getAddressList(id);
			} else {
				uni.setNavigationBarTitle({
					title: '添加地址'
				});
			}
		},
		methods: {
			change(data) {
				this.provinces = data.data.join('')
			},
			bindmap() {
				var that = this
				console.log("bindmapbindmap")
				uni.chooseLocation({
					success: function(res) {
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
						that.detailaddress = res.name
						that.latitude = res.latitude
						that.longitude = res.longitude
						that.shengcheng(res.longitude, res.latitude)
					}
				})
			},
			shengcheng(longitude, latitude) {
				this.$Request.getT('/app/Login/selectCity', {
					lat: latitude,
					lng: longitude
				}).then(res => {
					if (res.code == 0) {
						this.province = res.data.data.province
						this.city = res.data.data.city
						this.district = res.data.data.district
						this.cityaddress = res.data.data.province + res.data.data.city + res.data.data.district
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						});
					}
				})
			},
			getAddressList(id) {
				uni.showLoading({
					title: '加载中...'
				});
				this.$Request.getT('/app/address/getAddressInfo', {
					addressId: id
				}).then(res => {
					this.name = res.data.userName ? res.data.userName : '';
					this.mobile = res.data.userPhone ? res.data.userPhone : '';
					this.cityaddress = res.data.province + res.data.city + res.data.district
					this.detailaddress = res.data.addressDetail;
					this.isDefault = res.data.addressDefault;
					this.province = res.data.province
					this.city = res.data.city
					this.district = res.data.district
					this.userId = res.data.userId;
					this.latitude = res.data.lat;
					this.longitude = res.data.lng;
					this.value = !!this.isDefault
					console.log(res.data.lat, "dddddddddddddddddddd")
					uni.hideLoading();
				})
			},
			bindhelp() {
				if (this.id) {
					this.$queue.showLoading('修改中...')
				} else {
					this.$queue.showLoading('添加中...')
				}

				if (this.mobile.length < 11) {
					uni.hideLoading();
					uni.showToast({
						icon: 'none',
						title: '请输入正确的电话信息'
					});
					return;
				}
				if (this.name != undefined && this.name != '') {} else {
					uni.hideLoading();
					uni.showToast({
						icon: 'none',
						title: '姓名不能为空'
					});
					return;
				}
				if (this.cityaddress == '选择地址') {
					uni.hideLoading();
					uni.showToast({
						icon: 'none',
						title: '请选择地址信息'
					});
					return;
				}
				if (this.detailaddress != undefined && this.detailaddress != '') {} else {
					uni.hideLoading();
					uni.showToast({
						icon: 'none',
						title: '地址信息不能为空'
					});
					return;
				}
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
			//校验手机格式
			checkMobile(mobile) {
				return RegExp(/^1[34578]\d{9}$/).test(mobile);
			},
			switch1Change(e) {
				this.isDefault = e.detail.value == true ? 1 : 0;
			}
		},
		watch: {
			value(newv) {
				this.isDefault = ~~newv
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background: #F5F8FB;
	}

	.add-page {
		position: relative;
		min-height: 100vh;
		padding-bottom: 260rpx;
	}

	.page-bg {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 460rpx;
		background: linear-gradient(180deg, #17CBD2 0%, #11B2B8 38%, #64D5D1 74%, #EAF7F6 100%);
		overflow: hidden;
	}

	.page-glow {
		position: absolute;
		border-radius: 50%;
	}

	.page-glow--left {
		width: 320rpx;
		height: 320rpx;
		left: -120rpx;
		top: 120rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.22) 0%, rgba(255, 255, 255, 0) 74%);
	}

	.page-glow--right {
		width: 380rpx;
		height: 380rpx;
		right: -130rpx;
		top: -140rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0) 72%);
	}

	.page-body {
		position: relative;
		z-index: 1;
		padding: 48rpx 30rpx 0;
	}

	.form-card {
		margin-top: 0;
		border-radius: 28rpx;
		background: #FFFFFF;
		box-shadow: 0 16rpx 46rpx rgba(22, 44, 74, 0.08);
		padding: 10rpx 32rpx 36rpx;
		position: relative;
		z-index: 2;
	}

	.card-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx 0 10rpx;
	}

	.header-left {
		display: flex;
		align-items: center;
	}

	.header-bar {
		width: 8rpx;
		height: 28rpx;
		border-radius: 999rpx;
		background: linear-gradient(180deg, #15CAD2 0%, #0EA7AF 100%);
		margin-right: 14rpx;
	}

	.header-title {
		font-size: 32rpx;
		font-weight: 700;
		color: #1A2233;
	}

	.header-desc {
		font-size: 22rpx;
		color: #8C97A8;
		white-space: nowrap;
	}

	.field-row {
		display: flex;
		align-items: center;
		padding: 34rpx 0;
		border-bottom: 1rpx solid #EEF2F6;
		gap: 24rpx;
	}

	.field-main {
		width: 170rpx;
		flex-shrink: 0;
	}

	.field-label {
		font-size: 30rpx;
		font-weight: 700;
		color: #1A2233;
	}

	.field-tip {
		margin-top: 8rpx;
		font-size: 22rpx;
		line-height: 1.4;
		color: #93A0B3;
		white-space: nowrap;
	}

	.field-row--picker {
		.picker-value {
			flex: 1;
			font-size: 28rpx;
			color: #1A2233;
			line-height: 1.5;
		}

		.picker-placeholder {
			color: #A0AABD;
			white-space: nowrap;
		}
	}

	.picker-arrow {
		width: 56rpx;
		height: 56rpx;
		border-radius: 18rpx;
		background: #ECF8F8;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.field-block {
		padding-top: 32rpx;

		.field-label {
			width: auto;
		}
	}

	.field-block__head {
		display: flex;
		align-items: flex-end;
		justify-content: space-between;
		margin-bottom: 22rpx;
		gap: 20rpx;

		.field-tip {
			margin-top: 0;
		}
	}

	.textarea-wrap {
		background: #F6F9FC;
		border-radius: 20rpx;
		padding: 24rpx 26rpx;
		border: 1rpx solid #ECF1F6;
	}

	::v-deep .u-input {
		flex: 1;
		margin-bottom: 0 !important;
	}

	::v-deep .u-input__input {
		font-size: 28rpx;
		color: #1A2233;
	}

	::v-deep .u-textarea {
		background: transparent;
		padding: 0;
	}

	::v-deep .u-input__textarea {
		font-size: 28rpx;
		color: #1A2233;
	}

	.switch-card {
		margin-top: 28rpx;
		border-radius: 28rpx;
		background: #FFFFFF;
		box-shadow: 0 16rpx 46rpx rgba(22, 44, 74, 0.07);
		padding: 30rpx 32rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.switch-title {
		font-size: 30rpx;
		font-weight: 700;
		color: #1A2233;
	}

	.switch-desc {
		font-size: 22rpx;
		color: #93A0B3;
		margin-top: 10rpx;
		white-space: nowrap;
	}

	.footer_bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background: rgba(255, 255, 255, 0.96);
		backdrop-filter: blur(16rpx);
		padding: 22rpx 30rpx calc(22rpx + constant(safe-area-inset-bottom));
		padding: 22rpx 30rpx calc(22rpx + env(safe-area-inset-bottom));
		box-shadow: 0 -12rpx 36rpx rgba(22, 44, 74, 0.08);
		z-index: 99;
	}

	.footer-main {
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.submit-btn {
		width: 100%;
		max-width: 690rpx;
		height: 92rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #1FD8DE 0%, #0EB5BC 100%);
		box-shadow: 0 14rpx 30rpx rgba(14, 181, 188, 0.35);
		color: #FFFFFF;
		font-size: 30rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.btn-hover {
		opacity: 0.88;
	}
</style>
