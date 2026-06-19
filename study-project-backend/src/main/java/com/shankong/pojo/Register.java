package com.shankong.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Register {

    @TableId(type = IdType.ASSIGN_ID)  // 雪花算法自动生成 Long 型 ID
    private Long id;

    private String email;
    private String username;
    private String password;
    private String emailCode;
}
