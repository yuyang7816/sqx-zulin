<template>
  <div class="mod-user">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <!-- <el-form-item>
        <el-input v-model="dataForm.roleName" placeholder="用户名" clearable></el-input>
      </el-form-item> -->
      <el-form-item>
       <!-- <el-button @click="getDataList()">查询</el-button> -->
        <el-button  type="primary" :disabled="!isAuth('sys:user:save')"  @click="addOrUpdateHandle()">添加管理员</el-button>
        <!-- <el-button type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button> -->
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
	   fixed
		prop="id"
		label="ID">
	 </el-table-column>
	 <el-table-column
		prop="phone"
		label="账号">
	  </el-table-column>
	  <el-table-column
		 prop="userName"
		 label="备注">
	   </el-table-column>
	  <!-- <el-table-column
		prop="roleName"
		label="角色">
	  </el-table-column> -->
	  <el-table-column
		prop="createAt"
		label="创建时间">
	  </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
		  <el-button  type="text" size="small" @click="addOrLookHandle(scope.row.id)">查看</el-button>
          <el-button  type="text" :disabled="!isAuth('sys:user:update')" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button  type="text" :disabled="!isAuth('sys:user:delete')" size="small" @click="deleteHandle(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
	<!-- 查看弹框 -->
	<el-dialog title="查看详情" :visible.sync="dialogFormVisible" center>
		<el-form :model="form">
			<el-form-item label="账号：" :label-width="formLabelWidth" >
				 <el-input v-model="form.phone" style="width:65%;" disabled></el-input>
			</el-form-item>
			<el-form-item label="密码：" :label-width="formLabelWidth" >
				<el-input v-model="form.pwd" style="width:65%;" type="password" disabled></el-input>
			</el-form-item>
			<el-form-item label="备注：" :label-width="formLabelWidth" >
				<el-input v-model="form.userName" style="width:65%;" disabled></el-input>
			</el-form-item>
			<el-form-item label="角色：" :label-width="formLabelWidth" >
				<el-checkbox-group v-model="form.roleIdList" disabled="disabled">
				  <el-checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId" >{{ role.roleName }}</el-checkbox>
				</el-checkbox-group>
			</el-form-item>
		</el-form>
	  <div slot="footer" class="dialog-footer">
		<el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
	  </div>
	</el-dialog>
	</div>
</template>

<script>
  import AddOrUpdate from './user-add-or-update'
  export default {
    data () {
      return {
        dataForm: {
          roleName: ''
        },
        dataList: [],
        page: 1,
        size: 20,
        totalPage: 0,
		readonly:true,
		roleList:[],
		formLabelWidth:'200px',
        dataListLoading: false,
        dataListSelections: [],
		dialogFormVisible:false,
        addOrUpdateVisible: false,
		form:{
			phone:'',
			pwd:'',
			userName:'',
			roleIdList:[]
		}
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    methods: {
      // 获取数据列表
      getDataList () {
		let page = this.page-1
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl2('/admin/adminInfoList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size,
          })
        }).then(({data}) => {
          if (data && data.status === 0) {
            this.dataList = data.data
          } else {
            this.dataList = []
          }
          this.dataListLoading = false
        })
      },
	  // 查看
	  addOrLookHandle(id){
		  this.dialogFormVisible = true
		  this.$http({
		        url: this.$http.adornUrl2(`/admin/adminInfoDetails?id=${id}`),
		        method: 'get',
		        params: this.$http.adornParams()
		      }).then(({data}) => {
		        if (data && data.status === 0) {
		          this.form.userName = data.data.user.userName;
		          this.form.phone = data.data.user.phone;
		          this.form.pwd = data.data.user.pwd;
				  this.roleList = data.data.roleList
				 var roleList = data.data.roleList
				 var arr = [];
				 for(var i=0;i<roleList.length;i++){
				 	var arrValue = roleList[i].roleId;
				 	arr.push(arrValue)
				 }
				 this.form.roleIdList = arr
		        }
		      })
	  },
      // 每页数
      sizeChangeHandle (val) {
        this.size = val
        this.page = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.page = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
	  // 删除
		deleteHandle(row){
			let userId = this.$cookie.get('userId')
			let delid = row.id
			this.id = delid
			this.$http({
				url: this.$http.adornUrl2(`/admin/${userId}`),
				method: 'get',
				params: this.$http.adornParams({
				})
			}).then(({data}) => {
				let returnData = data.data;
				this.state = returnData.state
				this.phone = returnData.phone
				this.$confirm(`确定删除此条信息?`, '提示', {
				  confirmButtonText: '确定',
				  cancelButtonText: '取消',
				  type: 'warning'
				}).then(() => {
				  this.$http({
					url: this.$http.adornUrl2(`/admin/delete/${this.id}`),
					method: 'get',
					params: this.$http.adornParams({
					})
				  }).then(({data}) => {
					  this.$message({
						message: '删除成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
						  this.getDataList()
						}
					  })
				  })
				}).catch(() => {})
			})
		}
    }
  }
</script>
<style>
	.el-checkbox__input.is-disabled+span.el-checkbox__label{
		color:#000000!important;
	}
</style>
