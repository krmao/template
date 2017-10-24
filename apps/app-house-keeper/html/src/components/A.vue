<template>
    <div class="a_container">
        <div @click="onClick()">【onClick跳转】A->B</div>
        <router-link id="route-link" to="/B">【 a 标签跳转 】A->B</router-link>

        <div id="nativeCall" @click="onNativeCallClick()"> params=Hello </div>
        <div id="nativeCall2" @click="onNativeCallClick2()"> params=Hello, Native,3000 </div>
        <div id="nativeCall3" @click="onNativeCallClick3()"> http get </div>
        <div id="nativeCall4" @click="onNativeCallClick4()"> http post </div>
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
            onNativeCallClick: function () {
                window.hybird.showToast("show now")
            },
            onNativeCallClick2: function () {
                window.hybird.test("a", "b", function () {
                    console.log("回调被执行")
                })
            },
            onNativeCallClick3: function () {
                console.log("onNativeCallClick3 start")
                axios.get('/user?ID=12345')
                    .then(
                        function (response) {
                            console.log("success:" + response, response);
                        },
                        function (error) {
                            console.log("failure:" + error, error);
                        }
                    )
                    .catch(
                        function (error) {
                            console.log("exception:" + error, error);
                        }
                    );
                console.log("onNativeCallClick3 end")
            },
            onNativeCallClick4: function () {
                console.log("onNativeCallClick4 start")
                axios.post('/user', {
                    firstName: 'Fred',
                    lastName: 'Flintstone'
                }).then(
                    function (response) {
                        console.log("success:" + response, response);
                    },
                    function (error) {
                        console.log("failure:" + error, error);
                    }
                ).catch(
                    function (error) {
                        console.log("exception:" + error, error);
                    }
                );
                console.log("onNativeCallClick4 end")
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
            document.querySelector('body').setAttribute('style', 'background:yellow')
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
        color: orangered;
    }

    .a_container {
        /*padding: constant(safe-area-inset-top) constant(safe-area-inset-right) constant(safe-area-inset-bottom) constant(safe-area-inset-left);*/
        height: px2rem(2000);
        width: px2rem(750);
        /*padding-top: px2rem(50);*/
        font-size: px2rem(40);

        #route-link {
            margin-top: px2rem(60);
        }

        #nativeCall {
            margin-top: px2rem(60);
        }

        #nativeCall2 {
            margin-top: px2rem(60);
        }
        #nativeCall3 {
            margin-top: px2rem(60);
        }
        #nativeCall4 {
            margin-top: px2rem(60);
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
