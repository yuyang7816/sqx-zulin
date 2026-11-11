package com.sqx.domain.repository.mapper.member;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.domain.repository.entity.member.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberDao extends BaseMapper<Member> {


    List<Member> selectListByMember();

    IPage<?> selectPageByMember(@Param("pages")IPage<Member> pages);
}
