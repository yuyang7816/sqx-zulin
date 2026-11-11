package generator.mapper;

import generator.domain.Orders;

/**
* @author Admin
* @description 针对表【orders】的数据库操作Mapper
* @createDate 2025-03-04 16:13:50
* @Entity generator.domain.Orders
*/
public interface OrdersMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

}
