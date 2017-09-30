<template>
    <div class="hello">
        <router-link to="/home">【a标签跳转】A->B</router-link>
        <div @click="onClick()">【click事件跳转】A->B</div>
    </div>
</template>

<script>
    let page = '[组件A]'

    export default {
        name: 'hello',
        methods: {
            onClick: function () {
                window.location.href = "#/home"
            }
        },
        data() {
            return {
                msg: page,
                message: 'page'
            }
        },
        beforeCreate: function () {
            console.log(page + 'beforeCreate -------------------->>>>>>>>>>')
            console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
            console.log(page + 'beforeCreate <<<<<<<<<<--------------------')
        },
        created: function () {
            console.log(page + 'created -------------------->>>>>>>>>>')
            console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
            console.log(page + 'created <<<<<<<<<<--------------------')
        },
        beforeMount: function () {
            console.log(page + 'beforeMount -------------------->>>>>>>>>>')
            console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
            console.log(page + 'beforeMount <<<<<<<<<<--------------------')
        },
        mounted: function () {
            console.log(page + 'mounted -------------------->>>>>>>>>>')
            console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
            console.log(page + 'mounted <<<<<<<<<<--------------------')
        },
        beforeUpdate: function () {
            console.log(page + 'beforeUpdate -------------------->>>>>>>>>>')
            console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
            console.log(page + 'beforeUpdate <<<<<<<<<<--------------------')
        },
        updated: function () {
            console.log(page + 'updated 更新完成状态---------->>>>>>>>>>')
            console.log(page + 'el     : ' + this.$el)
            console.log(this.$el)
            console.log(page + 'data   : ' + this.$data)
            console.log(page + 'message: ' + this.message)
        },
        beforeDestroy: function () {
            console.log(page + 'beforeDestroy 销毁前状态---------->>>>>>>>>>')
            console.log(page + 'el     : ' + this.$el)
            console.log(this.$el)
            console.log(page + 'data   : ' + this.$data)
            console.log(page + 'message: ' + this.message)
        },
        destroyed: function () {
            console.log(page + 'destroyed 销毁完成状态---------->>>>>>>>>>')
            console.log(page + 'el     : ' + this.$el)
            console.log(this.$el)
            console.log(page + 'data   : ' + this.$data)
            console.log(page + 'message: ' + this.message)
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
