<template>
  <div class="marketing-page">
    <!-- hero -->
    <div class="nf-page-hero marketing-page__hero">
      <span class="nf-page-hero__badge">营销管理</span>
      <h2 class="nf-page-hero__title">拼团商品</h2>
      <p class="nf-page-hero__desc">创建与管理拼团活动商品，支持按名称筛选，可添加、修改商品及上下架操作</p>
    </div>

    <!-- 筛选白卡 -->
    <div class="nf-filter-card marketing-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="marketing-page__filter-body">
        <div class="marketing-page__filter-item">
          <label class="marketing-page__filter-label">拼团商品名称</label>
          <el-input class="marketing-page__filter-control" @keydown.enter.native="select" clearable
            placeholder="请输入拼团商品名称" v-model="title"></el-input>
        </div>
        <div class="marketing-page__filter-actions">
          <el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
          <el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
          <el-button :disabled="!isAuth('marketing:add')" size="mini" type="primary" icon="document"
            @click="addShop">添加拼团商品</el-button>
          <el-button size="mini" type="primary" plain icon="document" @click="refresh">刷新</el-button>
        </div>
      </div>
    </div>

    <!-- 表格白卡 -->
    <div class="nf-table-card marketing-page__table">
      <el-table v-loading="tableDataLoading" :data="tableData.content">
        <el-table-column fixed prop="goodsId" label="编号" width="80">
        </el-table-column>
        <el-table-column fixed prop="coverImg" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.coverImg" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="拼团商品名称" width="200">
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="拼团开始时间" width="160">
        </el-table-column>
        <el-table-column prop="endTime" label="拼团结束时间" width="160">
        </el-table-column>
        <el-table-column prop="hours" label="拼团时效(小时)" width="120">
        </el-table-column>
        <el-table-column prop="number" label="参团人数">
        </el-table-column>
        <el-table-column prop="status" label="商品上下架" width="150">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.id)"
              :active-value="openValue" :inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
            </el-switch>
          </template>
        </el-table-column>
        <!-- <el-table-column
	  			prop="createTime"
	  			label="创建时间"
	  			width="160">
	  		  </el-table-column> -->
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('marketing:update')"
              @click="shopUpdates(scope.$index, scope.row)">修改
            </el-button>
            <!-- <el-button
	  		        size="mini"
	  		        type="danger"
                :disabled="!isAuth('marketing:delete')"
	  		        @click="shopdeletes(scope.row)">删除
	  		      </el-button> -->
          </template>
        </el-table-column>
      </el-table>
      <div class="marketing-page__pagination">
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
        openValue: 1,
        closeValue: 2,
        size: 10,
        // page:1,
        pagelist: [],
        id: '',
        img: '',
        title: '',
        readonly: true,
        price: '',
        memberPrice: '',
        originalPrice: '',
        sales: '',
        status: 0,
        status2: '',
        descrition: '',
        totalnum: 0,
        imgUrl: '',
        linkUrl: '',
        value: [],
        classDatas: [],
        form1: {
          id: '',
          title: '',
          img: '',
          typeId: '',
          price: '',
          memberPrice: '',
          status: '',
          sales: '',
          descrition: '',
          originalPrice: '',
          createAt: ''
        },
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible2: false,
        dialogFormVisible3: false,
        typeDatas: [],
        advertData: [],
        tableData: [],
      }
    },
    filters: {
      numFilter(value) {
        let realVal = ''
        if (!isNaN(value) && value !== '') {
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
        for (let val of this.pagelist) {
          if (val.router == "marketing") {
            page = val.page;
          }
        }
        return page;
      }
    },
    methods: {
      // 状态
      change(val, id) {
        this.$http({
          url: this.$http.adornUrl2('/selfGroup/updateStatus'),
          method: 'get',
          params: this.$http.adornParams({
            'id': id
          })
        }).then(({
          data
        }) => {
          if (data.status == 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.shopSelect()
              }
            })
          }
          if (data.status == -1) {
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
        for (let item of this.pagelist) {
          if (item.router == "marketing") {
            item.page = val;
          }
        }
        this.$store.commit("common/updataPage", this.pagelist);
        this.shopSelect()
      },
      //搜索
      select() {
        this.shopSelect()
      },
      // 重置
      cleans() {
        this.title = ''
        this.status = 0
        this.typeId = ''
        this.value = []
        this.shopSelect()
      },
      // 刷新
      refresh() {
        this.shopSelect()
      },
      //添加商品
      addShop() {
        this.$router.push({
          path: '/marketingAdd'
        })
      },
      // 修改商品
      shopUpdates(index, rows) {
        console.log(rows)
        this.$router.push({
          path: '/marketingAmend',
          query: {
            id: rows.id
          }
        });
      },
      // 删除商品
      shopdeletes(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl2('/selfGroup/delete'),
            method: 'get',
            params: this.$http.adornParams({
              'id': row.id
            })
          }).then(({
            data
          }) => {
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
      shopSelect() {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl2('/selfGroup/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size,
            'title': this.title,
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
      this.shopSelect()
    }
  };
</script>

<style scoped>
.marketing-page {
  padding: 12px 16px;
  overflow-x: hidden;
}

.marketing-page__filter-body {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 12px 16px;
}

.marketing-page__filter-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.marketing-page__filter-label {
  font-size: 13px;
  color: var(--nf-text-secondary, #606266);
  line-height: 1;
}

.marketing-page__filter-control {
  width: 180px;
}

.marketing-page__filter-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
  margin-left: auto;
}

.marketing-page__table .el-table {
  width: 100%;
}

.marketing-page__table .el-table th {
  background: var(--nf-table-header-bg, #f7f8fa);
  color: var(--nf-text-primary, #303133);
  font-weight: 600;
}

.marketing-page__table .el-table td,
.marketing-page__table .el-table th {
  border-color: var(--nf-border, #e8eaed);
}

.marketing-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}

.marketing-page__filter-actions .el-button--primary:not(.is-plain),
.marketing-page__table .el-button--primary:not(.is-plain) {
  background-color: transparent;
  border-color: var(--nf-brand, #15cad2);
  color: var(--nf-brand, #15cad2);
}

.marketing-page__filter-actions .el-button--primary:not(.is-plain):hover,
.marketing-page__filter-actions .el-button--primary:not(.is-plain):focus,
.marketing-page__table .el-button--primary:not(.is-plain):hover,
.marketing-page__table .el-button--primary:not(.is-plain):focus {
  background-color: var(--nf-brand-lite, #e3f9fa);
  border-color: var(--nf-brand-deep, #109da4);
  color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
  .marketing-page__filter-control {
    width: 100%;
    min-width: 140px;
  }

  .marketing-page__filter-actions {
    margin-left: 0;
    width: 100%;
  }
}
</style>
