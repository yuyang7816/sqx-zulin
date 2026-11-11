package com.sqx.domain.repository.mapper.selfGoodsComment;

import com.sqx.domain.repository.entity.sys.SelfGoodsComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface SelfGoodsCommentRepository extends Repository<SelfGoodsComment, Long> {

    @Query(value = "select * from self_goods_comment  " +
            "where if(:goodsId!=0,goods_id=:goodsId,1=1) " +
            "and if(:scoreType!=0,score_type=:scoreType,1=1) " +
            "and if(:title!='',goods_id in (select id from goods where title like concat('%',:title,'%') ),1=1) " +
            "and if(:userName!='',user_name like concat('%',:userName,'%'),1=1) " +
            "and if(:phone!='',user_id in (select user_id from tb_user where phone=:phone),1=1) " +
            "and if(:content!='',content like concat('%',:content,'%'),1=1) order by create_time desc ",nativeQuery = true)
    Page<SelfGoodsComment> selectSelfGoodsCommentList(Pageable pageable,Long goodsId,Integer scoreType,String title,String userName,String phone,String content);


}