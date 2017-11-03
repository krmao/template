package com.smart.housekeeper.base.test;

import com.smart.housekeeper.database.dao.users.UsersDao;
import com.smart.housekeeper.database.service.users.IUsersService;
import com.mysql.cj.mysqlx.protobuf.MysqlxDatatypes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class HKAppMain {
    public static void main(MysqlxDatatypes.Scalar.String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(
                new String[]{
                        "src/main/resources/com/smart.housekeeper/resources/spring/applicationContext.xml"
                });
        UsersDao usersDao = (UsersDao) context.getBean("usersDao");
        System.out.println("usersDao:" + usersDao.getAllUsers());

        IUsersService iUsersService = (IUsersService) context.getBean("usersServiceImpl");
        System.out.println("iUsersService:" + iUsersService.getAllUsers());
    }
}
