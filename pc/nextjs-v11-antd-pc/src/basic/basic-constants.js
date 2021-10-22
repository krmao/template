/**
 * 务必使用 es5 语法,  且尽量不引用外部文件
 * 因为同时被 nextjs 外部 koa server 引用, 引用时不会使用 babel 编译 es6 等语法
 */
const _APP_DOMAIN_LOCALHOST_PORT = 3000;
const _APP_DOMAIN_LOCALHOST = "http://localhost:" + _APP_DOMAIN_LOCALHOST_PORT; // 站点的 localhost 域名
const _APP_DOMAIN_PROD = _APP_DOMAIN_LOCALHOST;                                 // 站点的生产域名 TODO
const _APP_DOMAIN_UAT = _APP_DOMAIN_LOCALHOST;                                  // 站点的 UAT 域名 TODO
const _APP_REQUEST_PATH_PREFIX = '/api';

module.exports = {
    APP_DOMAIN_PROD: _APP_DOMAIN_PROD,
    APP_DOMAIN_UAT: _APP_DOMAIN_UAT,
    APP_DOMAIN_LOCALHOST: _APP_DOMAIN_LOCALHOST,
    APP_DOMAIN_LOCALHOST_PORT: _APP_DOMAIN_LOCALHOST_PORT,
    APP_REQUEST_PATH_PREFIX: _APP_REQUEST_PATH_PREFIX,
}
