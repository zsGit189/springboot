package com.jk.service;

import com.jk.dao.UserDao;
import com.jk.model.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserBean queryUser(String  userBean) {


        return userDao.queryUser(userBean);
    }
}
