<template>
  <div class="inte-amend-page">
    <!-- hero -->
    <div class="nf-page-hero inte-amend-page__hero">
      <a href="#" class="inte-amend-page__back" @click.prevent="prev">
        <icon-svg name="jiantou" class="inte-amend-page__back-icon"></icon-svg>
        返回
      </a>
      <span class="nf-page-hero__badge">积分商城</span>
      <h2 class="nf-page-hero__title">积分商品修改</h2>
      <p class="nf-page-hero__desc">编辑积分商品信息，配置标题、积分、规格、图片与详情内容</p>
    </div>

    <!-- 基本信息白卡 -->
    <div class="nf-filter-card inte-amend-page__section">
      <div class="nf-filter-card__head">
        <i class="el-icon-edit-outline"></i>
        <span>基本信息</span>
      </div>
      <div class="inte-amend-page__form-grid">
        <div class="inte-amend-page__form-item">
          <label class="inte-amend-page__form-label">商品标题</label>
          <el-input class="inte-amend-page__form-control" placeholder="请输入商品标题" v-model="title" autosize></el-input>
        </div>
        <div class="inte-amend-page__form-item">
          <label class="inte-amend-page__form-label">商品积分</label>
          <el-input class="inte-amend-page__form-control" @keyup.native="proving" placeholder="请输入商品积分" type="number" :min="1" :controls="false" v-model="price" autosize></el-input>
          <p class="inte-amend-page__form-hint">注:积分等于商品加邮费</p>
        </div>
        <div class="inte-amend-page__form-item">
          <label class="inte-amend-page__form-label">商品销量</label>
          <el-input class="inte-amend-page__form-control" @keyup.native="proving" placeholder="请输入商品销量" type="number" :min="1" :controls="false" v-model="sales" autosize></el-input>
        </div>
        <div class="inte-amend-page__form-item">
          <label class="inte-amend-page__form-label">商品状态</label>
          <el-select v-model="status" class="inte-amend-page__form-control" placeholder="请选择商品类型">
            <el-option v-for="(item,index) in statusmain2" :key="item.index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </div>
        <div class="inte-amend-page__form-item">
          <label class="inte-amend-page__form-label">商品参数</label>
          <el-input class="inte-amend-page__form-control" placeholder="请输入商品参数" v-model="buyReason" autosize></el-input>
        </div>
        <div class="inte-amend-page__form-item">
          <label class="inte-amend-page__form-label">是否发货</label>
          <el-select v-model="isExpress" class="inte-amend-page__form-control" placeholder="请选择商品是否发货">
            <el-option v-for="(item,index) in isExpressmain" :key="item.index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </div>
      </div>
    </div>

    <!-- 商品图片白卡 -->
    <div class="nf-filter-card inte-amend-page__section">
      <div class="nf-filter-card__head">
        <i class="el-icon-picture-outline"></i>
        <span>商品图片</span>
      </div>
      <div class="inte-amend-page__upload-row">
        <label class="inte-amend-page__upload-label">商品封面图</label>
        <div class="inte-amend-page__upload-box">
          <el-upload
            class="avatar-uploader"
            v-model="coverImg"
            :action="$http.adornUrl2('/alioss/upload')"
            :show-file-list="false"
            :on-success="handleAvatarSuccess1">
            <img v-if="coverImg" :src="coverImg" class="avatar inte-amend-page__cover-img"/>
            <i v-else class="el-icon-plus avatar-uploader-icon inte-amend-page__upload-icon"></i>
          </el-upload>
        </div>
      </div>
      <div class="inte-amend-page__upload-row inte-amend-page__upload-row--carousel">
        <label class="inte-amend-page__upload-label">商品轮播图</label>
        <div class="inte-amend-page__carousel-list">
          <div v-for="(item,index) of imgs" :key="index" class="inte-amend-page__carousel-item">
            <img :src="item" class="avatar inte-amend-page__carousel-img"/>
            <div @click="clear(index)" class="divhove">
              <i class="el-icon-delete inte-amend-page__delete-icon"></i>
            </div>
          </div>
          <el-upload
            :action="$http.adornUrl2('/alioss/upload')"
            list-type="picture-card"
            :on-success="handleUploadSuccess"
            :on-change="handleChange"
            :on-remove="handleRemove">
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
      </div>
    </div>

    <!-- 商品规格白卡 -->
    <div class="nf-filter-card inte-amend-page__section">
      <div class="nf-filter-card__head">
        <i class="el-icon-s-goods"></i>
        <span>商品规格</span>
      </div>
      <div class="inte-amend-page__spec-row">
        <label class="inte-amend-page__form-label">规格类型</label>
        <el-radio-group v-model="guige" @change="guigeChange">
          <el-radio :label="0">单规格</el-radio>
          <el-radio :label="1">多规格</el-radio>
        </el-radio-group>
        <div v-if="guigeshow" class="inte-amend-page__spec-select">
          <el-select v-model="ruleId" placeholder="请选择商品规格" class="inte-amend-page__form-control" @change="selectTrigger(ruleId)">
            <el-option v-for="(item,index) in specifdata" :key="item.index" :label="item.ruleName" :value="item.id"></el-option>
          </el-select>
        </div>
      </div>
      <div v-if="ruleValue" class="inte-amend-page__rule-block">
			<div class="ruleitem" v-for="(item,index) in ruleValue" :key="index">
				<div>{{item.value}} <span @click="deleterule(index)"><icon-svg name="schu" class="ruleicon"></icon-svg></span></div>
				<div>
					<el-tag
					  :key="tag"
					  v-for="(tag,j) in item.detail.split(',')"
					  closable
					  :disable-transitions="false"
					  @close="handleClose(tag,j,item)">
					  {{tag}}
					</el-tag>
					<el-input
					  class="input-new-tag"
					  v-model="inputValues[index]"
					  ref="saveTagInput"
					  size="small"
					  style="width:120px;"
					  placeholder="请输入属性名"
					  @keyup.enter.native="handleInputConfirm(index,item)">
					</el-input>
					<el-button class="button-new-tag" size="small" @click="handleInputConfirm(index,item)">添加</el-button>
				</div>
			</div>
			<div class="inte-amend-page__rule-actions" v-if="ruleshow">
				<div v-if="isshow" class="btn_specif">
					<el-button type="primary" size="mini" @click="btnToclick()">添加新规格</el-button>
					<el-button type="success" size="mini" @click="create()">立即生成</el-button>
				</div>
			</div>
			<div v-if="!isshow" class="inte-amend-page__spec-inline">
				<div class="inte-amend-page__spec-field">
					<span class="inte-amend-page__spec-field-label">规格</span>
					<el-input v-model="releobject.value" placeholder="例:颜色"></el-input>
				</div>
				<div class="inte-amend-page__spec-field">
					<span class="inte-amend-page__spec-field-label">规格值</span>
					<el-input v-model="releobject.detail" placeholder="例:黑色"></el-input>
				</div>
				<div class="inte-amend-page__spec-field-actions">
					<el-button type="primary" size="mini" @click="speciTo()">确 定</el-button>
					<el-button size="mini" @click="speciTotwo()">取 消</el-button>
				</div>
			</div>
		</div>
		<div v-if="valueshow" class="inte-amend-page__attr-block">
			<label class="inte-amend-page__form-label">商品属性</label>
			<div class="property_table inte-amend-page__property-table">
				<table style="width: 100%;">
					<tr style="display:flex;text-align: left;">
						<th style="width:120px;" v-for="item in headerData">{{item}}</th>
					</tr>
				</table>
				<el-table
				  :data="valueData">
				  <el-table-column
				  v-if="detailJson.length == 1 || detailJson.length == 0"
				   prop="value0"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 2"
				   prop="value0"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 2"
				   prop="value1"
					width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 3"
				   prop="value0"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 3"
				   prop="value1"
					width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 3"
				   prop="value2"
					width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 4"
				   prop="value0"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 4"
				   prop="value1"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 4"
				   prop="value2"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 4"
				   prop="value3"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 5"
				   prop="value0"
					width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 5"
				   prop="value1"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 5"
				   prop="value2"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 5"
				   prop="value3"
				   width="120">
				  </el-table-column>
				  <el-table-column
				  v-if="detailJson.length == 5"
				   prop="value4"
				   width="120">
				  </el-table-column>
				  <el-table-column
					width="100">
					<template slot-scope="scope">
						<div class="imgWrap inte-amend-page__sku-upload">
							<el-upload
								class="avatar-uploader"
								v-model="scope.row.skuImg"
								:action="$http.adornUrl2('/alioss/upload')"
								:show-file-list="false"
								:on-success="handleAvatarSuccess"
								>
								<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar" style="border-radius: 6px;width:50px;height: 50px;"/>
								<i v-else class="el-icon-plus avatar-uploader-icon" @click="curRowIndex=scope.$index"></i>
							</el-upload>
						</div>
					</template>
				  </el-table-column>
				 <!-- <el-table-column
					prop="skuOriginalPrice"
					width="120">
					<template slot-scope="scope">
						<div>
						  <el-input type="text" v-model="scope.row.skuOriginalPrice"  style="width:80px;font-size:14px;">
						  </el-input>
						</div>
					</template>
				  </el-table-column> -->
				  <el-table-column
					prop="skuPrice"
					width="120">
					<template slot-scope="scope">
						<div>
						  <el-input type="text" v-model="scope.row.skuPrice"  style="width:80px;font-size:14px;">
						  </el-input>
						</div>
					</template>
				  </el-table-column>
				  <!-- <el-table-column
					prop="memberPrice"
					width="120">
					<template slot-scope="scope">
						<div>
						  <el-input type="text" v-model="scope.row.memberPrice"  style="width:80px;font-size:14px;">
						  </el-input>
						</div>
					</template>
				  </el-table-column> -->
				  <el-table-column
					prop="stock"
					width="120">
					<template slot-scope="scope">
						<div>
						  <el-input type="text" v-model="scope.row.stock"  style="width:80px;font-size:14px;">
						  </el-input>
						</div>
					</template>
				  </el-table-column>
				  <el-table-column
					prop="sales"
					width="120">
					<template slot-scope="scope">
						<div>
						  <el-input type="text" v-model="scope.row.sales"  style="width:80px;font-size:14px;">
						  </el-input>
						</div>
					</template>
				  </el-table-column>
				  <el-table-column
				    width="120">
				    <template slot-scope="scope">
				      <el-button
				  		:disabled="!isAuth('userList:delete')"
				        size="mini"
				        type="danger"
				        @click="deleter(scope.$index)">删除
				      </el-button>
				    </template>
				  </el-table-column>
				</el-table>
			</div>
		</div>
    <div v-if="valueshow1" class="inte-amend-page__attr-block">
      <label class="inte-amend-page__form-label">商品属性</label>
      <div class="property_table inte-amend-page__property-table">
      	<table style="width: 100%;">
      		<tr style="display:flex;text-align: left;">
      			<th style="width:150px;" v-for="item in headerData2">{{item}}</th>
      		</tr>
      	</table>
        <el-table
          :data="sku">
          <el-table-column
        	width="130">
        	<template slot-scope="scope">
        		<div class="imgWrap inte-amend-page__sku-upload">
        			<el-upload
        				class="avatar-uploader"
        				v-model="scope.row.skuImg"
        				:action="$http.adornUrl2('/alioss/upload')"
        				:show-file-list="false"
        				:on-success="handleAvatarSuccess3"
        				>
        				<img v-if="scope.row.skuImg" :src="scope.row.skuImg" class="avatar" style="border-radius: 6px;width:50px;height: 50px;"/>
        				<i v-else class="el-icon-plus avatar-uploader-icon" @click="curRowIndex=scope.$index"></i>
        			</el-upload>
        		</div>
        	</template>
          </el-table-column>
          <!-- <el-table-column
        	prop="skuOriginalPrice"
        	width="150">
        	<template slot-scope="scope">
        		<div>
        		  <el-input type="text" v-model="scope.row.skuOriginalPrice"  style="width:80px;font-size:14px;">
        		  </el-input>
        		</div>
        	</template>
          </el-table-column> -->
          <el-table-column
        	prop="skuPrice"
        	width="150">
        	<template slot-scope="scope">
        		<div>
        		  <el-input type="text" v-model="scope.row.skuPrice"  style="width:80px;font-size:14px;">
        		  </el-input>
        		</div>
        	</template>
          </el-table-column>
          <!-- <el-table-column
        	prop="memberPrice"
        	width="150">
        	<template slot-scope="scope">
        		<div>
        		  <el-input type="text" v-model="scope.row.memberPrice"  style="width:80px;font-size:14px;">
        		  </el-input>
        		</div>
        	</template>
          </el-table-column> -->
          <el-table-column
        	prop="stock"
        	width="150">
        	<template slot-scope="scope">
        		<div>
        		  <el-input type="text" v-model="scope.row.stock"  style="width:80px;font-size:14px;">
        		  </el-input>
        		</div>
        	</template>
          </el-table-column>
          <el-table-column
        	prop="sales"
        	width="150">
        	<template slot-scope="scope">
        		<div>
        		  <el-input type="text" v-model="scope.row.sales"  style="width:80px;font-size:14px;">
        		  </el-input>
        		</div>
        	</template>
          </el-table-column>
        </el-table>
      </div>
    </div>
      <div class="inte-amend-page__postage-row">
			<label class="inte-amend-page__form-label">是否包邮</label>
			<el-radio-group v-model="positage" @change="agreeChange">
			    <el-radio :label="0">是</el-radio>
			    <!-- <el-radio :label="1">否</el-radio>
				<el-input v-if="priceshow" style="width:200px;margin-left: 10px;" placeholder="请输入邮费" type="number"  v-model="postagePrice" autosize></el-input> -->
			</el-radio-group>
		</div>
    </div>

    <!-- 商品详情白卡 -->
    <div class="nf-filter-card inte-amend-page__section">
      <div class="nf-filter-card__head">
        <i class="el-icon-document"></i>
        <span>商品详情</span>
      </div>
      <div class="tinymce-content inte-amend-page__editor">
    		<quill-editor ref="myTextEditor" v-model="descrition" :options="quillOption"
    			class="inte-amend-page__quill">
    		</quill-editor>
      </div>
    </div>

    <!-- 操作区 -->
	<div class="inte-amend-page__footer">
		<el-button size="mini" type="primary" icon="document" @click="prev">返回上一页</el-button>
		<el-button size="mini" type="primary" icon="document" @click="artiReleass">保存</el-button>
	</div>
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
                type: '',
				typeId:'',
                title: '',
                merchants: '',
				originalPrice:'',
				price:'',
				memberPrice:'',
				coverImg:'',
				guige:'',
				commissionPrice:'',
				postagePrice:0,
				img:'',
				buyReason:'',
				sales:'',
				status:'',
				typeDatas:[],
				detailJson:[],
				imgs:[],
				value:[],
				potost:'',
				attr:[],
				brandId:'',
				brandIds:[],
				statusmain2:[
					{
						value:1,
						label:'上架'
					},
					{
						value:2,
						label:'下架'
					}
				],
				isExpress:'',
				isExpressmain:[
					{
						value:1,
						label:'普通商品需要发货'
					},
					{
						value:2,
						label:'虚拟商品无需发货'
					},
					{
						value:3,
						label:'门店核销无需发货'
					}
				],
				positage:0,
				valueshow:true,
				valueshow1:true,
				priceshow:false,
				specifdata:[],
				ruleValue:[],
				headerData:[],
				valueData:[],
				sku:[],
				releobject:{
					value:'',
					detail:''
				},
				isshow:true,
				ruleshow:false,
				inputValues:{
					inputValue0:'',
					inputValue1:'',
					inputValue2:'',
					inputValue3:'',
					inputValue4:'',
					inputValue5:''
				},
				ruleId:'',
				specif:'',
				propshow:false,
				guigeshow:true,
				attrName:'',
				headerData2:[],
				attrValue:[],
				skuImg:'',
				isSelect:'',
				homeGoods:'',
				isRecommend:'',
				isShow: false,
				hideUpload: false,
				limit:1,
                hasChange: false,
                hasInit: false,
				isJiFenGoods:'',
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
            }
        },
       
        methods: {
			// 验证input只能输入正整数
			proving() {
			  this.price = this.price.replace(/[^\.\d]/g,'');
			  this.price = this.price.replace('.','');
			  this.sales = this.sales.replace(/[^\.\d]/g,'');
			  this.sales = this.sales.replace('.','');
			},
			// 返回上一级
			prev(){
				this.$router.back()
			},
			// 图标上传
			handleAvatarSuccess(file) {
				if(this.curRowIndex == 0){
					this.skuImg = file.data;
					this.valueData[0].skuImg = this.skuImg
					return
				}else{
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
			agreeChange(val){
				if(val == 1){
					this.priceshow = true
					this.postagePrice = ''
				}else{
					this.priceshow = false
					this.postagePrice = 0
				}
			},
      // 是否多规格
      guigeChange(val){
      	if(val == 0){
          this.ruleId = 0
          this.valueshow1 = true
          this.valueshow = false
          this.guigeshow = false
          this.attr = []
          this.ruleshow = false
          this.ruleValue = []
          this.dansku()
        }
        if(val == 1){
          this.ruleId = ''
          this.sku = []
          this.guigeshow = true
          this.valueshow1 = false
        }
      },
     // 获取单规格的sku
     dansku(){
     		  if(this.originalPrice == ''){
     		  	this.originalPrice = '0'
     		  }
     		  if(this.price == ''){
     		  	this.price = '0'
     		  }
     		  if(this.memberPrice == ''){
     		  	this.memberPrice = '0'
     		  }
       this.$http({
       	url: this.$http.adornUrl2('/goods/onlyFormatAttr'),
       	method: 'get',
     				params: this.$http.adornParams({
     				'originalPrice':this.originalPrice,
     				'price':this.price,
     				'memberPrice':this.memberPrice,
     				'coverImg':this.coverImg,
       	})
       }).then(({data}) => {
       	let returnData = data.data;
         this.sku = returnData.value
     		  this.headerData2 = returnData.header
         this.valueshow = false
       	this.propshow = true
         this.guigeshow= false
       })
     },
	 // 回显规格
	 echo(goodsId){
	 	this.$http({
	 		url: this.$http.adornUrl2('/goods/formatAttr'),
	 		method: 'get',
	 			params: this.$http.adornParams({
	 			'goodsId':goodsId,
	 		})
	 	}).then(({data}) => {
	 		let returnData = data.data;
	 		this.sku = returnData.value
	 		this.headerData2 = returnData.header
	 		this.valueshow = false
	 		this.propshow = true
	 		this.guigeshow= false
	 	})
	 },
			handleChange1(value) {
				this.value.forEach(element => {
					this.typeId = element;
				});
			},
			// 删除
			clear(index) {
				this.imgs.splice(index, 1);
			 },
			handleChange(file, fileList){
				this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			handleAvatarSuccess1(file,fileList) {
				this.coverImg = file.data
			},
			//上传成功
			handleUploadSuccess(file,fileList) {
				this.potost+=file.data+','
			},
            init() {
                
              let id = this.$route.query.id
              this.$http({
                url: this.$http.adornUrl2('/goods/find'),
                method: 'get',
                params: this.$http.adornParams({
                  'id':id
                })
              }).then(({data}) => {
                let returnData = data.data;
                let imgs = returnData.img.split(',')
                this.title = returnData.title
                this.id = returnData.id;
                this.imgs = imgs;
                this.buyReason =returnData.buyReason
                this.coverImg =returnData.coverImg
                this.title = returnData.title;
                this.commissionPrice =returnData.commissionPrice,
                this.originalPrice = returnData.originalPrice;
                this.typeId = returnData.typeId;
                this.merchants = returnData.merchants;
                this.price = returnData.price;
                this.sales = returnData.sales;
                this.memberPrice = returnData.memberPrice;
                this.descrition = returnData.descrition;
                this.status = returnData.status;
                this.createAt = returnData.createAt;
                this.isSelect = returnData.isSelect;
                this.homeGoods = returnData.homeGoods;
                this.isRecommend = returnData.isRecommend;
                this.isExpress = returnData.isExpress;
                this.createAt = returnData.createAt;
                this.brandId = returnData.brandId;
                this.postagePrice = returnData.postagePrice
				this.isJiFenGoods = returnData.isJiFenGoods
                if(this.postagePrice > 0){
                  this.positage = 1
                  this.priceshow = true
                }else{
                  this.positage = 0
                  this.priceshow = false
                }

                this.attr = returnData.attr
                if(this.attr.length>0){
                  this.guige = 1
                  this.Triggerselect()
                  this.create2()
                  this.attrName = returnData.attr[0].attrName
                  this.attrValue = returnData.attr[0].attrValue
                  this.ruleId = returnData.attr[0].ruleId
                  this.goodsId = returnData.attr[0].goodsId
                  this.id = returnData.attr[0].id
                }else{
                   this.guige = 0
                   this.echo(this.id)
                   this.ruleId = 0
                   this.sku = returnData.sku
                }
              })
            },
			// 修改商品
			artiReleass(){
				let id = this.$route.query.id
				if(this.imgs == ''){
					this.img = this.potost
					this.img = this.img.substr(0,this.img.length-1)
				}else{
					this.img = this.potost+this.imgs
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
        if (this.postagePrice === '') {
            this.$notify({
                title: '提示',
                duration: 1800,
                message: '请输入邮费',
                type: 'warning'
            });
            return
        }
        this.$http({
          url: this.$http.adornUrl2('/goods/update'),
          method: 'post',
          data: this.$http.adornData({
        	'id':id,
        	'img':this.img,
        	'title':this.title,
        	'merchants':this.merchants,
        	'originalPrice':this.originalPrice,
        	'price':this.price,
        	'memberPrice':this.memberPrice,
        	'sales':this.sales,
			'brandId':this.brandId,
        	'status':this.status,
        	'coverImg':this.coverImg,
        	'descrition':this.descrition,
        	'commissionPrice':this.commissionPrice,
        	'isSelect':this.isSelect,
        	'homeGoods':this.homeGoods,
        	'buyReason':this.buyReason,
        	'isRecommend':this.isRecommend,
        	'isExpress':this.isExpress,
			'isJiFenGoods':this.isJiFenGoods,
        	'postagePrice':this.postagePrice,
        	'sku':this.sku,
        	'attr':this.attr,
        	'createAt':this.createAt
          })
        }).then(({data}) => {
        	this.$message({
        	  message: '操作成功',
        	  type: 'success',
        	  duration: 1500,
        	  onClose: () => {
        		this.$router.push({path: '/integralist'})
        	  }
        	})
        })
			},
            
			// 商品规格
			specifSelect () {
			  this.$http({
				url: this.$http.adornUrl2('/goods/info'),
				method: 'get',
				params: this.$http.adornParams({
				})
			  }).then(({data}) => {
					let returnData = data.data;
					this.specifdata = returnData;
			  })
			},
			// 商品规格
			Triggerselect(){
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl2('/goods/findAttrValue'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId':id
					})
				}).then(({data}) => {
					let returnData = data.data;
					this.ruleValue = returnData.attrValue
					this.attrName = returnData.attrName
					this.ruleshow = true
				})
			},
			// 删除商品属性
			deleter(index){
        let length = this.sku.length
        if(length == 1){
          this.$message({
              title: '提示',
          	type: 'error',
              duration: 1800,
              message: '属性至少要有一个',
              type: 'warning'
          });
          return
        }else{
          this.sku.splice(index, 1);
        }
			},
			// 查询商品规格
			selectTrigger(val){
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsRule/find'),
					method: 'get',
					params: this.$http.adornParams({
						'id':val
					})
				}).then(({data}) => {
					let returnData = data.data;
					this.ruleValue = returnData.ruleValue
					this.attrName = returnData.ruleName
					this.ruleshow = true
					this.ruleId = returnData.id
          this.create()
				})
			},
			deleterule(index){
				this.ruleValue.splice(index, 1);
			},
			btnToclick(){
				this.isshow = false
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTotwo(){
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTo(){
				if(this.releobject.value == ''){
					this.$message({
					    title: '提示',
						type: 'error',
					    duration: 1800,
					    message: '请输入规格',
					    type: 'warning'
					});
					return
				}
				if(this.releobject.detail == ''){
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
					detail:this.releobject.detail
				});
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			handleClose(tag,j,item) {
				var detailarr = item.detail.split(',')
				detailarr.splice(detailarr.indexOf('tag'),1);
				if(detailarr.length < 1){
					this.$message({
					    title: '提示',
						type: 'error',
					    duration: 1800,
					    message: '规格值至少要有一个',
					    type: 'warning'
					});
					return
				}else{
					item.detail = String(detailarr)
				}
			},
			handleInputConfirm(index,item) {
				let inputValue = this.inputValues[index];
				if (inputValue) {
					var detailarr = item.detail.split(',')
					detailarr.push(inputValue)
					for(var i=0;i<detailarr.length;i++){
					   for(var j=0;j<detailarr.length;j++){
							if(detailarr[i] == detailarr[j] && i !=j){
								detailarr.splice(j,1);
							}
						}
					}
					item.detail = String(detailarr)
					this.inputValues[index] = '';
				}
				this.inputVisible = false;
			},
			// 初始化立即生成
			create2(){
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl2('/goods/formatAttr'),
					method: 'get',
					params: this.$http.adornParams({
						'goodsId':id
					})
				}).then(({data}) => {
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
			create(){
				this.attr = []
				this.attr.push({
					attrName:this.attrName,
					attrValue:this.ruleValue,
					ruleId:this.ruleId
				})
				if(this.originalPrice == ''){
					this.originalPrice = '0'
				}
				if(this.price == ''){
					this.price = '0'
				}
				if(this.memberPrice == ''){
					this.memberPrice = '0'
				}
				this.$http({
					url: this.$http.adornUrl2(`/goods/isFormatAttr?coverImg=${this.coverImg}&price=${this.price}`),
					method: 'post',
					data: this.attr[0]
				}).then(({data}) => {
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
      brandSelect () {
      	let page = this.page-1
        this.tableDataLoading = true
        this.$http({
      	url: this.$http.adornUrl2('/selfGoodsBrand/result'),
      	method: 'get',
      	params: this.$http.adornParams({
      	})
        }).then(({data}) => {
      		this.tableDataLoading = false
      		let returnData = data.data;
      		this.brandIds = returnData
        })
      },
			// 商品分类
			dataSelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl2('/goodsType/result'),
				method: 'get',
				params: this.$http.adornParams({
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.typeDatas = returnData
			  })
			}
        },
        mounted() {
          this.init()
          this.specifSelect()
        },
    }
</script>

<style scoped>
.inte-amend-page {
  padding: 12px 16px;
  max-width: 100%;
  overflow-x: hidden;
}

.inte-amend-page__back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 12px;
  font-size: 14px;
  color: var(--nf-text-secondary, #606266);
  text-decoration: none;
  line-height: 1;
}

.inte-amend-page__back:hover {
  color: var(--nf-brand, #15cad2);
}

.inte-amend-page__back-icon {
  width: 1.2em;
  height: 1.2em;
  flex-shrink: 0;
}

.inte-amend-page__form-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 16px;
}

.inte-amend-page__form-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 200px;
  flex: 1 1 200px;
  max-width: 280px;
}

.inte-amend-page__form-label {
  font-size: 13px;
  color: var(--nf-text-secondary, #606266);
  line-height: 1;
}

.inte-amend-page__form-control {
  width: 100%;
}

.inte-amend-page__form-hint {
  margin: 0;
  color: #f56c6c;
  font-size: 12px;
  line-height: 1.4;
}

.inte-amend-page__upload-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.inte-amend-page__upload-row--carousel {
  align-items: center;
  flex-wrap: wrap;
}

.inte-amend-page__upload-label {
  flex-shrink: 0;
  width: 90px;
  font-size: 13px;
  color: var(--nf-text-secondary, #606266);
  line-height: 148px;
  text-align: right;
}

.inte-amend-page__upload-row--carousel .inte-amend-page__upload-label {
  line-height: 1;
  padding-top: 12px;
}

.inte-amend-page__upload-box {
  width: 148px;
  height: 148px;
  background-color: #fbfdff;
  border: 1px dashed #c0ccda;
  border-radius: 6px;
  text-align: center;
  line-height: 148px;
}

.inte-amend-page__cover-img {
  width: 140px;
  height: 140px;
}

.inte-amend-page__upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.inte-amend-page__carousel-list {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.inte-amend-page__carousel-item {
  position: relative;
}

.inte-amend-page__carousel-img {
  width: 148px;
  height: 148px;
  border-radius: 6px;
}

.inte-amend-page__delete-icon {
  margin: 0;
  font-size: 18px;
  color: #fff;
}

.inte-amend-page__spec-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px 16px;
  margin-bottom: 12px;
}

.inte-amend-page__spec-select {
  min-width: 200px;
}

.inte-amend-page__spec-select .inte-amend-page__form-control {
  width: 200px;
}

.inte-amend-page__rule-block {
  margin-top: 8px;
  padding-left: 8px;
}

.inte-amend-page__rule-actions {
  margin-top: 12px;
}

.inte-amend-page__spec-inline {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
}

.inte-amend-page__spec-field {
  display: flex;
  align-items: center;
  gap: 8px;
}

.inte-amend-page__spec-field-label {
  flex-shrink: 0;
  width: 60px;
  text-align: right;
  font-size: 13px;
  color: var(--nf-text-secondary, #606266);
}

.inte-amend-page__spec-field-actions {
  display: flex;
  gap: 8px;
}

.inte-amend-page__attr-block {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 12px;
}

.inte-amend-page__property-table {
  width: 100%;
  overflow-x: auto;
}

.inte-amend-page__property-table .el-table th {
  background: var(--nf-table-header-bg, #f7f8fa);
  color: var(--nf-text-primary, #303133);
  font-weight: 600;
}

.inte-amend-page__property-table .el-table td,
.inte-amend-page__property-table .el-table th {
  border-color: var(--nf-border, #e8eaed);
}

.inte-amend-page__sku-upload {
  width: 60px;
  height: 60px;
  background-color: #fbfdff;
  border: 1px dashed #c0ccda;
  border-radius: 6px;
  text-align: center;
  line-height: 60px;
}

.inte-amend-page__postage-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--nf-border, #e8eaed);
}

.inte-amend-page__editor {
  width: 100%;
}

.inte-amend-page__quill {
  padding-bottom: 50px;
  height: 300px;
  width: 100%;
  max-width: 100%;
  margin-bottom: 16px;
}

.inte-amend-page__footer {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 12px;
  padding: 16px 0 8px;
}

.inte-amend-page__footer .el-button--primary,
.inte-amend-page__rule-actions .el-button--primary:not(.el-button--success),
.inte-amend-page__spec-field-actions .el-button--primary {
  background-color: transparent;
  border-color: var(--nf-brand, #15cad2);
  color: var(--nf-brand, #15cad2);
}

.inte-amend-page__footer .el-button--primary:hover,
.inte-amend-page__footer .el-button--primary:focus,
.inte-amend-page__rule-actions .el-button--primary:not(.el-button--success):hover,
.inte-amend-page__rule-actions .el-button--primary:not(.el-button--success):focus,
.inte-amend-page__spec-field-actions .el-button--primary:hover,
.inte-amend-page__spec-field-actions .el-button--primary:focus {
  background-color: var(--nf-brand-lite, #e3f9fa);
  border-color: var(--nf-brand-deep, #109da4);
  color: var(--nf-brand-deep, #109da4);
}

.hide .el-upload--picture-card {
  display: none;
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
  padding-top: 50%;
  border-radius: 6px;
  background-color: rgba(0, 0, 0, 0.5);
  transition: opacity 0.3s;
}

.divhove:hover {
  opacity: 0.8;
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

.el-tag + .el-tag {
  margin-left: 10px;
}

.el-tag--medium {
  margin-right: 10px;
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
  border: 1px solid #dcdfe6;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

.inte-amend-page >>> .el-input--small .el-input__inner {
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
}

.inte-amend-page >>> .el-table--enable-row-transition .el-table__body td {
  text-align: center;
}

.inte-amend-page >>> .imgWrap .avatar-uploader .el-upload {
  width: 60px;
}

.inte-amend-page >>> .el-table .cell {
  text-align: center !important;
}
</style>
