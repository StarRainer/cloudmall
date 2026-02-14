package com.rainer.cloudmall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.member.dao.MemberLevelDao;
import com.rainer.cloudmall.member.entity.MemberLevelEntity;
import com.rainer.cloudmall.member.service.MemberLevelService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("memberLevelService")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        boolean isNumber = key != null && key.matches("^\\d+$");
        IPage<MemberLevelEntity> page = this.page(
                new Query<MemberLevelEntity>().getPage(params),
                new LambdaQueryWrapper<MemberLevelEntity>()
                        .eq(isNumber, MemberLevelEntity::getId, isNumber ? Long.parseLong(key) : null)
                        .or(isNumber).like(key != null, MemberLevelEntity::getName, key)
        );

        return new PageUtils(page);
    }

    public Long getDefaultLevelId() {
        MemberLevelEntity defaultMemberLevelEntity = getOne(new LambdaQueryWrapper<MemberLevelEntity>()
                .select(MemberLevelEntity::getId)
                .eq(MemberLevelEntity::getDefaultStatus, 1)
                .last("limit 1")
        );
        if (defaultMemberLevelEntity != null) {
            return defaultMemberLevelEntity.getId();
        }

        MemberLevelEntity memberLevelEntity = getOne(new LambdaQueryWrapper<MemberLevelEntity>()
                .select(MemberLevelEntity::getId)
                .orderByAsc(MemberLevelEntity::getId)
                .last("limit 1")
        );
        if (memberLevelEntity != null) {
            return memberLevelEntity.getId();
        }
        return null;
    }
}