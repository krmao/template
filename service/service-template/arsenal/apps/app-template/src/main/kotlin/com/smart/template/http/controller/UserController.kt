package com.smart.template.http.controller

//
//@RestController
//@RequestMapping("/user")
//class UserController(val userMapper: UserMapper) {
//    private val logger: Logger = LogManager.getLogger(UserController::class.java.name)
//
//    @ApiOperation("根据角色ID获取所有的用户")
//    @PostMapping("/findAllUsersByRoleId")
//    fun findAllUsersByRoleId(@RequestBody request: HKRequest<HKRoleIdDataPaging>): HKResponse<List<UserModel>> {
//        if (request.data == null || request.data?.roleId == null) return HKCode.ERROR_PARAMS.response(arrayListOf())
//
//        val pageIndex = (request.data?.pageIndex ?: 1).let { if (it > 0) it else CXConfig.DEFAULT_PAGE_INDEX }
//        val count = (request.data?.pageSize ?: 1).let { if (it > 0) it else CXConfig.DEFAULT_PAGE_SIZE }
//        val start = ((pageIndex - 1) * count).let { if (it >= 0) it else 0 }
//
//        logger.error("pageIndex:" + pageIndex)
//        logger.error("start:" + start)
//        logger.error("count:" + count)
//
//        return HKResponse(userMapper.findAllUsersByRoleId(roleId = request.data?.roleId ?: CXConfig.DEFAULT_SQL_ID, start = start, count = count))
//    }
//
//    @ApiOperation("更新用户", notes = "返回 受影响的行数")
//    @ApiImplicitParam(name = "userModel", value = "用户信息")
//    @PostMapping("/updateUser")
//    fun updateUser(@RequestBody request: HKRequest<UserModel>): HKResponse<HKColumnData> {
//        if (request.data == null) return HKCode.ERROR_PARAMS.response(HKColumnData())
//        return HKResponse(HKColumnData(userMapper.updateUser(request.data)))
//    }
//
//    @ApiOperation("创建用户", notes = "返回 userId")
//    @PostMapping("/createUser")
//    fun createUser(@RequestBody request: HKRequest<UserModel>): HKResponse<HKUserIdData> {
//        if (request.data == null || StringUtils.isEmpty(request.data?.userName)) return HKCode.ERROR_PARAMS.response(HKUserIdData())
//
//        return HKResponse(HKUserIdData(userMapper.createUser(request.data)))
//    }
//
//    @ApiOperation("删除用户", notes = "返回 受影响的行数")
//    @PostMapping("/deleteUser")
//    fun deleteUser(@RequestBody request: HKRequest<HKUserIdData>): HKResponse<HKColumnData> {
//        if (request.data == null || request.data?.userId == -1) return HKCode.ERROR_PARAMS.response(HKColumnData(-1))
//
//        return HKResponse(HKColumnData(userMapper.deleteUser(request.data?.userId)))
//    }
//}
