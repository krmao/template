package com.jianyi.database.service.users.impl;

import com.jianyi.database.dao.users.UsersDao;
import com.jianyi.database.model.UsersModel;
import com.jianyi.database.service.users.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements IUsersService {
//    @Autowired
    private UsersDao usersDao;

    @Override
    public UsersModel getUserById(Integer userId) {
        return usersDao.getUserById(userId);
    }

    @Override
    public List<UsersModel> getAllUsers() {
        return usersDao.getAllUsers();
    }
}
