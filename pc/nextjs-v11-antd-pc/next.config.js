const path = require("path");
const webpackRules = require("./webpack.config.js");
const withPlugins = require("next-compose-plugins");
const withBundleAnalyzer = require("@next/bundle-analyzer")({enabled: process.env.ANALYZE === "true"});
const withAntdLess = require("next-plugin-antd-less");

const IS_PRODUCTION_ENV = process.env.NODE_ENV === "production";
console.log(
    "\n[ ENV  ]  NODE_ENV:'" + process.env.NODE_ENV + "',",
    "APP_ENV:'" + process.env.APP_ENV + "',",
    "IS_PRODUCTION_ENV:'" + IS_PRODUCTION_ENV + "'\n"
);

const nextConfig = {
    distDir: "./build/.next", // 编译文件的输出目录
    generateEtags: true, // 是否给每个路由生成Etag
    pageExtensions: ["jsx", "js"], // 在pages目录下那种后缀的文件会被认为是页面
    useFileSystemPublicRoutes: true, // https://nextjs.frontendx.cn/docs/#%E7%A6%81%E6%AD%A2%E6%96%87%E4%BB%B6%E8%B7%AF%E7%94%B1
    generateBuildId: async () => (process.env.BUILD_ID ? process.env.BUILD_ID : new Date().toDateString()), // 返回 null 使用默认的 unique id
    webpackDevMiddleware: (config) => config, // 修改webpackDevMiddleware配置
    exportPathMap: function () {
        // https://nextjs.org/docs/api-reference/next.config.js/exportPathMap
        return {
            "/": {page: "/"}
        };
    },
    onDemandEntries: {
        // 页面内容缓存配置
        maxInactiveAge: 25 * 1000, // 内容在内存中缓存的时长（ms）
        pagesBufferLength: 2 // 同时缓存多少个页面
    },
    env: {
        // 可以在页面上通过 process.env.testEnv 获取 value
        testEnv: "testEnv"
    },
    sassOptions: {
        includePaths: [path.join(__dirname, "src/basic/styles")] // https://github.com/YutHelloWorld/Blog/issues/12#issue-254066318
    },
    serverRuntimeConfig: {
        // 通过 'next/config' 来读取, 只有在服务端渲染时才会获取的配置
        testServerRuntimeConfig: "testServerRuntimeConfig"
    },
    publicRuntimeConfig: {
        // 在服务端渲染和客户端渲染都可获取的配置
        testPublicRuntimeConfig: "testPublicRuntimeConfig"
    },
    webpack(config, {buildId, dev, isServer, defaultLoaders}) {
        // 手动修改webpack config
        // console.log("webpack config buildId:", buildId, "dev:", dev, "isServer:", isServer, "defaultLoaders:", defaultLoaders);

        config.module.rules.push({
            ...webpackRules
        });

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
        return config;
    },
    webpack5: true
};

const lessPlugin = withAntdLess({
    modifyVars: {"@primary-color": "#f74a49"}, // optional
    lessVarsFilePathAppendToEndOfContent: false, // optional
    cssModules: true, // optional https://github.com/webpack-contrib/css-loader#object
    cssLoaderOptions: {sourceMap: true}, // https://github.com/webpack-contrib/css-loader#object
    ...nextConfig
});

module.exports = withPlugins([withBundleAnalyzer, lessPlugin], nextConfig); // npm run analyze failure no output
