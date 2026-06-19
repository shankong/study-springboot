package com.shankong.service;

import com.shankong.pojo.Catalog;

import java.util.List;

public interface CatalogService {
    List<Catalog> showCatalog();

    /** 上移/下移：交换相邻行的 sort_order */
    void move(Long catalogId, String direction);

    void deactivateByCatalogId(Long catalogId);

    void batchDeactivate(List<Long> ids);

    void insertCatalog(Catalog catalog);

    void updateCatalog(Catalog catalog);
}
