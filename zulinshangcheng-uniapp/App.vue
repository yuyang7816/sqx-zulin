<script>
	export default {
		onLaunch: function() {
			uni.setStorageSync('showGoodInfoState', true)
			setInterval(d => { //定时器，定时去调取聊天未读消息
				let userId = uni.getStorageSync('userId')
				if (userId) {
					this.$Request.getT('/app/chat/selectChatCount').then(res => {
						if (res.code === 0) {
							let messageCount = res.data.messageCount
							let adminmessageCount = res.data.chatCount
							let AllchatCount = parseInt(parseInt(res.data.chatCount) + parseInt(res.data.messageCount))
							console.log(AllchatCount, "AllchatCount")
							uni.setStorageSync('AllchatCount', AllchatCount)
							uni.setStorageSync('messageCount', messageCount)
							uni.setStorageSync('adminmessageCount', adminmessageCount)
							if (AllchatCount == 0) {
								uni.removeTabBarBadge({
									index: 3
								})
								return;
							}
							uni.setTabBarBadge({
								index: 3,
								text: AllchatCount + ''
							})
						}
					});
				}
			}, 2000);

			//#ifdef APP-PLUS
			// APP检测更新 具体打包流程可以参考：https://ask.dcloud.net.cn/article/35667
			plus.screen.lockOrientation('portrait-primary'); //竖屏正方向锁定
			//获取是否热更新过
			const updated = uni.getStorageSync('updated'); // 尝试读取storage
			console.log(updated, "updated")
			if (updated.completed === true) {
				// 如果上次刚更新过
				// 删除安装包及安装记录
				console.log('安装记录被删除，更新成功');
				uni.removeSavedFile({
					filePath: updated.packgePath,
					success: res => {
						uni.removeStorageSync('updated');
					}
				});
			} else if (updated.completed === false) {
				uni.removeStorageSync('updated');
				plus.runtime.install(updated.packgePath, {
					force: true
				});
				uni.setStorage({
					key: 'updated',
					data: {
						completed: true,
						packgePath: updated.packgePath
					},
					success: res => {
						console.log('成功安装上次的更新，应用需要重启才能继续完成');
					}
				});
				uni.showModal({
					title: '温馨提示',
					content: '应用将重启以完成更新',
					showCancel: false,
					complete(ret) {
						if (ret.confirm) {
							plus.runtime.restart();
						}
					}
				})
			} else {
				//获取当前系统版本信息
				plus.runtime.getProperty(plus.runtime.appid, widgetInfo => {
					//请求后台接口 解析数据 对比版本
					that.$Request.getT('/app/user/selectNewApp').then(res => {
						console.log("/app/user/selectNewApp___:" + JSON.stringify(res));
						res = res.data[0];
						if (res.wgtUrl && widgetInfo.version < res.version) {
							let downloadLink = '';
							let androidLink = res.androidWgtUrl;
							let iosLink = res.iosWgtUrl;
							let ready = false;
							//校验是是不是热更新
							if (res.wgtUrl.match(RegExp(/.wgt/))) {
								// 判断系统类型
								if (plus.os.name.toLowerCase() === 'android') {
									console.log('安卓系统');
									if (androidLink && androidLink !== '#') {
										// 我这里默认#也是没有地址，请根据业务自行修改
										console.log('发现下载地址');
										// 安卓：创建下载任务
										if (androidLink.match(RegExp(/.wgt/))) {
											console.log('确认wgt热更新包');
											downloadLink = androidLink;
											ready = true;
										} else {
											console.log('安卓推荐.wgt强制更新，.apk的强制更新请您自行修改程序');
										}
									} else {
										console.log('下载地址是空的，无法继续');
									}
								} else {
									console.log('苹果系统');
									if (iosLink && iosLink !== '#') {
										// 我这里默认#也是没有地址，请根据业务自行修改
										console.log('发现下载地址');
										// 苹果(A)：进行热更新（如果iosLink是wgt更新包的下载地址）判断文件名中是否含有.wgt
										if (iosLink.match(RegExp(/.wgt/))) {
											console.log('确认wgt热更新包');
											downloadLink = iosLink;
											ready = true;
										} else {
											console.log('苹果只支持.wgt强制更新');
										}
									} else {
										console.log('下载地址是空的，无法继续');
									}
								}
								if (ready) {
									console.log('任务开始');
									let downloadTask = uni.downloadFile({
										url: downloadLink,
										success: res => {
											if (res.statusCode === 200) {
												// 保存下载的安装包
												console.log('保存安装包');
												uni.saveFile({
													tempFilePath: res.tempFilePath,
													success: res => {
														const packgePath = res
															.savedFilePath;
														// 保存更新记录到stroage，下次启动app时安装更新
														uni.setStorage({
															key: 'updated',
															data: {
																completed: false,
																packgePath: packgePath
															},
															success: () => {
																console
																	.log(
																		'成功保存记录'
																	);
															}
														});
														// 任务完成，关闭下载任务
														console.log(
															'任务完成，关闭下载任务，下一次启动应用时将安装更新'
														);
														downloadTask.abort();
														downloadTask = null;
													}
												});
											}
										}
									});
								} else {
									console.log('下载地址未准备，无法开启下载任务');
								}
							} else {
								//不是热更新是在线更新 校验是否强制升级
								if (res.method == 'true') {
									uni.showModal({
										title: '立即更新',
										content: '应用将重启以完成更新',
										showCancel: false,
										complete(ret) {
											if (ret.confirm) {
												that.$queue.showLoading('下载中...');
												if (uni.getSystemInfoSync().platform ==
													'android') {
													uni.downloadFile({
														url: androidLink,
														success: downloadResult => {
															if (downloadResult
																.statusCode ===
																200) {
																plus.runtime
																	.install(
																		downloadResult
																		.tempFilePath, {
																			force: false
																		},
																		d => {
																			console
																				.log(
																					'install success...'
																				);
																			plus.runtime
																				.restart();
																		},
																		e => {
																			console
																				.error(
																					'install fail...'
																				);
																		}
																	);
															}
														}
													});
												}
												if (uni.getSystemInfoSync().platform ==
													'ios') {
													plus.runtime.openURL(iosLink, function(
														res) {});
												}
											}
										}
									})
								} else {
									uni.showModal({
										title: '发现新版本',
										content: res.des,
										confirmText: '立即更新',
										cancelText: '下次更新',
										complete(ret) {
											if (ret.confirm) {
												that.$queue.showLoading('下载中...');
												if (uni.getSystemInfoSync().platform ==
													'android') {
													uni.downloadFile({
														url: androidLink,
														success: downloadResult => {
															if (downloadResult
																.statusCode ===
																200) {
																plus.runtime
																	.install(
																		downloadResult
																		.tempFilePath, {
																			force: false
																		},
																		d => {
																			console
																				.log(
																					'install success...'
																				);
																			plus.runtime
																				.restart();
																		},
																		e => {
																			console
																				.error(
																					'install fail...'
																				);
																		}
																	);
															}
														}
													});
												}
												if (uni.getSystemInfoSync().platform ==
													'ios') {
													plus.runtime.openURL(iosLink, function(
														res) {});
												}
											}
										}
									})
								}
							}
						}
					});
				});
			}
			//#endif
		},
		onShow: function() {
			console.log('App Show')
			// #ifdef APP-PLUS
			let clientid = plus.push.getClientInfo().clientid;
			let userId = uni.getStorageSync('userId');
			if (userId) {
				console.log(userId, "userId")
				console.log(clientid, 'clientidclientid')
				let data = {
					userId: userId,
					clientId: clientid,
				}
				this.$Request.getT('/app/user/updateClientId', data).then(
					res => {
						console.log(res, "updateClientId")
					});
			}
			//#endif
			//关键词过滤
			this.$Request.getT('/app/common/type/290').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('chatSearchKeys', res.data.value)
					}
				}
			});
			// 客服类型(1:电话，2:企业微信，3:客服二维码)
			this.$Request.getT('/app/common/type/296').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('kefuType', res.data.value)
					}
				}
			});
			// 客服二维码
			this.$Request.getT('/app/common/type/1').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('kefuQrCode', res.data.value)
					}
				}
			});
			// 公众号二维码
			this.$Request.getT('/app/common/type/2').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('gongzhonghQrCode', res.data.value)
					}
				}
			});
			// 客服微信号
			this.$Request.getT('/app/common/type/44').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('kefuWx', res.data.value)
					}
				}
			});
			// 海报邀请语
			this.$Request.getT('/app/common/type/116').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('invitationText', res.data.value)
					}
				}
			});
			// 客服电话
			this.$Request.getT('/app/common/type/298').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('kefuPhone', res.data.value)
					}
				}
			});
			//企业微信客服链接
			this.$Request.get('/app/common/type/300 ').then(res => {
				if (res.code == 0) {
					this.$queue.setData('kefuUrl', res.data.value);
				}
			});
			//企业微信客服appid
			this.$Request.get('/app/common/type/297').then(res => {
				if (res.code == 0) {
					this.$queue.setData('kefuAppId', res.data.value);
				}
			});
			//商家自提地址
			this.$Request.get('/app/common/type/527').then(res => {
				if (res.code == 0) {
					this.$queue.setData('shopAddressZt', res.data.value);
				}
			});
			//商家归还地址
			this.$Request.get('/app/common/type/521').then(res => {
				if (res.code == 0) {
					this.$queue.setData('shopAddress', res.data.value);
				}
			});
		},

		onHide: function() {
			console.log('App Hide')
			uni.setStorageSync('showGoodInfoState', true)
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	@import "uview-ui/index.scss";
	@import 'components/colorui/main.css';
	@import 'components/colorui/icon.css';
	@import './static/css/simplepro.css';

	page {
		background-color: #FFFFFF;
		color: #343546;
	}

	.bg {
		background-color: #F7F7F7;
	}
</style>