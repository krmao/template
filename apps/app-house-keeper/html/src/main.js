// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import router from './router'

import hybird from './assets/common/script/hybird'
import './assets/common/script/hybird-console'

window.hybird = hybird()

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    template: '<App/>',
    components: {App},
    data: {
        message: '[main]'
    },
    beforeCreate: function () {
        console.log('beforeCreate 创建前状态===============》')
        console.log('el     : ' + this.$el) //undefined
        console.log('data   : ' + this.$data) //undefined
        console.log('message: ' + this.message)
    },
    created: function () {
        console.log('created 创建完毕状态===============》')
        console.log('el     : ' + this.$el) //undefined
        console.log('data   : ' + this.$data) //已被初始化
        console.log('message: ' + this.message) //已被初始化
    },
    beforeMount: function () {
        console.log('beforeMount 挂载前状态===============》')
        console.log('el     : ' + (this.$el)) //已被初始化
        console.log(this.$el)
        console.log('data   : ' + this.$data) //已被初始化
        console.log('message: ' + this.message) //已被初始化
    },
    mounted: function () {
        console.log('mounted 挂载结束状态===============》')
        console.log('el     : ' + this.$el) //已被初始化
        console.log(this.$el)
        console.log('data   : ' + this.$data) //已被初始化
        console.log('message: ' + this.message) //已被初始化
    },
    beforeUpdate: function () {
        console.log('beforeUpdate 更新前状态===============》')
        console.log('el     : ' + this.$el)
        console.log(this.$el)
        console.log('data   : ' + this.$data)
        console.log('message: ' + this.message)
    },
    updated: function () {
        console.log('updated 更新完成状态===============》')
        console.log('el     : ' + this.$el)
        console.log(this.$el)
        console.log('data   : ' + this.$data)
        console.log('message: ' + this.message)
    },
    beforeDestroy: function () {
        console.log('beforeDestroy 销毁前状态===============》')
        console.log('el     : ' + this.$el)
        console.log(this.$el)
        console.log('data   : ' + this.$data)
        console.log('message: ' + this.message)
    },
    destroyed: function () {
        console.log('destroyed 销毁完成状态===============》')
        console.log('el     : ' + this.$el)
        console.log(this.$el)
        console.log('data   : ' + this.$data)
        console.log('message: ' + this.message)
    }
})

