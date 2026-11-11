<template>
	<div class="shopadmin-page">
		<!-- hero -->
		<div class="nf-page-hero shopadmin-page__hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">商品管理</h2>
			<p class="nf-page-hero__desc">管理自营商品信息，支持按标题、分类与状态筛选，可进行上下架、修改与评价查看</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card shopadmin-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="shopadmin-page__filter-body">
				<div class="shopadmin-page__filter-item">
					<label class="shopadmin-page__filter-label">商品标题</label>
					<el-input class="shopadmin-page__filter-control shopadmin-page__filter-control--title"
						@keydown.enter.native="select" clearable placeholder="请输入商品标题" v-model="title"></el-input>
				</div>
				<div class="shopadmin-page__filter-item">
					<label class="shopadmin-page__filter-label">一级分类</label>
					<el-select v-model="typeId" class="shopadmin-page__filter-control" placeholder="请选择一级分类"
						@change="handleChange1">
						<el-option v-for="(item,index) in classDatas" :key="item.index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="shopadmin-page__filter-item">
					<label class="shopadmin-page__filter-label">二级分类</label>
					<el-select v-model="twoTypeId" class="shopadmin-page__filter-control" placeholder="请选择二级分类"
						@change="handleChange2" :disabled="typeId==''">
						<el-option v-for="(item,index) in twoTypeDatas" :key="item.index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="shopadmin-page__filter-item">
					<label class="shopadmin-page__filter-label">三级分类</label>
					<el-select v-model="threeTypeId" class="shopadmin-page__filter-control" placeholder="请选择三级分类"
						@change="select()" :disabled="twoTypeId==''">
						<el-option v-for="(item,index) in threeTypeDatas" :key="item.index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="shopadmin-page__filter-item">
					<label class="shopadmin-page__filter-label">商品状态</label>
					<el-select v-model="status" class="shopadmin-page__filter-control shopadmin-page__filter-control--status"
						placeholder="请选择商品状态" @change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="shopadmin-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary" icon="document"
						@click="addShop">发布商品</el-button>
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card shopadmin-page__table">
		<el-table v-loading="tableDataLoading" :data="tableData.content">
			<el-table-column fixed prop="id" label="编号" width="80">
			</el-table-column>
			<el-table-column fixed prop="coverImg" label="商品图片">
				<template slot-scope="scope">
					<img :src="scope.row.coverImg" alt="" width="60" height="60">
				</template>
			</el-table-column>
			<el-table-column prop="type" label="一级分类">
				<template slot-scope="scope">
					<span v-if="scope.row.type == null ">无数据</span>
					<span v-else>{{scope.row.type.name}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="twoTypeName" label="二级分类">
			</el-table-column>
			<el-table-column prop="threeTypeName" label="三级分类">
			</el-table-column>
			<el-table-column prop="title" label="商品标题" width="200">
			</el-table-column>
			<el-table-column prop="label" label="热爆标签">
			</el-table-column>
			<el-table-column prop="originalPrice" label="商品原价">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.originalPrice | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="price" label="商品现价">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="memberPrice" label="会员价格">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.memberPrice | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="dailyRent" label="日租金额">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.dailyRent | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="memberRent" label="会员日租金">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.memberRent | numFilter}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="overdueRent" label="逾期日租金">
				<template slot-scope="scope">
					<span style="color: #f56c6c;">{{scope.row.overdueRent?scope.row.overdueRent:0}}</span>
				</template>
			</el-table-column>
			<el-table-column prop="depositMoney" label="商品押金">
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
			<el-table-column label="是否是热爆" width="120">
				<template slot-scope="scope">
					<span v-if="scope.row.isRecommend== 0">否</span>
					<span v-if="scope.row.isRecommend == 1">是</span>
				</template>
			</el-table-column>
			<el-table-column label="是否需要发货" width="150">
				<template slot-scope="scope">
					<div  v-for="(item,index) in scope.row.isExpress.split(',')" :key="index" style="display: inline-block;">
						<span v-if="item == '1'">包邮 </span>
						<span v-if="item == '2'">,邮寄 </span>
						<span v-if="item == '3'">,自取 </span>
					</div>
					<!-- <span v-if="scope.row.isExpress == 1">包邮</span>
					<span v-if="scope.row.isExpress== 2">邮寄</span>
					<span v-if="scope.row.isExpress == 3">自取</span> -->
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
			<el-table-column prop="status" label="商品状态" fixed="right">
				<template slot-scope="scope">
					<span class="shopadmin-page__status-link" v-if="scope.row.status === 1 ">上架</span>
					<span class="shopadmin-page__status-link" v-if="scope.row.status === 2 ">下架</span>
				</template>
			</el-table-column>

			<el-table-column fixed="right" label="操作" width="180">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="discuss(scope.$index, scope.row)">商品评价
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('shopAdmin:update')"
						@click="shopUpdates(scope.$index, scope.row)">修改
					</el-button>
					<!-- <el-button
	  		        size="mini"
	  		        type="danger"
                :disabled="!isAuth('shopAdmin:delete')"
	  		        @click="shopdeletes(scope.row)">删除
	  		      </el-button> -->
				</template>
			</el-table-column>
		</el-table>
			<div class="shopadmin-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				pagelist: [],
				openValue: 1,
				closeValue: 2,
				id: '',
				img: '',
				title: '',
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
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				typeDatas: [],
				advertData: [],
				tableData: [],
				twoTypeDatas: [],
				threeTypeDatas: [],
				typeId: '',
				twoTypeId: '',
				threeTypeId: '',
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
			//搜索
			select() {
				this.page = 1

				this.shopSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.title = ''
				this.status = 0
				this.typeId = ''
				this.twoTypeId = ''
				this.threeTypeId = ''
				this.value = []
				this.shopSelect()
			},
			//发布商品
			addShop() {
				this.$router.push({
					path: '/shopPublish'
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
						url: this.$http.adornUrl2('/goods/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.id
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
						'threeTypeId':this.threeTypeId,
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
				this.$http({
					url: this.$http.adornUrl2('/goodsType/result'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.classDatas = returnData
				})
			},
			handleChange1(value) {
				for (var i in this.classDatas) {
					if (this.typeId == this.classDatas[i].value) {
						this.twoTypeDatas = this.classDatas[i].children
					}
				}
			
				this.twoTypeId = ''
				this.threeTypeId = ''
				// this.page = 1
				// this.shopSelect()
			},
			handleChange2() {
				for (var i in this.twoTypeDatas) {
					if (this.twoTypeId == this.twoTypeDatas[i].value) {
						this.threeTypeDatas = this.twoTypeDatas[i].children
					}
				}
				this.threeTypeId = ''
				// this.page = 1
				// this.shopSelect()
			},
		},
		mounted() {
			this.title = this.$route.query.title
			console.log('标题：', this.title)
			this.shopSelect()
			this.dataSelect()
		},
		watch: {
			'$route': 'title',
			'$route': 'shopSelect'
		}
	};
</script>

<style scoped>
.shopadmin-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.shopadmin-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.shopadmin-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.shopadmin-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.shopadmin-page__filter-control {
	width: 180px;
}

.shopadmin-page__filter-control--title {
	width: 150px;
}

.shopadmin-page__filter-control--status {
	width: 150px;
}

.shopadmin-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.shopadmin-page__table .el-table {
	width: 100%;
}

.shopadmin-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.shopadmin-page__table .el-table td,
.shopadmin-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.shopadmin-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.shopadmin-page__status-link {
	color: var(--nf-brand, #15cad2);
	cursor: pointer;
}

.shopadmin-page__status-link:hover {
	color: var(--nf-brand-deep, #109da4);
}

.shopadmin-page__filter-actions .el-button--primary:not(.is-plain),
.shopadmin-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.shopadmin-page__filter-actions .el-button--primary:not(.is-plain):hover,
.shopadmin-page__filter-actions .el-button--primary:not(.is-plain):focus,
.shopadmin-page__table .el-button--primary:not(.is-plain):hover,
.shopadmin-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.shopadmin-page__filter-control,
	.shopadmin-page__filter-control--title,
	.shopadmin-page__filter-control--status {
		width: 100%;
		min-width: 140px;
	}

	.shopadmin-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>