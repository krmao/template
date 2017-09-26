'use strict';

new Vue({
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