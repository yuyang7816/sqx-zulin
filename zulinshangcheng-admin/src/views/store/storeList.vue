<template>
	<div class="store-list-page">
		<!-- hero -->
		<div class="nf-page-hero store-list-page__hero">
			<span class="nf-page-hero__badge">商城配置</span>
			<h2 class="nf-page-hero__title">商城配置</h2>
			<p class="nf-page-hero__desc">管理首页轮播、导航、精选商品、为你推荐、升级配置与底部图片等商城展示内容，支持添加、修改与删除</p>
		</div>

		<!-- 表格白卡 + tabs -->
		<div class="nf-table-card store-list-page__table">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="store-list-page__tabs">
		<el-tab-pane label="首页轮播" name="first">
			<div class="store-list-page__toolbar">
				<el-button :disabled="!isAuth('storeList:add')" size="mini" type="primary"
					icon="document" @click="advertNotice">添加轮播</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="advertData">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="imgUrl" label="图片地址">
					<template slot-scope="scope">
						<img :src="scope.row.imgUrl" alt="" width="170px" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="linkUrl" label="跳转链接">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('storeList:update')"
							@click="advertUpdates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('storeList:delete')"
							@click="advertdeletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-tab-pane>
		<el-tab-pane label="首页导航" name="second">
			<div class="store-list-page__toolbar">
				<el-button :disabled="!isAuth('storeList:add')" size="mini" type="primary"
					icon="document" @click="compile()">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="homeData">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="title" label="名称">
				</el-table-column>
				<el-table-column prop="content" label="副标题">
				</el-table-column>
				<el-table-column prop="url" label="路由">
				</el-table-column>
				<el-table-column prop="image_url" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.image_url" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="state" label="类型">
					<template slot-scope="scope">
						<span v-if="scope.row.state==1">金刚区</span>
						<span v-if="scope.row.state==2">精选好物</span>
						<span v-if="scope.row.state==3">热卖榜单</span>
						<span v-if="scope.row.state==4">每日上新</span>
						<span v-if="scope.row.state==5">拼团购</span>
						<span v-if="scope.row.state==6">邀请图</span>
						<span v-if="scope.row.state==7">首页邀请图</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('storeList:update')"
							@click="compile(scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('storeList:delete')"
							@click="compiledeletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>

		</el-tab-pane>
		
		<el-tab-pane label="精选商品" name="fifth">
			<div class="store-list-page__toolbar">
				<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
				<el-button :disabled="!isAuth('storeList:add')" size="mini" type="primary"
					icon="document" @click="choiaddNotice()">添加商品</el-button>
				<el-button :disabled="!isAuth('storeList:delete') || checkBoxData.length <= 0 "
					size="mini" type="danger" icon="document" @click="choideletes()">批量删除</el-button>
			</div>
			<el-table v-loading="tableDataLoading" @selection-change="changeFun" :data="choicenData.content">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="coverImg" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.coverImg" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="originalPrice" label="商品原价">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.originalPrice | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="memberPrice" label="会员价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.memberPrice | numFilter}}</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="commissionPrice" label="佣金">
				</el-table-column> -->
				<el-table-column prop="sales" label="商品销量">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" prop="id" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('storeList:delete')"
							@click="choidelete(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="store-list-page__pagination">
				<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="choicenData.totalElements">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="为你推荐" name="sixth">
			<div class="store-list-page__toolbar">
				<el-button size="mini" type="primary" icon="document" @click="refresh1">刷新</el-button>
				<el-button :disabled="!isAuth('storeList:add')" size="mini" type="primary"
					icon="document" @click="recomNotice()">添加商品</el-button>
				<el-button :disabled="!isAuth('storeList:delete') || checkBoxData.length <= 0 "
					size="mini" type="danger" icon="document" @click="recomdeletes()">批量删除</el-button>
			</div>
			<el-table v-loading="tableDataLoading" @selection-change="changeFun" :data="recomData.content">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="coverImg" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.coverImg" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="originalPrice" label="商品原价">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.originalPrice | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="memberPrice" label="会员价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.memberPrice | numFilter}}</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="commissionPrice" label="佣金">
				</el-table-column> -->
				<el-table-column prop="sales" label="商品销量">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" prop="id" width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('storeList:delete')"
							@click="recomdelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="store-list-page__pagination">
				<el-pagination @size-change="handleSizeChange5" @current-change="handleCurrentChange5"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="recomData.totalElements">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="升级配置" name="fourth">
			<div class="store-list-page__toolbar">
				<el-button :disabled="!isAuth('storeList:add')" size="mini" type="primary"
					icon="document" @click="addUpgrade">添加升级</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="upgradeData.records">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="wgtUrl" label="统一地址">
				</el-table-column>
				<el-table-column prop="androidWgtUrl" label="安卓">
				</el-table-column>
				<el-table-column prop="iosWgtUrl" label="苹果">
				</el-table-column>
				<el-table-column prop="version" label="安卓版本">
				</el-table-column>
				<el-table-column prop="iosVersion" label="ios版本">
				</el-table-column>
				<el-table-column prop="content" label="升级方式">
					<template slot-scope="scope">
						<span>{{ scope.row.method == 'true' ? '强制升级' : '普通升级' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('storeList:update')"
							@click="upgradebj(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('storeList:delete')"
							@click="upgradelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="store-list-page__pagination">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 40]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="upgradeData.total">
				</el-pagination>
			</div>
		</el-tab-pane>

		
		<el-tab-pane label="首页底部图片" name="dibutupian">
			<div class="store-list-page__toolbar">
				<el-button :disabled="!isAuth('storeList:add')" size="mini" type="primary"
					icon="document" @click="advertNotice">添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="advertData">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="imgUrl" label="图片地址">
					<template slot-scope="scope">
						<img :src="scope.row.imgUrl" alt="" width="170px" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="linkUrl" label="跳转链接">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('storeList:update')"
							@click="advertUpdates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('storeList:delete')"
							@click="advertdeletes(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 添加商品弹框（精选） -->
			<el-dialog title="添加商品" customClass="customWidth" :visible.sync="dialogFormVisible5" center>
				<div style="position: relative;display: inline-block;float: right;">
					<el-input style="width: 200px;" @keydown.enter.native="shousuo" placeholder="根据标题搜索商品"
						v-model="title">
					</el-input>
					<span @click="shousuo" style="position: absolute;right: 0;top:8px;">
						<icon-svg name="shousuo" class="site-sidebar__menu-icon"></icon-svg>
					</span>
				</div>
				<el-table v-loading="tableDataLoading" height="450" :data="choicenData2.content">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column fixed prop="coverImg" label="商品图片">
						<template slot-scope="scope">
							<img :src="scope.row.coverImg" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题" width="200">
					</el-table-column>
					<el-table-column prop="originalPrice" label="商品原价">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.originalPrice | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="price" label="商品价格">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价格">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.memberPrice | numFilter}}</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="commissionPrice" label="佣金">
					</el-table-column> -->
					<el-table-column prop="sales" label="商品销量">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" label="操作" prop="id">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="releasNoticeTo(scope.row)">添加
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="choicenData2.totalElements">
					</el-pagination>
				</div>
			</el-dialog>
		<!-- 添加商品弹框（为你推荐） -->
		<el-dialog title="添加商品" customClass="customWidth" :visible.sync="dialogFormVisible6" center>
				<div style="position: relative;display: inline-block;float: right;">
					<el-input style="width: 200px;" @keydown.enter.native="shousuo" placeholder="根据标题搜索商品"
						v-model="title">
					</el-input>
					<span @click="shousuo" style="position: absolute;right: 0;top:8px;">
						<icon-svg name="shousuo" class="site-sidebar__menu-icon"></icon-svg>
					</span>
				</div>
				<el-table v-loading="tableDataLoading" height="450" :data="choicenData2.content">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column fixed prop="coverImg" label="商品图片">
						<template slot-scope="scope">
							<img :src="scope.row.coverImg" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题" width="200">
					</el-table-column>
					<el-table-column prop="originalPrice" label="商品原价">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.originalPrice | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="price" label="商品价格">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="memberPrice" label="会员价格">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.memberPrice | numFilter}}</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="commissionPrice" label="佣金">
					</el-table-column> -->
					<el-table-column prop="sales" label="商品销量">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="160">
					</el-table-column>
					<el-table-column fixed="right" label="操作" prop="id">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="recomNoticeTo(scope.row)">添加
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="choicenData2.totalElements">
					</el-pagination>
				</div>
			</el-dialog>
		<!-- 添加升级弹框 -->
		<el-dialog title="添加升级" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>统一地址：</span>
					<el-input style="width: 50%;" v-model="wgtUrl" placeholder="请输入统一地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>安卓地址：</span>
					<el-input style="width:50%;" v-model="androidWgtUrl" placeholder="请输入安卓地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>苹果地址：</span>
					<el-input style="width: 50%;" v-model="iosWgtUrl" placeholder="请输入苹果地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>安卓版本号：</span>
					<el-input style="width: 50%;" v-model="version" placeholder="请输入安卓版本号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>ios版本号：</span>
					<el-input style="width: 50%;" v-model="iosVersion" placeholder="请输入ios版本号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>升级方式：</span>
					<el-select v-model="method" placeholder="请选择升级方式" style="width:50%;">
						<el-option v-for="item in methods" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;"><i
							style="color: #f56c6c;margin-right: 2px;">*</i>升级内容：</span>
					<el-input style="width: 50%;" v-model="des" placeholder="请输入升级内容"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="upgradaddTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改升级弹框 -->
			<el-dialog title="修改" :visible.sync="dialogFormVisible4" center>
				<el-form :model="formupgrad">
					<el-form-item label="统一地址：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.wgtUrl" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="安卓地址：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.androidWgtUrl" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="苹果地址：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.iosWgtUrl" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="安卓版本号：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.version" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="ios版本号：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.iosVersion" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="升级方式：" :label-width="formLabelWidth">
						<el-select v-model="formupgrad.method" placeholder="请选择升级方式" style="width:65%;">
							<el-option v-for="item in methods" :key="item.value" :label="item.label"
								:value="item.value">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="升级内容：" :label-width="formLabelWidth">
						<el-input v-model="formupgrad.des" style="width:65%;"></el-input>
					</el-form-item>
				</el-form>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible4 = false">取 消</el-button>
					<el-button type="primary" @click="upgradbjTo()">确 定</el-button>
				</div>
			</el-dialog>
		<!-- 添加轮播 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible2" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片地址：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="imgUrl"
						:action="$http.adornUrlUp('/alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="imgUrl" :src="imgUrl" class="avatar"
							style="border-radius: 6px;width:148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">跳转地址：</span>
				<el-input style="width:50%;" v-model="linkUrl" placeholder="请输入跳转地址"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="advertNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改轮播 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form2">
				<el-form-item label="图片地址：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height:148px;">
						<el-upload class="avatar-uploader" v-model="imgUrl"
							:action="$http.adornUrlUp('/alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess3">
							<img v-if="form2.imgUrl" :src="form2.imgUrl" class="avatar"
								style="border-radius: 6px;width:148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="跳转地址：" :label-width="formLabelWidth">
					<el-input v-model="form2.linkUrl" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendadvertTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 首页编辑 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form1">
				<el-form-item label="标题：" :label-width="formLabelWidth">
					<el-input v-model="form1.title" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="副标题：" :label-width="formLabelWidth">
					<el-input v-model="form1.content" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="路由：" :label-width="formLabelWidth">
					<el-input v-model="form1.url" style="width:65%;"></el-input>
				</el-form-item>
				<!-- <el-form-item label="类型：" :label-width="formLabelWidth" v-if="titles=='修改'">
					<el-select v-model="form1.state" placeholder="请选择类型" style="width:65%;">
						<el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</el-form-item> -->
				<el-form-item label="图片：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="image_url"
							:action="$http.adornUrlUp('/alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="form1.image_url" :src="form1.image_url" class="avatar"
								style="border-radius: 6px;width: 86px;height: 86px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="compileNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加合集分类/类型弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible9" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">标题：</span>
				<el-input style="width:50%;" v-model="title" placeholder="请输入分类标题"></el-input>
			</div>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">路由：</span>
				<el-input style="width: 50%;" v-model="url" placeholder="请输入分类路由"></el-input>
			</div> -->
			<!-- <div style="display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="image_url"
						:action="$http.adornUrlUp('/alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess4">
						<img v-if="image_url" :src="image_url" class="avatar"
							style="border-radius: 6px;width: 86px;height: 86px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div> -->
			<div style="margin-bottom: 10px;margin-top: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input-number size="medium" v-model="sort"></el-input-number>
			</div>
			<!-- <div style="margin-bottom: 10px;" v-if="nav==8">
				<span style="width: 200px;display: inline-block;text-align: right;">展示类型：</span>
				<el-radio-group v-model="type">
					<el-radio :label="1">PC端</el-radio>
					<el-radio :label="2">H5端</el-radio>
				</el-radio-group>
			</div> -->
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible9 = false">取 消</el-button>
				<el-button type="primary" @click="NagNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改集合分类/类型 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible8" center>
			<el-form :model="formcomp">
				<el-form-item label="分类标题：" :label-width="formLabelWidth">
					<el-input v-model="formcomp.title" style="width:65%;"></el-input>
				</el-form-item>
				<!-- <el-form-item label="分类路由：" :label-width="formLabelWidth">
						<el-input v-model="formcomp.url" style="width:65%;"></el-input>
					</el-form-item>
					<el-form-item label="图片：" :label-width="formLabelWidth">
						<div
							style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
							<el-upload class="avatar-uploader" v-model="image_url"
								:action="$http.adornUrlUp('/alioss/upload')" :show-file-list="false"
								:on-success="handleAvatarSuccess3">
								<img v-if="formcomp.image_url" :src="formcomp.image_url" class="avatar"
									style="border-radius: 6px;width: 86px;height: 86px;" />
								<i v-else class="el-icon-plus avatar-uploader-icon"></i>
							</el-upload>
						</div>
					</el-form-item> -->
				<el-form-item label="排序：" :label-width="formLabelWidth">
					<el-input-number size="medium" v-model="formcomp.sort"></el-input-number>
				</el-form-item>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;" v-if="nav==8">展示类型：</span>
					<el-radio-group v-model="formcomp.type">
						<el-radio :label="1">PC端</el-radio>
						<el-radio :label="2">H5端</el-radio>
					</el-radio-group>
				</div> -->
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible8 = false">取 消</el-button>
				<el-button type="primary" @click="compileNoticeTos()">确 定</el-button>
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
				id: '',
				img: '',
				title: '',
				type: '',
				sort: 'createAt',
				readonly: true,
				merchants: '',
				price: '',
				memberPrice: '',
				sales: 0,
				status: 0,
				status2: '',
				descrition: '',
				totalnum: 0,
				nav: '',
				imgUrl: '',
				linkUrl: '',
				image_url: '',
				content: '',
				checkBoxData: [], //多选框选择的值
				choicenData: {},
				choicenData2: {},
				recomData: [],
				recomData2: [],
				state: '',
				states: [{
						value: '1',
						label: '金刚区'
					},
					{
						value: '2',
						label: '精选好物'
					},
					{
						value: '3',
						label: '热卖榜单'
					},
					{
						value: '4',
						label: '每日上新'
					},
					{
						value: '5',
						label: '拼团购'
					},
					{
						value: '6',
						label: '邀请图'
					},
					{
						value: '7',
						label: '首页邀请图'
					}
				],
				form1: {
					id: '',
					image_url: '',
					content: '',
					title: '',
					url: '',
					state: '1',
					createAt: ''
				},
				form2: {
					id: '',
					imgUrl: '',
					linkUrl: '',
					createAt: '',
					type: 1
				},
				form3: {
					id: '',
					imgUrl: '',
					linkUrl: '',
					createAt: ''
				},
				statusmain: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				statusmain2: [{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				dialogFormVisible9: false,
				dialogFormVisible4: false,
				dialogFormVisible3: false,
				typeDatas: [],
				advertData: [],
				tableData: [],
				homeData: [],
				guangaoData: [],
				types: 1,
				formupgrad: {
					id: '',
					androidWgtUrl: '',
					iosWgtUrl: '',
					wgtUrl: '',
					version: '',
					des: '',
					method: '',
					iosVersion: ''
				},
				upgradeData: [],
				androidWgtUrl: '',
				iosWgtUrl: '',
				wgtUrl: '',
				version: '',
				des: '',
				iosVersion: '',
				method: 'false',
				methods: [{
						value: 'false',
						label: '普通升级'
					},
					{
						value: 'true',
						label: '强制升级'
					}
				],
				homeData1: {},
				guangaoData: [],
				formcomp: {
					id: '',
					state: '',
					title: '',
					url: '',
					image_url: '',
					type: [],
					sort: 1
				},
				jklxData: [],
				titles: '添加'
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后三位
					let tempVal = parseFloat(value).toFixed(3)
					realVal = tempVal.substring(0, tempVal.length - 1)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			handleAvatarSuccess(file) {
				this.img = file.data;
			},
			handleAvatarSuccess2(file) {
				this.form1.image_url = file.data;
			},
			handleAvatarSuccess1(file) {
				this.imgUrl = file.data;
			},
			handleAvatarSuccess3(file) {
				this.form2.imgUrl = file.data;
			},
			handleAvatarSuccess4(file) {
				this.form3.imgUrl = file.data;
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
				this.homeSelectJ()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.homeSelectJ()
			},
			handleSizeChange2(val) {
				this.size = val
				this.upgradeSelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.upgradeSelect()
			},
			handleSizeChange3(val) {
				this.size = val;
				this.choicenSelect()
			},
			handleCurrentChange3(val) {
				this.page = val;
				this.choicenSelect()
			},
			handleSizeChange4(val) {
				this.size = val;
				this.handpick()
			},
			handleCurrentChange4(val) {
				this.page = val;
				this.handpick()
			},
			handleSizeChange5(val) {
				this.size = val;
				this.recomSelect()
			},
			handleCurrentChange5(val) {
				this.page = val;
				this.recomSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '首页轮播') {
					this.types = 1
					this.advertSelect()
				}
				if (tab._props.label == 'PC端首页轮播') {
					this.types = 2
					this.advertSelect()
				}
				if (tab._props.label == '首页导航') {
					this.state = ''
					this.homeSelect()
				}
				if (tab._props.label == 'PC端首页导航') {
					this.state = 10
					this.homeSelectPc()
				}
				if (tab._props.label == '广告位') {
					this.guangaoSelect()
				}
				if (tab._props.label == '精选商品') {
					this.page = 1
					this.size = 10
					this.choicenSelect()
					this.handpick()
				}
				if (tab._props.label == '为你推荐') {
					this.page = 1
					this.size = 10
					this.recomSelect()
					this.handpick()
				}
				if (tab._props.label == '升级配置') {
					this.upgradeSelect()
				}
				if (tab._props.label == '集合分类') {
					this.nav = 8
					this.homeSelectJ()
					this.homeSelectLx()
				}
				if (tab._props.label == '集合类型') {
					this.nav = 9
					this.homeSelectJ()
				}
				if (tab._props.label == '首页底部图片') {
					this.types = 3
					this.advertSelect()
				}
				
			},
			// 刷新
			refresh() {
				this.choicenSelect()
				this.handpick()
			},
			// 刷新
			refresh1() {
				this.recomSelect()
				this.handpick()
			},
			// 添加轮播
			advertNotice() {
				this.imgUrl = ''
				this.linkUrl = ''
				this.dialogFormVisible2 = true
			},
			// 添加轮播
			advertNoticeTo() {
				if (this.linkUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入跳转链接',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/advert/save'),
					method: 'post',
					data: this.$http.adornData({
						'imgUrl': this.imgUrl,
						'linkUrl': this.linkUrl,
						'type': this.types
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible2 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.advertSelect()
						}
					})
				})
			},
			// 修改轮播
			advertUpdates(index, rows) {
				this.dialogFormVisible = true;
				this.form2.id = rows.id;
				this.form2.imgUrl = rows.imgUrl;
				this.form2.linkUrl = rows.linkUrl;
				this.form2.createAt = rows.createAt;
				this.form2.type = rows.type
			},
			// 修改轮播
			amendadvertTo() {
				this.$http({
					url: this.$http.adornUrl2('/advert/save'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form2.id,
						'imgUrl': this.form2.imgUrl,
						'linkUrl': this.form2.linkUrl,
						'createAt': this.form2.createAt,
						'type': this.form2.type
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
							this.advertSelect()
						}
					})
				})
			},
			// 删除广告
			advertdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/advert/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.id
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.advertSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 编辑广告位弹框
			guangaocompile(index, rows) {
				this.dialogFormVisible7 = true
				this.form3.linkUrl = rows.linkUrl
				this.form3.imgUrl = rows.imgUrl
				this.form3.id = rows.id
				this.form3.createAt = rows.createAt
			},
			// 修改商品类别
			guangaoNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/selfBanner/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form3.id,
						'imgUrl': this.form3.imgUrl,
						'linkUrl': this.form3.linkUrl,
						'createAt': this.form3.createAt,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible7 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.guangaoSelect()
						}
					})
				})
			},
			// 编辑弹框
			compile(rows) {
				this.dialogFormVisible1 = true
				if (rows) {
					this.titles = '修改'
					this.form1.url = rows.url
					this.form1.content = rows.content
					this.form1.image_url = rows.image_url
					this.form1.id = rows.id
					this.form1.state = rows.state
					this.form1.createAt = rows.createAt
					this.form1.title = rows.title
				} else {
					this.titles = '添加'
					this.form1.url = ''
					this.form1.content = ''
					this.form1.image_url = ''
					this.form1.id = ''
					this.form1.state = '1'
					this.form1.createAt = ''
					this.form1.title = ''
				}

			},
			// 修改商品类别
			compileNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/selfActivity/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form1.id,
						'state': this.form1.state,
						'title': this.form1.title,
						'url': this.form1.url,
						'content': this.form1.content,
						'image_url': this.form1.image_url,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							if (this.state == 10) {
								this.homeSelectPc()
							} else {
								this.homeSelect()
							}
						}
					})
				})
			},

			//添加精选商品
			choiaddNotice() {
				this.dialogFormVisible5 = true
			},
			// 添加商品
			releasNoticeTo(row) {
				this.$http({
					url: this.$http.adornUrl2('/goods/addSelectGoods'),
					method: 'get',
					params: this.$http.adornParams({
						'id': row.id,
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.choicenSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 精选淘宝商品
			handpick() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'title': this.title,
						'type': this.type,
						'status': this.status
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.choicenData2 = returnData
					}
				})
			},
			// 搜索
			shousuo() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goods/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'title': this.title,
						'type': this.type,
						'status': this.status
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.choicenData2 = returnData
					}
				})
			},
			// 批量删除商品
			choideletes(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				for (var i in ids) {
					this.$http({
						url: this.$http.adornUrl2('/goods/deleteSelectGoods'),
						method: 'get',
						params: this.$http.adornParams({
							'ids': ids[i]
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '批量删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.choicenSelect()
							}
						})
					})
				}
			},
			// 删除商品
			choidelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/goods/deleteSelectGoods'),
						method: 'get',
						params: this.$http.adornParams({
							'ids': delid
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.choicenSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 精选商品列表
			choicenSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goods/selectGoods'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'sort': this.sort
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.choicenData = returnData
				})
			},
			// 为你推荐列表
			recomSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/goods/recommend'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'sort': this.sort
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.recomData = returnData
				})
			},
			//添加为你推荐
			recomNotice() {
				this.dialogFormVisible6 = true
			},
			// 添加为你推荐
			recomNoticeTo(row) {
				this.$http({
					url: this.$http.adornUrl2('/goods/addRecommend'),
					method: 'get',
					params: this.$http.adornParams({
						'id': row.id,
					})
				}).then(({
					data
				}) => {
					if (data && data.status === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.choicenSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			// 批量删除为你推荐商品
			recomdeletes(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				for (var i in ids) {
					this.$http({
						url: this.$http.adornUrl2('/goods/deleteRecommend'),
						method: 'get',
						params: this.$http.adornParams({
							'ids': ids[i]
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '批量删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.choicenSelect()
							}
						})
					})
				}
			},
			// 删除为你推荐商品
			recomdelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/goods/deleteRecommend'),
						method: 'get',
						params: this.$http.adornParams({
							'ids': delid
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.choicenSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取首页导航列表
			homeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(`/selfActivity/list?state=${this.state}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData = returnData
				})
			},
			// 获取Pc首页导航列表
			homeSelectPc() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(`/selfActivity/state?state=${this.state}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.homeData = returnData
				})
			},
			// 获取轮播数据
			advertSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/advert/list'),
					method: 'get',
					params: this.$http.adornParams({
						type: this.types
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.advertData = returnData
				})
			},
			// 获取广告位数据
			guangaoSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfBanner/list'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.guangaoData = returnData
				})
			},
			// 添加升级弹框
			addUpgrade() {
				this.dialogFormVisible3 = true
			},
			// 添加升级
			upgradaddTo() {
				if (this.wgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入统一地址',
						type: 'warning'
					})
					return
				}
				if (this.androidWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓地址',
						type: 'warning'
					})
					return
				}
				if (this.iosWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入苹果地址',
						type: 'warning'
					})
					return
				}

				if (this.version == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓版本号',
						type: 'warning'
					})
					return
				}
				if (this.iosVersion == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入ios版本号',
						type: 'warning'
					})
					return
				}
				if (this.method == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择升级方式',
						type: 'warning'
					})
					return
				}
				if (this.des == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入升级内容',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/appinfo/save'),
					method: 'post',
					data: this.$http.adornData({
						'androidWgtUrl': this.androidWgtUrl,
						'iosWgtUrl': this.iosWgtUrl,
						'wgtUrl': this.wgtUrl,
						'version': this.version,
						'iosVersion': this.iosVersion,
						'method': this.method,
						'des': this.des
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible3 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.wgtUrl = ''
							this.iosWgtUrl = ''
							this.androidWgtUrl = ''
							this.version = ''
							this.iosVersion = ''
							this.des = ''
							this.upgradeSelect()
						}
					})
				})
			},
			// 修改升级弹框
			upgradebj(index, rows) {
				this.dialogFormVisible4 = true
				this.formupgrad.androidWgtUrl = rows.androidWgtUrl
				this.formupgrad.id = rows.id
				this.formupgrad.iosWgtUrl = rows.iosWgtUrl
				this.formupgrad.wgtUrl = rows.wgtUrl
				this.formupgrad.version = rows.version
				this.formupgrad.iosVersion = rows.iosVersion
				this.formupgrad.des = rows.des
				this.formupgrad.method = rows.method
			},
			// 修改升级
			upgradbjTo() {
				if (this.formupgrad.wgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入统一地址',
						type: 'warning'
					})
					return
				}
				if (this.formupgrad.androidWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓地址',
						type: 'warning'
					})
					return
				}
				if (this.formupgrad.iosWgtUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入苹果地址',
						type: 'warning'
					})
					return
				}

				if (this.formupgrad.version == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入安卓版本号',
						type: 'warning'
					})
					return
				}
				if (this.formupgrad.iosVersion == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入ios版本号',
						type: 'warning'
					})
					return
				}
				if (this.formupgrad.method == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择升级方式',
						type: 'warning'
					})
					return
				}
				if (this.formupgrad.des == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入升级内容',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/appinfo/save'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formupgrad.id,
						'androidWgtUrl': this.formupgrad.androidWgtUrl,
						'iosWgtUrl': this.formupgrad.iosWgtUrl,
						'wgtUrl': this.formupgrad.wgtUrl,
						'version': this.formupgrad.version,
						'iosVersion': this.formupgrad.iosVersion,
						'des': this.formupgrad.des,
						'method': this.formupgrad.method,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible4 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.upgradeSelect()
						}
					})
				})
			},
			// 删除升级
			upgradelete(row) {
				let delid = row.id
				this.id = delid
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/appinfo/delete/${this.id}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = ''
								this.upgradeSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 升级配置数据列表
			upgradeSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/appinfo/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page':this.page,
						'limit':this.size,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.upgradeData = returnData
				})
			},
			// 获取集合分类数据列表
			homeSelectJ() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(
						`/activity/state/${this.nav}?page=${this.page-1}&size=${this.size}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.homeData1 = returnData
				})
			},
			// 添加集合分类弹框
			addNavigation() {
				this.url = ''
				this.sort = 0
				this.title = ''
				this.image_url = ''
				this.type = 1
				this.dialogFormVisible9 = true
			},
			// 添加集合分类
			NagNoticeTo() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					})
					return
				}
				// if (this.url == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入路由',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.image_url == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传图片',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.nav == 8) {
					// if (this.type == '') {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请选择类型',
					// 		type: 'warning'
					// 	})
					// 	return
					// }
				}

				this.$http({
					url: this.$http.adornUrl2('/activity/save'),
					method: 'post',
					data: this.$http.adornData({
						'state': this.nav,
						// 'url': this.url,
						'title': this.title,
						// 'image_url': this.image_url,
						'type': this.type,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisible9 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {

								this.homeSelectJ()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 删除集合分类
			navdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/activity/delete/${row.id}`),
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
								this.homeSelectJ()
							}
						})
					})
				}).catch(() => {})
			},
			// 修改弹框
			compile1(index, rows) {
				this.dialogFormVisible8 = true
				this.formcomp.id = rows.id
				this.formcomp.state = rows.state
				this.formcomp.title = rows.title
				// this.formcomp.url = rows.url
				// this.formcomp.image_url = rows.image_url
				this.formcomp.type = Number(rows.type)
				this.formcomp.sort = Number(rows.sort)
			},
			// 修改商品类别
			compileNoticeTos() {
				if (this.formcomp.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					})
					return
				}
				// if (this.formcomp.url == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请输入路由',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				// if (this.formcomp.image_url == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请上传图片',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.nav == 8) {
					// if (this.formcomp.type == '') {
					// 	this.$notify({
					// 		title: '提示',
					// 		duration: 1800,
					// 		message: '请选择类型',
					// 		type: 'warning'
					// 	})
					// 	return
					// }
				}

				this.$http({
					url: this.$http.adornUrl2(`/activity/update/${this.formcomp.id}`),
					method: 'post',
					data: this.$http.adornData({
						'id': this.formcomp.id,
						'state': this.formcomp.state,
						'title': this.formcomp.title,
						// 'url': this.formcomp.url,
						// 'image_url': this.formcomp.image_url,
						'type': this.formcomp.type,
						'sort': this.formcomp.sort,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible8 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.homeSelectJ()
						}
					})
				})
			},
			// 商品列表
			goodsList(row) {
				this.$router.push({
					path: '/aggregate',
					query: {
						activityId: row.id
					}
				})
			},
			// 获取集合类型列表
			homeSelectLx() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2(
						`/activity/state/9`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.jklxData = returnData
				})
			},
			// 删除首页分类
			compiledeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/selfActivity/delete?id=${row.id}`),
						method: 'get',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.homeSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				}).catch(() => {})
			}
		},
		mounted() {
			this.advertSelect()
		}
	};
</script>

<style scoped>
.store-list-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.store-list-page__tabs {
	margin-bottom: 0;
}

.store-list-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.store-list-page__toolbar {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	justify-content: flex-end;
	gap: 8px;
	margin-bottom: 12px;
}

.store-list-page__table .el-table {
	width: 100%;
}

.store-list-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.store-list-page__table .el-table td,
.store-list-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.store-list-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.store-list-page__toolbar .el-button--primary:not(.is-plain),
.store-list-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.store-list-page__toolbar .el-button--primary:not(.is-plain):hover,
.store-list-page__toolbar .el-button--primary:not(.is-plain):focus,
.store-list-page__table .el-button--primary:not(.is-plain):hover,
.store-list-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}
</style>

<style>
.customWidth {
	width: 80% !important;
}
</style>
