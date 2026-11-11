<template>
	<div class="wechat-list-page">
		<!-- hero -->
		<div class="nf-page-hero wechat-list-page__hero">
			<span class="nf-page-hero__badge">微信管理</span>
			<h2 class="nf-page-hero__title">微信配置</h2>
			<p class="nf-page-hero__desc">管理微信公众号基础配置、消息模板、自定义菜单与关键字自动回复</p>
		</div>

		<!-- 筛选白卡 -->
		<div class="nf-filter-card wechat-list-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>快捷操作</span>
			</div>
			<div class="wechat-list-page__filter-body">
				<div class="wechat-list-page__filter-actions">
					<el-button size="mini" type="primary" plain icon="document" @click="refreshCurrent">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 + tabs -->
		<div class="nf-table-card wechat-list-page__table">
		<el-tabs v-model="activeName" @tab-click="handleClick" class="wechat-list-page__tabs">
			<el-tab-pane label="基础配置" name="first">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('wechatList:update')"
								@click="amendone(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="wechat-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.length">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="模板配置" name="second">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('wechatList:update')"
								@click="amendone(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="wechat-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.length">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="菜单配置" name="third">
				<div class="public-account-management clearfix">
					<!--左边配置菜单-->
					<div class="left">
						<div class="menu clearfix">
							<div class="menu_bottom" v-for="(item, i) of menu.button" :key="i">
								<!-- 一级菜单 -->
								<div @click="menuFun(i,item)" class="menu_item" :class="{'active': isActive == i}">
									{{item.name}}</div>
								<!--以下为二级菜单-->
								<div class="submenu" v-if="isSubMenuFlag == i">
									<div class="subtitle" v-for="(subItem, k) in item.sub_button" :key="k">
										<div class="menu_subItem" :class="{'active': isSubMenuActive == i + '' +k}"
											@click="subMenuFun(item, subItem, i, k)">{{subItem.name}}</div>
									</div>
									<!--  二级菜单加号， 当长度 小于  5 才显示二级菜单的加号  -->
									<div class="menu_bottom menu_addicon" v-if="item.sub_button.length < 5"
										@click="addSubMenu(item)"><i class="el-icon-plus"></i></div>
								</div>
							</div>
							<!-- 一级菜单加号 -->
							<div class="menu_bottom menu_addicon" v-if="menuKeyLength < 3" @click="addMenu"><i
									class="el-icon-plus"></i></div>
						</div>
					</div>
					<!--右边配置-->
					<div v-if="!showRightFlag" class="right">
						<div class="configure_page">
							<div class="delete_btn">
								<el-button size="mini" type="danger" icon="el-icon-delete"
									v-if="isAuth('wechatList:delete')" @click="deleteMenu(tempObj)">删除当前菜单</el-button>
							</div>
							<div>
								<span>菜单名称：</span>
								<el-input class="input_width" v-model="tempObj.name" placeholder="请输入菜单名称"
									clearable></el-input>
							</div>

							<div>
								<div class="menu_content">
									<span>菜单内容：</span>
									<el-radio-group v-model="tempObj.type">
										<el-radio :label="'view'">跳转链接</el-radio>
										<el-radio :label="'click'">发送关键词</el-radio>
										<el-radio :label="'miniprogram'">小程序</el-radio>
									</el-radio-group>
								</div>
								<div class="configur_content">
									<div v-if="tempObj.type == 'view'">
										<span>跳转链接：</span>
										<el-input class="input_width" v-model="tempObj.url" placeholder="请输入链接"
											clearable></el-input>
									</div>
									<div v-if="tempObj.type == 'click'">
										<div>
											<span>关键词：</span>
											<el-input class="input_width" v-model="tempObj.key" placeholder="请输入关键词"
												clearable></el-input>
										</div>
										<p class="blue">tips:这里需要配合关键词推送消息一起使用</p>
									</div>
									<div v-if="tempObj.type == 'miniprogram'">
										<div class="applet">
											<span>小程序的默认url：</span>
											<el-input class="input_width" v-model="tempObj.url" placeholder="请输入小程序的url"
												clearable></el-input>
										</div>
										<div class="applet">
											<span>小程序的appid：</span>
											<el-input class="input_width" v-model="tempObj.appid"
												placeholder="请输入小程序的appid" clearable></el-input>
										</div>
										<div>
											<span>小程序的页面路径：</span>
											<el-input class="input_width" v-model="tempObj.pagepath"
												placeholder="请输入小程序的页面路径，如：pages/index" clearable></el-input>
										</div>
										<p class="blue">tips:需要和公众号进行关联才可以把小程序绑定带微信菜单上哟！</p>
									</div>
								</div>
							</div>


						</div>
						<!-- <div>menu对象值：{{menu}}</div> -->
						<el-button class="save_btn" type="primary" v-if="isAuth('wechatList:save')"
							@click="saveFun">保存并发布至菜单</el-button>
					</div>
					<!--一进页面就显示的默认页面，当点击左边按钮的时候，就不显示了-->
					<div v-if="showRightFlag" class="right">
						<p>请选择菜单配置</p>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="关键字配置" name="fourth">
				<div class="wechat-list-page__toolbar">
					<el-button :disabled="!isAuth('wechatList:add')" size="mini" type="primary"
						icon="document" @click="addNotice">添加关键字</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="keywordData">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="lable" label="关键字">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('wechatList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('wechatList:delete')"
								@click="deletes(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="wechat-list-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="keywordData.length">
					</el-pagination>
				</div>
				<!-- 添加关键字 -->
				<el-dialog title="添加关键字" :visible.sync="dialogFormVisible" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">关键字：</span>
						<el-input style="width:50%;" v-model="lable" placeholder="请输入关键字"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span
							style="width: 200px;display: inline-block;text-align: right;position: relative;top: -90px;">内容：</span>
						<el-input style="width: 50%;" type="textarea" :rows="5" v-model="value"
							placeholder="请输入内容"></el-input>
					</div>
					<div slot="footer" class="dialog-footer" style="text-align: center;">
						<el-button @click="dialogFormVisible = false">取 消</el-button>
						<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
				<!-- 修改关键字弹框 -->
				<el-dialog title="修改" :visible.sync="dialogFormVisible2" center>
					<el-form :model="form">
						<el-form-item label="关键字：" :label-width="formLabelWidth">
							<el-input v-model="form.lable" style="width:65%;"></el-input>
						</el-form-item>
						<el-form-item label="内容：" :label-width="formLabelWidth">
							<el-input v-model="form.value" style="width:65%;" type="textarea" :rows="5"></el-input>
						</el-form-item>
					</el-form>
					<div slot="footer" class="dialog-footer" style="text-align: center;">
						<el-button @click="dialogFormVisible2 = false">取 消</el-button>
						<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane>
		</el-tabs>
		</div>
		<!-- 修改公告弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<el-form :model="formnone">
				<el-form-item label="配置类型：" :label-width="formLabelWidth">
					<el-input v-model="formnone.min" style="width:65%;" readonly></el-input>
				</el-form-item>
				<el-form-item label="内容：" :label-width="formLabelWidth">
					<el-input v-model="formnone.value" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer" style="text-align: center;">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendoneNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				lable: '',
				min: '',
				value: '',
				state: '',
				id: '',
				deployname: 'weixin',
				activeName: '',
				accesstoken: '',
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				form: {
					id: '',
					lable: '',
					value: '',
				},
				formnone: {
					id: '',
					min: '',
					value: '',
				},
				form2: {
					menuAdd: ''
				},
				keywordData: [],
				tableData: [],
				menuData: [],
				menuAdd: '',
				checkBoxData: [], //多选框选择的值
				showRightFlag: true, //右边配置显示默认详情还是配置详情
				menu: {
					// // 一级菜单
					button: [{
							type: 'view',
							name: '领券中心',
							url: 'https://www.gomyorder.cn/?invitation=2242941210',
							sub_button: [],
						},
						{
							type: 'view',
							name: '拼多多券',
							url: 'https://mobile.yangkeduo.com/duo_cms_mall.html?pid=8948958_104234125&cpsSign=CM_190809_8948958_104234125_05e68780da621fd572b728c3ae1ee027&range_items=1%3A0%3A10000%2C2%3A10%3A1000&duoduo_type=2',
							sub_button: [],
						},
						{
							name: '会员中心',
							sub_button: [{
									type: 'view',
									name: '个人中心',
									url: 'https://www.gomyorder.cn/pages/member/user',
									sub_button: [],
								},
								{
									type: 'view',
									name: '新人攻略',
									url: 'https://www.gomyorder.cn/help/news.html',
									sub_button: [],
								},
								{
									type: 'view',
									name: '联系客服',
									url: 'https://www.gomyorder.cn/custom.jpg',
									sub_button: [],
								},
							]
						}
					]
				}, //横向菜单
				isActive: -1, // 一级菜单点中样式
				isSubMenuActive: -1, // 一级菜单点中样式
				isSubMenuFlag: -1, // 二级菜单显示标志
				tempObj: {}, //右边临时变量，作为中间值牵引关系
				tempSelfObj: {}
			}
		},
		methods: {
			refreshCurrent() {
				if (this.activeName === 'first' || this.activeName === 'second') {
					this.dataSelect()
				} else if (this.activeName === 'third') {
					this.menuSelect()
				} else if (this.activeName === 'fourth') {
					this.keywordSelect()
				}
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect();
			},
			handleClick(tab, event) {
				if (tab._props.label == '基础配置') {
					this.page = 1
					this.size = 5
					this.deployname = 'weixin'
					this.dataSelect()
				}
				if (tab._props.label == '模板配置') {
					this.page = 1
					this.size = 5
					this.deployname = 'xiaoxi'
					this.dataSelect()
				}
				if (tab._props.label == '菜单配置') {
					this.page = 1
					this.size = 5
					this.menuSelect()
				}
				if (tab._props.label == '关键字配置') {
					this.page = 1
					this.size = 5
					this.keywordSelect()
				}
			},
			// 添加
			addNotice() {
				this.dialogFormVisible = true
			},
			// 添加关键字
			releasNoticeTo() {
				if (this.lable == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '关键字不能为空',
						type: 'warning'
					});
					return
				}
				if (this.value == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '内容不能为空',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/wx/saveWord'),
					method: 'post',
					data: this.$http.adornData({
						'lable': this.lable,
						'value': this.value,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.keywordSelect()
						}
					})
				})
			},
			// 修改配置弹框
			amendone(index, rows) {
				this.dialogFormVisible1 = true;
				this.formnone.id = rows.id;
				this.formnone.type = rows.type
				this.formnone.min = rows.min
				this.formnone.value = rows.value;
			},
			// 修改配置
			amendoneNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/common/save'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formnone.id,
						'type': this.formnone.type,
						'value': this.formnone.value,
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 修改弹框
			amend(index, rows) {
				this.dialogFormVisible2 = true;
				this.form.id = rows.id;
				this.form.lable = rows.lable
				this.form.value = rows.value;
			},
			// 修改商品类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/wx/saveWord'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'lable': this.form.lable,
						'value': this.form.value,
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.keywordSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 删除
			deletes(row) {
				let id = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/wx/deleteById/${id}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = '';
								this.keywordSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(`/common/type/condition/${this.deployname}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			// 获取关键字列表
			keywordSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/wx/getWordList'),
					method: 'post',
					params: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.keywordData = returnData
					}
				})
			},
			// 获取菜单列表
			menuSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/wx/token'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.accesstoken = data
					this.$http({
						url: this.$http.adornUrl3('/get'),
						method: 'get',
						params: this.$http.adornParams({
							'access_token': this.accesstoken
						})
					}).then(({
						data
					}) => {
						this.tableDataLoading = false
						let returnData = data.menu;
						if (returnData == '') {
							this.menuAdd = JSON.stringify(this.menu, null, 4)
							this.menu.button = this.menu.button;
						} else {
							this.menuAdd = JSON.stringify(returnData, null, 4)
							this.menu.button = returnData.button;
						}
					})
				})
			},
			// 素材内容弹框的选择按钮函数
			handleEdit(index, row) {
				this.visible2 = false;
				this.tempObj.media_id = row.name;
			},
			// 保存并发布至菜单
			saveFun() {
				console.log(this.menu);
				this.$http({
					url: this.$http.adornUrl2('/wx/token'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.accesstoken = data
					this.$http({
						url: this.$http.adornUrl3(`/create?access_token=${this.accesstoken}`),
						method: 'post',
						data: this.menu
					}).then(({
						data
					}) => {
						if (data.errcode == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.menuSelect()
								}
							})
						}
					}).catch(() => {
						this.$message({
							message: '操作失败',
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.menuSelect()
							}
						})
					})
				})
			},
			// 一级菜单点击事件
			menuFun(i, item) {
				this.showRightFlag = false; //右边菜单隐藏
				console.log(i);
				this.tempObj = item; //这个如果放在顶部，flag会没有。因为重新赋值了。
				this.tempSelfObj.grand = "1"; //表示一级菜单
				this.tempSelfObj.index = i; //表示一级菜单索引

				this.isActive = i; //一级菜单选中样式
				this.isSubMenuFlag = i; //二级菜单显示标志
				this.isSubMenuActive = -1; //二级菜单去除选中样式

			},
			// 二级菜单点击事件
			subMenuFun(item, subItem, index, k) {
				this.showRightFlag = false; //右边菜单隐藏

				this.tempObj = subItem; //将点击的数据放到临时变量，对象有引用作用

				this.tempSelfObj.grand = "2"; //表示二级菜单
				this.tempSelfObj.index = index; //表示一级菜单索引
				this.tempSelfObj.secondIndex = k; //表示二级菜单索引

				this.isSubMenuActive = index + "" + k; //二级菜单选中样式
				this.isActive = -1; //一级菜单去除样式
			},
			// 添加横向一级菜单
			addMenu() {
				// 先判断1，再判断2,对象增加，会进行往下计算，所以必须先判断2，再判断1
				if (this.menuKeyLength == 2) {
					this.$set(this.menu.button, "2", {
						// type: "",
						name: "菜单3",
						// url: "",//跳转链接
						// media_id:"",//素材名称--图文消息
						sub_button: []
					});
				}
				if (this.menuKeyLength == 1) {
					this.$set(this.menu.button, "1", {
						// type: "",
						name: "菜单2",
						// url: "",//跳转链接
						// media_id:"",//素材名称--图文消息
						sub_button: []
					});
				}
			},
			// 添加横向二级菜单
			addSubMenu(item) {
				let subMenuKeyLength = item.sub_button.length; //获取二级菜单key长度
				if (subMenuKeyLength == 4) {
					this.$set(item.sub_button, "4", {
						// type: "",
						name: "子菜单5",
						// url: "",//跳转链接
						// media_id:"",//素材名称--图文消息
					});
				}
				if (subMenuKeyLength == 3) {
					this.$set(item.sub_button, "3", {
						// type: "",
						name: "子菜单4",
						// url: "",//跳转链接
						// media_id:"",//素材名称--图文消息
					});
				}
				if (subMenuKeyLength == 2) {
					this.$set(item.sub_button, "2", {
						// type: "",
						name: "子菜单3",
						// url: "",//跳转链接
						// media_id:"",//素材名称--图文消息
					});
				}
				if (subMenuKeyLength == 1) {
					this.$set(item.sub_button, "1", {
						// type: "",
						name: "子菜单2",
						// url: "",//跳转链接
						// media_id:"",//素材名称--图文消息
					});
				}
				if (subMenuKeyLength == 0) {
					this.$set(item.sub_button, "0", {
						// type: "",
						name: "子菜单1",
						// url: "",//跳转链接
						// media_id:"",//素材名称--图文消息
					});
				}
			},
			//删除当前菜单
			deleteMenu(obj) {
				console.log(obj);
				var _this = this;
				this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					_this.deleteData(); // 删除菜单数据
				}).catch(() => {

				});
			},
			// 删除菜单数据
			deleteData() {
				// 一级菜单的删除方法
				if (this.tempSelfObj.grand == "1") {
					this.menu.button.splice(this.tempSelfObj.index, 1);
				}
				// 二级菜单的删除方法
				if (this.tempSelfObj.grand == "2") {
					this.menu.button[this.tempSelfObj.index].sub_button.splice(this.tempSelfObj.secondIndex, 1);
				}
				this.$message({
					type: 'success',
					message: '删除成功!'
				});
			}
		},
		computed: {
			// menuObj的长度，当长度 小于  3 才显示一级菜单的加号
			menuKeyLength: function() {
				return this.menu.button.length;
			}
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped="scoped">
.wechat-list-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.wechat-list-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.wechat-list-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.wechat-list-page__tabs {
	margin-bottom: 0;
}

.wechat-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.wechat-list-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	justify-content: flex-end;
	gap: 8px;
	margin-bottom: 12px;
}

.wechat-list-page__table .el-table {
	width: 100%;
}

.wechat-list-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.wechat-list-page__table .el-table td,
.wechat-list-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.wechat-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.wechat-list-page__filter-actions .el-button--primary:not(.is-plain),
.wechat-list-page__toolbar .el-button--primary:not(.is-plain),
.wechat-list-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.wechat-list-page__filter-actions .el-button--primary:not(.is-plain):hover,
.wechat-list-page__filter-actions .el-button--primary:not(.is-plain):focus,
.wechat-list-page__toolbar .el-button--primary:not(.is-plain):hover,
.wechat-list-page__toolbar .el-button--primary:not(.is-plain):focus,
.wechat-list-page__table .el-button--primary:not(.is-plain):hover,
.wechat-list-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.eit {
	height: 120px;
}

/* 公共颜色变量 */
.clearfix {
	*zoom: 1;
}

.clearfix::after {
	content: "";
	display: table;
	clear: both;
}

.wechat-list-page .public-account-management div {
	text-align: left;
}

.public-account-management {
	min-width: 1200px;
	width: 1200px;
	margin: 0 auto;
	/*右边菜单内容*/
}

.public-account-management .left {
	float: left;
	display: inline-block;
	width: 350px;
	height: 800px;
	background: url("~@/assets/iphone_backImg.png") no-repeat;
	background-size: 100% auto;
	padding: 581px 25px 88px;
	position: relative;
	box-sizing: border-box;
	/*第一级菜单*/
}

.public-account-management .left .menu {
	/*第二级菜单*/
}

.public-account-management .left .menu .menu_bottom {
	position: relative;
	float: left;
	display: inline-block;
	box-sizing: border-box;
	width: 100px;
	text-align: center;
	background-color: #e2e2e2;
	border: 1px solid #ebedee;
	cursor: pointer;
}

.public-account-management .left .menu .menu_bottom.menu_addicon {
	height: 46px;
	line-height: 46px;
}

.public-account-management .left .menu .menu_bottom .menu_item {
	height: 44px;
	line-height: 44px;
	text-align: center;
	box-sizing: border-box;
}

.public-account-management .left .menu .menu_bottom .menu_item.active {
	border: 1px solid var(--nf-brand, #15cad2);
}

.public-account-management .left .menu .menu_bottom .menu_subItem {
	height: 44px;
	line-height: 44px;
	text-align: center;
	box-sizing: border-box;
}

.public-account-management .left .menu .menu_bottom .menu_subItem.active {
	border: 1px solid var(--nf-brand, #15cad2);
}

.public-account-management .left .menu i {
	color: var(--nf-brand, #15cad2);
}

.public-account-management .left .menu .submenu {
	position: absolute;
	width: 100px;
	bottom: 45px;
}

.public-account-management .left .menu .submenu .subtitle {
	background-color: #e8e7e7;
	box-sizing: border-box;
	margin-bottom: 2px;
}

.public-account-management .right .save_btn {
	position: absolute;
	bottom: 20px;
	left: 40%;
}

.public-account-management .right {
	float: left;
	width: -webkit-calc(100% - 370px);
	width: -moz-calc(100% - 370px);
	width: calc(100% - 370px);
	position: relative;
	background-color: #e8e7e7;
	padding: 25px 10px 0px 20px;
	height: 710px;
	margin-left: 20px;
	-webkit-box-sizing: border-box;
	box-sizing: border-box;
}

.public-account-management .right .configure_page .delete_btn {
	text-align: right;
	margin-bottom: 15px;
}

.public-account-management .right .configure_page .menu_content {
	margin-top: 20px;
}

.public-account-management .right .configure_page .configur_content {
	margin-top: 20px;
}

.public-account-management .right .configure_page .blue {
	color: var(--nf-brand, #15cad2);
	margin-top: 10px;
}

.public-account-management .right .configure_page .applet {
	margin-bottom: 20px;
}

.public-account-management .right .configure_page .applet span {
	margin-right: 18px;
}

.public-account-management .right .configure_page .material .input_width {
	width: 30%;
}

.public-account-management .right .configure_page .material .el-textarea {
	width: 80%;
}

.public-account-management .el-input {
	width: 78%;
}

.public-account-management .material .el-button {
	float: right;
}
</style>