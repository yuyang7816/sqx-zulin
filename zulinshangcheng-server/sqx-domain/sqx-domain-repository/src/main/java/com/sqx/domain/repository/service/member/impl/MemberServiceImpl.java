package com.sqx.domain.repository.service.member.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.utils.PageUtils;
import com.sqx.domain.repository.entity.member.Member;
import com.sqx.domain.repository.mapper.member.MemberDao;
import com.sqx.domain.repository.service.member.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {


    @Override
    public Result insertMember(Member member){
        baseMapper.insert(member);
        return Result.success();
    }

    @Override
    public Result updateMember(Member member){
        baseMapper.updateById(member);
        return Result.success();
    }

    @Override
    public Result deleteMemberById(Long memberId){
        baseMapper.deleteById(memberId);
        return Result.success();
    }

    @Override
    public Result selectMemberList(){
        return Result.success(baseMapper.selectListByMember());
    }

    @Override
    public Result selectMemberPage(Integer page,Integer limit){
        IPage<Member> pages=new Page<>(page,limit);
        return Result.success(new PageUtils(baseMapper.selectPageByMember(pages)));
    }



}







