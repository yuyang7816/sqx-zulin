<template>
	<div class="accessory-page">
		<!-- hero -->
		<div class="nf-page-hero accessory-page__hero">
			<span class="nf-page-hero__badge">租赁管理</span>
			<h2 class="nf-page-hero__title">商品配件</h2>
			<p class="nf-page-hero__desc">管理商品配件信息，支持按商品类型与配件名称筛选、新增与维护</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card accessory-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="accessory-page__filter-body">
				<div class="accessory-page__filter-item">
					<label class="accessory-page__filter-label">商品类型</label>
					<el-select v-model="typeIdT" filterable placeholder="请选择商品类型" class="accessory-page__filter-control" @change="select()">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="accessory-page__filter-item">
					<label class="accessory-page__filter-label">配件名称</label>
					<el-input class="accessory-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入配件名称"
						v-model="title"></el-input>
				</div>
				<div class="accessory-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="clear">重置</el-button>
					<el-button :disabled="!isAuth('accessory:add')" size="mini" type="primary" icon="document" @click="addNotice">添加</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card accessory-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="typeName" label="商品类型">
				</el-table-column>
				<el-table-column prop="accessoryName" label="配件名称">
				</el-table-column>
				<el-table-column prop="accessoryNum" label="配件数量">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('accessory:update')"
							@click="updates(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('accessory:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="accessory-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>
		</div>

		<!-- 添加 -->
		<el-dialog title="添加" customClass="customWidth" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">商品类型：</span>
				<el-select v-model="typeId" filterable placeholder="请选择商品类型" style="width:50%;">
					<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">配件名称：</span>
				<el-input style="width:50%;" v-model="accessoryName" placeholder="请输入配件名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;">配件数量：</span>
				<el-input style="width:50%;"  v-model="accessoryNum" placeholder="请输入配件数量"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="specificationTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="商品类型：" :label-width="formLabelWidth">
					<el-select v-model="form.typeId" filterable placeholder="请选择商品类型" style="width:65%;">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="配件名称：" :label-width="formLabelWidth">
					<el-input v-model="form.accessoryName" placeholder="请输入配件名称" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="配件数量：" :label-width="formLabelWidth">
					<el-input v-model="form.accessoryNum" placeholder="请输入配件数量"
						style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				typeIdT:'',
				typeId:'',
				accessoryNameT:'',
				id: '',
				typeId:'',
				accessoryName: '',
				accessoryNum: '',
				form: {
					id: '',
					accessoryName: '',
					accessoryNum: '',
					createTime: '',
					typeId:'',
				},
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				tableData: {},
				statusmain:[],
			}
		},
		methods: {
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			// 添加品牌弹框
			addNotice() {
				this.typeId = ''
				this.accessoryName = ''
				this.accessoryNum = ''
				this.dialogFormVisible = true
			},
			// 添加品牌
			specificationTo() {
				if (this.accessoryName == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入配件名称',
						type: 'warning'
					});
					return
				}
				if (this.accessoryNum == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入配件数量',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/admin/goodsAccessory/addGoodsAccessory'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'id': '',
						'accessoryName': this.accessoryName,
						'accessoryNum': this.accessoryNum,
						'typeId': this.typeId,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
					
				})
			},
			// 修改品牌弹框
			updates(rows) {
				
				this.form.id = rows.id;
				this.form.accessoryName = rows.accessoryName;
				this.form.accessoryNum = rows.accessoryNum;
				this.form.typeId = rows.typeId;
				this.dialogFormVisible1 = true;
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/admin/goodsAccessory/addGoodsAccessory'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'id': this.form.id,
						'accessoryName': this.form.accessoryName,
						'accessoryNum': this.form.accessoryNum,
						'typeId': this.form.typeId,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
							}
						})
					}
					
				})
			},
			// 删除
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/admin/goodsAccessory/deleteGoodsAccessory?id=${row.id}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}else{
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 获取地址数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/goodsAccessory/selectGoodsAccessoryList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'typeId':this.typeIdT,
						'accessoryName':this.accessoryNameT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			select(){
				this.page = 1
				this.dataSelect()
			},
			clear(){
				this.page = 1
				this.typeIdT = ''
				this.accessoryNameT = ''
				this.dataSelect()
			},
			// 商品分类
			dataSelectFl() {
				this.$http({
					url: this.$http.adornUrl2('/goodsType/result'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.statusmain = returnData
				})
			}
		},
		mounted() {
			this.dataSelect()
			this.dataSelectFl()
		}
	};
</script>

<style scoped>
.accessory-page {
	padding: 12px 16px;
}

.accessory-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.accessory-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.accessory-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.accessory-page__filter-control {
	width: 180px;
}

.accessory-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.accessory-page__table .el-table {
	width: 100%;
}

.accessory-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.accessory-page__table .el-table td,
.accessory-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.accessory-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

/* 列表页 primary 描边钮（非 plain 亦透明底） */
.accessory-page__filter-actions .el-button--primary:not(.is-plain),
.accessory-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.accessory-page__filter-actions .el-button--primary:not(.is-plain):hover,
.accessory-page__filter-actions .el-button--primary:not(.is-plain):focus,
.accessory-page__table .el-button--primary:not(.is-plain):hover,
.accessory-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.accessory-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.accessory-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
