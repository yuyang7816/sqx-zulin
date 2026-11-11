<template>
	<view class="chat-page">
		<!-- #ifdef H5 -->
		<view class="margin-sm radius chat-fixed-head"
			style="position: fixed;top: 0rpx;left: 0; right: 0;z-index: 9;padding-top: 90rpx;background: transparent;">
		<!-- #endif -->
			<!-- #ifndef H5 -->
			<view class="radius chat-fixed-head"
				style="position: fixed;top: 0rpx;left: 0; right: 0;z-index: 9;background: transparent;width: calc(100% - 20rpx);margin: auto;padding-top: 20rpx;">
			<!-- #endif -->
				<view class="chat-head-wrap" style="background: transparent;">
					<view class="flex align-center padding chat-notice" style="background: #fff;">
						<image class="chat-notice__icon" src="../../static/images/msg/alert.png" style="width: 88upx;height: 88upx;"></image>
						<view class="text-sm margin-left-sm chat-notice__text" style="color: #333;width: 84%;">
							{{liaotian}}<text style="color: #FFE60F;" @click="navigate('/my/feedback')">去投诉</text>
						</view>
					</view>
					<view class="radius padding chat-goods-card" style="background: #fff;margin-top: 20rpx;"
						v-if="Object.keys(goodInfo).length > 0"
						@click="navigate('/subpackage/goodDetail/goodDetail?goodId='+goodInfo.id)">
						<order-bar :order="goodInfo" @getHeight="orderHeight = $event" style="padding: 0;"></order-bar>
					</view>
				</view>
			</view>

			<view class="chat-scroll-area" style="width: 100%;padding-bottom: 140rpx;" @touchstart="hideDrawer"
				:style="{'paddingTop':Object.keys(goodInfo).length > 0 ?'58%':'28%'}">
				<view class="chat-surface"></view>
				<view class="message-group" style="display: flex;flex-direction: column;" v-for="(item,index) in ListItem">
					<view class="message-time" style="margin-top: 15rpx;width: 100%;text-align: center;font-size: 26rpx;color: #999999;"
						v-if="item.createTime">
						{{item.createTime}}
					</view>
					<view class="message-row message-row--other" style="width: 83%;margin-right: 15%;" v-if="item.storeId == 0">
						<view class="chat-listitem message-item message-item--other" style="float: left;margin-left: 10rpx;">
							<view @click="navigate('/subpackage/dynamic/dynamic?userId='+item.userId)">
								<image :src="item.avatar ? item.avatar : '/static/logo.png'" class="chat-listitem-image"></image>
							</view>
							<view v-if="item.content && item.type === 1" @longpress="copy(item.content)" class="chat-listitem-text message-bubble"
								style="margin-left: 20rpx;">{{item.content}}</view>
							<image class="message-image" @tap="viewImg(item.content)" v-if="item.content && item.type === 2" :src="item.content"
								style="height: 200rpx;width: 200rpx;margin-left: 20rpx;"></image>
							<image class="chat-listitem-text message-emoji" @tap="viewImg(item.content)" v-if="item.content && item.type === 4"
								:src="'https://xxpw.xianmxkj.com/emoji/' +item.content"
								style="height: 80rpx;width: 80rpx;margin-left: 20rpx;"></image>
							<view v-if="item.content && item.type === 3" class="chat-listitem-text bubble voice message-bubble message-bubble--voice"
								:class="item.playMsgid?'play':''" style="margin-left: 20rpx;display: flex;align-items: center;"
								@tap="playVoice(item.content,index)">
								<image v-if="!item.playMsgid" src="../../static/pw/laba.png"
									style="width: 35upx;height: 40upx;margin-right: 10rpx;"></image>
								<image v-if="item.playMsgid" src="../../static/pw/labagif.gif"
									style="width: 35upx;height: 40upx;margin-right: 10rpx;"></image>
								<view class="length">{{item.voiceLength ? item.voiceLength : 0}}</view>
							</view>
						</view>
					</view>

					<view class="message-row message-row--self" style="width: 83%;margin-left: 15%;" v-if="item.userId && userId == item.userId&&item.storeId != 0">
						<view class="chat-listitem message-item message-item--self" style="float: right;">
							<view v-if="item.content && item.type === 1" @longpress="copy(item.content)" class="chat-listitem-text message-bubble"
								style="margin-right: 20rpx;">{{item.content}}</view>
							<image class="message-image" @tap="viewImg(item.content)" v-if="item.content && item.type === 2" :src="item.content"
								style="height: 200rpx;width: 200rpx;margin-right: 20rpx;"></image>
							<image class="chat-listitem-text message-emoji" @tap="viewImg(item.content)" v-if="item.content && item.type === 4"
								:src="'https://xxpw.xianmxkj.com/emoji/' +item.content"
								style="height: 80rpx;width: 80rpx;margin-right: 20rpx;">
							</image>
							<view v-if="item.content && item.type === 3" class="chat-listitem-text bubble voice message-bubble message-bubble--voice"
								:class="item.playMsgid ?'play':''" style="margin-right: 20rpx;display: flex;align-items: center;"
								@tap="playVoice(item.content,index)">
								<view class="length">{{item.voiceLength ? item.voiceLength : 0}}
								</view>
								<image v-if="!item.playMsgid" src="../../static/pw/labaleft.png"
									style="width: 35upx;height: 40upx;margin-left: 10rpx;"></image>
								<image v-if="item.playMsgid " src="../../static/pw/labagif.gif"
									style="width: 35upx;height: 40upx;margin-left: 10rpx;"></image>
							</view>
							<view class="chat-listitem-text1 message-card message-card--location" style="margin-right: 20rpx;" v-if="item.content && item.type === 5">
								<view class="padding-sm">{{item.content.address}}</view>
								<map style="width:520upx; height: 120px;" :latitude="item.content.latitude"
									:longitude="item.content.longitude">
								</map>
							</view>
							<view class="message-self-avatar-wrap">
								<image :src="imageUrl" class="chat-listitem-image message-self-avatar"></image>
							</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 抽屉栏 -->
			<view class="popup-layer chat-popup-layer" :class="popupLayerClass" @touchmove.stop.prevent="discard">
				<!-- 表情 -->
				<swiper class="emoji-swiper" indicator-active-color="#cccccc" :class="{hidden:hideEmoji}" indicator-dots="true"
					duration="150">
					<swiper-item v-for="(page1,pid) in emojiList" :key="pid">
						<view v-for="(em,eid) in page1" :key="eid" @tap="setChatSave(4,em.url)">
							<image mode="widthFix" :src="'https://xxpw.xianmxkj.com/emoji/'+em.url"></image>
						</view>
					</swiper-item>
				</swiper>
				<!-- 更多功能 相册-拍照-红包 -->
				<view class="more-layer" :class="{hidden:hideMore}">
					<view class="list">
						<!-- 上传图片 -->
						<view>
							<view class="box" @click="chooseImage(['album'])">
								<view class="icon tupian2" style="color: #000000;"></view>
							</view>
							<view style="width: 100%;text-align: center;color: #000000;">上传图片</view>
						</view>
						<!-- <view @click="addToBlacklist">
							<view class="box">
								<image src="/static/images/msg/hmd.png" mode="" style="width: 65rpx;height: 65rpx;"></image>
							</view>
							<view style="width: 100%;text-align: center;color: #000000;">加入黑名单</view>
						</view> -->
					</view>
				</view>
			</view>
			<!-- 底部输入栏 -->
			<view class="input-box chat-input-box" :class="popupLayerClass" @touchmove.stop.prevent="discard" :style="'bottom: {bottom}px'">
				<!-- H5下不能录音，输入栏布局改动一下 -->
				<!-- #ifndef H5 -->
				<view class="voice">
					<view class="icon" :class="isVoice?'jianpan':'yuyin'" @tap="switchVoice"></view>
				</view>
				<!-- #endif -->
				<!-- #ifdef H5 -->
				<view class="more" @tap="showMore">
					<view class="icon add"></view>
				</view>
				<!-- #endif -->
				<view class="textbox">
					<view class="voice-mode" :class="[isVoice?'':'hidden',recording?'recording':'']" @touchstart="voiceBegin"
						@touchmove.stop.prevent="voiceIng" @touchend="voiceEnd" @touchcancel="voiceCancel">{{voiceTis}}</view>
					<view class="text-mode" :class="isVoice?'hidden':''">
						<view class="box">
							<input style="color: #000000;" cursor-spacing="15" auto-height="true" @confirm='setChatSave(1)'
								v-model="content" @focus="textareaFocus" />
						</view>
						<view class="em" @tap="chooseEmoji">
							<view class="icon biaoqing"></view>
						</view>
					</view>
				</view>
				<!-- #ifndef H5 -->
				<view class="more" @tap="showMore">
					<view class="icon add"></view>
				</view>
				<!-- #endif -->
				<view class="send" :class="isVoice?'hidden':''" @tap='setChatSave(1)'>
					<view class="btn">发送</view>
				</view>
			</view>
			<!-- 录音UI效果 -->
			<view class="record" :class="recording?'':'hidden'">
				<view class="ing" :class="willStop?'hidden':''">
					<view class="icon luyin2"></view>
				</view>
				<view class="cancel" :class="willStop?'':'hidden'">
					<view class="icon chehui"></view>
				</view>
				<view class="tis" :class="willStop?'change':''">{{recordTis}}</view>
			</view>
			<!-- <u-popup :show="isShowTips" mode="top" :closeOnClickOverlay="true" @close="close">
				<view class="tips">
					申请(获取麦克风)权限，便于您使用该功能用于联系用户
				</view>
			</u-popup>
			<u-popup :show="isShowTip" mode="top" :closeOnClickOverlay="true" @close="closes">
				<view class="tips">
					申请(获取相册)权限，便于您使用该功能用于联系用户
				</view>
			</u-popup> -->
			<!-- <u-modal :show="isShowTips" title="提示" content='申请(获取麦克风)权限，便于您使用该功能用于联系用户' @confirm="confirmBtn"></u-modal>
			<u-modal :show="isShowTip" title="提示" content='申请(获取相册)权限，便于您使用该功能用于联系用户' @confirm="confirmBtns"></u-modal> -->
			<u-popup :show="isShowTips" mode="top" :closeOnClickOverlay="true">
				<view class="tips">
					申请(获取麦克风)权限，便于您使用该功能用于联系用户
				</view>
			</u-popup>
			<u-popup :show="isShowTip" mode="top" :closeOnClickOverlay="true">
				<view class="tips">
					申请(获取相册)权限，便于您使用该功能用于联系用户
				</view>
			</u-popup>
			<u-popup :show="isShowTipC" mode="top" :closeOnClickOverlay="true">
				<view class="tips">
					申请(获取相册储存)权限，便于您使用该功能用于保存聊天图片
				</view>
			</u-popup>
		</view>
</template>

<script>
	import OrderBar from '../../components/OrderBar.vue';
	import configdata from '../../common/config.js';
	export default {
		components: {
			OrderBar,
		},
		data() {
			return {
				isShowTips: false,
				isShowTip: false,
				antsCont: '',
				goodId: '',
				goodInfo: {},
				recordLength: 0,
				initPoint: {
					identifier: 0,
					Y: 0
				},
				showChangYong: false,
				changYongList: [],
				//播放语音相关参数
				AUDIO: uni.createInnerAudioContext(),
				playMsgid: null,
				VoiceTimer: null,

				//文字消息
				textMsg: '',
				//消息列表
				isHistoryLoading: false,
				scrollAnimation: false,
				scrollTop: 0,
				scrollToView: '',
				msgList: [],
				msgImgList: [],
				myuid: 0,

				//录音相关参数
				// #ifndef H5
				//H5不能录音
				RECORDER: uni.getRecorderManager(),
				// #endif
				isVoice: false,
				voiceTis: '按住 说话',
				recordTis: "手指上滑 取消发送",
				recording: false,
				willStop: false,
				initPoint: {
					identifier: 0,
					Y: 0
				},
				recordTimer: null,
				recordLength: 0,

				//播放语音相关参数
				AUDIO: uni.createInnerAudioContext(),
				playMsgid: false,
				VoiceTimer: null,
				// 抽屉参数
				popupLayerClass: '',
				// more参数
				hideMore: true,
				bottom: '0px',

				//表情定义
				hideEmoji: true,
				emojiList: [
					[{
						"url": "100.gif",
						alt: "[微笑]"
					}, {
						"url": "101.gif",
						alt: "[伤心]"
					}, {
						"url": "102.gif",
						alt: "[美女]"
					}, {
						"url": "103.gif",
						alt: "[发呆]"
					}, {
						"url": "104.gif",
						alt: "[墨镜]"
					}, {
						"url": "105.gif",
						alt: "[哭]"
					}, {
						"url": "106.gif",
						alt: "[羞]"
					}, {
						"url": "107.gif",
						alt: "[哑]"
					}, {
						"url": "108.gif",
						alt: "[睡]"
					}, {
						"url": "109.gif",
						alt: "[哭]"
					}, {
						"url": "110.gif",
						alt: "[囧]"
					}, {
						"url": "111.gif",
						alt: "[怒]"
					}, {
						"url": "112.gif",
						alt: "[调皮]"
					}, {
						"url": "113.gif",
						alt: "[笑]"
					}, {
						"url": "114.gif",
						alt: "[惊讶]"
					}, {
						"url": "115.gif",
						alt: "[难过]"
					}, {
						"url": "116.gif",
						alt: "[酷]"
					}, {
						"url": "117.gif",
						alt: "[汗]"
					}, {
						"url": "118.gif",
						alt: "[抓狂]"
					}, {
						"url": "119.gif",
						alt: "[吐]"
					}, {
						"url": "120.gif",
						alt: "[笑]"
					}, {
						"url": "121.gif",
						alt: "[快乐]"
					}, {
						"url": "122.gif",
						alt: "[奇]"
					}, {
						"url": "123.gif",
						alt: "[傲]"
					}],
					[{
						"url": "124.gif",
						alt: "[饿]"
					}, {
						"url": "125.gif",
						alt: "[累]"
					}, {
						"url": "126.gif",
						alt: "[吓]"
					}, {
						"url": "127.gif",
						alt: "[汗]"
					}, {
						"url": "128.gif",
						alt: "[高兴]"
					}, {
						"url": "129.gif",
						alt: "[闲]"
					}, {
						"url": "130.gif",
						alt: "[努力]"
					}, {
						"url": "131.gif",
						alt: "[骂]"
					}, {
						"url": "132.gif",
						alt: "[疑问]"
					}, {
						"url": "133.gif",
						alt: "[秘密]"
					}, {
						"url": "134.gif",
						alt: "[乱]"
					}, {
						"url": "135.gif",
						alt: "[疯]"
					}, {
						"url": "136.gif",
						alt: "[哀]"
					}, {
						"url": "137.gif",
						alt: "[鬼]"
					}, {
						"url": "138.gif",
						alt: "[打击]"
					}, {
						"url": "139.gif",
						alt: "[bye]"
					}, {
						"url": "140.gif",
						alt: "[汗]"
					}, {
						"url": "141.gif",
						alt: "[抠]"
					}, {
						"url": "142.gif",
						alt: "[鼓掌]"
					}, {
						"url": "143.gif",
						alt: "[糟糕]"
					}, {
						"url": "144.gif",
						alt: "[恶搞]"
					}, {
						"url": "145.gif",
						alt: "[什么]"
					}, {
						"url": "146.gif",
						alt: "[什么]"
					}, {
						"url": "147.gif",
						alt: "[累]"
					}],
					[{
						"url": "148.gif",
						alt: "[看]"
					}, {
						"url": "149.gif",
						alt: "[难过]"
					}, {
						"url": "150.gif",
						alt: "[难过]"
					}, {
						"url": "151.gif",
						alt: "[坏]"
					}, {
						"url": "152.gif",
						alt: "[亲]"
					}, {
						"url": "153.gif",
						alt: "[吓]"
					}, {
						"url": "154.gif",
						alt: "[可怜]"
					}, {
						"url": "155.gif",
						alt: "[刀]"
					}, {
						"url": "156.gif",
						alt: "[水果]"
					}, {
						"url": "157.gif",
						alt: "[酒]"
					}, {
						"url": "158.gif",
						alt: "[篮球]"
					}, {
						"url": "159.gif",
						alt: "[乒乓]"
					}, {
						"url": "160.gif",
						alt: "[咖啡]"
					}, {
						"url": "161.gif",
						alt: "[美食]"
					}, {
						"url": "162.gif",
						alt: "[动物]"
					}, {
						"url": "163.gif",
						alt: "[鲜花]"
					}, {
						"url": "164.gif",
						alt: "[枯]"
					}, {
						"url": "165.gif",
						alt: "[唇]"
					}, {
						"url": "166.gif",
						alt: "[爱]"
					}, {
						"url": "167.gif",
						alt: "[分手]"
					}, {
						"url": "168.gif",
						alt: "[生日]"
					}, {
						"url": "169.gif",
						alt: "[电]"
					}, {
						"url": "170.gif",
						alt: "[炸弹]"
					}, {
						"url": "171.gif",
						alt: "[刀子]"
					}],
					[{
						"url": "172.gif",
						alt: "[足球]"
					}, {
						"url": "173.gif",
						alt: "[瓢虫]"
					}, {
						"url": "174.gif",
						alt: "[翔]"
					}, {
						"url": "175.gif",
						alt: "[月亮]"
					}, {
						"url": "176.gif",
						alt: "[太阳]"
					}, {
						"url": "177.gif",
						alt: "[礼物]"
					}, {
						"url": "178.gif",
						alt: "[抱抱]"
					}, {
						"url": "179.gif",
						alt: "[拇指]"
					}, {
						"url": "180.gif",
						alt: "[贬低]"
					}, {
						"url": "181.gif",
						alt: "[握手]"
					}, {
						"url": "182.gif",
						alt: "[剪刀手]"
					}, {
						"url": "183.gif",
						alt: "[抱拳]"
					}, {
						"url": "184.gif",
						alt: "[勾引]"
					}, {
						"url": "185.gif",
						alt: "[拳头]"
					}, {
						"url": "186.gif",
						alt: "[小拇指]"
					}, {
						"url": "187.gif",
						alt: "[拇指八]"
					}, {
						"url": "188.gif",
						alt: "[食指]"
					}, {
						"url": "189.gif",
						alt: "[ok]"
					}, {
						"url": "190.gif",
						alt: "[情侣]"
					}, {
						"url": "191.gif",
						alt: "[爱心]"
					}, {
						"url": "192.gif",
						alt: "[蹦哒]"
					}, {
						"url": "193.gif",
						alt: "[颤抖]"
					}, {
						"url": "194.gif",
						alt: "[怄气]"
					}, {
						"url": "195.gif",
						alt: "[跳舞]"
					}],
					[{
						"url": "196.gif",
						alt: "[发呆]"
					}, {
						"url": "197.gif",
						alt: "[背着]"
					}, {
						"url": "198.gif",
						alt: "[伸手]"
					}, {
						"url": "199.gif",
						alt: "[耍帅]"
					}, {
						"url": "200.png",
						alt: "[微笑]"
					}, {
						"url": "201.png",
						alt: "[生病]"
					}, {
						"url": "202.png",
						alt: "[哭泣]"
					}, {
						"url": "203.png",
						alt: "[吐舌]"
					}, {
						"url": "204.png",
						alt: "[迷糊]"
					}, {
						"url": "205.png",
						alt: "[瞪眼]"
					}, {
						"url": "206.png",
						alt: "[恐怖]"
					}, {
						"url": "207.png",
						alt: "[忧愁]"
					}, {
						"url": "208.png",
						alt: "[眨眉]"
					}, {
						"url": "209.png",
						alt: "[闭眼]"
					}, {
						"url": "210.png",
						alt: "[鄙视]"
					}, {
						"url": "211.png",
						alt: "[阴暗]"
					}, {
						"url": "212.png",
						alt: "[小鬼]"
					}, {
						"url": "213.png",
						alt: "[礼物]"
					}, {
						"url": "214.png",
						alt: "[拜佛]"
					}, {
						"url": "215.png",
						alt: "[力量]"
					}, {
						"url": "216.png",
						alt: "[金钱]"
					}, {
						"url": "217.png",
						alt: "[蛋糕]"
					}, {
						"url": "218.png",
						alt: "[彩带]"
					}, {
						"url": "219.png",
						alt: "[礼物]"
					}, ]
				],
				connected: false,
				connecting: false,
				msg: false,
				type4: [],
				listRight: {
					chat: {
						userHead: ""
					},
					content: "",
					type: 1,
					type: 1
				},
				content: '',
				chatId: '',
				type: 1,
				ListItem: [],
				index: 0,
				page: 1,
				pages: 1,
				size: 100,
				byUserId: 0,
				imageUrl: '/static/logo.png',
				chatConversationId: 0,
				userId: 0,
				byNickName: '',
				countDown: '',
				liaotian: '',
				tousu: '',
				//服务列表
				show: false,
				list: [],
				page: 1,
				limit: 10,
				maps: 1,
				mapsValue: '',

				musicIndex: '',
				isVip: false,
				selectBlackStatus: '',
				isShowTipC: false
			};
		},
		computed: {
			showMsg() {
				if (this.connected) {
					if (this.msg) {
						return '收到消息：' + this.msg
					} else {
						return '等待接收消息'
					}
				} else {
					return '尚未连接'
				}
			}
		},
		watch: {
			recordLength(s) {
				if (s == 31) {
					this.recording = false;
					this.voiceTis = '按住 说话';
					this.recordTis = '手指上滑 取消发送'
					this.RECORDER.stop(); //录音结束
				}
			}
		},
		onUnload() {
			uni.closeSocket()
			uni.hideLoading()
			uni.offKeyboardHeightChange(); //取消监听键盘高度变化事件，避免内存消耗
		},
		onLoad(d) {
			// 聊天配置 300
			this.$Request.getT('/app/common/type/291').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.liaotian = res.data.value
					}
				}
			});
			this.userId = this.$queue.getData('userId');
			this.connect();
			this.byNickName = d.byNickName;
			this.byUserId = d.byUserId;
			if (d.goodId) {
				this.goodId = d.goodId
				this.getGoodsById()
				uni.setStorageSync('goodId', d.goodId)
			}
			// if (d.chatConversationId) {
			// 	this.chatConversationId = d.chatConversationId;
			// 	this.getTimeOrListItem1();
			// } else {
			this.insertChat();
			// }
			if (d.byNickName) {
				if (d.byNickName !== null) {
					uni.setNavigationBarTitle({
						title: d.byNickName
					})
				} else {
					uni.setNavigationBarTitle({
						title: '聊天'
					})
				}
			}
			let image_url = this.$queue.getData('avatar');
			if (image_url && image_url !== 'undefined') {
				this.imageUrl = image_url;
			} else {
				this.imageUrl = '/static/logo.png';
			}
			//语音自然播放结束
			this.AUDIO.onEnded((res) => {
				let musicIndexs = this.musicIndex;
				if (musicIndexs != '') {
					this.ListItem[musicIndexs].playMsgid = false;
				} else {
					this.ListItem[0].playMsgid = false;
				}
			});
			// #ifndef H5
			//录音开始事件
			this.RECORDER.onStart((e) => {
				this.recordBegin(e);
			})
			//录音结束事件
			this.RECORDER.onStop((e) => {
				this.recordEnd(e);
			})
			// #endif
		},
		onShow() {

			console.log("------")
			if (this.connected || this.connecting) {
				if (this.maps == 2) {
					this.setChatSave(5, this.mapsValue)
				}
			} else {
				this.connect();
			}

			this.keyboardHeightChange()

		},
		onBackPress() {
			this.AUDIO.stop();
			// this.playMsgid = null;
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.ListItem[musicIndexs].playMsgid = false;
			}

		},
		onHide() {
			this.AUDIO.stop();
			// this.playMsgid = null;
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.ListItem[musicIndexs].playMsgid = false;
			}
			uni.closeSocket()

		},
		methods: {
			quxLy() {
				this.RECORDER.onStop((e) => {
					this.recordEnd(e);
				})
			},
			close() {
				let that = this;
				that.isShowTips = false
				console.log(that.isShowTips, 'that.isShowTips ')
			},
			closes() {
				let that = this;
				that.isShowTip = false
			},
			// 获取商品详情
			getGoodsById() {
				uni.showLoading({
					title: '加载中...'
				})
				let data = {
					id: this.goodId,
					userId: uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
				}
				this.$Request.getT('/app/goods/findById', data).then(res => {
					uni.hideLoading()
					if (res.status == 0) {
						this.goodInfo = res.data
						console.log(this.goodInfo, "this.goodInfo")
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			// 监听键盘高度事件
			keyboardHeightChange() {
				uni.onKeyboardHeightChange((res) => { //监听键盘高度变化
					// const res_keyboard = uni.getSystemInfoSync();
					// let key_height = res.height - (res_keyboard.screenHeight - res_keyboard.windowHeight +
					// 	res_keyboard.safeAreaInsets.bottom) + 10

					// this.bottom = `${res.height ?  key_height : 0 }px`;
					if (res.height !== 0) {
						this.bottom = `${res.height ?  res.height : 0 }px`;
					}
				})
			},
			// 服务投诉
			goTousu(userId) {
				uni.navigateTo({
					url: '/my/order/complain?byUserId=' + this.byUserId + '&platform=4&platformId='
				});
			},
			showChangYongSelect() {
				this.showMore();
				this.showChangYong = !this.showChangYong;
			},
			selConfirm(e) {
				// this.form.game = e[0].label
				// this.form.gameId = e[0].value
				this.content = e[0].label;
				this.setChatSave(1);
			},
			//发送位置
			goMap() {
				let that = this
				that.showMore();
				uni.chooseLocation({
					success: function(res) {
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
						that.maps = 2;
						that.mapsValue = JSON.stringify(res);
						// console.log(typeof(res))
						// return

					}
				});
			},
			insertChat() {
				let userId = this.$queue.getData('userId');
				let data = {
					focusedUserId: this.byUserId,
					userId: userId,
				}
				this.$Request.postJson('/app/chats/save', data).then(res => {
					if (res.status == 0 && res.data) {
						this.chatConversationId = res.data.chatId;
						this.getTimeOrListItem1();
					}
				});
			},
			//更多功能(点击+弹出)
			showMore() {
				this.isVoice = false;
				this.hideEmoji = true;
				if (this.hideMore) {
					this.hideMore = false;
					this.openDrawer();
				} else {
					this.hideDrawer();
				}
			},
			// 打开抽屉
			openDrawer() {
				this.popupLayerClass = 'showLayer';
			},
			// 隐藏抽屉
			hideDrawer() {
				this.popupLayerClass = '';
				setTimeout(() => {
					this.hideMore = true;
					this.hideEmoji = true;
				}, 150);
			},
			// 选择表情
			chooseEmoji() {
				this.hideMore = true;
				if (this.hideEmoji) {
					this.hideEmoji = false;
					this.openDrawer();
				} else {
					this.hideDrawer();
				}
			},
			//添加表情
			addEmoji(em) {
				this.textMsg += em.alt;
			},
			//获取焦点，如果不是选表情ing,则关闭抽屉
			textareaFocus() {
				if (this.popupLayerClass == 'showLayer' && this.hideMore == false) {
					this.hideDrawer();
				}
			},
			//替换表情符号为图片
			replaceEmoji(str) {
				let replacedStr = str.replace(/\[([^(\]|\[)]*)\]/g, (item, index) => {
					console.log("item: " + item);
					for (let i = 0; i < this.emojiList.length; i++) {
						let row = this.emojiList[i];
						for (let j = 0; j < row.length; j++) {
							let EM = row[j];
							if (EM.alt == item) {
								//在线表情路径，图文混排必须使用网络路径，请上传一份表情到你的服务器后再替换此路径 
								//比如你上传服务器后，你的100.gif路径为https://www.xxx.com/emoji/100.gif 则替换onlinePath填写为https://www.xxx.com/emoji/
								let onlinePath = 'https://xxpw.xianmxkj.com/emoji/'
								let imgstr = '<img src="' + onlinePath + this.onlineEmoji[EM.url] + '">';
								console.log("imgstr: " + imgstr);
								return imgstr;
							}
						}
					}
				});
				return '<div style="display: flex;align-items: center;word-wrap:break-word;">' + replacedStr + '</div>';
			},
			// 播放语音
			playVoice(msg, index) {
				this.playMsgid = this.ListItem[index].playMsgid
				// this.AUDIO.src = 'https://bjetxgzv.cdn.bspapp.com/VKCEYUGU-hello-uniapp/2cc220e0-c27a-11ea-9dfb-6da8e309e0d8.mp3';
				// console.log(msg,'-----------')
				// this.AUDIO.src =
				// 	'https://jiazhengv5.xianmaxiong.com/file/uploadPath/2023/04/10/4d5eaa93ac1d13d653d1f292a7b2832f.mp3'
				this.AUDIO.src = msg[0]
				if (this.playMsgid == true) {
					// this.AUDIO.stop();
					this.$nextTick(function() {
						this.AUDIO.stop();
					});
					this.ListItem[index].playMsgid = false
				} else {
					let musicIndexs = this.musicIndex;
					if (musicIndexs != '') {
						this.ListItem[musicIndexs].playMsgid = false;
					} else if (musicIndexs == 0) {
						this.ListItem[0].playMsgid = false;
					}
					this.musicIndex = index;
					this.AUDIO.stop();

					// this.AUDIO.play();
					this.$nextTick(function() {
						this.AUDIO.play();
					});
					this.ListItem[index].playMsgid = true
				}

				// this.playMsgid = index;
				// this.AUDIO.src = msg;

				// this.$nextTick(function() {
				// 	this.AUDIO.play();
				// });
			},
			// 录音开始
			voiceBegin(e) {
				if (e.touches.length > 1) {
					return;
				}
				this.initPoint.Y = e.touches[0].clientY;
				this.initPoint.identifier = e.touches[0].identifier;
				this.RECORDER.start({
					format: "mp3"
				}); //录音开始,
			},
			//录音开始UI效果
			recordBegin(e) {
				this.recording = true;
				this.voiceTis = '松开 结束';
				this.recordLength = 0;
				this.recordTimer = setInterval(() => {
					this.recordLength++;
				}, 1000)
				// if () {
				console.log(this.recordTimer, "this.recordTimer")

				// }
			},
			// 录音被打断
			voiceCancel() {
				this.recording = false;
				this.voiceTis = '按住 说话';
				this.recordTis = '手指上滑 取消发送'
				this.willStop = true; //不发送录音
				this.RECORDER.stop(); //录音结束
			},
			// 录音中(判断是否触发上滑取消发送)
			voiceIng(e) {
				if (!this.recording) {
					return;
				}
				let touche = e.touches[0];
				//上滑一个导航栏的高度触发上滑取消发送
				if (this.initPoint.Y - touche.clientY >= uni.upx2px(100)) {
					this.willStop = true;
					this.recordTis = '松开手指 取消发送'
				} else {
					this.willStop = false;
					this.recordTis = '手指上滑 取消发送'
				}
			},
			// 结束录音
			voiceEnd(e) {
				if (!this.recording) {
					return;
				}
				this.recording = false;
				this.voiceTis = '按住 说话';
				this.recordTis = '手指上滑 取消发送'
				this.RECORDER.stop(); //录音结束
			},
			//录音结束(回调文件)
			recordEnd(e) {
				clearInterval(this.recordTimer);
				if (!this.willStop) {
					this.$queue.showLoading('发送中...')
					// this.voiceEnd()
					// this.voiceCancel()
					// console.log("e: " + JSON.stringify(e));
					let msg = {
						length: 0,
						url: e.tempFilePath
					}
					console.log(e.tempFilePath, "e.tempFilePathtempFilePath")
					let min = parseInt(this.recordLength / 60);
					let sec = this.recordLength % 60;
					min = min < 10 ? '0' + min : min;
					sec = sec < 10 ? '0' + sec : sec;
					msg.length = min + ':' + sec;
					// console.log('msg.length___:' + msg.length)
					uni.uploadFile({ // 上传接口
						url: this.config("APIHOSTA") + '/alioss/uploadMusic', //真实的接口地址
						filePath: e.tempFilePath,
						name: 'file',
						success: (uploadFileRes) => {
							uni.hideLoading();
							this.content = JSON.parse(uploadFileRes.data).data.url;
							let msglength = JSON.parse(uploadFileRes.data).data.sec
							console.log("语音：" + this.content)
							this.setChatSave(3, msglength);
							uni.hideLoading();
						}
					});
				} else {
					console.log('取消发送录音');
					uni.hideLoading();
				}
				this.willStop = false;
			},
			// 切换语音/文字输入
			switchVoice() {
				console.log('111111111')
				let that = this;
				// #ifdef APP
				// 在弹出选择弹框之前先判断一下是否获取了相册权限
				const sysInfo = uni.getSystemInfoSync()
				if (sysInfo.osName == 'ios') { //ios

				} else { //安卓
					console.log('走这里了')
					var Manifest = plus.android.importClass("android.Manifest");
					var MainActivity = plus.android.runtimeMainActivity();
					let flag = MainActivity.checkSelfPermission(Manifest.permission
						.RECORD_AUDIO)
					console.log(flag); // 查看是否拥有相机权限 -1/0
					if (flag == -1) { //没有授权
						that.isShowTips = true
						plus.android.requestPermissions(['android.permission.RECORD_AUDIO'], (
							success) => {
							if (success.deniedAlways && success.deniedAlways.length > 0) {
								that.isShowTips = true
								uni.showModal({
									title: '获取您的麦克风权限',
									content: '您是否允许申请(获取麦克风)权限，便于您使用该功能用于联系用户',
									success: function(res) {
										if (res.confirm) {
											uni.openAppAuthorizeSetting({
												success(res) {
													console.log(res)
												}
											})
										} else if (res.cancel) {
											// console.log('用户点击取消');
										}
										that.isShowTips = false
									}
								});

							} else {
								that.isShowTips = false
							}
						}, (err) => {
							console.log(err)
							that.isShowTips = false
						})
						return
					} else {
						that.isShowTips = false
					}
				}
				that.isShowTips = false
				that.hideDrawer();
				that.isVoice = that.isVoice ? false : true;
				// #endif
				// #ifndef APP
				this.hideDrawer();
				this.isVoice = this.isVoice ? false : true;
				// #endif

			},
			copy(content) {
				uni.showModal({
					title: '温馨提示',
					content: '确认要复制此文字吗？',
					showCancel: true,
					cancelText: '取消',
					confirmText: '确认',
					success: res => {
						if (res.confirm) {
							uni.setClipboardData({
								data: content,
								success: r => {
									this.$queue.showToast('复制成功');
								}
							});
						}
					}
				});
			},
			h5Copy(content) {
				if (!document.queryCommandSupported('copy')) {
					// 不支持
					return false
				}
				let textarea = document.createElement("textarea")
				textarea.value = content
				textarea.readOnly = "readOnly"
				document.body.appendChild(textarea)
				textarea.select() // 选择对象
				textarea.setSelectionRange(0, content.length) //核心
				let result = document.execCommand("copy") // 执行浏览器复制命令
				textarea.remove()
				return result
			},
			getDateDiff(data) {
				// 传进来的data必须是日期格式，不能是时间戳
				//var str = data;
				//将字符串转换成时间格式
				var timePublish = new Date(data);
				var timeNow = new Date();
				var minute = 1000 * 60;
				var hour = minute * 60;
				var day = hour * 24;
				var month = day * 30;
				var result = "2";

				var diffValue = timeNow - timePublish;
				var diffMonth = diffValue / month;
				var diffWeek = diffValue / (7 * day);
				var diffDay = diffValue / day;
				var diffHour = diffValue / hour;
				var diffMinute = diffValue / minute;


				if (diffMonth > 3) {
					result = timePublish.getFullYear() + "-";
					result += timePublish.getMonth() + "-";
					result += timePublish.getDate();
				} else if (diffMonth > 1) { //月
					result = data.substring(0, 10);
				} else if (diffWeek > 1) { //周
					result = data.substring(0, 10);
				} else if (diffDay > 1) { //天
					result = data.substring(0, 10);
				} else if (diffHour > 1) { //小时
					result = parseInt(diffHour) + "小时前";
				} else if (diffMinute > 1) { //分钟
					result = parseInt(diffMinute) + "分钟前";
				} else {
					result = "刚刚";
				}
				return result;
			},
			connect() {
				let that = this;
				let userId = this.$queue.getData('userId');
				let goodId = this.$queue.getData('goodId');
				if (Object.keys(that.goodInfo).length > 0) {
					goodId = that.goodInfo.id
				}
				console.log(goodId, Object.keys(that.goodInfo).length, "goodId")
				console.log(userId)
				if (this.connected || this.connecting) {
					uni.showModal({
						content: '正在连接或者已经连接，请勿重复连接',
						showCancel: false
					})
					return false
				}
				this.connecting = true
				uni.showLoading({
					title: '连接中...'
				})
				let datas = '';
				if (goodId) {
					datas = userId + '?goodId=' + goodId;
				} else {
					datas = userId
				}
				uni.connectSocket({
					// url: 'ws://192.168.0.129:8081/sqx_fast/chatSocket/' + userId,
					url: that.config("WSHOST") + datas,
					data() {
						return {
							msg: 'Hello'
						}
					},
					header: {
						'content-type': 'application/json'
					},
					method: 'GET',
					success(res) {
						// 这里是接口调用成功的回调，不是连接成功的回调，请注意
					},
					fail(err) {
						// 这里是接口调用失败的回调，不是连接失败的回调，请注意
					}
				})
				uni.onSocketOpen((res) => {
					this.connecting = false
					this.connected = true
					uni.hideLoading()
					console.log('onOpen', res);
					this.getTimeOrListItem1();

				})
				uni.onSocketError((err) => {
					this.connecting = false
					this.connected = false
					uni.hideLoading()
					uni.showModal({
						content: '网络较差，请稍后再试',
						showCancel: false
					})
					console.log('onError', err);
				})
				uni.onSocketMessage((res) => {
					setTimeout(() => {
						this.getTimeOrListItem1();
					}, 500)
				})
				uni.onSocketClose((res) => {
					this.connected = false
					this.startRecive = false
					this.msg = false
					console.log('onClose=---', res)
				})

			},
			close() {
				uni.closeSocket()
			},
			getTimeOrListItem1() {
				let userId = this.$queue.getData('userId')
				let data = {
					page: this.pages,
					limit: this.size,
					chatId: this.chatConversationId,
					userId: userId,
				}
				this.$Request.getT('/app/chats/list', data).then(
					res => {
						this.ListItem = [];
						if (res.data) {
							var time = '';
							res.data.forEach(d => {
								d.createTime = this.getDateDiff(d.createTime);
								if (d.createTime === time) {
									d.createTime = '';
								} else {
									time = d.createTime;
								}
								// if (!d.chat.userHead) {
								// 	d.chat.userHead = '/static/logo.png';
								// }
								if (d.type === 4) {
									let data = d.content.split(',');
									d.content = data;
								}
								if (d.type === 3) {

									let data = d.content.split(',');
									d.content = data;
									d.playMsgid = false
								}
								if (d.type === 5) {
									let data = d.content
									var jsonData = JSON.parse(data);
									d.content = jsonData;
								}
								if (d.type === 6) {
									var jsonData = JSON.parse(d.content);
									d.content = jsonData;
								}

								if (d.type === 7) {
									var jsonData = JSON.parse(d.content);
									d.content = jsonData;
								}
								this.ListItem.push(d);

							});
							// this.ListItem.reverse();
							console.log(this.ListItem, "this.ListItem")
							setTimeout(() => {
								uni.pageScrollTo({
									scrollTop: 99999,
									duration: 0
								});
							}, 50);
						}
						uni.hideLoading();
					});
			},
			setChatSave(type, url) {
				if (this.selectBlackStatus == true) {
					this.content = ''
					uni.showToast({
						title: '已拉黑不能发送消息',
						icon: 'none'
					})
					return
				}
				//type:1文字 2图片
				let userId = this.$queue.getData('userId');
				if (type === 1 && this.content == '') {
					this.$queue.showToast('请输入聊天内容');
					return;
				}
				if (type == 1 && this.$queue.getChatSearchKeys(this.content)) {
					uni.showToast({
						title: "输入内容带有非法关键字请重新输入",
						mask: false,
						duration: 1500,
						icon: "none"
					});
					return;
				}

				if (type === 4) { //表情包
					this.content = url;
				}


				let data = {};
				if (type == 3) {
					data = {
						userId: userId,
						content: this.content,
						voiceLength: url,
						type: type,
						chatId: this.chatConversationId,
						sendType: 1
					}
				} else {
					data = {
						userId: userId,
						content: this.content,
						type: type,
						chatId: this.chatConversationId,
						sendType: 1
					}
				}
				data = JSON.stringify(data);
				console.log(data, "发送消息")
				let that = this;
				uni.sendSocketMessage({
					data: data,
					success(res) {
						let image_url = that.$queue.getData('image_url');
						if (!image_url) {
							image_url = '/static/logo.png';
						}
						let data = {
							chat: {
								userHead: image_url
							},
							content: that.content,
							type: type,
							sendType: 1
						}
						that.ListItem.push(data);
						console.log(data, "data")
						console.log(that.ListItem, 'that.ListItem')
						setTimeout(() => {
							that.pages = 1
							that.getTimeOrListItem1();
						}, 500);
					},
					fail(err) {
						that.connected = false
						that.connecting = false
						that.connect()
					}
				})
				this.maps = 1;
				this.mapsValue = ''
				this.content = '';
			},
			//发送图片
			chooseImage(sourceType) {
				let that = this;
				// #ifdef APP
				// 在弹出选择弹框之前先判断一下是否获取了相册权限
				const sysInfo = uni.getSystemInfoSync()
				if (sysInfo.osName == 'ios') { //ios

				} else { //安卓
					console.log('走这里了')
					var Manifest = plus.android.importClass("android.Manifest");
					var MainActivity = plus.android.runtimeMainActivity();
					let flag = MainActivity.checkSelfPermission(Manifest.permission
						.READ_EXTERNAL_STORAGE)
					console.log(flag); // 查看是否拥有相机权限 -1/0
					if (flag == -1) { //没有授权
						that.isShowTip = true
						plus.android.requestPermissions(['android.permission.READ_EXTERNAL_STORAGE'], (
							success) => {
							if (success.deniedAlways && success.deniedAlways.length > 0) {
								that.isShowTip = true
								uni.showModal({
									title: '获取您的相册权限',
									content: '您是否允许申请(获取相册)权限，便于您使用该功能用于联系用户',
									success: function(res) {
										if (res.confirm) {
											uni.openAppAuthorizeSetting({
												success(res) {
													console.log(res)
												}
											})
										} else if (res.cancel) {
											// console.log('用户点击取消');
										}
										that.isShowTip = false
									}
								});

							} else {
								that.isShowTip = false
							}
						}, (err) => {
							console.log(err)
							that.isShowTip = false
						})
						return
					} else {
						that.isShowTip = false
					}
				}
				that.isShowTip = false
				uni.chooseImage({
					count: 1,
					sourceType: ['album'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							that.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								// url: this.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								url: that.config("APIHOSTA") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									that.content = JSON.parse(uploadFileRes.data).data;
									console.log(JSON.parse(uploadFileRes.data).data, "JSON.parse(uploadFileRes.data).data")
									that.setChatSave(2);
									uni.hideLoading();
								}
							});
						}
					}
				})
				// #endif
				// #ifndef APP
				uni.chooseImage({
					count: 1,
					sourceType: ['album'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							that.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								// url: this.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								url: that.config("APIHOSTA") + '/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									that.content = JSON.parse(uploadFileRes.data).data;
									console.log(JSON.parse(uploadFileRes.data).data, "JSON.parse(uploadFileRes.data).data")
									that.setChatSave(2);
									uni.hideLoading();
								}
							});
						}
					}
				})
				// #endif
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
			//查看大图
			viewImg(item) {
				let imgsArray = [];
				imgsArray[0] = item;
				let that = this;
				// #ifdef APP
				// 在弹出选择弹框之前先判断一下是否获取了相册权限
				const sysInfo = uni.getSystemInfoSync()
				if (sysInfo.osName == 'ios') { //ios

				} else { //安卓
					console.log('走这里了')
					var Manifest = plus.android.importClass("android.Manifest");
					var MainActivity = plus.android.runtimeMainActivity();
					let flag = MainActivity.checkSelfPermission(Manifest.permission
						.READ_EXTERNAL_STORAGE)
					console.log(flag); // 查看是否拥有相机权限 -1/0
					if (flag == -1) { //没有授权
						that.isShowTipC = true
						plus.android.requestPermissions(['android.permission.READ_EXTERNAL_STORAGE'], (
							success) => {
							if (success.deniedAlways && success.deniedAlways.length > 0) {
								that.isShowTipC = true
								uni.showModal({
									title: '获取您的储存信息',
									content: '您是否允许码兄申请(获取相册储存)权限，便于您使用该功能用于保存聊天图片',
									success: function(res) {
										if (res.confirm) {
											uni.openAppAuthorizeSetting({
												success(res) {
													console.log(res)
												}
											})
										} else if (res.cancel) {
											// console.log('用户点击取消');
										}
										that.isShowTipC = false
									}
								});

							} else {
								that.isShowTipC = false
							}
						}, (err) => {
							console.log(err)
							that.isShowTipC = false
						})
						return
					} else {
						that.isShowTipC = false
					}
				}
				that.isShowTipC = false
				uni.previewImage({
					current: 0,
					urls: imgsArray
				});
				// #endif
				// #ifndef APP
				uni.previewImage({
					current: 0,
					urls: imgsArray
				});
				// #endif

			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			// this.getorderList()
		},
		onPullDownRefresh: function() {
			this.page = 1;
			// this.getorderList()
		},
		onUnload() {
			uni.removeStorageSync('goodId')
		}
	};
</script>

<style lang="scss">
	@import "./css/style.scss";

	.tips {
		width: 100%;
		padding: 30rpx;
		font-size: 26rpx;
		color: #333333;
		position: fixed;
		top: 0rpx;
		left: 0;
		z-index: 99999 !important;
		background-color: #ffffff;
	}

	page {
		background: #F4F7FB;
		color: #FFFFFF;
	}

	.chat-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #D4F3F0 0%, #E7F4F7 240rpx, #F2F5FA 100%);
		position: relative;
		overflow: hidden;
	}

	.chat-page::before,
	.chat-page::after {
		content: '';
		position: fixed;
		left: 0;
		right: 0;
		pointer-events: none;
		z-index: 0;
	}

	.chat-page::before {
		top: 110rpx;
		left: -24%;
		right: -6%;
		height: 760rpx;
		background: linear-gradient(62deg, rgba(74, 205, 210, 0.34) 0%, rgba(74, 205, 210, 0.22) 22%, rgba(74, 205, 210, 0.08) 48%, rgba(74, 205, 210, 0) 72%);
		transform: rotate(-10deg);
	}

	.chat-page::after {
		top: 220rpx;
		left: -18%;
		right: 12%;
		height: 560rpx;
		background: linear-gradient(58deg, rgba(156, 226, 229, 0.24) 0%, rgba(156, 226, 229, 0.14) 32%, rgba(156, 226, 229, 0.06) 52%, rgba(156, 226, 229, 0.01) 66%, rgba(156, 226, 229, 0) 78%);
		transform: rotate(-14deg);
		opacity: 1;
	}

	.chat-fixed-head {
		box-sizing: border-box;
		position: relative;
		z-index: 2;
	}

	.chat-head-wrap {
		padding: 0 18rpx 4rpx;
	}

	.chat-notice {
		border-radius: 28rpx;
		box-shadow: 0 8rpx 18rpx rgba(21, 54, 93, 0.04);
		border: 1rpx solid rgba(255, 255, 255, 0.58);
		backdrop-filter: blur(10rpx);
		background: rgba(255, 255, 255, 0.52) !important;
		padding-top: 10rpx !important;
		padding-bottom: 10rpx !important;
	}

	.chat-notice__icon {
		flex-shrink: 0;
		transform: scale(0.78);
	}

	.chat-notice__text {
		line-height: 1.6;
		font-size: 22rpx;
		color: #4A586D !important;
	}

	.chat-goods-card {
		border-radius: 24rpx;
		box-shadow: 0 12rpx 36rpx rgba(21, 54, 93, 0.08);
		border: 1rpx solid rgba(255, 255, 255, 0.72);
	}

	.chat-scroll-area {
		box-sizing: border-box;
		padding-left: 20rpx;
		padding-right: 20rpx;
		position: relative;
		z-index: 1;
	}

	.chat-scroll-area::before,
	.chat-scroll-area::after {
		content: '';
		position: absolute;
		pointer-events: none;
		z-index: 0;
		border-radius: 50%;
		filter: blur(2rpx);
	}

	.chat-scroll-area::before {
		width: 420rpx;
		height: 420rpx;
		left: -140rpx;
		top: 220rpx;
		background: radial-gradient(circle, rgba(83, 207, 211, 0.34) 0%, rgba(83, 207, 211, 0.14) 42%, rgba(83, 207, 211, 0) 76%);
	}

	.chat-scroll-area::after {
		width: 460rpx;
		height: 460rpx;
		right: -150rpx;
		top: 500rpx;
		background: radial-gradient(circle, rgba(136, 223, 225, 0.24) 0%, rgba(136, 223, 225, 0.1) 42%, rgba(136, 223, 225, 0) 78%);
	}

	.chat-surface {
		position: absolute;
		left: 0;
		right: 0;
		top: 12rpx;
		bottom: 12rpx;
		border-radius: 28rpx 28rpx 0 0;
		background: linear-gradient(180deg, rgba(255, 255, 255, 0.04) 0%, rgba(240, 249, 251, 0.1) 100%);
		border: none;
		box-shadow: none;
		backdrop-filter: blur(1rpx);
		pointer-events: none;
		overflow: hidden;
	}

	.chat-surface::before,
	.chat-surface::after {
		content: '';
		position: absolute;
		inset: 0;
		pointer-events: none;
	}

	.chat-surface::before {
		left: -18%;
		right: -4%;
		top: 40rpx;
		bottom: auto;
		height: 420rpx;
		border-radius: 48% 52% 44% 56%;
		background: linear-gradient(64deg, rgba(96, 213, 217, 0.18) 0%, rgba(96, 213, 217, 0.11) 34%, rgba(96, 213, 217, 0.04) 54%, rgba(96, 213, 217, 0) 72%);
		transform: rotate(-12deg);
		opacity: 1;
	}

	.chat-surface::after {
		background: radial-gradient(circle at 78% 22%, rgba(255, 255, 255, 0.24) 0, rgba(255, 255, 255, 0.03) 18%, rgba(255, 255, 255, 0) 34%), linear-gradient(60deg, rgba(255, 255, 255, 0.12) 0%, rgba(255, 255, 255, 0.03) 16%, rgba(255, 255, 255, 0) 28%), linear-gradient(180deg, rgba(255, 255, 255, 0.02) 0%, rgba(255, 255, 255, 0) 14%, rgba(92, 189, 193, 0.08) 100%);
	}

	.message-group {
		padding-bottom: 6rpx;
		position: relative;
		z-index: 1;
	}

	.message-time {
		padding: 18rpx 0 10rpx;
		font-size: 22rpx !important;
		color: #99A3B3 !important;
		display: flex;
		justify-content: center;
		align-items: center;
		position: relative;
	}

	.message-time::before {
		content: '';
		position: absolute;
		width: 156rpx;
		height: 44rpx;
		border-radius: 999rpx;
		background: rgba(255, 255, 255, 0.58);
		border: 1rpx solid rgba(255, 255, 255, 0.86);
		box-shadow: 0 6rpx 14rpx rgba(21, 54, 93, 0.04);
		z-index: -1;
	}

	.message-row {
		max-width: 100%;
	}

	.message-item {
		align-items: flex-start;
		gap: 0;
		position: relative;
	}

	.message-item--self {
		align-items: flex-end;
	}

	.chat-listitem {
		display: flex;
		margin-top: 20rpx;
		padding: 0;
	}

	.chat-listitem-text {
		color: #FFFFFF;
		background: #557EFD;
		margin-top: 6rpx;
		width: fit-content;
		max-width: 520rpx;
		padding: 20rpx 24rpx;
		font-size: 28rpx;
		line-height: 1.55;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-radius: 24rpx;
		box-shadow: 0 10rpx 24rpx rgba(37, 78, 154, 0.14);
		position: relative;
	}

	.message-item--other .message-bubble {
		background: #FFFFFF;
		color: #1F2A3D;
		border-top-left-radius: 8rpx;
		box-shadow: 0 10rpx 28rpx rgba(21, 54, 93, 0.08);
	}

	.message-item--other .message-bubble::before {
		content: '';
		position: absolute;
		left: -10rpx;
		top: 22rpx;
		width: 20rpx;
		height: 20rpx;
		background: #FFFFFF;
		clip-path: polygon(0 50%, 100% 0, 100% 100%);
	}

	.message-item--self .message-bubble {
		background: linear-gradient(135deg, #1ACFD6 0%, #0EA7AF 100%);
		color: #FFFFFF;
		border-top-right-radius: 8rpx;
		box-shadow: 0 10rpx 28rpx rgba(14, 167, 175, 0.24);
	}

	.message-item--self .message-bubble::before {
		content: '';
		position: absolute;
		right: -10rpx;
		top: 22rpx;
		width: 20rpx;
		height: 20rpx;
		background: #11b5bb;
		clip-path: polygon(0 0, 100% 50%, 0 100%);
	}

	.message-bubble--voice {
		min-width: 140rpx;
		justify-content: center;
	}

	.message-emoji::before,
	.message-card::before {
		display: none;
	}

	.chat-listitem-text1 {
		color: #333;
		background: #FFFFFF;
		margin-top: 6rpx;
		width: fit-content;
		max-width: 520rpx;
		font-size: 28rpx;
		line-height: 1.5;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-radius: 24rpx;
		overflow: hidden;
		box-shadow: 0 10rpx 28rpx rgba(21, 54, 93, 0.08);
	}

	.chat-listitem-text2 {
		color: #333;
		background: #FFFFFF;
		margin-top: 6rpx;
		width: fit-content;
		max-width: 520rpx;
		padding: 18rpx;
		font-size: 28rpx;
		line-height: 1.5;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-radius: 24rpx;
		box-shadow: 0 10rpx 28rpx rgba(21, 54, 93, 0.08);
	}

	.message-card {
		overflow: hidden;
	}

	.message-card--location {
		padding-bottom: 14rpx;
		border-top-right-radius: 24rpx;
	}

	.chat-listitem-image-type4 {
		color: #000000;
		background: #FFFFFF;
		width: fit-content;
		font-size: 30rpx;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-top-left-radius: 20rpx;
		border-top-right-radius: 20rpx;
		// border-radius: 50rpx;
	}

	.chat-listitem-image {
		margin-top: 5rpx;
		width: 80rpx;
		height: 80rpx;
		border-radius: 50rpx;
		border: 4rpx solid rgba(255, 255, 255, 0.9);
		box-shadow: 0 8rpx 18rpx rgba(21, 54, 93, 0.12);
	}

	.message-self-avatar-wrap {
		width: 80rpx;
		margin-top: 4rpx;
		margin-left: 2rpx;
		flex-shrink: 0;
	}

	.message-self-avatar {
		background: #FFFFFF;
	}

	.message-image {
		border-radius: 22rpx;
		box-shadow: 0 10rpx 26rpx rgba(21, 54, 93, 0.12);
	}

	.message-emoji {
		background: transparent;
		box-shadow: none;
		padding: 0;
		border-radius: 0;
	}

	.save {
		width: 130rpx;
		text-align: center;
		border-radius: 10rpx;
		height: 70rpx;
		background: #FFFFFF;
		margin: 5rpx 10rpx 0;
		line-height: 70rpx;
	}

	.bg {
		background-color: #FFFFFF;
	}

	.chat-popup-layer {
		background: #FFFFFF;
		border-top-left-radius: 28rpx;
		border-top-right-radius: 28rpx;
		box-shadow: 0 -12rpx 30rpx rgba(21, 54, 93, 0.08);
		border-top: 1rpx solid rgba(230, 238, 246, 0.9);
	}

	.chat-input-box {
		left: 14rpx;
		right: 14rpx;
		width: auto;
		bottom: 16rpx !important;
		background: rgba(255, 255, 255, 0.78);
		backdrop-filter: blur(18rpx);
		box-shadow: 0 14rpx 34rpx rgba(21, 54, 93, 0.1);
		padding-left: 16rpx;
		padding-right: 16rpx;
		border: 1rpx solid rgba(255, 255, 255, 0.72);
		border-top: 1rpx solid rgba(255, 255, 255, 0.72);
		border-radius: 999rpx;
	}

	::v-deep .chat-input-box .text-mode {
		background: rgba(244, 247, 251, 0.92);
		border-radius: 999rpx;
	}

	::v-deep .chat-input-box .text-mode .box {
		min-height: 78rpx;
	}

	::v-deep .chat-input-box .textbox {
		margin-top: 11upx;
	}

	::v-deep .chat-input-box .voice,
	::v-deep .chat-input-box .more,
	::v-deep .chat-input-box .em {
		height: 88rpx;
	}

	::v-deep .chat-input-box .send .btn {
		width: 112rpx;
		height: 62rpx;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #1ACFD6 0%, #0EA7AF 100%);
		font-size: 24rpx;
		box-shadow: 0 10rpx 20rpx rgba(14, 167, 175, 0.22);
	}

	::v-deep .chat-input-box .icon {
		color: #0EA7AF;
	}

	::v-deep .chat-input-box input {
		font-size: 28rpx;
	}
</style>
