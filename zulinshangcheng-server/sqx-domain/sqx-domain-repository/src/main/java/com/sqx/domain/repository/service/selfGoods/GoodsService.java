package com.sqx.domain.repository.service.selfGoods;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Goods;
import com.sqx.domain.repository.entity.sys.SelfGoodsAttr;

public interface GoodsService {
    //后台管理商品列表
    Result findAll(Integer page, Integer size, String title, String type,Long twoTypeId,Long threeTypeId, String status, String isJiFenGoods, String activityId, String notActivityId);

    //虚拟商品列表
    Result goodsVirtualList(Integer page, Integer size, String title, String type, Integer status, Integer isExpress, Integer isJiFenGoods);

    //用户端商品列表
    Result findAllByUser(Integer page, Integer size, String type,String brandId ,String title, String sort, Integer isJiFenGoods);

    Result updateGoodsActivity(Integer type, Long goodsId, String activityId);

    //品牌列表
    Result searchBrandList(String title);

    //查询
    Result findOne(Long id);

    Result findById(Long id, Long userId);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(Goods entity);

    //修改
    Result updateBody(Goods entity);

    //精选商品
    Result selectGoods(Integer page, Integer size, String sort);

    //热卖榜单
    Result selling(Integer page, Integer size, String sort);

    //每日上新
    Result news(Integer page, Integer size, String sort);

    //每日推荐
    Result recommend(Integer page, Integer size, String sort);

    //首页商品
    Result homeGoods(Integer page, Integer size);

    //添加精选好物
    Result addSelectGoods(Long id);

    //删除精选好物
    Result deleteSelectGoods(String ids);

    //后台管理添加每日推荐
    Result addRecommend(Long id);

    //后台管理删除每日推荐
    Result deleteRecommend(String ids);

    //商品销量增加购买个数
    void salesAdd(Long id, Integer number);

    //生成属性
    Result isFormatAttr(SelfGoodsAttr attr, String coverImg, String originalPrice, String price, String memberPrice,String dailyRent,String memberRent,Integer stock,String oldDegree);

    //单规格生成属性
    Result onlyFormatAttr(String coverImg, String originalPrice, String price, String memberPrice,
                          String dailyRent,String memberRent,Integer stock,String oldDegree);

    //回显属性
    Result formatAttr(Long goodsId);

    //回显属性值
    Result findAttrValue(Long goodsId);

    //商品上下架
    Result updateStatus(Long goodsId);

    /**
     * 查询分类专区的品牌下的商品列表
     * @param typeId
     * @param brandId
     * @param page
     * @param size
     * @param type 1:价格正序 2:价格倒序
     * @param title 搜索商品
     * @return
     */
    Result goodsInfoByBrand(Integer page,Integer size,Long typeId, Long brandId,Integer type,String title);

    /**
     * 查询标签分类
     * @return
     */
    Result selectByLabelType(Integer page, Integer size);

    /**
     * 根据标签id查询商品列表
     * @param labelId
     * @param page
     * @param size
     * @return
     */
    Result goodListByLabelId(Integer page, Integer size,Long labelId);


    /**
     * 首页热爆租品
     * @param page
     * @param size
     * @param labelId
     * @return
     */
    Result selectedGood(Integer page, Integer size,Long labelId);

    /**
     * 首页精选推荐
     * @param page
     * @param size
     * @param typeId 一级分类id
     * @param type 1:价格正序 2:价格倒序
     * @param title 搜索商品
     * @return
     */
     Result selectedRecommendations(Integer page, Integer size,Long typeId,Integer type,String title);

    /**
     * 根据品牌id查询商品列表
     * @param page
     * @param size
     * @param threeTypeId
     * @return
     */
    Result goodsListByBrandId(Integer page, Integer size,String title,Long brandId,Integer type,Long threeTypeId);

}
