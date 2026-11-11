<template>
	<div class="virtual-amend-page">
		<!-- hero -->
		<div class="nf-page-hero virtual-amend-page__hero">
			<span class="nf-page-hero__badge">商品管理</span>
			<h2 class="nf-page-hero__title">兑换码列表</h2>
			<p class="nf-page-hero__desc">查看与管理虚拟商品兑换码，支持修改兑换地址与状态，可跳转查看关联订单详情</p>
		</div>

		<!-- 筛选白卡（快捷操作） -->
		<div class="nf-filter-card virtual-amend-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-back"></i>
				<span>快捷操作</span>
			</div>
			<div class="virtual-amend-page__filter-body">
				<div class="virtual-amend-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="prev">返回上一页</el-button>
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card virtual-amend-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="coverImg" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.coverImg" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="content" label="兑换码">
				</el-table-column>
				<el-table-column prop="linkUrl" label="兑换地址" width="200">
				</el-table-column>
				<el-table-column label="订单号" width="180">
					<template slot-scope="scope">
						<span class="virtual-amend-page__link"
							@click="orderDetails(scope.row.orderId)">{{scope.row.orderNum?scope.row.orderNum:'暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column label="支付金额">
					<template slot-scope="scope">
						<span>{{scope.row.payMoney?scope.row.payMoney:'暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column label="支付方式">
					<template slot-scope="scope">
						<span>{{scope.row.payWay==1 ? '微信' : scope.row.payWay==2? '支付宝' : '零钱'}}</span>
					</template>
				</el-table-column>
				<el-table-column label="用户名">
					<template slot-scope="scope">
						<span>{{scope.row.nickName ? scope.row.nickName:'暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column label="电话" width="150">
					<template slot-scope="scope">
						<span>{{scope.row.phone?scope.row.phone:'暂无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态" fixed="right">
					<template slot-scope="scope">
						<span class="virtual-amend-page__status--active" v-if="scope.row.status == 1">正常</span>
						<span v-if="scope.row.status == 2">关闭</span>
						<span v-if="scope.row.status == 3">已使用</span>
					</template>
				</el-table-column>
				<el-table-column prop="expressTime" label="出售时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.expressTime?scope.row.expressTime:'未出售'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="100" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.status!=='3'"
							@click="compile(scope.row)">修改
						</el-button>
						<el-button v-else size="mini" type="primary" disabled>修改
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="virtual-amend-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 15, 20, 25]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</div>

		<!-- 修改兑换码 -->
		<el-dialog title="兑换码修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="兑换地址：" :label-width="formLabelWidth">
					<el-input v-model="form.linkUrl" style="width:65%;" placeholder="请输入兑换地址"></el-input>
				</el-form-item>
				<el-form-item label="兑换码：" :label-width="formLabelWidth">
					<el-input v-model="form.content" style="width:65%;" placeholder="请输入兑换码"></el-input>
				</el-form-item>
				<el-form-item label="状态：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.status">
						<el-radio label="1">正常</el-radio>
						<el-radio label="2">关闭</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="compileNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
        data() {
          return {
          size:10,
          page:1,
          title:'',
          linkUrl:'',
          content:'',
          goodsId:'',
          coverImg:'',
          content:'',
          type:'',
          status:0,
          form:{
            id:'',
            goodsId:'',
            title:'',
            linkUrl:'',
            content:'',
            coverImg:'',
            status:'',
            createTime:'',
          },
          formLabelWidth:'200px',
          tableDataLoading:false,
          dialogFormVisible:false,
          tableData:[],
        }
      },
      methods: {
		// 详情
		orderDetails (id) {
		    this.$router.push({path: '/orderDetails', query: {id: id}})
		},
        // 返回上一级
        prev(){
        	this.$router.back()
        },
        handleSizeChange(val) {
          this.size = val;
          this.shopSelect()
        },
        handleCurrentChange(val) {
          this.page = val;
          this.shopSelect()
        },
        // 刷新
        refresh(){
          this.shopSelect()
        },
        // 补货弹框
        compile(row){
          this.dialogFormVisible = true
          this.form.title = row.title
          this.form.coverImg = row.coverImg
          this.form.goodsId = row.goodsId
          this.form.linkUrl = row.linkUrl
          this.form.content = row.content
          this.form.id = row.id
          this.form.status = row.status
          this.form.createTime = row.createTime
        },
        // 补货
        compileNoticeTo(){
          this.$http({
            url: this.$http.adornUrl2('/selfGoodsVirtual/update'),
            method: 'post',
            data: this.$http.adornData({
            'title':this.form.title,
            'content':this.form.content,
            'linkUrl':this.form.linkUrl,
            'goodsId':this.form.goodsId,
            'coverImg':this.form.coverImg,
            'id':this.form.id,
            'status':this.form.status,
            'createTime':this.form.createTime,
            })
          }).then(({data}) => {
            this.dialogFormVisible = false
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                 this.shopSelect()
                }
              })
          })
        },
        // 获取商品数据
        shopSelect () {
          let page = this.page-1
          let goodsId = this.$route.query.goodsId
          this.tableDataLoading = true
          this.$http({
           url: this.$http.adornUrl2('/selfGoodsVirtual/list'),
           method: 'get',
           params: this.$http.adornParams({
            'page':page,
            'size':this.size,
            'goodsId':goodsId,
           })
          }).then(({data}) => {
            this.tableDataLoading = false
            let returnData = data.data;
            this.tableData = returnData
          })
        },
      },
      mounted() {
        this.shopSelect()
      }
	  };
</script>

<style scoped>
.virtual-amend-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.virtual-amend-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px 16px;
}

.virtual-amend-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.virtual-amend-page__table .el-table {
	width: 100%;
}

.virtual-amend-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.virtual-amend-page__table .el-table td,
.virtual-amend-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.virtual-amend-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.virtual-amend-page__link {
	color: var(--nf-brand, #15cad2);
	cursor: pointer;
}

.virtual-amend-page__link:hover {
	color: var(--nf-brand-deep, #109da4);
}

.virtual-amend-page__status--active {
	color: var(--nf-brand, #15cad2);
}

.virtual-amend-page__filter-actions .el-button--primary:not(.is-plain),
.virtual-amend-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.virtual-amend-page__filter-actions .el-button--primary:not(.is-plain):hover,
.virtual-amend-page__filter-actions .el-button--primary:not(.is-plain):focus,
.virtual-amend-page__table .el-button--primary:not(.is-plain):hover,
.virtual-amend-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.virtual-amend-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
