<template>
	<div class="mission-add-page">
		<!-- hero -->
		<div class="nf-page-hero mission-add-page__hero">
			<a href="#" class="mission-add-page__back" @click.prevent="prev">
				<icon-svg name="jiantou" class="mission-add-page__back-icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-hero__badge">任务中心</span>
			<h2 class="nf-page-hero__title">添加任务</h2>
			<p class="nf-page-hero__desc">填写任务标题、分类、幸运值与标题图片，编辑任务详情内容后发布</p>
		</div>

		<!-- 基本信息白卡 -->
		<div class="nf-filter-card mission-add-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-edit-outline"></i>
				<span>基本信息</span>
			</div>
			<div class="mission-add-page__form-grid">
				<div class="mission-add-page__form-item mission-add-page__form-item--wide">
					<label class="mission-add-page__form-label">任务标题</label>
					<el-input class="mission-add-page__form-control" placeholder="请输入任务标题" v-model="title"
						autosize></el-input>
				</div>
				<div class="mission-add-page__form-item">
					<label class="mission-add-page__form-label">任务分类</label>
					<el-select v-model="classifyId" placeholder="请选择任务分类" class="mission-add-page__form-control">
						<el-option v-for="(item,index) in artiCletypes" :key="item.index" :label="item.classifyName"
							:value="item.id">
						</el-option>
					</el-select>
				</div>
				<div class="mission-add-page__form-item">
					<label class="mission-add-page__form-label">幸运值</label>
					<el-input class="mission-add-page__form-control" placeholder="请输入幸运值" type="number"
						v-model="luckyValue" autosize></el-input>
				</div>
			</div>
		</div>

		<!-- 标题图片白卡 -->
		<div class="nf-filter-card mission-add-page__section">
			<div class="nf-filter-card__head">
				<i class="el-icon-picture-outline"></i>
				<span>标题图片</span>
			</div>
			<div class="mission-add-page__upload-row">
				<label class="mission-add-page__upload-label">任务封面</label>
				<el-upload v-model="titlePicture" :class="{hide:hideUpload}"
					:action="$http.adornUrl2('/alioss/upload')" :limit="1" :on-success="handleUploadSuccess"
					list-type="picture-card" :on-remove="handleRemove" :on-change="handleChange">
					<i class="el-icon-plus"></i>
				</el-upload>
			</div>
		</div>

		<!-- 任务内容白卡 -->
		<div class="nf-table-card mission-add-page__section">
			<div class="nf-filter-card__head mission-add-page__content-head">
				<i class="el-icon-document"></i>
				<span>任务内容</span>
			</div>
			<div class="mission-add-page__editor tinymce-content">
				<textarea id="tinymceId" :value="content" class="tinymce-textarea" />
			</div>
		</div>

		<div class="mission-add-page__footer">
			<el-button size="mini" type="primary" icon="document" @click="prev">返回上一页</el-button>
			<el-button size="mini" type="primary" icon="document" @click="artiReleass">发布任务</el-button>
		</div>
	</div>
</template>

<script>
    import load from './tinymce-components/dynamicLoadScript'
    import toolbar from './tinymce-components/toolbar'
    import plugins from './tinymce-components/plugins'

    const tinymceCDN = 'https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js'

    export default {
        name: 'Tinymce',
        data() {
            return {
                classifyId: '',
                title: '',
                luckyValue: '',
				titlePicture:'',
				isShow: false,
				hideUpload: false,
				limit:1,
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
                content: ''
            }
        },
        watch: {
            content(val) {
                if (!this.hasChange && this.hasInit) {
                    this.$nextTick(() => window.tinymce.get(this.tinymceId).setContent(val || ''))
                }
            }
        },
        methods: {
			handleChange(file, fileList){
				this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			//上传成功
			handleUploadSuccess(file,fileList) {
			  this.titlePicture= file.data
			},
			// 返回上一级
			prev(){
				this.$router.back()
			},
			// 添加任务
			artiReleass(){
				if (this.title == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入任务标题',
				        type: 'warning'
				    });
				    return
				}
				if (this.classifyId == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务分类',
				        type: 'warning'
				    });
				    return
				}
				if (this.luckyValue == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务幸运值',
				        type: 'warning'
				    });
				    return
				}
				if (this.content == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务内容',
				        type: 'warning'
				    });
				    return
				}
				this.$http({
				  url: this.$http.adornUrl2('helpTaskPlatform/saveHelpTaskPlatform'),
				  method: 'post',
				  data: this.$http.adornData({
				    'title':this.title,
				    'classifyId':this.classifyId,
				    'content':this.content,
				    'luckyValue':this.luckyValue,
				    'titlePicture':this.titlePicture
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
				    this.$message({
				      message: '添加成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
				        this.$router.push({path: '/mission'});
				      }
				    })
				})
			},
            init() {
                load(tinymceCDN, (err) => {
                    if (err) {
                        this.$message.error(err.message)
                        return
                    }
                    this.initTinymce()
                })
            },

            initTinymce() {
                const _this = this
                window.tinymce.init({
                    selector: `#${this.tinymceId}`,
                    images_upload_handler: function (blobInfo, success, failure) {
                        var xhr, formData;

                        xhr = new XMLHttpRequest();
                        xhr.withCredentials = false;
                        xhr.open('POST', this.$http.adornUrl2('/alioss/upload'));
                        xhr.onload = function () {
                            var json;

                            if (xhr.status != 200) {
                                failure('HTTP Error: ' + xhr.status);
                                return;
                            }
                            json =JSON.parse(xhr.responseText);
                            if (json == "" || json == null) {
                                failure('Invalid JSON: ' + xhr.responseText);
                                return;
                            }
                            success(json.data);
                        };
                        formData = new FormData();
                        formData.append('file', blobInfo.blob(), blobInfo.filename());
                        xhr.send(formData);
                    },
                    language: this.languageTypeList['zh'],
                    height: this.height,
                    body_class: 'panel-body ',
                    object_resizing: false,
                    toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
                    plugins,
                    menubar: this.menubar,
                    end_container_on_empty_block: true,
                    powerpaste_word_import: 'clean',
                    code_dialog_height: 450,
                    code_dialog_width: 1000,
                    advlist_bullet_styles: 'square',
                    advlist_number_styles: 'default',
                    imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
                    default_link_target: '_blank',
                    link_title: false,
                    // Image
                    imagetools_toolbar: 'rotateleft rotateright | flipv fliph | editimage imageoptions',
                    nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
                    init_instance_callback: editor => {
                        if (_this.content) {
                            editor.setContent(_this.content)
                        }
                        _this.hasInit = true
                        editor.on('NodeChange Change KeyUp SetContent', () => {
                            this.hasChange = true
                            this.content = editor.getContent()
                        })
                    },
                    setup(editor) {
                        editor.on('FullscreenStateChanged', (e) => {
                            _this.fullscreen = e.state
                        })
                    }
                })
            },
            destroyTinymce() {
                const tinymce = window.tinymce.get(this.tinymceId)
                if (this.fullscreen) {
                    tinymce.execCommand('mceFullScreen')
                }
                if (tinymce) {
                    tinymce.destroy()
                }
            },
			// 获取分类
			classifySelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl2('helpClassify/selectClassifyList'),
				method: 'get',
				params: this.$http.adornParams({
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.artiCletypes = returnData
			  })
			},
        },
        mounted() {
            this.init()
			this.classifySelect()
        },
        beforeDestroy() {
            this.destroyTinymce()
        },
        activated() {
            if (window.tinymce) {
                this.initTinymce()
            }
        },
        deactivated() {
            this.destroyTinymce()
        }
    }
</script>

<style scoped>
.mission-add-page {
	padding: 12px 16px;
	max-width: 100%;
	overflow-x: hidden;
}

.mission-add-page__section {
	margin-bottom: 12px;
}

.mission-add-page__back {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	margin-bottom: 12px;
	font-size: 14px;
	color: var(--nf-text-secondary, #606266);
	text-decoration: none;
	line-height: 1;
}

.mission-add-page__back:hover {
	color: var(--nf-brand, #15cad2);
}

.mission-add-page__back-icon {
	width: 1.2em;
	height: 1.2em;
	flex-shrink: 0;
}

.mission-add-page__form-grid {
	display: flex;
	flex-wrap: wrap;
	gap: 12px 16px;
}

.mission-add-page__form-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
	min-width: 200px;
	flex: 1 1 200px;
	max-width: 280px;
}

.mission-add-page__form-item--wide {
	max-width: 100%;
	flex: 1 1 100%;
}

.mission-add-page__form-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.mission-add-page__form-control {
	width: 100%;
}

.mission-add-page__upload-row {
	display: flex;
	align-items: flex-start;
	gap: 12px;
}

.mission-add-page__upload-label {
	flex-shrink: 0;
	width: 90px;
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 148px;
	text-align: right;
}

.mission-add-page__content-head {
	padding: 12px 16px 0;
}

.mission-add-page__editor {
	padding: 12px 16px 16px;
	width: 100%;
}

.mission-add-page__footer {
	display: flex;
	justify-content: center;
	flex-wrap: wrap;
	gap: 12px;
	padding: 16px 0 8px;
}

.mission-add-page__footer .el-button--primary {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.mission-add-page__footer .el-button--primary:hover,
.mission-add-page__footer .el-button--primary:focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.tinymce-textarea {
	visibility: hidden;
	z-index: -1;
}

.mission-add-page >>> .mce-fullscreen {
	z-index: 10000;
}

.hide >>> .el-upload--picture-card {
	display: none;
}

@media (max-width: 768px) {
	.mission-add-page__form-item {
		max-width: 100%;
		flex: 1 1 100%;
	}

	.mission-add-page__upload-label {
		line-height: 1;
		padding-top: 12px;
	}
}
</style>
