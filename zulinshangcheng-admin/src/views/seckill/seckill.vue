<template>
  <div class="seckill-page">
    <!-- hero -->
    <div class="nf-page-hero seckill-page__hero">
      <span class="nf-page-hero__badge">营销活动</span>
      <h2 class="nf-page-hero__title">秒杀商品</h2>
      <p class="nf-page-hero__desc">管理秒杀活动商品，支持按名称筛选、上下架切换，以及添加、修改与删除操作</p>
    </div>

    <!-- 筛选白卡 -->
    <div class="nf-filter-card seckill-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="seckill-page__filter-body">
        <div class="seckill-page__filter-item">
          <label class="seckill-page__filter-label">秒杀商品名称</label>
          <el-input class="seckill-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入秒杀商品名称" v-model="title"></el-input>
        </div>
        <div class="seckill-page__filter-actions">
          <el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
          <el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
          <el-button size="mini" type="primary" icon="document" :disabled="!isAuth('seckill:add')" @click="addShop">添加秒杀商品</el-button>
          <el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
        </div>
      </div>
    </div>

    <!-- 表格白卡 -->
    <div class="nf-table-card seckill-page__table">
      <el-table v-loading="tableDataLoading" :data="tableData.content">
        <el-table-column fixed prop="secKillId" label="编号" width="80">
        </el-table-column>
        <el-table-column fixed prop="coverImg" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.coverImg" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="秒杀商品名称" width="200">
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="秒杀开始时间" width="160">
        </el-table-column>
        <el-table-column prop="endTime" label="秒杀结束时间" width="160">
        </el-table-column>
        <el-table-column prop="status" label="商品上下架">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              @change="change(scope.row.status,scope.row.secKillId)"
              :active-value="openValue"
              :inactive-value="closeValue"
              active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="statusString" label="秒杀状态" width="160">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              :disabled="!isAuth('seckill:update')"
              @click="shopUpdates(scope.$index, scope.row)">修改
            </el-button>
            <el-button
              size="mini"
              type="danger"
              :disabled="!isAuth('seckill:delete')"
              @click="shopdeletes(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="seckill-page__pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30, 50, 100]"
          :page-size="size"
          :current-page="page"
          layout="total,sizes, prev, pager, next"
          :total="tableData.totalElements">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
	export default {
        data() {
          return {
          openValue: 1,
          closeValue: 2,
          size:10,
          // page:1,
		  pagelist:[],
          id:'',
          img:'',
          title:'',
          readonly:true,
          price:'',
          memberPrice:'',
          originalPrice:'',
          sales:'',
          status:0,
          status2:'',
          descrition:'',
          totalnum:0,
          imgUrl:'',
          linkUrl:'',
          value:[],
          classDatas:[],
          form1:{
            id:'',
            title:'',
            img:'',
            typeId:'',
            price:'',
            memberPrice:'',
            status:'',
            sales:'',
            descrition:'',
            originalPrice:'',
            createAt:''
          },
          formLabelWidth:'200px',
          tableDataLoading:false,
          dialogFormVisible2:false,
          dialogFormVisible3:false,
          typeDatas:[],
          advertData:[],
          tableData:[],
        }
      },
      filters: {
        numFilter (value) {
          let realVal = ''
          if (!isNaN(value) && value!== '') {
            // 截取当前数据到小数点后三位
            let tempVal = parseFloat(value).toFixed(3)
            realVal = tempVal.substring(0, tempVal.length - 1)
          } else {
            realVal = '--'
          }
          return realVal
        }
      },
	  computed: {
	    page() {
	  	this.pagelist = this.$store.state.common.pagelist;
	  	var page = 1;
	      for(let val of this.pagelist) {
	        if(val.router == "seckill") {
	          page = val.page;
	        }
	      }
	  	return page;
	    }
	  },
      methods: {
        // 状态
        change (val, id) {
          this.$http({
            url: this.$http.adornUrl2('/selfSecKill/updateStatus'),
            method: 'get',
            params: this.$http.adornParams({
              'id':id
            })
          }).then(({data}) => {
            if(data.status == 0){
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.shopSelect()
                }
              })
            }
            if(data.status == -1){
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {
                  this.shopSelect()
                }
              })
            }
          })
        },
        handleChange(value) {
          this.value.forEach(element => {
            this.typeId = element;
          });
        },

        handleAvatarSuccess(file) {
           this.img = file.data;
        },
        handleAvatarSuccess2(file) {
             this.form1.img = file.data;
         },
        handleSizeChange(val) {
          this.size = val;
          this.shopSelect()
        },
        handleCurrentChange(val) {
          // this.page = val;
		  for(let item of this.pagelist) {
		    if(item.router == "seckill") {
		      item.page = val;
		    }
		  }
		  this.$store.commit("common/updataPage", this.pagelist);
          this.shopSelect()
        },
        //搜索
        select(){
           this.shopSelect()
        },
        // 刷新
        refresh(){
          this.shopSelect()
        },
        // 重置
        cleans(){
          this.title = ''
          this.status = 0
          this.typeId = ''
          this.value = []
          this.shopSelect()
        },
        //添加商品
        addShop(){
          this.$router.push({path: '/seckillAdd'})
        },
        // 修改商品
        shopUpdates(index,rows){
          this.$router.push({path: '/seckillAmend', query: {id: rows.secKillId}});
        },
        // 删除商品
        shopdeletes(row){
          console.log(row)
          this.$confirm(`确定删除此条信息?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$http({
              url: this.$http.adornUrl2('/selfSecKill/delete'),
              method: 'get',
              params: this.$http.adornParams({
              'id':row.secKillId
              })
            }).then(({data}) => {
              this.$message({
              message: '删除成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.shopSelect()
              }
              })
            })
          }).catch(() => {})
        },
        // 获取商品数据
        shopSelect () {
          let page = this.page-1
          this.tableDataLoading = true
          this.$http({
          url: this.$http.adornUrl2('/selfSecKill/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page':page,
            'size':this.size,
            'title':this.title,
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
.seckill-page {
  padding: 12px 16px;
  overflow-x: hidden;
}

.seckill-page__filter-body {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 12px 16px;
}

.seckill-page__filter-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.seckill-page__filter-label {
  font-size: 13px;
  color: var(--nf-text-secondary, #606266);
  line-height: 1;
}

.seckill-page__filter-control {
  width: 180px;
}

.seckill-page__filter-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
  margin-left: auto;
}

.seckill-page__table .el-table {
  width: 100%;
}

.seckill-page__table .el-table th {
  background: var(--nf-table-header-bg, #f7f8fa);
  color: var(--nf-text-primary, #303133);
  font-weight: 600;
}

.seckill-page__table .el-table td,
.seckill-page__table .el-table th {
  border-color: var(--nf-border, #e8eaed);
}

.seckill-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}

.seckill-page__filter-actions .el-button--primary:not(.is-plain),
.seckill-page__table .el-button--primary:not(.is-plain) {
  background-color: transparent;
  border-color: var(--nf-brand, #15cad2);
  color: var(--nf-brand, #15cad2);
}

.seckill-page__filter-actions .el-button--primary:not(.is-plain):hover,
.seckill-page__filter-actions .el-button--primary:not(.is-plain):focus,
.seckill-page__table .el-button--primary:not(.is-plain):hover,
.seckill-page__table .el-button--primary:not(.is-plain):focus {
  background-color: var(--nf-brand-lite, #e3f9fa);
  border-color: var(--nf-brand-deep, #109da4);
  color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
  .seckill-page__filter-control {
    width: 100%;
    min-width: 140px;
  }

  .seckill-page__filter-actions {
    margin-left: 0;
    width: 100%;
  }
}
</style>
