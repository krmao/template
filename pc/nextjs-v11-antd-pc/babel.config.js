const webpackRules = require("./webpack.config.js");
const path = require("path");

module.exports = function (api) {
    api.cache(true);

    const presets = [["next/babel"]];
    const plugins = [
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
        ]/*,
        [
            require.resolve("babel-plugin-module-resolver"),
            {
                root: ["./src/"],
                alias: {
                    "@public": path.resolve(__dirname, "./public"),
                    "@basic": path.resolve(__dirname, "./src/basic"),
                    "@styles": path.resolve(__dirname, "./src/basic/styles"),
                    "@utils": path.resolve(__dirname, "./src/basic/utils"),
                    "@components": path.resolve(__dirname, "./src/components")
                }
            }
        ]*/
    ];

    return {
        presets,
        plugins
    };
};
