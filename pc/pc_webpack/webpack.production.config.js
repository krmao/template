let webpack = require('webpack');
let HtmlWebpackPlugin = require('html-webpack-plugin');
let ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    devtool: 'eval-source-map',
    entry: './entry.js',
    output: {
        path: __dirname,
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
            use: {
                loader: "babel-loader",
                options: {
                    presets: [
                        "env"
                    ]
                }
            },
            exclude: /node_modules/
        }, {
            test: /\.css$/,
            use: extractTextPlugin.extract({
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
                        loader: "postcss-loader"
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
            template: __dirname + "/index.tmpl.html" //new 一个这个插件的实例，并传入相关的参数
        }),
        new webpack.optimize.OccurrenceOrderPlugin(),
        new webpack.optimize.UglifyJsPlugin(),
        new ExtractTextPlugin("style.css"),
        new webpack.HotModuleReplacementPlugin()//热加载插件
    ]
};