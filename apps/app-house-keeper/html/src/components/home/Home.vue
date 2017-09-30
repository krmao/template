<template>
    <div class="home" @click="onClick()">
        <label>【click事件跳转】：B->A</label>
        <img id="img" src="./image/share.png"/>
    </div>
</template>

<script>
    let page = '[组件B]'

    export default {
        name: page,
        methods: {
            onClick: function () {
                window.location.href = "https://c.chexiang.com/promotion/201709xinche.htm?channel=cx_zc_201709_cxyx_h5"
            }
        },
        data() {
            return {
                msg: page,
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
            console.log(page + 'updated -------------------->>>>>>>>>>')
            console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
            console.log(page + 'updated <<<<<<<<<<--------------------')
        },
        beforeDestroy: function () {
            console.log(page + 'beforeDestroy -------------------->>>>>>>>>>')
            console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
            console.log(page + 'beforeDestroy <<<<<<<<<<--------------------')
        },
        destroyed: function () {
            console.log(page + 'destroyed -------------------->>>>>>>>>>')
            console.log(page + 'el:' + this.$el + '  |  data:' + this.$data + '  |  msg:' + this.msg)
            console.log(page + 'destroyed <<<<<<<<<<--------------------')
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
