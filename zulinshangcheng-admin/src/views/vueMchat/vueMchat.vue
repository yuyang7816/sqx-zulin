<template>
  <div class="mchat-page">
    <div class="mchat-hero">
      <span class="mchat-hero__badge">消息</span>
      <h1 class="mchat-hero__title">聊天室</h1>
      <p class="mchat-hero__desc">与用户实时沟通，查看咨询记录并处理订单消息</p>
    </div>

    <div class="mchat-card">
      <div class="whole-wrapper">
        <!-- 左边的聊天列表 -->
        <div class="left_whole_wrapper">
          <!-- 搜索框 -->
          <div class="search">
            <el-input
              placeholder="搜索联系人"
              suffix-icon="el-icon-search"
              v-model="userName"
              clearable
              @change="changeSearch"
              @input="searchChange"
            ></el-input>
          </div>
          <!-- 聊天列表 -->
          <div class="left_wrapper" style="overflow-y: auto" v-if="chatList">
            <div
              class="left-item"
              style="position: relative; cursor: pointer"
              v-bind:class="{ active: index == current }"
              v-for="(item, index) in chatList"
              :key="index"
              @click="clickItem(item, index)"
            >
              <div class="left-item-left">
                <div
                  class="left-item-head"
                  v-if="item.userHead == null || item.userHead == ''"
                >
                  <img src="~@/assets/img/head.png" />
                </div>
                <div class="left-item-head" v-else><img :src="item.userHead" /></div>
                <div class="left-item-info">
                  <div class="left-item-info_title">{{ item.userName }}</div>
                  <div class="left-item-info_content">{{ item.content }}</div>
                </div>
              </div>
              <div class="left-item-time">
                <div class="left-item-time-time" v-if="item.contentTime == null"></div>
                <div class="left-item-time-time" v-else>
                  {{ item.contentTime.substring(11, 16) }}
                </div>
                <div
                  class="left-item-time-number"
                  style="position: absolute; left: 59px; top: 8px"
                  v-if="item.storeCount"
                >
                  {{ item.storeCount }}
                </div>
                <div
                  class="left-item-delete"
                  @click="deletes(item)"
                  v-if="isAuth('vueMchat:delete')"
                >
                  <img src="~@/assets/img/delete.png" alt="" width="25" height="25" />
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 中间的对话-->
        <div class="center_whole_wrapper">
          <div class="center_wrapper">
            <!-- 上面的表头 -->
            <div class="center_wrapper_top">
              <div class="center_wrapper_top_name">{{ name }}</div>
              <el-button
                v-if="name"
                size="mini"
                class="mchat-detail-btn"
                @click="updates(userId)"
                >用户详情
              </el-button>
            </div>
            <!-- 中间的聊天框，尝试模拟数据 -->
            <div
              class="center_wrapper_center"
              id="ele"
              style="overflow-y: auto"
              ref="dialogue_box"
            >
              <div
                v-for="(item, index) in dialogueList"
                :key="index"
                :class="
                  item.sendType == '1'
                    ? center_wrapper_center_item
                    : center_wrapper_center_item_reserve
                "
              >
                <div class="left-item-head">
                  <img v-if="item.sendType == '1' && userHead !== null" :src="userHead" />
                  <img
                    v-else-if="item.sendType == '1' && userHead == null"
                    src="~@/assets/img/head.png"
                  />
                  <img v-else src="~@/assets/img/avatar.png" />
                </div>
                <!-- 文字 -->
                <div class="center-item-info_wrapper" v-if="item.type == 1">
                  <div class="center-item-tip">
                    <div class="center-item-info">
                      {{ item.content }}
                    </div>
                  </div>
                  <div class="center-item-time">{{ item.createTime }}</div>
                </div>
                <!-- 图片 -->
                <div class="center-item-info_wrapper" v-if="item.type == 2">
                  <div class="center-item-tip">
                    <div>
                      <el-popover placement="top-start" title="" trigger="hover">
                        <img
                          style="width: 100px; height: 100px"
                          :src="item.content"
                          alt=""
                          slot="reference"
                        />
                        <img
                          style="width: 300px; height: 300px"
                          :src="item.content"
                          alt=""
                        />
                      </el-popover>
                    </div>
                  </div>
                  <div class="center-item-time">{{ item.createTime }}</div>
                </div>
                <!-- 订单 -->
                <div class="center-item-info_wrapper" v-if="item.type == 3">
                  <div class="center-item-tip center-item-tip--card">
                    <div class="" @click="orderDetails(item.content.split(',')[2])">
                      <div style="display: flex">
                        <img
                          :src="item.content.split(',')[0]"
                          alt=""
                          width="40"
                          height="40"
                        />
                        <div class="center-item-order-title">
                          {{ item.content.split(",")[1] }}
                        </div>
                      </div>
                      <p class="hoverp center-item-order-meta">
                        订单编号: {{ item.content.split(",")[3] }}
                      </p>
                      <p class="center-item-order-meta">
                        创建时间:{{ item.content.split(",")[4] }}
                      </p>
                    </div>
                  </div>
                  <div class="center-item-time">{{ item.createTime }}</div>
                </div>
                <!-- 商品 -->
                <div class="center-item-info_wrapper" v-if="item.type == 4">
                  <div class="center-item-tip center-item-tip--card">
                    <div class="">
                      <img
                        :src="item.content.split(',')[0]"
                        alt=""
                        width="160"
                        height="120"
                      />
                      <p class="center-item-goods-title">
                        {{ item.content.split(",")[1] }}
                      </p>
                      <p class="center-item-goods-price">
                        ¥{{ item.content.split(",")[2] }}
                      </p>
                    </div>
                  </div>
                  <div class="center-item-time">{{ item.createTime }}</div>
                </div>
              </div>
            </div>
            <!-- 下面的输入框，发送 -->
            <div class="center_wrapper_right">
              <div class="icon-list">
                <div class="icon-item">
                  <el-upload
                    class="avatar-uploader"
                    v-model="coverImg"
                    :action="$http.adornUrl2('/alioss/upload')"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                  >
                    <div class="icon-item" title="发送文件">
                      <img class="icon-item-img" src="~@/assets/img/wenjian.png" />
                    </div>
                  </el-upload>
                </div>
              </div>
              <el-input
                type="textarea"
                id="textarea"
                :rows="2"
                placeholder="请输入内容"
                v-model="content"
                @keyup.enter.native="sendTextarea(1)"
              ></el-input>
              <el-button
                type="primary"
                class="mchat-send-btn"
                v-if="content"
                @click="sendTextarea(1)"
                >发送</el-button
              >
              <el-button
                type="primary"
                class="mchat-send-btn"
                v-else
                disabled
                @click="sendTextarea(1)"
                >发送</el-button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      chatList: [],
      page: 1,
      size: 5,
      storeId: 0,
      targetId: "",
      userName: "",
      userId: "",
      chatId: "",
      type: "",
      current: 0,
      coverImg: "",
      sendType: "2",
      faceList: [],
      content: "",
      name: "",
      userHead: "",
      numlist: {},
      info: {
        stockDate: this.getNowTime(),
      },
      emojiShow: false,
      dialogueInfo: {},
      dialogueList: [],
      loadText: "加载中...",
      pageNum: 1,
      loadText1: "加载中...",
      pageNum1: 1,
      center_wrapper_center_item: "center_wrapper_center_item",
      center_wrapper_center_item_reserve: "center_wrapper_center_item_reserve",
    };
  },
  created() {
    this.loadEmojis();
  },
  watch: {
    $route: function (to, from) {
      if (to.name == "vueMchat") {
        let ele = document.getElementById("ele");
        this.$nextTick(() => {
          ele.scrollTop = ele.scrollHeight;
        });
      }
    },
  },
  activated() {
    let { userId, userName } = this.$route.query;
    if (userId && userName) {
      this.userName = userName;
      this.current = 0;
    }else{
         this.userName = "";
    }

    this.dataSelect();
    this.initWebSocket();
  },
  methods: {
    initWebSocket() {
		const domain = window.location.hostname;
      this.websock = new WebSocket("wss://"+domain+"/wss/websocket/0");
      this.websock.onmessage = this.websocketonmessage;
      this.websock.onerror = this.websocketonerror;
      this.websock.onopen = this.websocketonopen;
      this.websock.onclose = this.websocketclose;
    },
    websocketonopen() {
      console.log("WebSocket连接成功");
    },
    websocketonerror() {
      console.log("WebSocket连接失败");
    },
    websocketonmessage(e) {
      let msg = JSON.parse(e.data);
      this.dialogueList.push(msg);
      console.log(msg);

      let ele = document.getElementById("ele");
      this.dataSelect();
      this.$nextTick(() => {
        ele.scrollTop = ele.scrollHeight;
      });
    },
    websocketclose(e) {
      console.log("已关闭连接", e);
    },
    loadEmojis() {
      const appData = require("@/assets/img/emojis.json");
      for (let i in appData) {
        this.faceList.push(appData[i].char);
      }
    },
    getNowTime() {
      var now = new Date();
      var year = now.getFullYear();
      var month = now.getMonth();
      var date = now.getDate();
      var hour = now.getHours();
      var minute = now.getMinutes();
      var second = now.getSeconds();
      month = month + 1;
      month = month.toString().padStart(2, "0");
      date = date.toString().padStart(2, "0");
      var defaultDate = `${year}-${month}-${date} ${hour}:${minute}:${second}`;
      return defaultDate;
      this.$set(this.info, "stockDate", defaultDate);
    },
    updates(userId) {
      this.$router.push({ path: "/userDetail", query: { userId: userId } });
    },
    orderDetails(id) {
      this.$router.push({ path: "/orderDetails", query: { id: id } });
    },
    getEmo(index) {
      var textArea = document.getElementById("textarea");
      function changeSelectedText(obj, str) {
        if (window.getSelection) {
          textArea.setRangeText(str);
          textArea.selectionStart += str.length;
          textArea.focus();
        } else if (document.selection) {
          obj.focus();
          var sel = document.selection.createRange();
          sel.text = str;
        }
      }
      changeSelectedText(textArea, this.faceList[index]);
      this.description = textArea.value;
      this.emojiShow = false;
      return;
    },
    changeSearch(e){
        if(!e){
            this.userName = ""
            this.dataSelect();
        }
    },
    searchChange() {
      this.dataSelect();
    },
    clickItem(item, index) {
      this.current = index;
      this.userId = item.userId;
      this.name = item.userName;
      this.userHead = item.userHead;
      this.chatId = item.chatId;
	  this.chatList[index].storeCount = 0
      this.clickItem2();
      this.initWebSocket();
    },
    clickItem2() {
      this.$http({
        url: this.$http.adornUrl2("/chatsContent/storeList"),
        method: "get",
        params: this.$http.adornParams({
          chatId: this.chatId,
        }),
      }).then(({ data }) => {
        let returnData = data.data;
        this.dialogueList = returnData;
        let ele = document.getElementById("ele");
        this.$nextTick(() => {
          ele.scrollTop = ele.scrollHeight;
        });
      });
    },
    sendTextarea(type) {
      let data = {
        content: this.content,
        type: type,
        createTime: this.info.stockDate,
        sendType: this.sendType,
        userId: this.userId,
        storeId: this.storeId,
        chatId: this.chatId,
      };
      this.numlist = data;
      data = JSON.stringify(data);
      this.websock.send(data);
      this.content = "";
      this.dialogueList.push(this.numlist);
      let ele = document.getElementById("ele");
      this.$nextTick(() => {
        ele.scrollTop = ele.scrollHeight;
      });
      this.initWebSocket();
    },
    deletes(item) {
      this.$confirm(`确定删除聊天记录，删除后不能恢复?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl2(`/chat/delete/?id=${item.chatId}`),
            method: "get",
            params: this.$http.adornParams({}),
          }).then(({ data }) => {
            this.$message({
              message: "删除成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.dataSelect();
              },
            });
          });
        })
        .catch(() => {});
    },
    handleAvatarSuccess(file, fileList) {
      this.coverImg = file.data;
      this.content = this.coverImg;
      this.sendTextarea(2);
    },
    dataSelect() {
      this.tableDataLoading = true;

      this.$http({
        url: this.$http.adornUrl2("/chat/list"),
        method: "get",
        params: this.$http.adornParams({
          userName: this.userName,
          storeId: this.storeId,
        }),
      }).then(({ data }) => {
        this.tableDataLoading = false;
        let returnData = data.data;
        this.chatList = returnData;

        this.clickItem(this.chatList[this.current], this.current);
      });
    },
  },
  mounted() {},
  destroyed() {
    window.clearInterval(this.timer);
    if (this.websock) {
      this.websock.close();
    }
  },
};
</script>
<style scoped>
/* ===== 配方 token ===== */
$brand: #15cad2;
$brand-deep: #109da4;
$brand-lite: #e3f9fa;
$page-bg: #F4F6FA;
$border: #E8EAED;
$text-primary: #292929;
$text-secondary: #909090;

.mchat-page {
  background: $page-bg;
  padding: 16px;
  overflow-x: hidden;
  min-height: calc(100vh - 120px);
}

/* hero */
.mchat-hero {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 3px solid $brand;
}

.mchat-hero__badge {
  display: inline-block;
  padding: 2px 10px;
  font-size: 12px;
  line-height: 20px;
  color: $brand-deep;
  background: $brand-lite;
  border-radius: 6px;
  margin-bottom: 8px;
}

.mchat-hero__title {
  margin: 0 0 8px;
  font-size: 20px;
  font-weight: 700;
  color: $text-primary;
  line-height: 1.4;
}

.mchat-hero__desc {
  margin: 0;
  font-size: 13px;
  color: $text-secondary;
  line-height: 1.5;
}

/* 主白卡 */
.mchat-card {
  background: #fff;
  border: 1px solid $border;
  border-radius: 8px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.whole-wrapper {
  display: flex;
  background: #fff;
  height: 600px;
  position: relative;
  overflow-x: hidden;
}

/* 左边部分 */
.left_whole_wrapper {
  display: flex;
  flex-direction: column;
  width: 280px;
  flex-shrink: 0;
  border-right: 1px solid $border;
  background: #fff;
}

.left_wrapper {
  flex: 1;
  background-color: #F7F8FA;
}

.left-item {
  display: flex;
  justify-content: space-between;
  padding: 16px 20px;
  background-color: transparent;
  border-left: 3px solid transparent;
  transition: background 0.15s;
}

.left-item:hover {
  background-color: rgba(21, 202, 210, 0.04);
}

.left-item.active {
  background-color: $brand-lite;
  border-left-color: $brand;
}

.left-item-left {
  display: flex;
  min-width: 0;
}

.left-item-head > img {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  object-fit: cover;
}

.left-item-info {
  margin-left: 12px;
  max-width: 135px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  min-width: 0;
}

.left-item-info_title {
  font-size: 14px;
  font-weight: 500;
  text-align: left;
  color: $text-primary;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.left-item.active .left-item-info_title {
  color: $brand-deep;
}

.left-item-info_content {
  font-size: 13px;
  font-weight: 400;
  text-align: left;
  color: $text-secondary;
  margin-top: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.left-item-time {
  display: flex;
  flex-direction: column;
  text-align: right;
  align-items: flex-end;
  flex-shrink: 0;
}

.left-item-time-time {
  font-size: 12px;
  text-align: right;
  color: $text-secondary;
}

.left-item-time-number {
  width: 20px;
  height: 20px;
  line-height: 20px;
  border-radius: 50%;
  background: #ff4949;
  color: #ffffff;
  text-align: center;
  font-size: 12px;
  margin-top: 5px;
}

.left-item-delete {
  position: relative;
  top: 6px;
  cursor: pointer;
  opacity: 0.6;
  transition: opacity 0.15s;
}

.left-item-delete:hover {
  opacity: 1;
}

.search {
  padding: 16px;
  background-color: #fff;
  border-bottom: 1px solid $border;
}

/* 中间部分 */
.center_whole_wrapper {
  flex: 1;
  background: $page-bg;
  min-width: 0;
}

.center_wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.center_wrapper_top {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  position: relative;
  background: #fff;
  border-bottom: 1px solid $border;
  flex-shrink: 0;
}

.center_wrapper_top_name {
  font-size: 16px;
  font-weight: 700;
  text-align: left;
  color: $text-primary;
}

.mchat-detail-btn {
  position: absolute;
  top: 50%;
  right: 20px;
  transform: translateY(-50%);
  border-radius: 6px;
  background-color: transparent !important;
  border-color: $brand !important;
  color: $brand !important;
}

.mchat-detail-btn:hover,
.mchat-detail-btn:focus {
  background-color: $brand-lite !important;
  border-color: $brand-deep !important;
  color: $brand-deep !important;
}

.center_wrapper_center {
  flex: 1;
  padding: 20px;
  background: $page-bg;
  overflow-x: hidden;
}

.center_wrapper_center_item {
  display: flex;
  margin-bottom: 24px;
  position: relative;
}

.center_wrapper_right {
  flex-shrink: 0;
  background-color: #ffffff;
  padding: 16px 20px 56px;
  position: relative;
  border-top: 1px solid $border;
}

.center-item-info_wrapper {
  margin: 0 12px;
  min-width: 0;
}

.center-item-tip {
  display: flex;
  align-items: center;
}

.center-item-tip--card {
  background-color: #fff;
  cursor: pointer;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid $border;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.center-item-order-title {
  width: 145px;
  font-size: 13px;
  line-height: 20px;
  margin-left: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.center-item-order-meta {
  font-size: 13px;
  color: $text-secondary;
  margin: 4px 0 0;
}

.center-item-goods-title {
  width: 160px;
  line-height: 20px;
  margin: 8px 0 4px;
  font-size: 13px;
  color: $text-primary;
}

.center-item-goods-price {
  color: #f56c6c;
  margin: 0;
  font-size: 14px;
  font-weight: 500;
}

.center_wrapper_center_item .center-item-info {
  background: #ffffff;
  border-radius: 8px;
  border: 1px solid $border;
}

.center_wrapper_center_item_reserve .center-item-info {
  background: $brand;
  border-radius: 8px;
  color: #fff;
}

.center-item-info {
  max-width: 336px;
  font-size: 14px;
  font-weight: 400;
  text-align: left;
  color: #333333;
  line-height: 22px;
  padding: 12px 16px;
  word-wrap: break-word;
  word-break: break-all;
}

.center_wrapper_center_item_reserve .center-item-info {
  color: #fff;
}

.center-item-time {
  font-size: 12px;
  text-align: left;
  color: $text-secondary;
  line-height: 22px;
  margin-top: 8px;
}

/* 反向样式 */
.center_wrapper_center_item_reserve {
  display: flex;
  flex-direction: row-reverse;
  margin-bottom: 24px;
  position: relative;
}

.center_wrapper_center_item_reserve .center-item-time {
  text-align: right;
}

.center_wrapper_center_item_reserve .center-item-tip {
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
}

/* 图标 */
.icon-list {
  display: flex;
  margin-bottom: 8px;
}

.icon-list > div:not(:first-child) {
  margin-left: 12px;
}

.icon-item-img {
  width: 25px;
}

/* 组件覆写 */
.whole-wrapper >>> .el-input__inner {
  border-radius: 6px;
  border-color: $border;
}

.whole-wrapper >>> .el-input__inner:focus {
  border-color: $brand;
}

.whole-wrapper >>> .el-textarea__inner {
  min-height: 80px;
  resize: none;
  border: 1px solid $border;
  border-radius: 6px;
  background: #ffffff;
  padding: 8px 12px;
}

.whole-wrapper >>> .el-textarea__inner:focus {
  border-color: $brand;
}

.mchat-send-btn {
  width: 88px;
  height: 36px;
  position: absolute;
  bottom: 16px;
  right: 20px;
  font-size: 13px;
  border-radius: 6px;
  background-color: transparent !important;
  border-color: $brand !important;
  color: $brand !important;
}

.mchat-send-btn:not(.is-disabled):hover,
.mchat-send-btn:not(.is-disabled):focus {
  background-color: $brand-lite !important;
  border-color: $brand-deep !important;
  color: $brand-deep !important;
}

.mchat-send-btn.is-disabled {
  background-color: #f5f7fa !important;
  border-color: $border !important;
  color: #c0c4cc !important;
}

/* 气泡三角 */
.center_wrapper_center_item_reserve .center-item-info::after {
  content: "";
  position: absolute;
  right: 55px;
  top: 12px;
  width: 0;
  height: 0;
  border: 5px solid $brand;
  border-top-color: transparent;
  border-right-color: transparent;
  border-bottom-color: transparent;
}

.center_wrapper_center_item .center-item-info::after {
  content: "";
  position: absolute;
  left: 55px;
  top: 12px;
  width: 0;
  height: 0;
  border: 5px solid #ffffff;
  border-top-color: transparent;
  border-left-color: transparent;
  border-bottom-color: transparent;
}

.loading {
  text-align: center;
  color: $text-secondary;
}

.loading span {
  margin-left: 10px;
}
</style>
<style lang="scss">
.el-popover {
  height: 200px;
  width: 400px;
  overflow: scroll;
  overflow-x: auto;
  font-size: 20px;
}
</style>
