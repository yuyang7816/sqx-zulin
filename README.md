<div align="center">

# **省钱兄 JAVA 租赁商城项目全套开源系统（JAVA + Uni-app + Vue）**

**服务端 · 管理端 · 用户端**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-1.8-orange.svg)](docs/服务端-功能说明.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.11-6DB33F.svg)](docs/服务端-功能说明.md)
[![Vue](https://img.shields.io/badge/Vue-2.5-42b883.svg)](zulinshangcheng-admin/package.json)
[![uni-app](https://img.shields.io/badge/uni--app-用户端-2b9939.svg)](zulinshangcheng-uniapp/manifest.json)

八年沉淀，分批次开源由 AI 辅助开发的系统源码。  
开源为生态，服务才是根基。欢迎各位开发者一同交流。

</div>

---
⭐ 如果觉得项目不错，请点击右上角 Star 收藏支持！您的 Star 是我们持续开源迭代的最大动力！

## **1. 项目简介**

**省钱兄租赁商城项目全套开源系统**是一套面向 **3C 数码、家电办公、户外运动等品类** 的设备租赁电商系统，内置 **商品分类与检索、租期下单、保证金与买断、快递 / 自提订单、归还与物流、会员与优惠券、积分与营销玩法、钱包提现、邀请推广、实名认证、在线客服与即时通讯**，采用前后端分离架构，服务端、管理端与 C 端用户应用全套代码开源。

服务端基于 **Spring Boot 2.6.11**（**Java 8**）与 **MyBatis-Plus** 构建，负责接口、数据库，以及短信、对象存储、支付、WebSocket 等外部能力。  
管理端基于 **Vue 2 + Element UI + Webpack** 开发，供平台运营在电脑浏览器中维护商品、分类、订单、用户、营销、财务与系统设置。  
用户端基于 **uni-app（Vue）** 开发，应用名「租赁商城」，覆盖 H5 与微信小程序：逛首页与分类、搜商品、选租期下单、订单全生命周期、归还与评价、钱包与 VIP 等。

本仓库为服务端、管理端与用户端全套源码。线上 H5 演示见 [https://leaseshopyh.xianmxkj.com](https://leaseshopyh.xianmxkj.com)（与仓库 `zulinshangcheng-uniapp` 构建产物同源）。

- **官方网站开源计划**：[https://www.xiansqx.com](https://www.xiansqx.com)
- **开源计划**：[https://opensource.xiansqx.cn](https://opensource.xiansqx.cn)
- **官方文档**：[功能说明与运行教程](docs/服务端-运行教程.md)
- **Gitee 仓库**：[https://gitee.com/maxiaodong1996/sqx-zulin.git](https://gitee.com/maxiaodong1996/sqx-zulin.git)
- **GitHub 仓库**：[https://github.com/yuyang7816/sqx-zulin.git](https://github.com/yuyang7816/sqx-zulin.git)


## **2. 移动端界面预览**


<div align="center">

<table>
<tr>
<td align="center" style="padding:4px 6px;"><img src="https://leaseshopyh.xianmxkj.com/file/uploadPath/2026/11/11/a6a80dff590ca1c6ff6ab918b1f6494d.png" width="148" alt="首页" style="display:block;border-radius:6px;" /><br/>首页</td>
<td align="center" style="padding:4px 6px;"><img src="https://leaseshopyh.xianmxkj.com/file/uploadPath/2026/11/11/3d3c84539f2e8dc7ad1c4b1c7a00ee14.png" width="148" alt="分类" style="display:block;border-radius:6px;" /><br/>分类</td>
<td align="center" style="padding:4px 6px;"><img src="https://leaseshopyh.xianmxkj.com/file/uploadPath/2026/11/11/ff0386d5b28729eb1dc184e0e400dabd.png" width="148" alt="订单" style="display:block;border-radius:6px;" /><br/>订单</td>
<td align="center" style="padding:4px 6px;"><img src="https://leaseshopyh.xianmxkj.com/file/uploadPath/2026/11/11/e14a82e2658aa1e2a3f720e7b0bd1a58.png" width="148" alt="我的" style="display:block;border-radius:6px;" /><br/>我的</td>
<td align="center" style="padding:4px 6px;"><img src="https://leaseshopyh.xianmxkj.com/file/uploadPath/2026/11/11/a4363819f3f5c6e32f3b356f23b7b67c.png" width="148" alt="登录" style="display:block;border-radius:6px;" /><br/>登录</td>
</tr>
</table>

</div>

## **3. 开源说明**

> **【省钱兄科技｜开源计划启动】**
>
> 八年沉淀，我们将分批次开源全套由 AI 辅助开发的系统源码。  
> 本项目采用 **Apache 2.0** 许可协议，源码供学习、技术研究、商业使用、二次开发与参考使用。  
> **开源为生态，服务才是根基；** 欢迎各位开发者一同交流。

## **4. 快速开始**

建议顺序：**先服务端，再管理端，最后用户端。** 管理端和移动端都要等服务端已经启动，才能登录和加载数据。

**说明：** 仓库里**不包含** `node_modules`（体积大、随系统生成）。克隆或下载后，在 **`zulinshangcheng-admin`** 目录下**必须先执行一次** `npm install`，再启动开发命令。用户端推荐用 HBuilderX 打开；若 uView 等组件样式异常，可在 `zulinshangcheng-uniapp` 目录执行一次 `npm install`。详细步骤见下方与 [docs](docs/管理端-运行教程.md) 中的运行教程。

### **4.1 服务端**

安装 **JDK 8**、**Maven 3.8+**、**MySQL 8**。新建数据库（建议库名 **`lease-shop`**，与脚本一致），导入 [`zulinshangcheng-server/sql/lease-shop.sql`](zulinshangcheng-server/sql/lease-shop.sql)。本地连接信息在 [`application-dev.yml`](zulinshangcheng-server/sqx-admin/src/main/resources/application-dev.yml) 中配置，**JDBC 里的库名须与建库名相同**。

默认配置文件 [`application.yml`](zulinshangcheng-server/sqx-admin/src/main/resources/application.yml) 中 `spring.profiles.active` 为 **`dev`**。请按本机环境修改 `application-dev.yml` 中的数据库账号密码。

在 `zulinshangcheng-server` 目录执行 Maven 打包后，运行 `sqx-admin` 模块下的 `com.sqx.SqxAdminApplication`。默认 **端口 9101**，上下文路径 **`/sqx_fast`**。接口文档：`http://127.0.0.1:9101/sqx_fast/swagger-ui.html`（Springfox Swagger）。

Redis 在部分配置中可按需开启（`spring.redis.open`）。若打开 Redis 缓存，请同步修改 `application.yml` 中的 `spring.redis` 段。

更完整的模块划分与接口约定见 [服务端功能说明](docs/服务端-功能说明.md) 与 [`zulinshangcheng-server/README.md`](zulinshangcheng-server/README.md)。

### **4.2 管理端**

安装 **Node.js**（建议 14+，能运行 Webpack 开发服务即可）。进入 `zulinshangcheng-admin` 目录，**首次运行前**安装依赖并启动：

```bash
cd zulinshangcheng-admin
npm install
npm run dev
```

`npm install` 会根据 `package.json` / `npm-shrinkwrap.json` 在本机生成 `node_modules`，只需在依赖变更或新克隆后执行。

开发站点默认端口 **8009**（占用时会自动换端口）。请将 `config/index.js` 中代理 `target` 指向本机服务端（`http://127.0.0.1:9101/sqx_fast`），或通过 `src/utils/httpRequest.js` 中的接口前缀联调。

演示管理端账号：`admin` / `123456`（以库中数据为准）。登录页名称为「省钱兄设备租赁系统」。

### **4.3 用户端（C 端）**

**HBuilderX（推荐）：** 打开 `zulinshangcheng-uniapp` 目录，运行到浏览器或微信开发者工具。应用名称是「租赁商城」，底部四个入口：**首页、分类、订单、我的**。

**依赖安装（样式异常时）：**

```bash
cd zulinshangcheng-uniapp
npm install
```

开发时请确认 [`zulinshangcheng-uniapp/common/config.js`](zulinshangcheng-uniapp/common/config.js) 中的接口地址指向本机或已部署的服务端（线上演示为 `https://leaseshopyh.xianmxkj.com/sqx_fast`）。本地联调可改为 `http://127.0.0.1:9101/sqx_fast`。

用户端账号以导入的 `lease-shop.sql` 为准；亦可直接打开线上 H5 体验。

短信、微信、支付、对象存储、地图等密钥需自行申请，并在管理端相关配置页填写。

## **5. 文档**

| 想做的事 | 打开哪一份 |
|---|---|
| 把服务端跑起来 | [服务端运行教程](docs/服务端-运行教程.md) |
| 看服务端能做什么 | [服务端功能说明](docs/服务端-功能说明.md) |
| 把管理端跑起来 | [管理端运行教程](docs/管理端-运行教程.md) |
| 看管理端能做什么 | [管理端功能说明](docs/管理端-功能说明.md) |
| 把用户端跑起来 | [用户端运行教程](docs/用户端-运行教程.md) |
| 看用户端能做什么 | [用户端功能说明](docs/用户端-功能说明.md) |

## **6. 目录结构**

仓库根目录只放三端代码、说明和协议。编译出来的 `target`、`dist`、`unpackage`、`node_modules` 是运行时生成的，不需要改。

### **6.1 仓库根目录**

| 路径 | 是什么 |
|---|---|
| `zulinshangcheng-server` | **服务端**。接口、权限、支付、消息、租赁商城业务模块 |
| `zulinshangcheng-admin` | **管理端**。平台运营在电脑浏览器里打开的后台 |
| `zulinshangcheng-uniapp` | **用户端（C 端）**。浏览商品、下单租赁、订单与钱包 |
| `docs` | 功能说明、运行教程与 README 预览截图 |
| `LICENSE` | Apache License 2.0 全文 |

### **6.2 服务端 `zulinshangcheng-server`**

| 路径 | 是什么 |
|---|---|
| `sqx-admin/src/main/java/com/sqx/SqxAdminApplication.java` | 启动入口。启动成功后控制台会打印「租赁商城启动成功」 |
| `sqx-modules/sqx-lease-shop` | 租赁商城核心业务：商品、订单、支付、文件上传等 |
| `sqx-modules/sqx-system` | 系统管理、菜单与配置 |
| `sqx-auth` | 认证、鉴权与 Shiro 配置 |
| `sqx-common` | 公共组件、Redis 等 |
| `sqx-domain/sqx-domain-repository` | 实体、Mapper、领域服务 |
| `sqx-admin/src/main/resources` | `application.yml`、`application-dev.yml` 等 |
| `sql/lease-shop.sql` | 数据库脚本，导入前请先建库（建议库名 `lease-shop`） |

### **6.3 管理端 `zulinshangcheng-admin`**

| 路径 | 是什么 |
|---|---|
| `src/views/common/home.vue` | 数据中心 / 运营看板 |
| `src/views/selfShop` | 自营商品发布、规格、品牌、分类 |
| `src/views/order` | 订单中心（含租赁状态流转） |
| `src/views/lease` | 租期规则、配件、租赁须知等 |
| `src/views/user` | C 端用户 |
| `src/views/coupon` / `members` / `marketing` | 优惠券、会员、营销活动 |
| `src/views/finance` | 财务中心 |
| `src/views/allocation` | 系统与支付等配置 |
| `src/views/message` / `vueMchat` | 消息、在线客服 |
| `src/views/modules/sys` | 管理员、角色、菜单 |
| `src/utils/httpRequest.js` | 请求封装与接口前缀 |

### **6.4 用户端 `zulinshangcheng-uniapp`**

| 路径 | 是什么 |
|---|---|
| `pages/index` | 首页：城市、搜索、分类入口、热租爆品 |
| `pages/category` | 分类浏览与二级类目 |
| `pages/order` | 订单列表（快递 / 自提、租赁状态） |
| `pages/my` | 我的：会员、订单入口、钱包、工具 |
| `pages/public` | 登录、注册、找回密码 |
| `subpackage/goods` | 商品详情、下单确认 |
| `subpackage/order` | 订单详情、归还、评价 |
| `my` | 地址、实名、钱包、优惠券、VIP、设置等子包 |
| `common/config.js` | 接口与 WebSocket 地址 |
| `pages.json` | 页面路由与底部导航（首页 / 分类 / 订单 / 我的） |
| `manifest.json` | 应用名称「租赁商城」、小程序与 H5 配置 |

界面组件使用 **uView UI**。选品、租期、支付、归还、评价与 IM 均在移动端完成。

## **7. 核心特性与技术栈**

### **7.1 核心特性**

· **首页分类导航、搜索、活动 Banner 与热租推荐**  
· **多级商品分类，支持手机、电脑、家电等多品类租赁**  
· **按租期计费，保证金可退；支持继续租赁与买断**  
· **订单全流程：待付款、待发货、待收货、待归还、归还中、已完成等**  
· **快递订单与自提订单；物流跟踪与确认收货**  
· **VIP 尊享卡、优惠券、积分与秒杀 / 拼团等营销能力**  
· **钱包余额、提现、邀请好友与团队收益**  
· **实名认证、收货地址、开具发票、在线客服与聊天**  
· **管理端商品 / 订单 / 用户 / 营销 / 财务 / 系统配置**  
· **短信登录、微信登录（需配置）、图片上传至 OSS 或本机存储**

业务细节见 [服务端功能说明](docs/服务端-功能说明.md) 与 Swagger `swagger-ui.html`。  
图片上传接口：`POST /sqx_fast/alioss/upload`，表单字段 `file`。

版本以仓库里的配置文件为准。没写死版本号的，按上文快速开始安装对应大版本即可。

### **7.2 服务端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 语言 | **Java** | **1.8** | 整个服务端 |
| 框架 | **Spring Boot** | **2.6.11** | 提供网页接口，默认端口 `9101`，路径 `/sqx_fast` |
| 持久化 | **MyBatis-Plus** | 随工程 | 读写 MySQL |
| 构建 | **Maven** | 3.8 及以上 | 下载依赖、编译、启动 |
| 数据库 | **MySQL** | 8.0 及以上 | 业务数据，脚本 `sql/lease-shop.sql`，建议库名 `lease-shop` |
| 连接池 | **Druid** | 随工程 | 管理数据库连接 |
| 缓存 | **Redis** | 可选 | 登录态、缓存等 |
| 接口文档 | **Springfox Swagger** | 随工程 | 浏览器打开 `swagger-ui.html` |
| 安全 | **Shiro + JWT** | 随工程 | 管理端与 App 认证 |
| 对象存储 | 本地 / **阿里云 OSS** 等 | 随工程 | 商品图、聊天图等 |
| 支付 | 微信、支付宝等 | 随工程 | 订单、会员。密钥需自行申请 |
| 实时 | **WebSocket** | 随工程 | 客服与聊天 |

### **7.3 管理端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 运行环境 | **Node.js** | 14+ 推荐 | 安装依赖、启动开发网站 |
| 页面框架 | **Vue** | **2.5** | 后台每一个页面 |
| 构建 | **Webpack** | 3.x | 开发时热更新，默认端口 `8009` |
| 界面组件 | **Element UI** | **2.8** | 表格、表单、弹窗、菜单 |
| 页面跳转 | **Vue Router** | **3.x** | 登录页、看板和各业务页的地址 |
| 状态 | **Vuex** | **3.x** | 记住登录等信息 |
| 请求 | **Axios** | **0.17** | 把操作发给服务端 |

### **7.4 用户端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 框架 | **uni-app** | Vue 语法 | 同一套页面可运行到浏览器、微信小程序、App |
| 开发工具 | **HBuilderX** 或 CLI | 正式版 | 编译并运行 |
| 界面组件 | **uView UI** | 2.x | 按钮、表单、轮播、弹窗等 |
| 浏览器预览 | H5 | 以控制台为准 | 电脑上看用户端 |
| 微信小程序 | 微信开发者工具 | 可选 | 需自己的小程序 AppId |

## **8. 开源协议**

<div align="center">

本仓库使用 **Apache License 2.0**

全文见 [LICENSE](LICENSE)

</div>

## **9. 社区与支持**

运行、二次开发或商务合作，可以联系 **省钱兄科技**。

使用过程中如果接口起不来、页面打不开，先按本文 **4. 快速开始** 和 `docs` 里的运行教程排查。短信、微信、支付、对象存储等密钥需要自行申请，并在管理端系统配置中填写。仍解决不了时，可通过官方渠道说明你的环境和报错。

当前项目正在测试中，如遇到 BUG，可自行修复或者留言反馈，我们将第一时间进行版本迭代。  
欢迎一起交流学习。开源是把源码开放出来，具体落地、定制和长期维护仍由服务来承接。

<div align="center">

<img src="https://leaseshopyh.xianmxkj.com/file/uploadPath/2026/11/11/71d6280bd7bb899f0b922818262e1fb9.jpg" alt="省钱兄科技二维码" width="220" />

**省钱兄科技**

</div>
