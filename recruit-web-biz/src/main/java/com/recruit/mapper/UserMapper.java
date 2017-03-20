package com.recruit.mapper;

import com.recruit.entity.User;
import com.recruit.vo.request.UserPageRequestVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhuangjt on 2017/3/16.
 */
@Repository
public interface UserMapper {
    String UPDATE_SQL = "<set>" +
                            "id = #{user.id}, " +
                            "<if test='user.userName != null'> userName = #{user.userName}, </if>" +
                            "<if test='user.roleId != null'> roleId = #{user.roleId}, </if>" +
                            "<if test='user.lockPortQty != null'> lockPortQty = #{user.lockPortQty}, </if>" +
                            "<if test='user.allotPortQty != null'> allotPortQty = #{user.allotPortQty}, </if>" +
                        "</set>";

    @Select({"<script>",
            "select * from t_user where 1=1 ",
            "<if test='requestVo.userAccount != null'> and userAccount like concat('%', #{requestVo.userAccount}, '%') </if>",
            "<if test='requestVo.roleId != null'> and roleId = #{requestVo.roleId} </if>",
            "</script>"})
    List<User> queryAutoPage(@Param("requestVo") UserPageRequestVo requestVo);

    @Update({"<script>",
            "update t_user",
            UPDATE_SQL,
            "where id = #{user.id}",
            "</script>"})
    int update(@Param("user") User user);

    @Delete({"delete from t_user where id = #{id}"})
    int delete(@Param("id") Integer id);

    @Select({"select * from t_user where userAccount = #{userAccount} limit 1"})
    User queryByUserAccount(@Param("userAccount") String userAccount);

    @Insert({"insert into `t_user` (`id`, `userAccount`, `password`, `userName`, `salt`, `roleId`) ",
            "values(NULL, #{user.userAccount}, #{user.password}, #{user.userName}, #{user.salt}, #{user.roleId}) "})
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "user.id")
    int insert(@Param("user") User user);

    @Update({"update t_user set lockPortQty = #{lockPortQty} where id = #{id} "})
    int updateLockPortQty(@Param("lockPortQty") Integer lockPortQty, @Param("id") Integer id);
}
