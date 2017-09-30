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
            console.log('beforeCreate 创建前状态===============》')
            console.log('el     : ' + this.$el) //undefined
            console.log('data   : ' + this.$data) //undefined
            console.log('message: ' + this.message)
        },
        created: function () {
            console.log('created 创建完毕状态===============》')
            console.log('el     : ' + this.$el) //undefined
            console.log('data   : ' + this.$data) //已被初始化
            console.log('message: ' + this.message) //已被初始化
        },
        beforeMount: function () {
            console.log('beforeMount 挂载前状态===============》')
            console.log('el     : ' + (this.$el)) //已被初始化
            console.log(this.$el)
            console.log('data   : ' + this.$data) //已被初始化
            console.log('message: ' + this.message) //已被初始化
        },
        mounted: function () {
            console.log('mounted 挂载结束状态===============》')
            console.log('el     : ' + this.$el) //已被初始化
            console.log(this.$el)
            console.log('data   : ' + this.$data) //已被初始化
            console.log('message: ' + this.message) //已被初始化
        },
        beforeUpdate: function () {
            console.log('beforeUpdate 更新前状态===============》')
            console.log('el     : ' + this.$el)
            console.log(this.$el)
            console.log('data   : ' + this.$data)
            console.log('message: ' + this.message)
        },
        updated: function () {
            console.log('updated 更新完成状态===============》')
            console.log('el     : ' + this.$el)
            console.log(this.$el)
            console.log('data   : ' + this.$data)
            console.log('message: ' + this.message)
        },
        beforeDestroy: function () {
            console.log('beforeDestroy 销毁前状态===============》')
            console.log('el     : ' + this.$el)
            console.log(this.$el)
            console.log('data   : ' + this.$data)
            console.log('message: ' + this.message)
        },
        destroyed: function () {
            console.log('destroyed 销毁完成状态===============》')
            console.log('el     : ' + this.$el)
            console.log(this.$el)
            console.log('data   : ' + this.$data)
            console.log('message: ' + this.message)
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
