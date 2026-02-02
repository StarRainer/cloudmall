package com.rainer.cloudmall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rainer.cloudmall.member.vo.MemberRegisterVo;
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
import org.springframework.transaction.annotation.Transactional;


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

}