package com.rainer.cloudmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.member.entity.MemberEntity;
import com.rainer.cloudmall.member.to.GitHubUserTo;
import com.rainer.cloudmall.member.vo.MemberLoginVo;
import com.rainer.cloudmall.member.vo.MemberRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:40:48
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    long countByMobile(String phone);

    long countByUsername(String userName);

    MemberEntity login(MemberLoginVo memberLoginVo);

    MemberEntity getMemberBySocialUid(Long socialUid);
}

