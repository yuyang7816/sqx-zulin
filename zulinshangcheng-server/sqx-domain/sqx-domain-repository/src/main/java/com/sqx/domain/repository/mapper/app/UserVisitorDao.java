package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.sqx.domain.repository.domain.app.UserFollowResponse;
import com.sqx.domain.repository.entity.app.UserVisitor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserVisitorDao extends BaseMapper<UserVisitor> {
    IPage<UserFollowResponse> selectMyVisitor(IPage iPage, Long userId);

    List<UserFollowResponse> selectMyVisitor1(Long userId);
}
