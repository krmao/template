// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import router from './router'

import hybird from './assets/common/script/hybird'
import './assets/common/script/hybird-console'

window.hybird = hybird()

Vue.config.productionTip = false

var fixedWidthString = require('fixed-width-string');

let page = '[VUE]';

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    template: '<App/>',
    components: {App},
    data() {
        return {
            msg: 'VUE',
        }
    },
    beforeCreate: function () {
        console.log(page + '(' + fixedWidthString('beforeCreate', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:undefined?' + (this.msg === undefined))
    },
    created: function () {
        console.log(page + '(' + fixedWidthString('created', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:undefined?' + (this.msg === undefined))
    },
    beforeMount: function () {
        console.log(page + '(' + fixedWidthString('beforeMount', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:undefined?' + (this.msg === undefined))
    },
    mounted: function () {
        console.log(page + '(' + fixedWidthString('mounted', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:undefined?' + (this.msg === undefined))
    },
    beforeUpdate: function () {
        console.log(page + '(' + fixedWidthString('beforeUpdate', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:undefined?' + (this.msg === undefined))
    },
    updated: function () {
        console.log(page + '(' + fixedWidthString('updated', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:undefined?' + (this.msg === undefined))
    },
    beforeDestroy: function () {
        console.log(page + '(' + fixedWidthString('beforeDestroy', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:undefined?' + (this.msg === undefined))
    },
    destroyed: function () {
        console.log(page + '(' + fixedWidthString('destroyed', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:undefined?' + (this.msg === undefined))
    }
})

