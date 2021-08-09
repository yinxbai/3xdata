package com.txdata.modules.daily.dao;


import com.txdata.modules.daily.domain.MyUserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyUserDao {
    /**
     * 查找用户
     * @param id
     * @return
     */
    MyUserDO findById(String id);
}
