<template>
	<view class="page-box" :style="{height:height}">
		<view class="centre" :style="{top:top}">
			<view class="empty-illustration">
				<view class="empty-glow empty-glow-left"></view>
				<view class="empty-glow empty-glow-right"></view>
				<view class="empty-card">
					<view class="empty-card-top">
						<view class="empty-dot"></view>
						<view class="empty-line empty-line-short"></view>
					</view>
					<view class="empty-card-body">
						<view class="empty-ring">
							<view class="empty-ring-core"></view>
						</view>
						<view class="empty-line empty-line-main"></view>
						<view class="empty-line empty-line-sub"></view>
						<view class="empty-pill-row">
							<view class="empty-pill"></view>
							<view class="empty-pill empty-pill-light"></view>
						</view>
					</view>
				</view>
			</view>
			<view class="tips">
				{{content}}
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			content: {
				type: String,
				default: '暂无数据'
			},
			height: {
				type: String,
				default: '90vh'
			},
			top: {
				type: String,
				default: '100px'
			},
		}
	}
</script>

<style lang="scss" scoped>
	.page-box {
		position: relative;
		left: 0;

		height: 95vh;
	}

	.centre {
		position: absolute;
		left: 0;
		top: 30%;
		right: 0;
		bottom: 0;
		margin: auto;
		text-align: center;
		// padding: 200rpx auto;
		font-size: 32rpx;

		.empty-illustration {
			position: relative;
			width: 340rpx;
			height: 270rpx;
			margin: 0 auto 20rpx;
		}

		.empty-glow {
			position: absolute;
			border-radius: 50%;
			filter: blur(8rpx);
			opacity: 0.9;

			&.empty-glow-left {
				width: 140rpx;
				height: 140rpx;
				left: 18rpx;
				top: 52rpx;
				background: radial-gradient(circle, rgba(21, 202, 210, 0.22) 0%, rgba(21, 202, 210, 0) 72%);
			}

			&.empty-glow-right {
				width: 120rpx;
				height: 120rpx;
				right: 20rpx;
				top: 24rpx;
				background: radial-gradient(circle, rgba(12, 152, 161, 0.18) 0%, rgba(12, 152, 161, 0) 74%);
			}
		}

		.empty-card {
			position: absolute;
			left: 50%;
			top: 24rpx;
			transform: translateX(-50%);
			width: 252rpx;
			height: 190rpx;
			padding: 22rpx;
			box-sizing: border-box;
			border-radius: 32rpx;
			background: linear-gradient(180deg, rgba(255, 255, 255, 0.96) 0%, rgba(239, 251, 252, 0.96) 100%);
			border: 1rpx solid rgba(255, 255, 255, 0.9);
			box-shadow: 0 18rpx 36rpx rgba(21, 202, 210, 0.12);
		}

		.empty-card-top {
			display: flex;
			align-items: center;
			gap: 12rpx;
		}

		.empty-dot {
			width: 16rpx;
			height: 16rpx;
			border-radius: 50%;
			background: linear-gradient(135deg, #2AD5DB 0%, #0C98A1 100%);
			box-shadow: 0 0 0 8rpx rgba(21, 202, 210, 0.08);
		}

		.empty-card-body {
			margin-top: 18rpx;
			display: flex;
			flex-direction: column;
			align-items: center;
		}

		.empty-ring {
			width: 76rpx;
			height: 76rpx;
			border-radius: 50%;
			background: linear-gradient(135deg, rgba(42, 213, 219, 0.18) 0%, rgba(12, 152, 161, 0.08) 100%);
			border: 2rpx solid rgba(21, 202, 210, 0.16);
			display: flex;
			align-items: center;
			justify-content: center;
		}

		.empty-ring-core {
			width: 32rpx;
			height: 32rpx;
			border-radius: 50%;
			background: linear-gradient(135deg, #2AD5DB 0%, #0C98A1 100%);
			box-shadow: 0 6rpx 14rpx rgba(21, 202, 210, 0.2);
		}

		.empty-line {
			height: 12rpx;
			border-radius: 999rpx;
			background: linear-gradient(90deg, rgba(21, 202, 210, 0.22) 0%, rgba(21, 202, 210, 0.08) 100%);

			&.empty-line-short {
				width: 96rpx;
			}

			&.empty-line-main {
				width: 132rpx;
				margin-top: 20rpx;
			}

			&.empty-line-sub {
				width: 96rpx;
				margin-top: 14rpx;
			}
		}

		.empty-pill-row {
			margin-top: 18rpx;
			display: flex;
			gap: 12rpx;
		}

		.empty-pill {
			width: 48rpx;
			height: 16rpx;
			border-radius: 999rpx;
			background: rgba(21, 202, 210, 0.16);

			&.empty-pill-light {
				width: 32rpx;
				background: rgba(12, 152, 161, 0.1);
			}
		}

		.tips {
			font-size: 32rpx;
			color: #1A2233;
			margin-top: 20rpx;
			font-weight: 700;
		}

		.btn {
			margin: 80rpx auto;
			width: 600rpx;
			border-radius: 32rpx;
			line-height: 90rpx;
			color: #ffffff;
			font-size: 34rpx;
			background: #5074FF;
		}
	}
</style>
