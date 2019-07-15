const withPlugins = require("next-compose-plugins");
const typescript = require("@zeit/next-typescript");
const css = require("@zeit/next-css");
const sass = require("@zeit/next-sass");
const images = require("next-images");

const isProd = process.env.NODE_ENV === "production";
const next = {
    distDir: "./build/.next",
    pageExtensions: ["js"],
    generateEtags: false,
    useFileSystemPublicRoutes: false, // https://nextjs.frontendx.cn/docs/#%E7%A6%81%E6%AD%A2%E6%96%87%E4%BB%B6%E8%B7%AF%E7%94%B1
    onDemandEntries: {
        // period (in ms) where the server will keep pages in the buffer
        maxInactiveAge: 25 * 1000,
        // number of pages that should be kept simultaneously without being disposed
        pagesBufferLength: 2
    },
    generateBuildId: async () => {
        return new Date().toDateString(); // For example get the latest git commit hash here
    },
    assetPrefix: isProd ? "" : "", // http://127.0.0.1/_next/static/Mon%20Jul%2015%202019/pages/index.js
    exportPathMap: function() {
        return {
            "/": {page: "/"}
        };
    },
    webpack: (config, {buildId, dev, isServer, defaultLoaders}) => {
        return config;
    },
    webpackDevMiddleware: (config) => {
        return config;
    },
    serverRuntimeConfig: {
        // Will only be available on the server side
        mySecret: "secret"
    },
    publicRuntimeConfig: {
        // Will be available on both server and client
        staticFolder: "/static"
    }
};

const config = withPlugins([[typescript], [css, {cssModules: false}], [sass, {cssModules: true}], [images]], next);

module.exports = config;
