package com.shankong.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.shankong.mapper.CatalogMapper;
import com.shankong.pojo.Catalog;
import com.shankong.service.CatalogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {
    @Resource
    private CatalogMapper catalogMapper;

    @Override
    public List<Catalog> showCatalog() {
        return catalogMapper.showCatalog();
    }

    @Override
    @Transactional
    public void move(Long catalogId, String direction) {
        Catalog current = catalogMapper.selectById(catalogId);
        if (current == null) {
            throw new RuntimeException("分类不存在");
        }

        Catalog neighbor;
        if ("up".equals(direction)) {
            neighbor = catalogMapper.findPrevious(current.getSortOrder());
        } else {
            neighbor = catalogMapper.findNext(current.getSortOrder());
        }

        if (neighbor == null) {
            throw new RuntimeException("已经到边界了，无法继续移动");
        }

        // 三步哨兵法交换 sort_order
        // 不能用两步直接交换——sort_order 有 UNIQUE 约束
        // MySQL 每执行一条 SQL 立刻检查唯一性，不等事务提交
        Integer currentOld = current.getSortOrder();
        Integer neighborOld = neighbor.getSortOrder();

        catalogMapper.updateSortOrder(current.getCatalogId(), 0);          // 1. 哨兵占位
        catalogMapper.updateSortOrder(neighbor.getCatalogId(), currentOld); // 2. 邻居归位
        catalogMapper.updateSortOrder(current.getCatalogId(), neighborOld); // 3. 当前行归位
    }

    @Override
    public void deactivateByCatalogId(Long catalogId) {
        catalogMapper.deactivateByCatalogId(catalogId);
    }

    @Override
    public void batchDeactivate(List<Long> ids) {
        catalogMapper.batchDeactivate(ids);
    }

    @Override
    @Transactional
    public void insertCatalog(Catalog catalog) {
        catalog.setCatalogId(IdWorker.getId());
        Integer maxSortOrder = catalogMapper.selectMaxSortOrder();
        if(maxSortOrder == null) throw new RuntimeException("获取最大排序失败");
        catalog.setSortOrder(maxSortOrder + 1);

        catalogMapper.insertCatalog(catalog);
    }

    @Override
    public void updateCatalog(Catalog catalog) {
        catalogMapper.updateCatalog(catalog);
    }
}
