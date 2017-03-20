package com.recruit.common;

import com.recruit.entity.User;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Created by zhuangjt on 2017/3/20.
 */
public class SQLGenerator {
    public static <T> void updateSql(Class<T> clazz, String preParam) {
        if(clazz == null) {
            return;
        }

        if(StringUtils.isBlank(preParam)) {
            preParam = StringUtils.EMPTY;
        }

        StringBuilder stringBuilder = new StringBuilder(1024);        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields) {
            if(field.getName().equalsIgnoreCase("serialVersionUID")) {
                continue;
            }

            String param = StringUtils.isBlank(preParam) ? field.getName() : (preParam + "." + field.getName());
            stringBuilder.append("\"");
            stringBuilder.append("<if test='").append(field.getName()).append(" != null'> ");
            stringBuilder.append(field.getName()).append(" = #{").append(param).append("}");
            stringBuilder.append(" </if>").append("\"");
            stringBuilder.append(" + ").append("\n");
        }

        System.out.print(stringBuilder);
    }

    public static void main(String[] args) {
        updateSql(User.class, "user");
    }

}
