<template>
	<div class="couponuser-page">
		<!-- hero -->
		<div class="nf-page-hero couponuser-page__hero">
			<span class="nf-page-hero__badge">优惠券管理</span>
			<h2 class="nf-page-hero__title">优惠券领取记录</h2>
			<p class="nf-page-hero__desc">查看用户已领取的优惠券记录，支持按优惠券名称筛选，了解使用状态与失效时间</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card couponuser-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="couponuser-page__filter-body">
				<div class="couponuser-page__filter-item">
					<label class="couponuser-page__filter-label">优惠券名称</label>
					<el-input class="couponuser-page__filter-control" @keydown.enter.native="select" clearable
						placeholder="请输入优惠券名称" v-model="couponName"></el-input>
				</div>
				<div class="couponuser-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary" plain icon="document"
						@click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card couponuser-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column prop="couponName" label="优惠券名称" width="150">
				</el-table-column>
				<el-table-column prop="nickName" label="所属用户">
				</el-table-column>
				<el-table-column prop="lessMoney" label="优惠券面值">
				</el-table-column>
				<el-table-column prop="minMoney" label="优惠券最低消费">
				</el-table-column>
				<el-table-column prop="failureTime" label="优惠券失效时间" width="150">
				</el-table-column>
				<el-table-column label="状态">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.status == 1">未使用</span>
						<span v-if="scope.row.status == 2">已使用</span>
						<span v-if="scope.row.status == 3">已过期</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="150">
				</el-table-column>
			</el-table>
			<div class="couponuser-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
	    data() {
	      return {
				size:10,
				page:1,
        couponName:'',
				formLabelWidth:'200px',
				tableDataLoading:false,
				dialogFormVisible:false,
				tableData:[],
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
      // 查询
      select () {
        this.dataSelect()
      },
      // 重置
      cleans () {
        this.couponName = ''
        this.dataSelect()
      },
      // 刷新
      refresh(){
       this.dataSelect()
      },
			//获取已发布优惠券数据
			dataSelect () {
        let page = this.page-1
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl2('/selfCouponUser/list'),
				method: 'get',
				params: this.$http.adornParams({
          'page':page,
          'size':this.size,
          'couponName':this.couponName
				})
			  }).then(({data}) => {
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
.couponuser-page {
	padding: 12px 16px;
}

.couponuser-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.couponuser-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.couponuser-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.couponuser-page__filter-control {
	width: 200px;
}

.couponuser-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.couponuser-page__table .el-table {
	width: 100%;
}

.couponuser-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.couponuser-page__table .el-table td,
.couponuser-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.couponuser-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

/* 列表页 primary 描边钮（非 plain 亦透明底） */
.couponuser-page__filter-actions .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.couponuser-page__filter-actions .el-button--primary:not(.is-plain):hover,
.couponuser-page__filter-actions .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.couponuser-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.couponuser-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
