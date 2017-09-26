'use strict';

var women = { template: '<div id=""><img id="women" src="../../static/images/20170915hd/women.jpeg"></div>' };
var dog = { template: '<div><img id="dog" src="../../static/images/20170915hd/dog.gif"></div>' };
var routes = [{ path: '/women', component: women }, { path: '/dog', component: dog }];
var router = new VueRouter({ routes: routes });

new Vue({
    router: router,
    el: '#myApp',
    mixins: [commonObj.commonMixin()],
    data: {
        basicInfo: {
            showBackTopFlag: false
        }
    },
    beforeCreate: function beforeCreate() {
        document.title = 'template';
    },
    created: function created() {},
    mounted: function mounted() {}
});