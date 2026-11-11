<template>
	<div class="marketing-add-page">
		<!-- hero -->
		<div class="nf-page-hero marketing-add-page__hero">
			<a href="#" class="marketing-add-page__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="marketing-add-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-hero__badge">营销管理</span>
			<h2 class="nf-page-hero__title">添加拼团商品</h2>
			<p class="nf-page-hero__desc">从商品库选择商品并配置拼团价格、时效、人数及详情内容</p>
		</div>

		<!-- 选择商品白卡 -->
		<div class="nf-filter-card marketing-add-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-goods"></i>
				<span>选择商品</span>
			</div>
			<div class="marketing-add-page__upload-row">
				<label class="marketing-add-page__upload-label">商品来源</label>
				<div class="marketing-add-page__pick-box" @click="shopClick()">
					<img v-if="coverImg" :src="coverImg" class="avatar marketing-add-page__cover-img" />
					<i v-else class="el-icon-plus avatar-uploader-icon marketing-add-page__upload-icon"></i>
				</div>
				<p class="marketing-add-page__pick-hint">点击选择商品，将自动填充名称、价格与规格信息</p>
			</div>
		</div>

		<!-- 基本信息白卡 -->
		<div class="nf-filter-card marketing-add-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-edit-outline"></i>
				<span>基本信息</span>
			</div>
			<div class="marketing-add-page__form-grid">
				<div class="marketing-add-page__form-item marketing-add-page__form-item--wide">
					<label class="marketing-add-page__form-label">拼团商品名称</label>
					<el-input class="marketing-add-page__form-control" placeholder="请输入拼团商品名称" v-model="title"
						autosize></el-input>
				</div>
				<div class="marketing-add-page__form-item">
					<label class="marketing-add-page__form-label">拼团商品价格</label>
					<el-input class="marketing-add-page__form-control" placeholder="请输入拼团商品价格" v-model="price"
						autosize></el-input>
				</div>
				<div class="marketing-add-page__form-item">
					<label class="marketing-add-page__form-label">拼团会员价格</label>
					<el-input class="marketing-add-page__form-control" placeholder="请输入拼团会员价格" v-model="memberPrice"
						autosize></el-input>
				</div>
				<div class="marketing-add-page__form-item">
					<label class="marketing-add-page__form-label">拼团商品佣金</label>
					<el-input class="marketing-add-page__form-control" placeholder="例0.1 等于10%" type="number" :min="0"
						:controls="false" v-model="commissionPrice" autosize></el-input>
				</div>
				<div class="marketing-add-page__form-item">
					<label class="marketing-add-page__form-label">拼团开始时间</label>
					<el-date-picker class="marketing-add-page__form-control" v-model="startTime" align="right" type="datetime"
						format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="marketing-add-page__form-item">
					<label class="marketing-add-page__form-label">拼团结束时间</label>
					<el-date-picker class="marketing-add-page__form-control" v-model="endTime" align="right" type="datetime"
						format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择结束时间">
					</el-date-picker>
				</div>
				<div class="marketing-add-page__form-item">
					<label class="marketing-add-page__form-label">活动状态</label>
					<el-radio-group v-model="status">
						<el-radio :label="1">开启</el-radio>
						<el-radio :label="2">关闭</el-radio>
					</el-radio-group>
				</div>
			</div>
		</div>

		<!-- 商品图片白卡 -->
		<div class="nf-filter-card marketing-add-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-picture-outline"></i>
				<span>商品图片</span>
			</div>
			<div class="marketing-add-page__upload-row">
				<label class="marketing-add-page__upload-label">商品封面图</label>
				<div class="marketing-add-page__upload-box">
					<el-upload class="avatar-uploader" v-model="coverImg" :action="$http.adornUrl2('/alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess1">
						<img v-if="coverImg" :src="coverImg" class="avatar marketing-add-page__cover-img" />
						<i v-else class="el-icon-plus avatar-uploader-icon marketing-add-page__upload-icon"></i>
					</el-upload>
				</div>
			</div>
			<div class="marketing-add-page__upload-row marketing-add-page__upload-row--carousel">
				<label class="marketing-add-page__upload-label">商品轮播图</label>
				<div class="marketing-add-page__carousel-wrap">
					<div v-for="(item,index) of imgs" :key="index" class="marketing-add-page__carousel-item">
						<img :src="item" class="avatar marketing-add-page__carousel-img" />
						<div @click="clear(index)" class="divhove">
							<i class="el-icon-delete"></i>
						</div>
					</div>
					<el-upload :action="$http.adornUrl2('/alioss/upload')" list-type="picture-card"
						:on-success="handleUploadSuccess" :on-change="handleChange" :on-remove="handleRemove">
						<i class="el-icon-plus"></i>
					</el-upload>
				</div>
			</div>
		</div>

		<!-- 拼团设置白卡 -->
		<div class="nf-filter-card marketing-add-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-time"></i>
				<span>拼团设置</span>
			</div>
			<div class="marketing-add-page__form-grid">
				<div class="marketing-add-page__form-item">
					<label class="marketing-add-page__form-label">拼团时效(小时)</label>
					<el-input class="marketing-add-page__form-control" placeholder="请输入拼团时效" type="number" :min="1"
						:controls="false" v-model="hours" autosize></el-input>
				</div>
				<div class="marketing-add-page__form-item">
					<label class="marketing-add-page__form-label">拼团人数</label>
					<el-input class="marketing-add-page__form-control" placeholder="请输入拼团人数" type="number" :min="2"
						v-model="number" autosize></el-input>
				</div>
			</div>
		</div>

		<!-- 商品规格白卡 -->
		<div v-if="propshow || propshow1" class="nf-filter-card marketing-add-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-goods"></i>
				<span>商品规格</span>
			</div>
			<div v-if="propshow" class="marketing-add-page__attr-block">
				<label class="marketing-add-page__form-label">商品属性</label>
				<div class="property_table marketing-add-page__property-table">
					<table style="width: 100%;">
						<tr style="display:flex;">
							<th style="width:82px;text-align: left;" v-for="item in headerData" :key="item">{{item}}</th>
						</tr>
					</table>
					<el-table :data="valueData">
						<el-table-column v-if="detailJson.length == 1" prop="value0" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 2" prop="value0" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 2" prop="value1" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value0" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value1" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value2" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value0" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value1" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value2" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value3" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value0" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value1" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value2" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value3" width="80">
						</el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value4" width="80">
						</el-table-column>
						<el-table-column prop="skuImg" width="80">
							<template slot-scope="scope">
								<div class="imgWrap marketing-add-page__sku-upload">
									<el-upload style="width: 60px;height: 60px;" class="avatar-uploader"
										v-model="scope.row.skuImg" :action="$http.adornUrl2('/alioss/upload')"
										:show-file-list="false" :on-success="handleAvatarSuccess">
										<img v-if="scope.row.skuImg" :src="scope.row.skuImg"
											@click="curRowIndex=scope.$index" class="avatar"
											style="border-radius: 6px;width:60px;height: 60px;" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											@click="curRowIndex=scope.$index"></i>
									</el-upload>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuOriginalPrice" width="80">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.skuOriginalPrice"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuPrice" width="80">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.skuPrice"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="memberPrice" width="80">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.memberPrice"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="stock" width="80">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.stock"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="sales" width="80">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.sales"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column width="100">
							<template slot-scope="scope">
								<div>
									<el-input type="number" v-model="scope.row.pinkPrice" :min="0"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column width="100">
							<template slot-scope="scope">
								<div>
									<el-input type="number" v-model="scope.row.pinkStock" :min="1"
										:max="scope.row.stock" style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column width="80">
							<template slot-scope="scope">
								<el-button :disabled="!isAuth('userList:delete')" size="mini" type="danger"
									@click="deleter(scope.$index)">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
			<div v-if="propshow1" class="marketing-add-page__attr-block">
				<label class="marketing-add-page__form-label">商品属性</label>
				<div class="property_table marketing-add-page__property-table">
					<table style="width: 100%;">
						<tr style="display:flex;">
							<th style="width:120px;text-align: left;" v-for="item in headerData2" :key="item">{{item}}</th>
						</tr>
					</table>
					<el-table :data="valueData2">
						<el-table-column prop="skuImg" width="100">
							<template slot-scope="scope">
								<div class="imgWrap marketing-add-page__sku-upload">
									<el-upload style="width: 60px;height: 60px;" class="avatar-uploader"
										v-model="scope.row.skuImg" :action="$http.adornUrl2('/alioss/upload')"
										:show-file-list="false" :on-success="handleAvatarSuccess3">
										<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar"
											style="border-radius: 6px;width:60px;height: 60px;" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											@click="curRowIndex=scope.$index"></i>
									</el-upload>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuOriginalPrice" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.skuOriginalPrice"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuPrice" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.skuPrice"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="memberPrice" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.memberPrice"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="stock" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.stock"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="sales" width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="text" disabled v-model="scope.row.sales"
										style="width:60px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="number" v-model="scope.row.pinkPrice" :min="0"
										style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
						<el-table-column width="120">
							<template slot-scope="scope">
								<div>
									<el-input type="number" v-model="scope.row.pinkStock" :min="1"
										:max="scope.row.stock" style="width:80px;font-size:14px;">
									</el-input>
								</div>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
		</div>

		<!-- 商品详情白卡 -->
		<div class="nf-filter-card marketing-add-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-document"></i>
				<span>商品详情</span>
			</div>
			<div class="tinymce-content marketing-add-page__editor">
				<quill-editor ref="myTextEditor" v-model="descrition" :options="quillOption"
					class="marketing-add-page__quill">
				</quill-editor>
			</div>
		</div>

		<!-- 操作区 -->
		<div class="marketing-add-page__footer">
			<el-button size="mini" type="primary" icon="document" @click="prev">返回上一页</el-button>
			<el-button size="mini" type="primary" icon="document" @click="artiReleass">保存</el-button>
		</div>

		<!-- 选择商品弹框 -->
		<el-dialog title="商品列表" customClass="customWidth marketing-add-page__dialog" :visible.sync="dialogFormVisible" center width="80%">
			<div class="marketing-add-page__dialog-filter">
				<div class="marketing-add-page__dialog-filter-item">
					<label class="marketing-add-page__form-label">商品标题</label>
					<el-input class="marketing-add-page__dialog-input" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
						v-model="title"></el-input>
				</div>
				<div class="marketing-add-page__dialog-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
			<div class="nf-table-card marketing-add-page__dialog-table">
				<el-table v-loading="tableDataLoading" :data="tableData.content">
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
					<el-table-column label="操作" width="100">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="shopdeletes(scope.row)">选择
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="marketing-add-page__pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalElements">
					</el-pagination>
				</div>
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
		name: 'Tinymce',
		components: {
			quillEditor
		},
		data() {
			return {
				size: 10,
				page: 1,
				specif: '',
				typeId: '',
				title: '',
				hours: 1,
				number: 2,
				price: '',
				goodsId: '',
				memberPrice: '',
				tableData: [],
				tableDataLoading: true,
				dialogFormVisible: false,
				positage: 0,
				guige: 0,
				skuImg: '',
				ruleId: '',
				postagePrice: 0,
				priceshow: false,
				isshow: true,
				commissionPrice: '',
				buyReason: '',
				ruleshow: false,
				propshow: false,
				guigeshow: true,
				propshow1: false,
				ruleValue: [],
				attr: [],
				attrs: [],
				imgs: [],
				headerData: [],
				valueData: [],
				detailJson: [],
				inputValues: {
					inputValue0: '',
					inputValue1: '',
					inputValue2: '',
					inputValue3: '',
					inputValue4: '',
					inputValue5: ''
				},
				sku: [],
				img: '',
				sales: '',
				status: 0,
				photos: '',
				value: [],
				isSelect: 1,
				homeGoods: 1,
				isRecommend: 1,
				isExpress: '',
				coverImg: '',
				valueData2: [],
				headerData2: [],
				startTime: '',
				endTime: '',
				attrName: '',
				ruleName: '',
				attrValue: [],
				releobject: {
					value: '',
					detail: ''
				},
				dialogVisible: false,
				isShow: false,
				hideUpload: false,
				limit: 1,
				id: '',
				hasChange: false,
				hasInit: false,
				tinymceId: 'tinymceId',
				height: 435,
				toolbar: [],
				menubar: 'file edit insert view format table',
				languageTypeList: {
					'en': 'en',
					'zh': 'zh_CN'
				},
				artiCletypes: [],
				// 富文本内容
				descrition: '',
				quillOption: quillConfig,
			}
		},
		methods: {
			// 图标上传
			handleAvatarSuccess(file) {
				if (this.curRowIndex == 0) {
					this.skuImg = file.data;
					this.valueData[0].skuImg = this.skuImg
					return
				} else {
					this.skuImg = file.data;
					this.valueData[this.curRowIndex].skuImg = this.skuImg
				}
			},
			// 图标上传
			handleAvatarSuccess3(file) {
				this.skuImg = file.data;
				this.valueData2[0].skuImg = this.skuImg
			},
			handleSizeChange(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.shopSelect()
			},
			// 获取商品数据
			shopSelect() {
				let page = this.page - 1
				let status = 0
				this.title = ''
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goods/goodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'title': this.title,
						'type': this.typeId,
						'status': status,
						'isExpress': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					this.totalnum = returnData.totalElements
				})
			},
			// 选择商品
			shopClick() {
				this.dialogFormVisible = true
				this.shopSelect()
			},
			//搜索
			select() {
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.status = 0
				this.typeId = ''
				this.value = []
				this.shopSelect()
			},
			// 选择商品
			shopdeletes(row) {
				this.$http({
					url: this.$http.adornUrl2('/goods/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': row.id
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisible = false
						let returnData = data.data;
						let imgs = returnData.img.split(',')
						this.title = returnData.title
						this.status = returnData.status
						this.originalPrice = returnData.originalPrice
						this.price = returnData.price
						this.memberPrice = returnData.memberPrice
						this.commissionPrice = returnData.commissionPrice
						this.descrition = returnData.descrition
						this.coverImg = returnData.coverImg
						this.imgs = imgs
						this.img = returnData.img
						this.goodsId = returnData.id
						this.attr = returnData.attr
						if (this.attr.length > 0) {
							this.duoguigeclect(returnData.id)
						} else {
							this.guigeclect(returnData.id)
						}
					}

				})
			},
			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
			},
			// 单规格
			guigeclect(id) {
				this.$http({
					url: this.$http.adornUrl2('/selfGroup/onlyFormatSku'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': id
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.headerData2 = returnData.header
					this.valueData2 = returnData.value
					this.propshow1 = true
					this.propshow = false
					this.sku = returnData.value
				})
			},
			// 多规格
			duoguigeclect(id) {
				this.$http({
					url: this.$http.adornUrl2('/selfGroup/isFormatSku'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': id
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.headerData = returnData.header
					this.valueData = returnData.value
					this.propshow = true
					this.propshow1 = false
					this.sku = returnData.value
					this.detailJson = returnData.value[0].detailJson.split(',')
				})
			},
			handleChange1(value) {
				this.value.forEach(element => {
					this.typeId = element;
				});
			},
			handleChange(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			handleAvatarSuccess1(file, fileList) {
				this.coverImg = file.data
			},
			//上传成功
			handleUploadSuccess(file, fileList) {
				this.photos += file.data + ','
			},
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 发布商品
			artiReleass() {
				let photost = this.photos + this.img;
				console.log(photost)
				// photost = photost.substr(0,photost.length-1)
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品',
						type: 'warning'
					});
					return
				}
				if (this.startTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择拼团开始时间',
						type: 'warning'
					});
					return
				}
				if (this.endTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择拼团结束时间',
						type: 'warning'
					});
					return
				}
				if (this.coverImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品封面图',
						type: 'warning'
					});
					return
				}
				if (this.descrition == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入商品详情',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/selfGroup/save'),
					method: 'post',
					data: this.$http.adornData({
						'img': photost,
						'title': this.title,
						'typeId': this.typeId,
						'merchants': this.merchants,
						'originalPrice': this.originalPrice,
						'price': this.price,
						'memberPrice': this.memberPrice,
						'sales': this.sales,
						'status': this.status,
						'goodsId': this.goodsId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'hours': this.hours,
						'number': this.number,
						'coverImg': this.coverImg,
						'descrition': this.descrition,
						'commissionPrice': this.commissionPrice,
						'isSelect': this.isSelect,
						'homeGoods': this.homeGoods,
						'buyReason': this.buyReason,
						'isRecommend': this.isRecommend,
						'isExpress': this.isExpress,
						'postagePrice': this.postagePrice,
						'sku': this.sku,
					})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.$router.push({
								path: '/marketing'
							})
						}
					})
				})
			},

			// 删除商品属性
			deleter(index) {
				let length = this.sku.length
				if (length == 1) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '属性至少要有一个',
						type: 'warning'
					});
					return
				} else {
					this.sku.splice(index, 1);
				}
			}
		},
		mounted() {},

	}
</script>

<style scoped>
.marketing-add-page {
	padding: 12px 16px;
	max-width: 100%;
	overflow-x: hidden;
}

.marketing-add-page__section {
	margin-bottom: 12px;
}

.marketing-add-page__back {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	margin-bottom: 12px;
	font-size: 14px;
	color: var(--nf-text-secondary, #606266);
	text-decoration: none;
	line-height: 1;
}

.marketing-add-page__back:hover {
	color: var(--nf-brand, #15cad2);
}

.marketing-add-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	flex-shrink: 0;
}

.marketing-add-page__form-grid {
	display: flex;
	flex-wrap: wrap;
	gap: 12px 16px;
}

.marketing-add-page__form-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
	min-width: 200px;
	flex: 1 1 200px;
	max-width: 280px;
}

.marketing-add-page__form-item--wide {
	max-width: 100%;
	flex: 1 1 100%;
}

.marketing-add-page__form-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.marketing-add-page__form-control {
	width: 100%;
}

.marketing-add-page__pick-hint {
	margin: 0;
	font-size: 12px;
	color: var(--nf-text-secondary, #909399);
	line-height: 1.4;
	align-self: center;
}

.marketing-add-page__upload-row {
	display: flex;
	align-items: flex-start;
	gap: 12px;
	margin-bottom: 12px;
}

.marketing-add-page__upload-row--carousel {
	align-items: flex-start;
	flex-wrap: wrap;
}

.marketing-add-page__upload-label {
	flex-shrink: 0;
	width: 90px;
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 148px;
	text-align: right;
}

.marketing-add-page__upload-row--carousel .marketing-add-page__upload-label {
	line-height: 1;
	padding-top: 12px;
}

.marketing-add-page__pick-box,
.marketing-add-page__upload-box {
	width: 148px;
	height: 148px;
	background-color: #fbfdff;
	border: 1px dashed #c0ccda;
	border-radius: 6px;
	text-align: center;
	line-height: 148px;
	cursor: pointer;
}

.marketing-add-page__upload-box {
	cursor: default;
}

.marketing-add-page__cover-img {
	width: 140px;
	height: 140px;
}

.marketing-add-page__upload-icon {
	font-size: 28px;
	color: #8c939d;
}

.marketing-add-page__carousel-wrap {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
}

.marketing-add-page__carousel-item {
	position: relative;
	margin: 0;
}

.marketing-add-page__carousel-img {
	width: 140px;
	height: 140px;
	border-radius: 6px;
}

.marketing-add-page__attr-block {
	display: flex;
	flex-direction: column;
	gap: 8px;
}

.marketing-add-page__property-table {
	width: 100%;
	overflow-x: auto;
}

.marketing-add-page__property-table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.marketing-add-page__property-table .el-table td,
.marketing-add-page__property-table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.marketing-add-page__sku-upload {
	width: 60px;
	height: 60px;
	background-color: #fbfdff;
	border: 1px dashed #c0ccda;
	border-radius: 6px;
	text-align: center;
	line-height: 60px;
}

.marketing-add-page__editor {
	width: 100%;
}

.marketing-add-page__quill {
	padding-bottom: 50px;
	height: 300px;
	width: 100%;
	max-width: 100%;
	margin-bottom: 16px;
}

.marketing-add-page__footer {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
	gap: 12px;
	padding: 16px 0 8px;
}

.marketing-add-page__footer .el-button--primary,
.marketing-add-page__dialog-actions .el-button--primary:not(.is-plain),
.marketing-add-page__dialog-table .el-button--primary {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.marketing-add-page__footer .el-button--primary:hover,
.marketing-add-page__footer .el-button--primary:focus,
.marketing-add-page__dialog-actions .el-button--primary:not(.is-plain):hover,
.marketing-add-page__dialog-actions .el-button--primary:not(.is-plain):focus,
.marketing-add-page__dialog-table .el-button--primary:hover,
.marketing-add-page__dialog-table .el-button--primary:focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.marketing-add-page__dialog-filter {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
	margin-bottom: 12px;
}

.marketing-add-page__dialog-filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.marketing-add-page__dialog-input {
	width: 180px;
}

.marketing-add-page__dialog-actions {
	display: flex;
	flex-wrap: wrap;
	gap: 8px;
}

.marketing-add-page__dialog-table .el-table {
	width: 100%;
}

.marketing-add-page__dialog-table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.marketing-add-page__dialog-table .el-table td,
.marketing-add-page__dialog-table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.marketing-add-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.divhove {
	position: absolute;
	width: 100%;
	height: 100%;
	left: 0;
	top: 0;
	opacity: 0;
	cursor: default;
	text-align: center;
	display: flex;
	align-items: center;
	justify-content: center;
	border-radius: 6px;
	background-color: rgba(0, 0, 0, .5);
	transition: opacity .3s;
}

.divhove i {
	margin: 0;
	font-size: 18px;
	color: #fff;
}

.divhove:hover {
	opacity: 0.8;
}

.hide .el-upload--picture-card {
	display: none;
}

.marketing-add-page >>> .el-table--enable-row-transition .el-table__body td {
	text-align: center;
}

.marketing-add-page >>> .imgWrap .avatar-uploader .el-upload {
	width: 60px;
}

.marketing-add-page >>> .el-table .cell {
	text-align: center !important;
}

.customWidth {
	width: 80% !important;
}
</style>
