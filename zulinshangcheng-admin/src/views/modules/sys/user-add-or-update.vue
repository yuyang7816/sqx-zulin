<template>
  <el-dialog
    :title="!dataForm.id ? '添加管理员' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="账号" prop="phone">
        <el-input v-model="dataForm.phone" placeholder="账号"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="pwd" :class="{ 'is-required': !dataForm.id }">
        <el-input v-model="dataForm.pwd" type="password" placeholder="密码"></el-input>
      </el-form-item>
			<el-form-item label="备注" prop="userName" :class="{ 'is-required': !dataForm.id }">
			  <el-input v-model="dataForm.userName" placeholder="备注"></el-input>
			</el-form-item>
      <el-form-item label="角色" size="mini" prop="roleIdList" :class="{ 'is-required': !dataForm.id }">
        <el-checkbox-group v-model="dataForm.roleIdList">
          <el-checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId">{{ role.roleName }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { isEmail, isMobile } from '@/utils/validate'
  export default {
    data () {
      return {
        visible: false,
		page:1,
		size:10,
		roleName:'',
        roleList: [],
        dataForm: {
          id: 0,
          userName: '',
          pwd: '',
          phone: '',
		  createAt:'',
          roleIdList: [],
        },
        dataRule: {
          phone: [
            { required: true, message: '用户名不能为空', trigger: 'blur' }
          ],
          pwd: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
		  userName: [
		    { required: true, message: '备注不能为空', trigger: 'blur' }
		  ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
		let page = this.page-1
        this.$http({
          url: this.$http.adornUrl2('/sys/role/list'),
          method: 'get',
          params: this.$http.adornParams({
				'page': page,
				'size': this.size,
				'roleName': this.roleName
			})
        }).then(({data}) => {
          this.roleList = data && data.status === 0 ? data.data.content : []
        }).then(() => {
          this.visible = true
          this.$nextTick(() => {
            this.$refs['dataForm'].resetFields()
          })
        }).then(() => {
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl2(`/admin/adminInfoDetails?id=${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.status === 0) {
                this.dataForm.userName = data.data.user.userName;
                this.dataForm.phone = data.data.user.phone;
                this.dataForm.pwd = data.data.user.pwd;
				this.dataForm.id = data.data.user.id;
				this.dataForm.createAt = data.data.user.createAt;
				var roleList = data.data.roleList
				var arr = [];
				for(var i=0;i<roleList.length;i++){
					var arrValue = roleList[i].roleId;
					arr.push(arrValue)
				}
				this.dataForm.roleIdList = arr
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
			  if(this.dataForm.roleIdList.length<1){
					this.$message({
					  message: '请选择角色',
					  type: 'error',
					  duration: 1500,
					})
			  }else{
				this.$http({
				  url: this.$http.adornUrl2(`/admin/${!this.dataForm.id ? 'add' : 'update'}`),
				  method: 'post',
				  data: this.$http.adornData({
				    'id': this.dataForm.id || undefined,
				    'userName': this.dataForm.userName,
				    'pwd': this.dataForm.pwd,
				    'phone': this.dataForm.phone,
				    'roleIdList': this.dataForm.roleIdList,
					'createAt' : this.dataForm.createAt
				  })
				}).then(({data}) => {
				  if (data && data.status === 0) {
				    this.$message({
				      message: '操作成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
				        this.visible = false
				        this.$emit('refreshDataList')
				      }
				    })
				  } else {
				    this.$message.error(data.msg)
				  }
				})  
			  }
			}
        })
      }
    }
  }
</script>
