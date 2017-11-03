package com.smart.housekeeper.database.model;

import java.io.Serializable;

//user表
public class UsersModel implements Serializable {

    private static final long serialVersionUID = 8199663204871318189L;

    public int id;
    public String name;
    public String cell_phone;
    public String age;
    public String address;
    public String nick_name;
    public short gender;

    @Override
    public String toString() {
        return "UsersModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cell_phone='" + cell_phone + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", gender=" + gender +
                '}';
    }
}
