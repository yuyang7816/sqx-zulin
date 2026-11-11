package com.sqx.domain.repository.mapper.selfGoodsType;

import com.sqx.domain.repository.entity.sys.GoodsType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface GoodsTypeRepository extends Repository<GoodsType, Long> {

    /**
     * 查询所有一级分类
     */
    @Query(value = "from GoodsType s where s.parentId=0 order by s.sort asc")
    List<GoodsType> findAllStatus0();

    /**
     * 查询所有一级分类
     */
    @Query(value = "from GoodsType s where s.parentId = 0 and (:name is null or trim(:name) = '' or lower(s.name) like lower(concat('%', :name, '%'))) order by s.sort asc")
    List<GoodsType> findAllLevel1ByName(@Param("name") String name);

    /**
     * 查询所有非一级分类
     */
    @Query(value = "from GoodsType s where s.parentId <> 0 order by s.sort asc")
    List<GoodsType> findAllStatus();

    /**
     * 查询所有分类（包括一级、二级、三级）
     */
    @Query(value = "from GoodsType s order by s.sort asc")
    List<GoodsType> findAllTypes();

    /**
     * 查询所有分类（包括一级、二级、三级）
     */
    @Query(value = "from GoodsType s where :name is null or trim(:name) = '' or lower(s.name) like lower(concat('%', :name, '%')) order by s.sort asc")
    List<GoodsType> findAllTypesByName(@Param("name") String name);


    /**
     * 查询所有二级分类
     * @param typeId
     * @return
     */
    @Query(value = "from GoodsType s where s.parentId =: typeId order by s.sort asc")
    List<GoodsType> findAllTypesByParentId(@Param("typeId") Long typeId);
}