<template>
	<div class="message-page">
		<!-- hero -->
		<div class="nf-page-hero message-page__hero">
			<span class="nf-page-hero__badge">消息管理</span>
			<h2 class="nf-page-hero__title">消息中心</h2>
			<p class="nf-page-hero__desc">查看用户反馈与订单消息，支持切换消息类型浏览与跳转用户详情</p>
		</div>

		<!-- 操作白卡 -->
		<div class="nf-filter-card message-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>快捷操作</span>
			</div>
			<div class="message-page__filter-body">
				<div class="message-page__filter-actions">
					<el-button size="mini" type="primary" plain icon="document" @click="dataSelect">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 + tabs -->
		<div class="nf-table-card message-page__table">
	<el-tabs v-model="activeName" @tab-click="handleClick" class="message-page__tabs">
		<!-- <el-tab-pane label="公告中心" name="first">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:add')" size="mini" type="primary"
					icon="document" @click="addNotice">添加公告</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="标题">
				</el-table-column>
				<el-table-column prop="type" label="类型">
					<template slot-scope="scope">
						<span style="color: #15cad2;cursor: pointer;" v-if="scope.row.type === 'url' ">链接</span>
						<span style="color: #15cad2;cursor: pointer;" v-if="scope.row.type === 'word' ">文本</span>
					</template>
				</el-table-column>
				<el-table-column prop="url" label="链接">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('message:update')"
							@click="updates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('message:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
			<el-dialog title="添加公告" :visible.sync="dialogFormVisible" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
					<el-input style="width:50%;" v-model="title" placeholder="请输入公告标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
					<el-input style="width: 50%;" v-model="url" placeholder="请输入公告链接"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-select v-model="type" placeholder="请选择公告类型" style="width:50%;">
						<el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
				<el-form :model="form">
					<el-form-item label="标题：" :label-width="formLabelWidth">
						<el-input v-model="form.title" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="链接：" :label-width="formLabelWidth">
						<el-input v-model="form.url" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="类型：" :label-width="formLabelWidth">
						<el-select v-model="form.type" placeholder="请选择类型" style="width:65%;">
							<el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane> -->
		<el-tab-pane label="用户反馈" name="second">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="联系方式">
				</el-table-column>
				<el-table-column prop="content" label="内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="120">
				</el-table-column>
			</el-table>
			<div class="message-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="订单消息" name="third">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容" width="280">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="message-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="用户消息" name="fourth">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容" width="280">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</el-tab-pane> -->
		<!-- <el-tab-pane label="公众号消息" name="sixth">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容" width="280">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="消息推送" name="seventh">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="primary"
					icon="document" @click="magNotice">消息推送</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容" width="280">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
			<el-dialog title="消息推送" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择：</span>
					<el-select v-model="flag" placeholder="请选择类型" style="width:50%" @change="selectflag()">
						<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin-bottom: 10px;" v-if="flag == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">用户手机号：</span>
					<el-input style="width: 50%;" v-model="phone" placeholder="请输入用户手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">消息标题：</span>
					<el-input style="width: 50%;" v-model="title" placeholder="请输入消息标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">消息内容：</span>
					<el-input style="width: 50%;" type="textarea" rows="4" v-model="content"
						placeholder="请输入消息内容"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="magNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane> -->
	</el-tabs>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				state: 2,
				url: '',
				title: '',
				type: '',
				id: '',
				content: '',
				flag: '',
				phone: '',
				content: '',
				title: '',
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
				],
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				formLabelWidth: '200px',
				activeName: 'second',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible: false,
				tableData: [],
			}
		},
		methods: {
			// 详情跳转
			updataDetails(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				});
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '公告中心') {
					this.page = 1
					this.size = 10
					this.state = 1
					this.dataSelect()
				}
				if (tab._props.label == '用户反馈') {
					this.page = 1
					this.size = 10
					this.state = 2
					this.dataSelect()
				}
				if (tab._props.label == '订单消息') {
					this.page = 1
					this.size = 10
					this.state = 4
					this.dataSelect()
				}
				if (tab._props.label == '用户消息') {
					this.page = 1
					this.size = 10
					this.state = 5
					this.dataSelect()
				}
				if (tab._props.label == '公众号消息') {
					this.page = 1
					this.size = 10
					this.state = 7
					this.dataSelect()
				}
				if (tab._props.label == '消息推送') {
					this.page = 1
					this.size = 10
					this.state = 8
					this.dataSelect()
				}
			},
			selectflag(val) {
				console.log(val)
			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible3 = true
			},
			// 消息推送
			magNoticeTo() {
				if (this.flag == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					});
					return
				}
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
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
				if (this.flag == 1) {
					if (this.phone == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入用户手机号',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl2('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'phone': this.phone,
							'flag': this.flag
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible3 = false
						if (data.status == -100) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
						if (data.status == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
				if (this.flag == 2) {
					this.$http({
						url: this.$http.adornUrl2('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'flag': this.flag,
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible3 = false
						if (data.status == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
			},
			// 添加公告弹框
			addNotice() {
				this.dialogFormVisible = true
			},
			// 添加公告
			releasNoticeTo() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告标题',
						type: 'warning'
					});
					return
				}
				if (this.url == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告链接',
						type: 'warning'
					});
					return
				}
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告类型',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/message/add'),
					method: 'post',
					data: this.$http.adornData({
						'state': this.state,
						'url': this.url,
						'title': this.title,
						'type': this.type
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
			// 修改弹框
			updates(index, rows) {
				this.form.state = rows.state;
				this.dialogFormVisible1 = true;
				this.form.id = rows.id;
				this.form.title = rows.title;
				this.form.url = rows.url;
				this.form.type = rows.type;
			},
			// 修改商品类别
			amendNoticeTo() {
				if (this.form.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告标题',
						type: 'warning'
					});
					return
				}
				if (this.form.url == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告链接',
						type: 'warning'
					});
					return
				}
				if (this.form.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择公告类型',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/message/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'state': this.form.state,
						'title': this.form.title,
						'url': this.form.url,
						'type': this.form.type,
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
			// 删除公告
			deletes(row) {
				let delid = row.id
				this.id = delid
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/message/delete/${this.id}`),
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
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(`/message/page/${this.state}/${this.page-1}/${this.size}`),
					method: 'get',
					params: this.$http.adornParams({})
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
.message-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.message-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.message-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.message-page__tabs {
	margin-bottom: 0;
}

.message-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.message-page__table .el-table {
	width: 100%;
}

.message-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.message-page__table .el-table td,
.message-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.message-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.message-page__filter-actions .el-button--primary:not(.is-plain),
.message-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.message-page__filter-actions .el-button--primary:not(.is-plain):hover,
.message-page__filter-actions .el-button--primary:not(.is-plain):focus,
.message-page__table .el-button--primary:not(.is-plain):hover,
.message-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}
</style>