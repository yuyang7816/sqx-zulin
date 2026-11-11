<template>
	<div class="mission-page">
		<!-- hero -->
		<div class="nf-page-hero mission-page__hero">
			<span class="nf-page-hero__badge">任务中心</span>
			<h2 class="nf-page-hero__title">任务管理</h2>
			<p class="nf-page-hero__desc">管理任务发布与分类，审核用户提交任务，维护红包榜队列配置、审核与排名查询</p>
		</div>

		<!-- 筛选/操作白卡 -->
		<div class="nf-filter-card mission-page__filter" v-show="activeName !== 'sixth'">
			<div class="nf-filter-card__head">
				<i :class="activeName === 'seventh' ? 'el-icon-search' : 'el-icon-s-operation'"></i>
				<span>{{ activeName === 'seventh' ? '筛选条件' : '快捷操作' }}</span>
			</div>
			<div class="mission-page__filter-body" v-show="activeName === 'first'">
				<div class="mission-page__filter-actions">
					<el-button :disabled="!isAuth('mission:add')" size="mini" type="primary"
						icon="document" @click="addTasks()">发布任务</el-button>
					<el-button :disabled="!isAuth('mission:delete') || checkBoxData.length <= 0"
						size="mini" type="danger" icon="document" @click="delectTasks()">批量删除</el-button>
				</div>
			</div>
			<div class="mission-page__filter-body" v-show="activeName === 'third'">
				<div class="mission-page__filter-actions">
					<el-button :disabled="!isAuth('mission:add')" size="mini" type="primary"
						icon="document" @click="classifyStair()">添加分类</el-button>
					<el-button :disabled="!isAuth('mission:delete') || checkBoxData.length <= 0"
						size="mini" type="danger" icon="document" @click="deleteStairs()">批量删除</el-button>
				</div>
			</div>
			<div class="mission-page__filter-body" v-show="activeName === 'thirdly'">
				<div class="mission-page__filter-actions">
					<el-button :disabled="!isAuth('mission:pass') || checkBoxData.length <= 0"
						size="mini" type="primary" icon="document" @click="passClick()">通过</el-button>
					<el-button :disabled="!isAuth('mission:refuse') || checkBoxData.length <= 0"
						size="mini" type="primary" icon="document" @click="refuseClick()">拒绝</el-button>
				</div>
			</div>
			<div class="mission-page__filter-body" v-show="activeName === 'fourth'">
				<div class="mission-page__filter-actions">
					<el-button :disabled="!isAuth('mission:add')" size="mini" type="primary"
						icon="document" @click="addClick()">添加队列</el-button>
				</div>
			</div>
			<div class="mission-page__filter-body" v-show="activeName === 'fifth'">
				<div class="mission-page__filter-actions">
					<el-button :disabled="!isAuth('mission:pass') || checkBoxData.length <= 0"
						size="mini" type="primary" icon="document" @click="queueClick()">通过</el-button>
					<el-button :disabled="!isAuth('mission:refuse') || checkBoxData.length <= 0"
						size="mini" type="primary" icon="document" @click="queueRefuse()">拒绝</el-button>
				</div>
			</div>
			<div class="mission-page__filter-body mission-page__filter-body--search" v-show="activeName === 'seventh'">
				<div class="mission-page__filter-item">
					<label class="mission-page__filter-label">队列类型</label>
					<el-select v-model="id" placeholder="请选择类型" class="mission-page__filter-control"
						@change="selectTrigger(id)">
						<el-option v-for="item in orderStatus" :key="item.value" :label="item.name" :value="item.id">
						</el-option>
					</el-select>
				</div>
				<div class="mission-page__filter-item">
					<label class="mission-page__filter-label">手机号</label>
					<el-input class="mission-page__filter-control" @keydown.enter.native="select" clearable
						placeholder="请输入手机号" v-model="phone"></el-input>
				</div>
				<div class="mission-page__filter-actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" plain icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<!-- 表格白卡 + tabs -->
		<div class="nf-table-card mission-page__table">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="mission-page__tabs">
		<el-tab-pane label="任务管理" name="first">
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column prop="classifyName" label="分类">
				</el-table-column>
				<el-table-column prop="titlePicture" label="任务图片" width="80">
					<template slot-scope="scope">
						<img :src="scope.row.titlePicture" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="任务标题" width="150">
				</el-table-column>
				<el-table-column prop="content" label="任务内容" width="300">
				</el-table-column>
				<el-table-column prop="luckyValue" label="幸运值">
				</el-table-column>
				<el-table-column prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row.id)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('mission:update')"
							@click="redactTask(scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('mission:delete')"
							@click="delectTask(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="mission-page__pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="分类管理" name="third">
			<el-table v-loading="tableDataLoading" @selection-change="changeFun" :data="classifyData">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed type="index" label="编号" align="center" width="80">
					<template slot-scope="scope">
		 			<span>{{(page - 1) * size + scope.$index + 1}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="classifyName" label="分类名称">
				</el-table-column>
				<el-table-column label="操作" width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('mission:update')"
							@click="compile(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('mission:delete')"
							@click="deleteStair(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-tab-pane>
		<el-tab-pane label="任务审核" name="thirdly">
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="auditData">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="titlePicture" label="任务图片" width="80">
					<template slot-scope="scope">
						<img :src="scope.row.titlePicture" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="任务标题" width="160">
				</el-table-column>
				<el-table-column prop="Tcontent" label="用户内容">
				</el-table-column>
				<el-table-column label="用户编号">
					<template slot-scope="scope">
						<span style="color: #15cad2;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.userId}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="任务内容" width="300">
				</el-table-column>
				<el-table-column prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.state === 0 ">待提交</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 1 ">待审核</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 2 ">已通过</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 3 ">已拒绝</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 4 ">已放弃</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="lookTask(scope.row)">查看
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="mission-page__pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="任务进行中" name="sixth">
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="proceedData">
				<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column fixed prop="titlePicture" label="任务图片" width="80">
					<template slot-scope="scope">
						<img :src="scope.row.titlePicture" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="title" label="任务标题" width="160">
				</el-table-column>
				<el-table-column prop="Tcontent" label="用户内容">
				</el-table-column>
				<el-table-column label="用户编号">
					<template slot-scope="scope">
						<span style="color: #15cad2;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.userId}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="任务内容" width="300">
				</el-table-column>
				<el-table-column prop="state" label="状态" width="100">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.state === 0 ">待提交</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 1 ">待审核</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 2 ">已通过</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 3 ">已拒绝</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 4 ">已放弃</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('mission:warn')"
							@click="remind(scope.row)">提醒
						</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('mission:abandon')"
							@click="abandon(scope.row)">放弃
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="mission-page__pagination">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="红包榜分类" name="fourth">
			<el-table v-loading="tableDataLoading" :data="queueData">
				<el-table-column prop="name" label="队列名称">
				</el-table-column>
				<el-table-column prop="luckyValue" label="幸运值额度">
				</el-table-column>
				<el-table-column prop="sort" label="队列等级">
				</el-table-column>
				<el-table-column prop="recommendMoney" label="推荐奖" width="100">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.recommendMoney.toFixed(2)}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="handOut" label="派送红包" width="100">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{scope.row.handOut.toFixed(2)}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="numberOfPeople" label="所需人数" width="100">
				</el-table-column>
				<el-table-column prop="startPerson" label="队列开始所需人数" width="100">
				</el-table-column>
				<el-table-column prop="intervalPerson" label="队列间隔人数" width="100">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('mission:update')"
							@click="compileQueue(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('mission:delete')"
							@click="deleteQueue(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</el-tab-pane>
		<el-tab-pane label="红包榜审核" name="fifth">
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="checkData">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column prop="name" label="队列名称">
				</el-table-column>
				<el-table-column prop="imageUrl" label="用户图像">
					<template slot-scope="scope">
						<img :src="scope.row.imageUrl" alt="" width="60" height="60">
					</template>
				</el-table-column>
				<el-table-column prop="auditContent" label="审核内容">
				</el-table-column>
				<el-table-column prop="nickName" label="用户名称">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.state === 0 ">待审核</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 1 ">通过</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 2 ">拒绝</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div class="mission-page__pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="红包榜排名" name="seventh">
			<el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="rankData">
				<el-table-column type="selection">
				</el-table-column>
				<el-table-column prop="name" label="队列名称">
				</el-table-column>
				<el-table-column prop="nick_name" label="用户名">
				</el-table-column>
				<el-table-column prop="state" label="状态">
					<template slot-scope="scope">
						<span style="color: #15cad2;" v-if="scope.row.state === 1 ">正常</span>
						<span style="color: #15cad2;" v-if="scope.row.state === 2 ">晋级中</span>
					</template>
				</el-table-column>
				<el-table-column prop="create_time" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div class="mission-page__pagination">
				<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="totalnum">
				</el-pagination>
			</div>
		</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 添加分类 -->
		<el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input style="width:50%;" v-model="classifyName" type="text" placeholder="请输入分类名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入分类名称"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 编辑分类 -->
		<el-dialog title="编辑分类" :visible.sync="dialogFormVisible2" center>
			<el-form :model="form">
				<el-form-item label="分类名称：" :label-width="formLabelWidth">
					<el-input v-model="form.classifyName" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="分类排序：" :label-width="formLabelWidth">
					<el-input v-model="form.sort" style="width:65%;" type="number"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="CompileNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 查看任务 -->
		<el-dialog title="查看任务" :visible.sync="dialogFormVisible3">
			<div style="text-align: center;">{{Tcontent?Tcontent : '暂无内容'}}</div>
			<div v-for="(item,index) in (picture|| '').split(',')" style="text-align: center;">
				<img :src="item" alt="" style="width:50%;height: 100%;">
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="passClickrow(id)">通过</el-button>
			</div>
		</el-dialog>
		<!-- 任务拒绝弹框 -->
		<el-dialog title="判决理由" :visible.sync="dialogFormVisible5" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="auditContent" type="text" placeholder="请输入拒绝理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible5 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 添加队列类型 -->
		<el-dialog title="添加队列类型" :visible.sync="dialogFormVisible6" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">队列名称：</span>
				<el-input style="width:50%;" v-model="name" type="text" placeholder="请输入队列名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">幸运值额度：</span>
				<el-input style="width:50%;" v-model="luckyValue" type="number" placeholder="请输入幸运值额度"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">队列等级：</span>
				<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入队列等级"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">推荐奖：</span>
				<el-input style="width:50%;" v-model="recommendMoney" type="number" placeholder="请输入推荐奖"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">派送红包：</span>
				<el-input style="width:50%;" v-model="handOut" type="number" placeholder="请输入派送红包"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">所需人数：</span>
				<el-input style="width:50%;" v-model="numberOfPeople" type="number" placeholder="请输入所需人数">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">队列开始所需人数：</span>
				<el-input style="width:50%;" v-model="startPerson" type="number" placeholder="请输入队列开始所需人数">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">队列间隔人数：</span>
				<el-input style="width:50%;" v-model="intervalPerson" type="number" placeholder="请输入队列间隔人数">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible6 = false">取 消</el-button>
				<el-button type="primary" @click="queueNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 编辑队列类型 -->
		<el-dialog title="编辑队列类型" :visible.sync="dialogFormVisible7" center>
			<el-form :model="form2">
				<el-form-item label="队列名称：" :label-width="formLabelWidth">
					<el-input v-model="form2.name" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="幸运值额度：" :label-width="formLabelWidth">
					<el-input v-model="form2.luckyValue" style="width:65%;" type="number"></el-input>
				</el-form-item>
				<el-form-item label="队列等级：" :label-width="formLabelWidth">
					<el-input v-model="form2.sort" readonly style="width:65%;" type="number"></el-input>
				</el-form-item>
				<el-form-item label="推荐奖：" :label-width="formLabelWidth">
					<el-input v-model="form2.recommendMoney" style="width:65%;" type="number"></el-input>
				</el-form-item>
				<el-form-item label="派送红包：" :label-width="formLabelWidth">
					<el-input v-model="form2.handOut" style="width:65%;" type="number"></el-input>
				</el-form-item>
				<el-form-item label="所需人数：" :label-width="formLabelWidth">
					<el-input v-model="form2.numberOfPeople" style="width:65%;" type="number"></el-input>
				</el-form-item>
				<el-form-item label="队列开始所需人数：" :label-width="formLabelWidth">
					<el-input v-model="form2.startPerson" style="width:65%;" type="number"></el-input>
				</el-form-item>
				<el-form-item label="队列间隔人数：" :label-width="formLabelWidth">
					<el-input v-model="form2.intervalPerson" style="width:65%;" type="number"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible7 = false">取 消</el-button>
				<el-button type="primary" @click="CompileQueueTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 队列拒绝弹框 -->
		<el-dialog title="判决理由" :visible.sync="dialogFormVisible8" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
				<el-input style="width:50%;" v-model="auditContent" type="text" placeholder="请输入拒绝理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible8 = false">取 消</el-button>
				<el-button type="primary" @click="queueRefuseto()">确 定</el-button>
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
				state: 0,
				sort: '',
				id: '',
				totalnum: 0,
				readonly: true,
				name: '',
				phone: '',
				orderStatus: [],
				imageUrl: '',
				luckyValue: '',
				recommendMoney: '',
				handOut: '',
				numberOfPeople: '',
				startPerson: '',
				intervalPerson: '',
				proceedData: [],
				queueData: [],
				form2: {
					id: '',
					name: '',
					luckyValue: '',
					sort: '',
					recommendMoney: '',
					handOut: '',
					numberOfPeople: '',
					startPerson: '',
					intervalPerson: ''

				},
				openValue: 1,
				closeValue: 2,
				helpTakeId: '',
				helpMaintainId: '',
				auditContent: '同意',
				classifyId: '',
				classifyName: '',
				Tcontent: '',
				picture: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				tableData: [],
				rankData: [],
				info: {
					stockDate: this.getNowTime(), //日期
				},
				form: {
					id: '',
					sort: '',
					state: '',
					classifyName: '',
				},
				auditData: [],
				checkData: [],
				classifyData: [],
				checkBoxData: [], //多选框选择的值
			}
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 状态
			change(state, id) {
				this.$http({
					url: this.$http.adornUrl2(
						`/helpTaskPlatform/updateHelpTaskPlatformStateById?id=${id}&state=${state}`),
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
							this.dataSelect()
						}
					})
				})
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
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
				this.auditSelect()
			},
			handleCurrentChange1(val) {
				this.page = val;
				this.auditSelect()
			},
			handleSizeChange2(val) {
				this.size = val;
				this.proceedSelect()
			},
			handleCurrentChange2(val) {
				this.page = val;
				this.proceedSelect()
			},
			handleSizeChange3(val) {
				this.size = val;
				this.rankSelect()
			},
			handleCurrentChange3(val) {
				this.page = val;
				this.rankSelect()
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '任务管理') {
					this.dataSelect()
				}
				if (tab._props.label == '分类管理') {
					this.classifySelect()
				}
				if (tab._props.label == '任务审核') {
					this.auditSelect()
				}
				if (tab._props.label == '任务进行中') {
					this.proceedSelect()
				}
				if (tab._props.label == '红包榜分类') {
					this.queueSelect()
				}
				if (tab._props.label == '红包榜审核') {
					this.checkSelect()
				}
				if (tab._props.label == '红包榜排名') {
					this.rankSelect()
				}
			},
			// 修改任务
			redactTask(row) {
				this.$router.push({
					name: 'missionRedact',
					query: {
						id: row.id
					}
				});
			},
			//添加任务
			addTasks() {
				this.$router.push({
					path: '/missionAdd'
				});
			},
			//任务通过
			passClick(id, state) {
				this.state = 1
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl2(
						`/helpTaskPlatform/auditHelpGetTask/${ids}/${this.state}/${this.auditContent}`),
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
							this.auditSelect()
						}
					})
				})
			},
			//查看任务通过
			passClickrow(id) {
				this.state = 1
				this.$http({
					url: this.$http.adornUrl2(
						`/helpTaskPlatform/auditHelpGetTask/${id}/${this.state}/${this.auditContent}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible3 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.auditSelect()
						}
					})
				})
			},
			//任务拒绝
			refuseClick(id) {
				this.state = 2
				this.auditContent = ''
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				if (ids.length > 1) {
					this.$message({
						message: '拒绝不能批量，需要单条拒绝',
						type: 'error',
						duration: 1500,
					})
				} else {
					this.dialogFormVisible5 = true
				}
			},
			refuseto(id) {
				if (this.auditContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl2(
						`/helpTaskPlatform/auditHelpGetTask/${ids}/${this.state}/${this.auditContent}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible5 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.auditContent = ''
							this.auditSelect()
						}
					})
				})
			},
			// 查看任务
			lookTask(row) {
				this.dialogFormVisible3 = true
				this.Tcontent = row.Tcontent
				this.picture = row.picture
				this.id = row.id
			},
			// 批量删除任务
			delectTasks(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(
							`/helpTaskPlatform/deleteHelpTaskPlatformByIds/?ids=${ids}`),
						method: 'post',
						data: this.$http.adornData({})
				 }).then(({
						data
					}) => {
						this.$message({
							message: '批量删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
				  })
					})
				}).catch(() => {})
			},
			//删除任务
			delectTask(row) {
				let id = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/helpTaskPlatform/deleteHelpTaskPlatformById?id=${id}`),
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
								this.name = '';
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 添加分类弹框
			classifyStair() {
				this.dialogFormVisible = true
			},
			// 添加分类确定
			StairNoticeTo() {
				if (this.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl2('/helpClassify/saveHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'classifyName': this.classifyName,
						'state': this.state,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
				  message: '分类添加成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classifyName = ''
							this.classifySelect()
						}
					})
				})
			},
			// 修改分类
			compile(index, rows) {
				this.dialogFormVisible2 = true;
				this.form.id = rows.id;
				this.form.state = rows.state;
				this.form.classifyName = rows.classifyName;
				this.form.sort = rows.sort;
			},
			// 修改分类确定
			CompileNoticeTo() {
				this.$http({
					url: this.$http.adornUrl2('/helpClassify/updateHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'sort': this.form.sort,
						'state': this.form.state,
						'classifyName': this.form.classifyName,
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
							this.classifySelect()
						}
					})
				})
			},
			// 批量删除分类
			deleteStairs(id) {
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/helpClassify/deleteClassifyByIds/?ids=${ids}`),
						method: 'post',
						data: this.$http.adornData({})
				 }).then(({
						data
					}) => {
						this.$message({
							message: '批量删除成功',
				   type: 'success',
				   duration: 1500,
				  	onClose: () => {
								this.classifySelect()
							}
						})
					})
				}).catch(() => {})
			},
			//删除分类
			deleteStair(row) {
				let id = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/helpClassify/deleteClassifyById/?id=${id}`),
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
								this.name = '';
						  this.classifySelect()
							}
						})
					})
				}).catch(() => {})
			},
			//添加队列类型
			addClick() {
				this.dialogFormVisible6 = true
			},
			//添加队列类型
			queueNoticeTo() {
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入队列名称',
						type: 'warning'
					});
					return
				}
				if (this.luckyValue == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入幸运值额度',
						type: 'warning'
					});
					return
				}
				if (this.sort == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入队列等级',
						type: 'warning'
					});
					return
				}
				if (this.recommendMoney == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入推荐奖',
						type: 'warning'
					});
					return
				}
				if (this.handOut == '') {
					this.$notify({
						title: '提示',
				  duration: 1800,
						message: '请输入派送红包',
						type: 'warning'
					});
					return
				}
				if (this.numberOfPeople == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入所需人数',
						type: 'warning'
					});
					return
				}
				if (this.startPerson == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入队列开始所需人数',
						type: 'warning'
					});
					return
				}
				if (this.intervalPerson == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入间隔人数',
						type: 'warning'
				 });
					return
				}
				let state = 1
				this.$http({
					url: this.$http.adornUrl2('/lucky/insertLuckyQueue'),
					method: 'post',
					data: this.$http.adornData({
						'name': this.name,
						'luckyValue': this.luckyValue,
						'sort': this.sort,
						'recommendMoney': this.recommendMoney,
						'handOut': this.handOut,
						'numberOfPeople': this.numberOfPeople,
						'startPerson': this.startPerson,
						'intervalPerson': this.intervalPerson,
						'state': state,
						'createTime': this.info.stockDate
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible6 = false
					if (data.status == 0) {
						this.$message({
							message: '队列类型添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = ''
								this.luckyValue = ''
								this.sort = ''
								this.recommendMoney = ''
								this.handOut = ''
								this.numberOfPeople = ''
								this.startPerson = ''
								this.intervalPerson = ''
								this.queueSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
						})
					}
				})
			},
			// 编辑队列类型
			compileQueue(index, rows) {
				this.dialogFormVisible7 = true;
				this.form2.id = rows.id;
				this.form2.name = rows.name;
				this.form2.luckyValue = rows.luckyValue;
				this.form2.sort = rows.sort;
				this.form2.recommendMoney = rows.recommendMoney;
				this.form2.handOut = rows.handOut;
				this.form2.numberOfPeople = rows.numberOfPeople;
				this.form2.startPerson = rows.startPerson;
				this.form2.intervalPerson = rows.intervalPerson
			},
			// 编辑队列类型
			CompileQueueTo() {
				let state = 1
				this.$http({
					url: this.$http.adornUrl2('/lucky/updateLuckyQueue'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form2.id,
						'state': state,
						'name': this.form2.name,
						'luckyValue': this.form2.luckyValue,
						'sort': this.form2.sort,
						'recommendMoney': this.form2.recommendMoney,
						'handOut': this.form2.handOut,
						'numberOfPeople': this.form2.numberOfPeople,
						'createTime': this.info.stockDate,
						'startPerson': this.form2.startPerson,
						'intervalPerson': this.form2.intervalPerson
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible7 = false
					if (data.status == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.queueSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
						})
					}
				})
			},
			//删除队列类型
			deleteQueue(row) {
				let id = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(`/lucky/deleteLuckyQueue/?id=${id}`),
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
								this.queueSelect()
							}
						})
					})
				}).catch(() => {})
			},
			//队列审核通过
			queueClick(id, state) {
				var states = state ? [state] : this.checkBoxData.map(item => {
					return item.state
				})
				for (var i in states) {
					if (states[i] == 0) {
						this.state = 1
						var ids = id ? [id] : this.checkBoxData.map(item => {
							return item.id
						})
						this.$http({
							url: this.$http.adornUrl2(
								`/lucky/auditLuckyQueueRecord?ids=${ids}&state=${this.state}&auditContent=${this.auditContent}`
								),
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
									this.checkSelect()
								}
							})
						})
					} else {
						this.$message({
							message: '队列已经审核，请勿重复审核！',
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.checkSelect()
							}
						})
					}
				}
			},
			//队列拒绝
			queueRefuse(id) {
				this.state = 2
				this.auditContent = ''
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				if (ids.length > 1) {
					this.$message({
						message: '拒绝不能批量，需要单条拒绝',
						type: 'error',
						duration: 1500,
					})
				} else {
					this.dialogFormVisible8 = true
				}
			},
			queueRefuseto(id) {
				if (this.auditContent == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				var ids = id ? [id] : this.checkBoxData.map(item => {
					return item.id
				})
				this.$http({
					url: this.$http.adornUrl2(
						`/lucky/auditLuckyQueueRecord?ids=${ids}&state=${this.state}&auditContent=${this.auditContent}`
						),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible8 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.auditContent = ''
							this.checkSelect()
						}
					})
				})
			},
			// 获取任务数据列表
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/helpTaskPlatform/selectHelpTaskPlatformList'),
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
					this.tableData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			// 获取分类数据
			classifySelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/helpClassify/selectClassifyList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classifyData = returnData
				})
			},
			// 获取任务审核数据
			auditSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/helpTaskPlatform/selectHelpGetTaskList'),
					method: 'get',
					params: this.$http.adornParams({
						'size': this.size,
						'page': page
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.auditData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			// 获取任务进行中数据
			proceedSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/helpTaskPlatform/selectUnderwayHelpGetTaskList'),
					method: 'get',
					params: this.$http.adornParams({
						'size': this.size,
						'page': page
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.proceedData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			// 提醒任务
			remind(row) {
				this.$http({
					url: this.$http.adornUrl2(`/helpTaskPlatform/helpGetTaskRemind/${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '提醒成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.proceedSelect()
						}
					})
				})
			},
			// 放弃任务
			abandon(row) {
				this.$confirm(`确定放弃此条任务?`, '提示', {
					confirmButtonText: '确定',
				 cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl2(
							`/helpTaskPlatform/selectUnderwayHelpGetTaskList/${row.id}`),
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
								this.proceedSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取队列分类数据
			queueSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/lucky/selectLuckyQueueList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.queueData = returnData
				})
			},
			// 获取队列审核数据
			checkSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl2('/lucky/selectLuckyQueueRecordList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.checkData = returnData.content
					this.totalnum = returnData.totalElements
				})
			},
			// 搜索
			select() {
				this.rankSelect2()
			},
			// 重置
			cleans() {
				this.phone = ''
				this.id = ''
				this.rankSelect()
			},
			selectTrigger() {
				this.rankSelect2()
			},
			// 获取队列列表数据
			rankSelect() {
				let page = this.page - 1
				this.$http({
					url: this.$http.adornUrl2('/helpTaskPlatform/selectLuckyQueueList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.orderStatus = returnData
					let id = returnData[0].id
					this.$http({
						url: this.$http.adornUrl2('/helpTaskPlatform/selectLuckyQueueRanking'),
						method: 'get',
						params: this.$http.adornParams({
							'page': page,
							'size': this.size,
							'phone': this.phone,
							'id': id,
						})
					}).then(({
						data
					}) => {
						this.tableDataLoading = false
						let returnData = data.data;
						this.rankData = returnData.content
						this.totalnum = returnData.totalElements
					})
				})
			},
			// 获取队列列表数据
			rankSelect2() {
				let page = this.page - 1
				this.$http({
					url: this.$http.adornUrl2('/helpTaskPlatform/selectLuckyQueueRanking'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'size': this.size,
						'phone': this.phone,
						'id': this.id,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.rankData = returnData.content
					this.totalnum = returnData.totalElements
				})
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style scoped>
.mission-page {
	padding: 12px 16px;
	overflow-x: hidden;
}

.mission-page__filter-body {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px 16px;
}

.mission-page__filter-body--search {
	align-items: flex-end;
}

.mission-page__filter-item {
	display: flex;
	flex-direction: column;
	gap: 6px;
}

.mission-page__filter-label {
	font-size: 13px;
	color: var(--nf-text-secondary, #606266);
	line-height: 1;
}

.mission-page__filter-control {
	width: 180px;
}

.mission-page__filter-actions {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 8px;
	margin-left: auto;
}

.mission-page__tabs {
	margin-bottom: 0;
}

.mission-page__tabs >>> .el-tabs__header {
	margin-bottom: 0;
}

.mission-page__table .el-table {
	width: 100%;
}

.mission-page__table .el-table th {
	background: var(--nf-table-header-bg, #f7f8fa);
	color: var(--nf-text-primary, #303133);
	font-weight: 600;
}

.mission-page__table .el-table td,
.mission-page__table .el-table th {
	border-color: var(--nf-border, #e8eaed);
}

.mission-page__pagination {
	display: flex;
	justify-content: center;
	padding-top: 12px;
}

.mission-page__filter-actions .el-button--primary:not(.is-plain),
.mission-page__table .el-button--primary:not(.is-plain) {
	background-color: transparent;
	border-color: var(--nf-brand, #15cad2);
	color: var(--nf-brand, #15cad2);
}

.mission-page__filter-actions .el-button--primary:not(.is-plain):hover,
.mission-page__filter-actions .el-button--primary:not(.is-plain):focus,
.mission-page__table .el-button--primary:not(.is-plain):hover,
.mission-page__table .el-button--primary:not(.is-plain):focus {
	background-color: var(--nf-brand-lite, #e3f9fa);
	border-color: var(--nf-brand-deep, #109da4);
	color: var(--nf-brand-deep, #109da4);
}

.mission-page__table .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}

@media (max-width: 768px) {
	.mission-page__filter-control {
		width: 100%;
		min-width: 140px;
	}

	.mission-page__filter-actions {
		margin-left: 0;
		width: 100%;
	}
}
</style>
