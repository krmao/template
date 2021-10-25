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
        ]
    ];

    return {
        presets: presets,
        plugins: plugins
    };
};
