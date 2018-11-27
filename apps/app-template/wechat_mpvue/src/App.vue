import Vue from 'vue'
<script>
  export default {
    created () {
      // 调用API从本地缓存中获取数据
      const logs = wx.getStorageSync('logs') || []
      logs.unshift(Date.now())
      wx.setStorageSync('logs', logs)

      console.log('app created and cache logs by setStorageSync')
    },

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
