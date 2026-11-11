<template>
	<view>
		<!-- #ifdef H5 -->
		<view class="margin-sm radius padding"
			style="background: #fff;position: fixed;top: 90rpx;left: 0; right: 0;z-index: 9;">
		<!-- #endif -->
			<!-- #ifndef H5 -->
			<view class="margin-sm radius padding"
				style="background: #fff;position: fixed;top: 0rpx;left: 0; right: 0;z-index: 9;">
			<!-- #endif -->
				<view class="flex align-center">
					<image src="../../static/images/msg/alert.png" style="width: 88upx;height: 88upx;"></image>
					<view class="text-sm margin-left-sm" style="color: #333;width: 84%;">
						{{liaotian}}<text style="color: #557EFD;" @click="goTousu()">{{tousu}}</text>
					</view>
				</view>
			</view>

			<view style="width: 100%;padding-bottom: 140rpx;padding-top: 295rpx;" @touchstart="hideDrawer">
				<view style="display: flex;flex-direction: column;" v-for="(item,index) in ListItem">
					<view style="margin-top: 15rpx;width: 100%;text-align: center;font-size: 26rpx;color: #999999;"
						v-if="item.createTime">
						{{item.createTime}}
					</view>
					<view v-if="item.userId && userId != item.userId" style="width: 83%;margin-right: 15%;">
						<view class="chat-listitem" style="float: left;margin-left: 10rpx;">
							<view>
								<image @tap="goGuanZhu(item)" :src="item.avatar ? item.avatar : '/static/logo.png'"
									class="chat-listitem-image"></image>
							</view>
							<view v-if="item.content && item.messageType === 1" @longpress="copy(item.content)"
								class="chat-listitem-text" style="margin-left: 20rpx;">{{item.content}}</view>
							<image @tap="viewImg(item.content)" v-if="item.content && item.messageType === 2"
								:src="item.content" style="height: 200rpx;width: 200rpx;margin-left: 20rpx;"></image>
							<image class="chat-listitem-text" @tap="viewImg(item.content)"
								v-if="item.content && item.messageType === 4"
								:src="'https://h5.youxb.icu/emoji/' +item.content"
								style="height: 80rpx;width: 80rpx;margin-left: 20rpx;"></image>


							<view v-if="item.content && item.messageType === 3" class="chat-listitem-text bubble voice"
								:class="item.playMsgid?'play':''"
								style="margin-left: 20rpx;display: flex;align-items: center;"
								@tap="playVoice(item.content,index)">
								<image v-if="!item.playMsgid" src="../../static/pw/laba.png"
									style="width: 35upx;height: 40upx;margin-right: 10rpx;"></image>
								<image v-if="item.playMsgid" src="../../static/pw/labagif.gif"
									style="width: 35upx;height: 40upx;margin-right: 10rpx;"></image>

								<view class="length">{{item.voiceLength ? item.voiceLength : 0}}</view>
							</view>
							<view class="chat-listitem-text1 " style="margin-right: 20rpx;"
								v-if="item.content && item.messageType === 5"
								@click="openMap(item.content.latitude,item.content.longitude,item.content.address)">
								<view class="padding-sm">{{item.content.address}}</view>
								<map style="width: 100%; height: 120px;" :latitude="item.content.latitude"
									:longitude="item.content.longitude">
								</map>
							</view>

							<view class="chat-listitem-text2 " style="margin-right: 20rpx;"
								v-if="item.content && item.messageType === 6" @click="goOrder(item)">
								<view>我要咨询这个订单</view>
								<view class="flex margin-top-sm">
									<image
										:src="item.content.homepageImg?item.content.homepageImg: '../../static/logo.png'"
										style="width: 150rpx;height: 150rpx;border-radius: 10rpx;" mode="aspectFill">
									</image>
									<view class="flex-sub margin-left-sm text-white  ">
										<view class=" text-30"
											style="display: inline-block;width: 260rpx;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
											{{item.content.myLevel}}
										</view>

										<view class="flex radius" style="line-height: 34upx;">
											<view
												style="width: 100%;position: relative;line-height: 40rpx;color: #999999;">
												<text class="margin-right-xs"
													v-for="(item,index) in item.content.gameName"
													:key="index">{{item}}</text>
											</view>
										</view>
										<view style="color:#FF1200;font-size: 31rpx;">
											￥{{item.content.payMoney}}</text>
										</view>
									</view>
								</view>
							</view>

							<view class="chat-listitem-text2 " style="margin-right: 20rpx;"
								v-if="item.content && item.messageType === 7" @click="goOrderDet(item)">
								<view class="flex ">
									<image
										:src="item.content.homepageImg?item.content.homepageImg: '../../static/logo.png'"
										style="width: 150rpx;height: 150rpx;border-radius: 10rpx;" mode="aspectFill">
									</image>
									<view class="flex-sub margin-left-sm text-white  ">
										<view class=" text-30"
											style="display: inline-block;width: 260rpx;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
											{{item.content.myLevel}}
										</view>
										<view class="flex radius" style="line-height: 34upx;">
											<view
												style="width: 100%;position: relative;line-height: 40rpx;color: #999999;">
												<text class="margin-right-xs"
													v-for="(item,index) in item.content.gameName"
													:key="index">{{item}}</text>
											</view>
										</view>
										<view style="color:#FF1200;font-size: 31rpx;">
											￥{{item.content.money}}</text>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>

					<view v-if="item.userId && userId == item.userId" style="width: 83%;margin-left: 15%;">
						<view class="chat-listitem" style="float: right;">
							<view v-if="item.content && item.messageType === 1" @longpress="copy(item.content)"
								class="chat-listitem-text" style="margin-right: 20rpx;">{{item.content}}</view>
							<image @tap="viewImg(item.content)" v-if="item.content && item.messageType === 2"
								:src="item.content" style="height: 200rpx;width: 200rpx;margin-right: 20rpx;"></image>
							<image class="chat-listitem-text" @tap="viewImg(item.content)"
								v-if="item.content && item.messageType === 4"
								:src="'https://xxpw.xianmxkj.com/emoji/' +item.content"
								style="height: 80rpx;width: 80rpx;margin-right: 20rpx;">
							</image>

							<view v-if="item.content && item.messageType === 3" class="chat-listitem-text bubble voice"
								:class="item.playMsgid ?'play':''"
								style="margin-right: 20rpx;display: flex;align-items: center;"
								@tap="playVoice(item.content,index)">
								<view class="length">{{item.voiceLength ? item.voiceLength : 0}}
								</view>
								<image v-if="!item.playMsgid" src="../../static/pw/labaleft.png"
									style="width: 35upx;height: 40upx;margin-left: 10rpx;"></image>
								<image v-if="item.playMsgid " src="../../static/pw/labagif.gif"
									style="width: 35upx;height: 40upx;margin-left: 10rpx;"></image>

							</view>
							<view class="chat-listitem-text1 " style="margin-right: 20rpx;"
								v-if="item.content && item.messageType === 5"
								@click="openMap(item.content.latitude,item.content.longitude,item.content.address)">
								<view class="padding-sm">{{item.content.address}}</view>
								<map style="width:520upx; height: 120px;" :latitude="item.content.latitude"
									:longitude="item.content.longitude">
								</map>
							</view>

							<view class="chat-listitem-text2 " style="margin-right: 20rpx;"
								v-if="item.content && item.messageType === 6" @click="goOrder(item)">
								<view>我要咨询这个订单</view>
								<view class="flex margin-top-sm">
									<image
										:src="item.content.homepageImg?item.content.homepageImg: '../../static/logo.png'"
										style="width: 150rpx;height: 150rpx;border-radius: 10rpx;" mode="aspectFill">
									</image>
									<view class="flex-sub margin-left-sm text-white  ">
										<view class=" text-30"
											style="display: inline-block;width: 260rpx;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
											{{item.content.myLevel}}
										</view>

										<view class="flex radius" style="line-height: 34upx;">
											<view
												style="width: 100%;position: relative;line-height: 40rpx;color: #999999;">
												<text class="margin-right-xs"
													v-for="(item,index) in item.content.gameName"
													:key="index">{{item}}</text>
											</view>
										</view>
										<view style="color:#FF1200;font-size: 31rpx;">
											￥{{item.content.payMoney}}</text>
										</view>
									</view>
								</view>
							</view>
							<view style="position: relative;">
								<image :src="imageUrl" class="chat-listitem-image"></image>
								<view style="position: absolute;bottom: 0;left: 0;right: 0;z-index:9;" v-if="isVip">
									<!-- <image src="../../static/images/vip.png" style="width:90rpx;height:30rpx;"></image> -->
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 底部聊天输入框 -->
			<!-- <view class="input-box">
			<view style="display: flex;margin-top: 15rpx;width: 100%;">
				<image src="https://renwu.xiansqx.com/img/20210817/024ca47bc591460daf76762a8e98283f.png" @click="chooseImage(['album'])" style="width: 60rpx;height: 60rpx;margin-top: 8rpx;"></image>
				<input confirm-type="send" @confirm='setChatSave(1)' type="text" v-model="content"
					style="width: 68%;height: 70rpx;background: #FFFFFF;margin: 4rpx 10rpx 0;border-radius: 10rpx;padding-left: 10rpx;" />
				<view class="save" @tap='setChatSave(1)'>发送</view>
			</view>
		</view> -->


			<!-- 抽屉栏 -->
			<view class="popup-layer" :class="popupLayerClass" @touchmove.stop.prevent="discard">
				<!-- 表情 -->
				<swiper class="emoji-swiper" indicator-active-color="#cccccc" :class="{hidden:hideEmoji}"
					indicator-dots="true" duration="150">
					<swiper-item v-for="(page1,pid) in emojiList" :key="pid">
						<view v-for="(em,eid) in page1" :key="eid" @tap="setChatSave(4,em.url)">
							<!-- <image src="../../static/emoji/100.gif" mode="widthFix"></image> -->
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
						<!-- 常用语 -->
						<view>
							<view class="box" @tap="showChangYongSelect">
								<view class="icon jianpan" style="color: #000000;"></view>
							</view>
							<view style="width: 100%;text-align: center;color: #000000;">常用语句</view>
						</view>
						<!-- 添加常用语或自动回复内容 -->
						<view>
							<view class="box" @tap="goAdd">
								<view class="icon add" style="color: #000000;"></view>
							</view>
							<view style="width: 100%;text-align: center;color: #000000;">添加语句</view>
						</view>
						<view>
							<view class="box" @tap="goHistory">
								<view class="icon chehui" style="color: #000000;"></view>
							</view>
							<view style="width: 100%;text-align: center;color: #000000;">聊天记录</view>
						</view>
						<!-- #ifndef APP -->
						<view>
							<view class="box" @tap="goMap">
								<u-icon name="map-fill" color="#000000" size="72"></u-icon>
							</view>
							<view style="width: 100%;text-align: center;color: #000000;">发送位置</view>
						</view>
						<!-- #endif -->
						<view>
							<view class="box" @tap="openOrder">
								<u-icon name="bag-fill" color="#000000" size="72"></u-icon>
							</view>
							<view style="width: 100%;text-align: center;color: #000000;">发送订单</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 底部输入栏 -->
			<view class="input-box" :class="popupLayerClass" @touchmove.stop.prevent="discard"
				:style="'bottom: {bottom}px'">
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
					<view class="voice-mode" :class="[isVoice?'':'hidden',recording?'recording':'']"
						@touchstart="voiceBegin" @touchmove.stop.prevent="voiceIng" @touchend="voiceEnd"
						@touchcancel="voiceCancel">{{voiceTis}}</view>
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

			<u-select v-model="showChangYong" title="常用词语" mode="single-column" :list="changYongList"
				@confirm="selConfirm">
			</u-select>

			<u-popup v-model="show" mode="bottom" border-radius="14" height="400px">
				<view style="background: #f5f5f5;">
					<view class="flex padding-tb justify-center" style="position: relative;">
						<view class="text-black text-center text-lg text-bold ">发送服务</view>
						<view style="position: absolute;right: 30upx;top: 20upx;" @click="closeOrder">
							<u-icon name="close-circle" color="#333333" size="58"></u-icon>
						</view>
					</view>
					<view class="flex justify-between  bg padding-sm radius margin" v-for="(item,index) in list"
						:key='index'>
						<image :src="item.homepageImg?item.homepageImg: '../../static/logo.png'"
							style="width: 150rpx;height: 150rpx;border-radius: 10rpx;" mode="aspectFill"></image>
						<view class="flex-sub margin-left-sm text-white  ">
							<view class="flex align-center justify-between">
								<view class=" text-30"
									style="display: inline-block;width: 260rpx;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
									{{item.myLevel}}
								</view>
								<view style="color:#FF1200;font-size: 31rpx;">
									￥{{item.payMoney}}</text>
								</view>
							</view>
							<view class="flex radius" style="line-height: 34upx;">
								<view style="width: 100%;position: relative;line-height: 40rpx;color: #999999;">
									<text class="margin-right-xs" v-for="(item,index) in item.gameName"
										:key="index">{{item}}</text>
								</view>
							</view>
							<view class="flex justify-end" style="width: 100%;" @click="saveOrder(item)">
								<view
									style="background: #557EFD;color: #ffffff;padding: 12rpx 35rpx;border-radius: 45rpx;">
									发送
								</view>
							</view>
						</view>
					</view>
				</view>
			</u-popup>


		</view>
</template>

<script>
	import configdata from '../../common/config.js';
	export default {
		data() {
			return {
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
					messageType: 1,
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
		onUnload() {
			uni.closeSocket()
			uni.hideLoading()
			uni.offKeyboardHeightChange(); //取消监听键盘高度变化事件，避免内存消耗
		},
		onLoad(d) {

			// 聊天配置 300
			this.$Request.getT('/app/common/type/300').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.liaotian = res.data.value
					}
				}
			});
			this.$Request.getT('/app/common/type/603').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.tousu = res.data.value
					}
				}
			});
			this.userId = this.$queue.getData('userId');
			this.connect();
			this.byNickName = d.byNickName;
			this.byUserId = d.byUserId;
			if (d.chatConversationId) {
				this.chatConversationId = d.chatConversationId;
				this.getTimeOrListItem1();
			} else {
				this.insertChat();
			}
			if (this.byNickName) {
				uni.setNavigationBarTitle({
					title: this.byNickName
				})
			}
			let image_url = this.$queue.getData('avatar');
			if (image_url && image_url !== 'undefined') {
				this.imageUrl = image_url;
			} else {
				this.imageUrl = '/static/logo.png';
			}
			// this.emojiList.forEach(d =>{
			// 	d.forEach(e =>{
			// 		console.log('/static/emoji/' + e.url)
			// 	});
			// });
			//语音自然播放结束
			// this.AUDIO.onEnded((res) => {
			// 	this.playMsgid = null;
			// });
			//语音自然播放结束
			this.AUDIO.onEnded((res) => {
				// console.log("this.musicIndex___:" + that.musicIndex)
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
			if (this.userId) {
				this.getChangYong();
				this.getorderList()
				this.getIsVip()
			}
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
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						this.isVip = res.data
						uni.setStorageSync('isVIP', res.data)
					}
				});
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
			getChangYong() {
				this.$Request.getT('/app/message/selectMessageByUserId?page=1&limit=100&state=20')
					.then(res => {
						if (res.code == 0) {
							this.changYongList = [];
							res.data.list.forEach(d => {
								let data = {
									label: d.content,
									value: d.id
								}
								this.changYongList.push(data);
							});
						}
					});
			},
			goGuanZhu(item) {
				this.hideDrawer();
				//去关注页面
				uni.navigateTo({
					url: '/my/gird/guanzhuDetail?userId=' + item.userId
				});
				// uni.navigateTo({
				// 	url: '/package/pages/task/followUser?userId=' + item.userId
				// });
			},
			//添加常用语/自动回复
			goAdd() {
				this.showMore();
				uni.navigateTo({
					url: '/pages/msg/addmsg'
				});
			},
			//聊天记录
			goHistory() {
				this.showMore();
				uni.navigateTo({
					url: '/pages/msg/chathistory?chatConversationId=' + this.chatConversationId
				});
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
			//打开位置
			openMap(latitude, longitude, name) {

				uni.openLocation({
					latitude: latitude - 0, //要去的纬度-地址       
					longitude: longitude - 0, //要去的经度-地址
					name: name, //地址名称
					address: name, //详细地址名称
					success: function() {
						console.log('导航成功');
					},
					fail: function(error) {
						console.log(error)
					}
				});
			},
			//打开服务弹框
			openOrder() {
				if (this.list.length == 0) {
					uni.showToast({
						title: '暂无订单',
						icon: 'none'
					})
					this.showMore();
				} else {
					this.showMore();
					this.show = true
				}

			},
			closeOrder() {
				this.show = false
			},
			saveOrder(item) {
				this.setChatSave(6, JSON.stringify(item)) //发送订单
				this.show = false
			},
			goOrder(e) {
				uni.navigateTo({
					url: '/my/order/pay?id=' + e.content.ordersId + '&isTrue=1'
				})
			},
			goOrderDet(e) {
				uni.navigateTo({
					url: '/pages/index/game/order?id=' + e.content.id
				})
			},
			getorderList() {
				let userId = this.$queue.getData('userId');
				let data = {
					orderTakingUserId: this.byUserId,
					page: this.page,
					limit: this.limit,
					status:0
				}
				this.$Request.get('/app/orders/selectMyOrder', data).then(res => {
					if (res.code == 0 && res.data) {
						for (var i = 0; i < res.data.list.length; i++) {
							if (res.data.list[i].gameName) {
								res.data.list[i].gameName = res.data.list[i].gameName.split(",");
							}
							if (res.data.list[i].distance) {
								let distance = parseFloat(res.data.list[i].distance)
								if (distance >= 1000) {
									res.data.list[i].distance = (distance / 1000).toFixed(2) + 'km'
								} else if (distance < 1000) {
									res.data.list[i].distance = distance.toFixed(2) + 'm'
								}

							}
						}
						if (this.page == 1) {
							this.list = res.data.list
						} else {
							this.list = [...this.list, ...res.data.list]
						}
					}
				});
			},
			insertChat() {
				let userId = this.$queue.getData('userId');
				let data = {
					focusedUserId: this.byUserId,
					userId: userId
				}
				this.$Request.postJson('/appChat/insertChatConversation', data).then(res => {
					if (res.code == 0 && res.data) {
						this.chatConversationId = res.data.chatConversationId;
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
								let onlinePath = 'https://s2.ax1x.com/2019/04/12/'
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
					// console.log("e: " + JSON.stringify(e));
					let msg = {
						length: 0,
						url: e.tempFilePath
					}
					let min = parseInt(this.recordLength / 60);
					let sec = this.recordLength % 60;
					min = min < 10 ? '0' + min : min;
					sec = sec < 10 ? '0' + sec : sec;
					msg.length = min + ':' + sec;
					// console.log('msg.length___:' + msg.length)
					uni.uploadFile({ // 上传接口
						url: this.config("APIHOST1") + '/alioss/uploadMusic', //真实的接口地址
						filePath: e.tempFilePath,
						name: 'file',
						success: (uploadFileRes) => {
							uni.hideLoading();
							// console.log(JSON.parse(uploadFileRes.data).data.url,'--------------',JSON.parse(uploadFileRes.data).data.sec)
							// return
							this.content = JSON.parse(uploadFileRes.data).data.url;
							let msglength = JSON.parse(uploadFileRes.data).data.sec
							// console.log("语音：" + this.content)
							this.setChatSave(3, msglength);
							uni.hideLoading();
						}
					});
				} else {
					console.log('取消发送录音');
				}
				this.willStop = false;
			},
			// 切换语音/文字输入
			switchVoice() {
				this.hideDrawer();
				this.isVoice = this.isVoice ? false : true;
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
				let userId = this.$queue.getData('userId');
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
				uni.connectSocket({
					// url: 'wss://h5.youxb.icu/wss/sqx_fast/chatSocket/' + userId,
					url: this.config("WSHOST1") + userId,
					// url: 'ws://192.168.1.17:8893/sqx_fast/chatSocket/' + userId,
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
					if (this.maps == 2) {
						this.setChatSave(5, this.mapsValue)
					}

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
				this.$Request.getT('/app/chat/selectChatContent?page=' + this.pages + '&limit=' + this.size +
					'&chatConversationId=' + this.chatConversationId + '&userId=' + userId).then(
					res => {
						this.ListItem = [];
						if (res.data) {
							var time = '';
							res.data.list.forEach(d => {
								d.createTime = this.getDateDiff(d.createTime);
								if (d.createTime === time) {
									d.createTime = '';
								} else {
									time = d.createTime;
								}
								// if (!d.chat.userHead) {
								// 	d.chat.userHead = '/static/logo.png';
								// }
								if (d.messageType === 4) {
									let data = d.content.split(',');
									d.content = data;
								}
								if (d.messageType === 3) {

									let data = d.content.split(',');
									d.content = data;
									d.playMsgid = false
								}
								if (d.messageType === 5) {
									let data = d.content
									var jsonData = JSON.parse(data);
									d.content = jsonData;
								}
								if (d.messageType === 6) {
									var jsonData = JSON.parse(d.content);
									d.content = jsonData;
								}

								if (d.messageType === 7) {
									var jsonData = JSON.parse(d.content);
									d.content = jsonData;
								}
								this.ListItem.push(d);

							});
							this.ListItem.reverse();
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
				if (type == 5) { //定位
					this.content = url;
				}
				if (type == 6) { //订单
					this.content = url;
				}
				let data = {};
				if (type == 3) {
					data = {
						userId: this.byUserId,
						content: this.content,
						voiceLength: url,
						messageType: type,
						chatConversationId: this.chatConversationId
					}
				} else {
					data = {
						userId: this.byUserId,
						content: this.content,
						messageType: type,
						chatConversationId: this.chatConversationId
					}
				}
				data = JSON.stringify(data);
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
						setTimeout(() => {
							that.pages = 1
							that.getTimeOrListItem1();
						}, 50);

					},
					fail(err) {
						console.log(err, '----------');
					}
				})
				this.maps = 1;
				this.mapsValue = ''
				this.content = '';
			},
			//发送图片
			chooseImage(sourceType) {

				uni.chooseImage({
					count: 1,
					sourceType: ['album'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							this.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								// url: this.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								url: 'https://jiazhengv5.xianmaxiong.com/sqx_fast/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									this.content = JSON.parse(uploadFileRes.data).data;
									this.setChatSave(2);
									uni.hideLoading();
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
			//查看大图
			viewImg(item) {
				let imgsArray = [];
				imgsArray[0] = item;
				uni.previewImage({
					current: 0,
					urls: imgsArray
				});
			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getorderList()
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getorderList()
		},
	};
</script>

<style lang="scss">
	@import "../msg/css/style.scss";
	// @import "../../static/css/icon.css";

	page {
		background: #F5F5F5;
		color: #FFFFFF;
	}

	.chat-listitem {
		display: flex;
		margin-top: 20rpx;
		padding: 10rpx;
	}

	.chat-listitem-text {
		color: #FFFFFF;
		background: #557EFD;
		margin-top: 6rpx;
		width: fit-content;
		padding: 15rpx;
		font-size: 30rpx;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-radius: 10rpx;
	}

	.chat-listitem-text1 {
		color: #333;
		background: #FFFFFF;
		margin-top: 6rpx;
		width: fit-content;

		font-size: 30rpx;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-radius: 10rpx;
	}

	.chat-listitem-text2 {
		color: #333;
		background: #FFFFFF;
		margin-top: 6rpx;
		width: fit-content;
		padding: 15rpx;
		font-size: 30rpx;
		height: max-content;
		word-wrap: break-word;
		word-break: break-all;
		border-radius: 10rpx;
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
</style>