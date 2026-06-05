package com.shankong.mapper;

import com.shankong.pojo.Account;
import com.shankong.pojo.Register;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("select * from db_account where username = #{usernameOrEmail} or email = #{usernameOrEmail}")
    Account findByUsernameOrEmail(String usernameOrEmail);

    @Update("update db_account set password = #{password} where email = #{email}")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    @Insert("insert into db_account (email, username, password) values (#{email}, #{username}, #{password})")
    Integer insert(Register register);
}
