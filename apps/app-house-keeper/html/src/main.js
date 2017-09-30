// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import router from './router'

import hybird from './assets/common/script/hybird'
import './assets/common/script/hybird-console'

window.hybird = hybird()

Vue.config.productionTip = false

let page = '[VUE]'

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    template: '<App/>',
    components: {App},
    data() {
        return {
            msg: page,
        }
    },
    beforeCreate: function () {
        console.log(page + 'beforeCreate -------------------->>>>>>>>>>')
        console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
        console.log(page + 'beforeCreate <<<<<<<<<<--------------------')
    },
    created: function () {
        console.log(page + 'created -------------------->>>>>>>>>>')
        console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
        console.log(page + 'created <<<<<<<<<<--------------------')
    },
    beforeMount: function () {
        console.log(page + 'beforeMount -------------------->>>>>>>>>>')
        console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
        console.log(page + 'beforeMount <<<<<<<<<<--------------------')
    },
    mounted: function () {
        console.log(page + 'mounted -------------------->>>>>>>>>>')
        console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
        console.log(page + 'mounted <<<<<<<<<<--------------------')
    },
    beforeUpdate: function () {
        console.log(page + 'beforeUpdate -------------------->>>>>>>>>>')
        console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
        console.log(page + 'beforeUpdate <<<<<<<<<<--------------------')
    },
    updated: function () {
        console.log(page + 'updated -------------------->>>>>>>>>>')
        console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
        console.log(page + 'updated <<<<<<<<<<--------------------')
    },
    beforeDestroy: function () {
        console.log(page + 'beforeDestroy -------------------->>>>>>>>>>')
        console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
        console.log(page + 'beforeDestroy <<<<<<<<<<--------------------')
    },
    destroyed: function () {
        console.log(page + 'destroyed -------------------->>>>>>>>>>')
        console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
        console.log(page + 'destroyed <<<<<<<<<<--------------------')
    }
})

