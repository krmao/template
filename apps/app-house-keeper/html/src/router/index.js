import Vue from 'vue'
import Router from 'vue-router'
import A from '@/components/A'
import B from '@/components/B/B'

Vue.use(Router)

let router = new Router({
    mode: 'hash',//hash abstract
    // scrollBehavior(to, from, savedPosition) {
    //     return savedPosition || {x: 0, y: 0}
    // },
    routes: [
        {
            path: '/',
            name: 'A',
            component: A,
            // beforeEnter: function (to, from, next) {
            //     console.log("[routerA]:beforeEnter:to", to)
            //     console.log("[routerA]:beforeEnter:from", from)
            //     next()
            // },
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
    ]
})

// router.beforeEach((to, from, next) => {
// console.log("[router2]:beforeEach:to", to)
// console.log("[router2]:beforeEach:from", from)
// next()
// })

// router.beforeResolve((to, from, next) => {
// console.log("[router2]:beforeResolve:to", to)
// console.log("[router2]:beforeResolve:from", from)
// next()
// })
// router.afterEach((to, from) => {
// console.log("[router2]:afterEach:to", to)
// console.log("[router2]:afterEach:from", from)
//     to.matched[0].components.default.methods.onGoBack()
// })

export default router
