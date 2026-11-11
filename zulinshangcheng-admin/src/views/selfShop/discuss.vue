<template>
	<div class="discuss-page">
		<!-- hero -->
		<div class="nf-page-hero discuss-page__hero">
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">商品评论</h2>
			<p class="nf-page-hero__desc">查看与管理商品用户评价，支持按评价状态筛选，进行回复与删除操作</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card discuss-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="discuss-page__filter-body">
				<div class="discuss-page__filter-item">
					<label class="discuss-page__filter-label">评价状态</label>
					<el-select v-model="scoreType" placeholder="请选择评价状态" class="discuss-page__filter-control" @change="select(scoreType)">
						<el-option v-for="item in scoreTypes" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="discuss-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="prev">返回上一页</el-button>
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card discuss-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column prop="commentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="昵称">
					<template slot-scope="scope">
						<span class="discuss-page__link"
							@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userHeader" label="头像">
					<template slot-scope="scope">
						<img :src="scope.row.userHeader" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="content" label="评论内容" width="180">
				</el-table-column>
				<el-table-column label="评论图片" width="150">
					<template slot-scope="scope">
						<div v-if="scope.row.img == null || scope.row.img == ''">
							暂无图片
						</div>
						<div v-else-if="scope.row.img.includes(',')" style="display:flex;flex-wrap: wrap;">
							<div v-for="item in scope.row.img.split(',')" style="margin: 2px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 200px; height: 200px" :src="item" alt="">
								</el-popover>
							</div>
						</div>
						<div v-else>
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.img" alt="" slot="reference">
								<img style="width: 200px; height: 200px" :src="scope.row.img" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="score" label="评分">
					<template slot-scope="scope">
						<span>{{scope.row.score?scope.row.score:'暂无评分'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="score" label="评价状态">
					<template slot-scope="scope">
						<span v-if="scope.row.scoreType==1">好评</span>
						<span v-if="scope.row.scoreType==2">中评</span>
						<span v-if="scope.row.scoreType==3">差评</span>
					</template>
				</el-table-column>

				<el-table-column prop="sku" label="商品规格" width="180">
					<template slot-scope="scope">
						<span v-if="scope.row.sku==''">无</span>
						<span v-else>{{scope.row.sku}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="reply" label="回复内容" width="180">
					<template slot-scope="scope">
						<span>{{scope.row.reply?scope.row.reply:'未回复'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="180">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('brandShop:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('brandShop:delete')"
							@click="replyClick(scope.row)">回复
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="discuss-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</div>

		<!-- 回复 -->
		<el-dialog title="回复" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="回复内容：" :label-width="formLabelWidth">
					<el-input v-model="form.reply" type="textarea" :rows="4" style="width:65%;" placeholder="请输入回复内容">
					</el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="replyNoticeTo()">确 定</el-button>
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
				goodsId: '',
				form: {
					commentId: '',
					reply: ''
				},
				scoreType: 0,
				scoreTypes: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '好评'
					},
					{
						value: 2,
						label: '中评'
					},
					{
						value: 3,
						label: '差评'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			handleSizeChange(val) {
				this.size = val;
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
			//搜索
			select() {
				this.dataSelect()
			},
			// 回复
			replyClick(row) {
				this.dialogFormVisible = true
				this.form.commentId = row.commentId
				this.form.reply = row.reply
			},
			replyNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsComment/reply'),
					method: 'get',
					params: this.$http.adornParams({
						'commentId': this.form.commentId,
						'reply': this.form.reply,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '回复成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
					this.dialogFormVisible = false
				})
			},
			// 删除评论
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/selfGoodsComment/delete?id=${row.commentId}`),
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
				let goodsId = this.$route.query.goodsId
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsComment/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'goodsId': goodsId,
						'scoreType': this.scoreType
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 详情跳转
			updates(row) {
			  this.$router.push({
			    path: '/userDetail',
			    query: {
			      userId: row.userId
			    }
			  })
			},
		},
		activated() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.discuss-page {
	padding: 12px 16px;
}

.discuss-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.discuss-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.discuss-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.discuss-page__filter-control {
	width: 180px;
}

.discuss-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.discuss-page__table .el-table {
	width: 100%;
}

.discuss-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.discuss-page__table .el-table td,
.discuss-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.discuss-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.discuss-page__link {
	color: var(--nf-brand, #15cad2);
	cursor: pointer;
}

.discuss-page__link:hover {
	color: var(--nf-brand-deep, #109da4);
}

/* 列表页 primary 描边钮（非 plain 亦透明底） */
.discuss-page__filter-actions .el-button--primary:not(.is-plain),
.discuss-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.discuss-page__filter-actions .el-button--primary:not(.is-plain):hover,
.discuss-page__filter-actions .el-button--primary:not(.is-plain):focus,
.discuss-page__table .el-button--primary:not(.is-plain):hover,
.discuss-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.discuss-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.discuss-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
