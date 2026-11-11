<!-- 帮助反馈页面 -->
<template>
	<view class="help-page">
		<view class="help-shell">
			<view class="help-hero">
				<view class="hero-main">
					<view>
						<view class="hero-title">帮助中心</view>
						<view class="hero-subtitle">查看常见问题，也可以直接联系平台客服或提交反馈</view>
					</view>
					<view class="hero-badge">
						<u-icon name="server-man" size="34" color="#0EAFB6"></u-icon>
					</view>
				</view>
				<view class="hero-note">
					<view class="hero-note-dot"></view>
					<text>点击问题分类可展开查看对应内容</text>
				</view>
			</view>

			<view class="faq-card">
				<view class="card-title">常见问题</view>
				<view v-for="(item,index) in helpClassifyList" :key="index" class="faq-group">
					<view class="faq-head" @click.stop="openList(item)">
						<view class="faq-title">{{item.helpClassifyName}}</view>
						<u-icon :name="item.parentId==0 ? 'arrow-up-fill' : 'arrow-down-fill'" size="18" color="#90A0B1"></u-icon>
					</view>
					<view v-if="item.parentId==0" class="faq-body">
						<view v-for="(problemItem,problemIndex) in item.helpWordList" :key="problemIndex">
							<view class="faq-item" hover-class="hover" @click="onClick(problemItem)">
								<view class="faq-item-text">{{problemItem.helpWordTitle}}</view>
								<u-icon name="arrow-right" size="18" color="#B0BBC8"></u-icon>
							</view>
							<view class="faq-line" v-if="problemIndex!=item.helpWordList.length-1"></view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="help-bottom">
			<view class="help-actions">
				<view class="help-action" hover-class="hover" @click="Changekefu">
					<view class="action-icon action-icon--service">
						<u-icon name="server-man" size="28" color="#15CAD2"></u-icon>
					</view>
					<view class="action-text">
						<view class="action-title">联系客服</view>
						<view class="action-desc">获取人工协助</view>
					</view>
				</view>
				<view class="action-divider"></view>
				<view class="help-action" hover-class="hover" @click="toFeedback">
					<view class="action-icon action-icon--feedback">
						<u-icon name="edit-pen" size="28" color="#4F7CFF"></u-icon>
					</view>
					<view class="action-text">
						<view class="action-title">我要反馈</view>
						<view class="action-desc">提交问题与建议</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				helpClassifyList: []
			}
		},
		onLoad() {
			this.getlist()
		},
		methods: {
			openList(item) {
				var oldhelpClassifyList = item
				if (oldhelpClassifyList.parentId == 1) {
					item.parentId = 0
				} else {
					item.parentId = 1
				}
			},
			getlist() {
				this.$Request.getT('/app/help/selectHelpCenter').then(res => {
					if (res.code == 0) {
						this.helpClassifyList = res.data
						for (var i = 0; i < this.helpClassifyList.length; i++) {
							this.helpClassifyList[i].parentId = 1
						}
					}
				})
			},
			onClick(item) {
				uni.navigateTo({
					url: '/my/helpDetail/helpDetail?title=' + item.helpWordTitle + '&helpWordId=' + item.helpWordId,
				})
			},
			Changekefu() {
				uni.makePhoneCall({
					phoneNumber: '18892020825'
				});
				if (uni.getStorageSync('kefuType') == 1) {
					uni.makePhoneCall({
						phoneNumber: uni.getStorageSync('kefuPhone')
					});
				} else if (uni.getStorageSync('kefuType') == 3) {
					uni.navigateTo({
						url: '/mine/kefu/kefu'
					})
				} else {
					// #ifdef MP-WEIXIN
					let that = this
					try {
						wx.openCustomerServiceChat({
							extInfo: {
								url: that.$queue.getData('kefuUrl')
							},
							corpId: that.$queue.getData('kefuAppId'),
							success(res) {},
							fail(res) {
								console.error(res)
							}
						})
					} catch (error) {
						uni.showToast({
							title: '请更新至微信最新版本'
						});
					}
					// #endif
					// #ifndef MP-WEIXIN
					let url = this.$queue.getData('kefuUrl');
					if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
						uni.navigateTo({
							url
						});
					} else {
						//#ifndef H5
						uni.navigateTo({
							url: '/pages/index/webView?url=' + url
						});
						//#endif
						//#ifdef H5
						window.location.href = url;
						//#endif
					}
					// #endif
				}
			},
			toFeedback() {
				if (uni.getStorageSync('userId')) {
					uni.navigateTo({
						url: '/my/feedback'
					});
				} else {
					uni.showModal({
						title: '提示',
						content: '您还未登录,请先登录',
						success: function(res) {
							if (res.confirm) {
								uni.navigateTo({
									url: '/pages/public/login'
								})
							}
						}
					})
				}
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F3F6F8;
	}

	$primary: #15CAD2;
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;

	.help-page {
		min-height: 100vh;
		padding-bottom: 164rpx;
	}

	.help-shell {
		padding: 24rpx;
	}

	.help-hero,
	.faq-card {
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

	.hero-title,
	.card-title,
	.faq-title {
		color: $text-main;
		font-weight: 700;
	}

	.hero-title {
		font-size: 34rpx;
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

	.hero-note {
		margin-top: 24rpx;
		padding: 20rpx 22rpx;
		border-radius: 20rpx;
		background: #F7FAFB;
		border: 1rpx solid #EDF3F5;
		display: flex;
		align-items: flex-start;
		gap: 12rpx;
		font-size: 22rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.hero-note-dot {
		width: 10rpx;
		height: 10rpx;
		border-radius: 50%;
		background: $primary;
		margin-top: 12rpx;
		flex-shrink: 0;
	}

	.faq-card {
		margin-top: 18rpx;
	}

	.card-title {
		font-size: 28rpx;
	}

	.faq-group + .faq-group {
		margin-top: 18rpx;
	}

	.faq-group {
		background: #F7FAFB;
		border: 1rpx solid #E8EEF2;
		border-radius: 22rpx;
		padding: 0 20rpx;
	}

	.faq-head {
		height: 92rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.faq-title {
		font-size: 28rpx;
	}

	.faq-body {
		padding-bottom: 8rpx;
	}

	.faq-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 18rpx;
		padding: 24rpx 0;
	}

	.faq-item-text {
		flex: 1;
		font-size: 26rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.faq-line {
		height: 1rpx;
		background: #E8EEF2;
	}

	.help-bottom {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		padding: 16rpx 24rpx calc(16rpx + env(safe-area-inset-bottom));
		background: rgba(243, 246, 248, 0.96);
		backdrop-filter: blur(8rpx);
	}

	.help-actions {
		background: #FFFFFF;
		border: 1rpx solid #E8EEF2;
		border-radius: 24rpx;
		box-shadow: 0 8rpx 20rpx rgba(31, 44, 61, 0.04);
		display: flex;
		align-items: center;
		padding: 14rpx 0;
	}

	.help-action {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 14rpx;
		padding: 12rpx 18rpx;
	}

	.action-icon {
		width: 60rpx;
		height: 60rpx;
		border-radius: 18rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.action-icon--service {
		background: #ECF9FA;
	}

	.action-icon--feedback {
		background: #EDF3FF;
	}

	.action-text {
		min-width: 0;
	}

	.action-title {
		font-size: 26rpx;
		font-weight: 600;
		color: $text-main;
	}

	.action-desc {
		margin-top: 6rpx;
		font-size: 20rpx;
		color: $text-soft;
	}

	.action-divider {
		width: 1rpx;
		height: 56rpx;
		background: #E8EEF2;
	}

	.hover {
		opacity: 0.75;
	}
</style>
