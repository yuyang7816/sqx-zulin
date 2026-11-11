<template>
	<div class="comment-page">
		<!-- hero -->
		<div class="nf-page-hero comment-page__hero">
			<span class="nf-page-hero__badge">商品管理</span>
			<h2 class="nf-page-hero__title">评价管理</h2>
			<p class="nf-page-hero__desc">查看与管理商品用户评价，支持按商品标题、用户昵称、手机号与评论内容筛选，可进行回复与删除操作</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card comment-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="comment-page__filter-body">
				<div class="comment-page__filter-item">
					<label class="comment-page__filter-label">商品标题</label>
					<el-input class="comment-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
						v-model="title"></el-input>
				</div>
				<div class="comment-page__filter-item">
					<label class="comment-page__filter-label">用户昵称</label>
					<el-input class="comment-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>
				<div class="comment-page__filter-item">
					<label class="comment-page__filter-label">手机号</label>
					<el-input class="comment-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>
				<div class="comment-page__filter-item">
					<label class="comment-page__filter-label">评论内容/关键字</label>
					<el-input class="comment-page__filter-control comment-page__filter-control--wide" @keydown.enter.native="select" clearable placeholder="请输入评论内容/关键字"
						v-model="content"></el-input>
				</div>
				<div class="comment-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card comment-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column prop="commentId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户昵称">
					<template slot-scope="scope">
						<span style="color: #15cad2;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userHeader" label="头像">
					<template slot-scope="scope">
						<img :src="scope.row.userHeader" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="sku" label="商品标题" width="180">
					<template slot-scope="scope">
						<span style="color: #B94A48;cursor: pointer;"
							@click="toGoods(scope.row.goods.title)">{{scope.row.goods.title}}</span>
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
						<el-button size="mini" type="danger" :disabled="!isAuth('comment:delete')"
							@click="deletes(scope.row)">删除
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('comment:huifu')"
							@click="replyClick(scope.row)">回复
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="comment-page__pagination">
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
				content:'',
				title:'',
				userName:'',
				phone:'',
				
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
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans(){
				this.title = ''
				this.userName = ''
				this.phone = ''
				this.content = ''
				this.page = 1
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
						'userName': this.userName,
						'title': this.title,
						'content': this.content,
						'phone': this.phone
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
			// 商品跳转
			toGoods(title){
				this.$router.push({
					path: '/shopAdmin1',
					query: {
						title: title
					}
				})
			}
		},
		activated() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.comment-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.comment-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.comment-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.comment-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.comment-page__filter-control {
	width: 180px;
}

.comment-page__filter-control--wide {
	width: 220px;
}

.comment-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.comment-page__table .el-table {
	width: 100%;
}

.comment-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.comment-page__table .el-table td,
.comment-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.comment-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.comment-page__filter-actions .el-button--primary:not(.is-plain),
.comment-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.comment-page__filter-actions .el-button--primary:not(.is-plain):hover,
.comment-page__filter-actions .el-button--primary:not(.is-plain):focus,
.comment-page__table .el-button--primary:not(.is-plain):hover,
.comment-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.comment-page__filter-control,
	.comment-page__filter-control--wide {
		width: 100%;
		min-width: 140px;
	}

	.comment-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
