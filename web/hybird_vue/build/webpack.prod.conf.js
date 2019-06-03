var path = require('path')
var utils = require('./utils')
var webpack = require('webpack')
var config = require('../config')
var merge = require('webpack-merge')
var baseWebpackConfig = require('./webpack.base.conf')
var CopyWebpackPlugin = require('copy-webpack-plugin')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var OptimizeCSSPlugin = require('optimize-css-assets-webpack-plugin')
var ReplaceBundleStringPlugin = require('replace-bundle-webpack-plugin')

var ModuleConfigPlugin = require('./module-config-plugin')
let moduleName = path.basename(path.resolve("."));
let moduleVersion = "1.0";

var env = config.build.env

// 接收运行参数
const argv = require('yargs').argv;
console.log("env:", env)
console.log("argv:", argv)
console.log("argv.debug:", argv.debug)

var webpackConfig = merge(baseWebpackConfig, {
    module: {
        rules: utils.styleLoaders({
            sourceMap: config.build.productionSourceMap,
            extract: true
        })
    },
    devtool: config.build.productionSourceMap ? '#source-map' : false,
    output: {
        path: config.build.assetsRoot,
        filename: utils.assetsPath('js/[name].[chunkhash].js'),
        chunkFilename: utils.assetsPath('js/[id].[chunkhash].js')
    },
    plugins: [
        // http://vuejs.github.io/vue-loader/en/workflow/production.html
        new webpack.DefinePlugin({
            'process.env': env
        }),
        new webpack.optimize.UglifyJsPlugin({
            compress: {
                warnings: false
            },
            sourceMap: true
        }),
        // extract css into its own file
        new ExtractTextPlugin({
            filename: utils.assetsPath('css/[name].[contenthash].css')
        }),
        // Compress extracted CSS. We are using this plugin so that possible
        // duplicated CSS from different components can be deduped.
        new OptimizeCSSPlugin({
            cssProcessorOptions: {
                safe: true
            }
        }),
        // generate dist index.html with correct asset hash for caching.
        // you can customize output by editing /index.html
        // see https://github.com/ampedandwired/html-webpack-plugin
        new HtmlWebpackPlugin({
            favicon: path.resolve(__dirname, '../src/assets/common/image/favicon.ico'),
            filename: config.build.index,
            template: 'index.html',
            inject: true,
            minify: {
                removeComments: true,
                collapseWhitespace: true,
                removeAttributeQuotes: true
                // more options:
                // https://github.com/kangax/html-minifier#options-quick-reference
            },
            // necessary to consistently work with multiple chunks via CommonsChunkPlugin
            chunksSortMode: 'dependency'
        }),
        // keep module.id stable when vender modules does not change
        new webpack.HashedModuleIdsPlugin(),
        // split vendor js into its own file
        new webpack.optimize.CommonsChunkPlugin({
            name: 'vendor',
            minChunks: function (module, count) {
                // any required modules inside node_modules are extracted to vendor
                return (
                    module.resource &&
                    /\.js$/.test(module.resource) &&
                    module.resource.indexOf(
                        path.join(__dirname, '../node_modules')
                    ) === 0
                )
            }
        }),
        // extract webpack runtime and module manifest to its own file in order to
        // prevent vendor hash from being updated whenever app bundle is updated
        new webpack.optimize.CommonsChunkPlugin({
            name: 'manifest',
            chunks: ['vendor']
        }),
        // copy custom static assets
        new CopyWebpackPlugin([
            {
                from: path.resolve(__dirname, '../static'),
                to: config.build.assetsSubDirectory,
                ignore: ['.*']
            }
        ]),
        new ReplaceBundleStringPlugin([{
            partten: /"use strict";/g,
            replacement: function () {
                return '';
            }
        }]),
        new ModuleConfigPlugin({
            rootFolder: '',
            baseInfo: {
                moduleName: moduleName,
                moduleVersion: moduleVersion,
                moduleDebug: true,
                moduleUpdateStrategy: "ONLINE", // ONLINE=在线  , OFFLINE=离线

                //https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/buyMealCard/index.shtml#/cardList
                moduleMainUrl: "/cx/cxj/cxjappweb/" + moduleName + "/",
                //"https://s1.chexiangpre.com/msweb02/cx/cxj/cxjappweb/" + moduleName + "/"
                moduleScriptUrl: "/cx/cxj/cxjappweb/" + moduleName + "/",
                moduleConfigUrl: "http://10.47.58.14:8080/background/files/" + moduleName + ".json",
                moduleDownloadUrl: "http://10.47.58.14:8080/background/files/" + moduleName + "-" + moduleVersion + ".zip"
            },
            input: config.build.assetsRoot,
            output: [
                path.resolve(config.build.assetsRoot, '../../../../template/apps/app-house-keeper/android/arsenal/modules/module-housekeeper-hybird/src/main/assets/')
            ]
        })
    ]
});

if (config.build.productionGzip) {
    var CompressionWebpackPlugin = require('compression-webpack-plugin')

    webpackConfig.plugins.push(
        new CompressionWebpackPlugin({
            asset: '[path].gz[query]',
            algorithm: 'gzip',
            test: new RegExp(
                '\\.(' +
                config.build.productionGzipExtensions.join('|') +
                ')$'
            ),
            threshold: 10240,
            minRatio: 0.8
        })
    )
}

if (config.build.bundleAnalyzerReport) {
    var BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
    webpackConfig.plugins.push(new BundleAnalyzerPlugin())
}

module.exports = webpackConfig
