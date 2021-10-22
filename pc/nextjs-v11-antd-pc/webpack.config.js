// https://juejin.cn/post/6850418109484892168
// https://webpack.docschina.org/configuration/resolve/
// https://www.jetbrains.com/help/idea/2021.1/using-webpack.html#specify_webpack_configuration_file

const path = require('path');

module.exports = {
    resolve: {
        alias: {
            '@': path.resolve(__dirname),
            '@public': path.resolve(__dirname, './public'),
            '@basic': path.resolve(__dirname, './src/basic'),
            '@styles': path.resolve(__dirname, './src/basic/styles'),
            '@utils': path.resolve(__dirname, './src/basic/utils'),
            '@components': path.resolve(__dirname, './src/components'),
        }
    }
};
