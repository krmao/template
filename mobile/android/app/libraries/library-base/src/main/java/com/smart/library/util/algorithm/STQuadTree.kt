package com.smart.library.util.algorithm

import androidx.annotation.Keep
import java.util.*

/**
 * QuadTree ['kwɔdtri:] 四叉树
 *
 * A quad tree which tracks items with a Point geometry.
 * See http://en.wikipedia.org/wiki/Quadtree for details on the data structure.
 * See https://gamedevelopment.tutsplus.com/tutorials/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space--gamedev-374 for more details.
 * This class is not thread safe.
 *
 * 特征:
 * 1. 所有的数据都存放在叶子节点中
 */
//@Keep
@Suppress("unused")
class STQuadTree<T : STQuadTree.Item> @JvmOverloads constructor(
    private val bounds: Bounds,                                 // The bounds of this quad. 当前节点占用的 2D 空间/范围
    private val depth: Int = 0                                  // The depth of this quad in the tree. 当前节点的深度/级别
) {
    private var items: MutableList<T>? = null                   // The elements inside this quad, if any. 尚未拆分/分割的当前节点容纳的对象列表
    private var children: MutableList<STQuadTree<T>>? = null    // Child quads. 当前节点的四个子节点

    @JvmOverloads
    constructor(minX: Double, maxX: Double, minY: Double, maxY: Double, depth: Int = 0) : this(Bounds(minX, maxX, minY, maxY), depth)

    //region add/insert an item
    /**
     * Insert an item. 在当前节点添加一条数据, 如果已拆分, 添加到子节点中, 如果未拆分, 添加到当前节点中(添加后如果符合拆分条件, 则拆分并重新添加所有数据到四个子节点中)
     */
    fun add(item: T) {
        val point = item.getPoint()
        if (bounds.contains(point.x, point.y)) {
            insert(point.x, point.y, item)
        }
    }

    private fun insert(x: Double, y: Double, item: T) {
        val tmpChildren = children
        if (tmpChildren != null) {
            if (y < bounds.midY) {
                if (x < bounds.midX) {  // top left
                    tmpChildren[0].insert(x, y, item)
                } else {                // top right
                    tmpChildren[1].insert(x, y, item)
                }
            } else {
                if (x < bounds.midX) {  // bottom left
                    tmpChildren[2].insert(x, y, item)
                } else {
                    tmpChildren[3].insert(x, y, item)
                }
            }
            return
        }

        //region 添加到尚未分割当前节点
        val tmpItems = items ?: ArrayList()
        tmpItems.add(item)
        items = tmpItems
        //endregion

        //region 检查当前节点是否需要拆分/分割, 如果需要, 重新添加 items 到四个子节点, 并清空当前节点 items
        if (tmpItems.size > MAX_ELEMENTS && depth < MAX_DEPTH) {
            split()
        }
        //endregion
    }

    /**
     * Split this quad.
     */
    private fun split() {
        //region 拆分当前节点
        children = ArrayList<STQuadTree<T>>(4).also {
            it.add(STQuadTree(bounds.minX, bounds.midX, bounds.minY, bounds.midY, depth + 1))
            it.add(STQuadTree(bounds.midX, bounds.maxX, bounds.minY, bounds.midY, depth + 1))
            it.add(STQuadTree(bounds.minX, bounds.midX, bounds.midY, bounds.maxY, depth + 1))
            it.add(STQuadTree(bounds.midX, bounds.maxX, bounds.midY, bounds.maxY, depth + 1))
        }
        //endregion

        //region 重新添加 items 到四个子节点, 并清空当前节点 items
        val tmpItems: List<T>? = items
        items = null
        if (tmpItems != null) {
            for (item in tmpItems) {
                // re-insert items into child quads.
                insert(item.getPoint().x, item.getPoint().y, item)
            }
        }
        //endregion
    }
    //endregion

    //region remove/clear
    /**
     * Remove the given item from the set. 删除一条数据, 如果已经拆分, 在四个子节点中删除, 否则在当前节点删除
     *
     * @return whether the item was removed.
     */
    fun remove(item: T): Boolean {
        val point = item.getPoint()
        return if (bounds.contains(point.x, point.y)) {
            remove(point.x, point.y, item)
        } else {
            false
        }
    }

    private fun remove(x: Double, y: Double, item: T): Boolean {
        val tmpChildren = children
        return if (tmpChildren != null) {
            if (y < bounds.midY) {
                if (x < bounds.midX) {  // top left
                    tmpChildren[0].remove(x, y, item)
                } else {                // top right
                    tmpChildren[1].remove(x, y, item)
                }
            } else {
                if (x < bounds.midX) {  // bottom left
                    tmpChildren[2].remove(x, y, item)
                } else {
                    tmpChildren[3].remove(x, y, item)
                }
            }
        } else {
            items?.remove(item) ?: false
        }
    }

    /**
     * Removes all points from the quadTree. 清空当前节点, 删除所有子节点, 清空容纳的对象
     */
    fun clear() {
        children = null
        items?.clear()
    }
    //endregion

    //region search
    /**
     * Search for all items within a given bounds. 则当前节点(包含所有子节点)中查找所有在搜索范围内的数据
     * @param searchBounds 搜索范围
     */
    fun search(searchBounds: Bounds): Collection<T> {
        val results: MutableList<T> = ArrayList()
        search(searchBounds, results)
        return results
    }

    private fun search(searchBounds: Bounds, results: MutableCollection<T>) {
        //region 两个矩形范围不重叠, 返回
        if (!bounds.intersects(searchBounds)) {
            return
        }
        //endregion
        val tmpChildren = children
        val tmpItems = items

        if (tmpChildren != null) {
            // 如果四个子节点不为空, 则 items 为空, 当前节点所有容纳的数据都在四个子节点中, 只需要在四个子节点中查找
            for (quad in tmpChildren) {
                quad.search(searchBounds, results)
            }
        } else if (tmpItems != null) {
            // 如果四个子节点为空, 则 items 不为空, 当前节点所有容纳的数据都在 items 中, 只需要在当前节点中查找
            if (searchBounds.contains(bounds)) {
                // 搜索范围包含整个当前节点的范围, 则当前节点的所有数据都符合条件
                results.addAll(tmpItems)
            } else {
                // 搜索范围不包含整个当前节点的范围, 但是有部分区域相重叠, 则遍历查询在搜索范围内的数据
                for (item in tmpItems) {
                    if (searchBounds.contains(item.getPoint())) {
                        results.add(item)
                    }
                }
            }
        }
    }
    //endregion

    //region 工具类
    /**
     * Represents an area in the cartesian plane. 面积/范围
     */
    @Suppress("MemberVisibilityCanBePrivate")
    //@Keep
    class Bounds(val minX: Double, val maxX: Double, val minY: Double, val maxY: Double) {

        val midX: Double = (minX + maxX) / 2
        val midY: Double = (minY + maxY) / 2

        fun contains(x: Double, y: Double): Boolean {
            return x in minX..maxX && y in minY..maxY
        }

        operator fun contains(point: Point): Boolean {
            return contains(point.x, point.y)
        }

        operator fun contains(bounds: Bounds): Boolean {
            return bounds.minX >= minX && bounds.maxX <= maxX && bounds.minY >= minY && bounds.maxY <= maxY
        }

        /**
         * 范围是否重叠
         */
        fun intersects(minX: Double, maxX: Double, minY: Double, maxY: Double): Boolean {
            return minX < this.maxX && this.minX < maxX && minY < this.maxY && this.minY < maxY
        }

        /**
         * [ˌɪntərˈsekts] 范围是否重叠
         */
        @Suppress("KDocUnresolvedReference")
        fun intersects(bounds: Bounds): Boolean {
            return intersects(bounds.minX, bounds.maxX, bounds.minY, bounds.maxY)
        }

        override fun toString(): String {
            return "Bounds{(minX=$minX, minY=$minY), (maxX=$maxX ,maxY=$maxY)}"
        }
    }

    /**
     * 坐标
     */
    //@Keep
    class Point(val x: Double, val y: Double) {
        override fun toString(): String {
            return "Point{x=$x, y=$y}"
        }
    }

    interface Item {
        fun getPoint(): Point
    }
    //endregion

    companion object {
        private const val MAX_ELEMENTS = 50                     // Maximum number of elements to store in a quad before splitting.  尚未拆分/分割之前, 一个节点最多可以容纳多少个对象, 超过这个数则必须拆分/分割
        private const val MAX_DEPTH = 40                        // Maximum depth. 整颗四叉树最多可以拆分/分割为多少层
    }
}