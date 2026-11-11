<template>
  <view class="login-page">
    <view class="bg-layer">
      <view class="bg-gradient"></view>
      <view class="bg-blob bg-blob-1"></view>
      <view class="bg-blob bg-blob-2"></view>
      <view class="bg-blob bg-blob-3"></view>
      <view class="bg-fade"></view>
    </view>

    <view class="nav-back" @click="navBack">
      <u-icon name="arrow-left" size="32" color="#ffffff"></u-icon>
    </view>

    <view class="page-shell">
      <view class="hero-section">
        <view class="hero-showcase">
          <view class="hero-copy">
            <view class="brand-zone">
              <view class="logo-wrap">
                <image class="logo-img" src="/static/logo.png" mode="aspectFit"></image>
              </view>
              <view class="brand-copy">
                <text class="brand-name">码兄租赁</text>
                <text class="brand-slogan">品质数码 灵活租用</text>
              </view>
            </view>
            <text class="hero-title">轻松租到想要的数码好物</text>
            <text class="hero-desc">手机、平板、耳机等热门设备，按天起租，周期灵活</text>
            <view class="hero-tags">
              <text class="hero-tag">随租随还</text>
              <text class="hero-tag">新品尝鲜</text>
              <text class="hero-tag">顺丰发货</text>
            </view>
          </view>
          <view class="hero-device-cluster">
            <view class="device-float device-phone">
              <view class="phone-notch"></view>
              <view class="phone-screen">
                <view class="screen-glow"></view>
                <view class="screen-content">
                  <view class="screen-chip"></view>
                  <view class="screen-chip screen-chip--soft"></view>
                </view>
                <view class="screen-card"></view>
              </view>
            </view>
            <view class="device-float device-pad">
              <view class="pad-screen"></view>
            </view>
            <view class="device-float device-buds">
              <view class="bud"></view>
              <view class="bud bud-right"></view>
            </view>
          </view>
        </view>
      </view>

      <!-- #ifndef MP-WEIXIN -->
      <view class="login-card">
        <!-- <view class="section-head">
          <view class="section-accent"></view>
          <text class="section-title">验证码登录</text>
          <text class="section-desc">请输入已绑定手机号完成快捷登录</text>
        </view> -->

        <view class="form-zone">
          <view class="field-block">
            <text class="field-label">手机号</text>
            <view class="field-row" :class="{'field-focus': phoneFocus}">
              <view class="field-left">
                <u-icon name="phone" size="28" :color="phoneFocus ? '#15CAD2' : '#CBD5E1'"></u-icon>
              </view>
              <input class="field-input" type="number" maxlength="11" v-model="phoneData" placeholder="请输入手机号"
                placeholder-class="field-placeholder" @focus="phoneFocus = true" @blur="phoneFocus = false" />
            </view>
          </view>

          <view class="field-block">
            <view class="field-label-row">
              <text class="field-label">验证码</text>
              <text class="field-tip">短信验证</text>
            </view>
            <view class="field-row" :class="{'field-focus': codeFocus}">
              <view class="field-left">
                <u-icon name="lock" size="28" :color="codeFocus ? '#15CAD2' : '#CBD5E1'"></u-icon>
              </view>
              <input class="field-input" type="number" maxlength="6" v-model="verCode" placeholder="请输入验证码"
                placeholder-class="field-placeholder" @focus="codeFocus = true" @blur="codeFocus = false"
                @confirm="startReg" />
              <view class="code-btn" :class="{'code-disabled': sending}" @click="sendMsg">
                <text>{{sendTime}}</text>
              </view>
            </view>
          </view>

          <view class="field-block invite-block" :class="{'invite-show': show}">
            <text class="field-label">邀请码</text>
            <view class="field-row invite-row" :class="{'field-focus': inviteFocus}">
              <view class="field-left">
                <u-icon name="gift" size="28" :color="inviteFocus ? '#15CAD2' : '#CBD5E1'"></u-icon>
              </view>
              <input class="field-input" type="text" v-model="invitation" placeholder="请填写邀请码（选填）"
                placeholder-class="field-placeholder" @focus="inviteFocus = true" @blur="inviteFocus = false" />
            </view>
          </view>
        </view>

        <view class="helper-bar">
          <text class="helper-link" @click="navigate('/pages/public/loginphone')">密码登录</text>
          <text class="helper-link" @click="gozhuce()">快速注册</text>
        </view>

        <view class="btn-wrap" @click="startReg">
          <view class="login-btn" :class="{'btn-loading': isRotate}">
            <text v-if="!isRotate">立即登录</text>
            <u-icon v-else name="reload" size="32" color="#ffffff" class="btn-spin"></u-icon>
          </view>
        </view>

        <view class="protocol-bar">
          <view class="checkbox" :class="{'checked': showAgree}" @tap="isShowAgree">
            <u-icon v-if="showAgree" name="checkbox-mark" size="14" color="#fff"></u-icon>
          </view>
          <view class="protocol-text-wrap">
            <text class="protocol-text">已阅读并同意</text>
            <navigator url="/my/setting/xieyi?type=176" open-type="navigate">
              <text class="protocol-link">《隐私政策》</text>
            </navigator>
            <text class="protocol-text">及</text>
            <navigator url="/my/setting/xieyi?type=177" open-type="navigate">
              <text class="protocol-link">《注册协议》</text>
            </navigator>
          </view>
        </view>
      </view>
      <!-- #endif -->

      <!-- #ifdef MP-WEIXIN -->
      <view class="wx-card">
        <view class="section-head wx-section-head">
          <view class="section-accent"></view>
          <text class="section-title">微信快捷登录</text>
          <text class="section-desc">授权后自动获取登录所需信息</text>
        </view>

        <view class="wx-permission-card">
          <view class="wx-icon-wrap">
            <u-icon name="weixin-fill" size="40" color="#15CAD2"></u-icon>
          </view>
          <view class="wx-text-wrap">
            <text class="wx-title">申请获取以下权限</text>
            <text class="wx-desc">获得你的公开信息（昵称、头像、地区等）</text>
          </view>
        </view>

        <button v-if="weixinPhone" class="wx-btn primary" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">
          授权手机号
        </button>
        <button v-else class="wx-btn primary" bindtap="getUserProfile" @click="wxGetUserInfo">
          授权登录
        </button>

        <view class="protocol-bar wx-protocol">
          <view class="checkbox" :class="{'checked': showAgree}" @tap="isShowAgree">
            <u-icon v-if="showAgree" name="checkbox-mark" size="14" color="#fff"></u-icon>
          </view>
          <view class="protocol-text-wrap">
            <text class="protocol-text">同意</text>
            <navigator url="/my/setting/xieyi?type=176" open-type="navigate">
              <text class="protocol-link">《隐私政策》</text>
            </navigator>
            <text class="protocol-text">和</text>
            <navigator url="/my/setting/xieyi?type=516" open-type="navigate">
              <text class="protocol-link">《用户协议》</text>
            </navigator>
          </view>
        </view>
      </view>
      <!-- #endif -->
    </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {
        phoneFocus: false,
        codeFocus: false,
        inviteFocus: false,
        shows: false,
        mobile: '',
        code: '',
        weixinLogin: false,
        sending: false,
        sendTime: '获取验证码',
        count: 60,
        weixinPhone: false,
        sendDataList: {},
        sessionkey: '',
        phoneNum: false,
        phoneData: '',
        verCode: '',
        invitation: '',
        show: '',
        showAgree: false,
        isRotate: false,
        platform: "H5",
        sysphone: '1',
        isopen: false,
        searchBarTop: 60,
        searchBarHeight: 0,
        goodData: {},
        avatar: 'https://tcxxgj.xianmaxiong.com/file/uploadPath/2024/10/22/55b5afaacb619fdec242f9b50cf25aeb.png',
        inviterInviterCode: '',
        postData: {}
      };
    },
    onLoad(e) {
      if (e.invitationCode) {
        this.inviterInviterCode = e.invitationCode
      }
      // #ifdef APP-PLUS
      this.platform = 'app'
      //#endif
      //#ifdef H5
      this.platform = 'H5'
      // #endif 
      // #ifdef APP
      if (plus.os.name.toLowerCase() === 'android') {
        this.sysphone = 1;
      } else {
        this.sysphone = 2;
      }
      // #endif
      // #ifdef H5
      let ua = navigator.userAgent.toLowerCase();
      if (ua.indexOf('micromessenger') !== -1) {
        this.$Request.get(`/app/common/type/` + 108).then(res => {
          if (res.code == 0) {
            if (res.data && res.data.value && res.data.value == '是') {
              this.isopen = true;
            } else {
              this.isopen = false;
            }
          }
        })
      } else {
        this.isopen = false;
      }
      // #endif
      // #ifdef H5
      this.selbindwx()
      // #endif
      // #ifdef MP-WEIXIN
      let topHeight = uni.getSystemInfoSync().statusBarHeight
      let menuButtonInfo = uni.getMenuButtonBoundingClientRect();
      this.searchBarTop = topHeight;
      this.searchBarHeight = menuButtonInfo.height;
      // #endif
    },
    methods: {
      gozhuce() {
        uni.navigateTo({
          url: '/pages/public/register'
        })
      },
      navBack() {
        uni.switchTab({
          url: '/pages/index/index'
        })
      },
      selbindwx() {
        let ua = navigator.userAgent.toLowerCase();
        if (ua.indexOf('micromessenger') !== -1) {
          let openid = uni.getStorageSync('openid');
          if (window.location.href.indexOf('?code=') !== -1 || window.location.href.indexOf('&code=') !== -1) {
            let code;
            if (window.location.href.indexOf('?code=') !== -1) {
              code = window.location.href.split('?code=')[1].split('&')[0];
            } else {
              code = window.location.href.split('&code=')[1].split('&')[0];
            }
            let data = {
              code: code
            }
            this.$Request.getT('/app/Login/getOpenId', data).then(ret => {
              if (ret.code == 0) {
                uni.setStorageSync('openid', ret.data)
                this.$Request.getT('/app/Login/openid/login', {
                  openId: ret.data
                }).then(res => {
                  if (res.code == 0) {
                    uni.setStorageSync("phone", res.user.phone);
                    uni.setStorageSync("userId", res.user.userId);
                    uni.setStorageSync("token", res.token);
                    uni.setStorageSync('sex', res.user.sex)
                    uni.setStorageSync('openId', res.user.openId)
                    uni.setStorageSync("userName", res.user.userName);
                    uni.setStorageSync("avatar", res.user.avatar);
                    uni.setStorageSync("invitation", res.user.invitationCode);
                    uni.switchTab({
                      url: '/pages/index/index'
                    })
                  } else {
                    uni.navigateTo({
                      url: '/pages/public/bind'
                    });
                  }
                })
              }
            })
          }
        }
      },
      bingwx() {
        if (this.showAgree == false) {
          uni.showToast({
            title: '请先同意《隐私政策》和《用户服务协议》',
            icon: 'none'
          })
          return
        }
        let ua = navigator.userAgent.toLowerCase();
        if (ua.indexOf('micromessenger') !== -1) {
          let openid = uni.getStorageSync('openid');
          if (!openid) {
            if (window.location.href.indexOf('?code=') !== -1 || window.location.href.indexOf('&code=') !== -1) {
              let code;
              if (window.location.href.indexOf('?code=') !== -1) {
                code = window.location.href.split('?code=')[1].split('&')[0];
              } else {
                code = window.location.href.split('&code=')[1].split('&')[0];
              }
              this.$Request.get('/app/Login/getOpenId?code=' + code).then(ret => {
                if (ret.code == 0) {
                  uni.setStorageSync('openid', ret.data)
                  this.$Request.getT('/app/Login/openid/login?openId=' + ret.data).then(res => {
                    if (res.code == 0) {
                      uni.setStorageSync('sex', res.user.sex)
                      uni.setStorageSync("phone", res.user.phone);
                      uni.setStorageSync("userName", res.user.userName);
                      uni.setStorageSync("avatar", res.user.avatar);
                      uni.setStorageSync('openId', res.user.openId)
                      uni.setStorageSync('token', res.token)
                      uni.setStorageSync('userInfo', res.user)
                      uni.setStorageSync('userId', res.user.userId)
                      uni.setStorageSync('invitation', res.user.invitationCode)
                      uni.setStorageSync('recycleStatus', res.user.recycleStatus ? res.user.recycleStatus : '0')
                      uni.switchTab({
                        url: '/pages/index/index'
                      })
                    } else {
                      uni.navigateTo({
                        url: '/pages/public/bind'
                      });
                    }
                  })
                }
              })
            } else {
              let that = this;
              window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' +
                that.$queue.getWxAppid() + '&redirect_uri=' + window.location.href.split('#')[0] +
                '&response_type=code&scope=snsapi_userinfo#wechat_redirect';
            }
          } else {
            this.$Request.getT('/app/Login/openid/login', {
              openId: openid
            }).then(res => {
              if (res.code == 0) {
                uni.setStorageSync("phone", res.user.phone);
                uni.setStorageSync("userName", res.user.userName);
                uni.setStorageSync("avatar", res.user.avatar);
                uni.setStorageSync('openId', res.user.openId)
                uni.setStorageSync('token', res.token)
                uni.setStorageSync('userInfo', res.user)
                uni.setStorageSync('userId', res.user.userId)
                uni.setStorageSync('invitation', res.user.invitationCode)
                uni.setStorageSync('recycleStatus', res.user.recycleStatus ? res.user.recycleStatus : '0')
                uni.switchTab({
                  url: '/pages/index/index'
                })
              } else {
                uni.navigateTo({
                  url: '/pages/public/bind'
                });
              }
            })
          }
        }
      },
      getPhoneNumber: function(e) {
        if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {
          console.log('用户拒绝提供手机号');
        } else {
          this.setPhoneByInsert(e.detail.encryptedData, e.detail.iv);
        }
      },
      setPhoneByInsert(decryptData, iv) {
        let data = {
          decryptData: decryptData,
          key: this.sessionkey,
          iv: iv
        };
        this.$Request.postJson('/app/Login/selectPhone', data).then(res => {
          if (res.code == 0) {
            this.phone = res.data.data.phoneNumber;
            this.getWeixinInfo(this.sendDataList);
          }
        })
      },
      wxGetUserInfo(e) {
        if (this.showAgree == false) {
          uni.showToast({
            title: '请先同意《隐私政策》和《用户服务协议》',
            icon: 'none'
          })
          return
        }
        let params = {
          nickName: '微信用户',
          avatarUrl: this.avatar,
          sex: ''
        }
        this.login(params);
      },
      login({
        nickName,
        avatarUrl,
        sex
      }) {
        uni.showLoading({
          title: '正在登录中...'
        })
        let that = this;
        uni.login({
          provider: 'weixin',
          success: function(loginRes) {
            let data = {
              code: loginRes.code
            }
            that.$Request.getT('/app/Login/wxLogin', data).then(res => {
              if (res.code == 0) {
                uni.hideLoading()
                uni.setStorageSync('openId', res.data.open_id)
                uni.setStorageSync('unionId', res.data.unionId)
                that.sessionkey = res.data.session_key;
                uni.setStorageSync('recycleStatus', res.data.recycleStatus ? res.data.recycleStatus : '0')
                let sendData = {
                  openId: uni.getStorageSync('openId'),
                  unionId: uni.getStorageSync('unionId'),
                  userName: nickName,
                  avatar: avatarUrl,
                  sex: sex,
                  inviterCode: this.inviterInviterCode || this.invitation,
                };
                that.sendDataList = sendData;
                if (Number(res.data.isPhone) == 2) {
                  that.weixinPhone = true;
                } else if (Number(res.data.isPhone) == 1) {
                  that.getWeixinInfo(sendData);
                }
              } else {
                uni.showToast({
                  icon: 'none',
                  title: res.msg,
                  duration: 2000
                });
              }
            })
          }
        });
      },
      getWeixinInfo(sendData) {
        let that = this;
        uni.showLoading({
          title: '登录中...'
        });
        this.postData = {
          openId: uni.getStorageSync('openId'),
          unionId: uni.getStorageSync('unionId'),
          userName: sendData.userName,
          avatar: sendData.avatar ||
            'https://tcxxgj.xianmaxiong.com/file/uploadPath/2024/10/22/55b5afaacb619fdec242f9b50cf25aeb.png',
          sex: sendData.sex,
          phone: that.phone,
          inviterCode: this.inviterInviterCode || this.invitation
        };
        this.$Request.postJson('/app/Login/insertWxUser', this.postData).then(res => {
          uni.hideLoading();
          if (res.code == 0) {
            uni.setStorageSync('token', res.data.token)
            uni.setStorageSync('userInfo', res.data.user)
            uni.setStorageSync('userId', res.data.user.userId)
            uni.setStorageSync('invitation', res.data.user.invitationCode)
            uni.setStorageSync('openId', res.data.user.openId)
            uni.reLaunch({
              url: '/pages/index/index'
            })
          } else {
            uni.showModal({
              showCancel: false,
              title: '登录失败',
              content: res.msg
            });
          }
        })
      },
      isShowAgree() {
        this.showAgree = !this.showAgree
      },
      startReg() {
        if (this.showAgree != true) {
          uni.showToast({
            title: '请同意《隐私政策》和《用户服务协议》',
            icon: 'none'
          })
          return
        }
        if (this.phoneData == '') {
          uni.showToast({
            title: '请输入手机号',
            icon: 'none'
          })
          return
        }
        if (this.verCode == '') {
          uni.showToast({
            title: '请输入验证码',
            icon: 'none'
          })
          return
        }
        let _this = this
        let inviterCode = ''
        if (_this.inviterInviterCode) {
          inviterCode = _this.inviterInviterCode
        }
        uni.getSystemInfo({
          success(ret) {
            let data = {
              msg: _this.verCode,
              password: '',
              phone: _this.phoneData,
              openId: _this.$queue.getData('openid') ? _this.$queue.getData('openid') : '',
              inviterCode: inviterCode,
              platform: _this.platform,
              sysPhone: _this.sysphone,
              avatar: _this.avatar,
            }
            _this.$Request.postT('/app/Login/registerCode', data).then(res => {
              if (res.code == 0) {
                uni.setStorageSync("userId", res.data.user.userId);
                uni.setStorageSync("token", res.data.token);
                uni.setStorageSync("phone", res.data.user.phone);
                uni.setStorageSync("userName", res.data.user.userName);
                uni.setStorageSync("avatar", res.data.user.avatar);
                uni.setStorageSync("invitationCode", res.data.user.invitationCode);
                uni.setStorageSync("inviterCode", res.data.user.inviterCode);
                uni.hideLoading();
                uni.switchTab({
                  url: '/pages/my/index'
                })
              } else {
                uni.showToast({
                  title: res.msg,
                  icon: 'none'
                })
              }
            })
          }
        })
      },
      sendMsg() {
        if (this.sending) return;
        const {
          phoneData
        } = this;
        if (!phoneData) {
          uni.showToast({
            title: "请输入手机号",
            icon: 'none'
          });
        } else if (phoneData.length !== 11) {
          uni.showToast({
            title: "请输入正确的手机号",
            icon: 'none'
          });
        } else {
          uni.showLoading({
            title: "正在发送验证码..."
          });
          this.$Request.getT(`/app/Login/sendMsg/${phoneData}/3`).then(res => {
            if (res.code === 0) {
              if (res.data === 'register') {
                this.show = true;
              }
              this.sending = true;
              this.count = 60;
              this.sendTime = this.count + 's';
              uni.showToast({
                title: '验证码发送成功请注意查收',
                icon: 'none'
              });
              uni.hideLoading();
              let timer = setInterval(() => {
                this.count--;
                if (this.count <= 0) {
                  clearInterval(timer);
                  this.sending = false;
                  this.sendTime = '获取验证码';
                } else {
                  this.sendTime = this.count + 's';
                }
              }, 1000);
            } else {
              uni.hideLoading();
              uni.showModal({
                showCancel: false,
                title: '短信发送失败',
                content: res.msg ? res.msg : '请一分钟后再获取验证码',
              });
            }
          })
        }
      },
    }
  };
</script>

<style lang="scss" scoped>
  $primary: #15CAD2;
  $primary-dark: #0DA8B0;
  $primary-deep: #089199;
  $primary-soft: rgba(21, 202, 210, 0.06);
  $primary-glow: rgba(21, 202, 210, 0.12);
  $bg: #F0F4F8;
  $panel: #FFFFFF;
  $text-main: #0F172A;
  $text-sub: #475569;
  $text-hint: #94A3B8;
  $border: #E2E8F0;
  $border-light: #F1F5F9;
  $shadow-soft: 0 20rpx 60rpx rgba(15, 160, 170, 0.06);
  $shadow-card: 0 24rpx 80rpx rgba(15, 160, 170, 0.10);

  page {
    background: $bg;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
  }

  .login-page {
    min-height: 100vh;
    position: relative;
    overflow: hidden;
  }

  /* ========== 背景层：更柔和的空气感渐变 + 浮动光斑 ========== */
  .bg-layer {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 920rpx;
    z-index: 0;
    overflow: hidden;

    .bg-gradient {
      position: absolute;
      inset: 0;
      background: linear-gradient(165deg, #3AEBF2 0%, #1DD9E0 20%, #15CAD2 50%, #0DA8B0 80%, #089199 100%);
      border-radius: 0 0 120rpx 120rpx;
    }

    .bg-blob {
      position: absolute;
      border-radius: 50%;
      filter: blur(80rpx);
      pointer-events: none;
      animation: blobFloat 10s ease-in-out infinite;
    }

    .bg-blob-1 {
      width: 380rpx;
      height: 380rpx;
      background: rgba(255, 255, 255, 0.28);
      top: 8%;
      left: -8%;
      animation-delay: 0s;
    }

    .bg-blob-2 {
      width: 320rpx;
      height: 320rpx;
      background: rgba(255, 255, 255, 0.22);
      top: 2%;
      right: -10%;
      animation-delay: -3s;
    }

    .bg-blob-3 {
      width: 260rpx;
      height: 260rpx;
      background: rgba(255, 255, 255, 0.16);
      bottom: 15%;
      left: 35%;
      animation-delay: -6s;
    }

    .bg-fade {
      position: absolute;
      left: 0;
      right: 0;
      bottom: -2rpx;
      height: 200rpx;
      background: linear-gradient(180deg, rgba(240, 244, 248, 0) 0%, $bg 100%);
    }
  }

  @keyframes blobFloat {

    0%,
    100% {
      transform: translateY(0) scale(1);
    }

    50% {
      transform: translateY(-40rpx) scale(1.08);
    }
  }

  /* ========== 导航返回：更精致的玻璃 pill ========== */
  .nav-back {
    position: absolute;
    top: calc(var(--status-bar-height) + 24rpx);
    left: 32rpx;
    width: 72rpx;
    height: 72rpx;
    background: rgba(255, 255, 255, 0.18);
    backdrop-filter: blur(20rpx);
    -webkit-backdrop-filter: blur(20rpx);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1rpx solid rgba(255, 255, 255, 0.25);
    z-index: 10;
    box-shadow: 0 12rpx 32rpx rgba(8, 100, 105, 0.12);
    transition: all 0.25s ease;

    &:active {
      transform: scale(0.9);
      background: rgba(255, 255, 255, 0.3);
    }
  }

  /* ========== 页面外壳 ========== */
  .page-shell {
    position: relative;
    z-index: 2;
    padding: calc(var(--status-bar-height) + 110rpx) 32rpx 60rpx;
  }

  /* ========== Hero 区域：更强的玻璃拟态 + 简化设备插图 ========== */
  .hero-section {
    padding: 20rpx 10rpx 80rpx;
    color: #FFFFFF;
    position: relative;
    margin-bottom: 40rpx;
  }

  .hero-showcase {
    position: relative;
    padding: 40rpx 32rpx 36rpx;
    border-radius: 44rpx;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.22) 0%, rgba(255, 255, 255, 0.08) 100%);
    border: 1rpx solid rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(24rpx);
    -webkit-backdrop-filter: blur(24rpx);
    box-shadow: 0 32rpx 70rpx rgba(6, 100, 105, 0.18), inset 0 1rpx 0 rgba(255, 255, 255, 0.3);
    overflow: hidden;
  }

  .hero-copy {
    position: relative;
    z-index: 2;
    padding-right: 200rpx;
  }

  .brand-zone {
    display: flex;
    align-items: center;
    position: relative;
    z-index: 1;
  }

  .logo-wrap {
    width: 120rpx;
    height: 120rpx;
    border-radius: 36rpx;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.3) 0%, rgba(255, 255, 255, 0.1) 100%);
    border: 1rpx solid rgba(255, 255, 255, 0.25);
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    box-shadow: 0 20rpx 50rpx rgba(6, 90, 95, 0.2);
    backdrop-filter: blur(16rpx);
    position: relative;

    &::after {
      content: '';
      position: absolute;
      inset: 6rpx;
      border-radius: 30rpx;
      border: 1rpx solid rgba(255, 255, 255, 0.15);
    }
  }

  .logo-img {
    width: 80rpx;
    height: 80rpx;
    border-radius: 24rpx;
  }

  .brand-copy {
    margin-left: 24rpx;
    min-width: 0;
  }

  .brand-name {
    display: block;
    font-size: 48rpx;
    font-weight: 800;
    line-height: 1.1;
    letter-spacing: 1rpx;
    text-shadow: 0 8rpx 20rpx rgba(6, 85, 90, 0.25);
  }

  .brand-slogan {
    display: block;
    margin-top: 10rpx;
    font-size: 24rpx;
    line-height: 1.5;
    color: rgba(255, 255, 255, 0.95);
    font-weight: 500;
    text-shadow: 0 4rpx 12rpx rgba(6, 85, 90, 0.15);
  }

  .hero-title {
    display: block;
    margin-top: 28rpx;
    font-size: 30rpx;
    font-weight: 700;
    line-height: 1.4;
    text-shadow: 0 4rpx 10rpx rgba(6, 85, 90, 0.15);
  }

  .hero-desc {
    display: block;
    margin-top: 10rpx;
    font-size: 22rpx;
    line-height: 1.5;
    color: rgba(255, 255, 255, 0.88);
    max-width: 340rpx;
  }

  .hero-tags {
    display: flex;
    gap: 14rpx;
    margin-top: 24rpx;
    flex-wrap: wrap;
    position: relative;
    z-index: 1;
  }

  .hero-tag {
    height: 50rpx;
    line-height: 50rpx;
    padding: 0 20rpx;
    font-size: 20rpx;
    color: #FFFFFF;
    background: rgba(8, 130, 136, 0.35);
    border-radius: 25rpx;
    border: 1rpx solid rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10rpx);
    font-weight: 600;
    box-shadow: inset 0 1rpx 0 rgba(255, 255, 255, 0.15);
  }

  /* ========== 设备插图：更轻盈的抽象浮动设备 ========== */
  .hero-device-cluster {
    position: absolute;
    top: 50%;
    right: 20rpx;
    transform: translateY(-50%);
    width: 200rpx;
    height: 260rpx;
    z-index: 1;
  }

  .device-float {
    position: absolute;
    animation: deviceBob 4s ease-in-out infinite;
  }

  .device-phone {
    width: 100rpx;
    height: 160rpx;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.95) 0%, rgba(240, 252, 253, 0.9) 100%);
    border-radius: 18rpx;
    padding: 8rpx;
    box-shadow: 0 20rpx 40rpx rgba(8, 110, 116, 0.2), inset 0 0 0 1rpx rgba(255, 255, 255, 0.6);
    top: 20rpx;
    right: 10rpx;
    animation-delay: 0s;
    z-index: 3;

    .phone-notch {
      position: absolute;
      top: 8rpx;
      left: 50%;
      transform: translateX(-50%);
      width: 30rpx;
      height: 6rpx;
      border-radius: 0 0 6rpx 6rpx;
      background: rgba(49, 73, 86, 0.2);
      z-index: 2;
    }

    .phone-screen {
      width: 100%;
      height: 100%;
      border-radius: 14rpx;
      background: linear-gradient(180deg, #1FE0E5 0%, #15CAD2 60%, #0DA8B0 100%);
      overflow: hidden;
      position: relative;
    }

    .screen-glow {
      position: absolute;
      top: -20rpx;
      right: -20rpx;
      width: 60rpx;
      height: 60rpx;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.25);
      filter: blur(8rpx);
    }

    .screen-content {
      position: absolute;
      top: 40rpx;
      left: 10rpx;
      display: flex;
      gap: 6rpx;
    }

    .screen-chip {
      width: 18rpx;
      height: 18rpx;
      border-radius: 8rpx;
      background: rgba(255, 255, 255, 0.85);

      &.screen-chip--soft {
        background: rgba(255, 255, 255, 0.45);
      }
    }

    .screen-card {
      position: absolute;
      bottom: 12rpx;
      left: 10rpx;
      right: 10rpx;
      height: 28rpx;
      border-radius: 8rpx;
      background: rgba(255, 255, 255, 0.9);
      box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.05);
    }
  }

  .device-pad {
    width: 80rpx;
    height: 110rpx;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.85) 0%, rgba(230, 245, 247, 0.8) 100%);
    border-radius: 14rpx;
    padding: 6rpx;
    box-shadow: 0 16rpx 32rpx rgba(8, 110, 116, 0.15);
    top: 10rpx;
    right: 90rpx;
    animation-delay: -1.5s;
    z-index: 2;
    transform: rotate(-12deg);

    .pad-screen {
      width: 100%;
      height: 100%;
      border-radius: 10rpx;
      background: linear-gradient(180deg, #E0F7FA 0%, #B2EBF2 100%);
    }
  }

  .device-buds {
    width: 56rpx;
    height: 50rpx;
    bottom: 20rpx;
    right: 70rpx;
    animation-delay: -3s;
    z-index: 4;

    .bud {
      position: absolute;
      width: 22rpx;
      height: 38rpx;
      border-radius: 12rpx 12rpx 16rpx 16rpx;
      background: linear-gradient(180deg, #FFFFFF 0%, #E2ECF2 100%);
      box-shadow: 0 6rpx 12rpx rgba(90, 111, 126, 0.15);
      left: 0;
      transform: rotate(-10deg);

      &::after {
        content: '';
        position: absolute;
        left: 6rpx;
        bottom: -6rpx;
        width: 10rpx;
        height: 10rpx;
        border-radius: 0 0 6rpx 6rpx;
        background: inherit;
      }

      &.bud-right {
        left: auto;
        right: 0;
        transform: rotate(10deg);
      }
    }
  }

  @keyframes deviceBob {

    0%,
    100% {
      transform: translateY(0);
    }

    50% {
      transform: translateY(-12rpx);
    }
  }

  /* ========== 登录卡片：更柔和的悬浮感 ========== */
  .login-card {
    background: $panel;
    border-radius: 48rpx;
    padding: 48rpx 40rpx 44rpx;
    box-shadow: $shadow-card, 0 4rpx 12rpx rgba(15, 160, 170, 0.04);
    border: 1rpx solid rgba(255, 255, 255, 0.9);
    position: relative;
    overflow: hidden;
    margin-top: -90rpx;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 40rpx;
      right: 40rpx;
      height: 3rpx;
      background: linear-gradient(90deg, rgba(21, 202, 210, 0) 0%, rgba(21, 202, 210, 0.5) 50%, rgba(21, 202, 210, 0) 100%);
    }

    &::after {
      content: '';
      position: absolute;
      top: -60rpx;
      right: -60rpx;
      width: 220rpx;
      height: 220rpx;
      border-radius: 50%;
      background: radial-gradient(circle, rgba(21, 202, 210, 0.06) 0%, rgba(21, 202, 210, 0) 70%);
      pointer-events: none;
    }
  }

  .section-head {
    display: flex;
    flex-direction: column;
    padding-bottom: 36rpx;
    position: relative;
    z-index: 1;
    padding-left: 8rpx;
  }

  .section-accent {
    width: 72rpx;
    height: 8rpx;
    border-radius: 999rpx;
    background: linear-gradient(90deg, #15CAD2 0%, rgba(21, 202, 210, 0.15) 100%);
    margin-bottom: 20rpx;
  }

  .section-title {
    font-size: 40rpx;
    font-weight: 800;
    color: $text-main;
    line-height: 1.2;
    letter-spacing: 1rpx;
  }

  .section-desc {
    margin-top: 10rpx;
    font-size: 24rpx;
    line-height: 1.5;
    color: $text-sub;
  }

  .form-zone {
    padding-top: 4rpx;
  }

  .field-block {
    margin-bottom: 32rpx;
  }

  .field-label-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 14rpx;
  }

  .field-label {
    display: block;
    margin-bottom: 14rpx;
    font-size: 24rpx;
    font-weight: 600;
    color: $text-main;
  }

  .field-tip {
    font-size: 22rpx;
    color: $text-hint;
    font-weight: 500;
  }

  .field-row {
    display: flex;
    align-items: center;
    height: 104rpx;
    background: #F8FAFC;
    border-radius: 28rpx;
    padding: 0 28rpx;
    border: 2rpx solid transparent;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    position: relative;

    &.field-focus {
      background: #FFFFFF;
      border-color: rgba(21, 202, 210, 0.35);
      box-shadow: 0 0 0 6rpx rgba(21, 202, 210, 0.06), 0 12rpx 32rpx rgba(21, 117, 123, 0.06);
    }

    .field-left {
      width: 52rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
    }

    .field-input {
      flex: 1;
      height: 100%;
      font-size: 28rpx;
      color: $text-main;
      caret-color: $primary;
      margin-left: 12rpx;
      font-weight: 500;
    }

    .field-placeholder {
      color: #CBD5E1;
      font-size: 28rpx;
      font-weight: 400;
    }

    .code-btn {
      min-width: 156rpx;
      height: 64rpx;
      line-height: 64rpx;
      padding: 0 18rpx;
      margin-left: 16rpx;
      background: linear-gradient(180deg, rgba(21, 202, 210, 0.12) 0%, rgba(21, 202, 210, 0.05) 100%);
      border-radius: 20rpx;
      font-size: 22rpx;
      color: $primary;
      font-weight: 700;
      flex-shrink: 0;
      text-align: center;
      border: 1rpx solid rgba(21, 202, 210, 0.15);
      transition: all 0.2s ease;

      &:active {
        transform: scale(0.95);
        background: rgba(21, 202, 210, 0.18);
      }

      &.code-disabled {
        color: #CBD5E1;
        background: #F1F5F9;
        border-color: #E2E8F0;
        font-weight: 600;
      }
    }
  }

  .invite-block {
    max-height: 0;
    opacity: 0;
    margin-bottom: 0;
    overflow: hidden;
    transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);

    &.invite-show {
      max-height: 170rpx;
      opacity: 1;
      margin-bottom: 24rpx;
    }
  }

  .invite-row {
    margin-bottom: 0;
  }

  .helper-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-top: 8rpx;

    .helper-link {
      font-size: 26rpx;
      color: $text-sub;
      font-weight: 600;
      line-height: 1.5;
      transition: color 0.2s ease;

      &:active {
        color: $primary;
      }
    }
  }

  /* ========== 按钮：光泽感 + 按压动效 ========== */
  .btn-wrap {
    margin-top: 44rpx;
  }

  .login-btn {
    height: 100rpx;
    line-height: 100rpx;
    background: linear-gradient(135deg, #2EE5EC 0%, #15CAD2 45%, #0DA8B0 100%);
    border-radius: 32rpx;
    font-size: 32rpx;
    font-weight: 700;
    color: #ffffff;
    letter-spacing: 2rpx;
    text-align: center;
    box-shadow: 0 16rpx 40rpx rgba(21, 202, 210, 0.3), 0 4rpx 8rpx rgba(21, 202, 210, 0.2);
    position: relative;
    overflow: hidden;
    border: 1rpx solid rgba(255, 255, 255, 0.2);
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 60%;
      height: 100%;
      background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.25), transparent);
      animation: shimmer 4s infinite;
    }

    &:active {
      transform: scale(0.97);
      box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.2);
    }

    &.btn-loading {
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .btn-spin {
      animation: spin 1s linear infinite;
    }
  }

  @keyframes shimmer {
    0% {
      left: -100%;
    }

    100% {
      left: 200%;
    }
  }

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }

    to {
      transform: rotate(360deg);
    }
  }

  /* ========== 协议栏：更现代的勾选体验 ========== */
  .protocol-bar {
    display: flex;
    align-items: flex-start;
    justify-content: flex-start;
    margin-top: 32rpx;

    .checkbox {
      width: 36rpx;
      height: 36rpx;
      border-radius: 10rpx;
      border: 2rpx solid #D1D5DB;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 14rpx;
      margin-top: 2rpx;
      flex-shrink: 0;
      background: #FFFFFF;
      transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);

      &.checked {
        background: $primary;
        border-color: $primary;
        transform: scale(1.05);
      }

      &:active {
        transform: scale(0.9);
      }
    }

    .protocol-text-wrap {
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      gap: 4rpx;
    }

    .protocol-text {
      font-size: 22rpx;
      color: $text-hint;
      line-height: 1.6;
    }

    .protocol-link {
      font-size: 22rpx;
      color: $primary;
      font-weight: 700;
      line-height: 1.6;
    }

    &.wx-protocol {
      margin-top: 32rpx;
    }
  }

  /* ========== 微信登录卡片 ========== */
  .wx-card {
    background: $panel;
    border-radius: 48rpx;
    padding: 48rpx 40rpx 44rpx;
    box-shadow: $shadow-card, 0 4rpx 12rpx rgba(15, 160, 170, 0.04);
    border: 1rpx solid rgba(255, 255, 255, 0.9);
    position: relative;
    overflow: hidden;
    margin-top: -20rpx;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 40rpx;
      right: 40rpx;
      height: 3rpx;
      background: linear-gradient(90deg, rgba(21, 202, 210, 0) 0%, rgba(21, 202, 210, 0.5) 50%, rgba(21, 202, 210, 0) 100%);
    }

    &::after {
      content: '';
      position: absolute;
      top: -60rpx;
      right: -60rpx;
      width: 220rpx;
      height: 220rpx;
      border-radius: 50%;
      background: radial-gradient(circle, rgba(21, 202, 210, 0.06) 0%, rgba(21, 202, 210, 0) 70%);
      pointer-events: none;
    }
  }

  .wx-section-head {
    padding-bottom: 32rpx;
  }

  .wx-permission-card {
    background: linear-gradient(180deg, #FFFFFF 0%, #F8FAFC 100%);
    border: 2rpx solid $border-light;
    border-radius: 28rpx;
    padding: 32rpx;
    display: flex;
    align-items: center;
    margin-bottom: 28rpx;
    box-shadow: 0 4rpx 16rpx rgba(15, 160, 170, 0.03);

    .wx-icon-wrap {
      width: 80rpx;
      height: 80rpx;
      border-radius: 24rpx;
      background: linear-gradient(135deg, #E0F7FA 0%, #B2EBF2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      margin-right: 24rpx;
      box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.1);
    }

    .wx-text-wrap {
      flex: 1;
      min-width: 0;
    }

    .wx-title {
      display: block;
      font-size: 30rpx;
      color: $text-main;
      font-weight: 700;
      line-height: 1.4;
    }

    .wx-desc {
      display: block;
      margin-top: 8rpx;
      font-size: 24rpx;
      line-height: 1.5;
      color: $text-sub;
    }
  }

  .wx-btn {
    height: 100rpx;
    line-height: 100rpx;
    border-radius: 32rpx;
    font-size: 32rpx;
    font-weight: 700;
    color: #ffffff;
    letter-spacing: 2rpx;
    border: none;
    transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);

    &.primary {
      background: linear-gradient(135deg, #2EE5EC 0%, #15CAD2 50%, #0DA8B0 100%);
      box-shadow: 0 16rpx 40rpx rgba(21, 202, 210, 0.3);
    }

    &:active {
      transform: scale(0.97);
      box-shadow: 0 8rpx 20rpx rgba(21, 202, 210, 0.2);
    }

    &::after {
      border: none;
    }
  }
</style>