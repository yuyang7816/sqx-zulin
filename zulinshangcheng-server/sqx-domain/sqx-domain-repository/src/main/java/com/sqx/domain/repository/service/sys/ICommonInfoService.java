package com.sqx.domain.repository.service.sys;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.CommonInfo;

/**
 * @author fang
 * @date 2020/7/8
 */
public interface ICommonInfoService {

    /**
     * 保存对象
     *
     * @param
     */
    Result update(CommonInfo commonInfo);

    /**
     * 获取一个对象
     */
    CommonInfo findOne(int id);

    /**
     * 删除一个
     */
    Result delete(long id);

    /**
     * 修改
     */
    Result updateBody(CommonInfo commonInfo);
    /**
     * 通过类型查询
     */
    Result findByType(Integer type);

    /**
     * 通过类型查询
     */
    Result findByTypeAndCondition(String condition);


//    /**
//     * 系统配置放入缓存
//     */
//    void loadCommonInfo();
//
//    /**
//     * 获取commonInfo对象
//     * @param type
//     * @return
//     */
//    CommonInfo getCommonInfo(Integer type);

    /**
     * 获取配置值
     * @param type
     * @return
     */
//    String getCommonValue(Integer type);
//
//    /**
//     * 刷新系统缓存
//     * @return
//     */
//    Result<Void> refreshCache();
//
//    /**
//     * 刷新指定的系统缓存
//     * @param type
//     * @return
//     */
//    Result<Void> refreshCacheKey(Integer type);
}