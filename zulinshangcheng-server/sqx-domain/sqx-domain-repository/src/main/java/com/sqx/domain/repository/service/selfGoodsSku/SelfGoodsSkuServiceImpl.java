package com.sqx.domain.repository.service.selfGoodsSku;

import com.sqx.domain.repository.entity.sys.SelfGoodsSku;
import com.sqx.domain.repository.mapper.selfGoodsSku.SelfGoodsSkuJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelfGoodsSkuServiceImpl implements SelfGoodsSkuService {
    @Autowired
    private SelfGoodsSkuJpaRepository jpaRepository;

    @Override
    public boolean checkStock(Long skuId, Integer paNumber) {
        boolean flag = true;
        SelfGoodsSku sku = jpaRepository.findById(skuId).orElse(null);
        if (sku.getStock().intValue() < paNumber){
            flag = false;
        }
        return flag;
    }

    @Override
    public void lessStock(Long skuId, Integer number) {
        jpaRepository.lessStock(skuId, number);
    }

    @Override
    public void addStock(Long skuId, Integer number) {
        jpaRepository.addStock(skuId, number);
    }

    @Override
    public void addSales(Long skuId, Integer number) {
        jpaRepository.addSales(skuId, number);
    }
}
