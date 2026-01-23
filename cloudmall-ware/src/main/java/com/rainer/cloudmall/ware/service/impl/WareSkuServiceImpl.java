package com.rainer.cloudmall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.ware.dao.WareSkuDao;
import com.rainer.cloudmall.ware.entity.WareSkuEntity;
import com.rainer.cloudmall.ware.service.WareSkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("wareSkuService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();

        String key = (String) params.get("key");
        String skuIdText = (String) params.get("skuId");
        String wareIdText = (String) params.get("wareId");

        if (StringUtils.hasLength(skuIdText)) {
            long skuId = Long.parseLong(skuIdText);
            wrapper.eq(skuId != 0, WareSkuEntity::getSkuId, skuId);
        }

        if (StringUtils.hasLength(wareIdText)) {
            long wareId = Long.parseLong(wareIdText);
            wrapper.eq(wareId != 0, WareSkuEntity::getWareId, wareId);
        }

        if (StringUtils.hasLength(key)) {
            if (key.matches("^\\d+$")) {
                wrapper.eq(WareSkuEntity::getId, Long.parseLong(key));
            }
            wrapper.or().like(WareSkuEntity::getSkuName, key);
        }

        return new PageUtils(page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        ));
    }

}