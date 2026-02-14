package com.rainer.cloudmall.member.service;

import com.rainer.cloudmall.member.entity.MemberEntity;
import com.rainer.cloudmall.member.to.GitHubUserTo;
import com.rainer.cloudmall.member.vo.MemberRegisterVo;

public interface MemberManager {
    void register(MemberRegisterVo memberRegisterVo);

    MemberEntity loginOrRegister(GitHubUserTo gitHubUserTo);
}
