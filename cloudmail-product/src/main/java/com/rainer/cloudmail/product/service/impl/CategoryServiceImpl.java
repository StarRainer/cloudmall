package com.rainer.cloudmail.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainer.cloudmail.product.dao.CategoryDao;
import com.rainer.cloudmail.product.entity.CategoryEntity;
import com.rainer.cloudmail.product.service.CategoryService;
import com.rainer.common.utils.PageUtils;
import com.rainer.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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