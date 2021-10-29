// noinspection SpellCheckingInspection
/**
 * 务必使用 es5 语法,  且尽量不引用外部文件
 * 因为同时被 nextjs 外部 koa server 引用, 引用时不会使用 babel 编译 es6 等语法
 */
const _APP_DOMAIN_PROD = "http://localhost:5388"; // 站点的生产域名 TODO
const _APP_DOMAIN_UAT = _APP_DOMAIN_PROD; // 站点的 UAT 域名 TODO
const _APP_REQUEST_PATH_PREFIX = "/api";

module.exports = {
    APP_DOMAIN_PROD: _APP_DOMAIN_PROD,
    APP_DOMAIN_UAT: _APP_DOMAIN_UAT,
    APP_REQUEST_PATH_PREFIX: _APP_REQUEST_PATH_PREFIX,
    APP_BASIC_STYLES_PATH: () => require("path").join(__dirname, "basic-styles"),
    APP_BASIC_ANTD_LESS_PATH: () => require("path").join(__dirname, "basic-styles/basic-global-vars-antd.less")
};
