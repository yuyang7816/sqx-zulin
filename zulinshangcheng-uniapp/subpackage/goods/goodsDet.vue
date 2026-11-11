<template>
	<view class="container_box">
		<u-navbar :back-text="backText" back-icon-color="#333333" :back-text-style="backTextStyle"
			back-icon-size="29"></u-navbar>
		<!-- 顶部品牌渐变横幅 -->
		<view class="page-hero">
			<view class="hero-glow hero-glow--a"></view>
			<view class="hero-glow hero-glow--b"></view>
			<view class="hero-ring"></view>
		</view>
		<view class="ambient ambient--a"></view>
		<view class="ambient ambient--b"></view>
		<view class="page-body">
			<!-- 轮播图 -->
			<view class="wrap" v-if="goodInfo.img">
				<u-swiper :list="goodInfo.img.split(',')" height="400" border-radius="0" img-mode="aspectFit" bg-color="#ffffff"
					@click="previewImage"></u-swiper>
			</view>
			<!-- 商品信息主卡片 -->
			<view class="goodsTitle">
				<view class="goodsTitle_msg">
					<view class="price-row">
						<view class="price" v-if="goodInfo.sku">
							<text class="price-symbol">￥</text><text class="price-num">{{goodPrice}}</text><text class="price-unit" v-if="zqIndex != goodDays.length + 1">/天</text>
						</view>
						<view class="tags">
							<view class="tag-primary" v-if="goodInfo.leaseStart">{{goodInfo.leaseStart}}</view>
							<view class="tag-danger" v-if="goodInfo.supportBuyout == 1">支持买断</view>
						</view>
					</view>
					<view class="title-section">
						<text class="text_hid">
							{{goodInfo.title || ''}}
						</text>
					</view>
					<view class="service-row">
						<view class="service-badge">
							<image src="../../static/zlImage/fxg.png" mode=""></image>
						</view>
						<view class="service-item">
							<image src="../../static/zlImage/xin.png" mode="">
							</image>
							<view>平台可信赖</view>
						</view>
						<view class="service-item">
							<image src="/static/zlImage/zhi.png" mode=""></image>
							<view>产品有质保</view>
						</view>
						<view class="service-item">
							<image src="/static/zlImage/sh.png" mode=""></image>
							<view>售后保服务</view>
						</view>
					</view>
				</view>
			</view>
			<!-- 租赁流程 -->
			<view class="flowCard">
				<view class="card-head">
					<view class="card-head-title">租赁流程</view>
					<view class="card-head-sub">四步轻松租好物</view>
				</view>
				<view class="goodsLc">
					<view class="lc-track"></view>
					<view class="lc-step">
						<view class="lc-dot">1</view>
						<text>身份认证</text>
					</view>
					<view class="lc-step">
						<view class="lc-dot">2</view>
						<text>缴纳押金</text>
					</view>
					<view class="lc-step">
						<view class="lc-dot">3</view>
						<text>开始租机</text>
					</view>
					<view class="lc-step">
						<view class="lc-dot">4</view>
						<text>快递/自提</text>
					</view>
				</view>
			</view>
			<!-- 产品参数等信息 -->
			<view class="goodsCs">
				<view class="goodsCs_" @click="showgg = true">
					<view class="cs-label">选择</view>
					<view class="cs-content">
						请选择：租赁方案/内存/颜色/租期
					</view>
					<view class="cs-arrow-wrap">
						<image class="cs-arrow" src="/static/images/my/right.png" mode="">
						</image>
					</view>
				</view>
				<view class="goodsCs_" v-if="goodInfo.isExpressList">
					<view class="cs-label">配送</view>
					<view class="cs-content express-list">
						<view class="express-item" v-for="(item,index) in goodInfo.isExpressList" :key="item">
							<view v-if="item == 1">商家包邮</view>
							<view v-if="item == 7" class="express-sep">/</view>
							<view v-if="item == 2">不包邮<text>(邮费{{goodInfo.postagePrice}}元)</text></view>
							<view v-if="item == 3">到店自提</view>
						</view>
					</view>
					<view class="cs-arrow-wrap">
						<image class="cs-arrow" src="/static/images/my/right.png" mode="">
						</image>
					</view>
				</view>
				<view class="goodsCs_" @click="showPj = true"
					v-if="(goodInfo.accessoryList&&goodInfo.accessoryList.length > 0) || (goodInfo.giftList&&goodInfo.giftList.length > 0)">
					<view class="cs-label">配件</view>
					<view class="cs-content accessory-box">
						<view>
							<view class="accessory-row"
								v-if="goodInfo.accessoryList&&goodInfo.accessoryList.length > 0">
								<view class="tag tag-return">需归还</view>
								<view class="accessory-name" v-for="(item,index) in goodInfo.accessoryList.slice(0,1)" :key="index">
									{{item.accessoryName}}<text v-if="item.accessoryNum">*{{item.accessoryNum}}</text>
								</view>
							</view>
							<view class="accessory-row" v-if="goodInfo.giftList&&goodInfo.giftList.length > 0">
								<view class="tag tag-gift">赠品</view>
								<view class="accessory-name" v-for="(item,index) in goodInfo.giftList.slice(0,1)" :key="index">
									{{item.giftName}}<text v-if="item.giftNum">*{{item.giftNum}}</text>
								</view>
							</view>
						</view>
						<view class="cs-arrow-wrap">
							<image class="cs-arrow" src="/static/images/my/right.png" mode="">
							</image>
						</view>
					</view>
				</view>
				<view class="goodsCs_" v-if="goodInfo.guarantee">
					<view class="cs-label">保障</view>
					<view class="cs-content">{{goodInfo.guarantee }}</view>
				</view>
			</view>
			<!-- 详情选项卡 -->
			<view class="goodsDet">
				<view class="slot-wrap">
					<view :class="tabListIndex == 0?'tabAic':'tabItem'" @click="changeTabIndex(0)">商品详情</view>
					<view :class="tabListIndex == 1?'tabAic':'tabItem'" @click="changeTabIndex(1)">租赁须知
					</view>
					<view :class="tabListIndex == 2?'tabAic':'tabItem'" @click="changeTabIndex(2)">常见问题
					</view>
					<view :class="tabListIndex == 3?'tabAic':'tabItem'" @click="changeTabIndex(3)">评价
					</view>
				</view>
			</view>
			<!-- 商品详情 -->
			<view class="container" v-if="tabListIndex == 0">
				<u-parse :html="goodInfo.descrition"></u-parse>
			</view>
			<!-- 租赁须知 -->
			<view v-if="tabListIndex == 1">
				<view class="container">
					<view class="section-title">租期计算</view>
					<view class="timeline">
						<view class="grayLeft">运输中</view>
						<view class="grayCent">租赁中</view>
						<view class="grayRight">到期</view>
					</view>
					<view class="zlwz">
						<view>快递签收次日开始计费</view>
						<view>到期后可选买断/续租/归还</view>
					</view>
					<view class="timeline">
						<view class="grayLeft">到店提货</view>
						<view class="grayCent">租赁中</view>
						<view class="grayRight">到期</view>
					</view>
					<view class="zlwz zlwz--last">
						<view>提货次日开始计费</view>
						<view>到期后可选买断/续租/归还</view>
					</view>
					<view class="notice-text">{{ZqjsText}}</view>
				</view>
				<view class="container">
					<view class="section-title">租后方案</view>
					<view class="plan-block">
						<view class="plan-title">
							<image src="../../static/zlImage/quan.png" mode=""></image>1.续租
						</view>
						<view class="zhfa">
							<view>查看订单</view>
							<image src="/static/images/my/right.png" mode=""></image>
							<view>一键续租（按订单日租金续租）</view>
							<image src="/static/images/my/right.png" mode=""></image>
							<view>支付租金</view>
							<image src="/static/images/my/right.png" mode=""></image>
							<view>继续使用</view>
						</view>
					</view>
					<view class="plan-block">
						<view class="plan-title">
							<image src="../../static/zlImage/quan.png" mode=""></image>2.买断
						</view>
						<view class="zhfa">
							<view>查看订单</view>
							<image src="/static/images/my/right.png" mode=""></image>
							<view>一键买断（部分设备买断需申请）</view>
							<image src="/static/images/my/right.png" mode=""></image>
							<view>支付尾款</view>
							<image src="/static/images/my/right.png" mode=""></image>
							<view>终生拥有</view>
						</view>
					</view>
					<view class="plan-block">
						<view class="plan-title">
							<image src="../../static/zlImage/quan.png" mode=""></image>3.归还
						</view>
						<view class="zhfa">
							<view>查看订单</view>
							<image src="/static/images/my/right.png" mode=""></image>
							<view>寄回设备（商家签收并质检设备）</view>
							<image src="/static/images/my/right.png" mode=""></image>
							<view>完结订单</view>
						</view>
					</view>
				</view>
				<view class="container">
					<view class="section-title">货物邮费须知</view>
					<view class="freight-table">
						<view class="freight-header">
							<view class="yfxz">场景</view>
							<view class="yfxz">条件</view>
							<view class="yfxz">运费</view>
						</view>
						<view class="yfxzBox">
							<view class="yfxzText">下单发货</view>
							<view class="yfxzText">商户发货</view>
							<view class="yfxzText">商家承担</view>
						</view>
						<view class="yfxzBox">
							<view class="yfxzText">取消或拒收</view>
							<view class="yfxzText">发货后取消订单或拒收</view>
							<view class="yfxzText">客户承担</view>
						</view>
						<view class="yfxzBox">
							<view class="yfxzText">客户换货</view>
							<view class="yfxzText">7天内</view>
							<view class="yfxzText">商家承担</view>
						</view>
						<view class="yfxzBox">
							<view class="yfxzText">客户归还</view>
							<view class="yfxzText">商品到期归还</view>
							<view class="yfxzText">客户承担</view>
						</view>
					</view>
				</view>
				<view class="container">
					<view class="section-title">折旧/赔付规则须知</view>
					<view class="notice-text">{{pfText}}</view>
				</view>
			</view>
			<!-- 常见问题 -->
			<view class="container" v-if="tabListIndex == 2">
				<view class="slot-wrap collapse-tabs">
					<view :class="cjwttabIndex == item.helpClassifyId?'tabcjwtAic':'tabcjwtItem'"
						v-for="(item,index) in questionList" :key="index" @click="changecjwtTabIndex(item.helpClassifyId)">
						{{item.helpClassifyName }}
					</view>
				</view>
				<view class="collapse-content">
					<u-collapse>
						<u-collapse-item :title="item.helpWordTitle" v-for="(item, index) in questionItem" :key="index">
							<u-parse :html="item.helpWordContent"></u-parse>
						</u-collapse-item>
					</u-collapse>
				</view>
			</view>
			<!-- 商品评价 -->
			<view class="container" v-if="tabListIndex == 3&&evaluateList.length>0">
				<view class="more">
					<view class="pingj">评价({{evaluateList.length}})</view>
					<view class="more-link"
						@click="navigate('/subpackage/goods/evaluateList?goodId='+goodId)">查看更多<u-icon name="arrow-right"
							size="24" color="#0A9A9A"></u-icon></view>
				</view>
				<view class="" v-for="(item,index) in evaluateList" :key="index">
					<view class="container_">
						<image :src="item.userHeader" mode="" class="imagepl">
						</image>
						<view class="eval-info">
							<view class="eval-name">{{item.userName}}</view>
							<view class="label">{{item.content}}</view>
						</view>
						<view class="eval-time">{{item.createTime.substring(0, 16)}}
						</view>
					</view>
					<view class="container_ reply-row" v-if="item.reply">
						<image src="/static/logo.png" mode="" class="imagepl">
						</image>
						<view class="eval-info">
							<view class="eval-name">商家回复</view>
							<view class="label">{{item.reply}}</view>
						</view>
					</view>
				</view>
			</view>
			<empty v-if="tabListIndex == 3&&evaluateList.length==0" :height="eheight"></empty>
			<view class="bottom-space"></view>
		</view>
		<!-- 底部按钮 -->
		<view class="footer_btn">
			<view class="footer-actions">
				<view class="action-icon" @click="callShop">
					<view class="action-icon-circle">
						<image src="/static/zlImage/kef.png" mode=""></image>
					</view>
					<view>客服</view>
				</view>
				<view class="action-icon" @click="updateFollow">
					<view class="action-icon-circle" :class="{'action-icon-circle--on': goodInfo.isCollect == 1}">
						<image src="/static/zlImage/sc.png" mode=""
							v-if="goodInfo.isCollect == 0"></image>
						<image src="/static/zlImage/sc_.png" mode=""
							v-if="goodInfo.isCollect == 1"></image>
					</view>
					<view>收藏</view>
				</view>
			</view>
			<view class="goxd" hover-class="cta-hover" @click="buyGoods">立即下单</view>
		</view>
		<!-- 配件弹框 -->
		<u-popup v-model="showPj" mode="bottom" border-radius="14" :closeable="true">
			<view class="pj-popup">
				<view class="pj-popup-title">配件清单</view>
				<view class="pj-popup-body">
					<scroll-view :scroll-y="true" style="height: 100%;">
						<view class="pj-section" v-if="goodInfo.accessoryList&&goodInfo.accessoryList.length > 0">
							<view class="pj-section-title">
								<view class="pj-icon-chip">
									<image src="/static/zlImage/image/icongh.png" mode="">
									</image>
								</view>
								<view>需归还</view>
							</view>
							<view class="pj-list">
								<view class="pj-item" v-for="(item, index) in goodInfo.accessoryList" :key="index">
									<text>{{item.accessoryName}}</text>
									<text class="pj-num" v-if="item.accessoryNum">*{{item.accessoryNum}}</text>
								</view>
							</view>
						</view>
						<view class="pj-section" v-if="goodInfo.giftList&&goodInfo.giftList.length > 0">
							<view class="pj-section-title">
								<view class="pj-icon-chip pj-icon-chip--gift">
									<image src="/static/zlImage/image/zengpin.png" mode="">
									</image>
								</view>
								<view>赠品</view>
							</view>
							<view class="pj-list">
								<view class="pj-item pj-item--gift" v-for="(item, index) in goodInfo.giftList" :key="index">
									<text>{{item.giftName}}</text>
									<text class="pj-num" v-if="item.giftNum">*{{item.giftNum}}</text>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
			</view>
		</u-popup>
		<!-- 商品规格弹框 -->
		<u-popup v-model="showgg" mode="bottom" border-radius="24" :closeable="true">
			<view class="spec-popup">
				<view class="spec-header">
					<view class="spec-image">
						<image :src="goodInfo.coverImg" mode="aspectFit">
						</image>
					</view>
					<view class="spec-info">
						<view class="spec-price">￥<text>{{goodPrice}}</text><text class="spec-price-unit" v-if="zqIndex != goodDays.length + 1">/天</text>
						</view>
						<view class="spec-buyout" v-if="goodInfo.supportBuyout == 1">
							到期买断价：¥{{isVip == 1?goodInfo.memberPrice:goodInfo.originalPrice}}</view>
						<view class="text_hid spec-title">
							{{goodInfo.title}}
						</view>
					</view>
				</view>
				<view class="spec-body">
					<scroll-view :scroll-y="true" style="height: 100%;">
						<view class="spec-option">
							<view class="option-label">提货方式</view>
							<view class="option-scroll">
								<scroll-view class="scroll_tab" :scroll-x="true" style="height: 100%;">
									<view class="option-list">
										<template v-if="goodInfo.isExpress">
											<view :class="thIndex == item?'thtabItemAic':'thtabItem'" @click="thChange(item)"
												v-for="(item,index) in goodInfo.isExpress.split(',')" :key="item">
												{{item == 1?'商家包邮':(item == 2?'不包邮(邮费'+goodInfo.postagePrice+'元)':'到店自提')}}
											</view>
										</template>
									</view>
								</scroll-view>
							</view>
						</view>
						<view v-for="(item,index) in goodInfo.attr" :key="index">
							<view v-for="(attr,attrIndex) in item.attrValue" :key="attrIndex">
								<view class="option-label">
									{{attr.value}}
								</view>
								<view class="option-scroll">
									<scroll-view class="scroll_tab" :scroll-x="true" style="height: 100%;">
										<view class="option-list" v-if="attr.detail">
											<template v-if="attrIndex == 0">
												<view :class="ncIndex == itemsIndex?'thtabItemAic':'thtabItem'"
													@click="ysChange(attrIndex,itemsIndex,items)"
													v-for="(items,itemsIndex) in attr.detail.split(',')" :key="itemsIndex">
													{{items}}
												</view>
											</template>
											<template v-if="attrIndex == 1">
												<view :class="ysIndex == itemsIndex?'thtabItemAic':'thtabItem'"
													@click="ysChange(attrIndex,itemsIndex,items)"
													v-for="(items,itemsIndex) in attr.detail.split(',')" :key="itemsIndex">
													{{items}}
												</view>
											</template>
											<template v-if="attrIndex == 2">
												<view :class="csIndex == itemsIndex?'thtabItemAic':'thtabItem'"
													@click="ysChange(attrIndex,itemsIndex,items)"
													v-for="(items,itemsIndex) in attr.detail.split(',')" :key="itemsIndex">
													{{items}}
												</view>
											</template>
										</view>
									</scroll-view>
								</view>
							</view>
						</view>
						<view class="spec-option">
							<view class="option-label">租期</view>
							<view class="days-grid">
								<view :class="zqIndex == index?'thtabItemAic':'thtabItem'" @click="cqChange(item,index)"
									v-for="(item,index) in goodDays" :key="index">
									{{item.dayName}}天
								</view>
								<view :class="zqIndex == goodDays.length + 1?'thtabItemAic':'thtabItem'"
									@click="cqChange('买断',goodDays.length + 1)" v-if="goodInfo.supportBuyout == 1">买断
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
				<view class="spec-footer">
					<view class="amount-row">
						<view class="option-label option-label--inline">租赁数量</view>
						<u-number-box v-model="value" @change="valChange" :min="1"></u-number-box>
					</view>
					<view @click="confirmBtn" class="qdbtn" hover-class="cta-hover">
						确定
					</view>
				</view>
			</view>
		</u-popup>
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
				backText: '详情页',
				backTextStyle: {
					color: '#333333',
				},
				list: [{
						image: '/static/zlImage/delImage/xq.png',
					},
					{
						image: '/static/zlImage/delImage/xq.png',
					},
					{
						image: '/static/zlImage/delImage/xq.png',
					}
				],
				tabListIndex: 0,
				cjwttabIndex: 4,
				itemList: [],
				showgg: false,
				thIndex: '', //提货的index
				ysIndex: 0, //颜色的index
				csIndex: 0, //成色的index
				zqIndex: 0, //租期的index
				spIndex: 0, //商品的index
				ncIndex: 0, //内存的index
				upIndex: 0,
				ncName: '',
				ysName: '',
				csName: '',
				value: 1, //租赁数量
				goodId: '', //商品id
				goodInfo: {}, //商品信息
				isVip: uni.getStorageSync('isVip'), // isVip 1是会员 2不是
				showPj: false, //配件弹框
				pfText: '', //折旧赔付规则
				goodDays: [], //租赁天数，
				ZqjsText: '', //租期计算须知,
				ggChoose: '', //所选规格拼接
				questionItem: [],
				questionList: [],
				skuId: '', //所选规格id
				leaseTerm: '', //租赁天数
				evaluateList: [], //评论
				eheight: '40vh'
			}
		},
		computed: {
			goodPrice() {
				let price = 0; //商品价格
				let vipPrice = 0;
				let ggChoose = ''
				if (this.ncName) {
					ggChoose = this.ncName
					if (this.ysName) {
						ggChoose = this.ncName + ',' + this.ysName
						if (this.csName) {
							ggChoose = this.ncName + ',' + this.ysName + ',' + this.csName
						}
					}
				}
				this.ggChoose = ggChoose
				if (this.goodInfo && this.goodInfo.sku) { //没有选中买断--按照天数计算价格
					if (this.zqIndex != this.goodDays.length + 1) {
						this.goodInfo.sku.map(item => { //判断所选规格
							if (item.detailJson == this.ggChoose) {
								price = this.isVip == 1 ? item.memberRent : item.dailyRent
								this.skuId = item.id
							}
						})
					} else if (this.goodInfo.supportBuyout == 1 && this.zqIndex == this.goodDays.length + 1) { //选中买断
						this.goodInfo.sku.map(item => {
							if (item.detailJson == this.ggChoose) {
								price = this.isVip == 1 ? item.memberRent : item.skuOriginalPrice
								this.skuId = item.id
							}
						})
					}
				}
				return price
			}
		},
		onLoad(option) {
			this.goodId = option.goodId
			// 折旧赔付规则
			this.$Request.get('/app/common/type/518').then(res => {
				if (res.code == 0) {
					this.pfText = res.data.value
				}
			});
			// 租期计算须知
			this.$Request.get('/app/common/type/519').then(res => {
				if (res.code == 0) {
					this.ZqjsText = res.data.value
				}
			});
			this.findGoods()
			this.getQuestionList()
			this.getEvaluateList()
		},
		onShow() {},
		methods: {
			// 获取商品评价
			getEvaluateList() {
				let data = {
					page: 1,
					limit: 999,
					goodId: this.goodId
				}
				this.$Request.getT('/app/orders/getEvaluateList', data).then(res => {
					if (res.status == 0) {
						if (res.data.records.length > 2) {
							this.evaluateList = res.data.records.slice(0, 2)
						} else {
							this.evaluateList = res.data.records
						}
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			// 立即下单
			buyGoods() {
				if (!uni.getStorageSync('token')) {
					this.noLogin()
				} else {
					this.showgg = true
				}
			},
			// 联系客服
			callShop() {
				let that = this;
				uni.navigateTo({
					url: '/my/setting/chat?goodId=' + this.goodId
				})
			},
			confirmBtn() {
				if (!this.goodInfo.sku || this.goodInfo.sku.length == 0) {
					uni.showToast({
						title: '商品规格信息不存在',
						icon: 'none'
					})
					return
				}
				if (this.value > this.goodInfo.sku[0].stock) {
					uni.showToast({
						title: '库存不足清重新选择商品数量！',
						icon: 'none'
					})
					return
				}
				let data = {
					ggChoose: this.ggChoose,
					zqIndex: this.zqIndex,
					thIndex: this.thIndex,
					goodsId: this.goodInfo.id,
					goodPrice: this.goodPrice,
					goodsNum: this.value,
					skuId: this.skuId,
					leaseTerm: this.leaseTerm
				}
				uni.navigateTo({
					url: '/subpackage/goods/orderDet?data=' + encodeURIComponent(JSON.stringify(data))
				})
				this.showgg = false
			},
			previewImage(index) {
				let that = this;
				if (!that.goodInfo.img) return;
				const urls = that.goodInfo.img.split(',');
				uni.previewImage({
					current: urls[index],
					urls: urls
				});
			},
			// 顶部点击事件
			changeTabIndex(index) {
				this.tabListIndex = index
			},
			// 常见问题点击事件
			changecjwtTabIndex(index) {
				this.cjwttabIndex = index
				this.getQuestionList()
			},
			// 提货的点击切换
			thChange(index) {
				this.thIndex = index
			},
			spChange(index) {
				this.spIndex = index
			},
			// 颜色和内存的点击切换
			ysChange(upIndex, index, value) {
				this.upIndex = upIndex
				if (this.upIndex == 0) {
					this.ncIndex = index
					this.ncName = value
					console.log(index, "indexindex")
				} else if (this.upIndex == 1) {
					this.ysIndex = index
					this.ysName = value
				} else {
					this.csIndex = index
					this.csName = value
				}
			},
			// 租期的点击切换
			cqChange(item, index) {
				this.zqIndex = index
				if (item && item.dayName) {
					this.leaseTerm = item.dayName
				} else {
					this.leaseTerm = '买断'
				}
				console.log(index, "cqChangecqChange")
			},
			// 租赁数量加减
			valChange(e) {
				console.log('当前值为: ' + e.value)
			},
			kefuLeixing() {
				// 获取客服
				if (uni.getStorageSync('kefuType') == 1) {
					uni.makePhoneCall({
						phoneNumber: uni.getStorageSync('kefuPhone')
					});
				} else if (uni.getStorageSync('kefuType') == 3) {
					uni.navigateTo({
						url: '/my/kefu/kefu'
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
						console.error("catchcatch" + error)
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
			// 获取商品租赁天数
			getGoodsDays() {
				let data = {
					id: this.goodId,
					page: 1,
					limit: 999
				}
				this.$Request.getT('/app/leaseDays/getLeaseDaysList', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						this.goodDays = res.data.records
					}
				})
			},
			// 获取商品详情
			findGoods() {
				let data = {
					id: this.goodId,
					userId: uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
				}
				this.$Request.getT('/app/goods/findById', data).then(res => {
					if (res.code == 0 || res.status == 0) {
						this.goodInfo = res.data
						if (this.goodInfo.attr && this.goodInfo.attr[0] && this.goodInfo.attr[0].attrValue && this.goodInfo.attr[0].attrValue[0]) {
							this.ncName = this.goodInfo.attr[0].attrValue[0].detail.split(',')[0]
						}
						if (this.goodInfo.attr && this.goodInfo.attr[0] && this.goodInfo.attr[0].attrValue && this.goodInfo.attr[0].attrValue[1]) {
							this.ysName = this.goodInfo.attr[0].attrValue[1].detail.split(',')[0]
						}
						if (this.goodInfo.attr && this.goodInfo.attr[0] && this.goodInfo.attr[0].attrValue && this.goodInfo.attr[0].attrValue[2]) {
							this.csName = this.goodInfo.attr[0].attrValue[2].detail.split(',')[0]
						}
						if (this.goodInfo.isExpressList && this.goodInfo.isExpressList.length == 2) {
							this.goodInfo.isExpressList.splice(1, 0, 7)
						} else if (this.goodInfo.isExpressList && this.goodInfo.isExpressList.length > 2) {
							this.goodInfo.isExpressList.splice(1, 0, 7)
							this.goodInfo.isExpressList.splice(3, 0, 7)
						}
						console.log(this.goodInfo.isExpressList, 'this.goodInfo.isExpressList')
						this.thIndex = res.data.isExpressList ? res.data.isExpressList[0] : ''
						this.getGoodsDays()
					}
				})
			},
			// 收藏商品
			updateFollow() {
				if (uni.getStorageSync('token')) {
					let data = {
						byId: this.goodInfo.id,
						classify: 1 //类型 1商品收藏 2商品想要 3关注用户
					}
					this.$Request.postT('/app/scFollow/updateFollow', data).then(res => {
						if (res.code == 0) {
							console.log(res, "32233333333333")
							uni.showToast({
								title: res.data,
								icon: 'none'
							})
							setTimeout(() => {
								this.findGoods()
							}, 50)
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				} else {
					this.noLogin()
				}
			},
			noLogin() {
				let that = this;
				uni.showModal({
					title: '提示',
					content: '您还未登录,请先登录！',
					showCancel: false, // 是否显示取消按钮
					success: function(res) {
						if (res.confirm) {
							uni.navigateTo({
								url: '/pages/public/login'
							})
						}
					}
				});
			},
			getQuestionList() {
				let data = {
					helpClassifyId: this.cjwttabIndex
				}
				this.$Request.getT('/app/help/selectHelpList').then(res => {
					if (res.code == 0 || res.status == 0) {
						this.questionList = res.data.data[0].children
						this.$Request.getT('/app/help/selectHelpWordList', data).then(res => {
							this.questionItem = res.data
						})
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background: linear-gradient(180deg, #D8F0EF 0%, #E4F4F2 28%, #EDF7F4 55%, #F3F9F7 100%);
	}

	button::after {
		border: none;
	}

	::v-deep button {
		background-color: transparent !important;
	}

	::v-deep uni-button {
		background-color: transparent;
		height: 62rpx;
		padding: 0;
	}

	::v-deep button {
		background-color: transparent;
		height: 62rpx;
		padding: 0;
	}

	.container_box {
		min-height: 100vh;
		padding-bottom: 200rpx;
		position: relative;
	}

	// 顶部品牌渐变横幅
	.page-hero {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 640rpx;
		background: linear-gradient(180deg, #16CAD1 0%, #10B0B3 38%, #46C7C4 66%, #9FE2DE 85%, #D8F0EF 100%);
		overflow: hidden;
	}

	.hero-glow {
		position: absolute;
		border-radius: 50%;
	}

	.hero-glow--a {
		width: 380rpx;
		height: 380rpx;
		right: -110rpx;
		top: -150rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.32) 0%, rgba(255, 255, 255, 0) 68%);
	}

	.hero-glow--b {
		width: 300rpx;
		height: 300rpx;
		left: -120rpx;
		bottom: -140rpx;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0) 70%);
	}

	.hero-ring {
		position: absolute;
		width: 220rpx;
		height: 220rpx;
		border-radius: 50%;
		border: 26rpx solid rgba(255, 255, 255, 0.12);
		right: 60rpx;
		bottom: -60rpx;
	}

	// 环境光晕（柔化页面中下段）
	.ambient {
		position: absolute;
		border-radius: 50%;
		z-index: 0;
	}

	.ambient--a {
		width: 440rpx;
		height: 440rpx;
		right: -160rpx;
		top: 1150rpx;
		background: radial-gradient(circle, rgba(23, 205, 212, 0.1) 0%, rgba(23, 205, 212, 0) 70%);
	}

	.ambient--b {
		width: 480rpx;
		height: 480rpx;
		left: -180rpx;
		top: 2600rpx;
		background: radial-gradient(circle, rgba(14, 168, 168, 0.08) 0%, rgba(14, 168, 168, 0) 70%);
	}

	.page-body {
		position: relative;
		z-index: 1;
	}

	// 轮播图
	.wrap {
		margin: 24rpx 30rpx 0;
		border-radius: 32rpx;
		overflow: hidden;
		box-shadow: 0 20rpx 50rpx rgba(11, 148, 155, 0.22);
		background: #fff;
		position: relative;
		border: 6rpx solid rgba(255, 255, 255, 0.85);
	}

	::v-deep .u-swiper-wrap {
		border-radius: 26rpx;
	}

	::v-deep .u-indicator-item-round-active {
		background-color: #0EA8A8;
	}

	// 商品信息主卡片
	.goodsTitle {
		width: calc(100% - 60rpx);
		margin: -70rpx auto 0;
		border-radius: 32rpx;
		overflow: hidden;
		box-shadow: 0 20rpx 50rpx rgba(11, 148, 155, 0.12);
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		position: relative;
		z-index: 2;
	}

	.goodsTitle_msg {
		padding: 36rpx 32rpx 30rpx;
		position: relative;
	}

	.price-row {
		display: flex;
		justify-content: space-between;
		align-items: flex-end;
	}

	.price {
		display: flex;
		align-items: baseline;
		color: #F1302E;

		.price-symbol {
			font-size: 30rpx;
			font-weight: 800;
			margin-right: 4rpx;
		}

		.price-num {
			font-size: 62rpx;
			font-weight: 800;
			line-height: 1;
			letter-spacing: -1rpx;
		}

		.price-unit {
			font-size: 24rpx;
			color: #9AA6A6;
			margin-left: 6rpx;
			font-weight: 500;
		}
	}

	.tags {
		display: flex;
		align-items: center;
		flex-shrink: 0;
		padding-bottom: 8rpx;
	}

	.tag-primary {
		background: linear-gradient(135deg, #0FB9BF 0%, #0EA8A8 100%);
		color: #FFFFFF;
		font-size: 22rpx;
		border-radius: 999rpx;
		padding: 10rpx 22rpx;
		margin-right: 14rpx;
		font-weight: 600;
		box-shadow: 0 8rpx 18rpx rgba(21, 202, 210, 0.3);
	}

	.tag-danger {
		background: linear-gradient(135deg, #FF6B5E 0%, #F1302E 100%);
		color: #FFFFFF;
		font-size: 22rpx;
		border-radius: 999rpx;
		padding: 10rpx 22rpx;
		margin-right: 14rpx;
		font-weight: 600;
		box-shadow: 0 8rpx 18rpx rgba(241, 48, 46, 0.25);
	}

	.title-section {
		padding: 22rpx 0 24rpx;

		.text_hid {
			font-size: 34rpx;
			color: #1A2323;
			font-weight: 700;
			width: 100%;
			line-height: 1.5;
		}
	}

	.text_hid {
		overflow: hidden;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		text-overflow: ellipsis;
		white-space: normal;
		-webkit-line-clamp: 2;
		line-height: 1.5;
	}

	// 服务承诺条
	.service-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		background: linear-gradient(120deg, #F0FAFA 0%, #E9F6F4 100%);
		border: 1rpx solid rgba(21, 202, 210, 0.12);
		border-radius: 20rpx;
		padding: 20rpx 24rpx;
	}

	.service-badge {
		image {
			width: 81rpx;
			height: 29rpx;
		}
	}

	.service-item {
		display: flex;
		align-items: center;
		font-size: 22rpx;
		color: #4A5858;
		font-weight: 500;

		image {
			width: 26rpx;
			height: 29rpx;
			margin-right: 6rpx;
		}
	}

	// 通用卡片
	.container,
	.goodsDet,
	.flowCard {
		width: calc(100% - 60rpx);
		margin: auto;
		border-radius: 32rpx;
		margin-top: 28rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		overflow: hidden;
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		box-shadow: 0 16rpx 44rpx rgba(11, 148, 155, 0.07);
	}

	// 流程卡（浅青渐变，打破全白）
	.flowCard {
		background: linear-gradient(150deg, #F4FBF9 0%, #E3F3EF 100%);
		border: 1rpx solid rgba(21, 202, 210, 0.14);
	}
	.goodsCs{
		width: calc(100% - 30rpx);
		margin: auto;
		border-radius: 32rpx;
		margin-top: 28rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #F4FAF8 100%);
		overflow: hidden;
		border: 1rpx solid rgba(255, 255, 255, 0.7);
		box-shadow: 0 16rpx 44rpx rgba(11, 148, 155, 0.07);
	}
	.container {
		padding: 36rpx 34rpx;
	}

	// 卡片标题（强调条）
	.card-head {
		display: flex;
		align-items: baseline;
		justify-content: space-between;
		padding: 32rpx 34rpx 0;
	}

	.card-head-title {
		font-size: 32rpx;
		font-weight: 800;
		color: #1A2323;
		position: relative;
		padding-left: 24rpx;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 10rpx;
			height: 32rpx;
			border-radius: 6rpx;
			background: linear-gradient(180deg, #15CAD2 0%, #0EA8A8 100%);
			box-shadow: 0 4rpx 10rpx rgba(21, 202, 210, 0.4);
		}
	}

	.card-head-sub {
		font-size: 22rpx;
		color: #9AA6A6;
	}

	// 租赁流程
	.goodsLc {
		position: relative;
		display: flex;
		justify-content: space-between;
		padding: 44rpx 34rpx 40rpx;
	}

	.lc-track {
		position: absolute;
		top: 72rpx;
		left: 15%;
		right: 15%;
		height: 4rpx;
		border-radius: 4rpx;
		background: linear-gradient(90deg, rgba(21, 202, 210, 0.1) 0%, rgba(21, 202, 210, 0.5) 50%, rgba(21, 202, 210, 0.1) 100%);
	}

	.lc-step {
		display: flex;
		flex-direction: column;
		align-items: center;
		font-size: 22rpx;
		color: #4A5858;
		font-weight: 500;
		width: 25%;
		position: relative;
		z-index: 1;

		text {
			margin-top: 18rpx;
		}
	}

	.lc-dot {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background: linear-gradient(145deg, #19CFD6 0%, #0CA5A9 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 26rpx;
		font-weight: 800;
		color: #FFFFFF;
		border: 6rpx solid #FFFFFF;
		box-shadow: 0 0 0 4rpx rgba(21, 202, 210, 0.16), 0 10rpx 24rpx rgba(21, 202, 210, 0.35);
	}

	// 参数卡片
	.goodsCs {
		padding: 12rpx 34rpx;
		width: calc(100% - 60rpx);
	}

	.goodsCs_ {
		display: flex;
		align-items: center;
		padding: 30rpx 0;
		border-bottom: 1rpx solid #F2F5F5;

		&:last-child {
			border-bottom: none;
		}

		.cs-label {
			color: #0A9A9A;
			font-size: 22rpx;
			font-weight: 700;
			flex-shrink: 0;
			background: linear-gradient(135deg, #E6F8F8 0%, #DCF3F2 100%);
			border-radius: 12rpx;
			padding: 8rpx 16rpx;
			margin-right: 22rpx;
		}

		.cs-content {
			flex: 1;
			color: #2E3A3A;
			font-size: 26rpx;
			line-height: 1.6;
			min-width: 0;
		}

		.cs-arrow-wrap {
			width: 44rpx;
			height: 44rpx;
			border-radius: 50%;
			background: #E9F2EF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-left: auto;
			flex-shrink: 0;
		}

		.cs-arrow {
			width: 12rpx;
			height: 22rpx;
			opacity: 0.55;
		}
	}

	// 详情选项卡
	.goodsDet {
		padding: 12rpx;
	}

	.slot-wrap {
		display: flex;
		background: #E7F1EE;
		border-radius: 999rpx;
		padding: 8rpx;
	}

	.tabItem {
		flex: 1;
		height: 72rpx;
		line-height: 72rpx;
		text-align: center;
		font-size: 26rpx;
		color: #7A8A8A;
		border-radius: 999rpx;
		transition: all 0.25s;
		font-weight: 500;
	}

	.tabAic {
		flex: 1;
		height: 72rpx;
		line-height: 72rpx;
		text-align: center;
		font-weight: 700;
		font-size: 26rpx;
		color: #FFFFFF;
		border-radius: 999rpx;
		background: linear-gradient(135deg, #15CAD2 0%, #0EA8A8 100%);
		box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.35);
		transition: all 0.25s;
	}

	// 详情内容
	::v-deep .u-parse {
		font-size: 28rpx;
		line-height: 1.8;
		color: #4A5858;
	}

	// 租赁须知
	.section-title {
		font-size: 32rpx;
		font-weight: 800;
		color: #1A2323;
		position: relative;
		padding-left: 24rpx;
		margin-bottom: 30rpx;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 10rpx;
			height: 32rpx;
			border-radius: 6rpx;
			background: linear-gradient(180deg, #15CAD2 0%, #0EA8A8 100%);
			box-shadow: 0 4rpx 10rpx rgba(21, 202, 210, 0.4);
		}
	}

	.plan-block {
		margin-bottom: 12rpx;
	}

	.zhfa {
		font-size: 24rpx;
		display: flex;
		padding: 18rpx 0 26rpx;
		justify-content: space-between;
		align-items: center;
		flex-wrap: wrap;
		line-height: 40rpx;

		image {
			width: 10rpx;
			height: 18rpx;
			opacity: 0.4;
		}

		view {
			flex-shrink: 0;
			background: #F4FAFA;
			border: 1rpx solid rgba(21, 202, 210, 0.16);
			color: #0B7F82;
			border-radius: 999rpx;
			padding: 10rpx 18rpx;
			font-size: 22rpx;
			font-weight: 500;
		}
	}

	.timeline {
		position: relative;
		text-align: center;
		height: 56rpx;
		display: flex;
		align-items: center;
		margin: 24rpx 0 18rpx;

		.grayLeft,
		.grayRight,
		.grayCent {
			height: 56rpx;
			border-radius: 28rpx;
			font-size: 22rpx;
			line-height: 56rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			font-weight: 500;
		}

		.grayLeft {
			background: #E3EFED;
			width: 34%;
			padding-right: 8%;
			color: #7A8A8A;
		}

		.grayRight {
			background: #E3EFED;
			width: 34%;
			padding-left: 8%;
			color: #7A8A8A;
			position: absolute;
			left: 66%;
		}

		.grayCent {
			background: linear-gradient(90deg, #15CAD2, #0CA5A9);
			color: #FFFFFF;
			width: 50%;
			position: absolute;
			z-index: 2;
			left: 25%;
			box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.35);
			font-weight: 700;
			letter-spacing: 2rpx;
		}
	}

	.zlwz {
		padding: 14rpx 34rpx 30rpx;
		display: flex;
		font-size: 22rpx;
		color: #0A9A9A;
		justify-content: space-between;
		font-weight: 500;
	}

	.zlwz--last {
		padding-bottom: 36rpx;
	}

	.freight-table {
		background: #EBF5F2;
		border-radius: 24rpx;
		overflow: hidden;
		padding: 16rpx;
	}

	.yfxz {
		background: linear-gradient(135deg, #0FB9BF 0%, #0CA5A9 100%);
		font-size: 24rpx;
		height: 68rpx;
		border-radius: 14rpx;
		line-height: 68rpx;
		text-align: center;
		font-weight: 700;
		color: #FFFFFF;
		flex: 1;
		margin: 0 8rpx;
	}

	.yfxzText {
		font-size: 24rpx;
		height: 68rpx;
		line-height: 68rpx;
		border-radius: 14rpx;
		color: #4A5858;
		text-align: center;
		flex: 1;
		margin: 0 8rpx;
	}

	.yfxzBox {
		display: flex;
		justify-content: space-between;
		margin-top: 14rpx;
		border-radius: 14rpx;
		overflow: hidden;
		background-color: #FFFFFF;
		padding: 6rpx 0;
		box-shadow: 0 4rpx 14rpx rgba(16, 60, 62, 0.04);
	}

	// 常见问题
	.tabcjwtItem {
		min-width: 140rpx;
		height: 60rpx;
		border-radius: 999rpx;
		font-size: 24rpx;
		line-height: 60rpx;
		text-align: center;
		background-color: #E7F1EE;
		color: #7A8A8A;
		padding: 0 28rpx;
		transition: all 0.25s;
		font-weight: 500;
	}

	.tabcjwtAic {
		min-width: 140rpx;
		height: 60rpx;
		border-radius: 999rpx;
		font-size: 24rpx;
		line-height: 60rpx;
		text-align: center;
		padding: 0 28rpx;
		background: linear-gradient(135deg, #15CAD2 0%, #0EA8A8 100%);
		color: #FFFFFF;
		font-weight: 600;
		box-shadow: 0 8rpx 18rpx rgba(21, 202, 210, 0.3);
		transition: all 0.25s;
	}

	::v-deep.u-collapse-title {
		font-size: 26rpx;
		font-weight: 600;
		color: #2E3A3A;
	}

	::v-deep .u-collapse-content {
		font-size: 24rpx !important;
		color: #4A5858;
		line-height: 1.7;
	}

	::v-deep .u-collapse-item {
		border-bottom: solid 1rpx #F2F5F5;
	}

	::v-deep .u-collapse-item:last-child {
		border-bottom: none !important;
	}

	// 评价
	.more {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-bottom: 28rpx;
	}

	.pingj {
		font-size: 32rpx;
		font-weight: 800;
		color: #1A2323;
		position: relative;
		padding-left: 24rpx;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 10rpx;
			height: 32rpx;
			border-radius: 6rpx;
			background: linear-gradient(180deg, #15CAD2 0%, #0EA8A8 100%);
			box-shadow: 0 4rpx 10rpx rgba(21, 202, 210, 0.4);
		}
	}

	.more-link {
		font-size: 24rpx;
		color: #0A9A9A;
		font-weight: 600;
		background: linear-gradient(135deg, #E6F8F8 0%, #DCF3F2 100%);
		border-radius: 999rpx;
		padding: 10rpx 20rpx;
		display: flex;
		align-items: center;
	}

	.container_ {
		width: calc(100% - 48rpx);
		margin: auto;
		display: flex;
		margin-bottom: 24rpx;
		background: #EDF6F3;
		border-radius: 24rpx;
		padding: 24rpx;

		&:last-child {
			margin-bottom: 0;
		}
	}

	.imagepl {
		width: 72rpx;
		height: 72rpx;
		border-radius: 50%;
		margin-right: 20rpx;
		box-shadow: 0 0 0 4rpx rgba(21, 202, 210, 0.14);
		flex-shrink: 0;
	}

	.label {
		font-size: 26rpx;
		color: #2E3A3A;
		padding-top: 10rpx;
		line-height: 1.5;
	}

	.eval-info {
		flex: 1;
		min-width: 0;
	}

	.eval-name {
		color: #7A8A8A;
		font-size: 26rpx;
		font-weight: 600;
	}

	.eval-time {
		margin-left: auto;
		color: #9AA6A6;
		font-size: 22rpx;
		flex-shrink: 0;
	}

	.reply-row {
		margin-left: 92rpx;
		background: #FFFFFF;
		border: 1rpx solid rgba(21, 202, 210, 0.16);
		border-radius: 24rpx;
		width: calc(100% - 140rpx);
	}

	.bottom-space {
		height: 60rpx;
	}

	// 底部
	.footer_btn {
		width: calc(100% - 60rpx);
		left: 30rpx;
		background: #FFFFFF;
		height: 124rpx;
		position: fixed;
		bottom: 24rpx;
		padding: 0 28rpx;
		text-align: center;
		display: flex;
		justify-content: space-between;
		align-items: center;
		z-index: 3;
		border-radius: 999rpx;
		box-shadow: 0 16rpx 44rpx rgba(16, 60, 62, 0.16);
	}

	.goxd {
		background: linear-gradient(135deg, #15CAD2 0%, #0CA5A9 100%);
		border-radius: 999rpx;
		width: 400rpx;
		height: 88rpx;
		line-height: 88rpx;
		text-align: center;
		color: #FFFFFF;
		font-size: 30rpx;
		font-weight: 700;
		box-shadow: 0 12rpx 28rpx rgba(21, 202, 210, 0.4);
		letter-spacing: 4rpx;
	}

	.cta-hover {
		opacity: 0.88;
		transform: scale(0.97);
	}

	.footer-actions {
		display: flex;
		align-items: center;
	}

	.action-icon {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		width: 104rpx;

		view {
			font-size: 20rpx;
			color: #7A8A8A;
			margin-top: 6rpx;
		}
	}

	.action-icon-circle {
		width: 64rpx;
		height: 64rpx;
		border-radius: 50%;
		background: #E9F2EF;
		display: flex;
		align-items: center;
		justify-content: center;

		image {
			width: 38rpx;
			height: 37rpx;
		}
	}

	.action-icon-circle--on {
		background: linear-gradient(135deg, #FFF1F0 0%, #FFE3E1 100%);
	}

	// 弹框
	.qdbtn {
		width: 100%;
		background: linear-gradient(135deg, #15CAD2 0%, #0CA5A9 100%);
		color: #FFFFFF;
		height: 92rpx;
		line-height: 92rpx;
		border-radius: 999rpx;
		text-align: center;
		margin-top: 24rpx;
		font-size: 30rpx;
		font-weight: 700;
		box-shadow: 0 12rpx 28rpx rgba(21, 202, 210, 0.35);
		letter-spacing: 4rpx;
	}

	// 规格弹框
	.thtabItemAic {
		color: #FFFFFF;
		background: linear-gradient(135deg, #15CAD2 0%, #0CA5A9 100%);
		font-size: 26rpx;
		border-radius: 16rpx;
		height: 72rpx;
		line-height: 72rpx;
		text-align: center;
		margin-right: 20rpx;
		white-space: nowrap;
		padding: 0 38rpx;
		font-weight: 600;
		box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.32);
	}

	.thtabItem {
		color: #4A5858;
		background: #EAF2EF;
		font-size: 26rpx;
		border-radius: 16rpx;
		height: 72rpx;
		line-height: 72rpx;
		text-align: center;
		padding: 0 38rpx;
		margin-right: 20rpx;
		white-space: nowrap;
		font-weight: 500;
	}

	.tag {
		font-size: 22rpx;
		border-radius: 10rpx;
		padding: 8rpx 16rpx;
		margin-right: 16rpx;
		font-weight: 700;
		flex-shrink: 0;
	}

	.tag-return {
		background: linear-gradient(135deg, #E6F8F8 0%, #DCF3F2 100%);
		color: #0A9A9A;
	}

	.tag-gift {
		background: linear-gradient(135deg, #FFF4E8 0%, #FFE9D2 100%);
		color: #F07300;
	}

	// 商品参数
	.express-list {
		display: flex;
		flex-wrap: wrap;
	}

	.express-item {
		display: flex;
		align-items: center;
		font-size: 26rpx;
		color: #2E3A3A;
	}

	.express-sep {
		color: #B9C6C6;
		margin: 0 10rpx;
	}

	.accessory-box {
		display: flex;
		justify-content: space-between;
		width: 100%;
		align-items: center;
	}

	.accessory-row {
		display: flex;
		align-items: center;
		margin-bottom: 14rpx;

		&:last-child {
			margin-bottom: 0;
		}
	}

	.accessory-name {
		font-size: 26rpx;
		color: #2E3A3A;
	}

	// 租赁须知
	.plan-title {
		font-size: 28rpx;
		font-weight: 700;
		display: flex;
		align-items: center;
		color: #1A2323;
		margin-top: 20rpx;

		image {
			width: 30rpx;
			height: 30rpx;
			margin-right: 10rpx;
		}
	}

	.notice-text {
		font-size: 24rpx;
		color: #4A5858;
		line-height: 1.8;
		background: #EDF6F4;
		border-radius: 20rpx;
		padding: 24rpx 28rpx;
		border-left: 8rpx solid rgba(21, 202, 210, 0.35);
	}

	.freight-header {
		display: flex;
		justify-content: space-between;
		margin-bottom: 14rpx;
	}

	// 常见问题
	.collapse-tabs {
		justify-content: flex-start;
		flex-wrap: wrap;
		background: transparent;
		padding: 0 0 20rpx;
		border-radius: 0;

		.tabcjwtItem,
		.tabcjwtAic {
			margin-right: 18rpx;
			margin-bottom: 16rpx;
		}
	}

	.collapse-content {
		width: 100%;
		margin: auto;
	}

	// 规格弹框
	.spec-popup {
		padding: 0 30rpx 40rpx;
	}

	.spec-header {
		display: flex;
		padding: 34rpx 56rpx 8rpx 0;
		align-items: flex-end;
	}

	.spec-image {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 8rpx;
		box-shadow: 0 12rpx 32rpx rgba(16, 60, 62, 0.12);
		flex-shrink: 0;

		image {
			width: 200rpx;
			height: 200rpx;
			border-radius: 18rpx;
		}
	}

	.spec-info {
		padding-left: 26rpx;
		flex: 1;
		min-width: 0;
	}

	.spec-price {
		color: #F1302E;
		font-size: 26rpx;
		font-weight: 800;
		margin-bottom: 12rpx;

		text {
			font-size: 52rpx;
			line-height: 1;
		}

		.spec-price-unit {
			font-size: 24rpx;
			color: #9AA6A6;
			font-weight: 500;
			margin-left: 4rpx;
		}
	}

	.spec-buyout {
		background: linear-gradient(135deg, #FFF1F0 0%, #FFE3E1 100%);
		color: #F1302E;
		font-size: 22rpx;
		text-align: center;
		width: max-content;
		padding: 10rpx 20rpx;
		border-radius: 999rpx;
		font-weight: 600;
	}

	.spec-title {
		width: 95%;
		font-size: 28rpx;
		font-weight: 700;
		color: #1A2323;
		padding-top: 14rpx;
	}

	.spec-body {
		width: 100%;
		height: 620rpx;
		margin-top: 24rpx;
	}

	.spec-option {
		margin-bottom: 10rpx;
	}

	.option-label {
		font-size: 28rpx;
		font-weight: 700;
		margin-top: 30rpx;
		margin-bottom: 22rpx;
		color: #1A2323;
		position: relative;
		padding-left: 20rpx;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 8rpx;
			height: 26rpx;
			border-radius: 6rpx;
			background: linear-gradient(180deg, #15CAD2 0%, #0EA8A8 100%);
		}
	}

	.option-label--inline {
		margin-top: 0;
		margin-bottom: 0;
	}

	.option-scroll {
		height: 72rpx;
	}

	.option-list {
		display: flex;
	}

	.days-grid {
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;

		.thtabItem,
		.thtabItemAic {
			margin-bottom: 22rpx;
			width: 206rpx;
			margin-right: 0;
			padding: 0;
		}
	}

	.spec-footer {
		width: 100%;
		background-color: #FFFFFF;
		padding-top: 24rpx;
		border-top: 1rpx solid #F2F5F5;
	}

	.amount-row {
		justify-content: space-between;
		align-items: center;
		display: flex;
		width: 100%;
	}

	// 配件弹框
	.pj-popup {
		padding: 44rpx 30rpx 30rpx;
	}

	.pj-popup-title {
		text-align: center;
		font-size: 34rpx;
		font-weight: 800;
		color: #1A2323;
		padding-bottom: 28rpx;
		border-bottom: 1rpx solid #F2F5F5;
		position: relative;

		&::after {
			content: '';
			position: absolute;
			left: 50%;
			bottom: -3rpx;
			transform: translateX(-50%);
			width: 72rpx;
			height: 6rpx;
			border-radius: 6rpx;
			background: linear-gradient(90deg, #15CAD2, #0EA8A8);
		}
	}

	.pj-popup-body {
		width: 100%;
		height: 600rpx;
		margin-top: 28rpx;
	}

	.pj-section {
		margin-bottom: 34rpx;
	}

	.pj-section-title {
		display: flex;
		align-items: center;
		margin-bottom: 22rpx;
		font-size: 28rpx;
		font-weight: 700;
		color: #1A2323;
	}

	.pj-icon-chip {
		width: 60rpx;
		height: 60rpx;
		border-radius: 18rpx;
		background: linear-gradient(135deg, #E6F8F8 0%, #D5F2F0 100%);
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 16rpx;

		image {
			width: 40rpx;
			height: 40rpx;
		}
	}

	.pj-icon-chip--gift {
		background: linear-gradient(135deg, #FFF4E8 0%, #FFE9D2 100%);
	}

	.pj-list {
		display: flex;
		flex-wrap: wrap;
		gap: 18rpx;
	}

	.pj-item {
		background: #EDF6F3;
		border: 1rpx solid rgba(21, 202, 210, 0.14);
		border-radius: 18rpx;
		padding: 20rpx 26rpx;
		font-size: 26rpx;
		color: #2E3A3A;
		display: flex;
		justify-content: space-between;
		align-items: center;
		flex: 1;
		min-width: 280rpx;
	}

	.pj-item--gift {
		background: #FFFAF5;
		border: 1rpx solid rgba(240, 115, 0, 0.14);
	}

	.pj-num {
		color: #0A9A9A;
		font-weight: 700;
		margin-left: 16rpx;
	}
</style>
