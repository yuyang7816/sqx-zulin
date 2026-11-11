<template>
	<div class="finance-page">
		<!-- hero -->
		<div class="nf-page-hero finance-page__hero">
			<span class="nf-page-hero__badge">财务中心</span>
			<h2 class="nf-page-hero__title">财务中心</h2>
			<p class="nf-page-hero__desc">管理提现申请与转账，查看提现/支付统计数据及支付记录明细</p>
		</div>

		<!-- tabs 零外距 + 内容白卡 -->
		<div class="nf-table-card finance-page__content">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="finance-page__tabs">
				<!-- 提现管理 -->
				<el-tab-pane label="提现管理" name="first">
					<div class="finance-page__tab-filter">
						<div class="finance-page__filter-body">
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">支付宝名称</label>
								<div class="finance-page__search-wrap">
									<el-input class="finance-page__filter-control" @keydown.enter.native="select"
										placeholder="请输入支付宝名称查询" v-model="zhifubaoName"></el-input>
									<span class="finance-page__search-icon" @click="select">
										<icon-svg name="shousuo" class="site-sidebar__menu-icon"></icon-svg>
									</span>
								</div>
							</div>
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">会员编号</label>
								<div class="finance-page__search-wrap">
									<el-input class="finance-page__filter-control" @keydown.enter.native="select"
										placeholder="请输入会员编号查询" v-model="relationId"></el-input>
									<span class="finance-page__search-icon" @click="select">
										<icon-svg name="shousuo" class="site-sidebar__menu-icon"></icon-svg>
									</span>
								</div>
							</div>
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">状态</label>
								<el-select v-model="status" class="finance-page__filter-control" placeholder="请选择状态"
									@change="select(status)">
									<el-option v-for="item in statusmain" :key="item.value" :label="item.label"
										:value="item.value">
									</el-option>
								</el-select>
							</div>
							<div class="finance-page__filter-actions">
								<el-button size="mini" type="primary" icon="document" @click="transferClcik()"
									:disabled="checkBoxData.length <= 0 || !isAuth('financeList:accounts')">
									批量转账
								</el-button>
								<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置
								</el-button>
								<el-button size="mini" type="primary" plain icon="document" @click="refresh">刷新
								</el-button>
							</div>
						</div>
					</div>
					<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData">
						<el-table-column type="selection">
						</el-table-column>
						<el-table-column fixed prop="id" label="编号" width="80">
						</el-table-column>
						<el-table-column fixed prop="relationId" label="会员编号" width="120">
							<template slot-scope="scope">
								<span class="finance-page__link"
									@click="updates(scope.row)">{{scope.row.relationId}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="zhifubao" label="支付宝账号" width="150">
						</el-table-column>
						<el-table-column prop="zhifubaoName" label="姓名" width="100">
						</el-table-column>
						<el-table-column prop="money" label="提现金额" width="100">
						</el-table-column>
						<el-table-column prop="state" label="状态" width="100">
							<template slot-scope="scope">
								<span class="finance-page__link" v-if="scope.row.state == 0">待转账</span>
								<span v-if="scope.row.state == 1">已转账</span>
								<span v-if="scope.row.state == -1">已拒绝</span>
							</template>
						</el-table-column>
						<el-table-column prop="refund" label="拒绝原因" width="220">
						</el-table-column>
						<el-table-column prop="createAt" label="申请时间" width="170">
						</el-table-column>
						<el-table-column prop="outAt" label="转账/拒绝时间" width="180">
						</el-table-column>
						<el-table-column prop="orderNumber" label="转账订单号" width="150">
						</el-table-column>
						<el-table-column fixed="right" label="操作" width="150">
							<template slot-scope="scope">
								<el-button size="mini" type="primary"
									:disabled="!isAuth('financeList:accounts')||scope.row.state != 0"
									@click="batch(scope.row)">
									转账
								</el-button>
								<el-button size="mini" type="primary"
									:disabled="!isAuth('financeList:refund')||scope.row.state != 0"
									@click="refund(scope.row)">
									拒绝
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="finance-page__pagination">
						<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="totalnum">
						</el-pagination>
					</div>
				</el-tab-pane>

				<!-- 提现统计 -->
				<el-tab-pane label="提现统计" name="third">
					<div class="finance-page__tab-filter">
						<div class="finance-page__filter-body finance-page__filter-body--inline">
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">查询维度</label>
								<el-select v-model="flag" class="finance-page__filter-control" @change="animeDat">
									<el-option v-for="item in flags" :key="item.value" :label="item.label"
										:value="item.value">
									</el-option>
								</el-select>
							</div>
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">日期</label>
								<el-date-picker class="finance-page__filter-control" v-model="info.stockDate"
									align="right" type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
									placeholder="选择开始时间" @change="timeDate">
								</el-date-picker>
							</div>
						</div>
					</div>
					<div class="finance-stat-grid">
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">累计提现金额</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.sumMoney ? withdrawData.sumMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">累计提现笔数</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.countMoney ? withdrawData.countMoney : 0}}</span>
								<span class="finance-stat-tile__unit">笔</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">待转账金额</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.daiTXMoney ? withdrawData.daiTXMoney :0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">待转账提现笔数</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.stayMoney ? withdrawData.stayMoney : 0}}</span>
								<span class="finance-stat-tile__unit">笔</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">已转账金额</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.yiTXMoney ? withdrawData.yiTXMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">已提现笔数</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.yiTXCount ? withdrawData.yiTXCount : 0}}</span>
								<span class="finance-stat-tile__unit">笔</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">微信已提现金额</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.weiXinMoney ? withdrawData.weiXinMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">微信累计提现次数</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.weiXinXCXMoney ? withdrawData.weiXinXCXMoney : 0}}</span>
								<span class="finance-stat-tile__unit">笔</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">支付宝已提现金额</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.zfbMoney ? withdrawData.zfbMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">支付宝累计提现次数</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.zfbMoneyCount ? withdrawData.zfbMoneyCount : 0}}</span>
								<span class="finance-stat-tile__unit">笔</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">申请提现金额</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.applyMoney ? withdrawData.applyMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">拒绝提现金额</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.refuseMoney ? withdrawData.refuseMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">拒绝提现笔数</div>
							<div class="finance-stat-tile__value">
								<span>{{withdrawData.refuseMoneyCount ? withdrawData.refuseMoneyCount : 0}}</span>
								<span class="finance-stat-tile__unit">笔</span>
							</div>
						</div>
					</div>
				</el-tab-pane>

				<!-- 支付统计 -->
				<el-tab-pane label="支付统计" name="four">
					<div class="finance-page__tab-filter">
						<div class="finance-page__filter-body finance-page__filter-body--inline">
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">查询维度</label>
								<el-select v-model="flag" class="finance-page__filter-control" @change="animeDat">
									<el-option v-for="item in flags" :key="item.value" :label="item.label"
										:value="item.value">
									</el-option>
								</el-select>
							</div>
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">日期</label>
								<el-date-picker class="finance-page__filter-control" v-model="info.stockDate"
									align="right" type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
									placeholder="选择开始时间" @change="timeDate">
								</el-date-picker>
							</div>
						</div>
					</div>
					<div class="finance-stat-grid">
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">支付总金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.allMoney ? chonzhiData.allMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">app微信支付金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.appMoney ? chonzhiData.appMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">微信公众号支付金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.wxRwmMoney ? chonzhiData.wxRwmMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">微信小程序支付金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.wxAppMoney ? chonzhiData.wxAppMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">app支付宝支付金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.zfbMoney ? chonzhiData.zfbMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">支付宝H5支付金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.zfbH5Money ? chonzhiData.zfbH5Money : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">零钱支付金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.lqMoney ? chonzhiData.lqMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">系统支付金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.sysMoney ? chonzhiData.sysMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
						<div class="finance-stat-tile">
							<div class="finance-stat-tile__label">已退款金额</div>
							<div class="finance-stat-tile__value">
								<span>{{chonzhiData.refundMoney ? chonzhiData.refundMoney : 0}}</span>
								<span class="finance-stat-tile__unit">元</span>
							</div>
						</div>
					</div>
				</el-tab-pane>

				<!-- 支付记录 -->
				<el-tab-pane label="支付记录" name="fourth">
					<div class="finance-page__tab-filter">
						<div class="finance-page__filter-body">
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">开始时间</label>
								<el-date-picker class="finance-page__filter-control" v-model="startTime" align="right"
									type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
									placeholder="选择开始时间">
								</el-date-picker>
							</div>
							<div class="finance-page__filter-item">
								<label class="finance-page__filter-label">截止时间</label>
								<el-date-picker class="finance-page__filter-control" v-model="endTime" align="right"
									type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd"
									placeholder="选择截止时间">
								</el-date-picker>
							</div>
							<div class="finance-page__filter-actions">
								<el-button size="mini" type="primary" icon="document" @click="timeDate2">查询
								</el-button>
								<el-button size="mini" type="primary" plain icon="document" @click="refresh1">刷新
								</el-button>
							</div>
						</div>
					</div>
					<el-table v-loading="tableDataLoading" :data="rechargeData">
						<el-table-column fixed prop="id" label="编号" width="80">
						</el-table-column>
						<el-table-column prop="userName" label="用户名称">
							<template slot-scope="scope">
								<span class="finance-page__link"
									@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
							</template>
						</el-table-column>
						<el-table-column prop="orderId" label="支付订单号" width="250">
						</el-table-column>
						<el-table-column prop="money" label="支付金额" width="100">
						</el-table-column>
						<el-table-column prop="userId" label="会员编号" width="100">
						</el-table-column>
						<el-table-column label="分类" width="120">
							<template slot-scope="scope">
								<span class="finance-page__link" v-if="scope.row.classify == 1">微信APP</span>
								<span class="finance-page__link" v-if="scope.row.classify == 2">微信公众号</span>
								<span class="finance-page__link" v-if="scope.row.classify == 3">微信小程序</span>
								<span class="finance-page__link" v-if="scope.row.classify == 4">支付宝APP</span>
								<span class="finance-page__link" v-if="scope.row.classify == 5">支付宝H5</span>
								<span class="finance-page__link" v-if="scope.row.classify == 6">零钱</span>
							</template>
						</el-table-column>
						<el-table-column label="状态" width="120">
							<template slot-scope="scope">
								<span class="finance-page__link" v-if="scope.row.state == 0">待支付</span>
								<span class="finance-page__link" v-if="scope.row.state == 1">支付成功</span>
								<span class="finance-page__link" v-if="scope.row.state == 2">支付失败</span>
							</template>
						</el-table-column>
						<el-table-column prop="createTime" label="创建时间" width="170">
						</el-table-column>
						<el-table-column prop="payTime" label="支付时间">
						</el-table-column>
					</el-table>
					<div class="finance-page__pagination">
						<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
							:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
							layout="total,sizes, prev, pager, next" :total="totalnum2">
						</el-pagination>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 拒绝弹框 -->
		<el-dialog title="拒绝" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="拒绝原因：" :label-width="formLabelWidth">
					<el-input v-model="form.content" type="textarea" rows="4" placeholder="请输入拒绝原因"
						style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refundNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				size: 10,
				balance: -1,
				zhifubaoName: '',
				tkstatus: -1,
				platform: 'all',
				phone: '',
				content: '',
				startTime: '',
				endTime: '',
				cashId: '',
				type: -1,
				totalnum: 0,
				totalnum2: 0,
				sqxTotal: '',
				userTotal: '',
				taunTotal: '',
				relationId: '',
				sumMoneyByTime: '',
				activeName: 'first',
				formLabelWidth: '200px',
				dialogFormVisible: false,
				tableDataLoading: false,
				tableData: [],
				rechargeData: [],
				withdrawData: {},
				withdrawData1:{},
				chonzhiData: {},
				checkBoxData: [], //多选框选择的值
				way: 3,
				flag:1,
				form: {
					cashId: '',
					content: '',
				},
				item: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				platforms: [{
					value: 'all',
					label: '全部'
				}, {
					value: 'tb',
					label: '淘宝'
				}, {
					value: 'jd',
					label: '京东'
				}, {
					value: 'pdd',
					label: '拼多多'
				}],
				ways: [{
					value: 3,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 1,
					label: '按年查询'
				}],
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				statusmain: [{
						value: -2,
						label: '全部'
					},
					{
						value: 0,
						label: '待转账'
					},
					{
						value: 1,
						label: '已转账'
					},
					{
						value: -1,
						label: '已拒绝'
					}
				],
				status: -2
			}
		},
		methods: {
			// 会员详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 批量转账
			transferClcik(id) {
				let _this = this
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				for (var i in ids) {
					_this.$http({
						url: _this.$http.adornUrl2(`/cash/v1/alipay/${ids[i]}`),
						method: 'post',
						data: _this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							_this.$notify({
								title: '提示',
								duration: 1800,
								message: data.data,
								type: 'warning'
							})
						} else {
							_this.$notify({
								title: '提示',
								duration: 1800,
								message: data.msg,
								type: 'error'
							})
						}

					})
				}
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			cleans() {
				this.page = 1
				this.zhifubaoName = ''
				this.relationId = ''
				this.status = -2
				this.dataSelect()
			},
			// 结算
			close() {
				this.$http({
					url: this.$http.adornUrl2('/cash/userToTalMoney'),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			//转账
			batch(row) {
				let cashId = row.id
				if (row.state == 1) {
					this.$message({
						message: '已转账，请勿重复操作！',
						type: 'error',
						duration: 2500
					})
					return
				}
				this.$confirm(`确定转账?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/cash/v1/alipay/${cashId}`),
						method: 'post',
						data: this.$http.adornData(cashId, false)
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: data.data,
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
						if (data.status == 9999) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 2500
							})
							return
						}
					})
				})

			},
			// 拒绝
			refund(row) {
				if (row.state == -1) {
					this.$message({
						message: '已拒绝，请勿重复操作！',
						type: 'error',
						duration: 2500
					})
				} else {
					this.dialogFormVisible = true
					this.form.cashId = row.id
				}
			},
			// 拒绝操作
			refundNoticeTo() {
				if (this.form.content == '') {
					this.$message({
						message: '请输入拒绝原因！',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl2(`/cash/v1/refund/${this.form.cashId}/${this.form.content}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
					if (data.status == -100) {
						this.dialogFormVisible = false
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 2500,
							onClose: () => {
								this.form.content = ''
							}
						})
						return
					}
				})
			},
			timeDate() {
				this.withdrawSelect()
				this.chonzhiSelect()
			},
			timeDate2() {
				this.rechargeSelect()
			},
			// 日期选择
			animeDat(value) {
				let vanumber = value
				if (vanumber === 3) {
					this.withdrawSelect()
					this.chonzhiSelect()
				}
				if (vanumber === 2) {
					this.item = this.info.stockDate
					this.info.stockDate = this.info.stockDate.substring(0, 7)
					this.withdrawSelect()
					this.chonzhiSelect()
					this.info.stockDate = this.item
				}
				if (vanumber === 1) {
					this.item = this.info.stockDate
					this.info.stockDate = this.info.stockDate.substring(0, 4)
					this.withdrawSelect()
					this.chonzhiSelect()
					this.info.stockDate = this.item
				}
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
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
				this.rechargeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.rechargeSelect()
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			// 刷新
			refresh1() {
				this.rechargeSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '提现管理') {
					this.page = 1
					this.size = 10
					this.dataSelect()
					// this.withdrawSelect()
				}
				if (tab._props.label == '提现统计') {
					this.flag = 1
					this.withdrawSelect()
				}
				if (tab._props.label == '支付统计') {
					this.flag = 1
					this.chonzhiSelect()
				}

				if (tab._props.label == '支付记录') {
					this.page = 1
					this.size = 10
					this.rechargeSelect()
				}
			},
			// 支付宝账号查询
			zfbselect() {
				if (this.zhifubaoName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入支付宝名称',
						type: 'error'
					})
					return
				} else {
					this.tableDataLoading = true
					this.$http({
						url: this.$http.adornUrl2(`/cash/phone/${this.zhifubao}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
						this.totalnum = returnData.length
					})
				}
			},
			// 渠道账号查询
			qdselect() {
				if (this.relationId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入渠道账号',
						type: 'error'
					})
					return
				} else {
					this.tableDataLoading = true
					this.$http({
						url: this.$http.adornUrl2(`/cash/list/${this.relationId}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
						this.totalnum = returnData.length
					})
				}
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/cash/selectCashList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'zhifubaoName': this.zhifubaoName,
						'relationId': this.relationId,
						'state': this.status
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			// 提现统计
			withdrawSelect() {
				this.$http({
					url: this.$http.adornUrl2('/cash/statisticsCashMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info.stockDate,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					if(returnData.stayMoney){
						returnData.stayMoney = parseFloat(returnData.stayMoney).toFixed(2)
					}
					if(returnData.sumMoney){
						returnData.sumMoney = parseFloat(returnData.sumMoney).toFixed(2)
					}
					this.withdrawData = returnData
					
				})
			},

			// 支付统计
			chonzhiSelect() {
				this.$http({
					url: this.$http.adornUrl2('/user/payMessage'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info.stockDate,
						'type': this.flag
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.chonzhiData = returnData
				})
			},
			// 获取支付记录
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(
						`/cash/selectPayDetails?page=${this.page}&limit=${this.size}&startTime=${this.startTime}&endTime=${this.endTime}`
					),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.rechargeData = returnData.list
					this.totalnum2 = returnData.totalCount
				})
			},
			select(status) {
				this.page = 1
				this.dataSelect()
			}
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.finance-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.finance-page__tabs {
	margin-bottom: 0;
}

.finance-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.finance-page__tab-filter {
	padding: 12px 0;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	margin-bottom: 12px;
}

.finance-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.finance-page__filter-body--inline {
	align-items: flex-end;
}

.finance-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.finance-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.finance-page__filter-control {
	width: 180px;
}

.finance-page__search-wrap {
	position: relative;
	display: inline-block;
}

.finance-page__search-wrap .finance-page__filter-control {
	width: 200px;
}

.finance-page__search-icon {
	position: absolute;
	right: 10px;
	top: 50%;
	transform: translateY(-50%);
	cursor: pointer;
	display: flex;
	align-items: center;
}

.finance-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.finance-page__link {
	color: var(--nf-brand, #15cad2);
	cursor: pointer;
}

.finance-page__link:hover {
	color: var(--nf-brand-deep, #109da4);
}

.finance-page__content .el-table {
	width: 100%;
}

.finance-page__content .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.finance-page__content .el-table td,
.finance-page__content .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.finance-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.finance-stat-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
	padding-top: 4px;
}

.finance-stat-tile {
	background: var(--nf-sidebar-bg, #f7f8fa);
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: 8px;
	padding: 16px;
}

.finance-stat-tile__label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	margin-bottom: 8px;
}

.finance-stat-tile__value {
	font-size: 20px;
	font-weight: 600;
	color: var(--nf-brand, #15cad2);
	line-height: 1.2;
}

.finance-stat-tile__unit {
	font-size: 13px;
	font-weight: 400;
	color: var(--nf-text-muted, #909399);
	margin-left: 4px;
}

.finance-page__filter-actions .el-button--primary:not(.is-plain),
.finance-page__content .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.finance-page__filter-actions .el-button--primary:not(.is-plain):hover,
.finance-page__filter-actions .el-button--primary:not(.is-plain):focus,
.finance-page__content .el-button--primary:not(.is-plain):hover,
.finance-page__content .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 1200px) {
	.finance-stat-grid {
		grid-template-columns: repeat(2, 1fr);
	}
}

@media (max-width: 768px) {
	.finance-page__filter-control,
	.finance-page__search-wrap .finance-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.finance-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
