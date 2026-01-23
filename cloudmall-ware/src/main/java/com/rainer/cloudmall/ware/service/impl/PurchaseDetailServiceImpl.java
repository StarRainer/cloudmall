package com.rainer.cloudmall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.ware.dao.PurchaseDetailDao;
import com.rainer.cloudmall.ware.entity.PurchaseDetailEntity;
import com.rainer.cloudmall.ware.service.PurchaseDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseDetailEntity> wrapper = new LambdaQueryWrapper<>();

        String key = (String) params.get("key");
        String statusText = (String) params.get("status");
        String wareIdText = (String) params.get("wareId");

        if (StringUtils.hasLength(statusText)) {
            int status = Integer.parseInt(statusText);
            wrapper.eq(PurchaseDetailEntity::getStatus, status);
        }

        if (StringUtils.hasLength(wareIdText)) {
            long wareId = Long.parseLong(wareIdText);
            wrapper.eq(wareId != 0, PurchaseDetailEntity::getWareId, wareId);
        }

        if (StringUtils.hasLength(key) && key.matches("^\\d+$")) {
            long id = Long.parseLong(key);
            wrapper.and(w ->
                    w.eq(id != 0, PurchaseDetailEntity::getId, id)
                            .or().eq(id != 0, PurchaseDetailEntity::getSkuId, id)
                            .or().eq(id != 0, PurchaseDetailEntity::getPurchaseId, id)
            );
        }


        return new PageUtils(page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        ));
    }

}