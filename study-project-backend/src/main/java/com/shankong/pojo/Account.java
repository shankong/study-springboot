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

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("db_account")  // 指定对应的数据库表名
public class Account implements Serializable {
    @Serial
    private static final long serialVersionUID = -8940196742313994740L;

    @TableId(value = "account_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long accountId;

    @TableField("account_email")
    private String accountEmail;

    @TableField("account_username")
    private String accountUsername;

    @TableField("account_password")
    private String accountPassword;

    @TableField("account_state")
    private Integer accountState;
}
