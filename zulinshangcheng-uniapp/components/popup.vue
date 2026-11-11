<template>
	<view class="page-box">
		<u-popup v-model="show" mode="center" :closeable="showClose" :mask-close-able="maskCloseAble" border-radius="20"
			@close="close" width="580upx" bgColor="linear-gradient(23deg, #37225D 0%, #313035 89%)">
			<view class="padding">
				<view class="text-center text-lg text-bold ">{{titles}}</view>
				<view class="textMoney" :style="custTE">{{content}}</view>
				<view class="flex align-center justify-center margin-top-xl" style="margin-bottom: 20rpx;">
					<view class="quxiao" @click="closeT()">{{closeName}}</view>
					<view class="sure" @click="sureT()">{{confirmName}}</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		props: {
			custTE: {
				type: String,
				default: ''
			},
			/**
			 * 取消按钮名称
			 */
			closeName: {
				type: String,
				default: '取消'
			},
			/**
			 * 确定按钮名称
			 */
			confirmName: {
				type: String,
				default: '确定'
			},
			/**
			 * 是否启用点击遮罩关闭弹窗(重载i-popup的mask-close-able属性)
			 */
			maskCloseAble: {
				type: Boolean,
				default: true,
			},
			/**
			 * 是否启用关闭图标(重载i-popup的closeable属性)
			 */
			showClose: {
				type: Boolean,
				default: false,
			},
			/**
			 * 弹框提示
			 */
			titles: {
				type: String,
				default: '提示'
			},
			/**
			 * 显示状态
			 */
			show: {
				type: Boolean,
				default: false
			},
			/**
			 * 显示内容
			 */
			content: {
				type: String,
				default: ''
			},
		},
		methods: {
			sureT() {
				this.$emit('sureT', {
					name: '确认'
				})
			},
			closeT() {
				this.$emit('closeT', {
					name: '取消'
				})
			},
			close() {
				this.$emit('close', {
					name: '取消'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.textMoney {
		width: 445rpx;
		text-align: center;
		margin: 20rpx auto;
		margin-top: 40rpx;
		color: #333333;
	}

	.quxiao {
		width: 220rpx;
		height: 70rpx;
		// background: rgba(171, 116, 255, 0.3);
		background-color: #F5F5F5;
		text-align: center;
		line-height: 70rpx;
		// background: #AB74FF;
		// opacity: 0.3;
		border-radius: 35px;
	}

	.sure {
		width: 220rpx;
		height: 70rpx;
		// background: linear-gradient(90deg, #F386BD 0%, #A470F8 100%);
		background: #15CAD2;
		text-align: center;
		line-height: 70rpx;
		border-radius: 35px;
		margin-left: 30rpx;
	}
</style>