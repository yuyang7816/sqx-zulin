package com.sqx.domain.repository.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品表
 * @TableName goods
 */
@Data
@Entity
public class Goods implements Serializable {
//    /**
//     *
//     */
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 或者使用其他适合的策略
//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    /**
//     * 商品名称
//     */
//    @ApiModelProperty("商品名称")
//    private String goodName;
//
//    /**
//     * 品牌id
//     */
//    @ApiModelProperty("品牌id")
//    private Long brandId;
//
//    /**
//     * 品牌实体
//     */
//    @ApiModelProperty("品牌实体")
//    @Transient
//    private SelfGoodsBrand brand; //品牌实体
//
//    /**
//     * 商家用户id
//     */
//    @ApiModelProperty("商家用户id")
//    private Long userId;
//
//    /**
//     * 企业id
//     */
//    @ApiModelProperty("企业id")
//    private Long businessId;
//
//    /**
//     * 专区id
//     */
//    @ApiModelProperty("专区id")
//    private Long sectionId;
//
//    /**
//     * 标签id
//     */
//    @ApiModelProperty("标签id")
//    private Long labelId;
//
//    /**
//     * 商品租赁价格
//     */
//    @ApiModelProperty("商品租赁价格")
//    private BigDecimal price;
//
//    /**
//     * 商品买断价格
//     */
//    @ApiModelProperty("商品买断价格")
//    private BigDecimal purchaseMoney;
//
//    /**
//     * 商品押金
//     */
//    @ApiModelProperty("商品押金")
//    private BigDecimal antecedentMoney;
//
//    /**
//     * 商品主图
//     */
//    @ApiModelProperty("商品主图")
//    private String titleUrl;
//
//    /**
//     * 商品详情图
//     */
//    @ApiModelProperty("商品详情图")
//    private String detailsUrl;
//
//    /**
//     * 商品描述
//     */
//    @ApiModelProperty("商品描述")
//    private String description;
//
//    /**
//     * 商品状态类型：1热爆租品  2精选推荐 3..
//     */
//    @ApiModelProperty("商品状态类型：1热爆租品  2精选推荐 3..")
//    private Integer typeState;
//
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

//    /**
//     * 修改时间
//     */
//    @ApiModelProperty("修改时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    private Date updateTime;
//    /**
//     * 商品标题
//     */
//    @ApiModelProperty("商品标题")
//    private String remark;
//
//    /**
//     * 商品状态：1上架 2下架
//     */
//    @ApiModelProperty("商品状态：1上架 2下架")
//    private Integer status;
//
//    /**
//     * 品牌名称
//     */
//    @ApiModelProperty("品牌名称")
//    @TableField(exist = false)
//    private String brandTypeName;
//    /**
//     * 企业名称
//     */
//    @ApiModelProperty("企业名称")
//    @TableField(exist = false)
//    private String businessName;
//    /**
//     * 专区名称
//     */
//    @ApiModelProperty("专区名称")
//    @TableField(exist = false)
//    private String sectionName;
//    /**
//     * 标签名称
//     */
//    @ApiModelProperty("标签名称")
//    @TableField(exist = false)
//    private String label;
//    /**
//     * 商品规格
//     */
//    @ApiModelProperty("商品规格")
//    @TableField(exist = false)
//    private GoodsSpecifications goodsSpecifications;
//
//    /**
//     * 价格排序：1由低到高 2由高到低
//     */
//    @ApiModelProperty("价格排序：1由低到高 2由高到低")
//    @TableField(exist = false)
//    private Integer priceSort;
//
//    /**
//     * 统计有多少人想要这件商品
//     */
//    @TableField(exist = false)
//    @ApiModelProperty("统计有多少人想要这件商品")
//    private int needCount;
//
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 商品类型id
//     */
//    @Column
//    @ApiModelProperty("商品类型id")
//    private String typeId; //商品类型id
//
//    /**
//     * 商品类型实体
//     */
//    @Transient
//    @ApiModelProperty("商品类型实体")
//    private GoodsType type; //商品类型id
//
//    /**
//     * 规格
//     */
//    @ApiModelProperty("规格")
//    @Transient
//    private List<SelfGoodsAttr> attr; //规格
//
//    /**
//     * sku
//     */
//    @ApiModelProperty("sku")
//    @Transient
//    private List<SelfGoodsSku> sku; //sku
//
//    /**
//     * 活动id
//     */
//    @ApiModelProperty("活动id")
//    @Column
//    private String activityIds;
//
//    /**
//     * 复购率
//     */
//    @ApiModelProperty("复购率")
//    @Transient
//    private String repeat; //复购率
//
//    /**
//     * 销量
//     */
//    @ApiModelProperty("销量")
//    @Column
//    private Integer sales; //商品销量
//
//    /**
//     * 是否精选好物(0默认 1精选好物)
//     */
//    @ApiModelProperty("是否精选好物(0默认 1精选好物)")
//    @Column
//    private Integer isSelect; //精选好物(0默认 1精选好物)
//
//    /**
//     * 首页商品(0默认 1是首页商品)
//     */
//    @ApiModelProperty("首页商品(0默认 1是首页商品)")
//    @Column
//    private Integer homeGoods; //首页商品(0默认 1是首页商品)
//
//    /**
//     * 每日推荐(0默认 1推荐商品)
//     */
//    @ApiModelProperty("每日推荐(0默认 1推荐商品)")
//    @Column
//    private Integer isRecommend; //每日推荐(0默认 1推荐商品)
//
//    /**
//     * 是否积分商品（1积分商城商品）
//     */
//    @ApiModelProperty("是否积分商品（1积分商城商品）")
//    @Column
//    private Integer isJiFenGoods; //是否积分商品（1积分商城商品）
    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //id

    /**
     * 商户号
     */
    @ApiModelProperty("商户号")
    @Column
    private String merchants; //商户号

    /**
     * 一级商品类型id
     */
    @ApiModelProperty("一级商品类型id")
    @Column
    private Long typeId; //商品类型id

    /**
     * 二级商品类型id
     */
    @ApiModelProperty("二级商品类型id")
    @Column
    private Long twoTypeId;

    /**
     * 二级商品类型名称
     */
    @ApiModelProperty("二级商品类型名称")
    @Transient
    @TableField(exist = false)
    private String twoTypeName;

    /**
     * 三级商品类型id
     */
    @ApiModelProperty("三级商品类型id")
    @Column
    private Long threeTypeId;

    /**
     * 三级商品类型名称
     */
    @ApiModelProperty("三级商品类型名称")
    @Transient
    @TableField(exist = false)
    private String threeTypeName;

    /**
     * 商品类型实体
     */
    @ApiModelProperty("商品类型实体")
    @Transient
    @TableField(exist = false)
    private GoodsType type; //商品类型id

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column
    private String createAt; //创建时间

    /**
     * 商品标题
     */
    @ApiModelProperty("商品标题")
    @Column
    private String title; //商品标题

    /**
     * 必买理由
     */
    @ApiModelProperty("必买理由")
    @Column(columnDefinition="text")
    private String buyReason; //必买理由

    /**
     * 商品封面图片
     */
    @ApiModelProperty("商品封面图片")
    @Column
    private String coverImg; //商品封面图片

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    @Column(columnDefinition="text")
    private String img; //商品图片

    /**
     * 商品描述
     */
    @ApiModelProperty("商品描述")
    @Column(columnDefinition="text")
    private String descrition; //商品描述

    /**
     * 商品原价
     */
    @ApiModelProperty("商品原价")
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal originalPrice; //商品原价

    /**
     * 商品价格
     */
    @ApiModelProperty("商品价格")
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal price; //商品价格

    /**
     * 会员价格
     */
    @ApiModelProperty("会员价格")
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal memberPrice; //会员价格

    /**
     * 商品佣金
     */
    @ApiModelProperty("商品佣金")
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal commissionPrice; //商品佣金

    /**
     * 商品销量
     */
    @ApiModelProperty("商品销量")
    @Column
    private Integer sales; //商品销量

    /**
     * 商品状态(默认1正常 2下架)
     */
    @ApiModelProperty("商品状态(默认1正常 2下架)")
    @Column
    private Integer status; //商品状态(默认1正常 2下架)

    /**
     * 精选推荐（0默认 1精选推荐）
     */
    @ApiModelProperty("精选推荐（0默认 1精选推荐）")
    @Column
    private Integer isSelect; //精选好物(0默认 1精选好物)

    /**
     * 首页商品(0默认 1是首页商品)
     */
    @ApiModelProperty("首页商品(0默认 1是首页商品)")
    @Column
    private Integer homeGoods; //首页商品(0默认 1是首页商品)

    /**
     * 热爆租品（0默认 1热爆租品）
     */
    @ApiModelProperty("热爆租品（0默认 1热爆租品）")
    @Column
    private Integer isRecommend; //每日推荐(0默认 1推荐商品)

    /**
     * 是否需要发货(1包邮 2邮寄 3门店自取)
     */
    @ApiModelProperty("是否需要发货(1包邮 2邮寄 3门店自取)")
    @Column
    private String isExpress;

    /**
     * 是否需要发货(1包邮 2邮寄 3门店自取)的接收列表
     */
    @ApiModelProperty("是否需要发货(1包邮 2邮寄 3门店自取)的接收列表")
    @TableField(exist = false)
    @Transient
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private List<String> isExpressList;


    /**
     * 邮费
     */
    @ApiModelProperty("邮费")
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal postagePrice; //邮费

    /**
     * 规格
     */
    @ApiModelProperty("规格")
    @Transient
    @TableField(exist = false)
    private List<SelfGoodsAttr> attr; //规格

    /**
     * sku
     */
    @ApiModelProperty("sku")
    @Transient
    @TableField(exist = false)
    private List<SelfGoodsSku> sku; //sku
    /**
     * 品牌id
     */
    @ApiModelProperty("品牌id")
    @Column
    private Long brandId; //品牌id

    /**
     * 品牌实体
     */
    @ApiModelProperty("品牌实体")
    @Transient
    @TableField(exist = false)
    private SelfGoodsBrand brand; //品牌实体

    /**
     * 虚拟商品总数
     */
    @ApiModelProperty("虚拟商品总数")
    @Transient
    @TableField(exist = false)
    private Integer virtualSum; //虚拟商品总数

    /**
     * 虚拟商品库存
     */
    @ApiModelProperty("虚拟商品库存")
    @Transient
    @TableField(exist = false)
    private Integer virtualCount; //虚拟商品库存
    /**
     * 浏览次数
     */
    @ApiModelProperty("浏览次数")
    @Column(columnDefinition = "int default 0")
    private Integer browse; //浏览次数
    /**
     * 是否积分商品（1积分商城商品）
     */
    @ApiModelProperty("是否积分商品（1积分商城商品）")
    @Column
    private Integer isJiFenGoods; //是否积分商品（1积分商城商品）
    /**
     * 复购率
     */
    @ApiModelProperty("复购率")
    @Transient
    @TableField(exist = false)
    private String repeat; //复购率

    /**
     * 活动id
     */
    @ApiModelProperty("活动id")
    @Column
    private String activityIds;

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    @Column
    private Long labelId;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    @TableField(exist = false)
    @Transient
    private String label;
    /**
     * 经度
     */
    @Column
    @ApiModelProperty("经度")
    private Double lng;

    /**
     * 纬度
     */
    @Column
    @ApiModelProperty("纬度")
    private Double lat;

    /**
     * 省
     */
    @Column
    @ApiModelProperty("省")
    private String province;

    /**
     * 市
     */
    @Column
    @ApiModelProperty("市")
    private String city;

    /**
     * 区
     */
    @Column
    @ApiModelProperty("区")
    private String district;

    /**
     * 日租金
     */
    @ApiModelProperty("日租金")
    private Double dailyRent;

    /**
     * 会员日租金
     */
    @ApiModelProperty("会员日租金")
    private Double memberRent;

    /**
     * 新旧程度
     */
    @ApiModelProperty("新旧程度")
    private String oldDegree;

    /**
     * 是否支持买断：0不支持买断 1支持买断
     */
    @ApiModelProperty("是否支持买断：0不支持买断 1支持买断")
    private Integer supportBuyout;

    /**
     * 配件
     */
    @ApiModelProperty("配件")
    private String accessoryIds;

    /**
     * 配件列表
     */
    @ApiModelProperty("配件列表")
    @Transient
    @TableField(exist = false)
    private List<GoodsAccessory> accessoryList;

    /**
     * 赠品
     */
    @ApiModelProperty("赠品")
    private String giftIds;

    /**
     * 赠品列表
     */
    @ApiModelProperty("赠品列表")
    @Transient
    @TableField(exist = false)
    private List<GoodsGift> giftList;

    /**
     * 是否有配件
     */
    @ApiModelProperty("是否有配件")
    private Boolean accessoryStatus;

    /**
     * 是否包含赠品
     */
    @ApiModelProperty("是否包含赠品")
    private Boolean giftStatus;

    /**
     * 保障说明：
     */
    @ApiModelProperty("保障说明：")
    private String guarantee;

    /**
     * 起租日期
     */
    @ApiModelProperty("起租日期")
    private String leaseStart;

    /**
     * 商品押金
     */
    @ApiModelProperty("商品押金")
    @Column(columnDefinition="decimal(18,2)")
    private BigDecimal depositMoney;

    /**
     * 是否收藏 0否 1是
     */
    @ApiModelProperty("是否收藏 0否 1是")
    @Transient
    @TableField(exist = false)
    private Integer isCollect;

    /**
     * 逾期日租金
     */
    @ApiModelProperty("逾期日租金")
    private BigDecimal overdueRent;

    private static final long serialVersionUID = 1L;
}