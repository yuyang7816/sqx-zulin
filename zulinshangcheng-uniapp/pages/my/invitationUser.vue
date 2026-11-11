<template>
	<view class="content">
		<view class="view1" v-bind:style="{backgroundImage: 'url('+backgroundImage+')'}">
			<view style="padding-top: 820upx;" @longpress="logoTime(userImageUrl)">
				<view style="width: 100%;height: 150upx;display: flex;background: #FFFFFF;padding: 20upx 10upx;">
					<image :src="userImageUrl" style="border-radius: 100upx;width: 100upx;height: 100upx;margin-left: 30upx;"></image>
					<view class="login-view-text1" style="margin-left: 30upx;width: 59%;">
						<view style="font-size: 16px;">{{ nickName }}</view>
						<view style=" font-size: 12px;margin-top: 20upx;">邀请码:{{invitationCode}}</view>
					</view>
					<canvas canvas-id="qrcode" style="width: 140upx;height: 130upx;" />
				</view>
			</view>
		</view>
		<view style="display: flex;flex-direction: row; padding: 40upx;justify-content: center;">
			<!-- #ifdef H5 -->
			<button @click="share()" type="default" style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%;">文案推广</button>
			<button @tap="showModal()" type="default" style="background-color: #557EFD;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%; margin-left: 40upx;">生成海报</button>
			<!-- #endif -->
			<!-- #ifdef APP-PLUS -->
			<button v-if="isShowWxAPPShare=='是'" @click="shareWeiXin()" type="default" style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%;">文案推广</button>
			<button v-if="isShowWxAPPShare=='否'" @click="sharAPPUrl()" type="default" style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%;">文案推广</button>
			
			<button @tap="showModal()" type="default" style="background-color: #557EFD;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 50%; margin-left: 40upx;">生成海报</button>
			<!-- #endif -->
			
			<!-- #ifdef MP-WEIXIN -->
			<button open-type="share" style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 40%;">一键分享</button>
			<!-- <button @click="share()" type="default" style="background-color: #FFCB49;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 40%;">文案推广</button> -->
			<button @tap="onSaveImg()" type="default" style="background-color: #557EFD;font-size: 16px;font-weight: bold;color: #FFFFFF; width: 40%;">生成海报</button>
			
			<!-- #endif -->
			<!-- 生成海报 -->
			<!-- 图片展示由自己实现 -->
			<view class="flex_row_c_c modalView" :class="qrShow?'show':''" @tap="hideQr()">
				<view class="flex_column">
					<view class="backgroundColor-white padding1vh border_radius_10px">
						<image :src="poster.finalPath || ''" mode="widthFix" class="posterImage"></image>
					</view>
					<view class="flex_row marginTop2vh">
						<!-- #ifdef H5 -->
						<button type="primary" size="mini">长按上方图片保存</button>
						<!-- #endif -->
						<!-- #ifndef H5 -->
						<button type="primary" size="mini" @tap.prevent.stop="saveImage()">保存图片</button>
						<!-- #endif -->
					</view>
				</view>
			</view>
			<!-- 画布 -->
			<view class="hideCanvasView">
				<canvas class="hideCanvas" canvas-id="default_PosterCanvasId" :style="{width: (poster.width||10) + 'px', height: (poster.height||10) + 'px'}"></canvas>
			</view>
		</view>
		<!-- #ifndef MP-WEIXIN -->
		<tki-qrcode ref="qrcode" :val="url" :size="200" background="#fff" foreground="#000" pdground="#000" :onval="true"
		 :loadMake="true" @result="qrR" :show="false"></tki-qrcode>
		<view class="cu-modal" :class="modalName == 'Image' ? 'show' : ''" @tap="hideModal">
			<view class="cu-dialog" v-if="backgroundImage && erweimapath && haibaoShow" @tap="hideModal">
				<view class="bg-img">
					<wm-poster @success="posterSuccess" :imgSrc="backgroundImage" :Referrer="'我的邀请码:'+invitationCode" :QrSrc="erweimapath"
					 :Title="tuiguang" :LineType="false"></wm-poster>
				</view>
			</view>
		</view>
		<!-- #endif -->
		
		<!-- #ifdef MP-WEIXIN -->
		<view @tap="hideModal" :class="modalName == 'Image' ? 'show' : ''" class="modal"
			style="text-align: center;display: flex;justify-content: center;">
			<view style="width:100%;margin: auto;">
				<image :src="h5SaveImg" mode="widthFix" style="width: 90%;"></image>
			</view>
		</view>
		<canvas canvas-id="poster" class="poster_canvas"></canvas>
		<!-- #endif -->
	</view>
</template>

<script>
	let settingWritePhotosAlbum = false;
	import tkiQrcode from '@/components/tki-qrcode/tki-qrcode.vue';
	import appShare from '@/utils/share.js';
	import wmPoster from '@/components/wm-poster/wm-posterorders.vue';
	import uQRCode from "../../js_sdk/Sansnn-uQRCode/uqrcode.js"
	import _app from '../../js_sdk/QuShe-SharerPoster/QS-SharePoster/app.js';
	import configdata from '../../common/config.js';
	// import {
	// 	getSharePoster
	// } from '../../js_sdk/QuShe-SharerPoster/QS-SharePoster/QS-SharePoster.js';
	export default {
		components: {
			tkiQrcode,
			wmPoster,
			// getSharePoster
		},
		data() {
			return {
				erweimapath: '',
				poster: {},
				qrShow: false,
				haibaoImg: null,
				haibaoShow: false,
				modalName: '',
				canvasId: 'default_PosterCanvasId',
				imageUrl: '',
				userImageUrl: '',
				isShowWxAPPShare:'否',
				nickName: '',
				invitationCode: '',
				backgroundImage: '',
				tuiguang: '快来和我一起打游戏吧!',
				tuiguang1: '快来和我一起打游戏吧!',
				url: ''
			};
		},
		onLoad() {
			this.getBackImageList();

			let avatar = this.$queue.getData('avatar');
			if (avatar && avatar !== 'undefined') {
				this.userImageUrl = avatar;
			} else {
				this.userImageUrl = '/static/logo.png';
			}

			this.$Request.getT('/app/common/type/116').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value) {
						this.tuiguang = res.data.value;
					}
				}
			});
			//文案推广
			this.$Request.getT('/app/common/type/101').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value) {
						this.tuiguang1 = res.data.value;
					}
				}
			});
			//检测书否开启APP微信分享
			this.$Request.getT('/app/common/type/136').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value) {
						this.isShowWxAPPShare = res.data.value;
					}
				}
			});

			this.invitationCode = this.$queue.getData('invitationCode');
			// #ifndef H5
			this.$Request.getT('/app/common/type/25').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value) {
						this.url = res.data.value;
					}
				}
			});
			//#endif
			// #ifdef H5
			this.$Request.getT('/app/common/type/141').then(res => {
				if (res.code === 0) {
					if (res.data && res.data.value&& res.data.value=='是') {
						this.$Request.getT('/app/common/type/25').then(ress => {
							if (ress.code === 0) {
								if (ress.data && ress.data.value) {
									this.url = ress.data.value;
								}
							}
						});
					}else{
						this.url = this.$queue.publicYuMing() + '/?invitation=' + this.invitationCode;
					}
				}
			});
			
			//#endif

			let userName = this.$queue.getData('userName');
			if (userName && userName !== 'undefined') {
				this.nickName = userName;
			} else {
				this.nickName = '游客';
			}

		},
		onShareAppMessage(res) {
			return {
				path: '/pages/index/index?invitation=' + this.invitationCode, //这是为了传参   onload(data){let id=data.id;} 
				title: this.tuiguang,
				imageUrl: this.backgroundImage
			}
		},
		methods: {
			posterSuccess(haibaoImg) {
				this.haibaoImg = haibaoImg;
				this.modalName = 'Image';
			},
			showModal() {
				if (!this.haibaoImg) {
					this.haibaoShow = true;
					this.$queue.showLoading('海报生成中...');
				} else {
					this.modalName = 'Image';
				}
			},
			hideModal() {
				this.modalName = null;
			},
			qrR(path) {
				this.erweimapath = path;
			},
			getBackImageList() {
				this.$Request.getT('/app/banner/selectBannerList?state=-1&classify=5').then(res => {
					if (res.code === 0) {
						this.backgroundImage = res.data[0].imageUrl;
					}
				});
				this.make();

			},
			make() {
				uQRCode.make({
					canvasId: 'default_PosterCanvasId',
					componentInstance: this,
					text: this.url,
					size: 68,
					margin: 4,
					backgroundColor: '#ffffff',
					foregroundColor: '#000000',
					fileType: 'jpg',
					correctLevel: uQRCode.errorCorrectLevel.H,
					success: res => {
						console.log(res)
					}
				})
			},
			shareWeiXin() {
				this.$Request.getT('/app/common/type/103').then(res => {
					if (res.code === 0) {
						if (res.data && res.data.value) {
							let relationId = this.invitationCode;
							let shareData = {
								shareUrl: this.url,
								shareTitle: res.data.value,
								shareContent: '邀请码：' + relationId + '，' + res.data.value,
								shareImg: this.$queue.publicYuMing() + '/logo.png',
								type: 0
							};
							appShare(shareData, res => {
								console.log('分享成功回调', res);
							});
						}
					}
				});

			},
			share() {
				this.sharurl();
			},
			sharAPPUrl() {
				let that = this;
				let relationId = this.invitationCode;
				uni.showModal({
					title: '文案推广',
					content: this.tuiguang1+ relationId +'\n'+this.url,
					showCancel: true,
					cancelText: '关闭',
					confirmText: '一键复制',
					success: res => {
						if (res.confirm) {
							uni.setClipboardData({
								data: this.tuiguang1+ relationId +'\n'+this.url,
								success: function() {
									console.log('success');
									that.$queue.showToast('文案复制成功');
								}
							});
						}
					}
				});
			},
			sharurl() {
				let that = this;
				// this.$queue.showLoading('加载中...');
				let relationId = this.invitationCode;
				uni.showModal({
					title: '文案推广',
					content: this.tuiguang1+'\n'+this.url,
					showCancel: true,
					cancelText: '关闭',
					confirmText: '一键复制',
					success: res => {
						if (res.confirm) {
							uni.setClipboardData({
								data: this.tuiguang1 +'\n'+this.url,
								success: function() {
									console.log('success');
									that.$queue.showToast('复制成功');
								}
							});
						}
					}
				});
			},
			logoTime(urlList) {
				uni.previewImage({
					current: 0,
					urls: urlList,
					loop: true,
					longPressActions: {
						itemList: ['收藏'],
						itemColor: "#007AFF"
					}
				})
			},
			goList() {
				let userId = this.$queue.getData('userId');
				this.$Request.getT('/app/invite/selectInviteAndPoster?userId=' + userId).then(res => {
					if (res.code === 0) {
						if (res.data.user.imageUrl) {
							this.userImageUrl = res.data.user.imageUrl;
						} else {
							this.userImageUrl = '/static/img/common/logo.jpg';
						}

						if (res.data.user.nickName) {
							this.nickName = res.data.user.nickName;
						} else {
							this.nickName = res.data.user.phone;
						}
						this.invitationCode = res.data.user.invitationCode;
						this.imageUrl = res.data.url;

					}
				});
			},
			async shareFc() {
				let _this = this;
				try {
					const d = await getSharePoster({
						type: 'testShareType',
						backgroundImage: _this.backgroundImage,
						posterCanvasId: _this.canvasId, //canvasId
						delayTimeScale: 20, //延时系数
						drawArray: ({
							bgObj,
							type,
							bgScale
						}) => {
							const dx = bgObj.width * 0.3;
							const fontSize = bgObj.width * 0.045;
							const lineHeight = bgObj.height * 0.04;
							//可直接return数组，也可以return一个promise对象, 但最终resolve一个数组, 这样就可以方便实现后台可控绘制海报
							return new Promise((rs, rj) => {
								rs([{
										type: 'custom',
										setDraw(Context) {
											Context.setFillStyle('black');
											Context.setGlobalAlpha(0.3);
											Context.fillRect(0, bgObj.height - bgObj.height * 0.2, bgObj.width, bgObj.height * 0.2);
											Context.setGlobalAlpha(1);
										}
									},
									{
										type: 'text',
										fontStyle: 'italic',
										text: '邀请码:' + _this.invitationCode,
										size: fontSize,
										color: 'white',
										alpha: 1,
										textAlign: 'left',
										textBaseline: 'middle',
										infoCallBack(textLength) {
											return {
												dx: bgObj.width - textLength - fontSize,
												dy: bgObj.height - lineHeight * 3
											}
										},
										serialNum: 0,
										id: 'tag1' //自定义标识
									},
									{
										type: 'qrcode',
										text: _this.url,
										size: bgObj.width * 0.2,
										dx: bgObj.width * 0.05,
										dy: bgObj.height - bgObj.width * 0.25
									}
								]);
							})
						},
						setCanvasWH: ({
							bgObj,
							type,
							bgScale
						}) => { // 为动态设置画布宽高的方法，
							_this.poster = bgObj;
						}
					});
					//_app.log('海报生成成功, 时间:' + new Date() + '， 临时路径: ' + d.poster.tempFilePath)
					_this.poster.finalPath = d.poster.tempFilePath;
					_this.qrShow = true;
				} catch (e) {
					_app.hideLoading();
				}
			},
			saveImage() {
				uni.saveImageToPhotosAlbum({
					filePath: this.poster.finalPath,
					success(res) {
						_app.showToast('保存成功');
					}
				})
			},
			hideQr() {
				this.qrShow = false;
			},
			
			// 微信小程序保存图片
			async onSaveImg() {
				// #ifdef MP-WEIXIN
				let imgUrl = await this.createPoster();
				uni.showLoading({
					title: '海报下载中'
				});
				if (settingWritePhotosAlbum) {
					uni.getSetting({
						success: res => {
							if (res.authSetting['scope.writePhotosAlbum']) {
								uni.saveImageToPhotosAlbum({
									filePath: imgUrl,
									success: () => {
										uni.hideLoading();
										uni.showToast({
											title: '保存成功'
										});
									}
								});
							} else {
								uni.showModal({
									title: '提示',
									content: '请先在设置页面打开“保存相册”使用权限',
									confirmText: '去设置',
									cancelText: '算了',
									success: data => {
										if (data.confirm) {
											uni.hideLoading();
											uni.openSetting();
										}
									}
								});
							}
						}
					});
				} else {
					uni.hideLoading();
					settingWritePhotosAlbum = true;
					uni.authorize({
						scope: 'scope.writePhotosAlbum',
						success: () => {
							uni.saveImageToPhotosAlbum({
								filePath: imgUrl,
								success: () => {
									uni.hideLoading();
									uni.showToast({
										title: '保存成功'
									});
								}
							});
						}
					});
				}
				// #endif
			},
			//生成海报
			createPoster() {
				let that = this;
				return new Promise((resolve, reject) => {
					uni.showLoading({
						title: '海报生成中'
					});
					const ctx = uni.createCanvasContext('poster');
					ctx.fillRect(0, 0, 375, 673);
					ctx.setFillStyle("#FFF");
					ctx.fillRect(0, 0, 375, 673);
					let imgUrl = that.backgroundImage;
					uni.downloadFile({
						url: imgUrl,
						success: (res) => {
							if (res.statusCode === 200) {
								console.log(that.config("APIHOST1") + '/app/invite/mpCreateQr?invitationCode=' +that.invitationCode)
								uni.downloadFile({
									url: that.config("APIHOST1") + '/app/invite/mpCreateQr?invitationCode=' + that.invitationCode,
									success: (res2) => {
										uni.hideLoading();
										if (res.statusCode === 200) {
											ctx.drawImage(res.tempFilePath, 0, 0, 375,
												500);
											// 长按识别二维码访问
											let textTop = 0;
											ctx.setFontSize(19);
											ctx.setFillStyle('#333');
											ctx.fillText("长按识别图中二维码", 17, textTop + 590);
											// 二维码
											ctx.drawImage(res2.tempFilePath, 238, textTop +
												526, 120, 120);
											ctx.draw(true, () => {
												// canvas画布转成图片并返回图片地址
												uni.canvasToTempFilePath({
													canvasId: 'poster',
													width: 375,
													height: 673,
													success: (res) => {
														console.log(
															"海报制作成功！"
														);
														resolve(res
															.tempFilePath
														);
													},
													fail: () => {
														uni
															.hideLoading();
														reject();
													}
												})
											});
										} else {
											uni.hideLoading();
											uni.showToast({
												title: '海报制作失败，图片下载失败',
												icon: 'none'
											});
										}
									},
									fail: err => {
										console.log(err)
										uni.hideLoading();
										uni.showToast({
											title: '海报制作失败，图片下载失败',
											icon: 'none'
										});
									},
									complete: com => {
										console.log(com)
										uni.showToast({
											title: com,
											icon: 'none'
										});
									},
								});
							} else {
								uni.hideLoading();
								uni.showToast({
									title: '海报制作失败，图片下载失败',
									icon: 'none'
								});
							}
						},
						fail: err => {
							// that.yu.toast(err)
							console.log(err)
							uni.hideLoading();
							uni.showToast({
								title: '海报制作失败，图片下载失败',
								icon: 'none',
							});
						}
					});
				});
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
		}
	}
</script>

<style>
	.modal {
		position: fixed;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		z-index: 1110;
		opacity: 0;
		outline: 0;
		text-align: center;
		-ms-transform: scale(1.185);
		transform: scale(1.185);
		backface-visibility: hidden;
		perspective: 2000upx;
		/* background: rgba(0, 0, 0, 0.6); */
		background: #FFFFFF;
		transition: all 0.3s ease-in-out 0s;
		pointer-events: none;
	}
	
	.modal.show {
		opacity: 1;
		transition-duration: 0.3s;
		-ms-transform: scale(1);
		transform: scale(1);
		overflow-x: hidden;
		overflow-y: auto;
		pointer-events: auto;
	}
	
	page {
		background: #FFFFFF;
	}

	.view1 {
		border-radius: 15upx;
		background-size: 100%;
		margin: 20upx 20upx 0 20upx;
	}

	.hideCanvasView {
		position: relative;
	}

	.hideCanvas {
		position: fixed;
		top: -99999upx;
		left: -99999upx;
		z-index: -99999;
	}

	.flex_row_c_c {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
	}

	.modalView {
		width: 100%;
		height: 100%;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		opacity: 0;
		outline: 0;
		transform: scale(1.2);
		perspective: 2500upx;
		/* background: rgba(0, 0, 0, 0.6); */
		background: #FFFFFF;
		transition: all .3s ease-in-out;
		pointer-events: none;
		backface-visibility: hidden;
		z-index: 999;
	}

	.modalView.show {
		opacity: 1;
		transform: scale(1);
		pointer-events: auto;
	}

	.flex_column {
		display: flex;
		flex-direction: column;
	}

	.backgroundColor-white {
		background-color: white;
	}

	.border_radius_10px {
		border-radius: 10px;
	}

	.padding1vh {
		padding: 1vh;
	}

	.posterImage {
		width: 60vw;
	}

	.flex_row {
		display: flex;
		flex-direction: row;
	}

	.marginTop2vh {
		margin-top: 2vh;
	}
	
	.poster_canvas {
		width: 750upx;
		height: 1334upx;
		position: fixed;
		top: -10000upx;
		left: 0;
	}
</style>