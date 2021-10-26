const path = require("path");
const webpackRules = require("./webpack.config.js");
const withPlugins = require("next-compose-plugins");
const withBundleAnalyzer = require("@next/bundle-analyzer")({enabled: process.env.ANALYZE === "true"});
const withLess = require("next-with-less");

const IS_PRODUCTION_ENV = process.env.NODE_ENV === "production";
console.log(
    "info  - NODE_ENV:'" + process.env.NODE_ENV + "',",
    "APP_ENV:'" + process.env.APP_ENV + "',",
    "IS_PRODUCTION_ENV:'" + IS_PRODUCTION_ENV + "'\n"
);

const nextConfig = {
    distDir: "./build/.next",
    generateEtags: true,
    pageExtensions: ["jsx", "js"],
    useFileSystemPublicRoutes: true,
    generateBuildId: async () => (process.env.BUILD_ID ? process.env.BUILD_ID : new Date().toDateString()),
    webpackDevMiddleware: (config) => config,
    exportPathMap: () => ({
        "/": {page: "/"}
    }),
    onDemandEntries: {maxInactiveAge: 25 * 1000, pagesBufferLength: 2},
    env: {
        testEnv: "testEnv" // 可以在页面上通过 process.env.testEnv 获取 value
    },
    sassOptions: {
        includePaths: [path.join(__dirname, "src/basic/styles")] // https://github.com/YutHelloWorld/Blog/issues/12#issue-254066318
    },
    serverRuntimeConfig: {
        testServerRuntimeConfig: "testServerRuntimeConfig" // 通过 'next/config' 来读取, 只有在服务端渲染时才会获取的配置
    },
    publicRuntimeConfig: {
        testPublicRuntimeConfig: "testPublicRuntimeConfig" // 在服务端渲染和客户端渲染都可获取的配置
    },
    webpack(config, {buildId, dev, isServer}) {
        console.log("info  - Webpack config buildId:", buildId, "dev:", dev, "isServer:", isServer);

        config.module.rules.push({...webpackRules});

        //region *.svg file support
        // https://react-svgr.com/docs/webpack/
        // https://ant.design/components/icon-cn/#%E8%87%AA%E5%AE%9A%E4%B9%89-SVG-%E5%9B%BE%E6%A0%87
        config.module.rules.push({
            test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
            use: [
                {
                    loader: "babel-loader"
                },
                {
                    loader: "@svgr/webpack",
                    options: {
                        babel: false,
                        icon: true
                    }
                }
            ]
        });
        //endregion

        return config;
    },
    webpack5: true
};

module.exports = withPlugins(
    [
        [withBundleAnalyzer, {}],
        //region https://github.com/elado/next-with-less
        [
            withLess,
            {
                lessLoaderOptions: {
                    additionalData: (content) =>
                        `${content}\n\n@import '${path.resolve("./src/basic/styles/basic-global-vars-antd.less")}';`,
                    lessOptions: {modifyVars: {}}
                }
            }
        ]
        //endregion
    ],
    nextConfig
);
