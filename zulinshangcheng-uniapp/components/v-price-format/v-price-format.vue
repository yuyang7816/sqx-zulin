<template>
  <view class="price flex" :style="[customStyle]">
    <view class="unit">￥</view>
    <view>
      <slot></slot>
    </view>
  </view>
</template>


<script>
export default {
  props: {
    customStyle: {
      type: Object
    }
  }
}
</script>


<style lang="scss" scoped>
.price {
  font-weight: 800;
	justify-content: center;
  .unit {
    font-weight: 0 !important;
    position: relative;
    transform-origin: center bottom;
    transform: scale(.75) translateY(-5%);
    margin-right: -5%;
		color: #DFC5A7;
  }

  view {
    vertical-align: text-bottom;
    align-self: baseline;
  }
}
</style>