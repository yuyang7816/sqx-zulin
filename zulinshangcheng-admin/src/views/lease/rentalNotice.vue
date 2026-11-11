<template>
	<div class="rental-notice-page">
		<!-- hero -->
		<div class="nf-page-hero rental-notice-page__hero">
			<span class="nf-page-hero__badge">租赁管理</span>
			<h2 class="nf-page-hero__title">租赁须知</h2>
			<p class="nf-page-hero__desc">管理租赁须知内容，支持新增、修改与删除须知标题及富文本详情</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card rental-notice-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>快捷操作</span>
			</div>
			<div class="rental-notice-page__filter-body">
				<div class="rental-notice-page__filter-actions">
					<el-button :disabled="!isAuth('rentalNotice:add')" size="mini" type="primary"
						icon="document" @click="addCoupon">添加</el-button>
					<el-button size="mini" type="primary" icon="document"
						@click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card rental-notice-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="标题" width="300">
				</el-table-column>
				<el-table-column prop="content" label="内容">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('rentalNotice:update')"
							@click="shopUpdates(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('rentalNotice:delete')"
							@click="shopdeletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="rental-notice-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>
		</div>
		<!-- 添加-->
		<el-dialog title="添加" :visible.sync="dialogFormVisible" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
				<el-input style="width:50%;" v-model="title" placeholder="请输入标题"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">内容：</span>
				<!-- <el-input style="width:50%;" v-model="content" type="textarea" :rows="4"
					placeholder="请输入内容"></el-input> -->
				<quill-editor ref="myTextEditor" v-model="content" :options="quillOption"
					style="padding-bottom: 50px;height: 300px;width: 50%;display: inline-table;margin-bottom: 60px;">
				</quill-editor>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="couponNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center width="80%">
			<el-form :model="form">
				<el-form-item label="标题：" :label-width="formLabelWidth">
					<el-input v-model="form.title" style="width:65%;" placeholder="请输入标题"></el-input>
				</el-form-item>
				<el-form-item label="内容：" :label-width="formLabelWidth">
					<!-- <el-input v-model="form.content" style="width:65%;" placeholder="请输入内容"></el-input> -->
					<quill-editor ref="myTextEditor" v-model="form.content" :options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 65%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
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
	import {
		quillEditor
	} from 'vue-quill-editor'
	import {
		Quill
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	export default {
		name: 'Tinymce',
		components: {
			quillEditor
		},
		data() {
			return {
				limit: 10,
				page: 1,
				title: '',
				content: '',
				status: 1,
				form: {
					id: '',
					title: '',
					content: '',
					createTime: ''
				},
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: {},
				quillOption: quillConfig,
			}
		},

		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			// 奖品
			compile(index, rows) {
				this.$router.push({
					path: '/lotteryPrize',
					query: {
						prizeRuleId: rows.prizeRuleId
					}
				});
			},
			// 添加
			addCoupon() {
				this.title = ''
				this.content = ''
				this.dialogFormVisible = true
			},
			// 添加规则
			couponNoticeTo() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入抽奖名称',
						type: 'warning'
					});
					return
				}

				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/admin/rentalGuidelines/addRentalGuidelines'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'title': this.title,
						'content': this.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisible = false
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 修改规则
			shopUpdates(row) {
				this.dialogFormVisible1 = true
				this.form.title = row.title
				this.form.content = row.content
				this.form.id = row.id
				this.form.createTime = row.createTime
			},
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/admin/rentalGuidelines/addRentalGuidelines'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'id': this.form.id,
						'title': this.form.title,
						'content': this.form.content,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
						this.dialogFormVisible1 = false
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			// 删除
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/admin/rentalGuidelines/deleteRentalGuidelines'),
						method: 'post',
						params: this.$http.adornParams({
							'id': row.id
						})
					}).then(({
						data
					}) => {
						if (data && data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			//获取数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/rentalGuidelines/getRentalGuidelinesList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'title': '',
						'content': '',
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
.rental-notice-page {
	padding: 12px 16px;
}

.rental-notice-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.rental-notice-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.rental-notice-page__table .el-table {
	width: 100%;
}

.rental-notice-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.rental-notice-page__table .el-table td,
.rental-notice-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.rental-notice-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.rental-notice-page__filter-actions .el-button--primary:not(.is-plain),
.rental-notice-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.rental-notice-page__filter-actions .el-button--primary:not(.is-plain):hover,
.rental-notice-page__filter-actions .el-button--primary:not(.is-plain):focus,
.rental-notice-page__table .el-button--primary:not(.is-plain):hover,
.rental-notice-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.rental-notice-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
