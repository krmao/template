let webpack = require('webpack');
let HtmlWebpackPlugin = require('html-webpack-plugin');
let ExtractTextPlugin = require('extract-text-webpack-plugin');
let path = require("path");

module.exports = {
    devtool: 'source-map',
    entry: './src/entry.js',
    output: {
        path: __dirname + '/build',
        filename: 'bundle.js'
    },
    devServer: {
        contentBase: "./",
        port: 8088,
        inline: true,               //设置为true，当源文件改变时会自动刷新页面
        historyApiFallback: true,   //设置为true，所有的跳转将指向index.html
        hot: true                   //模块热更新
    },
    module: {
        rules: [{
            test: /(\.jsx|\.js)$/,
            use: [
                {
                    loader: "babel-loader",
                    options: {
                        presets: [
                            "env"
                        ]
                    }
                },
                {
                    loader: 'file-loader',
                    query: {
                        presets: ['es2016']
                        // name: '[name].[ext]'
                    }
                }
            ],
            exclude: path.resolve(__dirname, 'node_modules'),
            include: '/src/',
            enforce: "pre"//加载器的执行顺序，不设置为正常执行。可选值 'pre|post' 前|后
        }, {
            test: /\.css$/,
            use: ExtractTextPlugin.extract({
                fallback: "style-loader",
                use: [
                    {
                        loader: "css-loader",
                        options:
                            {
                                modules: true
                            }
                    },
                    {
                        loader: "postcss-loader",
                        options: {
                            plugins: [
                                require("autoprefixer")({browserslist: ["last 2 versions"]})
                            ]
                        }
                    }
                ],
            })
        }],
        loaders: [
            {test: /\.css$/, loader: 'style-loader!css-loader'}
        ]
    },
    plugins: [
        new webpack.BannerPlugin('krmao 版权所有，翻版必究'),
        new HtmlWebpackPlugin({
            // filename: 'index.html',
            // inject: 'body'
            template: __dirname + "/src/index.tmpl.html" //new 一个这个插件的实例，并传入相关的参数
        }),
        new webpack.optimize.OccurrenceOrderPlugin(),
        new webpack.optimize.UglifyJsPlugin(),
        new ExtractTextPlugin("style.css"),
        new webpack.HotModuleReplacementPlugin()//热加载插件
    ]
};