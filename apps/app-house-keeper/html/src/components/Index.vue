<template>
    <div class="hello">
        <router-link to="/home">【a标签跳转】A->B</router-link>
        <div @click="onClick()">【click事件跳转】A->B</div>
    </div>
</template>

<script>


    export default {
        name: 'hello',
        methods: {
            onClick: function () {
                window.location.href = "#/home"
            }
        },
        data() {
            return {
                msg: 'A页面',
                message: '[A页面]'
            }
        },
        beforeCreate: function () {
            console.log('[A页面]beforeCreate 创建前状态===============》')
            console.log('[A页面]el     : ' + this.$el) //undefined
            console.log('[A页面]data   : ' + this.$data) //undefined
            console.log('[A页面]message: ' + this.message)
        },
        created: function () {
            console.log('[A页面]created 创建完毕状态===============》')
            console.log('[A页面]el     : ' + this.$el) //undefined
            console.log('[A页面]data   : ' + this.$data) //已被初始化
            console.log('[A页面]message: ' + this.message) //已被初始化
        },
        beforeMount: function () {
            console.log('[A页面]beforeMount 挂载前状态===============》')
            console.log('[A页面]el     : ' + (this.$el)) //已被初始化
            console.log(this.$el)
            console.log('[A页面]data   : ' + this.$data) //已被初始化
            console.log('[A页面]message: ' + this.message) //已被初始化
        },
        mounted: function () {
            console.log('[A页面]mounted 挂载结束状态===============》')
            console.log('[A页面]el     : ' + this.$el) //已被初始化
            console.log(this.$el)
            console.log('[A页面]data   : ' + this.$data) //已被初始化
            console.log('[A页面]message: ' + this.message) //已被初始化
        },
        beforeUpdate: function () {
            console.log('[A页面]beforeUpdate 更新前状态===============》')
            console.log('[A页面]el     : ' + this.$el)
            console.log(this.$el)
            console.log('[A页面]data   : ' + this.$data)
            console.log('[A页面]message: ' + this.message)
        },
        updated: function () {
            console.log('[A页面]updated 更新完成状态===============》')
            console.log('[A页面]el     : ' + this.$el)
            console.log(this.$el)
            console.log('[A页面]data   : ' + this.$data)
            console.log('[A页面]message: ' + this.message)
        },
        beforeDestroy: function () {
            console.log('[A页面]beforeDestroy 销毁前状态===============》')
            console.log('[A页面]el     : ' + this.$el)
            console.log(this.$el)
            console.log('[A页面]data   : ' + this.$data)
            console.log('[A页面]message: ' + this.message)
        },
        destroyed: function () {
            console.log('[A页面]destroyed 销毁完成状态===============》')
            console.log('[A页面]el     : ' + this.$el)
            console.log(this.$el)
            console.log('[A页面]data   : ' + this.$data)
            console.log('[A页面]message: ' + this.message)
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
    @import "../assets/common/style/main.scss";

    .hello {
        height: px2rem(750);
        width: px2rem(750);
        background: #eee;
        padding-top: px2rem(30);
        font-size: px2rem(50);
    }

    h1, h2 {
        font-weight: normal;
    }

    ul {
        list-style-type: none;
        padding: 0;
    }

    li {
        display: inline-block;
        margin: 0 10px;
    }

    a {
        color: #42b983;
    }
</style>
