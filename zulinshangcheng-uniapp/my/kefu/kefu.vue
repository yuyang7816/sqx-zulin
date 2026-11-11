<template>
	<view style="height: 100vh;margin: 32upx;">
		<view style="text-align: center;background: #FFFFFF;padding: 40upx;border-radius: 32upx;">
			<view style="font-size: 38upx;">添加客服咨询</view>
			<view style="font-size: 32upx;margin: 32upx 0;">{{weixin}}</view>
			<view v-if="weixin" @click="copyHref"
				style="background: #FFE60F;width: 180rpx;margin:auto;font-size: 30upx;color: #333333;padding: 4upx 20upx;border-radius: 24upx;">
				一键复制</view>
			<image @click="saveImg" mode="aspectFit" style="margin-top: 32upx" :src="image"></image>
			<view style="font-size: 28upx;margin-top: 32upx" v-if="isWeiXin">{{ isWeiXin ? '长按识别上方二维码' : '' }}</view>


			<!-- <view @click="goChat"
				style="width:260upx;margin-top: 32upx;font-size: 30upx;margin-left: 28%;color: #557EFD;padding: 4upx 20upx;border-radius: 24upx;">
				联系在线客服</view>
			 -->
		</view>
	</view>
</template>

<script>
	// import {
	// 	typeList
	// } from '@/apis/pages.js'
	export default {
		data() {
			return {
				image: '',
				isWeiXin: false,
				weixin: '',
				webviewStyles: {
					progress: {
						color: '#1A1929 '
					}
				}
			};
		},
		onLoad() {
			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				this.isWeiXin = true;
			}
			// #endif
			//获取客服二维码
			this.$Request.getT('/app/common/type/1').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.image = res.data.value;
					}
				}
			})
			// 客服微信号
			this.$Request.getT('/app/common/type/44').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.weixin = res.data.value;
					}
				}
			});
		},
		onPullDownRefresh: function() {
			uni.stopPullDownRefresh(); // 停止刷新
		},
		methods: {
			//邀请码复制
			copyHref() {
				uni.setClipboardData({
					data: this.weixin,
					success: r => {
						this.$queue.showToast('复制成功');
					}
				});
			},
			saveImg() {
				let that = this;
				let imgArr = []
				imgArr.push(that.image);
				//预览图片
				uni.previewImage({
					urls: imgArr,
					current: imgArr[0]
				});
				// uni.saveImageToPhotosAlbum({
				// 	filePath: that.image,
				// 	success(res) {
				// 		that.$queue.showToast('保存成功');
				// 	}
				// });
			},
			rests() {
				uni.showToast({
					title: '已刷新请再次长按识别',
					mask: false,
					duration: 1500,
					icon: 'none'
				});
				window.location.reload();
			},
			// 在线客服
			goChat() {
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: '/my/setting/chat'
					});
				} else {
					this.goLoginInfo();
				}
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/public/login'
				});
			},
		}
	};
</script>

<style>
	/* @import '../../static/css/index.css'; */

	page {
		background: #F5F5F5;
	}
</style>