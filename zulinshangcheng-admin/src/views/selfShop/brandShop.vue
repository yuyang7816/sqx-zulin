<template>
	<div class="brandshop-page">
		<!-- hero -->
		<div class="nf-page-hero brandshop-page__hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">商品品牌</h2>
			<p class="nf-page-hero__desc">管理商品品牌信息，支持新增、修改与删除品牌及关键字配置</p>
		</div>

		<!-- 筛选/操作白卡 -->
		<div class="nf-filter-card brandshop-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-setting"></i>
				<span>快捷操作</span>
			</div>
			<div class="brandshop-page__filter-body">
				<div class="brandshop-page__filter-actions">
					<el-button :disabled="!isAuth('brandShop:add')" size="mini" type="primary"
						icon="document" @click="addNotice">添加品牌</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card brandshop-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column prop="brandId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="brandName" label="品牌名称">
				</el-table-column>
				<el-table-column prop="names" label="品牌关键字">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('brandShop:update')"
							@click="updates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('brandShop:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="brandshop-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</div>

		<!-- 添加品牌 -->
		<el-dialog title="添加品牌" customClass="customWidth" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">品牌名称：</span>
				<el-input style="width:50%;" v-model="brandName" placeholder="请输入品牌名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -75px;">品牌关键字：</span>
				<el-input style="width:50%;" type="textarea" :rows="4" v-model="names"
					placeholder="请输入品牌关键字"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="specificationTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改弹框 -->
		<el-dialog title="修改品牌" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="品牌名称：" :label-width="formLabelWidth">
					<el-input v-model="form.brandName" placeholder="请输入品牌名称" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="品牌关键字：" :label-width="formLabelWidth">
					<el-input v-model="form.names" type="textarea" :rows="4" placeholder="请输入品牌关键字"
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
				brandId: '',
				brandName: '',
				names: '',
				form: {
					brandId: '',
					brandName: '',
					names: '',
					createTime: ''
				},
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				tableData: [],
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
				this.dialogFormVisible = true
			},
			// 添加品牌
			specificationTo() {
				if (this.brandName == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入品牌名称',
						type: 'warning'
					});
					return
				}
				if (this.names == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入品牌关键字',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsBrand/save'),
					method: 'post',
					data: this.$http.adornData({
						'brandName': this.brandName,
						'names': this.names,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.brandName = ''
							this.names = ''
							this.dataSelect()
						}
					})
				})
			},
			// 修改品牌弹框
			updates(index, rows) {
				this.dialogFormVisible1 = true;
				this.form.brandId = rows.brandId;
				this.form.brandName = rows.brandName;
				this.form.names = rows.names;
				this.form.createTime = rows.createTime;
			},
			// 修改品牌
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsBrand/update'),
					method: 'post',
					data: this.$http.adornData({
						'brandId': this.form.brandId,
						'brandName': this.form.brandName,
						'names': this.form.names,
						'createTime': this.form.createTime,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			// 删除品牌
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/selfGoodsBrand/delete?id=${row.brandId}`),
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
						}
					})
				}).catch(() => {})
			},
			// 获取地址数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsBrand/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.brandshop-page {
	padding: 12px 16px;
}

.brandshop-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.brandshop-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.brandshop-page__table .el-table {
	width: 100%;
}

.brandshop-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.brandshop-page__table .el-table td,
.brandshop-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.brandshop-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

/* 列表页 primary 描边钮（非 plain 亦透明底） */
.brandshop-page__filter-actions .el-button--primary:not(.is-plain),
.brandshop-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.brandshop-page__filter-actions .el-button--primary:not(.is-plain):hover,
.brandshop-page__filter-actions .el-button--primary:not(.is-plain):focus,
.brandshop-page__table .el-button--primary:not(.is-plain):hover,
.brandshop-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.brandshop-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
