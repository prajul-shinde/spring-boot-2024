package com.omsai.securitydemo.dao;

import com.omsai.securitydemo.entity.User;

public interface UserDao {

    User findByUserName(String userName);
    
}
