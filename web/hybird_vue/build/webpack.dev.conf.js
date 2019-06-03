var path = require('path')
var utils = require('./utils')
var webpack = require('webpack')
var config = require('../config')
var merge = require('webpack-merge')
var baseWebpackConfig = require('./webpack.base.conf')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var FriendlyErrorsPlugin = require('friendly-errors-webpack-plugin')
var ReplaceBundleStringPlugin = require('replace-bundle-webpack-plugin')

var env = config.build.env

// 接收运行参数
const argv = require('yargs').argv;
console.log("env:", env)
console.log("argv:", argv)
console.log("argv.debug:", argv.debug)

// add hot-reload related code to entry chunks
Object.keys(baseWebpackConfig.entry).forEach(function (name) {
    baseWebpackConfig.entry[name] = ['./build/dev-client'].concat(baseWebpackConfig.entry[name])
})

module.exports = merge(baseWebpackConfig, {
    module: {
        rules: utils.styleLoaders({sourceMap: config.dev.cssSourceMap})
    },
    // cheap-module-eval-source-map is faster for development
    devtool: '#cheap-module-eval-source-map',
    plugins: [
        new webpack.DefinePlugin({
            'process.env': config.dev.env
        }),
        // https://github.com/glenjamin/webpack-hot-middleware#installation--usage
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NoEmitOnErrorsPlugin(),
        // https://github.com/ampedandwired/html-webpack-plugin
        new HtmlWebpackPlugin({
            favicon: path.resolve(__dirname, '../src/assets/common/image/favicon.ico'),
            filename: 'index.html',
            template: 'index.html',
            inject: true

        }),
        new FriendlyErrorsPlugin(),

        new ReplaceBundleStringPlugin([{
            partten: /"use strict";/g,
            replacement: function () {
                return '';
            }
        }])
    ]
})
