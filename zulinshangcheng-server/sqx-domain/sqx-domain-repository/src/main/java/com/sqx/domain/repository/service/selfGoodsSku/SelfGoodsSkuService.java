package com.sqx.domain.repository.service.selfGoodsSku;

public interface SelfGoodsSkuService {
    
    //判断库存是否大于购买数量
    boolean checkStock(Long skuId, Integer paNumber);

    //sku库存减少
    void lessStock(Long skuId, Integer number);

    //sku库存+1
    void addStock(Long skuId, Integer number);

    //sku销量添加
    void addSales(Long skuId, Integer number);

}
