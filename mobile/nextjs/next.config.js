const withPlugins = require("next-compose-plugins");
const typescript = require("@zeit/next-typescript");
const css = require("@zeit/next-css");
const sass = require("@zeit/next-sass");
const images = require("next-images");

const IS_PRODUCTION_ENV = process.env.NODE_ENV === "production";
console.log("\n[ ENV  ]  NODE_ENV:'" + process.env.NODE_ENV + "',", "IS_PRODUCTION_ENV:" + IS_PRODUCTION_ENV + "\n");

// noinspection JSUnusedLocalSymbols
const next = {
    distDir: "./build/.next",
    assetPrefix: IS_PRODUCTION_ENV ? "" : "", // http://127.0.0.1/_next/static/Mon%20Jul%2015%202019/pages/index.js
    generateEtags: true,
    pageExtensions: ["js"],
    useFileSystemPublicRoutes: true, // https://nextjs.frontendx.cn/docs/#%E7%A6%81%E6%AD%A2%E6%96%87%E4%BB%B6%E8%B7%AF%E7%94%B1
    onDemandEntries: {
        maxInactiveAge: 25 * 1000, // period (in ms) where the server will keep pages in the buffer
        pagesBufferLength: 2 // number of pages that should be kept simultaneously without being disposed
    },
    generateBuildId: async () => {
        return new Date().toDateString(); // For example get the latest git commit hash here
    },
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
    serverRuntimeConfig: {},
    publicRuntimeConfig: {}
};

const config = withPlugins([[typescript], [css, {cssModules: false}], [sass, {cssModules: true}], [images]], next);
module.exports = config;
