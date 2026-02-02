package com.rainer.cloudmall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:40:48
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Long getDefaultLevelId();
}

