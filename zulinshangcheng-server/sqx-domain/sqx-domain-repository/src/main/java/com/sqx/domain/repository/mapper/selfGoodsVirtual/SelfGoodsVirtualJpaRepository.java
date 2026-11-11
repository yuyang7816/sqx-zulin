package com.sqx.domain.repository.mapper.selfGoodsVirtual;

import com.sqx.domain.repository.entity.sys.SelfGoodsVirtual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface SelfGoodsVirtualJpaRepository extends JpaRepository<SelfGoodsVirtual, Long> {

    //分页查询
    Page<SelfGoodsVirtual> findAll(Pageable pageable);

    //条件查询
    Page<SelfGoodsVirtual> findAll(Specification<SelfGoodsVirtual> specification, Pageable pageable);

    List<SelfGoodsVirtual> findAll(Specification<SelfGoodsVirtual> specification);

    //查询可用的虚拟商品
    @Query(value = "select v.id,v.content,v.cover_img as coverImg,v.create_time as createTime,v.goods_id as goodsId,v.link_url as linkUrl,v.status,v.title,o.id as orderId,o.order_num as orderNum,o.pay_money as payMoney,o.pay_way as payWay,o.express_time as expressTime,u.nick_name as nickName,u.phone from self_goods_virtual v left join orders o on o.virtual_id = v.id left join user_info u on u.id = o.user_id where v.goods_id=:goodsId and v.content like concat('%',:content,'%') order by o.express_time desc",
            countQuery = "select count(*) from self_goods_virtual v left join orders o on o.virtual_id = v.id left join user_info u on u.id = o.user_id where v.goods_id=:goodsId and v.content like concat('%',:content,'%') order by o.express_time desc",
            nativeQuery=true)
    Page<Map<String,Object>>  findAllContent(Pageable pageable, @Param("goodsId") Long goodsId, @Param("content") String content);

    //查询可用的虚拟商品
    @Query(value = "select v.id,v.content,v.cover_img as coverImg,v.create_time as createTime,v.goods_id as goodsId,v.link_url as linkUrl,v.status,v.title,o.id as orderId,o.order_num as orderNum,o.pay_money as payMoney,o.pay_way as payWay,o.express_time as expressTime,u.user_name as nickName,u.phone from self_goods_virtual v left join orders o on o.virtual_id = v.id left join tb_user u on u.user_id = o.user_id where v.goods_id=:goodsId order by o.express_time desc",
            countQuery = "select count(*) from self_goods_virtual v left join orders o on o.virtual_id = v.id left join tb_user u on u.user_id = o.user_id where v.goods_id=:goodsId order by o.express_time desc",
            nativeQuery=true)
    Page<Map<String,Object>>  findAll(Pageable pageable, @Param("goodsId") Long goodsId);



    //查询可用的虚拟商品
    @Query(value = "from SelfGoodsVirtual s where  s.status=1 and s.goodsId=:goodsId")
    List<SelfGoodsVirtual>  findByGoodsId(@Param("goodsId") Long goodsId);

    //查询重复的卡密
    @Query(value = "from SelfGoodsVirtual s where  s.content=:content and s.goodsId=:goodsId")
    List<SelfGoodsVirtual>  checkContent(@Param("content") String content, @Param("goodsId") Long goodsId);

    //被使用
    @Modifying
    @Transactional
    @Query(value = "update SelfGoodsVirtual s set s.status=3 where s.id=:id")
    Integer used(@Param("id") Long id);

}
