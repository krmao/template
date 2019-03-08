<template>
    <div class="a_container">
        <div id="click" @click="onClick()">A跳转到B(onClick)</div>
        <router-link id="route-link" to="/B">A跳转到B(route-link)</router-link>

        <div id="nativeCall" @click="showToast()"> STHybird:toast </div>
        <div id="nativeCall5" @click="getValue()"> STHybird:getValue </div>
    </div>
</template>

<script>
    import fixedWidthString from 'fixed-width-string';
    import axios from 'axios'

    let page = fixedWidthString('[组件A', 7, {padding: '.', align: 'right'}) + ']';

    export default {
        name: page,
        methods: {
            onClick: function () {
                window.location.href = "#/B"
            },
            showToast: function () {
                window.hybird.showToast("hello I am html5")
            },
            getValue: function () {
                console.log("getValue start")
                window.hybird.getFromLocal("haha", function (value) {
                    console.log("onCallback,value=" + value)
                })
                console.log("getValue end")
            },
            onGoBack:
                function () {
                    console.log(page + '(' + fixedWidthString('onGoBack', 15, {padding: '_'}) + ')')
                }
        },
        data() {
            return {
                msg: "A",
            }
        },
        beforeCreate: function () {
            console.log(page + '(' + fixedWidthString('beforeCreate', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        },
        created: function () {
            console.log(page + '(' + fixedWidthString('created', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        },
        beforeMount: function () {
            console.log(page + '(' + fixedWidthString('beforeMount', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        },
        mounted: function () {
            this.msg = "new message"
            console.log(page + '(' + fixedWidthString('mounted', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        },
        beforeUpdate: function () {
            console.log(page + '(' + fixedWidthString('beforeUpdate', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        },
        updated: function () {
            console.log(page + '(' + fixedWidthString('updated', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        },
        activated: function () {
            var that = this;
            console.log(page + '(' + fixedWidthString('activated', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
            document.querySelector('body').setAttribute('style', 'background:white')
//            window.location.href = "hybird://hybird:8888/updateTitle?title=组件A"

//            window.hybird.onResume = ()=> {
//                that.activated()
//            }


        },
        deactivated: function () {
            console.log(page + '(' + fixedWidthString('deactivated', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
            document.querySelector('body').setAttribute('style', 'background:green')
//            window.location.href = "hybird://hybird:8888/updateTitle?title="
        },
        beforeDestroy: function () {
            console.log(page + '(' + fixedWidthString('beforeDestroy', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        },
        destroyed: function () {
            console.log(page + '(' + fixedWidthString('destroyed', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
    @import "../assets/common/style/main.scss";

    img {
        margin: 0;
        padding: 0;
        height: px2rem(2000);
    }

    a {
        color: #000;
    }

    a:active {
        color: #000;
    }

    .a_container {
        /*padding: constant(safe-area-inset-top) constant(safe-area-inset-right) constant(safe-area-inset-bottom) constant(safe-area-inset-left);*/
        height: px2rem(2000);
        width: px2rem(750);
        font-size: px2rem(30);
        padding-top: px2rem(30);
        color: black;
        text-align: start;

        #click {
            margin-top: px2rem(2);
            padding: px2rem(30);
            background-color: #268bd2;
        }
        #route-link {
            margin-top: px2rem(2);
            padding: px2rem(30);
            background-color: #268bd2;
        }

        #nativeCall {
            margin-top: px2rem(2);
            padding: px2rem(30);
            background-color: #268bd2;
        }

        #nativeCall5 {
            margin-top: px2rem(2);
            padding: px2rem(30);
            background-color: #268bd2;
        }
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
</style>
