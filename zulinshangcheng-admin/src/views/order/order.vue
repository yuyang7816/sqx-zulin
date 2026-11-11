<template>
	<div class="order-page">
		<!-- hero -->
		<div class="nf-page-hero order-page__hero">
			<span class="nf-page-hero__badge">订单管理</span>
			<h2 class="nf-page-hero__title">订单中心</h2>
			<p class="nf-page-hero__desc">查看与管理全部订单与待发货订单，支持多维度筛选、发货、物流查询与导出操作</p>
		</div>

		<!-- 筛选白卡 · 全部订单 -->
		<div v-show="activeName === 'first'" class="nf-filter-card order-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="order-page__filter-body">
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">订单类型</label>
					<el-select v-model="orderType" placeholder="请选择订单类型" class="order-page__filter-control" @change="select()">
						<el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">订单状态</label>
					<el-select v-model="status" placeholder="请选择订单状态" class="order-page__filter-control" @change="select()">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">发货类型</label>
					<el-select v-model="isExpressT" placeholder="请选择发货类型" class="order-page__filter-control" @change="select()">
						<el-option v-for="item in statusnum" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">收货人</label>
					<el-input class="order-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入收货人"
						v-model="consigneeT"></el-input>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">手机号</label>
					<el-input class="order-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="mobileT"></el-input>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">发货快递单号</label>
					<el-input class="order-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入发货快递单号"
						v-model="sellerNumberT"></el-input>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">归还快递单号</label>
					<el-input class="order-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入归还快递单号"
						v-model="expressNumberT"></el-input>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">订单号</label>
					<el-input class="order-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入订单号"
						v-model="orderNum"></el-input>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">开始时间</label>
					<el-date-picker class="order-page__filter-control" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">截止时间</label>
					<el-date-picker class="order-page__filter-control" v-model="endTime" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="order-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
					<el-button size="mini" type="primary" icon="document" @click="exportBtn">导出</el-button>
				</div>
			</div>
		</div>

		<!-- 筛选白卡 · 待发货订单 -->
		<div v-show="activeName === 'thirdly'" class="nf-filter-card order-page__filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="order-page__filter-body">
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">订单号</label>
					<el-input class="order-page__filter-control" @keydown.enter.native="select" clearable placeholder="请输入订单号"
						v-model="orderNum"></el-input>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">订单类型</label>
					<el-select v-model="orderType" placeholder="请选择订单类型" class="order-page__filter-control" @change="select()">
						<el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">开始时间</label>
					<el-date-picker class="order-page__filter-control" v-model="startTime" align="right"
						type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>
				</div>
				<div class="order-page__filter-item">
					<label class="order-page__filter-label">截止时间</label>
					<el-date-picker class="order-page__filter-control" v-model="endTime" align="right" type="datetime"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
				</div>
				<div class="order-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="document" @click="cleans3">重置</el-button>
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
					<el-button size="mini" type="primary" icon="document" @click="exportBtn">导出</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 + tabs -->
		<div class="nf-table-card order-page__table">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="order-page__tabs">
				<el-tab-pane label="全部订单" name="first">
			<el-table v-loading="tableDataLoading" :data="tableData.records" height="600px">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="img" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.img" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column label="订单类型">
					<template slot-scope="scope">
						<span v-if="scope.row.orderType == 1">普通订单</span>
						<span v-if="scope.row.orderType == 5">租赁订单</span>
						<span v-if="scope.row.orderType == 6">买断订单</span>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="detailJson" label="商品规格">
					<template slot-scope="scope">
						<span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="number" label="商品个数">
				</el-table-column>
				<el-table-column prop="descrition" label="订单备注">
					<template slot-scope="scope">
						<span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderNum" label="订单号" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.orderNum}}</span>
					</template>
				</el-table-column>
				<el-table-column label="发货类型">
					<template slot-scope="scope">
						<span v-if="scope.row.isExpress1 == 1">包邮</span>
						<span v-if="scope.row.isExpress1 == 2">邮寄</span>
						<span v-if="scope.row.isExpress1 == 3">自取</span>
					</template>
				</el-table-column>
				<el-table-column prop="expressName" label="发货快递公司" width="120">
				</el-table-column>
				<el-table-column prop="sellerNumber" label="发货快递单号" width="120">
				</el-table-column>
				<el-table-column prop="expressNumber" label="归还快递单号" width="120">
				</el-table-column>
				<el-table-column prop="consignee" label="收货人" width="120">
				</el-table-column>
				<el-table-column prop="mobile" label="手机号" width="120">
				</el-table-column>
				<el-table-column prop="detail" label="详细地址" width="120">
					<template slot-scope="scope">
						<span>{{scope.row.provinces}}{{scope.row.detail}}</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.price">{{scope.row.price | numFilter}}</span>
						<span style="color: #f56c6c;" v-else>0</span>
					</template>
				</el-table-column> -->

				<!-- <el-table-column prop="commissionPrice" label="佣金" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.commissionPrice">{{scope.row.commissionPrice | numFilter}}</span>
						<span style="color: #f56c6c;" v-else>0</span>
					</template>
				</el-table-column> -->
				<el-table-column prop="leaseTerm" label="租赁天数" width="80">
				</el-table-column>
				<el-table-column prop="deposit" label="押金" width="80">
				</el-table-column>
				<el-table-column prop="continueDate" label="续租日期" width="80">
				</el-table-column>
				<el-table-column prop="cancelTime" label="取消时间" width="80">
				</el-table-column>
				<el-table-column prop="leaseStartDate" label="预计起租日期" width="80">
				</el-table-column>
				<el-table-column prop="returnDate" label="预计归还日期" width="80">
				</el-table-column>
				<!-- <el-table-column prop="estimateMoney" label="预估价格" width="80">
				</el-table-column> -->
				<el-table-column prop="dailyRent" label="日租金" width="80">
				</el-table-column>
				<el-table-column prop="grossRent" label="总租金" width="80">
				</el-table-column>
				<el-table-column prop="purchaseTime" label="买断时间" width="80">
				</el-table-column>
				<el-table-column prop="returnTime" label="归还日期" width="80">
				</el-table-column>
				<el-table-column prop="supplementMoney" label="补缴的租金" width="80">
				</el-table-column>
				<el-table-column prop="supplementStatus" label="补缴是否支付" width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.supplementStatus == 1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="supplementTime" label="补缴时间" width="80">
				</el-table-column>
				<el-table-column prop="isEvaluate" label="是否已评论" width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.isEvaluate == 1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="supplementNum" label="补缴单号" width="80">
				</el-table-column>
				<el-table-column label="是否复购">
					<template slot-scope="scope">
						<span v-if="scope.row.isRepeat == 1">否</span>
						<span v-if="scope.row.isRepeat == 2">是</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;cursor:pointer;"
							v-if="scope.row.payMoney">{{scope.row.payMoney | numFilter}}</span>
						<span style="color: #f56c6c;" v-else>0</span>
					</template>
				</el-table-column>
				<el-table-column prop="payWay" label="支付方式" width="120">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.payWay === 1 ">微信</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 2 ">微信公众号</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 3 ">微信小程序</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 4 ">app支付宝</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 6 ">零钱</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 7 ">积分</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === null ">未支付</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="订单状态">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.status === 0 ">待支付</span>
						<span style="color: #15cad2;" v-if="scope.row.status === 1 ">已支付待发货</span>
						<span style="color: #15cad2;" v-if="scope.row.status === 2 ">已发货待收货 </span>
						<span style="color: #999999;" v-if="scope.row.status === 3 ">已收货 </span>
						<span style="color: #999999;" v-if="scope.row.status === 4 ">待归还</span>
						<span style="color: #999999;" v-if="scope.row.status === 5 ">归还中</span>
						<span style="color: #15cad2;" v-if="scope.row.status === 6 ">已完成</span>
						<span style="color: #999999;" v-if="scope.row.status === 7 ">继续租赁</span>
						<span style="color: #999999;" v-if="scope.row.status === 8 ">买断</span>
						<span style="color: #999999;" v-if="scope.row.status === 9 ">已取消</span>
						<span style="color: #999999;" v-if="scope.row.status === 10 ">待评价</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="payTime" label="付款时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="DeliveryTime" label="收货时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.DeliveryTime ? scope.row.DeliveryTime : '未收货'}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="160">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('orderAdmin:deliver')"
							v-if="scope.row.isExpress != 3&&scope.row.status === 1 " @click="colonelClick(scope.row)">立即发货
						</el-button>
						<!-- <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">订单详情
						</el-button> -->
						<el-button size="mini" type="primary" @click="contact(scope.row)">联系买家
						</el-button>
						<el-button size="mini" type="warning" plain :disabled="!isAuth('orderAdmin:deliver')"
							@click="shouClick(scope.row)" v-if="scope.row.isExpress = 3&&scope.row.status === 1">确认用户收货
						</el-button>
						<el-button size="mini" type="warning" plain :disabled="!isAuth('orderAdmin:deliver')"
							@click="wanchengClick1(scope.row)" v-if="scope.row.status === 5">确定归还
						</el-button>
						<el-button size="mini" type="warning" :disabled="!isAuth('orderAdmin:refund')"
							@click="refundClick(scope.row)" v-if="scope.row.status === 2 ||scope.row.status === 1">取消订单
						</el-button>
						<el-button size="mini" type="primary" v-if="scope.row.isExpress != 3&&scope.row.status !== 0"
							plain @click="wuliuBtn(scope.row,1)">查看发出物流
						</el-button>
						<el-button size="mini" type="primary" v-if="scope.row.isExpress != 3&&scope.row.status !== 0"
							@click="wuliuBtn(scope.row,2)">查看回收物流
						</el-button>

					</template>
				</el-table-column>
			</el-table>
			<div class="order-page__summary">
				本页累计交易统计：{{totalMoney.toFixed(2)}}元</div>
			<div class="order-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="待发货订单" name="thirdly">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="img" label="商品图片">
					<template slot-scope="scope">
						<img :src="scope.row.img" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column label="订单类型">
					<template slot-scope="scope">
						<span v-if="scope.row.orderType == 1">普通订单</span>
						<span v-if="scope.row.orderType == 5">租赁订单</span>
						<span v-if="scope.row.orderType == 6">买断订单</span>
					</template>
				</el-table-column>
				<el-table-column prop="title" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="detailJson" label="商品规格">
					<template slot-scope="scope">
						<span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="number" label="商品个数">
				</el-table-column>
				<el-table-column prop="descrition" label="订单备注">
					<template slot-scope="scope">
						<span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderNum" label="订单号" width="200">
					<template slot-scope="scope">
						<span>{{scope.row.orderNum}}</span>
					</template>
				</el-table-column>
				<el-table-column label="发货类型">
					<template slot-scope="scope">
						<span v-if="scope.row.isExpress1 == 1">包邮</span>
						<span v-if="scope.row.isExpress1 == 2">邮寄</span>
						<span v-if="scope.row.isExpress1 == 3">自取</span>
					</template>
				</el-table-column>
				<el-table-column prop="expressName" label="发货快递公司" width="120">
				</el-table-column>
				<el-table-column prop="sellerNumber" label="发货快递单号" width="120">
				</el-table-column>
				<el-table-column prop="expressNumber" label="归还快递单号" width="120">
				</el-table-column>
				<el-table-column prop="consignee" label="收货人" width="120">
				</el-table-column>
				<el-table-column prop="mobile" label="手机号" width="120">
				</el-table-column>
				<el-table-column prop="detail" label="详细地址" width="120">
					<template slot-scope="scope">
						<span>{{scope.row.provinces}}{{scope.row.detail}}</span>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="price" label="商品价格">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.price">{{scope.row.price | numFilter}}</span>
						<span style="color: #f56c6c;" v-else>0</span>
					</template>
				</el-table-column> -->
				
				<!-- <el-table-column prop="commissionPrice" label="佣金" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.commissionPrice">{{scope.row.commissionPrice | numFilter}}</span>
						<span style="color: #f56c6c;" v-else>0</span>
					</template>
				</el-table-column> -->
				<el-table-column prop="leaseTerm" label="租赁天数" width="80">
				</el-table-column>
				<el-table-column prop="deposit" label="押金" width="80">
				</el-table-column>
				<el-table-column prop="continueDate" label="续租日期" width="80">
				</el-table-column>
				<el-table-column prop="cancelTime" label="取消时间" width="80">
				</el-table-column>
				<el-table-column prop="leaseStartDate" label="预计起租日期" width="80">
				</el-table-column>
				<el-table-column prop="returnDate" label="预计归还日期" width="80">
				</el-table-column>
				<el-table-column prop="estimateMoney" label="预估价格" width="80">
				</el-table-column>
				<el-table-column prop="dailyRent" label="日租金" width="80">
				</el-table-column>
				<el-table-column prop="grossRent" label="总租金" width="80">
				</el-table-column>
				<el-table-column prop="purchaseTime" label="买断时间" width="80">
				</el-table-column>
				<el-table-column prop="returnTime" label="归还日期" width="80">
				</el-table-column>
				<el-table-column prop="supplementMoney" label="补缴的租金" width="80">
				</el-table-column>
				<el-table-column prop="supplementStatus" label="补缴是否支付" width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.supplementStatus == 1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="supplementTime" label="补缴时间" width="80">
				</el-table-column>
				<el-table-column prop="isEvaluate" label="是否已评论" width="80">
					<template slot-scope="scope">
						<span v-if="scope.row.isEvaluate == 1">是</span>
						<span v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column prop="supplementNum" label="补缴单号" width="80">
				</el-table-column>
				<el-table-column label="是否复购">
					<template slot-scope="scope">
						<span v-if="scope.row.isRepeat == 1">否</span>
						<span v-if="scope.row.isRepeat == 2">是</span>
					</template>
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额" width="120">
					<template slot-scope="scope">
						<span style="color: #f56c6c;cursor:pointer;"
							v-if="scope.row.payMoney">{{scope.row.payMoney | numFilter}}</span>
						<span style="color: #f56c6c;" v-else>0</span>
					</template>
				</el-table-column>
				<el-table-column prop="payWay" label="支付方式" width="120">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.payWay === 1 ">微信</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 2 ">微信公众号</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 3 ">微信小程序</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 4 ">app支付宝</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 6 ">零钱</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === 7 ">积分</span>
						<span style="color: #15cad2;" v-if="scope.row.payWay === null ">未支付</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" prop="status" label="订单状态">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.status === 0 ">待支付</span>
						<span style="color: #15cad2;" v-if="scope.row.status === 1 ">已支付待发货</span>
						<span style="color: #15cad2;" v-if="scope.row.status === 2 ">已发货待收货 </span>
						<span style="color: #999999;" v-if="scope.row.status === 3 ">已收货 </span>
						<span style="color: #999999;" v-if="scope.row.status === 4 ">待归还</span>
						<span style="color: #999999;" v-if="scope.row.status === 5 ">归还中</span>
						<span style="color: #15cad2;" v-if="scope.row.status === 6 ">已完成</span>
						<span style="color: #999999;" v-if="scope.row.status === 7 ">继续租赁</span>
						<span style="color: #999999;" v-if="scope.row.status === 8 ">买断</span>
						<span style="color: #999999;" v-if="scope.row.status === 9 ">已取消</span>
						<span style="color: #999999;" v-if="scope.row.status === 10 ">待评价</span>
					</template>
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="payTime" label="付款时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="finishTime" label="收货时间" width="160">
					<template slot-scope="scope">
						<span>{{scope.row.finishTime ? scope.row.finishTime : '未收货'}}</span>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('orderAdmin:deliver')"
							@click="colonelClick(scope.row)">立即发货
						</el-button>
						<el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">订单详情
						</el-button>
						<el-button size="mini" type="primary" @click="contact(scope.row)">联系买家
						</el-button>
						<el-button size="mini" type="warning" :disabled="!isAuth('orderAdmin:refund')"
							@click="refundClick(scope.row)" v-if="scope.row.status === 2 ||scope.row.status === 1">取消订单
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="order-page__summary">
				本页累计交易统计：{{totalMoney.toFixed(2)}}元</div>
			<div class="order-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.total">
				</el-pagination>
			</div>

		</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 发货弹框 -->
		<el-dialog title="发货" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">快递公司：</span>
				<el-input style="width:50%;" v-model="expressName" placeholder="请输入快递公司"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">快递单号：</span>
				<el-input style="width: 50%;" v-model="expressNumber" placeholder="请输入快递单号"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="deliverNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="物流信息" :visible.sync="dialogVisible" width="60%">
			<!-- <div class="shxx">物流信息</div> -->
			<div class="block" v-if="wuliuDate||wuliuDate.list.length>0">

				<el-steps direction="vertical" :active="1" :reverse="reverse">
					<el-step v-for="(activity, index) in wuliuDate.list" :key="index" :title="activity.time"
						:description="activity.status"></el-step>

				</el-steps>
			</div>
			<div class="block" v-else>
				暂无物流信息
			</div>
		</el-dialog>
		<!-- 退还押金 -->
		<el-dialog title="退还押金" :visible.sync="dialogFormVisibleYj" center>
			
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">当前押金：</span>
				<span style="color: red;">{{yajinNum}}</span>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">退还押金：</span>
				<el-input style="width:50%;" v-model="expressName" type="number" :min="0" placeholder="请输入退还押金"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">押金说明：</span>
				<el-input style="width: 50%;" v-model="expressNumber" placeholder="请输入押金说明"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisibleYj = false">取 消</el-button>
				<el-button type="primary" @click="deliverNoticeToYj()">确 定</el-button>
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
				phone: '',
				orderNum: '',
				title: '',
				mobile: '',
				isExpress: '',
				status: '',
				orderType: 0,
				orderTypes: [{
						value: 0,
						label: '全部'
					},
					{
						value: 1,
						label: '普通订单'
					},
					{
						value: 5,
						label: '租赁订单'
					},
					{
						value: 6,
						label: '买断订单'
					},
				],
				sumincome: '',
				totalnum: 0,
				income: '',
				wxincome: '',
				zfbincome: '',
				yueicome: '',
				ordersum0: '',
				ordersum1: '',
				ordersum2: '',
				expressName: '',
				expressNumber: '',
				refusedRefund: '',
				moneyFrom: 0,
				tableData2: [],
				info: {
					stockDate: this.getNowTime(), //日期
				},
				way: 3,
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
				statusmain: [{ //0待支付 1已支付待发货 2已发货待收货 3已收货 4待归还  5归还中 6已完成  7继续租赁  8买断 9已取消 10待评价'
						value: '',
						label: '全部'
					}, {
						value: 0,
						label: '待支付'
					},
					{
						value: 1,
						label: '已支付待发货'
					},
					{
						value: 2,
						label: '已发货待收货'
					},
					{
						value: 3,
						label: '已收货'
					},
					{
						value: 4,
						label: '待归还'
					},
					{
						value: 5,
						label: '归还中'
					},
					{
						value: 6,
						label: '已完成'
					},
					{
						value: 7,
						label: '继续租赁'
					},
					{
						value: 8,
						label: '买断'
					},
					{
						value: 9,
						label: '已取消'
					},
					{
						value: 10,
						label: '待评价'
					}
				],
				statusnum: [{
					value: '',
					label: '全部'
				}, {
					value: 1,
					label: '包邮'
				}, {
					value: 2,
					label: '邮寄'
				}, {
					value: 3,
					label: '自取'
				}],
				checkBoxData: [], //多选框选择的值
				userId: '',
				userHead: '',
				userName: '',
				storeId: 0,
				storeHead: '',
				storeName: '优宝物自营',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: [],
				userData: {},
				fenxiaoData: [],
				totalMoney: 0,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				startTime: '',
				endTime: '',
				consigneeT: '',
				mobileT: '',
				sellerNumberT: '',
				expressNumberT: '',
				isExpressT: '',
				dialogVisible: false,
				wuliuDate: {
					list: [],
				},
				reverse: true,
				dialogFormVisibleYj:false,
				yajinNum:'',
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后两位
					realVal = parseFloat(value).toFixed(2)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		methods: {
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
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			// 刷新
			refresh() {
				this.dataSelect()
			},
			// 刷新
			refresh1() {
				this.fenxiaoSelect()
			},
			// 订单评价跳转
			discuss(index, rows) {
				this.$router.push({
					path: '/orderDiscuss',
					query: {
						orderId: rows.id
					}
				});
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 发货
			colonelClick(rows) {


				if (rows.isExpress == 3) {
					this.id = rows.id
					this.$http({
						url: this.$http.adornUrl2('/orders/expressVirtual'),
						method: 'get',
						params: this.$http.adornParams({
							'id': rows.id
						})
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
				} else {
					if (rows.mobile == '' || rows.mobile == null) {
						this.$message({
							message: '手机号为空，请完善收货人信息',
							type: 'error',
							duration: 1500
						})
						return
					}
					this.mobile = rows.mobile.substring(7, 11)
					this.dialogFormVisible = true
					this.id = rows.id
				}
			},
			deliverNoticeTo() {
				if (this.expressName == '') {
					this.$message({
						message: '请输入快递公司',
						type: 'error',
						duration: 1500
					})
					return
				}
				if (this.expressNumber == '') {
					this.$message({
						message: '请输入快递单号',
						type: 'error',
						duration: 1500
					})
					return
				}
				var expressNumber = this.expressNumber + ':' + this.mobile
				this.$http({
					url: this.$http.adornUrl2('/orders/express'),
					method: 'get',
					params: this.$http.adornParams({
						'id': this.id,
						'expressName': this.expressName,
						'sellerNumber': this.expressNumber
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.dialogFormVisible = false
						let returnData = data.data;
						this.orderName = returnData.expressName
						this.orderNumber = returnData.expressNumber
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.expressName = ''
								this.expressNumber = ''
								this.dataSelect()
							}
						})
					}
				})
			},
			// 退款、取消
			refundClick(row) {
				this.$confirm(`确定取消订单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/orders/refundMoney'),
						method: 'get',
						params: this.$http.adornParams({
							'ordersId': row.id
						})
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
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {})
			},
			// 联系买家
			contact(row) {
				this.userId = row.userId;
				this.$http({
					url: this.$http.adornUrl2(`/user/${row.userId}`),
					method: "get",
					params: this.$http.adornParams({}),
				}).then(({
					data
				}) => {
					let returnData = data.data;
					if (returnData == null) {
						this.$message({
							message: "用户信息不正确",
							type: "error",
							duration: 1500,
						});
					} else {
						this.userHead = returnData.userEntity.avatar;
						this.userName = returnData.userEntity.userName;
						this.contactSelect();
					}
				});
			},
			contactSelect() {
				this.$http({
					url: this.$http.adornUrl2("/chat/save"),
					method: "post",
					data: this.$http.adornData({
						userId: this.userId,
						userHead: this.userHead,
						userName: this.userName,
						storeId: this.storeId,
						storeHead: this.storeHead,
						storeName: this.storeName,
					}),
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						console.log("data :>> ", data.userName);
						this.$confirm(`联系买家${data.data.userName}?`, "提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning",
						}).then(() => {
							this.$router.push({
								path: "/vueMchat",
								query: {
									userId: data.data.userId,
									userName: data.data.userName
								}
							});
						});
					}
				});
			},
			// 拒绝退款
			refuseClick(row) {
				this.ordersId = row.id
				this.dialogFormVisible1 = true
			},
			refuseNoticeTo() {
				if (this.refusedRefund == '') {
					this.$message({
						message: '请输入拒绝退款理由',
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.refusedRefund = ''
							this.dataSelect()
						}
					})
				} else {
					this.$http({
						url: this.$http.adornUrl2('/orders/refusedRefund'),
						method: 'get',
						params: this.$http.adornParams({
							'ordersId': this.ordersId,
							'refusedRefund': this.refusedRefund,
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
								this.refusedRefund = ''
								this.dataSelect()
							}
						})
					})
				}

			},
			// 详情
			orderDetails(id) {
				this.$router.push({
					path: '/orderDetails',
					query: {
						id: id
					}
				})
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
			// 日期选择
			animeDat(value) {
				let vanumber = value
				if (vanumber === 3) {
					this.moneySelect()
					this.orderSelect()
				}
				if (vanumber === 2) {
					this.info.stockDate = this.info.stockDate.substring(0, 7)
					this.moneySelect()
					this.info.stockDate = this.getNowTime()
				}
				if (vanumber === 1) {
					this.info.stockDate = this.info.stockDate.substring(0, 4)
					this.moneySelect()
					this.info.stockDate = this.getNowTime()
				}
			},
			timeDate() {
				this.moneySelect()
				this.orderSelect()
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
				this.fenxiaoSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.fenxiaoSelect()
			},
			handleSizeChange2(val) {
				this.size = val;
				this.dataSelect2()
			},
			handleCurrentChange2(val) {
				this.page = val;
				this.dataSelect2()
			},
			handleClick(tab, event) {
				if (tab._props.label == '全部订单') {
					this.status = ''
					this.orderType = 0
					this.size = 10
					this.page = 1
					this.dataSelect()
				}
				if (tab._props.label == '待发货订单') {
					this.size = 10
					this.page = 1
					this.status = 1
					this.orderType = 0
					this.dataSelect()
				}
				if (tab._props.label == '待退款订单') {
					this.size = 10
					this.page = 1
					this.status = 6
					this.orderType = 0
					this.dataSelect()
				}
				if (tab._props.label == '分销订单') {
					this.size = 10
					this.page = 1
					this.status = 0
					this.fenxiaoSelect()
				}
				if (tab._props.label == '待核销订单') {
					this.orderNum = ''
					this.mobile = ''
					this.size = 10
					this.page = 1
					this.status = 3
					this.isExpress = 3
					this.orderType = 0
					this.dataSelect2()
				}
				if (tab._props.label == '已核销订单') {
					this.orderNum = ''
					this.mobile = ''
					this.size = 10
					this.page = 1
					this.status = 4
					this.isExpress = 3
					this.orderType = 0
					this.dataSelect2()
				}
			},
			//搜索
			select() {
				this.page = 1
				this.size = 10
				this.dataSelect()
			},
			cleans() {
				this.orderNum = ''
				this.status = ''
				this.orderType = 0
				this.startTime = ''
				this.endTime = ''
				this.mobileT = ''
				this.isExpressT = ''
				this.sellerNumberT = ''
				this.expressNumberT = ''
				this.dataSelect()
			},
			cleans3() {
				this.orderNum = ''
				this.orderType = 0
				this.startTime = ''
				this.endTime = ''
				this.consigneeT = ''
				this.mobileT = ''
				this.isExpressT = ''
				this.sellerNumberT = ''
				this.expressNumberT = ''
				this.dataSelect()
			},
			select1() {
				this.page = 1
				this.size = 10
				this.fenxiaoSelect()
			},
			cleans1() {
				this.phone = ''
				this.status = 0
				this.fenxiaoSelect()
			},
			select2() {
				this.page = 1
				this.size = 10
				this.dataSelect2()
			},
			cleans2() {
				this.orderNum = ''
				this.mobile = ''
				this.orderType = 0
				this.startTime = '',
					this.endTime = '',
					this.dataSelect2()
			},
			refresh2() {
				this.dataSelect2()
			},
			// 获取订单数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.totalMoney = 0
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl2('/orders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.size,
						'orderNum': this.orderNum,
						'status': this.status,
						'orderType': this.orderType,
						'startTime': this.startTime,
						'endTime': endTime,
						'consignee': this.consigneeT,
						'mobile': this.mobileT,
						'isExpress': this.isExpressT,
						'sellerNumber': this.sellerNumberT,
						'expressNumber': this.expressNumberT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						data.data.records[i].isExpress1 = (data.data.records[i].isExpress).toString()
						if (data.data.records[i].payMoney) {
							this.totalMoney = this.totalMoney + data.data.records[i].payMoney
						}
					}
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			// 获取分销订单数据
			fenxiaoSelect() {
				let page = this.page - 1
				this.totalMoney = 0
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/ordersRelation/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'userId': this.userId,
						'phone': this.phone,
						'status': this.status,
						'moneyFrom': this.moneyFrom
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.content) {
						if (data.data.content[i].payMoney) {
							this.totalMoney = this.totalMoney + data.data.content[i].payMoney
						}
					}
					let returnData = data.data;
					this.fenxiaoData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			// 获取待核销订单数据
			dataSelect2() {
				let page = this.page - 1
				this.totalMoney = 0
				this.tableDataLoading = true
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl2('/orders/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'orderNum': this.orderNum,
						'status': this.status,
						'title': this.title,
						'mobile': this.mobile,
						'isExpress': this.isExpress,
						'orderType': this.orderType,
						'startTime': this.startTime,
						'endTime': endTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.content) {
						if (data.data.content[i].payMoney) {
							this.totalMoney = this.totalMoney + data.data.content[i].payMoney
						}
					}
					let returnData = data.data;
					this.tableData2 = returnData
				})
			},
			// 批量核销
			volume(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				for (var i in ids) {
					this.$http({
						url: this.$http.adornUrl2('/orders/isExpress'),
						method: 'get',
						params: this.$http.adornParams({
							'ids': ids[i]
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '批量核销成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect2()
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
				}
			},
			// 核销
			volumeOne(row) {
				this.$confirm(`确定核销此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/orders/isExpress'),
						method: 'get',
						params: this.$http.adornParams({
							'ids': row.id
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '核销成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect2()
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
				}).catch(() => {})
			},
			// 导出
			exportBtn() {
				// if (this.endTime == '') {
				// 	this.endTime = this.info.stockDate
				// }
				// if (this.startTime == '') {
				// 	this.startTime = this.info2.stockDate2
				// }
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl2('/orders/listExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						'orderNum': this.orderNum,
						'status': this.status,
						'orderType': this.orderType,
						'startTime': this.startTime,
						'endTime': endTime,
						'consignee': this.consigneeT,
						'mobile': this.mobileT,
						'isExpress': this.isExpressT,
						'sellerNumber': this.sellerNumberT,
						'expressNumber': this.expressNumberT,

					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			exportBtnH() {
				var endTime = this.endTime
				if (this.endTime != '') {
					endTime = this.endTime + " 23:59:59"
				}
				this.$http({
					url: this.$http.adornUrl2('/orders/listExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'orderNum': this.orderNum,
						'status': this.status,
						'title': this.title,
						'mobile': this.mobile,
						'isExpress': this.isExpress,
						'orderType': this.orderType,
						'startTime': this.startTime,
						'endTime': endTime,


					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '订单列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
			// 确认收货
			shouClick(row) {
				this.$confirm(`确定用户已收货?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/orders/updateOrderStatus'),
						method: 'post',
						params: this.$http.adornParams({
							'id': row.id,
							'status': 4
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
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
				}).catch(() => {})
			},
			
			// 确认 归还
			wanchengClick1(row){
				
				this.id = row.id
				this.expressName = row.deposit
				this.expressNumber = ''
				this.yajinNum = row.deposit
				this.dialogFormVisibleYj = true
				
			}, 
			deliverNoticeToYj(row) {
				if (this.expressName === '') {
					this.$message({
						message: '请输入退还押金',
						type: 'error',
						duration: 1500
					})
					return
				}
				
				this.$confirm(`确定已归还完成订单?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/admin/payDetails/refundPayDetails'),
						method: 'post',
						params: this.$http.adornParams({
							'orderId': this.id,
							'money': this.expressName,
							'remark':this.expressNumber
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							// this.$message({
							// 	message: '操作成功',
							// 	type: 'success',
							// 	duration: 1500,
							// 	onClose: () => {
							// 		this.dataSelect()
							// 	}
							// })
							this.wanchengOrder()
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
							return
						}

					})
				}).catch(() => {})
				
			},
			// 完成订单
			wanchengOrder(){
				this.$http({
					url: this.$http.adornUrl2('/orders/orderComplete'),
					method: 'post',
					params: this.$http.adornParams({
						'id': this.id,
						'status': 6
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisibleYj = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								
								this.dataSelect()
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
			// 查看物流
			wuliuBtn(row, index) {
				if (index == 1) {
					if (row.sellerNumber) {
						var arr = row.sellerNumber.split(':')
						if (arr.length == 1) {
							var trackingNumber = row.sellerNumber + ':' + row.mobile.substring(7, 11)
							this.wuliuLisr(trackingNumber)
						} else {
							var trackingNumber = row.sellerNumber
							this.wuliuLisr(trackingNumber)
						}
					} else {
						this.$message({
							message: '暂无物流信息',
							type: 'error',
							duration: 1500
						})
					}

				} else {
					if (row.expressNumber) {
						console.log('=====',row.expressNumber)
						var arr = row.expressNumber.split(':')
						if (arr.length == 1) {
							var trackingNumber = row.expressNumber
							// var trackingNumber = row.expressNumber + ':' + row.sellerPhone.substring(7, 11)
							this.wuliuLisr(trackingNumber)
						} else {
							var trackingNumber = row.expressNumber
							this.wuliuLisr(trackingNumber)
						}
					} else {
						this.$message({
							message: '暂无物流信息',
							type: 'error',
							duration: 1500
						})
					}

				}


			},
			wuliuLisr(trackingNumber) {
				this.$http({
					url: this.$http.adornUrl2('/orders/findExpress'),
					method: 'get',
					params: this.$http.adornParams({
						'expressNumber': trackingNumber,
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.wuliuDate = data.data.result;
						this.dialogVisible = true
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500
						})
					}
				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>
<style scoped>
.order-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.order-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.order-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.order-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.order-page__filter-control {
	width: 180px;
}

.order-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.order-page__tabs {
	margin-bottom: 0;
}

.order-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.order-page__table .el-table {
	width: 100%;
}

.order-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.order-page__table .el-table td,
.order-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.order-page__summary {
	color: #b94a48;
	font-size: 16px;
	font-weight: 600;
	margin-top: 12px;
	line-height: 1.4;
}

.order-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.order-page__filter-actions .el-button--primary:not(.is-plain),
.order-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.order-page__filter-actions .el-button--primary:not(.is-plain):hover,
.order-page__filter-actions .el-button--primary:not(.is-plain):focus,
.order-page__table .el-button--primary:not(.is-plain):hover,
.order-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.order-page__table .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}

@media (max-width: 768px) {
	.order-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.order-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>