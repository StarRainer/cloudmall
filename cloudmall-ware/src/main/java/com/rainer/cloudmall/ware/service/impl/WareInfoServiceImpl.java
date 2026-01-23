package com.rainer.cloudmall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.ware.dao.WareInfoDao;
import com.rainer.cloudmall.ware.entity.WareInfoEntity;
import com.rainer.cloudmall.ware.service.WareInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("wareInfoService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        LambdaQueryWrapper<WareInfoEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(key)) {
            if (key.matches("^\\d+$")) {
                wrapper.eq(WareInfoEntity::getId, Long.parseLong(key));
            }
            wrapper.or().like(WareInfoEntity::getName, key)
                    .or().like(WareInfoEntity::getAddress, key)
                    .or().like(WareInfoEntity::getAreacode, key);
        }
        return new PageUtils(page(new Query<WareInfoEntity>().getPage(params), wrapper));
    }

}