package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);//只有先查出l用户才能进行密码比对

    /**
     * 新增 插入员工数据
     * @param employee
     */
    @Insert("insert into employee(name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user,status)"+
    "values"+
    "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    @AutoFill(value= OperationType.INSERT)
    void insert(Employee employee);

    /*
    * 分页
    * */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /*
    * 启用禁用 根据id动态修改属性  编辑员工也共用
    * */
    @AutoFill(value= OperationType.UPDATE)
    void update(Employee employee);

    /*
    * 根据id查询员工
    * */
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);
}
