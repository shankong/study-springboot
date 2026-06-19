package com.shankong;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.shankong.mapper.CatalogMapper;
import com.shankong.mapper.UserMapper;
import com.shankong.pojo.Account;
import com.shankong.pojo.Catalog;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;

@SpringBootTest
class StudyProjectBackendApplicationTests {
    @Resource
    private CatalogMapper catalogMapper;

    @Test
    void contextLoads() {
        Catalog catalog = catalogMapper.selectById(2067439589892702211L);
        System.out.println(catalog);
    }

    /*@Test
    void replaceOldIdsWithSnowflake() {
        List<Catalog> catalogs = catalogMapper.showCatalog();
        for (Catalog catalog : catalogs) {
            Long oldId = catalog.getCatalogId();
            Long newId = IdWorker.getId();
            // 用旧 ID 定位行，替换成新 ID
            catalogMapper.updateCatalogId(newId, oldId);
            System.out.println(catalog.getCatalogName() + ": " + oldId + " → " + newId);
        }
        System.out.println("全部替换完成，共 " + catalogs.size() + " 条");
    }*/
}
