<template>
	<div class="materials-page">
		<!-- hero -->
		<div class="nf-page-hero materials-page__hero">
			<span class="nf-page-hero__badge">内容管理</span>
			<h2 class="nf-page-hero__title">文章管理</h2>
			<p class="nf-page-hero__desc">管理帮助文章分类与文章内容，支持分类增删改与文章发布、编辑与删除</p>
		</div>

		<!-- 筛选/操作白卡 -->
		<div class="nf-filter-card materials-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>{{ activeName === 'first' ? '分类操作' : '筛选条件' }}</span>
			</div>
			<div class="materials-page__filter-body">
				<div v-show="activeName === 'first'" class="materials-page__filter-actions">
					<el-button :disabled="!isAuth('materialsList:add')" size="mini" type="primary"
						icon="document" @click="addNotice">添加分类</el-button>
				</div>
				<template v-if="activeName === 'second'">
					<div class="materials-page__filter-item">
						<label class="materials-page__filter-label">分类</label>
						<el-select v-model="classifyId" placeholder="请选择类型" class="materials-page__filter-control"
							@change="articleSelect(classifyId)">
							<el-option v-for="(item,index) in classifyIds" :key="item.index" :label="item.helpClassifyName"
								:value="item.helpClassifyId">
							</el-option>
						</el-select>
					</div>
					<div class="materials-page__filter-actions">
						<el-button size="mini" type="primary" plain icon="document" @click="articleSelect">刷新</el-button>
						<el-button :disabled="!isAuth('materialsList:add')" size="mini" type="primary" icon="document"
							@click="addNoticewz">添加文章</el-button>
					</div>
				</template>
			</div>
		</div>

		<!-- 表格白卡 + tabs -->
		<div class="nf-table-card materials-page__table">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="materials-page__tabs">
				<el-tab-pane label="分类管理" name="first">
					<el-table v-loading="tableDataLoading" :data="tableDataFl">
						<el-table-column prop="helpClassifyId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="helpClassifyName" label="分类名称">
						</el-table-column>
						<el-table-column prop="sort" label="排序">
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间">
						</el-table-column>
						<el-table-column label="操作" width="150">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
									@click="updates(scope.$index, scope.row)">编辑
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
									@click="deletes(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
				</el-tab-pane>
				<el-tab-pane label="文章管理" name="second">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="helpWordId" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="helpWordTitle" label="标题" width="120">
						</el-table-column>
						<el-table-column prop="helpWordContent" label="内容">
							<template slot-scope="scope">
								<div class="materials-page__content-clamp">
									{{scope.row.helpWordContent}}
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="sort" label="排序" width="80">
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="160">
						</el-table-column>
						<el-table-column label="操作" width="160">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
									@click="updateswz(scope.$index, scope.row)">编辑
								</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
									@click="deleteswz(scope.row)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="materials-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 添加分类 -->
		<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input style="width:50%;" v-model="classifyName" placeholder="请输入分类名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width: 50%;" v-model="remark" placeholder="请输入排序"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改分类 -->
		<el-dialog title="修改分类" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="分类名称：" :label-width="formLabelWidth">
					<el-input v-model="form.classifyName" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="排序：" :label-width="formLabelWidth">
					<el-input v-model="form.remark" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加文章 -->
		<el-dialog title="添加文章" :visible.sync="dialogFormVisible2" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">文章标题：</span>
				<el-input style="width: 50%;" v-model="title" placeholder="请输入文章标题"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width: 50%;" v-model="remark" placeholder="请输入排序"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">文章内容：</span>
				<quill-editor ref="myTextEditor" v-model="content" :options="quillOption"
					style="height: 400px;display: inline-table;margin-bottom: 60px;margin-left: 200px">
				</quill-editor>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTowz()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改文章 -->
		<el-dialog title="修改文章" :visible.sync="dialogFormVisible3" center width="80%">
			<el-form :model="form">
				<el-form-item label="文章标题：" :label-width="formLabelWidth">
					<el-input v-model="formwz.title" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="排序：" :label-width="formLabelWidth">
					<el-input v-model="formwz.remark" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="文章内容：" :label-width="formLabelWidth">
					<quill-editor ref="myTextEditor" v-model="formwz.content" :options="quillOption"
						style="height: 400px;width: 80%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTowz()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				limit: 10,
				page: 1,
				state: 1,
				sort: 0,
				remark: '',
				createTime: '',
				classifyName: '',
				id: '',
				classifyId: '',
				classifyIds: [],
				form: {
					id: '',
					state: '',
					sort: '',
					classifyName: '',
					remark: '',
					createTime: ''
				},
				formwz: {
					id: '',
					picture: '',
					articleUrl: '',
					content: '',
					title: '',
					classifyId: '',
					createTime: '',
					remark: '',
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				picture: '',
				picture2: '',
				articleUrl: '',
				content: '',
				title: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: [],
				tableDataFl:[],
				quillOption: quillConfig,
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			handleSizeChange(val) {
				this.limit = val;
				this.articleSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.articleSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '分类管理') {
					this.dataSelect()
				}
				if (tab._props.label == '文章管理') {
					this.classifyId = this.classifyIds[0].helpClassifyId
					this.dataSelect()
					this.articleSelect()
				}
			},
			handleAvatarSuccess(file) {
				this.picture = file.data;
			},
			handleAvatarSuccess2(file2) {
				this.formwz.picture = file2.data;
			},
			// 添加分类弹框
			addNotice() {
				this.dialogFormVisible = true
			},
			// 添加分类
			releasNoticeTo() {
				if (this.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					});
					return
				}
				if (this.remark == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/help/insertHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'sort': this.remark,
						'helpClassifyName': this.classifyName,
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
							this.dataSelect()
						}
					})
				})
			},
			// 编辑分类弹框
			updates(index, rows) {
				this.dialogFormVisible1 = true;
				this.form.id = rows.helpClassifyId;
				this.form.classifyName = rows.helpClassifyName;
				this.form.remark = rows.sort;
				this.form.createTime = rows.createTime;
			},
			// 修改商品类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/help/updateHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'helpClassifyId': this.form.id,
						'helpClassifyName': this.form.classifyName,
						'sort': this.form.remark,
						'createTime': this.info.stockDate,
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
			// 删除分类
			deletes(row) {
				this.id = row.helpClassifyId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(
							`/help/deleteHelpClassify?helpClassifyId=${this.id}`),
						method: 'post',
						data: this.$http.adornData({})
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
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				}).catch(() => {})
			},
			// 添加文章弹框
			addNoticewz() {
				this.title = ''
				this.articleUrl = ''
				this.content = ''
				this.picture = ''
				this.dialogFormVisible2 = true
			},
			// 添加文章
			releasNoticeTowz() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入文章标题',
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
					url: this.$http.adornUrl2('/help/insertHelpWord'),
					method: 'post',
					data: this.$http.adornData({
						'helpClassifyId': this.classifyId,
						'helpWordContent': this.content,
						'sort': this.remark,
						'helpWordTitle': this.title,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.articleSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 编辑文章弹框
			updateswz(index, rows) {
				this.formwz.id = rows.helpWordId;
				this.formwz.classifyId = rows.helpClassifyId
				this.formwz.content = rows.helpWordContent;
				this.formwz.title = rows.helpWordTitle;
				this.formwz.remark = rows.sort;
				this.dialogFormVisible3 = true;
			},
			// 修改商品类别
			amendNoticeTowz() {
				this.$http({
					url: this.$http.adornUrl2('/help/updateHelpWord'),
					method: 'post',
					data: this.$http.adornData({
						'helpWordId': this.formwz.id,
						'helpWordContent': this.formwz.content,
						'helpClassifyId': this.formwz.classifyId,
						'helpWordTitle': this.formwz.title,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible3 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.articleSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'success',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 删除文章
			deleteswz(row) {
				this.id = row.helpWordId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/help/deleteHelpWord?helpWordId=${this.id}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.articleSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'success',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				}).catch(() => {})
			},
			// 获取分类管理数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/help/selectHelpClassifyList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataFl = returnData
					this.classifyIds = returnData
					this.classifyId = this.classifyIds[0].helpClassifyId
				})
			},
			// 获取文章管理数据
			articleSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/help/selectHelpWordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'helpClassifyId': this.classifyId,
						'helpWordTitle': '',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.materials-page {
	padding: 12px 16px;
}

.materials-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.materials-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.materials-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.materials-page__filter-control {
	width: 200px;
}

.materials-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.materials-page__tabs {
	margin-bottom: 0;
}

.materials-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.materials-page__table .el-table {
	width: 100%;
}

.materials-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.materials-page__table .el-table td,
.materials-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.materials-page__content-clamp {
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 3;
	overflow: hidden;
	max-height: 80px;
}

.materials-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.materials-page__filter-actions .el-button--primary:not(.is-plain),
.materials-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.materials-page__filter-actions .el-button--primary:not(.is-plain):hover,
.materials-page__filter-actions .el-button--primary:not(.is-plain):focus,
.materials-page__table .el-button--primary:not(.is-plain):hover,
.materials-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}
</style>
