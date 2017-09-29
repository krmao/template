// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import router from './router'

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
        console.group('beforeCreate 创建前状态===============》')
        console.log('%c%s', 'color:red', 'el     : ' + this.$el) //undefined
        console.log('%c%s', 'color:red', 'data   : ' + this.$data) //undefined
        console.log('%c%s', 'color:red', 'message: ' + this.message)
    },
    created: function () {
        console.group('created 创建完毕状态===============》')
        console.log('%c%s', 'color:red', 'el     : ' + this.$el) //undefined
        console.log('%c%s', 'color:red', 'data   : ' + this.$data) //已被初始化
        console.log('%c%s', 'color:red', 'message: ' + this.message) //已被初始化
    },
    beforeMount: function () {
        console.group('beforeMount 挂载前状态===============》')
        console.log('%c%s', 'color:red', 'el     : ' + (this.$el)) //已被初始化
        console.log(this.$el)
        console.log('%c%s', 'color:red', 'data   : ' + this.$data) //已被初始化
        console.log('%c%s', 'color:red', 'message: ' + this.message) //已被初始化
    },
    mounted: function () {
        console.group('mounted 挂载结束状态===============》')
        console.log('%c%s', 'color:red', 'el     : ' + this.$el) //已被初始化
        console.log(this.$el)
        console.log('%c%s', 'color:red', 'data   : ' + this.$data) //已被初始化
        console.log('%c%s', 'color:red', 'message: ' + this.message) //已被初始化
    },
    beforeUpdate: function () {
        console.group('beforeUpdate 更新前状态===============》')
        console.log('%c%s', 'color:red', 'el     : ' + this.$el)
        console.log(this.$el)
        console.log('%c%s', 'color:red', 'data   : ' + this.$data)
        console.log('%c%s', 'color:red', 'message: ' + this.message)
    },
    updated: function () {
        console.group('updated 更新完成状态===============》')
        console.log('%c%s', 'color:red', 'el     : ' + this.$el)
        console.log(this.$el)
        console.log('%c%s', 'color:red', 'data   : ' + this.$data)
        console.log('%c%s', 'color:red', 'message: ' + this.message)
    },
    beforeDestroy: function () {
        console.group('beforeDestroy 销毁前状态===============》')
        console.log('%c%s', 'color:red', 'el     : ' + this.$el)
        console.log(this.$el)
        console.log('%c%s', 'color:red', 'data   : ' + this.$data)
        console.log('%c%s', 'color:red', 'message: ' + this.message)
    },
    destroyed: function () {
        console.group('destroyed 销毁完成状态===============》')
        console.log('%c%s', 'color:red', 'el     : ' + this.$el)
        console.log(this.$el)
        console.log('%c%s', 'color:red', 'data   : ' + this.$data)
        console.log('%c%s', 'color:red', 'message: ' + this.message)
    }
})
