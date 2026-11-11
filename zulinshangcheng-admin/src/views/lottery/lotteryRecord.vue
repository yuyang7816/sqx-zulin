<template>
	<div class="lottery-record-page">
		<!-- hero -->
		<div class="nf-page-hero lottery-record-page__hero">
			<span class="nf-page-hero__badge">抽奖管理</span>
			<h2 class="nf-page-hero__title">抽奖记录</h2>
			<p class="nf-page-hero__desc">查看用户抽奖参与记录，支持按用户昵称筛选，展示积分变动与创建时间</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card lottery-record-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="lottery-record-page__filter-body">
				<div class="lottery-record-page__filter-item">
					<label class="lottery-record-page__filter-label">用户昵称</label>
					<el-input class="lottery-record-page__filter-control" @keydown.enter.native="select" clearable
						placeholder="请输入用户昵称" v-model="nickName"></el-input>
				</div>
				<div class="lottery-record-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card lottery-record-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column fixed prop="prizeUserId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="nickName" label="用户昵称">
				</el-table-column>
				<el-table-column prop="lessJiFen" label="减少的积分">
				</el-table-column>
				<el-table-column prop="addJiFen" label="增加的积分">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div class="lottery-record-page__pagination">
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
          nickName:'',
          formLabelWidth:'200px',
          tableDataLoading:false,
          tableData:[],
        }
      },
      methods: {
        handleSizeChange(val) {
          this.size = val;
          this.shopSelect()
        },
        handleCurrentChange(val) {
          this.page = val;
          this.shopSelect()
        },
        //搜索
        select(){
           this.shopSelect()
        },
        // 重置
        cleans(){
          this.nickName = ''
          this.shopSelect()
        },
        // 刷新
        refresh(){
          this.shopSelect()
        },
        // 获取商品数据
        shopSelect () {
          let page = this.page-1
          this.tableDataLoading = true
          this.$http({
          url: this.$http.adornUrl2('/selfPrizeUser/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page':page,
            'size':this.size,
            'nickName':this.nickName,
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
.lottery-record-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.lottery-record-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.lottery-record-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.lottery-record-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.lottery-record-page__filter-control {
	width: 180px;
}

.lottery-record-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.lottery-record-page__table .el-table {
	width: 100%;
}

.lottery-record-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.lottery-record-page__table .el-table td,
.lottery-record-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.lottery-record-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.lottery-record-page__filter-actions .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.lottery-record-page__filter-actions .el-button--primary:not(.is-plain):hover,
.lottery-record-page__filter-actions .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.lottery-record-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.lottery-record-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
