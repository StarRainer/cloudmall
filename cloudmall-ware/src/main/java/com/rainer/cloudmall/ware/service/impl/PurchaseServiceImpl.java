package com.rainer.cloudmall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;

import com.rainer.cloudmall.ware.dao.PurchaseDao;
import com.rainer.cloudmall.ware.entity.PurchaseEntity;
import com.rainer.cloudmall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryUnreceivePurchasePage(Map<String, Object> params) {
        return new PageUtils(page(
                new Query<PurchaseEntity>().getPage(params),
                new LambdaQueryWrapper<PurchaseEntity>()
                        .eq(PurchaseEntity::getStatus, 0)
                        .or().eq(PurchaseEntity::getStatus, 1)
        ));
    }

}