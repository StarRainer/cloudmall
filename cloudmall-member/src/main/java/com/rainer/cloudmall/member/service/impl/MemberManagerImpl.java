package com.rainer.cloudmall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainer.cloudmall.common.exception.CommonException;
import com.rainer.cloudmall.common.exception.PhoneExistException;
import com.rainer.cloudmall.common.exception.UsernameExistException;
import com.rainer.cloudmall.member.entity.MemberEntity;
import com.rainer.cloudmall.member.service.MemberLevelService;
import com.rainer.cloudmall.member.service.MemberManager;
import com.rainer.cloudmall.member.service.MemberService;
import com.rainer.cloudmall.member.vo.MemberRegisterVo;
import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberManagerImpl implements MemberManager {
    private final MemberLevelService memberLevelService;
    private final MemberService memberService;

    public MemberManagerImpl(MemberLevelService memberLevelService, MemberService memberService) {
        this.memberLevelService = memberLevelService;
        this.memberService = memberService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(MemberRegisterVo memberRegisterVo) {
        MemberEntity memberEntity = new MemberEntity();
        if (memberService.countByMobile(memberRegisterVo.getPhone()) > 0L) {
            throw new PhoneExistException();
        }
        if (memberService.countByUsername(memberRegisterVo.getUserName()) > 0L) {
            throw new UsernameExistException();
        }

        memberEntity.setMobile(memberRegisterVo.getPhone());
        memberEntity.setUsername(memberRegisterVo.getUserName());
        memberEntity.setLevelId(memberLevelService.getDefaultLevelId());
        memberEntity.setPassword(new BCryptPasswordEncoder().encode(memberRegisterVo.getPassword()));

        try {
            memberService.save(memberEntity);
        } catch (DuplicateKeyException e) {
            throw new CommonException("手机号已注册/用户名不可用");
        }
    }
}
