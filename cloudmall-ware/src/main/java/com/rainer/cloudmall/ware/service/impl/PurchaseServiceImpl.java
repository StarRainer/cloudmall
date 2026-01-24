package com.rainer.cloudmall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rainer.cloudmall.common.constant.WareConstant;
import com.rainer.cloudmall.ware.entity.PurchaseDetailEntity;
import com.rainer.cloudmall.ware.service.PurchaseDetailService;
import com.rainer.cloudmall.ware.service.WareSkuService;
import com.rainer.cloudmall.ware.utils.WareMapper;
import com.rainer.cloudmall.ware.vo.FinishPurchaseVo;
import com.rainer.cloudmall.ware.vo.MergePurchaseVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.util.CollectionUtils;


@Service("purchaseService")
@Transactional(rollbackFor = Exception.class, readOnly = true)
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    private final PurchaseDetailService purchaseDetailService;
    private final WareSkuService wareSkuService;

    public PurchaseServiceImpl(
            PurchaseDetailService purchaseDetailService,
            WareMapper wareMapper,
            WareSkuService wareSkuService
    ) {
        this.purchaseDetailService = purchaseDetailService;
        this.wareSkuService = wareSkuService;
    }

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergePurchase(MergePurchaseVo mergePurchaseVo) {
        if (mergePurchaseVo.getPurchaseId() == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            LocalDateTime now = LocalDateTime.now();
            purchaseEntity.setCreateTime(now);
            purchaseEntity.setUpdateTime(now);
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.CREATED.getCode());
            purchaseEntity.setPriority(0);
            save(purchaseEntity);
            mergePurchaseVo.setPurchaseId(purchaseEntity.getId());
        }

        PurchaseEntity entity = getById(mergePurchaseVo.getPurchaseId());
        if (entity == null) {
            throw new RuntimeException("采购单不存在");
        }

        if (entity.getStatus() != WareConstant.PurchaseStatus.CREATED.getCode()
                && entity.getStatus() != WareConstant.PurchaseStatus.ASSIGNED.getCode()) {
            throw new RuntimeException("采购单状态不对...");
        }

        if (CollectionUtils.isEmpty(mergePurchaseVo.getItems())) {
            return;
        }
        PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
        purchaseDetailEntity.setPurchaseId(mergePurchaseVo.getPurchaseId());
        purchaseDetailEntity.setStatus(WareConstant.PurchaseDeatilStatus.ASSIGNED.getCode());
        purchaseDetailService.update(purchaseDetailEntity, new LambdaUpdateWrapper<PurchaseDetailEntity>()
                .in(PurchaseDetailEntity::getId, mergePurchaseVo.getItems())
                .and(w ->
                        w.eq(PurchaseDetailEntity::getStatus, WareConstant.PurchaseDeatilStatus.CREATED.getCode())
                        .or().eq(PurchaseDetailEntity::getStatus, WareConstant.PurchaseDeatilStatus.ASSIGNED.getCode())
                )
        );

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(mergePurchaseVo.getPurchaseId());
        purchaseEntity.setUpdateTime(LocalDateTime.now());
        updateById(purchaseEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receivePurchase(List<Long> purchaseIds) {
        if (CollectionUtils.isEmpty(purchaseIds)) {
            return;
        }
        List<PurchaseEntity> purchaseEntities = list(new LambdaQueryWrapper<PurchaseEntity>()
                .select(PurchaseEntity::getId, PurchaseEntity::getStatus)
                .in(PurchaseEntity::getId, purchaseIds)
                .and(w -> {
                    w.eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatus.CREATED.getCode());
                    w.or().eq(PurchaseEntity::getStatus, WareConstant.PurchaseStatus.ASSIGNED.getCode());
                })
        );
        if (CollectionUtils.isEmpty(purchaseEntities)) {
            return;
        }
        purchaseEntities.forEach(purchaseEntity -> {
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.RECEIVED.getCode());
            purchaseEntity.setUpdateTime(LocalDateTime.now());
        });
        updateBatchById(purchaseEntities);

        List<Long> filteredPurchaseIds = purchaseEntities.stream().map(PurchaseEntity::getId).toList();
        PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
        purchaseDetailEntity.setStatus(WareConstant.PurchaseDeatilStatus.BUYING.getCode());
        purchaseDetailService.update(purchaseDetailEntity, new LambdaUpdateWrapper<PurchaseDetailEntity>()
                .in(PurchaseDetailEntity::getPurchaseId, filteredPurchaseIds)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishPurchase(FinishPurchaseVo finishPurchaseVo) {
        int purchaseStatus = WareConstant.PurchaseStatus.FINISHED.getCode();

        List<PurchaseDetailEntity> purchaseDetailEntities = new ArrayList<>();
        List<Long> itemIds = new ArrayList<>();
        for (FinishPurchaseVo.ItemVo itemVo : finishPurchaseVo.getItems()) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (itemVo.getStatus() == WareConstant.PurchaseDeatilStatus.FINISHED.getCode()) {
                purchaseDetailEntity.setStatus(itemVo.getStatus());
                itemIds.add(itemVo.getItemId());
            } else if (itemVo.getStatus() == WareConstant.PurchaseDeatilStatus.FAILURE.getCode()) {
                purchaseDetailEntity.setStatus(itemVo.getStatus());
                purchaseStatus = WareConstant.PurchaseStatus.ERROR.getCode();
            } else {
                continue;
            }
            purchaseDetailEntity.setId(itemVo.getItemId());
            purchaseDetailEntities.add(purchaseDetailEntity);
        }
        if (!CollectionUtils.isEmpty(purchaseDetailEntities)) {
            purchaseDetailService.updateBatchById(purchaseDetailEntities);
        }

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(finishPurchaseVo.getId());
        purchaseEntity.setUpdateTime(LocalDateTime.now());
        purchaseEntity.setStatus(purchaseStatus);
        updateById(purchaseEntity);

        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }
        purchaseDetailEntities = purchaseDetailService.listByIds(itemIds);
        wareSkuService.updateStockByPurchase(purchaseDetailEntities);
    }

}