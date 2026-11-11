<template>
	<div class="user-detail1-page">
		<!-- hero -->
		<div class="nf-page-hero user-detail1-page__hero">
			<span class="nf-page-hero__badge">用户管理</span>
			<h2 class="nf-page-hero__title">用户详情</h2>
			<p class="nf-page-hero__desc">查看用户基本信息、支付明细、评价、浏览收藏、优惠券与收货地址等完整资料</p>
		</div>

		<div class="nf-table-card user-detail1-page__main">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="user-detail1-page__tabs">
			<el-tab-pane label="用户详情" name="first">
				<div class="user-detail1-page__detail">
					<div class="nf-filter-card__head">
						<i class="el-icon-user"></i>
						<span>基本信息</span>
					</div>
					<div class="user-detail1-page__detail-table">
						<table>
							<tbody>
								<tr>
									<th>今日付款数量</th>
									<td>{{nowDayOrderNumber.count1 ? `${nowDayOrderNumber.count1}笔` : '0笔'}}</td>
									<th>本月付款数量</th>
									<td>{{nowDayOrderNumber.count2 ? `${nowDayOrderNumber.count2}笔` : '0笔'}}</td>
									<th>本月付款金额</th>
									<td class="border-rt">
										{{nowDayOrderNumber.count3 ? `${nowDayOrderNumber.count3}元` : '0元'}}
									</td>
								</tr>
								<!-- <tr>
								<th>金币</th>
								<td>{{tableData.totalGold}}</td>
									<th>上月付款金额</th>
									<td>{{nowDayOrderNumber.count4 ? `${nowDayOrderNumber.count4}元` : '0元'}}</td>
									<th>收益</th>
									<td style="color: rgb(245, 108, 108)">
										{{tableData.balance ? `${tableData.balance}元` : '0元'}} <span 
											style="color: #15cad2;cursor:pointer;"
											@click="withdraw(tableData.userId,tableData.balance)">提现提醒</span>
										<el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											style="color: #15cad2;background: #fff;border: none;"
											@click="rechargenone(tableData.userId)">修改
										</el-button>
									</td>
									<th>积分</th>
									<td class="border-rt">{{tableData.orderJifen?tableData.orderJifen:0}}</td>
								</tr>-->
								<tr>
									<th>上月付款金额</th>
									<td>{{nowDayOrderNumber.count4 ? `${nowDayOrderNumber.count4}元` : '0元'}}</td>
									<th>渠道</th>
									<td>
										{{tableData.platform }}
									</td>
									<th>已邀请人数</th>
									<td class="border-rt">{{totalnum}}人</td>
								</tr>
								<tr>
									<th>微信名称</th>
									<td>{{tableData.userName}}</td>
									<th>支付宝账号</th>
									<td>{{tableData.zhiFuBao ? tableData.zhiFuBao : '未绑定'}}</td>
									<!-- <th>地址</th>
                  <td>{{tableData.address ? tableData.address : '未绑定'}}</td> -->
									<th>支付宝姓名</th>
									<td class="border-rt">{{tableData.zhiFuBaoName ? tableData.zhiFuBaoName : '未绑定'}}
									</td>
								</tr>
								<tr>
									<th>创建时间</th>
									<td>{{tableData.createTime}}</td>
									<th>手机号</th>
									<td>{{tableData.phone ? tableData.phone : '未绑定'}}</td>
									<th>图像</th>
									<td class="border-rt"><img
											:src="tableData.image_url ? tableData.image_url : 'https://www.gomyorder.cn/logo.png'"
											width="80" height="80" /></td>
								</tr>
								<tr>
									<th>app微信登录openid</th>
									<td>{{tableData.wxOpenId ? tableData.wxOpenId : '未授权'}}</td>
									<th>性别</th>
									<td>{{tableData.sex == 2 ? '女' : (tableData.sex == 1 ?'男' : '未知') }}</td>
									<th>会员编号</th>
									<td class="border-rt">{{tableData.relationId ? tableData.relationId : '未绑定'}} <span
											class="user-detail1-page__link" @click="accredit(tableData.id)">
											{{tableData.isInvitation === 0 ? '立即授权' : '取消授权'}}</span> </td>
								</tr>
								<tr>
									<th></th>
									<td></td>
									<!-- <th>邀请人邀请码</th>
									<td>
										{{tableData.inviterCode}}
										<span style="color: #15cad2;cursor:pointer;"
											@click="colonelClick(tableData.inviterCode)">查看用户详情</span>
									</td> -->
									<th>邀请码</th>
									<td>{{tableData.invitationCode}}</td>
									<th>编号</th>
									<td class="border-rt">{{tableData.userId}}</td>
									<!-- <th>邀请人上级邀请码</th>
									<td class="border-rt">
										{{tableData.inviterInviterCode}}
									</td> -->
								</tr>
								<!-- <tr>
									<th>一级佣金比例</th>
									<td>{{tableData.divideScale}}</td>
									<th>二级佣金比例</th>
									<td>{{tableData.divideTwoScale}}</td>
									
								</tr> -->
								<tr>
									<th>用户类型</th>
									<td>
										<span v-if="tableData.userType==1">普通用户</span>
										<span v-if="tableData.userType==2">商家</span>
									</td>
									<th>是否是会员</th>
									<td>
										<span v-if="tableData.isVip==1">是</span>
										<span v-else>否</span>
									</td>
									<th>会员结束时间</th>
									<td class="border-rt">{{tableData.vipEndTime}}</td>
								</tr>
								<tr>
									<th class="border-bt">信誉评分</th>
									<td class="border-bt">
										<span v-if="tableData.reputationStatus==1">信誉差</span>
										<span v-if="tableData.reputationStatus==2">信誉良好</span>
										<span v-if="tableData.reputationStatus==3">信誉非常好</span>
									</td>
									<th class="border-bt">openId</th>
									<td class="border-bt">{{tableData.openId ? tableData.openId : '未绑定'}}</td>
									<th class="border-bt">用户状态</th>
									<td class="border-bt border-rt">
										<span v-if="tableData.status == 1">正常</span>
										<span v-else style="color: #f56c6c;">禁用</span>
										<!-- <span style="color: #15cad2;cursor:pointer;" @click="stateChange(tableData.id)">更改状态</span> -->
										<el-button size="mini" type="primary" :disabled="!isAuth('userList:updateStatus')"
											@click="stateChange(tableData.userId,tableData.status)">更改状态</el-button>
									</td>
								</tr>

							</tbody>
						</table>
					</div>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="团队成员" name="second">
				<el-table v-loading="tableDataLoading" :data="tableDatamin">
					<el-table-column fixed prop="id" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column fixed prop="nickName" label="昵称" width="150">
					</el-table-column>
					<el-table-column prop="phone" label="手机号" width="200">
						<template slot-scope="scope">
							<span style="color: #15cad2;cursor: pointer;"
								@click="updates(scope.row)">{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="100">
					</el-table-column>
					<el-table-column prop="gender" label="性别" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.gender == 1">男</span>
							<span v-if="scope.row.gender == 2">女</span>
						</template>
					</el-table-column>
					<el-table-column prop="relationId" label="会员编号" width="100">
						<template slot-scope="scope">
							<span>{{scope.row.isInvitation === 0 ? '未授权' : scope.row.relationId ? scope.row.relationId : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="invitation" label="邀请人" width="170">
						<template slot-scope="scope">
							<span style="color: #15cad2;cursor: pointer;"
								@click="colonelClick(scope.row.invitation)">{{scope.row.invitation ? scope.row.invitation : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="grade" label="等级">
						<template slot-scope="scope">
							<span>{{scope.row.grade * 100}}%</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝">
						<template slot-scope="scope">
							<span>{{scope.row.zhifubaoName}}{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="jifen" label="余额" width="120">
					</el-table-column>
					<el-table-column prop="orderJifen" label="积分">
					</el-table-column>
					<el-table-column prop="name" label="备注">
					</el-table-column>
					<el-table-column prop="pub_share_pre_fee_user_s" label="来源">
						<template slot-scope="scope">
							<span v-if="scope.row.platform == 'weixin' ">微信</span>
							<span v-if="scope.row.platform == 'wap' ">网站</span>
							<span v-if="scope.row.platform == 'app' ">APP</span>
							<span v-if="scope.row.platform == 'mp' ">小程序</span>
							<span v-if="scope.row.platform == '' "></span>
						</template>
					</el-table-column>
					<el-table-column fixed='right' prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row.id)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="totalnum">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="用户消息" name="third">
				<el-table v-loading="tableDataLoading" :data="userData.content">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户名称">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容" width="280">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="userData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="订单消息" name="fourth">
				<el-table v-loading="tableDataLoading" :data="userData.content">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户名称">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容" width="280">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="userData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="任务消息" name="fifth">
				<el-table v-loading="tableDataLoading" :data="userData.content">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户名称">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容" width="280">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="userData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="提现记录" name="sixth">
				<el-table v-loading="tableDataLoading" :data="withdrawData">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
					</el-table-column>
					<el-table-column prop="money" label="提现金额">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.money}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="state" label="状态" width="100">
						<template slot-scope="scope">
							<span style="color: #15cad2;cursor: pointer;" v-if="scope.row.state == 0">待转账</span>
							<span v-if="scope.row.state == 1">已转账</span>
							<span v-if="scope.row.state == -1">已退款</span>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="付款编号">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column prop="outAt" label="结束时间">
					</el-table-column>
				</el-table>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="我的积分" name="seventh">
				<el-table v-loading="tableDataLoading" :data="integralData">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="des" label="积分明细">
					</el-table-column>
					<el-table-column label="积分" width="80">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.number}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间" width="180">
					</el-table-column>
				</el-table>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="分销明细" name="fenxiao">
				<el-table v-loading="tableDataLoading" :data="fenxiaoData">
					<el-table-column prop="id" label="编号">
					</el-table-column>
					<el-table-column prop="userId" label="用户id">
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
					</el-table-column>
					<el-table-column prop="commissionPrice" label="订单佣金">
					</el-table-column>
					<el-table-column prop="status" label="分销状态">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 1">未到账</span>
							<span v-if="scope.row.status == 2">已到账</span>
						</template>
					</el-table-column>
					<el-table-column prop="detail" label="分销明细">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="totalnum">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<!-- <el-tab-pane label="钱包明细" name="eighth">
				<el-table v-loading="tableDataLoading" :data="walletData.content">
					<el-table-column prop="id" label="编号" width="50">
					</el-table-column>
					<el-table-column prop="title" label="标题" width="120">
					</el-table-column>
					<el-table-column prop="content" label="内容">
					</el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.money}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="walletData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="支付明细" name="ninth">
				<div class="user-detail1-page__filter">
					<div class="nf-filter-card__head">
						<i class="el-icon-search"></i>
						<span>筛选条件</span>
					</div>
					<div class="user-detail1-page__filter-body">
						<div class="user-detail1-page__filter-item">
							<label class="user-detail1-page__filter-label">开始时间</label>
							<el-date-picker class="user-detail1-page__filter-control" v-model="startTime" align="right"
								type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
							</el-date-picker>
						</div>
						<div class="user-detail1-page__filter-item">
							<label class="user-detail1-page__filter-label">截止时间</label>
							<el-date-picker class="user-detail1-page__filter-control" v-model="endTime" align="right"
								type="datetime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
							</el-date-picker>
						</div>
						<div class="user-detail1-page__filter-actions">
							<el-button size="mini" type="primary" icon="document" @click="timeDate2">查询</el-button>
						</div>
					</div>
				</div>
				<el-table v-loading="tableDataLoading" :data="rechargeData" class="user-detail1-page__table">
					<el-table-column fixed prop="id" label="编号" width="50">
					</el-table-column>
					<el-table-column prop="orderId" label="支付订单号" width="200">
					</el-table-column>
					<el-table-column prop="money" label="支付金额">
					</el-table-column>
					<el-table-column prop="userId" label="会员编号">
					</el-table-column>
					<el-table-column label="分类">
						<template slot-scope="scope">
							<span class="user-detail1-page__link" v-if="scope.row.classify == 1">微信APP</span>
							<span class="user-detail1-page__link" v-if="scope.row.classify == 2">微信公众号</span>
							<span class="user-detail1-page__link" v-if="scope.row.classify == 3">微信小程序</span>
							<span class="user-detail1-page__link" v-if="scope.row.classify == 4">支付宝APP</span>
							<span class="user-detail1-page__link" v-if="scope.row.classify == 5">支付宝H5</span>
							<span class="user-detail1-page__link" v-if="scope.row.classify == 6">零钱</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span class="user-detail1-page__link" v-if="scope.row.state == 0">待支付</span>
							<span class="user-detail1-page__link" v-if="scope.row.state == 1">支付成功</span>
							<span class="user-detail1-page__link" v-if="scope.row.state == 2">支付失败</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="170">
					</el-table-column>
					<el-table-column prop="payTime" label="支付时间" width="170">
					</el-table-column>
				</el-table>
				<div class="user-detail1-page__pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="totalnum2">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="消息推送" name="tenth">
				<div style="float: right;margin-right:2%;">
					<el-button style='margin: 10px 0;' size="mini" type="primary" icon="document"
						@click="magNotice">消息推送
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="userData.content">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户名称">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容" width="280">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="userData.totalElements">
					</el-pagination>
				</div>
				<el-dialog title="消息推送" :visible.sync="dialogFormVisible" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">消息标题：</span>
						<el-input style="width: 50%;" v-model="title" placeholder="请输入消息标题"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span
							style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">消息内容：</span>
						<el-input style="width: 50%;" type="textarea" rows="4" v-model="content"
							placeholder="请输入消息内容"></el-input>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible = false">取 消</el-button>
						<el-button type="primary" @click="magNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
			</el-tab-pane> -->
			<el-tab-pane label="用户评价" name="discuss">
				<el-table v-loading="tableDataLoading" :data="DiscussData.content" class="user-detail1-page__table">
					<el-table-column prop="commentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称">
					</el-table-column>
					<el-table-column prop="userHeader" label="头像">
						<template slot-scope="scope">
							<img :src="scope.row.userHeader" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评论内容" width="180">
					</el-table-column>
					<el-table-column prop="sku" label="商品规格名称" width="180">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" :disabled="!isAuth('brandShop:delete')"
								@click="deletes(scope.row)">
								删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="user-detail1-page__pagination">
					<el-pagination @size-change="handleSizeChange5" @current-change="handleCurrentChange5"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="DiscussData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="用户浏览" name="browse">
				<el-table v-loading="tableDataLoading" :data="browseData.content" class="user-detail1-page__table">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="coverImg" label="商品图片" width="100">
						<template slot-scope="scope">
							<img :src="scope.row.coverImg" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题">
					</el-table-column>
					<el-table-column prop="price" label="商品价格" width="180">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div class="user-detail1-page__pagination">
					<el-pagination @size-change="handleSizeChange6" @current-change="handleCurrentChange6"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="browseData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="用户收藏" name="collect">
				<el-table v-loading="tableDataLoading" :data="collectData.content" class="user-detail1-page__table">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="coverImg" label="商品图片" width="100">
						<template slot-scope="scope">
							<img :src="scope.row.coverImg" alt="" width="60" height="60">
						</template>
					</el-table-column>
					<el-table-column prop="title" label="商品标题">
					</el-table-column>
					<el-table-column prop="price" label="商品价格" width="180">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div class="user-detail1-page__pagination">
					<el-pagination @size-change="handleSizeChange7" @current-change="handleCurrentChange7"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="collectData.totalElements">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的优惠券" name="youhuiquan">
				<el-table v-loading="tableDataLoading" :data="userDataLl.records" class="user-detail1-page__table">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称">
					</el-table-column>
					<el-table-column prop="couponPicture" label="优惠券图片">
						<template slot-scope="scope">
							<img :src="scope.row.couponPicture" style="width: 50px; height: 50px;" />
						</template>
					</el-table-column>
					<el-table-column prop="money" label="优惠券金额">
					</el-table-column>
					<el-table-column prop="minMoney" label="最低消费">
					</el-table-column>
					<el-table-column prop="status" label="使用类型">
						<template slot-scope="scope">
							<span v-if="scope.row.status==0">正常</span>
							<span v-else-if="scope.row.status==1">已使用</span>
							<span v-else-if="scope.row.status==2">已失效</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="时间">
					</el-table-column>
				</el-table>
				<div class="user-detail1-page__pagination">
					<el-pagination @size-change="handleSizeChangeYhq" @current-change="handleCurrentChangeYhq"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="userDataLl.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的地址" name="dizhi">
				<el-table v-loading="tableDataLoading" :data="dizhiData.records" class="user-detail1-page__table">
					<el-table-column prop="addressId" label="编号" width="80"></el-table-column>
					<el-table-column prop="userName" label="姓名" width="100"></el-table-column>
					<el-table-column prop="userPhone" label="电话" width="120"></el-table-column>
					<el-table-column prop="money" label="地址">
						<template slot-scope="scope">
							<span>{{scope.row.address}}{{scope.row.addressDetail}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="lng" label="经度" width="160"></el-table-column>
					<el-table-column prop="lat" label="纬度" width="160"></el-table-column>
					<!-- <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column> -->
					<!-- <el-table-column fixed="right" prop="isDefault" label="是否默认" width="100">
						<template slot-scope="scope">
							<span style="color: #15cad2;" v-if="scope.row.isDefault === 0 ">否</span>
							<span style="color: red;" v-if="scope.row.isDefault === 1 ">是</span>
						</template>
					</el-table-column> -->
				</el-table>
				<div class="user-detail1-page__pagination">
					<el-pagination @size-change="handleSizeChangeDz" @current-change="handleCurrentChangeDz"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="dizhiData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 不可提现添加金额 -->
		<el-dialog title="修改金额" :visible.sync="dialogFormVisible1" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 'true',
				closeValue: 'false',
				state: 'false',
				size: 10,
				page: 1,
				limit: 10,
				userId: '',
				userclass: 0,
				phone: '',
				totalnum: 0,
				totalnum2: 0,
				startTime: '',
				endTime: '',
				moneyFrom: 0,
				flag: 1,
				content: '',
				title: '',
				status: 0,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				tablejifen: '',
				tableData: {},
				userData: [],
				tableDatamin: [],
				withdrawData: [],
				integralData: [],
				walletData: [],
				rechargeData: [],
				fenxiaoData: [],
				DiscussData: [],
				browseData: [],
				collectData: [],
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				relation_id: '',
				relationId: '',
				balance: '',
				tkstatus: '',
				invitation: '',
				nowDayOrderNumber: {},
				type: '',
				money: '',
				userDataLl: {},
				dizhiData: {},
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
			// 状态
			change(val, id) {
				this.$http({
					url: this.$http.adornUrl2(`user/updateState/${id}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.teamSelect()
						}
					})
				})
			},
			// 删除评论
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/selfGoodsComment/delete?id=${row.commentId}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.status == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}
					})
				}).catch(() => {})
			},
			// 团长跳转详情页
			colonelClick(relationId) {
				this.$http({
					url: this.$http.adornUrl2('/user/relationId'),
					method: 'get',
					params: this.$http.adornParams({
						'relationId': relationId,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					let id = returnData.id
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: id
						}
					})
					this.dataSelect()
				})
			},
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.id
					}
				})
				this.dataSelect()
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '用户详情') {
					this.dataSelect()
					this.teamSelect()
					this.nowDaySelect()
				}
				if (tab._props.label == '团队成员') {
					this.teamSelect()
				}
				if (tab._props.label == '用户消息') {
					this.userclass = 5
					this.userSelect()
				}
				if (tab._props.label == '订单消息') {
					this.userclass = 4
					this.userSelect()
				}
				if (tab._props.label == '任务消息') {
					this.userclass = 6
					this.userSelect()
				}
				if (tab._props.label == '消息推送') {
					this.userclass = 8
					this.userSelect()
				}
				if (tab._props.label == '提现记录') {
					this.withdrawSelect()
				}
				if (tab._props.label == '我的积分') {
					this.integralSelect()
				}
				if (tab._props.label == '分销明细') {
					this.phone = ''
					this.status = 0
					this.fenxiaoSelect()
				}
				if (tab._props.label == '钱包明细') {
					this.walletSelect()
				}
				if (tab._props.label == '支付明细') {
					this.rechargeSelect()
				}
				if (tab._props.label == '用户评价') {
					this.userDiscuss()
				}
				if (tab._props.label == '用户浏览') {
					this.userBrowse()
				}
				if (tab._props.label == '用户收藏') {
					this.userCollect()
				}
				if (tab._props.label == '我的优惠券') {
					this.limit = 10
					this.page = 1
					this.youhuiquanSelect()
				}
				if (tab._props.label == '我的地址') {
					this.limit = 10
					this.page = 1
					this.dizhiSelect()
				}
			},
			handleSizeChange(val) {
				this.size = val;
				this.teamSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.teamSelect()
			},
			handleSizeChange1(val) {
				this.size = val
				this.userSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.userSelect()
			},
			handleSizeChange2(val) {
				this.size = val
				this.walletSelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.walletSelect()
			},
			handleSizeChange3(val) {
				this.size = val
				this.rechargeSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleSizeChange4(val) {
				this.size = val
				this.fenxiaoSelect()
			},
			handleCurrentChange4(val) {
				this.page = val
				this.fenxiaoSelect()
			},
			handleSizeChange5(val) {
				this.size = val
				this.userDiscuss()
			},
			handleCurrentChange5(val) {
				this.page = val
				this.userDiscuss()
			},
			handleSizeChange6(val) {
				this.size = val
				this.userBrowse()
			},
			handleCurrentChange6(val) {
				this.page = val
				this.userBrowse()
			},
			handleSizeChange7(val) {
				this.size = val
				this.userCollect()
			},
			handleCurrentChange7(val) {
				this.page = val
				this.userCollect()
			},
			handleSizeChangeYhq(val) {
				this.limit = val
				this.youhuiquanSelect()
			},
			handleCurrentChangeYhq(val) {
				this.page = val
				this.youhuiquanSelect()
			},
			handleSizeChangeDz(val) {
				this.limit = val
				this.dizhiSelect()
			},
			handleCurrentChangeDz(val) {
				this.page = val
				this.dizhiSelect()
			},
			timeDate2() {
				this.rechargeSelect()
			},
			// 可提现金额
			withdraw(id, val) {
				if (val == 0) {
					this.$message({
						message: '可提现金额为0元',
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				} else {
					this.$confirm(`确定要推送提现消息吗?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl2(`/user/notification/${id}`),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							this.$message({
								message: '推送成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						})
					}).catch(() => {})
				}

			},
			//授权
			accredit(id) {
				this.$confirm(`确定要授权会员权限吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/user/updateInvitation/${id}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 更改状态
			stateChange(id,status) {
				if(status==1){
					var status1 = 2
				}else{
					var status1 = 1
				}
				this.$confirm(`确定要更改用户状态吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2('/user/updateUser'),
						method: 'post',
						// data: this.$http.adornData({
						params: this.$http.adornParams({
							'userId': id,
							'status': status1,
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible = true
			},
			// 消息推送
			magNoticeTo() {
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/cash/sendMsg'),
					method: 'get',
					params: this.$http.adornParams({
						'title': this.title,
						'content': this.content,
						'phone': this.phone,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					if (data.status == -100) {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.title = ''
								this.content = ''
								this.userSelect()
							}
						})
					}
					if (data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.title = ''
								this.content = ''
								this.userSelect()
							}
						})
					}
				})
			},
			// 获取数据列表
			dataSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2(`/user/${userId}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data;
						console.log(data.data)
						this.tableData = returnData.userEntity;
						this.phone = returnData.userEntity.phone
						this.tablejifen = Math.floor(returnData.userEntity.jifen)
					}
				})
			},
			// 获取用户付款统计
			nowDaySelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2('/orders/payCount'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.status === 0) {
						let returnData = data.data;
						this.nowDayOrderNumber = returnData;
					}
				})
			},
			// 获取团队成员
			teamSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2(`/user/${userId}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.status === 0) {
						let returnData = data.data;
						this.invitation = returnData.relationId
						this.$http({
							url: this.$http.adornUrl2(
								`/user/getInvitations/${this.invitation}/${this.page-1}/${this.size}`),
							method: 'get',
							params: this.$http.adornParams({})
						}).then(({
							data
						}) => {
							this.tableDataLoading = false
							let returnData = data.data;
							this.tableDatamin = returnData.content;
							this.totalnum = returnData.totalElements
							this.balance = returnData.content.length
						})
					}
				})
			},
			// 获取用户/订单消息
			userSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2(
						`/message/findType/${userId}/${this.userclass}/${this.page-1}/${this.size}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.userData = returnData;
				})
			},
			// 获取用户提现记录
			withdrawSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2(`/user/${userId}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.relationId = returnData.userEntity.relationId
						this.$http({
							url: this.$http.adornUrl2(`/cash/list/${this.relationId}`),
							method: 'get',
							params: this.$http.adornParams({})
						}).then(({
							data
						}) => {
							this.tableDataLoading = false
							let returnData = data.data;
							this.withdrawData = returnData;
						})
					}
				})
			},
			// 获取我的积分
			integralSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2(`/user/getUserJifenList/${userId}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.integralData = returnData;
				})
			},
			// 分销明细
			fenxiaoSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2('/ordersRelation/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'userId': userId,
						'phone': this.phone,
						'status': this.status,
						'moneyFrom': this.moneyFrom
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.fenxiaoData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			// 获取钱包明细
			walletSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				let page = this.page - 1
				this.$http({
					url: this.$http.adornUrl2(`/user/selectUserDetails`),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.walletData = returnData;
				})
			},
			// 获取支付明细
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2(`/user/${userId}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.phone = returnData.userEntity
							.phone //page=${this.page}&limit=${this.size}&phone=${this.phone}&startTime=${this.startTime}&endTime=${this.endTime}
						this.$http({
							url: this.$http.adornUrl2(
								`/cash/selectPayDetails`
							),
							method: 'post',
							params: this.$http.adornParams({
								'page': this.page,
								'limit': this.size,
								'userId': userId,
								// 'phone':this.phone,

							})
						}).then(({
							data
						}) => {
							this.tableDataLoading = false
							let returnData = data.data
							this.rechargeData = returnData.list
							this.totalnum2 = returnData.totalCount
						})
					}
				})
				this.tableDataLoading = true

			},
			// 获取用户评价
			userDiscuss() {
				let userId = this.$route.query.userId
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfGoodsComment/userList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.DiscussData = returnData
				})
			},
			// 获取用户浏览
			userBrowse() {
				let userId = this.$route.query.userId
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfUserBrowse/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'userId': userId,
						'createTime': this.createTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.browseData = returnData
				})
			},
			// 获取用户收藏数据
			userCollect() {
				let userId = this.$route.query.userId
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/selfUserCollect/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.collectData = returnData
				})
			},
			// 不可提现金额充值
			rechargenone() {
				this.dialogFormVisible1 = true
			},
			StairNoticeTo1() {
				let userId = this.$route.query.userId
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改类型',
						type: 'warning'
					})
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/user/updateUserMoneys'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'userId': userId,
						'type': this.type,
						'money': this.money
					})
				}).then(({
					data
				}) => {
					if (data.status == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.type = ''
								this.money = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible1 = false
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			// 我的优惠券
			youhuiquanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					//page=&limit=&userId=&couponType=&status=
					url: this.$http.adornUrl2('/admin/couponUser/getMyCouponList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'couponType': '',
						'status': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					console.log(data)
					this.tableDataLoading = false
					let returnData = data.data
					this.userDataLl = returnData
				})
			},
			// 我的地址
			dizhiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl2('/admin/address/getAddressList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.dizhiData = returnData
				})
			},
		},
		activated() {
			this.dataSelect()
			this.teamSelect()
			this.nowDaySelect()
		}
	}
</script>

<style scoped>
.user-detail1-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.user-detail1-page__tabs {
	margin-bottom: 0;
}

.user-detail1-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.user-detail1-page__detail-table {
	border: 1px solid var(--nf-border, #e8eaed);
	border-radius: var(--nf-radius-sm, 6px);
	overflow: hidden;
}

.user-detail1-page__detail-table table {
	width: 100%;
	border-collapse: collapse;
}

.user-detail1-page__detail-table tr {
	border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.user-detail1-page__detail-table tr th {
	background-color: var(--nf-table-header-bg, #f7f8fa);
	padding: 12px 16px;
	border-right: 1px solid var(--nf-border, #e8eaed);
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
	font-size: 13px;
	line-height: 1.5;
}

.user-detail1-page__detail-table tr td {
	padding: 12px 16px;
	border-right: 1px solid var(--nf-border, #e8eaed);
	border-bottom: 1px solid var(--nf-border, #e8eaed);
	color: var(--nf-text-secondary, #606266);
	font-size: 14px;
	line-height: 1.5;
	background: #fff;
}

.user-detail1-page__detail-table .border-rt {
	border-right: none !important;
}

.user-detail1-page__detail-table .border-bt {
	border-bottom: none !important;
}

.user-detail1-page__detail-table tr td.border-rt {
	width: 20%;
}

.user-detail1-page__detail-table img {
	border-radius: 4px;
	border: 1px solid var(--nf-border, #e8eaed);
	object-fit: cover;
}

.user-detail1-page__filter {
	margin-bottom: 12px;
	padding-bottom: 12px;
	border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.user-detail1-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.user-detail1-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.user-detail1-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.user-detail1-page__filter-control {
	width: 180px;
}

.user-detail1-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.user-detail1-page__table {
	width: 100%;
}

.user-detail1-page__table >>> .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.user-detail1-page__table >>> .el-table td,
.user-detail1-page__table >>> .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.user-detail1-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.user-detail1-page__link {
	color: var(--nf-brand, #15cad2);
	cursor: pointer;
}

.user-detail1-page__link:hover {
	color: var(--nf-brand-deep, #109da4);
}

.user-detail1-page .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.user-detail1-page .el-button--primary:not(.is-plain):hover,
.user-detail1-page .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

@media (max-width: 768px) {
	.user-detail1-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.user-detail1-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>