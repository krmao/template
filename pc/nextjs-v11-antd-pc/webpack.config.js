// https://juejin.cn/post/6850418109484892168
// https://webpack.docschina.org/configuration/resolve/
// https://www.jetbrains.com/help/idea/2021.1/using-webpack.html#specify_webpack_configuration_file

const path = require("path");

module.exports = {
    resolve: {
        alias: {
            "@public": path.resolve(__dirname, "./public"),
            "@basic": path.resolve(__dirname, "./src/components/basic"),
            "@basic-styles": path.resolve(__dirname, "./src/components/basic/basic-styles"),
            "@basic-utils": path.resolve(__dirname, "./src/components/basic/basic-utils"),
            "@components": path.resolve(__dirname, "./src/components"),
            "@business": path.resolve(__dirname, "./src/components/business")
        }
    }
};
