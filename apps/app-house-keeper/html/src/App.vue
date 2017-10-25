<template>
    <div id="app" class="root-container" v-bind:class="{ 'safe-area-padding-for-iphonex' :isIphoneX }">
        <transition name="slide-left">
            <keep-alive>
                <router-view></router-view>
            </keep-alive>
        </transition>
    </div>
</template>

<script>
    import platform from 'platform'

    console.log("platform:" + platform)
    console.log("platform:os:" + platform.os)

    export default {
        name: 'app',
        data() {
            return {
                isIphoneX: true //window.hybird.browser.versions.isIphoneX
            }
        },
    }
</script>

<style>
    body {
        background: red;
    }

    .safe-area-padding-for-iphonex {
        padding: constant(safe-area-inset-top) constant(safe-area-inset-right) constant(safe-area-inset-bottom) constant(safe-area-inset-left);
    }

    .root-container {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        /*padding: constant(safe-area-inset-top) constant(safe-area-inset-right) constant(safe-area-inset-bottom) constant(safe-area-inset-left);*/
    }
</style>
