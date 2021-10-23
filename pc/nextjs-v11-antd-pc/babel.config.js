const webpackRules = require("./webpack.config.js");

module.exports = function (api) {
    api.cache(true);

    const presets = [["next/babel"]];
    const plugins = [
        ["import", {libraryName: "antd", libraryDirectory: "lib", style: true}, "antd"],
        [
            "import",
            {libraryName: "@ant-design/icons", libraryDirectory: "lib/icons", camel2DashComponentName: false, style: false},
            "@ant-design/icons"
        ]
        /*[
            require.resolve("babel-plugin-module-resolver"),
            {
                root: ["./src/"],
                alias: webpackRules.resolve.alias
            }
        ]*/
    ];

    return {
        presets,
        plugins
    };
};
