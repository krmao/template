package com.smart.springcloud.appb.base.mapper

import com.smart.springcloud.appb.base.util.CXReflectUtil
import com.smart.springcloud.appb.base.util.toLowerUnderScoreFromUpperCamel
//import org.apache.ibatis.annotations.Param
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Suppress("unused")
class CXSqlProvider {
    private val logger: Logger = LogManager.getLogger(CXSqlProvider::class.java.name)

    fun insert(newData: Any?): String {
        val tableName = newData?.javaClass?.simpleName?.replace("Model", "")?.toLowerUnderScoreFromUpperCamel()
        val newDataNameList = CXReflectUtil.getFields(newData?.javaClass).filter { CXReflectUtil.getValue(it, newData) != null }.map { it.name }

        val sqlString = StringBuilder("insert into $tableName (")
        newDataNameList.forEachIndexed { index, valueName -> sqlString.append(valueName).append(if (index != newDataNameList.size - 1) "," else ") values(") }
        newDataNameList.forEachIndexed { index, valueName -> sqlString.append("#{$valueName}").append(if (index != newDataNameList.size - 1) "," else ")") }

        logger.warn("""
            ---------------------------------
            mybatis-buildSql-insert -->
            newData: $newData
                   sql: $sqlString
            ---------------------------------
        """)
        return sqlString.toString()
    }

//    fun updateV2(@Param("newData") newData: Any?, @Param("condition") condition: Any?): String {
//        val tableName = newData?.javaClass?.simpleName?.replace("Model", "")?.toLowerUnderScoreFromUpperCamel()
//        val newDataNameList = CXReflectUtil.getFields(newData?.javaClass).filter { CXReflectUtil.getValue(it, newData) != null }.map { it.name }
//
//        val sqlString = StringBuilder()
//        if (newDataNameList.isNotEmpty()) {
//            sqlString.append("update $tableName set ")
//            newDataNameList.forEachIndexed { index, valueName -> sqlString.append("$valueName=#{newData.$valueName}").append(if (index != newDataNameList.size - 1) "," else "") }
//
//            val conditionNameList = CXReflectUtil.getFields(condition?.javaClass).filter { CXReflectUtil.getValue(it, condition) != null }.map { it.name }
//            if (conditionNameList.isNotEmpty()) {
//                sqlString.append(" where ")
//                conditionNameList.forEachIndexed { index, valueName -> sqlString.append("$valueName=#{condition.$valueName}").append(if (index != newDataNameList.size - 1) "," else "") }
//            }
//
//        } else {
//            sqlString.append("update $tableName where 1=0")
//        }
//
//
//        logger.warn("""
//               newData=${newData?.javaClass?.name}
//               newData=$newData
//               condition=${condition?.javaClass?.name}
//               condition=$condition
//               tableName=$tableName
//               newDataNameList=$newDataNameList
//
//            """)
//        logger.warn("""
//            ---------------------------------
//            mybatis-buildSql-update -->
//              newData: $newData
//            condition: $condition
//                   sql: $sqlString
//            ---------------------------------
//        """)
//        return sqlString.toString()
//    }

    fun update(tableModel: Any?): String {
        val tableName = tableModel?.javaClass?.simpleName?.replace("Model", "")?.toLowerUnderScoreFromUpperCamel()
        val modelFields = CXReflectUtil.getFields(tableModel?.javaClass)
        val valueNameList = ArrayList<String>()
        val sqlString = StringBuilder("update $tableName set ")
        modelFields.forEach { field -> CXReflectUtil.getValue(field, tableModel)?.let { valueNameList.add(field.name) } }
        valueNameList.forEachIndexed { index, valueName -> sqlString.append("$valueName=#{$valueName}").append(if (index != valueNameList.size - 1) "," else "") }

        logger.warn("""
            ---------------------------------
            mybatis-buildSql-update -->
            tableModel: $tableModel
                   sql: $sqlString
            ---------------------------------
        """)
        return sqlString.toString()
    }

    fun delete(condition: Any?): String {
        val tableName = condition?.javaClass?.simpleName?.replace("Model", "")?.toLowerUnderScoreFromUpperCamel()
        val conditionNameList = CXReflectUtil.getFields(condition?.javaClass).filter { CXReflectUtil.getValue(it, condition) != null }.map { it.name }

        val sqlString = StringBuffer()
        if (conditionNameList.isNotEmpty()) {
            sqlString.append("delete from $tableName where ")
            conditionNameList.forEachIndexed { index, valueName -> sqlString.append("$valueName=#{$valueName}").append(if (index != conditionNameList.size - 1) " and " else "") }
        } else {
            sqlString.append("delete from $tableName where 1=0")
        }
        logger.warn("""
            ---------------------------------
            mybatis-buildSql-delete -->
            condition: $condition
                   sql: $sqlString
            ---------------------------------
        """)
        return sqlString.toString()
    }

    fun select(condition: Any?): String {
        val tableName = condition?.javaClass?.simpleName?.replace("Model", "")?.toLowerUnderScoreFromUpperCamel()
        val conditionNameList = CXReflectUtil.getFields(condition?.javaClass).filter { CXReflectUtil.getValue(it, condition) != null }.map { it.name }

        val sqlString = StringBuilder("select * from $tableName")
        if (conditionNameList.isNotEmpty()) {
            sqlString.append(" where ")
            conditionNameList.forEachIndexed { index, valueName -> sqlString.append("$valueName=#{$valueName}").append(if (index != conditionNameList.size - 1) " and " else "") }
        }
        logger.warn("""
            ---------------------------------
            mybatis-buildSql-select -->
            condition: $condition
                   sql: $sqlString
            ---------------------------------
        """)
        return sqlString.toString()
    }
}
