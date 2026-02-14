package com.rainer.cloudmall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainer.cloudmall.common.exception.PasswordNotCorrectException;
import com.rainer.cloudmall.common.exception.UserNotFoundException;
import com.rainer.cloudmall.member.to.GitHubUserTo;
import com.rainer.cloudmall.member.vo.MemberLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;

import com.rainer.cloudmall.member.dao.MemberDao;
import com.rainer.cloudmall.member.entity.MemberEntity;
import com.rainer.cloudmall.member.service.MemberService;



@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public long countByMobile(String phone) {
        return count(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getMobile, phone));
    }

    @Override
    public long countByUsername(String userName) {
        return count(new LambdaQueryWrapper<MemberEntity>().eq(MemberEntity::getUsername, userName));
    }

    @Override
    public MemberEntity login(MemberLoginVo memberLoginVo) {
        LambdaQueryWrapper<MemberEntity> queryWrapper = new LambdaQueryWrapper<>();
        String account = memberLoginVo.getLoginacct();
        if (account.matches("^1[3-9]\\d{9}$")) {
            queryWrapper.eq(MemberEntity::getMobile, account);
        } else {
            queryWrapper.eq(MemberEntity::getUsername, account);
        }
        queryWrapper.last("limit 1");
        MemberEntity memberEntity = getOne(queryWrapper);
        if (memberEntity == null) {
            throw new UserNotFoundException();
        }
        if (!new BCryptPasswordEncoder().matches(memberLoginVo.getPassword(), memberEntity.getPassword())) {
            throw new PasswordNotCorrectException();
        }
        return memberEntity;
    }

    @Override
    public MemberEntity getMemberBySocialUid(Long socialUid) {
        return getOne(new LambdaQueryWrapper<MemberEntity>()
                .eq(MemberEntity::getSocialUid, socialUid)
                .last("limit 1")
        );
    }

}