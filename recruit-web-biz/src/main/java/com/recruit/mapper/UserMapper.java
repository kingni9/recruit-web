package com.recruit.mapper;

import com.recruit.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by zhuangjt on 2017/3/16.
 */
@Repository
public interface UserMapper {
    @Select({"select * from t_user where userAccount = #{userAccount} limit 1"})
    User queryByUserAccount(@Param("userAccount") String userAccount);

    @Insert({"insert into `t_user` (`id`, `userAccount`, `password`, `userName`, `salt`, `roleId`) ",
            "values(NULL, #{user.userAccount}, #{user.password}, #{user.userName}, #{user.salt}, #{user.roleId}) "})
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "user.id")
    int insert(@Param("user") User user);

    @Update({"update t_user set lockPortQty = #{lockPortQty} where id = #{id} "})
    int updateLockPortQty(@Param("lockPortQty") Integer lockPortQty, @Param("id") Integer id);
}
