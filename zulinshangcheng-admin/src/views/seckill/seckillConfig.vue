<template>
  <div class="seckill-config-page">
    <!-- hero -->
    <div class="nf-page-hero seckill-config-page__hero">
      <span class="nf-page-hero__badge">营销活动</span>
      <h2 class="nf-page-hero__title">秒杀配置</h2>
      <p class="nf-page-hero__desc">配置秒杀活动开启时段，管理每日秒杀场次时间，支持添加、修改与删除配置</p>
    </div>

    <!-- 操作白卡 -->
    <div class="nf-filter-card seckill-config-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-s-operation"></i>
        <span>快捷操作</span>
      </div>
      <div class="seckill-config-page__filter-body">
        <div class="seckill-config-page__filter-actions">
          <el-button :disabled="!isAuth('seckillConfig:add')" size="mini" type="primary" icon="document" @click="addNotice">添加配置</el-button>
        </div>
      </div>
    </div>

    <!-- 表格白卡 -->
    <div class="nf-table-card seckill-config-page__table">
      <el-table
        v-loading="tableDataLoading"
        :data="configData.content"
        row-key="id">
        <el-table-column
          prop="secKillTimeId"
          label="编号"
          width="150">
        </el-table-column>
        <el-table-column
          prop="startHour"
          label="开启时间(整数小时)">
          <template slot-scope="scope">
            <span>{{scope.row.startHour}}:00</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="180">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              :disabled="!isAuth('seckillConfig:update')"
              @click="updates(scope.$index, scope.row)">修改
            </el-button>
            <el-button
              size="mini"
              type="danger"
              :disabled="!isAuth('seckillConfig:delete')"
              @click="deletes(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="seckill-config-page__pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[10,15,20,25]"
          :page-size="size"
          :current-page="page"
          layout="total,sizes, prev, pager, next"
          :total="configData.totalElements">
        </el-pagination>
      </div>
    </div>

    <!-- 添加配置 -->
    <el-dialog title="添加配置" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">开启时间(整数小时)：</span>
        <el-input style="width: 300px;" placeholder="请输入开启时间" type="number" :min="0" :max="23" :controls="false" v-model="startHour" autosize></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 修改配置 -->
    <el-dialog title="修改配置" :visible.sync="dialogFormVisible1" center>
      <el-form :model="form">
        <el-form-item label="开启时间(整数小时)：" :label-width="formLabelWidth">
          <el-input style="width: 300px;" placeholder="请输入开启时间" type="number" :min="0" :max="23" :controls="false" v-model="form.startHour" autosize></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
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
          startHour:0,
          holdHour:1,
          form:{
            secKillTimeId:'',
            holdHour:'',
            startHour:'',
          },
          formLabelWidth:'200px',
          tableDataLoading:false,
          dialogFormVisible1:false,
          dialogFormVisible:false,
          configData:[],
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
        // 添加配置
        addNotice(){
          this.dialogFormVisible = true
        },
        // 添加配置
        releasNoticeTo(){
          if(this.startHour>23){
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 3600,
            })
          }
          this.$http({
            url: this.$http.adornUrl2('/selfSecKillTime/save'),
            method: 'post',
            data: this.$http.adornData({
            'startHour':this.startHour,
            })
          }).then(({data}) => {

              if(data.status == 0){
                this.dialogFormVisible = false
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.startHour = 0
                    this.dataSelect()
                  }
                })
              }
              if(data.status == -1){
                this.dialogFormVisible = false
                this.$message({
                  message: data.msg,
                  type: 'error',
                  duration: 3600,
                })
              }
          })
        },
        // 修改配置弹框
        updates(index,rows){
          console.log(rows)
          this.dialogFormVisible1=true;
          this.form.secKillTimeId = rows.secKillTimeId;
          this.form.startHour = rows.startHour;
          this.form.holdHour = rows.holdHour;
        },
        // 修改秒杀配置
        amendNoticeTo(){
          this.$http({
            url: this.$http.adornUrl2('/selfSecKillTime/update'),
            method: 'post',
            data: this.$http.adornData({
            'secKillTimeId':this.form.secKillTimeId,
            'startHour': this.form.startHour,
            })
          }).then(({data}) => {
            if(data.status == 0){
              this.dialogFormVisible1 = false
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
            }
            if(data.status == -1){
              this.dialogFormVisible1 = false
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 3600,
              })
            }
          })
        },
        // 删除配置
        deletes(row){
          this.$confirm(`确定删除此条信息?`, '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$http({
            url: this.$http.adornUrl2('/selfSecKillTime/delete'),
            method: 'get',
            params: this.$http.adornParams({
              'id':row.secKillTimeId
            })
            }).then(({data}) => {
              this.$message({
                message: '删除成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
            })
          }).catch(() => {})
        },
        // 获取分类数据
        dataSelect () {
          let page = this.page-1
          this.tableDataLoading = true
          this.$http({
          url: this.$http.adornUrl2('/selfSecKillTime/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page':page,
            'size':this.size
          })
          }).then(({data}) => {
            this.tableDataLoading = false
            let returnData = data.data;
            this.configData = returnData
          })
        }
      },
      mounted() {
        this.dataSelect()
      }
	  };
</script>

<style scoped>
.seckill-config-page {
  padding: 12px 16px;
  overflow-x: hidden;
}

.seckill-config-page__filter-body {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 12px 16px;
}

.seckill-config-page__filter-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.seckill-config-page__table .el-table {
  width: 100%;
}

.seckill-config-page__table .el-table th {
  background: var(--nf-table-header-bg, #f7f8fa);
  color: var(--nf-text-primary, #303133);
  font-weight: 600;
}

.seckill-config-page__table .el-table td,
.seckill-config-page__table .el-table th {
  border-color: var(--nf-border, #e8eaed);
}

.seckill-config-page__pagination {
  display: flex;
  justify-content: center;
  padding-top: 12px;
}

.seckill-config-page__filter-actions .el-button--primary:not(.is-plain),
.seckill-config-page__table .el-button--primary:not(.is-plain) {
  background-color: transparent;
  border-color: var(--nf-brand, #15cad2);
  color: var(--nf-brand, #15cad2);
}

.seckill-config-page__filter-actions .el-button--primary:not(.is-plain):hover,
.seckill-config-page__filter-actions .el-button--primary:not(.is-plain):focus,
.seckill-config-page__table .el-button--primary:not(.is-plain):hover,
.seckill-config-page__table .el-button--primary:not(.is-plain):focus {
  background-color: var(--nf-brand-lite, #e3f9fa);
  border-color: var(--nf-brand-deep, #109da4);
  color: var(--nf-brand-deep, #109da4);
}
</style>
