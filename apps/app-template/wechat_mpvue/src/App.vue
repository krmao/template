import Vue from 'vue'
<script>
  export default {
    // vue 生命周期 ========================================================================================================
    beforeCreate () { console.log('vue [app] beforeCreate') },
    created () {
      console.log('vue [app] created')
      // 调用API从本地缓存中获取数据
      const logs = wx.getStorageSync('logs') || []
      logs.unshift(Date.now())
      wx.setStorageSync('logs', logs)
    },
    beforeMount () { console.log('vue [app] beforeMount') },
    mounted () { console.log('vue [app] mounted') },
    beforeUpdate () { console.log('vue [app] beforeUpdate') },
    updated () { console.log('vue [app] updated') },
    activated () { console.log('vue [app] activated, mpvue 实际运行并没有回调, 原因不支持 keep-alive') },
    deactivated () { console.log('vue [app] deactivated, mpvue 实际运行并没有回调, 原因不支持 keep-alive') },
    beforeDestroy () { console.log('vue [app] beforeDestroy, mpvue 实际运行并没有回调, bug') },
    destroyed () { console.log('vue [app] destroyed, mpvue 实际运行并没有回调, bug') },
    // vue 生命周期 ========================================================================================================

    // 小程序自有生命周期 ====================================================================================================
    onLaunch () { console.log('小程序 [app] 生命周期: onLaunch: 小程序初始化') },
    onShow () { console.log('小程序 [app] 生命周期: onShow: 当小程序启动，或从后台进入前台显示') },
    onHide () { console.log('小程序 [app] 生命周期: onHide: 当小程序从前台进入后台') }
    // 小程序自有生命周期 ====================================================================================================
  }
</script>

<style>
  .container {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    padding: 200rpx 0;
    box-sizing: border-box;
  }

  /* this rule will be remove */
  * {
    transition: width 2s;
    -moz-transition: width 2s;
    -webkit-transition: width 2s;
    -o-transition: width 2s;
  }
</style>
