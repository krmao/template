const withPlugins = require("next-compose-plugins");
const typescript = require("@zeit/next-typescript");
const css = require("@zeit/next-css");
const sass = require("@zeit/next-sass");
const images = require("next-images");

const next = {
    distDir: "./.next"
};

const config = withPlugins([
    [typescript],
    [css, {cssModules: false}],
    [sass, {cssModules: true}],
    [images]
], next);

module.exports = config;
