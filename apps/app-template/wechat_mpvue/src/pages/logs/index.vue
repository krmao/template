<template>
  <div>
    <ul class="container log-list">
      <li v-for="(log, index) in logs" :class="{ red: aa }" :key="index" class="log-item">
        <card :text="(index + 1) + ' . ' + log"></card>
      </li>
    </ul>
  </div>
</template>

<script>
import { formatTime } from '@/utils/index'
import card from '@/components/card'

export default {
  components: {
    card
  },

  data () {
    return {
      logs: []
    }
  },

  created () {
    const logs = (wx.getStorageSync('logs') || [])
    this.logs = logs.map(log => formatTime(new Date(log)))
  },

  // 小程序自有生命周期 ====================================================================================================
  onLoad () { console.log('小程序 [page-logs] 生命周期: onLoad: 监听页面加载') },
  onShow () { console.log('小程序 [page-logs] 生命周期: onShow: 监听页面显示') },
  onReady () { console.log('小程序 [page-logs] 生命周期: onReady: 监听页面初次渲染完成') },
  onHide () { console.log('小程序 [page-logs] 生命周期: onHide: 监听页面隐藏') },
  onUnload () { console.log('小程序 [page-logs] 生命周期: onUnload: 监听页面卸载') },
  onPullDownRefresh () { console.log('小程序 [page-logs] 生命周期: onPullDownRefresh: 监听用户下拉动作') },
  onReachBottom () { console.log('小程序 [page-logs] 生命周期: onReachBottom: 页面上拉触底事件的处理函数') },
  onShareAppMessage () { console.log('小程序 [page-logs] 生命周期: onShareAppMessage: 用户点击右上角分享') },
  onPageScroll () { console.log('小程序 [page-logs] 生命周期: onPageScroll: 页面滚动') },
  onTabItemTap () { console.log('小程序 [page-logs] 生命周期: onTabItemTap: 当前是 tab 页时，点击 tab 时触发 （mpvue 0.0.16 支持）') }
  // 小程序自有生命周期 ====================================================================================================

}
</script>

<style>
.log-list {
  display: flex;
  flex-direction: column;
  padding: 40rpx;
}

.log-item {
  margin: 10rpx;
}
</style>
