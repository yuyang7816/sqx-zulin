package com.sqx.domain.repository.mapper.selfGoodsComment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.domain.repository.entity.sys.SelfGoodsComment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/3/3 17:20
 */
@Mapper
@Repository
public interface SelfGoodsCommentDao extends BaseMapper<SelfGoodsComment> {
}
