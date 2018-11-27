<template>
  <div class="container" @click="clickHandle('test click', $event)">

    <div class="userinfo" @click="bindViewTap">
      <img class="userinfo-avatar" v-if="userInfo.avatarUrl" :src="userInfo.avatarUrl" background-size="cover"/>
      <div class="userinfo-nickname">
        <card :text="userInfo.nickName"></card>
      </div>
    </div>

    <div class="usermotto">
      <div class="user-motto">
        <card :text="motto"></card>
      </div>
    </div>

    <form class="form-container">
      <input type="text" class="form-control" v-model="motto" placeholder="v-model"/>
      <input type="text" class="form-control" v-model.lazy="motto" placeholder="v-model.lazy"/>
    </form>
    <a href="/pages/counter/main" class="counter">去往Vuex示例页面</a>
  </div>
</template>

<script>
  import card from '@/components/card'

  export default {
    data () {
      return {
        motto: 'Hello World',
        userInfo: {}
      }
    },

    components: {
      card
    },

    methods: {
      bindViewTap () {
        const url = '../logs/main'
        wx.navigateTo({url})
      },
      getUserInfo () {
        // 调用登录接口
        wx.login({
          success: () => {
            wx.getUserInfo({
              success: (res) => {
                this.userInfo = res.userInfo
              }
            })
          }
        })
      },
      clickHandle (msg, ev) {
        console.log('clickHandle:', msg, ev)
      }
    },

    // vue 生命周期 ========================================================================================================
    beforeCreate () { console.log('vue [page-index] beforeCreate') },
    created () {
      console.log('vue [page-index] created')
      this.getUserInfo() // 调用应用实例的方法获取全局数据
    },
    beforeMount () { console.log('vue [page-index] beforeMount') },
    mounted () { console.log('vue [page-index] mounted') },
    beforeUpdate () { console.log('vue [page-index] beforeUpdate') },
    updated () { console.log('vue [page-index] updated') },
    activated () { console.log('vue [page-index] activated, mpvue 实际运行并没有回调, 原因不支持 keep-alive') },
    deactivated () { console.log('vue [page-index] deactivated, mpvue 实际运行并没有回调, 原因不支持 keep-alive') },
    beforeDestroy () { console.log('vue [page-index] beforeDestroy, mpvue 实际运行并没有回调, bug') },
    destroyed () { console.log('vue [page-index] destroyed, mpvue 实际运行并没有回调, bug') },
    // vue 生命周期 ========================================================================================================

    // 小程序自有生命周期 ====================================================================================================
    onLoad () { console.log('小程序 [page-index] 生命周期: onLoad: 监听页面加载') },
    onShow () { console.log('小程序 [page-index] 生命周期: onShow: 监听页面显示') },
    onReady () { console.log('小程序 [page-index] 生命周期: onReady: 监听页面初次渲染完成') },
    onHide () { console.log('小程序 [page-index] 生命周期: onHide: 监听页面隐藏') },
    onUnload () { console.log('小程序 [page-index] 生命周期: onUnload: 监听页面卸载') },
    onPullDownRefresh () { console.log('小程序 [page-index] 生命周期: onPullDownRefresh: 监听用户下拉动作') },
    onReachBottom () { console.log('小程序 [page-index] 生命周期: onReachBottom: 页面上拉触底事件的处理函数') },
    onShareAppMessage () { console.log('小程序 [page-index] 生命周期: onShareAppMessage: 用户点击右上角分享') },
    onPageScroll () { console.log('小程序 [page-index] 生命周期: onPageScroll: 页面滚动') },
    onResize () { console.log('小程序 [page-index] 生命周期: onResize: 宽高改变') },
    onTabItemTap (item) { console.log('小程序 [page-index] 生命周期: onTabItemTap: 当前是 tab 页时，点击 tab 时触发 （mpvue 0.0.16 支持）') }
    // 小程序自有生命周期 ====================================================================================================
  }
</script>

<style scoped>
  .userinfo {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .userinfo-avatar {
    width: 128rpx;
    height: 128rpx;
    margin: 20rpx;
    border-radius: 50%;
  }

  .userinfo-nickname {
    color: #aaa;
  }

  .usermotto {
    margin-top: 150px;
  }

  .form-control {
    display: block;
    padding: 0 12px;
    margin-bottom: 5px;
    border: 1px solid #ccc;
  }

  .counter {
    display: inline-block;
    margin: 10px auto;
    padding: 5px 10px;
    color: blue;
    border: 1px solid blue;
  }
</style>
