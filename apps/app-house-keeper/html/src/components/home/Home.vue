<template>
    <div class="home" @click="onClick()">
        <label>【click事件跳转】：B->A</label>
        <img id="img" src="./image/share.png"/>
    </div>
</template>

<script>
    export default {
        name: 'hello',
        methods: {
            onClick: function () {
                window.location.href = "#/"
            }
        },
        data() {
            return {
                msg: 'B页面',
                message: '[B页面]'
            }
        },
        beforeCreate: function () {
            console.log('[B页面]beforeCreate 创建前状态===============》')
            console.log('[B页面]el     : ' + this.$el) //undefined
            console.log('[B页面]data   : ' + this.$data) //undefined
            console.log('[B页面]message: ' + this.message)
        },
        created: function () {
            console.log('[B页面]created 创建完毕状态===============》')
            console.log('[B页面]el     : ' + this.$el) //undefined
            console.log('[B页面]data   : ' + this.$data) //已被初始化
            console.log('[B页面]message: ' + this.message) //已被初始化
        },
        beforeMount: function () {
            console.log('[B页面]beforeMount 挂载前状态===============》')
            console.log('[B页面]el     : ' + (this.$el)) //已被初始化
            console.log(this.$el)
            console.log('[B页面]data   : ' + this.$data) //已被初始化
            console.log('[B页面]message: ' + this.message) //已被初始化
        },
        mounted: function () {
            console.log('[B页面]mounted 挂载结束状态===============》')
            console.log('[B页面]el     : ' + this.$el) //已被初始化
            console.log(this.$el)
            console.log('[B页面]data   : ' + this.$data) //已被初始化
            console.log('[B页面]message: ' + this.message) //已被初始化
        },
        beforeUpdate: function () {
            console.log('[B页面]beforeUpdate 更新前状态===============》')
            console.log('[B页面]el     : ' + this.$el)
            console.log(this.$el)
            console.log('[B页面]data   : ' + this.$data)
            console.log('[B页面]message: ' + this.message)
        },
        updated: function () {
            console.log('[B页面]updated 更新完成状态===============》')
            console.log('[B页面]el     : ' + this.$el)
            console.log(this.$el)
            console.log('[B页面]data   : ' + this.$data)
            console.log('[B页面]message: ' + this.message)
        },
        beforeDestroy: function () {
            console.log('[B页面]beforeDestroy 销毁前状态===============》')
            console.log('[B页面]el     : ' + this.$el)
            console.log(this.$el)
            console.log('[B页面]data   : ' + this.$data)
            console.log('[B页面]message: ' + this.message)
        },
        destroyed: function () {
            console.log('[B页面]destroyed 销毁完成状态===============》')
            console.log('[B页面]el     : ' + this.$el)
            console.log(this.$el)
            console.log('[B页面]data   : ' + this.$data)
            console.log('[B页面]message: ' + this.message)
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
