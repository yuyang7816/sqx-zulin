<template>
  <div class="merchant-clone-page">
    <div class="merchant-clone-page__inner">
      <div class="nf-page-hero merchant-clone-page__hero">
        <span class="nf-page-hero__badge">商家入驻</span>
        <h2 class="nf-page-hero__title">商家入驻申请</h2>
        <p class="nf-page-hero__desc">填写店铺、主体与经营者信息，提交后将在 1–3 个工作日内完成审核，结果将以短信形式通知</p>
      </div>

      <el-form
        @keyup.enter.native="dataFormSubmit()"
        status-icon
        class="merchant-clone-page__form"
      >
        <!-- 店铺信息 -->
        <div class="nf-filter-card merchant-clone-page__section">
          <div class="nf-filter-card__head">
            <i class="el-icon-shop"></i>
            <span>店铺信息</span>
          </div>
          <div class="merchant-clone-page__form-grid">
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">店铺名称</label>
              <el-form-item prop="storeName" class="merchant-clone-page__form-field">
                <el-input v-model="storeName" placeholder="请输入店铺名称"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">店铺类型</label>
              <el-form-item class="merchant-clone-page__form-field">
                <el-select v-model="storeType" placeholder="请选择店铺类型" style="width:100%;" @change="selectTrigger(storeType)">
                  <el-option v-for="item in storeTypes" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">客服电话</label>
              <el-form-item prop="serverPhone" class="merchant-clone-page__form-field">
                <el-input v-model="serverPhone" type="serverPhone" placeholder="请输入客服电话"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">店铺负责人</label>
              <el-form-item prop="legal" class="merchant-clone-page__form-field">
                <el-input v-model="legal" type="legal" placeholder="请输入店铺负责人姓名"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">负责人电话</label>
              <el-form-item prop="legalPhone" class="merchant-clone-page__form-field">
                <el-input v-model="legalPhone" type="legalPhone" placeholder="请输入店铺负责人电话"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item merchant-clone-page__form-item--wide">
              <label class="merchant-clone-page__form-label">店铺地址</label>
              <div class="merchant-clone-page__address-row">
                <el-form-item class="merchant-clone-page__form-field merchant-clone-page__form-field--addr">
                  <el-cascader
                    style="width:100%"
                    size="large"
                    ref="cascaderAddr"
                    :options="options"
                    v-model="storeAddress"
                    placeholder="请选择省份和城市"
                    @change="handleChange"
                  ></el-cascader>
                </el-form-item>
                <el-form-item prop="storeAddressDetail" class="merchant-clone-page__form-field merchant-clone-page__form-field--addr">
                  <el-input v-model="storeAddressDetail" placeholder="请输入店铺详细地址"></el-input>
                </el-form-item>
              </div>
            </div>
          </div>
        </div>

        <!-- 主体信息 -->
        <div v-if="storeType > 1" class="nf-filter-card merchant-clone-page__section">
          <div class="nf-filter-card__head">
            <i class="el-icon-office-building"></i>
            <span>主体信息</span>
          </div>
          <div class="merchant-clone-page__form-grid">
            <div class="merchant-clone-page__form-item" v-if="storeType == 2">
              <label class="merchant-clone-page__form-label">商户名称</label>
              <el-form-item prop="companyName" class="merchant-clone-page__form-field">
                <el-input v-model="companyName" placeholder="请输入商户名称"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item" v-if="storeType == 3">
              <label class="merchant-clone-page__form-label">公司名称</label>
              <el-form-item prop="companyName" class="merchant-clone-page__form-field">
                <el-input v-model="companyName" placeholder="请输入公司名称"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item" v-if="storeType == 4">
              <label class="merchant-clone-page__form-label">组织名称</label>
              <el-form-item prop="companyName" class="merchant-clone-page__form-field">
                <el-input v-model="companyName" placeholder="请输入组织名称"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">营业执照编号</label>
              <el-form-item prop="companyCode" class="merchant-clone-page__form-field">
                <el-input v-model="companyCode" placeholder="请输入营业执照编号"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item merchant-clone-page__form-item--wide">
              <label class="merchant-clone-page__form-label">公司注册地址</label>
              <div class="merchant-clone-page__address-row">
                <el-form-item class="merchant-clone-page__form-field merchant-clone-page__form-field--addr">
                  <el-cascader
                    style="width:100%"
                    size="large"
                    :options="options1"
                    ref="cascaderAddr"
                    v-model="companyAddress"
                    placeholder="请选择省市区"
                    @change="handleChange1"
                  ></el-cascader>
                </el-form-item>
                <el-form-item prop="companyAddressDetail" class="merchant-clone-page__form-field merchant-clone-page__form-field--addr">
                  <el-input v-model="companyAddressDetail" placeholder="请输入公司详细地址"></el-input>
                </el-form-item>
              </div>
            </div>
            <div class="merchant-clone-page__form-item merchant-clone-page__form-item--wide">
              <label class="merchant-clone-page__form-label">营业期限</label>
              <el-form-item class="merchant-clone-page__form-field">
                <el-date-picker
                  style="width: 100%;"
                  v-model="companyTerm"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                ></el-date-picker>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item merchant-clone-page__form-item--wide">
              <label class="merchant-clone-page__form-label">营业执照照片</label>
              <el-form-item class="merchant-clone-page__form-field">
                <div class="merchant-clone-page__upload-box">
                  <el-upload
                    class="avatar-uploader"
                    v-model="companyLicenseImg"
                    :action="$http.adornUrl2('/alioss/upload')"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess1"
                  >
                    <img v-if="companyLicenseImg" :src="companyLicenseImg" class="avatar merchant-clone-page__upload-img" />
                    <i v-else class="el-icon-plus avatar-uploader-icon merchant-clone-page__upload-icon"></i>
                  </el-upload>
                </div>
              </el-form-item>
            </div>
          </div>
        </div>

        <!-- 经营者信息 -->
        <div class="nf-filter-card merchant-clone-page__section">
          <div class="nf-filter-card__head">
            <i class="el-icon-user"></i>
            <span>经营者信息</span>
          </div>
          <div class="merchant-clone-page__form-grid">
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">经营者姓名</label>
              <el-form-item prop="storeHead" class="merchant-clone-page__form-field">
                <el-input v-model="storeHead" placeholder="请输入经营者姓名"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">证件类型</label>
              <el-form-item class="merchant-clone-page__form-field">
                <el-select v-model="idCardType" placeholder="请选择证件类型" style="width:100%;" @change="selectTrigger(storeType)">
                  <el-option v-for="item in idCardTypes" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item">
              <label class="merchant-clone-page__form-label">身份证号码</label>
              <el-form-item prop="idCardNumber" class="merchant-clone-page__form-field">
                <el-input v-model="idCardNumber" placeholder="请输入身份证号码"></el-input>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item merchant-clone-page__form-item--wide">
              <label class="merchant-clone-page__form-label">身份证有效期限</label>
              <el-form-item class="merchant-clone-page__form-field">
                <div class="merchant-clone-page__date-row">
                  <el-date-picker
                    style="width:100%;"
                    v-model="idCardValidTimeStart"
                    value-format="yyyy-MM-dd"
                    type="date"
                    placeholder="身份证有效期限起始时间"
                  ></el-date-picker>
                  <el-date-picker
                    style="width:100%;"
                    v-model="idCardValidTimeEnd"
                    value-format="yyyy-MM-dd"
                    type="date"
                    placeholder="身份证有效期限截止时间"
                  ></el-date-picker>
                </div>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item merchant-clone-page__form-item--wide">
              <label class="merchant-clone-page__form-label">证件照片</label>
              <el-form-item class="merchant-clone-page__form-field">
                <div class="merchant-clone-page__idcard-uploads">
                  <div class="merchant-clone-page__idcard-item">
                    <div class="merchant-clone-page__upload-box">
                      <el-upload
                        class="avatar-uploader"
                        v-model="idCardImg1"
                        :action="$http.adornUrl2('/alioss/upload')"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess2"
                      >
                        <img v-if="idCardImg1" :src="idCardImg1" class="avatar merchant-clone-page__upload-img" />
                        <i v-else class="el-icon-plus avatar-uploader-icon merchant-clone-page__upload-icon"></i>
                      </el-upload>
                    </div>
                    <span class="merchant-clone-page__upload-caption">正面</span>
                  </div>
                  <div class="merchant-clone-page__idcard-item">
                    <div class="merchant-clone-page__upload-box">
                      <el-upload
                        class="avatar-uploader"
                        v-model="idCardImg2"
                        :action="$http.adornUrl2('/alioss/upload')"
                        :show-file-list="false"
                        :on-success="handleAvatarSuccess3"
                      >
                        <img v-if="idCardImg2" :src="idCardImg2" class="avatar merchant-clone-page__upload-img" />
                        <i v-else class="el-icon-plus avatar-uploader-icon merchant-clone-page__upload-icon"></i>
                      </el-upload>
                    </div>
                    <span class="merchant-clone-page__upload-caption">国徽面</span>
                  </div>
                </div>
              </el-form-item>
            </div>
            <div class="merchant-clone-page__form-item merchant-clone-page__form-item--wide">
              <label class="merchant-clone-page__form-label">手持证件照</label>
              <el-form-item prop="companyTerm" class="merchant-clone-page__form-field">
                <div class="merchant-clone-page__idcard-item">
                  <div class="merchant-clone-page__upload-box">
                    <el-upload
                      class="avatar-uploader"
                      v-model="idCardImg3"
                      :action="$http.adornUrl2('/alioss/upload')"
                      :show-file-list="false"
                      :on-success="handleAvatarSuccess4"
                    >
                      <img v-if="idCardImg3" :src="idCardImg3" class="avatar merchant-clone-page__upload-img" />
                      <i v-else class="el-icon-plus avatar-uploader-icon merchant-clone-page__upload-icon"></i>
                    </el-upload>
                  </div>
                  <span class="merchant-clone-page__upload-caption">请务必上传高清图片</span>
                </div>
              </el-form-item>
            </div>
          </div>
        </div>

        <div class="merchant-clone-page__footer">
          <el-button class="login-btn-submit merchant-clone-page__submit" type="primary" @click="dataFormSubmit()">提交</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
  import { getUUID } from '@/utils'
  import { provinceAndCityData, regionData, provinceAndCityDataPlus, regionDataPlus, CodeToText, TextToCode } from 'element-china-area-data'
  export default {
    data () {
      return {
        storeName: '',
        serverPhone: '',
        legal: '',
        legalPhone:'',
        storeAddressDetail:'',
        companyName:'',
        companyCode:'',
        companyAddressDetail:'',
        storeHead:'',
        idCardNumber:'',
        options: provinceAndCityData,
        options1: regionData,
        storeAddress:[],
        storeAddressmain:'',
        storeAddressCity:'',
        storeAddressDistrict:'',
        companyAddress:[],
        companyAddressProvince:'',
        companyAddressCity:'',
        companyAddressDistrict:'',
        companyTerm:'',
        companyLicenseImg:'',
        idCardValidTimeStart:'',
        idCardValidTimeEnd:'',
        idCardImg1:'',
        idCardImg2:'',
        idCardImg3:'',
        storeType:'',
        storeTypes: [
             {
               value: 1,
               label: '个人'
             }, {
               value: 2,
               label: '个体工商户'
             }, {
               value: 3,
               label: '企业'
             }, {
               value: 4,
               label: '其他组织'
             },
          ],
          idCardType:'',
          idCardTypes: [
               {
                 value: 1,
                 label: '中国大陆居民身份证'
               }, {
                 value: 2,
                 label: '中国香港居民来往内地通行证'
               }, {
                 value: 3,
                 label: '中国澳门居民来往内地通行证'
               }, {
                 value: 4,
                 label: '中国台湾居民来往内地通行证'
               },{
                 value: 5,
                 label: '其他国家或地区居民护照'
               }
            ]
      }
    },
    created () {
    },
    methods: {
      handleChange (value) {
        console.log(value)
         value = this.$refs['cascaderAddr'].currentLabels
         this.storeAddressCity = value[0]
         this.storeAddressDistrict = value[1]
      },
      handleChange1 (value) {
         value = this.$refs['cascaderAddr'].currentLabels
         this.companyAddressProvince = value[0]
         this.companyAddressCity = value[1]
         this.companyAddressDistrict = value[2]
      },
      // 图片上传
      handleAvatarSuccess1(file,fileList) {
      	this.companyLicenseImg = file.data
      },
      handleAvatarSuccess2(file,fileList) {
      	this.idCardImg1 = file.data
      },
      handleAvatarSuccess3(file,fileList) {
      	this.idCardImg2 = file.data
      },
      handleAvatarSuccess4(file,fileList) {
      	this.idCardImg3 = file.data
      },
      selectTrigger(val){
        this.storeType = val
      },
      // 提交表单
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl2('/self/merchantApply/save'),
              method: 'post',
              data: this.$http.adornData({
                'storeName': this.storeName,
                'serverPhone':this.serverPhone,
                'legal': this.legal,
                'legalPhone': this.legalPhone,
                'storeAddressCity':this.storeAddressCity,
                'storeAddressDistrict': this.storeAddressDistrict,
                'storeAddressDetail': this.storeAddressDetail,
                'companyName': this.companyName,
                'companyCode': this.companyCode,
                'storeHead': this.storeHead,
                'idCardNumber': this.idCardNumber,
                'companyAddressProvince': this.companyAddressProvince,
                'companyAddressCity':this.companyAddressCity,
                'companyAddressDistrict':this.companyAddressDistrict,
                'companyAddressDetail': this.companyAddressDetail,
                'companyTerm': this.companyTerm,
                'companyLicenseImg': this.companyLicenseImg,
                'idCardValidTimeStart': this.idCardValidTimeStart,
                'idCardValidTimeEnd': this.idCardValidTimeEnd,
                'idCardImg1': this.idCardImg1,
                'idCardImg2': this.idCardImg2,
                'idCardImg3': this.idCardImg3,
                'storeType': this.storeType,
                'idCardType': this.idCardType,
              })
            }).then(({data}) => {
                if(data.status == 0){
                    this.$message({
                    message: '提交成功,我们已收到您的入驻申请，将会在1-3个工作日内审核完毕，审核结果将以短信形式发送至您的手机号',
                    type: 'success',
                    duration: 1500,
                    onClose: () => {
                     this.$router.replace({ name: 'login'})
                    }
                  })
                }
            })
          }
        })
      },
      // 获取数据列表
      dataSelect () {
        let id = this.$route.query.id
        this.$http({
          url: this.$http.adornUrl2('/self/merchantApply/find'),
          method: 'get',
          params: this.$http.adornParams({
            'id':id,
          })
        }).then(({data}) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.storeName = returnData.storeName
          this.storeType = returnData.storeType
          this.serverPhone = returnData.serverPhone
          this.legal = returnData.legal
          this.legalPhone = returnData.legalPhone
          // this.storeAddress = returnData.storeAddress
          console.log(this.storeAddress)
          this.storeAddressDetail = returnData.storeAddressDetail
          this.companyName = returnData.companyName
          this.companyCode = returnData.companyCode
          // this.companyAddress = returnData.companyAddress
          this.companyAddressDetail = returnData.companyAddressDetail
          this.companyTerm = returnData.companyTerm
          this.companyLicenseImg = returnData.companyLicenseImg
          this.storeHead = returnData.storeHead
          this.idCardType = returnData.idCardType
          this.idCardNumber = returnData.idCardNumber
          this.idCardValidTimeStart = returnData.idCardValidTimeStart
          this.idCardValidTimeEnd = returnData.idCardValidTimeEnd
          this.idCardImg1 = returnData.idCardImg1
          this.idCardImg2 = returnData.idCardImg2
          this.idCardImg3 = returnData.idCardImg3
          this.storeType = returnData.storeType
        })
      }
    },
    mounted () {
      this.dataSelect()
    }
  }
</script>

<style scoped>
.merchant-clone-page {
  min-height: 100vh;
  background-color: var(--nf-page-bg, #f4f6fa);
  overflow-x: hidden;
}

.merchant-clone-page__inner {
  max-width: 960px;
  margin: 0 auto;
  padding: 16px;
}

.merchant-clone-page__section {
  margin-bottom: 12px;
}

.merchant-clone-page__form-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 16px;
}

.merchant-clone-page__form-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  min-width: 200px;
  flex: 1 1 280px;
  max-width: 100%;
}

.merchant-clone-page__form-item--wide {
  flex: 1 1 100%;
  max-width: 100%;
}

.merchant-clone-page__form-label {
  font-size: 13px;
  color: var(--nf-text-secondary, #606266);
  line-height: 1;
}

.merchant-clone-page__form-field {
  margin-bottom: 0;
}

.merchant-clone-page__form-field >>> .el-form-item__content {
  line-height: 1;
}

.merchant-clone-page__address-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  width: 100%;
}

.merchant-clone-page__form-field--addr {
  flex: 1 1 240px;
  min-width: 200px;
}

.merchant-clone-page__date-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  width: 100%;
}

.merchant-clone-page__date-row .el-date-editor {
  flex: 1 1 200px;
  min-width: 180px;
}

.merchant-clone-page__upload-box {
  display: inline-block;
  width: 148px;
  height: 148px;
  background-color: #fbfdff;
  border: 1px dashed #c0ccda;
  border-radius: 6px;
  text-align: center;
  line-height: 148px;
}

.merchant-clone-page__upload-img {
  width: 148px;
  height: 148px;
  object-fit: cover;
  border-radius: 6px;
}

.merchant-clone-page__upload-icon {
  font-size: 28px;
  color: #8c939d;
}

.merchant-clone-page__idcard-uploads {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
}

.merchant-clone-page__idcard-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.merchant-clone-page__upload-caption {
  font-size: 12px;
  color: var(--nf-text-secondary, #909399);
  text-align: center;
  line-height: 1.4;
}

.merchant-clone-page__footer {
  display: flex;
  justify-content: center;
  padding: 16px 0 8px;
}

.merchant-clone-page__submit {
  width: 250px;
  height: 40px;
}

.merchant-clone-page >>> .el-form-item__error {
  position: relative;
  padding-top: 4px;
}
</style>
