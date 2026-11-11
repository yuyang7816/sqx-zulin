<template>
  <view class="dropdown">
    <!-- Dropdown Button -->
    <view @click="toggleDropdown" class="dropdown-button">
      {{ selectedOption || placeholder }}
      <uni-icons :type="isOpen ? 'arrowup' : 'arrowdown'" size="16"></uni-icons>
    </view>

    <!-- Options List -->
    <view v-if="isOpen" class="dropdown-options">
      <view v-for="(option, index) in options" :key="index" @click="selectOption(option)" class="dropdown-option">
        {{ option.label }}
      </view>
    </view>
  </view>
</template>

<script>
export default {
  props: {
    options: {
      type: Array,
      required: true,
      default: () => []
    },
    placeholder: {
      type: String,
      default: '请选择'
    }
  },
  data() {
    return {
      isOpen: false,
      selectedOption: null
    };
  },
  methods: {
    toggleDropdown() {
      this.isOpen = !this.isOpen;
    },
    selectOption(option) {
      this.selectedOption = option;
      this.$emit('input', option.value);
      this.isOpen = false;
    }
  }
};
</script>

<style scoped>
.dropdown {
  position: relative;
  width: 200px; /* Adjust as needed */
}

.dropdown-button {
  padding: 10px;
  border: 1px solid #ccc;
  background-color: #fff;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dropdown-options {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  border: 1px solid #ccc;
  max-height: 150px;
  overflow-y: auto;
  background-color: #fff;
}

.dropdown-option {
  padding: 10px;
  cursor: pointer;
}

.dropdown-option:hover {
  background-color: #f0f0f0;
}
</style>