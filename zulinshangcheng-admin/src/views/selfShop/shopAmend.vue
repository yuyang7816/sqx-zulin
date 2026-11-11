<template>
	<div class="shop-amend-page">
		<!-- hero -->
		<div class="nf-page-hero shop-amend-page__hero">
			<a href="#" class="shop-amend-page__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="shop-amend-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-hero__badge">自营商城</span>
			<h2 class="nf-page-hero__title">商品修改</h2>
			<p class="nf-page-hero__desc">编辑自营商品信息，调整分类、价格、规格、图片及详情内容</p>
		</div>

		<!-- 基本信息白卡 -->
		<div class="nf-filter-card shop-amend-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-edit-outline"></i>
				<span>基本信息</span>
			</div>
			<div class="shop-amend-page__form-grid">
				<div class="shop-amend-page__form-item shop-amend-page__form-item--wide">
					<label class="shop-amend-page__form-label">商品标题</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入商品标题" v-model="title" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">一级分类</label>
					<el-select v-model="typeId" class="shop-amend-page__form-control" placeholder="请选择一级分类" @change="handleChange1">
						<el-option v-for="(item,index) in typeDatas" :key="item.index" :label="item.label" :value="item.value"></el-option>
					</el-select>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">二级分类</label>
					<el-select v-model="twoTypeId" class="shop-amend-page__form-control" placeholder="请选择二级分类" @change="handleChange2">
						<el-option v-for="(item,index) in twoTypeDatas" :key="item.index" :label="item.label" :value="item.value"></el-option>
					</el-select>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">三级分类</label>
					<el-select v-model="threeTypeId" class="shop-amend-page__form-control" placeholder="请选择三级分类">
						<el-option v-for="(item,index) in threeTypeDatas" :key="item.index" :label="item.label" :value="item.value"></el-option>
					</el-select>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">商品原价</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入商品原价" type="number" :min="0" :controls="false" v-model="originalPrice" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">商品现价</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入商品现价" type="number" :min="0" :controls="false" v-model="price" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">会员价格</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入会员价格" type="number" :min="0" :controls="false" v-model="memberPrice" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">日租金额</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入日租金额" type="number" :min="0" :controls="false" v-model="dailyRent" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">会员日租金</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入会员日租金" type="number" :min="0" :controls="false" v-model="memberRent" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">逾期日租金</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入逾期日租金" type="number" :min="0" :controls="false" v-model="overdueRent" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">商品销量</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入商品销量" type="number" :min="0" :controls="false" v-model="sales" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">商品状态</label>
					<el-select v-model="status" class="shop-amend-page__form-control" placeholder="请选择商品类型">
						<el-option v-for="(item,index) in statusmain2" :key="item.index" :label="item.label" :value="item.value"></el-option>
					</el-select>
				</div>
			</div>
		</div>

		<!-- 商品设置白卡 -->
		<div class="nf-filter-card shop-amend-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-setting"></i>
				<span>商品设置</span>
			</div>
			<div class="shop-amend-page__form-grid">
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">必买理由</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入必买理由" v-model="buyReason" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">精选好物</label>
					<el-radio-group v-model="isSelect">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">首页商品</label>
					<el-radio-group v-model="homeGoods">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">是否是热爆</label>
					<el-radio-group v-model="isRecommend">
						<el-radio :label="1">是</el-radio>
						<el-radio :label="0">否</el-radio>
					</el-radio-group>
				</div>
				<div class="shop-amend-page__form-item shop-amend-page__form-item--wide">
					<label class="shop-amend-page__form-label">发货方式</label>
					<el-checkbox-group v-model="isExpress" @change="isExpressBtn">
						<el-checkbox label="1">包邮</el-checkbox>
						<el-checkbox label="2">邮寄</el-checkbox>
						<el-checkbox label="3">自取</el-checkbox>
					</el-checkbox-group>
				</div>
				<div class="shop-amend-page__form-item shop-amend-page__form-item--wide">
					<label class="shop-amend-page__form-label">热爆标签</label>
					<el-radio-group v-model="labelId">
						<el-radio v-for="(item,index) in labelList" :key="index" :label="item.labelId">{{item.label}}</el-radio>
					</el-radio-group>
				</div>
				<div class="shop-amend-page__form-item" v-if="isExpress1==2">
					<label class="shop-amend-page__form-label">邮费</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入邮费" type="number" v-model="postagePrice" autosize></el-input>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">新旧程度</label>
					<el-select v-model="oldDegree" class="shop-amend-page__form-control" placeholder="请选择新旧程度">
						<el-option v-for="(item,index) in degreeList" :key="index" :label="item" :value="item"></el-option>
					</el-select>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">商品品牌</label>
					<el-select v-model="brandId" class="shop-amend-page__form-control" placeholder="请选择商品品牌">
						<el-option v-for="(item,index) in brandIds" :key="item.index" :label="item.label" :value="item.value"></el-option>
					</el-select>
				</div>
				<div class="shop-amend-page__form-item">
					<label class="shop-amend-page__form-label">商品押金</label>
					<el-input class="shop-amend-page__form-control" placeholder="请输入商品押金" type="number" :min="0" v-model="depositMoney" autosize></el-input>
				</div>
			</div>
		</div>

		<!-- 商品图片白卡 -->
		<div class="nf-filter-card shop-amend-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-picture-outline"></i>
				<span>商品图片</span>
			</div>
			<div class="shop-amend-page__upload-row">
				<label class="shop-amend-page__upload-label">商品封面图</label>
				<div class="shop-amend-page__upload-box">
					<el-upload class="avatar-uploader" v-model="coverImg" :action="$http.adornUrlUp('/alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess1">
						<img v-if="coverImg" :src="coverImg" class="avatar shop-amend-page__cover-img" />
						<i v-else class="el-icon-plus avatar-uploader-icon shop-amend-page__upload-icon"></i>
					</el-upload>
				</div>
			</div>
			<div class="shop-amend-page__upload-row shop-amend-page__upload-row--carousel">
				<label class="shop-amend-page__upload-label">商品轮播图</label>
				<div class="shop-amend-page__carousel-wrap">
					<div v-for="(item,index) of imgs" :key="index" class="shop-amend-page__carousel-item">
						<img :src="item" class="avatar shop-amend-page__carousel-img" />
						<div @click="clear(index)" class="divhove">
							<i class="el-icon-delete"></i>
						</div>
					</div>
					<el-upload :action="$http.adornUrlUp('/alioss/upload')" list-type="picture-card"
						:on-success="handleUploadSuccess" :on-change="handleChange" :on-remove="handleRemove">
						<i class="el-icon-plus"></i>
					</el-upload>
				</div>
			</div>
		</div>

		<!-- 商品规格白卡 -->
		<div class="nf-filter-card shop-amend-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-goods"></i>
				<span>商品规格</span>
			</div>
			<div class="shop-amend-page__spec-row">
				<label class="shop-amend-page__form-label">商品规格</label>
				<el-radio-group v-model="guige" @change="guigeChange">
					<el-radio :label="0">单规格</el-radio>
					<el-radio :label="1">多规格</el-radio>
				</el-radio-group>
				<div v-if="guigeshow" class="shop-amend-page__spec-select">
					<el-select v-model="ruleId" placeholder="请选择商品规格" style="width:200px;" @change="selectTrigger(ruleId)">
						<el-option v-for="(item,index) in specifdata" :key="item.index" :label="item.ruleName" :value="item.id"></el-option>
					</el-select>
				</div>
			</div>
			<div v-if="ruleValue" class="shop-amend-page__rule-block">
				<div class="ruleitem" v-for="(item,index) in ruleValue" :key="index">
					<div>{{item.value}} <span @click="deleterule(index)"><icon-svg name="schu" class="ruleicon"></icon-svg></span></div>
					<div>
						<el-tag :key="tag" v-for="(tag,j) in item.detail.split(',')" closable
							:disable-transitions="false" @close="handleClose(tag,j,item)">
							{{tag}}
						</el-tag>
						<el-input class="input-new-tag" v-model="inputValues[index]" ref="saveTagInput" size="small"
							style="width:120px;" placeholder="请输入属性名"
							@keyup.enter.native="handleInputConfirm(index,item)">
						</el-input>
						<el-button class="button-new-tag" size="small" @click="handleInputConfirm(index,item)">添加</el-button>
					</div>
				</div>
				<div class="shop-amend-page__rule-actions" v-if="ruleshow">
					<div v-if="isshow" class="btn_specif">
						<el-button type="primary" @click="btnToclick()">添加新规格</el-button>
						<el-button type="success" @click="create()">立即生成</el-button>
					</div>
				</div>
				<div v-if="!isshow" class="shop-amend-page__rule-form">
					<div class="shop-amend-page__rule-form-item">
						<span class="shop-amend-page__rule-form-label">规格：</span>
						<el-input style="width:50%;" v-model="releobject.value" placeholder="例:颜色"></el-input>
					</div>
					<div class="shop-amend-page__rule-form-item">
						<span class="shop-amend-page__rule-form-label">规格值：</span>
						<el-input style="width:50%;" v-model="releobject.detail" placeholder="例:黑色"></el-input>
					</div>
					<div class="shop-amend-page__rule-form-item">
						<el-button type="primary" @click="speciTo()">确 定</el-button>
						<el-button @click="speciTotwo()">取 消</el-button>
					</div>
				</div>
			</div>

			<!-- 多规格属性表格 -->
			<div class="shop-amend-page__attr-block" v-if="valueshow">
				<label class="shop-amend-page__form-label">商品属性</label>
				<div class="property_table shop-amend-page__property-table">
					<table style="width: 100%;">
						<tr style="display:flex;text-align: left;">
							<th style="width:120px;" v-for="item in headerData" :key="item">{{item}}</th>
						</tr>
					</table>
					<el-table :data="valueData">
						<el-table-column v-if="detailJson.length == 1 || detailJson.length == 0" prop="value0" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 2" prop="value0" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 2" prop="value1" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value0" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value1" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 3" prop="value2" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value0" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value1" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value2" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 4" prop="value3" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value0" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value1" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value2" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value3" width="120"></el-table-column>
						<el-table-column v-if="detailJson.length == 5" prop="value4" width="120"></el-table-column>
						<el-table-column width="100">
							<template slot-scope="scope">
								<div class="imgWrap shop-amend-page__sku-upload">
									<el-upload class="avatar-uploader" v-model="scope.row.skuImg"
										:action="$http.adornUrlUp('/alioss/upload')" :show-file-list="false"
										:on-success="handleAvatarSuccess">
										<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar"
											style="border-radius: 6px;width:50px;height: 50px;"
											@click="curRowIndex=scope.$index" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											@click="curRowIndex=scope.$index"></i>
									</el-upload>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuOriginalPrice" width="120">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.skuOriginalPrice" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="skuPrice" width="120">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.skuPrice" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="memberPrice" width="120">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.memberPrice" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="dailyRent" width="120">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.dailyRent" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="memberRent" width="120">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.memberRent" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="stock" width="120">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.stock" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="oldDegree" width="150">
							<template slot-scope="scope">
								<el-select v-model="scope.row.oldDegree" style="width:120px;">
									<el-option v-for="(item,index) in degreeList" :key="index" :label="item" :value="item"></el-option>
								</el-select>
							</template>
						</el-table-column>
						<el-table-column prop="sales" width="120">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column width="120">
							<template slot-scope="scope">
								<el-button :disabled="!isAuth('userList:delete')" size="mini" type="danger" @click="deleter(scope.$index)">删除</el-button>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>

			<!-- 单规格属性表格 -->
			<div class="shop-amend-page__attr-block" v-if="valueshow1">
				<label class="shop-amend-page__form-label">商品属性</label>
				<div class="property_table shop-amend-page__property-table">
					<table style="width: 100%;">
						<tr style="display:flex;text-align: left;">
							<th style="width:150px;" v-for="item in headerData2" :key="item">{{item}}</th>
						</tr>
					</table>
					<el-table :data="sku">
						<el-table-column width="150">
							<template slot-scope="scope">
								<div class="imgWrap shop-amend-page__sku-upload">
									<el-upload class="avatar-uploader" v-model="scope.row.skuImg"
										:action="$http.adornUrlUp('/alioss/upload')" :show-file-list="false"
										:on-success="handleAvatarSuccess3">
										<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar"
											style="border-radius: 6px;width:50px;height: 50px;"
											@click="curRowIndex=scope.$index" />
										<i v-else class="el-icon-plus avatar-uploader-icon"
											@click="curRowIndex=scope.$index"></i>
									</el-upload>
								</div>
							</template>
						</el-table-column>
						<el-table-column prop="skuOriginalPrice" width="150">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.skuOriginalPrice" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="skuPrice" width="150">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.skuPrice" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="memberPrice" width="150">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.memberPrice" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="dailyRent" width="150">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.dailyRent" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="memberRent" width="150">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.memberRent" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="stock" width="150">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.stock" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
						<el-table-column prop="oldDegree" width="150">
							<template slot-scope="scope">
								<el-select v-model="scope.row.oldDegree" style="width:120px;">
									<el-option v-for="(item,index) in degreeList" :key="index" :label="item" :value="item"></el-option>
								</el-select>
							</template>
						</el-table-column>
						<el-table-column prop="sales" width="150">
							<template slot-scope="scope">
								<el-input type="text" v-model="scope.row.sales" style="width:80px;font-size:14px;"></el-input>
							</template>
						</el-table-column>
					</el-table>
				</div>
			</div>
		</div>

		<!-- 商品详情白卡 -->
		<div class="nf-filter-card shop-amend-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-document"></i>
				<span>商品详情</span>
			</div>
			<div class="tinymce-content shop-amend-page__editor">
				<quill-editor ref="myTextEditor" v-model="descrition" :options="quillOption"
					class="shop-amend-page__quill">
				</quill-editor>
			</div>
		</div>

		<!-- 操作区 -->
		<div class="shop-amend-page__footer">
			<el-button size="mini" type="primary" icon="document" @click="prev">返回上一页</el-button>
			<el-button size="mini" type="primary" icon="document" @click="artiReleass">保存</el-button>
		</div>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import {
		Quill
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	import Video from './video.js'
import { indexOf } from 'lodash'
	Quill.register(Video, true)
	export default {
		name: 'Tinymce',
		components: {
			quillEditor
		},
		data() {
			return {
				type: '',
				typeId: '',
				twoTypeId: '',
				threeTypeId: '',

				title: '',
				merchants: '',
				originalPrice: '',
				price: '',
				memberPrice: '',
				dailyRent:'',
				memberRent:'',
				coverImg: '',
				guige: '',
				commissionPrice: '',
				postagePrice: 0,
				img: '',
				buyReason: '',
				sales: '',
				status: '',
				typeDatas: [],
				twoTypeDatas: [],
				threeTypeDatas: [],
				detailJson: [],
				imgs: [],
				value: [],
				potost: '',
				attr: [],
				brandId: '',
				brandIds: [],
				statusmain2: [{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				isExpress: [],
				isExpress1:'',
				isExpressmain: [{ 
						value: '1',
						label: '包邮'
					},
					{
						value: '2',
						label: '邮寄'
					},
					{
						value: '3',
						label: '自取'
					}
				],
				positage: 0,
				valueshow: true,
				valueshow1: true,
				priceshow: false,
				specifdata: [],
				ruleValue: [],
				headerData: [],
				valueData: [],
				sku: [],
				releobject: {
					value: '',
					detail: ''
				},
				isshow: true,
				ruleshow: false,
				inputValues: {
					inputValue0: '',
					inputValue1: '',
					inputValue2: '',
					inputValue3: '',
					inputValue4: '',
					inputValue5: ''
				},
				ruleId: '',
				specif: '',
				propshow: false,
				guigeshow: true,
				attrName: '',
				headerData2: [],
				attrValue: [],
				skuImg: '',
				isSelect: '',
				homeGoods: '',
				isRecommend: '',
				isShow: false,
				hideUpload: false,
				limit: 1,
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
				curRowIndex: null,
				quillOption: quillConfig,
				degreeList:[],
				oldDegree:'',
				depositMoney:'',
				labelId:'',
				labelList:[],
				overdueRent:'',


			}
		},
		computed: {
			options() {
				return {
					modules: {
						toolbar: {
							container: this.toolbarOptions,
							handlers: {
								image: value => {
									if (value) {
										this.$refs.uploadRef.handleClick();
									} else {
										this.quill.format("image", false);
									}
								},
								video: value => {
									if (value) {
										this.$refs.uploadRef.handleClick();
									} else {
										this.quill.format("video", false);
									}
								}
							}
						}
					},
					placeholder: '请输入···',
					theme: 'snow',
					...this.props
				}
			},
			quill() {
				const {
					quill
				} = this.$refs.quillEditor || {}
				return quill
			}
		},
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
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
				this.sku[0].skuImg = this.skuImg
			},
			// 是否包邮
			agreeChange(val) {
				if (val == 1) {
					this.priceshow = true
					this.postagePrice = ''
				} else {
					this.priceshow = false
					this.postagePrice = 0
				}
			},
			// 是否多规格
			guigeChange(val) {
				if (val == 0) {
					this.ruleId = 0
					this.valueshow1 = true
					this.valueshow = false
					this.guigeshow = false
					this.attr = []
					this.ruleshow = false
					this.ruleValue = []
					this.dansku()
				}
				if (val == 1) {
					this.ruleId = ''
					this.sku = []
					this.guigeshow = true
					this.valueshow1 = false
				}
			},
			// 获取单规格的sku
			dansku() {
				if (this.originalPrice == '') {
					this.originalPrice = '0'
				}
				if (this.price == '') {
					this.price = '0'
				}
				if (this.memberPrice == '') {
					this.memberPrice = '0'
				}
				if (this.dailyRent == '') {
					this.dailyRent = '0'
				}
				if (this.memberRent == '') {
					this.memberRent = '0'
				}
				if (this.oldDegree == '') {
					this.oldDegree = this.degreeList[0]
				}
				
				
				
				
				this.$http({
					url: this.$http.adornUrl2('/goods/onlyFormatAttr'),
					method: 'get',
					params: this.$http.adornParams({
						'originalPrice': this.originalPrice,
						'price': this.price,
						'memberPrice': this.memberPrice,
						'coverImg': this.coverImg,
						'dailyRent': this.dailyRent,
						'memberRent': this.memberRent,
						'oldDegree':this.oldDegree,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.sku = returnData.value
					this.headerData2 = returnData.header
					this.valueshow = false
					this.propshow = true
					this.guigeshow = false
				})
			},
			// 回显规格
			echo(goodsId) {
				this.$http({
					url: this.$http.adornUrl2('/goods/formatAttr'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': goodsId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.sku = returnData.value
					this.headerData2 = returnData.header
					this.valueshow = false
					this.propshow = true
					this.guigeshow = false
				})
			},
			handleChange1(value) {
				// this.value.forEach(element => {
				// 	this.typeId = element;
				// });

				for (var i in this.typeDatas) {
					if (this.typeId == this.typeDatas[i].value) {
						this.twoTypeDatas = this.typeDatas[i].children
					}
				}

				this.twoTypeId = ''
				this.threeTypeId = ''
			},
			handleChange2() {
				for (var i in this.twoTypeDatas) {
					if (this.twoTypeId == this.twoTypeDatas[i].value) {
						this.threeTypeDatas = this.twoTypeDatas[i].children
					}
				}
				this.threeTypeId = ''
			},
			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
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
				this.potost += file.data + ','
			},
			init() {

				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl2('/goods/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': id
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					let imgs = returnData.img.split(',')
					this.title = returnData.title
					this.id = returnData.id;
					this.imgs = imgs;
					this.buyReason = returnData.buyReason
					this.coverImg = returnData.coverImg
					this.title = returnData.title;
					this.commissionPrice = returnData.commissionPrice,
					this.originalPrice = returnData.originalPrice;
					this.typeId = returnData.typeId;
					this.twoTypeId = returnData.twoTypeId
					this.threeTypeId = returnData.threeTypeId
					this.merchants = returnData.merchants;
					this.price = returnData.price;
					this.sales = returnData.sales;
					this.overdueRent = returnData.overdueRent
					this.memberPrice = returnData.memberPrice;
					this.depositMoney = returnData.depositMoney
					if(returnData.dailyRent){
						this.dailyRent = returnData.dailyRent
					}else{
						this.dailyRent = 0
					}
					if(returnData.memberRent){
						this.memberRent = returnData.memberRent
					}else{
						this.memberRent = 0
					}
					if(returnData.oldDegree){
						this.oldDegree = returnData.oldDegree
					}else{
						this.oldDegree = this.degreeList[0]
					}
					if(returnData.labelId){
						this.labelId =  returnData.labelId
					}else{
						this.labelId = ''
					}
					
					
					
					
					
					this.descrition = returnData.descrition;
					this.status = returnData.status;
					this.createAt = returnData.createAt;
					this.isSelect = returnData.isSelect;
					this.homeGoods = returnData.homeGoods;
					this.isRecommend = returnData.isRecommend;
					if(returnData.isExpress){
						this.isExpress = returnData.isExpress.split(',')
						console.log('-11----------',this.isExpress)
						if(this.isExpress.length>0){
							for(var i in this.isExpress){
								if(Number(this.isExpress[i]) ==2){
									this.isExpress1 = 2 
								}
							}
						}
					}else{
						this.isExpress = []
					}
					
					this.createAt = returnData.createAt;
					this.brandId = returnData.brandId;
					let label = JSON.parse(returnData.type.parentId)
					let value = JSON.parse(returnData.type.id)
					this.value = [label, value]
					if (returnData.postagePrice) {
						if (returnData.postagePrice == '') {
							this.postagePrice = 0
						} else {
							this.postagePrice = returnData.postagePrice
						}
					} else {
						this.postagePrice = 0
					}
					if (this.postagePrice > 0) {
						this.positage = 1
						this.priceshow = true
					} else {
						this.positage = 0
						this.priceshow = false
					}
					this.attr = returnData.attr
					if (this.attr.length > 0) {
						this.guige = 1
						this.Triggerselect()
						this.create2()
						this.attrName = returnData.attr[0].attrName
						this.attrValue = returnData.attr[0].attrValue
						this.ruleId = returnData.attr[0].ruleId
						this.goodsId = returnData.attr[0].goodsId
						this.id = returnData.attr[0].id
					} else {
						this.guige = 0
						this.echo(this.id)
						this.ruleId = 0
						this.sku = returnData.sku
					}
					for (var i in this.typeDatas) {
						if (this.typeId == this.typeDatas[i].value) {
							this.twoTypeDatas = this.typeDatas[i].children
							for (var j in this.twoTypeDatas) {
								if (this.twoTypeId == this.twoTypeDatas[j].value) {
									this.threeTypeDatas = this.twoTypeDatas[j].children
								}
							}
						}
					}
				})
			},
			// 修改商品
			artiReleass() {
				let id = this.$route.query.id
				if (this.imgs == '') {
					this.img = this.potost
					this.img = this.img.substr(0, this.img.length - 1)
				} else {
					this.img = this.potost + this.imgs
				}
				if (this.sku.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品规格',
						type: 'warning'
					});
					return
				}
				if (this.isExpress1 == 2) {
					if (this.postagePrice === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入邮费',
							type: 'warning'
						});
						return
					}
				}
				if (this.brandId == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品品牌',
						type: 'warning'
					});
					return
				}
				if (this.depositMoney === ''||this.depositMoney ==null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入押金',
						type: 'warning'
					});
					return
				}
				if (this.overdueRent === ''||this.overdueRent ==null) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入逾期日租金',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/goods/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': id,
						'img': this.img,
						'title': this.title,
						'typeId': this.typeId,
						'twoTypeId': this.twoTypeId,
						'threeTypeId': this.threeTypeId,
						'merchants': this.merchants,
						'originalPrice': this.originalPrice,
						'price': this.price,
						'memberPrice': this.memberPrice,
						'sales': this.sales,
						'brandId': this.brandId,
						'status': this.status,
						'coverImg': this.coverImg,
						'descrition': this.descrition,
						'commissionPrice': this.commissionPrice,
						'isSelect': this.isSelect,
						'homeGoods': this.homeGoods,
						'buyReason': this.buyReason,
						'isRecommend': this.isRecommend,
						'isExpress': this.isExpress.toString(),
						'postagePrice': this.postagePrice,
						'sku': this.sku,
						'attr': this.attr,
						'createAt': this.createAt,
						'dailyRent': this.dailyRent,
						'memberRent': this.memberRent,
						'oldDegree': this.oldDegree,
						'depositMoney':this.depositMoney,
						'labelId':this.labelId,
						'overdueRent':this.overdueRent,
						
					})
				}).then(({
					data
				}) => {
					if(data.status==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.$router.push({
									path: '/shopAdmin'
								})
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
			},

			// 商品规格
			specifSelect() {
				this.$http({
					url: this.$http.adornUrl2('/goods/info'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.specifdata = returnData;
				})
			},
			// 商品规格
			Triggerselect() {
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl2('/goods/findAttrValue'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId': id
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.ruleValue = returnData.attrValue
					this.attrName = returnData.attrName
					this.ruleshow = true
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
			},
			// 查询商品规格
			selectTrigger(val) {
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsRule/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id': val
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.ruleValue = returnData.ruleValue
					this.attrName = returnData.ruleName
					this.ruleshow = true
					this.ruleId = returnData.id
					this.create()
				})
			},
			deleterule(index) {
				this.ruleValue.splice(index, 1);
			},
			btnToclick() {
				this.isshow = false
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTotwo() {
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTo() {
				if (this.releobject.value == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入规格',
						type: 'warning'
					});
					return
				}
				if (this.releobject.detail == '') {
					this.$message({
						title: '提示',
						duration: 1800,
						type: 'error',
						message: '请输入规格值',
						type: 'warning'
					});
					return
				}
				this.ruleValue.push({
					value: this.releobject.value,
					detail: this.releobject.detail
				});
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			handleClose(tag, j, item) {
				var detailarr = item.detail.split(',')
				detailarr.splice(j, 1);
				if (detailarr.length < 1) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '规格值至少要有一个',
						type: 'warning'
					});
					return
				} else {
					item.detail = String(detailarr)
				}
			},
			handleInputConfirm(index, item) {
				let inputValue = this.inputValues[index];
				if (inputValue) {
					var detailarr = item.detail.split(',')
					detailarr.push(inputValue)
					for (var i = 0; i < detailarr.length; i++) {
						for (var j = 0; j < detailarr.length; j++) {
							if (detailarr[i] == detailarr[j] && i != j) {
								detailarr.splice(j, 1);
							}
						}
					}
					item.detail = String(detailarr)
					this.inputValues[index] = '';
				}
				this.inputVisible = false;
			},
			// 初始化立即生成
			create2() {
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl2('/goods/formatAttr'),
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
					this.valueshow = true
					this.valueshow1 = false
					this.sku = returnData.value
					this.detailJson = returnData.value[0].detailJson.split(',')
				})
			},
			// 立即生成
			create() {
				this.attr = []
				this.attr.push({
					attrName: this.attrName,
					attrValue: this.ruleValue,
					ruleId: this.ruleId
				})
				if (this.originalPrice == '') {
					this.originalPrice = '0'
				}
				if (this.price == '') {
					this.price = '0'
				}
				if (this.memberPrice == '') {
					this.memberPrice = '0'
				}
				if (this.dailyRent == '') {
					this.dailyRent = '0'
				}
				if (this.memberRent == '') {
					this.memberRent = '0'
				}
				if (this.oldDegree == '') {
					this.oldDegree = this.degreeList[0]
				}
				this.$http({
					url: this.$http.adornUrl2(
						`/goods/isFormatAttr?coverImg=${this.coverImg}&originalPrice=${this.originalPrice}&price=${this.price}&memberPrice=${this.memberPrice}&dailyRent=${this.dailyRent}&memberRent=${this.memberRent}&oldDegree=${this.oldDegree}`
					),
					method: 'post',
					data: this.attr[0]
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.headerData = returnData.header
					this.valueData = returnData.value
					this.valueshow = true
					this.valueshow1 = false
					this.sku = returnData.value
					this.detailJson = returnData.value[0].detailJson.split(',')
				})
			},
			// 获取品牌
			brandSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsBrand/result'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.brandIds = returnData
				})
			},
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goodsType/result'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.typeDatas = returnData
				})
			},
			// 获取新旧程度
			xianshi () {
			  this.$http({
			    url: this.$http.adornUrl2('/common/type/525'),
			    method: 'get',
			    data: this.$http.adornData({})
			  }).then(({data}) => {
				if (data.code == 0) {
						this.degreeList = data.data.value.split(',')
				}
			  })
			},
			isExpressBtn(value){
				console.log(value,'-----',this.isExpress)
				this.isExpress = value
				if(this.isExpress.length>0){
					for(var i in this.isExpress){
						if(Number(this.isExpress[i]) ==2){
							this.isExpress1 = 2 
							return
						}else{
							this.isExpress1 = ''
						}
					}
				}
			},
			// 标签列表
			dataSelectLabel() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/admin/labelType/getLabelTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page':'',
						'limit':'',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.labelList = returnData.records
				})
			},
		},
		mounted() {
			this.init()
			this.dataSelect()
			this.specifSelect()
			this.brandSelect()
			this.xianshi()
			this.dataSelectLabel()
		},

	}
</script>

<style scoped>
.shop-amend-page {
	padding: 12px 16px;
	max-width: 100%;
	overflow-x: hidden;
}

.shop-amend-page__section {
	margin-bottom: 12px;
}

.shop-amend-page__back {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	margin-bottom: 12px;
	font-size: 14px;
	color: var(--nf-text-secondary, #606266);
	text-decoration: none;
	line-height: 1;
}

.shop-amend-page__back:hover {
	color: var(--nf-brand, #15cad2);
}

.shop-amend-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	flex-shrink: 0;
}

.shop-amend-page__form-grid {
	display: flex;
	flex-wrap: wrap;
	gap: 12px 16px;
}

.shop-amend-page__form-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
	min-width: 200px;
	flex: 1 1 200px;
	max-width: 280px;
}

.shop-amend-page__form-item--wide {
	max-width: 100%;
	flex: 1 1 100%;
}

.shop-amend-page__form-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.shop-amend-page__form-control {
	width: 100%;
}

.shop-amend-page__upload-row {
	display: flex;
	align-items: flex-start;
	gap: 12px;
	margin-bottom: 12px;
}

.shop-amend-page__upload-row--carousel {
	align-items: flex-start;
	flex-wrap: wrap;
}

.shop-amend-page__upload-label {
	flex-shrink: 0;
	width: 90px;
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 148px;
	text-align: right;
}

.shop-amend-page__upload-row--carousel .shop-amend-page__upload-label {
	line-height: 1;
	padding-top: 12px;
}

.shop-amend-page__upload-box {
	width: 148px;
	height: 148px;
	background-color: #fbfdff;
	border: 1px dashed #c0ccda;
	border-radius: 6px;
	text-align: center;
	line-height: 148px;
	cursor: default;
}

.shop-amend-page__cover-img {
	width: 140px;
	height: 140px;
}

.shop-amend-page__upload-icon {
	font-size: 28px;
	color: #8c939d;
}

.shop-amend-page__carousel-wrap {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
}

.shop-amend-page__carousel-item {
	position: relative;
	margin: 0;
}

.shop-amend-page__carousel-img {
	width: 140px;
	height: 140px;
	border-radius: 6px;
}

.shop-amend-page__spec-row {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-bottom: 12px;
}

.shop-amend-page__spec-select {
	margin-left: 8px;
}

.shop-amend-page__rule-block {
	padding-left: 10px;
	margin-bottom: 12px;
}

.shop-amend-page__rule-actions {
	margin-top: 16px;
}

.shop-amend-page__rule-form {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
	margin-top: 16px;
}

.shop-amend-page__rule-form-item {
	display: inline-flex;
	align-items: center;
	gap: 8px;
}

.shop-amend-page__rule-form-label {
	width: 80px;
	text-align: right;
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
}

.shop-amend-page__attr-block {
	display: flex;
	flex-direction: column;
	gap: 8px;
	margin-top: 12px;
}

.shop-amend-page__property-table {
	width: 100%;
	overflow-x: auto;
}

.shop-amend-page__property-table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.shop-amend-page__property-table .el-table td,
.shop-amend-page__property-table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.shop-amend-page__sku-upload {
	width: 60px;
	height: 60px;
	background-color: #fbfdff;
	border: 1px dashed #c0ccda;
	border-radius: 6px;
	text-align: center;
	line-height: 60px;
}

.shop-amend-page__editor {
	width: 100%;
}

.shop-amend-page__quill {
	padding-bottom: 50px;
	height: 300px;
	width: 100%;
	max-width: 100%;
	margin-bottom: 16px;
}

.shop-amend-page__footer {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
	gap: 12px;
	padding: 16px 0 8px;
}

.shop-amend-page__footer .el-button--primary,
.shop-amend-page__rule-actions .el-button--primary,
.shop-amend-page__rule-form .el-button--primary {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.shop-amend-page__footer .el-button--primary:hover,
.shop-amend-page__footer .el-button--primary:focus,
.shop-amend-page__rule-actions .el-button--primary:hover,
.shop-amend-page__rule-actions .el-button--primary:focus,
.shop-amend-page__rule-form .el-button--primary:hover,
.shop-amend-page__rule-form .el-button--primary:focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.ruleitem {
	padding-left: 10px;
	margin-top: 15px;
}

.ruleitem .ruleicon {
	position: relative;
	top: 3px;
	left: 2px;
	width: 1.2em;
	height: 1.2em;
}

.btn_specif {
	display: flex;
	flex-wrap: wrap;
	gap: 8px;
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

.el-tag--medium {
	margin-right: 10px;
}

.el-tag + .el-tag {
	margin-left: 10px;
}

.button-new-tag {
	height: 32px;
	line-height: 31px;
	padding-top: 0;
	padding-bottom: 0;
	border-radius: 4px;
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
	background-color: #f5f7fa;
	position: relative;
	left: -5px;
	border-color: #dcdfe6;
	border: 1px solid #dcdfe6;
}

.input-new-tag {
	width: 90px;
	margin-left: 10px;
	vertical-align: bottom;
}

.hide .el-upload--picture-card {
	display: none;
}

.shop-amend-page >>> .el-table--enable-row-transition .el-table__body td {
	text-align: center;
}

.shop-amend-page >>> .imgWrap .avatar-uploader .el-upload {
	width: 60px;
}

.shop-amend-page >>> .el-table .cell {
	text-align: center !important;
}

.shop-amend-page >>> .el-input--small .el-input__inner {
	border-top-right-radius: 0;
	border-bottom-right-radius: 0;
}

@media (max-width: 768px) {
	.shop-amend-page__form-item {
		max-width: 100%;
		flex: 1 1 100%;
	}

	.shop-amend-page__upload-label {
		line-height: 1;
		padding-top: 12px;
	}
}
</style>
