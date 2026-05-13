package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid 用户的openid标识
     * @return 返回对应的User对象
     */
    @Select("select * from user where openid=#{openid}") // 使用MyBatis的注解方式执行SQL查询,从user表中查询openid匹配的记录
    User getByOpenid(String openid);//User对象

    /**
     * 根据id查询用户
     * @param id 用户ID,用于查询指定用户
     * @return 返回查询到的用户对象,如果未找到则返回null
     */
    @Select("select * from user where id = #{id}") // 使用MyBatis的注解,执行SQL查询语句,从user表中根据id字段查询用户信息
    User getById(Long id); // 方法定义,接收一个Long类型的id参数,返回一个User对象

    /**
     * 插入用户数据
     * @param user
     */
    void insert(User user);//插入完用户要返回主键值 在mapper配置里面

    /**
     * 根据条件统计用户数量
     * @param map 查询条件Map,包含begin和end时间范围
     * @return 符合条件的用户数量
     */
    Integer countByMap(@Param("map") Map map);
}
