package com.shankong.mapper;

import com.shankong.pojo.Account;
import com.shankong.pojo.Register;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from db_account where account_username = #{usernameOrEmail} or account_email = #{usernameOrEmail}")
    Account findByUsernameOrEmail(String usernameOrEmail);

    @Update("update db_account set account_password = #{password} where account_email = #{email}")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

    @Insert("insert into db_account (account_id, account_email, account_username, account_password) values (#{id}, #{email}, #{username}, #{password})")
    Integer insert(Register register);

    @Select("select * from db_account where account_state != 2")
    List<Account> showAccount();

    @Update("update db_account set account_state = 2 where account_username = #{username}")
    void deactivateByUsername(@Param("username") String username);

    @Select("select * from db_account where account_username = #{username}")
    Account findByUsername(String username);
}
