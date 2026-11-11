<template>
  <div class="lottery-prize-page">
    <!-- hero -->
    <div class="nf-page-hero lottery-prize-page__hero">
      <span class="nf-page-hero__badge">抽奖管理</span>
      <h2 class="nf-page-hero__title">抽奖奖品</h2>
      <p class="nf-page-hero__desc">管理当前抽奖规则下的奖品配置，支持添加、修改奖品名称、图片、权重与抽奖类型</p>
    </div>

    <!-- 操作白卡 -->
    <div class="nf-filter-card lottery-prize-page__filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-s-operation"></i>
        <span>快捷操作</span>
      </div>
      <div class="lottery-prize-page__filter-body">
        <div class="lottery-prize-page__filter-actions">
          <el-button :disabled="!isAuth('lottery:add')" size="mini" type="primary"
            icon="document" @click="addCoupon">添加奖品</el-button>
          <el-button size="mini" type="primary" plain icon="document"
            @click="prev">返回上一页</el-button>
          <el-button size="mini" type="primary" plain icon="document"
            @click="refresh">刷新</el-button>
        </div>
      </div>
    </div>

    <!-- 表格白卡 -->
    <div class="nf-table-card lottery-prize-page__table">
      <el-table
        v-loading="tableDataLoading"
        :data="tableData">
        <el-table-column
          prop="prizeId"
          label="编号"
          width="80">
        </el-table-column>
        <el-table-column
          prop="prizeImage"
          label="奖品图片">
          <template slot-scope="scope">
            <span v-if="scope.row.prizeImage == ''">暂无图片</span>
            <img v-else :src="scope.row.prizeImage" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="奖品名称"
          width="200">
        </el-table-column>
        <el-table-column
          prop="addJiFen"
          label="中奖积分">
        </el-table-column>
        <el-table-column
          prop="weight"
          label="权重">
        </el-table-column>
        <el-table-column
          prop="type"
          label="抽奖类型">
          <template slot-scope="scope">
            <span v-if="scope.row.type == 1">积分抽奖</span>
            <span v-if="scope.row.type == 2">其他</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间">
        </el-table-column>
        <el-table-column
          label="操作"
          width="160">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              :disabled="!isAuth('lottery:update')"
              @click="compile(scope.row)">修改
            </el-button>
            <el-button
              size="mini"
              type="danger"
              :disabled="!isAuth('lottery:delete')"
              @click="shopdeletes(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加将品-->
    <el-dialog title="添加将品" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">奖品展示名称：</span>
        <el-input style="width:50%;" v-model="name" placeholder="请输入奖品名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">中奖积分：</span>
        <el-input style="width:50%;" v-model="addJiFen" type="number" placeholder="请输入中奖积分"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -60px;">奖品图片：</span>
        <div style="display: inline-block;width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
          <el-upload
            class="avatar-uploader"
            v-model="prizeImage"
            :action="$http.adornUrl2('/alioss/upload')"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            >
            <img v-if="prizeImage" :src="prizeImage" class="avatar" style="width: 148px;height: 148px;"/>
            <i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">权重：</span>
        <el-input style="width:50%;" v-model="weight" type="number" :min="0" placeholder="请输入权重"></el-input>
        <p style="margin: 0;padding-left: 205px;font-size: 13px;color: #15cad2;">权重越大，抽中几率越大，建议1-100，权重为0则抽不中</p>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">抽奖类型：</span>
        <el-radio-group v-model="type">
          <el-radio :label="1">积分抽奖</el-radio>
          <el-radio :label="2">其他</el-radio>
        </el-radio-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="couponNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 修改兑换码 -->
    <el-dialog title="兑换码修改" :visible.sync="dialogFormVisible1" center>
      <el-form :model="form">
        <el-form-item label="奖品展示名称：" :label-width="formLabelWidth" >
          <el-input v-model="form.name" style="width:65%;" placeholder="请输入奖品展示名称"></el-input>
        </el-form-item>
        <el-form-item label="中奖积分：" :label-width="formLabelWidth" >
          <el-input v-model="form.addJiFen" type="number" style="width:65%;" placeholder="请输入中奖积分"></el-input>
        </el-form-item>
        <el-form-item label="奖品图片：" :label-width="formLabelWidth" >
          <div style="display: inline-block;width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
            <el-upload
              class="avatar-uploader"
              v-model="form.prizeImage"
              :action="$http.adornUrl2('/alioss/upload')"
              :show-file-list="false"
              :on-success="handleAvatarSuccess1"
              >
              <img v-if="form.prizeImage" :src="form.prizeImage" class="avatar" style="width: 148px;height: 148px;"/>
              <i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="权重：" :label-width="formLabelWidth" >
          <el-input v-model="form.weight" style="width:65%;" type="number"  :min="0" placeholder="请输入权重"></el-input>
          <p style="margin: 0;font-size: 13px;color: #15cad2;">权重越大，抽中几率越大，建议1-100，权重为0则抽不中</p>
        </el-form-item>
        <el-form-item label="抽奖类型：" :label-width="formLabelWidth" >
          <el-radio-group v-model="form.type">
            <el-radio :label="1">积分抽奖</el-radio>
            <el-radio :label="2">其他</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
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
          name:'',
          prizeImage:'',
          type:1,
          addJiFen:'',
          weight:'',
          form:{
            prizeId:'',
            prizeRuleId:'',
            name:'',
            addJiFen:'',
            prizeImage:'',
            type:'',
            weight:'',
            createTime:'',
          },
          formLabelWidth:'200px',
          tableDataLoading:false,
          dialogFormVisible:false,
          dialogFormVisible1:false,
          tableData:[],
        }
      },
      methods: {
        handleAvatarSuccess(file,fileList) {
        	this.prizeImage = file.data
        },
        handleAvatarSuccess1(file,fileList) {
        	this.form.prizeImage = file.data
        },
        // 返回上一级
        prev(){
        	this.$router.back()
        },
        // 刷新
        refresh(){
          this.shopSelect()
        },
        // 添加奖品
        addCoupon(){
          this.dialogFormVisible = true
        },
        // 添加奖品
        couponNoticeTo(){
          let prizeRuleId = this.$route.query.prizeRuleId
            if (this.name == '') {
                this.$notify({
                    title: '提示',
                    duration: 1800,
                    message: '请输入奖品名称',
                    type: 'warning'
                });
                return
            }
            if (this.weight == '') {
                this.$notify({
                    title: '提示',
                    duration: 1800,
                    message: '请输入奖品权重',
                    type: 'warning'
                });
                return
            }
            if (this.type == '') {
                this.$notify({
                    title: '提示',
                    duration: 1800,
                    message: '请选择抽奖类型',
                    type: 'warning'
                });
                return
            }
            this.$http({
              url: this.$http.adornUrl2('/selfPrize/save'),
              method: 'post',
              data: this.$http.adornData({
              'name':this.name,
              'prizeRuleId':prizeRuleId,
              'prizeImage':this.prizeImage,
              'weight':this.weight,
              'type':this.type,
              'addJiFen':this.addJiFen
              })
            }).then(({data}) => {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.name = ''
                    this.prizeImage = ''
                    this.weight = ''
                    this.type = 1
                    this.addJiFen = ''
                    this.shopSelect()
                  }
                })
                this.dialogFormVisible = false
            })
        },
        // 修改奖品弹框
        compile(row){
          this.dialogFormVisible1 = true
          this.form.prizeId = row.prizeId
          this.form.prizeRuleId = row.prizeRuleId
          this.form.name = row.name
          this.form.prizeImage = row.prizeImage
          this.form.type = row.type
          this.form.weight = row.weight
          this.form.createTime = row.createTime
          this.form.addJiFen = row.addJiFen
        },
        // 修改奖品弹框
        compileNoticeTo(){
          this.$http({
            url: this.$http.adornUrl2('/selfPrize/update'),
            method: 'post',
            data: this.$http.adornData({
            'prizeId':this.form.prizeId,
            'prizeRuleId':this.form.prizeRuleId,
            'name':this.form.name,
            'prizeImage':this.form.prizeImage,
            'type':this.form.type,
            'weight':this.form.weight,
            'addJiFen':this.form.addJiFen,
            'createTime':this.form.createTime,
            })
          }).then(({data}) => {
            this.dialogFormVisible1 = false
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
        // 删除奖品
        shopdeletes(row){
        	this.$confirm(`确定删除此条信息?`, '提示', {
        	  confirmButtonText: '确定',
        	  cancelButtonText: '取消',
        	  type: 'warning'
        	}).then(() => {
        	  this.$http({
        	    url: this.$http.adornUrl2('/selfPrize/delete'),
        	    method: 'get',
        	    params: this.$http.adornParams({
        			'id':row.prizeId
        	    })
        	  }).then(({data}) => {
              if(data&&data.status == 0){
                this.$message({
                  message: '删除成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.shopSelect()
                  }
                })
              }else{
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
        	}).catch(() => {})
        },
        // 获取商品数据
        shopSelect () {
          let prizeRuleId = this.$route.query.prizeRuleId
          this.tableDataLoading = true
          this.$http({
           url: this.$http.adornUrl2('/selfPrize/list'),
           method: 'get',
           params: this.$http.adornParams({
             'prizeRuleId':prizeRuleId
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
.lottery-prize-page {
  padding: 12px 16px;
}

.lottery-prize-page__filter-body {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 12px 16px;
}

.lottery-prize-page__filter-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.lottery-prize-page__table .el-table {
  width: 100%;
}

.lottery-prize-page__table .el-table th {
  background: var(--nf-table-header-bg, #f7f8fa);
  color: var(--nf-text-primary, #303133);
  font-weight: 600;
}

.lottery-prize-page__table .el-table td,
.lottery-prize-page__table .el-table th {
  border-color: var(--nf-border, #e8eaed);
}

/* 列表页 primary 描边钮（非 plain 亦透明底） */
.lottery-prize-page__filter-actions .el-button--primary:not(.is-plain),
.lottery-prize-page__table .el-button--primary:not(.is-plain) {
  background-color: transparent;
  border-color: var(--nf-brand, #15cad2);
  color: var(--nf-brand, #15cad2);
}

.lottery-prize-page__filter-actions .el-button--primary:not(.is-plain):hover,
.lottery-prize-page__filter-actions .el-button--primary:not(.is-plain):focus,
.lottery-prize-page__table .el-button--primary:not(.is-plain):hover,
.lottery-prize-page__table .el-button--primary:not(.is-plain):focus {
  background-color: var(--nf-brand-lite, #e3f9fa);
  border-color: var(--nf-brand-deep, #109da4);
  color: var(--nf-brand-deep, #109da4);
}
</style>
