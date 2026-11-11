package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import java.util.List;


/**
 * 自营商城商品分类数据封装
 */
@Data
public class GoodsTypeResult {
    private Long value;
    private String label;
    private List<GoodsType> children; //下级分类
}
