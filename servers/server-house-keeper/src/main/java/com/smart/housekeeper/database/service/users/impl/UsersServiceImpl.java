package com.smart.housekeeper.database.service.users.impl;

import com.smart.housekeeper.database.dao.users.UsersDao;
import com.smart.housekeeper.database.service.users.IUsersService;
import com.smart.housekeeper.database.model.UsersModel;
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
