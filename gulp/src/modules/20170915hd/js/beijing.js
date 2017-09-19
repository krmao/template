new Vue({
    el: '#myApp',
    mixins: [commonObj.commonMixin()],
    data: {
        basicInfo: {
            showShareFlag: false,
            showBackTopFlag: false
        },
        buyUrl: "#"
    },
    beforeCreate: function () {
        document.title = 'template';
    },
    created: function () {
        var queryObj = url('?');
        var userInfo = queryObj && queryObj.userInfo || '';
        this.buyUrl += '?userInfo=' + userInfo;
    },
    mounted: function () {
    }
});
