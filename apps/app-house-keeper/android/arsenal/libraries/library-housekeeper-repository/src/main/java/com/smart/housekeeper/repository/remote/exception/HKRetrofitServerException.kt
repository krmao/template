package com.smart.housekeeper.repository.remote.exception

class HKRetrofitServerException(val code: Int, val msg: String) : RuntimeException()
