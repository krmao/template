package com.smart.springcloud.appa.base.mapper
//
//import org.apache.ibatis.annotations.*
//
//interface CXBaseMapper<T> {
//
//    /**
//     * 调用结束后通过 tableModel.id 获取id值
//     */
//    @InsertProvider(type = CXSqlProvider::class, method = "insert")
//    fun insert(newData: T)
//
//    @DeleteProvider(type = CXSqlProvider::class, method = "delete")
//    fun delete(condition: T): Int
//
//    @UpdateProvider(type = CXSqlProvider::class, method = "update")
//    fun update(newData: T): Int
//
//    @UpdateProvider(type = CXSqlProvider::class, method = "updateV2")
//    fun updateV2(@Param("newData") newData: T, @Param("condition") condition: T): Int
//
//    @SelectProvider(type = CXSqlProvider::class, method = "select")
//    fun select(condition: T): MutableList<T>
//}
