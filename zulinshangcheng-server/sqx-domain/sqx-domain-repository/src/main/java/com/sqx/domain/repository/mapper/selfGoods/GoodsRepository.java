package com.sqx.domain.repository.mapper.selfGoods;

import com.sqx.domain.repository.entity.sys.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface GoodsRepository extends Repository<Goods, Long> {

    @Query(value = "select * from goods " +
            "where if(:title!='',title like concat('%',:title,'%'),1=1) " +
            "and if(:type!='',type_id =:type,1=1) " +
            "and if(:twoTypeId!=0,two_type_id =:twoTypeId,1=1) " +
            "and if(:threeTypeId!=0,three_type_id =:threeTypeId,1=1) " +
            "and if(:status!=0,status=:status,1=1) " +
            "and if(:activityId!='',(activity_ids like concat('%,',:activityId,'%') or activity_ids like concat('%,',:activityId,',%') or activity_ids like concat('%',:activityId,',%') or activity_ids =:activityId),1=1) " +
            "and if(:notActivityId!='',((activity_ids not like concat('%,',:notActivityId,'%') and  activity_ids not like concat('%,',:notActivityId,',%') and  activity_ids not like concat('%',:notActivityId,',%') and activity_ids <>:notActivityId) or activity_ids is null),1=1) " +
            "and if(:isJiFenGoods!='',is_ji_fen_goods=:isJiFenGoods,1=1) " +
            "order by create_at desc ",
            countQuery = "select count(*) from goods " +
                    "where if(:title!='',title like concat('%',:title,'%'),1=1) " +
                    "and if(:type!='',type_id =:type,1=1) " +
                    "and if(:twoTypeId!=0,two_type_id =:twoTypeId,1=1) " +
                    "and if(:threeTypeId!=0,three_type_id =:threeTypeId,1=1) " +
                    "and if(:status!=0,status=:status,1=1) " +
                    "and if(:activityId!='',(activity_ids like concat('%,',:activityId,'%') or activity_ids like concat('%,',:activityId,',%') or activity_ids like concat('%',:activityId,',%') or activity_ids =:activityId),1=1) " +
                    "and if(:notActivityId!='',((activity_ids not like concat('%,',:notActivityId,'%') and  activity_ids not like concat('%,',:notActivityId,',%') and  activity_ids not like concat('%',:notActivityId,',%') and activity_ids <>:notActivityId) or activity_ids is null),1=1) " +
                    "and if(:isJiFenGoods!='',is_ji_fen_goods=:isJiFenGoods,1=1) ",nativeQuery = true)
    Page<Goods> findAll(@Param("pageable") Pageable pageable,
                        @Param("title")String title,
                        @Param("type")String type,
                        @Param("twoTypeId")Long twoTypeId,
                        @Param("threeTypeId")Long threeTypeId,
                        @Param("status")String status,
                        @Param("isJiFenGoods")String isJiFenGoods,
                        @Param("activityId")String activityId,
                        @Param("notActivityId")String notActivityId);

}