package com.sqx.domain.repository.service.sys;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.GoodsRecovery;

/**
* @author Admin
* @description 针对表【goods_recovery(二手物品回收估价表)】的数据库操作Service
* @createDate 2024-06-28 10:50:20
*/
public interface GoodsRecoveryService extends IService<GoodsRecovery> {

    /**
     * 添加二手物品回收估价
     * @param goodsRecovery
     * @return
     */
    Result saveGoodsRecovery(GoodsRecovery goodsRecovery);

    /**
     * 修改二手物品回收估价
     * @param goodsRecovery
     * @return
     */
    Result updateGoodsRecovery(GoodsRecovery goodsRecovery);

    /**
     * 删除二手物品回收估价
     * @param id
     * @return
     */
    Result deleteGoodsRecovery(Long id);

    /**
     * 获取二手物品回收估价列表
     * @param pageNum
     * @param pageSize
     * @param name
     * @param brandId
     * @return
     */
    Result getGoodsRecoveryList(Integer pageNum, Integer pageSize, String name,Long brandId);

    /**
     * 获取二手物品回收估价详情
     * @param id
     * @return
     */
    Result getGoodsRecoveryById(Long id);

    /**
     * 根据回收id获取回收估价
     * @param recoveryId
     * @param userId
     * @return
     */
    Result estimatedPriceByRecoveryId(Long recoveryId,Long userId);

    /**
     * 获取回收规格列表
     * @param userId
     * @param id  二手物品回收估价表id
     * @return
     */
    Result selectListBySpecifications(Long userId,Long id);

    /**
     * 获取最近成交历史
     */
    Result selectListByHistory();
}
