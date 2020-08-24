package com.smart.springcloud.appb.base.config.exception

import com.smart.springcloud.appb.http.model.HKCode
import com.smart.springcloud.appb.http.model.HKResponse
import com.smart.springcloud.appb.http.model.HKServerException
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

@ControllerAdvice
class CXControllerAdvice {

    private val logger: Logger = LogManager.getLogger(CXControllerAdvice::class.java.name)

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun exceptionHandler(ex: Exception): HKResponse<Any> {
        logger.error("[全局拦截器] 拦截到错误 ! 返回 HKCode.ERROR.response(${ex.message})", ex)
        return HKCode.ERROR.response(ex.message)
    }

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun exceptionHandler(ex: HttpMessageNotReadableException): HKResponse<Any> {
        logger.error("[全局拦截器] 拦截到错误 ! 返回 HKCode.ERROR.response(${ex.message})", ex)
        return HKCode.ERROR.response(ex.message)
    }

    /**
     * 全局特定异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(HKServerException::class)
    fun serverExceptionHandler(ex: HKServerException): HKResponse<Any> {
        logger.error("[全局拦截器] 拦截到特定错误 ! 返回 HKCode.ERROR_SERVER.response(${ex.message})", ex)
        return HKCode.ERROR_SERVER.response(ex.message)
    }

    /**
     * 全局特定异常捕捉处理 for mybatis
     */
//    @ResponseBody
//    @ExceptionHandler(MyBatisSystemException::class)
//    fun serverExceptionHandler(ex: MyBatisSystemException): HKResponse<Any> {
//        logger.error("[全局拦截器] 拦截到 MyBatis 执行 错误 ! 返回 HKCode.ERROR_SQL.response(${ex.rootCause.message})", ex)
//        return HKCode.ERROR_SQL.response(ex.rootCause.message)
//    }

//    /**
//     * 全局特定异常捕捉处理 for mybatis
//     */
//    @ResponseBody
//    @ExceptionHandler(BadSqlGrammarException::class)
//    fun serverExceptionHandler(ex: BadSqlGrammarException): HKResponse<Any> {
//        logger.error("[全局拦截器] 拦截到 MyBatis 执行 错误 ! 返回 HKCode.ERROR_SQL.response(${ex.rootCause.message})", ex)
//        return HKCode.ERROR_SQL.response(ex.rootCause.message)
//    }
//
//    /**
//     * 全局特定异常捕捉处理 for mybatis
//     */
//    @ResponseBody
//    @ExceptionHandler(DataIntegrityViolationException::class)
//    fun serverExceptionHandler(ex: DataIntegrityViolationException): HKResponse<Any> {
//        logger.error("[全局拦截器] 拦截到 MyBatis 执行 错误 ! 返回 HKCode.ERROR_SQL_INSERT.response(${ex.rootCause.message})", ex)
//        return HKCode.ERROR_SQL_INSERT.response(ex.rootCause.message)
//    }
//
//    /**
//     * 全局特定异常捕捉处理 for mybatis
//     */
//    @ResponseBody
//    @ExceptionHandler(BindingException::class)
//    fun serverExceptionHandler(ex: BindingException): HKResponse<Any> {
//        logger.error("[全局拦截器] 拦截到 MyBatis 执行 错误 ! 返回 HKCode.ERROR_SQL.response(${ex.message})", ex)
//        return HKCode.ERROR_SQL.response(ex.message)
//    }
}
