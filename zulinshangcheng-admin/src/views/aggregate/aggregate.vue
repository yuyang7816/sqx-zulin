<template>
	<div class="aggregate-page">
		<!-- hero -->
		<div class="nf-page-hero aggregate-page__hero">
			<a href="#" class="aggregate-page__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="aggregate-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-hero__badge">商品管理</span>
			<h2 class="nf-page-hero__title">集合商品</h2>
			<p class="nf-page-hero__desc">管理活动集合内的商品，支持按标题、类型与状态筛选，添加商品及上下架操作</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card aggregate-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="aggregate-page__filter-body">
				<div class="aggregate-page__filter-item">
					<label class="aggregate-page__filter-label">商品标题</label>
					<el-input class="aggregate-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
						v-model="title"></el-input>
				</div>
				<div class="aggregate-page__filter-item">
					<label class="aggregate-page__filter-label">商品类型</label>
					<el-cascader class="aggregate-page__filter-control aggregate-page__filter-cascader" v-model="value" :options="classDatas" :show-all-levels="false"
						:change-on-select="true" :props="{ expandTrigger: 'hover' }" @change="handleChange">
					</el-cascader>
				</div>
				<div class="aggregate-page__filter-item">
					<label class="aggregate-page__filter-label">商品状态</label>
					<el-select v-model="status" placeholder="请选择商品状态" class="aggregate-page__filter-control" @change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="aggregate-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary" icon="document" @click="recomNotice">添加商品</el-button>
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card aggregate-page__table">
		<el-table v-loading="tableDataLoading" :data="tableData.content">
			<el-table-column fixed prop="id" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="store" label="商家名称" width="150" align="center">
				<template slot-scope="scope">
					<span v-if="scope.row.store == null ">自营商城</span>
					<span v-else>{{scope.row.store.storeName}}</span>
				</template>
			</el-table-column>
			<el-table-column fixed prop="coverImg" label="商品图片">
				<template slot-scope="scope">
					<img :src="scope.row.coverImg" alt="" width="60" height="60">
				</template>
			</el-table-column>
			<el-table-column prop="type" label="商品类型">
				<template slot-scope="scope">
					<span v-if="scope.row.type == null ">无数据</span>
					<span v-else>{{scope.row.type.name}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="title" label="商品标题" width="200">
			</el-table-column>
			<el-table-column prop="originalPrice" label="商品原价">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.originalPrice | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="price" label="商品价格">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="memberPrice" label="会员价格">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.memberPrice | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="commissionPrice" label="佣金">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.commissionPrice | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="sales" label="商品销量">
			</el-table-column>
			<el-table-column prop="buyReason" label="必买理由" width="200">
			</el-table-column>
			<el-table-column label="精选好物" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.isSelect == 0">否</span>
					<span v-if="scope.row.isSelect == 1">是</span>
				</template>
			</el-table-column>
			<el-table-column label="首页商品" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.homeGoods== 0">否</span>
					<span v-if="scope.row.homeGoods == 1">是</span>
				</template>
			</el-table-column>
			<el-table-column label="每日推荐" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.isRecommend== 0">否</span>
					<span v-if="scope.row.isRecommend == 1">是</span>
				</template>
			</el-table-column>
			<el-table-column label="是否需要发货" width="150">
				<template slot-scope="scope">
					<span v-if="scope.row.isExpress == 1">普通商品需要发货</span>
					<span v-if="scope.row.isExpress== 2">虚拟商品无需发货</span>
					<span v-if="scope.row.isExpress == 3">门店核销无需发货</span>
				</template>
			</el-table-column>
			<el-table-column prop="createAt" label="创建时间" width="160">
			</el-table-column>
			<el-table-column fixed='right' label="上下架">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.status" @change="change(scope.row.id)" :active-value="openValue"
						:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
					</el-switch>
				</template>
			</el-table-column>
			<el-table-column fixed="right" prop="status" label="商品状态">
				<template slot-scope="scope">
					<span style="color: #15cad2;cursor: pointer;" v-if="scope.row.status === 1 ">上架</span>
					<span style="color: #15cad2;cursor: pointer;" v-if="scope.row.status === 2 ">下架</span>
				</template>
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="180">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="discuss(scope.$index, scope.row)">商品评价
					</el-button>
					<!-- <el-button size="mini" type="primary" :disabled="!isAuth('shopAdmin:update')"
						@click="shopUpdates(scope.$index, scope.row)">修改
					</el-button> -->
					<el-button size="mini" type="danger" :disabled="!isAuth('shopAdmin:delete')"
						@click="shopdeletes(scope.row)">删除
					</el-button>
				</template>
			</el-table-column>
		</el-table>
			<div class="aggregate-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
		</div>
		<!-- 添加弹框 -->
		<el-dialog title="选择商品" customClass="customWidth" :visible.sync="dialogFormVisible6" center>
			<div style="position: relative;display: inline-block;float: right;">
				<!-- <el-input style="width: 150px;" @keydown.enter.native="shousuo" clearable placeholder="请输入商家名称"
			  	v-model="storeQ"></el-input>&nbsp;&nbsp;&nbsp;&nbsp; -->
				<el-input style="width: 200px;" @keydown.enter.native="shousuo" placeholder="根据标题搜索商品" v-model="titleT">
				</el-input>
				<span @click="shousuo" style="position: absolute;right: 0;top:8px;">
					<icon-svg name="shousuo" class="site-sidebar__menu-icon"></icon-svg>
				</span>
			</div>
			<el-table v-loading="tableDataLoading1" height="450" :data="choicenData2.content">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="store" label="商家名称" width="150" align="center">
					<template slot-scope="scope">
						<span v-if="scope.row.store == null ">自营商城</span>
						<span v-else>{{scope.row.store.storeName}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed prop="coverImg" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.coverImg" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="originalPrice" label="商品原价">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.originalPrice | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="memberPrice" label="会员价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.memberPrice | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="commissionPrice" label="佣金">
				</el-table-column>
				<el-table-column prop="sales" label="商品销量">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" prop="id">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="recomNoticeTo(scope.row)">添加
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page1"
					layout="total,sizes, prev, pager, next" :total="choicenData2.totalElements">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page:1,
				page1:1,
				pagelist: [],
				openValue: 1,
				closeValue: 2,
				id: '',
				img: '',
				title: '',
				titleT: '',
				type: '',
				typeId: '',
				readonly: true,
				merchants: '',
				price: '',
				memberPrice: '',
				originalPrice: '',
				sales: '',
				status: 0,
				status2: '',
				descrition: '',
				totalnum: 0,
				imgUrl: '',
				linkUrl: '',
				value: [],
				classDatas: [],
				form1: {
					id: '',
					title: '',
					img: '',
					typeId: '',
					merchants: '',
					price: '',
					memberPrice: '',
					status: '',
					sales: '',
					descrition: '',
					originalPrice: '',
					createAt: ''
				},
				statusmain: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				statusmain2: [{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				tableDataLoading1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible6: false,
				typeDatas: [],
				advertData: [],
				tableData: [],
				activityId: '',
				choicenData2: [],
				store: '',
				storeQ: '',
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后三位
					let tempVal = parseFloat(value).toFixed(3)
					realVal = tempVal.substring(0, tempVal.length - 1)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		computed: {
			page() {
				this.pagelist = this.$store.state.common.pagelist;
				var page = 1;
				for (let val of this.pagelist) {
					if (val.router == "shopAdmin") {
						page = val.page;
					}
				}
				return page;
			}
		},
		methods: {
			// 上下架
			change(id) {
				this.$http({
					url: this.$http.adornUrl2('/goods/updateStatus'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': id
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
					})
				})
			},
			// 刷新
			refresh() {
				this.shopSelect()
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.typeId = element;
				});
			},

			handleAvatarSuccess(file) {
				this.img = file.data;
			},
			handleAvatarSuccess2(file) {
				this.form1.img = file.data;
			},
			handleSizeChange(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.shopSelect()
			},
			handleSizeChange4(val) {
				this.size = val;
				this.handpick()
			},
			handleCurrentChange4(val) {
				this.page1 = val;
				this.handpick()
			},
			//搜索
			select() {
				for (let item of this.pagelist) {
					if (item.router == "shopAdmin") {
						item.page = 1;
					}
				}
				this.$store.commit("common/updataPage", this.pagelist);
				this.size = 10
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.status = 0
				this.typeId = ''
				this.store = ''
				this.value = []
				this.shopSelect()
			},
			//发布商品
			addShop() {
				this.$router.push({
					path: '/shopPublish',
					query: {
						activityId: this.activityId
					}
				})
			},
			// 修改商品
			shopUpdates(index, rows) {
				this.$router.push({
					path: '/shopAmend',
					query: {
						id: rows.id
					}
				});
			},
			// 商品评价跳转
			discuss(index, rows) {
				this.$router.push({
					path: '/discuss',
					query: {
						goodsId: rows.id
					}
				});
			},
			// 删除商品
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/goods/updateGoodsActivity'),
						method: 'post',
						params: this.$http.adornParams({
							'goodsId': row.id,
							'activityId': this.activityId,
							'type': 2
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取商品数据
			shopSelect() {
				this.activityId = this.$route.query.activityId
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'title': this.title,
						'type': this.typeId,
						'status': this.status,
						'activityId': this.activityId,
						// 'store':this.store,
						'storeId': -1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					this.totalnum = returnData.totalElements
				})
			},
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goodsType/result'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classDatas = returnData
				})
			},
			// 返回上一级
			prev() {
				// this.$router.back()
				this.$router.push({
					path: '/storeList',
					query: {
						name: '集合分类'
					}
				})
			},
			// 精选淘宝商品
			handpick() {
				let page = this.page1 - 1
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl2('/goods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'title': this.titleT,
						'type': this.type,
						'status': this.status,
						'notActivityId': this.activityId,
						// 'store':this.storeQ
						'storeId': -1
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.tableDataLoading1 = false
						let returnData = data.data;
						this.choicenData2 = returnData
					}
				})
			},
			//添加集合商品
			recomNotice() {
				this.handpick()
				this.dialogFormVisible6 = true
			},
			// 添加集合商品
			recomNoticeTo(row) {
				this.$http({
					url: this.$http.adornUrl2('/goods/updateGoodsActivity'),
					method: 'post',
					params: this.$http.adornParams({
						'goodsId': row.id,
						'activityId': this.activityId,
						'type': 1
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.handpick()
								this.shopSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},

			shousuo() {
				this.page = 1

				this.handpick()
			},
		},
		mounted() {
			this.shopSelect()
			this.dataSelect()
		}
	};
</script>

<style scoped>
.aggregate-page {
	padding: 12px 16px;
}

.aggregate-page__back {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	margin-bottom: 12px;
	font-size: 14px;
	color: var(--nf-text-secondary, #606266);
	text-decoration: none;
	line-height: 1;
}

.aggregate-page__back:hover {
	color: var(--nf-brand, #15cad2);
}

.aggregate-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	flex-shrink: 0;
}

.aggregate-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.aggregate-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.aggregate-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.aggregate-page__filter-control {
	width: 180px;
}

.aggregate-page__filter-cascader {
	width: 180px;
}

.aggregate-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.aggregate-page__table .el-table {
	width: 100%;
}

.aggregate-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.aggregate-page__table .el-table td,
.aggregate-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.aggregate-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.aggregate-page__filter-actions .el-button--primary:not(.is-plain),
.aggregate-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.aggregate-page__filter-actions .el-button--primary:not(.is-plain):hover,
.aggregate-page__filter-actions .el-button--primary:not(.is-plain):focus,
.aggregate-page__table .el-button--primary:not(.is-plain):hover,
.aggregate-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.aggregate-page__filter-control,
	.aggregate-page__filter-cascader {
		width: 100%;
		min-width: 140px;
	}

	.aggregate-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
