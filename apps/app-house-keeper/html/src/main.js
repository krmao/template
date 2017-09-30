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
        message: '[VUE]'
    },
    beforeCreate: function () {
        console.log('[VUE]beforeCreate 创建前状态===============》')
        console.log('[VUE]el     : ' + this.$el) //undefined
        console.log('[VUE]data   : ' + this.$data) //undefined
        console.log('[VUE]message: ' + this.message)
    },
    created: function () {
        console.log('[VUE]created 创建完毕状态===============》')
        console.log('[VUE]el     : ' + this.$el) //undefined
        console.log('[VUE]data   : ' + this.$data) //已被初始化
        console.log('[VUE]message: ' + this.message) //已被初始化
    },
    beforeMount: function () {
        console.log('[VUE]beforeMount 挂载前状态===============》')
        console.log('[VUE]el     : ' + (this.$el)) //已被初始化
        console.log(this.$el)
        console.log('[VUE]data   : ' + this.$data) //已被初始化
        console.log('[VUE]message: ' + this.message) //已被初始化
    },
    mounted: function () {
        console.log('[VUE]mounted 挂载结束状态===============》')
        console.log('[VUE]el     : ' + this.$el) //已被初始化
        console.log(this.$el)
        console.log('[VUE]data   : ' + this.$data) //已被初始化
        console.log('[VUE]message: ' + this.message) //已被初始化
    },
    beforeUpdate: function () {
        console.log('[VUE]beforeUpdate 更新前状态===============》')
        console.log('[VUE]el     : ' + this.$el)
        console.log(this.$el)
        console.log('[VUE]data   : ' + this.$data)
        console.log('[VUE]message: ' + this.message)
    },
    updated: function () {
        console.log('[VUE]updated 更新完成状态===============》')
        console.log('[VUE]el     : ' + this.$el)
        console.log(this.$el)
        console.log('[VUE]data   : ' + this.$data)
        console.log('[VUE]message: ' + this.message)
    },
    beforeDestroy: function () {
        console.log('[VUE]beforeDestroy 销毁前状态===============》')
        console.log('[VUE]el     : ' + this.$el)
        console.log(this.$el)
        console.log('[VUE]data   : ' + this.$data)
        console.log('[VUE]message: ' + this.message)
    },
    destroyed: function () {
        console.log('[VUE]destroyed 销毁完成状态===============》')
        console.log('[VUE]el     : ' + this.$el)
        console.log(this.$el)
        console.log('[VUE]data   : ' + this.$data)
        console.log('[VUE]message: ' + this.message)
    }
})

