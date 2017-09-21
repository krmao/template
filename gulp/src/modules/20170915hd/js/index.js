new Vue({
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
