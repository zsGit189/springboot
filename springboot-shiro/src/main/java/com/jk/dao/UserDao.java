package com.jk.dao;

import com.jk.model.UserBean;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    UserBean queryUser(String userName);
}
