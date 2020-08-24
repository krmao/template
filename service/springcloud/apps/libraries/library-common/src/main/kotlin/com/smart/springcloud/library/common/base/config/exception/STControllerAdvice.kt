package com.smart.springcloud.library.common.base.config.exception

import com.smart.springcloud.library.common.http.model.STCode
import com.smart.springcloud.library.common.http.model.STResponse
import com.smart.springcloud.library.common.http.model.STServerException
//import org.apache.ibatis.binding.BindingException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
//import org.mybatis.spring.MyBatisSystemException
//import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.converter.HttpMessageNotReadableException
//import org.springframework.jdbc.BadSqlGrammarException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@Suppress("unused")
@ControllerAdvice
class STControllerAdvice {

    private val logger: Logger = LogManager.getLogger(STControllerAdvice::class.java.name)

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(ex: Exception): STResponse<Any> {
        logger.error("[全局拦截器] 拦截到错误 ! 返回 STCode.ERROR.response(${ex.message})", ex)
        return STCode.ERROR.response(ex.message)
    }

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun exceptionHandler(ex: HttpMessageNotReadableException): STResponse<Any> {
        logger.error("[全局拦截器] 拦截到错误 ! 返回 STCode.ERROR.response(${ex.message})", ex)
        return STCode.ERROR.response(ex.message)
    }

    /**
     * 全局特定异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(STServerException::class)
    fun serverExceptionHandler(ex: STServerException): STResponse<Any> {
        logger.error("[全局拦截器] 拦截到特定错误 ! 返回 STCode.ERROR_SERVER.response(${ex.message})", ex)
        return STCode.ERROR.response(ex.message)
    }

    /**
     * 全局特定异常捕捉处理 for mybatis
     */
//    @ResponseBody
//    @ExceptionHandler(MyBatisSystemException::class)
//    fun serverExceptionHandler(ex: MyBatisSystemException): STResponse<Any> {
//        logger.error("[全局拦截器] 拦截到 MyBatis 执行 错误 ! 返回 STCode.ERROR_SQL.response(${ex.rootCause.message})", ex)
//        return STCode.ERROR_SQL.response(ex.rootCause.message)
//    }

//    /**
//     * 全局特定异常捕捉处理 for mybatis
//     */
//    @ResponseBody
//    @ExceptionHandler(BadSqlGrammarException::class)
//    fun serverExceptionHandler(ex: BadSqlGrammarException): STResponse<Any> {
//        logger.error("[全局拦截器] 拦截到 MyBatis 执行 错误 ! 返回 STCode.ERROR_SQL.response(${ex.rootCause.message})", ex)
//        return STCode.ERROR_SQL.response(ex.rootCause.message)
//    }
//
//    /**
//     * 全局特定异常捕捉处理 for mybatis
//     */
//    @ResponseBody
//    @ExceptionHandler(DataIntegrityViolationException::class)
//    fun serverExceptionHandler(ex: DataIntegrityViolationException): STResponse<Any> {
//        logger.error("[全局拦截器] 拦截到 MyBatis 执行 错误 ! 返回 STCode.ERROR_SQL_INSERT.response(${ex.rootCause.message})", ex)
//        return STCode.ERROR_SQL_INSERT.response(ex.rootCause.message)
//    }
//
//    /**
//     * 全局特定异常捕捉处理 for mybatis
//     */
//    @ResponseBody
//    @ExceptionHandler(BindingException::class)
//    fun serverExceptionHandler(ex: BindingException): STResponse<Any> {
//        logger.error("[全局拦截器] 拦截到 MyBatis 执行 错误 ! 返回 STCode.ERROR_SQL.response(${ex.message})", ex)
//        return STCode.ERROR_SQL.response(ex.message)
//    }
}
