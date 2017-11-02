package com.jianyi.base.test;

import com.jianyi.database.dao.users.UsersDao;
import com.jianyi.database.service.users.IUsersService;
import com.mysql.cj.mysqlx.protobuf.MysqlxDatatypes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AppMain {
    public static void main(MysqlxDatatypes.Scalar.String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(
                new String[]{
                        "src/main/resources/com/jianyi/resources/spring/applicationContext.xml"
                });
        UsersDao usersDao = (UsersDao) context.getBean("usersDao");
        System.out.println("usersDao:" + usersDao.getAllUsers());

        IUsersService iUsersService = (IUsersService) context.getBean("usersServiceImpl");
        System.out.println("iUsersService:" + iUsersService.getAllUsers());
    }
}
