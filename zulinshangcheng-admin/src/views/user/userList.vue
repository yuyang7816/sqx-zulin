<template>
	<div class="user-list-page">
		<!-- hero -->
		<div class="nf-page-hero user-list-page__hero">
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">用户列表</h2>
			<p class="nf-page-hero__desc">查看与管理平台注册用户，支持按渠道、手机号与微信昵称筛选，可进行状态切换、会员管理与用户详情查看</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card user-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="user-list-page__filter-body">
				<div class="user-list-page__filter-item">
					<label class="user-list-page__filter-label">渠道筛选</label>
					<el-select v-model="platform" class="user-list-page__filter-control" placeholder="请选择类型"
						@change="selectTrigger(platform)">
						<el-option v-for="item in platforms" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="user-list-page__filter-item">
					<label class="user-list-page__filter-label">手机号</label>
					<el-input class="user-list-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone">
					</el-input>
				</div>
				<div class="user-list-page__filter-item">
					<label class="user-list-page__filter-label">微信昵称</label>
					<el-input class="user-list-page__filter-control" @keydown.enter.native="select" clearable placeholder="微信昵称筛选"
						v-model="nickName"></el-input>
				</div>
				<div class="user-list-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 + tabs -->
		<div class="nf-table-card user-list-page__table">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="user-list-page__tabs">
				<el-tab-pane label="全部用户" name="first">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="userId" fixed label="编号" width="80">
						</el-table-column>
						<el-table-column fixed prop="userName" label="昵称" width="120">
							<template slot-scope="scope">
								<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号" width="120">
							<template slot-scope="scope">
								<span style="color: #15cad2;cursor: pointer;"
									@click="updates(scope.row)">{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="isVip" label="是否是会员" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.isVip == 1">是</span>
								<span v-else>否</span>
							</template>
						</el-table-column>
						<el-table-column prop="sex" label="性别" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.sex == 1">男</span>
								<span v-if="scope.row.sex == 2">女</span>
								<span v-if="!scope.row.sex">未知</span>
							</template>
						</el-table-column>
						<el-table-column prop="age" label="年龄" width="100">
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码" width="100">
						</el-table-column>
						<el-table-column prop="pub_share_pre_fee_user_s" label="来源">
							<template slot-scope="scope">
								<span v-if="scope.row.platform == 'weixin' ">公众号</span>
								<span v-if="scope.row.platform == 'wap' ">网站</span>
								<span v-if="scope.row.platform == 'app' ">APP</span>
								<span v-if="scope.row.platform == 'mp' ">小程序</span>
								<span v-if="scope.row.platform == 'H5' ">H5</span>
								<span v-if="scope.row.platform == '小程序' ">小程序</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhifubao" label="支付宝">
							<template slot-scope="scope">
								<span>{{scope.row.zhifubaoName}}{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="name" label="备注" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.name}}</span>
								<div>
									<el-input type="text" @keydown.enter.native="remark(scope.row)" v-model="scope.row.name"
										clearable placeholder="备注" style="width: 120px;font-size:14px;">
										<i slot="suffix" class="el-input__icon el-icon-search" @click="remark(scope.row)"></i>
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="100">
						</el-table-column>
						<el-table-column fixed='right' prop="status " label="状态">
							<template slot-scope="scope">
								<el-switch v-model="scope.row.status" @change="change(scope.row)"
									:active-value="openValue" :inactive-value="closeValue"
									:disabled="!isAuth('userList:updateStatus')" active-color="#13ce66"
									inactive-color="#ff4949">
								</el-switch>
							</template>
						</el-table-column>
						<el-table-column label="操作" fixed='right' width="120">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情
								</el-button>
								<el-button size="mini" type="warning" @click="zengsongVip(scope.row)" :disabled="!isAuth('userList:updateVip')" >修改会员</el-button>
								<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" :disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1" plain>取消会员</el-button>
								<el-button :disabled="!isAuth('userList:delete')" size="mini" type="danger"
									@click="deleteuser(scope.row)">
									删除用户
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="user-list-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</el-tab-pane>
				<el-tab-pane label="会员用户" name="second">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="userId" fixed label="编号" width="80">
						</el-table-column>
						<el-table-column fixed prop="userName" label="昵称" width="120">
							<template slot-scope="scope">
								<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号" width="120">
							<template slot-scope="scope">
								<span style="color: #15cad2;cursor: pointer;"
									@click="updates(scope.row)">{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="sex" label="性别" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.sex == 1">男</span>
								<span v-if="scope.row.sex == 2">女</span>
								<span v-if="!scope.row.sex">未知</span>
							</template>
						</el-table-column>
						<el-table-column prop="age" label="年龄" width="100">
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码" width="100">
						</el-table-column>
						<el-table-column prop="pub_share_pre_fee_user_s" label="来源">
							<template slot-scope="scope">
								<span v-if="scope.row.platform == 'weixin' ">公众号</span>
								<span v-if="scope.row.platform == 'wap' ">网站</span>
								<span v-if="scope.row.platform == 'app' ">APP</span>
								<span v-if="scope.row.platform == 'mp' ">小程序</span>
								<span v-if="scope.row.platform == 'H5' ">H5</span>
								<span v-if="scope.row.platform == '小程序' ">小程序</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhifubao" label="支付宝">
							<template slot-scope="scope">
								<span>{{scope.row.zhifubaoName}}{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="name" label="备注" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.name}}</span>
								<div>
									<el-input type="text" @keydown.enter.native="remark(scope.row)" v-model="scope.row.name"
										clearable placeholder="备注" style="width: 120px;font-size:14px;">
										<i slot="suffix" class="el-input__icon el-icon-search" @click="remark(scope.row)"></i>
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="100">
						</el-table-column>
						<el-table-column fixed='right' prop="state " label="状态">
							<template slot-scope="scope">
								<el-switch v-model="scope.row.status" @change="change(scope.row)"
									:active-value="openValue" :inactive-value="closeValue"
									:disabled="!isAuth('userList:updateStatus')" active-color="#13ce66"
									inactive-color="#ff4949">
								</el-switch>
							</template>
						</el-table-column>
						<el-table-column fixed='right' label="操作" width="120">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情
								</el-button>
								<el-button size="mini" type="warning" @click="zengsongVip(scope.row)" :disabled="!isAuth('userList:updateVip')" >修改会员</el-button>
								<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" :disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1" plain>取消会员</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
									@click="deleteuser(scope.row)">
									删除用户
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="user-list-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</el-tab-pane>
				<el-tab-pane label="普通用户" name="third">
					<el-table v-loading="tableDataLoading" :data="tableData.records">
						<el-table-column prop="userId" fixed label="编号" width="80">
						</el-table-column>
						<el-table-column fixed prop="userName" label="昵称" width="120">
							<template slot-scope="scope">
								<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="phone" label="手机号" width="120">
							<template slot-scope="scope">
								<span style="color: #15cad2;cursor: pointer;"
									@click="updates(scope.row)">{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="sex" label="性别" width="100">
							<template slot-scope="scope">
								<span v-if="scope.row.sex == 1">男</span>
								<span v-if="scope.row.sex == 2">女</span>
								<span v-if="!scope.row.sex">未知</span>
							</template>
						</el-table-column>
						<el-table-column prop="age" label="年龄" width="100">
						</el-table-column>
						<el-table-column prop="invitationCode" label="邀请码" width="100">
						</el-table-column>
						<el-table-column prop="pub_share_pre_fee_user_s" label="来源">
							<template slot-scope="scope">
								<span v-if="scope.row.platform == 'weixin' ">公众号</span>
								<span v-if="scope.row.platform == 'wap' ">网站</span>
								<span v-if="scope.row.platform == 'app' ">APP</span>
								<span v-if="scope.row.platform == 'mp' ">小程序</span>
								<span v-if="scope.row.platform == 'H5' ">H5</span>
								<span v-if="scope.row.platform == '小程序' ">小程序</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhifubao" label="支付宝">
							<template slot-scope="scope">
								<span>{{scope.row.zhifubaoName}}{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="name" label="备注" width="150">
							<template slot-scope="scope">
								<span>{{scope.row.name}}</span>
								<div>
									<el-input type="text" @keydown.enter.native="remark(scope.row)" v-model="scope.row.name"
										clearable placeholder="备注" style="width: 120px;font-size:14px;">
										<i slot="suffix" class="el-input__icon el-icon-search" @click="remark(scope.row)"></i>
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="100">
						</el-table-column>
						<el-table-column fixed='right' prop="state " label="状态">
							<template slot-scope="scope">
								<el-switch v-model="scope.row.status" @change="change(scope.row)"
									:active-value="openValue" :inactive-value="closeValue"
									:disabled="!isAuth('userList:updateStatus')" active-color="#13ce66"
									inactive-color="#ff4949">
								</el-switch>
							</template>
						</el-table-column>
						<el-table-column label="操作" fixed='right' width="120">
							<template slot-scope="scope">
								<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情
								</el-button>
								<el-button size="mini" type="warning" @click="zengsongVip(scope.row)" :disabled="!isAuth('userList:updateVip')" >修改会员</el-button>
								<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
									@click="deleteuser(scope.row)">
									删除用户
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="user-list-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="tableData.total">
						</el-pagination>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 修改会员 -->
		<el-dialog title="修改会员" :visible.sync="dialogFormVisibleZs" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
				<el-radio-group v-model="type">
					<el-radio :label="1">添加</el-radio>
					<el-radio :label="2">减少</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">修改天数：</span>
				<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
					placeholder="请输入修改天数"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleZs = false">取 消</el-button>
				<el-button type="primary" @click="updateVip()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 2,
				state: 'false',
				size: 10,
				page: 1,
				phone: '',
				nickName: '',
				relationId: '',
				nickNam: '',
				platform: '',
				isRelation: '',
				isTuan: '',
				value: '',
				nickName: '',
				grade: '',
				name: '',
				userData: [],
				activeName: 'first',
				tableDataLoading: true,
				tableData: [],
				platforms: [{
					value: '',
					label: '全部'
				}, {
					value: 'wap',
					label: '网站'
				}, {
					value: 'app',
					label: 'APP'
				}, {
					value: 'H5',
					label: 'H5'
				}, {
					value: '小程序',
					label: '小程序'
				}],
				dialogFormVisibleZs:false,
				isVip:'',
				type:1,
				proportion:'',
			}
		},
		methods: {
			// 团长跳转详情页
			colonelClick(relationId) {
				this.$http({
					url: this.$http.adornUrl2('/user/relationId'),
					method: 'get',
					params: this.$http.adornParams({
						'relationId': relationId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					let id = returnData.id
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: id
						}
					})
				})
			},
			// 修改等级1111
			addition(row) {
				let userId = row.userId
				let grade = row.divideScale
				this.$confirm(`确定要修改佣金比例吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/user/updateGrades'),
						method: 'get',
						params: this.$http.adornParams({
							'userId': userId,
							'divideScale': row.divideScale,
							'divideTwoScale': row.divideTwoScale,
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 修改备注
			remark(row) {
				let userId = row.id
				let name = row.name
				this.$confirm(`确定要修改备注吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/user/updateName'),
						method: 'get',
						params: this.$http.adornParams({
							'userId': userId,
							'name': name
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 状态
			change(row) {
				this.$http({
					url: this.$http.adornUrl2('/user/updateUser'),
					method: 'post',
					params: this.$http.adornParams({
						'userId': row.userId,
						'status': row.status,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			// 会员订单跳转
			hyorder(row) {
				this.$router.push({
					path: '/order',
					query: {
						relationid: row.relationId
					}
				})
			},
			// 提现记录跳转
			record(row) {
				this.$router.push({
					path: '/financeList'
				})
			},
			// 团长订单跳转
			colonel(row) {
				this.$router.push({
					path: '/order',
					query: {
						invitation: row.invitation
					}
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
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.size = val
				this.userSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.userSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '全部用户') {
					this.page = 1
					this.size = 10
					this.isRelation = ''
					this.isTuan = ''
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.page = 1
					this.size = 10
					this.isRelation = 1
					this.isTuan = ''
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					this.page = 1
					this.size = 10
					this.isRelation =2
					this.isTuan = ''
					this.dataSelect()
				}
				if (tab._props.label == '团长用户') {
					this.page = 1
					this.size = 10
					this.isRelation = ''
					this.isTuan = 1
					this.dataSelect()
				}
			},
			// select自动选择
			selectTrigger() {
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.platform = ''
				this.phone = ''
				this.nickName = ''
				this.dataSelect()
			},
			// 刷新
			refresh() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 查询
			select1() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 重置
			cleans1() {
				this.platform = ''
				this.phone = ''
				this.nickName = ''
				this.dataSelect()
			},
			// 刷新
			refresh1() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 查询
			select2() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 重置
			cleans2() {
				this.platform = ''
				this.phone = ''
				this.nickName = ''
				this.dataSelect()
			},
			// 刷新
			refresh2() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 查询
			select3() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			// 重置
			cleans3() {
				this.platform = ''
				this.phone = ''
				this.nickName = ''
				this.dataSelect()
			},
			// 刷新
			refresh3() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/user/deleteByUserId/?userId=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = ''
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(`/user/userList`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'phone': this.phone,
						'platform': this.platform,
						'isVip': this.isRelation,
						'isTuan': this.isTuan,
						'nickName': this.nickName,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 修改会员弹框
			zengsongVip(row){
				this.type = 1
				this.proportion = ''
				this.userIdss = row.userId
				this.dialogFormVisibleZs = true
			},
			// 设置会员
			updateVip(row){
			if (this.proportion === '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '请输入天数',
					type: 'warning'
				});
				return
			}
				this.$http({
					url: this.$http.adornUrl('/admin/vipDetails/giveUserVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId':this.userIdss,
						'num':this.proportion,
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleZs = false
								this.dataSelect()
							}
						})
					}else{
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}

				})
			},
			// 取消会员
			quxiaoVip(row){
				this.$confirm(`确定要取消会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
				this.$http({
					url: this.$http.adornUrl('/admin/vipDetails/cancelUserVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId':row.userId
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}else{
				this.$message({
					message: data.msg,
					type: 'warning',
					duration: 1500,
					onClose: () => {
					}
				})
					}

				})
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.user-list-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.user-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.user-list-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.user-list-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.user-list-page__filter-control {
	width: 180px;
}

.user-list-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.user-list-page__tabs {
	margin-bottom: 0;
}

.user-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.user-list-page__table .el-table {
	width: 100%;
}

.user-list-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.user-list-page__table .el-table td,
.user-list-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.user-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.user-list-page__filter-actions .el-button--primary:not(.is-plain),
.user-list-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.user-list-page__filter-actions .el-button--primary:not(.is-plain):hover,
.user-list-page__filter-actions .el-button--primary:not(.is-plain):focus,
.user-list-page__table .el-button--primary:not(.is-plain):hover,
.user-list-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.user-list-page__table .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}

@media (max-width: 768px) {
	.user-list-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.user-list-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
