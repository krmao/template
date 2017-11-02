package com.jianyi.database.dao.users;

import com.jianyi.database.model.UsersModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersDao {
    UsersModel getUserById(Integer userId);

    List<UsersModel> getAllUsers();
}
