package com.shankong.mapper;

import com.shankong.pojo.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from db_account where username = #{usernameOrEmail} or email = #{usernameOrEmail}")
    Account findByUsernameOrEmail(String usernameOrEmail);
}
