module.exports = function (api) {
    api.cache(true);

    const presets = [["next/babel"]];

    //region next-with-less 不要设置 plugins/babel-plugin-import, next-plugin-antd-less则需要设置, next-plugin-antd-less 会覆盖 withPlugins 的 nextConfig 配置, 以及使得 npm run analyze 失效(https://github.com/SolidZORO/next-plugin-antd-less/issues/84)
    // next-with-less
    // npm install next-with-less less less-loader && npm uninstall babel-plugin-import next-plugin-antd-less && remove plugins in babel.config.js
    // https://github.com/elado/next-with-less/issues/6#issuecomment-831676037
    // import "antd/dist/antd.less"; in _app.js
    // module.exports = withPlugins(
    //     [
    //         [withBundleAnalyzer, {}],
    //         [
    //             withLess,
    //             {
    //                 lessLoaderOptions: {
    //                     lessOptions: {
    //                         modifyVars: {
    //                             "primary-color": "#ff0000",
    //                             "border-radius-base": "2px"
    //                         }
    //                     }
    //                 }
    //             }
    //         ]
    //     ],
    //     nextConfig
    // );
    //
    // next-plugin-antd-less
    // npm install next-plugin-antd-less babel-plugin-import && npm uninstall next-with-less less less-loader && add plugins(the follow code) in babel.config.js
    // // import "antd/dist/antd.css"; in _app.js
    /*const plugins = [
        ["import", {libraryName: "antd", libraryDirectory: "lib", style: true}, "antd"],
        [
            "import",
            {
                libraryName: "@ant-design/icons",
                libraryDirectory: "lib/icons",
                camel2DashComponentName: false,
                style: false
            },
            "@ant-design/icons"
        ]
    ];*/
    //endregion

    return {
        presets: presets
        // plugins: plugins
    };
};
