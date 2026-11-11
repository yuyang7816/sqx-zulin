<template>
	<div class="lottery-page">
		<!-- hero -->
		<div class="nf-page-hero lottery-page__hero">
			<span class="nf-page-hero__badge">抽奖管理</span>
			<h2 class="nf-page-hero__title">抽奖规则</h2>
			<p class="nf-page-hero__desc">配置抽奖活动规则，管理消耗积分、每日次数与活动状态，支持添加与修改规则</p>
		</div>

		<!-- 操作白卡 -->
		<div class="nf-filter-card lottery-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>快捷操作</span>
			</div>
			<div class="lottery-page__filter-body">
				<div class="lottery-page__filter-actions">
					<el-button :disabled="!isAuth('lottery:add')" size="mini" type="primary"
						icon="document" @click="addCoupon">添加规则</el-button>
					<el-button size="mini" type="primary" plain icon="document"
						@click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card lottery-page__table">
			<el-table v-loading="tableDataLoading" :data="tableData">
				<el-table-column prop="prizeRuleId" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="name" label="抽奖名称">
				</el-table-column>
				<el-table-column prop="jiFen" label="消耗积分">
				</el-table-column>
				<el-table-column prop="number" label="每日抽奖次数">
				</el-table-column>
				<el-table-column prop="detail" label="活动规则" width="200">
				</el-table-column>
				<el-table-column label="状态">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.status == 1">开启</span>
						<span v-if="scope.row.status == 2">关闭</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary"
							@click="compile(scope.$index,scope.row)">奖品
						</el-button>
						<el-button size="mini" type="primary"
							:disabled="!isAuth('lottery:update')"
							@click="shopUpdates(scope.$index,scope.row)">修改
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 添加规则-->
		<el-dialog title="添加规则" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">抽奖名称：</span>
				<el-input style="width:50%;" v-model="name" placeholder="请输入抽奖名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">消耗积分：</span>
				<el-input style="width:50%;" v-model="jiFen" type="number" :min="0" placeholder="请输入消耗积分"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">每日抽奖次数：</span>
				<el-input style="width:50%;" v-model="number" type="number" :min="1" placeholder="请输入每日抽奖次数"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -75px;">活动规则：</span>
				<el-input style="width:50%;" v-model="detail" type="textarea" :rows="4" placeholder="请输入活动规则"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">开启</el-radio>
					<el-radio :label="2">关闭</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="couponNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改优惠券 -->
		<el-dialog title="修改优惠券" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="抽奖名称：" :label-width="formLabelWidth">
					<el-input v-model="form.name" style="width:65%;" placeholder="请输入抽奖名称"></el-input>
				</el-form-item>
				<el-form-item label="消耗积分：" :label-width="formLabelWidth">
					<el-input v-model="form.jiFen" style="width:65%;" placeholder="请输入消耗积分"></el-input>
				</el-form-item>
				<el-form-item label="每日抽奖次数：" :label-width="formLabelWidth">
					<el-input v-model="form.number" style="width:65%;" placeholder="请输入每日抽奖次数"></el-input>
				</el-form-item>
				<el-form-item label="活动规则：" :label-width="formLabelWidth">
					<el-input v-model="form.detail" style="width:65%;" placeholder="请输入活动规则"></el-input>
				</el-form-item>
				<el-form-item label="状态：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.status">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="2">关闭</el-radio>
					</el-radio-group>
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
        name:'',
        jiFen:'',
        number:'',
        detail:'',
        status:1,
        form:{
          prizeRuleId:'',
          name:'',
          jiFen:'',
          number:'',
          detail:'',
          status:'',
          createTime:''
        },
				formLabelWidth:'200px',
				tableDataLoading:false,
				dialogFormVisible:false,
        dialogFormVisible1:false,
				tableData:[]
			}
		},

		methods: {
      // 刷新
      refresh(){
       this.dataSelect()
      },
      // 奖品
      compile(index,rows){
         this.$router.push({path: '/lotteryPrize', query: {prizeRuleId: rows.prizeRuleId}});
      },
      // 添加规则
      addCoupon(){
        this.dialogFormVisible = true
      },
      // 添加规则
      couponNoticeTo(){
          if (this.name == '') {
              this.$notify({
                  title: '提示',
                  duration: 1800,
                  message: '请输入抽奖名称',
                  type: 'warning'
              });
              return
          }
          if (this.jiFen == '') {
              this.$notify({
                  title: '提示',
                  duration: 1800,
                  message: '请输入消耗积分',
                  type: 'warning'
              });
              return
          }
          if (this.number == '') {
              this.$notify({
                  title: '提示',
                  duration: 1800,
                  message: '请输入每日抽奖次数',
                  type: 'warning'
              });
              return
          }
          if (this.detail == '') {
              this.$notify({
                  title: '提示',
                  duration: 1800,
                  message: '请输入活动规则',
                  type: 'warning'
              });
              return
          }
          this.$http({
            url: this.$http.adornUrl2('/selfPrizeRule/save'),
            method: 'post',
            data: this.$http.adornData({
            'name':this.name,
            'jiFen':this.jiFen,
            'number':this.number,
            'detail':this.detail,
            'status':this.status
            })
          }).then(({data}) => {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.name = ''
                  this.jiFen = ''
                  this.number = ''
                  this.detail = ''
                  this.status = 1
                  this.dataSelect()
                }
              })
              this.dialogFormVisible = false
          })
      },
      // 修改规则
      shopUpdates(index,row){
         this.dialogFormVisible1 = true
         this.form.name = row.name
         this.form.jiFen = row.jiFen
         this.form.number = row.number
         this.form.detail = row.detail
         this.form.status = row.status
         this.form.prizeRuleId = row.prizeRuleId
         this.form.createTime = row.createTime
      },
      amendNoticeTo(){
          this.$http({
            url: this.$http.adornUrl2('/selfPrizeRule/update'),
            method: 'post',
            data: this.$http.adornData({
            'prizeRuleId':this.form.prizeRuleId,
            'name':this.form.name,
            'jiFen':this.form.jiFen,
            'number':this.form.number,
            'detail':this.form.detail,
            'status':this.form.status,
            'createTime':this.form.createTime,
            })
          }).then(({data}) => {
            if(data.status == 0){
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
              this.dialogFormVisible1 = false
            }else{
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
              this.dialogFormVisible1 = false
            }
          })
      },
			//获取优惠券数据
			dataSelect () {
        let page = this.page-1
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl2('/selfPrizeRule/list'),
				method: 'get',
				params: this.$http.adornParams({
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
.lottery-page {
	padding: 12px 16px;
}

.lottery-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.lottery-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
}

.lottery-page__table .el-table {
	width: 100%;
}

.lottery-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.lottery-page__table .el-table td,
.lottery-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

/* 列表页 primary 描边钮（非 plain 亦透明底） */
.lottery-page__filter-actions .el-button--primary:not(.is-plain),
.lottery-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.lottery-page__filter-actions .el-button--primary:not(.is-plain):hover,
.lottery-page__filter-actions .el-button--primary:not(.is-plain):focus,
.lottery-page__table .el-button--primary:not(.is-plain):hover,
.lottery-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}
</style>
