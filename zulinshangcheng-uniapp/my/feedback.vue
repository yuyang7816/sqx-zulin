<template>
	<view class="feedback-page">
		<view class="feedback-shell">
			<view class="feedback-hero">
				<view class="hero-main">
					<view>
						<view class="hero-title">问题反馈</view>
						<view class="hero-subtitle">告诉我们你遇到的问题或建议，我们会尽快处理并优化体验</view>
					</view>
					<view class="hero-badge">
						<u-icon name="edit-pen" size="34" color="#0EAFB6"></u-icon>
					</view>
				</view>
				<view class="hero-tip-list">
					<view class="hero-tip-item">
						<view class="hero-tip-dot"></view>
						<text>建议尽量描述具体场景与问题现象</text>
					</view>
					<view class="hero-tip-item">
						<view class="hero-tip-dot"></view>
						<text>留下联系方式后，我们可进一步与你确认</text>
					</view>
				</view>
			</view>

			<view class="feedback-card">
				<view class="section-head">
					<view class="section-title">问题和意见</view>
					<view class="quick-entry" @tap="chooseMsg">快速键入</view>
				</view>
				<textarea placeholder="请详细描述你的问题和意见..." v-model="sendDate.content" class="feedback-textare" />
			</view>

			<view class="feedback-card">
				<view class="section-title">QQ/邮箱</view>
				<view class="section-desc">方便我们联系你</view>
				<input class="feedback-input" v-model="sendDate.contact" placeholder="请输入QQ或邮箱" />
			</view>

			<button class="feedback-submit" @tap="send">提交反馈</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				msgContents: ['界面显示错乱', '启动缓慢，卡出翔了', 'UI无法直视，丑哭了', '偶发性崩溃'],
				stars: [1, 2, 3, 4, 5],
				imageList: [],
				sendDate: {
					score: 5,
					content: '',
					contact: ''
				}
			};
		},
		onLoad() {},
		methods: {
			chooseMsg() {
				//快速输入
				uni.showActionSheet({
					itemList: this.msgContents,
					success: res => {
						this.sendDate.content = this.msgContents[res.tapIndex];
					}
				});
				// uni.chooseImage({
				// 	sourceType: ['camera', 'album'],
				// 	sizeType: 'compressed',
				// 	count: 8 - this.imageList.length,
				// 	success: res => {
				// 		this.imageList = this.imageList.concat(res.tempFilePaths);
				// 	}
				// });
			},
			chooseImg() {
				//选择图片

			},
			chooseStar(e) {
				//点击评星
				this.sendDate.score = e;
			},
			previewImage() {
				//预览图片
				uni.previewImage({
					urls: this.imageList
				});
			},
			send() {
				//发送反馈
				console.log(JSON.stringify(this.sendDate));

				if (!this.sendDate.content) {
					uni.showToast({
						icon: 'none',
						title: '请输入反馈内容'
					});
					return;
				}
				if (!this.sendDate.contact) {
					uni.showToast({
						icon: 'none',
						title: '请填写QQ或邮箱'
					});
					return;
				}
				let data = {
					phone: this.sendDate.contact,
					content: this.sendDate.content,
					state: 2
				}
				this.$Request.postJson('/app/message/insertMessage', {
					phone: this.sendDate.contact,
					content: this.sendDate.content,
					state: 2
				}).then(res => {
					if (res.code === 0) {
						uni.showToast({
							title: '提交成功',
							icon: 'none'
						});
						setTimeout(function() {
							uni.navigateBack();
						}, 1000);
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			}
		}
	};
</script>

<style lang="scss">
	@font-face {
		font-family: uniicons;
		font-weight: normal;
		font-style: normal;
		src: url('https://img-cdn-qiniu.dcloud.net.cn/fonts/uni.ttf') format('truetype');
	}

	page {
		background: #F3F6F8;
	}

	$primary: #15CAD2;
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;

	.feedback-page {
		min-height: 100vh;
	}

	.feedback-shell {
		padding: 24rpx;
	}

	.feedback-hero {
		background: #FFFFFF;
		border-radius: 28rpx;
		padding: 28rpx 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.hero-main {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
	}

	.hero-title {
		font-size: 34rpx;
		font-weight: 700;
		color: $text-main;
	}

	.hero-subtitle {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.hero-badge {
		width: 84rpx;
		height: 84rpx;
		border-radius: 24rpx;
		background: #ECF9FA;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.hero-tip-list {
		margin-top: 24rpx;
		padding: 20rpx 22rpx;
		border-radius: 20rpx;
		background: #F7FAFB;
		border: 1rpx solid #EDF3F5;
		display: flex;
		flex-direction: column;
		gap: 14rpx;
	}

	.hero-tip-item {
		display: flex;
		align-items: flex-start;
		gap: 12rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.hero-tip-dot {
		width: 10rpx;
		height: 10rpx;
		border-radius: 50%;
		background: $primary;
		margin-top: 12rpx;
		flex-shrink: 0;
	}

	.feedback-card {
		margin-top: 18rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 24rpx;
		border: 1rpx solid #E8EEF2;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
	}

	.section-head {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.section-title {
		font-size: 28rpx;
		font-weight: 700;
		color: $text-main;
	}

	.section-desc {
		margin-top: 10rpx;
		font-size: 22rpx;
		color: $text-soft;
	}

	.quick-entry {
		height: 52rpx;
		padding: 0 18rpx;
		border-radius: 999rpx;
		background: #ECF9FA;
		color: #0EAFB6;
		font-size: 22rpx;
		font-weight: 600;
		display: inline-flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.feedback-textare {
		height: 260rpx;
		font-size: 28rpx;
		line-height: 1.7;
		width: 100%;
		padding: 20rpx 22rpx;
		margin-top: 18rpx;
		background: #F7FAFB;
		border-radius: 18rpx;
		border: 1rpx solid #E8EEF2;
	}

	.feedback-input {
		font-size: 28rpx;
		height: 78rpx;
		line-height: 78rpx;
		padding: 0 22rpx;
		margin-top: 16rpx;
		background: #F7FAFB;
		border-radius: 18rpx;
		border: 1rpx solid #E8EEF2;
		color: $text-main;
	}

	.feedback-submit {
		height: 88rpx;
		line-height: 88rpx;
		border-radius: 44rpx;
		margin-top: 28rpx;
		background: linear-gradient(135deg, #21C9D1 0%, #0EAFB6 100%);
		color: #FFFFFF;
		font-size: 28rpx;
		font-weight: 700;
		box-shadow: 0 12rpx 24rpx rgba(16, 137, 145, 0.16);
	}
</style>
