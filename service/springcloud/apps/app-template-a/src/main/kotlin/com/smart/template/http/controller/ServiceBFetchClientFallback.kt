package com.smart.template.http.controller


class ServiceBFetchClientFallback : ServiceBFetchClient {
    override fun testBController(): String? {
        return "ServiceBFetchClientFallback"
    }
}
