package com.smart.housekeeper.database.dao.users;

import com.smart.housekeeper.database.model.UsersModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersDao {
    UsersModel getUserById(Integer userId);

    List<UsersModel> getAllUsers();
}
