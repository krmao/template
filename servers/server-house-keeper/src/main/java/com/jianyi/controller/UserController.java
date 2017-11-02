//package com.jianyi.controller;
//
//import com.jianyi.controller.model.CommonRequestModel;
//import com.jianyi.controller.model.CommonResponseModel;
//import com.jianyi.controller.model.data.request.UserRequestDataModel;
//import com.jianyi.database.model.UsersModel;
//import com.jianyi.database.service.users.IUsersService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/user")
//public class UserController {
//    @Autowired
//    private IUsersService usersService;
//
//    @RequestMapping(value = {""}, method = {RequestMethod.GET})
//    public
//    @ResponseBody
//    CommonResponseModel<List<UsersModel>> getAllUsers() {
//        return new CommonResponseModel<>(usersService.getAllUsers());
//    }
//
//    @RequestMapping(value = {"/{userId}"}, method = {RequestMethod.GET})
//    public
//    @ResponseBody
//    CommonResponseModel getUserById(@PathVariable("userId") int userId) {
//        return new CommonResponseModel<>(usersService.getUserById(userId));
//    }
//
//    @RequestMapping(value = {"/getUserById"}, method = {RequestMethod.POST}, consumes = {"application/json"}, produces = {"application/json"})
//    public
//    @ResponseBody
//    CommonResponseModel getUserById(@RequestBody CommonRequestModel<UserRequestDataModel> requestModel) {
//        return new CommonResponseModel<>(usersService.getUserById(requestModel.data.id));
//    }
//}
