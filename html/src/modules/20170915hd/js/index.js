let women = {template: '<div id=""><img id="women" src="../../static/images/20170915hd/women.jpeg"></div>'};
let dog = {template: '<div><img id="dog" src="../../static/images/20170915hd/dog.gif"></div>'};
let routes = [{path: '/women', component: women}, {path: '/dog', component: dog}];
let router = new VueRouter({routes}); //（缩写）相当于 routes: routes

new Vue({
    router,
    el: '#myApp',
    mixins: [commonObj.commonMixin()],
    data: {
        basicInfo: {
            showBackTopFlag: false
        },
    },
    beforeCreate: function () {
        document.title = 'template';
    },
    created: function () {
    },
    mounted: function () {
    }
});
