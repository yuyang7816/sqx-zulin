<template>
	<div class="autonym-page">
		<!-- hero -->
		<div class="nf-page-hero autonym-page__hero">
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">实名认证</h2>
			<p class="nf-page-hero__desc">审核与管理用户实名认证信息，支持按姓名、手机号、身份证号与状态筛选，进行通过、拒绝与修改操作</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card autonym-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="autonym-page__filter-body">
				<div class="autonym-page__filter-item">
					<label class="autonym-page__filter-label">用户姓名</label>
					<el-input class="autonym-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入用户姓名"
						v-model="userName"></el-input>
				</div>
				<div class="autonym-page__filter-item">
					<label class="autonym-page__filter-label">手机号</label>
					<el-input class="autonym-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>
				<div class="autonym-page__filter-item">
					<label class="autonym-page__filter-label">身份证号</label>
					<el-input class="autonym-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入身份证号"
						v-model="idNumber"></el-input>
				</div>
				<div class="autonym-page__filter-item">
					<label class="autonym-page__filter-label">状态</label>
					<el-select v-model="status" class="autonym-page__filter-control" @change="select()">
						<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="autonym-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 + tabs -->
		<div class="nf-table-card autonym-page__table">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="autonym-page__tabs">
				<el-tab-pane label="个人认证" name="first">
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="realName" label="真实姓名" width="100">
						<template slot-scope="scope">
							<span style="color: #15cad2;cursor: pointer;"
								@click="updates(scope.row)">{{scope.row.realName}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="认证手机号" width="150"></el-table-column>
					<el-table-column prop="idNumber" label="身份证号" width="200"></el-table-column>
					<!-- <el-table-column prop="idCardFront" label="身份证正面" width="150">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.idCardFront" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.idCardFront"
											alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="idCardVerso" label="身份证反面" width="150">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.idCardVerso" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.idCardVerso"
											alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column> -->
					<!-- <el-table-column prop="enterpriseAddress" label="认证地址" width="150"></el-table-column> -->
					<el-table-column prop="remark" label="审核原因" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.remark?scope.row.remark:'暂无'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="status" label="状态">
						<template slot-scope="scope">
							<span style="color: green;" v-if="scope.row.status==0">待审核</span>
							<span style="color: green;" v-if="scope.row.status==1">已通过</span>
							<span style="color: red;" v-if="scope.row.status==2">已拒绝</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary"
								:disabled="!isAuth('autonym:shenhe') || scope.row.status!=0"
								@click="tonguo(scope.row)">通过</el-button>
							<el-button size="mini" type="danger"
								:disabled="!isAuth('autonym:shenhe') || scope.row.status!=0"
								@click="deleteuser(scope.row)">拒绝</el-button>
							<el-button size="mini" type="primary" :disabled="!isAuth('autonym:update')"
								@click="updateClicks(scope.row)" style="margin: 5px;">
								修改
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('autonym:delete')"
								@click="deletes(scope.row)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="autonym-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
				</el-tab-pane>
			</el-tabs>
		</div>
		<el-dialog title="审核拒绝" :visible.sync="dialogVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">操作：</span>
				<el-radio-group v-model="isSubmit">
					<el-radio :label="0">下次可再次提交</el-radio>
					<el-radio :label="1">下次不可再次提交</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;bottom: 54px;">审核原因：</span>
				<el-input type="textarea" :rows="3" placeholder="请输入审核原因" v-model="remark"
					style="width:50%;display: inline-block;">
				</el-input>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="jujue()">确 定</el-button>
			</span>
		</el-dialog>
		<el-dialog title="修改认证信息" :visible.sync="dialogFormVisibleR" center>
		
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">姓名：</span>
				<el-input style="width:50%;" v-model="realName" type="text" placeholder="请输入姓名"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">电话：</span>
				<el-input style="width:50%;" v-model="enterprisePhone" type="text" placeholder="请输入电话"></el-input>
			</div> -->

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">身份证号：</span>
				<el-input style="width:50%;" v-model="identityCardNumber" type="text" placeholder="请输入身份证号"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">认证地址：</span>
				<el-input style="width:50%;" v-model="enterpriseAddress" type="text" placeholder="请输入认证地址"></el-input>
			</div> -->
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">认证备注：</span>
				<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入认证备注"></el-input>
			</div> -->

			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">头像：</span>
				<div style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="avatar"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess5">
						<img v-if="avatar" :src="avatar" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div> -->
			<!-- <div style="margin-bottom: 10px;" >
				<span style="width: 200px;display: inline-block;text-align: right;">身份证正面图：</span>
				<div style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="identityCardFront"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="identityCardFront" :src="identityCardFront" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;" >
				<span style="width: 200px;display: inline-block;text-align: right;">身份证反面图：</span>
				<div style="display: inline-block;">
					<el-upload class="avatar-uploader" v-model="identityCardVerso"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess2">
						<img v-if="identityCardVerso" :src="identityCardVerso" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div> -->
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleR = false">取 消</el-button>
				<el-button type="primary" @click="refusetoUp()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				remark: '',
				dialogVisible: false,
				userName: '',
				phone: '',
				idNumber:'',
				limit: 10,
				page: 1,
				activeName: 'first',
				tableDataLoading: true,
				tableData: {},
				id: '',
				userTypeT: 1,
				// ````
				userType: '',
				classifyIds: '',
				enterprisePhone: '',
				realName: '',
				sex: '',
				identityCardNumber: '',
				identityCardFront: '',
				identityCardVerso: '',
				enterpriseAddress: '',
				businessLicense: '',
				enterpriseName: '',
				avatar: '',
				address: '',
				userId: '',
				dialogFormVisibleR: false,
				userIds: '',
				percentage1: 0,
				businessLicenseImg: [],
				imagePhoto: [],
				status:'',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待审核'
					},{
						value: 1,
						label: '已通过'
					}, {
						value: 2,
						label: '已拒绝'
					}
				],
				// info: {
				// 	stockDate: this.getNowTime(), //日期
				// },
				isSubmit:0,
			}
		},
		methods: {
			//拒绝审核
			jujue() {
				if (this.remark == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入审核原因',
						type: 'warning'
					})
					return
				}
				if (this.remark.length > 100) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '审核理由最多可输入100字',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/admin/userCertification/authCertification'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'status': 2,
						'id': this.id,
						'remark': this.remark,
						'isSubmit':this.isSubmit
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							type: 'success',
							message: '审核成功',
							duration: 1500,
							onClose: () => {
								this.dialogVisible = false
								this.dataSelect()
							}
						});


					} else {
						this.$message.error(data.msg);
						// this.dataSelect()
					}
				})
			},
			//拒绝审核弹窗
			deleteuser(item) {
				this.id = item.id
				this.remark = ''
				this.dialogVisible = true
			},
			//通过审核
			tonguo(item) {
				this.$confirm('确认通过审核吗?', '提示', {
					confirmButtonText: '确认',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/admin/userCertification/authCertification'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'status': 1,
							'id': item.id,
							'remark': '',

						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								type: 'success',
								message: '审核成功'
							});
							this.dataSelect()

						} else {
							this.$message.error(data.msg);
							this.dataSelect()
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消审核'
					});
				});
			},
			//分页
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			//重置
			cleans() {
				this.page = 1
				this.limit = 10
				this.userName = ''
				this.phone = ''
				this.idNumber= ''
				this.status = ''
				this.dataSelect()
			},
			//查询
			select() {
				this.dataSelect()
			},
			//切换认证类型数据 //first 个人认证  second 企业认证
			handleClick(tab, event) {
				console.log(tab._props.label, '---------',event)
				
				this.page = 1
				this.userName = ''
				this.phone = ''
				this.idNumber = ''
				if (tab._props.label == '企业认证') {
					this.userTypeT = 2
						this.dataSelect()
				}
				if (tab._props.label == '个人认证') {
					this.userTypeT = 1
					this.dataSelect()
				}

				// if (tab._props.label == '企业认证') {
				// 	this.userTypeT = 2
				// 	this.dataSelect()
				// }
			},
			// 获取认证数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/userCertification/getUserCertificationList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'realName': this.userName,
						'phone': this.phone,
						'idNumber':this.idNumber,
						'status':this.status
					})
				}).then(({
					data
				}) => {
					// console.log('---',data,this.tableDataLoading)
					this.tableDataLoading = false
					
					let returnData = data.data
					this.tableData = returnData
					console.log('---',this.tableData)
				})
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			updateClicks(row) {

				this.ids = row.id

				// this.userType = row.userType
				this.realName = row.realName
				// this.enterprisePhone = row.enterprisePhone
				this.identityCardNumber = row.idNumber
				// this.enterpriseAddress = row.enterpriseAddress
				this.identityCardFront = row.idCardFront
				this.identityCardVerso = row.idCardVerso
				this.remark = row.remark
				// this.businessLicense = row.businessLicense
				// this.enterpriseName = row.enterpriseName
				this.userId = row.userId
				this.dialogFormVisibleR = true
			},
			handleAvatarSuccess1(file, fileList) {
				this.identityCardFront = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.identityCardVerso = file.data
			},
			// 轮播图片上传
			handleRemove(file) {
				this.businessLicenseImg.push(file.data);
			},
			handleDownload(file) {

				console.log(file, this.img)
			},
			// 删除轮播图
			dels(index) {
				this.businessLicenseImg.splice(index, 1);
				console.log(this.img)
			},
			refusetoUp() {
				if (this.realName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入姓名',
						type: 'warning'
					})
					return
				}
				// if (this.enterprisePhone == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入电话',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.identityCardNumber == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入身份证号',
						type: 'warning'
					})
					return
				}
					// if (this.identityCardFront == '') {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请上传身份证正面照',
					// 		type: 'warning'
					// 	})
					// 	return
					// }
					// if (this.identityCardVerso == '') {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请上传身份证背面照',
					// 		type: 'warning'
					// 	})
					// 	return
					// }
				
				this.$http({
					url: this.$http.adornUrl2('/admin/userCertification/updateCertification'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'userId': this.userId,
						'realName': this.realName,
						'remark': this.remark,
						'idNumber': this.identityCardNumber,
						'idCardFront': this.identityCardFront,
						'idCardVerso': this.identityCardVerso,
						'id': this.ids
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleR = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			//删除
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/admin/userCertification/deleteUserCertificationById?id=' + delid),
						method: 'post',
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				})
			},
			dataSelect1(){
				if( this.$route.query.status!=null){
					this.status = this.$route.query.status
				}else{
					this.status =''
				}
				this.dataSelect()
			},
		},
		mounted() {
			if(this.$route.query.status!=null){
				this.status = this.$route.query.status 
			}else{
				this.status =''
			}
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelect1'
		}
	}
</script>

<style scoped>
.autonym-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.autonym-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.autonym-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.autonym-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.autonym-page__filter-control {
	width: 180px;
}

.autonym-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.autonym-page__tabs {
	margin-bottom: 0;
}

.autonym-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.autonym-page__table .el-table {
	width: 100%;
}

.autonym-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.autonym-page__table .el-table td,
.autonym-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.autonym-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.autonym-page__filter-actions .el-button--primary:not(.is-plain),
.autonym-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.autonym-page__filter-actions .el-button--primary:not(.is-plain):hover,
.autonym-page__filter-actions .el-button--primary:not(.is-plain):focus,
.autonym-page__table .el-button--primary:not(.is-plain):hover,
.autonym-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.el-upload {
	border: 1px dashed #d9d9d9;
	border-radius: 6px;
	cursor: pointer;
	position: relative;
	overflow: hidden;
}

.avatar-uploader {
	border: 1px dashed #d9d9d9;
	border-radius: 6px;
	cursor: pointer;
	position: relative;
	overflow: hidden;
	border-color: var(--nf-brand, #15cad2);
	width: 148px;
	height: 148px;
}

.el-upload:hover {
	border-color: var(--nf-brand, #15cad2);
}

.avatar-uploader-icon {
	font-size: 28px;
	color: #8c939d;
	width: 148px;
	height: 148px;
	line-height: 148px;
	text-align: center;
}

.avatar {
	width: 148px;
	height: 148px;
	display: block;
}

.autonym-page__table .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}

.imgs {
	position: relative;
	border-radius: 6px;
	width: 148px;
	height: 148px;
	margin-right: 10px;
	display: inline-block;
}

.dels {
	position: absolute;
	top: 0;
	left: 0;
	display: none;
}

.dels .el-icon-delete {
	line-height: 148px;
	padding-left: 58px;
	font-size: 25px;
	color: #fff;
}

.imgs:hover .dels {
	width: 100%;
	height: 100%;
	background: #000;
	display: block;
	opacity: 0.5;
}

@media (max-width: 768px) {
	.autonym-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.autonym-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>