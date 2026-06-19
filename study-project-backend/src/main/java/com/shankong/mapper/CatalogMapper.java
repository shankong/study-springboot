package com.shankong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shankong.pojo.Catalog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CatalogMapper extends BaseMapper<Catalog> {

    @Select("select * from db_catalog where catalog_state != 2 order by sort_order asc")
    List<Catalog> showCatalog();

    @Update("update db_catalog set catalog_id = #{newId} where catalog_id = #{oldId}")
    void updateCatalogId(@Param("newId") Long newId, @Param("oldId") Long oldId);

    /** 查当前行前面那条（用于上移），走 sort_order 索引 */
    @Select("select * from db_catalog where sort_order < #{sortOrder} order by sort_order desc limit 1")
    Catalog findPrevious(@Param("sortOrder") Integer sortOrder);

    /** 查当前行后面那条（用于下移），走 sort_order 索引 */
    @Select("select * from db_catalog where sort_order > #{sortOrder} order by sort_order asc limit 1")
    Catalog findNext(@Param("sortOrder") Integer sortOrder);

    /** 更新某行的 sort_order */
    @Update("update db_catalog set sort_order = #{sortOrder} where catalog_id = #{catalogId}")
    void updateSortOrder(@Param("catalogId") Long catalogId, @Param("sortOrder") Integer sortOrder);

    /** 逻辑删除某行 */
    @Update("update db_catalog set catalog_state = 2 where catalog_id = #{id}")
    void deactivateByCatalogId(@Param("id") Long id);

    /** 批量逻辑删除 */
    void batchDeactivate(@Param("ids") List<Long> ids);

    /** 新增catalog */
    @Insert("insert into db_catalog (catalog_id, catalog_name, catalog_number, catalog_state, sort_order) " +
            "values (#{catalogId}, #{catalogName}, #{catalogNumber}, #{catalogState}, #{sortOrder})")
    void insertCatalog(Catalog catalog);

    /** 查最大 sort_order */
    @Select("select max(sort_order) from db_catalog")
    Integer selectMaxSortOrder();

    /** 更新栏目*/
    void updateCatalog(Catalog catalog);
}
