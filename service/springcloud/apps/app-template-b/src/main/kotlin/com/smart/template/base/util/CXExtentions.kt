@file:Suppress("unused")

package com.smart.template.base.util

import com.google.common.base.CaseFormat

inline fun <reified T : Any> String.parse(): T? = CXJsonUtil.parse(this)
fun Any.toJSON(): String = CXJsonUtil.toJSON(this)

/**
 * hello_world -> helloWorld
 */
fun String.toLowerCamelFromLowerUnderScore(): String = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this)

/**
 * helloWorld -> hello_world
 */
fun String.toLowerUnderScoreFromLowerCamel(): String = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this)

/**
 * HelloWorld -> hello_world
 */
fun String.toLowerUnderScoreFromUpperCamel(): String = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this)
