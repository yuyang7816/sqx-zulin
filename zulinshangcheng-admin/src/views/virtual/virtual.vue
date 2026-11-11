<template>
	<div class="virtual-page">
		<!-- hero -->
		<div class="nf-page-hero virtual-page__hero">
			<span class="nf-page-hero__badge">商品管理</span>
			<h2 class="nf-page-hero__title">虚拟商品</h2>
			<p class="nf-page-hero__desc">管理虚拟商品库存与卡密，支持按商品标题筛选，可进行补货与编辑操作</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card virtual-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="virtual-page__filter-body">
				<div class="virtual-page__filter-item">
					<label class="virtual-page__filter-label">商品标题</label>
					<el-input class="virtual-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
						v-model="title"></el-input>
				</div>
				<div class="virtual-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card virtual-page__table">
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
				<el-table-column prop="isJiFenGoods" label="是否积分商品">
					<template slot-scope="scope">
						<span v-if="scope.row.isJiFenGoods == 1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="originalPrice" label="商品原价">
				</el-table-column>
				<el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span v-if="scope.row.isJiFenGoods == 1">{{scope.row.price}} 积分</span>
						<span v-else>{{scope.row.price}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="memberPrice" label="会员价格">
				</el-table-column>
				<el-table-column prop="virtualSum" label="虚拟商品总数">
				</el-table-column>
				<el-table-column prop="virtualCount" label="虚拟商品库存">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('virtual:replen')"
							@click="program(scope.row)">补货
						</el-button>
						<el-button size="mini" type="primary" @click="compile(scope.row)">编辑
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="virtual-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 15, 20, 25]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</div>

		<!-- 补货 -->
		<el-dialog title="补货" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">卡密链接：</span>
				<el-input style="width:60%;" v-model="linkUrl" placeholder="请输入卡密链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -110px;">卡密内容：</span>
				<el-input style="width:60%;" v-model="content" type="textarea" :rows="6" wrap="virtual" placeholder="请输入卡密内容"></el-input>
				<p style="font-size: 13px;color: #999999;padding-left: 200px;margin-top: 5px;">*可以批量新增，每行一个，可以智能去重</p>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="programNoticeTo()">确 定</el-button>
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
          title1:'',
          linkUrl:'',
          goodsId:'',
          coverImg:'',
          content:'',
		  isJiFenGoods:0,
          type:'',
          status:0,
          isExpress:2,
          formLabelWidth:'200px',
          tableDataLoading:false,
          dialogFormVisible:false,
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
          this.title = ''
          this.status = 0
          this.shopSelect()
        },
        // 刷新
        refresh(){
          this.shopSelect()
        },
        // 编辑
        compile(rows){
           this.$router.push({path: '/virtualAmend', query: {goodsId: rows.id}});
        },
        // 补货弹框
        program(row){
          this.dialogFormVisible = true
          this.title1 = row.title
          this.coverImg = row.coverImg
          this.goodsId = row.id
        },
        // 补货
        programNoticeTo(){
          let content = this.content.replace(/\r\n/g, ',').replace(/\n/g, ',').replace(/\s/g, ',')
          let cont = content.split(',')
          cont  = [...new Set(cont)]
          this.content = cont.join(',')
          if (this.linkUrl == '') {
              this.$notify({
                  title: '提示',
                  duration: 1800,
                  message: '请输入卡密链接',
                  type: 'warning'
              });
              return
          }
          if (this.content == '') {
              this.$notify({
                  title: '提示',
                  duration: 1800,
                  message: '请输入卡密内容',
                  type: 'warning'
              });
              return
          }
          this.$http({
            url: this.$http.adornUrl2('/selfGoodsVirtual/save'),
            method: 'post',
            data: this.$http.adornData({
            'title':this.title1,
            'content':this.content,
            'linkUrl':this.linkUrl,
            'goodsId':this.goodsId,
            'coverImg':this.coverImg,
            })
          }).then(({data}) => {
            if(data.status == 0){
              this.dialogFormVisible = false
              this.$message({
                message: data.data,
                type: 'success',
                duration: 3500,
                onClose: () => {
                 this.shopSelect()
                }
              })
            }else{
              this.$message({
                message: data.data,
                type: 'error',
                duration: 3500,
                onClose: () => {
                 this.shopSelect()
                }
              })
            }
          })
        },
        // 获取商品数据
        shopSelect () {
         let page = this.page-1
          let status = 0
          this.tableDataLoading = true
          this.$http({
           url: this.$http.adornUrl2('/goods/goodsList'),
           method: 'get',
           params: this.$http.adornParams({
            'page':page,
            'size':this.size,
            'title':this.title,
            'type':this.type,
            'status':status,
            'isExpress':this.isExpress,
			'isJiFenGoods':this.isJiFenGoods
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
.virtual-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.virtual-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.virtual-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.virtual-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.virtual-page__filter-control {
	width: 180px;
}

.virtual-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.virtual-page__table .el-table {
	width: 100%;
}

.virtual-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.virtual-page__table .el-table td,
.virtual-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.virtual-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.virtual-page__filter-actions .el-button--primary:not(.is-plain),
.virtual-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.virtual-page__filter-actions .el-button--primary:not(.is-plain):hover,
.virtual-page__filter-actions .el-button--primary:not(.is-plain):focus,
.virtual-page__table .el-button--primary:not(.is-plain):hover,
.virtual-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.virtual-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.virtual-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
