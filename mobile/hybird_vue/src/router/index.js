import Vue from "vue";
import Router from "vue-router";
import TestHybirdPage from "@/components/TestHybirdPage";

Vue.use(Router);

let router = new Router({
    mode: "hash", //hash abstract
    // scrollBehavior(to, from, savedPosition) {
    //     return savedPosition || {x: 0, y: 0}
    // },
    routes: [
        {
            path: "/",
            name: "test",
            component: TestHybirdPage
            // beforeEnter: function (to, from, next) {
            //     console.log("[routerA]:beforeEnter:to", to)
            //     console.log("[routerA]:beforeEnter:from", from)
            //     next()
            // },
        },
        {
            path: "/other",
            beforeEnter: (to, from, next) => {
                window.location = "https://www.baidu.com";
            }
        }
    ]
});

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

export default router;
