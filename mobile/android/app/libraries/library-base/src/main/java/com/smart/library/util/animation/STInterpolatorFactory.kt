package com.smart.library.util.animation

import android.view.animation.*
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

/**
 * https://blog.csdn.net/pzm1993/article/details/77926373
 * https://www.jianshu.com/p/639efa7762a1
 */
@Suppress("unused")
object STInterpolatorFactory {

    /**
     * 从 0f -> 1f 的过程是 匀速
     */
    fun createLinearInterpolator(): LinearInterpolator = LinearInterpolator()

    /**
     * 从 0f -> 1f 的过程是 加速, 即初始速度较慢, 速度越来越快, 末尾速度较快
     * @param factor 加速度因子, 值越大起始速度越慢, 但越来越快, 加速过程比较明显
     */
    fun createAccelerateInterpolator(factor: Float = 1f): AccelerateInterpolator = AccelerateInterpolator(factor)

    /**
     * 从 0f -> 1f 的过程是 减速, 即初始速度较大, 速度越来越慢, 末尾速度较慢
     * @param factor 加速度因子, 值越大起始速度越快, 但越来越慢, 减速过程比较明显
     */
    fun createDecelerateInterpolator(factor: Float = 1f): DecelerateInterpolator = DecelerateInterpolator(factor)

    /**
     * 从 0f -> 1f 的过程是 先加速再减速, 即初始速度较慢, 中间速度较快, 末尾速度较慢
     */
    fun createAccelerateDecelerateInterpolator(): AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()

    /**
     * 从 0f -> 1f 的过程是 先加速再减速
     */
    fun createFastOutSlowInInterpolator(): FastOutSlowInInterpolator = FastOutSlowInInterpolator()

    /**
     * 从 0f -> 1f 的过程是 先加速然后匀速
     */
    fun createFastOutLinearInInterpolator(): FastOutLinearInInterpolator = FastOutLinearInInterpolator()

    /**
     * 从 0f -> 1f 的过程是 先匀速再减速
     */
    fun createLinearOutSlowInInterpolator(): LinearOutSlowInInterpolator = LinearOutSlowInInterpolator()

    /**
     * 从 0f -> 1f 的过程是 起点反向越界回弹, 即初始速度为负减速, 反向越界, 然后回弹加速直至终点
     * @param tension 默认为2, 张力越大, 越界的距离越大
     */
    fun createAnticipateInterpolator(tension: Float = 2f): AnticipateInterpolator = AnticipateInterpolator(tension)

    /**
     * 从 0f -> 1f 的过程是 终点越界回弹, 即初始速度较快, 逐渐减速至越界, 然后往回弹至终点
     * @param tension 默认为2, 张力越大, 越界的距离越大
     */
    fun createOvershootInterpolator(tension: Float = 2f): OvershootInterpolator = OvershootInterpolator(tension)

    /**
     * 从 0f -> 1f 的过程是 起点和终点都会越界回弹, 即初始速度为负减速, 反向越界, 然后回弹加速直至终点越界, 然后再往回弹减速直至终点
     * @param tension 默认为2, 张力越大, 越界的距离越大
     */
    fun createAnticipateOvershootInterpolator(tension: Float = 2f): AnticipateOvershootInterpolator = AnticipateOvershootInterpolator(tension)

    /**
     * 从 0f -> 1f 的过程是 小球落地弹跳直至终止, 即减速至终点越界, 回弹减速至终点内, 再回弹减速至终点外, 再回弹直至停止在终点位置
     */
    fun createBounceInterpolator(): BounceInterpolator = BounceInterpolator()

    /**
     * 从 0f -> 1f 的过程是 正弦函数, 放大至终点, 然后缩小直至负终点, 如此往复直至恢复初始形状, 放大->缩小->放大->缩小-> ... ->放大->原来
     * @param cycles 循环次数
     */
    fun createCycleInterpolator(cycles: Float = 1f): CycleInterpolator = CycleInterpolator(cycles)

}