<template>
	<view class="userinfo-page">
		<view class="page-bg">
			<view class="page-glow page-glow-left"></view>
			<view class="page-glow page-glow-right"></view>
		</view>

		<view class="profile-card">
			<view class="profile-card__shine"></view>
			<view class="profile-main">
				<view class="profile-copy">
					<text class="profile-title">个人信息</text>
					<text class="profile-subtitle">维护你的基础资料与账号展示信息</text>
				</view>
				<view class="profile-avatar-wrap">
					<!-- #ifdef MP-WEIXIN -->
					<button class="avatar-button" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
						<image class="profile-avatar" :src="avatar" mode="aspectFill"></image>
						<view class="avatar-edit-badge">
							<u-icon name="camera-fill" size="20" color="#FFFFFF"></u-icon>
						</view>
					</button>
					<!-- #endif -->
					<!-- #ifndef MP-WEIXIN -->
					<view class="avatar-button" @click="uploadImg()">
						<image class="profile-avatar" :src="avatar" mode="aspectFill"></image>
						<view class="avatar-edit-badge">
							<u-icon name="camera-fill" size="20" color="#FFFFFF"></u-icon>
						</view>
					</view>
					<!-- #endif -->
				</view>
			</view>
		</view>

		<view class="form-card">
			<view class="card-header">
				<view class="header-left">
					<view class="header-bar"></view>
					<text class="header-title">基础资料</text>
				</view>
				<text class="header-desc">保存后将同步更新个人资料</text>
			</view>

			<view class="form-list">
				<view class="field-item">
					<view class="field-head">
						<text class="field-label">用户名</text>
					</view>
					<view class="field-control">
						<input class="field-input" type="nickname" v-model="userName" placeholder="请输入用户名" placeholder-class="field-placeholder" />
					</view>
				</view>

				<view class="field-item">
					<view class="field-head">
						<text class="field-label">年龄</text>
					</view>
					<view class="field-control">
						<input class="field-input" type="number" v-model="age" placeholder="请输入年龄" placeholder-class="field-placeholder" />
					</view>
				</view>

				<view class="field-item">
					<view class="field-head">
						<text class="field-label">手机</text>
						<text class="field-tip">已绑定</text>
					</view>
					<view class="field-control field-control--disabled">
						<input class="field-input field-input--disabled" v-model="phone" placeholder="请输入联系电话" :disabled="true" placeholder-class="field-placeholder" />
					</view>
				</view>

				<view class="field-item field-item--gender">
					<view class="field-head">
						<text class="field-label">性别</text>
					</view>
					<view class="gender-panel">
						<u-radio-group v-model="sex" class="gender-group">
							<view class="gender-option" :class="{'gender-option--active': Number(sex) === 1}" @click="sex = 1">
								<u-radio shape="circle" :name="1" active-color="#15CAD2">男</u-radio>
							</view>
							<view class="gender-option" :class="{'gender-option--active': Number(sex) === 2}" @click="sex = 2">
								<u-radio shape="circle" :name="2" active-color="#15CAD2">女</u-radio>
							</view>
						</u-radio-group>
					</view>
				</view>
			</view>
		</view>

		<view class="submit-bar">
			<view class="submit-btn" @click="messagebtn()">保存信息</view>
		</view>

		<view class="safe-bottom"></view>
	</view>
</template>

<script>
	import configdata from '../../common/config.js';
	export default {
		data() {
			return {
				phone: '',
				avatar: '../../static/logo.png',
				userName: '',
				nickName: '',
				userId: '',
				realName: '',
				weChatId: "",
				password: '',
				platform: '',
				createTime: '',
				money: '',
				jiFen: '',
				status: '',
				zhiFuBao: '',
				zhiFuBaoName: '',
				sex: 1,
				age: 0
			};
		},
		onLoad(e) {
			this.getUserInfo()
			// this.avatar = uni.getStorageSync('avatar')
		},
		methods: {
			uploadImg() {
				let token = uni.getStorageSync('token')

				if (!token) {
					this.goLoginInfo();
					return;
				}
				let that = this;
				var url = null;
				uni.showActionSheet({
					// itemList按钮的文字接受的是数组
					itemList: ["查看头像", "从相册选择图片"],
					success(e) {
						var index = e.tapIndex
						if (index === 0) {
							// 用户点击了预览当前图片
							// 可以自己实现当前头像链接的读取
							let url = that.avatar;
							let arr = []
							arr.push(url)
							uni.previewImage({
								// 预览功能图片也必须是数组的
								urls: arr
							})
						} else if (index === 1) {
							uni.chooseImage({
								count: 1, //默认9
								sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
								sourceType: ['album'], //从相册选择
								success: function(res) {
									uni.showLoading({
										title: '上传中...'
									});
									let token = uni.getStorageSync('token');
									let userId = uni.getStorageSync('userId');
									uni.uploadFile({
										// url: that.config("APIHOST1") + '/alioss/upload', //真实的接口地址
										url: 'https://xicheshop.xianmxkj.com/sqx_fast/alioss/upload',
										filePath: res.tempFilePaths[0],
										header: {
											token: token
										},
										name: 'file',
										success: uploadFileRes => {
											url = JSON.parse(uploadFileRes.data);
											that.avatar = url.data
											uni.hideLoading();
										}
									});
								}
							});
						}
					}
				})
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			},
			//微信填写能力获取头像
			onChooseAvatar(e) {
				console.log(e.detail.avatarUrl)
				let that = this;
				let token = uni.getStorageSync('token');
				uni.showLoading({
					title: '上传中...'
				});
				uni.uploadFile({
					// url: config.APIHOST1 + '/alioss/upload', //仅为示例，非真实的接口地址
					url: 'https://jiazhengv5.xianmaxiong.com/sqx_fast/alioss/upload', //仅为示例，非真实的接口地址
					filePath: e.detail.avatarUrl,
					// filePath: res.tempFilePaths[0],
					header: {
						token: token
					},
					name: 'file',
					success: uploadFileRes => {
						let url = JSON.parse(uploadFileRes.data).data;
						that.$Request.postJson(
							'/app/user/updateUserImageUrl?avatar=' + url).then(
							res => {
								uni.hideLoading();
								if (res.code === 0) {
									that.$queue.showToast(
										"更新成功");
									that.getUserInfo();
								}
							});
					}
				});

			},
			getUserInfo() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$Request.get("/app/user/selectUserByUserId", data).then(res => {
					if (res.code == 0) {
						this.$queue.setData('avatar', res.data.avatar);
						this.$queue.setData('userName', res.data.userName);
						this.$queue.setData('phone', res.data.phone);
						this.$queue.setData('age', res.data.age);
						uni.setStorageSync('userType', res.data.userType)
						this.sex = res.data.sex
						this.age = res.data.age
						this.phone = res.data.phone;
						this.avatar = res.data.avatar ? res.data.avatar : '../../static/logo.png';
						this.userName = res.data.userName;
						if (this.userName == null) {
							this.userName = res.data.nickName;
						} else {
							this.userName = res.data.userName;
						}
					}
					uni.hideLoading();
				});
			},
			// 保存
			messagebtn() {
				if (!this.userName) {
					uni.showToast({
						title: "用户名不能为空",
						icon: "none"
					})
				} else if (!this.phone) {
					uni.showToast({
						title: "联系电话不能为空",
						icon: "none"
					})
				} else {
					uni.showModal({
						title: '温馨提示',
						content: '确定保存信息',
						success: e => {
							if (e.confirm) {
								this.$Request.postJson("/app/user/updateUser", {
									userName: this.userName,
									avatar: this.avatar,
									phone: this.phone,
									sex: this.sex,
									age: this.age
								}).then(res => {
									if (res.code === 0) {
										uni.showToast({
											title: '保存成功',
											icon: "none"
										})
										setTimeout(function() {
											uni.navigateBack()
										}, 1000)
									} else {
										uni.showToast({
											title: res.msg,
											icon: "none"
										})
									}
								})
							}
						}
					});
				}
			}
		},
	};
</script>

<style lang="scss" scoped>
	page {
		background: #f3f7fb;
	}

	button::after {
		border: none;
	}

	.userinfo-page {
		position: relative;
		min-height: 100vh;
		padding: 24rpx 24rpx 0;
		overflow: hidden;
	}

	.page-bg {
		position: absolute;
		inset: 0;
		pointer-events: none;
	}

	.page-glow {
		position: absolute;
		border-radius: 50%;
		filter: blur(80rpx);
		opacity: 0.7;
	}

	.page-glow-left {
		top: -80rpx;
		left: -120rpx;
		width: 320rpx;
		height: 320rpx;
		background: rgba(21, 202, 210, 0.16);
	}

	.page-glow-right {
		top: 220rpx;
		right: -140rpx;
		width: 360rpx;
		height: 360rpx;
		background: rgba(112, 181, 255, 0.12);
	}

	.profile-card,
	.form-card {
		position: relative;
		z-index: 1;
		background: #ffffff;
		border-radius: 32rpx;
		box-shadow: 0 18rpx 50rpx rgba(23, 52, 86, 0.08);
	}

	.profile-card {
		overflow: hidden;
		background: linear-gradient(135deg, #24d8de 0%, #15cad2 48%, #0eabb3 100%);
		padding: 40rpx 34rpx;
	}

	.profile-card__shine {
		position: absolute;
		top: -120rpx;
		right: -120rpx;
		width: 320rpx;
		height: 320rpx;
		border-radius: 50%;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.22) 0%, rgba(255, 255, 255, 0) 70%);
	}

	.profile-main {
		position: relative;
		z-index: 1;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 24rpx;
	}

	.profile-copy {
		flex: 1;
		min-width: 0;
	}

	.profile-title {
		display: block;
		font-size: 46rpx;
		font-weight: 700;
		line-height: 1.2;
		color: #ffffff;
	}

	.profile-subtitle {
		display: block;
		margin-top: 14rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: rgba(255, 255, 255, 0.9);
	}

	.profile-avatar-wrap {
		flex-shrink: 0;
	}

	.avatar-button {
		position: relative;
		display: flex;
		align-items: center;
		justify-content: center;
		width: 156rpx;
		height: 156rpx;
		padding: 0;
		margin: 0;
		background: rgba(255, 255, 255, 0.12) !important;
		border-radius: 50%;
	}

	.profile-avatar {
		width: 132rpx;
		height: 132rpx;
		border-radius: 50%;
		border: 4rpx solid rgba(255, 255, 255, 0.58);
		box-shadow: 0 12rpx 26rpx rgba(8, 99, 104, 0.22);
	}

	.avatar-edit-badge {
		position: absolute;
		right: 8rpx;
		bottom: 6rpx;
		width: 44rpx;
		height: 44rpx;
		border-radius: 50%;
		background: #0e8f97;
		border: 4rpx solid #ffffff;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.form-card {
		margin-top: 24rpx;
		padding: 34rpx 28rpx 18rpx;
	}

	.card-header {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
		margin-bottom: 26rpx;
	}

	.header-left {
		display: flex;
		align-items: center;
		gap: 14rpx;
	}

	.header-bar {
		width: 8rpx;
		height: 34rpx;
		border-radius: 999rpx;
		background: linear-gradient(180deg, #27dce2 0%, #15cad2 100%);
	}

	.header-title {
		font-size: 34rpx;
		font-weight: 700;
		color: #182338;
	}

	.header-desc {
		font-size: 22rpx;
		line-height: 1.6;
		color: #7e8ca0;
		text-align: right;
	}

	.form-list {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}

	.field-item {
		padding: 24rpx;
		border-radius: 24rpx;
		background: linear-gradient(180deg, #ffffff 0%, #f8fbfd 100%);
		border: 2rpx solid #edf2f7;
	}

	.field-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 16rpx;
	}

	.field-label {
		font-size: 26rpx;
		font-weight: 600;
		color: #182338;
	}

	.field-tip {
		font-size: 22rpx;
		color: #8fa0b3;
	}

	.field-control {
		height: 88rpx;
		padding: 0 24rpx;
		border-radius: 22rpx;
		background: #f5f8fb;
		display: flex;
		align-items: center;
	}

	.field-control--disabled {
		background: #f0f4f8;
	}

	.field-input {
		width: 100%;
		height: 100%;
		font-size: 28rpx;
		color: #243244;
		background: transparent;
	}

	.field-input--disabled {
		color: #8b98aa;
	}

	.field-placeholder {
		font-size: 28rpx;
		color: #b4c0cf;
	}

	.field-item--gender {
		padding-bottom: 28rpx;
	}

	.gender-panel {
		padding: 6rpx 0 0;
	}

	.gender-group {
		display: flex;
		gap: 18rpx;
	}

	.gender-option {
		flex: 1;
		min-height: 88rpx;
		padding: 0 24rpx;
		border-radius: 22rpx;
		background: #f5f8fb;
		border: 2rpx solid transparent;
		display: flex;
		align-items: center;
	}

	.gender-option--active {
		background: rgba(21, 202, 210, 0.08);
		border-color: rgba(21, 202, 210, 0.18);
	}

	.submit-bar {
		position: sticky;
		bottom: 0;
		z-index: 2;
		padding: 26rpx 6rpx 0;
		background: linear-gradient(180deg, rgba(243, 247, 251, 0) 0%, rgba(243, 247, 251, 0.95) 28%, #f3f7fb 100%);
	}

	.submit-btn {
		height: 96rpx;
		line-height: 96rpx;
		border-radius: 999rpx;
		text-align: center;
		font-size: 30rpx;
		font-weight: 700;
		color: #ffffff;
		background: linear-gradient(135deg, #27dce2 0%, #15cad2 45%, #0eabb3 100%);
		box-shadow: 0 18rpx 34rpx rgba(21, 202, 210, 0.26);
	}

	.safe-bottom {
		height: calc(env(safe-area-inset-bottom) + 28rpx);
	}
</style>
