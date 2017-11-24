package com.smart.library.util

import java.util.concurrent.*


@Suppress("unused")
open class HKThreadPoolManager {

    private var blockingQueue: BlockingQueue<Runnable>
    private var executorService: ThreadPoolExecutor

    /**
     * 单线程池(无界队列)SINGLE_THREADPOOL
     */
    constructor() {
        blockingQueue = LinkedBlockingQueue<Runnable>()
        executorService = ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, blockingQueue)
    }

    /**
     * 固定大小线程池(有界界队列)FIXED_THREAD_POOL
     *
     * @param corePoolSize    核心数量(5)
     * @param maximumPoolSize 最大数量(10)
     * @param keepAliveTime   线程(corePoolSize<which></which><=maximumPoolSize)维持活着的时间(60s)
     */
    constructor(corePoolSize: Int, maximumPoolSize: Int, queueSize: Int, keepAliveTime: Long) {
        blockingQueue = ArrayBlockingQueue<Runnable>(queueSize, true)
        executorService = ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, blockingQueue)
    }

    /**
     * isSyncQueue==true p v 各位1的生产者与消费者模式 isSyncQueue==false 无界线程池
     */
    constructor(corePoolSize: Int, isSyncQueue: Boolean) {
        if (isSyncQueue) {
            blockingQueue = SynchronousQueue<Runnable>()
            executorService = ThreadPoolExecutor(0, Integer.MAX_VALUE, 0L, TimeUnit.MILLISECONDS, blockingQueue)
        } else {
            blockingQueue = LinkedBlockingQueue<Runnable>()
            executorService = ThreadPoolExecutor(corePoolSize, corePoolSize, 0L, TimeUnit.MILLISECONDS, blockingQueue)
        }

    }

    fun addCommand(runnable: Runnable) = executorService.execute(runnable)
}
