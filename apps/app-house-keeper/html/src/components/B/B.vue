<template>
    <div class="b_container">
        <img id="img" src="./image/share.png"/>
        <label @click="onClick()">【scheme:WebView-new】：B->外部链接</label>
        <router-link to="/other">【a标签跳转】B->外部链接</router-link>
    </div>

</template>

<script>
    import fixedWidthString from 'fixed-width-string';

    let page = fixedWidthString('[组件B', 11, {padding: '.', align: 'right'}) + ']';

    export default {
        name: page,
        methods: {
            testConsole: function () {
                console.log(1)
                console.log("string")
                console.log(undefined)
                console.log(null)
                console.log(function () {
                    let b = "aa"
                })
                console.log("this" + this)
                console.log("this", this)
                console.log(this)
                console.log(window)
            },
            onClick: function () {
                document.querySelector('body').setAttribute('style', 'background:white')
                setTimeout(function () {
                    let url = encodeURI("https://c.chexiang.com/promotion/201709xinche.htm?channel=cx_zc_201709_cxyx_h5")
                    console.error("scheme:url===" + url)
                    window.location.href = "hybird://hybird:7777/path/url=" + url
                }, 1000)
            }
        },
        data() {
            return {
                msg: "B",
            }
        },
        beforeCreate: function () {
            console.log(page + '(' + fixedWidthString('beforeCreate', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
        },
        created: function () {
            //this.testConsole()
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
            console.log(page + '(' + fixedWidthString('activated', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
            document.querySelector('body').setAttribute('style', 'background:cornflowerblue')
        },
        deactivated: function () {
            console.log(page + '(' + fixedWidthString('deactivated', 15, {padding: '_'}) + ')  el:undefined?' + (this.$el === undefined) + '  |  data:undefined?' + (this.$data === undefined) + '  |  msg:' + this.msg)
            document.querySelector('body').setAttribute('style', 'background:orange')
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
    @import "../../assets/common/style/main.scss";

    body {
        background: cornflowerblue;
    }

    .b_container {
        height: px2rem(750);
        width: px2rem(750);
        padding-top: px2rem(30);
        font-size: px2rem(50);

        #img {
            margin: 0 auto;
        }
    }
</style>
