package com.sqx.domain.repository.mapper.app;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.app.UserMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMoneyDao extends BaseMapper<UserMoney> {

    void updateMayMoney(@Param("type") Integer type, @Param("userId") Long userId, @Param("money") Double money);

}
