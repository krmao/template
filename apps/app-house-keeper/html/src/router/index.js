import Vue from 'vue'
import Router from 'vue-router'
import A from '@/components/A'
import B from '@/components/B/B'

Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'A',
            component: A
        },
        {
            path: '/B',
            name: 'B',
            component: B
        },
        {
            path: '/other',
            beforeEnter: (to, from, next) => {
                window.location = "https://c.chexiang.com/promotion/201709xinche.htm?channel=cx_zc_201709_cxyx_h5"
            }
        }
    ],
    beforeEach: function () {

    },
    beforeResolve: function () {

    } ,
    afterEach: function () {

    } ,
    onReady: function () {

    }
    ,
    onError: function () {

    }
    ,
    forward: function () {

    },
    go: function () {

    }, push: function () {

    },
    back: function () {

    }, replace: function () {

    }
})
