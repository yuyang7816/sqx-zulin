<template>
	<div class="order-details-page">
		<!-- hero -->
		<div class="nf-page-hero order-details-page__hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">订单详情</h2>
			<p class="nf-page-hero__desc">查看订单基本信息、商品明细、收货与物流及用户评价详情</p>
		</div>

		<el-row :gutter="12" class="order-details-page__body">
			<el-col :span="15">
				<!-- 订单信息 -->
				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-document"></i>
						<span>订单信息</span>
					</div>
					<div class="order-details-page__info-grid">
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">订单号</span>
								<span class="order-details-page__info-value">{{ tableData.orderNum }}</span>
							</div>
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">订单备注</span>
								<span class="order-details-page__info-value">{{ tableData.descrition ? tableData.descrition : '未填写' }}</span>
							</div>
						</div>
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">订单状态</span>
								<span class="order-details-page__info-value">
									<span v-if="tableData.status === 0">待支付</span>
									<span v-if="tableData.status === 1">已支付待发货</span>
									<span v-if="tableData.status === 2">已发货待收货</span>
									<span v-if="tableData.status === 3">已收货</span>
									<span v-if="tableData.status === 4">待归还</span>
									<span v-if="tableData.status === 5">归还中</span>
									<span v-if="tableData.status === 6">已完成</span>
									<span v-if="tableData.status === 7">继续租赁</span>
									<span v-if="tableData.status === 8">买断</span>
									<span v-if="tableData.status === 9">已取消</span>
									<span v-if="tableData.status === 10">待评价</span>
								</span>
							</div>
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">支付方式</span>
								<span class="order-details-page__info-value">
									<span v-if="tableData.payWay === 1">微信</span>
									<span v-if="tableData.payWay === 2">微信公众号</span>
									<span v-if="tableData.payWay === 3">微信小程序</span>
									<span v-if="tableData.payWay === 4">app支付宝</span>
									<span v-if="tableData.payWay === 5">H5支付宝</span>
									<span v-if="tableData.payWay === 6">零钱</span>
									<span v-if="tableData.payWay === 7">积分</span>
									<span v-if="tableData.payWay === null || tableData.payWay === ''">未支付</span>
								</span>
							</div>
						</div>
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">创建时间</span>
								<span class="order-details-page__info-value">{{ tableData.createAt }}</span>
							</div>
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">付款时间</span>
								<span class="order-details-page__info-value">{{ tableData.payTime ? tableData.payTime : '未付款' }}</span>
							</div>
						</div>
					</div>
				</div>

				<!-- 商品信息 -->
				<div class="nf-table-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-goods"></i>
						<span>商品信息</span>
					</div>
					<div class="order-details-page__product-table">
						<div class="order-details-page__product-head">
							<span class="order-details-page__product-col order-details-page__product-col--img">商品图片</span>
							<span class="order-details-page__product-col order-details-page__product-col--title">商品标题</span>
							<span class="order-details-page__product-col order-details-page__product-col--price">商品价格</span>
							<span class="order-details-page__product-col order-details-page__product-col--pay">支付金额</span>
						</div>
						<div class="order-details-page__product-row">
							<div class="order-details-page__product-col order-details-page__product-col--img">
								<img :src="tableData.img" alt="" class="order-details-page__product-img">
							</div>
							<div class="order-details-page__product-col order-details-page__product-col--title">
								<span class="order-details-page__product-name">{{ tableData.title }}</span>
								<div class="order-details-page__product-spec">规格：{{ tableData.detailJson ? tableData.detailJson : '-' }}</div>
							</div>
							<div class="order-details-page__product-col order-details-page__product-col--price">
								<span>￥{{ tableData.price }} x {{ tableData.number }}</span>
							</div>
							<div class="order-details-page__product-col order-details-page__product-col--pay">
								<span>￥{{ tableData.payMoney }}</span>
							</div>
						</div>
					</div>
				</div>
			</el-col>

			<el-col :span="9">
				<!-- 收货信息 -->
				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-location-outline"></i>
						<span>收货信息</span>
					</div>
					<div class="order-details-page__info-grid order-details-page__info-grid--single">
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">收货人</span>
								<span class="order-details-page__info-value">{{ tableData.consignee }}</span>
							</div>
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">手机号</span>
								<span class="order-details-page__info-value">{{ tableData.mobile }}</span>
							</div>
						</div>
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item order-details-page__info-item--full">
								<span class="order-details-page__info-label">收货地址</span>
								<span class="order-details-page__info-value">{{ tableData.provinces }}{{ tableData.detail }}</span>
							</div>
						</div>
					</div>
				</div>

				<!-- 物流信息 -->
				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-truck"></i>
						<span>物流信息</span>
					</div>
					<div v-if="tableData.status == 3 || tableData.status == 4" class="order-details-page__info-grid order-details-page__info-grid--single">
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">物流公司</span>
								<span class="order-details-page__info-value">{{ tableData.expressName }}</span>
							</div>
							<div class="order-details-page__info-item">
								<span class="order-details-page__info-label">运单号</span>
								<span class="order-details-page__link" @click="refresh">{{ tableData.expressNumber }}</span>
							</div>
						</div>
						<div class="order-details-page__info-row">
							<el-button size="mini" type="primary" icon="document" @click="refresh">查看物流信息</el-button>
						</div>
					</div>
					<div v-else class="order-details-page__empty">暂无信息</div>
				</div>

				<!-- 评价信息 -->
				<div class="nf-filter-card order-details-page__card">
					<div class="nf-filter-card__head">
						<i class="el-icon-chat-dot-round"></i>
						<span>评价信息</span>
					</div>
					<div v-if="discussData" class="order-details-page__info-grid order-details-page__info-grid--single">
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item order-details-page__info-item--full">
								<span class="order-details-page__info-label">评价用户</span>
								<span class="order-details-page__info-value">{{ discussData.userName }}</span>
							</div>
						</div>
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item order-details-page__info-item--full">
								<span class="order-details-page__info-label">用户图像</span>
								<img :src="discussData.img" alt="" class="order-details-page__avatar">
							</div>
						</div>
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item order-details-page__info-item--full">
								<span class="order-details-page__info-label">评价内容</span>
								<span class="order-details-page__info-value">{{ discussData.content }}</span>
							</div>
						</div>
						<div class="order-details-page__info-row">
							<div class="order-details-page__info-item order-details-page__info-item--full">
								<span class="order-details-page__info-label">评价时间</span>
								<span class="order-details-page__info-value">{{ discussData.createTime }}</span>
							</div>
						</div>
					</div>
					<div v-else class="order-details-page__empty">暂无评价</div>
				</div>
			</el-col>
		</el-row>

		<el-dialog title="物流信息" :visible.sync="dialogVisible" width="60%">
			<div class="block" v-if="wuliuDate.result">
				<el-steps direction="vertical" :active="1" :reverse="reverse">
					<el-step v-for="(activity, index) in wuliuDate.result.list" :key="index" :title="activity.time" :description="activity.status"></el-step>
				</el-steps>
			</div>
			<div class="block" v-else>
				暂无物流信息
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
				expressName: '',
				expressNumber: '',
				orderName: '',
				orderNumber: '',
				dialogFormVisible: false,
				dialogVisible: false,
				tableData: {},
				discussData: {},
				wuliuDate: {},
				kdName: '快递单号',
				reverse: true
			}
		},

		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 获取订单详情数据
			dataSelect() {
				let id = this.$route.query.id
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/orders/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': id
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					if (data.data.expressNumber) {
						console.log('``````````````````````', data.data.expressNumber)
						var exNum = data.data.expressNumber.slice(0, 2)
						var phNum = data.data.mobile.slice(6, 10)
						if (exNum == 'SF') {
							var expressNumber = data.data.expressNumber + ':' + phNum
						} else {
							var expressNumber = data.data.expressNumber
						}
						this.wuliu(expressNumber, data.data.mobile)
					}
				})
			},
			// 获取地址数据
			discusselect() {
				let id = this.$route.query.id
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsComment/findByOrderId'),
					method: 'get',
					params: this.$http.adornParams({
						'orderId': id
					})
				}).then(({
					data
				}) => {
					if (data && data.status == 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.discussData = returnData
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 查看物流信息
			refresh() {
				this.dialogVisible = true
			},
			// 获取物流信息
			wuliu(expressNumber, mobile) {
				console.log('`````````', expressNumber)
				this.$http({
					url: this.$http.adornUrl2('/orders/findExpress'),
					method: 'get',
					params: this.$http.adornParams({
						'expressNumber': expressNumber,
						'mobile': mobile
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.wuliuDate = data.data;
						this.wuliuDate.result.list[0].icon = 'el-icon-success'
						this.wuliuDate.result.list[0].type = 'success'
						this.kdName = this.wuliuDate.result.expName
					} else {
						this.$message(data.msg);
					}
				})
			},
		},
		activated() {
			this.dataSelect()
			this.discusselect()
		}
	};
</script>

<style scoped>
.order-details-page {
	padding: 12px 16px;
}

.order-details-page__body {
	margin-top: 0;
}

.order-details-page__card {
	margin-bottom: 12px;
}

.order-details-page__info-grid {
	display: flex;
	flex-direction: column;
	gap: 12px;
}

.order-details-page__info-row {
	display: flex;
	flex-wrap: wrap;
	gap: 12px 16px;
}

.order-details-page__info-item {
	flex: 1 1 calc(50% - 8px);
	min-width: 140px;
	display: flex;
	flex-direction: column;
	gap: 4px;
}

.order-details-page__info-item--full {
	flex: 1 1 100%;
}

.order-details-page__info-grid--single .order-details-page__info-item {
	flex: 1 1 calc(50% - 8px);
}

.order-details-page__info-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1.2;
}

.order-details-page__info-value {
	font-size: 14px;
	color: var(--nf-text-primary, #303133);
	line-height: 1.5;
	word-break: break-all;
}

.order-details-page__link {
	font-size: 14px;
	color: var(--nf-brand, #15cad2);
	cursor: pointer;
	line-height: 1.5;
	word-break: break-all;
}

.order-details-page__link:hover {
	color: var(--nf-brand-deep, #109da4);
}

.order-details-page__empty {
	font-size: 14px;
	color: var(--nf-text-secondary, #909399);
	padding: 4px 0;
}

.order-details-page__product-table {
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-sm, 6px);
	overflow: hidden;
}

.order-details-page__product-head,
.order-details-page__product-row {
	display: flex;
	align-items: center;
}

.order-details-page__product-head {
	background: var(--nf-table-header-bg, #f7f8fa);
	font-size: 13px;
	font-weight: 600;
	color: var(--nf-text-primary, #303133);
}

.order-details-page__product-head .order-details-page__product-col {
	padding: 10px 12px;
}

.order-details-page__product-row {
	border-top: 1px solid var(--nf-border, #e8eaed);
	font-size: 14px;
	color: var(--nf-text-secondary, #606266);
}

.order-details-page__product-row .order-details-page__product-col {
	padding: 12px;
}

.order-details-page__product-col--img {
	flex: 0 0 80px;
}

.order-details-page__product-col--title {
	flex: 1 1 0;
	min-width: 0;
}

.order-details-page__product-col--price,
.order-details-page__product-col--pay {
	flex: 0 0 120px;
}

.order-details-page__product-img {
	width: 40px;
	height: 40px;
	object-fit: cover;
	border-radius: 4px;
	border: 1px solid var(--nf-border, #e8eaed);
}

.order-details-page__product-name {
	display: block;
	line-height: 1.5;
	margin-bottom: 4px;
	color: var(--nf-text-primary, #303133);
}

.order-details-page__product-spec {
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
}

.order-details-page__avatar {
	width: 40px;
	height: 40px;
	object-fit: cover;
	border-radius: 4px;
	border: 1px solid var(--nf-border, #e8eaed);
}

/* 详情页 primary 描边钮 */
.order-details-page .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.order-details-page .el-button--primary:not(.is-plain):hover,
.order-details-page .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 992px) {
	.order-details-page__info-item {
		flex: 1 1 100%;
	}

	.order-details-page__product-col--price,
	.order-details-page__product-col--pay {
		flex: 0 0 90px;
	}
}
</style>
