<template>
    <div class="home">
        <img id="img" src="./image/share.png"/>
    </div>
</template>

<script>
    export default {
        name: 'hello',
        data() {
            return {
                msg: 'page:home',
                message: '[page:home]'
            }
        },
        beforeCreate: function () {
            console.group('beforeCreate 创建前状态===============》')
            console.log('%c%s', 'color:blue', 'el     : ' + this.$el) //undefined
            console.log('%c%s', 'color:blue', 'data   : ' + this.$data) //undefined
            console.log('%c%s', 'color:blue', 'message: ' + this.message)
        },
        created: function () {
            console.group('created 创建完毕状态===============》')
            console.log('%c%s', 'color:blue', 'el     : ' + this.$el) //undefined
            console.log('%c%s', 'color:blue', 'data   : ' + this.$data) //已被初始化
            console.log('%c%s', 'color:blue', 'message: ' + this.message) //已被初始化
        },
        beforeMount: function () {
            console.group('beforeMount 挂载前状态===============》')
            console.log('%c%s', 'color:blue', 'el     : ' + (this.$el)) //已被初始化
            console.log(this.$el)
            console.log('%c%s', 'color:blue', 'data   : ' + this.$data) //已被初始化
            console.log('%c%s', 'color:blue', 'message: ' + this.message) //已被初始化
        },
        mounted: function () {
            console.group('mounted 挂载结束状态===============》')
            console.log('%c%s', 'color:blue', 'el     : ' + this.$el) //已被初始化
            console.log(this.$el)
            console.log('%c%s', 'color:blue', 'data   : ' + this.$data) //已被初始化
            console.log('%c%s', 'color:blue', 'message: ' + this.message) //已被初始化
        },
        beforeUpdate: function () {
            console.group('beforeUpdate 更新前状态===============》')
            console.log('%c%s', 'color:blue', 'el     : ' + this.$el)
            console.log(this.$el)
            console.log('%c%s', 'color:blue', 'data   : ' + this.$data)
            console.log('%c%s', 'color:blue', 'message: ' + this.message)
        },
        updated: function () {
            console.group('updated 更新完成状态===============》')
            console.log('%c%s', 'color:blue', 'el     : ' + this.$el)
            console.log(this.$el)
            console.log('%c%s', 'color:blue', 'data   : ' + this.$data)
            console.log('%c%s', 'color:blue', 'message: ' + this.message)
        },
        beforeDestroy: function () {
            console.group('beforeDestroy 销毁前状态===============》')
            console.log('%c%s', 'color:blue', 'el     : ' + this.$el)
            console.log(this.$el)
            console.log('%c%s', 'color:blue', 'data   : ' + this.$data)
            console.log('%c%s', 'color:blue', 'message: ' + this.message)
        },
        destroyed: function () {
            console.group('destroyed 销毁完成状态===============》')
            console.log('%c%s', 'color:blue', 'el     : ' + this.$el)
            console.log(this.$el)
            console.log('%c%s', 'color:blue', 'data   : ' + this.$data)
            console.log('%c%s', 'color:blue', 'message: ' + this.message)
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
    @import "../../assets/common/style/main.scss";

    .home {
        height: px2rem(750);
        width: px2rem(750);
        background: #eee;
        padding-top: px2rem(30);
        font-size: px2rem(50);

        #img {
            margin: 0 auto;
        }
    }
</style>
