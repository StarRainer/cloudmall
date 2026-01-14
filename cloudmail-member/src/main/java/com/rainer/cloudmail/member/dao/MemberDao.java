package com.rainer.cloudmail.member.dao;

import com.rainer.cloudmail.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author StarRainer
 * @email estarrainer@gmail.com
 * @date 2026-01-14 14:40:48
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
