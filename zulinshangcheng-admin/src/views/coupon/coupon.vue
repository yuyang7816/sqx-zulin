<template>
	<div class="coupon-page">
		<!-- hero -->
		<div class="nf-page-hero coupon-page__hero">
			<span class="nf-page-hero__badge">优惠券管理</span>
			<h2 class="nf-page-hero__title">优惠券制作</h2>
			<p class="nf-page-hero__desc">创建与管理优惠券，支持通用券与商品券类型，可发布、修改优惠券配置</p>
		</div>

		<!-- 操作白卡 -->
		<div class="nf-filter-card coupon-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>快捷操作</span>
			</div>
			<div class="coupon-page__filter-body">
				<div class="coupon-page__filter-actions">
					<el-button :disabled="!isAuth('coupon:add')" size="mini" type="primary"
						icon="document" @click="addCoupon">添加优惠券</el-button>
					<el-button size="mini" type="primary" plain icon="document"
						@click="refresh">刷新</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card coupon-page__table">
		<el-table v-loading="tableDataLoading" :data="tableData.content">
			<el-table-column prop="couponName" label="优惠券名称" width="150">
			</el-table-column>
			<el-table-column prop="type" label="优惠券类型" width="100">
				<template slot-scope="scope">
					<span v-if="scope.row.type==1">通用券</span>
					<span style="color: #15cad2;" v-if="scope.row.type==2">商品券</span>
				</template>
			</el-table-column>
			<el-table-column prop="lessMoney" label="优惠券面值" width="100">
			</el-table-column>
			<el-table-column prop="minMoney" label="优惠券最低消费" width="150">
			</el-table-column>
			<el-table-column prop="validDay" label="优惠券有效期限(天)" width="150">
			</el-table-column>
			<el-table-column prop="sort" label="排序">
			</el-table-column>
			<el-table-column label="状态">
				<template slot-scope="scope">
					<span style="color: #15cad2;" v-if="scope.row.status == 1">开启</span>
					<span v-if="scope.row.status == 2">关闭</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="150">
			</el-table-column>
			<el-table-column label="操作" width="150">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" :disabled="!isAuth('coupon:issue')"
						@click="discuss(scope.$index,scope.row)">发布
					</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
						@click="shopUpdates(scope.$index,scope.row)">修改
					</el-button>
					<!-- <el-button
	  		        size="mini"
	  		        type="danger"
                :disabled="!isAuth('coupon:delete')"
	  		        @click="shopdeletes(scope.row)">删除
	  		      </el-button> -->
				</template>
			</el-table-column>
		</el-table>
			<div class="coupon-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
				</el-pagination>
			</div>
		</div>
		<!-- 添加优惠券-->
		<el-dialog title="添加优惠券" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券类型：</span>
				<el-radio-group v-model="type" @change="couponClick">
					<el-radio :label="1">通用券</el-radio>
					<el-radio :label="2">商品券</el-radio>
				</el-radio-group>
			</div>
			<div v-if="type==2">
				<span style="width: 200px;display: inline-block;text-align: right;">选择商品：</span>
				<div style="position: relative;left: 30%;top: -22px; width:148px;height:148px;font-size: 28px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 143px;cursor: pointer;"
					@click="shopClick()">
					<img v-if="goodsImages" :src="goodsImages" class="avatar" style="width: 138px;height: 138px;" />
					<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券名称：</span>
				<el-input style="width:50%;" v-model="couponName" placeholder="请输入优惠券名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券面值：</span>
				<el-input style="width:50%;" v-model="lessMoney" type="number" :min="0"
					placeholder="请输入优惠券面值"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券最低消费：</span>
				<el-input style="width:50%;" v-model="minMoney" type="number" :min="0"
					placeholder="请输入优惠券最低消费"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券有效期限(天)：</span>
				<el-input style="width:50%;" v-model="validDay" type="number" :min="1"
					placeholder="请输入优惠券最低消费"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="请输入排序号"></el-input>
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
		<el-dialog title="修改优惠券" :visible.sync="dialogFormVisible3" center>
			<el-form :model="form">
				<el-form-item label="优惠券类型：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.type" @change="couponClick">
						<el-radio :label="1">通用券</el-radio>
						<el-radio :label="2">商品券</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="选择商品：" :label-width="formLabelWidth" v-if="form.type==2">
					<div style=" width:148px;height:148px;font-size: 28px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;cursor: pointer;"
						@click="shopClick()">
						<img v-if="form.goodsImages" :src="form.goodsImages" class="avatar"
							style="width: 140px;height: 140px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</div>
				</el-form-item>
				<el-form-item label="优惠券名称：" :label-width="formLabelWidth">
					<el-input v-model="form.couponName" style="width:65%;" placeholder="请输入优惠券名称"></el-input>
				</el-form-item>
				<el-form-item label="优惠券面值：" :label-width="formLabelWidth">
					<el-input v-model="form.lessMoney" style="width:65%;" placeholder="请输入优惠券面值"></el-input>
				</el-form-item>
				<el-form-item label="优惠券最低消费：" :label-width="formLabelWidth">
					<el-input v-model="form.minMoney" style="width:65%;" placeholder="请输入优惠券最低消费"></el-input>
				</el-form-item>
				<el-form-item label="优惠券有效期限(天)：" :label-width="formLabelWidth">
					<el-input v-model="form.validDay" style="width:65%;" placeholder="请输入优惠券有效期限(天)"></el-input>
				</el-form-item>
				<el-form-item label="排序：" :label-width="formLabelWidth">
					<el-input v-model="form.sort" style="width:65%;" placeholder="请输入排序号"></el-input>
				</el-form-item>
				<el-form-item label="状态：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.status">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="2">关闭</el-radio>
					</el-radio-group>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible3 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 发布优惠券-->
		<el-dialog title="发布优惠券" :visible.sync="dialogFormVisible2" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券ID：</span>
				<el-input style="width:50%;" v-model="couponId" disabled></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券名称：</span>
				<el-input style="width:50%;" v-model="couponName1" disabled></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">领取开启时间：</span>
				<el-date-picker style="width:200px;" v-model="startTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择开始时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">券领结束时间：</span>
				<el-date-picker style="width:200px;" v-model="endTime" align="right" type="datetime"
					format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择结束时间">
				</el-date-picker>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布数量：</span>
				<el-input style="width:50%;" v-model="issueNumber" type="number" :min="0"
					placeholder="请输入发布数量"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">是否限量：</span>
				<el-radio-group v-model="isLimit">
					<el-radio :label="1">限量</el-radio>
					<el-radio :label="2">不限量</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">状态：</span>
				<el-radio-group v-model="status">
					<el-radio :label="1">开启</el-radio>
					<el-radio :label="2">关闭</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="discussNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 选择商品弹框 -->
		<el-dialog title="商品列表" customClass="customWidth" :visible.sync="dialogFormVisible1" center width="75%">
			<div>
				<div style="margin:2% 0;display: inline-block;">
					<span>商品标题：</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
						v-model="title"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;">
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
						@click="select">查询</el-button>
					<el-button style='margin-left:15px;' size="mini" type="primary" icon="document"
						@click="cleans">重置</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="shopData.content">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="coverImg" label="商品图片">
						<template slot-scope="scope">
							<img :src="scope.row.coverImg" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题" width="200">
					</el-table-column>
					<el-table-column prop="originalPrice" label="商品原价">
					</el-table-column>
					<el-table-column prop="price" label="商品价格">
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价格">
					</el-table-column>
					<el-table-column prop="dailyRent" label="日租金额">
					</el-table-column>
					<el-table-column prop="memberRent" label="会员日租金">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="selectClick(scope.$index,scope.row)">选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page1"
						layout="total,sizes, prev, pager, next" :total="shopData.totalElements">
					</el-pagination>
				</div>
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
				page1: 1,
				type: 1,
				title: '',
				typeId: '',
				couponName: '',
				lessMoney: 0,
				minMoney: 0,
				validDay: 1,
				sort: 0,
				status: 1,
				goodsIds: '',
				goodsImages: '',
				issueNumber: 0,
				remainNumber: '',
				isLimit: 1,
				form: {
					couponId: '',
					type: '',
					couponName: '',
					lessMoney: '',
					goodsIds: '',
					goodsImages: '',
					minMoney: '',
					validDay: '',
					sort: '',
					status: '',
					createTime: ''
				},
				coverImg: '',
				couponName1: '',
				couponId: '',
				startTime: '',
				endTime: '',
				checkBoxData: [], //多选框选择的值
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				tableData: [],
				shopData: []
			}
		},

		methods: {
			// 通用券/商品券
			couponClick(val) {
				if (val = 1) {
					this.goodsIds = ''
					this.goodsImages = ''
					this.form.goodsIds = ''
					this.form.goodsImages = ''
				}
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.shopSelect()
			},
			// 添加优惠券弹框
			addCoupon() {
				this.type = 1
				this.dialogFormVisible = true
			},
			// 添加优惠券
			couponNoticeTo() {
				if (this.couponName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入优惠券名称',
						type: 'warning'
					});
					return
				}
				if (this.type == 2 && this.goodsImages == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品',
						type: 'warning'
					});
					return
				}
				if (this.minMoney < this.lessMoney) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '优惠券金额不能高于最低消费金额',
						type: 'warning'
					});
					return
				}
				if (this.minMoney == this.lessMoney) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '优惠券金额不能高于最低消费金额',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/selfCoupon/save'),
					method: 'post',
					data: this.$http.adornData({
						'couponName': this.couponName,
						'goodsIds': this.goodsIds,
						'goodsImages': this.goodsImages,
						'lessMoney': this.lessMoney,
						'minMoney': this.minMoney,
						'sort': this.sort,
						'status': this.status,
						'type': this.type,
						'validDay': this.validDay,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.couponName = ''
							this.goodsImages = ''
							this.lessMoney = 0
							this.validDay = 1
							this.minMoney = 0
							this.sort = 0
							this.status = 1
							this.dataSelect()
						}
					})
					this.dialogFormVisible = false
				})
			},
			// 修改优惠券
			shopUpdates(index, row) {
				this.dialogFormVisible3 = true
				this.form.type = row.type
				this.form.couponId = row.couponId
				this.form.couponName = row.couponName
				this.form.lessMoney = row.lessMoney
				this.form.minMoney = row.minMoney
				this.form.sort = row.sort
				this.form.status = row.status
				this.form.validDay = row.validDay
				this.form.createTime = row.createTime
				this.form.goodsIds = row.goodsIds
				this.form.goodsImages = row.goodsImages
			},
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/selfCoupon/update'),
					method: 'post',
					data: this.$http.adornData({
						'couponId': this.form.couponId,
						'couponName': this.form.couponName,
						'goodsIds': this.form.goodsIds,
						'goodsImages': this.form.goodsImages,
						'lessMoney': this.form.lessMoney,
						'minMoney': this.form.minMoney,
						'sort': this.form.sort,
						'status': this.form.status,
						'type': this.form.type,
						'validDay': this.form.validDay,
						'createTime': this.form.createTime,
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
					this.dialogFormVisible3 = false
				})
			},
			// 发布优惠券弹框
			discuss(index, row) {
				this.dialogFormVisible2 = true
				this.couponId = row.couponId
				this.couponName1 = row.couponName
				this.goodsIds = row.goodsIds
				this.type = row.type
			},
			// 发布优惠券
			discussNoticeTo() {
				if (this.startTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择优惠券领取时间',
						type: 'warning'
					});
					return
				}
				if (this.endTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择优惠券结束时间',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/selfCouponIssue/save'),
					method: 'post',
					data: this.$http.adornData({
						'couponName': this.couponName1,
						'couponId': this.couponId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'issueNumber': this.issueNumber,
						'remainNumber': this.issueNumber,
						'isLimit': this.isLimit,
						'status': this.status,
						'type': this.type,
						'goodsIds': this.goodsIds
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.startTime = ''
							this.endTime = ''
							this.isLimit = 1
							this.status = 1
							this.issueNumber = ''
							this.dataSelect()
						}
					})
					this.dialogFormVisible2 = false
				})
			},
			// 删除优惠券
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/selfCoupon/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.couponId
						})
					}).then(({
						data
					}) => {
						if (data && data.status == 0) {
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
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}
					})
				}).catch(() => {})
			},
			//获取优惠券数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfCoupon/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			//搜索
			select() {
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.shopSelect()
			},
			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
				this.ids.splice(index, 1)
			},
			// 选择商品
			shopClick() {
				this.title = ''
				this.page1 = 1
				this.dialogFormVisible1 = true
				this.shopSelect()
			},
			// 获取商品数据
			shopSelect() {
				let page = this.page1 - 1
				// let status = 0
				// this.title = ''
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'title': this.title,
						'type': this.typeId,
						'status': status
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.shopData = returnData
				})
			},
			// 选择商品
			selectClick(index, row) {
				console.log(row)
				this.$http({
					url: this.$http.adornUrl2('/goods/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': row.id
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
					let returnData = data.data;
					this.goodsIds = returnData.id
					this.goodsImages = returnData.coverImg
					this.form.goodsIds = returnData.id
					this.form.goodsImages = returnData.coverImg
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.coupon-page {
	padding: 12px 16px;
}

.coupon-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.coupon-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
}

.coupon-page__table .el-table {
	width: 100%;
}

.coupon-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.coupon-page__table .el-table td,
.coupon-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.coupon-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

/* 列表页 primary 描边钮（非 plain 亦透明底） */
.coupon-page__filter-actions .el-button--primary:not(.is-plain),
.coupon-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.coupon-page__filter-actions .el-button--primary:not(.is-plain):hover,
.coupon-page__filter-actions .el-button--primary:not(.is-plain):focus,
.coupon-page__table .el-button--primary:not(.is-plain):hover,
.coupon-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}
</style>