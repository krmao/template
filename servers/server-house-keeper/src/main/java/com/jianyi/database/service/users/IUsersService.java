package com.jianyi.database.service.users;

import com.jianyi.database.model.UsersModel;

import java.util.List;

/**
 * 用户管理业务层接口定义
 */
public interface IUsersService {

    UsersModel getUserById(Integer userId);

    List<UsersModel> getAllUsers();

}
