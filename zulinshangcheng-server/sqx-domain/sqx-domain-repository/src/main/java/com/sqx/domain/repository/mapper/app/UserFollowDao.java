package com.sqx.domain.repository.mapper.app;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.sqx.domain.repository.domain.app.UserFollowResponse;
import com.sqx.domain.repository.entity.app.UserFollow;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserFollowDao extends BaseMapper<UserFollow> {

    IPage<Map<String, Object>> selectMyFollow(IPage iPage, Long userId);

    IPage<Map<String, Object>> selectFans(IPage iPage, Long userId);

    List<UserFollowResponse> selectMyFollow1(Long userId);

    List<UserFollowResponse> selectFans1(Long userId);


}
