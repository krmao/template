<template>
  <div class="counter-warp">
    <p>Vuex counter：{{ count }}</p>
    <p>
      <button @click="increment">+</button>
      <button @click="decrement">-</button>
    </p>

    <a href="/pages/index/main" class="home">去往首页</a>
  </div>
</template>

<script>
// Use Vuex
import store from './store'

export default {
  computed: {
    count () {
      return store.state.count
    }
  },
  methods: {
    increment () {
      store.commit('increment')
    },
    decrement () {
      store.commit('decrement')
    }
  },
  // vue 生命周期 ========================================================================================================
  beforeCreate () { console.log('vue [page-counter] beforeCreate') },
  created () { console.log('vue [page-counter] created') },
  beforeMount () { console.log('vue [page-counter] beforeMount') },
  mounted () { console.log('vue [page-counter] mounted') },
  beforeUpdate () { console.log('vue [page-counter] beforeUpdate') },
  updated () { console.log('vue [page-counter] updated') },
  activated () { console.log('vue [page-counter] activated, mpvue 实际运行并没有回调, 原因不支持 keep-alive') },
  deactivated () { console.log('vue [page-counter] deactivated, mpvue 实际运行并没有回调, 原因不支持 keep-alive') },
  beforeDestroy () { console.log('vue [page-counter] beforeDestroy, mpvue 实际运行并没有回调, bug') },
  destroyed () { console.log('vue [page-counter] destroyed, mpvue 实际运行并没有回调, bug') },
  // vue 生命周期 ========================================================================================================

  // 小程序自有生命周期 ====================================================================================================
  onLoad () { console.log('小程序 [page-counter] 生命周期: onLoad: 监听页面加载') },
  onShow () { console.log('小程序 [page-counter] 生命周期: onShow: 监听页面显示') },
  onReady () { console.log('小程序 [page-counter] 生命周期: onReady: 监听页面初次渲染完成') },
  onHide () { console.log('小程序 [page-counter] 生命周期: onHide: 监听页面隐藏') },
  onUnload () { console.log('小程序 [page-counter] 生命周期: onUnload: 监听页面卸载') },
  onPullDownRefresh () { console.log('小程序 [page-counter] 生命周期: onPullDownRefresh: 监听用户下拉动作') },
  onReachBottom () { console.log('小程序 [page-counter] 生命周期: onReachBottom: 页面上拉触底事件的处理函数') },
  onShareAppMessage () { console.log('小程序 [page-counter] 生命周期: onShareAppMessage: 用户点击右上角分享') },
  onPageScroll () { console.log('小程序 [page-counter] 生命周期: onPageScroll: 页面滚动') },
  onResize () { console.log('小程序 [page-counter] 生命周期: onResize: 宽高改变') },
  onTabItemTap (item) { console.log('小程序 [page-counter] 生命周期: onTabItemTap: 当前是 tab 页时，点击 tab 时触发 （mpvue 0.0.16 支持）') }
  // 小程序自有生命周期 ====================================================================================================

}
</script>

<style>
.counter-warp {
  text-align: center;
  margin-top: 100px;
}
.home {
  display: inline-block;
  margin: 100px auto;
  padding: 5px 10px;
  color: blue;
  border: 1px solid blue;
}
</style>
