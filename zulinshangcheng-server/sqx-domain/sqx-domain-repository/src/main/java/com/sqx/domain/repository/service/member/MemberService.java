package com.sqx.domain.repository.service.member;


import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.member.Member;

public interface MemberService {

    Result insertMember(Member member);

    Result updateMember(Member member);

    Result deleteMemberById(Long memberId);

    Result selectMemberList();

    Result selectMemberPage(Integer page,Integer limit);

}
