<template>
    <div class="root">
        <div class="item" @click="onItemClick('schemaH5')">
            0. open app by schemaH5
        </div>
        <div class="item" @click="onItemClick('schema')">
            0. open app by schema
        </div>
        <div class="item" @click="onItemClick('open')">1. open new page</div>
        <div class="item" @click="onItemClick('close')">
            2. close current page with result
        </div>
        <div class="item" @click="onItemClick('showToast')">3. show toast</div>
        <div class="item_multi">
            <div class="item" @click="onItemClick('put')">
                4. put value
            </div>
            <input class="item_edit" placeholder="请输入" type="text" maxlength="10" v-model="valueName" />
        </div>
        <div class="item" @click="onItemClick('get')">5. get value</div>
        <div class="item" @click="onItemClick('getUserInfo')">
            6. get user info
        </div>
        <div class="item" @click="onItemClick('getLocationInfo')">
            7. get location info
        </div>
        <div class="item" @click="onItemClick('getDeviceInfo')">
            8. get device info
        </div>
    </div>
</template>

<script>
import fixedWidthString from "fixed-width-string";

export default {
    data() {
        return {
            name: "test",
            valueName: null,
            msg: "test"
        };
    },
    methods: {
        onItemClick: function(type) {
            switch (type) {
                case "schema": {
                    window.location.href = "smart://template/rn?component=cc-rn&page=home&params=";
                    break;
                }
                case "schemaH5": {
                    window.location.href = "smart://template/h5?url=https://www.baidu.com";
                    break;
                }
                case "open": {
                    window.bridge.open("https://www.baidu.com");
                    break;
                }
                case "close": {
                    window.bridge.close("OK");
                    break;
                }
                case "showToast": {
                    window.bridge.showToast("Hello I am from Html5");
                    break;
                }
                case "put": {
                    console.log("userName:" + this.valueName);
                    window.bridge.put("userName", this.valueName);
                    break;
                }
                case "get": {
                    window.bridge.get("userName", function(value) {
                        alert(JSON.stringify(value.result));
                    });
                    break;
                }
                case "getUserInfo": {
                    window.bridge.getUserInfo(function(value) {
                        alert(JSON.stringify(value.result));
                    });
                    break;
                }
                case "getLocationInfo": {
                    window.bridge.getLocationInfo(function(value) {
                        alert(JSON.stringify(value.result));
                    });
                    break;
                }
                case "getDeviceInfo": {
                    window.bridge.getDeviceInfo(function(value) {
                        alert(JSON.stringify(value.result));
                    });
                    break;
                }
            }
        }
    },
    beforeCreate: function() {
        console.log(this.page + "(" + fixedWidthString("beforeCreate", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
    },
    created: function() {
        console.log(this.page + "(" + fixedWidthString("created", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
    },
    beforeMount: function() {
        console.log(this.page + "(" + fixedWidthString("beforeMount", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
    },
    mounted: function() {
        this.msg = "new message";
        console.log(this.page + "(" + fixedWidthString("mounted", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
    },
    beforeUpdate: function() {
        console.log(this.page + "(" + fixedWidthString("beforeUpdate", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
    },
    updated: function() {
        console.log(this.page + "(" + fixedWidthString("updated", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
    },
    activated: function() {
        let that = this;
        console.log(this.page + "(" + fixedWidthString("activated", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
        document.querySelector("body").setAttribute("style", "background:white");
    },
    deactivated: function() {
        console.log(this.page + "(" + fixedWidthString("deactivated", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
        document.querySelector("body").setAttribute("style", "background:green");
    },
    beforeDestroy: function() {
        console.log(this.page + "(" + fixedWidthString("beforeDestroy", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
    },
    destroyed: function() {
        console.log(this.page + "(" + fixedWidthString("destroyed", 15, {padding: "_"}) + ")  el:undefined?" + (this.$el === undefined) + "  |  data:undefined?" + (this.$data === undefined) + "  |  msg:" + this.msg);
    }
};
</script>

<style lang="scss">
@import "../assets/common/style/main.scss";

.root {
    height: px2rem(2000);
    width: px2rem(750);
    font-size: px2rem(30);
    padding-top: px2rem(30);
    font-style: italic;
    font-weight: bold;
    color: white;
    text-align: start;
    background-color: lightgray;
}

.item_multi {
    display: flex;
    flex-direction: row;
}

.item_edit {
    display: flex;
    flex: 2;
    font-size: px2rem(30);
    font-style: italic;
    font-weight: bold;
    color: black;
}

.item {
    display: flex;
    flex: 5;
    padding: px2rem(30);
    margin-bottom: px2rem(5);
    background-color: royalblue;
}

/*.item:hover {
        background-color: darkslateblue;
    }*/

.item:active {
    color: gray;
    background-color: midnightblue;
}

input::-webkit-input-placeholder {
    font-size: px2rem(30);
    font-style: normal;
    font-weight: bold;
    color: black;
    text-align: start;
}
</style>
