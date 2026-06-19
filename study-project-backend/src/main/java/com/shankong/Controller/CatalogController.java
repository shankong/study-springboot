package com.shankong.Controller;

import com.shankong.entity.RestBean;
import com.shankong.pojo.Catalog;
import com.shankong.service.CatalogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    @Resource
    private CatalogService catalogService;

    /** 获取分类列表（按 sort_order 排序） */
    @GetMapping("/list")
    public RestBean<List<Catalog>> list() {
        List<Catalog> catalogs = catalogService.showCatalog();
        return RestBean.<List<Catalog>>success(catalogs);
    }

    /** 上移/下移：direction = "up" 或 "down" */
    @PostMapping("/move")
    public RestBean<String> move(@RequestParam Long catalogId,
                                  @RequestParam String direction) {
        try {
            catalogService.move(catalogId, direction);
            return RestBean.success("移动成功");
        } catch (RuntimeException e) {
            return RestBean.failure(400, "移动失败");
        }
    }

    @GetMapping("/deactivate")
    public RestBean<String> deactivate(@RequestParam Long catalogId) {
        try {
            catalogService.deactivateByCatalogId(catalogId);
            return RestBean.success("删除成功");
        } catch (RuntimeException e) {
            return RestBean.failure(400, "删除失败");
        }
    }

    /** 批量删除 */
    @PostMapping("/batch-deactivate")
    public RestBean<String> batchDeactivate(@RequestBody List<Long> ids) {
        try {
            catalogService.batchDeactivate(ids);
            return RestBean.success("批量删除成功");
        } catch (RuntimeException e) {
            return RestBean.failure(400, "批量删除失败");
        }
    }

    @PostMapping("/insert")
    public RestBean<String> insert(@RequestBody Catalog catalog) {
        catalogService.insertCatalog(catalog);
        return RestBean.success("添加成功");
    }

    @PostMapping("/update")
    public RestBean<String> update(@RequestBody Catalog catalog) {
        catalogService.updateCatalog(catalog);
        return RestBean.success("更改成功");
    }
}
