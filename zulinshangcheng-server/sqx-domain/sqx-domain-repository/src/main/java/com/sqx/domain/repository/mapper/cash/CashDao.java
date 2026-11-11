package com.sqx.domain.repository.mapper.cash;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.sys.CashOut;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/24 11:31
 */
@Mapper
public interface CashDao extends BaseMapper<CashOut> {
    Double sumMoney(String time, Integer flag);

    Integer countMoney(@Param("time") String time, @Param("flag") Integer flag);

    Integer stayMoney(@Param("time") String time, @Param("flag") Integer flag);
    Integer weiXinXCXMoney(@Param("time") String time, @Param("flag") Integer flag);

    BigDecimal zfbMoney(@Param("time") String time, @Param("flag") Integer flag);

    BigDecimal daiTXMoney(@Param("time") String time, @Param("flag") Integer flag);

    BigDecimal yiTXMoney(@Param("time") String time, @Param("flag") Integer flag);

    BigDecimal weiXinMoney(@Param("time") String time, @Param("flag") Integer flag);

    BigDecimal refuseMoney(@Param("time") String time, @Param("flag") Integer flag);

    Integer yiTXCount(String time, Integer flag);

    Integer zfbMoneyCount(String time, Integer flag);

    Integer refuseMoneyCount(String time, Integer flag);
}
