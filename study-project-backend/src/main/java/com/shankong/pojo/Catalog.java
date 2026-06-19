package com.shankong.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("db_catalog")
public class Catalog {
    @TableId(value = "catalog_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)  // Long → String，避免 JS 精度丢失
    private Long catalogId;

    @TableField("catalog_name")
    private String catalogName;

    @TableField("catalog_number")
    private String catalogNumber;

    @TableField("catalog_state")
    private Integer catalogState;

    @TableField("sort_order")
    private Integer sortOrder;
}
