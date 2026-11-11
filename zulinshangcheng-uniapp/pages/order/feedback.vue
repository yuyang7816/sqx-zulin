<template>
	<view class="page-container">
		<!-- 顶部装饰背景 -->
		<view class="header-bg"></view>

		<!-- 评价内容 -->
		<view class="card" style="margin-top: 30rpx;">
			<view class="section-title">评价内容</view>
			<view class="input-wrap">
				<textarea 
					placeholder="请输入您的评价，分享您的使用体验..." 
					v-model="sendDate.content"
					class="feedback-textarea" 
					placeholder-style="color: #A0AEC0; font-size: 28rpx;"
					maxlength="500"
				/>
				<view class="textarea-count">{{ sendDate.content.length }}/500</view>
			</view>
		</view>

		<!-- 订单评分 -->
		<view class="card">
			<view class="section-title">订单评分</view>
			<view class="rate-wrap">
				<u-rate 
					:count="count" 
					v-model="value" 
					active-color="#15CAD2" 
					inactive-color="#E2E8F0"
					size="48"
					gutter="16"
				></u-rate>
				<text class="rate-text">{{ rateText }}</text>
			</view>
		</view>

		<!-- 评价图片 -->
		<view class="card">
			<view class="section-title">评价图片</view>
			<view class="image-tip">最多可上传 6 张图片</view>
			<view class="release_image">
				<shmily-drag-image :list.sync="imageList" :number="6"></shmily-drag-image>
			</view>
		</view>

		<!-- 提交按钮 -->
		<view class="submit-wrap">
			<button class="submit-btn" @tap="send">提交评价</button>
		</view>
	</view>
</template>

<script>
	import shmilyDragImage from '../../components/shmily-drag-image/shmily-drag-image.vue'
	export default {
		components: {
			shmilyDragImage
		},
		data() {
			return {
				msgContents: ['界面显示错乱', '启动缓慢，卡出翔了', 'UI无法直视，丑哭了', '偶发性崩溃'],
				stars: [1, 2, 3, 4, 5],
				imageList: [],
				sendDate: {
					score: 5,
					content: '',
					contact: ''
				},
				orderTakingId: '',
				count: 5,
				value: 5,
				indentNumber: '',
				userId: '',
				evaluateType: ''
			};
		},
		computed: {
			rateText() {
				const map = ['', '非常差', '差', '一般', '满意', '非常满意'];
				return map[this.value] || '';
			}
		},
		onLoad(e) {
			this.orderId = e.orderId
			this.userId = uni.getStorageSync('userId')
		},
		methods: {
			close(e) {
				this.imageList.splice(e, 1);
			},
			chooseMsg() {
				uni.showActionSheet({
					itemList: this.msgContents,
					success: res => {
						this.sendDate.content = this.msgContents[res.tapIndex];
					}
				});
			},
			chooseImg() {
				uni.chooseImage({
					sourceType: ['camera', 'album'],
					sizeType: 'compressed',
					count: 8 - this.imageList.length,
					success: res => {
						this.imageList = this.imageList.concat(res.tempFilePaths);
					}
				});
			},
			chooseStar(e) {
				this.sendDate.score = e;
			},
			previewImage() {
				uni.previewImage({
					urls: this.imageList
				});
			},
			send() {
				let images = '';
				if (!this.sendDate.content) {
					uni.showToast({
						icon: 'none',
						title: '请输入评价内容'
					});
					return;
				}
				if (this.$queue.getChatSearchKeys(this.sendDate.content)) {
					uni.showToast({
						title: '输入内容带有非法关键字请重新输入',
						mask: false,
						duration: 1500,
						icon: "none"
					});
					return;
				}
				if (this.imageList.length == 0) {
					uni.hideLoading();
					this.$queue.showToast('请添加图片!');
					return;
				} else {
					for (var i = 0; i < this.imageList.length; i++) {
						if (i === 0) {
							images = this.imageList[i];
						} else {
							images = images + ',' + this.imageList[i];
						}
					}
				}
				this.$queue.showLoading('加载中...');
				let data = {
					byUserId: this.userId,
					orderId: this.orderId,
					satisfactionFlag: this.value,
					evaluateImg: images,
					evaluateMessage: this.sendDate.content
				}
				uni.showLoading({
					title: '评价中...'
				})
				this.$Request.postT('/app/orders/appEvaluate', data).then(
					res => {
						uni.hideLoading()
						if (res.code == 0) {
							uni.showToast({
								title: '评价成功',
								icon: 'none'
							})
							setTimeout(() => {
								uni.navigateBack()
							}, 500)
						} else {
							uni.showToast({
								title: '评价失败',
								icon: 'none'
							})
						}
					})
			}
		}
	};
</script>

<style lang="scss" scoped>
	$primary: #15CAD2;
	$primary-light: #E3FAFB;
	$primary-soft: #B3F0F3;
	$primary-dark: #0EA8B0;
	$primary-deep: #088F96;
	$bg: #F0F9FA;
	$card-bg: #FFFFFF;
	$text-main: #1A1A2E;
	$text-sub: #4A5568;
	$text-tip: #A0AEC0;
	$border: #EDF2F7;
	$shadow: rgba(21, 202, 210, 0.08);

	page {
		background: $bg !important;
	}

	.page-container {
		min-height: 100vh;
		background: $bg;
		position: relative;
		padding: 0 24rpx 40rpx;
	}

	/* 顶部渐变装饰 */
	.header-bg {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 260rpx;
		background: linear-gradient(180deg, #E3FAFB 0%, #F0F9FA 70%, transparent 100%);
		pointer-events: none;
		z-index: 0;
	}

	/* 通用卡片 */
	.card {
		background: $card-bg;
		border-radius: 24rpx;
		padding: 32rpx;
		margin-bottom: 24rpx;
		box-shadow: 0 8rpx 32rpx $shadow;
		position: relative;
		z-index: 1;
	}

	.section-title {
		font-size: 32rpx;
		font-weight: 800;
		color: $text-main;
		margin-bottom: 24rpx;
		letter-spacing: 1rpx;
		position: relative;
		display: inline-block;
	}

	.section-title::after {
		content: '';
		position: absolute;
		bottom: -8rpx;
		left: 0;
		width: 40rpx;
		height: 6rpx;
		background: linear-gradient(90deg, $primary, $primary-soft);
		border-radius: 3rpx;
	}

	/* 输入区域 */
	.input-wrap {
		position: relative;
		background: #F8FAFC;
		border-radius: 20rpx;
		padding: 24rpx;
	}

	.feedback-textarea {
		width: 100%;
		height: 220rpx;
		font-size: 30rpx;
		line-height: 1.6;
		color: $text-main;
		caret-color: $primary;
	}

	.textarea-count {
		text-align: right;
		font-size: 24rpx;
		color: $text-tip;
		margin-top: 12rpx;
	}

	/* 评分 */
	.rate-wrap {
		display: flex;
		align-items: center;
		gap: 24rpx;
		padding: 8rpx 0;
	}

	.rate-text {
		font-size: 28rpx;
		color: $primary-deep;
		font-weight: 600;
	}

	/* 图片上传提示 */
	.image-tip {
		font-size: 24rpx;
		color: $text-tip;
		margin-bottom: 20rpx;
		margin-top: -8rpx;
	}

	.release_image {
		padding: 4rpx 0;
	}

	/* 提交按钮 */
	.submit-wrap {
		margin-top: 48rpx;
		padding: 0 8rpx;
		position: relative;
		z-index: 1;
	}

	.submit-btn {
		width: 100%;
		height: 90rpx;
		line-height: 90rpx;
		background: linear-gradient(135deg, $primary 0%, $primary-dark 100%);
		color: #FFFFFF;
		font-size: 32rpx;
		font-weight: 700;
		border-radius: 45rpx;
		border: none;
		box-shadow: 0 12rpx 32rpx rgba(21, 202, 210, 0.25);
		letter-spacing: 2rpx;
		transition: all 0.2s ease;
	}

	.submit-btn::after {
		border: none;
	}

	.submit-btn:active {
		transform: scale(0.98);
		opacity: 0.9;
	}

	/* 深度覆盖 u-rate */
	::v-deep .u-rate__icon {
		transition: transform 0.15s ease;
	}

	::v-deep .u-rate__icon--active {
		transform: scale(1.1);
	}
</style>