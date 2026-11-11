<template>
	<view class="msg-page">
		<u-navbar :back-text="backText" back-icon-color="#1A2233" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>

		<view class="msg-shell">
			

			<view class="entry-list">
				<view class="entry-card service" @tap="goChat">
					<view class="entry-icon-wrap">
						<image class="entry-icon" src="../../static/zlImage/magkf.png" mode="aspectFill"></image>
					</view>
					<view class="entry-content">
						<view class="entry-top">
							<text class="entry-title">在线客服</text>
							<view class="entry-badge" v-if="adminmessageCount>0">
								{{adminmessageCount < 99 ?adminmessageCount:adminmessageCount+'+'}}
							</view>
						</view>
						<view class="entry-desc">有问题请联系在线客服，获取实时帮助</view>
						<view class="entry-meta">
							<text>人工在线</text>
							<u-icon name="arrow-right" size="20" color="#15CAD2"></u-icon>
						</view>
					</view>
				</view>

				<view class="entry-card notice" @click="goMsg">
					<view class="entry-icon-wrap notice-icon-wrap">
						<image class="entry-icon" src="../../static/zlImage/msgtz.png" mode="aspectFill"></image>
					</view>
					<view class="entry-content">
						<view class="entry-top">
							<text class="entry-title">{{msgList.title?msgList.title: '通知消息'}}</text>
							<view class="entry-badge" v-if="messageCount>0">
								{{messageCount < 99 ?messageCount:messageCount+'+'}}
							</view>
						</view>
						<view class="entry-desc notice-text">{{msgList.content ? msgList.content : '最新通知、订单提醒会展示在这里'}}</view>
						<view class="entry-meta">
							<text>查看全部通知</text>
							<u-icon name="arrow-right" size="20" color="#15CAD2"></u-icon>
						</view>
					</view>
				</view>
			</view>

			<view v-if="chatList.length" class="conversation-panel">
				<view class="panel-header">
					<view>
						<view class="panel-title">最近会话</view>
						<view class="panel-subtitle">最近联系过的用户与会话消息</view>
					</view>
				</view>

				<view class="conversation-list">
					<view class="conversation-card" @click="goIM(item)" v-for="(item,index) in chatList" :key='index'>
						<view class="conversation-row" v-if="userId == item.userId">
							<u-image class="conversation-avatar" shape="circle" width='88rpx' height="88rpx" :src="item.byAvatar"></u-image>
							<view class="conversation-body">
								<view class="conversation-top">
									<text class="conversation-name">{{item.byUserName}}</text>
									<text class="conversation-time">{{item.messageTime}}</text>
								</view>
								<view class="conversation-bottom">
									<view class="conversation-text" v-if="item.messageType == 1">{{ item.content }}</view>
									<view class="conversation-text" v-if="item.messageType == 2">[图片]</view>
									<view class="conversation-text" v-if="item.messageType == 3">[语音]</view>
									<view class="conversation-text" v-if="item.messageType == 4">[表情]</view>
									<view class="conversation-text" v-if="item.messageType == 5">[地图]</view>
									<view class="conversation-text" v-if="item.messageType == 6">[订单]</view>
									<view class="conversation-text" v-if="item.messageType == 7">[服务]</view>
									<view class="chat-badge" v-if="item.contentCount>0">
										{{item.contentCount < 99 ?item.contentCount:item.contentCount+'+'}}
									</view>
								</view>
							</view>
						</view>

						<view class="conversation-row" v-else>
							<u-image class="conversation-avatar" shape="circle" width='88rpx' height="88rpx" :src="item.avatar"></u-image>
							<view class="conversation-body">
								<view class="conversation-top">
									<text class="conversation-name">{{item.userName}}</text>
									<text class="conversation-time">{{item.messageTime}}</text>
								</view>
								<view class="conversation-bottom">
									<view class="conversation-text" v-if="item.messageType == 1">{{ item.content }}</view>
									<view class="conversation-text" v-if="item.messageType == 2">[图片]</view>
									<view class="conversation-text" v-if="item.messageType == 3">[语音]</view>
									<view class="conversation-text" v-if="item.messageType == 4">[表情]</view>
									<view class="conversation-text" v-if="item.messageType == 5">[地图]</view>
									<view class="conversation-text" v-if="item.messageType == 6">[订单]</view>
									<view class="conversation-text" v-if="item.messageType == 7">[服务]</view>
									<view class="chat-badge" v-if="item.contentCount>0">
										{{item.contentCount < 99 ?item.contentCount:item.contentCount+'+'}}
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>

			<empty v-if="!chatList.length && !msgList.length" content='暂无消息'></empty>
		</view>
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
				// tabbarList: this.$store.state.list,
				// page: 1,
				// limit: 100,
				// chatList: [],
				// userId: '',
				// msgList: {
				// 	title: '',
				// 	content: '',
				// },
				// time: '',
				// arr: [],
				// messageCount: 0,
				// adminmessageCount: 0,



				backText: '消息',
				backTextStyle: {
					color: '#333333',
					// fontweighth:'bold',
				},
				keyword: '',
				page: 1,
				limit: 100,
				chatList: [],
				userId: '',
				msgList: {
					title: '',
					content: '',
				},
				time: '',
				arr: [],
				messageCount: 0,
				adminmessageCount: 0,
			}
		},
		onLoad() {
			// console.log(this.$store.state.list, '11111111111111111')
			let that = this;
			let userId = uni.getStorageSync('userId')
			if (userId) {
				that.$Request.getT('/app/common/type/249').then(res => { //私聊消息通知
					if (res.code == 0) {
						if (res.data && res.data.value) {
							that.arr.push(res.data.value)
						}
					}
				})
				that.$Request.getT('/app/common/type/236').then(res => { //退款通知
					if (res.code == 0) {
						if (res.data && res.data.value) {
							that.arr.push(res.data.value)
						}
					}
				})
				that.$Request.getT('/app/common/type/312').then(res => { //订单状态通知
					if (res.code == 0) {
						if (res.data && res.data.value) {
							that.arr.push(res.data.value)
						}
					}
				})
			}
		},
		onShow() {
			let that = this
			that.userId = uni.getStorageSync('userId')
			if (that.userId) {
				// that.getChatList()
				that.getMsgList()
				that.messageCount = uni.getStorageSync('messageCount')
				that.adminmessageCount = uni.getStorageSync('adminmessageCount')
				that.time = setInterval(function() {
					// that.getChatList()
					that.getMsgList()
					that.$nextTick(function() {
						that.messageCount = uni.getStorageSync('messageCount')
						that.adminmessageCount = uni.getStorageSync('adminmessageCount')
					})

				}, 3000)

			} else {
				that.chatList = []
				that.msgList = {
					title: '',
					content: '',
				}
			}
		},
		onHide() {
			clearInterval(this.time)
		},
		methods: {
			// 在线客服
			goChat() {
				if (this.SelKeFu === '1') {
					// #ifdef MP-WEIXIN
					let that = this
					try {
						wx.openCustomerServiceChat({
							extInfo: {
								url: that.$queue.getData('SelKeFuLink')
							},
							corpId: that.$queue.getData('SelKeFuAppId'),
							success(res) {},
							fail(res) {
								console.error(res)
							}
						})
					} catch (error) {
						console.error("catchcatch" + error)
						uni.showToast({
							title: '请更新至微信最新版本'
						});
					}
					// #endif
					// #ifndef MP-WEIXIN
					let url = this.$queue.getData('SelKeFuLink');
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
					// #endifS
				} else {
					let token = this.$queue.getData('token');
					if (token) {
						uni.navigateTo({
							url: '/my/setting/chat'
						});
					} else {
						uni.navigateTo({
							url: '/pages/public/login'
						})
					}
				}

			},
			getChatList() {
				this.$Request.get("/app/chat/selectChatConversationPage", {
					page: this.page,
					limit: this.limit,
					userType: 1
				}).then(res => {
					if (res.code == 0) {
						this.chatList = res.data.list
					}
				});
			},
			getMsgList() {
				this.$Request.get("/app/message/getUserMessageInfoListLimit1").then(res => {
					if (res.code == 0) {
						if (res.data) {
							this.msgList = res.data
						}
					}
				});
			},
			goIM(e) {
				let userId = this.$queue.getData('userId');
				let byNickName = ''
				// console.log(e.byUserId,userId,'----------',e.byUserId == userId, e.userId,e.userName)
				// return
				if (e.byUserId == userId) {
					userId = e.userId
					byNickName = e.userName
				} else {
					userId = e.byUserId
					byNickName = e.byUserName
				}

				// uni.navigateTo({
				// 	url: '/pages/msg/im?chatConversationId=' + e.chatConversationId + '&byUserId=' + userId +
				// 		'&byNickName=' + e.byUserName
				// })
				let token = this.$queue.getData('token');
				if (token) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendMsg')) {
						wx.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) {
								if (datas.indexOf("accept") != -1) {
									console.log(re)
								}
							},
							fail: (res) => {
								console.log(res)
							}
						})
					}
					// #endif
					uni.navigateTo({
						url: '/pages/msg/chat?chatConversationId=' + e.chatConversationId + '&byUserId=' + userId +
							'&byNickName=' + byNickName
					})
				} else {
					this.goLoginInfo();
				}
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
				})
			},
			goMsg() {
				// #ifdef MP-WEIXIN
				if (uni.getStorageSync('sendMsg')) {
					wx.requestSubscribeMessage({
						tmplIds: this.arr,
						success(re) {
							var datas = JSON.stringify(re);
							if (datas.indexOf("accept") != -1) {
								// console.log(re)
							}
						},
						fail: (res) => {
							// console.log(res)
						}
					})
				}
				// #endif
				this.messageCount = 0
				uni.navigateTo({
					url: '/pages/msg/message'
				})
			}
		}
	}
</script>

<style lang="scss">
	$primary: #15CAD2;
	$primary-dark: #0C98A1;
	$text-main: #1A2233;
	$text-sub: #627083;
	$text-soft: #8E9BAB;
	$panel-border: rgba(255, 255, 255, 0.76);

	page {
		background:
			linear-gradient(180deg, #7CE5EA 0%, #BFF3F5 22%, #E8FBFC 58%, #F4FEFE 100%),
			repeating-linear-gradient(135deg, rgba(255, 255, 255, 0.14) 0, rgba(255, 255, 255, 0.14) 16rpx, rgba(255, 255, 255, 0) 16rpx, rgba(255, 255, 255, 0) 44rpx);
	}

	::v-deep .u-border-bottom:after {
		border: none !important;
	}

	.msg-page {
		position: relative;
		min-height: 100vh;

		&::before {
			content: '';
			position: fixed;
			left: 0;
			right: 0;
			top: 0;
			height: 520rpx;
			background: linear-gradient(180deg, rgba(12, 152, 161, 0.2) 0%, rgba(12, 152, 161, 0.06) 48%, rgba(12, 152, 161, 0) 100%);
			pointer-events: none;
			z-index: 0;
		}
	}

	.msg-shell {
		position: relative;
		z-index: 1;
		padding: 18rpx 24rpx calc(40rpx + env(safe-area-inset-bottom));
	}

	.msg-hero {
		position: relative;
		overflow: hidden;
		padding: 30rpx 28rpx;
		border-radius: 32rpx;
		background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(240, 252, 252, 0.9) 100%);
		border: 1rpx solid $panel-border;
		box-shadow: 0 18rpx 40rpx rgba(33, 84, 90, 0.08);
	}

	.hero-decor {
		position: absolute;
		border-radius: 50%;
		pointer-events: none;

		&.hero-decor-1 {
			width: 220rpx;
			height: 220rpx;
			top: -100rpx;
			right: -60rpx;
			background: radial-gradient(circle, rgba(21, 202, 210, 0.18) 0%, rgba(21, 202, 210, 0) 72%);
		}

		&.hero-decor-2 {
			width: 160rpx;
			height: 160rpx;
			left: -50rpx;
			bottom: -70rpx;
			background: radial-gradient(circle, rgba(12, 152, 161, 0.14) 0%, rgba(12, 152, 161, 0) 74%);
		}
	}

	.hero-main {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		gap: 20rpx;
	}

	.hero-title {
		font-size: 40rpx;
		font-weight: 700;
		color: $text-main;
		line-height: 1.2;
	}

	.hero-subtitle {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: $text-sub;
	}

	.hero-mark {
		width: 84rpx;
		height: 84rpx;
		border-radius: 28rpx;
		background: linear-gradient(135deg, rgba(21, 202, 210, 0.14) 0%, rgba(21, 202, 210, 0.04) 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.9);
		flex-shrink: 0;
	}

	.hero-stats {
		margin-top: 28rpx;
		padding: 22rpx 24rpx;
		border-radius: 24rpx;
		background: rgba(244, 254, 254, 0.72);
		border: 1rpx solid rgba(21, 202, 210, 0.08);
		display: flex;
		align-items: center;
	}

	.hero-stat {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}

	.hero-stat-num {
		font-size: 36rpx;
		font-weight: 700;
		color: $text-main;
	}

	.hero-stat-label {
		font-size: 22rpx;
		color: $text-soft;
	}

	.hero-divider {
		width: 1rpx;
		height: 52rpx;
		background: rgba(12, 152, 161, 0.12);
		margin: 0 12rpx;
	}

	.entry-list {
		margin-top: 24rpx;
		display: flex;
		flex-direction: column;
		gap: 18rpx;
	}

	.entry-card {
		position: relative;
		display: flex;
		align-items: center;
		gap: 20rpx;
		padding: 28rpx 26rpx;
		border-radius: 30rpx;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.97) 0%, rgba(255, 255, 255, 0.92) 100%);
		border: 1rpx solid $panel-border;
		box-shadow: 0 16rpx 36rpx rgba(33, 84, 90, 0.07);

		&:active {
			transform: scale(0.985);
		}

		&.service {
			background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(234, 250, 251, 0.94) 100%);
		}

		&.notice {
			background: linear-gradient(135deg, rgba(255, 255, 255, 0.98) 0%, rgba(240, 252, 252, 0.94) 100%);
		}
	}

	.entry-icon-wrap {
		width: 92rpx;
		height: 92rpx;
		border-radius: 28rpx;
		background: linear-gradient(135deg, #E6FAFB 0%, #D5F5F7 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.92);
		flex-shrink: 0;
	}

	.notice-icon-wrap {
		background: linear-gradient(135deg, #EAFBFC 0%, #DDF7F8 100%);
	}

	.entry-icon {
		width: 56rpx;
		height: 56rpx;
	}

	.entry-content {
		flex: 1;
		min-width: 0;
	}

	.entry-top {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 14rpx;
	}

	.entry-title {
		flex: 1;
		min-width: 0;
		font-size: 32rpx;
		font-weight: 700;
		color: $text-main;
		line-height: 1.3;
	}

	.entry-badge,
	.chat-badge {
		min-width: 40rpx;
		height: 40rpx;
		padding: 0 12rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #1FD0D7 0%, $primary-dark 100%);
		color: #FFFFFF;
		font-size: 22rpx;
		font-weight: 600;
		display: inline-flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		box-sizing: border-box;
	}

	.entry-desc {
		margin-top: 12rpx;
		font-size: 24rpx;
		line-height: 1.6;
		color: $text-sub;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		overflow: hidden;
	}

	.notice-text {
		padding-right: 12rpx;
	}

	.entry-meta {
		margin-top: 14rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 22rpx;
		color: $primary-dark;
	}

	.conversation-panel {
		margin-top: 26rpx;
		padding: 28rpx 24rpx 14rpx;
		border-radius: 30rpx;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.97) 0%, rgba(255, 255, 255, 0.92) 100%);
		border: 1rpx solid $panel-border;
		box-shadow: 0 16rpx 36rpx rgba(33, 84, 90, 0.07);
	}

	.panel-header {
		padding: 0 4rpx 18rpx;
	}

	.panel-title {
		font-size: 32rpx;
		font-weight: 700;
		color: $text-main;
	}

	.panel-subtitle {
		margin-top: 10rpx;
		font-size: 22rpx;
		color: $text-soft;
	}

	.conversation-list {
		display: flex;
		flex-direction: column;
		gap: 14rpx;
	}

	.conversation-card {
		padding: 22rpx 8rpx;
		border-top: 1rpx solid rgba(12, 152, 161, 0.08);

		&:first-child {
			border-top: none;
		}

		&:active {
			opacity: 0.8;
		}
	}

	.conversation-row {
		display: flex;
		align-items: center;
		gap: 18rpx;
	}

	.conversation-avatar {
		flex-shrink: 0;
	}

	.conversation-body {
		flex: 1;
		min-width: 0;
	}

	.conversation-top,
	.conversation-bottom {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 14rpx;
	}

	.conversation-top {
		margin-bottom: 10rpx;
	}

	.conversation-name {
		flex: 1;
		min-width: 0;
		font-size: 30rpx;
		font-weight: 600;
		color: $text-main;
	}

	.conversation-time {
		font-size: 22rpx;
		color: $text-soft;
		flex-shrink: 0;
	}

	.conversation-text {
		flex: 1;
		min-width: 0;
		font-size: 24rpx;
		line-height: 1.5;
		color: $text-sub;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
</style>
