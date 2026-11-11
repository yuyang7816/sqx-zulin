<template>
	<div class="label-shop-page">
		<!-- hero -->
		<div class="nf-page-hero label-shop-page__hero">
			<span class="nf-page-hero__badge">标签管理</span>
			<h2 class="nf-page-hero__title">标签管理</h2>
			<p class="nf-page-hero__desc">维护商品标签名称、排序与启用状态，支持添加、修改与删除标签</p>
		</div>

		<!-- 操作白卡 -->
		<div class="nf-filter-card label-shop-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>快捷操作</span>
			</div>
			<div class="label-shop-page__filter-body">
				<div class="label-shop-page__filter-actions">
					<el-button :disabled="!isAuth('labelShop:add')" size="mini" type="primary"
						icon="document" @click="addNotice()">添加</el-button>
					<el-button size="mini" type="primary" plain icon="document"
						@click="Notice()">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card label-shop-page__table">
		<el-table v-loading="tableDataLoading" :data="tableData.records">
			<el-table-column prop="labelId" label="编号" width="80">
			</el-table-column>
			<el-table-column prop="label" label="标签名称">
			</el-table-column>
			<el-table-column prop="sort" label="排序">
			</el-table-column>
			<el-table-column prop="isEnable" label="是否启用">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.isEnable" @change="change(scope.row)" :active-value="1"
						:inactive-value="0" active-color="#13ce66" inactive-color="#ff4949">
					</el-switch>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="180">
			</el-table-column>
			<el-table-column label="操作" width="150">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('labelShop:update')"
						@click="addNotice(scope.row)">修改
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('labelShop:delete')"
						@click="deletes(scope.row)">删除
					</el-button>
				</template>
			</el-table-column>
		</el-table>
			<div class="label-shop-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>
		</div>
		<!-- 添加品牌 -->
		<el-dialog title="添加" customClass="customWidth" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="label" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" type="number" :min="1" v-model="sort"
					placeholder="请输入排序"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="specificationTo()">确 定</el-button>
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
				labelId: '',
				label: '',
				sort: '',
				isEnable:1,
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				tableData: [],
				titles:'',
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
			addNotice(rows) {
				if(rows){
					this.titles = '修改'
					this.dialogFormVisible = true;
					this.labelId = rows.labelId;
					this.label = rows.label;
					this.sort = rows.sort;
					this.isEnable = rows.isEnable;
				}else{
					this.titles = '添加'
					this.isEnable = 1
					this.label = ''
					this.sort = ''
					this.labelId = ''
					this.dialogFormVisible = true
				}
				
			},
			// 添加品牌
			specificationTo() {
				if (this.label == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				if (this.sort == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/admin/labelType/addLabelType'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'label': this.label,
						'sort': this.sort,
						'labelId':this.labelId,
						'isEnable':this.isEnable,
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
			
			// 删除
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/admin/labelType/deleteLabelType?id=${row.labelId}`),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
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
			Notice(){
				this.page = 1
				this.dataSelect()
			},
			// 获取地址数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/labelType/getLabelTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 上下架
			change(row) {
				this.$http({
					url: this.$http.adornUrl2('/admin/labelType/addLabelType'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'label': row.label,
						'sort': row.sort,
						'labelId':row.labelId,
						'isEnable':row.isEnable,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
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
								this.dataSelect()
							}
						})
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.label-shop-page {
	padding: 12px 16px;
}

.label-shop-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.label-shop-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
}

.label-shop-page__table .el-table {
	width: 100%;
}

.label-shop-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.label-shop-page__table .el-table td,
.label-shop-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.label-shop-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

/* 列表页 primary 描边钮（非 plain 亦透明底） */
.label-shop-page__filter-actions .el-button--primary:not(.is-plain),
.label-shop-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.label-shop-page__filter-actions .el-button--primary:not(.is-plain):hover,
.label-shop-page__filter-actions .el-button--primary:not(.is-plain):focus,
.label-shop-page__table .el-button--primary:not(.is-plain):hover,
.label-shop-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}
</style>
