package com.rainer.cloudmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmall.common.utils.PageUtils;
import com.rainer.cloudmall.common.utils.Query;
import com.rainer.cloudmall.product.dao.CategoryDao;
import com.rainer.cloudmall.product.entity.CategoryEntity;
import com.rainer.cloudmall.product.service.CategoryBrandRelationService;
import com.rainer.cloudmall.product.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    private final CategoryBrandRelationService categoryBrandRelationService;

    public CategoryServiceImpl(CategoryBrandRelationService categoryBrandRelationService) {
        this.categoryBrandRelationService = categoryBrandRelationService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 1.查出所有分类
        List<CategoryEntity> categoryEntities = list();

        // 2.组装成父子树形结构
        return categoryEntities
                .stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .map(menu -> {
                    // 递归设置子菜单
                    menu.setChildren(getChildrenCategory(menu, categoryEntities));
                    return menu;
                })
                // 第一个参数指定比较哪个字段，第二个参数指定排序规则
                .sorted(Comparator.comparing(CategoryEntity::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    @Override
    public void removeMenusByIds(List<Long> catIds) {
        // TODO: 如果有子节点没有被删除，不能删除当前节点
        removeByIds(catIds);
    }

    @Override
    public List<Long> getPathLink(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        long currentCatelogId = catelogId;
        do {
            paths.addFirst(currentCatelogId);
            currentCatelogId = getById(currentCatelogId).getParentCid();
        } while (currentCatelogId != 0);
        return paths;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCascade(CategoryEntity category) {
        updateById(category);
        if (StringUtils.hasLength(category.getName())) {
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    /**
     * 获取根菜单对应的菜单树
     */
    private List<CategoryEntity> getChildrenCategory(CategoryEntity currentMenu, List<CategoryEntity> menuList) {
        return menuList
                .stream()
                .filter(menu -> menu.getParentCid().equals(currentMenu.getCatId()))
                .map(menu -> {
                    menu.setChildren(getChildrenCategory(menu, menuList));
                    return menu;
                })
                .sorted(Comparator.comparing(CategoryEntity::getSort, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }
}