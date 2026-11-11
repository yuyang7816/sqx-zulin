package com.sqx.domain.repository.service.banner;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.Banner;

public interface IBannerService {
    /**
     * 获取全部的轮播图信息
     *
     * @return
     */
    Result findAll();

    /**
     * 保存banner对象
     *
     * @param
     */
    Result saveBody(String id, String url, String image);

    /**
     * 获取一个banner对象
     */
    Result findOne(long id);

    /**
     * 删除一个banner
     */
    Result delete(long id);

    /**
     * 修改
     */
    Result updateBody(Banner banner);


    Result updateState(String state, String id);


}
