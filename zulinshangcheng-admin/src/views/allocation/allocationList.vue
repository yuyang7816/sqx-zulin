<template>
	<div class="allocation-list-page">
		<div class="nf-page-hero allocation-list-page__hero">
			<span class="nf-page-hero__badge">系统配置</span>
			<h2 class="nf-page-hero__title">配置列表</h2>
			<p class="nf-page-hero__desc">管理系统全局、微信、支付宝、短信及协议等各类业务配置项，支持分模块查看与编辑</p>
		</div>

		<div class="nf-table-card allocation-list-page__table">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="allocation-list-page__tabs">
			<el-tab-pane label="全局系统配置" name="first">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<!-- <el-tab-pane label="佣金配置" name="fifth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane> -->
			<el-tab-pane label="微信配置" name="sixth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="公众号模板配置" name="sixths">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="支付宝配置" name="seventh">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="短信配置" name="eighth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="APP消息推送配置" name="tenth">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="文件上传配置" name="tenths">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="更新时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="协议配置" name="xieyi">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="积分配置" name="jifen">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="视频配置" name="video">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="开关配置" name="kaiguan">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			<el-tab-pane label="图片配置" name="image">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型" width="250">
					</el-table-column>
					<el-table-column prop="value" label="内容">
						<template slot-scope="scope">
							<div
								style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
								{{scope.row.value}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</el-tab-pane>
			</el-tabs>
		</div>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="配置类型：" :label-width="formLabelWidth">
					<el-input v-model="form.min" style="width:65%;" readonly></el-input>
				</el-form-item>
				<el-form-item label="内容：" :label-width="formLabelWidth">
					<quill-editor v-if="form.conditionFrom=='xieyi' " ref="myTextEditor" v-model="form.value"
						:options="quillOption"
						style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
					<div v-else-if="form.conditionFrom=='video'">
						<div>
							<el-upload class="upload-demo" v-model="form.value"
								:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
								:on-success="videoSuccess1" :before-upload="beforeVideoUpload"
								:on-progress="handlePreviewV">
								<el-button size="small" type="primary" v-loading="tableDataLoading5">点击上传</el-button>
								<el-input v-if="form.value" v-model="form.value" style="width:300px;">
								</el-input>
							</el-upload>
						</div>
					</div>
					<div v-else-if="form.conditionFrom=='kaiguan'">
						<div>
							<el-radio-group v-model="form.value">
								<el-radio label="是">是</el-radio>
								<el-radio label="否">否</el-radio>
							</el-radio-group>
						</div>
					</div>
					<el-upload class="avatar-uploader" v-else-if="form.conditionFrom=='image'" v-model="form.value"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="form.value" :src="form.value" class="avatar"
							style="border-radius: 6px;width:148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
					<el-input v-else v-model="form.value" style="width:65%;"></el-input>
					<!-- <el-input v-model="form.value" style="width:65%;"></el-input> -->
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				size: 10,
				page: 0,
				min: '',
				value: '',
				id: '',
				deployname: 'xitong',
				activeName: 'first',
				dialogFormVisible: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				form: {
					id: '',
					min: '',
					value: '',
					type: '',
					conditionFrom: '',
				},
				tableData: [],
				quillOption: quillConfig,
				checkBoxData: [], //多选框选择的值
				tableDataLoading5: false,
			}
		},
		methods: {
			handleSizeChange(val) {
				this.size = val;
				this.dataSelect();
			},
			handleCurrentChange(val) {
				this.page = val - 1;
				this.dataSelect();
			},
			handleClick(tab, event) {
				if (tab._props.label == '全局系统配置') {
					this.deployname = 'xitong'
					this.dataSelect()
				}
				if (tab._props.label == '佣金配置') {
					this.deployname = 'yongjing'
					this.dataSelect()
				}
				if (tab._props.label == '微信配置') {
					this.deployname = 'weixin'
					this.dataSelect()
				}
				if (tab._props.label == '公众号模板配置') {
					this.deployname = 'xiaoxi'
					this.dataSelect()
				}
				if (tab._props.label == '支付宝配置') {
					this.deployname = 'zhifubao'
					this.dataSelect()
				}
				if (tab._props.label == '短信配置') {
					this.deployname = 'duanxin'
					this.dataSelect()
				}
				if (tab._props.label == 'APP消息推送配置') {
					this.deployname = 'push'
					this.dataSelect()
				}
				if (tab._props.label == '文件上传配置') {
					this.deployname = 'oss'
					this.dataSelect()
				}
				if (tab._props.label == '协议配置') {
					this.deployname = 'xieyi'
					this.dataSelect()
				}
				if (tab._props.label == '积分配置') {
					this.deployname = 'jifen'
					this.dataSelect()
				}
				if (tab._props.label == '视频配置') {
					this.deployname = 'video'
					this.dataSelect()
				}
				if (tab._props.label == '开关配置') {
					this.deployname = 'kaiguan'
					this.dataSelect()
				}
				if (tab._props.label == '图片配置') {
					this.deployname = 'image'
					this.dataSelect()
				}
			},
			// 重置
			cleans() {
				this.min = '';
				this.dataSelect()
			},
			// 修改弹框
			amend(index, rows) {
				this.dialogFormVisible = true;
				this.form.id = rows.id;
				this.form.type = rows.type
				this.form.min = rows.min;
				this.form.value = rows.value;
				this.form.conditionFrom = rows.conditionFrom;
			},
			// 修改商品类别
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'type': this.form.type,
						'value': this.form.value,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible = false
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
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(`/common/type/condition/${this.deployname}`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'size': this.size
					})
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
			videoSuccess1(file) {
				console.log(file)
				this.form.value = file.data;


				this.tableDataLoading5 = false
			},
			// 验证只能上传视频
			beforeVideoUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'mp4'
				if (!extension) {
					this.$message({
						message: "上传文件只能是mp4格式！",
						type: 'error'
					})
				}
				return extension;
			},
			handlePreviewV(file) {
				this.tableDataLoading5 = true
			},
			handleAvatarSuccess1(file) {
				this.form.value = file.data;
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.allocation-list-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.allocation-list-page__tabs {
	margin-bottom: 0;
}

.allocation-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.allocation-list-page__table .el-table {
	width: 100%;
}

.allocation-list-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.allocation-list-page__table .el-table td,
.allocation-list-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.allocation-list-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.allocation-list-page__table .el-button--primary:not(.is-plain):hover,
.allocation-list-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.eit {
	height: 120px;
}
</style>