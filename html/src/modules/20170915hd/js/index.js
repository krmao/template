let women = {template: '<div id=""><img id="women" src="../../static/images/20170915hd/women.jpeg"></div>'};
let dog = {template: '<div><img id="dog" src="../../static/images/20170915hd/dog.gif"></div>'};
let routes = [{path: '/women', component: women}, {path: '/dog', component: dog}];
let router = new VueRouter({routes}); //（缩写）相当于 routes: routes

function initSB(_window) {
    _window.onBackPressed = function () {
        console.log("[html] onBackPressed()");
    };

    _window.onResume = function () {
        console.log("[html] onResume()");
    };

    _window.onPause = function () {
        console.log("[html] onPause()");
    };

    _window.onResult = function (dataString) {
        console.log("[html] onResult():dataString=" + dataString);
    };

    _window.onNetworkStateChanged = function (available) {
        console.log("[html] onNetworkStateChanged():available=" + available);
    };

    /**
     * app is visible
     * @param visible true/false
     */
    _window.onApplicationVisibleChanged = function (visible) {
        console.log("[html] onApplicationVisibleChanged():visible=" + visible);
    };
}


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


        initSB(window)
    },
    mounted: function () {
    }
});
