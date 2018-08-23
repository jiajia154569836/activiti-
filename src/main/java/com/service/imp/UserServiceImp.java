package com.service.imp;

import com.dao.UserDao;
import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("service1")
public class UserServiceImp implements UserService {

    @Autowired
   private UserDao userDao;


    @Override
    public User query(User user) {
        System.out.println(user.getPassword());
        System.out.println(user.getUsername());
             return  userDao.query(user)  ;
    }
}
